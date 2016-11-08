/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.VisitantecitaFacadeLocal;
import com.sv.udb.modelo.Visitante;
import com.sv.udb.modelo.Visitantecita;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author REGISTRO
 */
@Named(value = "VisitantecitaCitaBean")
@ViewScoped
public class VisitanteCitaBean implements Serializable{
   
    
    public VisitanteCitaBean() {
        
    }
     
    @EJB
    private VisitantecitaFacadeLocal FCDEVisiCita;    
    private Visitantecita objeVisiCita;
    private List<Visitantecita> listVisiCita;
    private boolean guardar;
    private Visitante objeVisi;
    

    public List<Visitantecita> getListVisiCita() {
        return listVisiCita;
    }

    public void setListVisiCita(List<Visitantecita> listVisiCita) {
        this.listVisiCita = listVisiCita;
    }
    
    public Visitantecita getObjeVisiCita() {
        return objeVisiCita;
    }

    public void setObjeVisiCita(Visitantecita objeVisiCita) {
        this.objeVisiCita = objeVisiCita;
    }

    public Visitante getObjeVisi() {
        return objeVisi;
    }

    public void setObjeVisi(Visitante objeVisi) {
        this.objeVisi = objeVisi;
    }
    
    
    
    public Visitantecita getobjeVisiCita() {
        return objeVisiCita;
    }

    public void setobjeVisiCita(Visitantecita objeVisiCita) {
        this.objeVisiCita = objeVisiCita;
    }

    public List<Visitantecita> getlistVisiCita() {
        return listVisiCita;
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
    
    public void limpForm()
    {
        this.objeVisiCita = new Visitantecita();
        this.guardar = true;        
    }
    
    
    
    
    public void consTodo()
    {
        try
        {
            this.listVisiCita = FCDEVisiCita.findAll();
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
            this.objeVisiCita = FCDEVisiCita.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s %s %s %s", this.objeVisiCita.getCodiVisi().getNombVisi(),
                            this.objeVisiCita.getCodiVisi().getApelVisi(),"en", this.objeVisiCita.getCodiCita().getCodiUbic()) + "')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
        finally
        {
            
        }
    }
    
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            FCDEVisiCita.create(this.objeVisiCita);
            this.listVisiCita.add(this.objeVisiCita);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar')");
        }
    }
    
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listVisiCita.remove(this.objeVisiCita); //Limpia el objeto viejo
            FCDEVisiCita.edit(this.objeVisiCita);
            this.listVisiCita.add(this.objeVisiCita); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
        }
    }
    
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            objeVisiCita.setEstaVisi(-1);
            FCDEVisiCita.edit(this.objeVisiCita);
            this.listVisiCita.remove(this.objeVisiCita);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
            this.limpForm();
        }
        catch(Exception ex) 
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
        }
    }   
}
