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
    
    //TODO gets
    
//    public void query_all_users () {
//        setQuery ("SELECT * FROM " + get_dbname() + "." + getTabladb()); 
//    }
    
    
    public void get_all_users_model (String db, String table, ArrayList users) throws SQLException {
       // System.out.println("DEBUG:Method get_all_users_model");
//       
//       this.query_all_users();
//       
//        try { 
//            this.set_stmt(this.getConn().createStatement());
//            this.set_rs(this.get_stmt().executeQuery(query));
//            
//            while (get_rs().next()) {
//                String id_user = get_rs().getString("id_user");
//                String name = get_rs().getString("name");
//
//                ModeloUsuario  user = new ModeloUsuario ();
//                user.setId_user(id_user);
//                user.setName(name);
//                
//                users.add (user);
//                
//            }
//        } catch (SQLException e ) {
//            System.out.println(e);
//        } 
//        finally {
//            if (get_stmt() != null) { get_stmt().close(); }
//        }
    }    
    
            
    public void get_all_users_login_model (String db, String table, ArrayList users, String usuario_login, String usuario_contrasenia) throws SQLException {
       // System.out.println("DEBUG:Method get_all_users_model");
      
        this.setQuery("SELECT * FROM "+get_dbname()+"."+getTabladb()+" WHERE id_user = '" + usuario_login+"';");
        this.conectar_bd();
       
        try {
            this.set_stmt(this.getConn().createStatement());
            
            this.set_rs(this.get_stmt().executeQuery(getQuery()));
            
            while (get_rs().next()) {
                
                String contras = get_rs().getString("password");
                String nomb = get_rs().getString("name");
                String sur1 = get_rs().getString("surname1");
                String sur2 = get_rs().getString("surname2");
                String foto = get_rs().getString("photo");
                int estado = get_rs().getInt("state");

                if (contras.equals(usuario_contrasenia)){
                    UsuariosMapeo  user = new UsuariosMapeo ();
                    user.setId_user(usuario_login);
                    user.setPassword(usuario_contrasenia);
                    user.setName(nomb);
                    user.setPhoto(foto);
                    user.setSurname1(sur1);
                    user.setSurname2(sur2);
                    user.setState(estado);

                    users.add (user);
                }
                
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }
    }    
    
    public boolean get_conexion(String db, String table,String nom_usuario) throws SQLException{
        boolean check = false;
        
        this.setQuery("SELECT * FROM "+get_dbname()+"."+getTabladb()+" WHERE id_user = '" + nom_usuario+"';");
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
    
    
    public void set_new_user_model (String db, String table, ModeloUsuario new_user) throws SQLException {
        
        
//        try {
//            System.out.println("DEBUG: INSERT (Mod)");
//            this.set_stmt(this.getConn().createStatement());
//            String insertar = ("INSERT INTO cristo_messenger.user VALUES ('"+new_user.getId_user()+"', '"+new_user.getName()+"', '"+new_user.getPassword()+"','"+new_user.getSurname1()+"','"+new_user.getSurname2()+"','"+new_user.getPhoto()+"','"+new_user.getState()+"');");
//            System.out.println(insertar);
//            this.stmt.executeUpdate(insertar);
//            
//            System.out.println("New user bd");
//            
//
//        } catch (SQLException e ) {
//            System.out.println(e);
//        } 
//        finally {
//            if (get_stmt() != null) { get_stmt().close(); }
//        }
    }
    
}
