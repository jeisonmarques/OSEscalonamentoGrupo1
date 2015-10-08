/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author jeison.marques
 */
public class LogMensagem {
    
    private String mensagem;
    private boolean status;

    public LogMensagem(String mensagem) {
        this.mensagem = mensagem;
        this.status = false;
    }    
    
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }          
}
