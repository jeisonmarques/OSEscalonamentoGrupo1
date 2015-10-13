package ui;


import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JRadioButton;
import javax.swing.table.DefaultTableModel;
import model.Processo;
import util.Temp;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jeison.marques
 */
public class ProcessamentoUI extends javax.swing.JInternalFrame {

    /**
     * Creates new form ProcessamentoUI
     */
    private int tempoCpu = 300;
    private final Timer timerProc;
    private final Timer timerIO;
    private Date init;
    private Date end;
    public ArrayList<Processo> listRun = new ArrayList();
    public ArrayList<Processo> listIO = new ArrayList();
    
    public ProcessamentoUI() {
        initComponents();
        timerProc = new Timer();
        timerIO = new Timer();
        
     timerProc.scheduleAtFixedRate(new TimerTask() {
     public void run() {
            try {             
                VerificaProcessoNovo();
                AtualizaListas();
                ProcessoPrioridade();
                ProcessoFifo();

            } catch (InterruptedException ex) {
                Logger.getLogger(ProcessamentoUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Date d = new Date();
            System.out.println("Processando as " + d.toString());
            System.out.println("Lista de Processamento: "+listRun.size());
            System.out.println("Lista de Processos: "+Temp.list.size());
            }
        }, 0, 1000);
    
    timerIO.scheduleAtFixedRate(new TimerTask() {
     public void run() {
         try {
             ProcessaIO();
             Popula();
         } catch (InterruptedException ex) {
             Logger.getLogger(ProcessamentoUI.class.getName()).log(Level.SEVERE, null, ex);
         }
            }
        }, 0, 100);
    }
    public void ProcessaIO() throws InterruptedException
    {
        if(listIO.size() > 0)
        {
            Processo proc = listIO.get(0);
            jLabelProcIO.setText("ID: "+proc.getPid());
            //System.out.println("tamanho io: "+ listIO.size());
            if("I/O-Bound(Disco)".equals(proc.getTipo()))
            {
                jProgressBarIO.setMaximum(100);
                if(proc.IOCount >= 100){
                    listIO.remove(0);
                    TrocaEstado(proc, "Ponto");
                    AtualizaProcessamento(proc.getPid(), true);                 
                    proc.IOCount = 0;
                }
                else
                {
                    //System.out.println(proc.getPid() +" - "+ proc.IOCount);
                    jProgressBarIO.setValue(proc.IOCount);
                    proc.IOCount++; 
                }
            }
            if("I/O-Bound(Fita)".equals(proc.getTipo()))
            {
                jProgressBarIO.setMaximum(150);
                if(proc.IOCount >= 150){
                    listIO.remove(0);
                    TrocaEstado(proc, "Ponto");
                    AtualizaProcessamento(proc.getPid(), true);
                    proc.IOCount = 0;
                }
                else
                {
                    //System.out.println(proc.getPid() +" - "+ proc.IOCount);
                    jProgressBarIO.setValue(proc.IOCount);
                    proc.IOCount++; 
                }
            }

        }   
    }
    
    public void ProcessoPrioridade()
    {        
        if(listRun.size() > 0)
        {
            Collections.sort(listRun, new Comparator<Processo>() {
            @Override
            public int compare(Processo o1, Processo o2) {
            return o2.getPrioridade() - o1.getPrioridade();

            }
            });

            for (Processo proc : listRun) {
                System.out.println(proc.toString());
            }  
        }    

    }            
    
    
    public void ProcessoFifo() throws InterruptedException
    {
        if(listRun.size() > 0)
        {
            Processo proc = listRun.get(0);
            listRun.remove(0);
            
            if(!proc.getSuspenso() && proc.getProcessado()){

                TrocaEstado(proc, "Execucao");      
                
                if("CPU-Bound".equals(proc.getTipo()))
                {
                    Thread.sleep(tempoCpu);
                    proc.setProcessado(true);
                }
                if(proc.getTipo().contains("I/O-Bound"))
                {
                    Thread.sleep(3);
                    TrocaEstado(proc, "I/O");
                    proc.setProcessado(false);
                    listIO.add(proc);
                }
                else
                {
                    TrocaEstado(proc, "Ponto");
                }

                //System.out.println(proc.toString());
                if(!proc.getFinaliza())
                {
                    listRun.add(proc);
                }
                else
                {
                    Temp.FinalizaProcesso(proc.getPid());
                }
            }
            else
            {
                listRun.add(proc);
            }
        } 
    }
    
    public void TrocaEstado(Processo proc, String estado)
    {
        Temp.AtualizaEstado(proc.getPid(), estado);
        if("Execucao".equals(estado))
        {
            init = new Date();
        }
        if("Ponto".equals(estado) && "CPU-Bound".equals(proc.getTipo()))
        {
            end = new Date();
            long tempo = end.getTime() - init.getTime();
            Temp.AtualizaTempo(proc.getPid(), ((int)tempo - tempoCpu));
        }
        if("I/O".equals(estado))
        {
            end = new Date();
            long tempo = end.getTime() - init.getTime();
            Temp.AtualizaTempo(proc.getPid(), ((int)tempo));
        }
    }
    
    public void AtualizaListas()
    {
        if(Temp.list.size() > 0)
        {
            for (Processo p : Temp.list) {
                AtualizaFinalizar(p.getPid(), p.getFinaliza());
                AtualizaSuspenso(p.getPid(), p.getSuspenso());
            }
        }
    }

    public void VerificaProcessoNovo()
    {
        if(Temp.list.size() != listRun.size() && Temp.list.size() > 0)
        {
            if(Temp.list.size() > listRun.size())
            {
                for (Processo proc : Temp.list) {
                    if("N/A".equals(proc.getEstado()))
                    {
                        listRun.add(proc);
                        Temp.AtualizaEstado(proc.getPid(), "Ponto");
                    }
                }
            }
        }
    }
    
    public void AtualizaFinalizar(int pid, boolean finalizar)
    {
        Processo returnProc = ReturnaProcessoPorPid(pid);       
        int index = listRun.indexOf(returnProc);
        returnProc.setFinaliza(finalizar);       
        listRun.set(index, returnProc);
    }
    
    public void AtualizaSuspenso(int pid, boolean suspenso)
    {
        Processo returnProc = ReturnaProcessoPorPid(pid);       
        int index = listRun.indexOf(returnProc);
        returnProc.setSuspenso(suspenso);
        listRun.set(index, returnProc);
    }
    
    public Processo ReturnaProcessoPorPid(int pid)
    {
        Processo returnProc = null;
        for (Processo proc : listRun) {
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
    
    public void AtualizaPrioridade(int pid, int prioridade)
    {
        Processo returnProc = ReturnaProcessoPorPid(pid);       
        int index = listRun.indexOf(returnProc);
        returnProc.setPrioridade(prioridade);  
        listRun.set(index, returnProc);
    }
    
    public void AtualizaProcessamento(int pid, boolean processado)
    {
        Processo returnProc = ReturnaProcessoPorPid(pid);       
        int index = listRun.indexOf(returnProc);
        returnProc.setProcessado(processado);
        listRun.set(index, returnProc);
    }
    
    public void deletaTodasLinhas(final DefaultTableModel model) {
        for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
            model.removeRow(i);
        }
    }
    
    public void Popula()
    {
        try{
        jLabelCountProc.setText(""+Temp.list.size());
        DefaultTableModel modeloIO = (DefaultTableModel) jTableIO.getModel();
        deletaTodasLinhas(modeloIO);
        synchronized(listIO){
            for (Processo val : listIO) {
                modeloIO.addRow(new String [] {""+val.getPid(),""+val.IOCount});
            }
        }
        DefaultTableModel modeloProc = (DefaultTableModel) jTableProc.getModel();
        deletaTodasLinhas(modeloProc);
        synchronized(listRun){
            if(listRun.size() > 0){
                for (Processo val : listRun) {
                    modeloProc.addRow(new String [] {""+val.getPid(),val.getTipo(), ""+val.getTempoProcessamento()});
                }
            }
        }
        
        }
        catch(Exception e)
        {
            ///sem tratamento
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableIO = new javax.swing.JTable();
        jProgressBarIO = new javax.swing.JProgressBar();
        jLabelProcIO = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableProc = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelCountProc = new javax.swing.JLabel();
        jRadioButtonFifo = new javax.swing.JRadioButton();
        jRadioButtonPrioridade = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();

        jTableIO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PID", "Contagem"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableIO);

        jLabelProcIO.setText("Carregando...");

        jTableProc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PID", "Tipo", "Tempo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableProc);

        jLabel1.setText("Ordem de I/O");

        jLabel2.setText("Ordem de Execução de Processos");

        jLabel3.setText("Numero de Processos:");

        jLabelCountProc.setText("...");

        jRadioButtonFifo.setText("FIFO");
        jRadioButtonFifo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonFifoActionPerformed(evt);
            }
        });

        jRadioButtonPrioridade.setText("Prioridade");
        jRadioButtonPrioridade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPrioridadeActionPerformed(evt);
            }
        });

        jLabel4.setText("Algoritimo:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelCountProc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonPrioridade)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButtonFifo)
                                .addGap(30, 30, 30)))
                        .addGap(117, 117, 117)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelProcIO)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jProgressBarIO, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelProcIO)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jLabelCountProc)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jProgressBarIO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRadioButtonFifo)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonPrioridade)))
                .addContainerGap(70, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonFifoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonFifoActionPerformed
        if(jRadioButtonPrioridade.isSelected())
        {
            jRadioButtonPrioridade.setSelected(false);
        }
    }//GEN-LAST:event_jRadioButtonFifoActionPerformed

    private void jRadioButtonPrioridadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonPrioridadeActionPerformed
        if(jRadioButtonFifo.isSelected())
        {
            jRadioButtonFifo.setSelected(false);
        }
    }//GEN-LAST:event_jRadioButtonPrioridadeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelCountProc;
    private javax.swing.JLabel jLabelProcIO;
    private javax.swing.JProgressBar jProgressBarIO;
    private javax.swing.JRadioButton jRadioButtonFifo;
    private javax.swing.JRadioButton jRadioButtonPrioridade;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableIO;
    private javax.swing.JTable jTableProc;
    // End of variables declaration//GEN-END:variables
}
