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
    
@Named(value = "ReportesBeanTicket")
@ViewScoped
public class ReportesBeanTicket {
    private static final long serialVersionUID = 1L;
    @Inject
    private GlobalAppBean globalAppBean; //Bean de aplicación (Instancia)
    private byte[] docuRepo; 
    private String aniofinalizada="";
    private String aniorealizado="";
    private String anioevaluacion="";
    private String manenimientos="";
    private String solicitud="";
    
    public ReportesBeanTicket() {
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")


    public String getAniofinalizada() {
        return aniofinalizada;
    }

    public void setAniofinalizada(String aniofinalizada) {
        this.aniofinalizada = aniofinalizada;
    }

    public String getAniorealizado() {
        return aniorealizado;
    }

    public void setAniorealizado(String aniorealizado) {
        this.aniorealizado = aniorealizado;
    }

    public String getAnioevaluacion() {
        return anioevaluacion;
    }

    public void setAnioevaluacion(String anioevaluacion) {
        this.anioevaluacion = anioevaluacion;
    }

    public String getManenimientos() {
        return manenimientos;
    }

    public void setManenimientos(String manenimientos) {
        this.manenimientos = manenimientos;
    }

    public String getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(String solicitud) {
        this.solicitud = solicitud;
    }

     public byte[] getDocuRepo() {
        return docuRepo;
    }
    
     
     //Procesar reporte 1
    public void finalizadas()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("anioCitas", aniofinalizada); //Para este ejemplo no es necesario
            System.out.println(aniofinalizada);
            String pathRepo = globalAppBean.getResourcePath("reportes_Ticket/Solicitudes_finalizadas_con_su_evaluación.jasper");
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
    public void realizados()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("anioCitas", aniorealizado); //Para este ejemplo no es necesario
            System.out.println(aniorealizado);
            String pathRepo = globalAppBean.getResourcePath("reportes_Ticket/Solicitudes_asignadas_a_encargados.jasper");
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
    public void evaluacion()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("anioCitas", anioevaluacion); //Para este ejemplo no es necesario
            System.out.println(anioevaluacion);
            String pathRepo = globalAppBean.getResourcePath("rreportes_Ticket/Solicitudes_finalizadas_con_su_evaluación.jasper");
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
    public void mantenimientos()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("anioCitas", manenimientos); //Para este ejemplo no es necesario
            System.out.println(manenimientos);
            String pathRepo = globalAppBean.getResourcePath("reportes_Ticket/Mantenimientos_activos.jasper");
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
    public void solicitud()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("anioCitas", solicitud); //Para este ejemplo no es necesario
            System.out.println(solicitud);
            String pathRepo = globalAppBean.getResourcePath("reportes_Ticket/Ticket_al_realizar_una_solicitud.jasper");
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
