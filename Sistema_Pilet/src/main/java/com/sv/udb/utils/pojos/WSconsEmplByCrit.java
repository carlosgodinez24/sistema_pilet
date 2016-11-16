/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils.pojos;

import java.util.List;

/**
 *
 * @author Alvin
 */
public class WSconsEmplByCrit {
    private boolean resp;
    private List<DatosDocentesByCrit> resu;
    
    public WSconsEmplByCrit() {
        
    }

    public WSconsEmplByCrit(boolean resp, List<DatosDocentesByCrit> resu) {
        this.resp = resp;
        this.resu = resu;
    }
    
    public boolean isResp() {
        return resp;
    }

    public void setResp(boolean resp) {
        this.resp = resp;
    }

    public List<DatosDocentesByCrit> getResu() {
        return resu;
    }

    public void setResu(List<DatosDocentesByCrit> resu) {
        this.resu = resu;
    }       
}
