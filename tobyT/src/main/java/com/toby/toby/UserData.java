/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.TobyUserBankService;
import com.toby.businessservice.TobyUserRoleService;
import com.toby.businessservice.TobyUserYearService;
import com.toby.businessservice.PathService;
import com.toby.businessservice.PrivilegeService;
import com.toby.dto.CrmEventDTO;
import com.toby.dto.CrmSettingDTO;
import com.toby.dto.EmployeeDTO;
import com.toby.dto.GlBankDTO;
import com.toby.dto.IgmaNotificationSettingDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlBank;
import com.toby.entity.GlYear;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyPrivilege;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserBank;
import com.toby.entity.TobyUserRole;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author hq004
 */
@Named(value = "userData")
@SessionScoped
public class UserData extends Basic implements Serializable {

    @EJB
    private PrivilegeService privilegeService;
    @EJB
    private TobyUserRoleService userRoleService;
    @EJB
    private PathService pathService;
    @EJB
    private TobyUserYearService tobyUserYearService;
    @EJB
    private TobyUserBankService tobyUserBankService;
    private String dir;
    private TobyUser user;
    private StreamedContent graphicImage;
    private TobyCompany company;
    private List<TobyPrivilege> userPrivileges;
    private Map<String, TobyPrivilege> userScreens = new HashMap<>();
    private Integer rowListCount;
    private Integer rowListMasterDetails;
    private Integer master;
    UserData userData;
    private Map<String, String> userDDs;
    private Boolean isAdmin = Boolean.FALSE;

    private String targetScreen;

    private Integer selectedBranch;

    private Branch userBranch;
    
    private TobyUserRole tobyUserRole;

    private List<Branch> branches;

    private GlYear currentYear;

    private List<GlYear> allCompanyYears;

    private String reportPath;

    private String imagePath;

    private String imageReportPath;

    private List<GlYear> glYearList;

    private List<TobyUserBank> tobyUserBankList;

    private TobyUserBank tobyUserBank;

    private List<ProProductionStagesDTO> ProProductionStagesDTOList;

    private ProProductionStagesDTO ProProductionStagesDTODefault;

    private GlBankDTO glBankDefault;

    private List<GlBankDTO> glBankList;

    private List<InvInventoryDTO> InventoryDTOList;
    
    private List<InvInventoryDTO> gallaryDTOList;

    private InvInventoryDTO inventoryDTODefault;

    private List<GlBankDTO> glBankDTOList;

    private GlBankDTO glBankDTODefault;

    private IgmaNotificationSettingDTO igmaNotificationSettingDTO;
    private List<CrmEventDTO> crmNotification;
    private CrmSettingDTO crmSettingDTO;
    
    

    private List<EmployeeDTO> employeeDTOList;

    private EmployeeDTO employeeDTODefault;

    Path path;

    private GlYear glYear;

    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            if (userData == null) {

                FacesContext.getCurrentInstance().getExternalContext().redirect("/toby");
                return;
            }
            this.setUserDDs(userData.getUserDDs());
            this.setDir(userData.getDir());
            this.setCompany(userData.getCompany());
            this.setUserScreens(userData.getUserScreens());
            this.setTargetScreen(userData.getTargetScreen());
            this.setUser(userData.getUser());
            this.setBranches(userData.getBranches());
            this.setUserBranch(userData.userBranch);
            this.setMaster(userData.getMaster());
            if (userData.getCompany() == null) {
                isAdmin = true;
            }
            this.setUserPrivileges(userData.getUserPrivileges());
            this.setCurrentYear(userData.getCurrentYear());
            this.setAllCompanyYears(userData.getAllCompanyYears());
            this.setBranches(userData.branches);
            this.setUserBranch(userData.getUserBranch());
            this.setSelectedBranch(userData.getSelectedBranch());
            this.setReportPath(userData.getReportPath());
            setImagePath(userData.getImagePath());
            setImageReportPath(userData.getImageReportPath());
            this.setGlYear(userData.getGlYear());
            this.setTobyUserBankList(userData.getTobyUserBankList());
            this.setTobyUserBank(userData.getTobyUserBank());
            this.setProProductionStagesDTOList(userData.getProProductionStagesDTOList());
            this.setProProductionStagesDTODefault(userData.getProProductionStagesDTODefault());
            this.setGlBankDefault(userData.getGlBankDefault());
            this.setGlBankList(userData.getGlBankList());

            this.setInventoryDTOList(userData.getInventoryDTOList());
            this.setInventoryDTODefault(userData.getInventoryDTODefault());
            this.setGlBankDTOList(userData.getGlBankDTOList());
            this.setGlBankDTODefault(userData.getGlBankDTODefault());
            this.setGallaryDTOList(userData.getGallaryDTOList());
            this.setIgmaNotificationSettingDTO(userData.getIgmaNotificationSettingDTO());
            this.setCrmNotification(userData.getCrmNotification());
            this.setCrmSettingDTO(userData.getCrmSettingDTO());            
            this.setEmployeeDTOList(userData.getEmployeeDTOList());
            this.setEmployeeDTODefault(userData.getEmployeeDTODefault());

            InputStream stream;
            try {
                if (userData.getUserBranch() != null) {
                    if (userData.getUserBranch().getImage() != null) {
                        stream = new FileInputStream(new File(userData.getImagePath() + userData.getUserBranch().getImage()));
                        this.setGraphicImage(new DefaultStreamedContent(stream, "image/png", userData.getUserBranch().getImage()));
                    }
                }
            } catch (FileNotFoundException ex) {
                saveError(ex, "UserData", "Image Not Found In Img Path");
            } catch (IOException ex) {
                Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
            saveError(ex, "UserData", "init");
        }

    }

    public String selectBranch() {
        if (branches != null) {
            for (int i = 0; i < branches.size(); i++) {
                Branch branch = branches.get(i);
                if (branch.getId().intValue() == selectedBranch.intValue()) {
                    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                    setUserPrivileges(privilegeService.getUserPrivileges(user.getId(), branch.getId()));
                    this.setUserBranch(branch);
                    this.setSelectedBranch(selectedBranch);
                    setproperties(context);
                    getUserlogin().setBranchId(branch);
                    setTobyUserRole(userRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), selectedBranch));
                    context.getSessionMap().put("userlogin", getUserlogin());
                    context.getSessionMap().put("userLogInData", this);
                }
            }
        }

        saveUserData(userData, true);

        return "home";
    }

//    public String logout() {
//        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//        session.invalidate();
//
//        String scheme = context.getRequestScheme() + "://";
//        String serverName = context.getRequestServerName();
//        String serverPort = (context.getRequestServerPort() == 80) ? "" : ":" + context.getRequestServerPort();
//        String contextPath = context.getApplicationContextPath();
//        String finalPath = scheme + serverName + serverPort + contextPath;
//              
//        try {
//            saveUserData(userData,false);
//            FacesContext.getCurrentInstance().getExternalContext().redirect(finalPath);
//        } catch (IOException ex) {
//            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return "login";
//    }
    public void setproperties(ExternalContext context) {
        String realPath = context.getRealPath("/");
        if (OperatingSystem.isWindows()) {
            System.out.println("This is Windows");
            this.setReportPath(pathService.findPath(1).getPath());
            this.setImagePath(pathService.findPath(2).getPath());
            if (this.getUserBranch() != null && this.getUserBranch().getImage() != null) {
                this.setImageReportPath(pathService.findPath(3).getPath() + this.getUserBranch().getImage());
            }
        } else if (OperatingSystem.isMac()) {
            System.out.println("This is Mac");
        } else if (OperatingSystem.isUnix()) {
            System.out.println("This is UNIX");
            this.setReportPath(pathService.findPath(4).getPath());
            this.setImagePath(pathService.findPath(5).getPath());
            if (this.getUserBranch() != null && this.getUserBranch().getImage() != null) {
                this.setImageReportPath(pathService.findPath(6).getPath() + this.getUserBranch().getImage());
            }
        } else if (OperatingSystem.isSolaris()) {
            System.out.println("This is Solaris");
        } else {
            System.out.println("Your OS is not support!!");
        }
        Branch branch = new Branch();
        branch.setId(selectedBranch);
        glYearList = tobyUserYearService.findYearByUserId(userData.getUser().getId(), branch);
        if (glYearList != null && !glYearList.isEmpty()) {
            this.setGlYear(glYearList.get(0));
        } else {
            this.setGlYear(null);
        }
    }

    /**
     * @return the dir
     */
    public String getDir() {

        return dir;
    }

    /**
     * @param dir the dir to set
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * @return the user
     */
    public TobyUser getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(TobyUser user) {
        this.user = user;
    }

    /**
     * @return the company
     */
    public TobyCompany getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(TobyCompany company) {
        this.company = company;
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
        userScreens = new HashMap();
        if (userPrivileges == null) {
            return;
        }
        int i = 0;
        for (TobyPrivilege priv : userPrivileges) {
            if (userScreens.get(priv.getScreenId().getName()) == null && priv.getView()) {
//                i = i + 1;
//                System.out.println(i);
                userScreens.put(priv.getScreenId().getName(), priv);
            }
        }
    }

    public String goToSave() {
        return "company/companyList";
    }

    public String goToExit() {
        return "role/roleList";
    }

    public void authorize(ComponentSystemEvent event) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        try {
            String entity = uri.substring(uri.indexOf("views"));
            entity = entity.substring(6, entity.lastIndexOf("/"));
            if (entity.equalsIgnoreCase("error") || entity.equalsIgnoreCase("home")) {
                return;
            }
            if (entity.equalsIgnoreCase("accountreport")) {
                entity = uri.substring(uri.indexOf("accountreport"));
                entity = entity.substring(14, entity.lastIndexOf("."));
            }
            if (entity.equalsIgnoreCase("bankreport")) {
                entity = uri.substring(uri.indexOf("bankreport"));
                entity = entity.substring(11, entity.lastIndexOf("."));
            }
            if (entity.equalsIgnoreCase("inventoryreports")) {
                entity = uri.substring(uri.indexOf("inventoryreports"));
                entity = entity.substring(17, entity.lastIndexOf("."));
            }
            if (entity.equalsIgnoreCase("instReport")) {
                entity = uri.substring(uri.indexOf("instReport"));
                entity = entity.substring(11, entity.lastIndexOf("."));
            }

            if (entity.equalsIgnoreCase("hosReports")) {
                entity = uri.substring(uri.indexOf("hosReports"));
                entity = entity.substring(11, entity.lastIndexOf("."));
            }

            if (entity.equalsIgnoreCase("rscReports")) {
                entity = uri.substring(uri.indexOf("rscReports"));
                entity = entity.substring(11, entity.lastIndexOf("."));
            }

            if (entity.equalsIgnoreCase("proproductionreport")) {
                entity = uri.substring(uri.indexOf("proproductionreport"));
                entity = entity.substring(20, entity.lastIndexOf("."));
            }

            if (entity.equalsIgnoreCase("crmReports")) {
                entity = uri.substring(uri.indexOf("crmReports"));
                entity = entity.substring(11, entity.lastIndexOf("."));
            }

            targetScreen = entity;
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            if (isAdmin) {
                context.getSessionMap().put("userLogInData", this);
                return;
            }
            if (!userData.isAdmin) {
                TobyUserRole userRole = userRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), selectedBranch);
                TobyPrivilege privilege = privilegeService.getUserPrivilegeOnScreen(userRole.getRoleId().getId(), entity
                );
                setTobyUserRole(userRole);
                if (privilege == null || privilege.getView() == null || !privilege.getView()) {
                    fc.getExternalContext().redirect("../base/error.xhtml");
                }
                context.getSessionMap().put("userLogInData", this);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class
                    .getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            fc.getExternalContext().redirect("../base/error.xhtml");
        }
    }

    /**
     * @return the userScreens
     */
    public Map<String, TobyPrivilege> getUserScreens() {
        return userScreens;
    }

    /**
     * @param userScreens the userScreens to set
     */
    public void setUserScreens(Map<String, TobyPrivilege> userScreens) {
        this.userScreens = userScreens;
    }

    /**
     * @return the userDDs
     */
    public Map<String, String> getUserDDs() {
        return userDDs;
    }

    /**
     * @param userDDs the userDDs to set
     */
    public void setUserDDs(Map<String, String> userDDs) {
        this.userDDs = userDDs;
    }

    /**
     * @return the rowListCount
     */
    public Integer getRowListCount() {
        if (company != null && company.getRowCountList() != null) {
            return company.getRowCountList();
        } else {
            return 10;
        }
    }

    /**
     * @param rowListCount the rowListCount to set
     */
    public void setRowListCount(Integer rowListCount) {
        this.rowListCount = rowListCount;
    }

    /**
     * @return the rowListMasterDetails
     */
    public Integer getRowListMasterDetails() {
        if (company != null && company.getRowCountMasterDetails() != null) {
            return company.getRowCountMasterDetails();
        } else {
            return 5;
        }
    }

    /**
     * @param rowListMasterDetails the rowListMasterDetails to set
     */
    public void setRowListMasterDetails(Integer rowListMasterDetails) {
        this.rowListMasterDetails = rowListMasterDetails;
    }

    /**
     * @return the targetScreen
     */
    public String getTargetScreen() {
        return targetScreen;
    }

    /**
     * @param targetScreen the targetScreen to set
     */
    public void setTargetScreen(String targetScreen) {
        this.targetScreen = targetScreen;
    }

    /**
     * @return the isAdmin
     */
    public Boolean getIsAdmin() {
        return company == null ? true : false;
    }

    /**
     * @param isAdmin the isAdmin to set
     */
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return the branches
     */
    public List<Branch> getBranches() {
        return branches;
    }

    /**
     * @param branches the branches to set
     */
    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    /**
     * @return the selectedBranch
     */
    public Integer getSelectedBranch() {
        return selectedBranch;
    }

    /**
     * @param selectedBranch the selectedBranch to set
     */
    public void setSelectedBranch(Integer selectedBranch) {
        this.selectedBranch = selectedBranch;
    }

    /**
     * @return the userBranch
     */
    public Branch getUserBranch() {
        return userBranch;
    }

    /**
     * @param userBranch the userBranch to set
     */
    public void setUserBranch(Branch userBranch) {
        this.userBranch = userBranch;
    }

    /**
     * @return the currentYear
     */
    public GlYear getCurrentYear() {
        return currentYear;
    }

    /**
     * @param currentYear the currentYear to set
     */
    public void setCurrentYear(GlYear currentYear) {
        this.currentYear = currentYear;
    }

    /**
     * @return the allCompanyYears
     */
    public List<GlYear> getAllCompanyYears() {
        return allCompanyYears;
    }

    /**
     * @param allCompanyYears the allCompanyYears to set
     */
    public void setAllCompanyYears(List<GlYear> allCompanyYears) {
        this.allCompanyYears = allCompanyYears;
    }

    /**
     * @return the reportPath
     */
    public String getReportPath() {
        return reportPath;
    }

    /**
     * @param reportPath the reportPath to set
     */
    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return the imageReportPath
     */
    public String getImageReportPath() {
        return imageReportPath;
    }

    /**
     * @param imageReportPath the imageReportPath to set
     */
    public void setImageReportPath(String imageReportPath) {
        this.imageReportPath = imageReportPath;
    }

    /**
     * @return the glYear
     */
    public GlYear getGlYear() {
        return glYear;
    }

    /**
     * @param glYear the glYear to set
     */
    public void setGlYear(GlYear glYear) {
        this.glYear = glYear;
    }

    /**
     * @return the graphicImage
     */
    public StreamedContent getGraphicImage() {
        return graphicImage;
    }

    /**
     * @param graphicImage the graphicImage to set
     */
    public void setGraphicImage(StreamedContent graphicImage) {
        this.graphicImage = graphicImage;
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
     * @return the glYearList
     */
    public List<GlYear> getGlYearList() {
        return glYearList;
    }

    /**
     * @param glYearList the glYearList to set
     */
    public void setGlYearList(List<GlYear> glYearList) {
        this.glYearList = glYearList;
    }

    /**
     * @return the tobyUserBankList
     */
    public List<TobyUserBank> getTobyUserBankList() {
        return tobyUserBankList;
    }

    /**
     * @param tobyUserBankList the tobyUserBankList to set
     */
    public void setTobyUserBankList(List<TobyUserBank> tobyUserBankList) {
        this.tobyUserBankList = tobyUserBankList;
    }

    /**
     * @return the tobyUserBank
     */
    public TobyUserBank getTobyUserBank() {
        return tobyUserBank;
    }

    /**
     * @param tobyUserBank the tobyUserBank to set
     */
    public void setTobyUserBank(TobyUserBank tobyUserBank) {
        this.tobyUserBank = tobyUserBank;
    }

    /**
     * @return the ProProductionStagesDTOList
     */
    public List<ProProductionStagesDTO> getProProductionStagesDTOList() {
        return ProProductionStagesDTOList;
    }

    /**
     * @param ProProductionStagesDTOList the ProProductionStagesDTOList to set
     */
    public void setProProductionStagesDTOList(List<ProProductionStagesDTO> ProProductionStagesDTOList) {
        this.ProProductionStagesDTOList = ProProductionStagesDTOList;
    }

    /**
     * @return the ProProductionStagesDTODefault
     */
    public ProProductionStagesDTO getProProductionStagesDTODefault() {
        return ProProductionStagesDTODefault;
    }

    /**
     * @param ProProductionStagesDTODefault the ProProductionStagesDTODefault to
     * set
     */
    public void setProProductionStagesDTODefault(ProProductionStagesDTO ProProductionStagesDTODefault) {
        this.ProProductionStagesDTODefault = ProProductionStagesDTODefault;
    }

    /**
     * @return the glBankDefault
     */
    public GlBankDTO getGlBankDefault() {
        return glBankDefault;
    }

    /**
     * @param glBankDefault the glBankDefault to set
     */
    public void setGlBankDefault(GlBankDTO glBankDefault) {
        this.glBankDefault = glBankDefault;
    }

    /**
     * @return the glBankList
     */
    public List<GlBankDTO> getGlBankList() {
        return glBankList;
    }

    /**
     * @param glBankList the glBankList to set
     */
    public void setGlBankList(List<GlBankDTO> glBankList) {
        this.glBankList = glBankList;
    }

    /**
     * @return the InventoryDTOList
     */
    public List<InvInventoryDTO> getInventoryDTOList() {
        return InventoryDTOList;
    }

    /**
     * @param InventoryDTOList the InventoryDTOList to set
     */
    public void setInventoryDTOList(List<InvInventoryDTO> InventoryDTOList) {
        this.InventoryDTOList = InventoryDTOList;
    }

    /**
     * @return the inventoryDTODefault
     */
    public InvInventoryDTO getInventoryDTODefault() {
        return inventoryDTODefault;
    }

    /**
     * @param inventoryDTODefault the inventoryDTODefault to set
     */
    public void setInventoryDTODefault(InvInventoryDTO inventoryDTODefault) {
        this.inventoryDTODefault = inventoryDTODefault;
    }

    /**
     * @return the glBankDTOList
     */
    public List<GlBankDTO> getGlBankDTOList() {
        return glBankDTOList;
    }

    /**
     * @param glBankDTOList the glBankDTOList to set
     */
    public void setGlBankDTOList(List<GlBankDTO> glBankDTOList) {
        this.glBankDTOList = glBankDTOList;
    }

    /**
     * @return the glBankDTODefault
     */
    public GlBankDTO getGlBankDTODefault() {
        return glBankDTODefault;
    }

    /**
     * @param glBankDTODefault the glBankDTODefault to set
     */
    public void setGlBankDTODefault(GlBankDTO glBankDTODefault) {
        this.glBankDTODefault = glBankDTODefault;
    }

    /**
     * @return the gallaryDTOList
     */
    public List<InvInventoryDTO> getGallaryDTOList() {
        return gallaryDTOList;
    }

    /**
     * @param gallaryDTOList the gallaryDTOList to set
     */
    public void setGallaryDTOList(List<InvInventoryDTO> gallaryDTOList) {
        this.gallaryDTOList = gallaryDTOList;
    }

    /**
     * @return the tobyUserRole
     */
    public TobyUserRole getTobyUserRole() {
        return tobyUserRole;
    }

    /**
     * @param tobyUserRole the tobyUserRole to set
     */
    public void setTobyUserRole(TobyUserRole tobyUserRole) {
        this.tobyUserRole = tobyUserRole;
    }

    /**
     * @return the igmaNotificationSettingDTO
     */
    public IgmaNotificationSettingDTO getIgmaNotificationSettingDTO() {
        return igmaNotificationSettingDTO;
    }

    /**
     * @param igmaNotificationSettingDTO the igmaNotificationSettingDTO to set
     */
    public void setIgmaNotificationSettingDTO(IgmaNotificationSettingDTO igmaNotificationSettingDTO) {
        this.igmaNotificationSettingDTO = igmaNotificationSettingDTO;
    }

    /**
     * @return the crmNotification
     */
    public List<CrmEventDTO> getCrmNotification() {
        if (crmNotification == null || crmNotification.isEmpty()) {
            crmNotification = new ArrayList<>();
        }
        return crmNotification;
    }

    /**
     * @param crmNotification the crmNotification to set
     */
    public void setCrmNotification(List<CrmEventDTO> crmNotification) {
        this.crmNotification = crmNotification;
    }

    /**
     * @return the crmSettingDTO
     */
    public CrmSettingDTO getCrmSettingDTO() {
        return crmSettingDTO;
    }

    /**
     * @param crmSettingDTO the crmSettingDTO to set
     */
    public void setCrmSettingDTO(CrmSettingDTO crmSettingDTO) {
        this.crmSettingDTO = crmSettingDTO;
    }

    /**
     * @return the employeeDTOList
     */
    public List<EmployeeDTO> getEmployeeDTOList() {
        return employeeDTOList;
    }

    /**
     * @param employeeDTOList the employeeDTOList to set
     */
    public void setEmployeeDTOList(List<EmployeeDTO> employeeDTOList) {
        this.employeeDTOList = employeeDTOList;
    }

    /**
     * @return the employeeDTODefault
     */
    public EmployeeDTO getEmployeeDTODefault() {
        return employeeDTODefault;
    }

    /**
     * @param employeeDTODefault the employeeDTODefault to set
     */
    public void setEmployeeDTODefault(EmployeeDTO employeeDTODefault) {
        this.employeeDTODefault = employeeDTODefault;
    }

}
