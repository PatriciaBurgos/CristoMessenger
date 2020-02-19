/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_servidor.modeloBD;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */
public class ModeloUsuario extends BDConnector{
    
    public UsuariosMapeo usuarios_mapeo;
    private String tabladb;
    private String query;
    
    //*****CONSTRUCTOR******//

    public ModeloUsuario() throws SQLException {
        super();
        usuarios_mapeo = new UsuariosMapeo();
        this.tabladb = "user";
        this.query = " ";
    }   
    
    ModeloUsuario(UsuariosMapeo usuarios) throws SQLException {
        super();
        usuarios_mapeo = usuarios;
        this.tabladb = "user";
        this.query = " ";
    }

    public String getTabladb() {
        return tabladb;
    }

    public void setTabladb(String tabladb) {
        this.tabladb = tabladb;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
    
    //*****CLASS METHODS*****//
    
    public void query_search_user(String usuario_login){
        this.setQuery("SELECT * FROM "+get_dbname()+"."+getTabladb()+" WHERE id_user = '" + usuario_login+"';");
    }

    public void query_change_status(String usuario, boolean status){
        this.setQuery("UPDATE "+ get_dbname() + "." + getTabladb()+" SET state = "+status+" WHERE id_user = '"+usuario+"';");
    }
    
    
    public boolean check_user_login (String db, String table, String usuario_login, String usuario_contrasenia, boolean check) throws SQLException {
      
        this.query_search_user(usuario_login);
        this.conectar_bd();
       
        try {
            this.set_stmt(this.getConn().createStatement());
            
            this.set_rs(this.get_stmt().executeQuery(getQuery()));
            
            while (get_rs().next()) {
                
                String contras = get_rs().getString("password");

                if (contras.equals(usuario_contrasenia)){
                    check=true;
                }
                
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }
        
        return check;
    }   
    
    public boolean check_user (String db, String table, String usuario_login,boolean check) throws SQLException {      
       
        this.query_search_user(usuario_login);
        this.conectar_bd();
       
        try {
            this.set_stmt(this.getConn().createStatement());
            
            this.set_rs(this.get_stmt().executeQuery(getQuery()));
            
            while (get_rs().next()) {
                check=true;                
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }
        
        return check;
    }   
    
    public boolean get_conexion(String db, String table,String nom_usuario) throws SQLException{
        boolean check = false;
        
        this.query_search_user(nom_usuario);
        this.conectar_bd();
       
        try {
            this.set_stmt(this.getConn().createStatement());            
            this.set_rs(this.get_stmt().executeQuery(getQuery()));
            
            while (get_rs().next()) {
                int estado = get_rs().getInt("state");
                
                if(estado==1){
                    check=true;
                }
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }
        
        return check;
    }
    
    
    public void get_data_user (String db, String table, UsuariosMapeo user_data) throws SQLException {
        this.query_search_user(user_data.getId_user());
        this.conectar_bd();
       
        try {
            this.set_stmt(this.getConn().createStatement());
            
            this.set_rs(this.get_stmt().executeQuery(getQuery()));
            
            while (get_rs().next()) {                
                String name = get_rs().getString ("name");
                String surname1 = get_rs().getString ("surname1");
                String surname2 = get_rs().getString ("surname2");
                
                user_data.setName(name);
                user_data.setSurname1(surname1);
                user_data.setSurname2(surname2); 
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }
    }
    
    public void changestatus(String usuario, boolean check) throws SQLException{
        this.query_change_status(usuario, check);
        this.conectar_bd();
        
        try {           
            this.set_stmt(this.getConn().createStatement());            
            this.stmt.executeUpdate(this.getQuery());
            
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }   
    }
    
    public String get_data_photo(String login) throws SQLException{
        this.query_search_user(login);
        this.conectar_bd();
        String ruta = "";
       
        try {
            this.set_stmt(this.getConn().createStatement());
            
            this.set_rs(this.get_stmt().executeQuery(getQuery()));
            
            while (get_rs().next()) {                
                ruta = get_rs().getString ("photo");
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }
        return ruta;
    }
    
}
