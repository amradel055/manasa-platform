package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.toby.Basic;
import com.toby.views.CustomerBalances;
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

@Named(value = "customerBalancesReport")
@ViewScoped
public class CustomerBalancesReport extends Basic {

    private List<CustomerBalances> customerBalancesesList;
    private CustomerBalances customerBalances;

    private InvInventoryDTOConverter gallaryDTOConverter;
    private List<InvInventoryDTO> gallaryList;

    @EJB
    private ReportsService reportsService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

    public void search() {
        setCustomerBalancesesList(reportsService.getCustomerBalances(customerBalances, getUserData().getUserBranch().getId()));
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
     * @return the gallaryDTOConverter
     */
    public InvInventoryDTOConverter getGallaryDTOConverter() {
        return gallaryDTOConverter;
    }

    /**
     * @param gallaryDTOConverter the gallaryDTOConverter to set
     */
    public void setGallaryDTOConverter(InvInventoryDTOConverter gallaryDTOConverter) {
        this.gallaryDTOConverter = gallaryDTOConverter;
    }

    /**
     * @return the gallaryList
     */
    public List<InvInventoryDTO> getGallaryListRole1() {
        
        if (gallaryList == null || gallaryList.isEmpty()) {
            gallaryList = tobyUserInventoryService.getAllGallaryByUserAndBranch(getUserData().getUser());
            setGallaryDTOConverter(new InvInventoryDTOConverter(gallaryList));
        }
        return gallaryList;
    }

    /**
     * @param gallaryList the gallaryList to set
     */
    public void setGallaryList(List<InvInventoryDTO> gallaryList) {
        this.gallaryList = gallaryList;
    }

    /**
     * @return the salesReportByValueAdedDetailsList
     */
}
