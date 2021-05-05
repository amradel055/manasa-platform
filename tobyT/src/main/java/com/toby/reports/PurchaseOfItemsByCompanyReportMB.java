/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.report.PurchaseOfItemsBySuppliersReportService;
import com.toby.businessservice.reports.searchBean.PurchaseOfItemsBySuppliersSearchBean;
import com.toby.toby.BaseInventoryReportBean;
import com.toby.views.SalesOfItemsByCompany;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hhhh
 */
@Named("purchaseOfItemsByCompanyReportMB")
@ViewScoped
public class PurchaseOfItemsByCompanyReportMB extends BaseInventoryReportBean {

    private PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean;

    private List<SalesOfItemsByCompany> salesOfItemsByCompanyList;

   

    @EJB
    PurchaseOfItemsBySuppliersReportService purchaseOfItemsBySuppliersReportService;

    @Override
    public void reset() {
        purchaseOfItemsBySuppliersSearchBean = null;
        salesOfItemsByCompanyList = null;
    }

    @Override
    public void search() {
        setSalesOfItemsByCompanyList(purchaseOfItemsBySuppliersReportService.getAllSalesOfItemsByCompany(getPurchaseOfItemsBySuppliersSearchBean()));
    }
    
     public void convertDateFrom() {
        getPurchaseOfItemsBySuppliersSearchBean().setDateFrom(findDateFromHijriFrom());
    }

    public void convertDateTo() {
         getPurchaseOfItemsBySuppliersSearchBean().setDateTo(findDateFromHijriTo());
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        String reportName = "SalesOfItemsByCompany.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getSalesOfItemsByCompanyList(), "pdf");
    }

    @Override
    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("dateFromText", "من تاريخ");
        hashMap.put("dateFromValue", getPurchaseOfItemsBySuppliersSearchBean().getDateFrom());
        hashMap.put("dateToText", "الى تاريخ");
        hashMap.put("dateToValue", getPurchaseOfItemsBySuppliersSearchBean().getDateTo());

        hashMap.put("fromInventoryDTONameText", "من رقم المستودع");
        hashMap.put("fromInventoryDTONameValue", getPurchaseOfItemsBySuppliersSearchBean().getFromInventoryDTOName() == null ? null : getPurchaseOfItemsBySuppliersSearchBean().getFromInventoryDTOName().getName());

        hashMap.put("companyCodeText", "كود الشركة");
        hashMap.put("companyNameText", "اسم الشركة");
        hashMap.put("totalSalesText", " إجمالي المبيعات");

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
     * @return the purchaseOfItemsBySuppliersSearchBean
     */
    public PurchaseOfItemsBySuppliersSearchBean getPurchaseOfItemsBySuppliersSearchBean() {
        if (purchaseOfItemsBySuppliersSearchBean == null) {
            purchaseOfItemsBySuppliersSearchBean = new PurchaseOfItemsBySuppliersSearchBean();
        }
        return purchaseOfItemsBySuppliersSearchBean;
    }

    /**
     * @param purchaseOfItemsBySuppliersSearchBean the
     * purchaseOfItemsBySuppliersSearchBean to set
     */
    public void setPurchaseOfItemsBySuppliersSearchBean(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean) {
        this.purchaseOfItemsBySuppliersSearchBean = purchaseOfItemsBySuppliersSearchBean;
    }

    /**
     * @return the salesOfItemsByCompanyList
     */
    public List<SalesOfItemsByCompany> getSalesOfItemsByCompanyList() {
        if (salesOfItemsByCompanyList == null) {
            salesOfItemsByCompanyList = new ArrayList<>();
        }
        return salesOfItemsByCompanyList;
    }

    /**
     * @param salesOfItemsByCompanyList the salesOfItemsByCompanyList to set
     */
    public void setSalesOfItemsByCompanyList(List<SalesOfItemsByCompany> salesOfItemsByCompanyList) {
        this.salesOfItemsByCompanyList = salesOfItemsByCompanyList;
    }

  

}
