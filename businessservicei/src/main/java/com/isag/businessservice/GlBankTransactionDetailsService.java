/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.GlBankTransactionDetail;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface GlBankTransactionDetailsService {

    public void addGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail);

    public void deleteGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail);

    public GlBankTransactionDetail updateGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail);

    public List<GlBankTransactionDetail> getAllGlBankTransactionDetailsByGlBankTransactionId(Integer glBankTransactionId);

    public GlBankTransactionDetail getGlBankTransactionDetailByGlBankTransactionId(Integer glBankTransactionId);

    public GlBankTransactionDetail findGlBankTransactionDetailsById(Integer glBankTransactionDetailsId);

    public GlBankTransactionDetail addOneGlBankTransactionDetail(GlBankTransactionDetail glBankTransactionDetail);

    public Integer finMaxSerial(Integer glBankTransactionId);

    public Boolean checkGlBankIsUsed(Integer glBankId, Integer branchId);
    
    public void addCashDetail(InvInvoiceDTO dTO, Integer glBankTransactionId, IsagUser tobyUser);

}
