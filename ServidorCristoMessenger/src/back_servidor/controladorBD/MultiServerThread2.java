/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_servidor.controladorBD;

import back_servidor.MultiServer;
import back_servidor.modeloBD.MensajesMapeo;
import front_servidor.VistaServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patri
 */
public class MultiServerThread2 extends Thread {
    private Socket socket = null;
    boolean conectar = true;
    MultiServer multiServer;
    
    ControladorUsuario controladorUsuario;
    ControladorAmigos controladorAmigos;
    ControladorMensajes controladorMensajes;
    
    ProtocoloServer protocolo;
    PrintWriter out;
    BufferedReader in;
    
    String entrada_cliente, salida_server;   
    
    public int num_men_fecha;
    String usuario,amigo,usuario_destino,fechahora_men_enviado, fechahora_men_leer, amigoestado;
    
    
    public MultiServerThread2(Socket socket, int id, MultiServer multiserver) throws SQLException, IOException {
        super("Thread " + id);
        System.out.println("SERVER: THREAD " + id);
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: THREAD "+id+ "\n");
        
        this.multiServer = multiserver;
        this.socket = socket;
        controladorUsuario = new ControladorUsuario();
        controladorAmigos = new ControladorAmigos();
        controladorMensajes = new ControladorMensajes();
        protocolo = new ProtocoloServer();
        
        out = new PrintWriter(socket.getOutputStream(), true);
        in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAmigo() {
        return amigo;
    }

    public void setAmigo(String amigo) {
        this.amigo = amigo;
    }

    public String getUsuario_destino() {
        return usuario_destino;
    }

    public void setUsuario_destino(String usuario_destino) {
        this.usuario_destino = usuario_destino;
    }

    public String getFechahora_men_enviado() {
        return fechahora_men_enviado;
    }

    public void setFechahora_men_enviado(String fechahora_men_enviado) {
        this.fechahora_men_enviado = fechahora_men_enviado;
    }

    public String getFechahora_men_leer() {
        return fechahora_men_leer;
    }

    public void setFechahora_men_leer(String fechahora_men_leer) {
        this.fechahora_men_leer = fechahora_men_leer;
    }

    public String getAmigoestado() {
        return amigoestado;
    }

    public void setAmigoestado(String amigoestado) {
        this.amigoestado = amigoestado;
    }
    
    

     
    public void run() {    
        while(conectar){
            try {
                //Recivo una cadena del cliente
                entrada_cliente = in.readLine();
            } catch (IOException ex) {
                Logger.getLogger(MultiServerThread2.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("CLIENT TO SERVER: "+entrada_cliente);
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "CLIENT TO SERVER: " +entrada_cliente+ "\n");

            //Compruebo que esta bien lo primero del protocolo                
            if(entrada_cliente.contains("PROTOCOLCRISTOMESSENGER1.0")){
                
                
                //COMPROBACION LOGIN
                if(entrada_cliente.contains("#CLIENT#LOGIN#")){
                    try {
                        this.hebra_login();
                    } catch (SQLException ex) {
                        Logger.getLogger(MultiServerThread2.class.getName()).log(Level.SEVERE, null, ex);
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
                        try {
                            salida_server = protocolo.procesarEntradaMensajesNumero(entrada_cliente, this, usuario, amigo);
                            this.mandar_salida(salida_server);
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(MultiServerThread2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        //2 CADENA CLIENTE OK SEND
                    }else if (comprobacion_entrada_cliente_mensajes_send != -1){
                        try {
                            this.hebra_mensajes_send(usuario,amigo,fechahora_men_leer);
                        } catch (SQLException ex) {
                            Logger.getLogger(MultiServerThread2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        //3 CADENA CLIENTE ALL RECEIVED
                    }else if(comprobacion_entrada_cliente_mensajes_received != -1){ 
                        System.out.println("SERVER: El cliente ha recibido todos los mensajes");
                        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: El cliente ha recibido todos los mensajes" + "\n");
                    }                    
                }
                
                
                //COMPROBACION DATOS USUARIO
                if(this.entrada_cliente.contains("#ALLDATA_USER#")){
                    //1- Llamo al protocolo para que me descifre la cadena y 
                        //1)Compruebe que es un usuario del sistema
                        //2)Recolecte toda la informacion
                        //3)Se mande por el socket
                    this.salida_server = this.protocolo.procesarEntradaObtenerDatos(this.entrada_cliente,this);
                    //2- Mando los datos o el error al usuario
                    this.mandar_salida(salida_server);                    
                }
               
                
                //COMPROBACION ENVIAR MENSAJE
                if(this.entrada_cliente.contains("#CLIENT#CHAT#")){
                    if(!this.entrada_cliente.contains("#RECEIVED_MESSAGE#")){
                        try {
                            //1- Llamo al protocolo para que me descrifre la cadena y
                                //1)- Compruebe que los dos usuarios estan en el sistema
                                //2)- Compruebe que los dos usuarios son amigos
                                //3)- Guarde en la base de datos el mensaje
                            this.salida_server = this.protocolo.procesarEntradaEnviarMensaje(this.entrada_cliente,this);
                            if(salida_server == null){
                                //2- Comprobar a traves del socket que el cliente destino esta conectado
                                boolean check_conex = false;
                                check_conex = this.comprobar_conexion_hebra();

                                if(check_conex == true){//EL CLIENTE ESTA CONECTADO

                                    //3- Si esta conectado enviar cadena al protocolo para transformarla en otra
                                    this.salida_server = this.protocolo.procesarSalidaEnviarMensajeClienteDestino(this.entrada_cliente);

                                    //4- Enviar cadena al usuario destino
                                    //NO TENGO NI IDEA DE COMO M HACER ESTO!!!!!!!!!!
                                    this.mandar_al_cliente_destino_nuevo_mensaje(salida_server);
                                }else{
                                    System.out.println("SERVER: El cliente destino no esta conectado");
                                    VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: El cliente destino no esta conectado" + "\n");
                                }

                            }else{//Ha habido un error en la cadena
                                this.mandar_salida(salida_server);
                            }

                        } catch (SQLException ex) {
                            Logger.getLogger(MultiServerThread2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }else if(this.entrada_cliente.contains("#RECEIVED_MESSAGE#")){ 
                        //Este mensaje es del cliente destino
                        //1- Cambiar booleano read de la bd
                        try {
                            this.salida_server = this.protocolo.procesarSalidaNotificarMensajeClienteOrigen(this.entrada_cliente,this.usuario,this);
                        } catch (SQLException ex) {
                            Logger.getLogger(MultiServerThread2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //2- Mandar cadena al cliente origen 
                            //1) Comprobar que el cliente origen esta conectado
                            //2) Acceder a su socket para mandarle un mensaje
                        this.mandar_al_cliente_origen_mensaje_bien_procesado(salida_server);
                    }
                    
                
                }
                
                
                //COMPROBACION ESTADOS
                if(this.entrada_cliente.contains("#CLIENT#STATUS#")){
                    //1- Descifrar cadena
                    //2- Comprobar la conexion
                    //3- Cambiar el atributo de conexion en la bd de ese usuario
                    //4- Mandar estado al cliente
                    try {
                        this.salida_server = this.protocolo.procesarEntradaEstado(this.entrada_cliente, this);
                        this.mandar_salida(this.salida_server);
                    } catch (SQLException ex) {
                        Logger.getLogger(MultiServerThread2.class.getName()).log(Level.SEVERE, null, ex);
                    }                                        
                }
                
                
                
                
                
                
                
                

            //ERROR EN EL PRINCIPIO DE LA CADENA
            }else{
                salida_server = protocolo.procesarErrorPrincipioCadena(); 
                this.mandar_salida(salida_server);
            }
        }
            
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(MultiServerThread2.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void hebra_login() throws SQLException{
        salida_server = protocolo.procesarEntradaLogin(entrada_cliente, this);        
        System.out.println("SERVER SALIDA PROTOCOLO: " +salida_server);
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida_server + "\n");

        int comprobar = salida_server.indexOf("PROTOCOLCRISTOMESSENGER1.0#SERVER#ERROR#BAD_LOGIN");

        if(comprobar == 0){
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER TO CLIENT: Error de login" + "\n");
            System.out.println("SERVER TO CLIENT: Error de login");
            //this.desconectar(); TENGO QUE CERRAR EL SOCKET
        } else {
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVEVR TO CLIENT: Login correcto" + "\n");
            System.out.println("SERVER TO CLIENT: Login correcto");
            
            this.setName(this.getUsuario());
        }
        
        out.println(salida_server);                        
    }
    
//    public void hebra_mensajes_numeroTotal_numeroFecha(String usuario, String amigo) throws SQLException{
//        
//        System.out.println("SERVER SALIDA PROTOCOLO: " +salida_server);
//        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida_server + "\n");
//        out.println(salida_server);
//    }
    
    public void hebra_mensajes_send (String usuario, String amigo, String fecha) throws SQLException{
        ArrayList <MensajesMapeo> mensajes = new ArrayList();
        for(int i=0; i<num_men_fecha; i++){
            salida_server = protocolo.procesarEntradaMensajesSend(entrada_cliente, this,usuario,amigo,mensajes,fecha);
            salida_server+=mensajes.get(i).toString();            
            System.out.println("SERVER SALIDA PROTOCOLO: " +salida_server);
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida_server + "\n");
            out.println(salida_server);  
        }
    }
    
//    public void mandar_al_cliente_todos_los_datos_usuario(String salida){
//        System.out.println("SERVER SALIDA PROTOCOLO: " +salida);
//        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida + "\n");
//        out.println(salida);  
//    }
//    
//    public void mandar_al_cliente_error_en_cadena_enviar_mensaje(String salida){
//        System.out.println("SERVER SALIDA PROTOCOLO: " +salida);
//        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida + "\n");
//        out.println(salida);  
//    }
    
    public void mandar_salida(String salida){
        System.out.println("SERVER SALIDA PROTOCOLO: " +salida);
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida + "\n");
        out.println(salida);  
    }
    
    public boolean comprobar_conexion_hebra(){
        boolean check = this.multiServer.buscar_en_hebras_conectadas(this.usuario_destino);
        return check;
    }
    
    public void mandar_al_cliente_destino_nuevo_mensaje(String salida) throws SQLException{
        //1-Acceder a esa hebra y mandar el mensaje
        this.multiServer.acceder_a_hebra_y_mandar_mensaje(this.usuario_destino, salida);
        //2-Cambiar el booleano enviado de la bd
        this.controladorMensajes.change_boolean_send(this.usuario, this.usuario_destino,this.getFechahora_men_enviado());
    }
    
    public void mandar_al_cliente_origen_mensaje_bien_procesado(String salida){
        boolean check = this.multiServer.buscar_en_hebras_conectadas(this.usuario_destino);
        if(check){
            this.multiServer.acceder_a_hebra_y_mandar_mensaje(this.usuario_destino, salida);
        }
    }
    
    public void desconectar (){
        System.out.println("SERVER: DESCONECTO HEBRA");
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: DESCONECTO HEBRA" + "\n");
                        
        conectar=false;
    }
    
    
}



