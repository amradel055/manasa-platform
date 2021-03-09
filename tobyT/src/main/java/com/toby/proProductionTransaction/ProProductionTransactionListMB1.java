package com.toby.proProductionTransaction;

import com.toby.businessservice.ProProductionItemsTransactionService;
import com.toby.dto.ProProductionItemsTransactionDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "proProductionTransactionListMB1")
@ViewScoped
public class ProProductionTransactionListMB1 extends BaseListBean {

    private ProProductionItemsTransactionDTO proProductionItemsTransactionDTOSelected;

    private List<ProProductionItemsTransactionDTO> proProductionItemsTransactionDTOList;
    @EJB
    private ProProductionItemsTransactionService proProductionItemsTransactionService;

    @PostConstruct
    @Override
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
    }

    @Override
    public void delete() {
        proProductionItemsTransactionDTOList.remove(proProductionItemsTransactionDTOSelected);
        proProductionItemsTransactionService.deleteproProductionItemsTransactionDTO(proProductionItemsTransactionDTOSelected, getUserData().getUser());
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
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
     * @return the proProductionItemsTransactionList
     */
}
