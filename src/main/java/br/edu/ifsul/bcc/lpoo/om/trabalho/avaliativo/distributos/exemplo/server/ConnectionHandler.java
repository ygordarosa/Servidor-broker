/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Estado;

/**
 *
 * @author elder
 */
public class ConnectionHandler implements Runnable {
    private Socket socket;
    private Integer clientId;
    private BufferedReader entrada;
    private PrintWriter saida;
    private FileServerFunctions serverFunctions;

    public ConnectionHandler(Socket socket, Integer clientId, FileServerFunctions serverFunctions){
        this.socket = socket;
        this.clientId = clientId;
        this.serverFunctions = serverFunctions;
    }
    
    
    
    
    public void trataProtocolo() throws IOException {
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            saida = new PrintWriter(socket.getOutputStream(), true);

            Estado estado = Estado.CONECTADO;

            //iniciando o tratamento do protocolo
            while (estado != Estado.FINALIZADO) {

                //4.tratamento do protocolo de aplicação. Realizar escritas e leituras para/do cliente
                // Receber a mensagem do cliente
                String mensagem = entrada.readLine();

                /*
                        Passos para tratar e responder a requisição
                        1. Decodificar a requisição (campos da mensagem)
                        2. Validação dos campos e da mensagem
                        3. Execução das operações
                        4. Construção da mensagem de resposta
                        5. Envio da resposta
                 */
                String[] protocolo;
                String operacao = "";
                String resposta = "";

                protocolo = mensagem.split(";");
                operacao = protocolo[0];
                String msg = null;
                if (protocolo.length > 3) {
                    msg = protocolo[3];
                }
                if (msg != null) {
                    System.out.println("Recebi recado: " + msg);
                }
                switch (estado) { //Design pattern: State
                    case CONECTADO:
                        switch (operacao) {
                            case "LOGIN":
                                String user = protocolo[1];
                                String pass = protocolo[2];
                                if (user.equals("aluno") && pass.equals("computacao")) {
                                    resposta = "LOGINRESPONSE;OK";
                                    estado = Estado.AUTENTICADO;
                                }
                                break;
                            case "SAIR":
                                resposta = "SAIRRESPONSE;OK";
                                estado = Estado.FINALIZADO;
                                break;

                            default:
                                resposta = operacao + "RESPONSE;ERRO;MENSAGEM INVALIDA OU NÃO PERMITIDA";
                        }
                        break;
                    case AUTENTICADO:
                        

                        switch (operacao) {
                            case "DOW":
                                //operacao de download
                                
                                break;
                            case "UPL":
                                //operação de upload
                                break;
                            case "LST":
                                //operacao de listagem
                                break;
                        

                            default:
                                resposta = operacao+"RESPONSE;ERRO;Mensagem inválida ou não autorizada";

                        }        
                        break;
                }

                saida.println(resposta);
            }
        } catch (Exception e) {
            System.out.println("Erro nos streams " + e);
        } finally {
            socket.close();
        }

    }

    @Override
    public void run() {
        try {
            trataProtocolo();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
