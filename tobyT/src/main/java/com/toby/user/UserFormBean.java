/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.user;

import com.toby.businessservice.BranchService;
import com.toby.businessservice.CompanyService;
import com.toby.businessservice.EmployeeService;
import com.toby.businessservice.TobyUserService;
import com.toby.businessservice.RoleService;
import com.toby.businessservice.SymbolService;
import com.toby.converter.EmployeeDTOConverter;
import com.toby.dto.EmployeeDTO;
import com.toby.entity.Branch;
import com.toby.entity.CompanyLanguage;
import com.toby.entity.Employee;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyRole;
import com.toby.entity.TobyUser;
import com.toby.entity.Symbol;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author hq002
 */
@Named(value = "UserFormBean")
@ViewScoped
public class UserFormBean extends BaseFormBean implements Serializable {

    private String userName;
    private String screenMode;
    private String userPassword;
    private String userCode;
    private Symbol userLang;
    private String userCompanyCode;
    private List<TobyCompany> userCompany;
    private UserData userData;
    private TobyCompany company;
    private TobyRole role;
    private TobyUser userDetails;
    private Integer selectedCompanyId;
    private TobyRole roleId;
    private Integer userId;
    private Integer selectedLangId;
    private Integer master;
    private String residencyNumber;
    private List<Symbol> languages;
    private Branch branch;

    private EmployeeDTO employeeSelected;
    private List<EmployeeDTO> employeeList;

    private EmployeeDTOConverter employeeConverter;

    @EJB
    private TobyUserService userService;

    @EJB
    private RoleService roleService;

    @EJB
    private CompanyService companyService;

    @EJB
    private SymbolService symbolService;

    @EJB
    private BranchService branchService;

    @EJB
    private EmployeeService employeeService;

    /**
     * Creates a new instance of UserFormBean
     */
    public UserFormBean() {
    }

    @Override
    public String save() {
        try {
            userDetails = new TobyUser();
            if (userName != null && userPassword != null && userCode != null && selectedLangId != null) {
                userDetails.setName(userName.trim());
                userDetails.setPassword(userPassword);
                userCode = userCompanyCode + "" + userCode;
                userDetails.setUserCode(userCode);
                userDetails.setResidencyNumber(residencyNumber);
                Symbol sym = new Symbol();
                sym.setId(selectedLangId);
                userDetails.setLang(sym);
                userDetails.setMaster(getMaster());
                TobyCompany companySelected = new TobyCompany();
                companySelected.setId(selectedCompanyId);
                userDetails.setCompanyId(companySelected);

                if (employeeSelected != null && employeeSelected.getId() != null) {
                    Employee employee = new Employee();
                    employee.setId(employeeSelected.getId());
                    userDetails.setEmployeeId(employee);
                }

                if (screenMode.equalsIgnoreCase("add")) {
                    userDetails.setCreationDate(new Date());
                    userDetails.setCreatedBy(userData.getUser());
                    try {
                        Branch b = null;
                        if (userData.getSelectedBranch() != null) {
                            b = branchService.findBranch(userData.getSelectedBranch());
                        }

                        userDetails.setBranchId(b);
                        userService.addUser(userDetails);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("SAVED")));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("DUBLICATED")));
                        e.printStackTrace();
                        saveError(e, "UserFormBean", "save");
                        return null;
                    }

                } else if (screenMode.equalsIgnoreCase("edit")) {
                    userDetails.setModifiedBy(userData.getUser());
                    userDetails.setModificationDate(new Date());
                    userDetails.setBranchId(branch);
                    userDetails.setId(userId);
                    userDetails.setCompanyId(company);
                    try {
                        userService.updateUser(userDetails);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userData.getUserDDs().get("INFO"), userData.getUserDDs().get("SAVED")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userData.getUserDDs().get("ERROR"), userData.getUserDDs().get("DUBLICATED")));
                        saveError(e, "UserFormBean", "save");
                        return null;

                    }

                }
                exit("../user/userList.xhtml");
                return "";
            }
            return "";
        } catch (Exception e) {
            saveError(e, "UserFormBean", "save");
            return null;
        }

    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "UserFormBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            screenMode = (String) context.getSessionMap().get("userMode");
            if (userData.getCompany() != null) {
                selectedCompanyId = userData.getCompany().getId();
                userCompanyCode = userData.getCompany().getCode();
            }
            if (screenMode != null) {
                if (screenMode.equalsIgnoreCase("add")) {
                    setUserDetailsEmpty();

                } else if (screenMode.equalsIgnoreCase("edit")) {
                    userId = (Integer) (context.getSessionMap().get("selecteduser"));
                    if (userId != null) {
                        try {
                            userDetails = userService.findUser(userId);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (userDetails != null) {
                            setUserDetails();
                        }
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "UserFormBean", "load");
        }

    }

    public void onCompanyListChange() {
        try {
            userCompanyCode = "";
            if (selectedCompanyId != null) {
                List<CompanyLanguage> companyLanguages = companyService.getCompanyLanguages(selectedCompanyId);
                languages = new ArrayList<>();
                for (CompanyLanguage companyLanguage : companyLanguages) {
                    languages.add(companyLanguage.getSymbol());
                }
                for (TobyCompany usercom : userCompany) {
                    if (usercom.getId().equals(selectedCompanyId)) {
                        setUserCompanyCode(usercom.getCode());

                    }
                }
                RequestContext.getCurrentInstance().update("form:lan");
                RequestContext.getCurrentInstance().update("form:code");
            }
        } catch (Exception e) {
            saveError(e, "UserFormBean", "onCompanyListChange");
        }
    }

    public void onLanguageListChange() {
        try {
            if (languages != null) {
                for (Symbol userLanguage : languages) {
                    if (userLanguage.getId().equals(selectedLangId)) {
                        Symbol sym = new Symbol();
                        sym.setId(selectedLangId);
                        setUserLang(sym);

                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "UserFormBean", "onLanguageListChange");
        }
    }

    public void setUserDetailsEmpty() {
        try {
            // set User details Empty if screenMode Add
            userCompany = new ArrayList<>();
            languages = new ArrayList<>();
            userCode = "";
            userName = " ";
            userPassword = "";
            if (selectedCompanyId != null) {
                List<CompanyLanguage> companyLanguages = companyService.getCompanyLanguages(selectedCompanyId);
                for (CompanyLanguage companyLanguage : companyLanguages) {
                    languages.add(companyLanguage.getSymbol());
                }
                userCompany.add(userData.getCompany());
                //selectedCompanyId = userData.getCompany().getId();
            } else {
                try {
                    userCompanyCode = "";
                    languages = symbolService.getLanguagesWhereCompanyNull();
                    userCompany = companyService.getAllCompanies();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            saveError(e, "UserFormBean", "setUserDetailsEmpty");
        }
    }

    public void setUserDetails() {
        try {
            // set user details if screenMode Edit
            userCompany = new ArrayList<>();
            languages = new ArrayList<>();
            company = userData.getCompany();
            if (company != null && company.getId() != null) {
                branch = userDetails.getBranchId();
                userName = userDetails.getName();
                userPassword = userDetails.getPassword();
                userCode = userDetails.getUserCode().substring(3);
                if (userDetails.getEmployeeId() != null) {
                    EmployeeDTO employeeDTO = new EmployeeDTO();
                    employeeDTO.setId(userDetails.getEmployeeId().getId());
                    employeeDTO.setCode(userDetails.getEmployeeId().getCode());
                    employeeDTO.setName(userDetails.getEmployeeId().getName());
                    employeeSelected = employeeDTO;
                }

                List<CompanyLanguage> companyLanguages = companyService.getCompanyLanguages(selectedCompanyId);
                for (CompanyLanguage companyLanguage : companyLanguages) {
                    languages.add(companyLanguage.getSymbol());
                }
                userCompany.add(company);
                userCompanyCode = company.getCode();
                selectedCompanyId = userDetails.getCompanyId().getId();
                selectedLangId = userDetails.getLang().getId();

            } else {
                // this our user admin show all details
                branch = userDetails.getBranchId();
                userName = userDetails.getName();
                userPassword = userDetails.getPassword();
                userCode = userDetails.getUserCode();

                if (userDetails.getEmployeeId() != null) {
                    EmployeeDTO employeeDTO = new EmployeeDTO();
                    employeeDTO.setId(userDetails.getEmployeeId().getId());
                    employeeDTO.setCode(userDetails.getEmployeeId().getCode());
                    employeeDTO.setName(userDetails.getEmployeeId().getName());
                    employeeSelected = employeeDTO;
                }
//            userLang = userDetails.getLang();
                try {
                    languages = symbolService.getLanguagesWhereCompanyNull();
                    userCompany = companyService.getAllCompanies();
                    if (userDetails.getCompanyId().getId() != null) {
                        selectedCompanyId = userDetails.getCompanyId().getId();
                    }
                    selectedLangId = userDetails.getLang().getId();

                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            saveError(e, "UserFormBean", "setUserDetails");
        }
    }

    public String cancel() {
        try {
            exit("../user/userList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "UserFormBean", "cancel");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        return "user";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Symbol getUserLang() {
        return userLang;
    }

    public void setUserLang(Symbol userLang) {
        this.userLang = userLang;
    }

    public String getUserCompanyCode() {
        return userCompanyCode;
    }

    public void setUserCompanyCode(String userCompanyCode) {
        this.userCompanyCode = userCompanyCode;
    }

    public List<TobyCompany> getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(List<TobyCompany> userCompanies) {
        this.userCompany = userCompanies;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public TobyCompany getCompany() {
        return company;
    }

    public void setCompany(TobyCompany company) {
        this.company = company;
    }

    public TobyRole getRole() {
        return role;
    }

    public void setRole(TobyRole role) {
        this.role = role;
    }

    public TobyUser getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(TobyUser userDetails) {
        this.userDetails = userDetails;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Integer CompanyId) {
        this.selectedCompanyId = CompanyId;
    }

    public TobyRole getRoleId() {
        return roleId;
    }

    public void setRoleId(TobyRole roleId) {
        this.roleId = roleId;
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setLanguages(List<Symbol> languages) {
        this.languages = languages;
    }

    public List<Symbol> getLanguages() {
        return languages;
    }

    public Integer getSelectedLangId() {
        return selectedLangId;
    }

    public void setSelectedLangId(Integer selectedLangId) {
        this.selectedLangId = selectedLangId;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * @return the master
     */
    public Integer getMaster() {
        return master;
    }

    /**
     * @param master the master to set
     */
    public void setMaster(Integer master) {
        this.master = master;
    }

    /**
     * @return the residencyNumber
     */
    public String getResidencyNumber() {
        return residencyNumber;
    }

    /**
     * @param residencyNumber the residencyNumber to set
     */
    public void setResidencyNumber(String residencyNumber) {
        this.residencyNumber = residencyNumber;
    }

    /**
     * @return the employeeConverter
     */
    public EmployeeDTOConverter getEmployeeConverter() {
        return employeeConverter;
    }

    /**
     * @param employeeConverter the employeeConverter to set
     */
    public void setEmployeeConverter(EmployeeDTOConverter employeeConverter) {
        this.employeeConverter = employeeConverter;
    }

    /**
     * @return the employeeSelected
     */
    public EmployeeDTO getEmployeeSelected() {
        return employeeSelected;
    }

    /**
     * @param employeeSelected the employeeSelected to set
     */
    public void setEmployeeSelected(EmployeeDTO employeeSelected) {
        this.employeeSelected = employeeSelected;
    }

    /**
     * @return the employeeList
     */
    public List<EmployeeDTO> getEmployeeList() {
        if ((employeeList == null || employeeList.isEmpty()) && getUserData().getCompany() != null) {
            employeeList = employeeService.getAllEmployeesListByBranchID(getUserData().getUserBranch().getId());
            setEmployeeConverter(new EmployeeDTOConverter(employeeList));
        }
        return employeeList;
    }

    /**
     * @param employeeList the employeeList to set
     */
    public void setEmployeeList(List<EmployeeDTO> employeeList) {
        this.employeeList = employeeList;
    }

}
