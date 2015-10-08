/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Date;


/**
 *
 * @author jeison.marques
 */
public class Log {
    public static ArrayList<LogMensagem> list = new ArrayList<>();
    
    
    public static void AdicionarMenssagem(String messagem)
    {
        Date d = new Date();
        String msg = d.toString() + " - " +messagem + "\n";
        
        LogMensagem msgObj = new LogMensagem(msg);
        
        list.add(msgObj);
    }
    
    public static void TrocaStatusMensagem(LogMensagem msg)
    {
        for (LogMensagem m: list) {
            if(m == msg)
            {
                msg.setStatus(true);
            }
        }
    }
}
