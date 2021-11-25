/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscreport;

import com.isag.businessservice.RscRestrictionQuantitiesService;
import com.isag.businessservice.reports.entityBean.RscExtractReport;
import com.isag.businessservice.reports.searchBean.RscExtractReportSearchBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Mostafa
 */
@Named(value = "rscQuantitiesReportMB")
@ViewScoped
public class RscQuantitiesReportMB extends BaseRSCReportBean {
    //  RscExtractReportSearchBean 

    @EJB
    RscRestrictionQuantitiesService restrictionQuantitiesService;

    private RscExtractReportSearchBean rscExtractReportSearchBean;//search
    private List<RscExtractReport> rscExtractReportList;//result

    /**
     * @return the rscExtractReportSearchBean
     */
    public RscExtractReportSearchBean getRscExtractReportSearchBean() {
        if (rscExtractReportSearchBean == null) {
            rscExtractReportSearchBean = new RscExtractReportSearchBean();
        }
        return rscExtractReportSearchBean;
    }

    /**
     * @param rscExtractReportSearchBean the rscExtractReportSearchBean to set
     */
    public void setRscExtractReportSearchBean(RscExtractReportSearchBean rscExtractReportSearchBean) {
        this.rscExtractReportSearchBean = rscExtractReportSearchBean;
    }

    /**
     * @return the rscExtractReportList
     */
    public List<RscExtractReport> getRscExtractReportList() {
        return rscExtractReportList;
    }

    /**
     * @param rscExtractReportList the rscExtractReportList to set
     */
    public void setRscExtractReportList(List<RscExtractReport> rscExtractReportList) {
        this.rscExtractReportList = rscExtractReportList;
    }

    public void shortReportListing() {

    }

    @Override
    public void reset() {
        try {
        rscExtractReportSearchBean = new RscExtractReportSearchBean(); // reset search
        rscExtractReportList = new ArrayList<>(); //reset result
         } catch (Exception e) {
            saveError(e, "RscExtractReportMB", "reset");
        }

    }

    @Override
    public void search() {
        try {
        setRscExtractReportList(restrictionQuantitiesService.getAllRscExtractReport(rscExtractReportSearchBean, getUserData().getUserBranch().getId()));
         } catch (Exception e) {
            saveError(e, "RscExtractReportMB", "search");
        }

    }

    @Override
    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();
        try {
        Map<String, String> userDDs = getUserData().getUserDDs();
        

        hashMap.put("UserName", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        //  hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("CompanyName", "اسم الشركه");

        hashMap.put("header2", "تقرير حصر الاعمال");
        if (rscExtractReportSearchBean.getDateFrom() != null) {
            hashMap.put("DateFromLabel", "من تاريخ");
            hashMap.put("DateFromValue", rscExtractReportSearchBean.getDateFrom());
        }
        if (rscExtractReportSearchBean.getDateTo() != null) {
            hashMap.put("DateToLabel", "الي تاريخ");
            hashMap.put("DateToValue", rscExtractReportSearchBean.getDateTo());
        }
        //Column Header
        hashMap.put("contractorName", "اسم المقاول");
        hashMap.put("serial", "رقم المستخلص");
        hashMap.put("ExtractPrice", "قيمه المستخلص");
        hashMap.put("date", "تاريخ");
        hashMap.put("remarks", "الملاحظات");

        return hashMap;
         } catch (Exception e) {
            saveError(e, "RscExtractReportMB", "prepareReport");
            return hashMap;
        }
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {
        if (rscExtractReportList != null && !rscExtractReportList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "rscExtractReport.jasper", getRscExtractReportList(), "pdf");
        }
         } catch (Exception e) {
            saveError(e, "RscExtractReportMB", "exportPDF");
        }

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

}
