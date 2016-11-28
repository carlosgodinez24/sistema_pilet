/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.modelo.Seguimiento;
import com.sv.udb.ejb.SeguimientoFacadeLocal;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ferna
 */
@Named(value = "seguimientoBean")
@ViewScoped
public class SeguimientoBean implements Serializable{
    @EJB
    private SeguimientoFacadeLocal FCDESegu;
    private Seguimiento objeSegu;
    private List<Seguimiento> listSegu;
    private List<Seguimiento> listEmpr;
    private List<Seguimiento> listEmprU;
    private List<Seguimiento> listSoli;
    private boolean guardar;  
    private static Logger log = Logger.getLogger(SeguimientoBean.class);
    public Seguimiento getObjeSegu() {
        return objeSegu;
    }

    public void setObjeSegu(Seguimiento objeSegu) {
        this.objeSegu = objeSegu;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public List<Seguimiento> getListSegu() {
        return listSegu;
    }

    public List<Seguimiento> getListEmpr() {
        return listEmpr;
    }

    public List<Seguimiento> getListSoli() {
        return listSoli;
    }

    public List<Seguimiento> getListEmprU() {
        return listEmprU;
    }
    
    

    /**
     * Creates a new instance of SeguimientoBean
     */
    public SeguimientoBean() {
    }
    
    private BecasBean objeBeca;
    private EmpresaBean objeEmpr;
    
    @PostConstruct
    public void init()
    {
        
        this.objeSegu = new Seguimiento();
        this.guardar = true;
        this.objeBeca = new BecasBean();
        this.consTodo();
        if (FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("empresaBean") != null) {
            objeEmpr = (EmpresaBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("empresaBean");
        }
        this.consEmpr();
        this.consSoli();
        if (FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becasBean") != null) {
            objeBeca = (BecasBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becasBean");
        }
    }
    
    public void limpForm()
    {
        this.objeSegu.setFechFin(new Date());
        this.objeSegu.setFechFin(new Date());
        this.objeSegu = new Seguimiento();
        this.guardar = true;        
    }
    
    public void guar()
    {
        this.objeSegu.setCodiSoliBeca(this.objeBeca.getObjeSoli());
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeSegu.setFechSegu(new Date());
            this.objeSegu.setEstaSegu(1);
            this.FCDESegu.create(this.objeSegu);
            if(this.objeSegu.getCodiEmpr() != null)
            {
                this.listEmpr.add(objeSegu);
            }
            else if(this.objeSegu.getCodiSoliBeca() != null)
            {
                this.listSoli.add(objeSegu);
            }
            else if(this.objeSegu.getCodiEmpr() == null && this.objeSegu.getCodiSoliBeca() == null)
            {
                this.listSegu.add(this.objeSegu);
            }
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            log.info("Seguimiento Guardado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            ////this.listSegu.remove(this.objeSegu); //Limpia el objeto viejo
            //this.listSoli.remove(objeSegu);
            System.out.println("asdfdafads");
            this.listEmpr.remove(objeSegu);
            System.out.println("asdfdafads");
            FCDESegu.edit(this.objeSegu);
            if(this.objeSegu.getCodiEmpr() != null)
            {
                this.listEmpr.add(objeSegu);
            }
            else if(this.objeSegu.getCodiSoliBeca() != null)
            {
                
                this.listSoli.add(objeSegu);
            }
            else if(this.objeSegu.getCodiEmpr() == null && this.objeSegu.getCodiSoliBeca() == null)
            {
                this.listSegu.add(this.objeSegu);
            }
            this.consEmpr();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            log.info("Seguimiento Modificado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void modiE()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeSegu.setCodiEmpr(objeEmpr.getObjeEmpr());
            this.listEmpr.remove(objeSegu);
            FCDESegu.edit(this.objeSegu);
            this.listEmpr.add(objeSegu);
            this.consEmpr();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            log.info("Seguimiento Modificado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void elim()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeSegu.setEstaSegu(0);
            FCDESegu.edit(this.objeSegu);
            if(this.objeSegu.getCodiEmpr() != null)
            {
                this.listEmpr.remove(objeSegu);
            }
            else if(this.objeSegu.getCodiSoliBeca() != null)
            {
                this.listSoli.remove(objeSegu);
            }
            else if(this.objeSegu.getCodiEmpr() == null && this.objeSegu.getCodiSoliBeca() == null)
            {
                this.listSegu.remove(this.objeSegu);
            }
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Eliminados')");
            log.info("Seguimiento Eliminado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al eliminar')");
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void consTodo()
    {
        try
        {
            this.listSegu = FCDESegu.findByAll();
            log.info("Seguimientos Consultados");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void consEmpr()
    {
        try
        {
            System.out.println("1");
            this.listEmpr = FCDESegu.findByEmpr();
            System.out.println(objeEmpr.getObjeEmpr().getCodiEmpr());
            this.listEmprU = FCDESegu.findByEmprU(objeEmpr.getObjeEmpr().getCodiEmpr());
            System.out.println("2");
            //log.info("Seguimientos Consultados");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void consSoli()
    {
        try
        {
            this.listSoli = FCDESegu.findBySoli();
            log.info("Seguimientos Consultados");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeSegu = FCDESegu.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
            String.format("%s", this.objeSegu.getDescSegu()) + "')");
            log.info("Seguimiento Consultado");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
            log.error(getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
}