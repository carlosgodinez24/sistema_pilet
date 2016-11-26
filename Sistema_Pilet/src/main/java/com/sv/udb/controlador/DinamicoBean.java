/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.DetalleFacadeLocal;
import com.sv.udb.ejb.OpcionFacadeLocal;
import com.sv.udb.ejb.PreguntaFacadeLocal;
import com.sv.udb.ejb.RespuestaFacadeLocal;
import com.sv.udb.ejb.SeccionFacadeLocal;
import com.sv.udb.modelo.Opcion;
import com.sv.udb.modelo.OpcionRespuesta;
import com.sv.udb.modelo.Pregunta;
import com.sv.udb.modelo.Respuesta;
import com.sv.udb.modelo.Seccion;
import com.sv.udb.modelo.SolicitudBeca;
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
    
    @PostConstruct
    public void init()
    {
        try {
            
            this.listCmps = new ArrayList<>();
            this.mapa = new HashMap<>();
            //Agrega un elemento
            consTodo();
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
        
       
//        for (int i = 0; i <this.listOpci.size() ; i++) {
//             Opcion opci =this.listOpci.get(i);
//             this.mapa.put("elem"+(i+1), null);             
//            this.listCmps.add(new DynamicField(opci.getTituOpci(), "elem"+(i+1), this.listOpci.get(i), opci.getCodiEstr().getTipoEstr()));
//        }
        
        /*
        this.mapa.put("elem1", "");
        this.listCmps.add(new DynamicField("Label elemento 1", "elem1", null, "TEXT"));
        //Agrega un segundo elemento
        this.mapa.put("elem2", "");
        this.listCmps.add(new DynamicField("Label elemento 2", "elem2", null, "TEXT"));
        //Agrega un tercer elemento
        //Para select
        Map<Object, Object> opciones = new HashMap<>();
        opciones.put(1, "opción 1");
        opciones.put(2, "opción 2");
        opciones.put(3, "opción 3");
        this.mapa.put("elem3", null);
        this.listCmps.add(new DynamicField("Label elemento 3", "elem3", opciones, "SELECT"));
        //Agrega un tercer elemento
        //Para select
        Map<Object, Object> opcionesCB = new HashMap<>();
        opcionesCB.put(1, "opción 1");
        opcionesCB.put(2, "opción 2");
        opcionesCB.put(3, "opción 3");
        opcionesCB.put(4, "opción 4");
        opcionesCB.put(5, "opción 5");
        this.mapa.put("elem4", null);
        this.listCmps.add(new DynamicField("Label elemento 4", "elem4", opcionesCB, "SELECTMANYCHECKBOX"));*/

        
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
        BecasBean objeBeca = new BecasBean();
        objeBeca.setCarnet(logiBean.getObjeUsua().getAcceUsua());
        System.out.println("Codigo de usuario: "+logiBean.getObjeUsua().getAcceUsua());   
        objeBeca.consW();       
             
        
        
        
        
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
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
                        System.out.println("Guarar: "+codigoOpcionRespuesta);
                        respArray = respArray + "-" + tempResp;
                        Respuesta respuesta = new Respuesta(new SolicitudBeca(1),new Opcion(codiDinaDb),new OpcionRespuesta(codigoOpcionRespuesta),1);
                        respuesta.setDescOpci("S/R");
                        FCDEResp.create(respuesta);
                    }
                    respArray = respArray.trim();
                    valor = "id: " + codiDinaDb + " === valor: " + respArray;
                }
                else
                {
                    String valorDb = this.mapa.get(temp.getFieldKey()).toString();                    
                    Respuesta respuesta = new Respuesta(new SolicitudBeca(1),new Opcion(codiDinaDb),valorDb,1);
                    FCDEResp.create(respuesta);
                    valor = "id: " + codiDinaDb + " === valor: " + this.mapa.get(temp.getFieldKey());
                }
                System.err.println(valor);
            }
//              for (int i = 0; i <this.listOpci.size() ; i++) {                 
//                  
//                  String valor1 = (String)this.mapa.get("elem"+(i+1));
//                  System.out.println(valor1);
//                  
//                  /*Object[] valor4 = (Object[])this.mapa.get("elem"+(i+1));
//                   System.out.println(valor4[0]);*/
//                }
            
            /*
            String valor1 = (String)this.mapa.get("elem1");
            String valor2 = (String)this.mapa.get("elem2");
            String valor3 = (String)this.mapa.get("elem3");
            Object[] valor4 = (Object[])this.mapa.get("elem4");
            
            System.out.println(valor1);
            System.out.println(valor2);
            System.out.println(valor3);
            
            
            
           
*/
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");

        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            System.out.println("Error: "+ex.getMessage());
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
               
        
//        HtmlForm form = (HtmlForm) event.getComponent();
//        form.getChildren().add(this.createUIOutput("<div class=\"panel panel-default\">"));
//        form.getChildren().add(this.createUIOutput(" <div class=\"panel-body\">"));         
//        form.getChildren().add(this.createUIOutput("<fieldset>"));
//
//        for (DynamicField field : this.listCmps) //Recorre los elementos
//        {
//            form.getChildren().add(this.createUIOutput("<div class=\"form-group input-group-xs\">"));
//            switch (field.getType())
//            {
//                case "TEXT":
//                    //Crea el label
//                    form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
//                    //Crea el input
//                    form.getChildren().add(this.getUIComponent(field, HtmlInputText.COMPONENT_TYPE));
//                    break;
//                case "SELECT":
//                    //Crea el label
//                    form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
//                    //Crea el select
//                    form.getChildren().add(this.getUIComponent(field, HtmlSelectOneMenu.COMPONENT_TYPE));
//                    break;
//                case "SELECTMANYCHECKBOX":
//                    //Crea el label
//                    form.getChildren().add(this.getUIComponent(field, HtmlOutputLabel.COMPONENT_TYPE));
//                    //Crea el select
//                    form.getChildren().add(this.getUIComponent(field, HtmlSelectManyCheckbox.COMPONENT_TYPE));
//                    break;
//            }
//            form.getChildren().add(this.createUIOutput("</div>"));
//        }
//        form.getChildren().add(this.createUIOutput("</fieldset>"));
//        form.getChildren().add(this.createUIOutput("</div>"));
//        form.getChildren().add(this.createUIOutput("</div>"));
//        //Agregar los botones
//        UIComponent btonGroup = this.getUIButtons(form);
//        if(btonGroup != null)
//        {
//            form.getChildren().add(btonGroup);
//        }
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
}
