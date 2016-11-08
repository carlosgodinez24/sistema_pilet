/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.NotificacionFacadeLocal;
import com.sv.udb.modelo.Notificacion;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author aleso
 */
@Named(value = "notificacionBean")
@ViewScoped
public class NotificacionBean implements Serializable {

    @EJB
    private NotificacionFacadeLocal FCDENotificacion;
    private Notificacion objeNoti;
    private boolean guardar;

    public Notificacion getObjeNoti() {
        return objeNoti;
    }

    public void setObjeNoti(Notificacion objeNoti) {
        this.objeNoti = objeNoti;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }

    
    /**
     * Creates a new instance of NotificacionBean
     */
    public NotificacionBean() {
    }
    
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); 
        try
        {
            System.out.println(" asd"+this.objeNoti);
            this.objeNoti.setEstaNoti(1);
            FCDENotificacion.edit(this.objeNoti);
            //log.info("Notificacion modificada: "+this.objeNoti.getCodiNoti());
        }
        catch(Exception ex)
        {
            //log.error("Error modificando pagina: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
}
