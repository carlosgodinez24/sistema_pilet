/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.BecaFacadeLocal;
import com.sv.udb.ejb.DetalleBecaFacadeLocal;
import com.sv.udb.ejb.DocumentoFacadeLocal;
import com.sv.udb.ejb.GradoFacadeLocal;
import com.sv.udb.ejb.SolicitudBecaFacadeLocal;
import com.sv.udb.ejb.TipoBecaFacadeLocal;
import com.sv.udb.modelo.Beca;
import com.sv.udb.modelo.DetalleBeca;
import com.sv.udb.modelo.Documento;
import com.sv.udb.modelo.Grado;
import com.sv.udb.modelo.SolicitudBeca;
import com.sv.udb.modelo.TipoBeca;
import com.sv.udb.modelo.TipoEstado;
import com.sv.udb.modelo.TipoRetiro;
import com.sv.udb.utils.AlumnosPojo;
import com.sv.udb.utils.Archivo;
import com.sv.udb.utils.pojos.DatosAlumnos;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Ariel
 */
@Named(value = "becasBean")
@ViewScoped
public class BecasBean implements Serializable{

    
    
    @EJB
    private GradoFacadeLocal FCDEGrado;

     @EJB
    private TipoBecaFacadeLocal FCDETipoBeca;

    @EJB
    private DetalleBecaFacadeLocal FCDEDetaBeca;
    private List<TipoBeca> listTipoBeca;

    public List<TipoBeca> getListTipoBeca() {
        return listTipoBeca;
    }

    public void setListTipoBeca(List<TipoBeca> listTipoBeca) {
        this.listTipoBeca = listTipoBeca;
    }
    
    
    @EJB
    private SolicitudBecaFacadeLocal FCDESoli;
    private SolicitudBeca objeSoli;
    private SolicitudBeca objeSoli2;
    private List<SolicitudBeca> listSoli;
    private List<SolicitudBeca> listSoliH;
    private List<SolicitudBeca> listSoliActivos;
    private String carnet; //Filotro de búsqueda

    
    
    @EJB
    private BecaFacadeLocal FCDEBeca;
    private Beca objeBeca;
    private Beca objeBeca2;
    private List<Beca> listBeca;
    private List<Beca> listBecaH;
    private List<Beca> listBecaActivos;
    private List<Beca> listBecaDocu;

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }
    
    private boolean guardar; 
    private static Logger log = Logger.getLogger(BecaSoliBean.class);

    public SolicitudBeca getObjeSoli() {
        return objeSoli;
    }

    public void setObjeSoli(SolicitudBeca objeSoli) {
        this.objeSoli = objeSoli;
    }

    public List<SolicitudBeca> getListSoliH() {
        return listSoliH;
    }

    public void setListSoliH(List<SolicitudBeca> listSoliH) {
        this.listSoliH = listSoliH;
    }

    public List<Beca> getListBecaH() {
        return listBecaH;
    }

    public void setListBecaH(List<Beca> listBecaH) {
        this.listBecaH = listBecaH;
    }

    public List<Beca> getListBecaActivos() {
        return listBecaActivos;
    }
    
    

    public List<SolicitudBeca> getListSoli() {
        return listSoli;
    }

    public void setListSoli(List<SolicitudBeca> listSoli) {
        this.listSoli = listSoli;
    }

    public List<Beca> getListBecaDocu() {
        return listBecaDocu;
    }
    
    
    
    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    public Beca getObjeBeca() {
        return objeBeca;
    }

    public void setObjeBeca(Beca objeBeca) {
        this.objeBeca = objeBeca;
    }

    public List<Beca> getListBeca() {
        return listBeca;
    }

    public void setListBeca(List<Beca> listBeca) {
        this.listBeca = listBeca;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public void setGuardar(boolean guardar) {
        this.guardar = guardar;
    }

    public String getFilt() {
        return carnet;
    }

    public void setFilt(String carnet) {
        this.carnet = carnet;
    }

    public List<SolicitudBeca> getListSoliActivos() {
        return listSoliActivos;
    }
    
    
    /**
     * Creates a new instance of BecaSoliBean
     */
    public BecasBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.objeSoli = new SolicitudBeca();
        this.guardar = true;
        this.objeBeca = new Beca();
        this.consTodo();
        this.consTodoH();
        if (this.objeSoli.getCodiSoliBeca() != null) {
            DetalleBecaBean asd = (DetalleBecaBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("detalleBecaBean");
            /*asd.setObjeCombPadr(objeSoli);
            asd.onAlumBecaSelect();*/
        }
        
    }
    
    public void limpForm()
    {
        this.objeBeca = new Beca();  
        this.objeSoli = new SolicitudBeca();
        this.objeSoli.setFechSoliBeca(new Date());
        this.objeBeca.setFechBaja(new Date());
        this.guardar = true; 
        this.carnet = "";
    }
    public void guar()
    {        
        this.objeSoli.setFechSoliBeca(new Date());
        this.objeBeca.setFechInic(new Date());
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(objeSoli.getCarnAlum() == null || objeSoli.getNombAlum() == null)
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Busque un alumno')");
            }
            else
            {
                    TipoEstado a = new TipoEstado();
                    a.setCodiTipoEsta(4);
                    this.objeSoli.setEstaSoliBeca(4);
                    FCDESoli.create(objeSoli);
                    this.objeSoli2 = FCDESoli.findLast();
                    this.objeBeca.setCodiSoliBeca(objeSoli2);
                    this.objeBeca.setCodiTipoEsta(a);                   
                    this.FCDEBeca.create(objeBeca);
                    this.listSoli.add(this.objeSoli);
                    if(this.listBeca == null)
                    {
                        this.listBeca = new ArrayList<>();
                    }
                    this.listBeca.add(objeBeca);
                    ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
                    log.info("Beca Guardada");
                    this.guardar = false;
                    objeSoli =FCDESoli.findLast();
                    objeBeca=FCDEBeca.findLast();
            
            }
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
            //this.listSoli.remove(this.objeSoli); //Limpia el objeto viejo
            System.out.println(this.objeSoli);
            System.out.println(this.objeSoli.getNombAlum());
            objeSoli2 = FCDESoli.find(this.objeSoli.getCodiSoliBeca());
            System.out.println(this.objeSoli2.getNombAlum());
            this.objeBeca = FCDEBeca.findSoli(this.objeSoli.getCodiSoliBeca());
            this.objeBeca2 = FCDEBeca.findSoli(this.objeSoli2.getCodiSoliBeca());
            this.listSoli.remove(this.objeSoli);
            this.objeSoli2.setEstaSoliBeca(3);
            //objeSoli.setBecaList(null);
            FCDESoli.edit(objeSoli2);
            this.listSoli.add(objeSoli2);
            this.objeSoli.setEstaSoliBeca(1);
            FCDESoli.create(objeSoli);
            this.listSoli.add(objeSoli);
            this.listBeca.remove(this.objeBeca2);
            TipoEstado es = new TipoEstado();
            es.setCodiTipoEsta(3);
            this.objeBeca2.setCodiTipoEsta(es);
            this.objeBeca2.setFechBaja(new Date());
            FCDEBeca.edit(objeBeca2);
            //this.listBeca.add(objeBeca2);
            this.objeSoli = FCDESoli.findLast();
            System.out.println(this.objeSoli.getCodiSoliBeca());
            this.objeBeca.setCodiSoliBeca(objeSoli);
            es.setCodiTipoEsta(this.objeSoli.getEstaSoliBeca());
            this.objeBeca.setCodiTipoEsta(es);
            FCDEBeca.create(objeBeca);
            this.listBeca.add(objeBeca);
            //FCDESoli.edit(this.objeSoli);
            //this.listSoli.add(this.objeSoli); //Agrega el objeto modificado
            this.consTodoH();
            this.consTodo();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            log.info("Solicitud Modificada");
            
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
    
    public void reActi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
             /*Busca el objeto viejo le setea el estado 3 de historial y lo modifica*/
            //this.listBeca.remove(this.objeBeca);
            this.objeSoli2 = this.objeSoli;
            this.objeSoli.setEstaSoliBeca(3);
            FCDESoli.edit(objeSoli);              //edita
            this.objeBeca2 = this.objeBeca;
            TipoEstado esta = new TipoEstado();
            esta.setCodiTipoEsta(3);
            this.objeBeca.setCodiTipoEsta(esta);
            FCDEBeca.edit(objeBeca);                          
            this.objeSoli2.setEstaSoliBeca(1);            
            TipoRetiro reti = new TipoRetiro();
            reti.setCodiReti(null);
            this.objeBeca2.setCodiReti(null);
            this.objeBeca2.setRetiBeca(null);
            esta.setCodiTipoEsta(1);
            this.objeBeca2.setCodiTipoEsta(esta);
            //objeSoli2.setBecaList(null);
            FCDESoli.create(objeSoli2);
            this.objeSoli2 = FCDESoli.findLast();
            this.objeBeca2.setCodiSoliBeca(objeSoli2);
            this.objeBeca2.setFechBaja(null);
            FCDEBeca.create(objeBeca2);     
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Beca Reactivada')");
            log.info("Beca reactivada");
            this.consTodo();
            this.consTodoH();
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            log.error(getRootCause(ex).getMessage());
            System.out.println("AQUI "+ex);
        }
        finally
        {
            
        }
    }
    
    public void desa()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listBeca.remove(this.objeBeca); //Limpia el objeto viejo
            TipoEstado esta = new TipoEstado();
            esta.setCodiTipoEsta(2);
            this.objeBeca.setCodiTipoEsta(esta);
            this.objeBeca.setFechBaja(new Date());
            this.objeSoli.setEstaSoliBeca(2);
            FCDEBeca.edit(this.objeBeca);
            FCDESoli.edit(objeSoli);
            FCDEDetaBeca.desa_deta(this.objeBeca.getCodiBeca());
            this.listBeca.add(this.objeBeca); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Beca Reactivada')");
            log.info("Beca reactivada");
            this.consTodo();
        }
        catch(Exception ex)
        {
                        ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            log.error(getRootCause(ex).getMessage());
            System.out.println("AQUI "+ex);
            System.out.println("AQUI "+ex.getMessage());
            System.out.println("AQUI "+ex.getCause());
            System.out.println("AQUI "+getRootCause(ex).getMessage());
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
           
            this.objeSoli = FCDESoli.find(codi);
            this.objeBeca = FCDEBeca.findSoli(objeSoli.getCodiSoliBeca());
            this.listDocu = FCDEDocu.findBySoli(codi);
            this.listDetaBeca = FCDEDetaBeca.findByBeca(objeBeca.getCodiBeca());
          
            this.guardar = false;
            this.carnet = objeSoli.getCarnAlum();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeBeca.getCodiSoliBeca().getNombAlum()) + "')");
            log.info("Beca Consultada");
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
    public boolean cons(SolicitudBeca obje)
    {
        boolean variable=false;
        try
        {
           SolicitudBeca s = new SolicitudBeca();
            s = FCDESoli.find(obje);
            if(s!=null)
            {variable=true;}
           
        }
        catch(Exception ex)
        {
            
        }
        return variable;
    }
    
    public boolean cons(String obje)
    {
        boolean variable=false;
        try
        {
           
           SolicitudBeca s1 = new SolicitudBeca();
            s1 = FCDESoli.findCarnet(obje);
            if(s1 != null)
            {variable=true;}
        }
        catch(Exception ex)
        {
            
        }
        return variable;
    }
    public void consTodo()
    {
        try
        {
            this.listSoli = FCDESoli.findAll();
            this.listBeca = FCDEBeca.findAllH();
            this.listSoliActivos = FCDESoli.findAllActivos();
            this.listBecaActivos = FCDEBeca.findAllActivos();
            this.listBecaDocu = FCDEBeca.findAllDocu();
            log.info("Beca Consultadas");
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
    
    public void consTodoH()
    {
        try
        {
            this.listSoliH = FCDESoli.findAll();
            this.listBecaH = FCDEBeca.findAllHisto();
            log.info("Beca Consultadas");
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
    public void consW()
    {
        RequestContext ctx = RequestContext.getCurrentInstance();
        Client client = ClientBuilder.newClient();
        String url = String.format("http://www.opensv.tk:8080/WebService/MiServicio/consAlum/%s", this.carnet.trim());        
       if(this.cons(carnet.trim()))
       {           
             ctx.execute("setMessage('MESS_ERRO', 'Atención', 'El Alumno ya se encuentra')");
       }
       else
       {
           WebTarget resource = client.target(url);
            Invocation.Builder request = resource.request();
            request.accept(MediaType.APPLICATION_JSON);
            Response response = request.get();
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
            {
                DatosAlumnos resp = response.readEntity(DatosAlumnos.class); //La respuesta de captura en un pojo que esta en el paquete utils
                this.objeSoli.setCarnAlum(carnet.trim());
                this.objeSoli.setNombAlum(resp.getNomb());;
                Grado grad = new Grado();
                String cortado= resp.getGrad().substring(0,1);                
                grad=FCDEGrado.find(Integer.parseInt(cortado));   
                this.objeSoli.setCodiGrad(grad);
                this.objeSoli.setFotoAlum(resp.getFoto());
                this.objeSoli.setEspeAlum(resp.getEspe());
                this.objeSoli.setGrupAlum(resp.getGrup());
                this.objeSoli.setSeccAcad(resp.getSeccAcad());
                this.objeSoli.setSeccTecn(resp.getSeccTecn());                
                this.toogFich();
                
                if(resp.getNomb() == null || resp.getNomb().equals(""))
                {
                    ctx.execute("setMessage('MESS_WARN', 'Atención', 'Alumno no encontrado.')");
                }
            }
            else
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar alumno')");
            }
       
       }
                
                
                
        
    }
    /*-----------------------------------------------------------*/
    //Aquí abajo estan toda la logica necesaria para los barridos, slider    
    /*-----------------------------------------------------------*/
    
    
    private boolean showCarn=false;
    private boolean showFich=false;
    private boolean showEmpr=false;
    
    public boolean isShowCarn() {
        return showCarn;
    }
    public boolean isShowFich() {
        return showFich;
    }
     public boolean isShowEmpr() {
        return showEmpr;
    }
    public void toogEmpr()
    {
        this.showEmpr = !this.showEmpr;
              
    }  
    public void toogFich()
    {
        this.showFich = !this.showFich;
        
    }       
    public void toogCarn()
    {
        this.showCarn = !this.showCarn;
    }
    
    /*todo lo de documentos xd*/
    
     @EJB
    private DocumentoFacadeLocal FCDEDocu;
    private Documento objeDocu;
    private List<Documento> listDocu;
    private boolean imagen;
    private String rutaC;
    private byte[] esto;
    private String tokens = "";

    public byte[] getEsto() {
        return esto;
    }

    public boolean isImagen() {
        return imagen;
    }

    public void setImagen(boolean imagen) {
        this.imagen = imagen;
    }

    public String getTokens() {
        return tokens;
    }
    
    
    
    public Documento getObjeDocu() {
        return objeDocu;
    }

    public void setObjeDocu(Documento objeDocu) {
        this.objeDocu = objeDocu;
    }


    public List<Documento> getListDocu() {
        return listDocu;
    }

  
    
    public void initDocus()
    {
        this.objeDocu = new Documento();
        this.guardar = true;
        this.consTodo();
        this.objeDocu.setFechDocu(new Date());
        this.inicializar();
        this.imagen = false;
        this.tokens = "Imagen";
    }
    
    public void limpFormDocus()
    {
        this.objeDocu = new Documento();
        this.guardar = true;  
        this.showImag=false;
        this.objeDocu.setFechDocu(new Date());
    }
    
    public void esta()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.objeDocu.setEstaDocu(1);                   
            this.objeDocu.setRutaDocu("mis heuvos");
            FCDEDocu.create(this.objeDocu);
            this.listDocu.add(this.objeDocu);
            this.limpForm();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            log.info("Documento Consultado");
        }
        catch (Exception e) 
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            log.error(getRootCause(e).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void guarDocus()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.carnet = objeDocu.getCodiSoliBeca().getCarnAlum();
            this.uploFile();
            this.objeDocu.setEstaDocu(1);   
            FCDEDocu.create(this.objeDocu);
            this.listDocu.add(this.objeDocu);
            this.limpForm();
            //this.carnet = objeDocu.getCodiSoliBeca().getCarnAlum();
            //this.uploFile();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            log.info("Documento Consultado");
        }
        catch (Exception e) 
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            log.error(getRootCause(e).getMessage());
        }
        finally
        {
            
        }
    }
    
    public void modiDocus()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.listDocu.remove(this.objeDocu); //Limpia el objeto viejo
            FCDEDocu.edit(this.objeDocu);
            this.listDocu.add(this.objeDocu); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            log.info("Documento Modificado");
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
          
            this.objeDocu.setEstaDocu(0);
            String ruta = this.rutas.get(0) + this.objeDocu.getRutaDocu();
            
    		File file = new File(ruta);

    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}


            
            FCDEDocu.remove(this.objeDocu);
            this.listDocu.remove(this.objeDocu); //Limpia el objeto viejo
            //this.listDocu.add(this.objeDocu); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            log.info("Documento Modificado");
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
     
    
    public void consTodoDocus()
    {
        try
        {
            this.listDocu = FCDEDocu.findAll();
            this.listSoli = FCDESoli.findAllDocu();
            log.info("Documentos Consultados");
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
    
    public void consDocu(int codi)
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            this.imagen = false;
            this.tokens = "Imagen";
            this.objeDocu = FCDEDocu.find(codi);
            String h = this.rutas.get(0);
            //System.out.println(h + this.objeDocu.getRutaDocu());
            rutaC = h + this.objeDocu.getRutaDocu();
            //this.objeDocu.setRutaDocu(h + this.objeDocu.getRutaDocu());
            this.guardar = false;
            this.showDocu = false;
            this.showImag=false;
            String ruta = this.rutas.get(0) + this.objeDocu.getRutaDocu();
            File file = new File(ruta);
            String[] tokens = file.getName().split("\\.(?=[^\\.]+$)");
            if("pdf".equals(tokens[1]))
            {
                this.imagen = !this.imagen;
                this.tokens = "Documento";
                InputStream docu = new FileInputStream(file);
                this.esto = readFully(docu);
            }
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeDocu.getRutaDocu()) + "')");
            log.info("Documento Consultado");
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
    
    
    
    /*para archivos xd*/
    
    private Part file;  
    List<String> rutas;
    int DireActuInde;    
    List<Archivo> listNombFile;


    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public List<Archivo> getListNombFile() {
        return listNombFile;
    }
    
    /**
     * Creates a new instance of UploadBean
     */
    
    public void inicializar()
    {
        try{
            
            this.listNombFile = new ArrayList<>();
            this.rutas = new ArrayList<>();
            //String ruta ="C:/Users/Ariel/Desktop/becas/";    
            //String ruta = "/home/eduardo/Escritorio/asd/";
            String ruta = "/Users/Kevin/Desktop";
           rutas.add(ruta);
           DireActuInde = 0;
           this.carnet = "";
        }
        catch(Exception e)
        {
            System.out.println(getRootCause(e).getMessage());
        }   
    }    
    public void RegresarRuta()
    {

        rutas.remove(rutas.size() - 1);
        DireActuInde--;
    }
    
    public void uploFile()
    {
        String Path = this.rutas.get(DireActuInde);
        try
        {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            System.out.println(request);
            for(Part item : request.getParts())
            {
                if(this.carnet.trim().length()==0)
                {
                   moveFilePart(item, Path);
                    
                }
                else
                {
                    String newPath= Path+this.carnet+"/";
                    File theDir = new File(newPath);

                    // if the directory does not exist, create it
                    if (!theDir.exists()) {
                        
                        boolean result = false;

                        try{
                            theDir.mkdir();
                            result = true;
                            this.listNombFile.add(new Archivo(carnet,"folder"  ));
                        } 
                        catch(SecurityException se){
                            //handle it
                        }        
                        if(result) {    
                            moveFilePart(item, newPath);
                        }
                    }
                    else
                    {
                        
                        moveFilePart(item, newPath);
                    }
                }
               
            }
            
        }
        catch(Exception ex)
        {
            System.out.println("Error en uploFile"+ex.getMessage());
        }
    }
    
    
    public static byte[] readFully(InputStream input) throws IOException
    {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
    
    
    private void moveFilePart(Part item,String path) throws IOException
    {
        try {
            if(item.getName().equals(file.getName()))
            {
                
                System.out.println(item.getSubmittedFileName());
                
                                System.out.println(item.getContentType());
                                
                                
                 this.listNombFile.add(new Archivo(
                        item.getSubmittedFileName(),
                        item.getInputStream(),
                        item.getContentType(),
                        readFully(item.getInputStream())
                ));

               // System.out.println(item.getSubmittedFileName() +" "+item.getInputStream()+" "+ item.getContentType());
                System.out.println("RUTA:" + item.getSubmittedFileName());//Aqui esta la ruta :3 
                
                this.processFilePart(item, String.format("%s%s",path, item.getSubmittedFileName()));
                this.objeDocu.setRutaDocu(this.objeDocu.getCodiSoliBeca().getCarnAlum() + "/" + item.getSubmittedFileName());
             }
        } catch (Exception e) {
            System.out.println("Error en moveFilePart"+e.getMessage());
        }
        
    }
    private void processFilePart(Part part, String filename) throws IOException
    {
        int DEFAULT_BUFFER_SIZE = 2048;
        InputStream input = null;
        OutputStream output = null;
        try
        {
            input = new BufferedInputStream(part.getInputStream(), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(new FileOutputStream(filename), DEFAULT_BUFFER_SIZE);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            for (int length = 0; ((length = input.read(buffer)) > 0);)
            {
                output.write(buffer, 0, length);
            }
            
        }
        finally
        {
            if (output != null)
                try
                {
                    output.close();
                }
                catch (IOException logOrIgnore)
                {
                }
            if (input != null)
                try
                {
                    input.close();
                }
                catch (IOException logOrIgnore)
                {
                }
        }

        // how to get the result

        part.delete();
    }
    
    
    //Lógica slider para docuemntos
    private  boolean showDocu = false;

    public boolean isShowDocu() {
        return showDocu;
    }
    
    
    public void toogDocu()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.showDocu = !this.showDocu;
    }
    
     //Lógica slider para imagenes
    private  boolean showImag = false;

    public boolean isShowImag() {
        return showImag;
    }
    
    
    public void toogImag()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        
        this.showImag = !this.showImag;
        System.out.println(this.showImag);
        
    }
    
    /*para detalles xd*/
    
   
    private DetalleBeca objeDetaBeca;
    private List<DetalleBeca> listDetaBeca;

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

    
    //Manejo de combobox
    private Beca objeCombPadr;
    //private SolicitudBeca objeCombPadr;

    public Beca getObjeCombPadr() {
        return objeCombPadr;
    }

    public void setObjeCombPadr(Beca objeCombPadr) {
        this.objeCombPadr = objeCombPadr;
    }
    
    public void onAlumBecaSelect(){
        
        listTipoBeca = FCDETipoBeca.findTipos(objeCombPadr.getCodiSoliBeca().getCodiGrad().getNivelGrad());
    }
    
    DetalleBeca vali;
    
    /**
     * Creates a new instance of DetalleBecaBean
     */
   
    
    
    public void initDetalle()
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
        }
         
    }
    
    public void limpFormDetalle()
    {
        this.objeDetaBeca = new DetalleBeca();
        this.guardar = true;        
    }
    
    public void guarDetalle()
    {
        boolean guardarDeta;
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
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
    
    public void modiDetalle()
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
    
    public void elimDetalle()
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
    
    public void consTodoDetalle()
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
    
    public void consDetalle(int codi)
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
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
    
    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
           
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
}
