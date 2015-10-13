/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import java.util.Random;

/**
 *
 * @author jeisonmarques
 */
public class Processo {
    
    private String tipo;
    private int pid;
    private int prioridade;
    private Date horaEntrada;
    private Date horaSaida;
    private int tempoProcessamento;
    private String estado;
    private boolean processado;
    private boolean finaliza;
    private boolean suspenso;
    public int IOCount;

    public Processo() {
        
        Random gerador = new Random(); 
        this.processado = true;
        this.tempoProcessamento = 0;
        this.estado = "N/A";
        this.pid = gerador.nextInt(999999);
        this.finaliza = false;
        this.suspenso = false;
    }
    
    public boolean getFinaliza() {
        return finaliza;
    }

    public void setFinaliza(boolean finaliza) {
        this.finaliza = finaliza;
    }

    public boolean getSuspenso() {
        return suspenso;
    }

    public void setSuspenso(boolean suspenso) {
        this.suspenso = suspenso;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Date getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(Date horaSaida) {
        this.horaSaida = horaSaida;
    }

    public int getTempoProcessamento() {
        return tempoProcessamento;
    }

    public void setTempoProcessamento(int tempoProcessamento) {
        this.tempoProcessamento = tempoProcessamento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean getProcessado() {
        return processado;
    }

    public void setProcessado(boolean processado) {
        this.processado = processado;
    }

    
    @Override
    public String toString() {
        return "Processo{" + "tipo=" + tipo + ", pid=" + pid 
                + ", prioridade=" + prioridade + ", hrEntrada=" + horaEntrada 
                + ",\n hrSaida=" + horaSaida + ", tProc=" + tempoProcessamento 
                + ", estado=" + estado + ", proc=" + processado 
                + ", finalizar="+finaliza+ ", suspenso+"+suspenso+'}';
    }
    
    public int compareTo(Processo compareProc) {
        int comparePrioridade=((Processo)compareProc).getPrioridade();
        /* For Ascending order*/
        return this.prioridade - comparePrioridade;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
}
