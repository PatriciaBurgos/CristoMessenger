/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_servidor.controladorBD;

import back_servidor.modeloBD.ModeloUsuario;
import back_servidor.modeloBD.UsuariosMapeo;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lenovo
 */
public class ControladorUsuario {
    
    private ModeloUsuario usuario_modelo;
    
    
    //*****CONSTRUCTOR******//

    public ControladorUsuario() throws SQLException {
        usuario_modelo = new ModeloUsuario();
    }
    
    //*****SETS AND GETS*****//

    
    //*****CLASS METHODS*****//
    
    //TODO metodos que dentro se llama a la query y al metodo correspodiente para que le pida a la bd
    
//    public void get_all_users_control(ArrayList users) throws SQLException {
//        
//        try{
//            usuario.get_all_users_model(usuario.get_dbname(), usuario.getTabladb(), users);
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    public boolean check_user_login (String usuario, String password) {
        ArrayList <UsuariosMapeo> login_array = new ArrayList (); 
        boolean check = false;
        
        try{
            usuario_modelo.get_all_users_login_model(usuario_modelo.get_dbname(), usuario_modelo.getTabladb(),login_array,usuario,password);
            if(login_array.size()!=0){
                System.out.println("comprobar esta condicion controlador usuario");
                check=true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProtocoloServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return check;
    }
    
    public boolean comprobar_conexion (String nom_usuario) throws SQLException{
        boolean check = false;
        
        check = usuario_modelo.get_conexion(usuario_modelo.get_dbname(), usuario_modelo.getTabladb(),nom_usuario);
        
        return check;
    }
    
//    public boolean check_user_id (String user_screen) {
//        ArrayList <User_Model> login = new ArrayList <User_Model>(); 
//        boolean check = false;
//        
//        try{
//            usuario.get_all_users_login_model(usuario.get_dbname(), usuario.getTabladb(),login);
//            
//            for (int i = 0; i<login.size(); i++) {
//                /*DEBUG*/
//               /* System.out.println("user: " + user_screen);
//                System.out.println("login: " + login.get(i).getId_user());
//                System.out.println("password: " + password);
//                System.out.println("password_login: "+ login.get(i).getPassword());*/
//                
//                
//                if (login.get(i).getId_user().equals(user_screen)) {
//                    check=true;
//                    
//                    /*DEBUG*/
//                    
//                  /*  System.out.println("Controller");
//                    System.out.println("check: " + check);
//                    System.out.println("user: " + user_screen);
//                    System.out.println("password: " + password);*/
//        
//                }
//            }
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return check;
//    }
    
//    public ModeloUsuario get_user_act (String user_2screen) throws SQLException {
//        ArrayList <ModeloUsuario> login_array = new ArrayList(); 
//        ModeloUsuario act=new ModeloUsuario();
//        
//        try{
//            usuario_modelo.get_all_users_login_model(usuario_modelo.get_dbname(), usuario_modelo.getTabladb(),login_array);
//            
//            for (int i = 0; i<login_array.size(); i++) {
//                
//                if (login_array.get(i).getId_user().equals(user_2screen) ) {
//                    
//                    act.setId_user(login_array.get(i).getId_user());
//                    act.setName(login_array.get(i).getName());
//                    act.setPassword(login_array.get(i).getPassword());
//                    act.setSurname1(login_array.get(i).getSurname1());
//                    act.setSurname2(login_array.get(i).getSurname2());
//                    act.setPhoto(login_array.get(i).getPhoto());
//                    act.setState(login_array.get(i).getState());
//                }
//            }
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(ProtocoloServer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return act;
//    } 
//    
//    public void agregar_user (String user,String name,String pass,String sur1,String sur2) throws SQLException {
//        User_Model user_mod=new User_Model(user,name,pass,sur1,sur2,"url",0);
//        System.out.println("DEBUG: INSERT (CON)");
//        try{
//            user_mod.set_new_user_model(user_mod.get_dbname(), user_mod.getTabladb(),user_mod);
//            System.out.println("DEBUG: INSERT TERMINADO (CON)");
//        } catch (SQLException ex) {
//            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
    
    
}
