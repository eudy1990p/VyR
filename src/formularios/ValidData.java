/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import javax.swing.JOptionPane;

/**
 *
 * @author Eudy
 */
public class ValidData {
    
    private boolean btCrearPaciente,btCrearHospital,btCrearTypoDeSangre,btCrearUsuario,btCrearEntrega,btCrearSonografia;
    private boolean btBuscasrPaciente,btBuscasrEntrega,btBuscasrSonografia;
    private boolean btcambiarClave,btaboutMe,btSalir,btGenerarReporte;
    
    public boolean getBtCrearPaciente(){ this.MSGSinPermiso(this.btCrearPaciente); return this.btCrearPaciente; }
    public boolean getBtCrearHospital(){ this.MSGSinPermiso(this.btCrearHospital); return this.btCrearHospital; }
    public boolean getBtCrearUsuario(){ this.MSGSinPermiso(this.btCrearUsuario); return this.btCrearUsuario; }
    public boolean getBtCrearEntrega(){ this.MSGSinPermiso(this.btCrearEntrega); return this.btCrearEntrega; }
    public boolean getBtCrearSonografia(){ this.MSGSinPermiso(this.btCrearSonografia); return this.btCrearSonografia; }
    public boolean getBtCrearTypoDeSangre(){ this.MSGSinPermiso(this.btCrearTypoDeSangre); return this.btCrearTypoDeSangre; }
    public boolean getBtBuscarPaciente(){ this.MSGSinPermiso(this.btBuscasrPaciente); return this.btBuscasrPaciente; }
    public boolean getBtBuscarEntrega(){ this.MSGSinPermiso(this.btBuscasrEntrega); return this.btBuscasrEntrega; }
    public boolean getBtBuscarSonografia(){ this.MSGSinPermiso(this.btBuscasrSonografia); return this.btBuscasrSonografia; }
    public boolean getBtCambiarClave(){ this.MSGSinPermiso(this.btcambiarClave); return this.btcambiarClave; }
    public boolean getBtAboutMe(){ this.MSGSinPermiso(this.btaboutMe); return this.btaboutMe; }
    public boolean getBtSalir(){ this.MSGSinPermiso(this.btSalir); return this.btSalir; }
    public boolean getBtGenerarReporte(){this.MSGSinPermiso(this.btGenerarReporte); return this.btGenerarReporte; }
    
    public void MSGSinPermiso(boolean b){
        if(b == false){
            JOptionPane.showMessageDialog(null, "Usted no tiene privilegios para entrar a esta sección.");
        }
    }
    
    public void asignarPermisos(boolean btCrearPaciente,boolean btCrearHospital,boolean btCrearTypoDeSangre,boolean btCrearUsuario,boolean btCrearEntrega,boolean btCrearSonografia,boolean btBuscasrPaciente,/*boolean btBuscasrHospital,boolean btBuscasrTypoDeSangre,boolean btBuscasrUsuario,*/boolean btBuscasrEntrega,boolean btBuscasrSonografia,boolean btcambiarClave,boolean btaboutMe,boolean btSalir,boolean btGenerarReporte){
       this.btCrearPaciente = btCrearPaciente;
       this.btCrearHospital =btCrearHospital;
       this.btCrearTypoDeSangre=btCrearTypoDeSangre;
       this.btCrearUsuario=btCrearUsuario;
       this.btCrearEntrega=btCrearEntrega;
       this.btCrearSonografia=btCrearSonografia;
        this.btBuscasrPaciente=btBuscasrPaciente;
       // this.btBuscasrHospital=btBuscasrHospital;
       // this.btBuscasrTypoDeSangre=btBuscasrTypoDeSangre;
       // this.btBuscasrUsuario=btBuscasrUsuario;
        this.btBuscasrEntrega=btBuscasrEntrega;
        this.btBuscasrSonografia=btBuscasrSonografia;
        this.btcambiarClave=btcambiarClave;
        this.btaboutMe=btaboutMe;
        this.btSalir=btSalir;
        this.btGenerarReporte=btGenerarReporte;
    }
    
    public void validarPermisos(String tipo_usuario){
            switch(tipo_usuario.toLowerCase()){
                case "admin":
                    this.asignarPermisos(true, true, true, true, true, true, true, true, true, true, true, true, true);
                    break;
                case "cajero":
                    this.asignarPermisos(btCrearPaciente=true, btCrearHospital=false, btCrearTypoDeSangre=false, btCrearUsuario=false, btCrearEntrega=false, btCrearSonografia=false, btBuscasrPaciente=true, btBuscasrEntrega=true, btBuscasrSonografia=true, btcambiarClave=true, btaboutMe=true, btSalir=true, btGenerarReporte=false);
                    break;
                case "medico":
                    this.asignarPermisos(btCrearPaciente=true, btCrearHospital=false, btCrearTypoDeSangre=false, btCrearUsuario=false, btCrearEntrega=true, btCrearSonografia=true, btBuscasrPaciente=true, btBuscasrEntrega=true, btBuscasrSonografia=true, btcambiarClave=true, btaboutMe=true, btSalir=true, btGenerarReporte=true);
                    break;
            }
    }
    
    public boolean differentPass(String pass1, String pass2){
        if((pass1.equals(pass2))){
            return false;
        }else{
            JOptionPane.showMessageDialog(null, "La claves no coinsiden");
            return true;
        }
    }
    
    public boolean validEmpty(String n,String name){
        if(n.isEmpty()){
            JOptionPane.showMessageDialog(null, "El Campo "+name+" esta vacio");
            return true;
        }else{
            return false;
        }
        
    }
    public boolean validEmpty(String n){
        if(n.isEmpty()){
            return false;
        }else{
            return true;
        }
        
    }
    
}
