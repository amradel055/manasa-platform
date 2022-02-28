/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.views.BankTransactionView;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface BankTransactionViewService {
    public List<BankTransactionView> getAllBankTransactions(Integer branchId , Integer bankId , Date dateFrom , Date dateTo);
}
