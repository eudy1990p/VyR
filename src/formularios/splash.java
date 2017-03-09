/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import com.sun.awt.AWTUtilities;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.edisoncor.gui.util.WindowsUtil;


/**
 *
 * @author Eudy
 */
public class splash extends javax.swing.JFrame implements Runnable {
    
    Thread hilo;
    private int cont=9;
    private conexion.Mysql mysql;
    /**
     * Creates new form splash
     */
    public splash() {
        initComponents();
        AWTUtilities.setWindowOpaque(this, false);
        this.setLocationRelativeTo(null);
    }
    public splash(conexion.Mysql mysql){
        initComponents();
        AWTUtilities.setWindowOpaque(this, false);
        this.setLocationRelativeTo(null);
        this.mysql = mysql;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icono/iconoclinica.gif"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 550, -1, 40));

        jProgressBar1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jProgressBar1StateChanged(evt);
            }
        });
        getContentPane().add(jProgressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jProgressBar1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jProgressBar1StateChanged
        // TODO add your handling code here:
                double resta =100 - this.jProgressBar1.getValue();
                double division = resta / 100;
                float  v = (float) division ;
                //double v = double.valueOf() )+"");
                //System.out.println(division+" "+resta+" "+v);
                WindowsUtil.makeWindowsOpacity(this,v);
                
    }//GEN-LAST:event_jProgressBar1StateChanged

    /**
     * @param args the command line arguments
     */
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        
        try {
            this.setVisible(true);
            for(int i = 0 ; i < 100 ; i++){
                this.jProgressBar1.setValue(i);
                this.jLabel2.setText(i+"%");
                hilo.sleep(60);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(splash.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
        new login_f(this.mysql).setVisible(true);
    }
}