package com.toby.reports;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.ProFactoryDeliveryService;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.toby.Basic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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

@Named(value = "proInvoiceStatusReportMB")
@ViewScoped
public class ProInvoiceStatusReportMB extends Basic {

    private PrintProductionDTO printProductionDTO;

    private List<InvInvoiceDTO> list;

    private Integer number;

    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private ProFactoryDeliveryService proFactoryDeliveryService;

    @PostConstruct
    public void init() {
        getPrintProductionDTO().getGallaryListSelected().addAll(getGallaryLists());
        getPrintProductionDTO().setFromDate(new Date());
        getPrintProductionDTO().setToDate(new Date());

    }

    public boolean validateSearch() {

        if (getPrintProductionDTO().getGallaryListSelected().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار المعرض "));
        } else if (getPrintProductionDTO().getInvoiceStatus() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار الحالة "));
        } else if (getPrintProductionDTO().getFromDate() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار التاريخ من  "));
        } else if (getPrintProductionDTO().getToDate() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار التاريخ الي  "));
        } else {
            return true;
        }
        return false;
    }

    public void search() {
        if (validateSearch() == true) {
            if (getPrintProductionDTO() != null && getPrintProductionDTO().getGallaryListSelected() != null && !getPrintProductionDTO().getGallaryListSelected().isEmpty()) {
                list = new ArrayList<>();
                for (InvInventoryDTO invInventoryDTO : getPrintProductionDTO().getGallaryListSelected()) {

                    getPrintProductionDTO().setGallaryFrom(invInventoryDTO);
                    if (printProductionDTO.getInvoiceStatus() == 4) {
                        getList().addAll(invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByStatus(getPrintProductionDTO(), getUserData().getUserBranch().getId()));
                    } else if (printProductionDTO.getInvoiceStatus() == 10) {
                        getList().addAll(proFactoryDeliveryService.getALLProFactoryDeliveryDTOByStatusRemain(getPrintProductionDTO(), getUserData().getUserBranch().getId()));
                    } else {
                        getList().addAll(proFactoryDeliveryService.getALLProFactoryDeliveryDTOByStatusRemain(getPrintProductionDTO(), getUserData().getUserBranch().getId()));
                    }

                }
                setNumber(getList() == null ? 0 : getList().size());
            }
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
//        search();
        String reportName = "invoiceStatus.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getList(), "pdf");
    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("dateValue", printProductionDTO.getFromDate());
        hashMap.put("invoiceSerialText", "رقم الفاتورة");
        hashMap.put("customerCodeText", "رقم العميل");
        hashMap.put("customerNameText", "اسم العميل");
        hashMap.put("customerMobileText", "جوال العميل");
        hashMap.put("dueDateText", "تاريخ التسليم");
        hashMap.put("numberOfToobText", " عدد التوب");
        hashMap.put("gallaryFromText", "من معرض");
        hashMap.put("gallaryToText", "الى معرض");
        hashMap.put("gallaryFromValue", printProductionDTO.getGallaryFrom() == null ? null : printProductionDTO.getGallaryFrom().getName());
//        hashMap.put("gallaryToValue", printProductionDTO.getGallaryTo().getName());
        hashMap.put("statusText", "الحالة");

        if (printProductionDTO.getInvoiceStatus() == 0) {
            hashMap.put("statusValue", "بالمشغل");
        } else if (printProductionDTO.getInvoiceStatus() == 1) {
            hashMap.put("statusValue", "ارسال للمعرض");
        } else if (printProductionDTO.getInvoiceStatus() == 2) {
            hashMap.put("statusValue", "استلام بالمعرض");
        } else if (printProductionDTO.getInvoiceStatus() == 3) {
            hashMap.put("statusValue", "تسليم للعميل");
        } else if (printProductionDTO.getInvoiceStatus() == 10) {
            hashMap.put("statusValue", "باقى الفواتير بالمشغل");
        }

        hashMap.put("dateText", "إلى تاريخ");
        hashMap.put("dateValue", printProductionDTO.getToDate());

        hashMap.put("gallaryNameText", "المعرض");

        return hashMap;
    }

    /**
     * @return the printProductionDTO
     */
    public PrintProductionDTO getPrintProductionDTO() {
        if (printProductionDTO == null) {
            printProductionDTO = new PrintProductionDTO();
        }
        return printProductionDTO;
    }

    /**
     * @param printProductionDTO the printProductionDTO to set
     */
    public void setPrintProductionDTO(PrintProductionDTO printProductionDTO) {
        this.printProductionDTO = printProductionDTO;
    }

    /**
     * @return the list
     */
    public List<InvInvoiceDTO> getList() {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<InvInvoiceDTO> list) {
        this.list = list;
    }

    /**
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }
}
