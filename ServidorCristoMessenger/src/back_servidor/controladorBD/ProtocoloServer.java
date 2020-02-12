/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_servidor.controladorBD;

import back_servidor.controladorBD.ControladorAmigos;
import back_servidor.controladorBD.ControladorUsuario;
import back_servidor.modeloBD.MensajesMapeo;
import back_servidor.modeloBD.ModeloAmigos;
import front_servidor.VistaServer;
import java.net.*;
import java.io.*;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author patri
 */
public class ProtocoloServer {
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    ProtocoloServer() throws SQLException{
        
    }
    
    public String procesarErrorPrincipioCadena(){
        String theOutput = null;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#ERROR";
        return theOutput;
    }
    
    public String procesarEntradaLogin(String entrada, MultiServerThread2 hebra) throws SQLException {
        String theOutput = null;
        String usuario = null;
        String contraseña;
        
        

        //Ajusto la cadena para quedarme con el usuario
        int comprob_entrada = entrada.indexOf("#CLIENT#LOGIN#");
        if(comprob_entrada!=-1){
            int ultimaAlmohadilla = entrada.lastIndexOf("#");
            int empieza_usuario = entrada.indexOf("LOGIN");
            usuario = entrada.substring(empieza_usuario+6, ultimaAlmohadilla);
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: Usuario introducido (falta comprobacion): " + usuario + "\n");
            System.out.println("SERVER: Usuario introducido (falta comprobacion): " + usuario);
            
            //Ajusto la cadena para quedarme con la contraseña
            contraseña = entrada.substring(ultimaAlmohadilla+1);
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER:Contraseña introducida (falta comprobacion): " + contraseña + "\n");
            System.out.println("SERVER:Contraseña introducida (falta comprobacion): " + contraseña);
            
            //Conecto con la base de datos y compruebo si esta o no
            boolean check = hebra.controladorUsuario.check_user_login(usuario, contraseña);            
            
            //Salida
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if(check == false){
                theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#ERROR#BAD_LOGIN";
            }else{
                //Como existe el login me traigo todos los amigos 
                ArrayList array_amigos = new ArrayList();
                hebra.controladorAmigos.get_all_friends_con(array_amigos, usuario);

                //Salida                
                theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#LOGIN_CORRECT#"+usuario+"#FRIENDS#"+array_amigos.size()+"#";
                for(int i = 0; i<array_amigos.size(); i++){
                    if(i==array_amigos.size()){
                        theOutput += array_amigos.get(i);
                        //Ahora me falta conectado o no conectado
                        if(hebra.controladorUsuario.comprobar_conexion(array_amigos.get(i).toString())){ //1 -- Conecatdo
                            theOutput += "CONNECTED";
                        }else{ //0 -- No Conectado
                            theOutput += "NOT_CONNECTED";
                        }
                    }else{
                        theOutput += array_amigos.get(i) + "#";
                        //Ahora me falta conectado o no conectado
                        if(hebra.controladorUsuario.comprobar_conexion(array_amigos.get(i).toString())){ //1 -- Conecatdo
                            theOutput += "CONNECTED#";
                        }else{ //0 -- No Conectado
                            theOutput += "NOT_CONNECTED#";
                        }
                    }
                }
            }        
        } else{
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#ERROR#BAD_LOGIN";
            System.out.println("SERVER: Error de cadena (ALERTA HACKER");
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: Error de cadena (ALERTA HACKER)" + "\n");
        }
        return theOutput;
    }
    
    public String procesarEntradaMensajesNumero (String entrada, MultiServerThread2 hebra, String usuario, String amigo) throws SQLException{
        String theOutput = null;
        int num_mensajes_totales=0, num_mensajes_fecha=0;
        String fecha_mensajes;     
        
        
        //1-Compruebo que contiene el MSGS
        if(entrada.contains("#MSGS#")){
            String[] parts = entrada.split("#");
            usuario = parts[4];
            amigo = parts[5];
            fecha_mensajes = parts[6];
            
            hebra.setUsuario(usuario);
            hebra.setAmigo(amigo);
            
            //2-Recoger el numero de mensajes totales
            num_mensajes_totales = hebra.controladorMensajes.get_messages_numero(usuario, amigo);
            
            //3-Recoger el numero de mensajes de una fecha si el num_mensajes es !=0
            if(num_mensajes_totales != 0){
                num_mensajes_fecha = hebra.controladorMensajes.get_messages_fecha(usuario, amigo,fecha_mensajes);
            }
            
            //3-Hacer la cadena
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());    
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#MSGS#"+usuario+"#"+amigo+"#"+num_mensajes_totales+"#"+num_mensajes_fecha;
            
        }else{
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#BAD_MSGPKG";
        }   
        
        //CAMBIARR POR EL NUMERO DE LA FECHA
        hebra.num_men_fecha=num_mensajes_totales;
        return theOutput;
        
        
        
//        //1-Descrifrar los dos usuarios
//        int empieza_usuario = entrada.indexOf("#MSGS#");
//        
//        if(empieza_usuario!=-1){
//            empieza_usuario+=6;
//            int ultima_almohadilla = entrada.lastIndexOf("#");
//            usuario = entrada.substring(empieza_usuario, ultima_almohadilla);
//            amigo = entrada.substring(ultima_almohadilla+1);
//        
//            hebra.setUsuario(usuario);
//            hebra.setAmigo(amigo);
//            
//            //2-Recoger el numero de mensajes totales
//            num_mensajes = hebra.controladorMensajes.get_messages_numero(usuario, amigo);
//            
//            //3-Hacer la cadena
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());    
//            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#MSGS#"+usuario+"#"+amigo+"#"+num_mensajes;
//            
//        }else{
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#BAD_MSGPKG";
//        }   
//        
//        hebra.num_men_fecha=num_mensajes;
//        return theOutput;
    }

    public String procesarEntradaMensajesSend(String entrada, MultiServerThread2 hebra, String usuario, String amigo, ArrayList mensajes) throws SQLException{
        String theOutput = null;
        //1- LLamar al controlador y al modelo para recojer un mensaje
        //Pasar i al controlador e ir de uno en uno 
        hebra.controladorMensajes.get_message_send(usuario, amigo,mensajes);
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#MSGS";
                
        return theOutput;  
    }
}

