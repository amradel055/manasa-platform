package com.toby.invoiceMovementReport;

import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.InvoiceMovementReportService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.businessservice.ProProductionTransactionService;
import com.toby.businessservice.ProductionReportService;
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.converter.ProProductionTransactionDTOConverter;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import com.toby.views.InvoiceMovementReport;
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

@Named(value = "invoiceMovementReportMB")
@ViewScoped
public class InvoiceMovementReportMB extends BaseListBean {

    private List<ProProductionStagesDTO> proProductionStagesDTOList;
    private List<ProProductionTransactionDTO> proProductionTransactionDTOList;
    private List<ProductionReport> productionReports;
    private List<InvoiceMovementReport> invoiceMovementReportList;
    private PrintProductionDTO printProductionDTO;

    private ProProductionStagesDTOConverter proProductionStagesDTOConverter;
    private ProProductionTransactionDTOConverter proProductionTransactionDTOConverter;

    @EJB
    private ProductionReportService productionReportService;
    @EJB
    private ProProductionStagesService proProductionStagesService;
    @EJB
    private ProProductionTransactionService proProductionTransactionService;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvoiceMovementReportService invoiceMovementReportService;

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        proProductionStagesDTOList = new ArrayList<>();
        proProductionTransactionDTOList = new ArrayList<>();
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddFromInvInvoice")) {
            InvInvoiceDTO invInvoiceDTO = (InvInvoiceDTO) context.getSessionMap().get("invInvoiceDTOSelected");
            getPrintProductionDTO().setFromInvoice(invInvoiceDTO);
            getPrintProductionDTO().setToInvoice(invInvoiceDTO);
            search();
        }
    }

    public void search() {

        // if ((printProductionDTO.getFromInvoice() != null && printProductionDTO.getToInvoice() != null)) {
        invoiceMovementReportList = invoiceMovementReportService.searchReport(printProductionDTO, getUserData().getUserBranch().getId());
        // } else {
//            printProductionDTO.setMsg("ادخل من فاتوره الي فاتوره");
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProductionDTO.getMsg()));
//
//        }
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (invoiceMovementReportList != null && !invoiceMovementReportList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "instProjectGuide.jasper", invoiceMovementReportList, "pdf");
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
     * @return the invoiceMovementReportList
     */
    public List<InvoiceMovementReport> getInvoiceMovementReportList() {
        if (invoiceMovementReportList == null || invoiceMovementReportList.isEmpty()) {
            invoiceMovementReportList = new ArrayList<>();
        }
        return invoiceMovementReportList;
    }

    /**
     * @param invoiceMovementReportList the invoiceMovementReportList to set
     */
    public void setInvoiceMovementReportList(List<InvoiceMovementReport> invoiceMovementReportList) {
        this.invoiceMovementReportList = invoiceMovementReportList;
    }

}
