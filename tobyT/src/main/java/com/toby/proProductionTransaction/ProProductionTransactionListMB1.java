package com.toby.proProductionTransaction;

import com.toby.businessservice.ProProductionItemsTransactionService;
import com.toby.dto.ProProductionItemsTransactionDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named(value = "proProductionTransactionListMB1")
@ViewScoped
public class ProProductionTransactionListMB1 extends BaseListBean {

    private ProProductionItemsTransactionDTO proProductionItemsTransactionDTOSelected;

    private List<ProProductionItemsTransactionDTO> proProductionItemsTransactionDTOList;

    private LazyDataModel<ProProductionItemsTransactionDTO> dataModel;

    @EJB
    private ProProductionItemsTransactionService proProductionItemsTransactionService;

    @PostConstruct
    @Override
    public void init() {
        load();
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            this.setDataModel(new LazyDataModel<ProProductionItemsTransactionDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<ProProductionItemsTransactionDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
//                    proProductionItemsTransactionService.getProProductionItemsTransaction(getUserData().getUser());
                    setRowCount(proProductionItemsTransactionService.getTotalRegistors(getUserData().getUserBranch().getId(), filters).intValue());
                    setProProductionItemsTransactionDTOList(proProductionItemsTransactionService.getALLProProductionItemsTransactionDTO(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters));
                    return getProProductionItemsTransactionDTOList();
                }
            });
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "load");
        }
    }

    public void search() {
        setProProductionItemsTransactionDTOList(proProductionItemsTransactionService.getALLProProductionItemsTransactionByInvoicesSerial(getProProductionItemsTransactionDTOSelected().getSerial()));
    }

    @Override
    public void delete() {
        getProProductionItemsTransactionDTOList().remove(getProProductionItemsTransactionDTOSelected());
        proProductionItemsTransactionService.deleteproProductionItemsTransactionDTO(getProProductionItemsTransactionDTOSelected(), getUserData().getUser());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "تم الحذف بنجاح"));
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().remove("ScreenMode");
            exit("../proProductionTransactionForm/proProductionTransactionFormNew.xhtml");
            return "";

        } catch (Exception e) {
            saveError(e, "proProductionTransactionListMB1", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (proProductionItemsTransactionDTOSelected != null && proProductionItemsTransactionDTOSelected.getId() > 0) {
                getContext().getSessionMap().put("proProductionItemsTransactionDTOSelected", proProductionItemsTransactionDTOSelected.getId());
                getContext().getSessionMap().put("ScreenMode", "Edit");
                exit("../proProductionTransactionForm/proProductionTransactionFormNew.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "proProductionTransactionListMB1", "goToEdit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the proProductionItemsTransactionDTOList
     */
    public List<ProProductionItemsTransactionDTO> getProProductionItemsTransactionDTOList() {
        if (proProductionItemsTransactionDTOList == null || proProductionItemsTransactionDTOList.isEmpty()) {
            proProductionItemsTransactionDTOList = proProductionItemsTransactionService.getProProductionItemsTransaction(getUserData().getUser());

        }
        return proProductionItemsTransactionDTOList;
    }

    /**
     * @param proProductionItemsTransactionDTOList the
     * proProductionItemsTransactionDTOList to set
     */
    public void setProProductionItemsTransactionDTOList(List<ProProductionItemsTransactionDTO> proProductionItemsTransactionDTOList) {
        this.proProductionItemsTransactionDTOList = proProductionItemsTransactionDTOList;
    }

    /**
     * @return the proProductionItemsTransactionDTOSelected
     */
    public ProProductionItemsTransactionDTO getProProductionItemsTransactionDTOSelected() {

        if (proProductionItemsTransactionDTOSelected == null) {
            proProductionItemsTransactionDTOSelected = new ProProductionItemsTransactionDTO();
        }
        return proProductionItemsTransactionDTOSelected;
    }

    /**
     * @param proProductionItemsTransactionDTOSelected the
     * proProductionItemsTransactionDTOSelected to set
     */
    public void setProProductionItemsTransactionDTOSelected(ProProductionItemsTransactionDTO proProductionItemsTransactionDTOSelected) {
        this.proProductionItemsTransactionDTOSelected = proProductionItemsTransactionDTOSelected;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<ProProductionItemsTransactionDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<ProProductionItemsTransactionDTO> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the proProductionItemsTransactionList
     */
}
