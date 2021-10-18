/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.setupNewInventory;

import com.isag.businessservice.CostCenterService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlAdminUnitService;
import com.isag.businessservice.InvInventoryService;
import com.isag.converter.CostCenterConverter;
import com.isag.converter.GlAdminUnitConverter;
import com.isag.converter.GlaccountConverter;
import com.isag.define.InventoryPricesGroupEnum;
import com.isag.entity.CostCenter;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.InvInventory;
import com.isag.entiy.InvInventoryEntity;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq002
 */
@Named(value = "setupNewInventoryFormMB")
@ViewScoped
public class setupNewInventoryFormMB extends BaseFormBean implements Serializable {

    private Integer invInventoryId;
    private Integer branchId;

    private Integer selectedCompanyId;
    private InvInventory invInventory;
    private List<InvInventory> invInventoryList;

//    Map<String, InvInventory> inventoryMap = new HashMap<>();
    private InvInventoryEntity invInventoryEntity;

    private List<GlAccount> glAccountList;
    private GlaccountConverter accountConverter;

    private List<GlAdminUnit> glAdminUnitList;

    private List<CostCenter> costCenterList;
    private CostCenterConverter costCenterConverter;

    private GlAdminUnitConverter glAdminUnitConverter;

    private Boolean showGeneralMessage;
    private Boolean isExist = false;

    @EJB
    private GlAccountService accountService;
    @EJB
    private CostCenterService costCenterService;
    @EJB
    private GlAdminUnitService adminUnitService;
    @EJB
    private InvInventoryService inventoryService;

    @Override
    @PostConstruct
    public void init() {
        try {
            invInventoryList = new ArrayList<>();
            invInventoryEntity = new InvInventoryEntity();
            invInventory = new InvInventory();
            invInventoryList = new ArrayList<>();
            glAccountList = new ArrayList<>();
            glAdminUnitList = new ArrayList<>();
            costCenterList = new ArrayList<>();

            load();
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            branchId = getUserData().getUserBranch().getId();

            invInventoryList = inventoryService.getALLInventoriesByBranch(branchId);

//        for (InvInventory inventory : invInventoryList) {
//            inventoryMap.put(inventory.getCode(), inventory);
//        }
            glAccountList = accountService.getBranchGLAccountsActive(getUserData().getSelectedBranch());
            accountConverter = new GlaccountConverter(glAccountList);

            costCenterList = costCenterService.getActiveSubCostCentersByBranch(getUserData().getSelectedBranch());
            setGlAdminUnitList(adminUnitService.getAllAdminUnitByBranchIdActive(getUserData().getSelectedBranch()));

            costCenterConverter = new CostCenterConverter(costCenterList);
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                setInvInventoryDataEmpty();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    invInventoryId = (Integer) context.getSessionMap().get("invInventorySelected");
                    setEditedInventoryDelegatedValues(invInventoryId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "load");
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
            saveError(e, "setupNewInventoryFormMB", "reset");
        }
    }

    public void checkCodeExistence() {
        try {

            List<InvInventory> invList = new ArrayList<>();

            if (invInventoryId == null) {
                invList = inventoryService.findInventory(branchId, invInventoryEntity.getCode());
            } else {
                invList = inventoryService.findInventory(branchId, invInventoryId, invInventoryEntity.getCode());
            }

            if (invList != null && !invList.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("UNIQE_SERIAL")));
                showGeneralMessage = true;
            }

//        InvInventory updatedInv = new InvInventory();
//        updatedInv = inventoryMap.get(invInventoryEntity.getCode());
//
//        if (invInventoryId == null && updatedInv != null && updatedInv.getId() != null) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("UNIQE_SERIAL")));
//            showGeneralMessage = true;
//            isExist = true;
//            return;
//        }
//
//        if (invInventoryId != null && updatedInv != null && updatedInv.getId() != null && !invInventoryId.equals(updatedInv.getId())) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("UNIQE_SERIAL")));
//            showGeneralMessage = true;
//            isExist = true;
//            return;
//        }
//
//        isExist = false;
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "checkCodeExistence");
        }
    }

    public List<CostCenter> completeCostCenter(String query) {
        try {
            List<CostCenter> costCenters = costCenterList;
            if (query == null || query.trim().equals("")) {
                costCenterConverter = new CostCenterConverter(costCenters);
                return costCenters;
            }
            List<CostCenter> filteredCostCenters = new ArrayList<>();

            String nameAr;
            Integer code;
            CostCenter costCenter;
            for (int i = 0; i < costCenterList.size(); i++) {
                costCenter = costCenters.get(i);
                nameAr = costCenter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredCostCenters.contains(costCenter)) {
                        filteredCostCenters.add(costCenter);
                    }
                }

                code = costCenter.getCode();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!filteredCostCenters.contains(costCenter)) {
                        filteredCostCenters.add(costCenter);
                    }
                }
            }

            costCenterConverter = new CostCenterConverter(filteredCostCenters);
            return filteredCostCenters;
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "completeCostCenter");
            return null;
        }
    }

    public List<GlAdminUnit> completeAdminUnit(String query) {
        try {
            List<GlAdminUnit> glAdminUnits = glAdminUnitList;
            if (query == null || query.trim().equals("")) {
                glAdminUnitConverter = new GlAdminUnitConverter(glAdminUnits);
                return glAdminUnits;
            }
            List<GlAdminUnit> filteredGlAdminUnitList = new ArrayList<>();

            String nameAr;
            Integer code;
            GlAdminUnit glAdminUnit;
            for (int i = 0; i < glAdminUnitList.size(); i++) {
                glAdminUnit = glAdminUnits.get(i);
                nameAr = glAdminUnit.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                        filteredGlAdminUnitList.add(glAdminUnit);
                    }
                }

                code = glAdminUnit.getCode();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                        filteredGlAdminUnitList.add(glAdminUnit);
                    }
                }
            }
            glAdminUnitConverter = new GlAdminUnitConverter(filteredGlAdminUnitList);
            return filteredGlAdminUnitList;
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "completeAdminUnit");
            return null;
        }
    }

    public List<GlAccount> completeGlAccount(String query) {
        try {
            List<GlAccount> glaccounts = glAccountList;//45
            if (query == null || query.trim().equals("")) {
                accountConverter = new GlaccountConverter(glaccounts);
                return glaccounts;
            }
            List<GlAccount> filteredGlaccounts = new ArrayList<>();
            String nameAr;
            Integer code;
            GlAccount glaccount;
            for (int i = 0; i < glAccountList.size(); i++) {
                glaccount = glaccounts.get(i);
                nameAr = glaccount.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlaccounts.contains(glaccount)) {
                        filteredGlaccounts.add(glaccount);
                    }
                }

                code = glaccount.getAccNumber();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!filteredGlaccounts.contains(glaccount)) {
                        filteredGlaccounts.add(glaccount);
                    }
                }
            }

            accountConverter = new GlaccountConverter(filteredGlaccounts);
            return filteredGlaccounts;
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "completeGlAccount");
            return null;
        }
    }

    @Override
    public String save() {
        try {

            invInventory.setAccountId(invInventoryEntity.getAccountId());
            invInventory.setAdminUnitId(invInventoryEntity.getAdminUnitId());
            invInventory.setCostCenterId(invInventoryEntity.getCostCenterId());
            invInventory.setCode(invInventoryEntity.getCode());
            invInventory.setCompanyId(getUserData().getCompany());
            invInventory.setName(invInventoryEntity.getName());
            invInventory.setTypeItem(invInventoryEntity.getType());
            invInventory.setBranchId(getUserData().getUserBranch());
            invInventory.setSellPriceType(invInventoryEntity.getSellPriceType());            
            invInventory.setSegilValue(invInventoryEntity.getSegilValue());
            invInventory.setDaribaValue(invInventoryEntity.getDaribaValue());
            invInventory = inventoryService.updateNewInventory(invInventory, getUserData().getUser(),
                    getUserData().getCompany(), getUserData().getUserBranch());
            if (invInventory != null && invInventory.getId() != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الحفظ بنجاح", getUserData().getUserDDs().get("SAVED")));
                showGeneralMessage = true;
                return "";
            } else {
                errorMessage("خطأ فى الحفظ");
                showGeneralMessage = true;
                return "";
            }
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "save");
            return null;
        }
    }

    //------------------------------------
    public String exit() {
        try {
            exit("../invcreate/setupNewInventoryList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "exit");
            return null;
        }
    }

    public void setInvInventoryDataEmpty() {
        invInventory = new InvInventory();
    }

    public void setEditedInventoryDelegatedValues(Integer bId) {
        try {
            invInventory = inventoryService.findInventory(bId);
            validateInputsForEdit();
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "setEditedInventoryDelegatedValues");
        }
    }

    public void validateInputsForEdit() {
        try {

            invInventoryEntity.setAccountId(invInventory.getAccountId());
            invInventoryEntity.setAdminUnitId(invInventory.getAdminUnitId());
            invInventoryEntity.setCostCenterId(invInventory.getCostCenterId());
            invInventoryEntity.setCode(invInventory.getCode());
            invInventoryEntity.setName(invInventory.getName());
            invInventoryEntity.setType(invInventory.getTypeItem());
            invInventoryEntity.setSellPriceType(invInventory.getSellPriceType());
            invInventoryEntity.setSegilValue(invInventory.getSegilValue());
            invInventoryEntity.setDaribaValue(invInventory.getDaribaValue());
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "validateInputsForEdit");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invInventoryId
     */
    public Integer getInvInventoryId() {
        return invInventoryId;
    }

    /**
     * @param invInventoryId the invInventoryId to set
     */
    public void setInvInventoryId(Integer invInventoryId) {
        this.invInventoryId = invInventoryId;
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
     * @return the invInventory
     */
    public InvInventory getInvInventory() {
        return invInventory;
    }

    /**
     * @param invInventory the invInventory to set
     */
    public void setInvInventory(InvInventory invInventory) {
        this.invInventory = invInventory;
    }

    /**
     * @return the inventoryService
     */
    public InvInventoryService getInventoryService() {
        return inventoryService;
    }

    /**
     * @param inventoryService the inventoryService to set
     */
    public void setInventoryService(InvInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * @return the glAccountList
     */
    public List<GlAccount> getGlAccountList() {
        return glAccountList;
    }

    /**
     * @param glAccountList the glAccountList to set
     */
    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
    }

    /**
     * @return the glAdminUnitList
     */
    public List<GlAdminUnit> getGlAdminUnitList() {
        return glAdminUnitList;
    }

    /**
     * @param glAdminUnitList the glAdminUnitList to set
     */
    public void setGlAdminUnitList(List<GlAdminUnit> glAdminUnitList) {
        this.glAdminUnitList = glAdminUnitList;
    }

    /**
     * @return the costCenterList
     */
    public List<CostCenter> getCostCenterList() {
        return costCenterList;
    }

    /**
     * @param costCenterList the costCenterList to set
     */
    public void setCostCenterList(List<CostCenter> costCenterList) {
        this.costCenterList = costCenterList;
    }

    public InventoryPricesGroupEnum[] getAccountGroups() {
        return InventoryPricesGroupEnum.values();
    }

    /**
     * @return the invInventoryEntity
     */
    public InvInventoryEntity getInvInventoryEntity() {
        return invInventoryEntity;
    }

    /**
     * @param invInventoryEntity the invInventoryEntity to set
     */
    public void setInvInventoryEntity(InvInventoryEntity invInventoryEntity) {
        this.invInventoryEntity = invInventoryEntity;
    }

    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }

    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Boolean getShowGeneralMessage() {
        return showGeneralMessage;
    }

    public void setShowGeneralMessage(Boolean showGeneralMessage) {
        this.showGeneralMessage = showGeneralMessage;
    }

    public CostCenterConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    public void setCostCenterConverter(CostCenterConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }

    public GlaccountConverter getAccountConverter() {
        return accountConverter;
    }

    public void setAccountConverter(GlaccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }

    public GlAdminUnitConverter getGlAdminUnitConverter() {
        return glAdminUnitConverter;
    }

    public void setGlAdminUnitConverter(GlAdminUnitConverter glAdminUnitConverter) {
        this.glAdminUnitConverter = glAdminUnitConverter;
    }
}
