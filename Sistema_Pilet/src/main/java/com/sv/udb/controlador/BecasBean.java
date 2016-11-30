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
import com.sv.udb.ejb.OpcionFacadeLocal;
import com.sv.udb.ejb.PreguntaFacadeLocal;
import com.sv.udb.ejb.RespuestaFacadeLocal;
import com.sv.udb.ejb.SeccionFacadeLocal;
import com.sv.udb.ejb.SolicitudBecaFacadeLocal;
import com.sv.udb.ejb.TipoBecaFacadeLocal;
import com.sv.udb.modelo.Beca;
import com.sv.udb.modelo.Grado;
import com.sv.udb.modelo.Opcion;
import com.sv.udb.modelo.OpcionRespuesta;
import com.sv.udb.modelo.Pregunta;
import com.sv.udb.modelo.Respuesta;
import com.sv.udb.modelo.Seccion;
import com.sv.udb.modelo.SolicitudBeca;
import com.sv.udb.modelo.TipoBeca;
import com.sv.udb.modelo.TipoEstado;
import com.sv.udb.modelo.TipoRetiro;
import com.sv.udb.modelo.UsuarioRol;
import com.sv.udb.utils.DynamicField;
import com.sv.udb.utils.pojos.DatosAlumnos;
import java.io.Serializable;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
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
    
    
    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;
    
    private static final long serialVersionUID = -5196715359527212081L;
    
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
        initDina();
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
    public boolean guar()
    {        
        boolean respGuar = false;
        this.objeSoli.setFechSoliBeca(new Date());
        this.objeBeca.setFechInic(new Date());
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(objeSoli.getCarnAlum() == null || objeSoli.getNombAlum() == null)
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Busque un alumno')");
                respGuar = false;
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
                    respGuar = true;
            
            }
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            log.error(getRootCause(ex).getMessage());
            respGuar = false;
        }
        finally
        {
            
        }
        return respGuar;
    }
    
    /*ORDEN DE LAS COSAS A GUARDAR EN LOS RETIROS Y ACTUALIZACIONES DEL ESTADO
    Tipo de operación (Ej. Cambio de empresa, cambio de condiciones, cancelación de beca)
    Estado antiguo (Ej. empresa antigua, antigua condición)
    Razón del cambio
    Nuevo estado (nombre de la nueva empresa padrino, nueva condición)*/
 
    //Variables para la razón de retiro
    String tipo, anti,razon, nuev;
    
    public void modi(int num)
    {
        
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            TipoEstado es = new TipoEstado();
            int tipoEsta = 0;
            razon = "["+this.objeBeca.getRetiBeca()+"]";
            //el objeto objeSoli tiene los datos de la solicitud pero modificados, entonces hay que consultar la misma solicitud
            //pero con los datos viejos.
            //Objetos viejitos
            objeSoli2 = FCDESoli.find(this.objeSoli.getCodiSoliBeca());
            this.objeBeca2 = FCDEBeca.findSoli(this.objeSoli2.getCodiSoliBeca());
            switch (num) {
                case 1:
                    tipo = "[Cambio de empresa patrocinador]";
                    //Empresa antigua
                    anti = "[" + this.objeSoli2.getCodiEmpr().getNombEmpr() + "]";
                    //Empresa nueva
                    nuev = "[" + this.objeSoli.getCodiEmpr().getNombEmpr() + "]";
                    break;
                case 2:
                    tipoEsta = this.objeBeca.getCodiTipoEsta().getCodiTipoEsta();
                    tipo = "[Cambio de estado]";
                    //Estado antiguo
                    anti = "["+this.objeBeca2.getCodiTipoEsta().getNombTipoEsta()+"]";
                    //Nuevo estado
                    nuev = "["+this.objeBeca.getCodiReti().getNombReti()+"]";
                    break;
                default:
                    ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
                    break;
            }
            //Objetos nuevitos (El de la soli ya lo tiene el objeSoli actual)
            this.objeBeca = FCDEBeca.findSoli(this.objeSoli.getCodiSoliBeca());
            //Operaciones a objetos viejitos
            this.objeSoli2.setEstaSoliBeca(3);//3 de historial
            FCDESoli.edit(objeSoli2);//edita el registro de la solicitud en la base
            this.listSoli.add(objeSoli2);//Agrega la solicitud "vieja" a la lista
            es.setCodiTipoEsta(3);
            //Setearle la razón a la criatura
            this.objeBeca2.setRetiBeca(tipo+anti+razon+nuev);
            this.objeBeca2.setCodiTipoEsta(es);//le setea el estado de la beca de 3 de historial
            this.objeBeca2.setFechBaja(new Date());//le setea la fecha de baja
            FCDEBeca.edit(objeBeca2);//edita el registro viejo de la base

            //Cosas que no sé que hacen aquí
            this.listSoli.remove(this.objeSoli2);//quita la solicitud de la lista
            this.listBeca.remove(this.objeBeca2);//quita la beca de la lista
            
            //Operaciones a los objetos nuevitos
            this.objeSoli.setFechSoliBeca(new Date());
            FCDESoli.create(objeSoli);//crea el nuevo registro de la solicitud
            this.listSoli.add(objeSoli);//lo agrega a la lista
            this.objeSoli = FCDESoli.findLast();//busca la ultima solicitud creada
            
            this.objeBeca.setCodiSoliBeca(objeSoli);//le setea el codigo de la solicitud
            es.setCodiTipoEsta(tipoEsta);//le setea el mismo estado de la soli a la beca
            this.objeBeca.setCodiTipoEsta(es);
            FCDEBeca.create(objeBeca);//crea el nuevo registro de la beca
            this.listBeca.add(objeBeca);//lo agrega a la lista
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
    
    //Funcion que desactiva de un solo
    public void desa()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            //Para el retiro
            String tipoCamb = "[Cancelación de beca]";
            this.objeBeca2 = FCDEBeca.findSoli(this.objeSoli.getCodiSoliBeca());
            String antiEsta = "["+this.objeBeca2.getCodiTipoEsta().getNombTipoEsta()+"]";
            String razon = "["+this.objeBeca.getRetiBeca()+"]";
            String nuevEsta = "[Beca cancelada]";
            
            this.listBeca.remove(this.objeBeca); //Limpia el objeto viejo
            TipoEstado esta = new TipoEstado();
            esta.setCodiTipoEsta(2);
            this.objeBeca.setCodiTipoEsta(esta);
            this.objeBeca.setRetiBeca(tipoCamb+antiEsta+razon+nuevEsta);
            this.objeBeca.setFechBaja(new Date());
            this.objeSoli.setEstaSoliBeca(2);
            FCDEBeca.edit(this.objeBeca);
            FCDESoli.edit(objeSoli);
            FCDEDetaBeca.desa_deta(this.objeBeca.getCodiBeca());
            this.listBeca.add(this.objeBeca); //Agrega el objeto modificado
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Beca desactivada')");
            log.info("Beca desactivada");
            this.consTodo();
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
    public boolean consW()
    {
        boolean respFunc = false;
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
                
                if(resp.getNomb() == null || resp.getNomb().equals(""))
                {
                    ctx.execute("setMessage('MESS_WARN', 'Atención', 'Alumno no encontrado.')");
                    respFunc = false;
                }
                else
                {                
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
                      //  String base64Image = new String(Base64.getDecoder().decode(resp.getFoto()));
                       // System.err.println("Base:" +base64Image);
    //                    this.fotoAlum = new DefaultStreamedContent(base64Image, "image/jpeg", "Demo.jpg");
                    }
                    else
                    {
                        this.fotoAlum = new DefaultStreamedContent();
                    }
                    this.toogFich();
                    respFunc = true;
                }
            }
            else
            {
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar alumno')");
                respFunc = false;
            }       
       }
       return respFunc;
    }
    /*-----------------------------------------------------------*/
    //Aquí abajo estan toda la logica necesaria para los barridos, slider    
    /*-----------------------------------------------------------*/
    
    //Otra variable que pertece a laogica para los barridos es guardar
    private boolean showCarn=false;
    private boolean showFich=false;
    private boolean showEmpr=false;
    private boolean showSoci=false;
    /*Explicación de como funciona:
    
    Guardar: Cuando por ejemplo guardar sea true, eso quiere decir que no se esta modificando. Eso quiere decir,que cuando sea true
    se pueden mostrar formularios que no deben mostrarse cuando se esta modificando, por ejemplo para ingresar el carnet o
    para ingresar la empresa. En ambos casos no se esta guardando, ah! pero son barridos diferentes, eso corresponde a otras
    variables. 
    
    showCarn: esta varible, solo define si se muestra el campo donde requerimos el carnet para hacer una nueva solicitud, 
    esto quiere decir que cuando esta variable sea true Y la variable de guardar sea true, solo se mostrara el campo que pida
    el carnet. Porque cuando pida el carnet, quiere decir que esta agregando una nueva solicitud y no esta modificando. 
    
    showEmpr: Es igual a showCarn, las dos variables se utilizan cuando se esta agregando una nueva solicitud, por lo tanto
    cuando guardar es igual a true , cuando no se esta modificando, lo complicado es que son dos variables, dos barridos que 
    deben activarse y desactivarse mientras guardar esta true
    
    Proceso para guardar -->            Pedir Carnet --> Mostrar Ficha --> Pedir Empresa --> Formulario para modificar
                        | Guardar         true              true            true                 false
                        | showCarnt       true              false           false                false    
                        | showFich        false             true            false                true
                        | showEmpres      false             false           true                 false
    
    
    En la tabla anterior se puede obsservar como es el comportamiento de las variables durante todo el proceso 
    se puede observar que empieza que hasta que se llega al formulario de modificar la variable de guardar va a ser true
    y segun sea el paso  o "step" de agregar solicitud las otras variablas van a ir cambiando, podes ver a "Empresa", "Ficha",
    "Carnet" como bloques, nosotros definimos que loque se muestra en cada paso, en el paso numero 1 donde pedimos el carnet
    decimos que solo muestre el bloque del carnet y así. 
    
    Ficha: este es un bloque ambiguo. Se puede interpretar de dos maneras, se puede utilizar para guardar y para modificar
    varios elementos de el se muestra y no se muestran  dependiente si se este guardando(gaurdar = true) o modificando
    (guardar = false) Por ejemplo cuando se esta guardando solo muestra la información del alumno y el boton "Agregar nueva soli"
    pero cuando se esta modificando  no  muestra ese boton si no, que muesta las tablas de documentos y detalles
      
    */

    public boolean isShowSoci() {
        return showSoci;
    }

    public void setShowSoci(boolean showSoci) {
        this.showSoci = showSoci;
    }
    
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
     public void toogSoci()
    {
        this.showSoci = !this.showSoci;
         showCarn=false;   
       showFich=false;
       showEmpr=false;
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
    /*-----------------------------------------------------------*/
    //Aquí empieza toda la logia del formulario dinamico  
    /*-----------------------------------------------------------*/
    
     @EJB
    private RespuestaFacadeLocal FCDEResp;
    private List<DynamicField> listCmps;
    private Map<String, Object> mapa;
    @EJB
    private OpcionFacadeLocal FCDEOpci;
     @EJB
     private PreguntaFacadeLocal FCDEPreg;
     @EJB
     private SeccionFacadeLocal FCDESecc;    
     
    private List<Opcion> listOpci;
    private List<Pregunta> listPreg;
    private List<Seccion> listSecc;
    
     //Bean de session
    @Inject
    private LoginBean logiBean; 
    
    
   
    public Map<String, Object> getMapa() {
        return mapa;
    }

    public void setMapa(Map<String, Object> mapa) {
        this.mapa = mapa;
    }
    
    public void initDina()
    {
        try {
             
            this.listCmps = new ArrayList<>();
            this.mapa = new HashMap<>();
            this.VeriRole();
          
            //Agrega un elemento
            consTodoDina();
           
            
            for(Opcion temp : this.listOpci)
            {
                String codiDina = String.format("Dina%s", String.valueOf(temp.getCodiOpci()));
                /*codigo y respuesta xd*/
                this.mapa.put(codiDina, null);
                Map<Object, Object> listOpciTemp = null;
                if(temp.getCodiEstr().getTipoEstr().equals("SELECT") || temp.getCodiEstr().getTipoEstr().equals("SELECTMANYCHECKBOX"))
                {
                    listOpciTemp = new HashMap<>();
                    for(OpcionRespuesta tempOR : temp.getOpcionRespuestaList())
                    {
                        if(tempOR.getEstaOpci() == 1)
                        {
                            listOpciTemp.put(tempOR.getCodiOpciResp(),tempOR.getDescOpci());
                        }
                    }
                }
                System.out.println(codiDina);
                this.listCmps.add(new DynamicField(temp.getTituOpci(), codiDina, listOpciTemp, temp.getCodiEstr().getTipoEstr(),temp.getCodiPreg()));
            }
            
        } catch (Exception e) {
            System.out.println("Error en init :"+e.getMessage());
        }
    }
    
    
    public void consTodoDina()
    {
        try
        {
            this.listOpci= FCDEOpci.findAll();
            this.listPreg = FCDEPreg.findAll();
            this.listSecc=FCDESecc.findAll();
           
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            
        }
        finally
        {
            
        }
    }
    
    public void guarDina()
    {
         RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
       
        try
        {            
            Beca objeBecaLoca;
                        
            //if(this.objeBecaBean.getCarnet()!=null)
            //{
                objeBecaLoca = this.objeBeca;
            //}
            /*else
            {
                objeBecaLoca = objeBecaBean;
                objeBecaLoca.setObjeSoli(objeSoli);
                objeBecaLoca.setCarnet(logiBean.getObjeUsua().getAcceUsua());
                if(objeBecaLoca.consW())
                {
                    objeBecaLoca.getObjeSoli().setCodiEmpr(new Empresa(1));
                    if(objeBecaLoca.guar())
                    {
                        objeSoli = FCDESoli.findLast();
                    }
                }
                else
                {
                    objeSoli = FCDESoli.findCarnet(logiBean.getObjeUsua().getAcceUsua());
                }
            }*/
            /*Crear la nuva solicitud*/
                      
            for(DynamicField temp:this.listCmps)
            {
                String valor = "";
                Integer codiDinaDb = Integer.parseInt(temp.getFieldKey().replace("Dina", ""));
                if(temp.getType().equals("SELECTMANYCHECKBOX"))
                {
                    String respArray = "";
                    for(Object tempResp : (Object[])this.mapa.get(temp.getFieldKey()))
                    {
                        int codigoOpcionRespuesta = Integer.parseInt(String.valueOf(tempResp));
                        respArray = respArray + "-" + tempResp;
                        Respuesta respuesta = new Respuesta( objeSoli,new Opcion(codiDinaDb),new OpcionRespuesta(codigoOpcionRespuesta),1);
                        respuesta.setDescOpci("S/R");
                        FCDEResp.create(respuesta);
                    }
                    respArray = respArray.trim();
                    valor = "id: " + codiDinaDb + " === valor: " + respArray;
                }
                else
                {
                    String valorDb = this.mapa.get(temp.getFieldKey()).toString();                    
                    Respuesta respuesta = new Respuesta(objeSoli,new Opcion(codiDinaDb),valorDb,1);
                    FCDEResp.create(respuesta);
                    valor = "id: " + codiDinaDb + " === valor: " + this.mapa.get(temp.getFieldKey());
                }
                System.err.println(valor);
            }
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");

        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            System.out.println("Error: "+ex.getMessage());
            System.out.println("Error: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
  
    
    @SuppressWarnings("cast")
    public void populateForm(ComponentSystemEvent event)
    {
        
        try  
        {
            
           HtmlForm form = (HtmlForm) event.getComponent();
            for(Seccion seccion : this.listSecc)
            {
                form.getChildren().add(this.createUIOutput("<div class=\"panel panel-primary\">"));
                form.getChildren().add(this.createUIOutput("<div class=\"panel-heading\">"));
                form.getChildren().add(this.createUIOutput(seccion.getNombSecc()));
                form.getChildren().add(this.createUIOutput("</div>"));
                form.getChildren().add(this.createUIOutput(" <div class=\"panel-body\">"));         
                form.getChildren().add(this.createUIOutput(" <h3>Indicaciones: "+seccion.getIndiSecc()+"</h3>"));       
                form.getChildren().add(this.createUIOutput("<fieldset>"));
                
                    for(Pregunta pregunta :this.listPreg)
                    {
                        if(seccion.getCodiSecc() == pregunta.getCodiSecc().getCodiSecc())
                        {

                            form.getChildren().add(this.createUIOutput("<h3>"+pregunta.getDescPreg()+"</h3>"));
                            for (DynamicField field : this.listCmps) //Recorre los elementos
                            {
                                if(Objects.equals(field.getId().getCodiPreg(), pregunta.getCodiPreg()))
                                {
                                    form.getChildren().add(this.createUIOutput("<div class=\"form-group\">"));
                                    switch (field.getType())
                                    {
                                        case "TEXT":
                                            //Crea el label
                                            form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
                                            //Crea el input
                                            form.getChildren().add(this.getUIComponent(field, HtmlInputText.COMPONENT_TYPE));
                                            break;
                                        case "TEXTAREA":
                                            //Crea el label
                                            form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
                                            //Crea el input
                                            form.getChildren().add(this.getUIComponent(field, HtmlInputTextarea.COMPONENT_TYPE));
                                            break;
                                        case "SELECT":
                                            //Crea el label
                                            form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
                                            //Crea el select
                                            form.getChildren().add(this.getUIComponent(field, HtmlSelectOneMenu.COMPONENT_TYPE));
                                            break;
                                        case "SELECTMANYCHECKBOX":
                                            //Crea el label
                                            form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
                                            //Crea el select
                                            form.getChildren().add(this.getUIComponent(field, HtmlSelectManyCheckbox.COMPONENT_TYPE));
                                            break;
                                    }
                                    form.getChildren().add(this.createUIOutput("</div>"));

                                }

                            }

                        }
                    }
                form.getChildren().add(this.createUIOutput("</fieldset>"));
                form.getChildren().add(this.createUIOutput("</div>"));
                form.getChildren().add(this.createUIOutput("</div>"));
            }
            //Agregar los botones
                UIComponent btonGroup = this.getUIButtons(form);
                if(btonGroup != null)
                {
                    form.getChildren().add(btonGroup);
                }
        } 
        catch (Exception e) {
            System.out.println("Error populate:"+e.getMessage());
        }
          
    }

    private ValueExpression createValueExpression(String string, Class<String> aClass) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().getExpressionFactory()
                .createValueExpression(context.getELContext(), string, aClass);
    }
    
    private UIOutput createUIOutput(String value)
    {
        UIOutput resp = new UIOutput();
        resp.setRendererType("javax.faces.Text");
        resp.setValue(value);
        return resp;
    }
    
    private UIComponent getUIButtons(HtmlForm form)
    {
        UIComponent resp = null;
        for(UIComponent temp : form.getChildren())
        {
            if(temp.getId().equals("btonGroup"))
            {
                resp = temp;
                break;
            }
        }
        return resp;
    }
    
    private UIComponent getUIComponent(DynamicField field, String type)
    {
        UIComponent resp = null;
        Application app = FacesContext.getCurrentInstance().getApplication();
        if(type.equals(HtmlOutputLabel.COMPONENT_TYPE))
        {
            HtmlOutputLabel label = (HtmlOutputLabel)app.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
            label.setFor(field.getFieldKey());
            label.setValueExpression("value", createValueExpression(field.getLabel(), String.class));
            resp = label;
        }          
        else if(type.equals(HtmlInputText.COMPONENT_TYPE))
        {
            HtmlInputText input = (HtmlInputText)app.createComponent(HtmlInputText.COMPONENT_TYPE);
            input.setId(field.getFieldKey());
            input.setValueExpression("value", createValueExpression("#{dinamicoBean.mapa['" + field.getFieldKey() + "']}", String.class));
            input.setStyleClass("form-control");
            resp = input;
        }
        
        else if(type.equals(HtmlInputTextarea.COMPONENT_TYPE))
        {
            HtmlInputTextarea input = (HtmlInputTextarea)app.createComponent(HtmlInputTextarea.COMPONENT_TYPE);
            input.setId(field.getFieldKey());
            input.setValueExpression("value", createValueExpression("#{dinamicoBean.mapa['" + field.getFieldKey() + "']}", String.class));            
            input.setStyleClass("form-control");
            resp = input;
        }
         else if(type.equals(HtmlDataTable.COMPONENT_TYPE ))
        {
            
        }     
        else if(type.equals(HtmlSelectOneMenu.COMPONENT_TYPE))
        {
            HtmlSelectOneMenu input = (HtmlSelectOneMenu)app.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
            input.setId(field.getFieldKey());
            input.setValueExpression("value", createValueExpression("#{dinamicoBean.mapa['" + field.getFieldKey() + "']}", String.class));
            input.setStyleClass("form-control");
            if(field.getFieldValue() != null)
            {
                UISelectItems objeItems = new UISelectItems();
                List<SelectItem> listItems = new ArrayList<>();
                SelectItem seleItem = new SelectItem();
                seleItem.setValue(null);
                seleItem.setLabel("Seleccione...");
                listItems.add(seleItem);
                for(Object entry : ((HashMap)field.getFieldValue()).entrySet())
                {
                    Map.Entry<Object, Object> item = (Map.Entry<Object, Object>)entry;
                    seleItem = new SelectItem();
                    seleItem.setValue(item.getKey());
                    seleItem.setLabel((String)item.getValue());
                    listItems.add(seleItem);
                }
                objeItems.setValue(listItems.toArray());
                input.getChildren().add(objeItems);
            }
            resp = input;
        }
        else if(type.equals(HtmlSelectManyCheckbox.COMPONENT_TYPE))
        {
            HtmlSelectManyCheckbox input = (HtmlSelectManyCheckbox)app.createComponent(HtmlSelectManyCheckbox.COMPONENT_TYPE);
            input.setId(field.getFieldKey());
            input.setValueExpression("value", createValueExpression("#{dinamicoBean.mapa['" + field.getFieldKey() + "']}", String.class));
            input.setStyleClass("form-control");
            if(field.getFieldValue() != null)
            {
                UISelectItems objeItems = new UISelectItems();
                List<SelectItem> listItems = new ArrayList<>();
//                SelectItem seleItem;
                for(Object entry : ((HashMap)field.getFieldValue()).entrySet())
                {
                    Map.Entry<Object, Object> item = (Map.Entry<Object, Object>)entry;
                    listItems.add(new SelectItem(item.getKey(), (String)item.getValue()));
                }
                objeItems.setValue(listItems);
                input.getChildren().add(objeItems);
            }
            resp = input;
        }
        return resp;
    }
    
    /*Para barridos */
      private boolean TrabaSoci=false;
    private boolean Alum=false;

    public boolean isTrabaSoci() {
        return TrabaSoci;
    }

    public void setTrabaSoci(boolean TrabaSoci) {
        this.TrabaSoci = TrabaSoci;
    }

    public boolean isAlum() {
        return Alum;
    }

    public void setAlum(boolean Alum) {
        this.Alum = Alum;
    }
    
    public void VeriRole()
    {
             List<UsuarioRol> lis = logiBean.getObjeUsua().getUsuarioRolList();
             for(UsuarioRol temp: lis)
             {                 
                 if(temp.getCodiRole().getCodiRole() == 7)
                 {
                     /*alumnoo */
                     this.Alum=true;
                 }
                 else if(temp.getCodiRole().getCodiRole() == 8)
                 {
                     /*trabajadora social*/
                     this.TrabaSoci=false;
                 }
             }
            
    }
     public boolean consIfCarnExis()
    {       
        return FCDEResp.ReadIfCarnExis(logiBean.getObjeUsua().getAcceUsua());
    }
    /*-----------------------------------------------------------*/
    //Aquí termina toda la logia del formulario dinamico  
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
