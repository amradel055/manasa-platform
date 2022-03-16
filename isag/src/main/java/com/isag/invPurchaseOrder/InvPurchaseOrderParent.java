/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invPurchaseOrder;

import com.isag.businessservice.InvPurchaseOrderService;
import com.isag.businessservice.InvPurchaseOrderServiceImpl;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entiy.InvPurchaseOrderDetailEntity;
import com.isag.entiy.InvPurchaseOrderEntity;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.views.ItemsBarcodesDetailsView;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author amr
 */
public class InvPurchaseOrderParent implements Serializable {

    BigDecimal hundred = new BigDecimal(100);
        
    private InvPurchaseOrderService invPurchaseOrderService = new InvPurchaseOrderServiceImpl();

    IReportService iReportService = new IReportServiceImpl();
    
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService ;
    
    public InvPurchaseOrderParent(ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService){
        this.itemsBarcodesDetailsViewService = itemsBarcodesDetailsViewService;
    }
      
    public void fillItemMap(Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap,List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList,Integer branchId) {
         itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
            
        }
        
    }

    public void mapInvPurchaseOrderToInvPurchaseOrderEntity(InvPurchaseOrder invPurchaseOrder , 
            List<InvPurchaseOrderDetail> invPurchaseOrderDetailsList,
            InvPurchaseOrderEntity invPurchaseOrderEntity,
            List<InvPurchaseOrderDetailEntity> invPurchaseOrderDetailEntityList, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap,
            BigDecimal total, BigDecimal totalQuatity,BigDecimal totalNet,BigDecimal totalNetAfterDiscount) {

        invPurchaseOrderEntity.setId(invPurchaseOrder.getId());
        invPurchaseOrderEntity.setSerial(invPurchaseOrder.getSerial());
        invPurchaseOrderEntity.setDelegateId(invPurchaseOrder.getDelegatorId());
        invPurchaseOrderEntity.setSupplierId(invPurchaseOrder.getSupplierId());
        invPurchaseOrderEntity.setSupplierReference(invPurchaseOrder.getSupplierReference());
        invPurchaseOrderEntity.setRate(invPurchaseOrder.getRate());
        invPurchaseOrderEntity.setRemarks(invPurchaseOrder.getRemarks());
        invPurchaseOrderEntity.setCurrencyId(invPurchaseOrder.getCurrencyId());
        invPurchaseOrderEntity.setDiscount(invPurchaseOrder.getDiscount());
        invPurchaseOrderEntity.setDiscountType(invPurchaseOrder.getDiscountType() != null ? invPurchaseOrder.getDiscountType() : 0);
        invPurchaseOrderEntity.setDate(invPurchaseOrder.getDate());
        invPurchaseOrderEntity.setCreatedBy(invPurchaseOrder.getCreatedBy());
        invPurchaseOrderEntity.setCreationDate(invPurchaseOrder.getCreationDate());

        
        InvPurchaseOrderDetailEntity invPurchaseOrderDetailEntityIterated;
        for (InvPurchaseOrderDetail detailsEditing : invPurchaseOrderDetailsList) {

            invPurchaseOrderDetailEntityIterated = new InvPurchaseOrderDetailEntity();
            invPurchaseOrderDetailEntityIterated.setDiscount(detailsEditing.getDiscountRate());
            invPurchaseOrderDetailEntityIterated.setId(detailsEditing.getId());
            invPurchaseOrderDetailEntityIterated.setSerial(detailsEditing.getSerial());

            if (detailsEditing.getItemBarcode() != null && !"".equals(detailsEditing.getItemBarcode())) {
                invPurchaseOrderDetailEntityIterated.setItemsBarcodesDetail(itemsBarcodeMap.get(detailsEditing.getItemBarcode()));
                invPurchaseOrderDetailEntityIterated.setItemsBarcodesDetailTrans(invPurchaseOrderDetailEntityIterated.getItemsBarcodesDetail());
                invPurchaseOrderDetailEntityIterated.setScrewing(invPurchaseOrderDetailEntityIterated.getItemsBarcodesDetail().getScrewing());
            }

            if (detailsEditing.getItemId() != null) {
                invPurchaseOrderDetailEntityIterated.setItemId(detailsEditing.getItemId().getId());
                invPurchaseOrderDetailEntityIterated.setItemName(detailsEditing.getItemId().getName());
            } else {
                invPurchaseOrderDetailEntityIterated.setItemId(null);
            }

            if (detailsEditing.getPrice() != null) {
                invPurchaseOrderDetailEntityIterated.setCost(detailsEditing.getPrice());
                total = total.add(detailsEditing.getPrice());
            } else {
                invPurchaseOrderDetailEntityIterated.setCost(null);
            }

            if (detailsEditing.getQuantity() != null) {
                invPurchaseOrderDetailEntityIterated.setQuantity(detailsEditing.getQuantity());
                totalQuatity = totalQuatity.add(detailsEditing.getQuantity());
            } else {
                invPurchaseOrderDetailEntityIterated.setQuantity(null);
            }

            invPurchaseOrderDetailEntityIterated.setTotal(detailsEditing.getTotal());
            invPurchaseOrderDetailEntityIterated.setUnit(detailsEditing.getUnitId());
            invPurchaseOrderDetailEntityIterated.setUnitName(detailsEditing.getUnitId().getName());
            invPurchaseOrderDetailEntityIterated.setColorPaintId((detailsEditing.getItemId().getPaintColor() != null && detailsEditing.getItemId().getPaintColor().getName() != null) ? detailsEditing.getItemId().getPaintColor().getName() : null);
            invPurchaseOrderDetailEntityIterated.setEnamel((detailsEditing.getItemId().getEnamelColor() != null && detailsEditing.getItemId().getEnamelColor().getName() != null) ? detailsEditing.getItemId().getEnamelColor().getName() : null);
            invPurchaseOrderDetailEntityIterated.setStones((detailsEditing.getItemId().getStone() != null && detailsEditing.getItemId().getStone().getName() != null) ? detailsEditing.getItemId().getStone().getName() : null);

            invPurchaseOrderDetailEntityIterated.setCreatedBy(detailsEditing.getCreatedBy());
            invPurchaseOrderDetailEntityIterated.setCreationDate(detailsEditing.getCreationDate());
            invPurchaseOrderDetailEntityIterated.setScrewing(detailsEditing.getScrewing());

            invPurchaseOrderDetailEntityList.add(invPurchaseOrderDetailEntityIterated);

        }
        
        updateSummition(invPurchaseOrderEntity,invPurchaseOrderDetailEntityList , 
            total, totalQuatity,totalNet,totalNetAfterDiscount);
    }
    
    public void updateSummition(InvPurchaseOrderEntity invPurchaseOrderEntity,
            List<InvPurchaseOrderDetailEntity> invPurchaseOrderDetailEntityList , 
            BigDecimal total, BigDecimal totalQuatity,BigDecimal totalNet,BigDecimal totalNetAfterDiscount) {

        BigDecimal quantity, discount, cost, valueAfterDiscount;
        totalQuatity = BigDecimal.ZERO;
        totalNet = BigDecimal.ZERO;

        BigDecimal rate = invPurchaseOrderEntity.getRate() != null ? invPurchaseOrderEntity.getRate() : BigDecimal.ONE;

        for (InvPurchaseOrderDetailEntity detailEntity : invPurchaseOrderDetailEntityList) {
            if (detailEntity.getItemsBarcodesDetail() == null && detailEntity.getItemsBarcodesDetailTrans() != null) {
                detailEntity.setItemsBarcodesDetail(detailEntity.getItemsBarcodesDetailTrans());
            }
            quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
            cost = detailEntity.getCost() != null ? detailEntity.getCost().multiply(rate) : BigDecimal.ZERO;
            discount = detailEntity.getDiscount() != null ? detailEntity.getDiscount() : BigDecimal.ZERO;
            total = quantity.multiply(cost);
            detailEntity.setTotal(total);

            valueAfterDiscount = total.subtract((total.multiply(discount).divide(hundred)));
            detailEntity.setNet(valueAfterDiscount.setScale(2, BigDecimal.ROUND_UP));
            totalQuatity = totalQuatity.add(quantity);
            totalNet = (totalNet.add(detailEntity.getNet())).setScale(2, BigDecimal.ROUND_UP);
        }
        calculateTotalNetAfterDiscount(invPurchaseOrderEntity,invPurchaseOrderDetailEntityList , 
            total, totalQuatity,totalNet,totalNetAfterDiscount);

    }

    public void calculateTotalNetAfterDiscount(InvPurchaseOrderEntity invPurchaseOrderEntity,
            List<InvPurchaseOrderDetailEntity> invPurchaseOrderDetailEntityList , 
            BigDecimal total, BigDecimal totalQuatity,BigDecimal totalNet,BigDecimal totalNetAfterDiscount) {

        totalNetAfterDiscount = BigDecimal.ZERO;
        if (invPurchaseOrderDetailEntityList != null && !invPurchaseOrderDetailEntityList.isEmpty()) {
            if (invPurchaseOrderEntity.getDiscountType() != null && invPurchaseOrderEntity.getDiscountType() == 0) {
                if (totalNet.compareTo(BigDecimal.ZERO) == 1
                        && (totalNet.compareTo(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO) == 0
                        || totalNet.compareTo(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO) == 1)) {
                    totalNetAfterDiscount = (totalNet.subtract(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO)).setScale(2, BigDecimal.ROUND_UP);
                } else {
                    invPurchaseOrderEntity.setDiscount(BigDecimal.ZERO);
                    totalNetAfterDiscount = totalNet.subtract(BigDecimal.ZERO);
                }

            } else {
                if (isDiscountValid(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO)) {
                    totalNetAfterDiscount = totalNet.subtract(totalNet.multiply(invPurchaseOrderEntity.getDiscount() != null ? invPurchaseOrderEntity.getDiscount() : BigDecimal.ZERO).divide(hundred));
                } else {
                    invPurchaseOrderEntity.setDiscount(BigDecimal.ZERO);
                    totalNetAfterDiscount = totalNet.subtract(BigDecimal.ZERO);
                }
            }
        }
    }
    
    private Boolean isDiscountValid(BigDecimal discValue) {
        if (discValue != null) {
            if ((discValue.compareTo(BigDecimal.ZERO) == 0 || discValue.compareTo(BigDecimal.ZERO) == 1)
                    && (discValue.compareTo(hundred) == 0 || hundred.compareTo(discValue) == 1)) {
                return true;
            } else {
                return errorMessage("لا يجب ادخال خصم اعلى من 100% في حالة النسبة");
            }
        } else {
            return false;
        }
    }

    public void print(InvPurchaseOrderEntity invPurchaseOrderEntity,
            List<InvPurchaseOrderDetailEntity> invPurchaseOrderDetailEntityList, UserData userData) {
//        iReportService.printReportInvPurchaseOrder(invPurchaseOrderEntity, invPurchaseOrderDetailEntityList, userData);

    }
    
    public boolean errorMessage(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
        return false;
    }

}
