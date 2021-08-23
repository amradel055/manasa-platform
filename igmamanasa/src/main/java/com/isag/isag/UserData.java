/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.IsagUserRoleService;
import com.isag.businessservice.PathService;
import com.isag.businessservice.PrivilegeService;
import com.isag.dto.IsagNotificationSettingDTO;
import com.isag.entity.Branch;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagPrivilege;
import com.isag.entity.IsagUser;
import com.isag.entity.IsagUserRole;
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
    private IsagUserRoleService userRoleService;
    @EJB
    private PathService pathService;
    private String dir;
    private IsagUser user;
    private StreamedContent graphicImage;
    private IsagCompany company;
    private List<IsagPrivilege> userPrivileges;
    private Map<String, IsagPrivilege> userScreens = new HashMap<>();
    private Integer rowListCount;
    private Integer rowListMasterDetails;
    private Integer master;
    UserData userData;
    private Map<String, String> userDDs;
    private Boolean isAdmin = Boolean.FALSE;

    private String targetScreen;

    private Integer selectedBranch;

    private Branch userBranch;

    private IsagUserRole isagUserRole;

    private List<Branch> branches;

    private String reportPath;

    private String imagePath;

    private String imageReportPath;

    private IsagNotificationSettingDTO isagNotificationSettingDTO;

    Path path;


    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            if (userData == null) {

                FacesContext.getCurrentInstance().getExternalContext().redirect("/igma");
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
//            this.setCurrentYear(userData.getCurrentYear());
//            this.setAllCompanyYears(userData.getAllCompanyYears());
            this.setBranches(userData.branches);
            this.setUserBranch(userData.getUserBranch());
            this.setSelectedBranch(userData.getSelectedBranch());
            this.setReportPath(userData.getReportPath());
            setImagePath(userData.getImagePath());
            setImageReportPath(userData.getImageReportPath());
//            this.setGlYear(userData.getGlYear());
//            this.setIsagUserBankList(userData.getIsagUserBankList());
//            this.setIsagUserBank(userData.getIsagUserBank());

//            this.setInventoryDTOList(userData.getInventoryDTOList());
//            this.setInventoryDTODefault(userData.getInventoryDTODefault());

//            this.setGlBankDTOList(userData.getGlBankDTOList());
//            this.setGlBankDTODefault(userData.getGlBankDTODefault());
            this.setIsagNotificationSettingDTO(userData.getIsagNotificationSettingDTO());
//            this.setCrmNotification(userData.getCrmNotification());
//            this.setCrmSettingDTO(userData.getCrmSettingDTO());
            InputStream stream;
            try {
                if (userData.getUserBranch() != null) {
                    if (userData.getUserBranch().getImage() != null) {
                        stream = new FileInputStream(new File(userData.getImagePath() + userData.getUserBranch().getImage()));
                        this.setGraphicImage(new DefaultStreamedContent(stream, "image/png", userData.getUserBranch().getImage()));
                        stream.close();
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
                    setIsagUserRole(userRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), selectedBranch));
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
            } else {
                this.setImageReportPath(null);
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
    public IsagUser getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(IsagUser user) {
        this.user = user;
    }

    /**
     * @return the company
     */
    public IsagCompany getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(IsagCompany company) {
        this.company = company;
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
        userScreens = new HashMap();
        if (userPrivileges == null) {
            return;
        }
        for (IsagPrivilege priv : userPrivileges) {
            if (userScreens.get(priv.getScreenId().getName()) == null && priv.getView()) {
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

            if (entity.equalsIgnoreCase("crmReports")) {
                entity = uri.substring(uri.indexOf("crmReports"));
                entity = entity.substring(11, entity.lastIndexOf("."));
            }

            if (entity.equalsIgnoreCase("invposReport")) {
                entity = uri.substring(uri.indexOf("posReport"));
                entity = entity.substring(13, entity.lastIndexOf("."));
            }

            targetScreen = entity;
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            if (isAdmin) {
                context.getSessionMap().put("userLogInData", this);
                return;
            }
            if (!userData.isAdmin) {
                IsagUserRole userRole = userRoleService.findUserRoleByUserIdAndBranch(userData.getUser().getId(), selectedBranch);
                IsagPrivilege privilege = privilegeService.getUserPrivilegeOnScreen(userRole.getRoleId().getId(), entity);
                setIsagUserRole(userRole);
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
    public Map<String, IsagPrivilege> getUserScreens() {
        return userScreens;
    }

    /**
     * @param userScreens the userScreens to set
     */
    public void setUserScreens(Map<String, IsagPrivilege> userScreens) {
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
     * @return the isagNotificationSettingDTO
     */
    public IsagNotificationSettingDTO getIsagNotificationSettingDTO() {
        return isagNotificationSettingDTO;
    }

    /**
     * @param isagNotificationSettingDTO the isagNotificationSettingDTO to set
     */
    public void setIsagNotificationSettingDTO(IsagNotificationSettingDTO isagNotificationSettingDTO) {
        this.isagNotificationSettingDTO = isagNotificationSettingDTO;
    }

    /**
     * @return the isagUserRole
     */
    public IsagUserRole getIsagUserRole() {
        return isagUserRole;
    }

    /**
     * @param isagUserRole the isagUserRole to set
     */
    public void setIsagUserRole(IsagUserRole isagUserRole) {
        this.isagUserRole = isagUserRole;
    }

}
