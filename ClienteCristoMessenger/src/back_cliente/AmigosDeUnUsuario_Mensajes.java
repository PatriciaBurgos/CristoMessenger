/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_cliente;

import ClasesMapeadoras.AmigosMapeo;
import ClasesMapeadoras.MensajesMapeo;
import ClasesMapeadoras.UsuariosMapeo;
import java.util.ArrayList;

/**
 *
 * @author patri
 */
public class AmigosDeUnUsuario_Mensajes extends UsuariosMapeo {
    public ArrayList <MensajesMapeo> mensajes_array;
    int num_amigos;
    
    public AmigosDeUnUsuario_Mensajes(){
        mensajes_array=new ArrayList();
    }
    
    

    public int getNum_amigos() {
        return num_amigos;
    }

    public void setNum_amigos(int num_amigos) {
        this.num_amigos = num_amigos;
    }

   
       
}
