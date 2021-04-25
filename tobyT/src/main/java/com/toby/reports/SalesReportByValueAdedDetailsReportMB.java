package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.toby.Basic;
import com.toby.views.SalesReportByValueAded;
import com.toby.views.SalesReportByValueAdedDetails;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import net.sf.jasperreports.engine.JRException;

@Named(value = "salesReportByValueAdedDetailsReportMB")
@ViewScoped
public class SalesReportByValueAdedDetailsReportMB extends Basic {

    private List<SalesReportByValueAdedDetails> salesReportByValueAdedDetailsList;
    private SalesReportByValueAdedDetails salesReportByValueAdedDetails;

    @EJB
    private ReportsService reportsService;

    public void search() {
        setSalesReportByValueAdedDetailsList(reportsService.getSalesReportByValueAdedDetails(salesReportByValueAdedDetails, getUserData().getUserBranch().getId()));
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        String reportName = "SalesReportByValueAdedDetailsReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getSalesReportByValueAdedDetailsList(), "pdf");
    }

    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("gallerySelectedText", " معرض");
        hashMap.put("gallerySelectedValue", salesReportByValueAdedDetails.getGallerySelected() == null ? null : salesReportByValueAdedDetails.getGallerySelected().getName());
        hashMap.put("dateFromText", " من تاريخ");
        hashMap.put("dateFromValue", salesReportByValueAdedDetails.getDateFrom());
        hashMap.put("dateToText", " الى تاريخ");
        hashMap.put("dateToValue", salesReportByValueAdedDetails.getDateTo());
        hashMap.put("invoiceNumberText", " رقم الفاتورة");
        hashMap.put("invoiceTypeText", " نوع الفاتورة");
        hashMap.put("invoiceDateText", "  التاريخ");
        hashMap.put("galleryNameText", " اسم المعرض");
        hashMap.put("employeeNameText", " اسم الموظف");
        hashMap.put("discountText", "الخصم");
        hashMap.put("totalBeforeTaxText", "قبل الضريبة");
        hashMap.put("totalAfterTaxText", "بعد الضريبة");
        hashMap.put("taxText", " بالضريبة");

        return hashMap;
    }

    /**
     * @return the salesReportByValueAdedDetailsList
     */
    public List<SalesReportByValueAdedDetails> getSalesReportByValueAdedDetailsList() {
        return salesReportByValueAdedDetailsList;
    }

    /**
     * @param salesReportByValueAdedDetailsList the
     * salesReportByValueAdedDetailsList to set
     */
    public void setSalesReportByValueAdedDetailsList(List<SalesReportByValueAdedDetails> salesReportByValueAdedDetailsList) {
        this.salesReportByValueAdedDetailsList = salesReportByValueAdedDetailsList;
    }

    /**
     * @return the salesReportByValueAdedDetails
     */
    public SalesReportByValueAdedDetails getSalesReportByValueAdedDetails() {

        if (salesReportByValueAdedDetails == null) {
            salesReportByValueAdedDetails = new SalesReportByValueAdedDetails();
        }
        return salesReportByValueAdedDetails;
    }

    /**
     * @param salesReportByValueAdedDetails the salesReportByValueAdedDetails to
     * set
     */
    public void setSalesReportByValueAdedDetails(SalesReportByValueAdedDetails salesReportByValueAdedDetails) {
        this.salesReportByValueAdedDetails = salesReportByValueAdedDetails;
    }

    /**
     * @return the invoiceNumber
     */
}
