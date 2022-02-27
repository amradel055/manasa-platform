/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.InvReturnPurchaseDetail;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvReturnPurchaseDetailService {

    public void addReturnPurchaseDetail(InvReturnPurchaseDetail details);

    public void deleteReturnPurchaseDetail(InvReturnPurchaseDetail details);

    public InvReturnPurchaseDetail updateReturnPurchaseDetail(InvReturnPurchaseDetail details);

    public InvReturnPurchaseDetail findInvReturnPurchaseDetailListById(Integer invReturnPurchaseDetailId);

    public List<InvReturnPurchaseDetail> getAllReturnPurchaseDetailsByReturnPurchaseId(Integer invReturnPurchaseId);

    public BigDecimal getQuantitySummitionForReturnPurchaseByPurchaseInvoiceDetail(Integer branchId, Integer purchaseInvoiceDetail);
}
