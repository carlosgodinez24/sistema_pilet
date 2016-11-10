/*
 * Controlador Excepcionhorariodisponible
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.ExcepcionhorariodisponibleFacadeLocal;
import com.sv.udb.modelo.Excepcionhorariodisponible;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.apache.log4j.Logger;

/**
 *
 * @author Sistema de citas
 * @version 1.0 13 de Octubre de 2016
 */
@Named(value = "excepcionHorarioDisponiblesBean")
@ViewScoped
public class ExcepcionHorarioDisponibleBean implements Serializable{
   
    
    public ExcepcionHorarioDisponibleBean() {
        
    }
     
    @EJB
    private ExcepcionhorariodisponibleFacadeLocal FCDEExceHoraDisp;    
    private Excepcionhorariodisponible objeExceHoraDisp;
    private List<Excepcionhorariodisponible> listExceHoraDisp;
    private boolean guardar;
    
    public Excepcionhorariodisponible getObjeExceHoraDisp() {
        return objeExceHoraDisp;
    }

    public void setObjeExceHoraDisp(Excepcionhorariodisponible objeExceHoraDisp) {
        this.objeExceHoraDisp = objeExceHoraDisp;
    }

    public List<Excepcionhorariodisponible> getListExceHoraDisp() {
        return listExceHoraDisp;
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
        this.objeExceHoraDisp = new Excepcionhorariodisponible();
        this.guardar = true;        
    }
    
    public void consTodo()
    {
        try
        {
            this.listExceHoraDisp = FCDEExceHoraDisp.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeExceHoraDisp = FCDEExceHoraDisp.find(codi);
            this.guardar = false;
            
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " +this.objeExceHoraDisp.getFechExceHoraDisp()+ "')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
     /**
     * Obteniendo los dias de la semana
     */ 
    private int getDay(String dia){
        int ndia = 0;
        String dias[] = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};
        for(int i = 0; i < dias.length; i++){
            if(dia.equals(dias[i])){ndia = i+1; break;}
        }
        return ndia;
    }
         /**
     * Validando
     * Objeto del tipo excepcion horarios disponibles     
     * @since incluido desde la version 1.0
     */ 
    private boolean validar(){
        boolean val = false;
        RequestContext ctx = RequestContext.getCurrentInstance();
            int diaHoraDisp = getDay(this.objeExceHoraDisp.getCodiHoraDisp().getDiaHoraDisp());
            int diaExceHoraDisp = this.objeExceHoraDisp.getFechExceHoraDisp().getDay();
            if(diaHoraDisp == diaExceHoraDisp){
                val = true;
            }else{
                ctx.execute("setMessage('MESS_INFO', 'Atención', 'La excepción seleccionada "
                        + "no coincide con la fecha ingresada');");
            }
        return val;
    }
         /**
     * Método que guarda un objeto del tipo excepcion horario en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */ 
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {   
            if(validar()){
              FCDEExceHoraDisp.create(this.objeExceHoraDisp);
              this.listExceHoraDisp.add(this.objeExceHoraDisp);
              ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");  
            }
        }
        catch(Exception ex)
        {
            
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar')");
        }
    }
     /**
     * Método que modifica un objeto del tipo excepcion horario en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */ 
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(validar()){
              this.listExceHoraDisp.remove(this.objeExceHoraDisp); //Limpia el objeto viejo
              FCDEExceHoraDisp.edit(this.objeExceHoraDisp);
              this.listExceHoraDisp.add(this.objeExceHoraDisp); //Agrega el objeto modificado
              ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
              this.listExceHoraDisp.add(this.objeExceHoraDisp); 
            }
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
    }
    /**
     * Método que elmina un objeto del tipo excepcion horario en la base de datos
     * @exception Error al realizar la operacion         
     * @since incluido desde la version 1.0
     */   
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            FCDEExceHoraDisp.remove(this.objeExceHoraDisp);
            this.listExceHoraDisp.remove(this.objeExceHoraDisp);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
    }
}
