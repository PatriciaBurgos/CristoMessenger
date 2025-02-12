/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_cliente;

import ClasesMapeadoras.MensajesMapeo;
import ClasesMapeadoras.UsuariosMapeo;
import front_cliente.VistaClienteChats;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;

/**
 *
 * @author patri
 */
public class ProtocoloCliente2 {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ConexionClienteconServer2 conexion;
    
    ProtocoloCliente2(ConexionClienteconServer2 conexion){
        this.conexion = conexion;
    }
    
    public void procesarEntradaServer(String entrada) throws InterruptedException{
        //SOLO PARA NUEVOS MENSAJES
        while(conexion.contador == 0){
            conexion.wait();
        }
        conexion.contador++;
        
        if(entrada.contains("#SERVER#CHAT#") && !entrada.contains("#MESSAGE_SUCCESFULLY_PROCESSED#")){
            String salida = this.procesarMensajeNuevo(entrada,conexion);            
            conexion.vchats.nuevo_mensaje(conexion.amigo);
            this.conexion.mandarSalida(salida);
        }           
        conexion.contador--;
        conexion.notifyAll();
    }
    
    
    public int contarCaracteres(String cadena, char caracter) {
        int posicion, contador = 0;
        //se busca la primera vez que aparece
        posicion = cadena.indexOf(caracter);
        while (posicion != -1) { //mientras se encuentre el caracter
            contador++;           //se cuenta
            //se sigue buscando a partir de la posición siguiente a la encontrada
            posicion = cadena.indexOf(caracter, posicion + 1);
        }
        return contador;
}
    
        
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
        String amigo;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       
        int empi = user_friend.indexOf(" ---");
        amigo = user_friend.substring(0, empi);
            
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#MSGS#"+user+"#"+amigo+"#"+auxDate1;
               
        
        return theOutput;
    }
    
    public void procesarConversacion_Numero(String salida_server, int num_men_totales,int num_men_dia){       
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
                
                boolean repetido = false;
                for(int j = 0; j<mensajes.size();j++){
                    repetido=this.conexion.array_mensajes_usuario.get(this.conexion.pos).comprobar_repetidos(men);
                }
                if(repetido==false){
                    mensajes.add(men);
                }
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
    
    public String procesarTodosLosDatos_Usuario(String nom_user){
        String theOutput = null;
        String usuario = nom_user;
        
        if(nom_user.contains(" --- ")){
            int empi = nom_user.indexOf(" ---");
            usuario = nom_user.substring(0, empi);
        }
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#ALLDATA_USER#"+usuario;
        
        return theOutput;
    }
    
    public String procesarDatosUsuario(String salida_server){
        String nombreCompleto = "";
        int comprobacion = salida_server.indexOf("#ALLDATA_USER#");
    
        String cadena_mensajes = salida_server.substring(comprobacion+1);
            
        String[] parts = cadena_mensajes.split("#");

        UsuariosMapeo usuario = new UsuariosMapeo();

        String login = parts[1];
        usuario.setId_user(login);

        String nombre = parts[2];
        usuario.setName(nombre);

        String apellido1 =parts[3];
        usuario.setSurname1(apellido1);

        String apellido2 =parts[4];
        usuario.setSurname2(apellido2);

        System.out.println(usuario.toString());    

        nombreCompleto = nombre + " " + apellido1 + " " + apellido2;
        
        return nombreCompleto;
    }
    
    public String procesarEnviarMensaje_texto(String texto, String usuario_origen, String usuario_destino){
        String theOutput = "";
        String usuario_dest = usuario_destino;
        
        if(usuario_destino.contains(" --- ")){
            int empi = usuario_destino.indexOf(" ---");
            usuario_dest = usuario_destino.substring(0, empi);
        }
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#CHAT#"+usuario_origen+"#"+usuario_dest+"#"+texto;

        return theOutput;
    }
    
    public String procesarMensajeNuevo(String entrada, ConexionClienteconServer2 conex){
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String[] parts = entrada.split("#");
        
        String login_usuario_del_mensaje = parts[4];
        String login_usuario_destino = parts[5];
        String mensaje = parts[6];
        String fechahora =parts[7];
        
        System.out.println("DM login_usuario_del_mensaje :"+ login_usuario_del_mensaje);
        
        
        for(int i = 0; i<conex.array_mensajes_usuario.size();i++){
//            System.out.println("DM mensajes.get(i).getId_user(): " + conex.array_mensajes_usuario.get(i).getId_user());
            if(conex.array_mensajes_usuario.get(i).getId_user().contains(login_usuario_del_mensaje)){
                conex.array_mensajes_usuario.get(i).num_men_recibidos++;
                
                MensajesMapeo mens = new MensajesMapeo();
                mens.setId_user_orig(login_usuario_del_mensaje);
                mens.setId_user_dest(login_usuario_destino);
                mens.setText(mensaje);
                mens.setDateTime(fechahora);
                mens.setSend(true);
                
                conex.array_mensajes_usuario.get(i).mensajes_array.add(mens);
//                System.out.println("DEBUG  1: " + conex.array_mensajes_usuario.get(i).mensajes_array.size());
                this.conexion.amigo = login_usuario_del_mensaje;
            }
        }
        
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#CHAT#RECEIVED_MESSAGE#"+login_usuario_del_mensaje+"#"+fechahora;
        
        return theOutput;
    }
    
    public String procesarEstadoUsuario_enviarServer(String usuario, UsuariosMapeo amigo){
        String theOutput = null;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String usuario_final ;
        
        if(amigo.getId_user().contains(" --- ")){
            int empi = amigo.getId_user().indexOf(" ---");
            usuario_final = amigo.getId_user().substring(0, empi);
        }else{
            usuario_final = amigo.getId_user();
        }
        
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#STATUS#"+usuario+"#"+usuario_final;

        return theOutput;
    }
    
    public String procesarEstadoUsuario (String entrada){
        String theOutput = null;
        
        String[] parts = entrada.split("#");        
        theOutput = parts[4]; //nom_amigo
        
        int comprobacion = entrada.indexOf("NOT");
        if(comprobacion == -1){ //conectado
            theOutput += " --- CONECTADO";
        }else{ //no conectado
            theOutput += " --- NO_CONECTADO";
        }

        return theOutput;
    }
    
    public String procesarObtenerFoto(String login){
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#GET_PHOTO#"+login;
        return theOutput;
    }
    
    public String procesarFoto(String entrada, FileOutputStream ficheroSalida, byte buffer[],int off) throws IOException{
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(entrada.contains("#STARTING_MULTIMEDIA_TRANSMISSION_TO#")){
            System.out.println("Voy a empezar a recibir cadenas");
        }
        
        if(entrada.contains("#SERVER#RESPONSE_MULTIMEDIA#")){
            String[] parts = entrada.split("#");
            String encodeCadena = parts[7];
            
            String s = new String(Base64.getDecoder().decode(encodeCadena));
            
            for (char c : s.toCharArray()) {
                ficheroSalida.write(c);//char 
            }            
        }        
        return theOutput;
    }
    
    public String procesarFotoUltimoMensaje (String entrada){
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(entrada.contains("#SERVER#ENDING_MULTIMEDIA_TRANSMISSION#")){
            String[] parts = entrada.split("#");        
            String user = parts[4];
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#CLIENT#PHOTO_RECEIVED#"+user;
        }
        return theOutput;
    }
}
