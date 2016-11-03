/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils;

/**
 *
 * @author Adonay
 */
public class NotificacionPojo {
    private int usua;
    private String mens; //Descripción
    private boolean leid; //Leido
    private String path;
    private String modu;

    public NotificacionPojo(int usua, String mens, String path, String modu) {
        this.mens = mens;
        this.path = path;
        this.usua = usua;
        this.modu = modu;
    }

    public NotificacionPojo() {
    }

    public String getMens() {
        return mens;
    }

    public void setMens(String mens) {
        this.mens = mens;
    }

    public boolean isLeid() {
        return leid;
    }

    public void setLeid(boolean leid) {
        this.leid = leid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getUsua() {
        return usua;
    }

    public void setUsua(int usua) {
        this.usua = usua;
    }

    public String getModu() {
        return modu;
    }

    public void setModu(String modu) {
        this.modu = modu;
    }

    @Override
    public String toString() {
        return mens;
    }

    
    
}
