/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InstPurchaseInvoiceDetailDTO;
import com.toby.entity.TobyUser;
import com.toby.views.ItemsBarcodesDetailsView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface InstPurchaseInvoiceDetailService {

    public InstPurchaseInvoiceDetailDTO addInstPurchaseInvoiceDetail(InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTO, TobyUser tobyUser);

    public InstPurchaseInvoiceDetailDTO findbyInstPurchaseInvoiceDetailId(Integer InstPurchaseInvoiceDetailId);

    public List<InstPurchaseInvoiceDetailDTO> findbyInstPurchaseInvoiceDetailList(Integer InstPurchaseInvoice, Integer branchId);

    public void deleteByInstPurchaseInvoiceDetailId(Integer InstPurchaseInvoiceDetailId);

    public void deleteByInstPurchaseInvoiceId(Integer InstPurchaseInvoiceId);

    public <T> List<ItemsBarcodesDetailsView> findItemBarcodeDetails(List<T> list, Integer branchId);

}
