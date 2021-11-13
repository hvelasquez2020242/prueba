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
public class ClientesController implements Initializable {

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
    private TextField txtNombres;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TableView tblClientes;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colApellidos;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEmail;
    @FXML
    private TableColumn colTipoCliente;
    @FXML
    private TableColumn colNombre;
    @FXML
    private ComboBox cmbTipoCliente;
   
    
    

    
 public boolean validar(ArrayList<TextField> listadoCampos, ArrayList<ComboBox> listaComboBox) {
       boolean respuesta = true; 

       if(cmbTipoCliente.getSelectionModel().getSelectedItem() == null) {
           return false;
       }
       for (ComboBox comboBox : listaComboBox){
           if (comboBox.getSelectionModel().getSelectedItem() == null) {
               return false;
           } 
       }
       for (TextField campoTexto : listadoCampos) {
           if(campoTexto.getText().trim().isEmpty()){
               return false;
           }
       }
       return respuesta;
   }
        
    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }
    
    
    private Operaciones operacion = Operaciones.NINGUNO;
    private ObservableList<Clientes> listaClientes;
    private ObservableList<TipoCliente> listaTipoCliente;
    private ObservableList<Locales> listaLocales;

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }
    
    public void mostrarMenuPrincipal(){
       this.escenarioPrincipal.mostrarMenuPrincipal();
    }
    
    @FXML
    void mostrarVistaCuentasPorCobrar(ActionEvent event) {
        escenarioPrincipal.mostrarCuentasPorCobrar();
    }

    

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL_sp_ListarClientes()}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Clientes(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("email"),
                        rs.getInt("idTipoCliente"))
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        listaClientes = FXCollections.observableArrayList(lista);
        return listaClientes;
    }

    public void cargarDatos() {
        System.out.println(getClientes());
        tblClientes.setItems(getClientes());
        colId.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
        colTipoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("idTipoCliente"));

        cmbTipoCliente.setItems(getTipoCliente());
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false);
        txtTelefono.setEditable(false);
        txtEmail.setEditable(false);
        txtDireccion.setEditable(false);
        cmbTipoCliente.setDisable(true);
    }

    public void activarControles() {
        txtId.setEditable(false);
        txtApellidos.setEditable(true);
        txtNombres.setEditable(true);
        txtTelefono.setEditable(true);
        txtEmail.setEditable(true);
        txtDireccion.setEditable(true);
        cmbTipoCliente.setDisable(false);
    }

    public void limpiarControles() {
        txtId.clear();
        txtApellidos.clear();
        txtNombres.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtDireccion.clear();
        cmbTipoCliente.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

  
   @FXML
    private void nuevo(ActionEvent event) {

        switch (operacion) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                operacion = Operaciones.GUARDAR;
                
                break;
            case GUARDAR:
                Clientes registro = new Clientes();
                registro.setDireccion(txtDireccion.getText());
                registro.setTelefono(txtTelefono.getText());

                if ((registro.getDireccion().equals("")) && (registro.getTelefono().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Llene todos los cuadros");

                } else if ((!(registro.getDireccion().equals(""))) && ((registro.getTelefono().equals("")))) {
                    JOptionPane.showMessageDialog(null, "Llene el numero de telefono");

                } else if (((registro.getDireccion().equals(""))) && (!(registro.getTelefono().equals("")))) {
                    JOptionPane.showMessageDialog(null, "Llene la administracion");

                } else {
                    

                    agregarClientes();
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
//                break;
        }

    }

    public void eliminarClientes() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarClientes(?)}");

            stmt.setInt(1, ((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getId());

            stmt.execute();
            System.out.println(stmt.toString());
        } catch (Exception e) {
            e.printStackTrace();
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
                Clientes registro = new Clientes();
                registro.setDireccion(txtDireccion.getText());
                registro.setTelefono(txtTelefono.getText());

                if ((registro.getDireccion().equals("")) && (registro.getTelefono().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Seleccione lo que quiere eliminar");

                } else {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar?", "Advertencia", JOptionPane.YES_NO_OPTION);
                    if (respuesta == 0) {
                        eliminarClientes();
                        limpiarControles();
                        cargarDatos();
                    }
                }

                break;
        }
    }

    public boolean validarTelefono(String numero) {
        Pattern pattern = Pattern.compile("^[d]{8}$");
        Matcher matcher = pattern.matcher(numero);
        return matcher.matches();
    }

    public void editarClientes() {
        Clientes registro = (Clientes) tblClientes.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
                registro.setNombre(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setEmail(txtEmail.getText());
        registro.setIdTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());


        try {
            PreparedStatement pstmt;
                pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarClientes(?, ?, ?, ?, ?, ?, ?)}");
                pstmt.setInt(1, registro.getId());
                pstmt.setString(2, registro.getNombre());
                pstmt.setString(3, registro.getApellidos());
                pstmt.setString(4, registro.getTelefono());
                pstmt.setString(5, registro.getDireccion());
                pstmt.setString(6, registro.getEmail());
                pstmt.setInt(7, registro.getIdTipoCliente());

                pstmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
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
                Clientes registro = new Clientes();
                registro.setDireccion(txtDireccion.getText());
                registro.setTelefono(txtTelefono.getText());

                if ((registro.getDireccion().equals("")) && (registro.getTelefono().equals(""))) {
                    JOptionPane.showMessageDialog(null, "Seleccione lo que quiere actualizar");

                } else {

                    editarClientes();
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
    private void reporte(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: 
            Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteClientes.jasper", "ReporteClientes", parametros);
        }
    }

    private TipoCliente buscarTipoCliente(int id) {

        TipoCliente registro = null;

        try {

            PreparedStatement pstmt;

            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarTipoCliente(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                registro = new TipoCliente(rs.getInt("id"), rs.getString("descripcion"));

            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return registro;
    }

    public boolean existeElementoSeleccionado() {
        return tblClientes.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    public void seleccionarElemento() {

        if (existeElementoSeleccionado()) {

            txtId.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getId()));
            txtNombres.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getNombre());
            txtApellidos.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getApellidos());
            txtTelefono.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getTelefono());
            txtDireccion.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getDireccion());
            txtEmail.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getEmail());

            cmbTipoCliente.getSelectionModel().select(buscarTipoCliente(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getIdTipoCliente()));
        }
    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }

    public void agregarClientes() {

        Clientes registro = new Clientes();
        registro.setNombre(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setTelefono(txtTelefono.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setEmail(txtEmail.getText());
        registro.setIdTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());
        PreparedStatement stmt = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarClientes(?, ?, ?, ?, ?, ?)}");
            stmt.setString(1, txtNombres.getText());
            stmt.setString(2, txtApellidos.getText());
            stmt.setString(3, txtTelefono.getText());
            stmt.setString(4, txtDireccion.getText());
            stmt.setString(5, txtEmail.getText());
            stmt.setInt(6, ((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1452) {
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

    }

    public ObservableList<TipoCliente> getTipoCliente() {
        ArrayList<TipoCliente> lista = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarTipoCLiente()}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new TipoCliente(rs.getInt("id"), rs.getString("descripcion")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        listaTipoCliente = FXCollections.observableArrayList(lista);
        return listaTipoCliente;
    }
    
}
