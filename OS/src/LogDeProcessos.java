
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultCaret;
import util.Log;
import util.LogMensagem;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jeison.marques
 */
public class LogDeProcessos extends javax.swing.JInternalFrame {

    /**
     * Creates new form LogDeProcessos
     */
    public LogDeProcessos() {
        initComponents();        
        jTextAreaLog.setEditable(false);
        DefaultCaret caret = (DefaultCaret)jTextAreaLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        Timer timer = new Timer();
    
        timer.scheduleAtFixedRate(new TimerTask() {
        public void run() {               
            
            for (LogMensagem msg : Log.list) {
                if(!msg.getStatus())
                {                    
                    jTextAreaLog.append(msg.getMensagem());
                    Log.TrocaStatusMensagem(msg);
                }                
            }
               }
           }, 0, 1000);        
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
        jTextAreaLog = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        jTextAreaLog.setColumns(20);
        jTextAreaLog.setRows(5);
        jScrollPane1.setViewportView(jTextAreaLog);

        jButton1.setText("Debug");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Log.AdicionarMenssagem("add1");
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaLog;
    // End of variables declaration//GEN-END:variables
}
