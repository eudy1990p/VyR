/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vyr;

import formularios.splash;

/**
 *
 * @author Eudy
 */
public class VyR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       conexion.Mysql mysql = new conexion.Mysql();
            new Thread(new splash(mysql)).start();
    }
    
}
