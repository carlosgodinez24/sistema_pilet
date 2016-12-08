/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Respuesta;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author eduardo
 */
@Stateless
public class RespuestaFacade extends AbstractFacade<Respuesta> implements RespuestaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RespuestaFacade() {
        super(Respuesta.class);
    }

    @Override
    public boolean ReadIfCarnExis(String carnet) {
        boolean resp= false;
        String consulta = "select count(1) from respuesta r inner join solicitud_beca s on r.codi_soli_beca = s.codi_soli_beca where s.carn_alum = ?1";
        Query q = getEntityManager().createNativeQuery(consulta);
        q.setParameter(1, carnet);
        int respuesta = Integer.parseInt(String.valueOf(q.getSingleResult()));
        if(respuesta > 1)
        {
            resp = true;
        }
        System.out.println(respuesta);
        return resp;
       
    }
    
}
