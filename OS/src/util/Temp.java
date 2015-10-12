package util;

import java.util.ArrayList;
import model.Processo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jeisonmarques
 */
public class Temp {
    
    public static ArrayList<Processo> list = new ArrayList<>();
    
    public static void AtualizaProcessado(int pid, boolean novo)
    {
        Processo returnProc = ReturnaProcessoPorPid(pid); 
        int index = list.indexOf(returnProc);
        returnProc.setProcessado(novo);   
        list.set(index, returnProc);
    }
    
    public static void AtualizaEstado(int pid, String novoEstado)
    {
        Processo returnProc = ReturnaProcessoPorPid(pid); 
        int index = list.indexOf(returnProc);
        returnProc.setEstado(novoEstado);  
        Log.AdicionarMenssagem("O processo: "+returnProc.getPid()+ " Trocou de status para: " +novoEstado);
        if(novoEstado.equals("Finalizar"))
        {
            returnProc.setFinaliza(true);
        }
        if(novoEstado.equals("Suspender"))
        {
            returnProc.setSuspenso(true);
        }
        if(novoEstado.equals("Prosseguir"))
        {
            returnProc.setSuspenso(false);
        }
        list.set(index, returnProc);        
    }  
    
    public static void AtualizaPrioridade(int pid, int prioridade)
    {
        Processo returnProc = ReturnaProcessoPorPid(pid);    
        int index = list.indexOf(returnProc);
        returnProc.setPrioridade(prioridade); 
        list.set(index, returnProc);
    }
     
    public static void AtualizaTempo(int pid, int tempo)
    {
        Processo returnProc = ReturnaProcessoPorPid(pid);    
        int index = list.indexOf(returnProc);
        int t = returnProc.getTempoProcessamento() + tempo;
        returnProc.setTempoProcessamento(t);
        list.set(index, returnProc);
    }
    
    public static void FinalizaProcesso(int pid)
    {
        Processo returnProc = ReturnaProcessoPorPid(pid);      
        int index = list.indexOf(returnProc);   
        list.remove(index);
    }
    
    public static Processo ReturnaProcessoPorPid(int pid)
    {
        Processo returnProc = null;
        for (Processo proc : list) {
            if(proc.getPid() == pid)
            {
                returnProc = proc;
            }
        }

        if(returnProc == null)
        {
            throw new ArrayIndexOutOfBoundsException("Pid não encontado");
        }
        else
        {
            return returnProc;
        }        
    }           
}
