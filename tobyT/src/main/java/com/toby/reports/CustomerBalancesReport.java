package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.toby.Basic;
import com.toby.views.CustomerBalances;
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

@Named(value = "customerBalancesReport")
@ViewScoped
public class CustomerBalancesReport extends Basic {

    private List<CustomerBalances> customerBalancesesList;
    private CustomerBalances customerBalances;

    @EJB
    private ReportsService reportsService;

    public void search() {
        setCustomerBalancesesList(reportsService.getCustomerBalances(customerBalances, getUserData().getUserBranch().getId()));
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        String reportName = "CustomerBalancesReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getCustomerBalancesesList(), "pdf");
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
        hashMap.put("gallerySelectedValue", customerBalances.getGallerySelected() == null ? null : customerBalances.getGallerySelected().getName());
        hashMap.put("clientCodeText", " كود العميل");
        hashMap.put("clientNameText", "اسم العميل");
        hashMap.put("debitText", " المدين");
        hashMap.put("creditorText", " الدائن");
        hashMap.put("balanceText", " الرصيد");

        return hashMap;
    }

    public List<CustomerBalances> getCustomerBalancesesList() {
        return customerBalancesesList;
    }

    /**
     * @param customerBalancesesList the customerBalancesesList to set
     */
    public void setCustomerBalancesesList(List<CustomerBalances> customerBalancesesList) {
        this.customerBalancesesList = customerBalancesesList;
    }

    /**
     * @return the customerBalances
     */
    public CustomerBalances getCustomerBalances() {
        if (customerBalances == null) {
            customerBalances = new CustomerBalances();
        }
        return customerBalances;
    }

    /**
     * @param customerBalances the customerBalances to set
     */
    public void setCustomerBalances(CustomerBalances customerBalances) {
        this.customerBalances = customerBalances;
    }

    /**
     * @return the salesReportByValueAdedDetailsList
     */
}
