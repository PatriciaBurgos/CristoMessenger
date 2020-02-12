///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package back_servidor.controladorBD;
//
//
//import back_servidor.controladorBD.ControladorAmigos;
//import back_servidor.controladorBD.ControladorUsuario;
//import front_servidor.VistaServer;
//import java.net.*;
//import java.io.*;
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
///**
// *
// * @author patri
// */
//public class MultiServerThread extends Thread{
//
////    private Socket socket = null;
////    boolean conectar = true;
////    
////    ControladorUsuario controladorUsuario;
////    ControladorAmigos controladorAmigos;
////    
////    //Array de amigos,usuario........
//// 
////    public MultiServerThread(Socket socket, int id) throws SQLException {
////        super("Thread " + id);
////        System.out.println("SERVER: THREAD " + id);
////        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: THREAD "+id+ "\n");
////        
////        this.socket = socket;
////        controladorUsuario = new ControladorUsuario();
////        controladorAmigos = new ControladorAmigos();
////    }
////     
////    public void run() {
////        
////            try (
////                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
////                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
////            ) {
////                String entrada_cliente, salida_server;                
////                ProtocoloServer protocolo_login = new ProtocoloServer();
//////                outputLine = protocolo_login.procesarEntradaLogin(null);
//////                out.println(outputLine);
////
////                //while ((entrada_cliente = in.readLine()) != null) {
////                
////                while(conectar){
////                    entrada_cliente = in.readLine();
////                    if(entrada_cliente !=null){
////                        System.out.println("CLIENT TO SERVER: "+entrada_cliente);
////                        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "CLIENT TO SERVER: " +entrada_cliente+ "\n");
////
////                        if (entrada_cliente != null) {
////                            salida_server = protocolo_login.procesarEntradaLogin(entrada_cliente, this);
////                            System.out.println("SERVER SALIDA PROTOCOLO: " +salida_server);
////                            VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER SALIDA PROTOCOLO: " +salida_server + "\n");
////
////                            int comprobar = salida_server.indexOf("PROTOCOLCRISTOMESSENGER1.0#SERVER#ERROR#BAD_LOGIN");
////
////                            if(comprobar == 0){
////                                VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER TO CLIENT: Error de login" + "\n");
////                                System.out.println("SERVER TO CLIENT: Error de login");
////                                out.println(salida_server);
////                                //this.desconectar(); TENGO QUE CERRAR EL SOCKET
////                            } else {
////                                VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVEVR TO CLIENT: Login correcto" + "\n");
////                                System.out.println("SERVER TO CLIENT: Login correcto");
////                                out.println(salida_server);                        
////                            }
////
////        //                    System.out.println(socket.getInetAddress().getHostAddress() + " " + inputLine);
////        //                    VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ socket.getInetAddress().getHostAddress() + " " + inputLine + "\n");
////        //                    
////        //                    out.println(outputLine);
////        //                    
////                        }  
////                    }else{
////                        System.out.println("SERVER: Error de cadena (ALERTA HACKER");
////                        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: Error de cadena (ALERTA HACKER)" + "\n");
////                        desconectar();
////                    }
////                }    
////            
////            }catch (IOException ex) {
////                Logger.getLogger(MultiServerThread.class.getName()).log(Level.SEVERE, null, ex);
////            } catch (SQLException ex) {
////                Logger.getLogger(MultiServerThread.class.getName()).log(Level.SEVERE, null, ex);
////            }        
////        try {
////            socket.close();
////        } catch (IOException ex) {
////            Logger.getLogger(MultiServerThread.class.getName()).log(Level.SEVERE, null, ex);
////        }        
////    }
////    
////    public void desconectar (){
////        System.out.println("SERVER: DESCONECTO HEBRA");
////        VistaServer.areaDebugServer.setText(VistaServer.areaDebugServer.getText()+ "SERVER: DESCONECTO HEBRA" + "\n");
////                        
////        conectar=false;
////    }
//}
//
