/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_servidor.controladorBD;

import back_servidor.modeloBD.MensajesMapeo;
import back_servidor.modeloBD.UsuariosMapeo;
import front_servidor.VistaServer;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author patri
 */
public class ProtocoloServer2 {
    MultiServerThread3 hebra;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String salida,usuario,amigo,fechahora_men_leer,entrada_cliente;
    public int num_men_fecha;
    public int contador;
    
    ProtocoloServer2(MultiServerThread3 hebra){
        this.hebra = hebra;
        contador=1;
    }
    
    synchronized public void procesarEntrada(String entrada) throws SQLException, IOException, InterruptedException{
        while(contador == 0){
            wait();
        }       
        contador++;
        
        //Compruebo que esta bien lo primero del protocolo
        this.entrada_cliente = entrada;
        if(entrada_cliente.contains("PROTOCOLCRISTOMESSENGER1.0")){


            //COMPROBACION LOGIN
            if(entrada_cliente.contains("#CLIENT#LOGIN#")){
                salida = this.procesarEntradaLogin();
                hebra.mandar_salida(salida);
                if(salida.contains("#SERVER#ERROR#BAD_LOGIN")){
                    hebra.desconectar();
                }
            }


            //COMPROBACION LEER MENSAJES
            int comprobacion_entrada_cliente_mensajes = entrada_cliente.indexOf("#MSGS#");             
            if(comprobacion_entrada_cliente_mensajes != -1){
                //COMPROBACION CADENA DE CLIENTE
                int comprobacion_entrada_cliente_mensajes_send = entrada_cliente.indexOf("OK_SEND!");
                int comprobacion_entrada_cliente_mensajes_received =entrada_cliente.indexOf("ALL_RECEIVED");

                //1 CADENA CLIENTE NUMERO DE MENSAJES TOTAL Y DE UNA FECHA 
                if((comprobacion_entrada_cliente_mensajes != -1) && (comprobacion_entrada_cliente_mensajes_send==-1) && (comprobacion_entrada_cliente_mensajes_received==-1)){

                    salida = this.procesarEntradaMensajesNumero();
                    hebra.mandar_salida(salida);                            

                    //2 CADENA CLIENTE OK SEND
                }else if (comprobacion_entrada_cliente_mensajes_send != -1){
                    ArrayList<MensajesMapeo> mensajes = new ArrayList();
                    this.salida = this.procesarEntradaMensajesSend(mensajes);
                    String cadena = salida;
                    for(int i = 0; i<mensajes.size(); i++){
                        salida = this.procesarEnvioMensajesSend(mensajes.get(i),cadena);
                        hebra.mandar_salida(salida);
                    }


                    //3 CADENA CLIENTE ALL RECEIVED
                }else if(comprobacion_entrada_cliente_mensajes_received != -1){ 
                    System.out.println("SERVER: El cliente ha recibido todos los mensajes");
                    VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: El cliente ha recibido todos los mensajes" + "\n");
                }                    
            }


            //COMPROBACION DATOS USUARIO
            if(entrada_cliente.contains("#ALLDATA_USER#")){
                //1- Llamo al protocolo para que me descifre la cadena y 
                    //1)Compruebe que es un usuario del sistema
                    //2)Recolecte toda la informacion
                    //3)Se mande por el socket
                this.salida = this.procesarEntradaObtenerDatos();
                //2- Mando los datos o el error al usuario
                this.hebra.mandar_salida(salida);                    
            }


            //COMPROBACION ENVIAR MENSAJE
            if(this.entrada_cliente.contains("#CLIENT#CHAT#")){
                if(!this.entrada_cliente.contains("#RECEIVED_MESSAGE#")){                    
                    //1- Llamo al protocolo para que me descrifre la cadena y
                        //1)- Compruebe que los dos usuarios estan en el sistema
                        //2)- Compruebe que los dos usuarios son amigos
                        //3)- Guarde en la base de datos el mensaje
                    this.salida = this.procesarEntradaEnviarMensaje();
                    if(salida == ""){
                        //2- Comprobar a traves del socket que el cliente destino esta conectado
                        boolean check_conex = false;
                        check_conex = this.hebra.comprobar_conexion_hebra();

                        if(check_conex == true){//EL CLIENTE ESTA CONECTADO

                            //3- Si esta conectado enviar cadena al protocolo para transformarla en otra
                            this.salida = this.procesarSalidaEnviarMensajeClienteDestino();

                            //4- Enviar cadena al usuario destino
                            this.hebra.mandar_al_cliente_destino_nuevo_mensaje(salida);
                        }else{
                            System.out.println("SERVER: El cliente destino no esta conectado");
                            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: El cliente destino no esta conectado" + "\n");
                        }

                    }else{//Ha habido un error en la cadena
                        this.hebra.mandar_salida(salida);
                    }

                }else if(this.entrada_cliente.contains("#RECEIVED_MESSAGE#")){ 
                    //Este mensaje es del cliente destino
                    //1- Cambiar booleano read de la bd                    
                    this.salida = this.procesarSalidaNotificarMensajeClienteOrigen();
                    
                    //2- Mandar cadena al cliente origen 
                        //1) Comprobar que el cliente origen esta conectado
                        //2) Acceder a su socket para mandarle un mensaje
                    this.hebra.mandar_al_cliente_origen_mensaje_bien_procesado(salida);
                }
            }


            //COMPROBACION ESTADOS
            if(this.entrada_cliente.contains("#CLIENT#STATUS#")){
                //1- Descifrar cadena
                //2- Comprobar la conexion
                //3- Cambiar el atributo de conexion en la bd de ese usuario
                //4- Mandar estado al cliente
                
                this.salida = this.procesarEntradaEstado();
                this.hebra.mandar_salida(this.salida);
                                                        
            }


            //COMPROBACION OBTENER FOTO
            if(this.entrada_cliente.contains("#CLIENT#GET_PHOTO#")){
                //1-Descrifrar cadena y sacar el login
                String login_foto = this.procesarEntradaObtenerFoto();
                //2-Notificar al cliente que se va a empezar a mandar la foto
                this.salida = this.procesarSalidaNotificarEnvioFoto(login_foto);
                this.hebra.mandar_salida(this.salida);
                
                //3-Llamo a un método para traerme la foto
                this.hebra.obtener_paquetes_foto(login_foto);
                //INTENTAR NO LLAMAR TANTO A LA HEBRA PARA QUE NO SEA SINCRONO
                
            }

        //ERROR EN EL PRINCIPIO DE LA CADENA
        }else{
            salida = procesarErrorPrincipioCadena(); 
            this.hebra.mandar_salida(salida);
        }
        
        contador--;
        notifyAll();
    }
    
    public String procesarErrorPrincipioCadena(){
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#ERROR";
        return theOutput;
    }
    
    public String procesarEntradaLogin() throws SQLException {
        String theOutput = "";
        String login = "";
        String contraseña;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        //Ajusto la cadena para quedarme con el usuario
        int comprob_entrada = entrada_cliente.indexOf("#CLIENT#LOGIN#");
        if(comprob_entrada!=-1){
            int ultimaAlmohadilla = entrada_cliente.lastIndexOf("#");
            int empieza_usuario = entrada_cliente.indexOf("LOGIN");
            login = entrada_cliente.substring(empieza_usuario+6, ultimaAlmohadilla);
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: Usuario introducido (falta comprobacion): " + login + "\n");
            System.out.println("SERVER: Usuario introducido (falta comprobacion): " + login);
            
            //Ajusto la cadena para quedarme con la contraseña
            contraseña = entrada_cliente.substring(ultimaAlmohadilla+1);
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER:Contraseña introducida (falta comprobacion): " + contraseña + "\n");
            System.out.println("SERVER:Contraseña introducida (falta comprobacion): " + contraseña);
            
            //Conecto con la base de datos y compruebo si esta o no
            boolean check = hebra.controladorUsuario.check_user_login(login, contraseña);            
            
            //Salida
            if(check == false){
                theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#ERROR#BAD_LOGIN";
            }else{
                //Como existe el login me traigo todos los amigos 
                ArrayList array_amigos = new ArrayList();
                hebra.controladorAmigos.get_all_friends_con(array_amigos, login);

                //Salida                
                theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#LOGIN_CORRECT#"+login+"#FRIENDS#"+array_amigos.size()+"#";
                
                //TODO ME ESTA MANDANDO UNA # al final
                for(int i = 0; i<array_amigos.size(); i++){
                    if(i==array_amigos.size()-1){
                        theOutput += array_amigos.get(i) + "#";
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
            
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#ERROR#BAD_LOGIN";
            System.out.println("SERVER: Error de cadena (ALERTA HACKER");
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: Error de cadena (ALERTA HACKER)" + "\n");
        }
        
        this.hebra.usuario = login;
        
        //busco a ver si hay una hebra con ese nombre de usuario
        boolean check = this.hebra.multiServer.buscar_en_hebras_conectadas(login);
        if(check == true){
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#ERROR#BAD_LOGIN";
        }
        
        usuario=login;
        this.hebra.setName(usuario);
        
        this.conectar();
        
        return theOutput;
    }
    
    public String procesarEntradaMensajesNumero () throws SQLException{
        String theOutput = "";
        int num_mensajes_totales=0, num_mensajes_fecha=0;
        String fecha_mensajes = "";     
        
        //1-Compruebo que contiene el MSGS
        if(entrada_cliente.contains("#MSGS#")){
            String[] parts = entrada_cliente.split("#");
            String login = parts[4];
            String friend = parts[5];
            fecha_mensajes = parts[6];
            
            this.usuario = login;
            this.amigo = friend;
            
            //2-Recoger el numero de mensajes totales
            num_mensajes_totales = hebra.controladorMensajes.get_messages_numero(login, friend);
            
            //3-Recoger el numero de mensajes de una fecha si el num_mensajes es !=0
            if(num_mensajes_totales != 0){
                num_mensajes_fecha = hebra.controladorMensajes.get_messages_fecha(login, friend,fecha_mensajes);
            }
            
            //3-Hacer la cadena
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());    
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#MSGS#"+login+"#"+friend+"#"+num_mensajes_totales+"#"+num_mensajes_fecha;
            
        }else{
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#BAD_MSGPKG";
        }   
        
        num_men_fecha = num_mensajes_fecha;
        fechahora_men_leer = fecha_mensajes;
        return theOutput;        
    }

    
    public String procesarEntradaMensajesSend(ArrayList mensajes) throws SQLException{
        String theOutput = "";
        //1- LLamar al controlador y al modelo para recojer mensajes
        hebra.controladorMensajes.get_message_send(usuario, amigo,mensajes,fechahora_men_leer);
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#MSGS";
                
        return theOutput;  
    }
    
    public String procesarEnvioMensajesSend(MensajesMapeo men, String cadena){
        String theOutput ="";
        theOutput = cadena+men.toString();  
        return theOutput;
    }
    
    public String procesarEntradaObtenerDatos (){
        String theOutput = "";
        boolean check = false;
        
        //1)Sacar el usuario de la cadena de entrada
        int empieza_user = entrada_cliente.lastIndexOf("#");
        String user = entrada_cliente.substring(empieza_user+1);
        
        //2)Comprobacion usuario logado
        check = hebra.controladorUsuario.check_user(user);
       
        //3)Si esta logeado, me traigo datos
        if(check == true){
            UsuariosMapeo usuario_datos = new UsuariosMapeo();
            usuario_datos.setId_user(user);
            hebra.controladorUsuario.get_data_user(usuario_datos);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#ALLDATA_USER#"+usuario_datos.getId_user()+""
                    + "#"+usuario_datos.getName()+"#"+usuario_datos.getSurname1()+"#"+usuario_datos.getSurname2();
        }else{
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#BAD_DATA";
        }
        
        //4)Mando la cadena
        return theOutput;
    }
    
    public String procesarEntradaEnviarMensaje() throws SQLException{        
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        boolean check_user_orig = false, check_user_dest = false, check_friendship=false;
        
        String[] parts = entrada_cliente.split("#");
        String fechahora = parts[1];
        String usuario_origen = parts[4];
        String usuario_destino = parts[5];
        String texto = parts[6];
        
        //1)- Compruebar que los dos usuarios estan en el sistema
        check_user_orig = hebra.controladorUsuario.check_user(usuario_origen);
        check_user_dest = hebra.controladorUsuario.check_user(usuario_destino);
        
        if(check_user_orig == true && check_user_dest == true){
            //2)- Compruebe que los dos usuarios son amigos
            check_friendship = hebra.controladorAmigos.check_friendship(usuario_origen, usuario_destino);
            
            if(check_friendship == true){
                //3)- Guarde en la base de datos el mensaje 
                hebra.controladorMensajes.save_message(usuario_origen, usuario_destino,texto,fechahora);
//                hebra.setUsuario_destino(usuario_destino);
//                hebra.setFechahora_men_enviado(fechahora);
                
            }else{
                theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#FORBIDDEN_CHAT";
            }
          
        }else{
            theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#FORBIDDEN_CHAT";
        }
        hebra.usuario=usuario_origen;
        hebra.usuario_destino=usuario_destino;
        hebra.fechahora_men_enviado=fechahora;
        return theOutput;
    }
    
    public String procesarSalidaEnviarMensajeClienteDestino(){
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        String[] parts = entrada_cliente.split("#");
        String fechahora = parts[1];
        String usuario_origen = parts[4];
        String usuario_destino = parts[5];
        String texto = parts[6];
        
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#CHAT#"+usuario_origen+"#"+usuario_destino+"#"+texto+"#"+fechahora;
        
        hebra.usuario=usuario_origen;
        hebra.usuario_destino=usuario_destino;
        hebra.fechahora_men_enviado=fechahora;
        
        return theOutput;
    }
    
    public String procesarSalidaNotificarMensajeClienteOrigen () throws SQLException{
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        String[] parts = entrada_cliente.split("#");
        String usuario_origen = parts[5];
        String fechahora = parts[6];
        
        //Cambio el booleano read de la bd
        hebra.controladorMensajes.change_boolean_read(usuario_origen, usuario, fechahora);
        hebra.usuario_destino=usuario_origen;
        
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#CHAT#"+usuario_origen+"#"+usuario+"#MESSAGE_SUCCESFULLY_PROCESSED#"+fechahora;
        
        return theOutput;
    }
    
    public String procesarEntradaEstado() throws SQLException{
        boolean check = false;
        String theOutput = "", estado_conex;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        String[] parts = entrada_cliente.split("#");
        String friend = parts[5];
//        hebra.setAmigoestado(friend);
        check = hebra.multiServer.buscar_en_hebras_conectadas(friend);
        
        //CAMBIO ATRIBUTO EN BD
        hebra.controladorUsuario.cambiarestado(friend, check);
        if(check==true){
            estado_conex = "CONNECTED";
        }else{
            estado_conex = "NOT_CONNECTED";
        }
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#STATUS#"+friend+"#"+estado_conex;
                
        return theOutput;
    }
    
    public String procesarEntradaObtenerFoto(){        
        String[] parts = entrada_cliente.split("#");
        String login = parts[4];
        return login;
    }
    
    public String procesarSalidaNotificarEnvioFoto(String usuario){
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#STARTING_MULTIMEDIA_TRANSMISSION_TO#"+usuario;
        
        return theOutput;
    }
    
    public String procesarFotoMandarACliente(ArrayList<String> encodeLines, String usuario, int total_bytes, int total){
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#RESPONSE_MULTIMEDIA#"+usuario+"#"+total+"#"+total_bytes+"#"+encodeLines.get(0).toString();
        
        return theOutput;
    }
    
    public String procesarSalidaNotificarFinFoto(String login){
        String theOutput = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        theOutput = "PROTOCOLCRISTOMESSENGER1.0#"+sdf.format(timestamp)+"#SERVER#ENDING_MULTIMEDIA_TRANSMISSION#"+login;
        
        return theOutput;
    }
    
    public void desconectar() throws SQLException{
        hebra.controladorUsuario.cambiarestado(hebra.usuario, false);
    }
    
    public void conectar() throws SQLException{
        hebra.controladorUsuario.cambiarestado(hebra.usuario, true);
    }
}