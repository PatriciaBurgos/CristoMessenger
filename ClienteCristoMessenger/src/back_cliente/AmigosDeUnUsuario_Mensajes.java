/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package back_cliente;

import ClasesMapeadoras.AmigosMapeo;
import ClasesMapeadoras.MensajesMapeo;
import ClasesMapeadoras.UsuariosMapeo;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author patri
 */
public class AmigosDeUnUsuario_Mensajes extends UsuariosMapeo {
    public ArrayList<MensajesMapeo> mensajes_array;
    int num_amigos;
    String ultima_fecha_buscada;
    int num_men_recibidos;
    
    public AmigosDeUnUsuario_Mensajes(){
        mensajes_array=new ArrayList();
    }

    public int getNum_amigos() {
        return num_amigos;
    }

    public void setNum_amigos(int num_amigos) {
        this.num_amigos = num_amigos;
    }

    public String getUltima_fecha_buscada() {
        return ultima_fecha_buscada;
    }

    public void setUltima_fecha_buscada(String ultima_fecha_buscada) {
        this.ultima_fecha_buscada = ultima_fecha_buscada;
    }

    public int getNum_men_recibidos() {
        return num_men_recibidos;
    }

    public void setNum_men_recibidos(int num_men_recibidos) {
        this.num_men_recibidos = num_men_recibidos;
    }

//    public void ordenar(){
//        Arrays.sort(this.mensajes_array);
//    }
    
    public void ordenar(){
        Collections.sort(this.mensajes_array, (MensajesMapeo m1, MensajesMapeo m2) -> {
            Timestamp f1 = Timestamp.valueOf(m1.getDatetime());
            long fecha1 = f1.getTime();
            
            Timestamp f2 = Timestamp.valueOf(m1.getDatetime());
            long fecha2 = f2.getTime();
            
            if(fecha1>fecha2){
                return 1;
            }
            if(fecha2<fecha1){
                return -1;
            }
            return 0;
        });
    }
    
}
