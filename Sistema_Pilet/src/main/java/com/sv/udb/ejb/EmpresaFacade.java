/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.Donacion;
import com.sv.udb.modelo.Empresa;
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
public class EmpresaFacade extends AbstractFacade<Empresa> implements EmpresaFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpresaFacade() {
        super(Empresa.class);
    }
    
    @Override
    public List<Empresa> findAllActive()  {        
        String consulta = "select * from empresa where empresa.esta_empr = 1";
        Query q = getEntityManager().createNativeQuery(consulta, Empresa.class);         
                  
        List resu = q.getResultList(); 
        return resu.isEmpty() ? null : resu;
    }
    
    @Override
    public int validar(int codi_empr)
    {
        String consulta = "SELECT COUNT(*) FROM empresa e INNER JOIN solicitud_beca sb ON sb.codi_empr = e.codi_empr INNER JOIN donacion d ON d.codi_empr = e.codi_empr WHERE d.esta_dona = 1 AND sb.esta_soli_beca != 2 AND sb.esta_soli_beca != 3 AND sb.esta_soli_beca != 4 AND e.codi_empr = ?1";
        Query q = getEntityManager().createNativeQuery(consulta);
        q.setParameter(1, codi_empr);
        //Object resu = q.getSingleResult().getClass().getCanonicalName();
        return Integer.parseInt(String.valueOf(q.getSingleResult()));
    }
}
