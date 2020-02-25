///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package back_cliente;
//
//import front_cliente.VistaClienteChats;
//import front_cliente.VistaClienteLogin;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JOptionPane;

/**
 *
 * @author patri
 */
//public class ConexionClienteconServer {
    
//    String ip,usuario,contraseña;
//    int puerto, contador;
//    ProtocoloCliente protocolo;
//    
//    Socket socket;
//    PrintWriter out;
//    BufferedReader in;
//    String fromServer, fromUser;
//    
//    int num_men_totales, num_men_dia;
//    String auxDate1;
//    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    
//    ArrayList<AmigosDeUnUsuario_Mensajes> array_mensajes_usuario;
//    
//    ThreadListeningNewMessages hebra_recibir_mensajes;
//    ThreadEstadoAmigos hebra_estados;
//    
//    VistaClienteChats vchats;
//    
//    public ConexionClienteconServer(String IP, int port, String user, String pass, ArrayList<AmigosDeUnUsuario_Mensajes> array_mensajes_usuario) throws IOException{
//        ip = IP;
//        puerto = port;
//        usuario = user;
//        contraseña = pass;
//        protocolo = new ProtocoloCliente();
//        socket = new Socket(ip, puerto);
//        out = new PrintWriter(socket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        contador = 1;
//        
//        System.out.println("CLIENT: IP-->" + ip + " PUERTO--> " + puerto + " USUARIO--> " + usuario + " CONTRASEÑA--> " +contraseña);
////        VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT:IP-->" + ip + " PUERTO--> " + puerto + " USUARIO--> " + usuario + " CONTRASEÑA--> " +contraseña+ "\n");
//         
//        //Creo una hebra que este escuchando por si le envían un mensaje
//        hebra_recibir_mensajes = new ThreadListeningNewMessages (this);
//        this.hebra_recibir_mensajes.start();
//        
//        hebra_estados = new ThreadEstadoAmigos(this);        
//        hebra_estados.start();
//
//        this.array_mensajes_usuario = array_mensajes_usuario;
////        
//    }
//
//    public int getNum_men_totales() {
//        return num_men_totales;
//    }
//
//    public void setNum_men_totales(int num_men_totales) {
//        this.num_men_totales = num_men_totales;
//    }
//
//    public int getNum_men_dia() {
//        return num_men_dia;
//    }
//
//    public void setNum_men_dia(int num_men_dia) {
//        this.num_men_dia = num_men_dia;
//    }
//    
//    
//    
//    
//    
//    synchronized public ArrayList conectar_con_server_login() throws IOException, InterruptedException{
//        ArrayList <String> salida = new ArrayList(); 
//        
//        while(contador == 0){
//            wait();
//        }       
//        contador++;
//        fromUser = protocolo.procesarEntradaLogin(usuario,contraseña);
//
//        //Lo envío al servidor (1)  
//        if (fromUser != null) {
//            System.out.println("CLIENT TO SERVER: " + fromUser);
//            out.println(fromUser); //Envia por el socket
//        }
//
//        //Me lo envía el servidor (2)
//        if((fromServer = in.readLine()) != null){
//            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//            salida = protocolo.procesarSalidaLogin(fromServer);
//            
//            if(fromServer.contains("#SERVER#LOGIN_CORRECT#")){
//                //this.hebra_recibir_mensajes.start();
//                //PONER UN IF POR SI NO TENGO AMIGOS
//                //ACTUALIZAR LA VISTA CUANDO ACTUALICE
//                //VER EL ERROR QUE ME DA
////                this.hebra_estados.start();
//            }            
//        }      
//        contador--;
//        notifyAll();
//        return salida;
//    }
//    
//    
//    synchronized public void conectar_con_server_leer_mensajes(String usuario_dest,ArrayList <AmigosDeUnUsuario_Mensajes> array_amigos_mensajes, int pos) throws IOException, InterruptedException{
//        //HACER DO WHILE POR SI EN UNA FECHA NO HAY MENSAJES PERO SI HAY EN LA CONVERSACION 
//        while(contador == 0){
//            wait();
//        }
//        contador++;
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        if(array_amigos_mensajes.get(pos).ultima_fecha_buscada==null){
//            auxDate1 = sdf.format(timestamp);
//        }else{
//            auxDate1 = array_amigos_mensajes.get(pos).ultima_fecha_buscada;
//        }        
//       
//        do{
//            //1-Pido los mensajes en una fecha y se lo mando al server            
//            this.pedir_conversaciones_server_fecha(usuario_dest);
//
//            //2-Recivo del server el numero de mensajes totales de la conversacion y el numero de mensajes totales de un dia
//            this.procesar_entrada_server_numero_mensajes();
//
//            //3-Compruebo que los numeros enviados por el server son != 0
//            if(this.num_men_totales!=0){//Si el total de conversacion == 0 no hay mensajes
//                if(this.num_men_dia!=0){
//                    this.enviar_al_server_ok_send();
//                    this.recibir_mensajes_un_dia(array_amigos_mensajes,pos);
//                    this.enviar_al_server_all_received();
//                }else{//Pido al server los mensajes de otro dia(programado en el protocolo)
//                    //BORRAR ELSE
//                }
//            }
//        }while(this.num_men_dia==0 && this.num_men_totales!=0 && array_amigos_mensajes.get(pos).getNum_men_recibidos() != this.num_men_totales);
//       
//        array_amigos_mensajes.get(pos).setUltima_fecha_buscada(auxDate1);
//        
//        contador--;
//        notifyAll();
//    }
//    
//    synchronized public void conectar_con_server_obtener_datos(String nom_user, VistaClienteChats vchats) throws IOException, InterruptedException{
//        while(contador == 0){
//            wait();
//        }
//        contador++;
//        //1- Llamar al protocolo para hacer la cadena
//        fromUser = this.protocolo.procesarTodosLosDatos_Usuario(nom_user);
//        
//        //2- Enviar al server la cadena formada
//        this.pedir_al_server_datos_usuario();
//        
//        //3- El server nos devuelve la cadena con los datos del usuario y lo mandamos al protocolo
//        String nombreCompleto = this.procesar_entrada_server_datos();
//        
//        //4- Guardamos en el array los datos(Es una opcion)
//        if(nom_user.equals(usuario)){
//            vchats.label_usuario_activo.setText(nombreCompleto);
//        }else{
//            vchats.label_amigo.setText(nombreCompleto);
//        }
//        contador--;
//        notifyAll();
//    }
//    
//    synchronized public void conectar_con_server_enviar_mensajes(String texto, String usuario_origen, String usuario_dest) throws InterruptedException{
//        while(contador == 0){
//            wait();
//        }
//        contador++;
//        //1- Llamo al protocolo para hacer la cadena
//        fromUser = this.protocolo.procesarEnviarMensaje_texto(texto, usuario_origen, usuario_dest);
//        
//        //2- Envio la cadena al server
//        this.enviar_al_server_texto_mensaje(); 
//        
//        contador--;
//        notifyAll();
//    }
//    
//    
//    public void pedir_conversaciones_server_fecha(String usuario_dest){
//        
//        fromUser = protocolo.procesarPedirConversacion(this.usuario, usuario_dest, this.auxDate1);
//        this.auxDate1 = sdf.format(Timestamp.valueOf(auxDate1).getTime() - (24*60*60*1000));
//         
//        //Envio al server que quiero los mensajes en una fecha
//        if (fromUser != null) {
//            System.out.println("CLIENT TO SERVER: " + fromUser);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//            out.println(fromUser); //Envia por el socket            
//        }
//    }
//    
//    public void procesar_entrada_server_numero_mensajes() throws IOException{
//        if((fromServer = in.readLine()) != null){
//            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
//            
//            num_men_totales=0;
//            num_men_dia = 0;
//
//            //Aqui solo saco los numeros de las conversaciones
//            protocolo.procesarConversacion_Numero(fromServer, num_men_totales,num_men_dia, this);
//        }
//    }
//    
//    public void enviar_al_server_ok_send(){
//        fromUser = protocolo.procesarConversacion_ok_send();
//        if (fromUser != null) {
//            System.out.println("CLIENT TO SERVER: " + fromUser);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//            out.println(fromUser); //Envia por el socket            
//        }
//    }
//    
//    synchronized public void recibir_mensajes_un_dia(ArrayList <AmigosDeUnUsuario_Mensajes> array_amigos_mensajes, int pos) throws IOException, InterruptedException{
//       // array_amigos_mensajes.get(pos).mensajes_array.clear();
//        while(contador == 0){
//            wait();
//        }
//        contador++;
//        //Me traigo los mensajes de un usuario en una fecha
//        for(int i=0; i<this.num_men_dia; i++){
//            //Si me envía algún mensaje
//            if((fromServer = in.readLine()) != null){
//                System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
//                protocolo.procesarConversacion_Mensajes(fromServer, array_amigos_mensajes.get(pos).mensajes_array, num_men_totales);   
//            }
//            array_amigos_mensajes.get(pos).num_men_recibidos++;
//        }    
//        contador--;
//        notifyAll();
//    }
//    
//    public void enviar_al_server_all_received(){
//        fromUser = protocolo.procesarConversacion_all_received();
//        if (fromUser != null) {
//            System.out.println("CLIENT TO SERVER: " + fromUser);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//            out.println(fromUser); //Envia por el socket            
//        }
//    }
//        
//    public void pedir_al_server_datos_usuario(){
//        if (fromUser != null) {
//            System.out.println("CLIENT TO SERVER: " + fromUser);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//            out.println(fromUser); //Envia por el socket            
//        }
//    }
//    
//    public String procesar_entrada_server_datos() throws IOException{
//        String nombreCompleto = "";
//        if((fromServer = in.readLine()) != null){
//            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
//            nombreCompleto = protocolo.procesarDatosUsuario(fromServer);   
//        }
//        return nombreCompleto;
//    }
//    
//    public void enviar_al_server_texto_mensaje (){
//        if (fromUser != null) {
//            System.out.println("CLIENT TO SERVER: " + fromUser);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//            out.println(fromUser); //Envia por el socket            
//        }
//    }
//    
//    synchronized public void recibo_mensaje() throws InterruptedException{
//        while(contador == 0){
//            wait();
//        }
//        contador++;
//        if(!fromServer.contains("#MESSAGE_SUCCESFULLY_PROCESSED#")){
//            //RECIVO DEL SERVIDOR
//            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
//            //Guardo el mensaje y hago la cadena para enviarla al server
//            fromUser = protocolo.procesarMensajeNuevo(fromServer,this.array_mensajes_usuario);
//            
//            //ENVIO AL SERVIDOR
//            if (fromUser != null) {
//                System.out.println("CLIENT TO SERVER: " + fromUser);
//                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//                out.println(fromUser); //Envia por el socket            
//            }
//
//        }else{
//            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
//        }
//        contador--;
//        notifyAll();
//    }
//    
//    synchronized public void comprobar_estados() throws IOException, InterruptedException{
//        while(contador == 0){
//            wait();
//        }
//        contador++;
//        String estado;
//        for(int i= 0; i<this.array_mensajes_usuario.size();i++){
//            fromUser = this.protocolo.procesarEstadoUsuario_enviarServer(this.usuario,this.array_mensajes_usuario.get(i));
//            //ENVIO AL SERVIDOR
//            if (fromUser != null) {
//                System.out.println("CLIENT TO SERVER: " + fromUser);
//                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//                out.println(fromUser); //Envia por el socket            
//            }
//            
//            //RECIVO DEL SERVIDOR
//            if((fromServer = in.readLine()) != null){
//                System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//                VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
//                estado = protocolo.procesarEstadoUsuario(fromServer);   
//                this.array_mensajes_usuario.get(i).setId_user(estado);
//            }
//        }    
//        VistaClienteChats.actualizar_estados_vista();
//        contador--;
//        notifyAll();
//    }
//    
//    synchronized public void obtenerFoto(String login) throws IOException, InterruptedException{
//        while(contador == 0){
//            wait();
//        }
//        contador++;
//        //1- Llamo al método del protocolo para que me convierta la cadena
//        this.fromUser = protocolo.procesarObtenerFoto(login);
//        //2-Envio la cadena al server
//        //ENVIO AL SERVIDOR
//        if (fromUser != null) {
//            System.out.println("CLIENT TO SERVER: " + fromUser);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT TO SERVER: " + fromUser+ "\n");
//            out.println(fromUser); //Envia por el socket            
//        }
//        //3-Recivo del servidor que me va a empezar a mandar los bytes de la foto
//        //RECIVO DEL SERVIDOR
//        if((fromServer = in.readLine()) != null){
//            System.out.println("CLIENT RECEIVE TO SERVER: " + fromServer);
//            VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText()+ "CLIENT RECEIVE TO SERVER: " + fromServer+ "\n");
//        }
//        if(fromServer.contains("#SERVER#STARTING_MULTIMEDIA_TRANSMISSION_TO#")){
//            //4-Voy a empezar a recivir los bytes en forma de protocolo. Mandar al protocolo
//            
//            //5-Decodificar los bytes de la cadena
//            
//            //6-Guardarlos en memoria local
//            
//            
//        }
//        
//        
//        contador--;
//        notifyAll();
//    }
//    
//}   
    
    
    
    