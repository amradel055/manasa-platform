/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invPurchaseOrder;

import com.isag.businessservice.InvPurchaseOrderService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entiy.InvPurchaseOrderDetailEntity;
import com.isag.entiy.InvPurchaseOrderEntity;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import com.isag.views.ItemsBarcodesDetailsView;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq002
 */
@Named(value = "invPurchaseOrderListMB")
@ViewScoped
public class invPurchaseOrderListMB extends BaseListBean implements Serializable {

    private List<InvPurchaseOrder> invPurchaseOrders = new ArrayList<>();
    private InvPurchaseOrder invPurchaseOrder;
    private UserData userData;
    private Integer invPurchaseOrderSelected;
    private List<InvPurchaseOrderEntity> invPurchaseOrderEntitys;
    private InvPurchaseOrderEntity invPurchaseOrderEntity;
    private InvPurchaseOrderEntity invPurchaseOrderEntitySelected;
    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;

    private InvPurchaseOrderParent invPurchaseOrderParent;

    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;

    @Override
    @PostConstruct
    public void init() {
        try {
        invPurchaseOrderEntitys = new ArrayList<>();
        invPurchaseOrderEntity = new InvPurchaseOrderEntity();
        invPurchaseOrder = new InvPurchaseOrder();
        load();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderListMB", "init"); 
        }
    }

    @Override
    public void load() {
        try {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        invPurchaseOrders = invPurchaseOrderService.getALLPurchaseOrderByBranchId(userData.getUserBranch().getId());
        for (InvPurchaseOrder purchaseMapper : invPurchaseOrders) {
            invPurchaseOrderEntity = new InvPurchaseOrderEntity();
            invPurchaseOrderEntity.setId(purchaseMapper.getId());
            invPurchaseOrderEntity.setSerial(purchaseMapper.getSerial());
            invPurchaseOrderEntity.setRemarks(purchaseMapper.getRemarks());
            invPurchaseOrderEntity.setDate(purchaseMapper.getDate());
            invPurchaseOrderEntity.setSupplierReference(purchaseMapper.getSupplierReference());
            invPurchaseOrderEntity.setDelegateId(purchaseMapper.getDelegatorId());
            invPurchaseOrderEntity.setSupplierId(purchaseMapper.getSupplierId());

            invPurchaseOrderEntitys.add(invPurchaseOrderEntity);

            invPurchaseOrder = purchaseMapper;

        }
         } catch (Exception e) {
            saveError(e, "invPurchaseOrderListMB", "load"); 
        }
    }

    @Override
    public void delete() {
        try {

        Map<String, String> userDDs = userData.getUserDDs();
        if (invPurchaseOrder != null) {
//            Integer InvPurchaseOrderIdToPass = invPurchaseOrderEntity.getId();
            invPurchaseOrder.setId(invPurchaseOrderEntity.getId());
            try {
                invPurchaseOrderService.deletePurchaseOrder(invPurchaseOrderEntity.getId());
                getInvPurchaseOrderEntitys().remove(invPurchaseOrderEntity);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        }
         } catch (Exception e) {
            saveError(e, "invPurchaseOrderListMB", "delete"); 
        }
    }

    @Override
    public void filter() {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "Add");
        exit("../invPurchaseOrder/invPurchaseOrderForm.xhtml");
        return "";
         } catch (Exception e) {
            saveError(e, "invPurchaseOrderListMB", "goToAdd"); 
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
        if (invPurchaseOrderSelected != null && invPurchaseOrderSelected > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("invPurchaseOrderSelected", invPurchaseOrderSelected);
            context.getSessionMap().put("ScreenMode", "Edit");
            exit("../invPurchaseOrder/invPurchaseOrderForm.xhtml");
            return "";
        } else {
            return "";
        }
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderListMB", "goToEdit"); 
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void print() {
        try {
        invPurchaseOrder = invPurchaseOrderService.findPurchaseOrderByPurchaseOrderId(invPurchaseOrderEntity.getId());
        List<InvPurchaseOrderDetail> invPurchaseOrderDetailsList = invPurchaseOrderService.getALLPurchaseOrderDetailsByPurchaseOrder(invPurchaseOrderEntity.getId());
//                mapInvPurchaseOrderToInvPurchaseOrderEntity(invPurchaseOrder);
        List<InvPurchaseOrderDetailEntity> invPurchaseOrderDetailEntityList = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO , totalQuatity = BigDecimal.ZERO , totalNet = BigDecimal.ZERO , totalNetAfterDiscount = BigDecimal.ZERO ;
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        getInvPurchaseOrderParent().fillItemMap(itemsBarcodeMap,itemsBarcodesDetailsViewList, getUserData().getUserBranch().getId());
        getInvPurchaseOrderParent().mapInvPurchaseOrderToInvPurchaseOrderEntity(invPurchaseOrder,
                invPurchaseOrderDetailsList,
                invPurchaseOrderEntity,
                invPurchaseOrderDetailEntityList,
                itemsBarcodeMap, total, totalQuatity, totalNet, totalNetAfterDiscount);
        getInvPurchaseOrderParent().print(invPurchaseOrderEntity, invPurchaseOrderDetailEntityList, getUserData());
         } catch (Exception e) {
            saveError(e, "invPurchaseOrderListMB", "print"); 
   
        }
    }

    public List<InvPurchaseOrder> getInvPurchaseOrders() {
        return invPurchaseOrders;
    }

    public void setInvPurchaseOrders(List<InvPurchaseOrder> invPurchaseOrders) {
        this.invPurchaseOrders = invPurchaseOrders;
    }

    public InvPurchaseOrder getInvPurchaseOrder() {
        return invPurchaseOrder;
    }

    public void setInvPurchaseOrder(InvPurchaseOrder invPurchaseOrder) {
        this.invPurchaseOrder = invPurchaseOrder;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Integer getInvPurchaseOrderSelected() {
        return invPurchaseOrderSelected;
    }

    public void setInvPurchaseOrderSelected(Integer invPurchaseOrderSelected) {
        this.invPurchaseOrderSelected = invPurchaseOrderSelected;
    }

    public List<InvPurchaseOrderEntity> getInvPurchaseOrderEntitys() {
        return invPurchaseOrderEntitys;
    }

    public void setInvPurchaseOrderEntitys(List<InvPurchaseOrderEntity> invPurchaseOrderEntitys) {
        this.invPurchaseOrderEntitys = invPurchaseOrderEntitys;
    }

    public InvPurchaseOrderEntity getInvPurchaseOrderEntity() {
        return invPurchaseOrderEntity;
    }

    public void setInvPurchaseOrderEntity(InvPurchaseOrderEntity invPurchaseOrderEntity) {
        this.invPurchaseOrderEntity = invPurchaseOrderEntity;
    }

    public InvPurchaseOrderService getInvPurchaseOrderService() {
        return invPurchaseOrderService;
    }

    public void setInvPurchaseOrderService(InvPurchaseOrderService invPurchaseOrderService) {
        this.invPurchaseOrderService = invPurchaseOrderService;
    }

    /**
     * @return the invPurchaseOrderParent
     */
    public InvPurchaseOrderParent getInvPurchaseOrderParent() {
        if (invPurchaseOrderParent == null) {
            invPurchaseOrderParent = new InvPurchaseOrderParent(itemsBarcodesDetailsViewService);
        }
        return invPurchaseOrderParent;
    }

    /**
     * @param invPurchaseOrderParent the invPurchaseOrderParent to set
     */
    public void setInvPurchaseOrderParent(InvPurchaseOrderParent invPurchaseOrderParent) {
        this.invPurchaseOrderParent = invPurchaseOrderParent;
    }

}
