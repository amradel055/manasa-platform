package com.toby.proProductionCheck;

import com.toby.businessservice.ProProductionTransactionService;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named(value = "proProductionCheckListMB")
@ViewScoped
public class ProProductionCheckListMB extends BaseFormBean {

    private ProProductionTransactionDTO proProductionTransactionDTOSelected;
    private LazyDataModel<ProProductionTransactionDTO> dataModel;

    @EJB
    private ProProductionTransactionService proProductionTransactionService;

    public ProProductionCheckListMB() {
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @PostConstruct
    public void init() {
        this.setDataModel(new LazyDataModel<ProProductionTransactionDTO>() {
            private static final long serialVersionUID = 1L;

            @Override
            public List<ProProductionTransactionDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                setRowCount(proProductionTransactionService.getTotalRegistors(getUserData().getUserBranch().getId(), filters).intValue());
                return proProductionTransactionService.getALLInvPurchaseInvoiceDTOByTypeAndBranchId(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters);
            }
        });
    }

    public void delete() {
        proProductionTransactionService.deleteProProductionTransaction(proProductionTransactionDTOSelected, getUserData().getUser());
        init();
    }

    public String goToForm() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../proProductionCheck/proProductionCheckNew.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "proProductionCheckListMB", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (proProductionTransactionDTOSelected != null && proProductionTransactionDTOSelected.getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("proProductionTransactionDTOSelected", proProductionTransactionDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../proProductionCheck/proProductionCheck.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "proProductionCheckListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ProProductionTransactionDTO getProProductionTransactionDTOSelected() {
        return proProductionTransactionDTOSelected;
    }

    public void setProProductionTransactionDTOSelected(ProProductionTransactionDTO proProductionTransactionDTOSelected) {
        this.proProductionTransactionDTOSelected = proProductionTransactionDTOSelected;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<ProProductionTransactionDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<ProProductionTransactionDTO> dataModel) {
        this.dataModel = dataModel;
    }

}
