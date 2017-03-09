/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;


import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
//import app.jframes.JFrameBlood;


/**
 *
 * @author Eudy
 */
public class Main extends javax.swing.JFrame {
     private conexion.Mysql mysql;
     private String[] session;
     private ValidData validador;
    
    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        this.configurations();
    }
    
    public Main(conexion.Mysql mysql){
        this.mysql = mysql;
        initComponents();
        this.configurations();
    }
    public Main(conexion.Mysql mysql,String[] session){
        this.mysql = mysql;
        this.session = session;
        this.validador = new ValidData();
        System.out.println(session[2]);
        this.validador.validarPermisos(session[2]);
        initComponents();
        this.configurations();
    }
    public void configurations(){
        //maximizar en hambos sentidos
        this.setExtendedState(MAXIMIZED_BOTH);
        
        //pantalla completa
        //GraphicsDevice grafica=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        //grafica.setFullScreenWindow(this);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();

        jMenuItem16.setText("jMenuItem16");

        jMenuItem17.setText("jMenuItem17");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu Principal / Tu Sonografia");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setText("USUARIO");

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton2.setText("REPARAR");

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton3.setText("COTIZACION");

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton4.setText("FACTURA");

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton5.setText("CLIENTE");

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton6.setText("INVENTARIO");

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton7.setText("REPORTE");

        jMenu1.setText("Inicio");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem2.setText("About It");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem2MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem13.setText("Cambiar Clave");
        jMenuItem13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem13MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem13);

        jMenuItem12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem12.setText("Salir");
        jMenuItem12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem12MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem12);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Reparaciones");
        jMenu2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem1.setText("Crear Nueva");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem1MousePressed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem3.setText("Buscar");
        jMenuItem3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem3MousePressed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu7.setText("Facturaciones");
        jMenu7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem18.setText("Crear Nueva");
        jMenuItem18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem18MousePressed(evt);
            }
        });
        jMenu7.add(jMenuItem18);

        jMenuItem19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem19.setText("Buscar");
        jMenuItem19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem19MousePressed(evt);
            }
        });
        jMenu7.add(jMenuItem19);

        jMenuBar1.add(jMenu7);

        jMenu4.setText("Cotizaciones");
        jMenu4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem6.setText("Crear Nueva");
        jMenuItem6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem6MousePressed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuItem7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem7.setText("Buscar");
        jMenu4.add(jMenuItem7);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Cliente");
        jMenu5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem4.setText("Crear Nuevo");
        jMenuItem4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem4MousePressed(evt);
            }
        });
        jMenu5.add(jMenuItem4);

        jMenuItem5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem5.setText("Buscar");
        jMenuItem5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem5MousePressed(evt);
            }
        });
        jMenu5.add(jMenuItem5);

        jMenuBar1.add(jMenu5);

        jMenu8.setText("Finanzas");
        jMenu8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem20.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem20.setText("Pago Nomina");
        jMenuItem20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem20MousePressed(evt);
            }
        });
        jMenu8.add(jMenuItem20);

        jMenuItem21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem21.setText("Cuadre Caja");
        jMenuItem21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem21MousePressed(evt);
            }
        });
        jMenu8.add(jMenuItem21);

        jMenuItem22.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem22.setText("Pago Cliente");
        jMenuItem22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem22MousePressed(evt);
            }
        });
        jMenu8.add(jMenuItem22);

        jMenuBar1.add(jMenu8);

        jMenu3.setText("Reportes");
        jMenu3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem8.setText("Generar");
        jMenuItem8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem8MousePressed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuBar1.add(jMenu3);

        jMenu6.setText("Administración");
        jMenu6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jMenuItem11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuItem11.setText("Usuarios");
        jMenuItem11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem11MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem11MousePressed(evt);
            }
        });
        jMenu6.add(jMenuItem11);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(230, 230, 230))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(75, 75, 75)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(76, 76, 76)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(112, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem11MouseClicked
        // TODO add your handling code here:
      
    }//GEN-LAST:event_jMenuItem11MouseClicked

    private void jMenuItem11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem11MousePressed
        // TODO add your handling code here:
        if(this.validador.getBtCrearUsuario()){
              //JFrameUser usuario =  new JFrameUser(this.mysql);
               //     usuario.setDatosUsuario(this.session[0], this.session[1], this.session[3]);
              // usuario.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem11MousePressed

    private void jMenuItem4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem4MousePressed
        // TODO add your handling code here:
         if(this.validador.getBtCrearPaciente()){
          //JFrameCrearPaciente crearPaciente=  new JFrameCrearPaciente(this.mysql);
          //crearPaciente.setDatosUsuario(this.session[0], this.session[1], this.session[3]);
          //crearPaciente.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem4MousePressed

    private void jMenuItem1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MousePressed
        // TODO add your handling code here:
        if(this.validador.getBtCrearSonografia()){
          //JFrameCrearSonografia sonografia =  new JFrameCrearSonografia(this.mysql);
          //sonografia.setYO(sonografia);
          //sonografia.setDatosUsuario(this.session[0], this.session[1], this.session[3]);
          //sonografia.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem1MousePressed

    private void jMenuItem12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem12MousePressed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem12MousePressed

    private void jMenuItem13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem13MousePressed
        // TODO add your handling code here:
        new JFrameCambiarClave(mysql).setVisible(true);
    }//GEN-LAST:event_jMenuItem13MousePressed

    private void jMenuItem2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MousePressed
        // TODO add your handling code here:
        new AboutCreator().setVisible(true);
    }//GEN-LAST:event_jMenuItem2MousePressed

    private void jMenuItem5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem5MousePressed
        // TODO add your handling code here:
        if(this.validador.getBtBuscarPaciente()){
            
        }
    }//GEN-LAST:event_jMenuItem5MousePressed

    private void jMenuItem3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MousePressed
        // TODO add your handling code here:
        //JFrameBuscarSonografia
        if(this.validador.getBtBuscarSonografia()){
        }
    }//GEN-LAST:event_jMenuItem3MousePressed

    private void jMenuItem8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem8MousePressed
        // TODO add your handling code here:
        if(this.validador.getBtGenerarReporte()){
        }
        
    }//GEN-LAST:event_jMenuItem8MousePressed

    private void jMenuItem6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem6MousePressed
        // TODO add your handling code here:
        if(this.validador.getBtCrearEntrega()){
        }
    }//GEN-LAST:event_jMenuItem6MousePressed

    private void jMenuItem18MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem18MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem18MousePressed

    private void jMenuItem19MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem19MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem19MousePressed

    private void jMenuItem20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem20MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem20MousePressed

    private void jMenuItem21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem21MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem21MousePressed

    private void jMenuItem22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem22MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem22MousePressed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    // End of variables declaration//GEN-END:variables
}