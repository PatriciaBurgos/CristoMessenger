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
    
    
    //*****CLASS METHODS*****//
    
    public boolean check_user_login (String usuario, String password) {
        boolean check = false;
        
        try{
            check = usuario_modelo.check_user_login(usuario_modelo.get_dbname(), usuario_modelo.getTabladb(),usuario,password,check);
           
        } catch (SQLException ex) {
            Logger.getLogger(ProtocoloServer2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return check;
    } 
    
    public boolean check_user(String usuario) {
        boolean check = false;        
        try{
            check = usuario_modelo.check_user(usuario_modelo.get_dbname(), usuario_modelo.getTabladb(),usuario,check);
            
        } catch (SQLException ex) {
            Logger.getLogger(ProtocoloServer2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return check;
    }
    
    public boolean comprobar_conexion (String nom_usuario) throws SQLException{
        boolean check = false;
        
        check = usuario_modelo.get_conexion(usuario_modelo.get_dbname(), usuario_modelo.getTabladb(),nom_usuario);
        
        return check;
    }
    
    public void get_data_user (UsuariosMapeo user_data){
        try{
            usuario_modelo.get_data_user(usuario_modelo.get_dbname(), usuario_modelo.getTabladb(),user_data);
            
        } catch (SQLException ex) {
            Logger.getLogger(ProtocoloServer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cambiarestado(String usuario, boolean check) throws SQLException{
        usuario_modelo.changestatus(usuario, check);
    }
    
    public String obtenerRutaFoto(String login) throws SQLException{
        String ruta = this.usuario_modelo.get_data_photo(login);
        return ruta;
    }
    
}
