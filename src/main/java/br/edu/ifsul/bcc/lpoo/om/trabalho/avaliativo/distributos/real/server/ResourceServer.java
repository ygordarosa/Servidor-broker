package server;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONObject;

public class ResourceServer {

    private static final List<Integer> pool = new ArrayList<>(Arrays.asList(2, 4, 4, 5, 5));
    private static int nucleos = 8; // Número total de núcleos disponíveis
    private int porta;
    private ServerSocket servidorSocket;

    public ResourceServer(int porta) {
        this.porta = porta;
    }

    public void criaServerSocket() throws IOException {
        servidorSocket = new ServerSocket(porta);
    }

    public Socket esperaConexao() throws IOException {
        System.out.println("Esperando conexão...");
        return servidorSocket.accept();
    }

    private void trataProtocolo(Socket socket) throws IOException {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true)) {

            // Ler a primeira linha da requisição HTTP
            String linha = entrada.readLine();
            if (linha == null) return;

            String[] partes = linha.split(" ");
            String metodo = partes[0];
            String recurso = partes[1];

            if (metodo.equals("POST") && recurso.equals("/allocate")) {
                alocarRecursos(entrada, saida);
            } else if (metodo.equals("POST") && recurso.equals("/release")) {
                liberarRecursos(entrada, saida);
            } else {
                enviaResposta(saida, 404, "Rota não encontrada");
            }
        } finally {
            socket.close();
        }
    }

    private synchronized void alocarRecursos(BufferedReader entrada, PrintWriter saida) throws IOException {
        // Processar o corpo da requisição
        int contentLength = 0;
        String linha;
        while (!(linha = entrada.readLine()).isEmpty()) {
            if (linha.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(linha.split(":")[1].trim());
            }
        }

        char[] corpo = new char[contentLength];
        entrada.read(corpo, 0, contentLength);
        JSONObject requisicao = new JSONObject(new String(corpo));

        int ram = requisicao.getInt("ram");
        int requestedCores = requisicao.getInt("nucleos");
        String user = requisicao.getString("user");

        // Tentar alocar recursos
        Optional<Integer> ramAlocada = pool.stream().filter(r -> r >= ram).findFirst();

        if (ramAlocada.isPresent() && nucleos >= requestedCores) {
            pool.remove(ramAlocada.get());
            nucleos -= requestedCores;

            JSONObject resposta = new JSONObject();
            resposta.put("status", "sucesso");
            resposta.put("ram", ramAlocada.get());
            resposta.put("nucleos", requestedCores);
            resposta.put("user", user);

            enviaResposta(saida, 200, resposta.toString());
        } else {
            enviaResposta(saida, 400, "Recursos insuficientes");
        }
    }

    private synchronized void liberarRecursos(BufferedReader entrada, PrintWriter saida) throws IOException {
        // Processar o corpo da requisição
        int contentLength = 0;
        String linha;
        while (!(linha = entrada.readLine()).isEmpty()) {
            if (linha.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(linha.split(":")[1].trim());
            }
        }

        char[] corpo = new char[contentLength];
        entrada.read(corpo, 0, contentLength);
        JSONObject requisicao = new JSONObject(new String(corpo));

        int ram = requisicao.getInt("ram");
        int releasedCores = requisicao.getInt("nucleos");

        pool.add(ram);
        nucleos += releasedCores;

        JSONObject resposta = new JSONObject();
        resposta.put("status", "sucesso");
        resposta.put("ram", ram);
        resposta.put("nucleos", releasedCores);

        enviaResposta(saida, 200, resposta.toString());
    }

    private void enviaResposta(PrintWriter saida, int statusCode, String mensagem) {
        saida.println("HTTP/1.1 " + statusCode + " OK");
        saida.println("Content-Type: application/json");
        saida.println("Content-Length: " + mensagem.length());
        saida.println();
        saida.println(mensagem);
    }

    public static void main(String[] args) {
        try {
            ResourceServer server = new ResourceServer(3000);
            server.criaServerSocket();
            System.out.println("Servidor iniciado na porta 3000");

            while (true) {
                Socket socket = server.esperaConexao();
                new Thread(() -> {
                    try {
                        server.trataProtocolo(socket);
                    } catch (IOException e) {
                        System.err.println("Erro ao processar requisição: " + e.getMessage());
                    }
                }).start();
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }
}
