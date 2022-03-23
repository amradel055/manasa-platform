/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.instContract;

import com.isag.businessservice.InstContractService;
import com.isag.entity.InstContract;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import com.isag.views.InstInstallmentView;
import java.util.ArrayList;
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
 * @author ahmed
 */
@Named(value = "instContactListMB")
@ViewScoped
public class InstContactListMB extends BaseListBean {

    private UserData userData;
    private Integer instContractEntityIdSelected;
    private InstInstallmentView instContractEntitySelected;
    private LazyDataModel<InstInstallmentView> dataModel;
    private List<InstInstallmentView> instContractDtoList;
    @EJB
    InstContractService instContractService;

    @Override
    @PostConstruct
    public void init() {
        try {
            instContractDtoList = new ArrayList<>();
            load();
        } catch (Exception e) {
            saveError(e, "InstContactListMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            userData = new UserData();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
//            List<InstContract> instContractList = instContractService.getALLInstContractByBranchId(userData.getUserBranch().getId());

            setInstContractDtoList(new ArrayList<>());

            setDataModel(new LazyDataModel<InstInstallmentView>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<InstInstallmentView> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    setRowCount(instContractService.getTotalRegistors(userData.getUserBranch().getId(), filters).intValue());
                    setInstContractDtoList(instContractService.getALLInstContractTransferByBranchId(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters));

                    return getInstContractDtoList();
                }
            });

//            if (instContractList != null && !instContractList.isEmpty()) {
//                for (InstContract instContract : instContractList) {
//                    InstContractEntity ice = new InstContractEntity();
//                    ice.setId(instContract.getId());
////                ice.setBranchId(instContract.getBranchId().getId());
////                ice.setCompanyId(instContract.getCompanyId().getId());
////                ice.setCreatedBy(instContract.getCreatedBy().getId());
////                ice.setModifiedBy(instContract.getModifiedBy().getId());
////                ice.setCreationDate(instContract.getCreationDate());
////                ice.setModificationDate(instContract.getModificationDate());
//                    ice.setDate(instContract.getDate());
//                    ice.setStartInstallment(instContract.getStartInstallment());
//                    ice.setDaysNumber(instContract.getDaysNumber());
//                    ice.setInstProjectGuide(instContract.getProjectId());
//                    ice.setInstallmentNumber(instContract.getInstallmentNumber());
//                    ice.setInvOrganizationSite(instContract.getOrganizationSiteId());
//                    ice.setInstallmentValue(instContract.getInstallmentValue());
//                    ice.setTotalValue(instContract.getTotalValue());
//                    ice.setAdvancePayment(instContract.getAdvancePayment());
//                    instContractEntityList.add(ice);
//                }
//            }
        } catch (Exception e) {
            saveError(e, "InstContactListMB", "load");
        }

    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();

            Integer instContractId = instContractEntitySelected.getId() != null ? instContractEntitySelected.getId() : null;
            InstContract contract = new InstContract();
            contract.setId(instContractId);

            try {
                instContractService.deleteInstContract(contract);
//                instContractEntityList.remove(instContractEntitySelected);
                instContractDtoList.remove(instContractEntitySelected);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        } catch (Exception e) {
            saveError(e, "InstContactListMB", "delete");

        }
    }

    @Override
    public void filter() {
        try {

            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        } catch (Exception e) {
            saveError(e, "InstContactListMB", "filter");
        }
    }

    @Override
    public String goToEdit() {

        if (instContractEntityIdSelected != null && instContractEntityIdSelected > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("instContractEntityIdSelected", instContractEntityIdSelected);
            context.getSessionMap().put("ScreenMode", "Edit");
            if (getUserlogin().getBranchId().getCompanyId().getId().equals(123)) {
                exit("../instContract/instContractForm_new.xhtml");
            } else {
                exit("../instContract/instContractForm.xhtml");
            }

            return "";
        } else {
            return "";
        }

    }

    @Override
    public String goToAdd() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            if (getUserlogin().getBranchId().getCompanyId().getId().equals(123)) {
                exit("../instContract/instContractForm_new.xhtml");
            } else {
                exit("../instContract/instContractForm.xhtml");
            }

        } catch (Exception e) {
            saveError(e, "InstContactListMB", "goToAdd");
        }
        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the instContractEntityIdSelected
     */
    public Integer getInstContractEntityIdSelected() {
        return instContractEntityIdSelected;
    }

    /**
     * @param instContractEntityIdSelected the instContractEntityIdSelected to
     * set
     */
    public void setInstContractEntityIdSelected(Integer instContractEntityIdSelected) {
        this.instContractEntityIdSelected = instContractEntityIdSelected;
    }

    /**
     * @return the instContractEntitySelected
     */
    public InstInstallmentView getInstContractEntitySelected() {
        return instContractEntitySelected;
    }

    /**
     * @param instContractEntitySelected the instContractEntitySelected to set
     */
    public void setInstContractEntitySelected(InstInstallmentView instContractEntitySelected) {
        this.instContractEntitySelected = instContractEntitySelected;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<InstInstallmentView> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<InstInstallmentView> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the instContractDtoList
     */
    public List<InstInstallmentView> getInstContractDtoList() {
        return instContractDtoList;
    }

    /**
     * @param instContractDtoList the instContractDtoList to set
     */
    public void setInstContractDtoList(List<InstInstallmentView> instContractDtoList) {
        this.instContractDtoList = instContractDtoList;
    }

}
