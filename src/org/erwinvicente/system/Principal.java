package org.erwinvicente.system;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import org.erwinvicente.controller.AdministracionController;
import org.erwinvicente.controller.AutorController;
import org.erwinvicente.controller.CargosController;
import org.erwinvicente.controller.ClientesController;
import org.erwinvicente.controller.CuentasPorCobrarController;
import org.erwinvicente.controller.CuentasPorPagarController;
import org.erwinvicente.controller.DepartamentoController;
import org.erwinvicente.controller.HorariosController;
import org.erwinvicente.controller.LocalesController;
import org.erwinvicente.controller.MenuPrincipalController;
import org.erwinvicente.controller.ProveedoresController;
import org.erwinvicente.controller.TipoClienteController;
import org.erwinvicente.controller.EmpleadosController;
import org.erwinvicente.controller.LoginController;
import java.util.Base64;
import org.erwinvicente.bean.Usuario;

/**
 *
 * @author esteb
 */
public class Principal extends Application{
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VIEW = "/org/erwinvicente/view/";
    private final String PAQUETE_IMAGES = "org/erwinvicente/resource/images/";
    private final String PAQUETE_CSS = "org/erwinvicente/resource/css/";
      private Usuario usuario; 
    public static void main(String[] args) {
        launch(args);
    }

    public boolean validar(ArrayList<TextField> listaTextField, ArrayList<ComboBox> listaComboBox) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.escenarioPrincipal = stage;
        stage.getIcons().add(new Image ("/org/erwinvicente/resouce/images/iconoMall.jpg"));

        
        usuario = new Usuario();
        
        mostrarLogin();
    }

    public void mostrarMenuPrincipal () {
        try {
            MenuPrincipalController menuController = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 890, 500);
            menuController.setEscenarioPrincipal(this);
        }        catch (IOException e) {
            System.out.println("Se produjo un error al mostrar la vista del menu principal");
            e.printStackTrace();
            
        }   
    }
    public void mostrarAutor () {
        try {
            AutorController autorController = (AutorController) cambiarEscena("AutorView.fxml", 600, 400);
            autorController.setEscenarioPrincipal(this);
        } catch (IOException e){
            System.out.println("Se produjo un error al mostrar la vista autor");
            e.printStackTrace();
        }
        
    }
    
    public Initializable cambiarEscena(String fxml,int ancho, int alto) throws IOException{
        Initializable resultado = null;

        FXMLLoader cargadorFXML = new FXMLLoader();
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VIEW + fxml));
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VIEW + fxml);
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escena.getStylesheets().add(PAQUETE_CSS + "estiloKinalMall.css");

        this.escenarioPrincipal.setScene(escena);
        this.escenarioPrincipal.show();
        this.escenarioPrincipal.sizeToScene();
        this.escenarioPrincipal.setResizable(false);
        resultado = (Initializable) cargadorFXML.getController();
        return resultado; 
    }
    public void mostrarAdministracion(){
        try {
          AdministracionController adminController = (AdministracionController)cambiarEscena("AdministracionView.fxml", 890, 500);
          adminController.setEscenarioPrincipal(this);
        }catch (Exception e){
            System.out.println("Se produjo un erro al mostrar la vista administracion");
            e.printStackTrace();
            
        }
    
            }
    public void mostrarClientes (){
        try{
            ClientesController clientescontroller;
            clientescontroller = (ClientesController) cambiarEscena("ClientesView.fxml", 1280, 650);
            clientescontroller.setEscenarioPrincipal(this);
        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista clientes");
            e.printStackTrace();
        }
        
    }
    public void mostrarLocales (){
            try{
                LocalesController localesController;
                localesController = (LocalesController) cambiarEscena("LocalesView.fxml", 1280, 650);
                localesController.setEscenarioPrincipal(this);
            }catch(Exception e){
                System.err.println("\nSe produjo un error al mostrar la vista Locales");
                e.printStackTrace();
            }
        }
        
    public void mostrarDepartamentos (){
            try{
                DepartamentoController departamentoController;
                departamentoController = (DepartamentoController) cambiarEscena("DepartamentoView.fxml", 890, 500);
                departamentoController.setEscenaPrincipal(this);
            }catch(Exception e){
                System.err.println("\nSe produjo un error al mostrar la vista Departamentos");
                e.printStackTrace();
            }
        }
        
    public void mostrarTipoCliente (){
           try{
                TipoClienteController tipoClienteController;
                tipoClienteController = (TipoClienteController) cambiarEscena("TipoClienteView.fxml", 1280, 650);
                tipoClienteController.setEscenarioPrincipal(this);
            }catch(Exception e){
                System.err.println("\nSe produjo un error al mostrar la vista Tipo Cliente");
                e.printStackTrace();
            } 
        }
        
    public void mostrarCuentasPorCobrar (){
            try{
                CuentasPorCobrarController CobrosController;
                CobrosController = (CuentasPorCobrarController) cambiarEscena("CuentasPorCobrarView.fxml", 1280,650);
                CobrosController.setEscenarioPrincipal(this);
            }catch(Exception e) {
                System.out.println("\nSe produjo un error al cargar la vista de Cuentas por cobrar");
                e.printStackTrace();
            }
            
        }
        
    public void mostrarPoveedores (){
        try{
            ProveedoresController proveedorescontroller;
            proveedorescontroller = (ProveedoresController) cambiarEscena("ProveedoresView.fxml", 1100, 575);
            proveedorescontroller.setEscenarioPrincipal(this);

        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista proveedores");
            e.printStackTrace();
        }
        
    } 
        
    public void mostrarHorarios (){
        try{
            HorariosController horarioscontroller;
            horarioscontroller = (HorariosController) cambiarEscena("HorariosView.fxml", 1280, 650);
            horarioscontroller.setEscenarioPrincipal(this);

        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista horarios");
            e.printStackTrace();
        }
    }  
    
    public void mostrarCuentasPorPagar (){
        try{
            CuentasPorPagarController cuentasPorPagarController;
            cuentasPorPagarController = (CuentasPorPagarController) cambiarEscena("CuentasPorPagarView.fxml", 1280, 650);
            cuentasPorPagarController.setEscenarioPrincipal(this);

        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista cuentas por pagar");
            e.printStackTrace();
        }
        
    } 
    public void mostrarEmpleados (){
       try{
            EmpleadosController empleadosController;
            empleadosController = (EmpleadosController) cambiarEscena("EmpleadosView.fxml", 1280, 650);
            empleadosController.setEscenarioPrincipal(this);

        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista empleados");
            e.printStackTrace();
        }
    }     
    
    public void mostrarCargos(){
       try{
            CargosController cargosController;
            cargosController = (CargosController) cambiarEscena("CargosView.fxml", 1100, 575);
            cargosController.setEscenarioPrincipal(this);

        } catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista cargos");
            e.printStackTrace();
        }
    }     
    
    
    
    public void mostrarLogin(){
        try{
            LoginController loginController;
            loginController = (LoginController) cambiarEscena("LoginView.fxml", 890, 526);
            loginController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
            
}
