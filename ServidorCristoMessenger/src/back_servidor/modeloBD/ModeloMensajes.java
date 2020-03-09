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
 * @author patri
 */
public class ModeloMensajes extends BDConnector{
    
    public MensajesMapeo mensajes_mapeo;    
    private String tabladb;
    private String query;
    
    //*****CONSTRUCTOR******//

    public ModeloMensajes() throws SQLException {
        super();
        mensajes_mapeo = new MensajesMapeo();
        this.tabladb = "message";
        this.query = " ";
    }

    ModeloMensajes(MensajesMapeo mensajes) throws SQLException {
        super();
        mensajes_mapeo = mensajes;
        this.tabladb = "message";
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
    
    //TODO querys
    public void query_messages (String usuario, String amigo) {
        this.setQuery("SELECT * FROM "+ get_dbname() + "." + getTabladb()+" WHERE (id_user_orig ='"+usuario+"'and id_user_dest = '"+amigo+"')or (id_user_dest ='"+usuario+"' and id_user_orig ='"+amigo+"');" );
    }
    
    public void query_messages_date (String usuario, String amigo, String solo_fecha) {
        this.setQuery("SELECT * FROM "+ get_dbname() + "." + getTabladb()+" WHERE (((id_user_orig ='"+usuario+"'and id_user_dest = '"+amigo+"')or (id_user_dest ='"+usuario+"' and id_user_orig ='"+amigo+"'))  and datetime like '"+solo_fecha+"%');" );
    }
    
    public void query_messages_save(String usuario, String amigo, String texto, String fecha){
        this.setQuery("INSERT INTO "+ get_dbname() + "." + getTabladb()+" VALUES ('"+usuario+"','"+amigo+"','"+fecha+"', 0 , 0 , '"+texto+"');");
    }
    
    public void query_messages_change_send(String usuario, String amigo, String fecha){
        this.setQuery("UPDATE "+ get_dbname() + "." + getTabladb()+" SET sent = 1 WHERE ((id_user_orig = '"+usuario+"' and id_user_dest = '"+amigo+"') and datetime = '"+fecha+"');");
    }
    
    public void query_messages_change_read(String usuario, String amigo, String fecha){
        this.setQuery("UPDATE "+ get_dbname() + "." + getTabladb()+" SET read_msg = 1 WHERE ((id_user_orig = '"+usuario+"' and id_user_dest = '"+amigo+"') and datetime = '"+fecha+"');");
    }

    public void get_messages(String db, String table, ArrayList mess, String usuario, String amigo, String fecha) throws SQLException {
        String[] parts = fecha.split(" ");
        String solo_fecha = parts[0];
        
        this.query_messages_date(usuario,amigo,solo_fecha);
        this.conectar_bd();
        
        try {
            this.set_stmt(this.getConn().createStatement());
            
            this.set_rs(this.get_stmt().executeQuery(getQuery()));
            
            while (get_rs().next()) {
                
//                this.change_boolean_read(db, table, usuario, amigo, fecha);
//                this.change_boolean_send(db, table, usuario, amigo, fecha);
                
                String id_user_orig = get_rs().getString("id_user_orig");
                String id_user_dest = get_rs().getString("id_user_dest");
                String text = get_rs().getString("text");
                String date = get_rs().getString("datetime");
                 
                  
                MensajesMapeo  messa = new MensajesMapeo ();
                messa.setId_user_orig(id_user_orig);
                messa.setId_user_dest(id_user_dest);
                messa.setText(text);
                messa.setDateTime(date);

                mess.add(messa); 
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }
    }
    
    public int get_messages_numero(String db, String table,String usuario, String amigo) throws SQLException {
        
        this.query_messages(usuario, amigo);
        this.conectar_bd();
        int num = 0;
        
        try {
            this.set_stmt(this.getConn().createStatement());            
            this.set_rs(this.get_stmt().executeQuery(getQuery()));
            
            while (get_rs().next()) {                
                num++;                                
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }
        return num;
    }

    public int get_messages_fecha(String db, String table,String usuario, String amigo, String fecha) throws SQLException {
        
        String[] parts = fecha.split(" ");
        String solo_fecha = parts[0];
        
        this.query_messages_date(usuario, amigo, solo_fecha);
        this.conectar_bd();
        int num = 0;
        
        try {
            this.set_stmt(this.getConn().createStatement());            
            this.set_rs(this.get_stmt().executeQuery(getQuery()));
            
            while (get_rs().next()) {
               
            num++;
                                
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } 
        finally {
            if (get_stmt() != null) { get_stmt().close(); this.desconectar_bd();}
        }
        return num;
    }
    
    public void save_message(String db, String table,String usuario, String amigo,String texto, String fecha) throws SQLException {
        
        
        this.query_messages_save(usuario, amigo, texto, fecha);
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
    
    public void change_boolean_send(String db, String table,String usuario, String amigo, String fecha) throws SQLException {
        
        this.query_messages_change_send(usuario, amigo, fecha);
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
    
    public void change_boolean_read(String db, String table,String usuario, String amigo, String fecha) throws SQLException {
        
        this.query_messages_change_read(usuario, amigo, fecha);
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
}
