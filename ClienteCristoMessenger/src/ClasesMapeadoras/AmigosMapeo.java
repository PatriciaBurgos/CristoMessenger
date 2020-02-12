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
public class AmigosMapeo {
    private String id_user_orig;
    private String id_user_dest;
    private boolean accept_request;
    
    public AmigosMapeo() {
        this.id_user_orig = "";
        this.id_user_dest = "";
        this.accept_request = false;        
    }

    public AmigosMapeo(String id_user_orig, String id_user_dest, boolean accept_request) {
        this.id_user_orig = id_user_orig;
        this.id_user_dest = id_user_dest;
        this.accept_request = accept_request;        
    }

    public String getId_user_orig() {
        return id_user_orig;
    }

    public void setId_user_orig(String id_user_orig) {
        this.id_user_orig = id_user_orig;
    }

    public String getId_user_dest() {
        return id_user_dest;
    }

    public void setId_user_dest(String id_user_dest) {
        this.id_user_dest = id_user_dest;
    }

    public boolean isAccept_request() {
        return accept_request;
    }

    public void setAccept_request(boolean accept_request) {
        this.accept_request = accept_request;
    }
}
