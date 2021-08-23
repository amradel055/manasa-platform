/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.userrole;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.CompanyService;
import com.isag.businessservice.IsagUserRoleService;
import com.isag.businessservice.RoleService;
import com.isag.converter.IsagRoleConverter;
import com.isag.converter.IsagUserConverter;
import com.isag.entity.Branch;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagRole;
import com.isag.entity.IsagUser;
import com.isag.entity.IsagUserRole;
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
import org.primefaces.event.SelectEvent;
import com.isag.businessservice.IsagUserService;

/**
 *
 * @author hq002
 */
public class UserRoleFormBean extends BaseFormBean implements Serializable {

    private List<Branch> branchList = new ArrayList<>();
    private List<IsagRole> roleListByCompany = new ArrayList<>();
    private List<IsagUser> userListByCompany = new ArrayList<>();
    private List<IsagUserRole> userRoleList = new ArrayList<>();
    private IsagUserConverter isagUserConverter;
    private IsagRoleConverter isagRoleConverter;
    private Branch selectedBranch;
    private List<IsagCompany> companiesList;
    private Integer selectedCompanyId;
    private IsagUserRole selectedUserRole;

//    private List<Employee> employeeList = new ArrayList<>();
//    private List<Employee> employeeOldList = new ArrayList<>();
//    private List<Employee> employeeListDeleted = new ArrayList<>();
//    private List<IsagUserEmployee> isagUseremployeeListAdded = new ArrayList<>();

    @EJB
    BranchService branchService;
    @EJB
    RoleService roleService;
    @EJB
    IsagUserService iSAGUserService;
    @EJB
    IsagUserRoleService isagUserRoleService;
    @EJB
    CompanyService companyService;

//    @EJB
//    EmployeeService employeeService;
//    @EJB
//    IsagUserEmployeeService isagUserEmployeeService;
    private IsagCompany companyId;
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
//        setUserroleList(isagUserRoleService.findUserRoleByUserId(getUserlogin().getId()));
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            companyId = getUserData().getCompany();
            if (companyId != null && companyId.getId() != null) {
                //  get all Branches for user company
                branchList = branchService.getAllBranchByCompanyId(companyId.getId());
                userListByCompany = iSAGUserService.getAllUsersListByCompanyId(companyId.getId());
                roleListByCompany = roleService.getAllRolesListByCompanyId(companyId.getId());

                // yearsList = glYearService.getALLGlyearByCompanyId(companyId.getId());
//            employeeList = employeeService.getAllEmployeesListByBranchId(getUserData().getUserBranch().getId());
            } else {
                companiesList = companyService.getAllCompanies();

//            yearsList = glYearService.getAllYear();
//            bankList = glBankService.getAllGlBank();
//            inventoryList = invInventoryService.getAllInventories();
            }

//        employeeConverter = new EmployeeConverter(employeeList);
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "load");
        }

    }

    public void addUserWithRole() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            IsagUserRole isagUserRole = selectedUserRole;
            isagUserRole.setBranchId(selectedBranch);
            isagUserRole.setModificationDate(new Date());
            isagUserRole.setModifiedBy(getUserData().getUser());
            isagUserRole.setCompanyId(companyId);
            isagUserRole.setMarkEdit(Boolean.FALSE);
            updateAndDelete();
            updateOrDeleteBanks();
            updateOrDeleteInventories();
//        updateOrDeleteEmployees();
            try {
            isagUserRoleService.addUserRole(isagUserRole);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));

            } catch (Exception e) {
                selectedUserRole.setMarkEdit(Boolean.TRUE);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                saveError(e, "UserRoleFormBean", "addUserWithRole");
            }
            userRoleList = isagUserRoleService.findUserRoleByBranch(selectedBranch.getId());
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "addUserWithRole");
        }
    }

    public void updateAndDelete() {
        try {
//            yearsListDeleted = new ArrayList<>();
//        IsagUserYearListAdded = new ArrayList<>();
//        if (getYearsListSelectionOld() != null) {
//            for (GlYear yearSelected : getYearsListSelectionOld()) {
//                if (!selectedUserRole.getGlYearList().contains(yearSelected)) {
//                    yearsListDeleted.add(yearSelected);
//                }
//            }
//        }
//
//        for (GlYear yearSelected : selectedUserRole.getGlYearList()) {
//            if (getYearsListSelectionOld() != null) {
//                if (!getYearsListSelectionOld().contains(yearSelected)) {
//                    fillIsagUserYearListAdded(yearSelected);
//                }
//            } else {
//                fillIsagUserYearListAdded(yearSelected);
//            }
//
//        }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "updateAndDelete");
        }
    }

    public void updateOrDeleteBanks() {
        try {

//        glBankListDeleted = new ArrayList<>();
//        isagUserBankListAdded = new ArrayList<>();
//
//        if (glBankOld != null) {
//            for (GlBank bank : glBankOld) {
//                if (!selectedUserRole.getGlBankList().contains(bank)) {
//                    glBankListDeleted.add(bank);
//                }
//            }
//        }
//
//        for (GlBank bank : selectedUserRole.getGlBankList()) {
//            if (glBankOld != null) {
//                if (!glBankOld.contains(bank)) {
//                    fillIsagUserBankListAdded(bank);
//                }
//            } else {
//                fillIsagUserBankListAdded(bank);
//            }
//
//        }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "updateOrDeleteBanks");
        }
    }

    public void updateOrDeleteInventories() {
        try {

//            inventoryListDeleted = new ArrayList<>();
//            isagUserInventoryListAdded = new ArrayList<>();
//
//            if (inventoryOldList != null) {
//                for (InvInventory inventory : inventoryOldList) {
//                    if (!selectedUserRole.getInventoryList().contains(inventory)) {
//                        inventoryListDeleted.add(inventory);
//                    }
//                }
//            }
//
//            for (InvInventory inventory : selectedUserRole.getInventoryList()) {
//                if (inventoryOldList != null) {
//                    if (!inventoryOldList.contains(inventory)) {
//                        fillIsagUserInventoryListAdded(inventory);
//                    }
//                } else {
//                    fillIsagUserInventoryListAdded(inventory);
//                }
//
//            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "updateOrDeleteInventories");
        }
    }

//    public void fillIsagUserInventoryListAdded(InvInventory inventory) {
//        try {
//            IsagUserInventory isagUserInventory = new IsagUserInventory();
//            isagUserInventory.setBranchId(selectedBranch);
//            isagUserInventory.setCompanyId(getUserData().getCompany());
//            isagUserInventory.setCreatedBy(getUserData().getUser());
//            isagUserInventory.setCreationDate(new Date());
//            isagUserInventory.setUserId(selectedUserRole.getUserId());
//            isagUserInventory.setInvInventoryId(inventory);
//            isagUserInventoryListAdded.add(isagUserInventory);
//        } catch (Exception e) {
//            saveError(e, "UserRoleFormBean", "fillIsagUserInventoryListAdded");
//        }
//    }
//    
//    public void updateOrDeleteEmployees() {
//        try {
//
//        employeeListDeleted = new ArrayList<>();
//        isagUseremployeeListAdded = new ArrayList<>();
//
//        if (employeeOldList != null) {
//            for ( Employee employee : employeeOldList) {
//                if (!selectedUserRole.getEmployeeList().contains(employee)) {
//                    employeeListDeleted.add(employee);
//                }
//            }
//        }
//
//        for (Employee employee : selectedUserRole.getEmployeeList()) {
//            if (employeeOldList != null) {
//                if (!employeeOldList.contains(employee)) {
//                    fillIsagUserEmployeeListAdded(employee);
//                }
//            } else {
//                fillIsagUserEmployeeListAdded(employee);
//            }
//
//        }
//         } catch (Exception e) {
//            saveError(e, "UserRoleFormBean", "updateOrDeleteInventories");
//        }
//    }

//    public void fillIsagUserEmployeeListAdded(Employee employee) {
//        try {
//        IsagUserEmployee isagUserEmployee = new IsagUserEmployee();
//        isagUserEmployee.setBranchId(selectedBranch);
//        isagUserEmployee.setCompanyId(getUserData().getCompany());
//        isagUserEmployee.setCreatedBy(getUserData().getUser());
//        isagUserEmployee.setCreationDate(new Date());
//        isagUserEmployee.setUserId(selectedUserRole.getUserId());
//        isagUserEmployee.setEmployee(employee);
//        isagUseremployeeListAdded.add(isagUserEmployee);
//         } catch (Exception e) {
//            saveError(e, "UserRoleFormBean", "fillIsagUserEmployeeListAdded");
//        }
//    }
//    public void fillIsagUserBankListAdded(GlBank bank) {
//        try {
//            IsagUserBank userBank = new IsagUserBank();
//            userBank.setBranchId(selectedBranch);
//            userBank.setCompanyId(getUserData().getCompany());
//            userBank.setCreatedBy(getUserData().getUser());
//            userBank.setCreationDate(new Date());
//            userBank.setUserId(selectedUserRole.getUserId());
//            userBank.setBankId(bank);
//
//            isagUserBankListAdded.add(userBank);
//        } catch (Exception e) {
//            saveError(e, "UserRoleFormBean", "fillIsagUserBankListAdded");
//        }
//    }

//    public void fillIsagUserYearListAdded(GlYear yearSelected) {
//        try {
//            IsagUserYear isagUserYear = new IsagUserYear();
//            isagUserYear.setBranchId(selectedBranch);
//            isagUserYear.setCompanyId(getUserData().getCompany());
//            isagUserYear.setCreatedBy(getUserData().getUser());
//            isagUserYear.setCreationDate(new Date());
//            isagUserYear.setUserId(selectedUserRole.getUserId());
//            isagUserYear.setYearId(yearSelected);
//            IsagUserYearListAdded.add(isagUserYear);
//        } catch (Exception e) {
//            saveError(e, "UserRoleFormBean", "fillIsagUserYearListAdded");
//        }
//    }

    public void selectUserRole(SelectEvent event) {
        try {
            selectedBranch = (Branch) event.getObject();
            System.out.println(selectedBranch.getId() + " " + selectedBranch.getNameAr() + " " + selectedBranch.getNameEn());
            if (selectedBranch != null) {
                userRoleList = isagUserRoleService.findUserRoleByBranch(selectedBranch.getId());
            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "selectUserRole");
        }
    }

    public void edit() {
        try {
            for (IsagUserRole isagUserRole : userRoleList) {
                isagUserRole.setMarkEdit(Boolean.FALSE);
            }
            selectedUserRole.setMarkEdit(Boolean.TRUE);

//            yearsListSelectionOld = isagUserRoleService.findUserRoleWithYear(selectedUserRole.getId()).getGlYearList();
//            glBankOld = isagUserBankService.getAllBankListByUserAndBranch(selectedUserRole.getUserId().getId(), selectedUserRole.getBranchId().getId());
//            inventoryOldList = isagUserInventoryService.getAllInventroisByUserAndBranch(selectedUserRole.getUserId().getId(), selectedUserRole.getBranchId().getId());
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
                userListByCompany = iSAGUserService.getAllUsersListByCompanyId(selectedCompanyId);
                roleListByCompany = roleService.getAllRolesListByCompanyId(selectedCompanyId);
            }
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "onCompanyListChange");
        }

    }

    public void addRow() {
        try {
            if (selectedBranch != null) {
                IsagUserRole isagUserRole = new IsagUserRole();
                isagUserRole.setRoleId(new IsagRole());
                isagUserRole.setUserId(new IsagUser());
                isagUserRole.setMarkEdit(Boolean.TRUE);
                userRoleList.add(0, isagUserRole);
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
                    isagUserRoleService.deleteUserRoleAndUserYear(selectedUserRole);
                    userRoleList = isagUserRoleService.findUserRoleByBranch(selectedBranch.getId());
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
        selectedUserRole = (IsagUserRole) event.getObject();

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
    public List<IsagUser> getUserListByCompany() {
        return userListByCompany;
    }

    /**
     * @param userListByCompany the userListByCompany to set
     */
    public void setUserListByCompany(List<IsagUser> userListByCompany) {
        this.userListByCompany = userListByCompany;
    }

    /**
     * @return the userRoleList
     */
    public List<IsagUserRole> getUserRoleList() {
        return userRoleList;
    }

    /**
     * @param userRoleList the userRoleList to set
     */
    public void setUserRoleList(List<IsagUserRole> userRoleList) {
        this.userRoleList = userRoleList;
    }

    /**
     * @return the isagUserConverter
     */
    public IsagUserConverter getIsagUserConverter() {
        return isagUserConverter;
    }

    /**
     * @param isagUserConverter the isagUserConverter to set
     */
    public void setIsagUserConverter(IsagUserConverter isagUserConverter) {
        this.isagUserConverter = isagUserConverter;
    }

    /**
     * @return the isagRoleConverter
     */
    public IsagRoleConverter getIsagRoleConverter() {
        return isagRoleConverter;
    }

    /**
     * @param isagRoleConverter the isagRoleConverter to set
     */
    public void setIsagRoleConverter(IsagRoleConverter isagRoleConverter) {
        this.isagRoleConverter = isagRoleConverter;
    }

    public List<IsagCompany> getCompaniesList() {
        return companiesList;
    }

    public void setCompaniesList(List<IsagCompany> companiesList) {
        this.companiesList = companiesList;
    }

    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    public List<IsagRole> getRoleListByCompany() {
        return roleListByCompany;
    }

    public void setRoleListByCompany(List<IsagRole> roleListByCompany) {
        this.roleListByCompany = roleListByCompany;
    }

    public Branch getSelectedBranch() {
        return selectedBranch;
    }

    public void setSelectedBranch(Branch selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    public IsagUserRole getSelectedUserRole() {
        return selectedUserRole;
    }

    public void setSelectedUserRole(IsagUserRole selectedUserRole) {
        this.selectedUserRole = selectedUserRole;
    }

    /**
     * @return the yearsList
//     */
//    public List<GlYear> getYearsList() {
//        return yearsList;
//    }
//
//    /**
//     * @param yearsList the yearsList to set
//     */
//    public void setYearsList(List<GlYear> yearsList) {
//        this.yearsList = yearsList;
//    }


    /**
     * @return the yearsListDeleted
     */
//    public List<GlYear> getYearsListDeleted() {
//        return yearsListDeleted;
//    }
//
//    /**
//     * @param yearsListDeleted the yearsListDeleted to set
//     */
//    public void setYearsListDeleted(List<GlYear> yearsListDeleted) {
//        this.yearsListDeleted = yearsListDeleted;
//    }

    /**
     * @return the isageUserYearList
     */
//    public List<IsagUserYear> getIsageUserYearList() {
//        return isageUserYearList;
//    }
//
//    /**
//     * @param isageUserYearList the isageUserYearList to set
//     */
//    public void setIsageUserYearList(List<IsagUserYear> isageUserYearList) {
//        this.isageUserYearList = isageUserYearList;
//    }
//
//    /**
//     * @return the isageUserYearSamplList
//     */
//    public List<IsagUserYear> getIsageUserYearSamplList() {
//        return isageUserYearSamplList;
//    }

    /**
     * @param isageUserYearSamplList the isageUserYearSamplList to set
     */
//    public void setIsageUserYearSamplList(List<IsagUserYear> isageUserYearSamplList) {
//        this.isageUserYearSamplList = isageUserYearSamplList;
//    }
//
//    /**
//     * @return the yearsListSelectionOld
//     */
//    public List<GlYear> getYearsListSelectionOld() {
//        return yearsListSelectionOld;
//    }
//
//    /**
//     * @param yearsListSelectionOld the yearsListSelectionOld to set
//     */
//    public void setYearsListSelectionOld(List<GlYear> yearsListSelectionOld) {
//        this.yearsListSelectionOld = yearsListSelectionOld;
//    }
//
//    /**
//     * @return the IsagUserYearListAdded
//     */
//    public List<IsagUserYear> getIsagUserYearListAdded() {
//        return IsagUserYearListAdded;
//    }

    /**
     * @param IsagUserYearListAdded the IsagUserYearListAdded to set
     */
//    public void setIsagUserYearListAdded(List<IsagUserYear> IsagUserYearListAdded) {
//        this.IsagUserYearListAdded = IsagUserYearListAdded;
//    }
//
//    public List<GlBank> getGlBankOld() {
//        return glBankOld;
//    }
//
//    public void setGlBankOld(List<GlBank> glBankOld) {
//        this.glBankOld = glBankOld;
//    }
//
//    public List<GlBank> getBankList() {
//        return bankList;
//    }
//
//    public void setBankList(List<GlBank> bankList) {
//        this.bankList = bankList;
//    }

//    public List<GlBank> getGlBankListDeleted() {
//        return glBankListDeleted;
//    }
//
//    public void setGlBankListDeleted(List<GlBank> glBankListDeleted) {
//        this.glBankListDeleted = glBankListDeleted;
//    }
//
//    public List<IsagUserBank> getIsagUserBankListAdded() {
//        return isagUserBankListAdded;
//    }
//
//    public void setIsagUserBankListAdded(List<IsagUserBank> isagUserBankListAdded) {
//        this.isagUserBankListAdded = isagUserBankListAdded;
//    }
//
//    public List<InvInventory> getInventoryList() {
//        return inventoryList;
//    }
//
//    public void setInventoryList(List<InvInventory> inventoryList) {
//        this.inventoryList = inventoryList;
//    }

//    public List<InvInventory> getInventoryOldList() {
//        return inventoryOldList;
//    }
//
//    public void setInventoryOldList(List<InvInventory> inventoryOldList) {
//        this.inventoryOldList = inventoryOldList;
//    }
//
//    public List<InvInventory> getInventoryListDeleted() {
//        return inventoryListDeleted;
//    }
//
//    public void setInventoryListDeleted(List<InvInventory> inventoryListDeleted) {
//        this.inventoryListDeleted = inventoryListDeleted;
//    }
//
//    public List<IsagUserInventory> getIsagUserInventoryListAdded() {
//        return isagUserInventoryListAdded;
//    }
//
//    public void setIsagUserInventoryListAdded(List<IsagUserInventory> isagUserInventoryListAdded) {
//        this.isagUserInventoryListAdded = isagUserInventoryListAdded;
//    }
//
//    /**
//     * @return the isagUseremployeeListAdded
//     */
//    public List<IsagUserEmployee> getIsagUseremployeeListAdded() {
//        return isagUseremployeeListAdded;
//    }
//
//    /**
//     * @param isagUseremployeeListAdded the isagUseremployeeListAdded to set
//     */
//    public void setIsagUseremployeeListAdded(List<IsagUserEmployee> isagUseremployeeListAdded) {
//        this.isagUseremployeeListAdded = isagUseremployeeListAdded;
//    }
//
//    /**
//     * @return the employeeList
//     */
//    public List<Employee> getEmployeeList() {
//        return employeeList;
//    }
//
//    /**
//     * @param employeeList the employeeList to set
//     */
//    public void setEmployeeList(List<Employee> employeeList) {
//        this.employeeList = employeeList;
//    }
//
//    /**
//     * @return the employeeOldList
//     */
//    public List<Employee> getEmployeeOldList() {
//        return employeeOldList;
//    }
//
//    /**
//     * @param employeeOldList the employeeOldList to set
//     */
//    public void setEmployeeOldList(List<Employee> employeeOldList) {
//        this.employeeOldList = employeeOldList;
//    }
//
//    /**
//     * @return the employeeListDeleted
//     */
//    public List<Employee> getEmployeeListDeleted() {
//        return employeeListDeleted;
//    }
//
//    /**
//     * @param employeeListDeleted the employeeListDeleted to set
//     */
//    public void setEmployeeListDeleted(List<Employee> employeeListDeleted) {
//        this.employeeListDeleted = employeeListDeleted;
//    }
}
