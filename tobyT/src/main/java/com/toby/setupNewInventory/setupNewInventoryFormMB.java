/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.setupNewInventory;

import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAdminUnitService;
import com.toby.businessservice.InvInventoryService;
import com.toby.converter.CostCenterDTOConverter;
import com.toby.converter.GlAccountDTOConverter;
import com.toby.converter.GlAdminUnitDTOConverter;
import com.toby.define.InventoryPricesGroupEnum;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseFormBean;
import java.io.Serializable;
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

    private InvInventoryDTO invInventoryDTO;
    private List<GlAccountDTO> glAccountDTOList;

    private List<GlAccountDTO> glCostAccountDTOList;
    private List<GlAdminUnitDTO> adminUnitDTOList;
    private List<CostCenterDTO> costCenterDTOList;
    private CostCenterDTOConverter costCenterDTOConverter;
    private GlAdminUnitDTOConverter adminUnitDTOConverter;
    private GlAccountDTOConverter accountDTOConverter;
    private List<GlAccountDTO> glRunningAccountDTOList;
    private GlAccountDTOConverter runningAccountDTOConverter;
    private GlAccountDTOConverter costAccountDTOConverter;
    private List<GlAccountDTO> customerAccountDTOList;
    private GlAccountDTOConverter customerAccountDTOConverter;

    private Boolean showGeneralMessage;

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
        load();
    }

    @Override
    public void load() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {

            try {
                if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                    reset();
                } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                    try {
                        Integer invInventoryId = (Integer) context.getSessionMap().get("invInventorySelected");
                        setEditedInventoryDelegatedValues(invInventoryId);
                        getAdminUnitDTOList();
                        getCostCenterDTOList();
                        getGlAccountDTOList();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                saveError(e, "setupNewInventoryFormMB", "load");
            }

        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "init");
        }
    }

    public void reset() {
        try {
            invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setInvType(0);

        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "reset");
        }
    }

    public List<CostCenterDTO> completeCostCenter(String query) {
        getCostCenterDTOList();
        return AutoComplete.completeCostCenter(query, costCenterDTOList, costCenterDTOConverter);
    }

    public List<GlAdminUnitDTO> completeAdminUnit(String query) {
        getAdminUnitDTOList();
        return AutoComplete.completeAdminUnit(query, adminUnitDTOList, adminUnitDTOConverter);
    }

    public List<GlAccountDTO> completeGlAccount(String query) {
        getGlAccountDTOList();
        return AutoComplete.completeGlAccount(query, glAccountDTOList, accountDTOConverter);
    }

    public List<GlAccountDTO> completeRunningGlAccount(String query) {
        getGlRunningAccountDTOList();
        return AutoComplete.completeGlAccount(query, getGlRunningAccountDTOList(), runningAccountDTOConverter);
    }

    public List<GlAccountDTO> completeCostGlAccount(String query) {
        getGlCostAccountDTOList();
        return AutoComplete.completeGlAccount(query, getGlCostAccountDTOList(), costAccountDTOConverter);
    }

    public List<GlAccountDTO> completeCustomerGlAccount(String query) {
        getCustomerAccountDTOList();
        return AutoComplete.completeGlAccount(query, getCustomerAccountDTOList(), customerAccountDTOConverter);
    }

    public boolean validateCode() {
        if (invInventoryDTO != null && invInventoryDTO.getCode() != null && invInventoryDTO.getMsg() == null) {
            Boolean x = inventoryService.validateCode(getUserData().getUser(), invInventoryDTO.getId(), invInventoryDTO.getCode());
            if (!x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "الكود موجود", null));
                showGeneralMessage = true;
                return false;
            }
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطا فى الحفظ", null));
            showGeneralMessage = true;
            return false;
        }
    }

    public boolean validateName() {
        if (invInventoryDTO != null && invInventoryDTO.getName() != null && invInventoryDTO.getMsg() == null) {
            Boolean x = inventoryService.validateName(getUserData().getUser(), invInventoryDTO.getId(), invInventoryDTO.getName());
            if (!x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "الاسم موجود", null));
                showGeneralMessage = true;
                return false;
            }
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطا فى الحفظ", null));
            showGeneralMessage = true;
            return false;
        }
    }

    @Override
    public String save() {
        try {
            if (invInventoryDTO != null && !invInventoryDTO.getName().isEmpty() && !invInventoryDTO.getCode().isEmpty()) {
                invInventoryDTO = inventoryService.updateNewInventoryDTO(invInventoryDTO, getUserData().getUser());
                if (invInventoryDTO != null && invInventoryDTO.getId() != null && invInventoryDTO.getMsg() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الحفظ بنجاح", null));
                    exit();
                } else {
                    errorMessage(invInventoryDTO.getMsg());
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "الاسم او الكود فارغ", null));
            }
            showGeneralMessage = true;
            return "";
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

    public void setEditedInventoryDelegatedValues(Integer id) {
        try {
            invInventoryDTO = inventoryService.findInventory(id);
        } catch (Exception e) {
            saveError(e, "setupNewInventoryFormMB", "setEditedInventoryDelegatedValues");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public InventoryPricesGroupEnum[] getAccountGroups() {
        return InventoryPricesGroupEnum.values();
    }

    public Boolean getShowGeneralMessage() {
        return showGeneralMessage;
    }

    public void setShowGeneralMessage(Boolean showGeneralMessage) {
        this.showGeneralMessage = showGeneralMessage;
    }

    /**
     * @return the invInventoryDTO
     */
    public InvInventoryDTO getInvInventoryDTO() {
        if (invInventoryDTO == null) {
            invInventoryDTO = new InvInventoryDTO();
        }
        return invInventoryDTO;
    }

    /**
     * @param invInventoryDTO the invInventoryDTO to set
     */
    public void setInvInventoryDTO(InvInventoryDTO invInventoryDTO) {
        this.invInventoryDTO = invInventoryDTO;
    }

    /**
     * @return the costCenterDTOList
     */
    public List<CostCenterDTO> getCostCenterDTOList() {
        if (costCenterDTOList == null || costCenterDTOList.isEmpty()) {
            costCenterDTOList = costCenterService.getAllCostCenterDTOList(getUserData().getUser());
            costCenterDTOConverter = new CostCenterDTOConverter(costCenterDTOList);
        }
        return costCenterDTOList;
    }

    /**
     * @param costCenterDTOList the costCenterDTOList to set
     */
    public void setCostCenterDTOList(List<CostCenterDTO> costCenterDTOList) {
        this.costCenterDTOList = costCenterDTOList;
    }

    /**
     * @return the costCenterDTOConverter
     */
    public CostCenterDTOConverter getCostCenterDTOConverter() {
        return costCenterDTOConverter;
    }

    /**
     * @param costCenterDTOConverter the costCenterDTOConverter to set
     */
    public void setCostCenterDTOConverter(CostCenterDTOConverter costCenterDTOConverter) {
        this.costCenterDTOConverter = costCenterDTOConverter;
    }

    /**
     * @return the adminUnitDTOList
     */
    public List<GlAdminUnitDTO> getAdminUnitDTOList() {
        if (adminUnitDTOList == null || adminUnitDTOList.isEmpty()) {
            adminUnitDTOList = adminUnitService.getAllAdminUnitDTOByBranchIdActive(getUserData().getUser());
            setAdminUnitDTOConverter(new GlAdminUnitDTOConverter(adminUnitDTOList));
        }
        return adminUnitDTOList;
    }

    /**
     * @param adminUnitDTOList the adminUnitDTOList to set
     */
    public void setAdminUnitDTOList(List<GlAdminUnitDTO> adminUnitDTOList) {
        this.adminUnitDTOList = adminUnitDTOList;
    }

    /**
     * @return the adminUnitDTOConverter
     */
    public GlAdminUnitDTOConverter getAdminUnitDTOConverter() {
        return adminUnitDTOConverter;
    }

    /**
     * @param adminUnitDTOConverter the adminUnitDTOConverter to set
     */
    public void setAdminUnitDTOConverter(GlAdminUnitDTOConverter adminUnitDTOConverter) {
        this.adminUnitDTOConverter = adminUnitDTOConverter;
    }

    /**
     * @return the glAccountDTOList
     */
    public List<GlAccountDTO> getGlAccountDTOList() {
        if (glAccountDTOList == null || glAccountDTOList.isEmpty()) {
            glAccountDTOList = accountService.getBranchGLAccountDTOListActive(getUserData().getUser());
            accountDTOConverter = new GlAccountDTOConverter(glAccountDTOList);
        }
        return glAccountDTOList;
    }

    /**
     * @param glAccountDTOList the glAccountDTOList to set
     */
    public void setGlAccountDTOList(List<GlAccountDTO> glAccountDTOList) {
        this.glAccountDTOList = glAccountDTOList;
    }

    /**
     * @return the accountDTOConverter
     */
    public GlAccountDTOConverter getAccountDTOConverter() {
        return accountDTOConverter;
    }

    /**
     * @param accountDTOConverter the accountDTOConverter to set
     */
    public void setAccountDTOConverter(GlAccountDTOConverter accountDTOConverter) {
        this.accountDTOConverter = accountDTOConverter;
    }

    /**
     * @return the runningAccountDTOConverter
     */
    public GlAccountDTOConverter getRunningAccountDTOConverter() {
        return runningAccountDTOConverter;
    }

    /**
     * @param runningAccountDTOConverter the runningAccountDTOConverter to set
     */
    public void setRunningAccountDTOConverter(GlAccountDTOConverter runningAccountDTOConverter) {
        this.runningAccountDTOConverter = runningAccountDTOConverter;
    }

    /**
     * @return the glRunningAccountDTOList
     */
    public List<GlAccountDTO> getGlRunningAccountDTOList() {
        if (glRunningAccountDTOList == null || glRunningAccountDTOList.isEmpty()) {
            glRunningAccountDTOList = accountService.getBranchGLAccountDTOListActive(getUserData().getUser());
            runningAccountDTOConverter = new GlAccountDTOConverter(glRunningAccountDTOList);
        }
        return glRunningAccountDTOList;
    }

    /**
     * @param glRunningAccountDTOList the glRunningAccountDTOList to set
     */
    public void setGlRunningAccountDTOList(List<GlAccountDTO> glRunningAccountDTOList) {
        this.glRunningAccountDTOList = glRunningAccountDTOList;
    }

    /**
     * @return the glCostAccountDTOList
     */
    public List<GlAccountDTO> getGlCostAccountDTOList() {
        if (glCostAccountDTOList == null || glCostAccountDTOList.isEmpty()) {
            glCostAccountDTOList = accountService.getBranchGLAccountDTOListActive(getUserData().getUser());
            costAccountDTOConverter = new GlAccountDTOConverter(glCostAccountDTOList);
        }
        return glCostAccountDTOList;
    }

    /**
     * @param glCostAccountDTOList the glCostAccountDTOList to set
     */
    public void setGlCostAccountDTOList(List<GlAccountDTO> glCostAccountDTOList) {
        this.glCostAccountDTOList = glCostAccountDTOList;
    }

    /**
     * @return the costAccountDTOConverter
     */
    public GlAccountDTOConverter getCostAccountDTOConverter() {
        return costAccountDTOConverter;
    }

    /**
     * @param costAccountDTOConverter the costAccountDTOConverter to set
     */
    public void setCostAccountDTOConverter(GlAccountDTOConverter costAccountDTOConverter) {
        this.costAccountDTOConverter = costAccountDTOConverter;
    }

    /**
     * @return the customerAccountDTOConverter
     */
    public GlAccountDTOConverter getCustomerAccountDTOConverter() {
        return customerAccountDTOConverter;
    }

    /**
     * @param customerAccountDTOConverter the customerAccountDTOConverter to set
     */
    public void setCustomerAccountDTOConverter(GlAccountDTOConverter customerAccountDTOConverter) {
        this.customerAccountDTOConverter = customerAccountDTOConverter;
    }

    /**
     * @return the customerAccountDTOList
     */
    public List<GlAccountDTO> getCustomerAccountDTOList() {
        if (customerAccountDTOList == null || customerAccountDTOList.isEmpty()) {
            customerAccountDTOList = accountService.getBranchGLAccountDTOListActive(getUserData().getUser());
            customerAccountDTOConverter = new GlAccountDTOConverter(customerAccountDTOList);
        }
        return customerAccountDTOList;
    }

    /**
     * @param customerAccountDTOList the customerAccountDTOList to set
     */
    public void setCustomerAccountDTOList(List<GlAccountDTO> customerAccountDTOList) {
        this.customerAccountDTOList = customerAccountDTOList;
    }
}
