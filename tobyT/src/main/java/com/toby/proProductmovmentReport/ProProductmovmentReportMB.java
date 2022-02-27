/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.proProductmovmentReport;

import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.ProProductionMovementDetailService;
import com.toby.businessservice.ProProductionMovementReportService;
import com.toby.converter.InvPurchaseInvoiceDTOConverter;
import com.toby.converter.ProProductMovementDetailDTOConvertor;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.PrintProProductDTO;
import com.toby.dto.ProProductMovementDetailDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import com.toby.views.ProProductionMovementReport;
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
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 *
 * @author H
 */
@Named(value = "proProductmovmentReportMB")
@ViewScoped
public class ProProductmovmentReportMB extends BaseFormBean {

    private List<ProProductMovementDetailDTO> ProProductMovementDetailDTOList;
    private ProProductMovementDetailDTOConvertor proProductMovementDetailDTOConvertor;
    private PrintProProductDTO printProProductDTO;
    private List<ProProductionMovementReport> proProductionMovementReportList;
    private List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOList;
    private InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private ProProductionMovementReportService proProductionMovementReportService;
    @EJB
    private ProProductionMovementDetailService proProductionMovementDetailService;
    @EJB
    private OrganizationSiteService organizationSiteService;

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @PostConstruct
    @Override
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        getProProductMovementDetailDTOList();
        proProductionMovementReportList = new ArrayList<>();
        ProProductMovementDetailDTOList = new ArrayList<>();
        getInvPurchaseInvoiceDTOList();
    }

    public List<InvPurchaseInvoiceDTO1> completeInvpurchaseInvoice(String query) {

        return AutoComplete.completeInvpurchaseInvoice(query, invPurchaseInvoiceDTOList, invPurchaseInvoiceDTOConverter);
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void search() {
        if ((printProProductDTO.getFromInvoice() != null && printProProductDTO.getToInvoice() != null)
                || (printProProductDTO.getFromDate() != null && printProProductDTO.getToDate() != null)) {

            proProductionMovementReportList = proProductionMovementReportService.getProProductionMovementReportList(printProProductDTO, getUserData().getUser());
        } else {

            printProProductDTO.setMsg("ادخل من فاتوره الي فاتوره او من تاريخ الي تاريخ ");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProProductDTO.getMsg()));

        }
    }

    public void convertDateFrom() {
        getPrintProProductDTO().setFromDate(findDateFromHijriFrom());
    }

    public void convertDateTo() {
       getPrintProProductDTO().setToDate(findDateFromHijriTo());
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
        if (proProductionMovementReportList != null && !proProductionMovementReportList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "productionUserReport.jasper", proProductionMovementReportList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("fromInvoice", printProProductDTO.getFromInvoice().getSerial());
        hashMap.put("toInvoice", printProProductDTO.getToInvoice().getSerial());
        hashMap.put("toDate", printProProductDTO.getToDate());
        hashMap.put("fromDate", printProProductDTO.getFromDate());
        hashMap.put("driverNameText", "اسم السائق");
        hashMap.put("dateText", "التاريخ");
        hashMap.put("SrialText", "رقم الفاتورة");
        hashMap.put("fromInvoiceText", "من فاتورة");
        hashMap.put("toInvoiceText", "إلى فاتورة");
        hashMap.put("toDateText", "من تاريخ");
        hashMap.put("fromDateText", "إلى تاريخ");
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
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the ProProductMovementDetailDTOList
     */
    public List<ProProductMovementDetailDTO> getProProductMovementDetailDTOList() {
        if (ProProductMovementDetailDTOList == null || ProProductMovementDetailDTOList.isEmpty()) {
            ProProductMovementDetailDTOList = proProductionMovementDetailService.getinpurchaseinvoice(getUserData().getUser());
            setProProductMovementDetailDTOConvertor(new ProProductMovementDetailDTOConvertor(ProProductMovementDetailDTOList));

        }
        return ProProductMovementDetailDTOList;
    }

    /**
     * @param ProProductMovementDetailDTOList the
     * ProProductMovementDetailDTOList to set
     */
    public void setProProductMovementDetailDTOList(List<ProProductMovementDetailDTO> ProProductMovementDetailDTOList) {
        this.ProProductMovementDetailDTOList = ProProductMovementDetailDTOList;
    }

    /**
     * @return the proProductMovementDetailDTOConvertor
     */
    public ProProductMovementDetailDTOConvertor getProProductMovementDetailDTOConvertor() {
        return proProductMovementDetailDTOConvertor;
    }

    /**
     * @param proProductMovementDetailDTOConvertor the
     * proProductMovementDetailDTOConvertor to set
     */
    public void setProProductMovementDetailDTOConvertor(ProProductMovementDetailDTOConvertor proProductMovementDetailDTOConvertor) {
        this.proProductMovementDetailDTOConvertor = proProductMovementDetailDTOConvertor;
    }

    /**
     * @return the printProProductDTO
     */
    public PrintProProductDTO getPrintProProductDTO() {
        if (printProProductDTO == null) {
            printProProductDTO = new PrintProProductDTO();
        }
        return printProProductDTO;
    }

    /**
     * @param printProProductDTO the printProProductDTO to set
     */
    public void setPrintProProductDTO(PrintProProductDTO printProProductDTO) {
        this.printProProductDTO = printProProductDTO;
    }

    /**
     * @return the proProductionMovementReportList
     */
    public List<ProProductionMovementReport> getProProductionMovementReportList() {
        return proProductionMovementReportList;
    }

    /**
     * @param proProductionMovementReportList the
     * proProductionMovementReportList to set
     */
    public void setProProductionMovementReportList(List<ProProductionMovementReport> proProductionMovementReportList) {
        this.proProductionMovementReportList = proProductionMovementReportList;
    }

    /**
     * @return the invPurchaseInvoiceDTOList
     */
    public List<InvPurchaseInvoiceDTO1> getInvPurchaseInvoiceDTOList() {
        if (invPurchaseInvoiceDTOList == null || invPurchaseInvoiceDTOList.isEmpty()) {
            invPurchaseInvoiceDTOList = invPurchaseInvoiceService.findInvPurchaseInvoiceDTOList(getUserData().getUser());
//          setInvPurchaseInvoiceDTOConverter(new InvPurchaseInvoiceDTOConverter(invPurchaseInvoiceDTOList));
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
