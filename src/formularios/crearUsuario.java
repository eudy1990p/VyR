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
public class crearUsuario extends javax.swing.JFrame {

    /**
     * Creates new form crearCliente
     */
    private boolean mostrarOpciones = true;
    private Mysql mysql;
    private String nombre="",apellido,sexo,rnc,fechaNacimiento,cedula,email,telefono,provincia,sector,direccion,usuario,clave,tipoUsuario;
    private String personaID="1",usuarioID="1",empleadoID="1",usuarioEmpleadoID="1";
    private SeleccionLista classSeleccionListado;
    private String trabajandoClase = "contizacion";
            
    public crearUsuario(Mysql mysql) {
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
        this.tipoUsuario = this.jCBTipoUsuario.getSelectedItem().toString();
        this.usuario = this.jTFUsuario.getText();
        this.clave = new String(this.jPFPassword.getPassword());
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
    public void insertarDBEmpleado(){
           String campos = "usuario_id,persona_id,fecha_creado,usuario_empleado_id";//fecha_nacimiento
             String valores = "'"+this.usuarioID+"','"+this.personaID+"',now(),'"+this.usuarioEmpleadoID+"'";
             this.mysql.insertData("empleado", campos, valores);
             this.empleadoID = this.mysql.optenerUltimoID("empleado");
    }
    public void insertarDBUsuario(){
           String campos = "nombre_usuario,clave_usuario,tipo_usuario,usuario_id,fecha_creado";//fecha_nacimiento
             String valores = "'"+this.usuario+"','"+this.clave+"','"+this.tipoUsuario+"','"+this.usuarioID+"',now()";
             this.mysql.insertData("usuario", campos, valores);
             this.usuarioEmpleadoID = this.mysql.optenerUltimoID("usuario");
    }
    public boolean validarUsuario(String usuario){
         int existe = this.mysql.getValues("usuario", "where nombre_usuario = '"+this.usuario+"' ");
          if(existe < 1){
            return true;
        }else{
            //JOptionPane.showMessageDialog(null, "El usuario ya existe");
            return false;
          }
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
          
          this.JLEmailCliente.setVisible(mostrarOpciones);
          this.JTFEmailCliente.setVisible(mostrarOpciones);
                  
          this.jLabel4.setVisible(mostrarOpciones);
          this.JTFTelefonoCliente.setVisible(mostrarOpciones);          
    }
    public void insertDatosCliente(){
        this.cargarVariables();
        boolean existeUsuario = this.validarUsuario( this.usuario);
        if(this.usuario.isEmpty()){
            JOptionPane.showMessageDialog(null, "El campo usuario esta vacío","Existen campo vacío",JOptionPane.WARNING_MESSAGE);
        }else if(!existeUsuario){
            JOptionPane.showMessageDialog(null, "El usuario ya existe, favor introduzca otro usuario","Existe este usuario",JOptionPane.WARNING_MESSAGE);
        }else if(this.clave.isEmpty()){
            JOptionPane.showMessageDialog(null, "El campo clave esta vacío","Existen campo vacío",JOptionPane.WARNING_MESSAGE);
        }else{
           
           this.insertarDBPersona();
           this.insertarDBUsuario();
           
           if(!this.email.isEmpty()){
               this.insertarDBEmail();
           }
           if(!this.telefono.isEmpty()){
               this.insertarDBTelefono();
           }
           if( (!this.sector.isEmpty()) || (!this.provincia.isEmpty()) || (!this.direccion.isEmpty()) ){
               this.insertarDBDireccion();
           }
           this.insertarDBEmpleado();
           /*if(this.classSeleccionListado.getCotizacion() != null){
                
                this.dispose();
                this.classSeleccionListado.getCotizacion().setDatosCliente(this.clienteID, this.nombre, this.cedula,this.telefono,this.email);
                this.classSeleccionListado.perderFocus();
                this.classSeleccionListado.dispose();
           }
           
           if(this.classSeleccionListado.getFacturacion() != null){
                //this.insertarDBCliente();
                this.dispose();
                this.classSeleccionListado.getFacturacion().setDatosCliente(this.clienteID, this.nombre, this.cedula,this.telefono,this.email);
                 this.classSeleccionListado.getFacturacion().setRNCCliente(rnc);
                this.classSeleccionListado.perderFocus();
                this.classSeleccionListado.dispose();
           }
           if(this.classSeleccionListado.getReparacion() != null){
                //this.insertarDBCliente();
                this.dispose();
                this.classSeleccionListado.getReparacion().setDatosCliente(this.clienteID, this.nombre, this.cedula,this.telefono,this.email);
                this.classSeleccionListado.perderFocus();
                this.classSeleccionListado.dispose();
           }
           if( (this.classSeleccionListado.getReparacion()  == null) && (this.classSeleccionListado.getFacturacion()  == null) && (this.classSeleccionListado.getCotizacion() == null) ){
               this.insertarDBCliente();
               JOptionPane.showMessageDialog(null, "Insertado Correctamente","Cliente Creado",JOptionPane.INFORMATION_MESSAGE);
           }*/
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
        JCBSexoCliente = new javax.swing.JComboBox<>();
        JLSexoCliente = new javax.swing.JLabel();
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
        jPanel1 = new javax.swing.JPanel();
        jTFUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPFPassword = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jCBTipoUsuario = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        JTFNombreCliente = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        JTFApellidoCliente = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        JTADireccionCliente.setColumns(20);
        JTADireccionCliente.setRows(5);
        jScrollPane2.setViewportView(JTADireccionCliente);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 367, 496, 32));

        JCBSexoCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "masculino", "femenino" }));
        JCBSexoCliente.setToolTipText("");
        getContentPane().add(JCBSexoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 253, 158, -1));

        JLSexoCliente.setText("Sexo cliente");
        getContentPane().add(JLSexoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 228, -1, -1));

        jLabel4.setText("Teléfono cliente");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(364, 172, -1, -1));
        getContentPane().add(JTFTelefonoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(354, 197, 138, -1));

        JLEmailCliente.setText("Email cliente");
        getContentPane().add(JLEmailCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 284, -1, -1));

        JTFEmailCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFEmailClienteActionPerformed(evt);
            }
        });
        getContentPane().add(JTFEmailCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 309, 152, -1));

        JTFCedulaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFCedulaClienteActionPerformed(evt);
            }
        });
        getContentPane().add(JTFCedulaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(354, 253, 138, -1));

        JLCedulaCliente.setText("Cedula cliente ");
        getContentPane().add(JLCedulaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(354, 228, -1, -1));
        getContentPane().add(JDCFechaNacimientoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 253, 152, -1));

        JLFechaNacimientoCliente.setText("Fecha Nacimiento");
        getContentPane().add(JLFechaNacimientoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 228, -1, -1));

        JLProvinciaCliente.setText("Provincia cliente");
        getContentPane().add(JLProvinciaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 284, -1, -1));

        JTFProvinciaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFProvinciaClienteActionPerformed(evt);
            }
        });
        getContentPane().add(JTFProvinciaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 309, 152, -1));

        JLSectorCliente.setText("Sector cliente");
        getContentPane().add(JLSectorCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(354, 286, 118, -1));

        JTFSectorCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFSectorClienteActionPerformed(evt);
            }
        });
        getContentPane().add(JTFSectorCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(354, 311, 138, -1));

        JLDireccionCliente.setText("Dirección cliente");
        getContentPane().add(JLDireccionCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 347, -1, -1));

        JLRNCCliente.setText("RNC cliente");
        getContentPane().add(JLRNCCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));
        getContentPane().add(JTFRNCCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 152, -1));

        jButton1.setText("Crear Empleado");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 11, -1, -1));

        btMostrarOpciones.setText("Ver mas opciones");
        btMostrarOpciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btMostrarOpcionesMouseClicked(evt);
            }
        });
        getContentPane().add(btMostrarOpciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 11, -1, -1));

        jLabel2.setText("Usuario");

        jLabel5.setText("Clave");

        jCBTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "cajero", "tecnico", "supervisor", "versatil" }));

        jLabel6.setText("Tipo de usuario");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(134, 134, 134)
                        .addComponent(jLabel5)
                        .addGap(147, 147, 147)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTFUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPFPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jCBTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTFUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPFPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 510, 50));

        jLabel1.setText("Nombre cliente *");

        JTFApellidoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFApellidoClienteActionPerformed(evt);
            }
        });

        jLabel3.setText("Apellido cliente *");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(90, 90, 90)
                        .addComponent(jLabel3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(JTFNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(JTFApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 5, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JTFNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTFApellidoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 340, 50));

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
    private javax.swing.JComboBox<String> jCBTipoUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPasswordField jPFPassword;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTFUsuario;
    // End of variables declaration//GEN-END:variables
}
