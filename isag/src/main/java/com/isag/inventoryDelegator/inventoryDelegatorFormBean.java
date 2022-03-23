/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.inventoryDelegator;

import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.CompanyService;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.IsagCompany;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
 * @author hq002
 */
@Named(value = "inventoryDelegatorFormBean")
@ViewScoped
public class inventoryDelegatorFormBean extends BaseFormBean implements Serializable {

    private Integer delegatorId;
    private Integer branchId;
    private List<IsagCompany> userCompany;
    private Integer selectedCompanyId;
    private boolean visible = false;
    private InventoryDelegator inventoryDelegator;
    private Map<String, String> userDDs;
    private Boolean showMessage = Boolean.FALSE;

    private HttpServletRequest request;
    private String url;

    @EJB
    private CompanyService companyService;

    @EJB
    private InvDelegatorService inventoryDelegatorService;

    @Override
    @PostConstruct
    public void init() {
        try {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        branchId = getUserData().getUserBranch().getId();

        userDDs = getUserData().getUserDDs();
        load();
         } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "init");
        }
    }

    @Override
    public void load() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode(((String) context.getSessionMap().get("ScreenMode")));

        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
            setInventoryDelegatedDataEmpty();

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
            try {
                delegatorId = (Integer) context.getSessionMap().get("inventoryDelegatorSelected");
                setEditedInventoryDelegatedValues(delegatorId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "load");
        }
    }

    public void setInventoryDelegatedDataEmpty() {
        try {
        inventoryDelegator = new InventoryDelegator();
        inventoryDelegator.setType(0);
        } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "setInventoryDelegatedDataEmpty");
        }
    }

    public void setEditedInventoryDelegatedValues(Integer bId) {
        try {
        inventoryDelegator = inventoryDelegatorService.findInventoryDelegator(bId);
        if (inventoryDelegator.getType() == 1) {
            visible = true;
        }
        } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "setEditedInventoryDelegatedValues");
        }
    }

    public void checkVisability() {
        try {
        if (inventoryDelegator.getType() == 1) {
            visible = true;
        } else if (inventoryDelegator.getType() == 0) {
            visible = false;
        }
         } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "checkVisability");
        }
    }

    public void checkCodeExistence() {
        try {

        List< InventoryDelegator> delegatorList = new ArrayList<>();

//        if (delegatorId == null) {
//            if (url.contains("salesperson")) {
//                delegatorList = inventoryDelegatorService.findDelegator(branchId, null, inventoryDelegator.getCode(), 1);
//            } else {
//                delegatorList = inventoryDelegatorService.findDelegator(branchId, null, inventoryDelegator.getCode(), 0);
//            }
//        } else {
        if (url.contains("salesperson")) {
            delegatorList = inventoryDelegatorService.findDelegator(branchId, delegatorId, inventoryDelegator.getCode(), 1);
        } else {
            delegatorList = inventoryDelegatorService.findDelegator(branchId, delegatorId, inventoryDelegator.getCode(), 0);
        }
//        }

        if (delegatorList != null && !delegatorList.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("UNIQE_SERIAL")));
            showMessage = true;
        }
         } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "checkCodeExistence");
        }
    }

    @Override
    public String save() {
        try {
        if (inventoryDelegator.getCode() == null || "".equals(inventoryDelegator.getCode())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب إدخال الكود"));
            showMessage = true;
            return "";
        }

        if (inventoryDelegator.getName() == null || "".equals(inventoryDelegator.getName())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب إدخال الأسم"));
            showMessage = true;
            return "";
        }

        if (inventoryDelegator.getId() == null) {
            inventoryDelegator.setCreatedBy(getUserData().getUser());
            inventoryDelegator.setCreationDate(new Date());
        } else {
            inventoryDelegator.setModificationDate(new Date());
            inventoryDelegator.setModifiedBy(getUserData().getUser());
        }

        if (url.contains("salesperson")) {
            inventoryDelegator.setType(1);
        } else {
            inventoryDelegator.setType(0);
        }

        inventoryDelegator.setBranchId(getUserData().getUserBranch());
        inventoryDelegator.setCompanyId(getUserData().getCompany());

        if (inventoryDelegatorService.updateInventoryDelegator(inventoryDelegator) == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("UNIQE_SERIAL")));
            showMessage = true;
            return "";
        }
       // cancel();
       savedMeesage(getUserData().getUserDDs().get("SAVED"));
        return "";
         } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "save");
            return null;
        }
    }
 public void reset() {
     try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        context.getSessionMap().replace("ScreenMode", "Add");
        setScreenMode("Add");

        init();
         } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "reset");
        }
    }
    public String cancel() {
        try {

        if (url.contains("salesperson")) {
            exit("../salesperson/salespersonList.xhtml");
        } else {
            exit("../purchasesrepresentative/purchasesrepresentativeList.xhtml");
        }

        return "";
         } catch (Exception e) {
            saveError(e, "inventoryDelegatorFormBean", "cancel");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the userCompany
     */
    public List<IsagCompany> getUserCompany() {
        return userCompany;
    }

    /**
     * @param userCompany the userCompany to set
     */
    public void setUserCompany(List<IsagCompany> userCompany) {
        this.userCompany = userCompany;
    }

    /**
     * @return the selectedCompanyId
     */
    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    /**
     * @param selectedCompanyId the selectedCompanyId to set
     */
    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return the inventoryDelegator
     */
    public InventoryDelegator getInventoryDelegator() {
        return inventoryDelegator;
    }

    /**
     * @param inventoryDelegator the inventoryDelegator to set
     */
    public void setInventoryDelegator(InventoryDelegator inventoryDelegator) {
        this.inventoryDelegator = inventoryDelegator;
    }

    /**
     * @return the delegatorId
     */
    public int getDelegatorId() {
        return delegatorId;
    }

    /**
     * @param delegatorId the delegatorId to set
     */
    public void setDelegatorId(Integer delegatorId) {
        this.delegatorId = delegatorId;
    }

    public Map<String, String> getUserDDs() {
        return userDDs;
    }

    public void setUserDDs(Map<String, String> userDDs) {
        this.userDDs = userDDs;
    }

    public Boolean getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(Boolean showMessage) {
        this.showMessage = showMessage;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

}
