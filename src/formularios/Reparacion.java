/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import conexion.Mysql;
import java.sql.SQLException;
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
public class Reparacion extends javax.swing.JFrame {

    /**
     * Creates new form Reparacion
     */
    private ArrayList<String> producto_id = new ArrayList<String>();
    private ArrayList<String> sub_total_producto = new ArrayList<String>();
    private ArrayList<String> codigo_producto = new ArrayList<String>();
    private ArrayList<String> nombre_producto = new ArrayList<String>();
    private ArrayList<String> precio_producto = new ArrayList<String>();
    private ArrayList<String> cantidad_producto = new ArrayList<String>();
    private ArrayList<String> nota_producto = new ArrayList<String>();
    private ArrayList<String> estado_producto = new ArrayList<String>();
    private ArrayList<String> precio_completo_producto  = new ArrayList<String>();
                
    private double montoTotal = 0.00,subMontoTotal = 0.00,abonoTotal = 0.00;

    public void resetArrayList(){
        this.producto_id = new ArrayList<String>();
        this.sub_total_producto = new ArrayList<String>();
        this.codigo_producto = new ArrayList<String>();
        this.nombre_producto = new ArrayList<String>();
        this.precio_producto = new ArrayList<String>();
        this.cantidad_producto = new ArrayList<String>();
        this.nota_producto = new ArrayList<String>();
        this.estado_producto = new ArrayList<String>();
        this.precio_completo_producto = new ArrayList<String>();
    }
    private List productos;
    
    private String no_producto,n_producto,p_producto,c_producto,co_producto, sub_total,itbis_total, monto_total;
    private boolean factura_generada = false,deuda = false,mostrarPDF = true;
    
    private boolean existe_cliente = false,existe_producto= false,cotizado = false,no_agrego_producto = true,no_agrego_cliente = true;
    private Mysql mysql;
    
    private String rnc_cliente="";
    private String estado_productot="",precio_completo_productot="";
    
    private String ClienteID="1",UsuarioID="1",ReparacionID="1",productoID="1";
    
    private String NCF = "",notaReparacion="";
    
    private Reparacion ObjectReparacion;
    
    public Reparacion() {
        initComponents();
        //this.limpiarProductoTexto();
        this.limpiarTodoTexto();
        mysql =new Mysql();
//        this.jTDeuda.setVisible(false);
        this.jTReparacion.setVisible(false);
  //      this.jTCotizacion.setVisible(false);
        
    }
    
    public void setObjectReparacion(Reparacion c){
        this.ObjectReparacion = c;
    }
    public void setDatosCliente(String ClienteID,String nombre,String cedula,String telefono,String email){
            this.ClienteID = ClienteID;
            this.t_codigo_cliente.setText(this.ClienteID);
            this.t_nombre_cliente.setText(nombre);
            this.t_cedula_cliente.setText(cedula);
            this.t_telefono_cliente.setText(telefono);
            this.t_email_cliente.setText(email);
            this.no_agrego_cliente = false;
            //this.optenerCotizacionPendienteFacturarDelCliente();
            this.optenerReparacionesPendienteFacturarDelCliente();
            //this.optenerDeudasPendienteFacturarDelCliente();
    }
    public void setRNCCliente(String rnc){
        this.rnc_cliente = rnc;
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
        this.no_producto = this.jTANotaReparacion.getText();
    }
    public void limpiarProductoTexto(){
        this.t_nombre_producto.setText(Texto.nombre_producto);
        this.t_precio_producto.setText(Texto.precio_producto);
        this.t_cantidad_producto.setText(Texto.cantidad_producto);
        this.t_codigo_producto.setText(Texto.codigo_producto);
        this.jTANotaReparacion.setText(Texto.nota_producto);

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
    public void optenerReparacionesPendienteFacturarDelCliente(){
            
            String[] titulos = {"CODIGO REPARACION","ESTADO REPARACION","MONTO A PAGAR","CANTIDAD REPARACION","FECHA REPARACION"};
            String table_name = " reparacion as c";
            String campos = "c.id,c.sub_total,c.fecha_creada,c.cliente_id,c.estado, (SELECT count(c1.id) FROM reparacion_detalle as c1 WHERE c1.reparacion_id = c.id) as cantidad ";
            String otros = " where c.cliente_id = '"+this.ClienteID+"' ";
            //SELECT c.id,c.sub_total,c.fecha_creada,c.cliente_id,c.estado, (SELECT count(c1.id) FROM reparacion_detalle as c1 WHERE c1.reparacion_id = c.id) as cantidad FROM reparacion as c

           java.sql.ResultSet resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
        try {
            resultSet.beforeFirst();
            resultSet.last();
            int totalFila  = resultSet.getRow();
            Object[][] fila = new Object[totalFila][5];
            String codigo,estado,fecha,monto,cantidad;
            resultSet.beforeFirst();
            int c = 0;
            while(resultSet.next()){

                     codigo = Texto.validarNull(resultSet.getString("id"));
                     estado = Texto.validarNull(resultSet.getString("sub_total"));
                     monto = Texto.validarNull(resultSet.getString("estado"));
                     fecha =Texto.validarNull(resultSet.getString("fecha_creada"));
                      cantidad =Texto.validarNull(resultSet.getString("cantidad"));

                     fila[c][0] = codigo;
                     fila[c][1] = estado;
                     fila[c][2] = monto;
                     fila[c][3] = cantidad;
                     fila[c][4] = fecha;
                     c++;
                     
            } 
             DefaultTableModel modelo = new DefaultTableModel(fila,titulos);
             this.jTReparacion.setModel(modelo);
             this.jTReparacion.setVisible(true);    
        } catch (SQLException ex) {
            Logger.getLogger(SeleccionLista.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
     public void optenerCotizacionPendienteFacturarDelCliente(){
            
            String[] titulos = {"CODIGO COTIZACION","ESTADO COTIZACION","MONTO A PAGAR COTIZACION","CANTIDAD COTIZACION","FECHA COTIZACION"};
            String table_name = " cotizacion as c ";
            String campos = "c.id,c.sub_total,c.fecha_creada,c.cliente_id,c.estado, (SELECT count(c1.id) FROM cotizacion_detalle as c1 WHERE c1.cotizacion_id = c.id) as cantidad ";
            String otros = " where c.cliente_id = '"+this.ClienteID+"' ";
            //SELECT c.id,c.sub_total,c.fecha_creada,c.cliente_id,c.estado, (SELECT count(c1.id) FROM cotizacion_detalle as c1 WHERE c1.cotizacion_id = c.id) as cantidad FROM cotizacion as c
           java.sql.ResultSet resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
        try {
            resultSet.beforeFirst();
            resultSet.last();
            int totalFila  = resultSet.getRow();
            Object[][] fila = new Object[totalFila][5];
            String codigo,estado,fecha,monto,cantidad;
            resultSet.beforeFirst();
            int c = 0;
            while(resultSet.next()){

                     codigo = Texto.validarNull(resultSet.getString("id"));
                     estado = Texto.validarNull(resultSet.getString("sub_total"));
                     monto = Texto.validarNull(resultSet.getString("estado"));
                     fecha =Texto.validarNull(resultSet.getString("fecha_creada"));                     
                     cantidad =Texto.validarNull(resultSet.getString("cantidad"));

                     fila[c][0] = codigo;
                     fila[c][1] = estado;
                     fila[c][2] = monto;
                     fila[c][3] = cantidad;
                     fila[c][4] = fecha;
                     c++;
                     
            } 
             DefaultTableModel modelo = new DefaultTableModel(fila,titulos);
//             this.jTCotizacion.setModel(modelo);
  ///           this.jTCotizacion.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(SeleccionLista.class.getName()).log(Level.SEVERE, null, ex);
        } 
    
    
    
    }
     
     public void cargarTablaParaFacturar(String id,String accion){
          this.no_agrego_producto = false;
          this.resetArrayList();
          this.resetTotatel();
          this.ReparacionID = "1";
            java.sql.ResultSet resultSet = null;
            this.deuda = false; 
            if(accion.equals("deuda")){
                String table_name = "  factura_detalle as c ";
                String campos = "c.id,c.nombre,c.fecha_creada,c.precio,c.cantidad, c.total,c.factura_id,c.cliente_id";
                String otros = " where c.factura_id = '"+id+"'";
                resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
                this.deuda = true;
                this.factura_generada = true;
            }else if(accion.equals("cotizacion")){
                String table_name = "  cotizacion_detalle as c ";
                String campos = "c.id,c.nombre,c.fecha_creada,c.precio,c.cantidad, c.total,c.cotizacion_id,c.cliente_id";
                String otros = " where c.cotizacion_id = '"+id+"'";
                resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
            }else if(accion.equals("reparacion")){
                String table_name = "  reparacion_detalle as c ";
                String campos = "c.nota,c.estado,c.precio_completado,c.precio , c.id,c.nombre,c.fecha_creada,c.cantidad, c.total,c.precio_completado,c.reparacion_id,c.cliente_id,if(c.precio_completado is null or c.precio_completado = 0.00 , c.precio, c.precio_completado ) as precio1";
                String otros = " where c.reparacion_id = '"+id+"'";
                //SELECT if(precio_completado is null or precio_completado = 0.00 , precio, precio_completado ) as precio FROM `reparacion_detalle`
                resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
                this.deuda = true;
                this.factura_generada = true;
            }
        try {
            if(resultSet != null){
                while(resultSet.next()){
                         this.productoID = Texto.validarNull(resultSet.getString("id"));
                         this.c_producto = Texto.validarNull(resultSet.getString("cantidad"));
                         this.n_producto = Texto.validarNull(resultSet.getString("nombre"));
                         this.p_producto =Texto.validarNull(resultSet.getString("precio"));                     
                         this.co_producto =Texto.validarNull(resultSet.getString("id"));
                         this.no_producto =Texto.validarNull(resultSet.getString("nota"));
                         this.estado_productot =Texto.validarNull(resultSet.getString("estado"));
                         this.precio_completo_productot =Texto.validarNull(resultSet.getString("precio_completado"));
                         if(this.deuda){
                             this.ReparacionID = Texto.validarNull(resultSet.getString("reparacion_id"));
                         }
                         this.asignarAArrayList();
                } 
            }
        } catch (SQLException ex) {
            Logger.getLogger(SeleccionLista.class.getName()).log(Level.SEVERE, null, ex);
        }  
        this.cargarJTable();
     }
    public void optenerDeudasPendienteFacturarDelCliente(){
            
            String[] titulos = {"CODIGO FACTURA","ESTADO FACTURA","MONTO FACTURA","MONTO PAGADO FACTURA","FECHA FACTURA"};
            String table_name = "  factura as c ";
            String campos = "c.id,c.total,c.fecha_creada,c.cliente_id,c.estado, (SELECT SUM(c1.monto_pagado) FROM pagos as c1 WHERE c1.factura_id = c.id) as monto_pagado ";
            String otros = " where c.cliente_id = '"+this.ClienteID+"' and c.estado = 'pendiente' ";
            //SELECT c.id,c.total,c.fecha_creada,c.cliente_id,c.estado, (SELECT SUM(c1.monto_pagado) FROM pagos as c1 WHERE c1.factura_id = c.id) as monto_pagado FROM factura as c
            java.sql.ResultSet resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
        try {
            resultSet.beforeFirst();
            resultSet.last();
            int totalFila  = resultSet.getRow();
            Object[][] fila = new Object[totalFila][5];
            String codigo,estado,fecha,monto,cantidad;
            resultSet.beforeFirst();
            int c = 0;
            while(resultSet.next()){

                     codigo = Texto.validarNull(resultSet.getString("id"));
                     estado = Texto.validarNull(resultSet.getString("total"));
                     monto = Texto.validarNull(resultSet.getString("estado"));
                     fecha =Texto.validarNull(resultSet.getString("fecha_creada"));                     
                     cantidad =Texto.validarNull(resultSet.getString("monto_pagado"));

                     fila[c][0] = codigo;
                     fila[c][2] = estado;
                     fila[c][1] = monto;
                     fila[c][3] = cantidad;
                     fila[c][4] = fecha;
                     c++;
                     
            } 
             DefaultTableModel modelo = new DefaultTableModel(fila,titulos);
//             this.jTDeuda.setModel(modelo);
  //           this.jTDeuda.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(SeleccionLista.class.getName()).log(Level.SEVERE, null, ex);
        } 
    
    
    
    }
    public void insertarDBDetalleReparacion(String id,String nombre,String precio,String cantidad,String total,String nota){
            if(!this.existe_producto){
             String campos = "usuario_id,nombre,precio,cantidad,total,fecha_creada,cliente_id,reparacion_id,producto_inventariado_id,nota";
             String valores = "'"+this.UsuarioID+"','"+nombre+"','"+precio+"','"+cantidad+"','"+total+"',now(),'"+this.ClienteID+"','"+this.ReparacionID+"','"+id+"','"+nota+"'";
             this.mysql.insertData("reparacion_detalle", campos, valores);
            }
    }
    public void insertarDBReparacion(String sub_total,String nota,double abono){
             String campos = "usuario_id,sub_total,fecha_creada,cliente_id,nota,abono";
             String valores = "'"+this.UsuarioID+"','"+sub_total+"',now(),'"+this.ClienteID+"','"+nota+"','"+abono+"' ";
             this.mysql.insertData("reparacion", campos, valores);
             this.ReparacionID = this.mysql.optenerUltimoID("reparacion");
    }
    public void completarReparacion(){
        boolean respuesta = this.mysql.actulizarDatos("reparacion","estado = 'completo' ", " id = '"+this.ReparacionID+"' ");
        if(respuesta){
            JOptionPane.showMessageDialog(null, "La reparación paso al estado de completado ","Reparacioón Completada",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void crearReparacion(){
           if(!this.cotizado && (!this.deuda) ){
            this.insertarDBReparacion(this.subMontoTotal+"", this.notaReparacion,this.abonoTotal);
            int lineas = this.nombre_producto.size();
            String nombre,precio,cantidad,total,id,nota;
            for(int c = 0 ; c < lineas ; c++){
                     nombre = this.nombre_producto.get(c);
                     precio = this.precio_producto.get(c);
                     cantidad = this.cantidad_producto.get(c);
                     total = this.sub_total_producto.get(c);
                     nota = this.nota_producto.get(c);
                    id = this.producto_id.get(c);
                    this.insertarDBDetalleReparacion(id,nombre, precio, cantidad, total,nota);
            }
            this.cotizado = true;
           }
           if(this.mostrarPDF){
           this.generarPDF();
           }
    }
    public void reCalcular(){
           int lineas = this.nombre_producto.size();
            String nombre,precio,cantidad,total,id;
            for(int c = 0 ; c < lineas ; c++){
                     nombre = this.nombre_producto.get(c);
                     precio = this.precio_producto.get(c);
                     cantidad = this.cantidad_producto.get(c);
                     total = this.sub_total_producto.get(c);
                     id = this.producto_id.get(c);
             double  monto = Double.parseDouble(total);
                     this.totales(monto);
            }
    }
    public void asignarAArrayList(){
        this.producto_id.add(this.productoID);
        this.cantidad_producto.add(this.c_producto);
        this.nombre_producto.add(this.n_producto);
        this.precio_producto.add(""+this.p_producto);
        this.codigo_producto.add(this.co_producto);
        this.nota_producto.add(this.no_producto);
        this.estado_producto.add(this.estado_productot);
        this.precio_completo_producto.add(this.precio_completo_productot);
        double monto =(Double.parseDouble(c_producto) * Double.parseDouble(p_producto) );
        this.sub_total_producto.add( ""+monto );
        this.totales(monto);
    }
    public void agregarTabla(){
        this.validarAgregarEntradaProducto();
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
        Texto.placeholder(Texto.nota_producto,this.jTANotaReparacion.getText(), this.jTANotaReparacion);
        
    }
   
    public void totales(double monto){
        double itbis = 0,monto_total = 0;
        this.sub_total = ""+monto;
        
        monto_total = monto ; 
        this.montoTotal +=monto_total;
        //this.itbisTotal += itbis;
        this.subMontoTotal += monto;
        this.itbis_total = ""+itbis;
        this.monto_total = ""+monto_total;
        this.cotizacion_itbis_total.setText("$ 0.00"/*+this.itbisTotal*/);
        this.cotizacion_sub_total.setText("$ "+this.subMontoTotal);
        this.cotizacion_monto_total.setText("$ "+this.montoTotal);
    }
    public void resetTotatel(){
         this.montoTotal = 0.00;
        //this.itbisTotal = 0.00;
        this.subMontoTotal = 0.00;
    }
    public void cargarJTable(){
           String[] titulos = {"CODIGO","PRODUCTO","CANTIDAD","PRECIO","SUB TOTAL","PRECIO COMPLETADO","ESTADO","NOTA"};
           int lineas = this.nombre_producto.size();
           String nombre,precio,cantidad,total,id,nota,precioCompleto,estado;
           
           this.productos  = new LinkedList();
           
           Object[][] fila = new Object[lineas][8];
           
           for(int c = 0 ; c < lineas ; c++){
                    id = this.codigo_producto.get(c);
                    nombre = this.nombre_producto.get(c);
                    precio = this.precio_producto.get(c);
                    cantidad = this.cantidad_producto.get(c);
                    total = this.sub_total_producto.get(c);
                    nota = this.nota_producto.get(c);
                    estado = this.estado_producto.get(c);
                    precioCompleto = this.precio_completo_producto.get(c);
                    fila[c][0] = id;
                    fila[c][1] = nombre;
                    fila[c][2] = cantidad;
                    fila[c][3] = precio;
                    fila[c][4] = total;
                    fila[c][5] = estado;
                    fila[c][6] = precioCompleto;
                    
                    fila[c][7] = nota;
                    
                    this.productos.add( new producto(nombre,precio,cantidad,total) );
           }
          DefaultTableModel modelo = new DefaultTableModel(fila,titulos);
          this.jTableR.setModel(modelo);
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
            JasperReport loadObject = (JasperReport) JRLoader.loadObject(Reparacion.class.getResource("../theme/Reparacion.jasper"));
            Map parameters = new HashMap<String, Object>();
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(productos); 
            //System.out.println(getClass().getResource("../icono/iconoclinica.gif"));
            parameters.put("logo_empresa", ""+getClass().getResource(Empresa.logo));
            parameters.put("nombre_empresa", Empresa.nombre);
            parameters.put("eslogan_empresa", Empresa.eslogan);

            parameters.put("ncf_empresa", this.NCF);
            parameters.put("rnc_empresa", Empresa.rnc);
            parameters.put("telefono_empresa", Empresa.telefono);
            parameters.put("celular_empresa", Empresa.celular);
            parameters.put("email_empresa", Empresa.email);

            parameters.put("no_factura", this.ReparacionID);
            parameters.put("fecha", this.obtenerFechaActual());
            
            
            parameters.put("rnc_cliente", this.rnc_cliente);
            parameters.put("numero_cliente", this.ClienteID);
            parameters.put("nombre_cliente", this.t_nombre_cliente.getText());
            /*parameters.put("no_factura", this.idFactura);*/
            parameters.put("sub_total","$ "+this.subMontoTotal);
            parameters.put("monto_total","$ "+this.montoTotal);
            parameters.put("itbis", "$ 0.00"/*+this.itbisTotal*/);

            
            JasperPrint jp = JasperFillManager.fillReport(loadObject, parameters, ds);           
            JasperViewer jv = new JasperViewer(jp,false);
            jv.setDefaultCloseOperation(JasperViewer.DISPOSE_ON_CLOSE);
            jv.setVisible(true);
                    
        } catch (JRException ex) {
            Logger.getLogger(Reparacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void administrar_focus_clientes(String menu,String tabla){
        SeleccionLista selecciona = new SeleccionLista(this.mysql,tabla);
         selecciona.setObjectReparacion(this.ObjectReparacion);
         selecciona.validarReparacion();
        
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        RegistrarPaso = new javax.swing.JMenuItem();
        Historial = new javax.swing.JMenuItem();
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
        jTableR = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        JPReparacion = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTReparacion = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTANotaReparacion = new javax.swing.JTextArea();

        RegistrarPaso.setText("Registrar Avances");
        RegistrarPaso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                RegistrarPasoMousePressed(evt);
            }
        });
        jPopupMenu1.add(RegistrarPaso);

        Historial.setText("Detalle");
        jPopupMenu1.add(Historial);

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
                    .addComponent(cotizacion_sub_total, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cotizacion_monto_total, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cotizacion_itbis_total, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cotizacion_sub_total, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cotizacion_itbis_total, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cotizacion_monto_total, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jTableR.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableR.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(jTableR);

        jButton2.setText("VISTA PREVIA");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        jButton3.setText("Facturar");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jButton4.setText("Abonar");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("REPARACIONES PENDIENTES DEL CLIENTE");

        jButton5.setText("Agregar Nota");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        jButton6.setText("Reparar");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });

        jTReparacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTReparacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTReparacionMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTReparacion);

        javax.swing.GroupLayout JPReparacionLayout = new javax.swing.GroupLayout(JPReparacion);
        JPReparacion.setLayout(JPReparacionLayout);
        JPReparacionLayout.setHorizontalGroup(
            JPReparacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
        );
        JPReparacionLayout.setVerticalGroup(
            JPReparacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("REPARACIONES", JPReparacion);

        jTANotaReparacion.setColumns(20);
        jTANotaReparacion.setRows(5);
        jTANotaReparacion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTANotaReparacionFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTANotaReparacionFocusLost(evt);
            }
        });
        jScrollPane5.setViewportView(jTANotaReparacion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTabbedPane1)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3)
                        .addComponent(jButton2)
                        .addComponent(jButton4)
                        .addComponent(jButton5))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        this.agregarTabla();
        this.deuda = false;
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
        /*if(this.validarQueTengaUnoSeleccionado()){
            this.crearReparacion();
            this.factura_generada = true;
        }*/
        if(this.validarQueTengaUnoSeleccionado()){
            this.mostrarPDF  = false;
            this.crearReparacion();
            this.factura_generada = true;
            this.mostrarPDF = true;
            this.completarReparacion();
            
            Facturacion f = new Facturacion();
            
            f.setDatosCliente(this.ClienteID, this.t_nombre_cliente.getText(), this.t_cedula_cliente.getText(), this.t_telefono_cliente.getText(),this.t_email_cliente.getText());
            f.cargarTablaParaFacturar(this.ReparacionID,"reparacion");
            
            JOptionPane.showMessageDialog(null, "Ha pasado a FACTURACIÓN si no desea hacer cambios, clic en facturar","Generar Factura",JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            f.setVisible(true);
      }
    }//GEN-LAST:event_jButton3MouseClicked

    private void t_codigo_clienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_codigo_clienteFocusGained
        // TODO add your handling code here:
       /* Texto.placeholder(Texto.codigo_cliente,this.t_codigo_cliente.getText(), this.t_codigo_cliente);
        SeleccionLista selecciona = new SeleccionLista(this.mysql);
        selecciona.setObjectReparacion(this.ObjectReparacion);
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

    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseClicked
        // TODO add your handling code here:
      // this.NCF = JOptionPane.showInputDialog(null, "Ingrese su NCF (Número de Comprobante Fiscal)", "NCF (Número de Comprobante Fiscal)", JOptionPane.INFORMATION_MESSAGE);
      if(this.validarQueTengaUnoSeleccionado()){
            this.crearReparacion();
            this.factura_generada = true;
      }
    }//GEN-LAST:event_jButton6MouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        try{
            this.abonoTotal =  Double.parseDouble(JOptionPane.showInputDialog(null, "Ingrese su Abono", "Agregar Abono a la Reparación", JOptionPane.INFORMATION_MESSAGE));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Debe de ingresar solo número","Solo se perminten números", JOptionPane.WARNING_MESSAGE);
        }
        // TODO add your handling code here:
       /* if(this.factura_generada){
            Pago p = new Pago(this.mysql);
            p.setObjectReparacion(this.ObjectReparacion);
            p.setBalance(this.monto_total);
            p.setVisible(true);
        }else{
            int respuesta  = JOptionPane.showConfirmDialog(  null,  "Debe generar la factura antes de pagarla, Desea generarla ahora?","Advertencia ",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
               // JOptionPane.showMessageDialog(null, "null");
                if(this.validarQueTengaUnoSeleccionado()){
                        this.crearReparacion();
                        this.factura_generada = true;
                }
                if(this.factura_generada){
                        Pago p = new Pago(this.mysql);
                        p.setObjectReparacion(this.ObjectReparacion);
                        p.setBalance(this.monto_total);
                        p.setVisible(true);
                }
            }
        }*/
    }//GEN-LAST:event_jButton4MouseClicked

    private void jTReparacionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTReparacionMouseClicked
        // TODO add your handling code here:
        int row =  this.jTReparacion.getSelectedRow();
        String id = this.jTReparacion.getValueAt(row, 0).toString();
        this.cargarTablaParaFacturar(id,"reparacion");
    }//GEN-LAST:event_jTReparacionMouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // TODO add your handling code here:
        this.notaReparacion = JOptionPane.showInputDialog(null, "Ingrese su Nota", "Agregar Nota a la Reparación", JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_jButton5MouseClicked

    private void jTANotaReparacionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTANotaReparacionFocusGained
        // TODO add your handling code here:
        Texto.placeholder(Texto.nota_producto,this.jTANotaReparacion.getText(), this.jTANotaReparacion);
         //Texto.setPlaceholder(Texto.email_cliente,this.t_email_cliente.getText(), this.t_email_cliente);
   
    }//GEN-LAST:event_jTANotaReparacionFocusGained

    private void jTANotaReparacionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTANotaReparacionFocusLost
        // TODO add your handling code here:
       // Texto.placeholder(Texto.nota_producto,this.jTANotaReparacion.getText(), this.jTANotaReparacion);
        Texto.setPlaceholder(Texto.nota_producto,this.jTANotaReparacion.getText(), this.jTANotaReparacion);
 
        //Texto.setPlaceholder(Texto.email_cliente,this.t_email_cliente.getText(), this.t_email_cliente);
   
    }//GEN-LAST:event_jTANotaReparacionFocusLost

    private void RegistrarPasoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RegistrarPasoMousePressed
        // TODO add your handling code here:
        this.pop();
    }//GEN-LAST:event_RegistrarPasoMousePressed
public void pop(){
    String r_detalle_id = "";
        int row = this.jTableR.getSelectedRow();
        r_detalle_id = this.jTableR.getValueAt(row, 0).toString();
        PasoReparacionSeguimiento pasos = new PasoReparacionSeguimiento(this.mysql);
        pasos.obtenerDatosDB(r_detalle_id);
        pasos.setVisible(true);
}
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Reparacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reparacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reparacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reparacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               Reparacion c = new Reparacion();
               c.setObjectReparacion(c);
               c.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Historial;
    private javax.swing.JPanel JPReparacion;
    private javax.swing.JMenuItem RegistrarPaso;
    private javax.swing.JLabel cotizacion_itbis_total;
    private javax.swing.JLabel cotizacion_monto_total;
    private javax.swing.JLabel cotizacion_sub_total;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextArea jTANotaReparacion;
    private javax.swing.JTable jTReparacion;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableR;
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
