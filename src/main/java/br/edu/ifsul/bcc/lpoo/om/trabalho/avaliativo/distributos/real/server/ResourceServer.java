package server;

import br.edu.ifsul.bcc.lpoo.om.trabalho.avaliativo.distributos.real.server.UnidadeComputacional;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONObject;

public class ResourceServer {

    private static final List<UnidadeComputacional> unidades = Arrays.asList(
    new UnidadeComputacional(8, 16, 0.5), // 8 CPUs, 16 GB RAM, custo 0.5
    new UnidadeComputacional(16, 32, 1.0), // 16 CPUs, 32 GB RAM, custo 1.0
    new UnidadeComputacional(32, 64, 1.5)  // 32 CPUs, 64 GB RAM, custo 1.5
);
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

    double cpuNecessaria = requisicao.getDouble("cpu");
    double memoriaNecessaria = requisicao.getDouble("memoria");
    String user = requisicao.getString("user");

    UnidadeComputacional melhorUnidade = null;
    double menorEspacoLivre = Double.MAX_VALUE;
    for (UnidadeComputacional unidade : unidades) {
        if (unidade.podeAlocar(cpuNecessaria, memoriaNecessaria)) {
            double espacoLivre = unidade.getCpuDisponivel() - cpuNecessaria +
                                 unidade.getMemoriaDisponivel() - memoriaNecessaria;

            if (espacoLivre < menorEspacoLivre) {
                melhorUnidade = unidade;
                menorEspacoLivre = espacoLivre;
            }
        }
    }

    if (melhorUnidade != null) {
        melhorUnidade.alocar(cpuNecessaria, memoriaNecessaria);

        JSONObject resposta = new JSONObject();
        resposta.put("status", "sucesso");
        resposta.put("cpu", cpuNecessaria);
        resposta.put("memoria", memoriaNecessaria);
        resposta.put("user", user);

        enviaResposta(saida, 200, resposta.toString());
    } else {
        enviaResposta(saida, 400, "Recursos insuficientes");
    }
}


   private synchronized void liberarRecursos(BufferedReader entrada, PrintWriter saida) throws IOException {
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

    double cpuLiberada = requisicao.getDouble("cpu");
    double memoriaLiberada = requisicao.getDouble("memoria");

    for (UnidadeComputacional unidade : unidades) {
        if (unidade.getCpuDisponivel() + cpuLiberada <= unidade.getCpuTotal() &&
            unidade.getMemoriaDisponivel() + memoriaLiberada <= unidade.getMemoriaTotal()) {
            unidade.liberar(cpuLiberada, memoriaLiberada);

            JSONObject resposta = new JSONObject();
            resposta.put("status", "sucesso");
            resposta.put("cpu", cpuLiberada);
            resposta.put("memoria", memoriaLiberada);

            enviaResposta(saida, 200, resposta.toString());
            return;
        }
    }

    enviaResposta(saida, 400, "Falha ao liberar recursos");
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
