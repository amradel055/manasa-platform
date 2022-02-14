/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InvDetailDTO;
import com.isag.entity.GlYear;
import com.isag.entity.InvInventory;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.IsagUser;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvPurchaseInvoiceDetailsService {

    public List<InvPurchaseInvoiceDetail> getAllInvPurchaseInvoiceDetailByBranchId(Integer branchId);

    public List<InvPurchaseInvoiceDetail> getAllInvPurchaseInvoiceDetailByBranchIdByDate(Integer branchId);

    public void addInvPurchaseInvoiceDetails(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail);

    public void deleteInvPurchaseInvoiceDetails(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail);

    public InvPurchaseInvoiceDetail updateInvPurchaseInvoiceDetails(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail);

    public List<InvPurchaseInvoiceDetail> getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(Integer invPurchaseInvoiceId);

    public List<InvPurchaseInvoiceDetail> getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdWithStatus(Integer invPurchaseInvoiceId);

    public InvPurchaseInvoiceDetail findInvPurchaseInvoiceDetailsById(Integer invPurchaseInvoiceDetailsId);

    public BigDecimal getLastCostItem(Integer itemId, GlYear glYear, Integer branchId);

    public void updateCostAvarage(Integer branchId);

    public void reCalcCostAvarage(Integer branchId);

    public List<InvDetailDTO> getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO dto);

    public List<InvDetailDTO> getAllInvPurchaseInvoiceDetailList(InvInvoiceDTO dto);

    public <T> List<ItemsBarcodesDetailsView> findItemBarcodeDetails(List<T> list, InvInvoiceDTO invInvoiceDTO);

    public void deleteInvPurchaseInvoiceDetailListDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList);

    public String addInvPurchaseInvoiceDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invPurchaseInvoiceDTO, IsagUser isagUser);

    public BigDecimal findLastCost(Integer itemId);

    public BigDecimal FindTotalvalueByDate(List<InvInventory> inventoryIdList, Integer branchId, Integer type);

    public BigDecimal FindTotalvalueByMonth(List<InvInventory> inventoryIdList, Integer branchId, Integer type);

    public Integer FindInvoiceNumberByDate(List<InvInventory> inventoryIdList, Integer branchId, Integer type);

    public Integer FindInvoiceNumberByMonth(List<InvInventory> inventoryIdList, Integer branchId, Integer type) ;

    }
