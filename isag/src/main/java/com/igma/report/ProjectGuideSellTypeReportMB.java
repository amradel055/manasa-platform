package com.igma.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.isag.businessservice.InstProjectGuideService;

import com.isag.businessservice.reports.searchBean.ProjectGuideSearchBean;

import com.isag.converter.InstProjectGuideConverter;
import com.isag.converter.InstProjectGuideDTOConverter;
import com.isag.dto.InstProjectGuideDTO;

import com.isag.entity.InstProjectGuide;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;

import com.isag.report.entity.ProjectGuideBean;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

@Named("projectGuideSellTypeReportMB")
@ViewScoped
public class ProjectGuideSellTypeReportMB extends BaseReportBean {

    private Integer branchId;
    private String uri;
    private String active;
    private String screenMode;
    private List<InstProjectGuideDTO> projectGuideDTOChildList;
    private List<InstProjectGuideDTO> projectGuideDTOParentList;
    private List<InstProjectGuideDTO> projectGuideDTOParentListSelected;
    private List<InstProjectGuide> projectGuideList;
    private List<ProjectGuideBean> projectGuideBeanList;
    private InstProjectGuideConverter projectGuideConverter;
    private InstProjectGuideDTOConverter projectGuideDTOConverter;
    private InstProjectGuideDTOConverter projectGuideCildDTOConverter;
    private JasperPrint jasperPrint;

    private ProjectGuideSearchBean projectGuideSearchBean;

    @EJB
    private InstProjectGuideService projectGuideService;

    @Override
    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        setBranchId(getUserData().getUserBranch().getId());
        setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
        init();
        fillLists();

    }

    public void init() {
        setProjectGuideList(new ArrayList<>());
        setProjectGuideSearchBean(new ProjectGuideSearchBean());
        getProjectGuideSearchBean().setBranchId(branchId);
        getProjectGuideSearchBean().setShowReport(false);
        setProjectGuideBeanList(new ArrayList<>());

//        setProjectGuideDTOParentListSelected(getProjectGuideDTOParentList());
    }

    private void fillLists() {

        setProjectGuideList(projectGuideService.getAllInstProjectGuides(branchId));
        setProjectGuideConverter(new InstProjectGuideConverter(getProjectGuideList()));

    }

    @Override
    public void reset() {
        setProjectGuideList(new ArrayList<>());
        setProjectGuideBeanList(new ArrayList<>());
        setProjectGuideSearchBean(new ProjectGuideSearchBean());
    }

    @Override
    public void search() {
        setProjectGuideBeanList(new ArrayList<>(0));
        getProjectGuideSearchBean().setBranchId(getUserData().getUserBranch().getId());
        List<InstProjectGuide> list = projectGuideService.getAllprojectGuideSellAndNotSell(getProjectGuideSearchBean());
        ProjectGuideBean projectGuideBean;
        for (InstProjectGuide projectGuide : list) {
            projectGuideBean = new ProjectGuideBean();
            if (projectGuide.getActive() == null) {
                projectGuideBean.setActive("غير نشط");
            } else if (projectGuide.getActive() == true) {
                projectGuideBean.setActive("نشط");
            } else if (projectGuide.getActive() == false) {
                projectGuideBean.setActive("غير نشط");
            }

            projectGuideBean.setName(projectGuide.getName());
            projectGuideBean.setCode(projectGuide.getCode());

            projectGuideBean.setLevel(projectGuide.getLevel());
            projectGuideBean.setParent(projectGuide.getParent() != null ? (projectGuide.getParent().getName() != null ? projectGuide.getParent().getName() : null) : null);
            projectGuideBean.setSellType(projectGuide.getSell());
            projectGuideBean.setReserved(projectGuide.getReserved());
            getProjectGuideBeanList().add(projectGuideBean);
        }
    }

    public void searchNew() {
        setProjectGuideBeanList(new ArrayList<>(0));
        getProjectGuideSearchBean().setBranchId(getUserData().getUserBranch().getId());
//        List<InstProjectGuide> list = projectGuideService.getAllprojectGuideSellAndNotSell(getProjectGuideSearchBean());
        List<InstProjectGuideDTO> list = projectGuideService.getAllprojectGuideSellAndNotSellMulti(getProjectGuideSearchBean());
        ProjectGuideBean projectGuideBean;
        for (InstProjectGuideDTO projectGuide : list) {
            projectGuideBean = new ProjectGuideBean();
            if (projectGuide.getActive() == null) {
                projectGuideBean.setActive("غير نشط");
            } else if (projectGuide.getActive() == true) {
                projectGuideBean.setActive("نشط");
            } else if (projectGuide.getActive() == false) {
                projectGuideBean.setActive("غير نشط");
            }

            projectGuideBean.setName(projectGuide.getName());
            projectGuideBean.setCode(projectGuide.getCode());
            projectGuideBean.setCustomerName(projectGuide.getCustomerName());

            projectGuideBean.setLevel(projectGuide.getLevel());
            projectGuideBean.setParent(projectGuide.getParent() != null ? (projectGuide.getParent().getName() != null ? projectGuide.getParent().getName() : null) : null);
            projectGuideBean.setSellType(projectGuide.getSell());
            projectGuideBean.setReserved(projectGuide.getReserved());
            getProjectGuideBeanList().add(projectGuideBean);
        }
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    @Override
    public HashMap prepareReport() {

        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();

        hashMap.put("projectGuideIdFrom", getProjectGuideSearchBean().getProjectGuideIdFrom() != null ? getProjectGuideSearchBean().getProjectGuideIdFrom().getCode() : null);
        hashMap.put("projectGuideNameFrom", getProjectGuideSearchBean().getProjectGuideIdFrom() != null ? getProjectGuideSearchBean().getProjectGuideIdFrom().getName() : null);
        hashMap.put("projectGuideIdTo", getProjectGuideSearchBean().getProjectGuideIdTo() != null ? getProjectGuideSearchBean().getProjectGuideIdTo().getCode() : null);
        hashMap.put("projectGuideNameTo", getProjectGuideSearchBean().getProjectGuideIdTo() != null ? getProjectGuideSearchBean().getProjectGuideIdTo().getName() : null);
        hashMap.put("projectGuideText", userDDs.get("PROJ_GUID_REP"));
        hashMap.put("projectGuideFromText", userDDs.get("FROM_PROJ_GUIDE"));
        hashMap.put("projectGuideToText", userDDs.get("TO_PROJ_GUIDE"));
        hashMap.put("codeText", userDDs.get("CS_COD"));
        hashMap.put("unitNameText", userDDs.get("NAME"));
        hashMap.put("levelText", userDDs.get("LEVEL"));

        hashMap.put("parentCostCenterText", userDDs.get("MAIN_PROJ_GUID"));
        hashMap.put("accountStatusText", userDDs.get("ACCOUNT_STATUS"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        // hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {

        if (getProjectGuideBeanList() != null && !projectGuideBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "instProjectGuide.jasper", getProjectGuideBeanList(), "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
        }
    }

    public List<InstProjectGuide> completeProjectGuide(String query) {
        List<InstProjectGuide> projectGuideList = getProjectGuideList();
        if (query == null || query.trim().equals("")) {
            setProjectGuideConverter(new InstProjectGuideConverter(projectGuideList));
            return projectGuideList;
        }
        List<InstProjectGuide> filteredProjectGuide = new ArrayList<>();

        String nameAr;
        String code;
        InstProjectGuide projectGuide;
        for (int i = 0; i < getProjectGuideList().size(); i++) {
            projectGuide = projectGuideList.get(i);
            nameAr = projectGuide.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredProjectGuide.contains(projectGuide)) {
                    filteredProjectGuide.add(projectGuide);
                }
            }

            code = projectGuide.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredProjectGuide.contains(projectGuide)) {
                    filteredProjectGuide.add(projectGuide);
                }
            }
        }

        setProjectGuideConverter(new InstProjectGuideConverter(filteredProjectGuide));
        return filteredProjectGuide;
    }

    /**
     * @return the projectGuideBeanList
     */
    public List<ProjectGuideBean> getProjectGuideBeanList() {
        return projectGuideBeanList;
    }

    /**
     * @param projectGuideBeanList the projectGuideBeanList to set
     */
    public void setProjectGuideBeanList(List<ProjectGuideBean> projectGuideBeanList) {
        this.projectGuideBeanList = projectGuideBeanList;
    }

    /**
     * @return the projectGuideSearchBean
     */
    public ProjectGuideSearchBean getProjectGuideSearchBean() {
        return projectGuideSearchBean;
    }

    /**
     * @param projectGuideSearchBean the projectGuideSearchBean to set
     */
    public void setProjectGuideSearchBean(ProjectGuideSearchBean projectGuideSearchBean) {
        this.projectGuideSearchBean = projectGuideSearchBean;
    }

    /**
     * @return the projectGuideServiceService
     */
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

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * @return the projectGuideDTOParentList
     */
    public List<InstProjectGuideDTO> getProjectGuideDTOParentList() {
        if (projectGuideDTOParentList == null) {
            projectGuideDTOParentList = projectGuideService.getAllInstProjectGuidesParentDTO(getUserData().getUserBranch().getId());
            setProjectGuideDTOConverter(new InstProjectGuideDTOConverter(projectGuideDTOParentList));
        }
        return projectGuideDTOParentList;
    }

    /**
     * @param projectGuideDTOParentList the projectGuideDTOParentList to set
     */
    public void setProjectGuideDTOParentList(List<InstProjectGuideDTO> projectGuideDTOParentList) {
        this.projectGuideDTOParentList = projectGuideDTOParentList;
    }

    /**
     * @return the projectGuideDTOConverter
     */
    public InstProjectGuideDTOConverter getProjectGuideDTOConverter() {
        return projectGuideDTOConverter;
    }

    /**
     * @param projectGuideDTOConverter the projectGuideDTOConverter to set
     */
    public void setProjectGuideDTOConverter(InstProjectGuideDTOConverter projectGuideDTOConverter) {
        this.projectGuideDTOConverter = projectGuideDTOConverter;
    }

    /**
     * @return the projectGuideDTOParentListSelected
     */
    public List<InstProjectGuideDTO> getProjectGuideDTOParentListSelected() {
        return projectGuideDTOParentListSelected;
    }

    /**
     * @param projectGuideDTOParentListSelected the
     * projectGuideDTOParentListSelected to set
     */
    public void setProjectGuideDTOParentListSelected(List<InstProjectGuideDTO> projectGuideDTOParentListSelected) {
        this.projectGuideDTOParentListSelected = projectGuideDTOParentListSelected;
    }

    /**
     * @return the projectGuideDTOChildList
     */
    public List<InstProjectGuideDTO> getProjectGuideDTOChildList() {
        if (projectGuideDTOParentListSelected != null) {
            projectGuideDTOChildList = projectGuideService.getAllInstProjectGuidesChildForParent(getProjectGuideDTOParentListSelected(), getUserData().getUserBranch().getId());
            setProjectGuideCildDTOConverter(new InstProjectGuideDTOConverter(projectGuideDTOChildList));
        }
        return projectGuideDTOChildList;
    }

    public List<InstProjectGuideDTO> findProjectGuideDTOChildList() {
        if (projectGuideDTOParentListSelected != null) {
            projectGuideDTOChildList = projectGuideService.getAllInstProjectGuidesChildForParent(getProjectGuideDTOParentListSelected(), getUserData().getUserBranch().getId());
            setProjectGuideCildDTOConverter(new InstProjectGuideDTOConverter(projectGuideDTOChildList));
            getProjectGuideSearchBean().setProjectGuideListSelected(projectGuideDTOChildList);
        }
        return projectGuideDTOChildList;
    }

    /**
     * @param projectGuideDTOChildList the projectGuideDTOChildList to set
     */
    public void setProjectGuideDTOChildList(List<InstProjectGuideDTO> projectGuideDTOChildList) {
        this.projectGuideDTOChildList = projectGuideDTOChildList;
    }

    /**
     * @return the projectGuideCildDTOConverter
     */
    public InstProjectGuideDTOConverter getProjectGuideCildDTOConverter() {
        return projectGuideCildDTOConverter;
    }

    /**
     * @param projectGuideCildDTOConverter the projectGuideCildDTOConverter to
     * set
     */
    public void setProjectGuideCildDTOConverter(InstProjectGuideDTOConverter projectGuideCildDTOConverter) {
        this.projectGuideCildDTOConverter = projectGuideCildDTOConverter;
    }

    /**
     * @return the screenMode
     */
}
