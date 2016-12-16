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
public class WSconsFamAlum {
    private boolean resp;
    private List<DatosFamilia> fami;
    
    public WSconsFamAlum()
    {
        
    }

    public boolean isResp() {
        return resp;
    }

    public void setResp(boolean resp) {
        this.resp = resp;
    }

    public List<DatosFamilia> getFami() {
        return fami;
    }

    public void setFami(List<DatosFamilia> fami) {
        this.fami = fami;
    }    
}
