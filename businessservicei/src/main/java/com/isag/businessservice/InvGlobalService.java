/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.define.CompanyActivityClassEnum;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

@Remote
public interface InvGlobalService {

    public InvDetailDTO calculateDetailTotalsRow(InvDetailDTO detailEntity, InvInvoiceDTO invPurchaseInvoiceDTO, Boolean isNeeadQuantity);

    public InvInvoiceDTO updateSummition(InvInvoiceDTO invPurchaseInvoiceDTO);

    public InvInvoiceDTO recalcHeadValues(InvInvoiceDTO invPurchaseInvoiceDTO);
    
    public InvInvoiceDTO addInvPurchaseInvoiceDetail(InvInvoiceDTO invPurchaseInvoiceDTO, Integer index);
    
    public String validateSave(List<InvDetailDTO> invPurchaseInvoiceDetailDTOList, Integer invtentortId, ScreenNameClassEnum screenNameClassEnum,
            Boolean negativeSell, Boolean isload, Boolean sellBuy,String invoiceSupplierNumber);
    
    public Boolean validatePrice(InvDetailDTO detailDTO);
    
    public Boolean validateActualQuantity(InvDetailDTO detailDTO);
    
    public Boolean validateQuantity(InvDetailDTO detailDTO);
    
    public List<InvDetailDTO> updatePriceAndQuantityForDetail(List<InvDetailDTO> invDetailDTOList,
            ScreenNameClassEnum screenNameClassEnum);
    
    public InvDetailDTO validateItems(InvDetailDTO detailDTO, Integer inventoryId, 
            ScreenNameClassEnum screenNameClassEnum, Boolean sellBuy, Boolean sellNegative, Boolean isEdit,CompanyActivityClassEnum companyActivityClassEnum);
    
    public List<InvDetailDTO> AvailableQuantity(List<InvDetailDTO> detailDTOList, InvInvoiceDTO invInvoiceDTO) ;
    
    public InvDetailDTO updateAvailableQuantityRow(ScreenNameClassEnum screenNameClassEnum, InvDetailDTO detailDTO, Integer inventoryId, Boolean sellBuy, Boolean sellNegative, Boolean isEdit);
    
    public InvDetailDTO validatePriceColumn(InvDetailDTO detailDTO);
    
    public InvDetailDTO validateActualQuantityColumn(InvDetailDTO detailDTO);
    
    public InvDetailDTO validateQuantityColum(InvDetailDTO detailDTO, Boolean isload, ScreenNameClassEnum screenNameClassEnum,Boolean negativeSell,CompanyActivityClassEnum companyActivityClassEnum) ;
    
    public InvDetailDTO validateDiscountColumn(InvDetailDTO detailDTO);
    
    public InvDetailDTO validateNetColumn(InvDetailDTO detailDTO);
    
    public Boolean validateNet(InvDetailDTO detailDTO);
    
    public InvDetailDTO fillItemDetail(InvDetailDTO dTO, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap,
            Integer id,Integer serial,Integer branchId,BigDecimal cost,BigDecimal discount,BigDecimal quantity,Integer discountType,Integer invInvoiceId,
            BigDecimal extraCost,BigDecimal net,Integer index,Date createdDate,Integer createdBy,
            String itemBarcode, BigDecimal screwing, Integer itemId, BigDecimal weight, Integer unitId,
            Integer unitSerial,String unitName,Integer inventoryItem,BigDecimal actualQuantity,BigDecimal oldQuantity, 
            BigDecimal add1, BigDecimal add2, BigDecimal add3, BigDecimal disc1, BigDecimal disc2, BigDecimal disc3
            , BigDecimal totalBeforefracture , BigDecimal fractureNumber , BigDecimal valuefracture,BigDecimal cartonsNumber,BigDecimal cartonsCapacity,BigDecimal addTotal,BigDecimal discountValue);

}
