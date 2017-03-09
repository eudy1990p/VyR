/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;
/**
 *
 * @author Eudy
 */
import conexion.Mysql;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
public class JFrameCambiarClave extends javax.swing.JFrame {

    
    private int lineas = 10;
    private String palabraBuscar="",id="";
    private conexion.Mysql mysql;
    private String usuarioID,nombreUsuario,nombreTituloUsuario;
        private String usuario,clave,type_user;
    private String[] key = {"nombre_usuario","clave_usuario","fecha_creado","usuario_id","tipo_usuario"};
    //private int lineas=10;
    private boolean agregar=true;
    private ArrayList<String> camposEdit = new ArrayList<String>();
    private ArrayList<String> valorEdit = new ArrayList<String>();
    private ValidData valid;
   // private String usuarioID,nombreUsuario,nombreTituloUsuario;
    /**
     * Creates new form User
     */
    public JFrameCambiarClave() {
        initComponents();
      // this.user.mostrarDatosTabla(jTable1,this.jLabel6);
      // this.jtable();
    }
    public JFrameCambiarClave(conexion.Mysql mysql) {
       initComponents();
        this.mysql = mysql;
        this.valid = new ValidData();
    }

     public boolean getAgregar(){
        return agregar;
    }
    
    public void setAgregar(boolean estado){
        this.agregar = estado;
    }
    public boolean elimnar(String id){
        boolean respuesta = this.mysql.deleteRecord("users", id);
        return respuesta;
    }
    
    public void setDatosUsuario(String usuarioID, String nombreUsuario,String nombreTituloUsuario){
        this.usuarioID = usuarioID;
        this.nombreUsuario = nombreUsuario;
        this.nombreTituloUsuario = nombreTituloUsuario;
        //JOptionPane.showMessageDialog(null, "Usuario "+this.usuarioID+" "+this.nombreUsuario+" "+this.nombreTituloUsuario);    
    }
    public boolean insert(String usuario,String clave,String RepetidaClave,String type_user){
        if(valid.validEmpty(usuario, "Nombre de usuario")){
           return false; 
        }else if(valid.validEmpty(clave, "Clave de usuario")){
            return false;
        }else if(valid.validEmpty(RepetidaClave, "Repetir clave de usuario")){
            return false;
        }else if(valid.differentPass(clave, RepetidaClave)){
            return false;
        }else{
           
                if(this.validarUsuario(usuario)){
                    this.usuario = usuario;
                    this.clave = clave;
                    this.type_user = type_user;
                    boolean respuesta = this.procesarInsert();
                    return respuesta;
                }else{
                    return false;
                }
         }
        
    }
    public boolean validarUsuario(String usuario){
         int existe = this.mysql.getValues("usuario", "where nombre_usuario = '"+usuario+"' ");
          if(existe < 1){
            return true;
        }else{
            JOptionPane.showMessageDialog(null, "El usuario ya existe");
            return false;
          }
    }
    public void limpiarTexto(javax.swing.JPasswordField p1,javax.swing.JPasswordField p2){
        p1.setText("");p2.setText("");
    
    }
    public boolean update( String clave,String RepetidaClave,String id){
        
       /* if(valid.validEmpty(type_user)){
          this.valorEdit.add(type_user);
          this.camposEdit.add("type_of_user");
        }*/
        if(valid.validEmpty(clave)){
          this.valorEdit.add(clave);
          this.camposEdit.add("password_user");
        }
            //this.usuario = usuario;
            this.clave = clave;
            //this.type_user = type_user;            
            this.id = this.usuarioID;

            boolean respuesta = this.procesarUpdate();
            return respuesta;
       // }
    }
    public boolean procesarInsert(){
        String[] values = {this.usuario,this.clave,"now()",this.usuarioID,this.type_user};
        System.out.println(" key "+this.key+" Values "+values+" total index "+values.length);
        boolean respuesta = mysql.generarInsert(this.key, values, "users");
        return respuesta;
    }
    
    public boolean procesarUpdate(){
         int total =this.camposEdit.size();
         String[] key = new String[total];
        String[] values =new String[total];

        for(int i = 0 ; i <  this.camposEdit.size() ; i++){
            key[i] =  this.camposEdit.get(i)/*{"password_user","type_of_user"}*/;
            values[i] = this.valorEdit.get(i);
        }
        System.out.println(" key "+this.key+" Values "+values+" total index "+values.length);
        boolean respuesta = mysql.updateRecord("usuario",this.id,key, values);
        return respuesta;
    }
    
    public void mostrarDatosTabla(JTable table,JLabel JLabelTotal,String palabraBuscar){
        String[] datos = {"id","name_user","type_of_user"};
        String campo = "name_user";
        System.out.println(palabraBuscar);
        Object[][] resultado = (Object[][]) this.mysql.generarSelect("users", datos,palabraBuscar,campo);
        Object[][] infoTabla= (Object[][]) resultado[0][0];
        DefaultTableModel modelo = new DefaultTableModel(infoTabla,datos);
        JLabelTotal.setText(resultado[0][1]+"");
        table.setModel(modelo); 
    }
    public String[] mostrarEditarUsuario(String id){
        String[] campos = {"id","name_user","type_of_user"};
        String[] respuesta = this.mysql.generarSelect("users", id,campos);
        this.setAgregar(false);
        return respuesta;
    }
    
    public void cambiarTestoParaEditar(javax.swing.JButton b,javax.swing.JLabel l,javax.swing.JTextField jtf){
        if(this.getAgregar()){
            b.setText("Agregar");
            l.setText("Agregar Usuario");
            jtf.enable(true);
        }else{
            b.setText("Editar");
            l.setText("Editar Usuario");
            jtf.enable(false);
        }
    }
    
    public void mostrarDatosTabla(JTable table,JLabel JLabelTotal){
        /*String[] titulos = {"id","usuario"};
          //modelo.setColumnIdentifiers(titulos);
           Object[][] fila = new Object[this.lineas][2];
           for(int c = 0 ; c < this.lineas ; c++){
               System.out.println("---- x "+ c);
                //for(int i = 0 ; i < 2 ; i++){
                    fila[c][0] = "x "+ c+" y ";
                    //System.out.println("x "+ c+" y "+i);
                    fila[c][1]  = "Eudy";
                    //modelo.addRow(fila);
                //}
           }
          DefaultTableModel modelo = new DefaultTableModel(fila,titulos);
          table.setModel(modelo);*/


        //table.setModel(null);
        //DefaultTableModel modal = (DefaultTableModel) table.getModel();
        //table.setModel(new DefaultTableModel());
        //table.setModel(modal);
        //DefaultTableModel modal = (DefaultTableModel) table.getModel();
        
        //Object[] columnas = new Object[modal.getColumnCount()];
        //{"name_user","password_user","when_it","id_user","type_of_user"}
        String[] datos = {"id","name_user","type_of_user"};
        
        Object[][] resultado = (Object[][]) this.mysql.generarSelect("users", datos);
        Object[][] infoTabla= (Object[][]) resultado[0][0];
        DefaultTableModel modelo = new DefaultTableModel(infoTabla,datos);
        JLabelTotal.setText(resultado[0][1]+"");
        table.setModel(modelo);
       // JLabelTotal.setText(""+modal.getRowCount());
        
    }
    /*public void setDatosUsuario(String usuarioID, String nombreUsuario,String nombreTituloUsuario){
        this.usuarioID = usuarioID;
        this.nombreUsuario = nombreUsuario;
        this.nombreTituloUsuario = nombreTituloUsuario;
        //JOptionPane.showMessageDialog(null, "Usuario "+this.usuarioID+" "+this.nombreUsuario+" "+this.nombreTituloUsuario);
       // this.user.setDatosUsuario(usuarioID, nombreUsuario, nombreTituloUsuario);
        this.jLabel2.setText( this.nombreUsuario );
    }*/
    
    
    //EJEMPLO SIMPLE PARA MOSTRAR DATOS EN UNA TABLA
    public void jtable(){
            JOptionPane.showMessageDialog(null, "Tabla");
           //DefaultTableModel modelo = (DefaultTableModel) this.jTable1.getModel();
           
           String[] titulos = {"id","usuario"};
           this.lineas = this.lineas + 2;
          //modelo.setColumnIdentifiers(titulos);
           
           Object[][] fila = new Object[this.lineas][2];
           for(int c = 0 ; c < this.lineas ; c++){
               System.out.println("---- x "+ c);
                //for(int i = 0 ; i < 2 ; i++){
                    fila[c][0] = "x "+ c+" y ";
                    //System.out.println("x "+ c+" y "+i);
                    fila[c][1]  = "Eudy";
                    //modelo.addRow(fila);
                //}
           }
          DefaultTableModel modelo = new DefaultTableModel(fila,titulos);
          //this.jTable1.setModel(modelo);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();

        jMenuItem2.setText("Eliminar");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem2MousePressed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Administrar Usuario");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Cambiar Clave De Usuario");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("usuario");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Clave de usuario");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jButton1.setText("Cambiar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jPasswordField1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jPasswordField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Repetir Clave ");

        jPasswordField2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jPasswordField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPasswordField2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
      // this.jtable();
            String p1 = new String(this.jPasswordField1.getPassword());
            String p2 = new String(this.jPasswordField2.getPassword());
           // String type_user = (String) this.jComboBox1.getSelectedItem();
           if(p1.equals(p2)){
            //JOptionPane.showMessageDialog(null, type_user);
                boolean respuesta = this.update( p1, p2,this.id);
                if(respuesta){
                    this.limpiarTexto(jPasswordField1, jPasswordField2);
                    JOptionPane.showMessageDialog(null, "Clave cambiada");
                    //this.user.mostrarNuevoDatoTabla(this.jTable1, this.jLabel6);           
                }
           }
      
      
    }//GEN-LAST:event_jButton1MouseClicked

    private void jMenuItem2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MousePressed
        // TODO add your handling code here:
         // TODO add your handling code here:
        /*int fila = this.jTable1.getSelectedRow();
         
        String id = this.jTable1.getValueAt(fila, 0).toString();
        String user = this.jTable1.getValueAt(fila, 0).toString();
        
        this.id = id;
        int respuesta = JOptionPane.showConfirmDialog(null, "Desea eliminar este registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION);
        if(respuesta == JOptionPane.YES_OPTION){
            boolean eliminado = this.user.elimnar(id);
            if(eliminado){
                JOptionPane.showMessageDialog(null, "Se elimino correctamente el registro.");
                this.user.mostrarDatosTabla(this.jTable1, this.jLabel6);
                this.user.setAgregar(true);
                this.user.cambiarTestoParaEditar(this.jButton1, this.jLabel9,this.jTextField1);
                this.user.limpiarTexto(jPasswordField1, jPasswordField2, jTextField1, jComboBox1);
            }else{
                 JOptionPane.showMessageDialog(null, "No se pudo eliminar correctamente el registro.");           
            }
        }else{
            //JOptionPane.showMessageDialog(null, "NO");

        }*/
       //String id1 = this.jTable1.getModel().getValueAt(this.jTable1.getSelectedRow(),0).toString();
        //String id1 = this.jTable1.getValueAt(select, 0).toString();
        
        
        
        System.out.println("prueba "+id+" prueba ");
    }//GEN-LAST:event_jMenuItem2MousePressed

    
    /**
     * @param args the command line arguments
     */
   //public static void  main(String[] a){
   //   new JFrameCambiarClave().setVisible(true);
  // }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPopupMenu jPopupMenu1;
    // End of variables declaration//GEN-END:variables
}
