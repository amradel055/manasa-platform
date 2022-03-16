/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.views.GetOperationRateView;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class GetOperationRateServiceImpl implements GetOperationRateService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<GetOperationRateView> getAllOperationRate() {
        return dao.findAll(GetOperationRateView.class);
    }

}
