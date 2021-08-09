/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscreport;

import com.isag.businessservice.reports.RscTenderContractViewService;
import com.isag.businessservice.reports.searchBean.RscTenderContractReportSearchBean;
import com.isag.dto.RscTenderContractViewDTO;
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
 * @author hq004
 */
@Named(value = "rscTenderContractReportMB")
@ViewScoped
public class RscTenderContractReportMB extends BaseRSCReportBean {
    //  RscExtractReportSearchBean 

    @EJB
    RscTenderContractViewService tenderContractViewService;

    private RscTenderContractReportSearchBean tenderContractReportSearchBean;
    private List<RscTenderContractViewDTO> rscTenderContractReportViewList;

    @Override
    public void reset() {
        try {
            tenderContractReportSearchBean = new RscTenderContractReportSearchBean();
            rscTenderContractReportViewList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "RscTenderContractReportMB", "reset");
        }

    }

    @Override
    public void search() {
        try {
            rscTenderContractReportViewList = tenderContractViewService.getAllRscTenderContractViewByDateSearchBean(tenderContractReportSearchBean, getUserData().getUserBranch().getId());

        } catch (Exception e) {
            saveError(e, "RscTenderContractReportMB", "search");
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

            hashMap.put("header2", "تقرير العطائات المنفذة");
            if (tenderContractReportSearchBean.getFromDate() != null) {
                hashMap.put("DateFromLabel", "من تاريخ");
                hashMap.put("DateFromValue", tenderContractReportSearchBean.getFromDate());
            }
            if (tenderContractReportSearchBean.getToDate() != null) {
                hashMap.put("DateToLabel", "الي تاريخ");
                hashMap.put("DateToValue", tenderContractReportSearchBean.getToDate());
            }
            //Column Header
            hashMap.put("DateText", "تاريخ");
            hashMap.put("orgSiteText", "اسم المقاول");
            hashMap.put("projectManager", "مدير المشروع");
            hashMap.put("generalConsultant", "الاستشارى العام");
            hashMap.put("profissionalBussinessConsultant", "استشارى الأعمال التخصصية");

            hashMap.put("serialText", "رقم العطاء");
            hashMap.put("docNumberText", "الرقم الدفترى");

            return hashMap;
        } catch (Exception e) {
            saveError(e, "RscTenderContractReportMB", "prepareReport");
            return hashMap;
        }
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {
            if (rscTenderContractReportViewList != null && !rscTenderContractReportViewList.isEmpty()) {
                fillReport(prepareReport(), getUserData().getReportPath() + "rscTenderContractReport.jasper", getRscTenderContractReportViewList(), "pdf");
            }
        } catch (Exception e) {
            saveError(e, "RscTenderContractReportMB", "exportPDF");
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

    /**
     * @return the tenderContractReportSearchBean
     */
    public RscTenderContractReportSearchBean getTenderContractReportSearchBean() {
        if (tenderContractReportSearchBean == null) {
            tenderContractReportSearchBean = new RscTenderContractReportSearchBean();
        }
        return tenderContractReportSearchBean;
    }

    /**
     * @param tenderContractReportSearchBean the tenderContractReportSearchBean
     * to set
     */
    public void setTenderContractReportSearchBean(RscTenderContractReportSearchBean tenderContractReportSearchBean) {
        this.tenderContractReportSearchBean = tenderContractReportSearchBean;
    }

    /**
     * @return the rscTenderContractReportViewList
     */
    public List<RscTenderContractViewDTO> getRscTenderContractReportViewList() {
        return rscTenderContractReportViewList;
    }

    /**
     * @param rscTenderContractReportViewList the
     * rscTenderContractReportViewList to set
     */
    public void setRscTenderContractReportViewList(List<RscTenderContractViewDTO> rscTenderContractReportViewList) {
        this.rscTenderContractReportViewList = rscTenderContractReportViewList;
    }

}
