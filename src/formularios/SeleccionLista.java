/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import conexion.Mysql;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Eudy
 */
public class SeleccionLista extends javax.swing.JFrame {
    private Mysql mysql;
    private int totalFila;
    private Cotizacion cotizacion;

    public Cotizacion getCotizacion() {
        return cotizacion;
    }
    private SeleccionLista seleccionador;
    private javax.swing.JTextField TextBox;
    private String palabra="";
    /**
     * Creates new form SeleccionLista
     */
    public SeleccionLista(Mysql mysql) {
        initComponents();
        this.VistaPrincipal();
        this.mysql = mysql;
        cargarJTableCliente("","");
    }
    public void setSeleccionLista(SeleccionLista seleccionador){
        this.seleccionador = seleccionador;
    }
    public void setPalabra(String palabra){
        this.palabra = palabra;
    }
    
    private void VistaPrincipal() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
 
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                close();
            }
        });
    }
 
    private void close(){
        if (JOptionPane.showConfirmDialog(rootPane, "Aun no ha seleccionado una fila ¿Desea realmente salir de esta ventanta?",
                "Salir de la ventana", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            //System.exit(0);
            this.dispose();
            this.perderFocus();
        }
    } 
    
    public void setObjectCotizacion(Cotizacion cotizacion){
       this.cotizacion = cotizacion;
    }
    public void setTextBox(javax.swing.JTextField TextBox){
       this.TextBox = TextBox;
    }
    public void perderFocus(){
        this.TextBox.setFocusable(false);
        this.TextBox.setFocusable(true);
    }
    public void validarAgregarNuevo(){
        if(this.totalFila <= 0){
            this.jButton1.setVisible(true);
        }else{
            this.jButton1.setVisible(false);
        }
    }
    public void cargarJTableCliente(String menu,String palabra){
            
            String[] titulos = {"CODIGO CLIENTE","NOMBRE CLIENTE","CEDULA CLIENTE","SEXO CLIENTE","TELÉFONO CLIENTE","EMAIL CLIENTE","DIRECCIÓN CLIENTE"};
            //SELECT * FROM persona as p inner join cliente as c on p.id = c.persona_id
            String table_name = " persona as p inner join cliente as c on p.id = c.persona_id"
                    + " left join email as e on p.id = e.persona_id\n" +
                    " left join telephone as t on p.id = t.persona_id\n" +
                    " left join direccion as d on p.id = d.persona_id ";
            String campos = "c.id,p.nombre,p.apellido,p.cedula,p.sexo, e.email, t.telephone, concat('Provincia: ',d.provincia,' Sector: ',d.sector,' Dirección: ',d.localidad) as direccion";
            String otros = "";
            
           if(menu.toLowerCase().equals("codigo")){
                otros = " where c.id = '"+palabra+"' ";
           }else if(menu.toLowerCase().equals("cedula")){
                otros = " where p.cedula like '%"+palabra+"%' ";
           }else if(menu.toLowerCase().equals("nombre")){
                otros = " where concat(p.nombre,' ',p.apellido) like '%"+palabra+"%' ";
           }else if(menu.toLowerCase().equals("telefono")){
                otros = " where t.telephone like '%"+palabra+"%' ";
           }else if(menu.toLowerCase().equals("email")){
                otros = " where e.email like '%"+palabra+"%' ";
           }
           
           otros = otros+" GROUP by c.id\n" +
                         " order by e.fecha_creado, t.fecha_creado, d.fecha_creado desc ";
           
           java.sql.ResultSet resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
        try {
            resultSet.beforeFirst();
            resultSet.last();
            this.totalFila  = resultSet.getRow();
            this.validarAgregarNuevo();
            Object[][] fila = new Object[this.totalFila][7];
           String nombre,apellido,sexo,cedula,id,telefono,email,direccion;
                    
            resultSet.beforeFirst();
            
            int c = 0;
            
            
            while(resultSet.next()){
                   
                    nombre = resultSet.getString("nombre");
                     apellido = resultSet.getString("apellido");
                     sexo = resultSet.getString("sexo");
                     cedula = resultSet.getString("cedula");
                     id =resultSet.getString("id");
                     telefono =resultSet.getString("telephone");
                     email =resultSet.getString("email");
                     direccion =resultSet.getString("direccion");
                     fila[c][0] = id;
                     fila[c][1] = nombre+" "+apellido;
                     fila[c][2] = cedula;
                     fila[c][3] = sexo;
                     fila[c][4] = telefono;
                     fila[c][5] = email;
                     fila[c][6] = direccion;
                     JOptionPane.showMessageDialog(null, c);
                     c++;
                     
            } 
             DefaultTableModel modelo = new DefaultTableModel(fila,titulos);
             this.jTable1.setModel(modelo);
                    
        } catch (SQLException ex) {
            Logger.getLogger(SeleccionLista.class.getName()).log(Level.SEVERE, null, ex);
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
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

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

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton1.setText("Crear Nuevo");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTextField1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        // TODO add your handling code here:
        this.filaSeleccionada();
    }//GEN-LAST:event_jTable1MousePressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        this.cargarJTableCliente(this.palabra, this.jTextField1.getText());
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        crearCliente crearCliente = new crearCliente(this.mysql);
        crearCliente.setClassSeleccionListado(this.seleccionador);
        crearCliente.setVisible(true);
    }//GEN-LAST:event_jButton1MouseClicked
    public void filaSeleccionada(){
        int fila = this.jTable1.getSelectedRow();
        String ClienteID = this.jTable1.getValueAt(fila, 0).toString();
        String nombre = this.jTable1.getValueAt(fila, 1).toString();
        String cedula = this.jTable1.getValueAt(fila, 2).toString();
        this.cotizacion.setDatosCliente(ClienteID, nombre, cedula);
        this.perderFocus();
        this.dispose();
    }
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
