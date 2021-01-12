/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.views.PostRestrictionsView;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface postRestrictionsViewService {
    public List<PostRestrictionsView> getPostRestrictionByScreenCode(Integer branchId ,Date dateFrom , Date dateTo, Integer DocumentId ,Integer postFlag);
    
    public  List<PostRestrictionsView> getTransactionsIdForUpdatePostFlag(Integer BankIdDebitAccount, Date documentDate,Long  screenCode);

    public List<PostRestrictionsView> getBankTransactions(Integer branchId, Date dateFrom, Date dateTo, Integer DocumentId, Integer postFlag);

}
