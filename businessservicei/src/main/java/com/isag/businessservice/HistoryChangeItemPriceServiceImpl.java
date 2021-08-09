/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.HistoryChangeItemPrice;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author elsakr6
 */
@Stateless
public class HistoryChangeItemPriceServiceImpl implements HistoryChangeItemPriceService {

    @EJB
    private GenericDAO dao;
    @EJB
    private HistoryChangeItemPriceService historyChangeItemPriceService;

    @Override
    public synchronized HistoryChangeItemPrice addHistoryChangeItemPriceService(HistoryChangeItemPrice historyChangeItemPrice) {
        dao.saveEntity(historyChangeItemPrice);
        return historyChangeItemPrice;
    }

    @Override
    public synchronized void addListOfHistoryChangeItemPriceService(List<HistoryChangeItemPrice> historyChangeItemPriceList) {
        for (HistoryChangeItemPrice itemPrice : historyChangeItemPriceList) {
            historyChangeItemPriceService.addHistoryChangeItemPriceService(itemPrice);
        }
    }

}
