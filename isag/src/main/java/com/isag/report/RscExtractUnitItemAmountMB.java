/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.reports.RscExtractUnitItemAmountViewService;
import com.isag.businessservice.reports.entityBean.RscExtractUnitItemAmountViewBean;
import com.isag.businessservice.reports.searchBean.RscExtractUnitItemAmountSearchBean;
import com.isag.isag.BaseReportBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author hhhh
 */
@Named("rscExtractUnitItemAmountMB")
@ViewScoped
public class RscExtractUnitItemAmountMB extends BaseReportBean {

    // Objects
    private RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean;
    private List<RscExtractUnitItemAmountViewBean> extractUnitItemAmountViewBeanList;
    private Boolean viewDetail;
    

    @EJB
    RscExtractUnitItemAmountViewService rscExtractUnitItemAmountViewService;

    public void load() {

    }

    @Override
    public void reset() {
        rscExtractUnitItemAmountSearchBean = new RscExtractUnitItemAmountSearchBean();
        extractUnitItemAmountViewBeanList = new ArrayList<>();
    }

    @Override
    public void search() {
        setExtractUnitItemAmountViewBeanList(rscExtractUnitItemAmountViewService.findRscExtractUnitItemViewService(rscExtractUnitItemAmountSearchBean, getUserData().getUserBranch().getId()));
    }
    public void searchDetail(Integer itemId) {
        setExtractUnitItemAmountViewBeanList(rscExtractUnitItemAmountViewService.findRscExtractUnitItemDetailViewService(rscExtractUnitItemAmountSearchBean, getUserData().getUserBranch().getId(),itemId));
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        String reportName = "workUnitAmountTotal.jasper";
        if (getViewDetail()) {
            reportName = "workUnitAmountTotalDetail.jasper";
        }
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getExtractUnitItemAmountViewBeanList(), "pdf");
    }

    @Override
    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
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
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("UserName", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
        //  hashMap.put("CompanyName", getUserData().getCompany().getName());

        //  hashMap.put("CompanyLogo", getUserData().getImageReportPath());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        
        hashMap.put("dateText", userDDs.get("DATE"));

        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("dateFromValue", getRscExtractUnitItemAmountSearchBean().getDateFrom());
        hashMap.put("dateToText", userDDs.get("YEAR_TO"));
        hashMap.put("dateToValue", getRscExtractUnitItemAmountSearchBean().getDateTo());
        hashMap.put("header2", "تكاليف بنود الاعمال");
        hashMap.put("projectText", "المشروع");
        hashMap.put("balanceText", "التكلفه");
        hashMap.put("serialText", "البند");
        hashMap.put("siteText",  "المقاول");
        hashMap.put("remarkText",  "العميل");
        hashMap.put("subreport", getUserData().getReportPath() + "workUnitAmountTotalDetail_sub.jasper");
        hashMap.put("totalText", userDDs.get("TOTAL"));
       
        HashMap param = new HashMap();
        hashMap.put("param", param); 
        param.put("guideNameTXt", "البند");
        param.put("excuteQtyTxt", "الكميه المنفذه");
        param.put("priceSellTxt", "سعر البيع");
        param.put("discountValueTxt", "المقاول");
        param.put("discountPercentageTxt", "المشروع");
        param.put("netCostTxt", "صافي بند المستخلص");

        return hashMap;
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
     * @return the rscExtractUnitItemAmountSearchBean
     */
    public RscExtractUnitItemAmountSearchBean getRscExtractUnitItemAmountSearchBean() {
        if(rscExtractUnitItemAmountSearchBean == null){
            rscExtractUnitItemAmountSearchBean = new RscExtractUnitItemAmountSearchBean();
        }
        return rscExtractUnitItemAmountSearchBean;
    }

    /**
     * @param rscExtractUnitItemAmountSearchBean the
     * rscExtractUnitItemAmountSearchBean to set
     */
    public void setRscExtractUnitItemAmountSearchBean(RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean) {
        this.rscExtractUnitItemAmountSearchBean = rscExtractUnitItemAmountSearchBean;
    }

    /**
     * @return the extractUnitItemAmountViewBeanList
     */
    public List<RscExtractUnitItemAmountViewBean> getExtractUnitItemAmountViewBeanList() {
        if (extractUnitItemAmountViewBeanList == null) {
            extractUnitItemAmountViewBeanList = new ArrayList<>();
        }
        return extractUnitItemAmountViewBeanList;
    }

    /**
     * @param extractUnitItemAmountViewBeanList the
     * extractUnitItemAmountViewBeanList to set
     */
    public void setExtractUnitItemAmountViewBeanList(List<RscExtractUnitItemAmountViewBean> extractUnitItemAmountViewBeanList) {
        this.extractUnitItemAmountViewBeanList = extractUnitItemAmountViewBeanList;
    }

    /**
     * @return the viewDetail
     */
    public Boolean getViewDetail() {
        return viewDetail;
    }

    /**
     * @param viewDetail the viewDetail to set
     */
    public void setViewDetail(Boolean viewDetail) {
        this.viewDetail = viewDetail;
    }
}
