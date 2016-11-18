/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.PermisoRol;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Adonay
 */
@Stateless
public class PermisoRolFacade extends AbstractFacade<PermisoRol> implements PermisoRolFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermisoRolFacade() {
        super(PermisoRol.class);
    }
 
    @Override
    public PermisoRol findByPermAndRole(Object perm, Object codiRole) {
        Query q = getEntityManager().createQuery("SELECT u FROM PermisoRol u WHERE u.codiPerm = :codiPerm AND u.codiRole = :codiRole", PermisoRol.class);        
        q.setParameter("codiPerm", perm);
        q.setParameter("codiRole", codiRole);
        List resu = q.getResultList();
        return resu.isEmpty() ? null : (PermisoRol)resu.get(0);
    }
}
