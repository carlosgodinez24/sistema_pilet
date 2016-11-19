/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.modelo.DetalleBeca;
import com.sv.udb.ejb.DetalleBecaFacadeLocal;
import com.sv.udb.ejb.TipoBecaFacadeLocal;
import com.sv.udb.modelo.Beca;
import com.sv.udb.modelo.SolicitudBeca;
import com.sv.udb.modelo.TipoBeca;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eduardo
 */
@Named(value = "detalleBecaBean")
@ViewScoped
public class DetalleBecaBean implements Serializable{

    @EJB
    private TipoBecaFacadeLocal FCDETipoBeca;
    @EJB
    private DetalleBecaFacadeLocal FCDEDetaBeca;
    private DetalleBeca objeDetaBeca;
    private List<DetalleBeca> listDetaBeca;
    private boolean guardar;      
    private static Logger log = Logger.getLogger(DetalleBean.class);

    public DetalleBeca getObjeDetaBeca() {
        return objeDetaBeca;
    }

    public void setObjeDetaBeca(DetalleBeca objeDetaBecaBeca) {
        this.objeDetaBeca = objeDetaBecaBeca;
    }

    public List<DetalleBeca> getListDetaBeca() {
        return listDetaBeca;
    }

    public void setListDetaBeca(List<DetalleBeca> listDetaBeca) {
        this.listDetaBeca = listDetaBeca;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }
    
    
    //Manejo de combobox
    private Beca objeCombPadr;
    //private SolicitudBeca objeCombPadr;
    private List<TipoBeca> listTipoBeca;

    public Beca getObjeCombPadr() {
        return objeCombPadr;
    }

    public void setObjeCombPadr(Beca objeCombPadr) {
        this.objeCombPadr = objeCombPadr;
    }
    public List<TipoBeca> getListTipoBeca() {
        return listTipoBeca;
    }

    public void setListTipoBeca(List<TipoBeca> listTipoBeca) {
        this.listTipoBeca = listTipoBeca;
    }
    
    public void onAlumBecaSelect(){
        
        listTipoBeca = FCDETipoBeca.findTipos(objeCombPadr.getCodiSoliBeca().getCodiGrad().getNivelGrad());
    }
    
    DetalleBeca vali;
    
    /**
     * Creates a new instance of DetalleBecaBean
     */
    public DetalleBecaBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.objeDetaBeca = new DetalleBeca();
        this.vali = new DetalleBeca();
        this.guardar = true;
        this.consTodo();
        
        if( FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becaSoliBean") != null)
        {
            BecaSoliBean asd = (BecaSoliBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becaSoliBean");
           Beca a = new Beca();
         
            this.objeDetaBeca.setCodiBeca(asd.getObjeBeca());
            System.out.println(this.objeDetaBeca.getCodiBeca().getCodiBeca());
        }
         
    }
    
    public void limpForm()
    {
        this.objeDetaBeca = new DetalleBeca();
        this.guardar = true;        
    }
    
    public void guar()
    {
        boolean guardarDeta;
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
//            if (this.objeDetaBeca.getCantMese() > 0) {
//                //Proceso normal
//                switch (this.objeDetaBeca.getCodiTipoBeca().getTipoTipoBeca()) {
//                    case 1:
//                        //Matricula
//                        if (this.objeDetaBeca.getCantMese() > 1) {
//                            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'No se puede entregar matrícula más de una vez')");
//                            guardarDeta = false;
//                        } else {
//                            guardarDeta = true;
//                        }
//                        break;
//                    case 2:
//                        //Mensualidad
//                        if (this.objeDetaBeca.getCantMese() > 11) {
//                            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'No se puede entregar más de 11 mensualidades')");
//                            guardarDeta = false;
//                        } else {
//                            guardarDeta = true;
//                        }
//                        break;
//                    case 3:
//                        guardarDeta = true;
//                        break;
//                    default:
//                        guardarDeta = false;
//                        break;
//                }
//                if (guardarDeta) {
//                    
//                      this.objeDetaBeca.setCodiBeca(objeCombPadr);
//                      this.objeDetaBeca.setEstaDetaBeca(1);
//                      this.FCDEDetaBeca.create(objeDetaBeca);
//                      this.listDetaBeca.add(this.objeDetaBeca);
//                      this.limpForm();
////                    System.out.println("AQUIIIIIIIIII "+this.objeCombPadr.getCodiBeca());
////                    this.objeDetaBeca.setEstaDetaBeca(1);
////                    FCDEDetaBeca.create(this.objeDetaBeca);
////                    this.listDetaBeca.add(this.objeDetaBeca);
////                    this.limpForm();
//                    ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
//                    log.info("Detalle Guardado");
//                }
//            } else {
//                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'La cantidad de meses no puede ser 0')");
//            }
            if (this.objeDetaBeca.getCantMese() > 0) 
            {
                switch (this.objeDetaBeca.getCodiTipoBeca().getTipoTipoBeca()) {
                    case 1:
                        //Matricula
                        if (this.objeDetaBeca.getCantMese() > 1) {
                            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'No se puede entregar matrícula más de una vez')");
                            guardarDeta = false;
                        } else {
                            guardarDeta = true;
                        }
                        break;
                    case 2:
                        //Mensualidad
                        if (this.objeDetaBeca.getCantMese() > 11) {
                            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'No se puede entregar más de 11 mensualidades')");
                            guardarDeta = false;
                        } else {
                            guardarDeta = true;
                        }
                        break;
                    case 3:
                        guardarDeta = true;
                        break;
                    default:
                        guardarDeta = false;
                        break;
                }
                System.out.println(guardarDeta);
                System.out.println("AQUI VE"+this.objeCombPadr.getCodiBeca());
                this.objeDetaBeca.setCodiBeca(objeCombPadr);
                System.out.println("AQUI VE"+this.objeDetaBeca.getCodiTipoBeca().getCodiTipoBeca());
                if (guardarDeta) {
                    System.out.println("Resultadisimo:"+this.FCDEDetaBeca.validar(this.objeCombPadr.getCodiBeca(), this.objeDetaBeca.getCodiTipoBeca().getCodiTipoBeca()));
                    int resuVali=this.FCDEDetaBeca.validar(this.objeCombPadr.getCodiBeca(), this.objeDetaBeca.getCodiTipoBeca().getCodiTipoBeca());
                    System.out.println("Estoy apunto de entrar en una validación");
                    if(resuVali>= 1)
                    {
                        System.out.println("Entro en la validacion");
                        ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El alumno ya tiene asignado este tipo de beca.')");
                    }
                    else
                    {
                        System.out.println("Pase");
                        this.objeDetaBeca.setCodiBeca(objeCombPadr);
                        this.objeDetaBeca.setEstaDetaBeca(1);
                        this.FCDEDetaBeca.create(objeDetaBeca);
                        this.listDetaBeca.add(this.objeDetaBeca);
                        this.limpForm();
                        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
                        log.info("Detalle Guardado");
                    }
                }
//                this.objeDetaBeca.setCodiBeca(objeCombPadr);
//                this.objeDetaBeca.setEstaDetaBeca(1);
//                this.FCDEDetaBeca.create(objeDetaBeca);
//                this.listDetaBeca.add(this.objeDetaBeca);
//                this.limpForm(); 
            }
            else 
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'La cantidad de meses no puede ser 0')");
            }
                    
            
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            System.out.println(getRootCause(ex).getMessage());
            log.error("Error en "+ getRootCause(ex).getMessage());
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
            this.listDetaBeca.remove(this.objeDetaBeca); //Limpia el objeto viejo
            FCDEDetaBeca.edit(this.objeDetaBeca);
            this.listDetaBeca.add(this.objeDetaBeca); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            log.info("Detalle Modificado");
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
            this.objeDetaBeca.setEstaDetaBeca(0);
            this.listDetaBeca.remove(this.objeDetaBeca); //Limpia el objeto viejo
            FCDEDetaBeca.edit(this.objeDetaBeca);
            this.listDetaBeca.add(this.objeDetaBeca); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            log.info("Detalle Modificado");
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
    
    public void consTodo()
    {
        try
        {
            this.listDetaBeca = FCDEDetaBeca.findAll();
            log.info("Detalles Consultados");
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
            this.objeDetaBeca = FCDEDetaBeca.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeDetaBeca.getCantMese()) + "')");
            log.info("Detalle Consultado");
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
