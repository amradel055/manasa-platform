/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invQuotation;

import com.toby.businessservice.InvQuotationService;
import com.toby.dto.InvInvoiceDTO;
import com.toby.toby.InventoryBasicDataForm;
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
@Named(value = "invQuotationListDTOMB")
@ViewScoped
public class InvQuotationListDTOMB extends InventoryBasicDataForm {

    private InvInvoiceDTO invQuotationSelected;
    @EJB
    private InvQuotationService invQuotationService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            this.setDataModel(new LazyDataModel<InvInvoiceDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<InvInvoiceDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    setRowCount(invQuotationService.getTotalRegistors(getUserData().getUserBranch().getId(), filters).intValue());
                    return invQuotationService.getALLInvQotationByBranchId(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters);
                }
            });
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "load");
        }
    }

    public void delete() {
        try {
            if (getInvQuotationSelected() != null) {
                try {
                    invQuotationService.deleteInvQotation(getInvQuotationSelected());
                    load();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserDDs().get("INFO"), getUserDDs().get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), getUserDDs().get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "delete");
        }
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddInvQuotation");
            exit("../invQuotation_1/invQuotationForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (getInvQuotationSelected() != null && getInvQuotationSelected().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invQuotationSelected", getInvQuotationSelected().getId());
                context.getSessionMap().put("ScreenMode", "EditInvQuotation");
                exit("../invQuotation_1/invQuotationForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "invQuotationListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invQuotationSelected
     */
    public InvInvoiceDTO getInvQuotationSelected() {
        if (invQuotationSelected == null) {
            invQuotationSelected = new InvInvoiceDTO();
        }
        return invQuotationSelected;
    }

    /**
     * @param invQuotationSelected the invQuotationSelected to set
     */
    public void setInvQuotationSelected(InvInvoiceDTO invQuotationSelected) {
        this.invQuotationSelected = invQuotationSelected;
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
