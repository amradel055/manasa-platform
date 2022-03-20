package com.igma.report;

import com.toby.businessservice.ReportsService;
import com.toby.toby.Basic;
import com.toby.views.CustomerBalancesDebtAge;
import com.toby.views.CustomerBalancesDebtAgeSearch;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

@Named(value = "customerBalancesDebtAgeReport")
@ViewScoped
public class CustomerBalancesDebtAgeReport extends Basic {

    private List<CustomerBalancesDebtAge> customerBalancesesList;
    private CustomerBalancesDebtAgeSearch customerBalances;

    @EJB
    private ReportsService reportsService;

    public void search() {
        setCustomerBalancesesList(reportsService.getCustomerBalancesDebtAge(customerBalances));
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);

            cell.setCellStyle(cellStyle);
        }
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
//        hashMap.put("gallerySelectedValue", customerBalances.getGallerySelected() == null ? null : customerBalances.getGallerySelected().getName());
        hashMap.put("clientCodeText", " كود العميل");
        hashMap.put("clientNameText", "اسم العميل");
        hashMap.put("debitText", " المدين");
        hashMap.put("creditorText", " الدائن");
        hashMap.put("balanceText", " الرصيد");

        return hashMap;
    }

    public List<CustomerBalancesDebtAge> getCustomerBalancesesList() {
        return customerBalancesesList;
    }

    /**
     * @param customerBalancesesList the customerBalancesesList to set
     */
    public void setCustomerBalancesesList(List<CustomerBalancesDebtAge> customerBalancesesList) {
        this.customerBalancesesList = customerBalancesesList;
    }

    /**
     * @return the customerBalances
     */
    public CustomerBalancesDebtAgeSearch getCustomerBalances() {
        if (customerBalances == null) {
            customerBalances = new CustomerBalancesDebtAgeSearch();
        }
        return customerBalances;
    }

    /**
     * @param customerBalances the customerBalances to set
     */
    public void setCustomerBalances(CustomerBalancesDebtAgeSearch customerBalances) {
        this.customerBalances = customerBalances;
    }
}
