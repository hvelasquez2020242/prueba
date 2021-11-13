/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erwinvicente.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.erwinvicente.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.SpringLayout;
import org.erwinvicente.db.Conexion;
import com.jfoenix.controls.JFXButton;
import java.util.Base64;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.erwinvicente.bean.Usuario;
/**
 *
 * @author esteb
 */
public class LoginController implements Initializable{
   private Principal escenarioPrincipal;
    @FXML
    private TextField txtUser;

    @FXML
    private PasswordField psPass;
    
    private Usuario usuario;
    
    @FXML
    void validar(ActionEvent event) {
        String user = txtUser.getText();
        String pass = psPass.getText();
        
        getPassword(user);
        
        if(pass.equals(usuario.getPass())){
            escenarioPrincipal.mostrarMenuPrincipal();
        }
    }
    
    
    private void getPassword (String user){
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try{
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarUsuario(?)}");
            pstmt.setString(1, user);
            System.out.println(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()){
                usuario = new Usuario(rs.getString("user"), rs.getString("pass"), rs.getString("nombre"), rs.getInt("rol"));
                usuario.setPass(new String(Base64.getDecoder().decode(usuario.getPass())));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }
    
}
