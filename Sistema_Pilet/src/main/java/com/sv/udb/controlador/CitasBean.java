/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.ejb.AlumnovisitanteFacadeLocal;
import com.sv.udb.ejb.CambiocitaFacadeLocal;
import com.sv.udb.ejb.CitaFacadeLocal;
import com.sv.udb.ejb.ExcepcionhorariodisponibleFacadeLocal;
import com.sv.udb.ejb.HorariodisponibleFacadeLocal;
import com.sv.udb.ejb.VisitanteFacadeLocal;
import com.sv.udb.ejb.VisitantecitaFacadeLocal;
import com.sv.udb.modelo.Alumno;
import com.sv.udb.modelo.Alumnovisitante;
import com.sv.udb.modelo.Cambiocita;
import com.sv.udb.modelo.Cita;
import com.sv.udb.modelo.Horariodisponible;
import com.sv.udb.modelo.Visitante;
import com.sv.udb.modelo.Visitantecita;
import com.sv.udb.utils.pojos.DatosAlumnos;
import com.sv.udb.utils.pojos.WSconsAlumByDoce;
import com.sv.udb.utils.pojos.WSconsDoceByAlum;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.SessionBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 *
 * @author REGISTRO
 */
@Named(value = "citasBean")
@ViewScoped
public class CitasBean implements Serializable{
    
    //Bean Sesion
    @Inject
    private LoginBean logiBean; 
   
    //FACADE
    @EJB
    private VisitantecitaFacadeLocal FCDEVisiCita;
    @EJB
    private CambiocitaFacadeLocal FCDECambCita; 
    @EJB
    private HorariodisponibleFacadeLocal FCDEHoraDisp;
    @EJB
    private CitaFacadeLocal FCDECita;
    @EJB
    private AlumnovisitanteFacadeLocal FCDEAlumnoVisitante;
     @EJB
    private VisitanteFacadeLocal FCDEVisi; 
    @EJB
    private ExcepcionhorariodisponibleFacadeLocal FCDEExceHoraDisp;
    
    //OBJETOS
    private Cita objeCita;
    private Cambiocita objeCambCita;
    private Horariodisponible horaSeleCita;
    private Alumnovisitante alumVisiSelec;
    private Alumnovisitante objeAlumVisi;
    private Visitantecita objeVisiCita = new Visitantecita();
    private Visitante objeVisi;
    
    private WSconsDoceByAlum objeAlumDepe;
    private Cambiocita objeCambCitaDepe;
    //LISTAS
    private List<DatosAlumnos> listAlumnosWS;
    private List<Visitantecita> listVisiCitaRecep;
    private List<Horariodisponible> listHoraDisp;
    private List<Cita> listCitaAlum;
    private List<Cita> listCitaAlumUsua;
    private List<Cita> listCitaVisiUsua;
    private List<Visitante> listVisi;
    private List<Horariodisponible> listHoraDispUsua;
    private WSconsAlumByDoce objeWebServAlumByDoce;
    private List<Alumnovisitante> listVisiTemp;
    private List<Visitantecita> listVisiCitaDepe;
    private List<Cita> listVisiUsua;
    private List<Visitante> listVisiVisiTemp;
    //variables de funcionalidad y lógica de negocio
    private boolean guardar;
    private String motivo;
    private Date fechSoliCita;
    private Date fechSoliCita2;
    private String carnAlum;
    private String FechInic;
    private String FechFina;
    
    //variables para manejar campos de la lógica de negocio
    private boolean confirmar;
    private boolean programar;
    private boolean reprogramar;
    private boolean ignoHoraDisp;
    private boolean isGrouVisi;
    private boolean LugaEven;
    private String nombProf;
    
    //Switch para formularios
    private boolean switFormCita=true;

    public boolean getSwitFormCita() {
        return switFormCita;
    }

    public void setSwitFormCita(boolean switFormCita) {
        this.switFormCita = switFormCita;
    }
    
    public void toggSwitFormCita()
    {
        this.switFormCita = !this.switFormCita;
        /*RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        ctx.execute("INIT_OBJE_TABL()");*/
    }
    
    public CitasBean() {
        
    }
    //Encapsulamientos
    
   
    public List<Alumnovisitante> getListVisiTemp() {
        return listVisiTemp;
    }

    public Visitante getObjeVisi() {
        return objeVisi;
    }

    public void setObjeVisi(Visitante objeVisi) {
        this.objeVisi = objeVisi;
    }

    public Visitantecita getObjeVisiCita() {
        return objeVisiCita;
    }

    public void setObjeVisiCita(Visitantecita objeVisiCita) {
        this.objeVisiCita = objeVisiCita;
    }
    
    public List<Cita> getListCitaAlum() {
        this.consListCitaAlum();
        return listCitaAlum;
    }

    public Date getFechSoliCita() {
        return fechSoliCita;
    }

    public void setFechSoliCita(Date fechSoliCita) {
        this.fechSoliCita = fechSoliCita;
    }
    public Date getFechSoliCita2() {
        return fechSoliCita2;
    }

    public void setFechSoliCita2(Date fechSoliCita2) {
        this.fechSoliCita2 = fechSoliCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Alumnovisitante getAlumVisiSelec() {
        return alumVisiSelec;
    }

    public void setAlumVisiSelec(Alumnovisitante alumVisiSelec) {
        this.alumVisiSelec = alumVisiSelec;
    }
      
    public List<Horariodisponible> getListHoraDisp() {
        return listHoraDisp;
    }

    public void setListHoraDisp(List<Horariodisponible> listHoraDisp) {
        this.listHoraDisp = listHoraDisp;
    }

    public Horariodisponible getHoraSeleCita() {
        return horaSeleCita;
    }

    public void setHoraSeleCita(Horariodisponible horaSeleCita) {
        this.horaSeleCita = horaSeleCita;
    }
        
    public Cita getObjeCita() {
        return objeCita;
    }

    public void setObjeCita(Cita objeCita) {
        this.objeCita = objeCita;
    }

    public List<Visitantecita> getListVisiCitaRecep() {
        consListVisCitaRecep();
        return listVisiCitaRecep;
    }

    
    

    public boolean isGuardar() {
        return guardar;
    }


    public Cambiocita getObjeCambCita() {
        return objeCambCita;
    }

    public void setObjeCambCita(Cambiocita objeCambCita) {
        this.objeCambCita = objeCambCita;
    }

    public List<Visitante> getListVisi() {
        return listVisi;
    }

    public String getCarnAlum() {
        return carnAlum;
    }

    public void setCarnAlum(String carnAlum) {
        this.carnAlum = carnAlum;
    }

    public boolean isConfirmar() {
        return confirmar;
    }

    public void setConfirmar(boolean confirmar) {
        this.confirmar = confirmar;
    }

    public boolean isProgramar() {
        return programar;
    }

    public void setProgramar(boolean programar) {
        this.programar = programar;
    }

    

    public boolean isReprogramar() {
        return reprogramar;
    }

    public void setReprogramar(boolean reprogramar) {
        this.reprogramar = reprogramar;
    }

    public List<Horariodisponible> getListHoraDispUsua() {
        this.consListHoraDispUsua();
        return listHoraDispUsua;
    }

    public Alumnovisitante getObjeAlumVisi() {
        return objeAlumVisi;
    }

    public void setObjeAlumVisi(Alumnovisitante objeAlumVisi) {
        this.objeAlumVisi = objeAlumVisi;
    }

    public List<DatosAlumnos> getListAlumnosWS() {
        consAlumWS();
        return listAlumnosWS;
    }

    public void setListAlumnosWS(List<DatosAlumnos> listAlumnosWS) {
        this.listAlumnosWS = listAlumnosWS;
    }


    private void consAlumWS()
    {
        /*
            26 = Permitir Recibir Solicitud de Citas por parte de sus Alumnos
            27 = Permitir Recibir Solicitud de Citas por parte de cualquier Alumno
            28 = Recibir Citas de Visitantes
        */
        int permCita = 26;
        if(permCita==26)
        {
            this.objeWebServAlumByDoce = new WebServicesBean().consAlumPorDoce(String.valueOf(LoginBean.getCodiEmplSesi()));
            this.listAlumnosWS = this.objeWebServAlumByDoce.getResu();
        }
        else if(permCita==27)
        {
            
        }
        else if(permCita==28)
        {
            
        }        
    }

    public boolean isIgnoHoraDisp() {
        return ignoHoraDisp;
    }

    public void setIgnoHoraDisp(boolean ignoHoraDisp) {
        this.ignoHoraDisp = ignoHoraDisp;
    }

    public String getFechInic() {
        return FechInic;
    }

    public void setFechInic(String FechInic) {
        this.FechInic = FechInic;
    }

    public String getFechFina() {
        return FechFina;
    }

    public void setFechFina(String FechFina) {
        this.FechFina = FechFina;
    }
    
    
    
    public WSconsDoceByAlum getObjeAlumDepe() {
        return objeAlumDepe;
    }

    public void setObjeAlumDepe(WSconsDoceByAlum objeAlumDepe) {
        this.objeAlumDepe = objeAlumDepe;
    }

    

    public Cambiocita getObjeCambCitaDepe() {
        return objeCambCitaDepe;
    }

    public void setObjeCambCitaDepe(Cambiocita objeCambCitaDepe) {
        this.objeCambCitaDepe = objeCambCitaDepe;
    }

    public List<Visitantecita> getListVisiCitaDepe() {
        return listVisiCitaDepe;
    }

    public boolean isIsGrouVisi() {
        return isGrouVisi;
    }

    public void setIsGrouVisi(boolean isGrouVisi) {
        this.isGrouVisi = isGrouVisi;
    }

    public List<Visitante> getListVisiVisiTemp() {
        return listVisiVisiTemp;
    }

    public boolean isLugaEven() {
        return LugaEven;
    }

    public void setLugaEven(boolean LugaEven) {
        this.LugaEven = LugaEven;
    }

    public List<Cita> getListVisiUsua() {
        consListVisiUsua();
        return listVisiUsua;
    }

    public List<Cita> getListCitaAlumUsua() {
        consListCitaAlumUsua();
        return listCitaAlumUsua;
    }

    public List<Cita> getListCitaVisiUsua() {
        consListCitaVisiUsua();
        return listCitaVisiUsua;
    }

    public String getNombProf() {
        return nombProf;
    }

    public void setNombProf(String nombProf) {
        this.nombProf = nombProf;
    }

    

    
        
    
    
    @PostConstruct
    public void init()
    {
        this.limpForm();
    }
    
    
    public void limpForm()
    {
        this.objeCita = new Cita();
        this.objeCita.setCodiUbic(null);
        this.fechSoliCita=null;
        this.fechSoliCita2=null;
        this.guardar = true;
        this.objeVisi = new Visitante();
        this.listVisi = new ArrayList<Visitante>();
        listVisi.clear();
        this.objeVisiCita = new Visitantecita();
        listVisiTemp = new ArrayList<Alumnovisitante>();
        listVisiTemp.clear();
        this.carnAlum = "";
        this.confirmar = false;
        this.reprogramar = false;
        this.programar = true;
        this.switFormCita=true;
        this.ignoHoraDisp = false;
        this.horaSeleCita = null;
        this.motivo=null;
        this.objeCambCita  = new Cambiocita();
        this.listVisiVisiTemp = new ArrayList<Visitante>();
        this.LugaEven = true;
        this.nombProf = null;
    }
    
    
    
      
    public void consHorarios()
    {
        try
        {
            this.listHoraDisp = FCDEHoraDisp.findByCodiUsua(this.objeCita.getCodiUsua());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void consListHoraDispProf(int codi)
    {
        try
        {
            this.listHoraDisp = FCDEHoraDisp.findByCodiUsua(codi);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
    
    /* SECCIÓN DESTINADA A LA PROGRAMACIÓN DE CITAS PARA VISITANTES */
    
    public void consObjeCitaAlum()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeCita = FCDECita.find(codi);
            this.objeCambCita = FCDECambCita.findByCita(objeCita);
            fechSoliCita = objeCambCita.getFechInicCitaNuev();
            this.listVisiTemp = FCDEAlumnoVisitante.findByCarnAlum(logiBean.getObjeUsua().getAcceUsua());//--> Variable session carnet alumno
            alumVisiSelec= listVisiTemp.get(0);
            consListHoraDispProf(objeCita.getCodiUsua());
            this.nombProf = new WebServicesBean().consEmplPorCodi(String.valueOf(objeCita.getCodiUsua())).getNomb();
            estaCita();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Consultada"+ objeCita.getDescCita()+"')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
    
     public void consListCitaAlum()
    {
        try
        {
            this.listCitaAlum = FCDECita.findByCarnAlum(String.valueOf(logiBean.getObjeUsua().getAcceUsua()));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void soliCitaVisi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        if(valiDatoCitaVisi())
        {
            this.objeCita.setTipoCita(1);
            this.objeCita.setTipoVisi(2);
            this.objeCita.setTipoDura(2);
            this.objeCita.setEstaCita(1);
            this.objeCita.setDescCita(this.motivo);
            FCDECita.create(this.objeCita);            
            this.listCitaAlum.add(this.objeCita);
            //Cambiocita objeCambCita = new Cambiocita();
            objeCambCita.setCodiCita(this.objeCita);
            objeCambCita.setFechCambCita(new Date());
            objeCambCita.setFechInicCitaNuev(fechSoliCita);
            objeCambCita.setFechFinCitaNuev(fechSoliCita);
            DateFormat df = new SimpleDateFormat("K:mm:a");
            objeCambCita.setHoraCambCita(df.format(new Date()));
            objeCambCita.setHoraInicCitaNuev(this.getHoraSeleCita().getHoraInicHoraDisp());
            objeCambCita.setHoraFinCitaNuev(this.getHoraSeleCita().getHoraFinaHoraDisp());
            objeCambCita.setMotiCambCita(this.motivo);
            objeCambCita.setEstaCambCita(0);
            FCDECambCita.create(objeCambCita);
            objeVisiCita.setCodiCita(this.objeCita);
            objeVisiCita.setCodiVisi(alumVisiSelec.getCodiVisi());
            objeVisiCita.setCarnAlum(String.valueOf(new LoginBean().getObjeUsua().getAcceUsua()));
            FCDEVisiCita.create(objeVisiCita);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Se ha solicitado la cita, espere por la respuesta.')");
            this.limpForm();
        }
    }
    
    private int getDay(String dia){
        int ndia = 0;
        String dias[] = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};
        for(int i = 0; i < dias.length; i++){
            if(dia.equals(dias[i])){ndia = i+1; break;}
        }
        return ndia;
    }
    
    private boolean valiDatoCitaVisi(){
        boolean val = false;
        RequestContext ctx = RequestContext.getCurrentInstance();
            int diaHoraDisp = getDay(this.horaSeleCita.getDiaHoraDisp());
            int diaExceHoraDisp = this.fechSoliCita.getDay();
            if(diaHoraDisp == diaExceHoraDisp){
                if(this.fechSoliCita.after(new Date()))
                {
                    if(FCDEExceHoraDisp.findByDispHora(horaSeleCita, fechSoliCita)){
                        val = true;
                    }
                    else
                    {
                        ctx.execute("setMessage('MESS_INFO', 'Atención', 'El Empleado Marco una Excepción para este día');");
                    }
                }
                else
                {
                    ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Fecha ya ha pasado');");
                }
            }else{
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Fecha Inválida para el Horario Seleccionado');");
            }
        return val;
    }
    
    
    /* TERMINA SECCIÓN DESTINADA A LA PROGRAMACIÓN DE CITAS PARA VISITANTES */
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*SECCIÓN DESTINADA A LA PROGRAMACIÓN DE REGISTRO DE VISITAS (CITAS DE TIPO 2), PARA LLEVAR CONTROL DE VISITANTES*/
    
    
    
    
    public void consListVisCitaRecep()
    {
        try
        {
            this.listVisiCitaRecep = FCDEVisiCita.findAll();
            List<Cambiocita> listCambCitaTemp = new ArrayList<Cambiocita>();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //tomamos todos los ultimos cambio cita que sean para "hoy"
            for(Visitantecita obj : listVisiCitaRecep){
                Cambiocita camb = FCDECambCita.findByCita(obj.getCodiCita());
                if(df.format(camb.getFechInicCitaNuev()).equals(df.format(new Date())) || df.format(camb.getFechFinCitaNuev()).equals(df.format(new Date()))){
                    listCambCitaTemp.add(camb);
                }
            }
            //eliminamos posibles cambioCita duplicados
            HashSet<Cambiocita> hashSet = new HashSet<Cambiocita>(listCambCitaTemp);
            listCambCitaTemp.clear();
            listCambCitaTemp.addAll(hashSet);
            //limpiamos la lista, y agregamos todos los registros que involucren a los cambio cita de "hoy"
            listVisiCitaRecep.clear();
            for(Cambiocita obj: listCambCitaTemp){
                listVisiCitaRecep.addAll(FCDEVisiCita.findByCodiCita(obj.getCodiCita()));
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void consObjeVisiCitaRecep(){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            limpForm();
            this.objeVisiCita = FCDEVisiCita.find(codi);
            if(objeVisiCita.getCodiCita().getTipoCita() == 1){
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Consultada')");
            }else{
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Visita Consultada')");
            }
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
    
    //1(registrar entrada), 2 (registrar Salida)
    public void cambVisiCitaRecep(int estado){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            listVisiCitaRecep.remove(objeVisiCita);
            objeVisiCita.setEstaVisi(estado);
            
            DateFormat dfh = new SimpleDateFormat("K:mm a");
            switch(estado){
                case 1:
                    objeVisiCita.setFechLlegCita(new Date());
                    objeVisiCita.setHoraLlegCita(dfh.format(new Date()));
                break;
                case 2:
                    objeVisiCita.setFechSaliCita(new Date());
                    objeVisiCita.setHoraSaliCita(dfh.format(new Date()));
                break;
            }
            FCDEVisiCita.edit(objeVisiCita);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cambio Realizado'); $('#ModaFormRegi').modal('hide');");
            listVisiCitaRecep.add(objeVisiCita);
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al realizar la acción')");
            ex.printStackTrace();
        }
    }
    
    
    /*SECCIÓN TERMINADA DE VISITAS (CITAS DE TIPO 2), PARA LLEVAR CONTROL DE VISITANTES*/
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
                                    /*SECCIÓN PARA CITAS (DOCENTE Y PERSONAL ADMIN.)*/
    
    //consultar el ultimo objeto "cambio cita" a partir de un objeto "cita"
    
     //establecer booleanos segun el estado de la cita
    private void estaCita(){
        switch(this.objeCita.getEstaCita()){
            //cuando una cita está por ser programada
            case 0:
                this.reprogramar = false;
                this.confirmar = true;
                this.programar = false;
                break;
            //cuando una cita está solicitada
            case 1:
                this.reprogramar = false;
                this.confirmar = true;
                this.programar = false;
            break;
            //cuando una cita está confirmada
            case 2:
                this.reprogramar = true;
                this.confirmar = false;
                this.programar = false;
            break;
            //cuando una cita está rechazada
            case 3:
                this.reprogramar = true;
                this.confirmar = false;
                this.programar = false;
            break;
        }
    }
     
    //método para retornar parentesco, dependiendo del código se le pase
    public String getParen(Visitante visi){
        String parentesco = "";
        Alumnovisitante obje = consObjeAlumVisi(visi);
        switch(obje.getPareAlumVisi()){
            case 1:
                parentesco = "Padre/Madre";
            break;
            case 2:
                parentesco = "Tio/Tia";
            break;
            case 3:
                parentesco = "Abuelo/Abuela";
            break;
            case 4:
                parentesco = "Hermano/Hermana";
            break;
            case 5:
               parentesco = "Primo/Prima";
            break;
            case 6:
                parentesco = obje.getEspeAlumVisi();
            break;
            
        }
        return parentesco;
    }
    //consultar el ultimo cambio de la cita (para mostrar en la tabla)

    
    //consultar el ultimo cambio de la cita (para mostrar en la tabla)
    public Visitante consObjeVisi(int codi){
        Visitante objecons = null; 
        try
        {
             objecons = FCDEVisi.find(codi);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return objecons;
    }
    
    //consultar un alumno visitante a partir de un visitante y el carné del alumno seleccionado (para mostrar en tabla)
    //se tuvo que haber seleccionado el visitante en combobox primero
    public Alumnovisitante consObjeAlumVisi(Visitante visi){
        try
        {
            this.objeAlumVisi = FCDEAlumnoVisitante.findByAlumVisi(visi, carnAlum);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return objeAlumVisi;
    }
    
    //consultar un onbjeto de tipo alumno de la lista hipotética del web service
    // considerar consultar un solo registro del web service en el futuro....
    public WSconsDoceByAlum consObjeAlumno(String carn){
        return new WebServicesBean().consDocePorAlum(carn);
    }
    
    public WSconsDoceByAlum consDoceAlum(){
        return new WebServicesBean().consDocePorAlum(logiBean.getObjeUsua().getAcceUsua());
    }
    
    //consultar las citas del usuario
    public void consListCitaAlumUsua()
    {
        try
        {
            this.listCitaAlumUsua = FCDECita.findCitaByCodiUsua(LoginBean.getCodiEmplSesi());
            if(this.listCitaAlumUsua == null)this.listCitaAlumUsua = new ArrayList<Cita>();
            List<Cita> listCitaAlumUsuaTemp = new ArrayList<Cita>(); 
            listCitaAlumUsuaTemp.addAll(listCitaAlumUsua);
            for(Cita obje : listCitaAlumUsuaTemp){
                Visitantecita objeAlumVisiTemp = FCDEVisiCita.findByCodiCita(obje).get(0);
                if(objeAlumVisiTemp == null) objeAlumVisiTemp = new Visitantecita();
               if(objeAlumVisiTemp.getCarnAlum() == null && listCitaAlumUsua.contains(obje))listCitaAlumUsua.remove(obje);
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    //consultar las citas del usuario
    public void consListCitaVisiUsua()
    {
        try
        {
            this.listCitaVisiUsua = FCDECita.findCitaByCodiUsua(LoginBean.getCodiEmplSesi());
            if(this.listCitaVisiUsua == null)this.listCitaVisiUsua = new ArrayList<Cita>();
            List<Cita> listCitaVisiUsuaTemp = new ArrayList<Cita>(); 
            listCitaVisiUsuaTemp.addAll(listCitaVisiUsua);
            for(Cita obje : listCitaVisiUsuaTemp){
               Visitantecita objeAlumVisiTemp = FCDEVisiCita.findByCodiCita(obje).get(0);
               if(objeAlumVisiTemp == null) objeAlumVisiTemp = new Visitantecita();
               if(objeAlumVisiTemp.getCarnAlum() != null && listCitaVisiUsua.contains(obje))listCitaVisiUsua.remove(obje);
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
        
    //consultar los encargados de un alumno
    public void consListVisiAlum(){
        try
        {
            if(carnAlum!=null){
                listVisi = FCDEVisi.findByCarnAlum(carnAlum);
            }else{
                listVisi = new ArrayList<Visitante>();
            }
            consVisiCita(true);
            
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }
    
     //consultar los horariso disponibles del usuario 
    private void consListHoraDispUsua(){
        try
        {
            this.listHoraDispUsua = FCDEHoraDisp.findByCodiUsua(LoginBean.getCodiEmplSesi());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    //consultar visitantes de una cita y los guardamos en una lista temporal
    public void consVisiCita(boolean padre){
        try{
            if(objeCita==null)objeCita=new Cita();
            if(padre){
                if(listVisiTemp==null){
                    listVisiTemp =FCDEAlumnoVisitante.findByCita(objeCita);
                    if(listVisiTemp==null)listVisiTemp = new ArrayList<Alumnovisitante>();
                }else{
                    //tomamos registros anteriores en una nueva lista
                    List<Alumnovisitante> listTransac = listVisiTemp; 
                    listVisiTemp=FCDEAlumnoVisitante.findByCita(objeCita);
                    if(listVisiTemp==null )listVisiTemp = new ArrayList<Alumnovisitante>();
                    //agregamos los anteriores a los nuevos
                    listVisiTemp.addAll(listTransac);
                    //..eliminamos repetidos
                    HashSet<Alumnovisitante> hashSet = new HashSet<Alumnovisitante>(listVisiTemp);
                    listVisiTemp.clear();
                    listVisiTemp.addAll(hashSet);
                }
                if(listVisi==null)listVisi=new ArrayList<Visitante>();
                for(Alumnovisitante visi : listVisiTemp){
                    for(Visitante visi2 : listVisi){
                        if(Objects.equals(visi.getCodiVisi(), visi2)){
                            listVisi.remove(visi2);//quitamos los visitantes que ya estan en la cita, del combobox (lista combobox)
                        }
                    }
                }
            }else{
            listVisiVisiTemp = FCDEVisi.findByCita(objeCita);
            if(listVisiVisiTemp == null)listVisiVisiTemp = new ArrayList<Visitante>();
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void consDepeListCitaUsua(Cita cita){
        try
        {
            //consultamos la lista de visitante_cita
            this.listVisiCitaDepe = FCDEVisiCita.findByCodiCita(cita);
            if(listVisiCitaDepe == null)listVisiCitaDepe = new ArrayList<Visitantecita>();
            //consultamos el ultimo objeto cambio_cita
            this.objeCambCitaDepe = FCDECambCita.findByCita(cita);
            if(objeCambCitaDepe==null)objeCambCitaDepe = new Cambiocita();
            //consultamos el alumno 
            System.out.println(listVisiCitaDepe.get(0).getCarnAlum());
            this.objeAlumDepe = consObjeAlumno(listVisiCitaDepe.get(0).getCarnAlum());
            if(objeAlumDepe==null)objeAlumDepe= new WSconsDoceByAlum();
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public Visitantecita consFirtObjeVisiCitaDepe(){
        return listVisiCitaDepe.get(0);
    } 
    //usado para consultar los encargados de un alumno, al seleccionar un alumno desde una tabla
    public void setAlumn(){
        String Carn = String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjeAlum"));
        if(Carn!=null){
            this.carnAlum = Carn;
            consListVisiAlum();
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Encargados Consultados')");
        }
    }
    
    
    //consultar una cita del usuario (de los que estan en la tabla de datos)
    public void consObjeCitaUsua(boolean padre){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            limpForm();
            this.objeCita = FCDECita.find(codi);
            this.objeCambCita = FCDECambCita.findByCita(objeCita);//consultamos el ultimo cambio de cita
            if(objeCambCita == null) objeCambCita = new Cambiocita();
            this.fechSoliCita = objeCambCita.getFechInicCitaNuev();
            listVisiTemp = null;
            consVisiCita(padre);
            this.guardar = false;
            estaCita();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Consultada')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
        
    }
   
    
    //"Agregar un visitante a una lista temporal"
    public void addVisiCita(){
        try
        {
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            if(listVisiTemp == null)listVisiTemp = new ArrayList<Alumnovisitante>();
            consVisiCita(true);//consultamos si ya hay visitantes en la cita
            this.listVisiTemp.add(consObjeAlumVisi(objeVisi));//i agregamos al nuevo
            this.listVisi.remove(objeVisi);
            ctx.execute("setMessage('MESS_INFO', 'Atención', 'Visitante Agregado')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    //quitar un visitante de la lista temporal
    public void dropVisiCita(Alumnovisitante obje){
        try
        {
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            this.listVisiTemp.remove(obje);
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Visitante Eliminado de la Cita')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
    
    
    //confirmar(1), rechazar(2), Reprogramar(3) cita
    public void cambCita(int acci, boolean padre){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        
        try
        {
            switch(acci){
                case 1:
                    objeCita.setEstaCita(2);
                break;
                case 2:
                    objeCita.setEstaCita(3);
                break;
                case 3:{
                    objeCambCita.setFechInicCitaNuev(fechSoliCita);
                    objeCambCita.setFechFinCitaNuev(fechSoliCita);
                    DateFormat df = new SimpleDateFormat("K:mm:a");
                    objeCambCita.setHoraCambCita(df.format(new Date()));
                    if(ignoHoraDisp){
                        objeCambCita.setHoraInicCitaNuev(FechInic);
                        objeCambCita.setHoraFinCitaNuev(FechFina);
                    }else{
                        objeCambCita.setHoraInicCitaNuev(horaSeleCita.getHoraInicHoraDisp());
                        objeCambCita.setHoraFinCitaNuev(horaSeleCita.getHoraFinaHoraDisp());
                    }
                    objeCita.setEstaCita(2);
                }
            }
            if(padre){
                listCitaAlumUsua.remove(objeCita);
            }else{
                listCitaVisiUsua.remove(objeCita);
            }
            objeCambCita.setCodiCita(objeCita);
            objeCambCita.setMotiCambCita(motivo);
            objeCambCita.setEstaCambCita(1);
            objeCambCita.setFechCambCita(new Date());
            DateFormat df = new SimpleDateFormat("K:mm:a");
            objeCambCita.setHoraCambCita(df.format(new Date()));
            FCDECita.edit(objeCita);
            FCDECambCita.create(objeCambCita);
            objeCambCita.setCodiCita(objeCita);
            if(padre){
                listCitaAlumUsua.add(objeCita);
            }else{
                listCitaVisiUsua.add(objeCita);
            }
            switch(acci){
                case 1:
                    ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Confirmada'); $('#ModaFormRegi').modal('hide');");
                    this.limpForm();
                break;
                case 2:
                    ctx.execute("setMessage('MESS_INFO', 'Atención', 'Cita Rechazada, Proceda a Reprogramar');");
                break;
                case 3:{
                    ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Reprogramada'); $('#ModaFormRegi').modal('hide');");
                }
            }
            estaCita();
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al realizar la acción')");
            ex.printStackTrace();
        }
    }
    
    private boolean valiDatoProgVisiUsua(){
        boolean vali = false;
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        if((horaSeleCita != null || ignoHoraDisp) && fechSoliCita!= null && objeCita.getCodiUbic() != null){
            int diaHoraDisp = (horaSeleCita == null)? 0 : getDay(this.horaSeleCita.getDiaHoraDisp());
            int diaExceHoraDisp = this.fechSoliCita.getDay();
            DateFormat formatter = new SimpleDateFormat("hh:mm a");
            if(diaHoraDisp == diaExceHoraDisp || ignoHoraDisp){
                if(listVisiTemp.size() > 0 || listVisiVisiTemp.size() > 0){
                    if(!this.fechSoliCita.before(new Date())){
                        vali = true;
                    }else{
                        ctx.execute("setMessage('MESS_INFO', 'Atención', 'Esta fecha ya pasó, debe solicitar con anticipación');");
                    }
                }else{
                    ctx.execute("setMessage('MESS_INFO', 'Atención', 'Ningun Visitante Agregado a la Cita');");
                }
            }else{
                ctx.execute("setMessage('MESS_INFO', 'Atención', 'El dia de la fecha no coincide con el horario disponible');");
            }
        }else{
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Rellenar Campos');");
        }
        return vali;
    }
    //programar cita, variable "padre" =  "true" -> padre de familia, "padre" = "false-> visitante particular
    public void progCitaUsua(boolean padre){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        
        try
        {
            if(valiDatoProgVisiUsua()){
                objeCita.setEstaCita(2);
                objeCita.setDescCita(motivo);
                objeCita.setTipoCita(1);
                objeCita.setTipoDura(2);
                objeCita.setTipoVisi(2);
                objeCita.setCodiUsua(LoginBean.getCodiEmplSesi());
                //crear el objeto cita
                FCDECita.create(objeCita);

                objeCambCita.setCodiCita(objeCita);
                objeCambCita.setEstaCambCita(1);
                objeCambCita.setFechCambCita(new Date());

                objeCambCita.setFechInicCitaNuev(fechSoliCita);
                objeCambCita.setFechFinCitaNuev(fechSoliCita);
                DateFormat df = new SimpleDateFormat("K:mm:a");
                objeCambCita.setHoraCambCita(df.format(new Date()));
                if(ignoHoraDisp){
                    objeCambCita.setHoraInicCitaNuev(FechInic);
                    objeCambCita.setHoraFinCitaNuev(FechFina);
                }else{
                    objeCambCita.setHoraInicCitaNuev(horaSeleCita.getHoraInicHoraDisp());
                    objeCambCita.setHoraFinCitaNuev(horaSeleCita.getHoraFinaHoraDisp());
                }
                objeCambCita.setEstaCambCita(1);

                //crear el objeto cambio Cita
                FCDECambCita.create(objeCambCita);
                
                if(padre){
                    //registrando visitantes desde lista AlumnoVisitante
                    for(Alumnovisitante visi : listVisiTemp){
                        objeVisiCita.setCodiCita(objeCita);
                        objeVisiCita.setCarnAlum(visi.getCarnAlum());
                        objeVisiCita.setCodiVisi(visi.getCodiVisi());
                        FCDEVisiCita.create(objeVisiCita);

                    }
                }else{
                    //registrando visitantes lista Visitante
                    for(Visitante visi : listVisiVisiTemp){
                        objeVisiCita.setCodiCita(objeCita);
                        objeVisiCita.setCodiVisi(visi);
                        FCDEVisiCita.create(objeVisiCita);

                    }
                }
                
                if(padre){
                    if(this.listCitaAlumUsua ==  null)this.listCitaAlumUsua = new ArrayList<Cita>();
                    this.listCitaAlumUsua.add(objeCita);
                }else{
                    if(this.listCitaVisiUsua ==  null)this.listCitaVisiUsua = new ArrayList<Cita>();
                    this.listCitaVisiUsua.add(objeCita);
                }
                this.limpForm();
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Programada');");
            }
            
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al Programar')");
            ex.printStackTrace();
        }
    }
    
    /*TERMINA SECCIÓN PARA CITAS (DOCENTE Y PERSONAL ADMIN.)*/
    
    
    
    /*SECCIÓN DESTINADA  PARA LA GESTIÓN DE VISITAS*/
    
    //consultar una lisata de visitas por usuario
    public void consListVisiUsua(){
        try{
            listVisiUsua = FCDECita.findVisiByCodiUsua(LoginBean.getCodiEmplSesi());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void consObjeVisiUsua(){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            limpForm();
            this.objeCita = FCDECita.find(codi);
            this.objeCambCita = FCDECambCita.findByCita(objeCita);//consultamos el ultimo cambio de cita
            if(objeCambCita == null) objeCambCita = new Cambiocita();
            this.fechSoliCita = objeCambCita.getFechInicCitaNuev();
            this.fechSoliCita2 = objeCambCita.getFechFinCitaNuev();
            this.FechInic = objeCambCita.getHoraInicCitaNuev();
            this.FechFina = objeCambCita.getHoraFinCitaNuev();
            consVisiCita(false);
            if(listVisiVisiTemp.size() == 1 && objeCita.getCantGrupCita() != null){
                isGrouVisi = true;
            }else {
                isGrouVisi = false;
            }
            if(objeCita.getCodiEven() == null){
                this.LugaEven = true;
            }else{
                this.LugaEven = false;
            }
            
            estaCita();
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Consultada')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
        }
    }
    
    
    
    //usado para consultar un visitante en una cita, al seleccionar un AlumnoVisitante desde una tabla
    public void setVisi(){
        try{
            int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjeVisi"));
            this.objeVisi = FCDEVisi.find(codi);
            if(objeVisi == null) objeVisi = new Visitante();
            if(listVisiVisiTemp == null)listVisiVisiTemp = new ArrayList<Visitante>();
            
                RequestContext ctx = RequestContext.getCurrentInstance();
            if((isGrouVisi && listVisiVisiTemp.size() < 1) || !isGrouVisi){
                listVisiVisiTemp.add(objeVisi);
                //eliminamos posibles cambioCita duplicados
                HashSet<Visitante> hashSet = new HashSet<Visitante>(listVisiVisiTemp);
                listVisiVisiTemp.clear();
                listVisiVisiTemp.addAll(hashSet); 
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Visitante Agregado')");
            }else{
                ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Este grupo ya tiene un encargado')");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    //quitar un visitante de la lista temporal
    public void dropVisiVisi(Visitante obje){
        try
        {
            RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
            this.listVisiVisiTemp.remove(obje);
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Visitante Eliminado de la Cita')");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    
    private boolean valiDatoProgVisi(){
        boolean vali = false;
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
       
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        try
        {
            if(listVisiVisiTemp.size() > 0){
                if((this.fechSoliCita2.after(this.fechSoliCita))||(this.fechSoliCita2.equals(this.fechSoliCita)&&formatter.parse(this.FechFina).after(formatter.parse(this.FechInic)))){
                    vali = true;
                }
                else{
                    ctx.execute("setMessage('MESS_INFO', 'Atención', 'Fecha Final no debe ser antes de la Inicial');");
                }
            }else{
                ctx.execute("setMessage('MESS_INFO', 'Atención', 'No se ha seleccionado ningun visitante');");
            }
        }
        catch(Exception err)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Horas no válidas')");
            return false;
        }
        
        return vali;
    }
    
    public void progVisi(){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        
        try
        {
            if(valiDatoProgVisi()){
                objeCita.setEstaCita(2);
                objeCita.setDescCita(motivo);
                objeCita.setTipoCita(2);
                objeCita.setTipoDura(2);
                objeCita.setTipoVisi(2);
                //crear el objeto cita
                FCDECita.create(objeCita);

                objeCambCita.setCodiCita(objeCita);
                objeCambCita.setEstaCambCita(1);
                objeCambCita.setFechCambCita(new Date());

                objeCambCita.setFechInicCitaNuev(fechSoliCita);
                objeCambCita.setFechFinCitaNuev(fechSoliCita2);
                DateFormat df = new SimpleDateFormat("K:mm:a");
                objeCambCita.setHoraCambCita(df.format(new Date()));
                objeCambCita.setHoraInicCitaNuev(FechInic);
                objeCambCita.setHoraFinCitaNuev(FechFina);
                objeCambCita.setEstaCambCita(1);

                //crear el objeto cambio Cita
                FCDECambCita.create(objeCambCita);
                
                if(objeVisiCita == null)objeVisiCita = new Visitantecita();
                //registrando visitantes Cita
                for(Visitante visi : listVisiVisiTemp){
                    objeVisiCita.setCodiCita(objeCita);
                    objeVisiCita.setCodiVisi(visi);
                    FCDEVisiCita.create(objeVisiCita);
                    
                }
                if(this.listVisiUsua ==  null)this.listVisiUsua = new ArrayList<Cita>();
                this.listVisiUsua.add(objeCita);
                this.limpForm();
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Programada');");
            }
            
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al Programar')");
            ex.printStackTrace();
        }
    }
    //cambios cita 
    public void cambVisi(){
        RequestContext ctx = RequestContext.getCurrentInstance();
        
        try
        {
            objeCita.setEstaCita(2);
            listVisiUsua.remove(objeCita);
            
            DateFormat df = new SimpleDateFormat("K:mm a");
            
            objeCambCita.setFechCambCita(new Date());
            objeCambCita.setFechInicCitaNuev(fechSoliCita);
            objeCambCita.setFechFinCitaNuev(fechSoliCita2);
            objeCambCita.setHoraCambCita(df.format(new Date()));
            objeCambCita.setHoraInicCitaNuev(FechInic);
            objeCambCita.setHoraFinCitaNuev(FechFina);
            objeCambCita.setCodiCita(objeCita);
            objeCambCita.setMotiCambCita(motivo);
            objeCambCita.setEstaCambCita(1);
            FCDECita.edit(objeCita);
            FCDECambCita.create(objeCambCita);
            listVisiUsua.add(objeCita);
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Cita Reprogramada'); $('#ModaFormRegi').modal('hide');");
            estaCita();
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al realizar la acción')");
            ex.printStackTrace();
        }
    }
    
    
    
    
    /*TERMINA SECCIÓN DESTINADA  PARA LA GESTIÓN DE VISITAS*/
    public void prueba(){
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
       
        ctx.execute("setMessage('MESS_SUCC', 'Atención', 'MENSAJE');");
        
    }
}
