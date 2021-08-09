/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;


import com.isag.businessservice.reports.RscWorkUnitsGuideService;
import com.isag.businessservice.reports.searchBean.RscWorkUnitsGuideSearchBean;
import com.isag.isag.UserData;
import com.isag.businessservice.reports.entityBean.RscWorkUnitsGuideBean;
import com.isag.rscreport.BaseRSCReportBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * @author Mostafa
 */
@Named("rscWorkUnitsGuideReportMB")
@ViewScoped
public class rscWorkUnitsGuideReportMB extends BaseRSCReportBean {

    private Integer branchId;
    private String uri;
    private String active;
    private String screenMode;

    private RscWorkUnitsGuideSearchBean rscWorkUnitsGuideSearchBean; //search
    private List<RscWorkUnitsGuideBean> rscWorkUnitsGuideBeans;//result
    
    @EJB
    private RscWorkUnitsGuideService rscWorkUnitsGuideService;
      
    @Override
    public void reset() {
        setRscWorkUnitsGuideBeans(new ArrayList<>());
        setRscWorkUnitsGuideSearchBean(new RscWorkUnitsGuideSearchBean()); 
    }

    @Override
    public void search() {
        try {
        setRscWorkUnitsGuideBeans(rscWorkUnitsGuideService.getAllRscWorkUnitsGuideBeanBySearchBean(rscWorkUnitsGuideSearchBean, getUserData().getUserBranch().getId()));
         } catch (Exception e) {
            saveError(e, "rscWorkUnitsGuideReportMB", "search");
        }
    }

    @Override
    public HashMap prepareReport() {
    
        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();

       
        hashMap.put("WorkUnitsNameFrom", getRscWorkUnitsGuideSearchBean().getRscWorkUnitsGuideDTOFrom()!= null ? getRscWorkUnitsGuideSearchBean().getRscWorkUnitsGuideDTOFrom().getName() : null);
        hashMap.put("WorkUnitsNameTo", getRscWorkUnitsGuideSearchBean().getRscWorkUnitsGuideDTOTo()!= null ? getRscWorkUnitsGuideSearchBean().getRscWorkUnitsGuideDTOTo().getName() : null);
        
        hashMap.put("WorkUnitsGuideText", "تقرير دليل بنود الاعمال");
        hashMap.put("WorkUnitsGuideFromText", "من بند");
        hashMap.put("WorkUnitsGuideToText", "الي بند");
        hashMap.put("codeText", "الكود");
        hashMap.put("unitNameText", "الاسم");
        hashMap.put("levelText", "المستوي");

        hashMap.put("type", "نوع البند");
        hashMap.put("accountStatusText", "حاله الحساب");
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

    @Override
    public void exportPDF(ActionEvent actionEvent)  {
     try {
        if (rscWorkUnitsGuideBeans != null && !rscWorkUnitsGuideBeans.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "rscworkunitsguide.jasper", getRscWorkUnitsGuideBeans(), "pdf");
            print();
            search();
        }
         } catch (Exception e) {
            saveError(e, "rscWorkUnitsGuideReportMB", "exportPDF");
        } 
    
    }
    private void print(){
    
    System.out.print("Print .............");
    
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

    /**
     * @return the rscWorkUnitsGuideSearchBean
     */
    public RscWorkUnitsGuideSearchBean getRscWorkUnitsGuideSearchBean() {
        if (rscWorkUnitsGuideSearchBean == null) {
            rscWorkUnitsGuideSearchBean = new RscWorkUnitsGuideSearchBean();
        }
        return rscWorkUnitsGuideSearchBean;
    }

    /**
     * @param rscWorkUnitsGuideSearchBean the rscWorkUnitsGuideSearchBean to set
     */
    public void setRscWorkUnitsGuideSearchBean(RscWorkUnitsGuideSearchBean rscWorkUnitsGuideSearchBean) {
        this.rscWorkUnitsGuideSearchBean = rscWorkUnitsGuideSearchBean;
    }

    /**
     * @return the rscWorkUnitsGuideBeans
     */
    public List<RscWorkUnitsGuideBean> getRscWorkUnitsGuideBeans() {
//        if (rscWorkUnitsGuideBeans.isEmpty() || rscWorkUnitsGuideBeans == null) {
//            rscWorkUnitsGuideBeans = new ArrayList<>();
//        }
        return rscWorkUnitsGuideBeans;
    }

    /**
     * @param rscWorkUnitsGuideBeans the rscWorkUnitsGuideBeans to set
     */
    public void setRscWorkUnitsGuideBeans(List<RscWorkUnitsGuideBean> rscWorkUnitsGuideBeans) {
        this.rscWorkUnitsGuideBeans = rscWorkUnitsGuideBeans;
    }

    /**
     * @return the branchId
     */
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
     * @return the screenMode
     */
    public String getScreenMode() {
        return screenMode;
    }

    /**
     * @param screenMode the screenMode to set
     */
    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }
}
