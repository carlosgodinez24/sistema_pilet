/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.DetalleFacadeLocal;
import com.sv.udb.ejb.GradoFacadeLocal;
import com.sv.udb.ejb.OpcionFacadeLocal;
import com.sv.udb.ejb.PreguntaFacadeLocal;
import com.sv.udb.ejb.RespuestaFacadeLocal;
import com.sv.udb.ejb.SeccionFacadeLocal;
import com.sv.udb.ejb.SolicitudBecaFacadeLocal;
import com.sv.udb.modelo.Empresa;
import com.sv.udb.modelo.Opcion;
import com.sv.udb.modelo.OpcionRespuesta;
import com.sv.udb.modelo.Pregunta;
import com.sv.udb.modelo.Respuesta;
import com.sv.udb.modelo.Seccion;
import com.sv.udb.modelo.SolicitudBeca;
import com.sv.udb.modelo.UsuarioRol;
import com.sv.udb.utils.DynamicField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.annotation.PostConstruct;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author Mauricio
 */
@Named(value = "dinamicoBean")
@ViewScoped
public class DinamicoBean implements Serializable{

    
    @EJB
    private RespuestaFacadeLocal FCDEResp;

    private static final long serialVersionUID = -5196715359527212081L;
    private List<DynamicField> listCmps;
    private Map<String, Object> mapa;
    @EJB
    private OpcionFacadeLocal FCDEOpci;
     @EJB
     private PreguntaFacadeLocal FCDEPreg;
     @EJB
     private SeccionFacadeLocal FCDESecc;
     
     @EJB
    private SolicitudBecaFacadeLocal FCDESoli;
    private List<Opcion> listOpci;
    private List<Pregunta> listPreg;
    private List<Seccion> listSecc;
    
     //Bean de session
    @Inject
    private LoginBean logiBean; 
    
    
    /**
     * Creates a new instance of DinamicoBean
     */
    public DinamicoBean() {
    }

    public Map<String, Object> getMapa() {
        return mapa;
    }

    public void setMapa(Map<String, Object> mapa) {
        this.mapa = mapa;
    }
    @Inject
     private BecasBean objeBecaBean;
    
    @PostConstruct
    public void init()
    {
        try {
             
            this.listCmps = new ArrayList<>();
            this.mapa = new HashMap<>();
            this.VeriRole();
          
            //Agrega un elemento
            consTodo();
            
            if (FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becasBean") != null) {
                objeBecaBean = (BecasBean) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get("becasBean");
            }
            else
            {
                objeBecaBean = new BecasBean();
            }
            
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
    public void consTodo()
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
    
    public void guar()
    {
         RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
       
        try
        {            
            BecasBean objeBecaLoca;
            SolicitudBeca objeSoli = new SolicitudBeca();            
            //if(this.objeBecaBean.getCarnet()!=null)
            //{
                objeBecaLoca = this.objeBecaBean;
                objeSoli = this.objeBecaBean.getObjeSoli();
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
                    Entry<Object, Object> item = (Entry<Object, Object>)entry;
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
                    Entry<Object, Object> item = (Entry<Object, Object>)entry;
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
}
