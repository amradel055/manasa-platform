/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invaddingorder;

import com.isag.businessservice.InvAddingOrderDetailsService;
import com.isag.businessservice.InvAddingOrderService;
import com.isag.businessservice.InvPurchaseOrderDetailsService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.QuantityItemsStoreService;
import com.isag.converter.InvPurchaseOrderDetailConverter;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entity.InventorySetup;
import com.isag.entiy.InvAddingOrderDetailsEntity;
import com.isag.entiy.InvAddingOrderEntity;
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
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author WIN7
 */
@Named(value = "invAddingOrderListMB")
@ViewScoped
public class invAddingOrderListMB extends BaseListBean implements Serializable {

    UserData userData;
    private Integer branchId;
    private Integer companyId;
    private Integer invAddingOrderSelected;
    private InvAddingorder invAddingOrder;
    private List<InvAddingorder> invAddingOrderList;
    private InvAddingOrderEntity invAddingOrderEntitySelected;
    private InvAddingOrderEntity invAddingOrderMappingEntity;
    private List<InvAddingOrderEntity> invAddingOrderEntityList;
    private List<InvAddingorderDetail> invAddingOrderDetailList;
    private String uri;
    private boolean isPermission;
    private InvAddingOrderParent invAddingOrderParent;

    @EJB
    private InvAddingOrderService invAddingOrderService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    private InvAddingOrderDetailsService invAddingorderDetailsService;
    @EJB
    private QuantityItemsStoreService quantityItemsStoreService;
    @EJB
    private InvPurchaseOrderDetailsService invPurchaseOrderDetailsService;

    @Override
    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            initObjs();

            setBranchId(getUserData().getUserBranch().getId());
            setCompanyId(getUserData().getCompany().getId());

            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
            isPermission = !uri.contains("invaddingorder1");

            load();
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            invAddingOrderList = invAddingOrderService.getALLInvAddingOrderByBranchId(branchId, isPermission ?1 : 0);

            for (InvAddingorder invAddingOrder : invAddingOrderList) {
                invAddingOrderMappingEntity = new InvAddingOrderEntity();
                invAddingOrderMappingEntity.setId(invAddingOrder.getId());
                invAddingOrderMappingEntity.setSerial(invAddingOrder.getSerial());
                invAddingOrderMappingEntity.setDate(invAddingOrder.getDate());
                invAddingOrderMappingEntity.setSupplierId(invAddingOrder.getOrganizationSiteId());
                invAddingOrderMappingEntity.setPostFlag(invAddingOrder.getPostFlag());
                invAddingOrderMappingEntity.setSupplierDate(invAddingOrder.getSupplierDate());
                invAddingOrderMappingEntity.setSupplierInvoice(invAddingOrder.getSupplierInvoice());
                invAddingOrderMappingEntity.setPurchaseOrderId(invAddingOrder.getPurchaseOrderId());
                invAddingOrderMappingEntity.setInvInventory(invAddingOrder.getInvInventoryId());

                invAddingOrderEntityList.add(invAddingOrderMappingEntity);
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "load");
        }
    }

    private void initObjs() {
        try {
            invAddingOrder = new InvAddingorder();
            invAddingOrderEntitySelected = new InvAddingOrderEntity();
            invAddingOrderEntityList = new ArrayList<>();
            invAddingOrderList = new ArrayList<>();
            invAddingOrderMappingEntity = new InvAddingOrderEntity();
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "initObjs");
        }
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            if (isPermission) {
                exit("../invpermissionorder/invpermissionorderform.xhtml");
            } else {
                exit("../invaddingorder1/invAddingOrderForm1.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (invAddingOrderSelected != null && invAddingOrderSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invAddingOrderSelected", invAddingOrderSelected);
                context.getSessionMap().put("ScreenMode", "Edit");
                if (isPermission) {
                    exit("../invpermissionorder/invpermissionorderform.xhtml");
                } else {
                    exit("../invaddingorder1/invAddingOrderForm1.xhtml");
                }
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();

            Integer invAddingOrderDeletedId = invAddingOrderEntitySelected.getId() != null ? invAddingOrderEntitySelected.getId() : null;
            invAddingOrder = new InvAddingorder();
            invAddingOrder.setId(invAddingOrderDeletedId);
            try {

                invAddingOrderService.deleteInvAddingOrder(invAddingOrder);
                invAddingOrderEntityList.remove(invAddingOrderEntitySelected);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "delete");
        }
    }

    public void print() {
        try {

            InvAddingorder invAddingOrder = null;
            List<InvAddingorderDetail> invAddingorderDetailList = new ArrayList<>();
            InvAddingOrderEntity invAddingOrderEntity = null;
            InvAddingOrderEntity invAddingOrderEntityRetrieved = null;
            InvAddingOrderDetailsEntity invAddingOrderDetailsEntity = null;
            Boolean isPermission = Boolean.FALSE;
            Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
            BigDecimal totalQuatity = BigDecimal.ZERO;
            List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityList = new ArrayList<>();
            List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityListFirstBackup = new ArrayList<>();
            StringBuilder headIdList = new StringBuilder();
            List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = new ArrayList<>();

            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

            if (uri.contains("invaddingorder1")) {
                isPermission = false;
            } else {
                isPermission = true;
            }

            getInvAddingOrderParent().fillItemBarcodeMap(itemsBarcodesDetailsViewList, itemsBarcodeMap, branchId);

            invAddingOrder = invAddingOrderService.findInvAddingOrderByInvAddingOrderId(invAddingOrderSelected);

            invAddingorderDetailList = invAddingorderDetailsService.getAllInvAddingOrderDetailsByInvAddingOrderId(invAddingOrder.getId());

            invAddingOrderEntity = getInvAddingOrderParent().mapInvAddingOrderToInvAddingOrderEntity(invAddingOrder,
                    invAddingorderDetailList, invAddingOrderEntity,
                    invAddingOrderEntityRetrieved, invAddingOrderDetailsEntity,
                    isPermission, itemsBarcodeMap, totalQuatity, invAddingOrderDetailsEntityList,
                    invAddingOrderDetailsEntityListFirstBackup, headIdList);

            getInvAddingOrderParent().print(invAddingOrderEntity, invAddingOrderDetailsEntityList, getUserData(), isPermission);
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "print");
        }
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public InvAddingorder getInvAddingOrder() {
        return invAddingOrder;
    }

    public void setInvAddingOrder(InvAddingorder invAddingOrder) {
        this.invAddingOrder = invAddingOrder;
    }

    public InvAddingOrderEntity getInvAddingOrderEntitySelected() {
        return invAddingOrderEntitySelected;
    }

    public void setInvAddingOrderEntitySelected(InvAddingOrderEntity invAddingOrderEntitySelected) {
        this.invAddingOrderEntitySelected = invAddingOrderEntitySelected;
    }

    public List<InvAddingOrderEntity> getInvAddingOrderEntityList() {
        return invAddingOrderEntityList;
    }

    public void setInvAddingOrderEntityList(List<InvAddingOrderEntity> invAddingOrderEntityList) {
        this.invAddingOrderEntityList = invAddingOrderEntityList;
    }

    public List<InvAddingorder> getInvAddingOrderList() {
        return invAddingOrderList;
    }

    public void setInvAddingOrderList(List<InvAddingorder> invAddingOrderList) {
        this.invAddingOrderList = invAddingOrderList;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public InvAddingOrderEntity getInvAddingOrderMappingEntity() {
        return invAddingOrderMappingEntity;
    }

    public void setInvAddingOrderMappingEntity(InvAddingOrderEntity invAddingOrderMappingEntity) {
        this.invAddingOrderMappingEntity = invAddingOrderMappingEntity;
    }

    public List<InvAddingorderDetail> getInvAddingOrderDetailList() {
        return invAddingOrderDetailList;
    }

    public void setInvAddingOrderDetailList(List<InvAddingorderDetail> invAddingOrderDetailList) {
        this.invAddingOrderDetailList = invAddingOrderDetailList;
    }

    public Integer getInvAddingOrderSelected() {
        return invAddingOrderSelected;
    }

    public void setInvAddingOrderSelected(Integer invAddingOrderSelected) {
        this.invAddingOrderSelected = invAddingOrderSelected;
    }

    /**
     * @return the invAddingOrderParent
     */
    public InvAddingOrderParent getInvAddingOrderParent() {
        if (invAddingOrderParent == null) {
            invAddingOrderParent = new InvAddingOrderParent(invAddingOrderService, itemsBarcodesDetailsViewService,
                    invAddingorderDetailsService,
                    quantityItemsStoreService,
                    invPurchaseOrderDetailsService);
        }
        return invAddingOrderParent;
    }

    /**
     * @param invAddingOrderParent the invAddingOrderParent to set
     */
    public void setInvAddingOrderParent(InvAddingOrderParent invAddingOrderParent) {
        this.invAddingOrderParent = invAddingOrderParent;
    }
}
