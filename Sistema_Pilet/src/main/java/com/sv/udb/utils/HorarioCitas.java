/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils;

import com.sv.udb.modelo.Horariodisponible;

/**
 *
 * @author Alvin
 */
public class HorarioCitas {
    private String fecha;
    private String horaInic;
    private String horaFina;
    private Horariodisponible objeHoraDisp;
    
    public HorarioCitas()
    {
        
    }

    public HorarioCitas(String fecha, String horaInic, String horaFina, Horariodisponible objeHoraDisp) {
        this.fecha = fecha;
        this.horaInic = horaInic;
        this.horaFina = horaFina;
        this.objeHoraDisp=objeHoraDisp;
    }    

    public Horariodisponible getObjeHoraDisp() {
        return objeHoraDisp;
    }

    public void setObjeHoraDisp(Horariodisponible objeHoraDisp) {
        this.objeHoraDisp = objeHoraDisp;
    }

    
    
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInic() {
        return horaInic;
    }

    public void setHoraInic(String horaInic) {
        this.horaInic = horaInic;
    }

    public String getHoraFina() {
        return horaFina;
    }

    public void setHoraFina(String horaFina) {
        this.horaFina = horaFina;
    }
    
    
}
