/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsul.bcc.lpoo.om.trabalho.avaliativo.distributos.real.server;

/**
 *
 * @author 20221PF.CC0018
 */
public class InstanciaProcessamento {
    private String clienteId;
    private double cpuAlocada;
    private double memoriaAlocada;
    private UnidadeComputacional unidade;
    private long inicioUso;

    public InstanciaProcessamento(String clienteId, double cpuAlocada, double memoriaAlocada, UnidadeComputacional unidade) {
        this.clienteId = clienteId;
        this.cpuAlocada = cpuAlocada;
        this.memoriaAlocada = memoriaAlocada;
        this.unidade = unidade;
        this.inicioUso = System.currentTimeMillis();
    }

    public double getCpuAlocada() {
        return cpuAlocada;
    }

    public double getMemoriaAlocada() {
        return memoriaAlocada;
    }

    public UnidadeComputacional getUnidade() {
        return unidade;
    }

    public String getClienteId() {
        return clienteId;
    }

    public long getInicioUso() {
        return inicioUso;
    }
}
