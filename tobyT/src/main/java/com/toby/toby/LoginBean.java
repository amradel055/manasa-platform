/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.GlBankServiceImpl;
import com.toby.businessservice.TobyUserBankService;
import com.toby.businessservice.TobyUserLoginService;
import com.toby.businessservice.TobyUserYearService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyPrivilege;
import com.toby.entity.TobyRole;
import com.toby.entity.TobyScreen;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserBank;
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
import com.toby.businessservice.TobyUserService;
import com.toby.businessservice.BranchService;
import com.toby.businessservice.CompanyService;
import com.toby.businessservice.DDService;
import com.toby.businessservice.GlAnalyticsAccountsServcie;
import com.toby.businessservice.GlYearService;
import com.toby.businessservice.PathService;
import com.toby.businessservice.PrivilegeService;
import com.toby.businessservice.TobyUserRoleService;
import com.toby.core.UserDataDTO;
import com.toby.dto.GlBankDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlAnalyticsAccounts;
import com.toby.entity.GlYear;

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
    private TobyUserLoginService tobyUserLoginService1;
    @EJB
    private CompanyService companyService;

    @EJB
    private TobyUserService userService;

    private TobyRole loggedUserRole;

    @EJB
    private DDService ddService;
    @EJB
    private PrivilegeService privilegeService;
    @EJB
    private TobyUserRoleService userRoleService;


    @EJB
    private BranchService branchService;

    @EJB
    private GlYearService yearService;

    @EJB
    private PathService pathService;
    @EJB
    private TobyUserYearService tobyUserYearService;

    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

    private List<InvInventoryDTO> userInventorys;

    private List<TobyPrivilege> userPrivileges;

    private List<TobyScreen> userScreens;

    private List<GlYear> userYears;

    private List<TobyCompany> allCompanies = new ArrayList<>();

    private Integer selectedCompanyId;
    private String userCode;
    private String password;
    private String errorMsg;
    String realPath = null;

    @EJB
    private GlAnalyticsAccountsServcie glAnalyticsAccountsServcie;
    @EJB
    private TobyUserBankService tobyUserBankService;
    @EJB
    private ProProductionStagesService proProductionStagesService;

    public LoginBean() throws IOException {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session.getAttribute("userLogInData") != null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/toby/faces/views/base/home.xhtml");
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
            TobyUser loggedInUser = userService.authenticateUser(userCode, password);

            System.out.println("loggedInUser: " + loggedInUser);
            if (loggedInUser != null) {
                System.out.println("Logged In Successfully");
//                int branchId = branchService.getBranchesForUser(loggedInUser.getId()).get(0).getId();
//                  
//                 boolean state = tobyUserLoginService1.checkUserLogged(loggedInUser.getId(), branchId);
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
                 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"", "عفوًا لا يمكن الدخول في الوقت الحالي"));
                 setErrorMsg( "عفوًا لا يمكن الدخول في الوقت الحالي");
                 System.out.print("+++++++++++++++++ already Logged +++++++++++++++++");
                 return "";
                 }*/
            } else {
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
    public void logout() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        HttpSession session = (HttpSession) context.getSession(false);

        try {
            userData = (UserData) context.getSessionMap().get("userLogInData");
            if (userData == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/toby");
            }

//            String scheme = context.getRequestScheme() + "://";
//            String serverName = context.getRequestServerName();
//            String serverPort = (context.getRequestServerPort() == 80) ? "" : ":" + context.getRequestServerPort();
//            String contextPath = context.getApplicationContextPath();
//            String finalPath = scheme + serverName + serverPort + contextPath;
            saveUserData(userData, false);

//            FacesContext.getCurrentInstance().getExternalContext().redirect(finalPath);
            //  FacesContext.getCurrentInstance().getExternalContext().redirect("/toby");
            //      context.redirect("/toby");
            session.invalidate();
            context.redirect("/toby/");
//        } catch (IOException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();

//            saveError(ex, "LoginBean", "logout");
        } finally {
            session.invalidate();
        }
    }

    public void adminValues(TobyUser loggedInUser) {
        List<GlYear> years = yearService.getALLGlyearByCompanyId(selectedCompanyId);
        userData.setAllCompanyYears(years);
        Date today = new Date();

        for (GlYear year : years) {
            if (today.after(year.getDateFrom()) && today.before(year.getDateTo())) {
                userData.setCurrentYear(year);
            }
        }
        userData.setGlYear(getUserYears().get(0));
        List<TobyUserBank> tobyUserBanks = tobyUserBankService.findAll();

        userData.setTobyUserBankList(tobyUserBanks);

    }

    public void userValues(TobyUser loggedInUser) {
        List<Branch> branches = branchService.getBranchesForUser(loggedInUser.getId());
        List<GlYear> years = yearService.getALLGlyearByCompanyId(selectedCompanyId);

        userData.setAllCompanyYears(years);
        Date today = new Date();

        userData.setBranches(branches);
        if (branches != null && branches.size() > 0) {
            userData.setUserBranch(branches.get(0));
            userData.setSelectedBranch(branches.get(0).getId());
            userData.setUserPrivileges(privilegeService.getUserPrivileges(loggedInUser.getId(), branches.get(0).getId()));
            userData.setTobyUserRole(userRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), userData.getUserBranch().getId()));
            
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

            List<TobyUserBank> tobyUserBanks = tobyUserBankService.getAllTobyUserBankListByUserAndBranch(userData.getUser().getId(), userData.getUserBranch().getId());

            userData.setTobyUserBankList(tobyUserBanks);
            userData.setGlBankList(new ArrayList<>());
            GlBankServiceImpl bankServiceImpl = new GlBankServiceImpl();
            if (tobyUserBanks != null && tobyUserBanks.size() > 0) {
                for (TobyUserBank tobyUserBank : tobyUserBanks) {
                    GlBankDTO dTO = bankServiceImpl.mapToDTO(tobyUserBank.getBankId());
                    userData.getGlBankList().add(dTO);
                }
                userData.setTobyUserBank(tobyUserBanks.get(0));
                GlBankDTO dTO = bankServiceImpl.mapToDTO(tobyUserBanks.get(0).getBankId());
                userData.setGlBankDefault(dTO);
            }

            List<ProProductionStagesDTO> tobyUserproproductionStatgesList = proProductionStagesService.getAllProproductionStagesListByUserAndBranch(userData.getUser(), userData.getUserBranch().getId());

            userData.setProProductionStagesDTOList(tobyUserproproductionStatgesList);
            if (tobyUserproproductionStatgesList != null && tobyUserproproductionStatgesList.size() > 0) {
                userData.setProProductionStagesDTODefault(tobyUserproproductionStatgesList.get(0));
            }
            
            List<InvInventoryDTO> tobyUserInventoryDTO = tobyUserInventoryService.getAllInventroisByUserAndBranchPerDTO(userData.getUser().getId(), userData.getUserBranch().getId());
            userData.setInventoryDTOList(tobyUserInventoryDTO);
            if (tobyUserInventoryDTO != null && tobyUserInventoryDTO.size() > 0) {
                userData.setInventoryDTODefault(tobyUserInventoryDTO.get(0));
            }

            List<GlBankDTO> glBankDTOList = tobyUserBankService.getAllGlBankForUserByTypeAndBranchIdPerDTO(userData.getUser().getId(), userData.getUserBranch().getId(),null);
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
    public List<TobyCompany> getAllCompanies() {
        return allCompanies;
    }

    /**
     * @param allCompanies the allCompanies to set
     */
    public void setAllCompanies(List<TobyCompany> allCompanies) {
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
    public List<TobyPrivilege> getUserPrivileges() {
        return userPrivileges;
    }

    /**
     * @param userPrivileges the userPrivileges to set
     */
    public void setUserPrivileges(List<TobyPrivilege> userPrivileges) {
        this.userPrivileges = userPrivileges;
    }

    /**
     * @return the userScreens
     */
    public List<TobyScreen> getUserScreens() {
        return userScreens;
    }

    /**
     * @param userScreens the userScreens to set
     */
    public void setUserScreens(List<TobyScreen> userScreens) {
        this.userScreens = userScreens;
    }

    /**
     * @return the userYears
     */
    public List<GlYear> getUserYears() {
        if (userYears == null || userYears.isEmpty()) {
            if (userData.getUserBranch() != null) {
                userYears = tobyUserYearService.findYearByUserId(userData.getUser().getId(), userData.getUserBranch());
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
