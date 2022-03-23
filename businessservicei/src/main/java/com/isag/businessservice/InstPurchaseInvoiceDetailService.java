/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InstPurchaseInvoiceDetailDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface InstPurchaseInvoiceDetailService {

    public InstPurchaseInvoiceDetailDTO addInstPurchaseInvoiceDetail(InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTO, IsagUser isagUser);

    public InstPurchaseInvoiceDetailDTO findbyInstPurchaseInvoiceDetailId(Integer InstPurchaseInvoiceDetailId);

    public List<InstPurchaseInvoiceDetailDTO> findbyInstPurchaseInvoiceDetailList(Integer InstPurchaseInvoice, Integer branchId);

    public void deleteByInstPurchaseInvoiceDetailId(Integer InstPurchaseInvoiceDetailId);

    public void deleteByInstPurchaseInvoiceId(Integer InstPurchaseInvoiceId);

}
