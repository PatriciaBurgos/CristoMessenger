/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package front_cliente;

import ClasesMapeadoras.AmigosMapeo;
import ClasesMapeadoras.MensajesMapeo;
import back_cliente.ConexionClienteconServer2;
import back_cliente.AmigosDeUnUsuario_Mensajes;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Lenovo
 */
public class VistaClienteChats extends javax.swing.JFrame {

    /**
     * Creates new form Segunda_ventana
     */
    public VistaClienteChats(ConexionClienteconServer2 conexion, ArrayList<AmigosDeUnUsuario_Mensajes> array_mensajes_usuario, String usuario) throws SQLException, IOException, InterruptedException {

//        setExtendedState(MAXIMIZED_BOTH);
        initComponents();

        this.conexionCliente = conexion;
        amigos_del_usuario_mensajes = array_mensajes_usuario;
        this.conexionCliente.conectar_con_server_obtener_datos(usuario, this);

        //Registro
        user = " ";
        name = " ";
        pass = " ";
        sur1 = " ";
        sur2 = " ";

        //Chat_imagen
        ImageIcon fot = new ImageIcon("imagenes/logo.jpg");
        Icon icono = new ImageIcon(fot.getImage().getScaledInstance(miImagen.getWidth(), miImagen.getHeight(), Image.SCALE_DEFAULT));
        miImagen.setIcon(icono);
        this.repaint();

        ImageIcon foto = new ImageIcon("imagenes/logo.jpg");
        Icon icono2 = new ImageIcon(foto.getImage().getScaledInstance(label_imagen_amigo.getWidth(), label_imagen_amigo.getHeight(), Image.SCALE_DEFAULT));
        label_imagen_amigo.setIcon(icono2);
        this.repaint();

        String rutaUser = this.traeFoto(usuario);
        ImageIcon foto1 = new ImageIcon(rutaUser);
        Icon icono3 = new ImageIcon(foto1.getImage().getScaledInstance(label_imagen_user.getWidth(), label_imagen_user.getHeight(), Image.SCALE_DEFAULT));
        label_imagen_user.setIcon(icono3);
        this.repaint();

        System.out.println("CLIENT: IMAGENES DE LOGOS AÑADIDAS");
        VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText() + "CLIENT: IMAGENES DE LOGOS AÑADIDAS" + "\n");

        //Chat_friends
        listModel = new DefaultListModel();
        this.list_friends.setModel(listModel);
        list_friends.setCellRenderer(new ListaRender());
        

    }
    
    public class ListaRender extends JLabel implements ListCellRenderer{
        ImageIcon logo = new ImageIcon("imagenes/logo.jpg"); 
        ImageIcon foto1;
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            
            String valor = value.toString();
            setText(valor);
            
//            if(!"".equals(label_amigo.getText())){
//                for(int i = 0; i<amigos_del_usuario_mensajes.size();i++){
//                    int empi = amigos_del_usuario_mensajes.get(i).getId_user().indexOf(" ---");
//                    String amigo = amigos_del_usuario_mensajes.get(i).getId_user().substring(0, empi);
//                    String rutaUser = "";
//                    File ruta = new File("imagenes/"+amigo+".jpg");
//                    if(!ruta.exists()){
//                        try {
//                            rutaUser = traeFoto(amigo);
//                        } catch (IOException ex) {
//                            Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
//                        }                    
//                        
//                        foto1 = new ImageIcon(rutaUser); 
//                        
//                        Image image = foto1.getImage(); // transform it 
//                        Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
//                        foto1 = new ImageIcon(newimg);  // transform it back
//                        
//                        
//                    }else{
//                        foto1 = new ImageIcon(ruta.toString());
//                        Image image = foto1.getImage(); // transform it 
//                        Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
//                        foto1 = new ImageIcon(newimg);  // transform it back
//                    }
//                    setIcon(foto1);
//                }
//                
//            
//            }else{            
                if(!valor.equals("")){
                    setIcon(logo);
                }
//            }
            if(isSelected){
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }else{
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pestañas = new javax.swing.JTabbedPane();
        panel_chat = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();
        label_usuario_activo = new javax.swing.JLabel();
        boton_envio_mensajes = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        list_friends = new javax.swing.JList<>();
        label_amigo = new javax.swing.JLabel();
        label_imagen_amigo = new javax.swing.JLabel();
        label_imagen_user = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        text_area = new javax.swing.JTextArea();
        cargar = new javax.swing.JButton();
        miImagen = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TextAreaDebug = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        text.setColumns(20);
        text.setRows(5);
        jScrollPane2.setViewportView(text);

        label_usuario_activo.setText("Aqui va el usuario activo");

        boton_envio_mensajes.setText(">>");
        boton_envio_mensajes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_envio_mensajesActionPerformed(evt);
            }
        });

        list_friends.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_friendsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(list_friends);

        text_area.setColumns(20);
        text_area.setRows(5);
        scroll.setViewportView(text_area);

        cargar.setText("CARGAR");
        cargar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cargarMouseClicked(evt);
            }
        });
        cargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_chatLayout = new javax.swing.GroupLayout(panel_chat);
        panel_chat.setLayout(panel_chatLayout);
        panel_chatLayout.setHorizontalGroup(
            panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_chatLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_chatLayout.createSequentialGroup()
                        .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel_chatLayout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(boton_envio_mensajes)
                                .addGap(18, 18, 18)
                                .addComponent(cargar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_chatLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)
                                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panel_chatLayout.createSequentialGroup()
                        .addComponent(label_imagen_user, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(label_usuario_activo, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100)
                        .addComponent(label_imagen_amigo, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_chatLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(label_amigo, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_chatLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
                                .addComponent(miImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(19, Short.MAX_VALUE))))
        );
        panel_chatLayout.setVerticalGroup(
            panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_chatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_chatLayout.createSequentialGroup()
                        .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label_imagen_user, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_usuario_activo))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panel_chatLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label_imagen_amigo, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_chatLayout.createSequentialGroup()
                        .addComponent(miImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label_amigo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(panel_chatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(boton_envio_mensajes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(cargar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pestañas.addTab("CHAT", panel_chat);

        TextAreaDebug.setColumns(20);
        TextAreaDebug.setRows(5);
        jScrollPane4.setViewportView(TextAreaDebug);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1026, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addContainerGap())
        );

        pestañas.addTab("DEBUG", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pestañas)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pestañas, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
                .addContainerGap())
        );

        pestañas.getAccessibleContext().setAccessibleName("REGISTRO");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarActionPerformed
        if(!this.label_amigo.equals("")){
            try {
                try {
                    this.cargar_mensajes_anteriores(label_amigo.getText(), pos_amigo);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("No hay seleccionado nadie");
        }
    }//GEN-LAST:event_cargarActionPerformed

    private void cargarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cargarMouseClicked
        //        this.seleccionar_amigo(evt);
        //        try {
            //            this.cargar_mensajes_anteriores(label_amigo.getText(),pos_amigo);
            //        } catch (IOException ex) {
            //            Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
            //        }
    }//GEN-LAST:event_cargarMouseClicked

    private void list_friendsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_friendsMouseClicked
        this.seleccionar_amigo(evt);
        try {
            this.fotoAmigo();
            this.get_messages_friend(label_amigo.getText(), pos_amigo);
            //this.conexionCliente.esperar_mensaje();
        } catch (IOException ex) {
            Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_list_friendsMouseClicked

    private void boton_envio_mensajesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_envio_mensajesActionPerformed
        String texto;

        texto = this.text.getText();

        if (texto.length() == 1000) {
            JOptionPane.showMessageDialog(null, "Mensaje demasiado largo", "Mensaje de error",
                JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                this.enviar_mensaje(texto, pos_amigo);
            } catch (InterruptedException ex) {
                Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //this.text_area.append(this.label_usuario_activo.getText()+": "+texto+"\n");
        this.text.setText("");
    }//GEN-LAST:event_boton_envio_mensajesActionPerformed

    public void seleccionar_amigo(java.awt.event.MouseEvent evt) {
        JList theList = (JList) evt.getSource();
        //if (evt.getClickCount() == 2) {
        int index = theList.locationToIndex(evt.getPoint());
        if (index >= 0) {
            Object o = theList.getModel().getElementAt(index);
            System.out.println("Double-clicked on: " + o.toString());
            label_amigo.setText(o.toString());
            friend_mess = o.toString();
            pos_amigo = index;
            
            //ME TENGO QUE TRAER LA FOTO AQUI
            
        }
        //  }
    }

    public void getFriends(ArrayList amigos) {
        
        for (int i = 0; i < amigos.size(); i++) {
//            ImageIcon foto1 = null;
//            String rutaUser = "";
            listModel.addElement(amigos.get(i));
            AmigosDeUnUsuario_Mensajes amigo = new AmigosDeUnUsuario_Mensajes();
            amigo.setId_user((String) amigos.get(i));
                        
            File ruta = new File("imagenes/"+amigo.getId_user()+".jpg");
//            if(!ruta.exists()){
//                try {
//                    rutaUser = traeFoto(amigo.getId_user());
//                } catch (IOException ex) {
//                    Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
//                }                    
//
//                foto1 = new ImageIcon(rutaUser); 
//
//                Image image = foto1.getImage(); // transform it 
//                Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
//                foto1 = new ImageIcon(newimg);  // transform it back
//
//
//            }else{
//                foto1 = new ImageIcon(ruta.toString());
//                Image image = foto1.getImage(); // transform it 
//                Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
//                foto1 = new ImageIcon(newimg);  // transform it back
//            }

            
//            listModel.addElement(foto1.getImage());
            this.amigos_del_usuario_mensajes.add(amigo);
        }
        this.list_friends.setModel(listModel);

        System.out.println("CLIENT: AMIGOS EN LA LISTA");
        VistaClienteChats.TextAreaDebug.setText(VistaClienteChats.TextAreaDebug.getText() + "CLIENT: AMIGOS EN LA LISTA" + "\n");

    }

    public void get_messages_friend(String user_dest, int pos) throws IOException, InterruptedException {

        //Tengo que cambiar lo de los amigos_del_usuario_mensajes
        this.conexionCliente.conectar_con_server_leer_mensajes(user_dest, pos);

        this.text_area.setText("");
        this.amigos_del_usuario_mensajes.get(pos).ordenar();

        for (int i = 0; i < amigos_del_usuario_mensajes.get(pos).mensajes_array.size(); i++) {
            this.text_area.setText(text_area.getText() + amigos_del_usuario_mensajes.get(pos).mensajes_array.get(i).getId_user_orig() + "-->" + amigos_del_usuario_mensajes.get(pos).mensajes_array.get(i).getText() + "--->" + amigos_del_usuario_mensajes.get(pos).mensajes_array.get(i).getDatetime() + "\n");
        }

        this.conexionCliente.conectar_con_server_obtener_datos(user_dest, this);
//        if(nom_user.equals(usuario)){
//            vchats.label_usuario_activo.setText(nombreCompleto);
//        }else{
//            vchats.label_amigo.setText(nombreCompleto);
//        }
    }

    public void cargar_mensajes_anteriores(String user_dest, int pos) throws IOException, InterruptedException {

        //Tengo que cambiar lo de los amigos_del_usuario_mensajes
        this.conexionCliente.conectar_con_server_leer_mensajes(user_dest, pos);

        this.text_area.setText("");
        this.amigos_del_usuario_mensajes.get(pos).ordenar();
        for (int i = 0; i < amigos_del_usuario_mensajes.get(pos).mensajes_array.size(); i++) {
            this.text_area.setText(this.text_area.getText() + amigos_del_usuario_mensajes.get(pos).mensajes_array.get(i).getId_user_orig() + "-->" + amigos_del_usuario_mensajes.get(pos).mensajes_array.get(i).getText() + "--->" + amigos_del_usuario_mensajes.get(pos).mensajes_array.get(i).getDatetime() + "\n");
        }

        // this.conexionCliente.conectar_con_server_obtener_datos(user_dest);
    }

    public void enviar_mensaje(String texto, int pos) throws InterruptedException {
        this.conexionCliente.conectar_con_server_enviar_mensajes(texto, this.user_act, this.friend_mess);
        this.guardar_mensaje_memoria_local(texto, pos);

    }

    public void guardar_mensaje_memoria_local(String texto, int pos) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //for(int i = 0; i<this.amigos_del_usuario_mensajes.size();i++){
        // if(this.amigos_del_usuario_mensajes.get(pos).getId_user() == this.friend_mess){
        MensajesMapeo mess = new MensajesMapeo();
        mess.setId_user_orig(user_act);
        mess.setId_user_dest(friend_mess);
        mess.setText(texto);
        mess.setDateTime(sdf.format(timestamp));
        amigos_del_usuario_mensajes.get(pos).mensajes_array.add(mess);
        //this.amigos_del_usuario_mensajes.get(pos).setNum_men_recibidos(amigos_del_usuario_mensajes.get(pos).getNum_men_recibidos()+1);

        this.text_area.setText(this.text_area.getText() + this.user_act + "-->" + amigos_del_usuario_mensajes.get(pos).mensajes_array.get(amigos_del_usuario_mensajes.get(pos).mensajes_array.size() - 1).getText() + "--->" + amigos_del_usuario_mensajes.get(pos).mensajes_array.get(amigos_del_usuario_mensajes.get(pos).mensajes_array.size() - 1).getDatetime() + "\n");

        //}
        //}
    }

    public static void actualizar_estados_vista() {
        DefaultListModel listModel2 = new DefaultListModel();

        for (int i = 0; i < VistaClienteChats.amigos_del_usuario_mensajes.size(); i++) {
            listModel2.addElement(amigos_del_usuario_mensajes.get(i).getId_user());
        }
        VistaClienteChats.list_friends.setModel(listModel2);
        //HACER LA SELECCION

    }

    public void nuevo_mensaje(String amigo) {
//        if (amigo.equals(this.friend_mess)) {
//            this.text_area.setText(this.text_area.getText() + this.friend_mess + "-->" + amigos_del_usuario_mensajes.get(this.pos_amigo).mensajes_array.get(amigos_del_usuario_mensajes.get(pos_amigo).mensajes_array.size() - 1).getText() + "--->" + amigos_del_usuario_mensajes.get(pos_amigo).mensajes_array.get(amigos_del_usuario_mensajes.get(pos_amigo).mensajes_array.size() - 1).getDatetime() + "\n");
//        }
        
        System.out.println("VISTA");
        System.out.println(" U1 : " + this.amigos_del_usuario_mensajes.get(pos_amigo).getId_user());
        System.out.println(" U2 : " + amigo);
        if(this.amigos_del_usuario_mensajes.get(pos_amigo).getId_user().contains(amigo)){
            System.out.println("VISTA ENTRA");
            this.text_area.setText("");
            this.amigos_del_usuario_mensajes.get(pos_amigo).ordenar();
            for (int i = 0; i < amigos_del_usuario_mensajes.get(pos_amigo).mensajes_array.size(); i++) {
                this.text_area.setText(this.text_area.getText() + amigos_del_usuario_mensajes.get(pos_amigo).mensajes_array.get(i).getId_user_orig() + "-->" + amigos_del_usuario_mensajes.get(pos_amigo).mensajes_array.get(i).getText() + "--->" + amigos_del_usuario_mensajes.get(pos_amigo).mensajes_array.get(i).getDatetime() + "\n");
            }
            
        }
    }
    
    public String traeFoto(String usuario) throws IOException, InterruptedException{
        String ruta = this.conexionCliente.obtenerFoto(usuario);
        return ruta;
    }
    
    public void fotoAmigo() throws IOException, InterruptedException{
        String rutaUser;
        String amigo;
        ImageIcon foto1;
        
        int empi = this.label_amigo.getText().indexOf(" ---");
        amigo = this.label_amigo.getText().substring(0, empi);
        
        File ruta = new File("imagenes/"+amigo+".jpg");
        if(!ruta.exists()){
            rutaUser = this.traeFoto(amigo);
            
            for(int i = 0; i<amigos_del_usuario_mensajes.size();i++){
                System.out.println("amigo: " + amigo);
                System.out.println("id : " + this.amigos_del_usuario_mensajes.get(i).getId_user());
                if(this.amigos_del_usuario_mensajes.get(i).getId_user().contains(amigo)){
                    System.out.println("i : " + i);
                    amigos_del_usuario_mensajes.get(i).setRuta_img(rutaUser);
                }
            }
            foto1 = new ImageIcon(rutaUser);
            
        }else{
            foto1 = new ImageIcon(ruta.toString());
        }
            
        Icon icono3 = new ImageIcon(foto1.getImage().getScaledInstance(label_imagen_amigo.getWidth(), label_imagen_amigo.getHeight(), Image.SCALE_DEFAULT));
        label_imagen_amigo.setIcon(icono3);
        this.repaint();
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaClienteChats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaClienteChats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaClienteChats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaClienteChats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</ediator-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                try {
//                    new VistaClienteChats().setVisible(true);
//                    
//                } catch (SQLException ex) {
//                    Logger.getLogger(VistaClienteChats.class.getName()).log(Level.SEVERE, null, ex);
//                }

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextArea TextAreaDebug;
    private javax.swing.JButton boton_envio_mensajes;
    private javax.swing.JButton cargar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JLabel label_amigo;
    private javax.swing.JLabel label_imagen_amigo;
    private javax.swing.JLabel label_imagen_user;
    public javax.swing.JLabel label_usuario_activo;
    private static javax.swing.JList<String> list_friends;
    private javax.swing.JLabel miImagen;
    private javax.swing.JPanel panel_chat;
    private javax.swing.JTabbedPane pestañas;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTextArea text;
    private javax.swing.JTextArea text_area;
    // End of variables declaration//GEN-END:variables

    /*Registro*/
    private String user;
    private String name;
    private String pass;
    private String sur1;
    private String sur2;

    /*Chat*/
    public String user_act;
    ArrayList chat_array_friends;
    static DefaultListModel listModel;
    String[] listData;
    MouseListener mouseListener;
    String friend_mess;

    ConexionClienteconServer2 conexionCliente;
    static ArrayList<AmigosDeUnUsuario_Mensajes> amigos_del_usuario_mensajes;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    int pos_amigo;
}
