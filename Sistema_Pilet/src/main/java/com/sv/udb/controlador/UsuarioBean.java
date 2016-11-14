/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.UsuarioFacadeLocal;
import com.sv.udb.modelo.Usuario;
import com.sv.udb.utils.LOG4J;
import com.sv.udb.utils.pojos.DatosUsuarios;
import java.io.Serializable;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Carlos
 */
@Named(value = "usuarioBean")
@ViewScoped
@ManagedBean
public class UsuarioBean implements Serializable{
    //Campos de la clase
    @EJB
    private UsuarioFacadeLocal FCDEUsuario;
    
    //Bean de session
    @Inject
    private LoginBean logiBean; 
   
    private List<Usuario> listUsua;
    private Usuario objeUsua;
    private boolean guardar;
    private LOG4J<UsuarioBean> lgs = new LOG4J<UsuarioBean>(UsuarioBean.class) {
    };
    private Logger log = lgs.getLog();
    /**
     * Creates a new instance of UsuarioBean
     */
    
    /**
     * Creates a new instance of UsuarioBean
     * @return
     */
    public List<Usuario> getListUsua() {
        return listUsua;
    }

    public void setListUsua(List<Usuario> listUsua) {
        this.listUsua = listUsua;
    }

    public Usuario getObjeUsua() {
        return objeUsua;
    }

    public void setObjeUsua(Usuario objeUsua) {
        this.objeUsua = objeUsua;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }
    
    public UsuarioBean() {
    }
    
    /**
    * Método que se ejecuta después de haber construido la clase e inicializa las variables
    */
    @PostConstruct
    public void init()
    {
        this.listUsua = FCDEUsuario.findAll();
        //log = new LOG4J();
        //log.debug("Se inicializa el modelo de Usuarios");
    }
    
    /**
     * Método que consulta la información de la base de datos
     */
    public void consTodo()
    {
        try
        {
            this.listUsua = FCDEUsuario.findAll();
        }
        catch(Exception ex)
        {
            ex.getMessage();
        }
        finally
        {
            
        }
    }
   
    public void guar(DatosUsuarios obje)
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {            
            this.objeUsua.setAcceUsua(obje.getUsua());
            this.objeUsua.setEstaUsua(1);
            FCDEUsuario.create(this.objeUsua);            
            this.guardar = false;
            log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"Usuario"+"-"+"Usuario habilitado: "+this.objeUsua.getAcceUsua());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"Usuarios"+"-"+"Error habilitando Usuario: "+getRootCause(ex).getMessage());
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
            this.objeUsua.setEstaUsua(0);
            FCDEUsuario.edit(this.objeUsua);
            this.guardar = false;
            log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"Usuario"+"-"+"Usuario inhabilitado: "+this.objeUsua.getAcceUsua());
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"Usuarios"+"-"+"Error inhabilitando Usuario: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
}
