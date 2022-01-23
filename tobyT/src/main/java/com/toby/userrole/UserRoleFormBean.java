/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.userrole;

import com.toby.businessservice.BranchService;
import com.toby.businessservice.CompanyService;
import com.toby.businessservice.GlBankService;
import com.toby.businessservice.GlYearService;
import com.toby.businessservice.InvInventoryService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.businessservice.TobyUserService;
import com.toby.businessservice.TobyUserBankService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.TobyUserRoleService;
import com.toby.businessservice.TobyUserYearService;
import com.toby.businessservice.RoleService;
import com.toby.businessservice.TobyUserProproductionService;
import com.toby.converter.GlBankConverter;
import com.toby.converter.GlYearConverter;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.ProProductionStagesConverter;
import com.toby.converter.TobyRoleConverter;
import com.toby.converter.TobyUserConverter;
import com.toby.entity.Branch;
import com.toby.entity.GlBank;
import com.toby.entity.GlYear;
import com.toby.entity.InvInventory;
import com.toby.entity.ProProductionStages;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyRole;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserBank;
import com.toby.entity.TobyUserInventory;
import com.toby.entity.TobyUserProproduction;
import com.toby.entity.TobyUserRole;
import com.toby.entity.TobyUserYear;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
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
import org.primefaces.event.SelectEvent;

/**
 *
 * @author hq002
 */
public class UserRoleFormBean extends BaseFormBean implements Serializable {

    private List<Branch> branchList = new ArrayList<>();
    private List<TobyRole> roleListByCompany = new ArrayList<>();
    private List<TobyUser> userListByCompany = new ArrayList<>();
    private List<TobyUserRole> userRoleList = new ArrayList<>();
    private TobyUserConverter tobyUserConverter;
    private TobyRoleConverter tobyRoleConverter;
    private Branch selectedBranch;
    private List<TobyCompany> companiesList;
    private Integer selectedCompanyId;
    private TobyUserRole selectedUserRole;
    private List<GlYear> yearsList = new ArrayList<>();
    private List<GlYear> yearsListSelectionOld = new ArrayList<>();

    private List<GlBank> bankList = new ArrayList<>();
    private List<GlBank> glBankOld = new ArrayList<>();

    private List<ProProductionStages> productionStagesList = new ArrayList<>();
    private List<ProProductionStages> productionStagesListOld = new ArrayList<>();
    private List<ProProductionStages> productionStagesListDeleted = new ArrayList<>();
    private List<TobyUserProproduction> tobyUserProProductionStagesListAdded = new ArrayList<>();
    private ProProductionStagesConverter proProductionStagesConverter;

    private List<InvInventory> inventoryList = new ArrayList<>();
    private List<InvInventory> inventoryOldList = new ArrayList<>();
    private List<InvInventory> inventoryListDeleted = new ArrayList<>();
    private List<TobyUserInventory> tobyUserInventoryListAdded = new ArrayList<>();
    private InvInventoryConverter inventoryConverter;

    private List<InvInventory> gallaryList = new ArrayList<>();
    private List<InvInventory> gallaryOldList = new ArrayList<>();
    private List<InvInventory> gallaryListDeleted = new ArrayList<>();
    private List<TobyUserInventory> tobyUsergallaryListAdded = new ArrayList<>();
    private InvInventoryConverter gallaryConverter;

    private List<TobyUserYear> tobyeUserYearList = new ArrayList<>();
    private List<TobyUserYear> tobyeUserYearSamplList = new ArrayList<>();

    private List<GlYear> yearsListDeleted = new ArrayList<>();

    private List<GlBank> glBankListDeleted = new ArrayList<>();
    private List<TobyUserYear> TobyUserYearListAdded = new ArrayList<>();
    private List<TobyUserBank> tobyUserBankListAdded = new ArrayList<>();

    private GlYearConverter glYearConverter;
    private GlBankConverter glBankConverter;

    @EJB
    BranchService branchService;
    @EJB
    RoleService roleService;
    @EJB
    TobyUserService tobyUserService;
    @EJB
    TobyUserRoleService tobyUserRoleService;
    @EJB
    CompanyService companyService;
    @EJB
    TobyUserYearService tobyUserYearService;
    @EJB
    GlYearService glYearService;

    @EJB
    GlBankService glBankService;
    @EJB
    ProProductionStagesService proProductionStagesService;
    @EJB
    TobyUserBankService tobyUserBankService;
    @EJB
    TobyUserProproductionService tobyUserProproductionService;
    @EJB
    TobyUserInventoryService tobyUserInventoryService;
    @EJB
    InvInventoryService invInventoryService;

    private TobyCompany companyId;
    Integer branchId;

    public UserRoleFormBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "init");
        }
    }

    @Override
    public void load() {
        try {
//        setUserroleList(tobyUserRoleService.findUserRoleByUserId(getUserlogin().getId()));
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            companyId = getUserData().getCompany();
            if (companyId != null && companyId.getId() != null) {
                //  get all Branches for user company
                branchList = branchService.getAllBranchByCompanyId(companyId.getId());
                userListByCompany = tobyUserService.getAllUsersListByCompanyId(companyId.getId());
                roleListByCompany = roleService.getAllRolesListByCompanyId(companyId.getId());

                // yearsList = glYearService.getALLGlyearByCompanyId(companyId.getId());
                yearsList = glYearService.getALLGlyearByBranchId(getUserData().getUserBranch().getId());
                bankList = glBankService.getAllGlBankByBranchId(getUserData().getUserBranch().getId());
                productionStagesList = proProductionStagesService.getAllProproductionStagesByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());

                inventoryList = invInventoryService.getALLInventoriesByBranchByType(getUserData().getUserBranch().getId(),0);
                gallaryList = invInventoryService.getALLInventoriesByBranchByType(getUserData().getUserBranch().getId(),2);
            } else {
                companiesList = companyService.getAllCompanies();

                yearsList = glYearService.getAllYear();
                bankList = glBankService.getAllGlBank();
                inventoryList = invInventoryService.getALLInventoriesByType(0);
                gallaryList = invInventoryService.getALLInventoriesByType(1);

            }

            glYearConverter = new GlYearConverter(yearsList);
            glBankConverter = new GlBankConverter(bankList);
            inventoryConverter = new InvInventoryConverter(inventoryList);
            gallaryConverter = new InvInventoryConverter(gallaryList);
            proProductionStagesConverter = new ProProductionStagesConverter(productionStagesList);
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "load");
        }

    }

    public void addUserWithRole() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            TobyUserRole tobyUserRole = selectedUserRole;
            tobyUserRole.setBranchId(selectedBranch);
            tobyUserRole.setModificationDate(new Date());
            tobyUserRole.setModifiedBy(getUserData().getUser());
            tobyUserRole.setCompanyId(companyId);
            tobyUserRole.setMarkEdit(Boolean.FALSE);
            updateAndDelete();
            updateOrDeleteBanks();
            updateOrDeleteInventories();
            updateOrDeleteGallaries();
            updateOrDeleteProproduction();
            try {
                tobyUserRoleService.addUserRole(tobyUserRole, yearsListDeleted, TobyUserYearListAdded,
                        glBankListDeleted, tobyUserBankListAdded,
                        productionStagesListDeleted, getTobyUserProProductionStagesListAdded(),
                        inventoryListDeleted, tobyUserInventoryListAdded,
                        gallaryListDeleted, tobyUsergallaryListAdded);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));

            } catch (Exception e) {
                selectedUserRole.setMarkEdit(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                saveError(e, "UserRoleFormBean", "addUserWithRole");
            }
            userRoleList = tobyUserRoleService.findUserRoleByBranch(selectedBranch.getId());
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "addUserWithRole");
        }
    }

    public void updateAndDelete() {
        try {
            yearsListDeleted = new ArrayList<>();
            TobyUserYearListAdded = new ArrayList<>();
            if (getYearsListSelectionOld() != null) {
                for (GlYear yearSelected : getYearsListSelectionOld()) {
                    if (!selectedUserRole.getGlYearList().contains(yearSelected)) {
                        yearsListDeleted.add(yearSelected);
                    }
                }
            }

            for (GlYear yearSelected : selectedUserRole.getGlYearList()) {
                if (getYearsListSelectionOld() != null) {
                    if (!getYearsListSelectionOld().contains(yearSelected)) {
                        fillTobyUserYearListAdded(yearSelected);
                    }
                } else {
                    fillTobyUserYearListAdded(yearSelected);
                }

            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "updateAndDelete");
        }
    }

    public void updateOrDeleteBanks() {
        try {

            glBankListDeleted = new ArrayList<>();
            tobyUserBankListAdded = new ArrayList<>();

            if (glBankOld != null) {
                for (GlBank bank : glBankOld) {
                    if (!selectedUserRole.getGlBankList().contains(bank)) {
                        glBankListDeleted.add(bank);
                    }
                }
            }

            for (GlBank bank : selectedUserRole.getGlBankList()) {
                if (glBankOld != null) {
                    if (!glBankOld.contains(bank)) {
                        fillTobyUserBankListAdded(bank);
                    }
                } else {
                    fillTobyUserBankListAdded(bank);
                }

            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "updateOrDeleteBanks");
        }
    }

    public void updateOrDeleteProproduction() {
        try {

            productionStagesListDeleted = new ArrayList<>();
            setTobyUserProProductionStagesListAdded(new ArrayList<>());

            if (productionStagesListOld != null) {
                for (ProProductionStages stagesDTO : productionStagesListOld) {
                    if (!selectedUserRole.getProductionStagesList().contains(stagesDTO)) {
                        productionStagesListDeleted.add(stagesDTO);
                    }
                }
            }

            for (ProProductionStages stages : selectedUserRole.getProductionStagesList()) {
                if (productionStagesListOld != null) {
                    if (!productionStagesListOld.contains(stages)) {
                        fillTobyUserProproductionListAdded(stages);
                    }
                } else {
                    fillTobyUserProproductionListAdded(stages);
                }

            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "updateOrDeleteProproduction");
        }
    }

    public void updateOrDeleteInventories() {
        try {

            inventoryListDeleted = new ArrayList<>();
            tobyUserInventoryListAdded = new ArrayList<>();

            if (inventoryOldList != null) {
                for (InvInventory inventory : inventoryOldList) {
                    if (!selectedUserRole.getInventoryList().contains(inventory)) {
                        inventoryListDeleted.add(inventory);
                    }
                }
            }

            for (InvInventory inventory : selectedUserRole.getInventoryList()) {
                if (inventoryOldList != null) {
                    if (!inventoryOldList.contains(inventory)) {
                        fillTobyUserInventoryListAdded(inventory);
                    }
                } else {
                    fillTobyUserInventoryListAdded(inventory);
                }

            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "updateOrDeleteInventories");
        }
    }

    public void fillTobyUserInventoryListAdded(InvInventory inventory) {
        try {
            TobyUserInventory tobyUserInventory = new TobyUserInventory();
            tobyUserInventory.setBranchId(selectedBranch);
            tobyUserInventory.setCompanyId(getUserData().getCompany());
            tobyUserInventory.setCreatedBy(getUserData().getUser());
            tobyUserInventory.setCreationDate(new Date());
            tobyUserInventory.setUserId(selectedUserRole.getUserId());
            tobyUserInventory.setInvInventoryId(inventory);
            tobyUserInventoryListAdded.add(tobyUserInventory);
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "fillTobyUserInventoryListAdded");
        }
    }
    
    public void updateOrDeleteGallaries() {
        try {

            gallaryListDeleted = new ArrayList<>();
            tobyUsergallaryListAdded = new ArrayList<>();

            if (gallaryOldList != null) {
                for (InvInventory inventory : gallaryOldList) {
                    if (!selectedUserRole.getGallaryList().contains(inventory)) {
                        gallaryListDeleted.add(inventory);
                    }
                }
            }

            for (InvInventory inventory : selectedUserRole.getGallaryList()) {
                if (gallaryOldList != null) {
                    if (!gallaryOldList.contains(inventory)) {
                        fillTobyUsergallaryListAdded(inventory);
                    }
                } else {
                    fillTobyUsergallaryListAdded(inventory);
                }

            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "updateOrDeleteInventories");
        }
    }

    public void fillTobyUsergallaryListAdded(InvInventory inventory) {
        try {
            TobyUserInventory tobyUserInventory = new TobyUserInventory();
            tobyUserInventory.setBranchId(selectedBranch);
            tobyUserInventory.setCompanyId(getUserData().getCompany());
            tobyUserInventory.setCreatedBy(getUserData().getUser());
            tobyUserInventory.setCreationDate(new Date());
            tobyUserInventory.setUserId(selectedUserRole.getUserId());
            tobyUserInventory.setInvInventoryId(inventory);
            tobyUsergallaryListAdded.add(tobyUserInventory);
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "fillTobyUserInventoryListAdded");
        }
    }

    public void fillTobyUserBankListAdded(GlBank bank) {
        try {
            TobyUserBank userBank = new TobyUserBank();
            userBank.setBranchId(selectedBranch);
            userBank.setCompanyId(getUserData().getCompany());
            userBank.setCreatedBy(getUserData().getUser());
            userBank.setCreationDate(new Date());
            userBank.setUserId(selectedUserRole.getUserId());
            userBank.setBankId(bank);

            tobyUserBankListAdded.add(userBank);
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "fillTobyUserBankListAdded");
        }
    }

    public void fillTobyUserProproductionListAdded(ProProductionStages productionStages) {
        try {
            TobyUserProproduction userProproduction = new TobyUserProproduction();
            userProproduction.setBranchId(selectedBranch);
            userProproduction.setCompanyId(getUserData().getCompany());
            userProproduction.setCreatedBy(getUserData().getUser());
            userProproduction.setUserId(selectedUserRole.getUserId());
            userProproduction.setProproductionId(productionStages);

            getTobyUserProProductionStagesListAdded().add(userProproduction);
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "fillTobyUserProproductionListAdded");
        }
    }

    public void fillTobyUserYearListAdded(GlYear yearSelected) {
        try {
            TobyUserYear tobyUserYear = new TobyUserYear();
            tobyUserYear.setBranchId(selectedBranch);
            tobyUserYear.setCompanyId(getUserData().getCompany());
            tobyUserYear.setCreatedBy(getUserData().getUser());
            tobyUserYear.setCreationDate(new Date());
            tobyUserYear.setUserId(selectedUserRole.getUserId());
            tobyUserYear.setYearId(yearSelected);
            TobyUserYearListAdded.add(tobyUserYear);
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "fillTobyUserYearListAdded");
        }
    }

    public void selectUserRole(SelectEvent event) {
        try {
            selectedBranch = (Branch) event.getObject();
            System.out.println(selectedBranch.getId() + " " + selectedBranch.getNameAr() + " " + selectedBranch.getNameEn());
            if (selectedBranch != null) {
                userRoleList = tobyUserRoleService.findUserRoleByBranch(selectedBranch.getId());
            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "selectUserRole");
        }
    }

    public void edit() {
        try {
            for (TobyUserRole tobyUserRole : userRoleList) {
                tobyUserRole.setMarkEdit(Boolean.FALSE);
            }
            selectedUserRole.setMarkEdit(Boolean.TRUE);

            yearsListSelectionOld = tobyUserRoleService.findUserRoleWithYear(selectedUserRole.getId()).getGlYearList();
            glBankOld = tobyUserBankService.getAllBankListByUserAndBranch(selectedUserRole.getUserId().getId(), selectedUserRole.getBranchId().getId());
            inventoryOldList = tobyUserInventoryService.getAllInventroisByUserAndBranchPerByType(selectedUserRole.getUserId().getId(), selectedUserRole.getBranchId().getId(),0);
            gallaryOldList   = tobyUserInventoryService.getAllInventroisByUserAndBranchPerByType(selectedUserRole.getUserId().getId(), selectedUserRole.getBranchId().getId(),2);
            productionStagesListOld = proProductionStagesService.getAllProproductionListByUserAndBranch(selectedUserRole.getUserId(), selectedUserRole.getBranchId().getId());
       
            selectedUserRole.setGlYearList(yearsListSelectionOld);
            selectedUserRole.setGlBankList(glBankOld);
            selectedUserRole.setInventoryList(inventoryOldList);
            selectedUserRole.setGallaryList(gallaryOldList);
            selectedUserRole.setProductionStagesList(productionStagesListOld);

        
        
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "edit");
        }
    }

    public void reload() {
        try {
            selectedUserRole.setMarkEdit(Boolean.FALSE);
            if (selectedUserRole.getId() == null) {
                userRoleList.remove(selectedUserRole);
            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "reload");
        }
    }

    public void onCompanyListChange() {
        try {
            if (selectedCompanyId != null) {
                companyId = companyService.findCompany(selectedCompanyId);
                branchList = branchService.getAllBranchByCompanyId(selectedCompanyId);
                userListByCompany = tobyUserService.getAllUsersListByCompanyId(selectedCompanyId);
                roleListByCompany = roleService.getAllRolesListByCompanyId(selectedCompanyId);
            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "onCompanyListChange");
        }

    }

    public void addRow() {
        try {
            if (selectedBranch != null) {
                TobyUserRole tobyUserRole = new TobyUserRole();
                tobyUserRole.setRoleId(new TobyRole());
                tobyUserRole.setUserId(new TobyUser());
                tobyUserRole.setMarkEdit(Boolean.TRUE);
                userRoleList.add(0, tobyUserRole);
            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "addRow");
        }
    }

    @Override
    public String getScreenName() {
        return "userRoleForm";
    }

    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (selectedUserRole != null && selectedUserRole.getId() != null) {
                try {
                    tobyUserRoleService.deleteUserRoleAndUserYear(selectedUserRole);
                    userRoleList = tobyUserRoleService.findUserRoleByBranch(selectedBranch.getId());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }

            } else {
                userRoleList.remove(0);
            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "delete");
        }
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void cancel() {

    }

    public void onSelect(SelectEvent event) {
        selectedUserRole = (TobyUserRole) event.getObject();

    }

    /**
     * @return the branchList
     */
    public List<Branch> getBranchList() {
        return branchList;
    }

    /**
     * @param branchList the branchList to set
     */
    public void setBranchList(List<Branch> branchList) {
        this.branchList = branchList;
    }

    /**
     * @return the userListByCompany
     */
    public List<TobyUser> getUserListByCompany() {
        return userListByCompany;
    }

    /**
     * @param userListByCompany the userListByCompany to set
     */
    public void setUserListByCompany(List<TobyUser> userListByCompany) {
        this.userListByCompany = userListByCompany;
    }

    /**
     * @return the userRoleList
     */
    public List<TobyUserRole> getUserRoleList() {
        return userRoleList;
    }

    /**
     * @param userRoleList the userRoleList to set
     */
    public void setUserRoleList(List<TobyUserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }

    /**
     * @return the tobyUserConverter
     */
    public TobyUserConverter getTobyUserConverter() {
        return tobyUserConverter;
    }

    /**
     * @param tobyUserConverter the tobyUserConverter to set
     */
    public void setTobyUserConverter(TobyUserConverter tobyUserConverter) {
        this.tobyUserConverter = tobyUserConverter;
    }

    /**
     * @return the tobyRoleConverter
     */
    public TobyRoleConverter getTobyRoleConverter() {
        return tobyRoleConverter;
    }

    /**
     * @param tobyRoleConverter the tobyRoleConverter to set
     */
    public void setTobyRoleConverter(TobyRoleConverter tobyRoleConverter) {
        this.tobyRoleConverter = tobyRoleConverter;
    }

    public List<TobyCompany> getCompaniesList() {
        return companiesList;
    }

    public void setCompaniesList(List<TobyCompany> companiesList) {
        this.companiesList = companiesList;
    }

    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    public List<TobyRole> getRoleListByCompany() {
        return roleListByCompany;
    }

    public void setRoleListByCompany(List<TobyRole> roleListByCompany) {
        this.roleListByCompany = roleListByCompany;
    }

    public Branch getSelectedBranch() {
        return selectedBranch;
    }

    public void setSelectedBranch(Branch selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    public TobyUserRole getSelectedUserRole() {
        return selectedUserRole;
    }

    public void setSelectedUserRole(TobyUserRole selectedUserRole) {
        this.selectedUserRole = selectedUserRole;
    }

    /**
     * @return the yearsList
     */
    public List<GlYear> getYearsList() {
        return yearsList;
    }

    /**
     * @param yearsList the yearsList to set
     */
    public void setYearsList(List<GlYear> yearsList) {
        this.yearsList = yearsList;
    }

    /**
     * @return the glYearConverter
     */
    public GlYearConverter getGlYearConverter() {
        return glYearConverter;
    }

    /**
     * @param glYearConverter the glYearConverter to set
     */
    public void setGlYearConverter(GlYearConverter glYearConverter) {
        this.glYearConverter = glYearConverter;
    }

    /**
     * @return the yearsListDeleted
     */
    public List<GlYear> getYearsListDeleted() {
        return yearsListDeleted;
    }

    /**
     * @param yearsListDeleted the yearsListDeleted to set
     */
    public void setYearsListDeleted(List<GlYear> yearsListDeleted) {
        this.yearsListDeleted = yearsListDeleted;
    }

    /**
     * @return the tobyeUserYearList
     */
    public List<TobyUserYear> getTobyeUserYearList() {
        return tobyeUserYearList;
    }

    /**
     * @param tobyeUserYearList the tobyeUserYearList to set
     */
    public void setTobyeUserYearList(List<TobyUserYear> tobyeUserYearList) {
        this.tobyeUserYearList = tobyeUserYearList;
    }

    /**
     * @return the tobyeUserYearSamplList
     */
    public List<TobyUserYear> getTobyeUserYearSamplList() {
        return tobyeUserYearSamplList;
    }

    /**
     * @param tobyeUserYearSamplList the tobyeUserYearSamplList to set
     */
    public void setTobyeUserYearSamplList(List<TobyUserYear> tobyeUserYearSamplList) {
        this.tobyeUserYearSamplList = tobyeUserYearSamplList;
    }

    /**
     * @return the yearsListSelectionOld
     */
    public List<GlYear> getYearsListSelectionOld() {
        return yearsListSelectionOld;
    }

    /**
     * @param yearsListSelectionOld the yearsListSelectionOld to set
     */
    public void setYearsListSelectionOld(List<GlYear> yearsListSelectionOld) {
        this.yearsListSelectionOld = yearsListSelectionOld;
    }

    /**
     * @return the TobyUserYearListAdded
     */
    public List<TobyUserYear> getTobyUserYearListAdded() {
        return TobyUserYearListAdded;
    }

    /**
     * @param TobyUserYearListAdded the TobyUserYearListAdded to set
     */
    public void setTobyUserYearListAdded(List<TobyUserYear> TobyUserYearListAdded) {
        this.TobyUserYearListAdded = TobyUserYearListAdded;
    }

    public List<GlBank> getGlBankOld() {
        return glBankOld;
    }

    public void setGlBankOld(List<GlBank> glBankOld) {
        this.glBankOld = glBankOld;
    }

    public List<GlBank> getBankList() {
        return bankList;
    }

    public void setBankList(List<GlBank> bankList) {
        this.bankList = bankList;
    }

    public GlBankConverter getGlBankConverter() {
        return glBankConverter;
    }

    public void setGlBankConverter(GlBankConverter glBankConverter) {
        this.glBankConverter = glBankConverter;
    }

    public List<GlBank> getGlBankListDeleted() {
        return glBankListDeleted;
    }

    public void setGlBankListDeleted(List<GlBank> glBankListDeleted) {
        this.glBankListDeleted = glBankListDeleted;
    }

    public List<TobyUserBank> getTobyUserBankListAdded() {
        return tobyUserBankListAdded;
    }

    public void setTobyUserBankListAdded(List<TobyUserBank> tobyUserBankListAdded) {
        this.tobyUserBankListAdded = tobyUserBankListAdded;
    }

    public List<InvInventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<InvInventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public List<InvInventory> getInventoryOldList() {
        return inventoryOldList;
    }

    public void setInventoryOldList(List<InvInventory> inventoryOldList) {
        this.inventoryOldList = inventoryOldList;
    }

    public List<InvInventory> getInventoryListDeleted() {
        return inventoryListDeleted;
    }

    public void setInventoryListDeleted(List<InvInventory> inventoryListDeleted) {
        this.inventoryListDeleted = inventoryListDeleted;
    }

    public List<TobyUserInventory> getTobyUserInventoryListAdded() {
        return tobyUserInventoryListAdded;
    }

    public void setTobyUserInventoryListAdded(List<TobyUserInventory> tobyUserInventoryListAdded) {
        this.tobyUserInventoryListAdded = tobyUserInventoryListAdded;
    }

    public InvInventoryConverter getInventoryConverter() {
        return inventoryConverter;
    }

    public void setInventoryConverter(InvInventoryConverter inventoryConverter) {
        this.inventoryConverter = inventoryConverter;
    }

    /**
     * @return the productionStagesList
     */
    public List<ProProductionStages> getProductionStagesList() {
        return productionStagesList;
    }

    /**
     * @param productionStagesList the productionStagesList to set
     */
    public void setProductionStagesList(List<ProProductionStages> productionStagesList) {
        this.productionStagesList = productionStagesList;
    }

    /**
     * @return the productionStagesListOld
     */
    public List<ProProductionStages> getProductionStagesListOld() {
        return productionStagesListOld;
    }

    /**
     * @param productionStagesListOld the productionStagesListOld to set
     */
    public void setProductionStagesListOld(List<ProProductionStages> productionStagesListOld) {
        this.productionStagesListOld = productionStagesListOld;
    }

    /**
     * @return the productionStagesListDeleted
     */
    public List<ProProductionStages> getProductionStagesListDeleted() {
        return productionStagesListDeleted;
    }

    /**
     * @param productionStagesListDeleted the productionStagesListDeleted to set
     */
    public void setProductionStagesListDeleted(List<ProProductionStages> productionStagesListDeleted) {
        this.productionStagesListDeleted = productionStagesListDeleted;
    }

    /**
     * @return the tobyUserProProductionStagesListAdded
     */
    public List<TobyUserProproduction> getTobyUserProProductionStagesListAdded() {
        return tobyUserProProductionStagesListAdded;
    }

    /**
     * @param tobyUserProProductionStagesListAdded the
     * tobyUserProProductionStagesListAdded to set
     */
    public void setTobyUserProProductionStagesListAdded(List<TobyUserProproduction> tobyUserProProductionStagesListAdded) {
        this.tobyUserProProductionStagesListAdded = tobyUserProProductionStagesListAdded;
    }

    /**
     * @return the proProductionStagesConverter
     */
    public ProProductionStagesConverter getProProductionStagesConverter() {
        return proProductionStagesConverter;
    }

    /**
     * @param proProductionStagesConverter the proProductionStagesConverter to
     * set
     */
    public void setProProductionStagesConverter(ProProductionStagesConverter proProductionStagesConverter) {
        this.proProductionStagesConverter = proProductionStagesConverter;
    }

    /**
     * @return the gallaryList
     */
    public List<InvInventory> getGallaryList() {
        return gallaryList;
    }

    /**
     * @param gallaryList the gallaryList to set
     */
    public void setGallaryList(List<InvInventory> gallaryList) {
        this.gallaryList = gallaryList;
    }

    /**
     * @return the gallaryOldList
     */
    public List<InvInventory> getGallaryOldList() {
        return gallaryOldList;
    }

    /**
     * @param gallaryOldList the gallaryOldList to set
     */
    public void setGallaryOldList(List<InvInventory> gallaryOldList) {
        this.gallaryOldList = gallaryOldList;
    }

    /**
     * @return the gallaryListDeleted
     */
    public List<InvInventory> getGallaryListDeleted() {
        return gallaryListDeleted;
    }

    /**
     * @param gallaryListDeleted the gallaryListDeleted to set
     */
    public void setGallaryListDeleted(List<InvInventory> gallaryListDeleted) {
        this.gallaryListDeleted = gallaryListDeleted;
    }

    /**
     * @return the tobyUsergallaryListAdded
     */
    public List<TobyUserInventory> getTobyUsergallaryListAdded() {
        return tobyUsergallaryListAdded;
    }

    /**
     * @param tobyUsergallaryListAdded the tobyUsergallaryListAdded to set
     */
    public void setTobyUsergallaryListAdded(List<TobyUserInventory> tobyUsergallaryListAdded) {
        this.tobyUsergallaryListAdded = tobyUsergallaryListAdded;
    }

    /**
     * @return the gallaryConverter
     */
    public InvInventoryConverter getGallaryConverter() {
        return gallaryConverter;
    }

    /**
     * @param gallaryConverter the gallaryConverter to set
     */
    public void setGallaryConverter(InvInventoryConverter gallaryConverter) {
        this.gallaryConverter = gallaryConverter;
    }
}
