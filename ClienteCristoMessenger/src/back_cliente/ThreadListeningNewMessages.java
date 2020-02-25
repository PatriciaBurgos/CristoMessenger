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
public class ThreadListeningNewMessages extends Thread {
    ConexionClienteconServer2 conexionCliente;
    boolean listening;
    
    public ThreadListeningNewMessages(ConexionClienteconServer2 conexion_cliente) {
        this.conexionCliente = conexion_cliente;     
        listening=true;
    }
    
    @Override
    public void run() {   
        while(listening){
            
//            try {
//                Thread.sleep(20000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(ThreadEstadoAmigos.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//            try {
//                if((conexionCliente.fromServer = conexionCliente.in.readLine()) != null){
//                    
//                    if(conexionCliente.fromServer.contains("#SERVER#CHAT#") && !conexionCliente.fromServer.contains("#MESSAGE_SUCCESFULLY_PROCESSED#")){
//                        try {
//                            conexionCliente.recibo_mensaje();
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(ThreadListeningNewMessages.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            } catch (IOException ex) { 
//                Logger.getLogger(ThreadListeningNewMessages.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }
    
}
