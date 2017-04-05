/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import conexion.Mysql;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Eudy
 */
public class CuadreCaja extends javax.swing.JFrame {
    private Mysql mysql;
    private int totalFila;
    
    private javax.swing.JTextField TextBox;
    private String montoFacturado="",montoEnCaja;
    private String fechaDesde="",fechaHasta="";
    private String cuadreID="1",usuarioID="1";
    private double montoTotal;
    private ArrayList<String> ids = new ArrayList<String>();
    
    /**
     * Creates new form SeleccionLista
     */
    public CuadreCaja(Mysql mysql) {
        initComponents();
        this.CuandoCerrar();
        this.mysql = mysql;
        //cargarJTableCliente("","");
        //this.cargarDBTabla();
        cargarJTableFacturasSaldadas();
    }

    
    private void CuandoCerrar() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
 
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                close();
            }
        });
    }

 
    private void close(){
        /*if (JOptionPane.showConfirmDialog(rootPane, "Aun no ha seleccionado una fila ¿Desea realmente salir de esta ventanta?",
                "Salir de la ventana", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            //System.exit(0);
            this.dispose();
            this.perderFocus();
        }*/
         this.dispose();
    } 
    
    public void cargarJTableFacturasSaldadas(){
        
            /*
        SELECT p.nombre,p.apellido,f.id,f.total,u.nombre_usuario,f.`fecha_creada` FROM `factura` as f inner join cliente as c on f.cliente_id = c.id
            inner join persona as p on p.id = c.persona_id
        inner join usuario as u on u.id =f.usuario_id
            */
            String[] titulos = {"CODIGO FACTURA","MONTO FACTURA","CAJERO FACTURADOR","CLIENTE FACTURADO","FECHA FACTURA"};
            String table_name = "  `factura` as f inner join cliente as c on f.cliente_id = c.id " +
                                "inner join persona as p on p.id = c.persona_id " +
                                "inner join usuario as u on u.id =f.usuario_id ";
            String campos = "p.nombre,p.apellido,f.id,f.total,u.nombre_usuario,f.fecha_creada , (select MIN(fecha_creada) from factura where f.estado = 'saldada' and f.cuadrada = '0') as min_fecha, (select MAX(fecha_creada) from factura where f.estado = 'saldada' and f.cuadrada = '0') as max_fecha ";
            String otros = " where f.estado = 'saldada'  and f.cuadrada = '0' ";
            
          /* otros = otros+" GROUP by c.id\n" +
                         " order by e.fecha_creado, t.fecha_creado, d.fecha_creado desc ";
           */
           java.sql.ResultSet resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
        try {
            resultSet.beforeFirst();
            resultSet.last();
            this.totalFila  = resultSet.getRow();
           // this.validarAgregarNuevo();
            Object[][] fila = new Object[this.totalFila][5];
           String nombre ,usuario,id,total,fecha;
                    
            resultSet.beforeFirst();
            
            int c = 0;
            this.montoTotal = 0.00;
            
            while(resultSet.next()){

                     nombre = Texto.validarNull(resultSet.getString("nombre"))+" "+Texto.validarNull(resultSet.getString("apellido"));
                     usuario = Texto.validarNull(resultSet.getString("nombre_usuario"));
                     total = Texto.validarNull(resultSet.getString("total"));
                     this.montoTotal += Double.parseDouble(total);
                     id =Texto.validarNull(resultSet.getString("id"));  
                     fecha =Texto.validarNull(resultSet.getString("fecha_creada"));
                     this.fechaDesde = Texto.validarNull(resultSet.getString("min_fecha"));
                     this.fechaHasta = Texto.validarNull(resultSet.getString("max_fecha"));
                     this.ids.add(id);
                     fila[c][0] = id;
                     fila[c][1] = total;
                     fila[c][2] = usuario;
                     fila[c][3] = nombre;
                     fila[c][4] = fecha;
                     c++;
                     
            }
            
            this.montoFacturado = String.valueOf(this.montoTotal);
            if(this.totalFila <= 0){
                this.fechaDesde = "00-00-0000";
                this.fechaHasta = "00-00-0000";
                this.montoFacturado = "0.00";
            }
            this.asignarDatos();
             DefaultTableModel modelo = new DefaultTableModel(fila,titulos);
             this.jTable1.setModel(modelo);
                    
        } catch (SQLException ex) {
            Logger.getLogger(SeleccionLista.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void asignarDatos(){
        this.jLFechaFDesde.setText(this.fechaDesde);
        this.jLFechaFHasta.setText(this.fechaHasta);
        this.jLMontoFacturado.setText(this.montoFacturado);
    }
    
    public void insertarDBCuadre(){
             String campos = "usuario_id,fecha_creado,fecha_desde,fecha_hasta,monto_facturado,monto_en_caja";
             String valores = "'"+this.usuarioID+"',now(),'"+this.fechaDesde+"','"+this.fechaHasta+"','"+this.montoFacturado+"','"+this.montoEnCaja+"' ";
             this.mysql.insertData("cuadre", campos, valores);
             this.cuadreID = this.mysql.optenerUltimoID("cuadre");
    }
    public void insertarDBDetalleCuadre(String facturaID){
             String campos = "usuario_id,fecha_creado,factura_id,cuadre_id";
             String valores = "'"+this.usuarioID+"',now(),'"+facturaID+"','"+this.cuadreID+"' ";
             this.mysql.insertData("cuadre_detalle", campos, valores);
            // this.cuadreID = this.mysql.optenerUltimoID("cuadre");
    }
    public void adctualizarFactura(String facturaID){
        String campos = "usuario_id_cuadre = '"+this.usuarioID+"' ,fecha_cuadre = now(), cuadrada = '1' ";
        String where = " id = '"+facturaID+"' ";
        this.mysql.actulizarDatos(" factura ",campos, where);
    }
    public void modificarDatos(){
        this.montoEnCaja = this.jTFMontoEnCaja.getText();
        double totalEnCaja = Double.parseDouble(this.montoEnCaja);
        if(totalEnCaja < this.montoTotal){
            int rep = JOptionPane.showConfirmDialog(null, "El monto en caja es mejor a lo facturado, los montos deberian ser iguales, desea continuar", "Montos no coinsiden ",JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if(rep == JOptionPane.YES_OPTION){
               this.ejecutarActualizaciones();
            }else{
                JOptionPane.showMessageDialog(null, "Ok no se genero el cuadre.","No generar cuadre",JOptionPane.WARNING_MESSAGE);
            }    
        }else{
            this.ejecutarActualizaciones();
        }
    }
    public void ejecutarActualizaciones(){
            this.insertarDBCuadre();
            for(int i = 0 ; i < this.ids.size() ; i++){
                this.insertarDBDetalleCuadre(this.ids.get(i));
                this.adctualizarFactura(this.ids.get(i));
            }
          JOptionPane.showMessageDialog(null, "Se genero el cuadre.","Cuadre Generado",JOptionPane.INFORMATION_MESSAGE);
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
        jTable1 = new javax.swing.JTable();
        jTFMontoEnCaja = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLMontoFacturado = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLFechaFDesde = new javax.swing.JLabel();
        jLFechaFHasta = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTFMontoEnCaja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTFMontoEnCajaKeyReleased(evt);
            }
        });

        jButton1.setText("Guardar Cuadre");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jLabel1.setText("Monto Facturado");

        jLMontoFacturado.setText("jLabel2");

        jLabel2.setText("Fecha Facturacón Desde");

        jLabel3.setText("Fecha Facturacón Hasta");

        jLabel4.setText("Monto En Caja");

        jLFechaFDesde.setText("jLabel2");

        jLFechaFHasta.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(jLMontoFacturado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLFechaFDesde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLFechaFHasta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(jTFMontoEnCaja))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLMontoFacturado)
                    .addComponent(jTFMontoEnCaja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLFechaFDesde)
                    .addComponent(jLFechaFHasta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jTable1MousePressed

    private void jTFMontoEnCajaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFMontoEnCajaKeyReleased
        // TODO add your handling code here:
        //this.cargarJTableCliente(this.palabra, this.jTextField1.getText());
       
    }//GEN-LAST:event_jTFMontoEnCajaKeyReleased

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        this.modificarDatos();
    }//GEN-LAST:event_jButton1MouseClicked
   
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLFechaFDesde;
    private javax.swing.JLabel jLFechaFHasta;
    private javax.swing.JLabel jLMontoFacturado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFMontoEnCaja;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
