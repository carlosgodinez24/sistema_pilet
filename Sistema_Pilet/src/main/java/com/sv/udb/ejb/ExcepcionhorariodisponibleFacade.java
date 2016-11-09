/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Excepcionhorariodisponible;
import com.sv.udb.modelo.Horariodisponible;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Kevin
 */
@Stateless
public class ExcepcionhorariodisponibleFacade extends AbstractFacade<Excepcionhorariodisponible> implements ExcepcionhorariodisponibleFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExcepcionhorariodisponibleFacade() {
        super(Excepcionhorariodisponible.class);
    }
    @Override
    public boolean findByDispHora(Horariodisponible hora, Date fech) {
        TypedQuery<Horariodisponible> q = (TypedQuery<Horariodisponible>) getEntityManager().createQuery("SELECT e FROM Horariodisponible h, Excepcionhorariodisponible e WHERE h = :horaDisp and e.codiHoraDisp = h and h.estaHoraDisp = 1 and e.fechExceHoraDisp = :fech");       
        q.setParameter("fech", fech);
        q.setParameter("horaDisp", hora);
        List resu = q.getResultList();
        return resu.isEmpty();
    }
    
}
