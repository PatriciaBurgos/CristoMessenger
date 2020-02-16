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
public class MensajesMapeo {
    private String id_user_orig;
    private String id_user_dest;
    private String datetime;
    private String text;
    private boolean read;
    private boolean send;

    public MensajesMapeo() {
        this.id_user_orig = " ";
        this.id_user_dest = " ";
        this.datetime= " ";
        this.text = " ";
        this.read = false;
        this.send = false;
    }

    public MensajesMapeo(String id_user_orig, String id_user_dest, String datetime, String text, boolean read, boolean send) {
        this.id_user_orig = id_user_orig;
        this.id_user_dest = id_user_dest;
        this.datetime = datetime;
        this.text = text;
        this.read = read;
        this.send = send;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDateTime(String date) {
        this.datetime = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    @Override
    public String toString() {
        return "#" + id_user_orig + "#" + id_user_dest + "#" + datetime + "#" + text;
    }

    
    
    
}
