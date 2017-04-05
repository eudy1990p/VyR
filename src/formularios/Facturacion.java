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
public class Facturacion extends javax.swing.JFrame {

    /**
     * Creates new form Facturacion
     */
    private ArrayList<String> producto_id = new ArrayList<String>();
    private ArrayList<String> sub_total_producto = new ArrayList<String>();
    private ArrayList<String> codigo_producto = new ArrayList<String>();
    private ArrayList<String> nombre_producto = new ArrayList<String>();
    private ArrayList<String> precio_producto = new ArrayList<String>();
    private ArrayList<String> cantidad_producto = new ArrayList<String>();
        private double montoTotal = 0.00,subMontoTotal = 0.00,itbisTotal = 0.00;

    public void resetArrayList(){
        this.producto_id = new ArrayList<String>();
        this.sub_total_producto = new ArrayList<String>();
        this.codigo_producto = new ArrayList<String>();
        this.nombre_producto = new ArrayList<String>();
        this.precio_producto = new ArrayList<String>();
        this.cantidad_producto = new ArrayList<String>();
    }
    private List productos;
    
    private String n_producto,p_producto,c_producto,co_producto, sub_total,itbis_total, monto_total;
    private boolean tiene_itbis = false,factura_generada = false,deuda = false;
    
    private boolean existe_cliente = false,existe_producto= false,cotizado = false,no_agrego_producto = true,no_agrego_cliente = true;
    private Mysql mysql;
    
    private String rnc_cliente="";
    
    private String ClienteID="1",UsuarioID="1",FacturacionID="1",productoID="1";
    
    private String NCF = "",notaFactura="";
    
    private Facturacion ObjectFacturacion;
    
    public Facturacion(Mysql mysql) {
        initComponents();
        //this.limpiarProductoTexto();
        this.limpiarTodoTexto();
        this.mysql = mysql;
        this.jTDeuda.setVisible(false);
        this.jTReparacion.setVisible(false);
        this.jTCotizacion.setVisible(false);
    }
    
    public void setObjectFacturacion(Facturacion c){
        this.ObjectFacturacion = c;
    }
    public void setDatosCliente(String ClienteID,String nombre,String cedula,String telefono,String email){
            this.ClienteID = ClienteID;
            this.t_codigo_cliente.setText(this.ClienteID);
            this.t_nombre_cliente.setText(nombre);
            this.t_cedula_cliente.setText(cedula);
            this.t_telefono_cliente.setText(telefono);
            this.t_email_cliente.setText(email);
            this.no_agrego_cliente = false;
            this.optenerCotizacionPendienteFacturarDelCliente();
            this.optenerReparacionesPendienteFacturarDelCliente();
            this.optenerDeudasPendienteFacturarDelCliente();
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
             this.jTCotizacion.setModel(modelo);
             this.jTCotizacion.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(SeleccionLista.class.getName()).log(Level.SEVERE, null, ex);
        } 
    
    
    
    }
     
     public void cargarTablaParaFacturar(String id,String accion){
          this.no_agrego_producto = false;
          this.resetArrayList();
          this.resetTotatel();
          this.FacturacionID = "1";
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
                String campos = "c.id,c.nombre,c.fecha_creada,c.cantidad, c.total,c.precio_completado,c.reparacion_id,c.cliente_id,if(c.precio_completado is null or c.precio_completado = 0.00 , c.precio, c.precio_completado ) as precio";
                String otros = " where c.reparacion_id = '"+id+"'";
                //SELECT if(precio_completado is null or precio_completado = 0.00 , precio, precio_completado ) as precio FROM `reparacion_detalle`
                resultSet = this.mysql.optenerDatosParaTabla(table_name, campos, otros);
            }
        try {
            if(resultSet != null){
                while(resultSet.next()){
                         this.productoID = Texto.validarNull(resultSet.getString("id"));
                         this.c_producto = Texto.validarNull(resultSet.getString("cantidad"));
                         this.n_producto = Texto.validarNull(resultSet.getString("nombre"));
                         this.p_producto =Texto.validarNull(resultSet.getString("precio"));                     
                         this.co_producto =Texto.validarNull(resultSet.getString("id"));
                         if(this.deuda){
                             this.FacturacionID = Texto.validarNull(resultSet.getString("factura_id"));
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
             this.jTDeuda.setModel(modelo);
             this.jTDeuda.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(SeleccionLista.class.getName()).log(Level.SEVERE, null, ex);
        } 
    
    
    
    }
    public void insertarDBDetalleFacturacion(String id,String nombre,String precio,String cantidad,String total){
            if(!this.existe_producto){
             String campos = "usuario_id,nombre,precio,cantidad,total,fecha_creada,cliente_id,factura_id,producto_inventariado_id";
             String valores = "'"+this.UsuarioID+"','"+nombre+"','"+precio+"','"+cantidad+"','"+total+"',now(),'"+this.ClienteID+"','"+this.FacturacionID+"','"+id+"'";
             this.mysql.insertData("factura_detalle", campos, valores);
            }
    }
    public void insertarDBFacturacion(String sub_total,String itbis,String tiene_itbis,String total){
            String tieneNCF = "0";
            if(!this.NCF.isEmpty()){
                tieneNCF = "1";
            }
            
            if(this.tiene_itbis){
                tiene_itbis = "1";
            }
            String campos = "usuario_id,sub_total,itbis,tiene_itbis,total,fecha_creada,cliente_id,numero_comprovante_fiscal,tiene_comprovante_fiscal";
             String valores = "'"+this.UsuarioID+"','"+sub_total+"','"+itbis+"','"+tiene_itbis+"','"+total+"',now(),'"+this.ClienteID+"', '"+this.NCF+"','"+tieneNCF+"' ";
             this.mysql.insertData("factura", campos, valores);
             this.FacturacionID = this.mysql.optenerUltimoID("factura");
    }
    public void crearFacturacion(){
           if(!this.cotizado && (!this.deuda) ){
            this.insertarDBFacturacion(this.subMontoTotal+"", this.itbisTotal+"","0", this.montoTotal+"");
            int lineas = this.nombre_producto.size();
            String nombre,precio,cantidad,total,id;
            for(int c = 0 ; c < lineas ; c++){
                     nombre = this.nombre_producto.get(c);
                     precio = this.precio_producto.get(c);
                     cantidad = this.cantidad_producto.get(c);
                     total = this.sub_total_producto.get(c);
                     id = this.producto_id.get(c);
                 this.insertarDBDetalleFacturacion(id,nombre, precio, cantidad, total);
            }
            this.cotizado = true;
           }
           this.generarPDF();
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
    public void tieneITBIS(){
        if(this.JCBTieneITBIS.isSelected()){
            this.tiene_itbis = true;
        }
    }
    public void totales(double monto){
        double itbis = 0,monto_total = 0;
        this.sub_total = ""+monto;
        this.tieneITBIS();
        if(this.tiene_itbis){
            itbis = monto * 0.18; 
        }
        monto_total = monto + itbis; 
        this.montoTotal +=monto_total;
        this.itbisTotal += itbis;
        this.subMontoTotal += monto;
        this.itbis_total = ""+itbis;
        this.monto_total = ""+monto_total;
        this.cotizacion_itbis_total.setText("$ "+this.itbisTotal);
        this.cotizacion_sub_total.setText("$ "+this.subMontoTotal);
        this.cotizacion_monto_total.setText("$ "+this.montoTotal);
    }
    public void resetTotatel(){
         this.montoTotal = 0.00;
        this.itbisTotal = 0.00;
        this.subMontoTotal = 0.00;
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
            JasperReport loadObject = (JasperReport) JRLoader.loadObject(Facturacion.class.getResource("../theme/facturacion.jasper"));
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

            parameters.put("no_factura", this.FacturacionID);
            parameters.put("fecha", this.obtenerFechaActual());
            
            
            parameters.put("rnc_cliente", this.rnc_cliente);
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
            Logger.getLogger(Facturacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void administrar_focus_clientes(String menu,String tabla){
        SeleccionLista selecciona = new SeleccionLista(this.mysql,tabla);
         selecciona.setObjectFacturacion(this.ObjectFacturacion);
        
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
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        JCBTieneITBIS = new javax.swing.JCheckBox();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        JPReparacion = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTReparacion = new javax.swing.JTable();
        JPCotizacion = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTCotizacion = new javax.swing.JTable();
        JPDeuda = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTDeuda = new javax.swing.JTable();

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

        jButton3.setText("Facturar");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jButton4.setText("Pagar");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("COTIZACIONES, REPARACIONES, DEUDAS DEL CLIENTE");

        JCBTieneITBIS.setText("Tiene ITBIS?");
        JCBTieneITBIS.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                JCBTieneITBISStateChanged(evt);
            }
        });

        jButton5.setText("Agregar Nota");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        jButton6.setText("Agregar NCF");
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

        jTCotizacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTCotizacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTCotizacionMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTCotizacion);

        javax.swing.GroupLayout JPCotizacionLayout = new javax.swing.GroupLayout(JPCotizacion);
        JPCotizacion.setLayout(JPCotizacionLayout);
        JPCotizacionLayout.setHorizontalGroup(
            JPCotizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
        );
        JPCotizacionLayout.setVerticalGroup(
            JPCotizacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("COTIZACIONES", JPCotizacion);

        jTDeuda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTDeuda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTDeudaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTDeuda);

        javax.swing.GroupLayout JPDeudaLayout = new javax.swing.GroupLayout(JPDeuda);
        JPDeuda.setLayout(JPDeudaLayout);
        JPDeudaLayout.setHorizontalGroup(
            JPDeudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
        );
        JPDeudaLayout.setVerticalGroup(
            JPDeudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("DEUDAS", JPDeuda);

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
                        .addComponent(JCBTieneITBIS, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(JCBTieneITBIS)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                .addContainerGap())
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
        if(this.validarQueTengaUnoSeleccionado()){
            this.crearFacturacion();
            this.factura_generada = true;
        }
    }//GEN-LAST:event_jButton3MouseClicked

    private void t_codigo_clienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_codigo_clienteFocusGained
        // TODO add your handling code here:
       /* Texto.placeholder(Texto.codigo_cliente,this.t_codigo_cliente.getText(), this.t_codigo_cliente);
        SeleccionLista selecciona = new SeleccionLista(this.mysql);
        selecciona.setObjectFacturacion(this.ObjectFacturacion);
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
       this.NCF = JOptionPane.showInputDialog(null, "Ingrese su NCF (Número de Comprobante Fiscal)", "NCF (Número de Comprobante Fiscal)", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton6MouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // TODO add your handling code here:
       this.notaFactura = JOptionPane.showInputDialog(null, "Ingrese su Nota", "Agregar Nota a la Factura", JOptionPane.INFORMATION_MESSAGE);
       
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        // TODO add your handling code here:
        if(this.factura_generada){
            Pago p = new Pago(this.mysql);
            p.setObjectFacturacion(this.ObjectFacturacion);
            p.setBalance(this.monto_total);
            p.setVisible(true);
        }else{
            int respuesta  = JOptionPane.showConfirmDialog(  null,  "Debe generar la factura antes de pagarla, Desea generarla ahora?","Advertencia ",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
               // JOptionPane.showMessageDialog(null, "null");
                if(this.validarQueTengaUnoSeleccionado()){
                        this.crearFacturacion();
                        this.factura_generada = true;
                }
                if(this.factura_generada){
                        Pago p = new Pago(this.mysql);
                        p.setObjectFacturacion(this.ObjectFacturacion);
                        p.setBalance(this.monto_total);
                        p.setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_jButton4MouseClicked

    private void jTDeudaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTDeudaMouseClicked
        // TODO add your handling code here:
        int row =  this.jTDeuda.getSelectedRow();
        String id = this.jTDeuda.getValueAt(row, 0).toString();
        this.cargarTablaParaFacturar(id,"deuda");
    }//GEN-LAST:event_jTDeudaMouseClicked

    private void jTCotizacionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTCotizacionMouseClicked
        // TODO add your handling code here:
        int row =  this.jTCotizacion.getSelectedRow();
        String id = this.jTCotizacion.getValueAt(row, 0).toString();
        this.cargarTablaParaFacturar(id,"cotizacion");
    }//GEN-LAST:event_jTCotizacionMouseClicked

    private void jTReparacionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTReparacionMouseClicked
        // TODO add your handling code here:
        int row =  this.jTReparacion.getSelectedRow();
        String id = this.jTReparacion.getValueAt(row, 0).toString();
        this.cargarTablaParaFacturar(id,"reparacion");
    }//GEN-LAST:event_jTReparacionMouseClicked

    private void JCBTieneITBISStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_JCBTieneITBISStateChanged
        // TODO add your handling code here:
        this.resetTotatel();
       if( this.JCBTieneITBIS.isSelected())
       {
           this.tiene_itbis = true;
           this.reCalcular();
       }else{
           this.tiene_itbis = false;
           this.reCalcular();
       }
    }//GEN-LAST:event_JCBTieneITBISStateChanged

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox JCBTieneITBIS;
    private javax.swing.JPanel JPCotizacion;
    private javax.swing.JPanel JPDeuda;
    private javax.swing.JPanel JPReparacion;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTCotizacion;
    private javax.swing.JTable jTDeuda;
    private javax.swing.JTable jTReparacion;
    private javax.swing.JTabbedPane jTabbedPane1;
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
