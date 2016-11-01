/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.ejb;

import com.sv.udb.modelo.UsuarioRol;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Adonay
 */
@Stateless
public class UsuarioRolFacade extends AbstractFacade<UsuarioRol> implements UsuarioRolFacadeLocal {

    @PersistenceContext(unitName = "PILETPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioRolFacade() {
        super(UsuarioRol.class);
    }
    
}
