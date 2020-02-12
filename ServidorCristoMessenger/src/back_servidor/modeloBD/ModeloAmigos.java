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
public class ModeloAmigos extends BDConnector{
    
    public AmigosMapeo amigos_mapeo;
    private String tabladb;
    private String query;
    
    //*****CONSTRUCTOR******//
    
    public ModeloAmigos() throws SQLException {
        super();
        amigos_mapeo = new AmigosMapeo();     
        this.tabladb = "friend";
        this.query = "";
    }

    ModeloAmigos(AmigosMapeo amigos) throws SQLException {
        super();
        amigos_mapeo = amigos;
        this.tabladb = "friend";
        this.query = "";
    }
    
    //*****CLASS METHODS*****//

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
    
    
    //TODO querys
//
//    public void query_all_friends () {
//        setQuery ("SELECT * FROM " + get_dbname() + "." + getTabladb());
//    }
    
    
    public void get_all_friends_model (String db, String table, ArrayList friends, String id_orig) throws SQLException {
        
        this.setQuery("SELECT * FROM "+ get_dbname() + "." + getTabladb()+" WHERE (id_user_orig ='"+id_orig+"');" );
        
        this.conectar_bd();
       
        try {
            this.set_stmt(this.getConn().createStatement());
            
            this.set_rs(this.get_stmt().executeQuery(getQuery()));
            
            while (get_rs().next()) {
                
                String id_user_dest = get_rs().getString("id_user_dest");
                
                String name = id_user_dest;                    
                friends.add(name);
                              
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }
    }
    
//    public boolean comprobar_amistad (String db, String table, String usuario, String amigo) throws SQLException {
//        Boolean amigos = true;
//        this.query_all_friends();
//        this.conectar_bd();
//       
//        try {
//            this.set_stmt(this.getConn().createStatement());
//            
//            this.set_rs(this.get_stmt().executeQuery(getQuery()));
//            
//            while (get_rs().next()) {
//                
//                String id_user_orig = get_rs().getString("id_user_orig");
//                String id_user_dest = get_rs().getString("id_user_dest");
//                
//                //COMPROBACION
//            }
//        } catch (SQLException e ) {
//            System.out.println(e);
//        } 
//        finally {
//            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
//        }
//        return amigos;
//    }
//    
}
