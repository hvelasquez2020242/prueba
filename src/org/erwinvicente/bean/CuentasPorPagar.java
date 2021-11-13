/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.erwinvicente.bean;
import java.math.BigDecimal;
import java.sql.Date;
/**
 *
 * @author esteb
 */
public class CuentasPorPagar {
    private int id;
    private String numeroFactura;
    private Date fechaLimitePago;
    private String estadoPago;
    private BigDecimal valorNetoPago;
    private int idAdministracion;
    private int idProveedor;
    
    public CuentasPorPagar(int id, String numeroFactura, Date fechaLimitePago, String estadoPago, BigDecimal valorNetoPago, int idAdministracion, int idProveedor){
        this.id = id;
        this.numeroFactura = numeroFactura;
        this.fechaLimitePago = fechaLimitePago;
        this.estadoPago = estadoPago;
        this.valorNetoPago = valorNetoPago;
        this.idAdministracion = idAdministracion;
        this.idProveedor = idProveedor;
    }

    public CuentasPorPagar(int aInt, String string, String string0) {

    }

    public CuentasPorPagar() {
    }

   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFechaLimitePago() {
        return fechaLimitePago;
    }

    public void setFechaLimitePago(Date fechaLimitePago) {
        this.fechaLimitePago = fechaLimitePago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public BigDecimal getValorNetoPago() {
        return valorNetoPago;
    }

    public void setValorNetoPago(BigDecimal valorNetoPago) {
        this.valorNetoPago = valorNetoPago;
    }

    public int getIdAdministracion() {
        return idAdministracion;
    }

    public void setIdAdministracion(int idAdministracion) {
        this.idAdministracion = idAdministracion;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }
    
}

