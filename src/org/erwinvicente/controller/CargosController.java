/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erwinvicente.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.erwinvicente.system.Principal;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.erwinvicente.db.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.erwinvicente.bean.Cargos;
import org.erwinvicente.report.GenerarReporte;
/**
 *
 * @author esteb
 */
public class CargosController implements Initializable{

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }
    
    private ObservableList<Cargos> listaCargos; 
    private Principal escenarioPrincipal;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;
    
     @FXML
    private Button btnNuevo;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private Button btnEditar;

    @FXML
    private ImageView imgEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private Button btnReporte;

    @FXML
    private ImageView imgReporte;

    @FXML
    private TableView tblCargos;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colNombre;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtId;
    
    @FXML
    void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarMenuPrincipal();
    }
    
     public ObservableList<Cargos> getCargos() {
        
        ArrayList <Cargos> listado = new ArrayList<Cargos>();
                
        try {
            PreparedStatement stmt ; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCargos()}"); 
            ResultSet resultado = stmt.executeQuery();
            
            while (resultado.next()) {
                listado.add(new Cargos(resultado.getInt("id")
                        , resultado.getString("nombre")
                )
             );
                
           }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaCargos = FXCollections.observableArrayList(listado);
        return listaCargos;   
        
    }
     
    public void cargarDatos() {
        tblCargos.setItems(getCargos());
        colId.setCellValueFactory(new PropertyValueFactory<Cargos, Integer >("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cargos, String > ("nombre"));
    }
    
    private void agregarAdministracion() {
        Cargos registro = new Cargos();
        registro.setNombre(txtNombre.getText());
        
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCargos(?)}");
            stmt.setString(1, registro.getNombre());
            stmt.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public void editarAdministracion(){
        Cargos registro = (Cargos) tblCargos.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setNombre(txtNombre.getText());
        try {
            PreparedStatement stmt; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCargos(?, ?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getNombre());
            stmt.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void eliminarAdministracion(){
        try {
            PreparedStatement stmt; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCargos(?)}");
           
            
            stmt.setInt(1, ((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getId());            
            stmt.execute();
            System.out.println(stmt.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
               
    }

     @FXML
    public void seleccionarElemento() {
        txtId.setText(String.valueOf(((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getId()));
        txtNombre.setText(((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getNombre());
    }
    
    private void habilitarCampos() {            
        txtId.setDisable(false);
        txtNombre.setEditable(true);
    }
    private void desabilitarCampos() {
        txtId.setDisable(false);
        txtNombre.setEditable(false);
    }
    private void limpiarCampos() {
       txtId.clear();
       txtNombre.clear();
    }
    
    @FXML
    private void editar(ActionEvent event) {
        
       switch (operacion) {
            case NINGUNO:
                habilitarCampos();
                btnEditar.setText("Actualzar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                operacion = Operaciones.ACTUALIZAR;
                break; 
            case ACTUALIZAR: 
                Cargos registro = new Cargos();
                registro.setNombre(txtNombre.getText());
                
                
               if ((registro.getNombre().equals(""))) {
               JOptionPane.showMessageDialog(null, "Seleccione lo que quiere actualizar");

                } else {
                                
                editarAdministracion();
                desabilitarCampos();
                cargarDatos();
                limpiarCampos();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                operacion = Operaciones.NINGUNO;
                }
               break; 
        
                
           
        }
        
    }

   @FXML
    private void eliminar(ActionEvent event) {
        switch (operacion) {
            case GUARDAR: 
                limpiarCampos();
                desabilitarCampos();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                operacion = Operaciones.NINGUNO;
                break; 
            case NINGUNO:
                Cargos registro = new Cargos();
                registro.setNombre(txtNombre.getText());
                
                if ((registro.getNombre().equals(""))) {
               JOptionPane.showMessageDialog(null, "Seleccione lo que quiere eliminar");

    } else {
       int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar?", "Advertencia", JOptionPane.YES_NO_OPTION);
            if (respuesta == 0) {
        eliminarAdministracion();
        limpiarCampos();
        cargarDatos();
             } 
                } 
               
                break;
        }
    }



   @FXML
    private void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: 
                habilitarCampos();
                limpiarCampos();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR: 
                Cargos registro = new Cargos();
                registro.setNombre(txtNombre.getText());
                
                if ((registro.getNombre().equals(""))&&(registro.getNombre().equals(""))) {
               JOptionPane.showMessageDialog(null, "Llene el campo nombre");

                 
                
                } 
                else {
                
                agregarAdministracion();
                cargarDatos();
                desabilitarCampos();
                limpiarCampos();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cargarDatos();
                operacion = Operaciones.NINGUNO;
                
                }
                break; 
        }

    }

    @FXML
    void reporte(ActionEvent event) {
        switch(operacion){
            case NINGUNO:
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteCargos.jasper", "ReporteCargos", parametros);
        }
    }

    @FXML
    void seleccionarElemento(MouseEvent event) {

    }

    
    
}
