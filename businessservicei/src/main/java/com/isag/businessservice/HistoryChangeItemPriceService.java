/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.HistoryChangeItemPrice;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author elsakr6
 */
@Remote
public interface HistoryChangeItemPriceService {

    public HistoryChangeItemPrice addHistoryChangeItemPriceService(HistoryChangeItemPrice historyChangeItemPrice);

    public void addListOfHistoryChangeItemPriceService(List< HistoryChangeItemPrice> historyChangeItemPriceList);
}
