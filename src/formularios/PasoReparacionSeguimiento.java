/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import conexion.Mysql;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Eudy
 */
public class PasoReparacionSeguimiento extends javax.swing.JFrame {

     private Mysql mysql;
     private String estado = "pendiente";
     private String clausula = "";
     private String reparacion_detalle_id = "",usuario_id = "1";

    
     private String nota = "",precioCompleto = "0.00";
     private String[] TiposNotas = {"","","","",""};
     private String item_id = "";
     private boolean activarSelect = false;
    /**
     * Creates new form PasoReparacionSeguimiento
     */
     public void setReparacion_detalle_id(String reparacion_detalle_id) {
        this.reparacion_detalle_id = reparacion_detalle_id;
    }
    public PasoReparacionSeguimiento(Mysql mysql) {
        initComponents();
         this.mysql = mysql;
    }
    public void validarSelect(){
        this.estado = this.JCBestado.getSelectedItem().toString();
        this.precioCompleto = this.JTFPrecioFinal.getText();
    }
    public void obtenerDatosDB(){
        this.activarSelect = false;
         String table_name = " reparacion_detalle as c ";
         String campos = " c.* ";
         String otros = " where c.id = '"+this.item_id+"' ";
         java.sql.ResultSet resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
         String codigo,monto,fecha,cantidad,precio,nombre,precio_completo;
         try {
             if(resultSet.next()){
                 codigo = item_id;
                     monto = Texto.validarNull(resultSet.getString("total"));
                     precio = Texto.validarNull(resultSet.getString("precio"));
                    precio_completo = Texto.validarNull(resultSet.getString("precio_completado"));
                    this.precioCompleto = precio_completo;
                     estado = Texto.validarNull(resultSet.getString("estado"));
                     fecha =Texto.validarNull(resultSet.getString("fecha_creada"));
                      cantidad =Texto.validarNull(resultSet.getString("cantidad"));
                      nombre =Texto.validarNull(resultSet.getString("nombre"));
                   this.TiposNotas[0] = Texto.validarNull(resultSet.getString("nota"));
                   this.TiposNotas[1] = Texto.validarNull(resultSet.getString("nota_procesando"));
                   this.TiposNotas[2] = Texto.validarNull(resultSet.getString("nota_completado"));
                   this.TiposNotas[3] = Texto.validarNull(resultSet.getString("nota_incompleto_por"));
                   this.TiposNotas[4] = Texto.validarNull(resultSet.getString("comentario_anulado"));
                 setDatosItemDetalle(precio,cantidad, nombre, codigo, monto,estado,precio_completo);
                 
             }
         } catch (SQLException ex) {
             Logger.getLogger(PasoReparacionSeguimiento.class.getName()).log(Level.SEVERE, null, ex);
         }
         this.activarSelect = true;
    }
    public void obtenerDatosDB(String item_id){
        this.item_id =item_id;
        this.reparacion_detalle_id =item_id;
        this.activarSelect = false;
         String table_name = " reparacion_detalle as c ";
         String campos = " c.* ";
         String otros = " where c.id = '"+this.item_id+"' ";
         java.sql.ResultSet resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
         String codigo="",monto="",fecha="",cantidad="",precio="",nombre="",precio_completo="";
         try {
             

             if(resultSet.next()){
                 System.out.println("prueba");
                 codigo = item_id;
                     monto = Texto.validarNull(resultSet.getString("total"));
                     precio = Texto.validarNull(resultSet.getString("precio"));
                    precio_completo = Texto.validarNull(resultSet.getString("precio_completado"));
                    this.precioCompleto = precio_completo;
                     estado = Texto.validarNull(resultSet.getString("estado"));
                     fecha =Texto.validarNull(resultSet.getString("fecha_creada"));
                      cantidad =Texto.validarNull(resultSet.getString("cantidad"));
                      nombre =Texto.validarNull(resultSet.getString("nombre"));
                   
                   this.TiposNotas[0] = Texto.validarNull(resultSet.getString("nota"));
                   this.TiposNotas[1] = Texto.validarNull(resultSet.getString("nota_procesando"));
                   this.TiposNotas[2] = Texto.validarNull(resultSet.getString("nota_completado"));
                   this.TiposNotas[3] = Texto.validarNull(resultSet.getString("nota_incompleto_por"));
                   this.TiposNotas[4] = Texto.validarNull(resultSet.getString("comentario_anulado"));
                 
                    
             }
             //this.setDatosItemDetalle(precio,cantidad, nombre, codigo, monto,estado,precio_completo);
                
             System.out.println("========prueba");
         } catch (SQLException ex) {
             Logger.getLogger(PasoReparacionSeguimiento.class.getName()).log(Level.SEVERE, null, ex);
         }
        this.setDatosItemDetalle(precio,cantidad, nombre, codigo, monto,estado,precio_completo);
        this.activarSelect = true;
    }
    public void setDatosItemDetalle(String precio,String cantidad,String Nombre, String Codigo,String montoTotal,String estado,String precioCompletado){
        System.out.println("setDatosItemDetalle");
        this.JLPrecioTotal.setText(montoTotal);
        this.JLCodigo.setText(Codigo);
        this.JLPrecio.setText(precio);
        this.JTFPrecioFinal.setText(precioCompletado);
        this.JLCantidad.setText(cantidad);
        this.JLNombre.setText(Nombre);
        this.JCBestado.setSelectedItem(estado);
        validarEstadoDB(estado);
       // this.validarEstadoDB(estado,this.TiposNotas[0],this.TiposNotas[3],this.TiposNotas[2],this.TiposNotas[4],this.TiposNotas[1]);
    }
    
    public void validarEstado(){
        //this.validarSelect();
         this.clausula = " ";
        this.clausula += " estado = '"+this.estado+"', precio_completado = '"+precioCompleto+"' ";
        switch(this.estado){
            case "pendiente":
               this.clausula += ",nota = '"+nota+"' ";
                break;
             case "procesando":
               this.clausula += ",nota_procesando = '"+nota+"', usuario_id_procesando = '"+usuario_id+"', fecha_procesado = now() ";                
                break;
            case "completado":
                this.clausula += ",nota_completado = '"+nota+"', usuario_id_completado = '"+usuario_id+"', fecha_completado = now() ";                
                break;
            case "incompleto":
                this.clausula += ",nota_incompleto_por = '"+nota+"', usuario_id_incompleto_por = '"+usuario_id+"', fecha_incompleto = now() ";                                
                break;
            case "anulada":
                this.clausula += ",comentario_anulado = '"+nota+"', usuario_id_anulado = '"+usuario_id+"', fecha_anulado = now() ";                                
                break;
        }
    }
     public void validarEstadoDB(String estado){
       System.out.println("validarEstadoDB");
       //this.TiposNotas[0],this.TiposNotas[3],this.TiposNotas[2],this.TiposNotas[4],this.TiposNotas[1]
       String notaPendiente=this.TiposNotas[0];
       String notaIncompleto=this.TiposNotas[3];
       String notaCompleto=this.TiposNotas[2];
       String notaAnulado=this.TiposNotas[4];
       String notaProcesando=this.TiposNotas[1];
       this.JTANota.setText("notaPendiente"+notaPendiente);
       
        switch(estado){
            case "pendiente":
                this.JTANota.setText(notaPendiente);
                 break;
             case "procesando":
                 this.JTANota.setText(notaProcesando);
                 break;
            case "completado":
                this.JTANota.setText(notaCompleto);
                break;
            case "incompleto":
                this.JTANota.setText(notaIncompleto);
                break;
            case "anulada":
                this.JTANota.setText(notaAnulado);
                break;
        }
    }
    public void validarEstadoDB(String estado,String notaPendiente,String notaIncompleto,String notaCompleto,String notaAnulado,String notaProcesando){
       System.out.println("validarEstadoDB");
       
       this.JTANota.setText("notaPendiente"+notaPendiente);
       

       
       
        switch(estado){
            case "pendiente":
                this.JTANota.setText(notaPendiente);
                this.JTANota.setVisible(true);
                break;
             case "procesando":
                 this.JTANota.setText(notaProcesando);
                 //this.JTANotaProcesando.setVisible(true);
                 break;
            case "completado":
                this.JTANota.setText(notaCompleto);
                //this.JTANotaCompletado.setVisible(true);
                break;
            case "incompleto":
                this.JTANota.setText(notaIncompleto);
                //this.JTANotaIncompleto.setVisible(true);
                break;
            case "anulada":
                this.JTANota.setText(notaAnulado);
                //this.JTANotaAnulado.setVisible(true);
                break;
        }
    }
    public void MostrarNotasPorEstado(String estado){
        switch(estado){
            case "pendiente":
                this.JTANota.setVisible(true);
                System.out.println("Pendiente");
                break;
             case "procesando":
                 this.JTANota.setVisible(true);
                 System.out.println("procesando");
                 break;
            case "completado":
                this.JTANota.setVisible(true);
                System.out.println("completado");
                break;
            case "incompleto":
                this.JTANota.setVisible(true);
                System.out.println("incompleto");
                break;
            case "anulada":
                this.JTANota.setVisible(true);
                System.out.println("anulada");
                break;
        }
    }
    public void actualizarDato(){
        this.nota = this.JTANota.getText();
        this.estado = this.JCBestado.getSelectedItem().toString();
        this.validarEstado();
       boolean r = this.mysql.actulizarDatos("reparacion_detalle",this.clausula, " id = '"+reparacion_detalle_id+"' ");
       if(r){
           JOptionPane.showMessageDialog(null, "Se guardaron los datos", "Datos Guardados", JOptionPane.INFORMATION_MESSAGE);
       }else{
       JOptionPane.showMessageDialog(null, "No se guardaron los datos", "Error", JOptionPane.WARNING_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JTANota = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        JBGuardar = new javax.swing.JButton();
        JCBestado = new javax.swing.JComboBox<>();
        JLNombre = new javax.swing.JLabel();
        JTFPrecioFinal = new javax.swing.JTextField();
        JLCodigo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        JLPrecio = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        JLCantidad = new javax.swing.JLabel();
        JLPrecioTotal = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JTANota.setColumns(20);
        JTANota.setRows(5);
        jScrollPane1.setViewportView(JTANota);

        jLabel9.setText("NOTA PARA EL ESTADO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel9)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        JBGuardar.setText("Guardar Cambios");
        JBGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JBGuardarMouseClicked(evt);
            }
        });

        JCBestado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "procesando", "pendiente", "completado", "incompleto", "anulada" }));
        JCBestado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCBestadoItemStateChanged(evt);
            }
        });

        JLNombre.setText("jLabel1");

        JTFPrecioFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTFPrecioFinalActionPerformed(evt);
            }
        });

        JLCodigo.setText("jLabel2");

        jLabel3.setText("NOMBRE ");

        jLabel4.setText("CODIGO");

        JLPrecio.setText("jLabel5");

        jLabel6.setText("PRECIO");

        jLabel7.setText("ESTADO");

        jLabel8.setText("PRECIO FINAL");

        jLabel10.setText("CANTIDAD");

        JLCantidad.setText("jLabel11");

        JLPrecioTotal.setText("PRECIO TOTAL");

        jLabel13.setText("PRECIO TOTAL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel4)
                .addGap(49, 49, 49)
                .addComponent(jLabel3)
                .addGap(45, 45, 45)
                .addComponent(jLabel6)
                .addGap(22, 22, 22)
                .addComponent(jLabel10)
                .addGap(48, 48, 48)
                .addComponent(jLabel13))
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(JLCodigo)
                .addGap(56, 56, 56)
                .addComponent(JLNombre)
                .addGap(56, 56, 56)
                .addComponent(JLPrecio)
                .addGap(36, 36, 36)
                .addComponent(JLCantidad)
                .addGap(50, 50, 50)
                .addComponent(JLPrecioTotal))
            .addGroup(layout.createSequentialGroup()
                .addGap(210, 210, 210)
                .addComponent(jLabel7)
                .addGap(130, 130, 130)
                .addComponent(jLabel8))
            .addGroup(layout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addComponent(JCBestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(JTFPrecioFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(340, 340, 340)
                .addComponent(JBGuardar))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JLCodigo)
                    .addComponent(JLNombre)
                    .addComponent(JLPrecio)
                    .addComponent(JLCantidad)
                    .addComponent(JLPrecioTotal))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JCBestado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JTFPrecioFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(JBGuardar))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTFPrecioFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTFPrecioFinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTFPrecioFinalActionPerformed

    private void JBGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JBGuardarMouseClicked
        // TODO add your handling code here:
        this.actualizarDato();
    }//GEN-LAST:event_JBGuardarMouseClicked

    private void JCBestadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCBestadoItemStateChanged
        // TODO add your handling code here:
        if(this.activarSelect){
           // this.obtenerDatosDB();
           this.validarEstadoDB(this.JCBestado.getSelectedItem().toString());
           //this.MostrarNotasPorEstado(this.JCBestado.getSelectedItem().toString());
           System.out.println("se esta cambiando el checkbox");
        }
    }//GEN-LAST:event_JCBestadoItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBGuardar;
    private javax.swing.JComboBox<String> JCBestado;
    private javax.swing.JLabel JLCantidad;
    private javax.swing.JLabel JLCodigo;
    private javax.swing.JLabel JLNombre;
    private javax.swing.JLabel JLPrecio;
    private javax.swing.JLabel JLPrecioTotal;
    private javax.swing.JTextArea JTANota;
    private javax.swing.JTextField JTFPrecioFinal;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
