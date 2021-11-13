/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erwinvicente.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.erwinvicente.bean.Proveedores;
import org.erwinvicente.system.Principal;
import org.erwinvicente.db.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javax.swing.JOptionPane;
import org.erwinvicente.report.GenerarReporte;

/**
 *
 * @author esteb
 */
public class ProveedoresController implements Initializable {

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }
    
    private Principal escenarioPrincipal; 

    private final String PAQUETE_IMAGES = "/org/erwinvicente/resource/images/";
    
    
    private enum Operaciones{
        NUEVO, GUARDAR, ACTUALIZAR, ELIMINAR, NINGUNO   
    }
    
    private Operaciones operaciones = Operaciones.NINGUNO;
    private ObservableList<Proveedores> listaProveedores; 

    
    /* public boolean validar(ArrayList<TextField> listadoCampos, ArrayList<TextField> listaArrayList) {
       boolean respuesta = true; 

       if(txtINombre.getSelection() == null) {
           return false;
       }
       for (TextField campoTexto : listadoCampos) {
           if(campoTexto.getText().trim().isEmpty()){
               return false;
           }
       }
       return respuesta;
   }*/
 

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
    private TableView tblProveedores;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colNit;

    @FXML
    private TableColumn colServicioPrestado;

    @FXML
    private TableColumn colTelefono;

    @FXML
    private TableColumn colDireccion;

    @FXML
    private TableColumn colSaldoFavor;

    @FXML
    private TableColumn colSaldoContra;

    @FXML
    private TextField txtNit;

    @FXML
    private TextField txtServicioPrestado;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtSaldoFavor;

    @FXML
    private TextField txtSaldoContra;

    @FXML
    void mostrarVistaCuentasPorPagar(ActionEvent event) {
        escenarioPrincipal.mostrarCuentasPorPagar();
    }
    
@FXML
    private void editar(ActionEvent event) {

        switch (operaciones) {
            case NINGUNO:
                activarControles();
                btnEditar.setText("Actualzar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                operaciones = Operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                
                editarProveedor();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                operaciones = Operaciones.NINGUNO;
                }
  
        }
    
    public ObservableList<Proveedores> getProveedores() {
        
        ArrayList <Proveedores> listado = new ArrayList<Proveedores>();
                
        try {
            PreparedStatement stmt ; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarProveedores()}"); 
            ResultSet resultado = stmt.executeQuery();
            
            while (resultado.next()) {
                listado.add(new Proveedores( resultado.getInt("id")
                        ,resultado.getString("nit") 
                        , resultado.getString("servicioPrestado")
                        , resultado.getString("telefono")
                        , resultado.getString("direccion")
                        , resultado.getBigDecimal("saldoFavor")
                        , resultado.getBigDecimal("saldoContra")
                )
             );
                
           }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaProveedores = FXCollections.observableArrayList(listado);
        return listaProveedores;   
        
    }
    
     private void agregarProveedor() {
        Proveedores registro = new Proveedores();
        registro.setNit(txtNit.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setSaldoContra(new BigDecimal(txtSaldoContra.getText()));
        registro.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarProveedores(?, ?, ?, ?, ?, ?)}");
            stmt.setString(1, registro.getNit());
            stmt.setString(2, registro.getServicioPrestado());
            stmt.setString(3, registro.getTelefono());
            stmt.setString(4, registro.getDireccion());
            stmt.setBigDecimal(5, new BigDecimal (txtSaldoContra.getText()));
            stmt.setBigDecimal(6, new BigDecimal(txtSaldoFavor.getText()));
            stmt.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
     
     public void editarProveedor(){
        Proveedores registro = (Proveedores) tblProveedores.getSelectionModel().getSelectedItem();
        registro.setNit(txtNit.getText());
        registro.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        registro.setSaldoContra(new BigDecimal(txtSaldoContra.getText()));
        registro.setServicioPrestado(txtServicioPrestado.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());

        try {
            PreparedStatement stmt; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarProveedores(?, ?, ?, ?, ?, ?)}");
            stmt.setString(1, registro.getNit());
            stmt.setString(2, registro.getServicioPrestado());
            stmt.setString(3, registro.getTelefono());
            stmt.setString(4, registro.getDireccion());
            stmt.setBigDecimal(5, registro.getSaldoContra());
            stmt.setBigDecimal(6, registro.getSaldoFavor());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     private void desactivarControles() {
        txtNit.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtServicioPrestado.setEditable(false);
        txtSaldoContra.setEditable(false);
        txtSaldoFavor.setEditable(false);
    }

     private void activarControles(){
        txtNit.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtServicioPrestado.setEditable(true);
        txtSaldoContra.setEditable(true);
        txtSaldoFavor.setEditable(true);
    }
     
      public void limpiarControles(){
        txtNit.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtServicioPrestado.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
    }
      
    public void cargarDatos() {
        tblProveedores.setItems(getProveedores());
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer >("nit"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedores, String > ("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedores, String > ("telefono"));
        colServicioPrestado.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("servicioPrestado"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoContra"));
    }
      
   @FXML
    private void eliminar(ActionEvent event) {
        switch (operaciones) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                operaciones = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                
                }
          
        }

   @FXML
    private void nuevo(ActionEvent event) {
        switch (operaciones) {
            case NINGUNO: 
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                operaciones = Operaciones.GUARDAR;
                break;
            case GUARDAR: 
                          
                
                agregarProveedor();
                cargarDatos();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cargarDatos();
                operaciones = Operaciones.NINGUNO;
                
                }
                
    }


   @FXML
    private void reporte(ActionEvent event) {
        switch (operaciones) {
            case NINGUNO: 
            Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteProveedores.jasper", "ReporteProveedor", parametros);
        }

    }

  
    public void seleccionarElemento() {
        txtNit.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNit()));
        txtDireccion.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getDireccion());
        txtTelefono.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getTelefono());
        txtServicioPrestado.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getServicioPrestado());
        txtSaldoContra.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra()));
        txtSaldoFavor.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor()));
    }
    
    
    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }


}

