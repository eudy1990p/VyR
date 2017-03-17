/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import conexion.Mysql;
import javax.swing.JOptionPane;

/**
 *
 * @author Eudy
 */
public class crearCliente extends javax.swing.JFrame {

    /**
     * Creates new form crearCliente
     */
    private boolean mostrarOpciones = true;
    private Mysql mysql;
    private String nombre="",apellido,sexo,rnc,fechaNacimiento,cedula,email,telefono,provincia,sector,direccion;
    private String personaID="1",usuarioID="1",clienteID="1";
    private SeleccionLista classSeleccionListado;
    private String trabajandoClase = "contizacion";
            
    public crearCliente(Mysql mysql) {
        initComponents();
        this.mysql = mysql;
        this.mostrarOpciones();
    }
    public void setTrabajandoClase(String clase){
        this.trabajandoClase = clase ;
    }
    public void setClassSeleccionListado(SeleccionLista classSeleccionListado){
        this.classSeleccionListado =classSeleccionListado;
    }
    public void cargarVariables(){
        nombre = this.JTFNombreCliente.getText();
        apellido = this.JTFApellidoCliente.getText();
        rnc = this.JTFRNCCliente.getText();
        cedula = this.JTFCedulaCliente.getText();
        email = this.JTFEmailCliente.getText();
        telefono = this.JTFTelefonoCliente.getText();
        provincia = this.JTFProvinciaCliente.getText();
        sector = this.JTFSectorCliente.getText();
        direccion = this.JTADireccionCliente.getText();
        sexo = this.JCBSexoCliente.getSelectedItem().toString();
        //javax.swing.JTextField fn = (javax.swing.JTextField) this.JDCFechaNacimientoCliente.getDateEditor().getUiComponent();
       fechaNacimiento = ((javax.swing.JTextField)this.JDCFechaNacimientoCliente.getDateEditor().getUiComponent()).getText();

        //fechaNacimiento = fn.getText();
       // JOptionPane.showMessageDialog(null, this.fechaNacimiento);
    }
    public void insertarDBEmail(){
             String campos = "usuario_id,email,persona_id,fecha_creado";
             String valores = "'"+this.usuarioID+"','"+this.email+"','"+this.personaID+"',now()";
             this.mysql.insertData("email", campos, valores);
    }
    
     public void insertarDBTelefono(){
             String campos = "usuario_id,telephone,persona_id,fecha_creado,tipo_telefono_id";
             String valores = "'"+this.usuarioID+"','"+this.telefono+"','"+this.personaID+"',now(),'1'";
             this.mysql.insertData("telephone", campos, valores);
    }
     public void insertarDBDireccion(){
             String campos = "usuario_id,sector,persona_id,fecha_creado,provincia,localidad";
             String valores = "'"+this.usuarioID+"','"+this.sector+"','"+this.personaID+"',now(),'"+this.provincia+"','"+this.direccion+"'";
             this.mysql.insertData("direccion", campos, valores);
    }
    public void insertarDBPersona(){
           String campos = "nombre,apellido,sexo,fecha_creado,usuario_id,rnc";//fecha_nacimiento
             String valores = "'"+this.nombre+"','"+this.apellido+"','"+this.sexo+"',now(),'"+this.usuarioID+"','"+this.rnc+"'";
             this.mysql.insertData("persona", campos, valores);
             this.personaID = this.mysql.optenerUltimoID("persona");
    }
    public void insertarDBCliente(){
           String campos = "usuario_id,persona_id,fecha_creado";//fecha_nacimiento
             String valores = "'"+this.usuarioID+"','"+this.personaID+"',now()";
             this.mysql.insertData("cliente", campos, valores);
             this.clienteID = this.mysql.optenerUltimoID("cliente");
    }
    public void mostrarOpciones(){
          if(this.mostrarOpciones){
              this.mostrarOpciones = false;
          }else{
              this.mostrarOpciones = true;
          }
          this.JCBSexoCliente.setVisible(mostrarOpciones);
          this.JLSexoCliente.setVisible(mostrarOpciones);
          
          this.JDCFechaNacimientoCliente.setVisible(mostrarOpciones);
          this.JLFechaNacimientoCliente.setVisible(mostrarOpciones);
          
          this.JTADireccionCliente.setVisible(mostrarOpciones);
          this.JLDireccionCliente.setVisible(mostrarOpciones);
          
          this.JTFCedulaCliente.setVisible(mostrarOpciones);
          this.JLCedulaCliente.setVisible(mostrarOpciones);
          
          this.JTFProvinciaCliente.setVisible(mostrarOpciones);
          this.JLProvinciaCliente.setVisible(mostrarOpciones);
          
          this.JTFRNCCliente.setVisible(mostrarOpciones);
          this.JLRNCCliente.setVisible(mostrarOpciones);
          
          this.JTFSectorCliente.setVisible(mostrarOpciones);
          this.JLSectorCliente.setVisible(mostrarOpciones);
    }
    public void insertDatosCliente(){
        this.cargarVariables();
        if(this.nombre.isEmpty()){
            JOptionPane.showMessageDialog(null, "El campo nombre esta vacío","Existen campo vacío",JOptionPane.WARNING_MESSAGE);
        }else if(this.apellido.isEmpty()){
            JOptionPane.showMessageDialog(null, "El campo apellido esta vacío","Existen campo vacío",JOptionPane.WARNING_MESSAGE);
        }else{
           
           this.insertarDBPersona();
           if(!this.email.isEmpty()){
               this.insertarDBEmail();
           }
           if(!this.telefono.isEmpty()){
               this.insertarDBTelefono();
           }
           if( (!this.sector.isEmpty()) || (!this.provincia.isEmpty()) || (!this.direccion.isEmpty()) ){
               this.insertarDBDireccion();
           }
           if(this.classSeleccionListado.getCotizacion() != null){
                this.insertarDBCliente();
                this.dispose();
                this.classSeleccionListado.getCotizacion().setDatosCliente(this.clienteID, this.nombre, this.cedula,this.telefono,this.email);
                this.classSeleccionListado.perderFocus();
                this.classSeleccionListado.dispose();
           }
           
           if(this.classSeleccionListado.getFacturacion() != null){
                this.insertarDBCliente();
                this.dispose();
                this.classSeleccionListado.getFacturacion().setDatosCliente(this.clienteID, this.nombre, this.cedula,this.telefono,this.email);
                 this.classSeleccionListado.getFacturacion().setRNCCliente(rnc);
                this.classSeleccionListado.perderFocus();
                this.classSeleccionListado.dispose();
           }
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

        jScrollPane2 = new javax.swing.JScrollPane();
        JTADireccionCliente = new javax.swing.JTextArea();
        JTFNombreCliente = new javax.swing.JTextField();
        JTFApellidoCliente = new javax.swing.JTextField();
        JCBSexoCliente = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        JLSexoCliente = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        JTFTelefonoCliente = new javax.swing.JTextField();
        JLEmailCliente = new javax.swing.JLabel();
        JTFEmailCliente = new javax.swing.JTextField();
        JTFCedulaCliente = new javax.swing.JTextField();
        JLCedulaCliente = new javax.swing.JLabel();
        JDCFechaNacimientoCliente = new com.toedter.calendar.JDateChooser();
        JLFechaNacimientoCliente = new javax.swing.JLabel();
        JLProvinciaCliente = new javax.swing.JLabel();
        JTFProvinciaCliente = new javax.swing.JTextField();
        JLSectorCliente = new javax.swing.JLabel();
        JTFSectorCliente = new javax.swing.JTextField();
        JLDireccionCliente = new javax.swing.JLabel();
        JLRNCCliente = new javax.swing.JLabel();
        JTFRNCCliente = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btMostrarOpciones = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JTADireccionCliente.setColumns(20);
        JTADireccionCliente.setRows(5);
        jScrollPane2.setViewportView(JTADireccionCliente);

        JTFApellidoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFApellidoClienteActionPerformed(evt);
            }
        });

        JCBSexoCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "masculino", "femenino" }));
        JCBSexoCliente.setToolTipText("");

        jLabel1.setText("Nombre cliente *");

        JLSexoCliente.setText("Sexo cliente");

        jLabel3.setText("Apellido cliente *");

        jLabel4.setText("Teléfono cliente");

        JLEmailCliente.setText("Email cliente");

        JTFEmailCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFEmailClienteActionPerformed(evt);
            }
        });

        JTFCedulaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFCedulaClienteActionPerformed(evt);
            }
        });

        JLCedulaCliente.setText("Cedula cliente ");

        JLFechaNacimientoCliente.setText("Fecha Nacimiento");

        JLProvinciaCliente.setText("Provincia cliente");

        JTFProvinciaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFProvinciaClienteActionPerformed(evt);
            }
        });

        JLSectorCliente.setText("Sector cliente");

        JTFSectorCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFSectorClienteActionPerformed(evt);
            }
        });

        JLDireccionCliente.setText("Dirección cliente");

        JLRNCCliente.setText("RNC cliente");

        jButton1.setText("Crear Cliente");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        btMostrarOpciones.setText("Ver mas opciones");
        btMostrarOpciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btMostrarOpcionesMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTFRNCCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JLRNCCliente))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(JLEmailCliente)
                                            .addComponent(JTFEmailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(JLDireccionCliente)))
                                    .addComponent(JCBSexoCliente, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(JTFNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1)
                                            .addComponent(JLSexoCliente))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(JDCFechaNacimientoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(JLFechaNacimientoCliente)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(JLCedulaCliente)
                                            .addComponent(JTFCedulaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(JTFApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(JTFTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(JLProvinciaCliente)
                                            .addComponent(JTFProvinciaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(JLSectorCliente)
                                            .addComponent(JTFSectorCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(btMostrarOpciones)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton1)))))
                        .addGap(20, 20, 20))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(JLRNCCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTFRNCCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(btMostrarOpciones))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(JTFApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(JTFTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(22, 22, 22)
                                .addComponent(JLFechaNacimientoCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JDCFechaNacimientoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(JLCedulaCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JTFCedulaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(JLProvinciaCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JTFProvinciaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(JLSectorCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JTFSectorCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTFNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(JLSexoCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JCBSexoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(JLEmailCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTFEmailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(JLDireccionCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTFApellidoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTFApellidoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTFApellidoClienteActionPerformed

    private void JTFEmailClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTFEmailClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTFEmailClienteActionPerformed

    private void JTFCedulaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTFCedulaClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTFCedulaClienteActionPerformed

    private void JTFProvinciaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTFProvinciaClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTFProvinciaClienteActionPerformed

    private void JTFSectorClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTFSectorClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTFSectorClienteActionPerformed

    private void btMostrarOpcionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btMostrarOpcionesMouseClicked
        // TODO add your handling code here:
        this.mostrarOpciones();
    }//GEN-LAST:event_btMostrarOpcionesMouseClicked

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        //this.cargarVariables();
        insertDatosCliente();
    }//GEN-LAST:event_jButton1MouseClicked

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> JCBSexoCliente;
    private com.toedter.calendar.JDateChooser JDCFechaNacimientoCliente;
    private javax.swing.JLabel JLCedulaCliente;
    private javax.swing.JLabel JLDireccionCliente;
    private javax.swing.JLabel JLEmailCliente;
    private javax.swing.JLabel JLFechaNacimientoCliente;
    private javax.swing.JLabel JLProvinciaCliente;
    private javax.swing.JLabel JLRNCCliente;
    private javax.swing.JLabel JLSectorCliente;
    private javax.swing.JLabel JLSexoCliente;
    private javax.swing.JTextArea JTADireccionCliente;
    private javax.swing.JTextField JTFApellidoCliente;
    private javax.swing.JTextField JTFCedulaCliente;
    private javax.swing.JTextField JTFEmailCliente;
    private javax.swing.JTextField JTFNombreCliente;
    private javax.swing.JTextField JTFProvinciaCliente;
    private javax.swing.JTextField JTFRNCCliente;
    private javax.swing.JTextField JTFSectorCliente;
    private javax.swing.JTextField JTFTelefonoCliente;
    private javax.swing.JButton btMostrarOpciones;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
