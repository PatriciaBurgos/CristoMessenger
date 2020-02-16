/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author patri
 */
//public class ThreadPeticionesUsuario implements Runnable{
//    ConexionClienteconServer conexionCliente;
//    String ip;
//    int puerto;
//    String usuario;
//    String contraseña;
//    
//    ThreadPeticionesUsuario(ConexionClienteconServer conexionCliente,String IP, int port, String user, String pass, ArrayList<AmigosDeUnUsuario_Mensajes> array_mensajes_usuario) {
//        this.conexionCliente = conexionCliente;
//        ip = IP;
//        puerto = port;
//        usuario = user;
//        contraseña = pass;
//        
//        System.out.println("CLIENT: IP-->" + ip + " PUERTO--> " + puerto + " USUARIO--> " + usuario + " CONTRASEÑA--> " +contraseña);
//
//    }
//    
//    @Override
//    public void run(){
//        while(true){
//            
//        }
//    }
//    
//    
//}
