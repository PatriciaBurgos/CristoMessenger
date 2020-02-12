/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_servidor.controladorBD;

import back_servidor.modeloBD.MensajesMapeo;
import back_servidor.modeloBD.ModeloMensajes;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patri
 */
public class ControladorMensajes {
    private ModeloMensajes message;
    
    //*****CONSTRUCTOR******//

    ControladorMensajes() throws SQLException {
        message = new ModeloMensajes();
    }
    
    //*****CLASS METHODS*****//
    
    //TODO metodos que dentro se llama a la query y al metodo correspodiente para que le pida a la bd
    
    
    public int get_messages_numero(String usuario, String amigo) throws SQLException{
        int num = this.message.get_messages_numero(this.message.get_dbname(), this.message.getTabladb(), usuario, amigo);
        return num;
    }
    
    public int get_messages_fecha(String usuario, String amigo, String fecha) throws SQLException{
        //Aqui me voy a traer todos los mensajes y los voy a mandar uno a uno+       
        int num = this.message.get_messages_fecha(this.message.get_dbname(), this.message.getTabladb(), usuario, amigo, fecha);
        return num;    
    }
    
    public void get_message_send(String usuario, String amigo, ArrayList mensajes) throws SQLException{
        //Aqui me voy a traer todos los mensajes y los voy a mandar uno a uno+       
        this.message.get_messages(this.message.get_dbname(), this.message.getTabladb(),mensajes, usuario, amigo);   
    }
    
    
    
}
