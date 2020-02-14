/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_servidor.controladorBD;

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
    
    ControladorUsuario controladorUsuario;
    ControladorAmigos controladorAmigos;
    ControladorMensajes controladorMensajes;
    
    ProtocoloServer protocolo;
    PrintWriter out;
    BufferedReader in;
    
    String entrada_cliente, salida_server;   
    
    public int num_men_fecha;
    String usuario,amigo;
    
    
    public MultiServerThread2(Socket socket, int id) throws SQLException, IOException {
        super("Thread " + id);
        System.out.println("SERVER: THREAD " + id);
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: THREAD "+id+ "\n");
        
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
//////            int comprobacion_entrada_cliente = entrada_cliente.indexOf("PROTOCOLCRISTOMESSENGER1.0");
//////            if(comprobacion_entrada_cliente != -1){
                
            if(entrada_cliente.contains("PROTOCOLCRISTOMESSENGER1.0")){
                
                
                //COMPROBACION LOGIN
////////                int comprobacion_entrada_cliente_login = entrada_cliente.indexOf("#CLIENT#LOGIN#");
////////                if(comprobacion_entrada_cliente_login != -1){

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
                            this.hebra_mensajes_numeroTotal_numeroFecha(usuario, amigo);
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(MultiServerThread2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        //2 CADENA CLIENTE OK SEND
                    }else if (comprobacion_entrada_cliente_mensajes_send != -1){
                        try {
                            this.hebra_mensajes_send(usuario,amigo);
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
                    //1- Llamo al protocolo para que me descibre la cadena y 
                    //1)Compruebe que es un usuario del sistema
                    //2)Recolecte toda la informacion
                    //3)Se mande por el socket
                    this.salida_server = this.protocolo.procesarEntradaObtenerDatos(this.entrada_cliente,this);
                    //2- Mando los datos o el error al usuario
                    this.mandar_al_cliente_todos_los_datos_usuario(salida_server);                    
                }
               
                
                
                
                
                
                
                
                
                

            //ERROR EN EL PRINCIPIO DE LA CADENA
            }else{
                salida_server = protocolo.procesarErrorPrincipioCadena();  
                System.out.println("SERVER: ERROR DE CADENA");
                VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: ERROR DE CADENA" + "\n");
                out.println(salida_server);  
            }
        }
            
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(MultiServerThread.class.getName()).log(Level.SEVERE, null, ex);
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
        }
        
        out.println(salida_server);                        
    }
    
    public void hebra_mensajes_numeroTotal_numeroFecha(String usuario, String amigo) throws SQLException{
        salida_server = protocolo.procesarEntradaMensajesNumero(entrada_cliente, this, usuario, amigo);
        System.out.println("SERVER SALIDA PROTOCOLO: " +salida_server);
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida_server + "\n");
        out.println(salida_server);
    }
    
    public void hebra_mensajes_send (String usuario, String amigo) throws SQLException{
        ArrayList <MensajesMapeo> mensajes = new ArrayList();
        for(int i=0; i<num_men_fecha; i++){
            salida_server = protocolo.procesarEntradaMensajesSend(entrada_cliente, this,usuario,amigo,mensajes);
            salida_server+=mensajes.get(i).toString();            
            System.out.println("SERVER SALIDA PROTOCOLO: " +salida_server);
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida_server + "\n");
            out.println(salida_server);  
        }
    }
    
    public void mandar_al_cliente_todos_los_datos_usuario(String salida){
        System.out.println("SERVER SALIDA PROTOCOLO: " +salida);
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida + "\n");
        out.println(salida);  
    }
    
    
    
    public void desconectar (){
        System.out.println("SERVER: DESCONECTO HEBRA");
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: DESCONECTO HEBRA" + "\n");
                        
        conectar=false;
    }
}



