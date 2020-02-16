/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_servidor;
import back_servidor.controladorBD.*;
import front_servidor.VistaServer;
import java.net.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author patri
 */
public class MultiServer {
    
    int puerto;
    boolean listening;
    ArrayList <MultiServerThread2> hebras_array;
    
    public MultiServer(int port) throws SQLException{
        hebras_array = new ArrayList();
        puerto = port;
        listening=true;
        aceptar_conexion();         
    }
    
    public void aceptar_conexion() throws SQLException{
        try (ServerSocket serverSocket = new ServerSocket(puerto)) { 
            int i = 0;
            while(listening) {                   
                MultiServerThread2 hebra = new MultiServerThread2 (serverSocket.accept(),i,this);
                hebras_array.add(hebra);
                System.out.println("SERVER: AÑADO HEBRA");
                VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: AÑADO HEBRA" + "\n");
                hebras_array.get(i).start();
                i++;
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + puerto);
            System.exit(-1);
        }
    }   
    
    public void desconectar() throws InterruptedException{
        System.out.println("SERVER: SE VA A DESCONECTAR");
        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: SE VA A DESCONECTAR" + "\n");
        
        listening=false;
        for(int i = 0; i<hebras_array.size(); i++){
            hebras_array.get(i).join(); 
        }
        for(int i = 0; i<hebras_array.size(); i++){
            hebras_array.get(i).desconectar();
        }        
    }
    
    public boolean buscar_en_hebras_conectadas (String nombre){
        boolean check = false;
        for(int i = 0; i<this.hebras_array.size(); i++){
            System.out.println("nombre = " + this.hebras_array.get(i).getName());
            if(this.hebras_array.get(i).getName().equals(nombre)){
                check = true;
                System.out.println("ENTRA");
            }
        }
        return check;
    }
    
    public void acceder_a_hebra_y_mandar_mensaje(String nombre,String salida){
        for(int i = 0; i<this.hebras_array.size(); i++){
            if(this.hebras_array.get(i).getName().equals(nombre)){
                this.hebras_array.get(i).mandar_salida(salida);
            }
        }
    }
    
    
}