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
public class crearProducto extends javax.swing.JFrame {

    /**
     * Creates new form crearProducto
     */
    private boolean mostrarOpciones = true;
    private Mysql mysql;
    private String nombre="",precioVenta,precioCompra,cantidadComprada,cantidadVendida;
    private String productoID="1",usuarioID="1";
    private SeleccionLista classSeleccionListado;
    
     private String nombreUsuario = "";
    
    public void setDatosUsuario(String nombre,String id){
        this.nombreUsuario = nombre;
        this.usuarioID = id;
        System.out.println(nombre+" "+id);
    }
    
    public crearProducto(Mysql mysql) {
        initComponents();
        this.mysql = mysql;
        this.mostrarOpciones();
    }
    public void setClassSeleccionListado(SeleccionLista classSeleccionListado){
        this.classSeleccionListado =classSeleccionListado;
    }
    public void cargarVariables(){
        nombre = this.JTFNombreProducto.getText();
        precioVenta = this.JTFPrecioVentaProducto.getText();
        //rnc = this.JTFRNCCliente.getText();
        precioCompra = this.JTFPrecioCompraProducto.getText();
        cantidadComprada = this.JTFCantidadCompradaProducto.getText();
        cantidadVendida = this.JTFCantidadVendidaProducto.getText();
        //provincia = this.JTFProvinciaCliente.getText();
        //sector = this.JTFSectorCliente.getText();
        //direccion = this.JTADireccionCliente.getText();
        //sexo = this.JCBSexoCliente.getSelectedItem().toString();
        //javax.swing.JTextField fn = (javax.swing.JTextField) this.JDCFechaNacimientoCliente.getDateEditor().getUiComponent();
       //fechaNacimiento = ((javax.swing.JTextField)this.JDCFechaNacimientoCliente.getDateEditor().getUiComponent()).getText();

        //fechaNacimiento = fn.getText();
       // JOptionPane.showMessageDialog(null, this.fechaNacimiento);
    }
 /*   public void insertarDBEmail(){
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
    }*/
    public void insertarDBProducto(){
            String tipoProducto = "venta_producto";
            if(this.classSeleccionListado.getReparacion() != null){
               tipoProducto = "repacion_producto"; 
            }
           String campos = "usuario_id,nombre,cantidad_comprada,precio_compra,precio_venta,fecha_creado,tipo_producto";//fecha_nacimiento
             String valores = "'"+this.usuarioID+"','"+this.nombre+"','"+this.cantidadComprada+"','"+this.precioCompra+"','"+this.precioVenta+"',now(),'"+tipoProducto+"'";
             this.mysql.insertData("producto_inventariado", campos, valores);
             this.productoID = this.mysql.optenerUltimoID("producto_inventariado");
    }
    public void mostrarOpciones(){
          if(this.mostrarOpciones){
              this.mostrarOpciones = false;
          }else{
              this.mostrarOpciones = true;
          }
          //this.JCBSexoCliente.setVisible(mostrarOpciones);
          //this.JLSexoCliente.setVisible(mostrarOpciones);
          
          //this.JDCFechaNacimientoCliente.setVisible(mostrarOpciones);
          //this.JLFechaNacimientoCliente.setVisible(mostrarOpciones);
          
         // this.JTADireccionCliente.setVisible(mostrarOpciones);
          //this.JLDireccionCliente.setVisible(mostrarOpciones);
          
          this.JTFPrecioCompraProducto.setVisible(mostrarOpciones);
          this.JLCedulaCliente.setVisible(mostrarOpciones);
          
          this.JTFCantidadCompradaProducto.setVisible(mostrarOpciones);
          this.JLEmailCliente.setVisible(mostrarOpciones);
          
                  
          //this.JTFProvinciaCliente.setVisible(mostrarOpciones);
          //this.JLProvinciaCliente.setVisible(mostrarOpciones);
          
          //this.JTFRNCCliente.setVisible(mostrarOpciones);
          //this.JLRNCCliente.setVisible(mostrarOpciones);
          
          //this.JTFSectorCliente.setVisible(mostrarOpciones);
          //this.JLSectorCliente.setVisible(mostrarOpciones);
    }
    public void insertDatosCliente(){
        this.cargarVariables();
        if(this.nombre.isEmpty()){
            JOptionPane.showMessageDialog(null, "El campo nombre esta vacío","Existen campo vacío",JOptionPane.WARNING_MESSAGE);
        }else if(this.precioVenta.isEmpty()){
            JOptionPane.showMessageDialog(null, "El campo precio venta esta vacío","Existen campo vacío",JOptionPane.WARNING_MESSAGE);
        }else{
           if(this.cantidadComprada.isEmpty()){
               this.cantidadComprada = "0";
           }
           if(this.precioCompra.isEmpty()){
               this.precioCompra = "0.00";
           }
           this.insertarDBProducto();
           
           if(this.classSeleccionListado.getCotizacion() != null){
                this.dispose();
                this.classSeleccionListado.getCotizacion().setDatosProducto(this.productoID, this.nombre, this.precioVenta,this.cantidadVendida);
                this.classSeleccionListado.perderFocus();
                this.classSeleccionListado.dispose();
           }
           if(this.classSeleccionListado.getFacturacion() != null){
                this.dispose();
                this.classSeleccionListado.getFacturacion().setDatosProducto(this.productoID, this.nombre, this.precioVenta,this.cantidadVendida);
                this.classSeleccionListado.perderFocus();
                this.classSeleccionListado.dispose();
           }
           if(this.classSeleccionListado.getReparacion()!= null){
                this.dispose();
                this.classSeleccionListado.getReparacion().setDatosProducto(this.productoID, this.nombre, this.precioVenta,this.cantidadVendida);
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

        JTFNombreProducto = new javax.swing.JTextField();
        JTFPrecioVentaProducto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        JTFCantidadVendidaProducto = new javax.swing.JTextField();
        JLEmailCliente = new javax.swing.JLabel();
        JTFCantidadCompradaProducto = new javax.swing.JTextField();
        JTFPrecioCompraProducto = new javax.swing.JTextField();
        JLCedulaCliente = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btMostrarOpciones = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Crear Producto");

        JTFPrecioVentaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFPrecioVentaProductoActionPerformed(evt);
            }
        });

        jLabel1.setText("Nombre Producto *");

        jLabel3.setText("Precio Venta Producto *");

        jLabel4.setText("Cantidad Vendida Producto");

        JLEmailCliente.setText("Cantidad Comprada Producto");

        JTFCantidadCompradaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFCantidadCompradaProductoActionPerformed(evt);
            }
        });

        JTFPrecioCompraProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFPrecioCompraProductoActionPerformed(evt);
            }
        });

        JLCedulaCliente.setText("Precio Compra Producto");

        jButton1.setText("Crear Producto");
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JTFNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(0, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(JTFPrecioVentaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JTFCantidadVendidaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btMostrarOpciones)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)))
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JLCedulaCliente)
                            .addComponent(JTFPrecioCompraProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JLEmailCliente)
                            .addComponent(JTFCantidadCompradaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btMostrarOpciones))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JTFPrecioVentaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(JTFCantidadVendidaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JLEmailCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTFCantidadCompradaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTFNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(JLCedulaCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(JTFPrecioCompraProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTFPrecioVentaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTFPrecioVentaProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTFPrecioVentaProductoActionPerformed

    private void JTFCantidadCompradaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTFCantidadCompradaProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTFCantidadCompradaProductoActionPerformed

    private void JTFPrecioCompraProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTFPrecioCompraProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTFPrecioCompraProductoActionPerformed

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
    private javax.swing.JLabel JLCedulaCliente;
    private javax.swing.JLabel JLEmailCliente;
    private javax.swing.JTextField JTFCantidadCompradaProducto;
    private javax.swing.JTextField JTFCantidadVendidaProducto;
    private javax.swing.JTextField JTFNombreProducto;
    private javax.swing.JTextField JTFPrecioCompraProducto;
    private javax.swing.JTextField JTFPrecioVentaProducto;
    private javax.swing.JButton btMostrarOpciones;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
