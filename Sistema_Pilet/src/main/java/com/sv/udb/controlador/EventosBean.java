/*
 * Controlador eventos
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.EventoFacadeLocal;
import com.sv.udb.modelo.Evento;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sistema de citas
 * @version 1.0 13 de Octubre de 2016
 */
@Named(value = "eventosBean")
@ViewScoped
public class EventosBean implements Serializable{
   
    
    public EventosBean() {
        
    }
     
    @EJB
    private EventoFacadeLocal FCDEEven;    
    private Evento objeEven;
    private List<Evento> listEven;
    private boolean guardar;
    
    public Evento getObjeEven() {
        return objeEven;
    }

    public void setObjeEven(Evento objeEven) {
        this.objeEven = objeEven;
    }

    public List<Evento> getListEven() {
        return listEven;
    }

    public boolean isGuardar() {
        return guardar;
    }
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
        this.consTodo();
    }
    //Limpiando el formulario
    public void limpForm()
    {
        this.objeEven = new Evento();
        this.guardar = true;        
    }
    
    public void consTodo()
    {
        try
        {
            this.listEven = FCDEEven.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void cons()
    {
        //Capturo el contexto de la página
        RequestContext ctx = RequestContext.getCurrentInstance(); 
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeEven = FCDEEven.find(codi); //Encontrando el codigo
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " +this.objeEven.getNombEven()+ "')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
     /**
     * Método que guarda un objeto del tipo evento en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */   
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {            
            if(validar())
            {
                FCDEEven.create(this.objeEven);
                this.listEven.add(this.objeEven);
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            }
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar')");
        }
    }
    
     /**
     * Método que modifica un objeto del tipo evento en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */       
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(validar())
            {
                this.listEven.remove(this.objeEven); //Limpia el objeto viejo
                FCDEEven.edit(this.objeEven);
                this.listEven.add(this.objeEven); //Agrega el objeto modificado
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            }
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
    }
    
     /**
     * Método boleano para validar ls fecha y hora
     * Para que no se pueda colocar una fecha u hora antes de la actual.
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */  
    private boolean validar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance();
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        try
        {
            if((this.objeEven.getFechFinaEven().after(this.objeEven.getFechInicEven()))||(this.objeEven.getFechFinaEven().equals(this.objeEven.getFechInicEven())&&formatter.parse(this.objeEven.getHoraFinaEven()).after(formatter.parse(this.objeEven.getHoraInicEven()))))
            {
                return true;
            }
            else
            {
                ctx.execute("setMessage('MESS_INFO', 'Atención', 'Fecha Final no debe ser antes de la Inicial');");
                return false;
            }
        }
        catch(Exception err)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Horas no válidas')");
            return false;
        }
    }
  
     /**
     * Método que elimina un objeto del tipo evento en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */    
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            FCDEEven.remove(this.objeEven);
            this.listEven.remove(this.objeEven);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
    }
}