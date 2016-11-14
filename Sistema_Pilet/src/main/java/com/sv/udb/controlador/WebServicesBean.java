package com.sv.udb.controlador;

import com.sv.udb.modelo.Usuario;
import com.sv.udb.utils.ConsultarCodiEmpleadoLogin;
import com.sv.udb.utils.UsuariosPojo;
import com.sv.udb.utils.pojos.DatosUsuarios;
import com.sv.udb.utils.pojos.WSconsUsua;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.List;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Adonay
 */
@Named(value = "webServicesBean")
@ViewScoped
public class WebServicesBean implements Serializable {
    
    private static final long serialVersionUID = 1L;    
    private String filtNomb = null; //Filtro de búsqueda
    private String filtApel = null;
    private String filtTipo = null;
    private WSconsUsua objeWebServ;
    //Lógica slider
    private boolean showBusc = false;
    
    

    public String getFiltNomb() {
        return filtNomb;
    }

    public void setFiltNomb(String filtNomb) {
        this.filtNomb = filtNomb;
    }

    public String getFiltApel() {
        return filtApel;
    }

    public void setFiltApel(String filtApel) {
        this.filtApel = filtApel;
    }

    public String getFiltTipo() {
        return filtTipo;
    }

    public void setFiltTipo(String filtTipo) {
        this.filtTipo = filtTipo;
    }

    public WSconsUsua getObjeWebServ() {
        return objeWebServ;
    }

    public void setObjeWebServ(WSconsUsua objeWebServ) {
        this.objeWebServ = objeWebServ;
    }
    
    
    /**
     * Creates a new instance of WebServicesBean
     */
    public WebServicesBean() {
        
    }

    public boolean isShowBusc() {
        return showBusc;
    }
    
    public UsuariosPojo consLogi(String acce, String cont)
    {
        UsuariosPojo resp;
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        Client client = ClientBuilder.newClient();
        String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
        url = String.format("%s/%s/%s/%s", url, "consLogi", acce, getHash(cont));
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            resp = response.readEntity(UsuariosPojo.class);//La respuesta de captura en un pojo que esta en el paquete utils
            if(!resp.getTipo().equals("alum")){
                LoginBean.setCodiEmplSesi(new ConsultarCodiEmpleadoLogin().consultarCodigo(acce));
            }
        }
        else
        {
            resp = null;
        }
        return resp;
    }
    
    private String getHash(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    private String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
    
    public void nuev()
    {
        this.objeWebServ = new WSconsUsua();
    }
    
    public void abri()
    {
        
    }
    
    public void limpForm()
    {
        this.objeWebServ = new WSconsUsua();
    }
    
    public void consWebServHabiUsua()
    {
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        Client client = ClientBuilder.newClient();
        String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
        url = String.format("%s/%s/%s/%s/%s", url, "consUsua", this.filtNomb.equals("") ? null : this.filtNomb, this.filtApel.equals("") ? null : this.filtApel, this.filtTipo.equals("") ? null : this.filtTipo);
        System.out.println(url);
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"));
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            this.objeWebServ = response.readEntity(WSconsUsua.class); //La respuesta de captura en un pojo que esta en el paquete utils
            UsuarioBean usua = new UsuarioBean();
            for(DatosUsuarios temp : objeWebServ.getResu()){ 
                Usuario objeUsua = new Usuario();
                objeUsua.setAcceUsua(temp.getUsua());
                if(usua.getListUsua().contains(objeUsua)){
                    this.objeWebServ.getResu().remove(temp);
                }
            }
            
        }
        else
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al procesar la consulta')");
        }
    }
    
    /*
    * Toogle buscador, cambia el valor del buscador
    */
    public void toogBusc()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        this.showBusc = !this.showBusc;
    }
    
}