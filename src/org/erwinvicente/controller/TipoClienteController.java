/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erwinvicente.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.erwinvicente.bean.Clientes;
import org.erwinvicente.bean.TipoCliente;
import org.erwinvicente.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.erwinvicente.bean.Locales;
import org.erwinvicente.db.Conexion;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.erwinvicente.report.GenerarReporte;

/**
 *
 * @author esteb
 */
public class TipoClienteController implements Initializable {

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private Principal escenarioPrincipal;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtId;
    @FXML
    private TableColumn colId;
    @FXML
    private TableView tblTipoCliente;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TextField txtDescripcion;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    private ObservableList<TipoCliente> listaTipoCliente;

    public ObservableList<TipoCliente> getTipoCliente() {

        ArrayList<TipoCliente> listado = new ArrayList<>();

        try {
            PreparedStatement stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarTipoCliente()}");
            ResultSet rs = stmt.executeQuery();

            System.out.println("La conexion fue exitosa");
            while (rs.next()) {
                listado.add(new TipoCliente(rs.getInt("id"),
                         rs.getString("descripcion")
                )
                );

            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaTipoCliente = FXCollections.observableArrayList(listado);
        return listaTipoCliente;

    }

    public void cargarDatos() {
        tblTipoCliente.setItems(getTipoCliente());
        colId.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("id"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoCliente, String>("descripcion"));
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtDescripcion.setEditable(false);
    }

    public void activarControles() {
        txtId.setEditable(false);
        txtDescripcion.setEditable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtDescripcion.clear();
    }

    public void editarTipoCliente() {
        TipoCliente registro = (TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarTipoCliente(?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getDescripcion());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarTipoCliente() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarTipoCliente(?)}");

            stmt.setInt(1, ((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getId());

            stmt.execute();
            System.out.println(stmt.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarTipoCliente() {
        TipoCliente dato = new TipoCliente();
        dato.setDescripcion(txtDescripcion.getText());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarTipoCliente(?)}");
            stmt.setString(1, dato.getDescripcion());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private ObservableList<TipoCliente> listaTipoClientes;

    @FXML
    private void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                TipoCliente dato = new TipoCliente();
                dato.setDescripcion(txtDescripcion.getText());
                
                agregarTipoCliente();
                cargarDatos();
                activarControles();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cargarDatos();
                operacion = Operaciones.NINGUNO;
                break;
        }

    }

    @FXML
    private void editar(ActionEvent event) {

        switch (operacion) {
            case NINGUNO:
                activarControles();
                btnEditar.setText("Actualzar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                operacion = Operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                TipoCliente registro = new TipoCliente();
                registro.setDescripcion(txtDescripcion.getText());

                if ((registro.getDescripcion().equals("")) && (registro.getClass().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Seleccione lo que quiere actualizar");

                } else {

                    editarTipoCliente();
                    desactivarControles();
                    cargarDatos();
                    limpiarControles();
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
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                TipoCliente dato = new TipoCliente();
                dato.setDescripcion(txtDescripcion.getText());

                int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar?", "Advertencia", JOptionPane.YES_NO_OPTION);
                if (respuesta == 0) {
                    eliminarTipoCliente();
                    limpiarControles();
                    cargarDatos();
                    break;
                }

        }

    }

    @FXML
    private void reporte(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: 
            Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteTipoCliente.jasper", "ReporteTipoCliente", parametros);
        }
    }

    @FXML
    public void seleccionarElemento() {
        txtId.setText(String.valueOf(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getId()));
        txtDescripcion.setText(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getDescripcion());
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarMenuPrincipal();
    }
    
    

}
