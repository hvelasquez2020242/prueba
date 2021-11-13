/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erwinvicente.controller;

import com.jfoenix.controls.JFXTimePicker;
import com.sun.media.jfxmediaimpl.HostUtils;
import java.net.URL;
import java.util.ResourceBundle;
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
import org.erwinvicente.system.Principal;
import org.erwinvicente.bean.Horarios;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javax.swing.JOptionPane;
import org.erwinvicente.db.Conexion;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import org.erwinvicente.report.GenerarReporte;

/**
 *
 * @author esteb
 */
public class HorariosController implements Initializable {

    private final String PAQUETE_IMAGES = "/org/erwinvicente/resource/images/";

    // ---------------------------------------------------------------------------
    private ObservableList<Horarios> listaHorarios;
    @FXML
    private TableColumn colHorariosSalida;
    @FXML
    private JFXTimePicker tpHoraEntrada;
    @FXML
    private JFXTimePicker tpHoraSalida;

    //----------------------------------------------------------------------------
    private enum Operaciones {
        NUEVO, GUARDAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    //-------------------------------------------------------------------------
    private Principal escenarioPrincipal;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    //-------------------------------------------------------------------------
    

    private TextField txtHorarioEntrada;
    @FXML
    private CheckBox chkLunes;
    @FXML
    private CheckBox chkMartes;
    @FXML
    private CheckBox chkJueves;
    @FXML
    private CheckBox chkMiercoles;
    @FXML
    private CheckBox chkViernes;
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
    private TableView tblHorarios;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colHorarioEntrada;
    @FXML
    private TableColumn colLunes;
    @FXML
    private TableColumn colMartes;
    @FXML
    private TableColumn colMiercoles;
    @FXML
    private TableColumn colJueves;
    @FXML
    private TableColumn colViernes;
    @FXML
    private TextField txtId;

    //-------------------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();

        // Locale locale = new Locale("es", "GT");
        // Locale.setDefault(locale);
//        tpHorarioEntrada.set24HourView(false);
        //      tpHorarioSalida.set24HourView(false);
        //    tpHorarioEntrada.setEditable(false);
        //  tpHorarioSalida.setEditable(false);
    }

    //-------------------------------------------------------------------------
    public ObservableList<Horarios> getHorarios() {
        ArrayList<Horarios> lista = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarHorarios()}");
            System.out.println(pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Horarios horario = new Horarios(
                        rs.getInt("id"),
                        rs.getTime("horarioSalida"),
                        rs.getTime("horarioEntrada"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("martes"),
                        rs.getBoolean("miercoles"),
                        rs.getBoolean("jueves"),
                        rs.getBoolean("viernes")
                );
                lista.add(horario);

            }

            listaHorarios = FXCollections.observableArrayList(lista);

        } catch (SQLException e) {
            System.out.println("\nSe produjo un error al consultar la lista de Horarios");
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
        return listaHorarios;
    }

    //-------------------------------------------------------------------------
    public void cargarDatos() {

        tblHorarios.setItems(getHorarios());
        colId.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("id"));
        colHorarioEntrada.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioEntrada"));
        colHorariosSalida.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));

    }

    //-------------------------------------------------------------------------
    public void agregarHorarios() {

        Horarios horario = new Horarios();

        horario.setHorarioEntrada(Time.valueOf(tpHoraEntrada.getValue()));
        horario.setHorarioSalida(Time.valueOf(tpHoraSalida.getValue()));
        horario.setLunes(chkLunes.isSelected());
        horario.setMartes(chkMartes.isSelected());
        horario.setMiercoles(chkMiercoles.isSelected());
        horario.setJueves(chkJueves.isSelected());
        horario.setViernes(chkViernes.isSelected());

        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarHorarios(?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setTime(1, horario.getHorarioSalida());
            pstmt.setTime(2, horario.getHorarioEntrada());

            pstmt.setBoolean(3, horario.isLunes());
            pstmt.setBoolean(4, horario.isMartes());
            pstmt.setBoolean(5, horario.isMiercoles());
            pstmt.setBoolean(6, horario.isJueves());
            pstmt.setBoolean(7, horario.isViernes());

            System.out.println(pstmt.toString());

            pstmt.execute();

        } catch (SQLException e) {
            System.out.println("\nSe produjo un error al intentar agragar un nuevo horario");
            e.printStackTrace();
        } finally {

            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //-------------------------------------------------------------------------
    @FXML
    private void seleccionarElemento(MouseEvent event) {

        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getId()));

            tpHoraEntrada.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada().toLocalTime());
            tpHoraSalida.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida().toLocalTime());

            chkLunes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isLunes());
            chkMartes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMartes());
            chkMiercoles.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles());
            chkJueves.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isJueves());
            chkViernes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isViernes());

        } else {
            Alert vacio = new Alert(Alert.AlertType.ERROR);
            vacio.setTitle("Error");
            vacio.setContentText("Este campo esta vacio");
            vacio.setHeaderText(null);
            vacio.show();
        }
    }
    //-------------------------------------------------------------------------

    public void eliminarHorarios() {

        if (existeElementoSeleccionado()) {

            Horarios horarios = (Horarios) tblHorarios.getSelectionModel().getSelectedItem();

            System.out.println(horarios);

            PreparedStatement pstmt = null;

            try {
                pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarHorarios(?)}");

                pstmt.setInt(1, ((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getId());

                System.out.println(pstmt);

                pstmt.execute();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Registro eliminado exitosamente");
                alert.show();

            } catch (SQLException e) {
                System.err.println("\nSe produjo un error al intentar eliminar el registro con el id " + horarios.getId());
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

    //-------------------------------------------------------------------------
    public void editarHorarios() {

        Horarios horario = new Horarios();
        horario.setId(Integer.parseInt(txtId.getText()));
        horario.setHorarioEntrada(Time.valueOf(tpHoraEntrada.getValue()));
        horario.setHorarioSalida(Time.valueOf(tpHoraSalida.getValue()));
        horario.setLunes(chkLunes.isSelected());
        horario.setMartes(chkMartes.isSelected());
        horario.setMiercoles(chkMiercoles.isSelected());
        horario.setJueves(chkJueves.isSelected());
        horario.setViernes(chkViernes.isSelected());

        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarHorarios(?, ?, ?, ?, ?, ?, ?, ?)}");

            pstmt.setInt(1, horario.getId());
            pstmt.setTime(2, horario.getHorarioSalida());
            pstmt.setTime(3, horario.getHorarioEntrada());

            pstmt.setBoolean(4, horario.isLunes());
            pstmt.setBoolean(5, horario.isMartes());
            pstmt.setBoolean(6, horario.isMiercoles());
            pstmt.setBoolean(7, horario.isJueves());
            pstmt.setBoolean(8, horario.isViernes());

            System.out.println(pstmt.toString());

            pstmt.execute();

        } catch (SQLException e) {
            System.out.println("\nSe produjo un error al intentar editar un nuevo horario");
            e.printStackTrace();
        } finally {

            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //-------------------------------------------------------------------------
    public void activarControles() {

        txtId.setEditable(false);
        txtId.setDisable(false);
        tpHoraEntrada.setDisable(false);
        tpHoraSalida.setDisable(false);
        chkLunes.setDisable(false);
        chkMartes.setDisable(false);
        chkMiercoles.setDisable(false);
        chkJueves.setDisable(false);
        chkViernes.setDisable(false);
    }

    public void desactivarControles() {

        txtId.setEditable(false);
        txtId.setDisable(true);
        tpHoraEntrada.setDisable(true);
        tpHoraSalida.setDisable(true);
        chkLunes.setDisable(true);
        chkMartes.setDisable(true);
        chkMiercoles.setDisable(true);
        chkJueves.setDisable(true);
        chkViernes.setDisable(true);
    }

    public void limpiarControles() {

        txtId.clear();
        tpHoraEntrada.getEditor().clear();
        tpHoraSalida.getEditor().clear();
        chkLunes.setSelected(false);
        chkMartes.setSelected(false);
        chkMiercoles.setSelected(false);
        chkJueves.setSelected(false);
        chkViernes.setSelected(false);

    }

    //-------------------------------------------------------------------------
    public boolean existeElementoSeleccionado() {
        return tblHorarios.getSelectionModel().getSelectedItem() != null;
    }

    //-------------------------------------------------------------------------------------------------------------
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
                Horarios registro = new Horarios();
                registro.setHorarioEntrada(Time.valueOf(tpHoraEntrada.getValue()));
                registro.setHorarioSalida(Time.valueOf(tpHoraSalida.getValue()));
                registro.setLunes(true);
                registro.setMartes(true);
                registro.setMiercoles(true);
                registro.setJueves(true);
                registro.setViernes(true);
                
                agregarHorarios();
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
               // break; 
        }

    
        
//-------------------------------------------------------------------------
    @FXML
    private void eliminar(ActionEvent event) {
        switch (operacion) {
            case GUARDAR:

                btnNuevo.setText("Nuevo");

                btnEliminar.setText("Eliminar");

                imgNuevo.setImage(new Image(PAQUETE_IMAGES + "nuevo1.png"));
                imgEliminar.setImage(new Image(PAQUETE_IMAGES + "eliminar.png"));

                btnEditar.setDisable(false);
                btnReporte.setDisable(false);

                limpiarControles();
                desactivarControles();

                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO: // Eliminación
                if (existeElementoSeleccionado()) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Está seguro que desea eliminar este registro?");

                    Optional<ButtonType> respuesta = alert.showAndWait();

                    if (respuesta.get() == ButtonType.OK) {
                        eliminarHorarios();
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

//-------------------------------------------------------------------------
    @FXML
    private void editar(ActionEvent event) {

        switch (operacion) {
            case NINGUNO:
                if (existeElementoSeleccionado()) {
                    activarControles();

                    btnEditar.setText("Actualizar");

                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image(PAQUETE_IMAGES + "guardar.png"));
                    imgReporte.setImage(new Image(PAQUETE_IMAGES + "cancel.png"));

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

                if (true) {
                    editarHorarios();
                    limpiarControles();
                    desactivarControles();
                    cargarDatos();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);

                    btnEditar.setText("Editar");

                    btnReporte.setText("Reporte");
                    imgReporte.setImage(new Image(PAQUETE_IMAGES + "reporte.png"));
                    imgEditar.setImage(new Image(PAQUETE_IMAGES + "editar1.png"));

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

//-------------------------------------------------------------------------
    @FXML
    private void reporte(ActionEvent event) {
        switch (operacion) {
            case NINGUNO: 
            Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteHorarios.jasper", "ReporteHorario", parametros);
        }
    }

    //-------------------------------------------------------------------------
    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarMenuPrincipal();
    }

    //-------------------------------------------------------------------------
    private boolean validarHorarios() {
        //HH:MM:SS   00:00:00   23:59:59
        String horarioEntrada = txtHorarioEntrada.getText();
        Pattern pattern = Pattern.compile("([01][0-9]|[2][0123]):([0-5][0-9]):([0-5][0-9])");
        Matcher matcher = pattern.matcher(horarioEntrada);

        boolean resultado = matcher.matches();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(String.valueOf(resultado));
        alert.show();

        return resultado;
    }

    //-------------------------------------------------------------------------
}
