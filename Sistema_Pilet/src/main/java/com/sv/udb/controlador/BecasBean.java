/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.BecaFacadeLocal;
import com.sv.udb.ejb.DetalleBecaFacadeLocal;
import com.sv.udb.ejb.GradoFacadeLocal;
import com.sv.udb.ejb.SolicitudBecaFacadeLocal;
import com.sv.udb.ejb.TipoBecaFacadeLocal;
import com.sv.udb.modelo.Beca;
import com.sv.udb.modelo.Grado;
import com.sv.udb.modelo.SolicitudBeca;
import com.sv.udb.modelo.TipoBeca;
import com.sv.udb.modelo.TipoEstado;
import com.sv.udb.modelo.TipoRetiro;
import com.sv.udb.utils.pojos.DatosAlumnos;
import java.io.Serializable;
import java.util.ArrayList;
//import java.util.Base64;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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
    private StreamedContent fotoAlum;

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
    private static Logger log = Logger.getLogger(BecasBean.class);

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

    public StreamedContent getFotoAlum() {
        return fotoAlum;
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
            //el objeto objeSoli tiene los datos de la solicitud pero modificados, entonces hay que consultar la misma solicitud
            //pero con los datos viejos.
            objeSoli2 = FCDESoli.find(this.objeSoli.getCodiSoliBeca());//Consulta la solicitud "vieja"
            this.objeBeca = FCDEBeca.findSoli(this.objeSoli.getCodiSoliBeca());//Consulta la beca de la solicitud actual
            this.objeBeca2 = FCDEBeca.findSoli(this.objeSoli2.getCodiSoliBeca());//consulta la beca de la solicitud "vieja"
            this.listSoli.remove(this.objeSoli);//quita la solicitud de la lista
            this.objeSoli2.setEstaSoliBeca(3);//le setea a la solicitud "vieja" el estado 3 de historial
            FCDESoli.edit(objeSoli2);//edita el registro de la solicitud en la base
            this.listSoli.add(objeSoli2);//Agrega la solicitud "vieja" a la lista 
            this.objeSoli.setEstaSoliBeca(1);//Mmmm la verda no se por que es esto xD
            FCDESoli.create(objeSoli);//crea el nuevo registro de la solicitud
            this.listSoli.add(objeSoli);//lo agrega a la lista
            this.listBeca.remove(this.objeBeca2);//quita la beca de la lista
            TipoEstado es = new TipoEstado();
            es.setCodiTipoEsta(3);
            this.objeBeca2.setCodiTipoEsta(es);//le setea el estado de la beca de 3 de historial
            this.objeBeca2.setFechBaja(new Date());//le setea la fecha de baja
            FCDEBeca.edit(objeBeca2);//edita el registro viejo de la base
            this.objeSoli = FCDESoli.findLast();//busca la ultima solicitud creada
            this.objeSoli.setFechSoliBeca(new Date());//le seta la fecha
            this.objeBeca.setCodiSoliBeca(objeSoli);//le setea el codigo de la solicitud
            es.setCodiTipoEsta(this.objeSoli.getEstaSoliBeca());//le setea el mismo estado de la soli a la beca
            this.objeBeca.setCodiTipoEsta(es);
            FCDEBeca.create(objeBeca);//crea el nuevo registro de la beca
            this.listBeca.add(objeBeca);//lo agrega a la lista
            this.consTodoH();//consulta los registros con estado 3
            this.consTodo();//consulta los registros con estado diferente a 3
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
            FCDESoli.edit(objeSoli);     
             this.objeBeca = FCDEBeca.find(objeSoli.getCodiSoliBeca());
            this.objeBeca2 = this.objeBeca;
            TipoEstado esta = new TipoEstado();
            esta.setCodiTipoEsta(3);
            this.objeBeca.setCodiTipoEsta(esta);
            System.out.println(objeBeca.getCodiSoliBeca().getNombAlum());
            FCDEBeca.edit(objeBeca);                          
            this.objeSoli2.setEstaSoliBeca(1);            
            TipoRetiro reti = new TipoRetiro();
            reti.setCodiReti(null);
            this.objeBeca2.setCodiReti(null);
            this.objeBeca2.setRetiBeca(null);
            esta.setCodiTipoEsta(1);
            this.objeBeca2.setCodiTipoEsta(esta);
            //objeSoli2.setBecaList(null);
            objeSoli2.setFechSoliBeca(new Date());
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
            listTipoBeca = FCDETipoBeca.findTipos(objeBeca.getCodiSoliBeca().getCodiGrad().getNivelGrad());          
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
                if(resp.getFoto() != null)
                {
                    //String base64Image = new String(Base64.getDecoder().decode(resp.getFoto()));
                    //System.err.println("Base:" +base64Image);
//                    this.fotoAlum = new DefaultStreamedContent(base64Image, "image/jpeg", "Demo.jpg");
                }
                else
                {
                    this.fotoAlum = new DefaultStreamedContent();
                }
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
    public void toogRegre()
    {
       showCarn=false;   
       showFich=false;
       showEmpr=false;
       this.guardar=true;
    }
    
    /*-----------------------------------------------------------*/
    //Aquí termina  toda la logica necesaria para los barridos, slider    
    /*-----------------------------------------------------------*/
    
    
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
