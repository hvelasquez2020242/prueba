/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erwinvicente.controller;


import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import org.erwinvicente.bean.Administracion;
import org.erwinvicente.bean.Clientes;
import org.erwinvicente.bean.Locales;
import org.erwinvicente.db.Conexion;
import javafx.scene.control.cell.PropertyValueFactory;
import org.erwinvicente.bean.CuentasPorCobrar;
import org.erwinvicente.report.GenerarReporte;
import org.erwinvicente.system.Principal;


/**
 *
 * @author esteb
 */
public class CuentasPorCobrarController implements Initializable {

    private Principal escenarioPrincipal;
    
    private final String PAQUETE_IMAGES = "/org/erwinvicente/resource/images/";
    @FXML
    private TableColumn colIdClientes;
    @FXML
    private TextField txtNumeroFactura;
    @FXML
    private ComboBox cmbIdClientes;
    @FXML
    private ComboBox cmbIdLocal;
    @FXML
    private ComboBox cmbIlAdministracion;

    private enum Operaciones {
        NUEVO, GUARDAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;
    
    private ObservableList<CuentasPorCobrar> listaCuetasPorCobrar;
    
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<Locales> listaLocales;
    

    @FXML
    private Button btnNuevo;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TableView tblCuentasPorCobrar;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colFactura;
    @FXML
    private TableColumn colAnio;
    @FXML
    private TableColumn colMes;
    @FXML
    private TableColumn colValorNeto;
    @FXML
    private TableColumn colEstadoPago;
    @FXML
    private TableColumn colIdLocal;
    @FXML
    private TextField txtId;
    private TextField txtFactura;
    @FXML
    private TextField txtEstadoPago;
    @FXML
    private TextField txtValorNeto;
    private ComboBox cmbAdministracion;

    @FXML
    private Spinner<Integer> spnAnio;

    private SpinnerValueFactory<Integer> valueFactoryAnio;

    @FXML
    private Spinner<Integer> spnMes;

    private SpinnerValueFactory<Integer> valueFactoryMes;

    private ComboBox cmbCliente;
    private ComboBox cmbLocal;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        valueFactoryAnio = new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2050, 2021);
        spnAnio.setValueFactory(valueFactoryAnio);

        valueFactoryMes = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 6);
        spnMes.setValueFactory(valueFactoryMes);
        
        cargarDatos();
    }
    
    
    public ObservableList<CuentasPorCobrar> getCuentasPorCobrar() {
        ArrayList<CuentasPorCobrar> listado = new ArrayList<>();
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCuentasPorCobrar()}");
            System.out.println(pstmt);    
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                listado.add(new CuentasPorCobrar(
                        rs.getInt("id"), 
                        rs.getString("numeroFactura"), 
                        rs.getInt("anio"), 
                        rs.getInt("mes"), 
                        rs.getBigDecimal("valorNetoPago"), 
                        rs.getString("estadoPago"), 
                        rs.getInt("idAdministracion"), 
                        rs.getInt("idCliente"), 
                        rs.getInt("idLocal")
                    )
                );
            }
            
            listaCuetasPorCobrar = FXCollections.observableArrayList(listado);
           
            
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
                    listaCuetasPorCobrar = FXCollections.observableArrayList(listado);

        return listaCuetasPorCobrar;
    }
    
    

    public ObservableList<Administracion> getAdministracion() {
        ArrayList<Administracion> listado = new ArrayList<Administracion>();
        PreparedStatement stmt = null;
        ResultSet resultado = null;
        try {
            
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion()}");
            resultado = stmt.executeQuery();

            while (resultado.next()) {
                listado.add(new Administracion(
                        resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")
                )
                );
            }
            listaAdministracion = FXCollections.observableArrayList(listado);
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla Administración en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultado.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listaAdministracion;
    }

    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales}");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Locales(
                        rs.getInt("id"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"),
                        rs.getInt("mesesPendientes"),
                        rs.getBoolean("disponibilidad"),
                        rs.getBigDecimal("valorLocal"),
                        rs.getBigDecimal("valorAdministracion"))
                );
            }
            listaLocales = FXCollections.observableArrayList(lista);
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla Locales en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        
        return listaLocales;
    }

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarClientes}");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Clientes(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("idTipoCliente")
                )
                );
            }
            listaClientes = FXCollections.observableArrayList(lista);
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla Clientes en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return listaClientes;
    }
    
    public Clientes buscarCliente(int id) {
        Clientes cliente = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarClientes(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                cliente = new Clientes(
                        rs.getInt("id"), 
                        rs.getString("nombres"), 
                        rs.getString("apellidos"), 
                        rs.getString("telefono"), 
                        rs.getString("direccion"), 
                        rs.getString("email"), 
                        rs.getInt("idTipoCliente")
                );
            }
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla Clientes del registro con el ID: " + id);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cliente;
    }
    
    public Locales buscarLocal(int id) {
        Locales local = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarLocales(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                local = new Locales(
                        rs.getInt("id"), 
                        rs.getBigDecimal("saldoFavor"), 
                        rs.getBigDecimal("saldoContra"), 
                        rs.getInt("mesesPendientes"), 
                        rs.getBoolean("disponibilidad"), 
                        rs.getBigDecimal("valorLocal"), 
                        rs.getBigDecimal("valorAdministracion")
                );
            }
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla Locales del registro con el ID: " + id);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return local;
    }    
    
    public Administracion buscarAdministracion(int id) {
        Administracion administracion = null;
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarAdministracion(?)}");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                administracion = new Administracion(
                        rs.getInt("id"), 
                        rs.getString("direccion"), 
                        rs.getString("telefono")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar buscar una Administracion con el ID " + id);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
                    
        return administracion;
    }
    
    
    public void eliminarCuentasPorCobrar() {
        if (existeElementoSeleccionado()) {
            CuentasPorCobrar cuentaCobrar = (CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem();
            
            System.out.println(cuentaCobrar);
            
            PreparedStatement pstmt = null;
            
            try {
                pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCuentasPorCobrar(?)}");
                pstmt.setInt(1, cuentaCobrar.getId());
                
                System.out.println(pstmt);
                
                pstmt.execute();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Registro eliminado exitosamente");
                alert.show();
                
            } catch (SQLException e) {
                System.err.println("\nSe produjo un error al intentar eliminar el registro con el id " + cuentaCobrar.getId());
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    public void editarCuentasPorCobrar() {
        CuentasPorCobrar cuentaCobrar = new CuentasPorCobrar();
        cuentaCobrar.setId(Integer.parseInt(txtId.getText()));
        cuentaCobrar.setNumeroFactura(txtFactura.getText());
        cuentaCobrar.setAnio(spnAnio.getValue());
        cuentaCobrar.setMes(spnMes.getValue());
        cuentaCobrar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        cuentaCobrar.setEstadoPago(txtEstadoPago.getText());
        cuentaCobrar.setIdAdministracion(((Administracion)cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setIdCliente(((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setIdLocal(((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId());
        
        PreparedStatement pstmt = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCuentasPorCobrar(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            
            pstmt.setInt(1, cuentaCobrar.getId());
            
            pstmt.setString(2, cuentaCobrar.getNumeroFactura());
            pstmt.setInt(3, cuentaCobrar.getAnio());
            pstmt.setInt(4, cuentaCobrar.getMes());
            pstmt.setBigDecimal(5, cuentaCobrar.getValorNetoPago());
            pstmt.setString(6, cuentaCobrar.getEstadoPago());
            pstmt.setInt(7, cuentaCobrar.getIdAdministracion());
            pstmt.setInt(8, cuentaCobrar.getIdCliente());
            pstmt.setInt(9, cuentaCobrar.getIdLocal());
            
            System.out.println(pstmt);
            
            pstmt.execute();
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar editar una Cuenta por cobrar");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }        
    }
    
    
    public void agregarCuentasPorCobrar() {
        CuentasPorCobrar cuentaCobrar = new CuentasPorCobrar();
        cuentaCobrar.setNumeroFactura(txtNumeroFactura.getText());
        cuentaCobrar.setAnio(spnAnio.getValue());
        cuentaCobrar.setMes(spnMes.getValue());
        cuentaCobrar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        cuentaCobrar.setEstadoPago(txtEstadoPago.getText());
        cuentaCobrar.setIdAdministracion(((Administracion)cmbIlAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setIdCliente(((Clientes) cmbIdClientes.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setIdLocal(((Locales) cmbIdLocal.getSelectionModel().getSelectedItem()).getId());
        
        PreparedStatement pstmt = null;
        
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCuentasPorCobrar(?, ?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setString(1, cuentaCobrar.getNumeroFactura());
            pstmt.setInt(2, cuentaCobrar.getAnio());
            pstmt.setInt(3, cuentaCobrar.getMes());
            pstmt.setBigDecimal(4, cuentaCobrar.getValorNetoPago());
            pstmt.setString(5, cuentaCobrar.getEstadoPago());
            pstmt.setInt(6, cuentaCobrar.getIdAdministracion());
            pstmt.setInt(7, cuentaCobrar.getIdCliente());
            pstmt.setInt(8, cuentaCobrar.getIdLocal());
            
            System.out.println(pstmt);
            
            pstmt.execute();
            
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar agregar una nueva Cuenta por cobrar");
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    public void cargarDatos() {
        tblCuentasPorCobrar.setItems(getCuentasPorCobrar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("id"));
        colFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("numeroFactura"));
        colAnio.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("anio"));
        colMes.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("mes"));
        colValorNeto.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, BigDecimal>("valorNetoPago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("estadoPago"));
      //  colIdAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("idAdministracion"));
          colIdClientes.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("idCliente"));
        colIdLocal.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("idLocal"));
        
       cmbIlAdministracion.setItems(getAdministracion());
        cmbIdClientes.setItems(getClientes());
        cmbIdLocal.setItems(getLocales());
    }
    
    public boolean existeElementoSeleccionado() {
        return tblCuentasPorCobrar.getSelectionModel().getSelectedItem() != null;
    }
    
    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf( ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getId()));
            txtNumeroFactura.setText(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getNumeroFactura());
            spnAnio.getValueFactory().setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getAnio());
            spnMes.getValueFactory().setValue(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getMes());
            txtValorNeto.setText(String.valueOf(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            txtEstadoPago.setText(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getEstadoPago());
            
            cmbIlAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getIdAdministracion()));
            cmbIdClientes.getSelectionModel().select(buscarCliente( ((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getIdCliente() ));
            cmbIdLocal.getSelectionModel().select(buscarLocal(((CuentasPorCobrar) tblCuentasPorCobrar.getSelectionModel().getSelectedItem()).getIdLocal()));
        }
    }
    
    
        
    private void activarControles() {
        txtId.setEditable(false);

        txtNumeroFactura.setEditable(true);
        txtValorNeto.setEditable(true);
        txtEstadoPago.setEditable(true);

        spnAnio.setDisable(false);
        spnMes.setDisable(false);

        cmbIlAdministracion.setDisable(false);
        cmbIdClientes.setDisable(false);
        cmbIdLocal.setDisable(false);
    }

    public void limpiarControles() {
        txtId.clear();
        txtNumeroFactura.clear();
        txtValorNeto.clear();
        txtEstadoPago.clear();

        spnAnio.getValueFactory().setValue(2021);
        spnMes.getValueFactory().setValue(1);

        cmbIlAdministracion.valueProperty().set(null);
        cmbIdClientes.valueProperty().set(null);
        cmbIdLocal.valueProperty().set(null);
    }
    
    
    public void desactivarControles() {
        txtId.setEditable(false);
        txtNumeroFactura.setEditable(false);
        txtValorNeto.setEditable(false);
        txtEstadoPago.setEditable(false);
        
        spnAnio.setDisable(true);
        spnMes.setDisable(true);
        
        cmbIlAdministracion.setDisable(true);
        cmbIdClientes.setDisable(true);
        cmbIdLocal.setDisable(true);
    }

    @FXML
    private void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                
                btnNuevo.setText("Guardar");
//                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "nuevo.png"));
                
                btnEditar.setDisable(true);
                
                btnEliminar.setText("Cancelar");
//                imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));
                
                btnReporte.setDisable(true);
                
                operacion = Operaciones.GUARDAR;
                break;
                
            case GUARDAR:

                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtNumeroFactura);
                listaTextField.add(txtValorNeto);
                listaTextField.add(txtEstadoPago);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbIlAdministracion);
                listaComboBox.add(cmbIdClientes);
                listaComboBox.add(cmbIdLocal);

                if (validar(listaTextField, listaComboBox)) {

                    agregarCuentasPorCobrar();
                    cargarDatos();
                    desactivarControles();
                    limpiarControles();
                    
                    btnNuevo.setText("Nuevo");
                    imgNuevo.setImage(new Image(PAQUETE_IMAGES + "nuevo.png"));
                    
                    btnEliminar.setText("Eliminar");
                    imgEliminar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));
                    
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    
                    operacion = Operaciones.NINGUNO;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Por favor llene todos lo campos de textos");
                    alert.show();
                    
                }

                break;
        }
    }
    public boolean validar(ArrayList<TextField> listadoCampos, ArrayList<ComboBox> listaComboBox) {
        boolean respuesta = true;
        
        if (cmbIlAdministracion.getSelectionModel().getSelectedItem() == null) {
            return false;
        }
        for (ComboBox combobox : listaComboBox) {
            if (combobox.getSelectionModel().getSelectedItem() == null) {
                return false;
            }
        }
        for (TextField campoTexto : listadoCampos) {
            if (campoTexto.getText().trim().isEmpty()) {
                return false;
            }
        }
        return respuesta;
    }


    @FXML
    private void eliminar(ActionEvent event) {
        switch (operacion) {
            case GUARDAR:
                
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "nuevo.png"));
                
                
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));
                
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                
                limpiarControles();
                desactivarControles();
                
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                                        
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Está seguro que desea eliminar este registro?");
                    
                    Optional<ButtonType> respuesta = alert.showAndWait();
                    
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarCuentasPorCobrar();
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
    private void editar(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                    activarControles();
                    
                    btnEditar.setText("Actualizar");
                    imgEditar.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));
                    
                    btnReporte.setText("Cancelar");
                    imgReporte.setImage(new Image(PAQUETE_IMAGES + "cancelar.png"));
                    
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    
                    operacion = Operaciones.ACTUALIZAR;
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
                listaTextField.add(txtFactura);
                listaTextField.add(txtValorNeto);
                listaTextField.add(txtEstadoPago);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbLocal);

                if (escenarioPrincipal.validar(listaTextField, listaComboBox)) {
                    editarCuentasPorCobrar();
                    limpiarControles();
                    desactivarControles();
                    cargarDatos();

                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);

                    btnEditar.setText("Editar");
                    imgEditar.setImage(new Image(PAQUETE_IMAGES + "editar.png"));

                    btnReporte.setText("Reporte");
                    imgReporte.setImage(new Image(PAQUETE_IMAGES + "reporte.png"));

                    operacion = Operaciones.NINGUNO;
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
    private void reporte(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: 
            Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReportesCuentasPorCobrar.jasper", "ReportesCuentasPorCobrar", parametros);
        }
       
    }
    
    
    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }
    
   /* public boolean validarTexto(String dato){
        String patron = "\\D+$";
        
    }*/

}