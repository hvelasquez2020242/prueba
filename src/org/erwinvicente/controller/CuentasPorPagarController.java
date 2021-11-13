/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erwinvicente.controller;

import com.jfoenix.controls.JFXDatePicker;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.erwinvicente.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import org.erwinvicente.bean.Administracion;
import org.erwinvicente.db.Conexion;
import org.erwinvicente.bean.CuentasPorPagar;
import org.erwinvicente.bean.Proveedores;
import javafx.scene.image.Image;
import org.erwinvicente.bean.CuentasPorCobrar;
import org.erwinvicente.report.GenerarReporte;

/**
 *
 * @author esteb
 */
public class CuentasPorPagarController implements Initializable {
    
    private Principal escenarioPrincipal; 

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    private ObservableList<CuentasPorPagar> listaCuetasPorPagar;
    
    private final String PAQUETE_IMAGES = "/org/erwinvicente/resource/images/";
    
    private enum Operaciones{
        NUEVO, GUARDAR, ACTUALIZAR, ELIMINAR, NINGUNO   
    }
    
    private Operaciones operaciones = Operaciones.NINGUNO;
    private ObservableList<CuentasPorPagar> listaCuentasPorPagar;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Proveedores>  listaProveedores;
    
    private ObservableList<Administracion> listaClientes;
    private ObservableList<Proveedores> listaLocales;
    

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
      cargarDatos();
    }
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
    private TableView tblCuentasPorPagar;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colNumeroFactura;

    @FXML
    private TableColumn colFechaLimite;

    @FXML
    private TableColumn colEstadoPago;

    @FXML
    private TableColumn colValorNeto;
    
    @FXML
    private TableColumn colIdAdministracion;

    @FXML
    private TableColumn colIdProveedor;

    @FXML
    private TextField txtId;

    @FXML
    private JFXDatePicker dpFechaLimite;

    @FXML
    private TextField txtEstadoPago;

    @FXML
    private TextField txtNumeroFactura;

    @FXML
    private TextField txtValorNeto;

     @FXML
    private ComboBox cmbIdAdministracion;

    @FXML
    private ComboBox cmbIdProveedor;
    
  
    
     public ObservableList<CuentasPorPagar> getCuentasPorPagar() {
        ArrayList<CuentasPorPagar> listado = new ArrayList<>();
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCuentasPorPagar()}");
            System.out.println(pstmt);    
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                listado.add(new CuentasPorPagar(
                        rs.getInt("id"), 
                        rs.getString("numeroFactura"), 
                        rs.getDate("fechaLimitePago"), 
                        rs.getString("estadoPago"), 
                        rs.getBigDecimal("valorNetoPago"), 
                        rs.getInt("idAdministracion"), 
                        rs.getInt("idProveedor") 
                        
                    )
                );
            }
            
            listaCuetasPorPagar = FXCollections.observableArrayList(listado);
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla Cuentas por cobrar de la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        return listaCuetasPorPagar;
    }
        
     
        
        public void cargarDatos() {
         tblCuentasPorPagar.setItems(getCuentasPorPagar());
         colId.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer> ("id"));
         colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String> ("numeroFactura"));
         colFechaLimite.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Date> ("fechaLimitePago") );
         colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String> ("estadoPago"));
         colValorNeto.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, BigDecimal> ("valorNetoPago"));
         colIdAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer> ("idAdministracion"));
         colIdProveedor.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer> ("idProveedor"));
         cmbIdAdministracion.setItems(getAdministracion());
         cmbIdProveedor.setItems(getProveedores());
        }
        public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarProveedores()}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Proveedores(rs.getInt("id"),
                        rs.getString("nit"),
                        rs.getString("servicioPrestado"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra")));
            }
            pstmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        listaProveedores = FXCollections.observableArrayList(lista);
        return listaProveedores;
    }

            public ObservableList<Administracion> getAdministracion() {

        ArrayList<Administracion> listado = new ArrayList<Administracion>();

        try {
            PreparedStatement stmt;
            //CallableStatement stmt;  
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion()}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                listado.add(new Administracion(resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")
                )
                );

            }
            resultado.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;

    }
    
    public ObservableList<CuentasPorPagar> getHorarios() {
        
        ArrayList <CuentasPorPagar> listado = new ArrayList<CuentasPorPagar>();
                
        try {
            PreparedStatement stmt ; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCuentasPorPagar()}"); 
            ResultSet resultado = stmt.executeQuery();
            
            while (resultado.next()) {
                listado.add(new CuentasPorPagar(resultado.getInt("id")
                        , resultado.getString("numeroFactura")
                        , resultado.getDate("fechaLimitePago")
                        , resultado.getString("estadoPago")
                        , resultado.getBigDecimal("valorNetoPago")
                        , resultado.getInt("idAdministracion")
                        , resultado.getInt("idProveedores")
                )
             );
                
           }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaCuentasPorPagar = FXCollections.observableArrayList(listado);
        return listaCuentasPorPagar;   
        
    }
    
     private void activarControles() {
        txtId.setEditable(false);

        txtNumeroFactura.setEditable(true);
        txtValorNeto.setEditable(true);
        txtEstadoPago.setEditable(true);
        
        cmbIdAdministracion.setDisable(false);
        cmbIdProveedor.setDisable(false);
        
       dpFechaLimite.getEditor().clear();
    }

    public void limpiarControles() {
        txtId.clear();
        txtNumeroFactura.clear();
        txtValorNeto.clear();
        txtEstadoPago.clear();
        
        cmbIdAdministracion.valueProperty().set(null);
        cmbIdProveedor.valueProperty().set(null);
       
        //dpFechaLimite
    }
    
    public boolean existeElementoSeleccionado() {
        return tblCuentasPorPagar.getSelectionModel().getSelectedItem() != null;
    }
    
    public void desactivarControles() {
        txtId.setEditable(false);
        txtNumeroFactura.setEditable(false);
        txtValorNeto.setEditable(false);
        txtEstadoPago.setEditable(false);
        
        cmbIdAdministracion.setEditable(false);
        cmbIdProveedor.setEditable(false);
    }
    
    public void agregarCuentasPorPagar() {
        CuentasPorPagar cuentaPagar = new CuentasPorPagar();
        cuentaPagar.setNumeroFactura(txtNumeroFactura.getText());
        cuentaPagar.setEstadoPago(txtEstadoPago.getText());
        cuentaPagar.setFechaLimitePago(Date.valueOf(dpFechaLimite.getValue()));
        cuentaPagar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        cuentaPagar.setEstadoPago(txtEstadoPago.getText());
        cuentaPagar.setIdAdministracion(((Administracion)cmbIdAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaPagar.setIdProveedor(((Proveedores)cmbIdProveedor.getSelectionModel().getSelectedItem()).getId());
        
        PreparedStatement pstmt = null;
        
        try {
           pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCuentasPorPagar(?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, cuentaPagar.getNumeroFactura());
            pstmt.setDate(2, cuentaPagar.getFechaLimitePago());
            pstmt.setString(3, cuentaPagar.getEstadoPago());
            pstmt.setBigDecimal(4, cuentaPagar.getValorNetoPago());
            pstmt.setInt(5, cuentaPagar.getIdAdministracion());
            pstmt.setInt(6, cuentaPagar.getIdProveedor());
            pstmt.executeQuery();
            
            System.out.println(pstmt);
            
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar agregar una nueva Cuenta por pagar");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void editar(ActionEvent event) {
        switch (operaciones) {
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                    activarControles();
                    
                    btnEditar.setText("Actualizar");
                    imgEditar.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));
                    
                    btnReporte.setText("Cancelar");
                    imgReporte.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));
                    
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    
                    operaciones = Operaciones.ACTUALIZAR;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar, selecciona un registro");
                    alert.show();
                }

                break;
            case ACTUALIZAR:
                
                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtId);
                listaTextField.add(txtNumeroFactura);
                listaTextField.add(txtValorNeto);
                listaTextField.add(txtEstadoPago);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbIdAdministracion);
                listaComboBox.add(cmbIdProveedor);

                if (escenarioPrincipal.validar(listaTextField, listaComboBox)) {
                   // editarCuentasPorCobrar();
                    limpiarControles();
                    desactivarControles();
                    cargarDatos();

                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);

                    btnEditar.setText("Editar");
                    imgEditar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));

                    btnReporte.setText("Reporte");
                    imgReporte.setImage(new Image(PAQUETE_IMAGES + "reporte.png"));

                    operaciones = Operaciones.NINGUNO;
                    break;                    
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor llene todos lo campos de textos");
                    alert.show();
                    
                }      
                
        }
    }

    @FXML
    public void eliminarCuentasPorPagar() {
        CuentasPorPagar cuentasPorPagar = ((CuentasPorPagar) tblCuentasPorPagar.getSelectionModel().getSelectedItem());

        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCuentasPorPagar(?)}");
            pstmt.setInt(1, cuentasPorPagar.getId());
            pstmt.executeQuery();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kinal mall");
            alert.setHeaderText(null);
            alert.setContentText("Registro eliminado correctamente");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
            }
        }

    }

    @FXML
    void mostrarVistaMenuPrincipal(MouseEvent event) {

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
                //CuentasPorPagar dato = new CuentasPorPagar();
                //dato.setNumeroFactura(txtNumeroFactura.getText(), dato.setFechaLimitePago(new Date (dpFechaLimite.getText)), dato.setEstadoPago(txtEstadoPago.getText()), dato.setValorNetoPago(txtValorNeto.getText()), dato.setIdAdministracion(cmbIdAdministracion.get))

                agregarCuentasPorPagar();
                cargarDatos();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                operaciones = Operaciones.NINGUNO;
                break;
        }

    }

    @FXML
    void reporte(ActionEvent event) {
        switch (operaciones) {
            case NINGUNO: 
            Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteCuentasPorPagar.jasper", "ReporteCuentasPorPagar", parametros);
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        switch (operaciones) {
            case GUARDAR:
                
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "nuevo.png"));
                
                
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));
                
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                limpiarControles();
                desactivarControles();
                
                operaciones = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                                        
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Está seguro que desea eliminar este registro?");
                    
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarCuentasPorPagar();
                        limpiarControles();
                        cargarDatos();                        
                    }
                    
                    
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Antes de continuar, selecciona un registro");
                    alert.show();
                }

                break;
        }
    }
    @FXML
    void seleccionarElemento(MouseEvent event) {

    }

}

