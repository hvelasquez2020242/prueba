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
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.erwinvicente.system.Principal;

/**
 *
 * @author esteb
 */
public class MenuPrincipalController implements Initializable{

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
    void MostrarVistaAutor(ActionEvent event) {
        escenarioPrincipal.mostrarAutor();
    }
    @FXML
    private void mostrarVistaAdministracion(){ 
        escenarioPrincipal.mostrarAdministracion();
    }

    @FXML
    private void mostrarVistaClientes(ActionEvent event) {
        escenarioPrincipal.mostrarClientes();
    }

    @FXML
    private void mostrarVistaDepartamentos(ActionEvent event){
        escenarioPrincipal.mostrarDepartamentos();
    }

    @FXML
    private void mostrarVistaCuentasPorCobrar(ActionEvent event) {
        escenarioPrincipal.mostrarCuentasPorCobrar();
    }
    
    @FXML 
    private void mostrarVistaProveedores(ActionEvent event){
        escenarioPrincipal.mostrarPoveedores();
    }
    @FXML
    private void mostrarVistaHorarios(ActionEvent event){
        escenarioPrincipal.mostrarHorarios();
    }
    @FXML
    private void mostrarVistaCuentasPorPagar(ActionEvent event){
        escenarioPrincipal.mostrarCuentasPorPagar();
    }
     @FXML
    void mostrarVistaEmpleados(ActionEvent event) {
        escenarioPrincipal.mostrarEmpleados();
    }
    
    @FXML
    void mostrarVistaCargos(ActionEvent event) {
        escenarioPrincipal.mostrarCargos();
    }
}

