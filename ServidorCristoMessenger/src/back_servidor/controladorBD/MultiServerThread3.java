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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patri
 */
public class MultiServerThread3 extends Thread {
    private Socket socket = null;
    boolean conectar = true;
    MultiServer multiServer;
    
    ControladorUsuario controladorUsuario;
    ControladorAmigos controladorAmigos;
    ControladorMensajes controladorMensajes;
    
    public ProtocoloServer2 protocolo;
    PrintWriter out;
    BufferedReader in;
    
    String entrada_cliente, salida_server;   
    
//    public int num_men_fecha;
    String usuario,amigo,usuario_destino,fechahora_men_enviado/*, fechahora_men_leer, amigoestado*/;
    
    
    public MultiServerThread3(Socket socket, int id, MultiServer multiserver) throws SQLException, IOException {
        super("Thread " + id);
        System.out.println("SERVER: THREAD " + id);
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: THREAD "+id+ "\n");
        
        this.multiServer = multiserver;
        this.socket = socket;
        controladorUsuario = new ControladorUsuario();
        controladorAmigos = new ControladorAmigos();
        controladorMensajes = new ControladorMensajes();
        protocolo = new ProtocoloServer2(this);
        
        out = new PrintWriter(socket.getOutputStream(), true);
        in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    
    @Override
    public void run() {            
        while(conectar){
            try {
                //Recivo una cadena del cliente
                entrada_cliente = in.readLine();
                entrada_cliente = entrada_cliente.replaceAll("'", "");
                entrada_cliente = entrada_cliente.replaceAll("\"", "");
            } catch (IOException ex) {
                try {
                    this.desconectar();
                } catch (SQLException ex1) {
                    Logger.getLogger(MultiServerThread3.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(MultiServerThread3.class.getName()).log(Level.SEVERE, null, ex);
                
            }
            System.out.println("CLIENT TO SERVER: "+entrada_cliente);
            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "CLIENT TO SERVER: " +entrada_cliente+ "\n");

            //Mando cadena al protocolo
            if(entrada_cliente!=null){
                try {
                    
                    this.protocolo.procesarEntrada(entrada_cliente);
                
                } catch (SQLException ex) {
                    Logger.getLogger(MultiServerThread3.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MultiServerThread3.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MultiServerThread3.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
            }
        }
    }
    
    public void mandar_salida(String salida) throws InterruptedException{
        System.out.println("SERVER SALIDA PROTOCOLO: " +salida);
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida + "\n");
        out.println(salida);          
    }
    
    public void mandar_salida_foto(String salida){
        out.println(salida);  
    }
    
    public boolean comprobar_conexion_hebra(){
        boolean check = this.multiServer.buscar_en_hebras_conectadas(this.usuario_destino);
        return check;
    }
    
    public void mandar_al_cliente_destino_nuevo_mensaje(String salida) throws SQLException, InterruptedException{
        //1-Acceder a esa hebra y mandar el mensaje
        this.multiServer.acceder_a_hebra_y_mandar_mensaje(this.usuario_destino, salida);
        //2-Cambiar el booleano enviado de la bd
        this.controladorMensajes.change_boolean_send(this.usuario, this.usuario_destino,this.fechahora_men_enviado);
    }
    
    public void mandar_al_cliente_origen_mensaje_bien_procesado(String salida) throws InterruptedException{
        boolean check = this.multiServer.buscar_en_hebras_conectadas(this.usuario_destino);
        if(check){
            this.multiServer.acceder_a_hebra_y_mandar_mensaje(this.usuario_destino, salida);
        }
    }
    
    public void obtener_paquetes_foto(String login) throws SQLException, IOException, InterruptedException{  
        FileInputStream fileInputStream;
        //1-Llamar al controlador para traerme la ruta de la foto
        String rutaFoto = this.controladorUsuario.obtenerRutaFoto(login);
        //2-Extraer bytes con file input stream en un buffer de array de 512bytes
        try{
            fileInputStream  = new FileInputStream(rutaFoto);
        }catch(IOException e){
            System.out.println(e);
            fileInputStream  = new FileInputStream("data/error.jpg");
        }
       
        if(fileInputStream.equals("")){
            fileInputStream  = new FileInputStream("data/error.jpg");
        }
        
        String line = "";
        int cont = 0;
        ArrayList<String> arrayLines = new ArrayList<String>();
//        ArrayList<String> encodeLines = new ArrayList<String>();
        byte buffer[] = new byte[512];
        int off = 0;
        int len = 512;
        int total = 0;
         
        int valor = fileInputStream.read();
        while(valor!=-1){
            if (cont > 511) {
                arrayLines.add(line);
                line = "";

                cont = 0;
            }
            line += (char)valor;
            valor=fileInputStream.read();
            cont++;
            total++;
        }
        if (cont > 0) {
            arrayLines.add(line);
            cont = 0;
        }


        for (String s : arrayLines) {
            ArrayList<String> encodeLines = new ArrayList<String>();
            encodeLines.add(Base64.getEncoder().encodeToString(s.getBytes()));
            len = encodeLines.get(0).length();
            this.salida_server = this.protocolo.procesarFotoMandarACliente(encodeLines, login, len,total);
            this.mandar_salida_foto(this.salida_server); 
        }          
        
        //3-Mandar al cliente que ya se ha terminado la cadena
        this.salida_server = this.protocolo.procesarSalidaNotificarFinFoto(login);
        this.mandar_salida(this.salida_server);
        
    }
    
    public void desconectar () throws SQLException{
        this.protocolo.desconectar();
        
        System.out.println("SERVER: DESCONECTO HEBRA");
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: DESCONECTO HEBRA" + "\n");
        this.setName("");
        conectar=false;
    }
    
    
}