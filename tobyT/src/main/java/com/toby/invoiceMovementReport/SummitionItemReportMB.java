package com.toby.invoiceMovementReport;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.toby.Basic;
import com.toby.toby.UserData;
import com.toby.views.SummitionItemsView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private BigDecimal sumDifferentTotal;
    private BigDecimal sumDeliveryNumberTotal;
    private BigDecimal sumCustomerNumberTotal;
    private BigDecimal sumNumberTotal;
    private BigDecimal sumAllCostTotal;
    private BigDecimal sumAllAvarageCost;

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

    @PostConstruct
    public void init() {
        gallaryListSelected = new ArrayList<>();
        getGallaryListSelected().addAll(getGallaryList());
    }

    public void search() {
//        List<SummitionItemsView> itemsViewForGallary = null;
        if (getGallaryListSelected() != null && !getGallaryListSelected().isEmpty()) {
            itemsViewList = new ArrayList<>();
            sumFactoryNumberTotal = BigDecimal.ZERO;
            sumSendNumberTotal = BigDecimal.ZERO;
            sumDeliveryNumberTotal = BigDecimal.ZERO;
            sumCustomerNumberTotal = BigDecimal.ZERO;
            sumDifferentTotal = BigDecimal.ZERO;
            setSumNumberTotal(BigDecimal.ZERO);
            setSumAllCostTotal(BigDecimal.ZERO);
            setSumAllAvarageCost(BigDecimal.ZERO);
            List<SummitionItemsView> itemsViewForGallaryinner = new ArrayList<>();
            List<SummitionItemsView> itemsViewForGallaryinnerForAll = new ArrayList<>();
            SummitionItemsView summitionItemsView = null;
            for (InvInventoryDTO invInventoryDTO : getGallaryListSelected()) {

                summitionItemsView = new SummitionItemsView();
                BigDecimal sumAverageCost = BigDecimal.ZERO, sumNumber = BigDecimal.ZERO, sumAllCost = BigDecimal.ZERO, sumFactoryNumber = BigDecimal.ZERO, sumSendNumber = BigDecimal.ZERO, sumDeliveryNumber = BigDecimal.ZERO, sumCustomerNumber = BigDecimal.ZERO, sumDifferentNumber = BigDecimal.ZERO;;

                summitionItemsView.setName(invInventoryDTO.getName());
                summitionItemsView.setRowType(1);

                if (getUri().contains("summitionItemReportPro")) {
                    itemsViewForGallaryinner = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(invInventoryDTO.getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId(), getUserData().getUserBranch().getId(), getUserData().getUser());
                } else {
                    itemsViewForGallaryinner = invPurchaseInvoiceService_1.findSummitionQuantity(invInventoryDTO.getId(), getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId(), null);
                    itemsViewForGallaryinnerForAll = invPurchaseInvoiceService_1.findSummitionQuantity(invInventoryDTO.getId(), getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId(), null);
                }
                if (itemsViewForGallaryinner != null && !itemsViewForGallaryinner.isEmpty()) {
                    for (SummitionItemsView view : itemsViewForGallaryinner) {
                        if (view.getCode() == 1 || view.getCode() == 2) {
                            setValueForGallary(summitionItemsView, view);
                        }
                    }
                    getItemsViewList().add(summitionItemsView);
                    getItemsViewList().addAll(itemsViewForGallaryinner);
                    setFinalTotals(sumFactoryNumber, sumSendNumber, sumDeliveryNumber, sumCustomerNumber, sumDifferentNumber, sumNumber, sumAllCost, sumAverageCost);
                }

            }

            if (getUri().contains("summitionItemReportPro")) {
            } else {
                itemsViewForGallaryinnerForAll = invPurchaseInvoiceService_1.findSummitionQuantity(null, getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId(), getGallaryListSelected());
                if (itemsViewForGallaryinnerForAll != null && !itemsViewForGallaryinnerForAll.isEmpty()) {
                    summitionItemsView = new SummitionItemsView();
                    summitionItemsView.setName("إجماليات المعارض");
                    summitionItemsView.setRowType(1);
                    for (SummitionItemsView view : itemsViewForGallaryinnerForAll) {
                        if (view.getCode() == 1 || view.getCode() == 2) {
                            setValueForGallary(summitionItemsView, view);
                        }
                    }
                    getItemsViewList().add(summitionItemsView);
                    getItemsViewList().addAll(itemsViewForGallaryinner);
                }
            }

        }

    }

    private void setValueForGallary(SummitionItemsView summitionItemsView, SummitionItemsView view) {
        summitionItemsView.setFactoryNumber(summitionItemsView.getFactoryNumber().add(view.getFactoryNumber()));
        summitionItemsView.setSendNumber(summitionItemsView.getSendNumber().add(view.getSendNumber()));
        summitionItemsView.setDeliveryNumber(summitionItemsView.getDeliveryNumber().add(view.getDeliveryNumber()));
        summitionItemsView.setCustomerNumber(summitionItemsView.getCustomerNumber().add(view.getCustomerNumber()));
        summitionItemsView.setDifferent(summitionItemsView.getDifferent().add(view.getDifferent()));
        summitionItemsView.setAllCost(summitionItemsView.getAllCost().add(view.getAllCost()));
        summitionItemsView.setNumber(summitionItemsView.getNumber().add(view.getNumber()));
        summitionItemsView.setAllAvarageCost(summitionItemsView.getAllAvarageCost().add(view.getAllAvarageCost()));
    }

    private void setFinalTotals(BigDecimal sumFactoryNumber, BigDecimal sumSendNumber, BigDecimal sumDeliveryNumber, BigDecimal sumCustomerNumber, BigDecimal sumDifferentNumber, BigDecimal sumNumber, BigDecimal sumAllCost, BigDecimal sumAverageCost) {
        setSumFactoryNumberTotal(getSumFactoryNumberTotal().add(sumFactoryNumber));
        setSumSendNumberTotal(getSumSendNumberTotal().add(sumSendNumber));
        setSumDeliveryNumberTotal(getSumDeliveryNumberTotal().add(sumDeliveryNumber));
        setSumCustomerNumberTotal(getSumCustomerNumberTotal().add(sumCustomerNumber));
        setSumDifferentTotal(getSumDifferentTotal().add(sumDifferentNumber));
        setSumNumberTotal(getSumNumberTotal().add(sumNumber));
        setSumAllCostTotal(getSumAllCostTotal().add(sumAllCost));
        setSumAllAvarageCost(getSumAllAvarageCost().add(sumAverageCost));
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
        hashMap.put("allCostText", "سعر البيع ");
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

        if (gallaryListSelected == null) {

            gallaryListSelected = new ArrayList<>();
        }

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

    /**
     * @return the sumDifferentTotal
     */
    public BigDecimal getSumDifferentTotal() {
        if (sumDifferentTotal == null) {
            return BigDecimal.ZERO;
        }
        return sumDifferentTotal;
    }

    /**
     * @param sumDifferentTotal the sumDifferentTotal to set
     */
    public void setSumDifferentTotal(BigDecimal sumDifferentTotal) {
        this.sumDifferentTotal = sumDifferentTotal;
    }

    /**
     * @return the sumNumberTotal
     */
    public BigDecimal getSumNumberTotal() {
        return sumNumberTotal;
    }

    /**
     * @param sumNumberTotal the sumNumberTotal to set
     */
    public void setSumNumberTotal(BigDecimal sumNumberTotal) {
        this.sumNumberTotal = sumNumberTotal;
    }

    /**
     * @return the sumAllCostTotal
     */
    public BigDecimal getSumAllCostTotal() {
        return sumAllCostTotal;
    }

    /**
     * @param sumAllCostTotal the sumAllCostTotal to set
     */
    public void setSumAllCostTotal(BigDecimal sumAllCostTotal) {
        this.sumAllCostTotal = sumAllCostTotal;
    }

    /**
     * @return the sumAllAvarageCost
     */
    public BigDecimal getSumAllAvarageCost() {
        return sumAllAvarageCost;
    }

    /**
     * @param sumAllAvarageCost the sumAllAvarageCost to set
     */
    public void setSumAllAvarageCost(BigDecimal sumAllAvarageCost) {
        this.sumAllAvarageCost = sumAllAvarageCost;
    }

}
