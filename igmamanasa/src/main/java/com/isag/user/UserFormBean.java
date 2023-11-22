/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.user;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.CompanyService;
import com.isag.businessservice.RoleService;
import com.isag.businessservice.SymbolService;
import com.isag.entity.Branch;
import com.isag.entity.CompanyLanguage;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaRole;
import com.isag.entity.IgmaUser;
import com.isag.entity.Symbol;
import com.isag.isag.BaseFormBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import com.isag.businessservice.IgmaUserService;

/**
 *
 * @author hq002
 */
@Named(value = "UserFormBean")
@ViewScoped
public class UserFormBean extends BaseFormBean implements Serializable {

    private String userName;
    private String userPassword;
    private String userCode;
    private Symbol userLang;
    private String userCompanyCode;
    private List<IgmaCompany> userCompany;
    private IgmaCompany company;
    private IgmaRole role;
    private IgmaUser userDetails;
    private Integer selectedCompanyId;
    private IgmaRole roleId;
    private Integer userId;
    private Integer selectedLangId;
    private Integer master;
    private List<Symbol> languages;
    private Branch branch;
    


    @EJB
    private IgmaUserService userService;

    @EJB
    private RoleService roleService;

    @EJB
    private CompanyService companyService;

    @EJB
    private SymbolService symbolService;

    @EJB
    private BranchService branchService;

    /**
     * Creates a new instance of UserFormBean
     */
    public UserFormBean() {
    }

    @Override
    public String save() {
        try {
            userDetails = new IgmaUser();
            if (userName != null && userPassword != null && userCode != null && selectedLangId != null) {

                userDetails.setName(userName.trim());
                userDetails.setPassword(userPassword);
                userCode = userCompanyCode + "" + userCode;
                userDetails.setUserCode(userCode);
                Symbol sym = new Symbol();
                sym.setId(selectedLangId);
                userDetails.setLang(sym);
                userDetails.setMaster(getMaster());
                IgmaCompany companySelected = new IgmaCompany();
                companySelected.setId(selectedCompanyId);
                userDetails.setCompanyId(companySelected);
//                if (getRemCompanyDTO != null) {
//                    RemCompany remCompany = new RemCompany();
//                    remCompany.setId(getRemCompanyDTO().getId());
//                    userDetails.setRemCompanyId(remCompany);
//                }
//                userDetails.setEmployeeId(employeeSelected);
                if (getScreenMode().equalsIgnoreCase("add")) {
                    userDetails.setCreationDate(new Date());
                    userDetails.setCreatedBy(getUserData().getUser());
                    try {
                        Branch b = null;
                        if (getUserData().getSelectedBranch() != null) {
                            b = branchService.findBranch(getUserData().getSelectedBranch());
                        }
                        userDetails.setBranchId(b);
                        userService.addUser(userDetails);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("DUBLICATED")));
                        e.printStackTrace();
                        saveError(e, "UserFormBean", "save");
                        return null;
                    }

                } else if (getScreenMode().equalsIgnoreCase("edit")) {
                    userDetails.setModifiedBy(getUserData().getUser());
                    userDetails.setModificationDate(new Date());
                    userDetails.setBranchId(branch);
                    userDetails.setId(userId);
                    userDetails.setCompanyId(company);
                    try {
                        userService.updateUser(userDetails);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("DUBLICATED")));
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
            if (getUserData().getCompany() != null) {
                selectedCompanyId = getUserData().getCompany().getId();
                userCompanyCode = getUserData().getCompany().getCode();
            }
            if (getScreenMode() != null) {
                if (getScreenMode().equalsIgnoreCase("add")) {
                    setUserDetailsEmpty();

                } else if (getScreenMode().equalsIgnoreCase("edit")) {
                    userId = (Integer) (getContext().getSessionMap().get("selecteduser"));
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
                for (IgmaCompany usercom : userCompany) {
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
                userCompany.add(getUserData().getCompany());
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
            company = getUserData().getCompany();
            if (company != null && company.getId() != null) {
                branch = userDetails.getBranchId();
                userName = userDetails.getName();
                master = userDetails.getMaster();
                userPassword = userDetails.getPassword();
                userCode = userDetails.getUserCode().substring(3);
//                employeeSelected = userDetails.getEmployeeId();
                List<CompanyLanguage> companyLanguages = companyService.getCompanyLanguages(selectedCompanyId);
                for (CompanyLanguage companyLanguage : companyLanguages) {
                    languages.add(companyLanguage.getSymbol());
                }
                userCompany.add(company);
                userCompanyCode = company.getCode();
                selectedCompanyId = userDetails.getCompanyId().getId();
//                if (userDetails.getRemCompanyId() != null) {
//                    getRemCompanyDTO().setId(userDetails.getRemCompanyId().getId());
//                    getRemCompanyDTO().setCode(userDetails.getRemCompanyId().getCode());
//                    getRemCompanyDTO().setName(userDetails.getRemCompanyId().getName());
//                }
                selectedLangId = userDetails.getLang().getId();

            } else {
                // this our user admin show all details
                branch = userDetails.getBranchId();
                userName = userDetails.getName();
                master = userDetails.getMaster();
                userPassword = userDetails.getPassword();
                userCode = userDetails.getUserCode();
                //employeeSelected = userDetails.getEmployeeId();

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

    public List<IgmaCompany> getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(List<IgmaCompany> userCompanies) {
        this.userCompany = userCompanies;
    }

    public IgmaCompany getCompany() {
        return company;
    }

    public void setCompany(IgmaCompany company) {
        this.company = company;
    }

    public IgmaRole getRole() {
        return role;
    }

    public void setRole(IgmaRole role) {
        this.role = role;
    }

    public IgmaUser getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(IgmaUser userDetails) {
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

    public IgmaRole getRoleId() {
        return roleId;
    }

    public void setRoleId(IgmaRole roleId) {
        this.roleId = roleId;
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
}
