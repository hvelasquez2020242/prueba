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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.erwinvicente.bean.Administracion;
import org.erwinvicente.bean.Clientes;
import org.erwinvicente.bean.Locales;
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
import javax.swing.plaf.RootPaneUI;
import org.erwinvicente.db.Conexion;
import org.erwinvicente.report.GenerarReporte;

/**
 *
 * @author esteb
 */
public class AdministracionController implements Initializable{

    private Principal escenarioPrincipal; 
    
    

    
   
   private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;
           
    private ObservableList<Administracion> listaAdministracion; 
        
    

    @FXML
    private TableView tblAdministracion;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colDireccion;

    @FXML
    private TableColumn colTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnReporte;
    
  
    
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
    
    public void mostrarVistaMenuPrincipal(){
        this.escenarioPrincipal.mostrarMenuPrincipal();
    }
   
    @FXML
    void mostrarVistaCargos(ActionEvent event) {
        escenarioPrincipal.mostrarCargos();
    }

    @FXML
    void mostrarVistaDepartamento(ActionEvent event) {
        escenarioPrincipal.mostrarDepartamentos();
    }

    @FXML
    void mostrarVistaLocales(ActionEvent event) {
        escenarioPrincipal.mostrarLocales();
    }

    @FXML
    void mostrarVistaTipoCliente(ActionEvent event) {
        escenarioPrincipal.mostrarTipoCliente();
    }

    
    @FXML
    public void mostrarAdministracion(){
        this.escenarioPrincipal.mostrarLocales();
    }
  public ObservableList<Administracion> getAdministracion() {
        
        ArrayList <Administracion> listado = new ArrayList<Administracion>();
                
        try {
            PreparedStatement stmt ; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion()}"); 
            ResultSet resultado = stmt.executeQuery();
            
            while (resultado.next()) {
                listado.add(new Administracion(resultado.getInt("id")
                        , resultado.getString("direccion")
                        , resultado.getString("telefono")
                )
             );
                
           }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;   
        
    }

  
 public void cargarDatos() {
        tblAdministracion.setItems(getAdministracion());
        colId.setCellValueFactory(new PropertyValueFactory<Administracion, Integer >("id"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Administracion, String > ("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Administracion, String > ("telefono"));
    }

    
    private void agregarAdministracion() {
        Administracion registro = new Administracion();
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());
        
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarAdministracion(?, ?)}");
            stmt.setString(1, registro.getDireccion());
            stmt.setString(2, registro.getTelefono());
            stmt.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public void editarAdministracion(){
        Administracion registro = (Administracion) tblAdministracion.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setDireccion(txtDireccion.getText());
        registro.setTelefono(txtTelefono.getText());
        try {
            PreparedStatement stmt; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarAdministracion(?, ?, ?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getDireccion());
            stmt.setString(3, registro.getTelefono());
            stmt.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void eliminarAdministracion(){
        try {
            PreparedStatement stmt; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarAdministracion(?)}");
           
            
            stmt.setInt(1, ((Administracion)tblAdministracion.getSelectionModel().getSelectedItem()).getId());
            
            stmt.execute();
            System.out.println(stmt.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
               
    }
    @FXML
    public void seleccionarElemento() {
        txtId.setText(String.valueOf(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getId()));
        txtDireccion.setText(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getDireccion());
        txtTelefono.setText(((Administracion) tblAdministracion.getSelectionModel().getSelectedItem()).getTelefono());
    }
    
    private void habilitarCampos() {
            
        txtId.setDisable(false);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
    }
    private void desabilitarCampos() {
         txtId.setDisable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
    }
    private void limpiarCampos() {
       txtId.clear();
       txtDireccion.clear();
       txtTelefono.clear();
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
                Administracion registro = new Administracion();
                registro.setDireccion(txtDireccion.getText());
                registro.setTelefono(txtTelefono.getText());
                
                if ((registro.getDireccion().equals(""))&&(registro.getTelefono().equals(""))) {
               JOptionPane.showMessageDialog(null, "Llene todos los cuadros");

                } 
                else if ((!(registro.getDireccion().equals("")))&&((registro.getTelefono().equals("")))) {
               JOptionPane.showMessageDialog(null, "Llene el numero de telefono");

                }
                else if (((registro.getDireccion().equals("")))&&(!(registro.getTelefono().equals("")))) {
               JOptionPane.showMessageDialog(null, "Llene todos los campos");

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
                Administracion registro = new Administracion();
                registro.setDireccion(txtDireccion.getText());
                registro.setTelefono(txtTelefono.getText());
                
                if ((registro.getDireccion().equals(""))&&(registro.getTelefono().equals(""))) {
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
                Administracion registro = new Administracion();
                registro.setDireccion(txtDireccion.getText());
                registro.setTelefono(txtTelefono.getText());
                
                
               if ((registro.getDireccion().equals(""))&&(registro.getTelefono().equals(""))) {
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
    private void reporte(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: 
            Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteAdministracion.jasper", "ReporteAdministracion", parametros);
        }
        
    }

    public static boolean validarNumeroTelefono(String datos){
        return datos.matches("[0-9]{1,8}");
    }
}
