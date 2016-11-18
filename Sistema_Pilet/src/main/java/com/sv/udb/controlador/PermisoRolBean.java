/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.PermisoRolFacadeLocal;
import com.sv.udb.modelo.PermisoRol;
import com.sv.udb.utils.LOG4J;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Carlos
 */
@Named(value = "permisoRolBean")
@ViewScoped
public class PermisoRolBean implements Serializable{
    //Bean de session
    @Inject
    private LoginBean logiBean; 
    @Inject
    private GlobalAppBean globalAppBean;
    //Campos de la clase
    @EJB
    private PermisoRolFacadeLocal FCDEPermRole;
    
    private PermisoRol objePermRole;
    private PermisoRol objeOldPermRole;
    private PermisoRol objeVali;
    private List<PermisoRol> listPermRole;
    private List<String> valores;
    private boolean guardar;
    
    private LOG4J<PermisoRolBean> lgs = new LOG4J<PermisoRolBean>(PermisoRolBean.class) {
    };
    
    private Logger log = lgs.getLog();

    public PermisoRol getObjePermRole() {
        return objePermRole;
    }

    public void setObjePermRole(PermisoRol objePermRole) {
        this.objePermRole = objePermRole;
    }

    public PermisoRol getObjeOldPermRole() {
        return objeOldPermRole;
    }

    public void setObjeOldPermRole(PermisoRol objeOldPermRole) {
        this.objeOldPermRole = objeOldPermRole;
    }

    public PermisoRol getObjeVali() {
        return objeVali;
    }

    public void setObjeVali(PermisoRol objeVali) {
        this.objeVali = objeVali;
    }

    public List<PermisoRol> getListPermRole() {
        return listPermRole;
    }

    public void setListPermRole(List<PermisoRol> listPermRole) {
        this.listPermRole = listPermRole;
    }

    public List<String> getValores() {
        return valores;
    }

    public void setValores(List<String> valores) {
        this.valores = valores;
    }
    
    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }
    
    
    /**
     * Creates a new instance of PermisoRolBean
     */
    public PermisoRolBean() {
    }
    
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
        this.consTodo();
        //log = new LOG4J();
        //log.debug("Se inicializa el modelo de UsuarioRol");
    }
    
    /**
     * Método que limpia el formulario reiniciando las variables
     */
    public void limpForm()
    {
        this.objePermRole= new PermisoRol();
        this.guardar = true;        
    }
    
    /**
    * Método que valida si ya existe un registro ingresado
    * @return el valor de true = si no hay registros duplicados y false = si hay registros duplicados para posteriormente mostrar un error
    */
    public boolean valiPermRole()
    {
        boolean resp = true;
        this.objeVali = FCDEPermRole.findByPermAndRole(this.objePermRole.getCodiPerm(), this.objePermRole.getCodiRole());
        if(this.objeVali != null)
            resp = (this.objeVali.getCodiPermRole() == this.objePermRole.getCodiPermRole()) ? true : false;
        return resp;
    }
    
   
    
   
    /**
     * Método que guarda la información en la base de datos
     */
//    public void guar()
//    {
//        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
//        try
//        {
//            if(valiPermRole()){
//                FCDEPermRole.create(this.objePermRole);
//                this.listUsuaRole.add(this.objeUsuaRole);
//                this.guardar = false;
//                globalAppBean.addNotificacion(this.objeOldUsuaRole.getCodiUsua().getCodiUsua(), "Se le ha asignado el rol de " + this.objeUsuaRole.getCodiRole().getNombRole(), "Modulo usuarios", "");
//                log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"UsuarioRol"+"-"+"Usuariorol creado: "+this.objeUsuaRole.getCodiUsua()+"/"+this.objeUsuaRole.getCodiRole());
//                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
//            }
//            else
//               ctx.execute("setMessage('MESS_WARN', 'Atención', 'Datos ya registrados')"); 
//        }
//        catch(Exception ex)
//        {
//            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
//            ex.printStackTrace();
//            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"UsuarioRol"+"-"+"Error creando Usuariorol: "+getRootCause(ex).getMessage());
//        }
//        finally
//        {
//            
//        }
//    }
    
    /**
     * Método que modifica la información en la base de datos
     */
//    public void modi()
//    {
//        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
//        try
//        {
//            if(valiUsuaRole()){
//                String oldRole = this.objeOldUsuaRole.getCodiRole().getNombRole();
//                this.listUsuaRole.remove(this.objeUsuaRole); //Limpia el objeto viejo
//                FCDEUsuaRole.edit(this.objeUsuaRole);
//                this.listUsuaRole.add(this.objeUsuaRole); //Agrega el objeto modificado
//                globalAppBean.addNotificacion(this.objeUsuaRole.getCodiUsua().getCodiUsua(), "Se ha modificado su rol de " + oldRole + " a " + this.objeUsuaRole.getCodiRole().getNombRole() , "Modulo usuarios", "");
//                log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"UsuarioRol"+"-"+"Usuariorol modificado: "+this.objeUsuaRole.getCodiUsuaRole());
//                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
//            }
//            else
//               ctx.execute("setMessage('MESS_WARN', 'Atención', 'Datos ya registrados')"); 
//        }
//        catch(Exception ex)
//        {
//            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
//            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"UsuarioRol"+"-"+"Error modificando Usuariorol: "+getRootCause(ex).getMessage());
//        }
//        finally
//        {
//            
//        }
//    }
    
    /**
     * Método que consulta la información de la base de datos
     */
    public void consTodo()
    {
        try
        {
            this.listPermRole = FCDEPermRole.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            
        }
    }
    
    /**
     * Método que consutla la información de un registro en específico de la base de datos
     */
//    public void cons()
//    {
//        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
//        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
//        try
//        {
//            this.objeUsuaRole = FCDEUsuaRole.find(codi);
//            this.objeOldUsuaRole = FCDEUsuaRole.find(codi);
//            this.guardar = false;
//            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
//                    String.format("%s", this.objeUsuaRole.getCodiUsua().getAcceUsua()) + "')");
//        }
//        catch(Exception ex)
//        {
//            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
//            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"UsuarioRol"+"-"+"Error consultando Usuariorol: "+getRootCause(ex).getMessage());
//        }
//        finally
//        {
//            
//        }
//    }
}
