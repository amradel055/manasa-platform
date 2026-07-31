/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.manasasecurity.services;

import com.igma.manasasecurity.entities.Security;
import com.isag.core.GenericDAO;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class LoginDataServicesImpl implements LoginDataService {

    @EJB
    private GenericDAO dao;

    @Override
    public Security findData(String name, Integer version) {
        List<Security> securities = null;
        if (name != null && version != null) {
            securities = dao.executeNativeQuery("SELECT * FROM security s WHERE s.name = " + name + " AND s.version =  " + version);
        }

        return securities != null && !securities.isEmpty() ? securities.get(0) : null;
    }

}
