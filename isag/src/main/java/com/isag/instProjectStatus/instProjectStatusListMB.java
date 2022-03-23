package com.isag.instProjectStatus;

import com.isag.businessservice.ProjectStatusService;
import com.isag.converter.InstProjectGuideConverter;
import com.isag.entity.InstProjectGuide;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entiy.InstProjectStatusEntity;
import com.isag.entiy.InvPurchaseInvoiceDetailsEntity;
import com.isag.invPurchaseInvoice.InvPurchaseInvoiceFormMB;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import com.isag.views.ProjectStatusView;
import java.io.IOException;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import tafqeet.Tafqeet;

@Named(value = "instProjectStatusListMB")
@ViewScoped
public class instProjectStatusListMB extends BaseListBean {

    /**
     * @return the isSearch
     */
    public Boolean getIsSearch() {
        return isSearch;
    }

    /**
     * @param isSearch the isSearch to set
     */
    public void setIsSearch(Boolean isSearch) {
        this.isSearch = isSearch;
    }

    private UserData userData;
    private Boolean isSearch;
    private List<ProjectStatusView> projectStatusList = new ArrayList<>();
    private ProjectStatusView projectStatus;
    private InstProjectStatusEntity projectStatusEntity;
    private List<InstProjectStatusEntity> projectStatusEntityList = new ArrayList<>();
    private List<InstProjectStatusEntity> projectStatusEntityReport = new ArrayList<>();
    private List<InstProjectGuide> projectGuideList = new ArrayList<>();
    private InstProjectGuide projectGuide;
    private InstProjectGuideConverter projectListConverter;
    private List<InstProjectGuide> projectGuideParent = new ArrayList<>();
    private InstProjectGuide projectParent;
    private InstProjectGuideConverter projectGuideConverter;
    private Integer status;
    private InstProjectGuide parent;
    private InstProjectGuide level;

    @EJB
    private ProjectStatusService projectStatusService;

    @Override
    @PostConstruct
    public void init() {
        try {
            projectStatus = new ProjectStatusView();
            projectStatusList = new ArrayList<>();
            projectStatusEntity = new InstProjectStatusEntity();
            projectStatusEntityList = new ArrayList<>();
            projectGuideList = new ArrayList<>();
            projectGuide = new InstProjectGuide();
            projectGuideParent = new ArrayList<>();
            projectParent = new InstProjectGuide();

            this.load();
        } catch (Exception e) {
            saveError(e, "ProjectStatusService", "init");
        }

    }

    @Override
    public void filter() {

    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            projectGuideList = projectStatusService.getProjectGuideByLevel(userData.getUserBranch().getId());
            projectListConverter = new InstProjectGuideConverter(projectGuideList);
            isSearch = false;
        } catch (Exception e) {
            saveError(e, "ProjectStatusService", "load");
        }
    }

    public void search() {
        try {
            isSearch = true;
            projectStatus();
        } catch (Exception e) {
            saveError(e, "ProjectStatusService", "search");
        }

    }

    public void projectStatus() {
        try {

            projectStatusList = projectStatusService.getProjectStatusByBranchId(userData.getUserBranch().getId(), getParent() == null ? null : getParent().getId(), getLevel() == null ? null : getLevel().getId(), getStatus());
            projectStatusEntityList = new ArrayList<>();
            for (ProjectStatusView statusMapper : projectStatusList) {
                projectStatusEntity = new InstProjectStatusEntity();
                projectStatusEntity.setProjectId(statusMapper.getProjectId());
                projectStatusEntity.setProjectCode(statusMapper.getProjectCode() != null ? statusMapper.getProjectCode() : "");
                projectStatusEntity.setProjectName(statusMapper.getProjectName() != null ? statusMapper.getProjectName() : "");
                projectStatusEntity.setStatus(statusMapper.getStatus());
                if (statusMapper.getStatus() == 0) {
                    projectStatusEntity.setStatustext("متاحة");
                } else if (statusMapper.getStatus() == 1) {
                    projectStatusEntity.setStatustext("مباعة");
                }

                getProjectStatusEntityList().add(projectStatusEntity);
            }
        } catch (Exception e) {
            saveError(e, "ProjectStatusService", "projectStatus");
        }
    }

    public void projectGuideParent() {
        try {
            if (getParent() != null) {
                projectGuideParent = projectStatusService.getProjectGuideByLevelAndParent(userData.getUserBranch().getId(), getParent().getId());
                projectGuideConverter = new InstProjectGuideConverter(projectGuideParent);
            } else {
                projectGuideParent = new ArrayList<>();
            }
        } catch (Exception e) {
            saveError(e, "ProjectStatusService", "projectGuideParent");
        }

    }

    public List<InstProjectGuide> completeGuide(String query) {
        List<InstProjectGuide> instList = projectGuideList;
        if (query == null || query.trim().equals("")) {

            projectListConverter = new InstProjectGuideConverter(instList);
            return instList;
        }
        List<InstProjectGuide> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InstProjectGuide instFilter;
        for (int i = 0; i < projectGuideList.size(); i++) {
            instFilter = instList.get(i);
            nameAr = instFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(instFilter)) {
                    filteredInvs.add(instFilter);
                }
            }

            code = instFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(instFilter)) {
                    filteredInvs.add(instFilter);
                }
            }
        }

        projectListConverter = new InstProjectGuideConverter(filteredInvs);
        return filteredInvs;
    }

    public List<InstProjectGuide> completeProject(String query) {
        List<InstProjectGuide> instList = projectGuideParent;
        if (query == null || query.trim().equals("")) {

            projectGuideConverter = new InstProjectGuideConverter(instList);
            return instList;
        }
        List<InstProjectGuide> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InstProjectGuide instFilter;
        for (int i = 0; i < projectGuideParent.size(); i++) {
            instFilter = instList.get(i);
            nameAr = instFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(instFilter)) {
                    filteredInvs.add(instFilter);
                }
            }

            code = instFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(instFilter)) {
                    filteredInvs.add(instFilter);
                }
            }
        }

        projectGuideConverter = new InstProjectGuideConverter(filteredInvs);
        return filteredInvs;
    }

    //-----------------------------------------------------------------------
    public void print() {
        try {

            projectGuide = new InstProjectGuide();
            try {
                fillReport(prepareReport(), getUserData().getReportPath() + "instProjectStatus.jasper", projectStatusEntityList, "pdf");
            } catch (JRException ex) {
                Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            saveError(e, "ProjectStatusService", "print");
        }

    }

    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            if ("pdf".equals(exportType)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                servletOutputStream.flush();
                servletOutputStream.close();
                httpServletResponse.getOutputStream().flush();
                httpServletResponse.getOutputStream().close();
            } else if ("excel".equals(exportType)) {

            } else if ("html".equals(exportType)) {

            }
        } catch (Exception e) {
            saveError(e, "ProjectStatusService", "fillReport");
        }

    }

    public HashMap prepareReport() {

        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("companyName", getUserData().getCompany().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("projectGuideFromText", "دليل المشاريع الأب");
        hashMap.put("projectGuideNameFrom", level != null ? level.getName() : null);

        hashMap.put("projectGuideToText", "دليل المشاريع");
        hashMap.put("projectGuideNameTo", parent != null ? parent.getName() : null);

        hashMap.put("statusText", "حالة المشروع");

        if (getStatus() != null && getStatus() == 0) {
            hashMap.put("statusValue", "متاحة");
        } else if (getStatus() != null && getStatus() == 1) {
            hashMap.put("statusValue", "مباعة");
        } else {
            hashMap.put("statusValue", "الكل");
        }

        hashMap.put("codeText", "الكود");

        hashMap.put("unitNameText", "الاسم");
        hashMap.put("statustext", "الحالة");

        hashMap.put("projectGuideText", "تقرير الاستعلام عن حالة المشاريع");

        return hashMap;
    }
    //-----------------------------------------------------------------------

    @Override
    public String getScreenName() {
        return null;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the projectStatusList
     */
    public List<ProjectStatusView> getProjectStatusList() {
        return projectStatusList;
    }

    /**
     * @param projectStatusList the projectStatusList to set
     */
    public void setProjectStatusList(List<ProjectStatusView> projectStatusList) {
        this.projectStatusList = projectStatusList;
    }

    /**
     * @return the projectStatus
     */
    public ProjectStatusView getProjectStatus() {
        return projectStatus;
    }

    /**
     * @param projectStatus the projectStatus to set
     */
    public void setProjectStatus(ProjectStatusView projectStatus) {
        this.projectStatus = projectStatus;
    }

    /**
     * @return the projectStatusService
     */
    public ProjectStatusService getProjectStatusService() {
        return projectStatusService;
    }

    /**
     * @param projectStatusService the projectStatusService to set
     */
    public void setProjectStatusService(ProjectStatusService projectStatusService) {
        this.projectStatusService = projectStatusService;
    }

    /**
     * @return the projectStatusEntity
     */
    public InstProjectStatusEntity getProjectStatusEntity() {
        return projectStatusEntity;
    }

    /**
     * @param projectStatusEntity the projectStatusEntity to set
     */
    public void setProjectStatusEntity(InstProjectStatusEntity projectStatusEntity) {
        this.projectStatusEntity = projectStatusEntity;
    }

    /**
     * @return the projectStatusEntityList
     */
    public List<InstProjectStatusEntity> getProjectStatusEntityList() {
        return projectStatusEntityList;
    }

    /**
     * @param projectStatusEntityList the projectStatusEntityList to set
     */
    public void setProjectStatusEntityList(List<InstProjectStatusEntity> projectStatusEntityList) {
        this.projectStatusEntityList = projectStatusEntityList;
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the projectGuideList
     */
    public List<InstProjectGuide> getProjectGuideList() {
        return projectGuideList;
    }

    /**
     * @param projectGuideList the projectGuideList to set
     */
    public void setProjectGuideList(List<InstProjectGuide> projectGuideList) {
        this.projectGuideList = projectGuideList;
    }

    /**
     * @return the projectGuide
     */
    public InstProjectGuide getProjectGuide() {
        return projectGuide;
    }

    /**
     * @param projectGuide the projectGuide to set
     */
    public void setProjectGuide(InstProjectGuide projectGuide) {
        this.projectGuide = projectGuide;
    }

    /**
     * @return the projectGuideParent
     */
    public List<InstProjectGuide> getProjectGuideParent() {
        return projectGuideParent;
    }

    /**
     * @param projectGuideParent the projectGuideParent to set
     */
    public void setProjectGuideParent(List<InstProjectGuide> projectGuideParent) {
        this.projectGuideParent = projectGuideParent;
    }

    /**
     * @return the projectParent
     */
    public InstProjectGuide getProjectParent() {
        return projectParent;
    }

    /**
     * @param projectParent the projectParent to set
     */
    public void setProjectParent(InstProjectGuide projectParent) {
        this.projectParent = projectParent;
    }

    /**
     * @return the projectGuideConverter
     */
    public InstProjectGuideConverter getProjectGuideConverter() {
        return projectGuideConverter;
    }

    /**
     * @param projectGuideConverter the projectGuideConverter to set
     */
    public void setProjectGuideConverter(InstProjectGuideConverter projectGuideConverter) {
        this.projectGuideConverter = projectGuideConverter;
    }

    /**
     * @return the projectListConverter
     */
    public InstProjectGuideConverter getProjectListConverter() {
        return projectListConverter;
    }

    /**
     * @param projectListConverter the projectListConverter to set
     */
    public void setProjectListConverter(InstProjectGuideConverter projectListConverter) {
        this.projectListConverter = projectListConverter;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the parent
     */
    public InstProjectGuide getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(InstProjectGuide parent) {
        this.parent = parent;
    }

    /**
     * @return the level
     */
    public InstProjectGuide getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(InstProjectGuide level) {
        this.level = level;
    }

    /**
     * @return the projectStatusEntityReport
     */
    public List<InstProjectStatusEntity> getProjectStatusEntityReport() {
        return projectStatusEntityReport;
    }

    /**
     * @param projectStatusEntityReport the projectStatusEntityReport to set
     */
    public void setProjectStatusEntityReport(List<InstProjectStatusEntity> projectStatusEntityReport) {
        this.projectStatusEntityReport = projectStatusEntityReport;
    }
}
