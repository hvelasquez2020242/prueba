/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erwinvicente.controller;

import com.mysql.cj.jdbc.result.ResultSetImpl;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.erwinvicente.bean.Locales;
import org.erwinvicente.bean.TipoCliente;
import org.erwinvicente.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.erwinvicente.db.Conexion;
import org.erwinvicente.report.GenerarReporte;

/**
 *
 * @author esteb
 */
public class LocalesController implements Initializable {

     private Principal escenarioPrincipal;
    
    @FXML
    private TableColumn colId;
    @FXML
    private CheckBox chDisponibilidad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }
    

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

    private ObservableList<Locales> listaLocales;

    @FXML
    public void mostrarVistaMenuPrincipal() {
        this.escenarioPrincipal.mostrarMenuPrincipal();
    }

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
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtSaldoContra;
    @FXML
    private TableView tblLocales;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSaldoContra;
    @FXML
    private TableColumn colMesesPendientes;
    @FXML
    private TableColumn colDisponibilidad;
    @FXML
    private TableColumn colValorLocal;
    @FXML
    private TableColumn colValorAdministrativo;
    @FXML
    private TextField txtMesesPendientes;
    @FXML
    private TextField txtValorLocal;
    @FXML
    private TextField txtLocalDisponible;
    @FXML
    private TextField txtValorAdministrativo;

    private ObservableList<Locales> listarLocales;


    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales()}");
            ResultSet rs = pstmt.executeQuery();
            
            int contador = 0;
            
            while (rs.next()) {
                lista.add(new Locales(
                        rs.getInt("id"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"),
                        rs.getInt("mesesPendientes"),
                        rs.getBoolean("disponibilidad"),
                        rs.getBigDecimal("valorLocal"),
                        rs.getBigDecimal("valorAdministracion")
                        )
                        
                );
                if(rs.getBoolean("Disponibilidad")== true){
                    contador++;
                }
            }
            txtLocalDisponible.setText(String.valueOf(contador));
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;
    }

    public void cargarDatos() {
        tblLocales.setItems(getLocales());
        colId.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("id"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("saldoContra"));
        colMesesPendientes.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("mesesPendientes"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Locales, Boolean>("disponibilidad"));
        colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("valorLocal"));
        colValorAdministrativo.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("valorAdministracion"));

    }
    
    private void agregarLocales() {
        Locales locales = new Locales();
        locales.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        locales.setSaldoContra(new BigDecimal(txtSaldoContra.getText()));
        locales.setMesesPendientes(Integer.valueOf(txtMesesPendientes.getText()));
        locales.setDisponibilidad(chDisponibilidad.isSelected());
        locales.setValorLocal(new BigDecimal(txtValorLocal.getText()));
        locales.setValorAdministracion(new BigDecimal(txtValorAdministrativo.getText()));
        
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarLocales(?, ?, ?, ?, ?, ?)}");
            stmt.setBigDecimal(1, locales.getSaldoFavor());
            stmt.setBigDecimal(2, locales.getSaldoContra());
            stmt.setInt(3, locales.getMesesPendientes());
            stmt.setBoolean(4, locales.getDisponibilidad());
            stmt.setBigDecimal(5, locales.getValorLocal());
            stmt.setBigDecimal(6, locales.getValorAdministracion());
            stmt.execute();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
public void editarLocales(){
        Locales locales = (Locales) tblLocales.getSelectionModel().getSelectedItem();
        locales.setId(Integer.parseInt(txtId.getText()));
        locales.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        locales.setSaldoContra(new BigDecimal(txtSaldoContra.getText()));
        locales.setMesesPendientes(Integer.valueOf(txtMesesPendientes.getText()));
        locales.setDisponibilidad(chDisponibilidad.isSelected());
        locales.setValorLocal(new BigDecimal(txtValorLocal.getText()));
        locales.setValorAdministracion(new BigDecimal(txtValorAdministrativo.getText()));

        try {
            PreparedStatement stmt; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarLocales(?, ?, ?, ?, ?, ?, ?)}");
            stmt.setInt(1, locales.getId());
            stmt.setBigDecimal(2, locales.getSaldoFavor());
            stmt.setBigDecimal(3, locales.getSaldoContra());
            stmt.setInt(4, locales.getMesesPendientes());
            stmt.setBoolean(5, locales.getDisponibilidad());
            stmt.setBigDecimal(6, locales.getValorLocal());
            stmt.setBigDecimal(7, locales.getValorAdministracion());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void desactivarControles() {
      txtId.setDisable(false);
        txtSaldoContra.setEditable(false);
        chDisponibilidad.setDisable(false);
        txtMesesPendientes.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtValorAdministrativo.setEditable(false);
        txtValorLocal.setEditable(false);
        
    }

    
    public void activarControles() {
      txtId.setDisable(false);
        txtSaldoContra.setEditable(true);
        chDisponibilidad.setDisable(false);
        txtMesesPendientes.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtValorAdministrativo.setEditable(true);
        txtValorLocal.setEditable(true);
        //tx.setEditable(false);
    }

    public void limpiarControles() {
        txtId.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        txtMesesPendientes.clear();
         chDisponibilidad.setSelected(false);
        txtValorLocal.clear();
        txtValorAdministrativo.clear();
    }
    
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
                          
                
                agregarLocales();
                cargarDatos();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cargarDatos();
                operacion = Operaciones.NINGUNO;
                
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
                
                editarLocales();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                operacion = Operaciones.NINGUNO;
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
                eliminarLocales();
                cargarDatos();
                limpiarControles();
        }
          
        }

    @FXML
    private void reporte(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: 
            Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteLocales.jasper", "ReporteLocales", parametros);
        }
        
    }

    @FXML
    private void seleccionarElemento(MouseEvent event) {
        desactivarControles();
        txtId.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getId()));
            txtSaldoFavor.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf((((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra())));
            txtMesesPendientes.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getMesesPendientes()));          
            chDisponibilidad.setSelected(((Locales)tblLocales.getSelectionModel().getSelectedItem()).getDisponibilidad());
            txtValorLocal.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorLocal()));
            txtValorAdministrativo.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorAdministracion()));
    }

    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarMenuPrincipal();

    }
   private void eliminarLocales() {
        PreparedStatement stmt = null;
        Locales locales = ((Locales) tblLocales.getSelectionModel().getSelectedItem());

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarLocales(?)}");
            stmt.setInt(1, locales.getId());
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
    }
    
}

   
     
     