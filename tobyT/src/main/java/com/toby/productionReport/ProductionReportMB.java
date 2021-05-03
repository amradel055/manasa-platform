package com.toby.productionReport;

import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.businessservice.ProProductionTransactionService;
import com.toby.businessservice.ProductionReportService;
import com.toby.converter.InvPurchaseInvoiceDTOConverter;
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.converter.ProProductionTransactionDTOConverter;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import com.toby.views.ProductionReport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

@Named(value = "productionReportMB")
@ViewScoped
public class ProductionReportMB extends BaseListBean {

    private List<ProProductionStagesDTO> proProductionStagesDTOList;
    private List<ProProductionTransactionDTO> proProductionTransactionDTOList;
    private List<ProductionReport> productionReports;
    private List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOList;
    private PrintProductionDTO printProductionDTO;

    private ProProductionStagesDTOConverter proProductionStagesDTOConverter;
    private ProProductionTransactionDTOConverter proProductionTransactionDTOConverter;
    private InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter;

    @EJB
    private ProductionReportService productionReportService;
    @EJB
    private ProProductionStagesService proProductionStagesService;
    @EJB
    private ProProductionTransactionService proProductionTransactionService;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;

    public ProductionReportMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        proProductionStagesDTOList = new ArrayList<>();
        proProductionTransactionDTOList = new ArrayList<>();
        invPurchaseInvoiceDTOList = new ArrayList<>();
        getProProductionStagesDTOList();
        getInvPurchaseInvoiceDTOList();

    }

    public List<ProProductionStagesDTO> completeProProductionId(String query) {
        return AutoComplete.completeProProductionId(query, proProductionStagesDTOList, proProductionStagesDTOConverter);

    }

    public List<InvPurchaseInvoiceDTO1> completeInvpurchaseInvoice(String query) {

        return AutoComplete.completInvPurchaseInvoice(query, invPurchaseInvoiceDTOList, invPurchaseInvoiceDTOConverter);
    }

    public void search() {
        if ((printProductionDTO.getFromInvoice() != null && printProductionDTO.getToInvoice() != null)
                || (printProductionDTO.getFromDate() != null && printProductionDTO.getToDate() != null)
                || (printProductionDTO.getFromStatus() != null) && (printProductionDTO.getToStatus() != null)) {
            productionReports = productionReportService.searchReport(printProductionDTO, getUserData().getUser());
        } else {
            printProductionDTO.setMsg("ادخل من فاتوره  الي فاتوره او من تاريخ الي تاريخ او من مرحله الي مرحله  ");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProductionDTO.getMsg()));

        }
    }

    public void convertDateFrom() {
        getPrintProductionDTO().setFromDate(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getPrintProductionDTO().setToDate(findDateFromHijriTo());
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
        if (productionReports != null && !productionReports.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "productionUserReport.jasper", productionReports, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("fromInvoice", printProductionDTO.getFromInvoice().getId());
        hashMap.put("toInvoice", printProductionDTO.getToInvoice().getId());
        hashMap.put("fromStatus", printProductionDTO.getFromStatus());
        hashMap.put("toStatus", printProductionDTO.getToStatus());
        hashMap.put("toDate", printProductionDTO.getToDate());
        hashMap.put("fromDate", printProductionDTO.getFromDate());
        hashMap.put("toStatusText", "الى  مرحلة");
        hashMap.put("fromStatusText", "من مرحلة");
        hashMap.put("fromInvoiceText", "من فاتورة");
        hashMap.put("toInvoiceText", "إلى فاتورة");
        hashMap.put("toDateText", "من تاريخ");
        hashMap.put("fromDateText", "إلى تاريخ");
        hashMap.put("empNameText", "اسم الموظف");
        hashMap.put("invoiceIdText", "رقم الفاتورة");
        hashMap.put("productionStageText", "رقم المرحلة");
        hashMap.put("dateText", "التاريخ");
        hashMap.put("priceText", "السعر");
        hashMap.put("quantityText", "الكمية");
        hashMap.put("totalText", "الاجمالى");
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
    }

    @Override
    public void filter() {
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
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<ProductionReport> getProductionReports() {
        return productionReports;
    }

    public void setProductionReports(List<ProductionReport> productionReports) {
        this.productionReports = productionReports;
    }

    public PrintProductionDTO getPrintProductionDTO() {
        if (printProductionDTO == null) {
            printProductionDTO = new PrintProductionDTO();
        }
        return printProductionDTO;
    }

    public void setPrintProductionDTO(PrintProductionDTO printProductionDTO) {
        this.printProductionDTO = printProductionDTO;
    }

    public List<ProProductionStagesDTO> getProProductionStagesDTOList() {
        if (proProductionStagesDTOList == null | proProductionStagesDTOList.isEmpty()) {
            proProductionStagesDTOList = proProductionStagesService.getAllProproductionStages(getUserData().getUser());
            setProProductionStagesDTOConverter(new ProProductionStagesDTOConverter(proProductionStagesDTOList));
        }
        return proProductionStagesDTOList;
    }

    public void setProProductionStagesDTOList(List<ProProductionStagesDTO> proProductionStagesDTOList) {
        this.proProductionStagesDTOList = proProductionStagesDTOList;
    }

    public List<ProProductionTransactionDTO> getProProductionTransactionDTOList() {
        if (proProductionTransactionDTOList == null | proProductionTransactionDTOList.isEmpty()) {
            proProductionTransactionDTOList = proProductionTransactionService.getAll(getUserData().getUser());
            setProProductionTransactionDTOConverter(new ProProductionTransactionDTOConverter(proProductionTransactionDTOList));
        }
        return proProductionTransactionDTOList;
    }

    public void setProProductionTransactionDTOList(List<ProProductionTransactionDTO> proProductionTransactionDTOList) {
        this.proProductionTransactionDTOList = proProductionTransactionDTOList;
    }

    public ProProductionStagesDTOConverter getProProductionStagesDTOConverter() {
        return proProductionStagesDTOConverter;
    }

    public void setProProductionStagesDTOConverter(ProProductionStagesDTOConverter proProductionStagesDTOConverter) {
        this.proProductionStagesDTOConverter = proProductionStagesDTOConverter;
    }

    public ProProductionTransactionDTOConverter getProProductionTransactionDTOConverter() {
        return proProductionTransactionDTOConverter;
    }

    public void setProProductionTransactionDTOConverter(ProProductionTransactionDTOConverter proProductionTransactionDTOConverter) {
        this.proProductionTransactionDTOConverter = proProductionTransactionDTOConverter;
    }

    /**
     * @return the invPurchaseInvoiceDTOList
     */
    public List<InvPurchaseInvoiceDTO1> getInvPurchaseInvoiceDTOList() {
        if (invPurchaseInvoiceDTOList == null || invPurchaseInvoiceDTOList.isEmpty()) {
            invPurchaseInvoiceDTOList = invPurchaseInvoiceService.findInvPurchaseInvoiceDTOList(getUserData().getUser());
//            setInvPurchaseInvoiceDTOConverter(new InvPurchaseInvoiceDTOConverter(invPurchaseInvoiceDTOList));
        }
        return invPurchaseInvoiceDTOList;
    }

    /**
     * @param invPurchaseInvoiceDTOList the invPurchaseInvoiceDTOList to set
     */
    public void setInvPurchaseInvoiceDTOList(List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOList) {
        this.invPurchaseInvoiceDTOList = invPurchaseInvoiceDTOList;
    }

    /**
     * @return the invPurchaseInvoiceDTOConverter
     */
    public InvPurchaseInvoiceDTOConverter getInvPurchaseInvoiceDTOConverter() {
        return invPurchaseInvoiceDTOConverter;
    }

    /**
     * @param invPurchaseInvoiceDTOConverter the invPurchaseInvoiceDTOConverter
     * to set
     */
    public void setInvPurchaseInvoiceDTOConverter(InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter) {
        this.invPurchaseInvoiceDTOConverter = invPurchaseInvoiceDTOConverter;
    }

}
