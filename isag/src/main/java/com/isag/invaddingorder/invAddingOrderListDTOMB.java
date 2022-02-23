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
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entiy.InvAddingOrderDetailsEntity;
import com.isag.entiy.InvAddingOrderEntity;
import com.isag.isag.BaseListBean;
import com.isag.isag.InventoryBasicDataForm;
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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author WIN7
 */
@Named(value = "invAddingOrderListDTOMB")
@ViewScoped
public class invAddingOrderListDTOMB extends InventoryBasicDataForm  {

      private InvInvoiceDTO invInvoiceDTOSelected;
   
   
    
    private String uri;
    private boolean isPermission;
 
    @EJB
    private InvAddingOrderService invAddingOrderService;
//    @EJB
//    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
//    @EJB
//    private InvAddingOrderDetailsService invAddingorderDetailsService;
//    @EJB
//    private QuantityItemsStoreService quantityItemsStoreService;
//    @EJB
//    private InvPurchaseOrderDetailsService invPurchaseOrderDetailsService;

    @Override
    @PostConstruct
    public void init() {
        try { 
            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
            isPermission = !uri.contains("invaddingorder");
            load();
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
             this.setDataModel(new LazyDataModel<InvInvoiceDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<InvInvoiceDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    Integer type = 0;
                    if (!getUri().contains("invAddingOrderList1")) {
                        type = 1;
                    } 
                    setRowCount(invAddingOrderService.getTotalRegistors(type, getUserData().getUserBranch().getId(), filters).intValue());
                    return invAddingOrderService.getALLInvAddingorderDTOByBranchId(type, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters);
                }
            });
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "load");
        }
    }


     public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Addinvorder");
            if (isPermission) {
                exit("../invpermissionorder_1/invpermissionorderform.xhtml");
            } else {
                exit("../invaddingorder/invAddingOrderForm1.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "goToAdd");
            return null;
        }
    }

     public String goToEdit() {
        try {
            if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invAddingOrderSelected", getInvInvoiceDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "Editinvorder");
                if (isPermission) {
                    exit("../invpermissionorder_1/invpermissionorderform.xhtml");
                } else {
                    exit("../invaddingorder/invAddingOrderForm1.xhtml");
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

     public void delete() {
        try { 
        if(getInvInvoiceDTOSelected() != null){
            try { 
                invAddingOrderService.deleteInvAddingOrder(new InvAddingorder(getInvInvoiceDTOSelected().getId()));
                load();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,getUserDDs().get("INFO"), getUserDDs().get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,getUserDDs().get("ERROR"), getUserDDs().get("UNIQE_KEY_ERROR")));
            }
        }
        } catch (Exception e) {
            saveError(e, "invAddingOrderListMB", "delete");
        }
    }

//    public void print() {
//        try {
//
//            InvAddingorder invAddingOrder = null;
//            List<InvAddingorderDetail> invAddingorderDetailList = new ArrayList<>();
//            InvAddingOrderEntity invAddingOrderEntity = null;
//            InvAddingOrderEntity invAddingOrderEntityRetrieved = null;
//            InvAddingOrderDetailsEntity invAddingOrderDetailsEntity = null;
//            Boolean isPermission = Boolean.FALSE;
//            Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
//            BigDecimal totalQuatity = BigDecimal.ZERO;
//            List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityList = new ArrayList<>();
//            List<InvAddingOrderDetailsEntity> invAddingOrderDetailsEntityListFirstBackup = new ArrayList<>();
//            StringBuilder headIdList = new StringBuilder();
//            List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = new ArrayList<>();
//
//            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
//
//            if (uri.contains("invaddingorder1")) {
//                isPermission = true;
//            } else {
//                isPermission = false;
//            }
//
//            getInvAddingOrderParent().fillItemBarcodeMap(itemsBarcodesDetailsViewList, itemsBarcodeMap, branchId);
//
//            invAddingOrder = invAddingOrderService.findInvAddingOrderByInvAddingOrderId(invAddingOrderSelected);
//
//            invAddingorderDetailList = invAddingorderDetailsService.getAllInvAddingOrderDetailsByInvAddingOrderId(invAddingOrder.getId());
//
//            invAddingOrderEntity = getInvAddingOrderParent().mapInvAddingOrderToInvAddingOrderEntity(invAddingOrder,
//                    invAddingorderDetailList, invAddingOrderEntity,
//                    invAddingOrderEntityRetrieved, invAddingOrderDetailsEntity,
//                    !isPermission, itemsBarcodeMap, totalQuatity, invAddingOrderDetailsEntityList,
//                    invAddingOrderDetailsEntityListFirstBackup, headIdList);
//
//            getInvAddingOrderParent().print(invAddingOrderEntity, invAddingOrderDetailsEntityList, getUserData(), !isPermission);
//        } catch (Exception e) {
//            saveError(e, "invAddingOrderListMB", "print");
//        }
//    }

    

    /**
     * @return the invAddingOrderParent
     */
//    public InvAddingOrderParent getInvAddingOrderParent() {
//        if (invAddingOrderParent == null) {
//            invAddingOrderParent = new InvAddingOrderParent(invAddingOrderService, itemsBarcodesDetailsViewService,
//                    invAddingorderDetailsService,
//                    quantityItemsStoreService,
//                    invPurchaseOrderDetailsService);
//        }
//        return invAddingOrderParent;
//    }

     

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String exit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invInvoiceDTOSelected
     */
    public InvInvoiceDTO getInvInvoiceDTOSelected() {
        if(invInvoiceDTOSelected == null){
            invInvoiceDTOSelected = new InvInvoiceDTO();
        }
        return invInvoiceDTOSelected;
    }

    /**
     * @param invInvoiceDTOSelected the invInvoiceDTOSelected to set
     */
    public void setInvInvoiceDTOSelected(InvInvoiceDTO invInvoiceDTOSelected) {
        this.invInvoiceDTOSelected = invInvoiceDTOSelected;
    }
}
