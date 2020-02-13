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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
    
    int num_men_totales, num_men_dia;
    String auxDate1;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
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

    public int getNum_men_totales() {
        return num_men_totales;
    }

    public void setNum_men_totales(int num_men_totales) {
        this.num_men_totales = num_men_totales;
    }

    public int getNum_men_dia() {
        return num_men_dia;
    }

    public void setNum_men_dia(int num_men_dia) {
        this.num_men_dia = num_men_dia;
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
    
        //HACER DO WHILE POR SI EN UNA FECHA NO HAY MENSAJES PERO SI HAY EN LA CONVERSACION 
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        auxDate1 = sdf.format(timestamp);
        
        do{
            //1-Pido los mensajes en una fecha y se lo mando al server
            
            this.pedir_conversaciones_server_fecha(usuario_dest);

            //2-Recivo del server el numero de mensajes totales de la conversacion y el numero de mensajes totales de un dia
            this.procesar_entrada_server_numero_mensajes();

            //3-Compruebo que los numeros enviados por el server son != 0
            if(this.num_men_totales!=0){//Si el total de conversacion == 0 no hay mensajes
                if(this.num_men_dia!=0){
                    this.enviar_al_server_ok_send();
                    this.recibir_mensajes_un_dia(array_amigos_mensajes,pos);
                    this.enviar_al_server_all_received();
                }else{//Pido al server los mensajes de otro dia(programado en el protocolo)
                    //BORRAR ELSE
                }
            }
        }while(this.num_men_totales!=0 && this.num_men_dia==0);
    
    }
    
    public void pedir_conversaciones_server_fecha(String usuario_dest){
        
        fromUser = protocolo.procesarPedirConversacion(this.usuario, usuario_dest, this.auxDate1);
        this.auxDate1 = sdf.format(Timestamp.valueOf(auxDate1).getTime() - (24*60*60*1000));
         
        //Envio al server que quiero los mensajes en una fecha
        if (fromUser != null) {
            System.out.println("CLIENT TO SERVER: " + fromUser);
            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
            out.println(fromUser); //Envia por el socket            
        }
    }
    
    public void procesar_entrada_server_numero_mensajes() throws IOException{
        if((fromServer = in.readLine()) != null){
            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
            
            num_men_totales=0;
            num_men_dia = 0;
            
            
            //Aqui solo saco los numeros de las conversaciones
            protocolo.procesarConversacion_Numero(fromServer, num_men_totales,num_men_dia, this);
        }
    }
    
    public void enviar_al_server_ok_send(){
        fromUser = protocolo.procesarConversacion_ok_send();
        if (fromUser != null) {
            System.out.println("CLIENT TO SERVER: " + fromUser);
            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
            out.println(fromUser); //Envia por el socket            
        }
    }
    
    public void recibir_mensajes_un_dia(ArrayList <AmigosDeUnUsuario_Mensajes> array_amigos_mensajes, int pos) throws IOException{
        array_amigos_mensajes.get(pos).mensajes_array.clear();

        //Me traigo los mensajes de un usuario en una fecha
        for(int i=0; i<this.num_men_totales; i++){
            //Si me envía algún mensaje
            if((fromServer = in.readLine()) != null){
                System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
                protocolo.procesarConversacion_Mensajes(fromServer, array_amigos_mensajes.get(pos).mensajes_array, num_men_totales);
            }
        }    
    }
    
    public void enviar_al_server_all_received(){
        fromUser = protocolo.procesarConversacion_all_received();
        if (fromUser != null) {
            System.out.println("CLIENT TO SERVER: " + fromUser);
            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
            out.println(fromUser); //Envia por el socket            
        }
    }
}   
    
    
    
    
//    public void conectar_con_server_mensajes(String usuario_dest,ArrayList <AmigosDeUnUsuario_Mensajes> array_amigos_mensajes, int pos) throws IOException{

//        fromUser = protocolo.procesarPedirConversacion(this.usuario, usuario_dest);
//         
//        //Envio al server que quiero los mensajes de una fecha y con unos usuarios
//        if (fromUser != null) {
//            System.out.println("CLIENT TO SERVER: " + fromUser);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//
//            out.println(fromUser); //Envia por el socket            
//        }
//        
//        //Recivo mensaje del numero de mensajes y del numero de mensajes de el dia que ha indicado el server
//        if((fromServer = in.readLine()) != null){
//            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
//            
//            num_men_totales=0;
//            num_men_dia = 0;
//            fromUser = protocolo.procesarConversacion_Numero(fromServer, num_men_totales,num_men_dia, this);
//            
//            if(num_men_totales !=0){
//            
//                if(num_men_dia != 0){ //Si el num de mensajes es != 0 entonces me van a llegar mensajes
//                   //Compruebo lo que se le va a mandar al server de salida por parte del cliente 
//                    int comprobacion_send = fromUser.indexOf("#CLIENT#MSGS#OK_SEND!");
//                    int comprobacion_received = fromUser.indexOf("#CLIENT#MSGS#ALL_RECEIVED");
//
//                    if(comprobacion_send != 0 || comprobacion_received!=0){
//                        System.out.println("CLIENT TO SERVER: " + fromUser);
//                        VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//                        out.println(fromUser); //Envia por el socket
//
//                        if(comprobacion_send != -1){
//                            //El server me va a enviar los amigos_del_usuario_mensajes
//                            //Los tengo que guardar en un array de una clase que tenga un array de amigos_del_usuario_mensajes
//
//                            //Limpio el array de mensajes ---CAMBIAR---
//                            array_amigos_mensajes.get(pos).mensajes_array.clear();
//
//                            //Me traigo los mensajes de ese usuario en una fecha
//
//                                for(int i=0; i<this.num_men_totales; i++){
//                                    //Si me envía algún mensaje
//                                    if((fromServer = in.readLine()) != null){
//                                        System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//                                        VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
//
//                                        protocolo.procesarConversacion_Mensajes(fromServer, array_amigos_mensajes.get(pos).mensajes_array, num_men_totales);
//                                    }
//                                }

//
//                            fromUser = protocolo.procesarSalidaConversacion_AllReceived();        
//                            if (fromUser != null) {
//                                System.out.println("CLIENT TO SERVER: " + fromUser);
//                                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//
//                                out.println(fromUser); //Envia por el socket            
//                            }
//
//                        }
//
//                    }
//
//                }else { //Busca otra fecha
//                    
//                }
//            }
//                    
//        } else { //No hay mensajes
//
//
//        }
//                                 
//    }
//                   
//}
