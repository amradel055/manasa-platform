/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.define.CompanyActivityClassEnum;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.views.ItemsBarcodesDetailsView;
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
            Boolean negativeSell, Boolean isload, Boolean sellBuy, CompanyActivityClassEnum companyActivityClassEnum);
    
    public Boolean validatePrice(InvDetailDTO detailDTO);
    
    public Boolean validateQuantity(InvDetailDTO detailDTO, CompanyActivityClassEnum companyActivityClassEnum,ScreenNameClassEnum screenNameClassEnum);
    
    public List<InvDetailDTO> updatePriceAndQuantityForDetail(Integer priceType, List<InvDetailDTO> invDetailDTOList,
            ScreenNameClassEnum screenNameClassEnum, CompanyActivityClassEnum activityClassEnum);
    
    public InvDetailDTO validateItems(InvDetailDTO detailDTO, Integer inventoryId, 
            ScreenNameClassEnum screenNameClassEnum, Boolean sellBuy,
            Integer priceType, CompanyActivityClassEnum activityClassEnum
            , Boolean sellNegative, Boolean isEdit);
    
    public InvDetailDTO updateAvailableQuantityRow(ScreenNameClassEnum screenNameClassEnum, InvDetailDTO detailDTO, Integer inventoryId, Boolean sellBuy, Boolean sellNegative, Boolean isEdit);
    
    public InvDetailDTO validatePriceColumn(InvDetailDTO detailDTO);
    
    public InvDetailDTO validateQuantityOfUnitColum(InvDetailDTO detailDTO, InvInvoiceDTO invInvoiceDTO);
    
    public InvDetailDTO validateQuantityColum(InvDetailDTO detailDTO, Boolean isload, ScreenNameClassEnum screenNameClassEnum,Boolean negativeSell, CompanyActivityClassEnum companyActivityClassEnum) ;
    
    public InvDetailDTO validateDiscountColumn(InvDetailDTO detailDTO); 
    
    public InvDetailDTO fillItemDetail(InvDetailDTO dTO, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap,
            Integer id,Integer serial,Integer branchId,BigDecimal cost,BigDecimal discount,BigDecimal quantity,Integer discountType,Integer invInvoiceId,
            BigDecimal extraCost,BigDecimal net,Integer index,Date createdDate,Integer createdBy,
            Integer clothNumber,Integer idParent, String itemBarcodeParent, String itemNameParent,Integer proof,
            String itemBarcode, BigDecimal screwing, Integer itemId, BigDecimal weight, 
            Integer unitId,Integer unitSerial,String unitName,Integer inventoryItem, BigDecimal number, BigDecimal quantityOfOneUnit, 
            InvInventory inventoryDTO,InvItem invItem
            ,CompanyActivityClassEnum companyActivityClassEnum,ScreenNameClassEnum screenNameClassEnum);

    public Date sumDateToDueDate(Date date, Date dueDate, Integer duePeroid);
    
    public Integer subtractDatefromDueDate(Date date, Date dueDate, Integer duePeroid);
}
