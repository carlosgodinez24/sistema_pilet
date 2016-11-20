/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import javax.ejb.Stateless;

import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import com.sv.udb.utils.Conexion;
import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JasperRunManager;
import org.primefaces.context.RequestContext;

import com.sv.udb.ejb.CambiocitaFacadeLocal;
import com.sv.udb.modelo.Cambiocita;
/**
 *
 * @author Larry Orellana
 */
@Named(value = "ReportesBeanBecas")
@ViewScoped
public class ReportesBeanBecas implements Serializable{
    private static final long serialVersionUID = 1L;
    @Inject
    private GlobalAppBean globalAppBean; //Bean de aplicación (Instancia)
    private byte[] docuRepo; 
    private String donacionescanceladas="";
    private String donacionesactivas="";
    private String Generalbecas="";
    private String patrocinador="";
    private String Complementosdebeca="";
    private String Filtrobeca="";
    
    public ReportesBeanBecas() {
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public String getDonacionescanceladas() {
        return donacionescanceladas;
    }

    public void setDonacionescanceladas(String donacionescanceladas) {
        this.donacionescanceladas = donacionescanceladas;
    }

    public String getDonacionesactivas() {
        return donacionesactivas;
    }

    public void setDonacionesactivas(String donacionesactivas) {
        this.donacionesactivas = donacionesactivas;
    }

    public String getGeneralbecas() {
        return Generalbecas;
    }

    public void setGeneralbecas(String Generalbecas) {
        this.Generalbecas = Generalbecas;
    }

    public String getPatrocinador() {
        return patrocinador;
    }

    public void setPatrocinador(String patrocinador) {
        this.patrocinador = patrocinador;
    }

    public String getComplementosdebeca() {
        return Complementosdebeca;
    }

    public void setComplementosdebeca(String Complementosdebeca) {
        this.Complementosdebeca = Complementosdebeca;
    }

    public String getFiltrobeca() {
        return Filtrobeca;
    }

    public void setFiltrobeca(String Filtrobeca) {
        this.Filtrobeca = Filtrobeca;
    }
    
    public byte[] getDocuRepo() {
        return docuRepo;
    }
    
    //Procesar reporte 1
    public void donacionescanceladas()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("donacionescanceladas", donacionescanceladas); //Para este ejemplo no es necesario
            System.out.println(donacionescanceladas);
            String pathRepo = globalAppBean.getResourcePath("reportes_becas/Empresas_y_donaciones_canceladas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");            
        }
    }
    
    //Procesar reporte 2 
    public void donacionesactivas()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("donacionesactivas", donacionesactivas); //Para este ejemplo no es necesario
            System.out.println(donacionesactivas);
            String pathRepo = globalAppBean.getResourcePath("reportes_becas/Empresas_y_donaciones_activas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");            
        }
    }
    
    //Procesar reporte 3
    public void Generalbecas()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("Generalbecas", Generalbecas); //Para este ejemplo no es necesario
            System.out.println(Generalbecas);
            String pathRepo = globalAppBean.getResourcePath("reportes_becas/General_becas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");            
        }
    }
    
    //Procesar reporte 4
    public void patrocinador()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("patrocinador", patrocinador); //Para este ejemplo no es necesario
            System.out.println(patrocinador);
            String pathRepo = globalAppBean.getResourcePath("reportes_becas/Patrocinador_becas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");            
        }
    }
    
    //Procesar reporte 5
    public void Complementosdebeca()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("Complementosdebeca", Complementosdebeca); //Para este ejemplo no es necesario
            System.out.println(Complementosdebeca);
            String pathRepo = globalAppBean.getResourcePath("reportes_becas/Complementos de beca.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");            
        }
    }
    
    //Procesar reporte 6
    public void Filtrobeca()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("Filtrobeca", Filtrobeca); //Para este ejemplo no es necesario
            System.out.println(Filtrobeca);
            String pathRepo = globalAppBean.getResourcePath("reportes_becas/Empresas_y_donaciones_canceladas.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");            
        }
    }
}
