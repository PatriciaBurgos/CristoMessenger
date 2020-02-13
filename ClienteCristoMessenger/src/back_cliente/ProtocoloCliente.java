/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_cliente;

import ClasesMapeadoras.MensajesMapeo;
import front_cliente.VistaClienteChats;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author patri
 */
public class ProtocoloCliente {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //Timestamp auxDate = new Timestamp(System.currentTimeMillis());
    //String auxDate1 = null;
        
    public String procesarEntradaLogin(String usuario, String contraseña) {
        String theOutput = null;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        theOutput =  "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#LOGIN#"+usuario+"#"+contraseña;
        
        System.out.println("CLIENT TO SERVER: " + theOutput);
//        VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + theOutput + "\n");
               
        return theOutput;
    }
    
    public ArrayList procesarSalidaLogin(String salidaServer){
        ArrayList <String> nombre_amigos = new ArrayList();
        
        int comprobacion_cadena_error = salidaServer.indexOf("#SERVER#ERROR#BAD_LOGIN");
        if(comprobacion_cadena_error != -1){
            nombre_amigos.add("BAD_LOGIN");
            System.out.println("CLIENT RECEIVE: " + salidaServer);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE: PROTOCOLCRISTOMESSENGER1.0#SERVER#ERROR#BAD_LOGIN" + "\n");
            
        } else{
            int empiezaAmigos = salidaServer.indexOf("FRIENDS"); //Donde empieza friends
            //empiezaAmigos += 9; //empieza la cadena del numero de amigos
            String num_amigos = salidaServer.substring(empiezaAmigos+8); //De el numero hacia delante(la cadena)
            int acaba_amig = num_amigos.indexOf("#"); //Donde hay otro # para saber donde acaban los amigos
            int amigos = Integer.valueOf(num_amigos.substring(0, acaba_amig)); //numero de amigos           
            String empiezan_nombres = salidaServer.substring(empiezaAmigos+8+acaba_amig+1);

                        
            int acaba_nom;
            String nom;
            
            for(int i = 0; i<amigos; i++){
                acaba_nom=0;
                acaba_nom = empiezan_nombres.indexOf("#");
                nom = empiezan_nombres.substring(0,acaba_nom);
                String comprobacion_con = empiezan_nombres.substring(0,acaba_nom+5);
                //CONNECTED OR NOT
                int comprobacion = comprobacion_con.indexOf("NOT");
                if(comprobacion == -1){ //conectado
                    nom += " --- CONECTADO";
                    nombre_amigos.add(nom);
                    acaba_nom+=11;
                }else{ //no conectado
                    nom += " --- NO_CONECTADO";
                    nombre_amigos.add(nom);
                    acaba_nom +=15;
                }
                if(i<amigos-1){
                    //acaba_nom += empiezan_nombres.indexOf("#");
                    empiezan_nombres = empiezan_nombres.substring(acaba_nom);
                }
            }                
        }        
        return nombre_amigos;
    }
    
    public String procesarPedirConversacion(String user,String user_friend, String auxDate1){
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       
        int empi = user_friend.indexOf(" ---");
        String amigo = user_friend.substring(0, empi);
        
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#MSGS#"+user+"#"+amigo+"#"+auxDate1;
               
        
        return theOutput;
    }
    
    public void procesarConversacion_Numero(String salida_server, int num_men_totales,int num_men_dia, ConexionClienteconServer conexion){       
        int comprobacion_validez = salida_server.indexOf("#SERVER#BAD_MSGPKG");        
        if(comprobacion_validez ==-1){ //Mensaje correcto
            
            String[] parts = salida_server.split("#");        
            num_men_totales = Integer.valueOf(parts[6]); 
            num_men_dia = Integer.valueOf(parts[7]); //Poner el 7 que son los mensajes en una fecha
        
            conexion.setNum_men_totales(num_men_totales);
            conexion.setNum_men_dia(num_men_dia);                        
        }else{
            System.out.println("Mal paquete");
        }
    }
    
    public String procesarConversacion_ok_send (){
        String theOutput = null;
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#MSGS#OK_SEND!";
        
        return theOutput;
    }
    
    public void procesarConversacion_Mensajes(String salida_server, ArrayList <MensajesMapeo> mensajes, int num_men){
        
        int comprobacion = salida_server.indexOf("#SERVER#MSGS#");
        if(comprobacion != -1){
            comprobacion+=13;
            
            String cadena_mensajes = salida_server.substring(comprobacion);
            
            String[] parts = cadena_mensajes.split("#");
            for(int i = 0;i<parts.length; i++){
                MensajesMapeo men = new MensajesMapeo();
                String emisor = parts[i];
                i++;
                men.setId_user_orig(emisor);
                String receptor =parts[i];
                i++;
                men.setId_user_dest(receptor);
                String fechahora = parts[i];
                i++;
                men.setDateTime(fechahora);
                String texto = parts[i];
                i++;
                men.setText(texto);
                
                mensajes.add(men);
            }            
            
        }else{
            //Error de paquete
            System.out.println("Error de paquete en mensajes");
        }
    }
    
    public String procesarConversacion_all_received(){
        String theOutput = null;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#MSGS#ALL_RECEIVED";
        return theOutput;        
    }
    
}
