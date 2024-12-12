/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsul.bcc.lpoo.om.trabalho.avaliativo.distributos.real.server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ygor
 */
public class UnidadeComputacional {
    private double cpuTotal;
    private double cpuDisponivel;
    private double memoriaTotal;
    private double memoriaDisponivel;
    private double custoOperacional;
    private String estado;
    private List<InstanciaProcessamento> instancias; // Gerencia as instâncias

    public UnidadeComputacional(double cpuTotal, double memoriaTotal, double custoOperacional) {
        this.cpuTotal = cpuTotal;
        this.cpuDisponivel = cpuTotal;
        this.memoriaTotal = memoriaTotal;
        this.memoriaDisponivel = memoriaTotal;
        this.custoOperacional = custoOperacional;
        this.estado = "Ligado";
        this.instancias = new ArrayList<>();
    }

        public boolean podeAlocar(double cpuNecessaria, double memoriaNecessaria) {
        return estado.equals("Ligado") &&
               cpuDisponivel >= cpuNecessaria &&
               memoriaDisponivel >= memoriaNecessaria;
    }

    // Aloca os recursos na unidade
    public void alocar(double cpuNecessaria, double memoriaNecessaria) {
        if (podeAlocar(cpuNecessaria, memoriaNecessaria)) {
            cpuDisponivel -= cpuNecessaria;
            memoriaDisponivel -= memoriaNecessaria;
        } else {
            throw new IllegalStateException("Recursos insuficientes para alocar.");
        }
    }

    // Libera os recursos alocados na unidade
    public void liberar(double cpuLiberada, double memoriaLiberada) {
        cpuDisponivel += cpuLiberada;
        memoriaDisponivel += memoriaLiberada;

        // Garantir que os recursos não ultrapassem o total disponível
        if (cpuDisponivel > cpuTotal) {
            cpuDisponivel = cpuTotal;
        }
        if (memoriaDisponivel > memoriaTotal) {
            memoriaDisponivel = memoriaTotal;
        }
    }
    
public InstanciaProcessamento criarInstancia(String clienteId, double cpuNecessaria, double memoriaNecessaria) {
    if (podeAlocar(cpuNecessaria, memoriaNecessaria)) {
        alocar(cpuNecessaria, memoriaNecessaria);
        InstanciaProcessamento instancia = new InstanciaProcessamento(clienteId, cpuNecessaria, memoriaNecessaria, this);
        instancias.add(instancia);
        return instancia;
    }
    throw new IllegalArgumentException("Não foi possível alocar os recursos solicitados.");
}


    public boolean liberarInstancia(InstanciaProcessamento instancia) {
        if (instancias.remove(instancia)) {
            liberar(instancia.getCpuAlocada(), instancia.getMemoriaAlocada());
            return true;
        }
        return false;
    }

    public List<InstanciaProcessamento> getInstancias() {
        return instancias;
    }
    
    public double getCpuDisponivel() {
        return cpuDisponivel;
    }

    public double getMemoriaDisponivel() {
        return memoriaDisponivel;
    }

    public double getCpuTotal() {
        return cpuTotal;
    }

    public double getMemoriaTotal() {
        return memoriaTotal;
    }
}
