/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.utils.pojos;

/**
 *
 * @author Alvin
 */
public class DatosFamilia {
    private String nomb;
    private String apel;
    private String mail;
    private String  pare;
    
    public DatosFamilia()
    {
        
    }

    public String getApel() {
        return apel;
    }

    public void setApel(String apel) {
        this.apel = apel;
    }
    
    public String getNomb() {
        return nomb;
    }

    public void setNomb(String nomb) {
        this.nomb = nomb;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPare() {
        return pare;
    }

    public void setPare(String pare) {
        this.pare = pare;
    }
    
}
