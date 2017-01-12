/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Owner
 */
@Entity
@Table(name = "grado", catalog = "sistemas_pilet", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grado.findAll", query = "SELECT g FROM Grado g"),
    @NamedQuery(name = "Grado.findByCodiGrad", query = "SELECT g FROM Grado g WHERE g.codiGrad = :codiGrad"),
    @NamedQuery(name = "Grado.findByNombGrad", query = "SELECT g FROM Grado g WHERE g.nombGrad = :nombGrad"),
    @NamedQuery(name = "Grado.findByMensGrad", query = "SELECT g FROM Grado g WHERE g.mensGrad = :mensGrad"),
    @NamedQuery(name = "Grado.findByEstaGrad", query = "SELECT g FROM Grado g WHERE g.estaGrad = :estaGrad"),
    @NamedQuery(name = "Grado.findByMatrGrad", query = "SELECT g FROM Grado g WHERE g.matrGrad = :matrGrad")})
public class Grado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codi_grad")
    private Integer codiGrad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nomb_grad")
    private String nombGrad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "mens_grad")
    private BigDecimal mensGrad;
    @Column(name = "esta_grad")
    private Integer estaGrad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "matr_grad")
    private BigDecimal matrGrad;

    public Grado() {
    }

    public Grado(Integer codiGrad) {
        this.codiGrad = codiGrad;
    }

    public Grado(Integer codiGrad, String nombGrad, BigDecimal mensGrad, BigDecimal matrGrad) {
        this.codiGrad = codiGrad;
        this.nombGrad = nombGrad;
        this.mensGrad = mensGrad;
        this.matrGrad = matrGrad;
    }

    public Integer getCodiGrad() {
        return codiGrad;
    }

    public void setCodiGrad(Integer codiGrad) {
        this.codiGrad = codiGrad;
    }

    public String getNombGrad() {
        return nombGrad;
    }

    public void setNombGrad(String nombGrad) {
        this.nombGrad = nombGrad;
    }

    public BigDecimal getMensGrad() {
        return mensGrad;
    }

    public void setMensGrad(BigDecimal mensGrad) {
        this.mensGrad = mensGrad;
    }

    public Integer getEstaGrad() {
        return estaGrad;
    }

    public void setEstaGrad(Integer estaGrad) {
        this.estaGrad = estaGrad;
    }

    public BigDecimal getMatrGrad() {
        return matrGrad;
    }

    public void setMatrGrad(BigDecimal matrGrad) {
        this.matrGrad = matrGrad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codiGrad != null ? codiGrad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grado)) {
            return false;
        }
        Grado other = (Grado) object;
        if ((this.codiGrad == null && other.codiGrad != null) || (this.codiGrad != null && !this.codiGrad.equals(other.codiGrad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.udb.modelo.Grado[ codiGrad=" + codiGrad + " ]";
    }
    
}
