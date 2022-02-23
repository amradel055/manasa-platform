/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.views.InstcontractdetailwithbanktransactionView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface instcontractdetailwithbanktransactionviewService {

    public List<InstcontractdetailwithbanktransactionView> findInstContractDetailByBankTransaction(Integer instContractId);

    public List<Integer> findInstContractDetailByInstContractId(Integer branchId);
}
