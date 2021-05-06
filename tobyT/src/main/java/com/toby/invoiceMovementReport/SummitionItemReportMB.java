package com.toby.invoiceMovementReport;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.toby.Basic;
import com.toby.views.SummitionItemsView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

@Named(value = "summitionItemReportMB")
@ViewScoped
public class SummitionItemReportMB extends Basic {

    private InvInventoryDTO gallaryDTOSelected;
    private Date dateFrom;
    private Date dateTo;
    private ProGroupDTO proGroupDTOSelected;
    private List<InvInventoryDTO> gallaryList;

    private List<SummitionItemsView> itemsViewList;

    private List<InvInventoryDTO> gallaryListSelected;

    private InvInventoryDTOConverter gallaryDTOConverter;

    private BigDecimal sumFactoryNumberTotal;
    private BigDecimal sumSendNumberTotal;
    private BigDecimal sumDeliveryNumberTotal;
    private BigDecimal sumCustomerNumberTotal;

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

    public void search() {
//        List<SummitionItemsView> itemsViewForGallary = null;
        if (getGallaryListSelected() != null && !getGallaryListSelected().isEmpty()) {

            for (InvInventoryDTO invInventoryDTO : getGallaryListSelected()) {
                List<SummitionItemsView> itemsViewForGallaryinner = new ArrayList<>();
                SummitionItemsView summitionItemsView = new SummitionItemsView();
                BigDecimal sumFactoryNumber = BigDecimal.ZERO, sumSendNumber = BigDecimal.ZERO, sumDeliveryNumber = BigDecimal.ZERO, sumCustomerNumber = BigDecimal.ZERO;

                summitionItemsView.setName(invInventoryDTO.getName());
                summitionItemsView.setRowType(1);

                if (getUri().contains("summitionItemReportPro")) {
                    itemsViewForGallaryinner = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(invInventoryDTO.getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId(), getUserData().getUserBranch().getId(), getUserData().getUser());
                } else {
                    itemsViewForGallaryinner = invPurchaseInvoiceService_1.findSummitionQuantity(invInventoryDTO.getId(), getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId());
                }

                if (itemsViewForGallaryinner != null && !itemsViewForGallaryinner.isEmpty()) {
                    for (SummitionItemsView view : itemsViewForGallaryinner) {
                        if (view.getCode() == 1 || view.getCode() == 2) {
                            sumFactoryNumber = sumFactoryNumber.add(view.getFactoryNumber());
                            sumSendNumber = sumSendNumber.add(view.getSendNumber());
                            sumDeliveryNumber = sumDeliveryNumber.add(view.getDeliveryNumber());
                            sumCustomerNumber = sumCustomerNumber.add(view.getCustomerNumber());
                        }
                    }

                    summitionItemsView.setFactoryNumber(sumFactoryNumber);
                    summitionItemsView.setSendNumber(sumSendNumber);
                    summitionItemsView.setDeliveryNumber(sumDeliveryNumber);
                    summitionItemsView.setCustomerNumber(sumCustomerNumber);

                    getItemsViewList().add(summitionItemsView);
                    getItemsViewList().addAll(itemsViewForGallaryinner);

                    setSumFactoryNumberTotal(getSumFactoryNumberTotal().add(sumFactoryNumber));
                    setSumSendNumberTotal(getSumSendNumberTotal().add(sumSendNumber));
                    setSumDeliveryNumberTotal(getSumDeliveryNumberTotal().add(sumDeliveryNumber));
                    setSumCustomerNumberTotal(getSumCustomerNumberTotal().add(sumCustomerNumber));
                }

            }

        }

    }

    public void convertDateFrom() {
        setDateFrom(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        setDateTo(findDateFromHijriTo());
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
        if (itemsViewList != null && !itemsViewList.isEmpty()) {
            if (getUri().contains("summitionItemReportPro")) {
                fillReport(prepareReport(), getUserData().getReportPath() + "summitionItemReportPro.jasper", itemsViewList, "pdf");
            } else {
                fillReport(prepareReport(), getUserData().getReportPath() + "summitionItem.jasper", itemsViewList, "pdf");
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }

    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();

        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("dateFromText", "  من تاريخ");
        hashMap.put("dateFromValue", getDateFrom());
        hashMap.put("dateToText", "  الى تاريخ");
        hashMap.put("dateToValue", getDateTo());
        hashMap.put("gallaryText", "المعرض ");
        hashMap.put("gallaryValue", getGallaryDTOSelected() == null ? null : getGallaryDTOSelected().getName());
        hashMap.put("proGroupText", "الفئة ");
        hashMap.put("proGroupValue", getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getName());
        hashMap.put("codeText", "الكود ");
        hashMap.put("nameText", "الفئة ");
        hashMap.put("numberText", "العدد ");
        hashMap.put("allCostText", "التكلفة ");
        hashMap.put("factoryNumberText", "استلام بالمشغل ");
        hashMap.put("sendNumberText", "ارسال للمعرض ");
        hashMap.put("differentText", "الفرق ");
        hashMap.put("deliveryNumberText", "المستلم بالمعرض ");

        return hashMap;
    }

    public List<InvInventoryDTO> getGallaryList() {
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
     * @return the gallaryDTOSelected
     */
    public InvInventoryDTO getGallaryDTOSelected() {
        return gallaryDTOSelected;
    }

    /**
     * @param gallaryDTOSelected the gallaryDTOSelected to set
     */
    public void setGallaryDTOSelected(InvInventoryDTO gallaryDTOSelected) {
        this.gallaryDTOSelected = gallaryDTOSelected;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the proGroupDTOSelected
     */
    public ProGroupDTO getProGroupDTOSelected() {
        return proGroupDTOSelected;
    }

    /**
     * @param proGroupDTOSelected the proGroupDTOSelected to set
     */
    public void setProGroupDTOSelected(ProGroupDTO proGroupDTOSelected) {
        this.proGroupDTOSelected = proGroupDTOSelected;
    }

    /**
     * @return the itemsViewList
     */
    public List<SummitionItemsView> getItemsViewList() {
        if (itemsViewList == null) {
            itemsViewList = new ArrayList<>();
        }
        return itemsViewList;
    }

    /**
     * @param itemsViewList the itemsViewList to set
     */
    public void setItemsViewList(List<SummitionItemsView> itemsViewList) {
        this.itemsViewList = itemsViewList;
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
     * @return the gallaryListSelected
     */
    public List<InvInventoryDTO> getGallaryListSelected() {

        return gallaryListSelected;
    }

    /**
     * @param gallaryListSelected the gallaryListSelected to set
     */
    public void setGallaryListSelected(List<InvInventoryDTO> gallaryListSelected) {
        this.gallaryListSelected = gallaryListSelected;
    }

    /**
     * @return the sumFactoryNumberTotal
     */
    public BigDecimal getSumFactoryNumberTotal() {
        if (sumFactoryNumberTotal == null) {
            sumFactoryNumberTotal = BigDecimal.ZERO;
        }
        return sumFactoryNumberTotal;
    }

    /**
     * @param sumFactoryNumberTotal the sumFactoryNumberTotal to set
     */
    public void setSumFactoryNumberTotal(BigDecimal sumFactoryNumberTotal) {
        this.sumFactoryNumberTotal = sumFactoryNumberTotal;
    }

    /**
     * @return the sumSendNumberTotal
     */
    public BigDecimal getSumSendNumberTotal() {
        if (sumSendNumberTotal == null) {
            sumSendNumberTotal = BigDecimal.ZERO;
        }
        return sumSendNumberTotal;
    }

    /**
     * @param sumSendNumberTotal the sumSendNumberTotal to set
     */
    public void setSumSendNumberTotal(BigDecimal sumSendNumberTotal) {
        this.sumSendNumberTotal = sumSendNumberTotal;
    }

    /**
     * @return the sumDeliveryNumberTotal
     */
    public BigDecimal getSumDeliveryNumberTotal() {
        if (sumDeliveryNumberTotal == null) {
            sumDeliveryNumberTotal = BigDecimal.ZERO;
        }
        return sumDeliveryNumberTotal;
    }

    /**
     * @param sumDeliveryNumberTotal the sumDeliveryNumberTotal to set
     */
    public void setSumDeliveryNumberTotal(BigDecimal sumDeliveryNumberTotal) {
        this.sumDeliveryNumberTotal = sumDeliveryNumberTotal;
    }

    /**
     * @return the sumCustomerNumberTotal
     */
    public BigDecimal getSumCustomerNumberTotal() {
        if (sumCustomerNumberTotal == null) {
            sumCustomerNumberTotal = BigDecimal.ZERO;
        }
        return sumCustomerNumberTotal;
    }

    /**
     * @param sumCustomerNumberTotal the sumCustomerNumberTotal to set
     */
    public void setSumCustomerNumberTotal(BigDecimal sumCustomerNumberTotal) {
        this.sumCustomerNumberTotal = sumCustomerNumberTotal;
    }

}
