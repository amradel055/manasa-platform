package com.isag.invreservation;

import com.isag.businessservice.InvReservationService;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvReservation;
import com.isag.entiy.InvReservationEntity;
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
 * @author khaled
 */
@Named(value = "invReservationListMB")
@ViewScoped
public class InvReservationListMB extends InventoryBasicDataForm {

    private InvInvoiceDTO invInvoiceDTOSelected;

    @EJB
    private InvReservationService invReservationService;

    @Override
    @PostConstruct
    public void init() {
        this.load();

    }

    @Override
    public void load() {
        this.setDataModel(new LazyDataModel<InvInvoiceDTO>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<InvInvoiceDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(invReservationService.getTotalRegistors(getUserData().getUserBranch().getId(), filters).intValue());
                return invReservationService.getALLInvPurchaseInvoiceByBranchId(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters);
            }
        });
    }

    public void delete() {

        try {
            if (getInvInvoiceDTOSelected() != null) {
                invReservationService.deleteInvReservation(getInvInvoiceDTOSelected());
                load();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserDDs().get("INFO"), getUserDDs().get("DELETED")));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), getUserDDs().get("UNIQE_KEY_ERROR")));
        }

    }

    public String goToAdd() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "Add");
        exit("../invreservation/invReservationForm.xhtml");
        return "";
    }

    public String goToEdit() {
        if (getInvInvoiceDTOSelected() != null && getInvInvoiceDTOSelected().getId() > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("invReservationSelected", getInvInvoiceDTOSelected().getId());
            context.getSessionMap().put("ScreenMode", "Edit");
            exit("../invreservation/invReservationForm.xhtml");
            return "";
        } else {
            return "";
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String exit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invInvoiceDTOSelected
     */
    public InvInvoiceDTO getInvInvoiceDTOSelected() {
        if (invInvoiceDTOSelected == null) {
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
