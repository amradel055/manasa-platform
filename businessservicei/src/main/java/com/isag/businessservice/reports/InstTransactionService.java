/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.views.InstTransactionTemp;
import com.isag.views.InstTransactionView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface InstTransactionService {

    public List<InstTransactionView> getInstViewBySearchBean(Integer instContactId);
    
    public List<InstTransactionTemp> getInstTransactionTemp(Integer branchId , Integer instContactId);


}
