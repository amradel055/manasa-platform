/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InstProjectGuideService;
import com.isag.businessservice.RscExtractRegisterService;
import com.isag.businessservice.reports.RscExtractReportService;
import com.isag.businessservice.reports.searchBean.ProjectAccountSearchBean;

import com.isag.converter.InstProjectGuideDTOConverter;

import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.RscExtractRegisterDTO;

import com.isag.isag.AutoComplete;

import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hhhh
 */
@Named("projectAccountReportMB")
@ViewScoped
public class ProjectAccountReportMB extends BaseReportBean {

    // Objects
    private String uri;
    private String screenMode;
    private AutoComplete autoComplete;
    private InstProjectGuideDTOConverter instProjectGuideDTOConverter;
    private ProjectAccountSearchBean projectAccountSearchBean;
    private List<InstProjectGuideDTO> instProjectGuideDtoList;
    private List<RscExtractRegisterDTO> rscExtractRegisterDTOView;
    @EJB
    private InstProjectGuideService instProjectGuideService;

    @EJB
    private RscExtractReportService extractReportService;
    

    @Override
    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        initObjs();
        fillLists();
    }

    public void fillLists() {
        getInstProjectGuideDtoList();
    }

    private void initObjs() {
        getProjectAccountSearchBean().setBranchId(getUserlogin().getBranchId().getId());
        fillLists();
    }
    
//    public void fillDetail(RscExtractRegisterDTO rscExtractRegisterDTO){
//        rscExtractRegisterDTO = rscExtractRegisterService.getAllRscExtractRegisterById(rscExtractRegisterDTO.getId());
//        for (RscExtractRegisterDTO detailDTO : getRscExtractRegisterDTOView()) {
//            if (detailDTO.getIndex().equals(rscExtractRegisterDTO.getIndex())) {
//                int itemIndex = getRscExtractRegisterDTOView().indexOf(detailDTO);
//                if (itemIndex != -1) {
//                    getRscExtractRegisterDTOView().set(itemIndex, rscExtractRegisterDTO);
//                }
//            }
//        }
//    }

    @Override
    public void reset() {
        setProjectAccountSearchBean(new ProjectAccountSearchBean());
        setRscExtractRegisterDTOView(new ArrayList<>());
        initObjs();
    }

    public void showItemReport() {
    }

    @Override
    public void search() {

//        setInstProjectGuideDTORootList(new ArrayList<>());
//       setInstProjectGuideDTORootList(extractReportService.getAllRscExtractRegisterDetailReportMB(projectAccountSearchBean));
//        
//       setRscExtractRegisterDTOView(new ArrayList<>());
//    rscExtractRegisterDTOView =  extractReportService.getAllRscExtractRegisterReportMB(projectAccountSearchBean);
        rscExtractRegisterDTOView = extractReportService.getAllRscExtractRegisterReport(projectAccountSearchBean);
        projectAccountSearchBean.setTotle(rscExtractRegisterDTOView.get(rscExtractRegisterDTOView.size() - 1).getTotalFinal());

    }

 
    
    
    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        String reportName = "projectAccount.jasper";
        if (projectAccountSearchBean.getDetailed()) {
            reportName = "projectAccountDetail.jasper";
        }
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getRscExtractRegisterDTOView(), "pdf");
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
       HashMap hashMap = new HashMap();

        hashMap.put("UserName", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("dateText", userDDs.get("DATE"));

        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("dateFromValue", projectAccountSearchBean.getDateFrom());
        hashMap.put("dateToText", userDDs.get("YEAR_TO"));
        hashMap.put("dateToValue", projectAccountSearchBean.getDateTo());
        hashMap.put("header2", "كشف حساب مشروع");
        hashMap.put("projectText", "المشروع");
        hashMap.put("balanceText", "التكلفه");
        hashMap.put("serialText", "رقم المستخلص");
        hashMap.put("siteText",  "المقاول");
        hashMap.put("remarkText",  "البيان");
        hashMap.put("subreport", getUserData().getReportPath() + "projectAccountDetail_sub.jasper");
        hashMap.put("totalText", userDDs.get("TOTAL"));
       
        HashMap param = new HashMap();
        hashMap.put("param", param); 
        param.put("guideNameTXt", "البند");
        param.put("excuteQtyTxt", "الكميه المنفذه");
        param.put("priceSellTxt", "سعر البيع");
        param.put("discountValueTxt", "خصم قيمة");
        param.put("discountPercentageTxt", "خصم نسبة");
        param.put("netCostTxt", "صافي بند المستخلص");
        
       

        return hashMap;
    }

    public List<InstProjectGuideDTO> completeInstProjectFrom(String query) {
        return AutoComplete.completeInstProjectDTO(query, getInstProjectGuideDtoList(), instProjectGuideDTOConverter);
    }

    public List<InstProjectGuideDTO> completeInstProjectTo(String query) {
        return AutoComplete.completeInstProjectDTO(query, getInstProjectGuideDtoList(), instProjectGuideDTOConverter);
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
     * @return the autoComplete
     */
    private AutoComplete getAutoComplete() {
        if (autoComplete == null) {
            autoComplete = new AutoComplete();
        }
        return autoComplete;
    }

    /**
     * @return the instProjectGuideDTOConverter
     */
    public InstProjectGuideDTOConverter getInstProjectGuideDTOConverter() {
        return instProjectGuideDTOConverter;
    }

    /**
     * @param instProjectGuideDTOConverter the instProjectGuideDTOConverter to
     * set
     */
    public void setInstProjectGuideDTOConverter(InstProjectGuideDTOConverter instProjectGuideDTOConverter) {
        this.instProjectGuideDTOConverter = instProjectGuideDTOConverter;
    }

    /**
     * @return the projectAccountSearchBean
     */
    public ProjectAccountSearchBean getProjectAccountSearchBean() {
        if (projectAccountSearchBean == null) {
            projectAccountSearchBean = new ProjectAccountSearchBean();
        }
        return projectAccountSearchBean;
    }

    /**
     * @param projectAccountSearchBean the projectAccountSearchBean to set
     */
    public void setProjectAccountSearchBean(ProjectAccountSearchBean projectAccountSearchBean) {
        this.projectAccountSearchBean = projectAccountSearchBean;
    }

    /**
     * @return the instProjectGuideDtoList
     */
    public List<InstProjectGuideDTO> getInstProjectGuideDtoList() {
        if (instProjectGuideDtoList == null || instProjectGuideDtoList.isEmpty()) {
            instProjectGuideDtoList = instProjectGuideService.getAllSubInstProjectGuideByBranchIdActiveAndLevelDTO(getUserData().getUserBranch().getId(), 2, 3);
            setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(instProjectGuideDtoList));
        }
        return instProjectGuideDtoList;
    }

    /**
     * @param instProjectGuideDtoList the instProjectGuideDtoList to set
     */
    public void setInstProjectGuideDtoList(List<InstProjectGuideDTO> instProjectGuideDtoList) {
        this.instProjectGuideDtoList = instProjectGuideDtoList;
    }

    /**
     * @return the rscExtractRegisterDTOView
     */
    public List<RscExtractRegisterDTO> getRscExtractRegisterDTOView() {
        return rscExtractRegisterDTOView;
    }

    /**
     * @param rscExtractRegisterDTOView the rscExtractRegisterDTOView to set
     */
    public void setRscExtractRegisterDTOView(List<RscExtractRegisterDTO> rscExtractRegisterDTOView) {
        this.rscExtractRegisterDTOView = rscExtractRegisterDTOView;
    }
}
