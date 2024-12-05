/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author elder
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*        1 - Criar o servidor de conexÃµes
 	* 2 -Esperar o um pedido de conexÃ£o; // Outro processo 2.1 e criar uma
 	* nova conexÃ£o; 3 - Criar streams de enechar socket de comunicaÃ§Ã£o entre
 	* servidor/cliente 4.2 - Fechar streams de entrada e saÃ­da trada e saÃ­da;
 	* 4 - Tratar a conversaÃ§Ã£o entre cliente e servidor (tratar protocolo);
 	* 4.1 - Fechar socket de comunicaÃ§Ã£o entre servidor/cliente 4.2 - Fechar
 */
public class ConnectionServer implements FileServerFunctions{

    int porta;
    ServerSocket servidorSocket;
    ArrayList<Arquivo> arquivos;
    ArrayList<ConnectionHandler> clientes;
    ArrayList<Thread> threads;
    

    public ConnectionServer(int porta) {
        this.porta = porta;
        clientes = new ArrayList<>();
        threads = new ArrayList<>();
    }
    /**
     * Faz upload de arquivo;
     * presume que os parametros ja estejam validados
     * @param nome
     * @param conteudo
     * @param dono
     * @return 
     */
    public StatusCode upload(String nome, String conteudo, String dono){
        
        //verificar se o arquivo já existe
        //criar arquivo
        //salvar arquivo
        
        return StatusCode.OK;
        
    }
    
    public Map<StatusCode, Arquivo> download(String nome){
        //busca o arquivo
        //verifica
        //envia StatusCode com conteudo;
        HashMap<StatusCode, Arquivo> map= new HashMap<>();
        map.put(StatusCode.OK, new Arquivo("ex", "nome", "conteudo"));
        
        return map;
    }
    
    public ArrayList<Arquivo> listarArquivos(String nome){
        //busca arquivos que contenham o nome
        //retorna array
        ArrayList<Arquivo> found = new ArrayList<>();
        
        return found;
    }
    

    public void criaServerSocket() throws IOException {
        servidorSocket = new ServerSocket(porta);
    }

    public Socket esperaConexao() throws IOException {
        System.out.println("Esperando conexão...");

        return servidorSocket.accept();
    }

    
    public void mainLoop(){
        try { //1.criando o ServerSocket. Servidor de conexão TCP
            criaServerSocket();
            //loop de conexões
            int id = 0;
            while (true) { //n pode ser infinito
                Socket socket = esperaConexao();
                //criação dos fluxos (threads) de tratamento para cada cliente
                ConnectionHandler cliente = new ConnectionHandler(socket, id++ , this);
                clientes.add(cliente);
                Thread t = new Thread(cliente);
                threads.add(t);
                t.start();
            }

        } catch (IOException e) {
            System.out.println("Erro ao processar a conexão do cliente: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        
        ConnectionServer server = new ConnectionServer(3000);
        server.mainLoop();
        
        
    }

    @Override
    public Arquivo getArquivo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public StatusCode delete(String nome) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Map<StatusCode, Arquivo> update(Arquivo arquivo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String auth(String user, String pass) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getAuth(ConnectionHandler connHandler) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public StatusCode logout() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
