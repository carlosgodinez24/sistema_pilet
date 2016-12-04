/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.modelo.Empresa;
import com.sv.udb.utils.Conexion;
import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JasperRunManager;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eduardo
 */
@Named(value = "reportesBean")
@ViewScoped
public class ReportesBean implements Serializable{

    @Inject
    private GlobalAppBean globalAppBean; //Bean de aplicación (Instancia)
    private byte[] docuRepo;

    public byte[] getDocuRepo() {
        return docuRepo;
    }
    
    private Empresa objeEmpr;

    public Empresa getObjeEmpr() {
        return objeEmpr;
    }

    public void setObjeEmpr(Empresa objeEmpr) {
        this.objeEmpr = objeEmpr;
    }
    
    
    /**
     * Creates a new instance of ReportesBean
     */
    public ReportesBean() {
    }
    
    public void procVisi() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            String pathRepo = globalAppBean.getResourcePath("Reportes_Becas/EmpresasDonacionesActivas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar reporte ')");
        }
    }
    
    public void EmprInac() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            String pathRepo = globalAppBean.getResourcePath("Reportes_Becas/EmpresasDonacionesCanceladas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar reporte ')");
        }
    }
    
    public void EmprBeca() {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            params.put("Empresa", objeEmpr.getCodiEmpr());
            String pathRepo = globalAppBean.getResourcePath("Reportes_Becas/EmpresasBecas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al cargar reporte ')");
        }
    }
    
}
