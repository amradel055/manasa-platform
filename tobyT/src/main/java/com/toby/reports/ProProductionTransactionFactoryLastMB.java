package com.toby.reports;

import com.toby.businessservice.ProProductionItemsTransactionService;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProProductionItemsTransactionDTO;
import com.toby.toby.Basic;
import com.toby.views.ProProductionTransactionFactoryView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
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

@Named(value = "proProductionTransactionFactoryLastMB")
@ViewScoped
public class ProProductionTransactionFactoryLastMB extends Basic {

    private List<ProProductionItemsTransactionDTO> proProductionItemsTransactionDTOList;
    private ProProductionTransactionFactoryView proProductionTransactionFactoryView;

    @EJB
    private ProProductionItemsTransactionService proProductionItemsTransactionService;

    @PostConstruct
    public void init() {
        getProProductionTransactionFactoryView().getGallerySelectedList().addAll(getGallaryLists());
    }

    public void search() {
        if (getProProductionTransactionFactoryView() != null && getProProductionTransactionFactoryView().getGallerySelectedList() != null && !getProProductionTransactionFactoryView().getGallerySelectedList().isEmpty()) {
            proProductionItemsTransactionDTOList = new ArrayList<>();
            for (InvInventoryDTO invInventoryDTO : getProProductionTransactionFactoryView().getGallerySelectedList()) {
                getProProductionTransactionFactoryView().setGallerySelected(invInventoryDTO);
                getProProductionItemsTransactionDTOList().addAll(proProductionItemsTransactionService.getALLProProductionItemsTransactionLastDTO(getProProductionTransactionFactoryView(), getUserData().getUserBranch().getId()));
            }
        }
    }

    public void convertDateFrom() {
        getProProductionTransactionFactoryView().setDate(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getProProductionTransactionFactoryView().setDateTo(findDateFromHijriTo());
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
        String reportName = "ProProductionTransactionFactoryLast.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getProProductionItemsTransactionDTOList(), "pdf");
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

        hashMap.put("dateText", "من تاريخ");
        hashMap.put("dateValue", proProductionTransactionFactoryView.getDate());
        hashMap.put("dateToText", " الى تاريخ");
        hashMap.put("dateToValue", proProductionTransactionFactoryView.getDateTo());
        hashMap.put("gallerySelectedText", "  المعرض");
        hashMap.put("gallerySelectedValue", proProductionTransactionFactoryView.getGallerySelected() == null ? null : proProductionTransactionFactoryView.getGallerySelected().getName());
        hashMap.put("invoiceSerialText", "رقم الفاتورة");
        hashMap.put("galleryNameText", "اسم المعرض");
        hashMap.put("createdByNameText", "اسم العامل ");
        hashMap.put("createdByCodeText", " رقم العامل ");
        hashMap.put("clothNumberText", "  عدد الثياب");
        hashMap.put("stageNameText", "  المرحله");
        hashMap.put("orderDateText", "  تاريخ الشغل");
        hashMap.put("invoiceDateText", "  تاريخ الفاتورة");

        return hashMap;
    }

    public List<ProProductionItemsTransactionDTO> getProProductionItemsTransactionDTOList() {
        if (proProductionItemsTransactionDTOList == null) {
            proProductionItemsTransactionDTOList = new ArrayList<>();
        }
        return proProductionItemsTransactionDTOList;
    }

    /**
     * @param proProductionItemsTransactionDTOList the
     * proProductionItemsTransactionDTOList to set
     */
    public void setProProductionItemsTransactionDTOList(List<ProProductionItemsTransactionDTO> proProductionItemsTransactionDTOList) {
        this.proProductionItemsTransactionDTOList = proProductionItemsTransactionDTOList;
    }

    /**
     * @return the proProductionTransactionFactoryView
     */
    public ProProductionTransactionFactoryView getProProductionTransactionFactoryView() {

        if (proProductionTransactionFactoryView == null) {
            proProductionTransactionFactoryView = new ProProductionTransactionFactoryView();
        }
        return proProductionTransactionFactoryView;
    }

    /**
     * @param proProductionTransactionFactoryView the
     * proProductionTransactionFactoryView to set
     */
    public void setProProductionTransactionFactoryView(ProProductionTransactionFactoryView proProductionTransactionFactoryView) {
        this.proProductionTransactionFactoryView = proProductionTransactionFactoryView;
    }

}
