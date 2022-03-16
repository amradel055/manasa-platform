/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.InvPriceSellItem;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class InvPriceSellItemServiceImpl implements InvPriceSellItemService {

    @EJB
    private GenericDAO dao;

    @Override
    public InvPriceSellItem addInvPriceSellItem(InvPriceSellItem invPriceSellItem) {
        dao.saveEntity(invPriceSellItem);
        return invPriceSellItem;
    }

}
