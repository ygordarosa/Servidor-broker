/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteCalculadora {

    public static void main(String[] args) {
        String enderecoServidor = "localhost";
        int porta = 3000;

        try {
            //estabelecer a conexão com ServerSocket, retornando o socket da comunicação
            Socket socket = new Socket(enderecoServidor, porta);
            //criação dos streams de entrada e saída
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String comando = "";
            Scanner console = new Scanner(System.in);
            while(!comando.equals("sair")){
                System.out.print("$ ");
                comando = console.nextLine();
                saida.println(comando);
                saida.flush();
                System.out.println(entrada.readLine());
            }

            //comunicação com base no protocolo de comunicação
            //Realiza login para poder calcular
            String login = "LOGIN;aluno;computacao";
            saida.println(login);
            String loginRes = entrada.readLine();
            System.out.println("Resposta do Login:" + loginRes);
            
            // Exemplo de requisição: SOMA;5;3
            String requisicao = "SUM;5;3";
            saida.println(requisicao);

            String resposta = entrada.readLine();
            System.out.println("Resposta do servidor: " + resposta);

            requisicao = "SUB;5;3";
            saida.println(requisicao);
            resposta = entrada.readLine();
            System.out.println("Resposta do servidor: " + resposta);

            requisicao = "SUB;5;3";
            saida.println(requisicao);
            resposta = entrada.readLine();
            System.out.println("Resposta do servidor: " + resposta);

            requisicao = "MUL;5;3";
            saida.println(requisicao);
            resposta = entrada.readLine();
            System.out.println("Resposta do servidor: " + resposta);

            requisicao = "DIV;5;3";
            saida.println(requisicao);
            resposta = entrada.readLine();
            System.out.println("Resposta do servidor: " + resposta);

            requisicao = "DIV;5;0";
            saida.println(requisicao);
            resposta = entrada.readLine();
            System.out.println("Resposta do servidor: " + resposta);
            
           /* //fazLogout
            requisicao = "LOGOUT";
            saida.println(requisicao);
            resposta = entrada.readLine();
            System.out.println("Resposta Logout: " + resposta);
            
            //SAIR
            requisicao = "SAIR";
            saida.println(requisicao);
            resposta = entrada.readLine();
            System.out.println("Resposta SAIR: " + resposta);
            
            socket.close();*/

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
