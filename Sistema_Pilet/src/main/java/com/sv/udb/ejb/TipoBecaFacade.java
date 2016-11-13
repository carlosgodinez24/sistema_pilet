/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Empresa;
import com.sv.udb.modelo.TipoBeca;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author eduardo
 */
@Stateless
public class TipoBecaFacade extends AbstractFacade<TipoBeca> implements TipoBecaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoBecaFacade() {
        super(TipoBeca.class);
    }
    @Override
    public List<TipoBeca> findAllActive()  {        
        String consulta = "select * from tipo_beca  where tipo_beca.esta_tipo_beca = 1";
        Query q = getEntityManager().createNativeQuery(consulta, TipoBeca.class);         
                  
        List resu = q.getResultList(); 
        return resu.isEmpty() ? null : resu;
    }
}
