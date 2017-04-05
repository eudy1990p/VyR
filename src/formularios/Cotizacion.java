/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import conexion.Mysql;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Eudy
 */
public class Cotizacion extends javax.swing.JFrame {

    /**
     * Creates new form Cotizacion
     */
    private ArrayList<String> producto_id = new ArrayList<String>();
    private ArrayList<String> sub_total_producto = new ArrayList<String>();
    private ArrayList<String> codigo_producto = new ArrayList<String>();
    private ArrayList<String> nombre_producto = new ArrayList<String>();
    private ArrayList<String> precio_producto = new ArrayList<String>();
    private ArrayList<String> cantidad_producto = new ArrayList<String>();

    private List productos;
    
    private String n_producto,p_producto,c_producto,co_producto, sub_total,itbis_total, monto_total;
    private boolean tiene_itbis = false;
    
    private boolean existe_cliente = false,existe_producto= false,cotizado = false,no_agrego_producto = true,no_agrego_cliente = true;
    private Mysql mysql;
    
    private double montoTotal = 0.00,subMontoTotal = 0.00,itbisTotal = 0.00;
            
    
    private String ClienteID="1",UsuarioID="1",CotizacionID="1",productoID="1";
    
    private Cotizacion ObjectCotizacion;
    
    public Cotizacion(Mysql mysql) {
        initComponents();
        //this.limpiarProductoTexto();
        this.limpiarTodoTexto();
        this.mysql = mysql;  
    }
    
    public void setObjectCotizacion(Cotizacion c){
        this.ObjectCotizacion = c;
    }
    public void setDatosCliente(String ClienteID,String nombre,String cedula,String telefono,String email){
            this.ClienteID = ClienteID;
            this.t_codigo_cliente.setText(this.ClienteID);
            this.t_nombre_cliente.setText(nombre);
            this.t_cedula_cliente.setText(cedula);
            this.t_telefono_cliente.setText(telefono);
            this.t_email_cliente.setText(email);
            this.no_agrego_cliente = false;
    }
    public void setDatosProducto(String productoID,String nombre,String precio,String cantidad){
            this.productoID = productoID;
            this.t_codigo_producto.setText(productoID);
            this.t_nombre_producto.setText(nombre);
            this.t_precio_producto.setText(precio);
            this.t_cantidad_producto.setText(cantidad);
            
    }
    public void asignarProductoTexto(){
        n_producto = this.t_nombre_producto.getText();
        p_producto = this.t_precio_producto.getText();
        c_producto = this.t_cantidad_producto.getText();
        co_producto = this.t_codigo_producto.getText();
    }
    public void limpiarProductoTexto(){
        this.t_nombre_producto.setText(Texto.nombre_producto);
        this.t_precio_producto.setText(Texto.precio_producto);
        this.t_cantidad_producto.setText(Texto.cantidad_producto);
        this.t_codigo_producto.setText(Texto.codigo_producto);
        
        //this.t_nombre_cliente.setText(Texto.nombre_cliente);
        //this.t_cedula_cliente.setText(Texto.cedula_cliente);
        //this.t_codigo_cliente.setText(Texto.codigo_cliente);
        //this.t_telefono_cliente.setText(Texto.telefono_cliente);
        //this.t_email_cliente.setText(Texto.email_cliente);

    }
    public void limpiarTodoTexto(){
        this.t_nombre_producto.setText(Texto.nombre_producto);
        this.t_precio_producto.setText(Texto.precio_producto);
        this.t_cantidad_producto.setText(Texto.cantidad_producto);
        this.t_codigo_producto.setText(Texto.codigo_producto);
        
        this.t_nombre_cliente.setText(Texto.nombre_cliente);
        this.t_cedula_cliente.setText(Texto.cedula_cliente);
        this.t_codigo_cliente.setText(Texto.codigo_cliente);
        this.t_telefono_cliente.setText(Texto.telefono_cliente);
        this.t_email_cliente.setText(Texto.email_cliente);

    }
    public void insertarDBDetalleCotizacion(String id,String nombre,String precio,String cantidad,String total){
            if(!this.existe_producto){
             String campos = "usuario_id,nombre,precio,cantidad,total,fecha_creada,cliente_id,cotizacion_id,producto_inventariado_id";
             String valores = "'"+this.UsuarioID+"','"+nombre+"','"+precio+"','"+cantidad+"','"+total+"',now(),'"+this.ClienteID+"','"+this.CotizacionID+"','"+id+"'";
             this.mysql.insertData("cotizacion_detalle", campos, valores);
            }
    }
    public void insertarDBCotizacion(String sub_total,String itbis,String tiene_itbis,String total){
            String campos = "usuario_id,sub_total,itbis,tiene_itbis,total,fecha_creada,cliente_id";
             String valores = "'"+this.UsuarioID+"','"+sub_total+"','"+itbis+"','"+tiene_itbis+"','"+total+"',now(),'"+this.ClienteID+"'";
             this.mysql.insertData("cotizacion", campos, valores);
             this.CotizacionID = this.mysql.optenerUltimoID("cotizacion");
    }
    public void crearCotizacion(){
           if(!this.cotizado){
            this.insertarDBCotizacion(this.subMontoTotal+"", this.itbisTotal+"","0", this.montoTotal+"");
            int lineas = this.nombre_producto.size();
            String nombre,precio,cantidad,total,id;
            for(int c = 0 ; c < lineas ; c++){
                     nombre = this.nombre_producto.get(c);
                     precio = this.precio_producto.get(c);
                     cantidad = this.cantidad_producto.get(c);
                     total = this.sub_total_producto.get(c);
                     id = this.producto_id.get(c);
                 this.insertarDBDetalleCotizacion(id,nombre, precio, cantidad, total);
            }
            this.cotizado = true;
           }
           this.generarPDF();
    }
    public void asignarAArrayList(){
        this.producto_id.add(this.productoID);
        this.cantidad_producto.add(this.c_producto);
        this.nombre_producto.add(this.n_producto);
        this.precio_producto.add(""+this.p_producto);
        this.codigo_producto.add(this.co_producto);
        double monto =(Double.parseDouble(c_producto) * Double.parseDouble(p_producto) );
        this.sub_total_producto.add( ""+monto );
        this.totales(monto);
    }
    public void agregarTabla(){
        boolean r = this.validadVacio();
        if(r){
            this.no_agrego_producto = false;
            this.validarAgregarEntradaProducto();
            this.asignarAArrayList();
            this.cargarJTable();
            this.limpiarProductoTexto();
        }
    }
    public void validarAgregarEntradaProducto(){
        Texto.placeholder(Texto.codigo_producto,this.t_codigo_producto.getText(), this.t_codigo_producto);
        Texto.placeholder(Texto.nombre_producto,this.t_nombre_producto.getText(), this.t_nombre_producto);
        Texto.placeholder(Texto.precio_producto,this.t_precio_producto.getText(), this.t_precio_producto);
        Texto.placeholder(Texto.cantidad_producto,this.t_cantidad_producto.getText(), this.t_cantidad_producto);
    }
    public void totales(double monto){
        double itbis = 0,monto_total = 0;
        this.sub_total = ""+monto;
        
        if(this.tiene_itbis){
            itbis = monto * 0.18; 
        }
        monto_total = monto - itbis; 
        
        this.montoTotal +=monto_total;
        this.itbisTotal += itbis;
        this.subMontoTotal += monto;
        this.itbis_total = ""+itbis;
        this.monto_total = ""+monto_total;
        this.cotizacion_itbis_total.setText("$ "+this.itbisTotal);
        this.cotizacion_sub_total.setText("$ "+this.subMontoTotal);
        this.cotizacion_monto_total.setText("$ "+this.montoTotal);
    }
    public void cargarJTable(){
           String[] titulos = {"CODIGO","PRODUCTO","CANTIDAD","PRECIO","SUB TOTAL"};
           int lineas = this.nombre_producto.size();
           String nombre,precio,cantidad,total,id;
           
           this.productos  = new LinkedList();
           
           Object[][] fila = new Object[lineas][5];
           
           for(int c = 0 ; c < lineas ; c++){
                    id = this.codigo_producto.get(c);
                    nombre = this.nombre_producto.get(c);
                    precio = this.precio_producto.get(c);
                    cantidad = this.cantidad_producto.get(c);
                    total = this.sub_total_producto.get(c);
                    
                    fila[c][0] = id;
                    fila[c][1] = nombre;
                    fila[c][2] = cantidad;
                    fila[c][3] = precio;
                    fila[c][4] = total;
                    
                    this.productos.add( new producto(nombre,precio,cantidad,total) );
           }
          DefaultTableModel modelo = new DefaultTableModel(fila,titulos);
          this.jTable1.setModel(modelo);
    }
    public boolean validadVacio(){
        boolean respuesta = true;
        this.asignarProductoTexto();
        if(this.n_producto.isEmpty()){
            JOptionPane.showMessageDialog(null, "El campo nombre del producto esta vacío","Campos Vacíos",JOptionPane.WARNING_MESSAGE);
            respuesta = false;
        }else if(this.p_producto.isEmpty()){
            JOptionPane.showMessageDialog(null,"El campo precio del producto esta vacío","Campos Vacíos",JOptionPane.WARNING_MESSAGE);
            respuesta = false;
        }else if(this.c_producto.isEmpty()){
            JOptionPane.showMessageDialog(null,"El campo cantidad del producto esta vacío","Campos Vacíos",JOptionPane.WARNING_MESSAGE);
            respuesta = false;
        }else{
            respuesta = true;
        }
        return respuesta;
    }
    public String obtenerFechaActual(){
       Calendar c1 = Calendar.getInstance();
       return Integer.toString(c1.get(Calendar.DATE))+"-"+(Integer.toString(c1.get(Calendar.MONTH))+1)+"-"+Integer.toString(c1.get(Calendar.YEAR));
   }
    public void generarPDF(){
        try {
            JasperReport loadObject = (JasperReport) JRLoader.loadObject(Cotizacion.class.getResource("../theme/cotizacion.jasper"));
            Map parameters = new HashMap<String, Object>();
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(productos); 
            System.out.println(getClass().getResource("../icono/iconoclinica.gif"));
            parameters.put("logo_empresa", ""+getClass().getResource("../icono/iconoclinica.gif"));
            parameters.put("nombre_empresa", "V & R");
            parameters.put("no_factura", this.CotizacionID);
            parameters.put("eslogan_empresa", "SERVICIOS DE HERRAMIENTAS");
            parameters.put("fecha", this.obtenerFechaActual());
            
            parameters.put("numero_cliente", this.ClienteID);
            parameters.put("nombre_cliente", this.t_nombre_cliente.getText());
            /*parameters.put("no_factura", this.idFactura);*/
            parameters.put("sub_total","$ "+this.subMontoTotal);
            parameters.put("monto_total","$ "+this.montoTotal);
            parameters.put("itbis", "$ "+this.itbisTotal);

            
            JasperPrint jp = JasperFillManager.fillReport(loadObject, parameters, ds);           
            JasperViewer jv = new JasperViewer(jp,false);
            jv.setDefaultCloseOperation(JasperViewer.DISPOSE_ON_CLOSE);
            jv.setVisible(true);
                    
        } catch (JRException ex) {
            Logger.getLogger(Cotizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void administrar_focus_clientes(String menu,String tabla){
        SeleccionLista selecciona = new SeleccionLista(this.mysql,tabla);
         selecciona.setObjectCotizacion(this.ObjectCotizacion);
         selecciona.setSeleccionLista(selecciona);
        //JOptionPane.showMessageDialog(null, menu+" "+tabla);
         switch(menu){
            case "codigo_cliente":
                Texto.placeholder(Texto.codigo_cliente,this.t_codigo_cliente.getText(), this.t_codigo_cliente);
                selecciona.setTextBox(t_codigo_cliente);
                selecciona.setPalabra("codigo");
                
            break;
            case "cedula_cliente":
                Texto.placeholder(Texto.cedula_cliente,this.t_cedula_cliente.getText(), this.t_cedula_cliente);
                selecciona.setTextBox(t_cedula_cliente);
                selecciona.setPalabra("cedula");
            break;
            case "nombre_cliente":
                Texto.placeholder(Texto.nombre_cliente,this.t_nombre_cliente.getText(), this.t_nombre_cliente);
                selecciona.setTextBox(t_nombre_cliente);
                selecciona.setPalabra("nombre");
            break;
            case "telefono_cliente":
                Texto.placeholder(Texto.telefono_cliente,this.t_telefono_cliente.getText(), this.t_telefono_cliente);
                selecciona.setTextBox(t_telefono_cliente);
                selecciona.setPalabra("telefono");
                
            break;
            case "email_cliente":
                Texto.placeholder(Texto.email_cliente,this.t_email_cliente.getText(), this.t_email_cliente);
                selecciona.setTextBox(t_email_cliente);
                selecciona.setPalabra("email");
            break;
            
            case "codigo_producto":
                
                Texto.placeholder(Texto.codigo_producto,this.t_codigo_producto.getText(), this.t_codigo_producto);
                selecciona.setTextBox(t_codigo_producto);
                selecciona.setPalabra("codigo");
            break;
            case "nombre_producto":
                Texto.placeholder(Texto.nombre_producto,this.t_nombre_producto.getText(), this.t_nombre_producto);
                //Texto.setPlaceholder(Texto.nombre_producto,this.t_nombre_producto.getText(), this.t_nombre_producto);
                selecciona.setTextBox(t_nombre_producto);
                selecciona.setPalabra("nombre");
            break;
        }
        
        
        selecciona.setVisible(true);
    }
    public boolean validarQueTengaUnoSeleccionado(){
        if(this.no_agrego_cliente)
        {
            JOptionPane.showMessageDialog(null, "Debe de agregar un cliente primero", "Falta Agregar Un Cliente", JOptionPane.WARNING_MESSAGE);
            return false;
        }else if(this.no_agrego_producto)
        {
            JOptionPane.showMessageDialog(null, "Debe de agregar un producto primero", "Falta Agregar Un Producto", JOptionPane.WARNING_MESSAGE);
            return false;
        }else{
            return true;
        }
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cotizacion_sub_total = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cotizacion_itbis_total = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cotizacion_monto_total = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        t_codigo_cliente = new javax.swing.JTextField();
        t_cedula_cliente = new javax.swing.JTextField();
        t_nombre_cliente = new javax.swing.JTextField();
        t_telefono_cliente = new javax.swing.JTextField();
        t_email_cliente = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        t_codigo_producto = new javax.swing.JTextField();
        t_nombre_producto = new javax.swing.JTextField();
        t_precio_producto = new javax.swing.JTextField();
        t_cantidad_producto = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Sub Total $");

        jLabel3.setText("Itbis Total $");

        jLabel4.setText("Monto Total $");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cotizacion_sub_total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cotizacion_monto_total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cotizacion_itbis_total, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cotizacion_sub_total, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cotizacion_itbis_total, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cotizacion_monto_total, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TOTALES");

        t_codigo_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_codigo_clienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_codigo_clienteFocusLost(evt);
            }
        });

        t_cedula_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_cedula_clienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_cedula_clienteFocusLost(evt);
            }
        });

        t_nombre_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_nombre_clienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_nombre_clienteFocusLost(evt);
            }
        });

        t_telefono_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_telefono_clienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_telefono_clienteFocusLost(evt);
            }
        });

        t_email_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_email_clienteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_email_clienteFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(t_codigo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_cedula_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_nombre_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_telefono_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_email_cliente))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(t_codigo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_cedula_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_nombre_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_telefono_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_email_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        t_codigo_producto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_codigo_productoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_codigo_productoFocusLost(evt);
            }
        });

        t_nombre_producto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_nombre_productoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_nombre_productoFocusLost(evt);
            }
        });

        t_precio_producto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_precio_productoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_precio_productoFocusLost(evt);
            }
        });

        t_cantidad_producto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_cantidad_productoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_cantidad_productoFocusLost(evt);
            }
        });

        jButton1.setText("Agregar Producto");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(t_codigo_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_nombre_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_precio_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_cantidad_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(t_codigo_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_nombre_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_precio_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_cantidad_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton1))
        );

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
        jScrollPane1.setViewportView(jTable1);

        jButton2.setText("VISTA PREVIA");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setText("COTIZAR");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        this.agregarTabla();
    }//GEN-LAST:event_jButton1MouseClicked

    private void t_codigo_productoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_codigo_productoFocusGained
        // TODO add your handling code here:
        //Texto.placeholder(Texto.codigo_producto,this.t_codigo_producto.getText(), this.t_codigo_producto);
               administrar_focus_clientes("codigo_producto","producto");

    }//GEN-LAST:event_t_codigo_productoFocusGained

    private void t_codigo_productoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_codigo_productoFocusLost
        // TODO add your handling code here:
        Texto.setPlaceholder(Texto.codigo_producto,this.t_codigo_producto.getText(), this.t_codigo_producto);
    }//GEN-LAST:event_t_codigo_productoFocusLost

    private void t_nombre_productoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_nombre_productoFocusGained
        // TODO add your handling code here:
        //Texto.placeholder(Texto.nombre_producto,this.t_nombre_producto.getText(), this.t_nombre_producto);
        administrar_focus_clientes("nombre_producto","producto");

    }//GEN-LAST:event_t_nombre_productoFocusGained

    private void t_nombre_productoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_nombre_productoFocusLost
        // TODO add your handling code here:
        Texto.setPlaceholder(Texto.nombre_producto,this.t_nombre_producto.getText(), this.t_nombre_producto);
    }//GEN-LAST:event_t_nombre_productoFocusLost

    private void t_precio_productoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_precio_productoFocusGained
        // TODO add your handling code here:
        Texto.placeholder(Texto.precio_producto,this.t_precio_producto.getText(), this.t_precio_producto);
    }//GEN-LAST:event_t_precio_productoFocusGained

    private void t_precio_productoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_precio_productoFocusLost
        // TODO add your handling code here:
        Texto.setPlaceholder(Texto.precio_producto,this.t_precio_producto.getText(), this.t_precio_producto);
    }//GEN-LAST:event_t_precio_productoFocusLost

    private void t_cantidad_productoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_cantidad_productoFocusGained
        // TODO add your handling code here:
      Texto.placeholder(Texto.cantidad_producto,this.t_cantidad_producto.getText(), this.t_cantidad_producto);
    }//GEN-LAST:event_t_cantidad_productoFocusGained

    private void t_cantidad_productoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_cantidad_productoFocusLost
        // TODO add your handling code here:
      Texto.setPlaceholder(Texto.cantidad_producto,this.t_cantidad_producto.getText(), this.t_cantidad_producto);
    }//GEN-LAST:event_t_cantidad_productoFocusLost

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        if(this.validarQueTengaUnoSeleccionado()){
            
             this.generarPDF();
        }
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
        if(this.validarQueTengaUnoSeleccionado()){
            this.crearCotizacion();
        }
    }//GEN-LAST:event_jButton3MouseClicked

    private void t_codigo_clienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_codigo_clienteFocusGained
        // TODO add your handling code here:
       /* Texto.placeholder(Texto.codigo_cliente,this.t_codigo_cliente.getText(), this.t_codigo_cliente);
        SeleccionLista selecciona = new SeleccionLista(this.mysql);
        selecciona.setObjectCotizacion(this.ObjectCotizacion);
        selecciona.setTextBox(t_codigo_cliente);
        selecciona.setPalabra("codigo");
        selecciona.setVisible(true);*/
       administrar_focus_clientes("codigo_cliente","cliente");
    }//GEN-LAST:event_t_codigo_clienteFocusGained

    private void t_codigo_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_codigo_clienteFocusLost
        // TODO add your handling code here:
         Texto.setPlaceholder(Texto.codigo_cliente,this.t_codigo_cliente.getText(), this.t_codigo_cliente);
    
    }//GEN-LAST:event_t_codigo_clienteFocusLost

    private void t_cedula_clienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_cedula_clienteFocusGained
        // TODO add your handling code here:
       //Texto.placeholder(Texto.cedula_cliente,this.t_cedula_cliente.getText(), this.t_cedula_cliente);
       administrar_focus_clientes("cedula_cliente","cliente");
    }//GEN-LAST:event_t_cedula_clienteFocusGained

    private void t_cedula_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_cedula_clienteFocusLost
        // TODO add your handling code here:
         Texto.setPlaceholder(Texto.cedula_cliente,this.t_cedula_cliente.getText(), this.t_cedula_cliente);
     
    }//GEN-LAST:event_t_cedula_clienteFocusLost

    private void t_nombre_clienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_nombre_clienteFocusGained
        // TODO add your handling code here:
       Texto.placeholder(Texto.nombre_cliente,this.t_nombre_cliente.getText(), this.t_nombre_cliente);
       //Texto.setPlaceholder(Texto.nombre_cliente,this.t_nombre_cliente.getText(), this.t_nombre_cliente);
       administrar_focus_clientes("nombre_cliente","cliente");
    }//GEN-LAST:event_t_nombre_clienteFocusGained

    private void t_nombre_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_nombre_clienteFocusLost
        // TODO add your handling code here:
        //Texto.placeholder(Texto.nombre_cliente,this.t_nombre_cliente.getText(), this.t_nombre_cliente);
       Texto.setPlaceholder(Texto.nombre_cliente,this.t_nombre_cliente.getText(), this.t_nombre_cliente);
   
    }//GEN-LAST:event_t_nombre_clienteFocusLost

    private void t_telefono_clienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_telefono_clienteFocusGained
        // TODO add your handling code here:
        Texto.placeholder(Texto.telefono_cliente,this.t_telefono_cliente.getText(), this.t_telefono_cliente);
       //Texto.setPlaceholder(Texto.telefono_cliente,this.t_telefono_cliente.getText(), this.t_telefono_cliente);
       administrar_focus_clientes("telefono_cliente","cliente");
    }//GEN-LAST:event_t_telefono_clienteFocusGained

    private void t_telefono_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_telefono_clienteFocusLost
        // TODO add your handling code here:
        //Texto.placeholder(Texto.telefono_cliente,this.t_telefono_cliente.getText(), this.t_telefono_cliente);
       Texto.setPlaceholder(Texto.telefono_cliente,this.t_telefono_cliente.getText(), this.t_telefono_cliente);
   
    }//GEN-LAST:event_t_telefono_clienteFocusLost

    private void t_email_clienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_email_clienteFocusGained
        // TODO add your handling code here:
        Texto.placeholder(Texto.email_cliente,this.t_email_cliente.getText(), this.t_email_cliente);
       //Texto.setPlaceholder(Texto.email_cliente,this.t_email_cliente.getText(), this.t_email_cliente);
       administrar_focus_clientes("email_cliente","cliente");
    }//GEN-LAST:event_t_email_clienteFocusGained

    private void t_email_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_email_clienteFocusLost
        // TODO add your handling code here:
        //Texto.placeholder(Texto.email_cliente,this.t_email_cliente.getText(), this.t_email_cliente);
       Texto.setPlaceholder(Texto.email_cliente,this.t_email_cliente.getText(), this.t_email_cliente);
   
    }//GEN-LAST:event_t_email_clienteFocusLost

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cotizacion_itbis_total;
    private javax.swing.JLabel cotizacion_monto_total;
    private javax.swing.JLabel cotizacion_sub_total;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField t_cantidad_producto;
    private javax.swing.JTextField t_cedula_cliente;
    private javax.swing.JTextField t_codigo_cliente;
    private javax.swing.JTextField t_codigo_producto;
    private javax.swing.JTextField t_email_cliente;
    private javax.swing.JTextField t_nombre_cliente;
    private javax.swing.JTextField t_nombre_producto;
    private javax.swing.JTextField t_precio_producto;
    private javax.swing.JTextField t_telefono_cliente;
    // End of variables declaration//GEN-END:variables
}
