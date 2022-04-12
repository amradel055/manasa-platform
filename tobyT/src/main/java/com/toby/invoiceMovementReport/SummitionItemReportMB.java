package com.toby.invoiceMovementReport;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.ProGroupService;
import com.toby.converter.ProGroupDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.toby.Basic;
import com.toby.views.SummitionItemsView;
import com.toby.views.Test;
import java.io.IOException;
import java.math.BigDecimal;
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

@Named(value = "summitionItemReportMB")
@ViewScoped
public class SummitionItemReportMB extends Basic {

    private Date dateFrom;
    private Date dateTo;
    private Integer invoiceType;
    private ProGroupDTO proGroupDTOSelected;
    private List<ProGroupDTO> proGroupListSelected;

    private List<ProGroupDTO> groupDTOList;
    private ProGroupDTOConverter proGroupDTOConverter;

    private List<SummitionItemsView> itemsViewList;
    private ArrayList<Test> list;

    private BigDecimal sumFactoryNumberTotal;
    private BigDecimal sumSendNumberTotal;
    private BigDecimal sumDifferentTotal;
    private BigDecimal sumDeliveryNumberTotal;
    private BigDecimal sumCustomerNumberTotal;
    private BigDecimal sumNumberTotal;
    private BigDecimal sumAllCostTotal;
    private BigDecimal sumAllAvarageCost;
    private Map<String, String> userDDs;

    private Boolean summution;

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    ProGroupService proGroupService;

    @PostConstruct
    public void init() {
        setUserDDs(getUserData().getUserDDs());

    }

    public boolean validateSearch() {
        if (getGallaryListSelected().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), "يجب اختيار المعرض "));
        } else if (getProGroupListSelected().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), "يجب اختيار الفئة "));
        } else if (getDateFrom() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), "يجب اختيار التاريخ من  "));
        } else if (getDateTo() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserDDs().get("ERROR"), "يجب اختيار التاريخ الي  "));
        } else {
            return true;
        }
        return false;
    }

    public void search() {
        if (validateSearch() == true) {
//        List<SummitionItemsView> itemsViewForGallary = null;
            if (getGallaryListSelected() != null && !getGallaryListSelected().isEmpty()) {
                if (getProGroupListSelected() != null && !getProGroupListSelected().isEmpty()) {
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
                    HashMap<Integer, Test> map = new HashMap();
                    if (!summution) {
                        for (InvInventoryDTO invInventoryDTO : getGallaryListSelected()) {

                            summitionItemsView = new SummitionItemsView();

                            summitionItemsView.setName(invInventoryDTO.getName());
                            summitionItemsView.setRowType(1);

                            if (getUri().contains("summitionItemReportPro")) {
                                itemsViewForGallaryinner = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(invInventoryDTO.getId(), getDateFrom(), getDateTo(), getUserData().getUserBranch().getId(), getUserData().getUser(), null, getProGroupListSelected());
                            } else {
                                itemsViewForGallaryinner = invPurchaseInvoiceService_1.findSummitionQuantity(invInventoryDTO.getId(), getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId(), null, getProGroupListSelected(), getInvoiceType());
                            }
                            if (itemsViewForGallaryinner != null && !itemsViewForGallaryinner.isEmpty()) {
                                for (SummitionItemsView view : itemsViewForGallaryinner) {
                                    if (view.getCode() == 1 || view.getCode() == 2) {
                                        setValueForGallary(summitionItemsView, view);
                                    }
                                    distributeDate(invInventoryDTO, map, view);
                                }
                                getItemsViewList().add(summitionItemsView);
                                getItemsViewList().addAll(itemsViewForGallaryinner);
                                setFinalTotals(summitionItemsView);
                            }

                        }
                    }

                    if (getUri().contains("summitionItemReportPro")) {
                        itemsViewForGallaryinnerForAll = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(null, getDateFrom(), getDateTo(), getUserData().getUserBranch().getId(), getUserData().getUser(), getGallaryListSelected(), getProGroupListSelected());
                    } else {
                        itemsViewForGallaryinnerForAll = invPurchaseInvoiceService_1.findSummitionQuantity(null, getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId(), getGallaryListSelected(), getProGroupListSelected(), getInvoiceType());
                    }
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
                        getItemsViewList().addAll(itemsViewForGallaryinnerForAll);

                    }
                    setList(new ArrayList<Test>(map.values()));
                }
            }
        }
    }

    private void distributeDate(InvInventoryDTO invInventoryDTO, HashMap<Integer, Test> map, SummitionItemsView view) {
        Test test;
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("1")) {

            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }

            test.getB1().setNumber(view.getNumber());
            test.getB1().setPrice(view.getAllCost());
            test.getB1().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);
        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("2")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB2().setNumber(view.getNumber());
            test.getB2().setPrice(view.getAllCost());
            test.getB2().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("3")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB3().setNumber(view.getNumber());
            test.getB3().setPrice(view.getAllCost());
            test.getB3().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("4")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB4().setNumber(view.getNumber());
            test.getB4().setPrice(view.getAllCost());
            test.getB4().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("5")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB5().setNumber(view.getNumber());
            test.getB5().setPrice(view.getAllCost());
            test.getB5().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("6")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB6().setNumber(view.getNumber());
            test.getB6().setPrice(view.getAllCost());
            test.getB6().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("7")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB7().setNumber(view.getNumber());
            test.getB7().setPrice(view.getAllCost());
            test.getB7().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("8")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB8().setNumber(view.getNumber());
            test.getB8().setPrice(view.getAllCost());
            test.getB8().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("9")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB9().setNumber(view.getNumber());
            test.getB9().setPrice(view.getAllCost());
            test.getB9().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("10")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB10().setNumber(view.getNumber());
            test.getB10().setPrice(view.getAllCost());
            test.getB10().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("11")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB11().setNumber(view.getNumber());
            test.getB11().setPrice(view.getAllCost());
            test.getB11().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("12")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB12().setNumber(view.getNumber());
            test.getB12().setPrice(view.getAllCost());
            test.getB12().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("13")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB13().setNumber(view.getNumber());
            test.getB13().setPrice(view.getAllCost());
            test.getB13().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("14")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB14().setNumber(view.getNumber());
            test.getB14().setPrice(view.getAllCost());
            test.getB14().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("15")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB15().setNumber(view.getNumber());
            test.getB15().setPrice(view.getAllCost());
            test.getB15().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("16")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB16().setNumber(view.getNumber());
            test.getB16().setPrice(view.getAllCost());
            test.getB16().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("17")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB17().setNumber(view.getNumber());
            test.getB17().setPrice(view.getAllCost());
            test.getB17().setTotalCost(view.getAllAvarageCost());
            map.put(view.getCode(), test);

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

    private void setFinalTotals(SummitionItemsView summitionItemsView) {
        setSumFactoryNumberTotal(getSumFactoryNumberTotal().add(summitionItemsView.getFactoryNumber()));
        setSumSendNumberTotal(getSumSendNumberTotal().add(summitionItemsView.getSendNumber()));
        setSumDeliveryNumberTotal(getSumDeliveryNumberTotal().add(summitionItemsView.getDeliveryNumber()));
        setSumCustomerNumberTotal(getSumCustomerNumberTotal().add(summitionItemsView.getCustomerNumber()));
        setSumDifferentTotal(getSumDifferentTotal().add(summitionItemsView.getDifferent()));
        setSumNumberTotal(getSumNumberTotal().add(summitionItemsView.getNumber()));
        setSumAllCostTotal(getSumAllCostTotal().add(summitionItemsView.getAllCost()));
        setSumAllAvarageCost(getSumAllAvarageCost().add(summitionItemsView.getAllAvarageCost()));
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

    public void print2() throws JRException, IOException {
        Test testNew = null;
        for (Test t : getList()) {
            testNew = new Test();
            t.setTestNew(new ArrayList<>());
            testNew.setB14(t.getB14());
            testNew.setB15(t.getB15());
            testNew.setB16(t.getB16());
            testNew.setB17(t.getB17());
            testNew.setTotal(t.getTotal());
            t.getTestNew().add(testNew);
        }
        fillReport(prepareReport(), getUserData().getReportPath() + "testReportPro.jasper", getList(), "pdf");

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

        hashMap.put("titleText", " مبيعات الصناف حسب الفروع و الفئات");
        hashMap.put("dateFromText", "  من تاريخ");
        hashMap.put("dateFromValue", getDateFrom());
        hashMap.put("dateToText", "  الى تاريخ");
        hashMap.put("dateToValue", getDateTo());
        hashMap.put("gallaryText", "المعرض ");
//        hashMap.put("gallaryValue", getGallaryDTOSelected() == null ? null : getGallaryDTOSelected().getName());
        hashMap.put("proGroupListSelectedText", "الفئة ");
        hashMap.put("proGroupListSelectedValue", getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getName());
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

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        if (dateFrom == null) {
            dateFrom = new Date();
        }
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
        if (dateTo == null) {
            dateTo = new Date();
        }
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
            sumDifferentTotal = BigDecimal.ZERO;
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

    /**
     * @return the groupDTOList
     */
    public List<ProGroupDTO> getGroupDTOList() {
        if (groupDTOList == null || groupDTOList.isEmpty()) {
            groupDTOList = proGroupService.getallProGroupByBranchIdDTO(getUserData().getUser());
            setProGroupDTOConverter(new ProGroupDTOConverter(groupDTOList));
        }
        return groupDTOList;
    }

    /**
     * @param groupDTOList the groupDTOList to set
     */
    public void setGroupDTOList(List<ProGroupDTO> groupDTOList) {
        this.groupDTOList = groupDTOList;
    }

    /**
     * @return the proGroupDTOConverter
     */
    public ProGroupDTOConverter getProGroupDTOConverter() {
        return proGroupDTOConverter;
    }

    /**
     * @param proGroupDTOConverter the proGroupDTOConverter to set
     */
    public void setProGroupDTOConverter(ProGroupDTOConverter proGroupDTOConverter) {
        this.proGroupDTOConverter = proGroupDTOConverter;
    }

    /**
     * @return the proGroupListSelected
     */
    public List<ProGroupDTO> getProGroupListSelected() {
        if (proGroupListSelected == null) {
            proGroupListSelected = new ArrayList<>();
        }
        return proGroupListSelected;
    }

    /**
     * @param proGroupListSelected the proGroupListSelected to set
     */
    public void setProGroupListSelected(List<ProGroupDTO> proGroupListSelected) {
        this.proGroupListSelected = proGroupListSelected;
    }

    /**
     * @return the list
     */
    public ArrayList<Test> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(ArrayList<Test> list) {
        this.list = list;
    }

    /**
     * @return the invoiceType
     */
    public Integer getInvoiceType() {
        return invoiceType;
    }

    /**
     * @param invoiceType the invoiceType to set
     */
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     * @return the userDDs
     */
    public Map<String, String> getUserDDs() {
        return userDDs;
    }

    /**
     * @param userDDs the userDDs to set
     */
    public void setUserDDs(Map<String, String> userDDs) {
        this.userDDs = userDDs;
    }

    /**
     * @return the summution
     */
    public Boolean getSummution() {
        if(summution == null){
            summution = Boolean.FALSE;
        }
        return summution;
    }

    /**
     * @param summution the summution to set
     */
    public void setSummution(Boolean summution) {
        this.summution = summution;
    }

}
