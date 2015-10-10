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
        int index = 0;
       
        Processo returnProc = ReturnaProcessoPorPid(pid); 

        index = list.indexOf(returnProc);
        returnProc.setProcessado(novo);
        
        list.set(index, returnProc);
    }
    
    public static void AtualizaEstado(int pid, String novoEstado)
    {
        int index = 0;
       
        Processo returnProc = ReturnaProcessoPorPid(pid); 

        index = list.indexOf(returnProc);

        returnProc.setEstado(novoEstado);     
        list.set(index, returnProc);
        

    }  
    
    public static void AtualizaPrioridade(int pid, int prioridade)
    {
        int index = 0;
        Processo returnProc = ReturnaProcessoPorPid(pid);  
        
        index = list.indexOf(returnProc);
        returnProc.setPrioridade(prioridade);
        
        list.set(index, returnProc);
    }
    
    public static void FinalizaProcesso(int pid)
    {
        int index = 0;
        Processo returnProc = ReturnaProcessoPorPid(pid);  
        
        index = list.indexOf(returnProc);
        
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
