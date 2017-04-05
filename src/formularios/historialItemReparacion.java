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
public class historialItemReparacion extends javax.swing.JFrame {

     private Mysql mysql;
     private String estado = "pendiente";
     private String clausula = "";
     private String reparacion_detalle_id = "",usuario_id = "1";

    
     private String nota = "",precioCompleto = "0.00";
     private String[] TiposNotas = {"","","","","","","","","",""};
     private String item_id = "";
     private boolean activarSelect = false;
    /**
     * Creates new form PasoReparacionSeguimiento
     */
     public void setReparacion_detalle_id(String reparacion_detalle_id) {
        this.reparacion_detalle_id = reparacion_detalle_id;
    }
    public historialItemReparacion(Mysql mysql) {
        initComponents();
         this.mysql = mysql;
    }
/*    public void validarSelect(){
        //this.estado = this.JCBestado.getSelectedItem().toString();
        //this.precioCompleto = this.JTFPrecioFinal.getText();
    }*/

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
                   this.TiposNotas[5] = Texto.validarNull(resultSet.getString("fecha_creada"));
                   this.TiposNotas[6] = Texto.validarNull(resultSet.getString("fecha_procesado"));
                   this.TiposNotas[7] = Texto.validarNull(resultSet.getString("fecha_completado"));
                   this.TiposNotas[8] = Texto.validarNull(resultSet.getString("fecha_incompleto"));
                   this.TiposNotas[9] = Texto.validarNull(resultSet.getString("fecha_anulado"));

                    
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
        this.jLPrecioFinal.setText(precioCompletado);
        this.JLCantidad.setText(cantidad);
        this.JLNombre.setText(Nombre);
        this.jLEstado.setText(estado);
        AsignarNotaFechaEstados();
        //validarEstadoDB(estado);
       // this.validarEstadoDB(estado,this.TiposNotas[0],this.TiposNotas[3],this.TiposNotas[2],this.TiposNotas[4],this.TiposNotas[1]);
    }
 
     public void AsignarNotaFechaEstados(){
       System.out.println("validarEstadoDB");
       //this.TiposNotas[0],this.TiposNotas[3],this.TiposNotas[2],this.TiposNotas[4],this.TiposNotas[1]
       String notaPendiente=this.TiposNotas[0];
       String fechaPendiente=this.TiposNotas[5];
       
       String notaIncompleto=this.TiposNotas[3];
       String fechaIncompleto=this.TiposNotas[8];
       String notaCompleto=this.TiposNotas[2];
       String fechaCompleto=this.TiposNotas[7];
       String notaAnulado=this.TiposNotas[4];
       String fechaAnulado=this.TiposNotas[9];
       String notaProcesando=this.TiposNotas[1];
       String fechaProcesando=this.TiposNotas[6];
      // this.JTANota.setText("notaPendiente"+notaPendiente);
       this.jLNotaPendiente.setText(notaPendiente);
       this.jLNotaProcesando.setText(notaProcesando);
       this.jLNotaCompleto.setText(notaCompleto);
       this.jLNotaIncompleto.setText(notaIncompleto);
       this.jLNotaAnulado.setText(notaAnulado);
       
       this.jLFechaPendiente.setText(fechaPendiente);
       this.jLFechaProcesado.setText(fechaProcesando);
       this.jLFechaCompleto.setText(fechaCompleto);
       this.jLFechaIncompleto.setText(fechaIncompleto);
       this.jLFechaAnulado.setText(fechaAnulado);
       
        
    }
 
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JLNombre = new javax.swing.JLabel();
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
        jLEstado = new javax.swing.JLabel();
        jLPrecioFinal = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLNotaPendiente = new javax.swing.JLabel();
        jLFechaPendiente = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLNotaProcesando = new javax.swing.JLabel();
        jLFechaProcesado = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLNotaCompleto = new javax.swing.JLabel();
        jLFechaCompleto = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLNotaIncompleto = new javax.swing.JLabel();
        jLFechaIncompleto = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLNotaAnulado = new javax.swing.JLabel();
        jLFechaAnulado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JLNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLNombre.setText("jLabel1");

        JLCodigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLCodigo.setText("jLabel2");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("NOMBRE ");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CODIGO");

        JLPrecio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLPrecio.setText("jLabel5");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("PRECIO");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("ESTADO");

        jLabel8.setText("PRECIO FINAL");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("CANTIDAD");

        JLCantidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLCantidad.setText("jLabel11");
        JLCantidad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        JLPrecioTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLPrecioTotal.setText("PRECIO TOTAL");
        JLPrecioTotal.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel13.setText("PRECIO TOTAL");

        jLEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLEstado.setText("jLabel1");

        jLPrecioFinal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLPrecioFinal.setText("jLabel2");

        jLNotaPendiente.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLNotaPendiente.setText("jLabel9");
        jLNotaPendiente.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLFechaPendiente.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLFechaPendiente.setText("jLabel5");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLNotaPendiente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 468, Short.MAX_VALUE)
                        .addComponent(jLFechaPendiente, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLFechaPendiente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLNotaPendiente, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Nota Estado Pendiente", jPanel1);

        jLNotaProcesando.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLNotaProcesando.setText("jLabel9");
        jLNotaProcesando.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLFechaProcesado.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLFechaProcesado.setText("jLabel5");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(468, Short.MAX_VALUE)
                        .addComponent(jLFechaProcesado, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLNotaProcesando, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLFechaProcesado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLNotaProcesando, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Nota Estado Procesando", jPanel2);

        jLNotaCompleto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLNotaCompleto.setText("jLabel9");
        jLNotaCompleto.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLFechaCompleto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLFechaCompleto.setText("jLabel5");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap(468, Short.MAX_VALUE)
                        .addComponent(jLFechaCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLNotaCompleto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jLFechaCompleto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLNotaCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Nota Estado Completo", jPanel4);

        jLNotaIncompleto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLNotaIncompleto.setText("jLabel9");
        jLNotaIncompleto.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLFechaIncompleto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLFechaIncompleto.setText("jLabel5");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap(468, Short.MAX_VALUE)
                        .addComponent(jLFechaIncompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLNotaIncompleto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jLFechaIncompleto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLNotaIncompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Nota Estado Incompleto", jPanel5);

        jLNotaAnulado.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLNotaAnulado.setText("jLabel9");
        jLNotaAnulado.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLFechaAnulado.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLFechaAnulado.setText("jLabel5");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap(468, Short.MAX_VALUE)
                        .addComponent(jLFechaAnulado, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLNotaAnulado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jLFechaAnulado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLNotaAnulado, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Nota Estado Anulado", jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JLCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JLNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(JLPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLPrecioFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                            .addComponent(JLCantidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JLPrecioTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
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
                                    .addComponent(JLPrecioTotal)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLPrecioFinal)))
                        .addGap(18, 18, 18)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLEstado)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLCantidad;
    private javax.swing.JLabel JLCodigo;
    private javax.swing.JLabel JLNombre;
    private javax.swing.JLabel JLPrecio;
    private javax.swing.JLabel JLPrecioTotal;
    private javax.swing.JLabel jLEstado;
    private javax.swing.JLabel jLFechaAnulado;
    private javax.swing.JLabel jLFechaCompleto;
    private javax.swing.JLabel jLFechaIncompleto;
    private javax.swing.JLabel jLFechaPendiente;
    private javax.swing.JLabel jLFechaProcesado;
    private javax.swing.JLabel jLNotaAnulado;
    private javax.swing.JLabel jLNotaCompleto;
    private javax.swing.JLabel jLNotaIncompleto;
    private javax.swing.JLabel jLNotaPendiente;
    private javax.swing.JLabel jLNotaProcesando;
    private javax.swing.JLabel jLPrecioFinal;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
