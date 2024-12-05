/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package server;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author elder
 */
public interface FileServerFunctions {
    public Arquivo getArquivo();
    public StatusCode upload(String nome, String conteudo, String dono);
    public Map<StatusCode, Arquivo> download(String nome);
    public StatusCode delete(String nome);
    public Map<StatusCode, Arquivo> update(Arquivo arquivo);
    public String auth(String user, String pass);
    public String getAuth(ConnectionHandler connHandler);
    public StatusCode logout();
    public ArrayList<Arquivo> listarArquivos(String nome);

}
