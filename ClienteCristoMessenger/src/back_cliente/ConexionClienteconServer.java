/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_cliente;

import front_cliente.VistaClienteChats;
import front_cliente.VistaClienteLogin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author patri
 */
public class ConexionClienteconServer {
    
    String ip;
    int puerto;
    String usuario;
    String contraseña;
    ProtocoloCliente protocolo;
    
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    String fromServer;
    String fromUser;
    
    int num_men;
    
    public ConexionClienteconServer(String IP, int port, String user, String pass) throws IOException{
        ip = IP;
        puerto = port;
        usuario = user;
        contraseña = pass;
        protocolo = new ProtocoloCliente();
        socket = new Socket(ip, puerto);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        System.out.println("CLIENT: IP-->" + ip + " PUERTO--> " + puerto + " USUARIO--> " + usuario + " CONTRASEÑA--> " +contraseña);
//        VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT:IP-->" + ip + " PUERTO--> " + puerto + " USUARIO--> " + usuario + " CONTRASEÑA--> " +contraseña+ "\n");
         
        
    }

    public int getNum_men() {
        return num_men;
    }

    public void setNum_men(int num_men) {
        this.num_men = num_men;
    }
    
    
    
    public ArrayList conectar_con_server_login() throws IOException{
        Boolean check = false;
        ArrayList <String> salida = new ArrayList();         
                    
            
        fromUser = protocolo.procesarEntradaLogin(usuario,contraseña);

        //Lo envío al servidor (1)  
        if (fromUser != null) {
            System.out.println("CLIENT TO SERVER: " + fromUser);
//                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
            out.println(fromUser); //Envia por el socket
        }

        //Me lo envía el servidor (2)
        if((fromServer = in.readLine()) != null){
            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
            salida = protocolo.procesarSalidaLogin(fromServer);
        }        
        return salida;
    }
    
    public void conectar_con_server_mensajes(String usuario_dest,ArrayList <AmigosDeUnUsuario_Mensajes> array_amigos_mensajes, int pos) throws IOException{
        //El array tendria que esatr declarado en la vista de chats

        fromUser = protocolo.procesarPedirConversacion(this.usuario, usuario_dest);
         
        //Envio al server que quiero los mensajes de una fecha y con unos usuarios
        if (fromUser != null) {
            System.out.println("CLIENT TO SERVER: " + fromUser);
            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");

            out.println(fromUser); //Envia por el socket            
        }
        
        //Recivo mensaje del numero de mensajes y del numero de mensajes de el dia que ha indicado el server
        if((fromServer = in.readLine()) != null){
            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
            
            
            fromUser = protocolo.procesarConversacion_Numero(fromServer, num_men,this);
            
            //Compruebo lo que se le va a mandar al server
            int comprobacion_send = fromUser.indexOf("#CLIENT#MSGS#OK_SEND!");
            int comprobacion_received = fromUser.indexOf("#CLIENT#MSGS#ALL_RECEIVED");
            if(comprobacion_send != 0 || comprobacion_received!=0){
                System.out.println("CLIENT TO SERVER: " + fromUser);
                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
                out.println(fromUser); //Envia por el socket
                
                if(comprobacion_send != -1){
                    //El server me va a enviar los amigos_del_usuario_mensajes
                    //Los tengo que guardar en un array de una clase que tenga un array de amigos_del_usuario_mensajes
                    
                    //Limpio el array de mensajes ---CAMBIAR---
                    array_amigos_mensajes.get(pos).mensajes_array.clear();
                    
                    //Me traigo los mensajes de ese usuario en una fecha
                    for(int i=0; i<this.num_men; i++){
                        if((fromServer = in.readLine()) != null){
                            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
                            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");

                            protocolo.procesarConversacion_Mensajes(fromServer, array_amigos_mensajes.get(pos).mensajes_array, num_men);
                        }
                    }
                    fromUser = protocolo.procesarSalidaConversacion_AllReceived();        
                    if (fromUser != null) {
                        System.out.println("CLIENT TO SERVER: " + fromUser);
                        VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");

                        out.println(fromUser); //Envia por el socket            
                    }
                                
                    
                    //Le mando al servidor que todo esta recibido
                    
                }
                
            }else {
                //ERROR
            }
        }  
    }       
}
