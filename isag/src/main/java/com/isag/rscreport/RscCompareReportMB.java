/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscreport;

import com.isag.businessservice.RscTenderService;
import com.isag.businessservice.reports.searchBean.RscExtractReportSearchBean;
import com.isag.dto.RscTenderDTO;
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
@Named(value = "rscCompareReportMB")
@ViewScoped
public class RscCompareReportMB extends BaseRSCReportBean {
    //  RscExtractReportSearchBean 

    @EJB
    RscTenderService rscTenderService;

    private RscExtractReportSearchBean rscExtractReportSearchBean;//search
    private List<RscTenderDTO> RscTenderDTOReportList;//result

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

    public void shortReportListing() {

    }

    @Override
    public void reset() {
        try {
            setRscExtractReportSearchBean(new RscExtractReportSearchBean()); // reset search
            RscTenderDTOReportList = new ArrayList<>(); //reset result
        } catch (Exception e) {
            saveError(e, "RscExtractReportMB", "reset");
        }

    }

    @Override
    public void search() {
        try {
            setRscTenderDTOReportList(rscTenderService.getAllRscExtractReport(getRscExtractReportSearchBean(), getUserData().getUserBranch().getId()));
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
            if (getRscExtractReportSearchBean().getDateFrom() != null) {
                hashMap.put("DateFromLabel", "من تاريخ");
                hashMap.put("DateFromValue", getRscExtractReportSearchBean().getDateFrom());
            }
            if (getRscExtractReportSearchBean().getDateTo() != null) {
                hashMap.put("DateToLabel", "الي تاريخ");
                hashMap.put("DateToValue", getRscExtractReportSearchBean().getDateTo());
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
            if (RscTenderDTOReportList != null && !RscTenderDTOReportList.isEmpty()) {
                fillReport(prepareReport(), getUserData().getReportPath() + "rscExtractReport.jasper", getRscTenderDTOReportList(), "pdf");
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

    /**
     * @return the RscTenderDTOReportList
     */
    public List<RscTenderDTO> getRscTenderDTOReportList() {
        return RscTenderDTOReportList;
    }

    /**
     * @param RscTenderDTOReportList the RscTenderDTOReportList to set
     */
    public void setRscTenderDTOReportList(List<RscTenderDTO> RscTenderDTOReportList) {
        this.RscTenderDTOReportList = RscTenderDTOReportList;
    }

}
