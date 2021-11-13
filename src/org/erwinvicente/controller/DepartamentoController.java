package org.erwinvicente.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mysql.cj.jdbc.result.ResultSetImpl;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.erwinvicente.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.erwinvicente.bean.Departamentos;
import org.erwinvicente.db.Conexion;
import org.erwinvicente.report.GenerarReporte;

/**
 *
 * @author esteb
 */
public class DepartamentoController implements Initializable {

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
    private TextField txtNombres;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableView tblDepartamento;
    @FXML
    private TextField txtNombre;
    

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Departamentos> listaDepartamentos;

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
                Departamentos dato = new Departamentos();
                dato.setNombre(txtNombre.getText());

                agregarDepartamentos();
                cargarDatos();
                desabilitarCampos();
                limpiarCampos();
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
                Departamentos dato = new Departamentos();
                dato.setNombre(txtNombre.getText());

                int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar?", "Advertencia", JOptionPane.YES_NO_OPTION);
                if (respuesta == 0) {
                    eliminarDepartamento();
                    limpiarCampos();
                    cargarDatos();
                    break;
                }

        }

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
                Departamentos registro = new Departamentos();
                registro.setNombre(txtNombre.getText());

                if ((registro.getNombre().equals("")) && (registro.getClass().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Seleccione lo que quiere actualizar");

                } else {

                    editarDepartamento();
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
    private void reporte(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: 
            Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteDepartamento.jasper", "ReporteDepartamento", parametros);
        }
    }

    

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
         this.escenaPrincipal.mostrarMenuPrincipal();
    }

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Principal escenaPrincipal;

    public Principal getEscenaPrincipal() {
        return escenaPrincipal;
    }

    public void setEscenaPrincipal(Principal escenaPrincipal) {
        this.escenaPrincipal = escenaPrincipal;
    }


    public void cargarDatos() {
        tblDepartamento.setItems(getDepartamentos());
        colId.setCellValueFactory(new PropertyValueFactory<Departamentos, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Departamentos, String>("nombre"));
    }

    private void agregarDepartamentos() {
        Departamentos dato = new Departamentos();
        dato.setNombre(txtNombre.getText());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarDepartamentos(?)}");
            stmt.setString(1, dato.getNombre());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editarDepartamento() {
        Departamentos registro = (Departamentos) tblDepartamento.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setNombre(txtNombre.getText());
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarDepartamentos(?, ?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getNombre());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarDepartamento() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarDepartamentos(?)}");

            stmt.setInt(1, ((Departamentos) tblDepartamento.getSelectionModel().getSelectedItem()).getId());

            stmt.execute();
            System.out.println(stmt.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
@FXML
    public void seleccionarElemento() {
        txtId.setText(String.valueOf(((Departamentos) tblDepartamento.getSelectionModel().getSelectedItem()).getId()));
        txtNombre.setText(((Departamentos) tblDepartamento.getSelectionModel().getSelectedItem()).getNombre());
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

    public ObservableList<Departamentos> getDepartamentos() {
        ArrayList<Departamentos> listado = new ArrayList<>();

        try {
            PreparedStatement pstmt;
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarDepartamentos()}");
            ResultSet resultado = pstmt.executeQuery();
            while (resultado.next()) {
            listado.add(new Departamentos(resultado.getInt("id"),
            resultado.getString("nombre")));
            }
            resultado.close();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        listaDepartamentos = FXCollections.observableArrayList(listado);
        return listaDepartamentos;
    }

}
