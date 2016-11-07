package com.sv.udb.controlador;

import com.sv.udb.utils.UsuariosPojo;
import com.sv.udb.utils.pojos.DatosUsuarios;
import com.sv.udb.utils.pojos.WSconsUsua;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
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
@Dependent
public class WebServicesBean implements Serializable {

    private static final long serialVersionUID = 1L;    
    private String nombUsua;
    private String filt; //Filotro de búsqueda
    private String filtApel;
    private String filtTipo;
    private WSconsUsua objeWebServ;
    //Lógica slider
    private boolean showBusc = false;

    public String getNombUsua() {
        return nombUsua;
    }

    public void setNombUsua(String nombUsua) {
        this.nombUsua = nombUsua;
    }

    public String getFilt() {
        return filt;
    }

    public void setFilt(String filt) {
        this.filt = filt;
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
        consWebServ();
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
        Client client = ClientBuilder.newClient();
        String url = String.format("http://www.opensv.tk:8080/WebService/MiServicio/consLogi/%s/%s", acce,getSHA256Hash(cont));
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            resp = response.readEntity(UsuariosPojo.class); //La respuesta de captura en un pojo que esta en el paquete utils
        }
        else
        {
            resp = null;
        }
        return resp;
    }
    
    private String getSHA256Hash(String data) {
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
        this.nombUsua = "";
    }
    
    public void abri()
    {
        
    }
    
    public void limpForm()
    {
        
    }
    
    public void consWebServ()
    {
        FacesContext facsCtxt = FacesContext.getCurrentInstance();
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        Client client = ClientBuilder.newClient();
        String url = facsCtxt.getExternalContext().getInitParameter("webservices.URL"); //Esta en el web.xml
        url = String.format("%s/%s/%s/%s/%s", url, "consUsua", this.filt, "P", "alum");
        System.out.println(url);
        WebTarget resource = client.target(url);
        Invocation.Builder request = resource.request();
        request.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"));
        Response response = request.get();
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
        {
            this.objeWebServ = response.readEntity(WSconsUsua.class); //La respuesta de captura en un pojo que esta en el paquete utils
            for(DatosUsuarios temp : this.objeWebServ.getResu())
            {
                System.out.println(temp.getNomb());
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
