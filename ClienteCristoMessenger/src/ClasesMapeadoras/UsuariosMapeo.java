/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesMapeadoras;

/**
 *
 * @author patri
 */
public class UsuariosMapeo {
    private String id_user; //login
    private String name; 
    private String password;
    private String surname1;
    private String surname2;
    private String photo;
    private int state;
    

    public UsuariosMapeo() {
        this.id_user = " ";
        this.name = " ";
        this.password = " ";
        this.surname1 = " ";
        this.surname2 = " ";
        this.photo = " ";
        this.state = -1;
    }

    public UsuariosMapeo(String id_user, String name, String password, String surname1, String surname2, String photo, int state, String tabladb, String query) {
        this.id_user = id_user;
        this.name = name;
        this.password = password;
        this.surname1 = surname1;
        this.surname2 = surname2;
        this.photo = photo;
        this.state = state;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname1() {
        return surname1;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
