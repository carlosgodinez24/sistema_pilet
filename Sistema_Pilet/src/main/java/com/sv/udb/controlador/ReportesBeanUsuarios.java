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
@Named(value = "ReportesBeanUsuarios")
@ViewScoped
public class ReportesBeanUsuarios implements Serializable{
    private static final long serialVersionUID = 1L;
    @Inject
    private GlobalAppBean globalAppBean; //Bean de aplicación (Instancia)
    private byte[] docuRepo; 
    private String aniousua="";

    public ReportesBeanUsuarios() {
    }
    
    public String getAniousua() {
        return aniousua;
    }
    
    public void setAnioVisi(String aniousua) {
        this.aniousua = aniousua;
    }
    
    public byte[] getDocuRepo() {
        return docuRepo;
    }
    /**
     * Creates a new instance of ReportesBean
     */
    
    //Procesar reporte
    public void totalusua()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            Connection cn = new Conexion().getCn(); //La conexión
            Map params = new HashMap(); //Mapa de parámetros
            
            params.put("anioCitas", aniousua); //Para este ejemplo no es necesario
            System.out.println(aniousua);
            String pathRepo = globalAppBean.getResourcePath("reportes_citas/Cantidad_de_Usuario_por_rol.jasper");
            this.docuRepo = JasperRunManager.runReportToPdf(pathRepo, params, cn);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Reporte cargado correctamente')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");            
        }
    }  
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
