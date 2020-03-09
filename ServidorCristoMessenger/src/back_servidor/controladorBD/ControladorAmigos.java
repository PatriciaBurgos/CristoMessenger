/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_servidor.controladorBD;

import back_servidor.modeloBD.ModeloAmigos;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lenovo
 */
public class ControladorAmigos {
    
     private ModeloAmigos friends_modelo;
    
    //*****CONSTRUCTOR******//

    public ControladorAmigos() throws SQLException {
        this.friends_modelo = new ModeloAmigos();
    }
    
    //*****CLASS METHODS*****//
    
    //TODO metodos que dentro se llama a la query y al metodo correspodiente para que le pida a la bd
    
    public void get_all_friends_con (ArrayList friends, String id_orig) {
        try{
            this.friends_modelo.get_all_friends_model(this.friends_modelo.get_dbname(), this.friends_modelo.getTabladb(), friends, id_orig);
            
        } catch (SQLException ex) {
            Logger.getLogger(ProtocoloServer2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean check_friendship(String usuario, String amigo) throws SQLException{
        Boolean check;
        check = this.friends_modelo.check_friendship(this.friends_modelo.get_dbname(), this.friends_modelo.getTabladb(),usuario, amigo);
        return check;
    }
    
}
