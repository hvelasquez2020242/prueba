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
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import org.erwinvicente.system.Principal;
/**
 *
 * @author esteb
 */
public class AutorController implements Initializable {
    
    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
    @FXML
    void MostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }
    
}
