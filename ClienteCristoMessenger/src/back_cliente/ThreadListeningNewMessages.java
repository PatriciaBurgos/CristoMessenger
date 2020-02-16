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
    ConexionClienteconServer conexionCliente;
    boolean listening;
    
    public ThreadListeningNewMessages(ConexionClienteconServer conexion_cliente) {
        this.conexionCliente = conexion_cliente;     
        listening=true;
    }
    
    @Override
    public void run() {   
        while(listening){
            try {
                if((conexionCliente.fromServer = conexionCliente.in.readLine()) != null){
                    
                    if(conexionCliente.fromServer.contains("#SERVER#CHAT#") && !conexionCliente.fromServer.contains("#MESSAGE_SUCCESFULLY_PROCESSED#")){
                        if(!conexionCliente.fromServer.contains("#MESSAGE_SUCCESFULLY_PROCESSED#")){
                            //RECIVO DEL SERVIDOR
                            System.out.println("CLIENT RECEIVE TO SERVER: " + conexionCliente.fromServer);
                            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + conexionCliente.fromServer+ "\n");
                            //Guardo el mensaje y hago la cadena para enviarla al server
                            conexionCliente.fromUser = conexionCliente.protocolo.procesarMensajeNuevo(conexionCliente.fromServer,this.conexionCliente.array_mensajes_usuario);

                            //ENVIO AL SERVIDOR
                            if (conexionCliente.fromUser != null) {
                                System.out.println("CLIENT TO SERVER: " + conexionCliente.fromUser);
                                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + conexionCliente.fromUser+ "\n");
                                conexionCliente.out.println(conexionCliente.fromUser); //Envia por el socket            
                            }
                            
                        }else{
                            System.out.println("CLIENT RECEIVE TO SERVER: " + conexionCliente.fromServer);
                            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + conexionCliente.fromServer+ "\n");
                        }
                    }
                }
            } catch (IOException ex) { 
                Logger.getLogger(ThreadListeningNewMessages.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
