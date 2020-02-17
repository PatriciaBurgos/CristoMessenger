/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patri
 */

public class ThreadEstadoAmigos extends Thread{
    ConexionClienteconServer conexionCliente;
    boolean listening;
    
    public ThreadEstadoAmigos(ConexionClienteconServer conexion_cliente) {
        this.conexionCliente = conexion_cliente;     
        listening=true;
    }
    
    @Override
    public void run() {   
        while(listening){
            try {
                Thread.sleep(15000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadEstadoAmigos.class.getName()).log(Level.SEVERE, null, ex);
            }
            conexionCliente.comprobar_estados();
            
        }
    }
}