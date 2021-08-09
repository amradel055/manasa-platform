/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invPurchaseOrder;

import com.isag.businessservice.InvPurchaseOrderService;
import com.isag.dto.InvInvoiceDTO;
import com.isag.isag.InventoryBasicDataForm;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author hq002
 */
@Named(value = "invPurchaseOrderListDTOMB")
@ViewScoped
public class invPurchaseOrderListDTOMB extends InventoryBasicDataForm {


    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderListMB", "init");
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
                    if (!getUri().contains("invPurchaseOrderList")) {
                        type = 1;
                    }

                    setRowCount(invPurchaseOrderService.getTotalRegistors(type, getUserData().getUserBranch().getId(), filters).intValue());
                    return invPurchaseOrderService.getALLPurchaseOrderByBranchIdDTO(type, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters);

                }
            });
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderListMB", "load");
        }
    }

    public void delete() {
        try {
            if ((getInvInvoiceDTO().getPostFlag() == null || (getInvInvoiceDTO().getPostFlag() != null && getInvInvoiceDTO().getPostFlag() != 1)) && getInvInvoiceDTO().getId() != null) {
                try {
                    invPurchaseOrderService.deletePurchaseOrder(getInvInvoiceDTO().getId());
                    load();

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserDDs().get("INFO"), getUserDDs().get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), getUserDDs().get("UNIQE_KEY_ERROR")));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "هذا السند مرحل"));
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "delete");
        }
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "addInvoiceOrder");
            exit("../invpurchaseorder_1/invPurchaseOrderForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "invPurchaseOrderListMB", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (getInvInvoiceDTO() != null && getInvInvoiceDTO().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invPurchaseOrderSelected", getInvInvoiceDTO().getId());
                context.getSessionMap().put("ScreenMode", "editInvoiceOrder");
                exit("../invpurchaseorder_1/invPurchaseOrderForm.xhtml");
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

    public InvPurchaseOrderService getInvPurchaseOrderService() {
        return invPurchaseOrderService;
    }

    public void setInvPurchaseOrderService(InvPurchaseOrderService invPurchaseOrderService) {
        this.invPurchaseOrderService = invPurchaseOrderService;
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String exit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
