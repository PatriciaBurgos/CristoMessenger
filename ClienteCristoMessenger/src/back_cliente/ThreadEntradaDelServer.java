/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_cliente;

import front_cliente.VistaClienteChats;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patri
 */
public class ThreadEntradaDelServer extends Thread{
    boolean listening;
    ConexionClienteconServer2 conexion;
    String amigo;
    
    public ThreadEntradaDelServer(ConexionClienteconServer2 conex) {
        this.conexion = conex;
    }

    @Override
    public void run() {
        
//        while ((conexion.fromServer = conexion.in.readLine()) != null) {
//                System.out.println("CLIENT RECEIVE TO SERVER: " + conexion.fromServer);
//                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + conexion.fromServer+ "\n");
//            }
//        try {
//            conexion.protocolo.procesarEntradaServer(conexion.fromServer);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ThreadEntradaDelServer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//            conexion.vchats.nuevo_mensaje(amigo);
        
        while(listening){
            try {
                System.out.println("ENTRO EN LA HEBRA");
                if((conexion.fromServer = conexion.in.readLine()) != null){
                    
                    System.out.println("CLIENT RECEIVE TO SERVER: " + conexion.fromServer);
                    VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + conexion.fromServer+ "\n");
                    conexion.protocolo.procesarEntradaServer(conexion.fromServer);
                    conexion.vchats.nuevo_mensaje(amigo);
                }
            } catch (IOException ex) {
                Logger.getLogger(ThreadEntradaDelServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadEntradaDelServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
