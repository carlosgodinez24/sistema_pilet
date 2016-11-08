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
public class DatosDocentes {
    private String codi;
    private String nomb;
    private byte[] foto;
    private String secc;
    
    public DatosDocentes()
    {
    
    }

    public DatosDocentes(String codi, String nomb, byte[] foto, String secc) {
        this.codi = codi;
        this.nomb = nomb;
        this.foto = foto;
        this.secc = secc;
    }
    
    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }

    public String getNomb() {
        return nomb;
    }

    public void setNomb(String nomb) {
        this.nomb = nomb;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getSecc() {
        return secc;
    }

    public void setSecc(String secc) {
        this.secc = secc;
    }
    
    
}
