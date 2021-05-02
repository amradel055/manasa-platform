package com.toby.productionReport;

import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.ProductionMovementInvoiceReportService;
import com.toby.converter.InvPurchaseInvoiceDTOConverter;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.PrintProProductDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import com.toby.views.ProductionMovementInvoiceReport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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

@Named(value = "movementInvoiceReportMB")
@ViewScoped
public class MovementInvoiceReportMB extends BaseListBean {

    private PrintProProductDTO printProductionDTO;
    private List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOs;
    private List<ProductionMovementInvoiceReport> productionMovementInvoiceReports;

    private InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter;

    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;

    @EJB
    private ProductionMovementInvoiceReportService productionMovementInvoiceReportService;

    public MovementInvoiceReportMB() {
    }

    @PostConstruct
    @Override
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        productionMovementInvoiceReports = new ArrayList<>();
        getInvPurchaseInvoiceDTOs();

    }

    public List<InvPurchaseInvoiceDTO1> completeInvpurchaseInvoice(String query) {

        return AutoComplete.completeInvpurchaseInvoice(query, invPurchaseInvoiceDTOs, invPurchaseInvoiceDTOConverter);
    }

    public void search() {
//        if ((printProductionDTO.getFromInvoice() != null && printProductionDTO.getToInvoice() != null)
//                || (printProductionDTO.getFromDate() != null && printProductionDTO.getToDate() != null)) {
            productionMovementInvoiceReports = productionMovementInvoiceReportService.getAll(printProductionDTO);
//        } else {
//            printProductionDTO.setMsg("ادخل من فاتوره الي فاتوره او من تاريخ الي تاريخ ");
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProductionDTO.getMsg()));
//        }
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
        if (productionMovementInvoiceReports != null && !productionMovementInvoiceReports.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "instProjectGuide.jasper", productionMovementInvoiceReports, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("fromInvoice", printProductionDTO.getFromInvoice().getId());
        hashMap.put("toInvoice", printProductionDTO.getToInvoice().getId());

        hashMap.put("toDate", printProductionDTO.getToDate());
        hashMap.put("fromDate", printProductionDTO.getFromDate());

        hashMap.put("fromInvoiceText", "من فاتورة");
        hashMap.put("toInvoiceText", "إلى فاتورة");
        hashMap.put("toDateText", "من تاريخ");
        hashMap.put("fromDateText", "إلى تاريخ");

        hashMap.put("invoiceText", "الفاتورة");
        hashMap.put("inventoryNameText", "الفرع");
        hashMap.put("clienNameText", "اسم العميل");
        hashMap.put("deliveryDateText", "تاريخ التسليم للطيار");
        hashMap.put("clientDateText", "تاريخ التسليم للعميل");
        hashMap.put("galaryDateText", " تاريخ التسليم للمعرض");
        hashMap.put("factoryDateText", "تاريخ التسليم للمشغل");

        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<InvPurchaseInvoiceDTO1> getInvPurchaseInvoiceDTOs() {
        if (invPurchaseInvoiceDTOs == null || invPurchaseInvoiceDTOs.isEmpty()) {
            invPurchaseInvoiceDTOs = invPurchaseInvoiceService.findInvPurchaseInvoiceDTOList(getUserData().getUser());
//            setInvPurchaseInvoiceDTOConverter(new InvPurchaseInvoiceDTOConverter(invPurchaseInvoiceDTOs));
        }
        return invPurchaseInvoiceDTOs;
    }

    public void setInvPurchaseInvoiceDTOs(List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOs) {

        this.invPurchaseInvoiceDTOs = invPurchaseInvoiceDTOs;
    }

    public InvPurchaseInvoiceDTOConverter getInvPurchaseInvoiceDTOConverter() {
        return invPurchaseInvoiceDTOConverter;
    }

    public PrintProProductDTO getPrintProductionDTO() {
        if (printProductionDTO == null) {
            printProductionDTO = new PrintProProductDTO();
        }
        return printProductionDTO;
    }

    public void setPrintProductionDTO(PrintProProductDTO printProductionDTO) {
        this.printProductionDTO = printProductionDTO;
    }

    public List<ProductionMovementInvoiceReport> getProductionMovementInvoiceReports() {
        return productionMovementInvoiceReports;
    }

    public void setProductionMovementInvoiceReports(List<ProductionMovementInvoiceReport> productionMovementInvoiceReports) {
        this.productionMovementInvoiceReports = productionMovementInvoiceReports;
    }

    /**
     * @param invPurchaseInvoiceDTOConverter the invPurchaseInvoiceDTOConverter
     * to set
     */
    public void setInvPurchaseInvoiceDTOConverter(InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter) {
        this.invPurchaseInvoiceDTOConverter = invPurchaseInvoiceDTOConverter;
    }

}
