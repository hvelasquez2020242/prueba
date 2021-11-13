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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.erwinvicente.bean.Empleados;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import org.erwinvicente.bean.Administracion;
import org.erwinvicente.bean.Cargos;
import org.erwinvicente.bean.Departamentos;
import org.erwinvicente.bean.Horarios;
import org.erwinvicente.db.Conexion;
import org.erwinvicente.report.GenerarReporte;
import org.erwinvicente.system.Principal;
/**
 *
 * @author esteb
 */
public class EmpleadosController implements Initializable{
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }
    
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Cargos> listaCargos;
    private ObservableList<Departamentos> listaDepartamentos;
    private ObservableList<Horarios> listaHorarios;
    
    private Principal escenarioPrincipal;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    private enum Operaciones{
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }
    
    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Empleados> listaEmpleados;
    
    @FXML
    void mostrarVistaMenuPrincipal(MouseEvent event) {
        this.escenarioPrincipal.mostrarMenuPrincipal();
    }

    @FXML
    void mostrarVistaHorarios(ActionEvent event) {
        escenarioPrincipal.mostrarHorarios();
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
    private TableView tblEmpleados;

    @FXML
    private TableColumn colId;

    @FXML
    private TableColumn colNombre;

    @FXML
    private TableColumn colApellido;

    @FXML
    private TableColumn colEmail;

    @FXML
    private TableColumn colTelefono;

    @FXML
    private TableColumn colFechaContrato;

    @FXML
    private TableColumn colSueldo;

    @FXML
    private TableColumn colIdDepartamento;

    @FXML
    private TableColumn colidCargo;

    @FXML
    private TableColumn colIdHorario;
    
    @FXML
    private TableColumn colIdAdministracion;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefonp;

    @FXML
    private TextField txtSueldo;

    @FXML
    private DatePicker dpkFechacontratacion;

    @FXML
    private ComboBox cmbIdDepartamento;

    @FXML
    private ComboBox cmbIdCargo;

    @FXML
    private ComboBox cmbIdHorarios;

    @FXML
    private ComboBox cmbIdAdministracion;
    
    public ObservableList<Empleados> getEmpleados(){
         ArrayList<Empleados> lista = new ArrayList<>();

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarEmpleados()}");
            ResultSet rs = pstmt.executeQuery();
                       
            while (rs.next()) {
                lista.add(new Empleados(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getDate("fechaContratacion"),
                        rs.getBigDecimal("sueldo"),
                        rs.getInt("idDepartamento"),
                        rs.getInt("idCargo"),
                        rs.getInt("idHorario"),
                        rs.getInt("idAdministracion")
                                
                        )
                        
                );
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        listaEmpleados = FXCollections.observableArrayList(lista);
        return listaEmpleados;
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
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colId.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombres"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidos"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Empleados, String>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleados, String>("telefono"));
        colFechaContrato.setCellValueFactory(new PropertyValueFactory<Empleados, DatePicker>("fechaContratacion"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, BigDecimal>("sueldo"));
        colIdDepartamento.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idDepartamento"));
        colIdHorario.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idHorario"));
        colidCargo.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idCargo"));
        colIdAdministracion.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("idAdministracion"));
        cmbIdAdministracion.setItems(getAdministracion());
        cmbIdCargo.setItems(getCargos());
        cmbIdDepartamento.setItems(getDepartamentos());
        cmbIdHorarios.setItems(getHorarios());
        
    }
    
    private void agregarEmpleados() {
        Empleados empleados = new Empleados();
        empleados.setNombres(txtNombre.getText());
        empleados.setApellidos(txtApellido.getText());
        empleados.setEmail(txtEmail.getText());
        empleados.setTelefono(txtTelefonp.getText());
        empleados.setFechaContratacion(Date.valueOf(dpkFechacontratacion.getValue()));
        empleados.setSueldo(new BigDecimal(txtSueldo.getText()));
        empleados.setIdAdministracion(((Administracion)cmbIdAdministracion.getSelectionModel().getSelectedItem()).getId());
        empleados.setIdCargo(((Cargos)cmbIdCargo.getSelectionModel().getSelectedItem()).getId());
        empleados.setIdHorario(((Horarios) cmbIdHorarios.getSelectionModel().getSelectedItem()).getId());
        empleados.setIdDepartamento(((Departamentos) cmbIdDepartamento.getSelectionModel().getSelectedItem()).getId());
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarEmpleados(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            stmt.setString(1, empleados.getNombres());
            stmt.setString(2, empleados.getApellidos());            
            stmt.setString(3, empleados.getEmail());
            stmt.setString(4, empleados.getTelefono());
            stmt.setDate(5, empleados.getFechaContratacion());
            stmt.setBigDecimal(6, empleados.getSueldo());
            stmt.setInt(7, empleados.getIdAdministracion());
            stmt.setInt(8, empleados.getIdCargo());
            stmt.setInt(9, empleados.getIdHorario());
            stmt.setInt(10, empleados.getIdDepartamento());
            stmt.execute();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public void editarLocales(){
        Empleados empleados = (Empleados) tblEmpleados.getSelectionModel().getSelectedItem();
        empleados.setId(Integer.parseInt(txtId.getText()));
        empleados.setNombres(txtNombre.getText());
        empleados.setApellidos(txtApellido.getText());
        empleados.setEmail(txtEmail.getText());
        empleados.setTelefono(txtTelefonp.getText());
        empleados.setSueldo(new BigDecimal(txtSueldo.getText()));
        empleados.setIdAdministracion(((Administracion)cmbIdAdministracion.getSelectionModel().getSelectedItem()).getId());
        empleados.setIdCargo(((Cargos)cmbIdCargo.getSelectionModel().getSelectedItem()).getId());
        empleados.setIdHorario(((Horarios) cmbIdHorarios.getSelectionModel().getSelectedItem()).getId());
        empleados.setIdDepartamento(((Departamentos) cmbIdDepartamento.getSelectionModel().getSelectedItem()).getId());

        try {
            PreparedStatement stmt; 
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarEmpleados(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            stmt.setInt(1, empleados.getId());
            stmt.setString(2, empleados.getNombres());
            stmt.setString(2, empleados.getApellidos());
            stmt.setString(2, empleados.getEmail());
            stmt.setString(2, empleados.getTelefono());
            stmt.setDate(6, empleados.getFechaContratacion());
            stmt.setBigDecimal(7, empleados.getSueldo());
            stmt.setInt(7, empleados.getIdAdministracion());
            stmt.setInt(8, empleados.getIdCargo());
            stmt.setInt(9, empleados.getIdHorario());
            stmt.setInt(10, empleados.getIdDepartamento());
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void eliminarEmplados() {
        PreparedStatement stmt = null;
        Empleados empleados = ((Empleados) tblEmpleados.getSelectionModel().getSelectedItem());

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarLocales(?)}");
            stmt.setInt(1, empleados.getId());
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
    
    public void desactivarControles() {
      txtId.setDisable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtEmail.setEditable(false);
        txtTelefonp.setEditable(false);
        txtSueldo.setEditable(false);
        
        cmbIdAdministracion.setDisable(true);
        cmbIdCargo.setDisable(true);
        cmbIdDepartamento.setDisable(true);
        cmbIdHorarios.setDisable(true);
        
        dpkFechacontratacion.setDisable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtApellido.clear();
        txtNombre.clear();
        txtEmail.clear();
        txtTelefonp.clear();
        
        txtSueldo.clear();
        cmbIdCargo.valueProperty().set(null);
        cmbIdAdministracion.valueProperty().set(null);
        cmbIdDepartamento.valueProperty().set(null);
        cmbIdHorarios.valueProperty().set(null);
        
        //dpkFechacontratacion
    }
    
    private void activarControles() {
        txtId.setEditable(false);

        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtEmail.setEditable(true);
        txtTelefonp.setEditable(true);
        txtSueldo.setEditable(false);
        cmbIdAdministracion.setDisable(false);
        cmbIdCargo.setDisable(false);
        cmbIdDepartamento.setDisable(false);
        cmbIdHorarios.setEditable(false);
        
        //dpkFechacontratacion.setEditable(false);
    }
    
     
     
    public ObservableList<Cargos> getCargos(){
        ArrayList<Cargos> listado = new ArrayList<Cargos>();
        PreparedStatement stmt = null;
        ResultSet resultado = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCargos()}");
            resultado = stmt.executeQuery();
            
            while (resultado.next()){
                listado.add(new Cargos(
                          resultado.getInt("id")
                        , resultado.getString("nombre")
                )
                );
            }
        listaCargos = FXCollections.observableArrayList(listado);
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla en la base de datos.");
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

        return listaCargos;
    }
   
     public ObservableList<Horarios> getHorarios(){
        ArrayList<Horarios> listado = new ArrayList<Horarios>();
        PreparedStatement stmt = null;
        ResultSet resultado = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarHorarios()}");
            resultado = stmt.executeQuery();
            
             while (resultado.next()){
                listado.add(new Horarios(
                          resultado.getInt("id")
                        , resultado.getTime("horarioEntrada")
                        , resultado.getTime("horarioSalida")
                        , resultado.getBoolean("lunes")
                        , resultado.getBoolean("martes")
                        , resultado.getBoolean("miercoles")
                        , resultado.getBoolean("jueves")
                        , resultado.getBoolean("viernes")
                        
                )
                );
            }
            
        listaHorarios = FXCollections.observableArrayList(listado);
        } catch (SQLException e) {
            System.err.println("\nSe produjo un error al intentar consultar la tabla en la base de datos.");
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

        return listaHorarios;
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
                eliminarEmplados();
                cargarDatos();
                limpiarControles();
        }
          
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
                          
                
                agregarEmpleados();
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
    void reporte(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: 
            Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReportEmpleados.jasper", "ReporteEmpleado", parametros);
        }
    }

    public boolean existeElementoSeleccionado() {
        return tblEmpleados.getSelectionModel().getSelectedItem() != null;
    }
    
    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
   
              txtId.setText(String.valueOf( ((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getId()));
              txtNombre.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getNombres());
              txtApellido.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidos());
              txtEmail.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getEmail());
              txtTelefonp.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefono());
              cmbIdAdministracion.getSelectionModel().select(buscarAdministracion(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdAdministracion()));
              cmbIdCargo.getSelectionModel().select(buscarAdministracion(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdAdministracion()));
              cmbIdDepartamento.getSelectionModel().select(buscarAdministracion(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdAdministracion()));
              cmbIdHorarios.getSelectionModel().select(buscarAdministracion(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getIdAdministracion()));
             
        }
    }
}  