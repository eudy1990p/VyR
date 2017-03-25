/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Eudy
 */
public class Mysql {

     private static Connection Conexion;
     private String user="root",pass="",db_name="vyr_db";
     private String orden = "";
     private boolean mostrarMensaje = true;

    public boolean isMostrarMensaje() {
        return mostrarMensaje;
    }

    public void setMostrarMensaje(boolean mostrarMensaje) {
        this.mostrarMensaje = mostrarMensaje;
    }
             
     public Mysql(){
         this.MySQLConnection(user, pass, db_name);
     }

    public void MySQLConnection(String user, String pass, String db_name) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db_name, user, pass);
            //Conexion.createStatement().executeQuery("use java_hospital_theme_db");
            System.out.println("Se ha iniciado la conexión con el servidor de forma exitosa");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error1");
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            
            System.out.println("error "+ex.getLocalizedMessage());
            System.out.println("Error2 : 1 "+ex.getErrorCode()+" 2- "+ex.getCause()+" 3- "+ex.getLocalizedMessage()+" 4- "+ex.getMessage()+" 5- "+ex.getSQLState()
                    );
            switch(ex.getSQLState()){
                /*case "java.net.ConnectException: Connection refused: connect":
                    System.out.println("entro en este caso");
                    break;*/
                 case "08S01":
                    System.out.println("no conecta al servidor");
                    break;
                case "42000":
                    System.out.println("acceso denegado");
                    System.out.println(ex.getCause());
                    if(ex.getCause() == null){
                        System.out.println("prueba caso "+ex.getCause());
                    }
                    break;
            }
            //Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean generarInsert(String[] key, String[] value,String tableName) {
        String values = "";
        String keys = "";
        //JOptionPane.showMessageDialog(null,String.join(",", value));
        //JOptionPane.showMessageDialog(null,String.join(",", key));
        try {
            for(int i = 0 ; i < value.length ; i++){
                System.out.println(i +" "+value.length);
                if(i == 0){
                    System.out.println("0 Values "+value[i]+" key "+key[i]);
                    keys += key[i];
                    if(value[i].equals("now()")){
                        values += ""+value[i]+"";
                    }else{
                        values += "'"+value[i]+"'";
                    }
                }else{
                    System.out.println("0 + Values "+value[i]+" key "+key[i]);
                    keys += ","+key[i];
                    if(value[i].equals("now()")){
                        values += ","+value[i]+"";
                    }else{
                        values += ",'"+value[i]+"'";
                    }
                }
                
            }
            //System.out.println("Salimos Values "+values+" key "+keys);
            String Query = "INSERT INTO " + tableName + " "
                    + "("+keys+")"
                    + "values ("+values+")";
            System.out.println("insert "+Query);
          /*  String Query = "INSERT INTO " + table_name + " VALUES("
                    + "\"" + ID + "\", "
                    + "\"" + name + "\", "
                    + "\"" + lastname + "\", "
                    + "\"" + age + "\", "
                    + "\"" + gender + "\")";*/
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
            if(this.mostrarMensaje){
            JOptionPane.showMessageDialog(null, "Datos almacenados de forma exitosa");
            }
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en el almacenamiento de datos");
                        System.out.println(ex.getErrorCode()+" "+ex.getMessage()+" "+ex.getCause());

            return false;
        }
    }
    
    public String generarInsertWithGetLastID(String[] key, String[] value,String tableName) {
        String values = "";
        String keys = "";
        String ultimoID = "";
        try {
            for(int i = 0 ; i < value.length ; i++){
                System.out.println(i +" "+value.length);
                if(i == 0){
                    System.out.println("0 Values "+value[i]+" key "+key[i]);
                    keys += key[i];
                    if(value[i].equals("now()")){
                        values += ""+value[i]+"";
                    }else{
                        values += "'"+value[i]+"'";
                    }
                }else{
                    System.out.println("0 + Values "+value[i]+" key "+key[i]);
                    keys += ","+key[i];
                    if(value[i].equals("now()")){
                        values += ","+value[i]+"";
                    }else{
                        values += ",'"+value[i]+"'";
                    }
                }
                
            }
            String Query = "INSERT INTO " + tableName + " "
                    + "("+keys+")"
                    + "values ("+values+")";
            System.out.println("Query "+Query);
          
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "Datos almacenados de forma exitosa");
            
            Query = "SELECT max(id) as id FROM " + tableName+"";
            st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);
            
            if(resultSet.next()){
                ultimoID = resultSet.getString("id");
            }
                
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en el almacenamiento de datos");
            ultimoID = "";
                        System.out.println(ex.getErrorCode()+" "+ex.getMessage()+" "+ex.getCause());

        }
        return ultimoID;
    }
    
    public void generarSelectLastRow(String table_name,String where,DefaultTableModel model,Object[] columnas,String[] datos) {
        try {
            String Query = "SELECT * FROM " + table_name +" "+where;
            System.out.println(Query);
            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);
            
            
            while (resultSet.next()) {
                    for(int i = 0 ; i < datos.length; i++){
                        columnas[i] = resultSet.getString(datos[i]);
                    }
                    model.addRow(columnas);
               /* System.out.println("ID: " + resultSet.getString("ID") + " "
                        + "Nombre: " + resultSet.getString("Nombre") + " " + resultSet.getString("Apellido") + " "
                        + "Edad: " + resultSet.getString("Edad") + " "
                        + "Sexo: " + resultSet.getString("Sexo"));
                    */
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos generarSelectLastRow");
        }
    }
    
    public Object generarSelect(String table_name,String[] campos) {
        try {
            String Query = "SELECT * FROM " + table_name+" where display = '1' ";
            System.out.println("QUERY "+Query);

            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);            
            resultSet.beforeFirst();  
            resultSet.last();  
            
           //System.out.print("GetRow "+resultSet.getRow());
           int totalFilas = resultSet.getRow();
           int f = 0 ;
            resultSet.beforeFirst();
            Object[][] fila = new Object[totalFilas][campos.length];
            while (resultSet.next()) {
                    //System.out.println("ID: " + resultSet.getString("id"));
                    for(int i = 0 ; i < campos.length ;i++){
                      System.out.println("i " + i+" valor campo "+resultSet.getString(campos[i])+" campo "+campos[i]);
                        fila[f][i] = resultSet.getString(campos[i]);
                    }
                    f++;
            }
            System.out.print(" fin while" );
            Object[][] resultado = new Object[1][2];
            System.out.print(" objecto" );

            resultado[0][0]=fila;
            resultado[0][1]=totalFilas;
            System.out.print(" fin" );

            return resultado;
        } catch (SQLException ex) {
            Object[][] resultado = new Object[1][2];
            Object[][] fila = new Object[1][2];
            resultado[0][0]=fila;
            resultado[0][1]=0;
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos generarSelect");
            return resultado;
        }
            
    }
    
    public Object generarSelect(String table_name,String[] campos, String where) {
        try {
            String Query = "SELECT * FROM " + table_name+" where "+where+" display = '1' ";
            System.out.println("QUERY "+Query);

            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);            
            resultSet.beforeFirst();  
            resultSet.last();  
            
           //System.out.print("GetRow "+resultSet.getRow());
           int totalFilas = resultSet.getRow();
           int f = 0 ;
            resultSet.beforeFirst();
            Object[][] fila = new Object[totalFilas][campos.length];
            while (resultSet.next()) {
                    //System.out.println("ID: " + resultSet.getString("id"));
                    for(int i = 0 ; i < campos.length ;i++){
                      System.out.println("i " + i+" valor campo "+resultSet.getString(campos[i])+" campo "+campos[i]);
                        fila[f][i] = resultSet.getString(campos[i]);
                    }
                    f++;
            }
            System.out.print(" fin while" );
            Object[][] resultado = new Object[1][2];
            System.out.print(" objecto" );

            resultado[0][0]=fila;
            resultado[0][1]=totalFilas;
            System.out.print(" fin" );

            return resultado;
        } catch (SQLException ex) {
            Object[][] resultado = new Object[1][2];
            Object[][] fila = new Object[1][2];
            resultado[0][0]=fila;
            resultado[0][1]=0;
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos generarSelect");
            return resultado;
        }
            
    }
    
    public Object generarSelectMultipleTabla(String table_name,String[] campos,String select, String where,String prefijo) {
        System.out.println(String.join(",", campos));

        try {
            String Query = "SELECT "+select+" FROM " + table_name+" where "+where+" "+prefijo+"display = '1' "+this.orden;
            System.out.println("QUERY "+Query);

            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);            
            resultSet.beforeFirst();  
            resultSet.last();  
            
           //System.out.print("GetRow "+resultSet.getRow());
           int totalFilas = resultSet.getRow();
           int f = 0 ;
            resultSet.beforeFirst();
            Object[][] fila = new Object[totalFilas][campos.length];
             System.out.println(" ok estamos por mostrar ");
            while (resultSet.next()) {
                    
                    //System.out.println("ID: " + resultSet.getString("id"));
                    for(int i = 0 ; i < campos.length ;i++){
                        if(campos[i].equalsIgnoreCase("poh.price")){
                            System.out.println(" dato que no se muestra "+resultSet.getBigDecimal("poh.price")+" | "+campos[1]);
                            String da = new String(resultSet.getBigDecimal(campos[i]).toPlainString()); 
                            fila[f][i] = da;
                        }else{
                            System.out.println(" ok ya estamos ");
                            
                            System.out.println("i " + i+" valor campo "+resultSet.getString(campos[i])+" campo "+campos[i]);
                            fila[f][i] = resultSet.getString(campos[i]).toString();
                        }
                    }
                    f++;
            }
            this.mostrarElementoArreglo(fila);
            System.out.print(" fin while" );
            Object[][] resultado = new Object[1][2];
            System.out.print(" objecto" );

            resultado[0][0]=fila;
            resultado[0][1]=totalFilas;
            System.out.print(" fin" );

            return resultado;
        } catch (SQLException ex) {
            Object[][] resultado = new Object[1][2];
            Object[][] fila = new Object[1][2];
            resultado[0][0]=fila;
            resultado[0][1]=0;
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos generarSelectMultipleTabla(String table_name,String[] campos,String select, String where,String prefijo)");
            return resultado;
        }
            
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }
    
    public void mostrarElementoArreglo(Object[][] p){
        for(int i = 0 ; i < p.length ; i++){
            for(int c = 0 ; c < p[i].length ; c++){
                System.out.println(i+"Resultado "+p[i][c]);
           }   
        }
        
    }
    public Object generarSelect(String table_name,String[] campos,String CampoOrdenar,String tipoOrden,String otros) {
        try {
            String Query = "SELECT * FROM " + table_name+" where display = '1' "+otros+"  order by "+CampoOrdenar+" "+tipoOrden+" ";
            System.out.println("Generar Select QUERY "+Query);

            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);            
            resultSet.beforeFirst();  
            resultSet.last();  
            
           //System.out.print("GetRow "+resultSet.getRow());
           int totalFilas = resultSet.getRow();
           int f = 0 ;
            resultSet.beforeFirst();
            Object[][] fila = new Object[totalFilas][campos.length];
            while (resultSet.next()) {
                    //System.out.println("ID: " + resultSet.getString("id"));
                    for(int i = 0 ; i < campos.length ;i++){
                        System.out.println("i " + i+" valor campo "+resultSet.getString(campos[i])+" campo "+campos[i]);
                        fila[f][i] = resultSet.getString(campos[i]);
                    }
                    f++;
            }
            System.out.print(" fin while" );
            Object[][] resultado = new Object[1][2];
            System.out.print(" objecto" );

            resultado[0][0]=fila;
            resultado[0][1]=totalFilas;
            System.out.print(" fin" );

            return resultado;
        } catch (SQLException ex) {
            Object[][] resultado = new Object[1][2];
            resultado[0][0]=0;
            resultado[0][1]=0;
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos generarSelect(String table_name,String[] campos,String CampoOrdenar,String tipoOrden,String otros)");
            return resultado;
        }
            
    }
    
    
    public Object generarSelect(String table_name,String[] campos,String palabra,String campo) {
        try {
            String Query = "SELECT * FROM " + table_name+" where "+campo+" like '%"+palabra+"%' and display = '1' ";
            System.out.println(Query);
            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);
            
            resultSet.beforeFirst();  
            resultSet.last();  
            
           //System.out.print("GetRow "+resultSet.getRow());
           int totalFilas = resultSet.getRow();
           int f = 0 ;
            resultSet.beforeFirst();
            Object[][] fila = new Object[totalFilas][campos.length];
            while (resultSet.next()) {
                    //System.out.println("ID: " + resultSet.getString("id"));
                      for(int i = 0 ; i < campos.length ;i++){
                        fila[f][i] = resultSet.getString(campos[i]);
                    }
                    //fila[f][0] = resultSet.getString("id");
                    //fila[f][1]  = resultSet.getString("name_user");
                    //fila[f][2]  = resultSet.getString("type_of_user");

                    f++;
            }
            Object[][] resultado = new Object[1][2];
            resultado[0][0]=fila;
            resultado[0][1]=totalFilas;
            return resultado;
        } catch (SQLException ex) {
            
            Object[][] resultado = new Object[1][2];
            Object[][] fila = new Object[1][2];
            resultado[0][0]=fila;
            resultado[0][1]=0;
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos generarSelect(String table_name,String[] campos,String palabra,String campo)");
            return resultado;
        }
            
    }
    
       public String[] generarSelect(String table_name,String id,String[] campos) {
        try {
            String Query = "SELECT * FROM " + table_name+" where id = "+id+" and display = '1'";
            System.out.println(Query);
            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);
            resultSet.beforeFirst();  
            resultSet.last();  
           int totalFilas = resultSet.getRow();
           int f = 0 ;
            resultSet.beforeFirst();
           String[] fila = new String[campos.length];
            if (resultSet.next()) {
                    for(int i = 0 ; i < campos.length ; i++){
                        fila[i]=resultSet.getString(campos[i]);
                        System.out.println(" Probando "+fila[i]+" ID: " + resultSet.getString("id"));
                    }       
            }
            return fila;
        } catch (SQLException ex) {
            String[] fila = {"no"};
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos generarSelect(String table_name,String id,String[] campos)");
            return fila;
        }
    }
    
       public String[] generarSelectWithJoin(String table_name,String mostrarCampos,String id,String[] campos,String prefijo) {
        try {
            String Query = "SELECT "+mostrarCampos+" FROM " + table_name+" where "+prefijo+"id = "+id+" and  "+prefijo+"display = '1'";
            System.out.println(Query);
            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);
            resultSet.beforeFirst();  
            resultSet.last();  
           int totalFilas = resultSet.getRow();
           int f = 0 ;
            resultSet.beforeFirst();
           String[] fila = new String[campos.length];
            if (resultSet.next()) {
                    for(int i = 0 ; i < campos.length ; i++){
                         if(campos[i].equalsIgnoreCase("poh.price")){
                            System.out.println(" dato que no se muestra "+resultSet.getBigDecimal("poh.price")+" | "+campos[1]);
                            //fila[i]=resultSet.getString(campos[i]);
                            String da = new String(resultSet.getBigDecimal(campos[i]).toPlainString()); 
                            fila[i] = da;
                        }else{
                            fila[i]=resultSet.getString(campos[i]);
                            System.out.println(" Probando "+fila[i]+" ID: " + resultSet.getString("id"));
                        }
                        
                        
                    }       
            }
            return fila;
        } catch (SQLException ex) {
            String[] fila = {"no"};
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos generarSelectWithJoin(String table_name,String mostrarCampos,String id,String[] campos,String prefijo)");
            return fila;
        }
    }
       
       
    public String[] generarSelect(String table_name,String columnas,String id,String[] campos) {
        try {
            String Query = "SELECT "+columnas+" FROM " + table_name+" where id = "+id+" and display = '1'";
            System.out.println(Query);
            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);
            resultSet.beforeFirst();  
            resultSet.last();  
           int totalFilas = resultSet.getRow();
           int f = 0 ;
            resultSet.beforeFirst();
           String[] fila = new String[campos.length];
            if (resultSet.next()) {
                    for(int i = 0 ; i < campos.length ; i++){
                        fila[i]=resultSet.getString(campos[i]);
                        System.out.println(" Probando "+fila[i]+" ID: " + resultSet.getString("id"));
                    }       
            }
            return fila;
        } catch (SQLException ex) {
            String[] fila = {"no"};
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos generarSelect(String table_name,String columnas,String id,String[] campos)");
            return fila;
        }
    }
    
    
    public String[] generarSelect(String table_name,String nameId,String id,String[] campos,String CampoOrder,String order) {
        try {
            String Query = "SELECT * FROM " + table_name+" where "+nameId+" = "+id+" and display = '1' order by "+CampoOrder+" "+order+" ";
            System.out.println(Query);
            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);
            resultSet.beforeFirst();  
            resultSet.last();  
           int totalFilas = resultSet.getRow();
           int f = 0 ;
            resultSet.beforeFirst();
           String[] fila = new String[campos.length];
            if (resultSet.next()) {
                    for(int i = 0 ; i < campos.length ; i++){
                        fila[i]=resultSet.getString(campos[i]);
                        System.out.println(" Probando "+fila[i]+" ID: " + resultSet.getString("id"));
                    }       
            }
            return fila;
        } catch (SQLException ex) {
            String[] fila = {"no"};
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos generarSelect(String table_name,String nameId,String id,String[] campos,String CampoOrder,String order)");
            return fila;
        }
    }
    public void closeConnection() {
        try {
            Conexion.close();
            System.out.println("Se ha finalizado la conexión con el servidor");
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createDB(String name) {
        try {
            String Query = "CREATE DATABASE " + name;
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
            MySQLConnection("root", "", name);
            JOptionPane.showMessageDialog(null, "Se ha creado la base de datos " + name + " de forma exitosa");
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createTable(String name) {
        try {
            String Query = "CREATE TABLE " + name + ""
                    + "(ID VARCHAR(25),Nombre VARCHAR(50), Apellido VARCHAR(50),"
                    + " Edad VARCHAR(3), Sexo VARCHAR(1))";
            JOptionPane.showMessageDialog(null, "Se ha creado la base de tabla " + name + " de forma exitosa");
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertData(String table_name, String ID, String name, String lastname, String age, String gender) {
        try {
            String Query = "INSERT INTO " + table_name + " VALUES("
                    + "\"" + ID + "\", "
                    + "\"" + name + "\", "
                    + "\"" + lastname + "\", "
                    + "\"" + age + "\", "
                    + "\"" + gender + "\")";
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
            JOptionPane.showMessageDialog(null, "Datos almacenados de forma exitosa");
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode()+" "+ex.getMessage()+" "+ex.getCause());
            JOptionPane.showMessageDialog(null, "Error en el almacenamiento de datos");
        }
    }

    public int getValues(String table_name, String where) {
        try {
            String Query = "SELECT * FROM " + table_name+" "+where;
            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);
            
            resultSet.beforeFirst();  
            resultSet.last();  
           int totalFilas = resultSet.getRow();
           
           return totalFilas;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos getValues(String table_name, String where)");
            return 0;
        }
    }
    private String selectCompos = "*";
    public String[] getValues(String table_name, String where, String[] campos) {
        String[] datos = new String[campos.length];
        try {
            String Query = "SELECT "+this.selectCompos+" FROM " + table_name+" "+where;
            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);
             
             
             if(resultSet.next()){
                 for(int i = 0 ; i < campos.length ; i++){
                     datos[i] = resultSet.getString(campos[i]);
                 }
             }
             
           return datos;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos getValues(String table_name, String where, String[] campos)");
            return datos;
        }
    }

    public void setSelectCompos(String selectCompos) {
        this.selectCompos = selectCompos;
    }
    
    public void getValues(String table_name) {
        try {
            String Query = "SELECT * FROM " + table_name;
            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getString("ID") + " "
                        + "Nombre: " + resultSet.getString("Nombre") + " " + resultSet.getString("Apellido") + " "
                        + "Edad: " + resultSet.getString("Edad") + " "
                        + "Sexo: " + resultSet.getString("Sexo"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos getValues(String table_name)");
        }
    }

    public boolean deleteRecord(String table_name, String ID) {
        try {
            //String Query = "DELETE FROM " + table_name + " WHERE ID = \"" + ID + "\"";
            String Query = "update " + table_name + " set display = '0' WHERE ID = \"" + ID + "\"";
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error borrando el registro especificado");
            return false;
        }
    }
    
    public boolean actulizarDatos(String table_name,String campos, String where) {
        try {
            //String Query = "DELETE FROM " + table_name + " WHERE ID = \"" + ID + "\"";
            String Query = "update " + table_name + " set "+campos+" WHERE  " + where +"";
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error ha marcar como completado la reparación");
            return false;
        }
    }
    
    public boolean updateRecord(String table_name, String id,String[] key, String[] value) {
        String keysValues = "";
        try {
            for(int i = 0 ; i < value.length ; i++){
                System.out.println(i +" "+value.length);
                if(i == 0){
                    System.out.println("0 Values "+value[i]+" key "+key[i]);
                    if(value[i].equals("now()")){
                        keysValues += key[i]+" = "+value[i]+" ";
                    }else{
                         keysValues += key[i]+" = '"+value[i]+"' ";
                    }
                }else{
                    System.out.println("0 + Values "+value[i]+" key "+key[i]);
                    if(value[i].equals("now()")){
                         keysValues += ", "+key[i]+" = "+value[i]+" ";
                    }else{
                         keysValues += ", "+key[i]+" = '"+value[i]+"' ";
                    }
                }
            }
            
            String Query = "update " + table_name + " set "+keysValues+" WHERE id = '" +id+ "' ";
            System.out.println(Query);
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error actualizando el registro especificado");
            return false;
        }
    }
    
    public boolean updateRecord(String table_name,String nameId, String id,String[] key, String[] value) {
        String keysValues = "";
        try {
            for(int i = 0 ; i < value.length ; i++){
                System.out.println(i +" "+value.length);
                if(i == 0){
                    System.out.println("0 Values "+value[i]+" key "+key[i]);
                    if(value[i].equals("now()")){
                        keysValues += key[i]+" = "+value[i]+" ";
                    }else{
                         keysValues += key[i]+" = '"+value[i]+"' ";
                    }
                }else{
                    System.out.println("0 + Values "+value[i]+" key "+key[i]);
                    if(value[i].equals("now()")){
                         keysValues += ", "+key[i]+" = "+value[i]+" ";
                    }else{
                         keysValues += ", "+key[i]+" = '"+value[i]+"' ";
                    }
                }
            }
            
            String Query = "update " + table_name + " set "+keysValues+" WHERE "+nameId+" = " +id+ " ";
            System.out.println(Query);
            Statement st = Conexion.createStatement();
            st.executeUpdate(Query);
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error borrando el registro especificado");
            return false;
        }
    }
    
    
    
    
    
    
    
    
    public void insertData(String table_name,String campos, String valores) {
        try {
            String sqlInsert = "INSERT INTO " + table_name + "("+campos+")  VALUES("+valores+" )";
               System.out.println("consulta insert "+sqlInsert);
            Statement st = Conexion.createStatement();
            st.executeUpdate(sqlInsert);
            //JOptionPane.showMessageDialog(null, "Datos almacenados de forma exitosa");
         
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode()+" "+ex.getMessage()+" "+ex.getCause());
            JOptionPane.showMessageDialog(null, "Error en el almacenamiento de datos");
        }
    }
    public String optenerUltimoID(String table_name) {
        String id = "1";
        try {
            String Query = "SELECT max(id) as id FROM " + table_name;
            Statement st = Conexion.createStatement();
            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);

           if (resultSet.next()) {
                id = resultSet.getString("id");
                System.out.println("ID: " + resultSet.getString("ID") );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos getValues(String table_name)");
        }
        return id;
    }
    public java.sql.ResultSet optenerDatosParaTabla(String table_name, String campos,String otros) {
     java.sql.ResultSet resultSet = null;
        try {
            String Query = "SELECT "+campos+" FROM " + table_name+" "+otros;
           System.out.print(Query);
            Statement st = Conexion.createStatement();
            
            resultSet = st.executeQuery(Query);
           /*
           if (resultSet.next()) {
                id = resultSet.getString("id");
                System.out.println("ID: " + resultSet.getString("ID") );
            }*/

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la adquisición de datos getValues(String table_name)");
        }
        return resultSet;
    }
}
