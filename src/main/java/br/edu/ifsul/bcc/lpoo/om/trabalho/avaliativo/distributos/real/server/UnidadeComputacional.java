/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsul.bcc.lpoo.om.trabalho.avaliativo.distributos.real.server;

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
    private String estado; // "Ligado", "Desligado", "Sobrecarregado"

    public UnidadeComputacional(double cpuTotal, double memoriaTotal, double custoOperacional) {
        this.cpuTotal = cpuTotal;
        this.cpuDisponivel = cpuTotal;
        this.memoriaTotal = memoriaTotal;
        this.memoriaDisponivel = memoriaTotal;
        this.custoOperacional = custoOperacional;
        this.estado = "Ligado";
    }

    public boolean podeAlocar(double cpuNecessaria, double memoriaNecessaria) {
        return estado.equals("Ligado") &&
               cpuDisponivel >= cpuNecessaria &&
               memoriaDisponivel >= memoriaNecessaria;
    }

    public void alocar(double cpuNecessaria, double memoriaNecessaria) {
        cpuDisponivel -= cpuNecessaria;
        memoriaDisponivel -= memoriaNecessaria;
    }

    public void liberar(double cpuLiberada, double memoriaLiberada) {
        cpuDisponivel += cpuLiberada;
        memoriaDisponivel += memoriaLiberada;

        // Garantir que não exceda os valores máximos
        if (cpuDisponivel > cpuTotal) {
            cpuDisponivel = cpuTotal;
        }
        if (memoriaDisponivel > memoriaTotal) {
            memoriaDisponivel = memoriaTotal;
        }
    }

    public double getCpuDisponivel() {
        return cpuDisponivel;
    }

    public double getMemoriaDisponivel() {
        return memoriaDisponivel;
    }
}


