/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.CRMEventServiceDTO;
import com.isag.businessservice.CompanyService;
import com.isag.businessservice.CrmSettingService;
import com.isag.businessservice.DDService;
import com.isag.businessservice.EmployeeService;
import com.isag.businessservice.GlAnalyticsAccountsServcie;
import com.isag.businessservice.GlYearService;
import com.isag.businessservice.IsagNotificationSettingService;
import com.isag.businessservice.IsagUserBankService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.IsagUserRoleService;
import com.isag.businessservice.IsagUserYearService;
import com.isag.businessservice.PathService;
import com.isag.businessservice.PrivilegeService;
import com.isag.core.UserDataDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.entity.Branch;
import com.isag.entity.GlAnalyticsAccounts;
import com.isag.entity.GlYear;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagPrivilege;
import com.isag.entity.IsagScreen;
import com.isag.entity.IsagUser;
import com.isag.entity.IsagUserBank;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.isag.businessservice.IsagUserService;
import com.isag.entity.IsagUserRole;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hq004
 */
public class LoginBean extends Basic implements Serializable {

    private UserData userData;
    /**
     * Creates a new instance of LoginBean
     */
    @EJB
    private CompanyService companyService;

    @EJB
    private IsagUserService userService;

    @EJB
    private DDService ddService;
    @EJB
    private PrivilegeService privilegeService;
    @EJB
    private IsagUserRoleService userRoleService;

    @EJB
    private BranchService branchService;

    @EJB
    private GlYearService yearService;

    @EJB
    private PathService pathService;
    @EJB
    private IsagUserYearService isagUserYearService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private EmployeeService employeeService;
    @EJB
    private IsagNotificationSettingService isagNotificationSettingService;
    @EJB
    private CRMEventServiceDTO cRMEventServiceDTO;
    @EJB
    private CrmSettingService crmSettingService;

    private List<IsagPrivilege> userPrivileges;

    private List<IsagScreen> userScreens;

    private List<GlYear> userYears;

    private List<InvInventoryDTO> userInventorys;

    private List<IsagCompany> allCompanies = new ArrayList<>();

    private Integer selectedCompanyId;
    private String userCode;
    private String password;
    private String errorMsg;
    String realPath = null;

    @EJB
    private GlAnalyticsAccountsServcie glAnalyticsAccountsServcie;
    @EJB
    private IsagUserBankService isagUserBankService;

    public LoginBean() throws IOException {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session.getAttribute("userLogInData") != null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/igma/faces/dashboard.xhtml");
        }
    }

    @PostConstruct
    public void init() {
        allCompanies = companyService.getAllCompanies();
    }

    public String login() {
        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.invalidate();

            System.out.println("session: " + session);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            IsagUser loggedInUser = userService.authenticateUser(userCode, password);

            System.out.println("loggedInUser: " + loggedInUser);
            if (loggedInUser != null) {
                System.out.println("Logged In Successfully");
//                int branchId = branchService.getBranchesForUser(loggedInUser.getId()).get(0).getId();
//                  
//                 boolean state = isagUserLoginService1.checkUserLogged(loggedInUser.getId(), branchId);
//                if (!state /*|| loggedInUser.getId() == 61*/) {
                userData = new UserData();
                UserDataDTO userDataDTO = new UserDataDTO();
//            loggedUserRole = loggedInUser.getRoleId();

                userData.setCompany(loggedInUser.getCompanyId());
                userData.setUser(loggedInUser);
                userData.setMaster(loggedInUser.getMaster());
                selectedCompanyId = (loggedInUser.getCompanyId() == null ? null : loggedInUser.getCompanyId().getId());
                userData.setUserDDs(ddService.getDDByCompanyAndLanguage(loggedInUser.getLang().getId(), selectedCompanyId));
                userData.setDir((loggedInUser.getLang().getName().equalsIgnoreCase("العربية")) ? "RTL" : "LTR");
                if (loggedInUser.getCompanyId() != null) {
                    userValues(loggedInUser);
                } else {
                    adminValues(loggedInUser);
                }

                fillUserDataDTO(userDataDTO);

                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

                setproperties(context);

                context.getSessionMap().put("userLogInData", userData);
                loggedInUser.setBranchId(userData.getUserBranch());
                context.getSessionMap().put("userlogin", loggedInUser);
                context.getSessionMap().put("userDataDTO", userDataDTO);

                saveUserData(userData, true);

                if (userData.getUserBranch() != null) {
//                    designateMonitoringForEachBranch();
//                    purchaseAndSalesAccountsForEachBranch();
                    if (userData.getMaster() != null && userData.getMaster() == 1) {
                        return "views/monitoring/monitoringForm?faces-redirect=true";
                    }
                }

                return "dashboard?faces-redirect=true";

                /*   } else {
                 //already logged
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"", "ع�?وًا لا يمكن الدخول �?ي الوقت الحالي"));
                 setErrorMsg( "ع�?وًا لا يمكن الدخول �?ي الوقت الحالي");
                 System.out.print("+++++++++++++++++ already Logged +++++++++++++++++");
                 return "";
                 }*/
            } else {
                setErrorMsg("كلمه المرور او اسم المستخدم خطاء");
                System.out.println("login Failure");
                return "";
            }

        } catch (Exception e) {
            System.out.print(e);
            saveError(e, "LoginBean", "login()");
        }
        return "";
    }

    private void fillUserDataDTO(UserDataDTO userDataDTO) {
        userDataDTO.setInventoryDTOList(userData.getInventoryDTOList());
        userDataDTO.setGlBankDTOList(userData.getGlBankDTOList());
        userDataDTO.setBranchId(userData.getUserBranch() == null ? null : userData.getUserBranch().getId());
        userDataDTO.setCompanyId(userData.getCompany() == null ? null : userData.getCompany().getId());
    }

//    @PreDestroy
    public void logout(Integer test) {
        if (test != null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) context.getResponse();
            HttpServletRequest request = (HttpServletRequest) context.getRequest();
            HttpSession session = (HttpSession) context.getSession(false);

            try {
                userData = (UserData) context.getSessionMap().get("userLogInData");
                if (userData == null) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/igma/faces/login.xhtml");
                }

                String scheme = context.getRequestScheme() + "://";
                String serverName = context.getRequestServerName();
                String serverPort = (context.getRequestServerPort() == 80) ? "" : ":" + context.getRequestServerPort();
//            String contextPath = context.getApplicationContextPath();
//            String finalPath = scheme + serverName + serverPort + contextPath;
                saveUserData(userData, false);

//            FacesContext.getCurrentInstance().getExternalContext().redirect(finalPath);
                //  FacesContext.getCurrentInstance().getExternalContext().redirect("/isag");
                //      context.redirect("/isag");
                if (session != null && request.isRequestedSessionIdValid()) {
                    session.invalidate();
                }
                if (!response.isCommitted()) {
                    context.redirect("/igma?faces-redirect=true");
                }
//        } catch (IOException ex) {
            } catch (Exception ex) {
                ex.printStackTrace();
//            saveError(ex, "LoginBean", "logout");
            }
        }
    }

    public void adminValues(IsagUser loggedInUser) {
        List<GlYear> years = yearService.getALLGlyearByCompanyId(selectedCompanyId);
        userData.setAllCompanyYears(years);
        Date today = new Date();

        for (GlYear year : years) {
            if (today.after(year.getDateFrom()) && today.before(year.getDateTo())) {
                userData.setCurrentYear(year);
            }
        }
        userData.setGlYear(getUserYears().get(0));
        List<IsagUserBank> isagUserBanks = isagUserBankService.findAll();

        userData.setIsagUserBankList(isagUserBanks);

    }

    public void userValues(IsagUser loggedInUser) {
        List<Branch> branches = branchService.getBranchesForUser(loggedInUser.getId());
        List<GlYear> years = yearService.getALLGlyearByCompanyId(selectedCompanyId);

        userData.setAllCompanyYears(years);
        Date today = new Date();

        userData.setBranches(branches);
        if (branches != null && branches.size() > 0) {
            userData.setUserBranch(branches.get(0));
            userData.setSelectedBranch(branches.get(0).getId());
            userData.setUserPrivileges(privilegeService.getUserPrivileges(loggedInUser.getId(), branches.get(0).getId()));
            userData.setIsagUserRole(userRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), userData.getUserBranch().getId()));

            userData.setIsagNotificationSettingDTO(isagNotificationSettingService.getIsagNotificationSettingDTOByUserId(branches.get(0).getId(), loggedInUser.getId()));
            userData.setCrmNotification(cRMEventServiceDTO.findALLCrmNotification(branches.get(0).getId(), loggedInUser.getEmployeeId() != null ? loggedInUser.getEmployeeId().getId() : null));
            userData.setCrmSettingDTO(crmSettingService.getCrmSettingDTOByUserId(branches.get(0).getId(), loggedInUser.getId()));
            List<GlYear> branchYears = yearService.getALLGlyearByBranchId(userData.getUserBranch().getId());

            for (GlYear year : branchYears) {
                if (today.after(year.getDateFrom()) && today.before(year.getDateTo())) {
                    userData.setCurrentYear(year);
                }
            }

            GlYear defaultYear = yearService.getDefaultYearsByBranchIdByUser(userData.getUser().getId(), userData.getUserBranch().getId());

            if (defaultYear != null) {
                userData.setGlYear(defaultYear);
            } else if (getUserYears() != null && !getUserYears().isEmpty()) {
                userData.setGlYear(getUserYears().get(0));
            }

            List<IsagUserBank> isagUserBanks = isagUserBankService.getAllIsagUserBankListByUserAndBranch(userData.getUser().getId(), userData.getUserBranch().getId());
            userData.setIsagUserBankList(isagUserBanks);
            if (isagUserBanks != null && isagUserBanks.size() > 0) {
                userData.setIsagUserBank(isagUserBanks.get(0));
            }

            List<InvInventoryDTO> isagUserInventoryDTO = isagUserInventoryService.getAllInventroisByUserAndBranchPerDTO(userData.getUser().getId(), userData.getUserBranch().getId());
            userData.setInventoryDTOList(isagUserInventoryDTO);
            if (isagUserInventoryDTO != null && isagUserInventoryDTO.size() > 0) {
                userData.setInventoryDTODefault(isagUserInventoryDTO.get(0));
            }

            List<EmployeeDTO> isagUserEmployeeDTO = isagUserInventoryService.getAllEmployeesByUserAndBranchPerDTO(userData.getUser().getId(), userData.getUserBranch().getId());
            userData.setEmployeeDTOList(isagUserEmployeeDTO);
            if (isagUserEmployeeDTO != null && isagUserEmployeeDTO.size() > 0) {
                userData.setEmployeeDTODefault(isagUserEmployeeDTO.get(0));
            }

            List<GlBankDTO> glBankDTOList = isagUserBankService.getAllGlBankForUserByTypeAndBranchIdPerDTO(userData.getUser().getId(), userData.getUserBranch().getId(), null);
            userData.setGlBankDTOList(glBankDTOList);
            if (glBankDTOList != null && glBankDTOList.size() > 0) {
                userData.setGlBankDTODefault(glBankDTOList.get(0));
            }
        }

    }

    public void setproperties(ExternalContext context) {
        realPath = context.getRealPath("/");
        if (OperatingSystem.isWindows()) {
            System.out.println("This is Windows");
            userData.setReportPath(pathService.findPath(1).getPath());
            userData.setImagePath(pathService.findPath(2).getPath());
            if (userData.getUserBranch() != null && userData.getUserBranch().getImage() != null) {
                userData.setImageReportPath(pathService.findPath(3).getPath() + userData.getUserBranch().getImage());
            }
        } else if (OperatingSystem.isMac()) {
            System.out.println("This is Mac");
        } else if (OperatingSystem.isUnix()) {
            System.out.println("This is UNIX");
            userData.setReportPath(pathService.findPath(4).getPath());
            userData.setImagePath(pathService.findPath(5).getPath());
            if (userData.getUserBranch() != null && userData.getUserBranch().getImage() != null) {
                userData.setImageReportPath(pathService.findPath(6).getPath() + userData.getUserBranch().getImage());
            }
        } else if (OperatingSystem.isSolaris()) {
            System.out.println("This is Solaris");
        } else {
            System.out.println("Your OS is not support!!");
        }
    }

    public void designateMonitoringForEachBranch() {
        List<GlAnalyticsAccounts> accountsServcieList = new ArrayList<>();
        List<GlAnalyticsAccounts> accountsServcieForSaveList = new ArrayList<>();
        Long count = glAnalyticsAccountsServcie.getCountsForMonitoringByBranch(userData.getUserBranch().getId(), 1);
        if (count == 0) {
            accountsServcieList = glAnalyticsAccountsServcie.getALLNullMonitoringAccounts();
            if (accountsServcieList != null && !accountsServcieList.isEmpty()) {
                for (GlAnalyticsAccounts glan : accountsServcieList) {
                    GlAnalyticsAccounts gaa = new GlAnalyticsAccounts();
                    gaa.setCode(glan.getCode());
                    gaa.setBranchId(userData.getUserBranch());
                    gaa.setCompanyId(userData.getCompany());
                    gaa.setCreatedBy(userData.getUser());
                    gaa.setCreationDate(new Date());
                    gaa.setType(1);
                    accountsServcieForSaveList.add(gaa);
                }
                glAnalyticsAccountsServcie.saveGlAnalyticsAccounts(accountsServcieForSaveList);
            }
        }
    }

    public void purchaseAndSalesAccountsForEachBranch() {
        List<GlAnalyticsAccounts> accountsServcieList = new ArrayList<>();
        List<GlAnalyticsAccounts> accountsServcieForSaveList = new ArrayList<>();
        Long count = glAnalyticsAccountsServcie.getCountsForMonitoringByBranch(userData.getUserBranch().getId(), 2);
        if (count == 0 || 1 == count) {
            accountsServcieList = glAnalyticsAccountsServcie.getALLNullPurchaseAndSalesAccounts();
            if (accountsServcieList != null && !accountsServcieList.isEmpty()) {
                for (GlAnalyticsAccounts glan : accountsServcieList) {
                    GlAnalyticsAccounts gaa = new GlAnalyticsAccounts();
                    gaa.setCode(glan.getCode());
                    gaa.setBranchId(userData.getUserBranch());
                    gaa.setCompanyId(userData.getCompany());
                    gaa.setCreatedBy(userData.getUser());
                    gaa.setCreationDate(new Date());
                    gaa.setType(2);
                    accountsServcieForSaveList.add(gaa);
                }
                glAnalyticsAccountsServcie.saveGlAnalyticsAccounts(accountsServcieForSaveList);
            }
        }
    }

    /**
     * @return the allCompanies
     */
    public List<IsagCompany> getAllCompanies() {
        return allCompanies;
    }

    /**
     * @param allCompanies the allCompanies to set
     */
    public void setAllCompanies(List<IsagCompany> allCompanies) {
        this.allCompanies = allCompanies;
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
     * @return the userCode
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * @param userCode the userCode to set
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the userPrivileges
     */
    public List<IsagPrivilege> getUserPrivileges() {
        return userPrivileges;
    }

    /**
     * @param userPrivileges the userPrivileges to set
     */
    public void setUserPrivileges(List<IsagPrivilege> userPrivileges) {
        this.userPrivileges = userPrivileges;
    }

    /**
     * @return the userScreens
     */
    public List<IsagScreen> getUserScreens() {
        return userScreens;
    }

    /**
     * @param userScreens the userScreens to set
     */
    public void setUserScreens(List<IsagScreen> userScreens) {
        this.userScreens = userScreens;
    }

    /**
     * @return the userYears
     */
    public List<GlYear> getUserYears() {
        if (userYears == null || userYears.isEmpty()) {
            if (userData.getUserBranch() != null) {
                userYears = isagUserYearService.findYearByUserId(userData.getUser().getId(), userData.getUserBranch());
            } else {
                userYears = yearService.getAllYear();

            }
        }

        return userYears;
    }

    /**
     * @param userYears the userYears to set
     */
    public void setUserYears(List<GlYear> userYears) {
        this.userYears = userYears;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
