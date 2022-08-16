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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

@Named(value = "summitionItemReportMB2")
@ViewScoped
public class SummitionItemReportMB2 extends Basic {

    private Date dateFrom;
    private Date dateTo;
    private ProGroupDTO proGroupDTOSelected;
    private Integer invoiceType;
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
    private boolean tobAndKhiata;

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;
    @EJB
    ProGroupService proGroupService;

    @PostConstruct
    public void init() {

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
            HashMap<Integer, Test> map = new HashMap();
            Test test = new Test();
            List<Test> listTest = new ArrayList<>();
            for (InvInventoryDTO invInventoryDTO : getGallaryListSelected()) {

                listTest = new ArrayList<>();

                summitionItemsView = new SummitionItemsView();

                summitionItemsView.setName(invInventoryDTO.getName());
                summitionItemsView.setCode(Integer.parseInt(invInventoryDTO.getCode()));
                summitionItemsView.setRowType(1);

                if (getUri().contains("summitionItemReportPro")) {
//                    itemsViewForGallaryinner = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(invInventoryDTO.getId(), getDateFrom(), getDateTo(), getUserData().getUserBranch().getId(), getUserData().getUser(), null, null);
                    itemsViewForGallaryinner = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(invInventoryDTO.getId(), getDateFrom(), getDateTo(), getUserData().getUserBranch().getId(), getUserData().getUser(), null, getProGroupListSelected());

                } else {
//                    itemsViewForGallaryinner = invPurchaseInvoiceService_1.findSummitionQuantity(invInventoryDTO.getId(), getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId(), null, null, null);
                    itemsViewForGallaryinner = invPurchaseInvoiceService_1.findSummitionQuantity(invInventoryDTO.getId(), getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId(), null, getProGroupListSelected(), getInvoiceType());

                }
                if (itemsViewForGallaryinner != null && !itemsViewForGallaryinner.isEmpty()) {
                    for (SummitionItemsView view : itemsViewForGallaryinner) {
                        if (tobAndKhiata) {
                            if (view.getCode() == 1 || view.getCode() == 2) {
                                setValueForGallary(summitionItemsView, view);
                            }
                        } else {
                            setValueForGallary(summitionItemsView, view);
                        }

                        distributeDate(invInventoryDTO, map, view);

                    }
                    getItemsViewList().add(summitionItemsView);
                    getItemsViewList().addAll(itemsViewForGallaryinner);
                    setFinalTotals(summitionItemsView);
                }

            }

            if (getUri().contains("summitionItemReportPro")) {
//                itemsViewForGallaryinnerForAll = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(null, getDateFrom(), getDateTo(), getUserData().getUserBranch().getId(), getUserData().getUser(), getGallaryListSelected(), null);
                itemsViewForGallaryinnerForAll = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(null, getDateFrom(), getDateTo(), getUserData().getUserBranch().getId(), getUserData().getUser(), getGallaryListSelected(), getProGroupListSelected());
            } else {
//                itemsViewForGallaryinnerForAll = invPurchaseInvoiceService_1.findSummitionQuantity(null, getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getProGroupDTOSelected() == null ? null : getProGroupDTOSelected().getId(), getGallaryListSelected(), null, null);
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
                    test = map.get(view.getCode());
                    if (test == null) {
                        test = new Test();
                        test.setCode(view.getCode());
                        test.setName(view.getName());
                    }
                    test.getTotal().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
                    test.getTotal().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
                    test.getTotal().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
                    map.put(view.getCode(), test);
                }
                getItemsViewList().add(summitionItemsView);
                getItemsViewList().addAll(itemsViewForGallaryinnerForAll);

            }
            setList(new ArrayList<Test>(map.values()));
        }

    }

    public void searchNew() {
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
            HashMap<Integer, Test> map = new HashMap();
            Test test = new Test();
            List<Test> listTest = new ArrayList<>();
            Boolean update = Boolean.TRUE;

            for (InvInventoryDTO invInventoryDTO : getGallaryListSelected()) {
                System.out.println(invInventoryDTO.getCode() + "   " + new Date());
                listTest = new ArrayList<>();
                List<InvInventoryDTO> gallaryListSelected = new ArrayList<>();
                gallaryListSelected.add(invInventoryDTO);
                summitionItemsView = new SummitionItemsView();

                summitionItemsView.setName(invInventoryDTO.getName());
                summitionItemsView.setCode(Integer.parseInt(invInventoryDTO.getCode()));
                summitionItemsView.setRowType(1);

//                BigDecimal totalNotice = BigDecimal.ZERO;
                BigDecimal totalPersentageNotice = BigDecimal.ZERO;
                if (getUri().contains("summitionItemReportPro")) {
//                    itemsViewForGallaryinner = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(invInventoryDTO.getId(), getDateFrom(), getDateTo(), getUserData().getUserBranch().getId(), getUserData().getUser(), null, null);
                    itemsViewForGallaryinner = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(invInventoryDTO.getId(), getDateFrom(), getDateTo(), getUserData().getUserBranch().getId(), getUserData().getUser(), null, getProGroupListSelected());

                } else {
                    if (getUri().contains("proproductionreport/summitionItemReport.xhtml")) {
                        itemsViewForGallaryinner = invPurchaseInvoiceService_1.findSummitionQuantityNew(invInventoryDTO.getId(), getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), null, getProGroupListSelected(), getInvoiceType(), false);
                    } else {
                        itemsViewForGallaryinner = invPurchaseInvoiceService_1.findSummitionQuantityNew(invInventoryDTO.getId(), getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), null, getProGroupListSelected(), getInvoiceType(), true);
                    }
                    update = Boolean.FALSE;
//                    totalNotice = invPurchaseInvoiceService_1.findNotice(invInventoryDTO.getId(), getUserData().getUserBranch().getId(), dateFrom, dateTo, getGallaryListSelected());
                    totalPersentageNotice = invPurchaseInvoiceService_1.findPersentageNotice(invInventoryDTO.getId(), getUserData().getUserBranch().getId(), dateFrom, dateTo, gallaryListSelected);
                }
                if (itemsViewForGallaryinner != null && !itemsViewForGallaryinner.isEmpty()) {
                    for (SummitionItemsView view : itemsViewForGallaryinner) {
                        if (totalPersentageNotice == null) {
                            totalPersentageNotice = BigDecimal.ZERO;
                        }
                        view.setAllCost(new BigDecimal((view.getAllCost().subtract(view.getAllCost().multiply(totalPersentageNotice))).setScale(3, RoundingMode.HALF_UP).toPlainString()));
                        if (tobAndKhiata) {
                            if (view.getCode() == 1 || view.getCode() == 2) {
                                setValueForGallary(summitionItemsView, view);
                            }
                        } else {
                            setValueForGallary(summitionItemsView, view);
                        }
                        distributeDate(invInventoryDTO, map, view);
                    }
                    getItemsViewList().add(summitionItemsView);
                    getItemsViewList().addAll(itemsViewForGallaryinner);
                    setFinalTotals(summitionItemsView);
                }
            }
            BigDecimal totalNotice = BigDecimal.ZERO;
            BigDecimal totalPersentageNotice = BigDecimal.ZERO;
            if (getUri().contains("summitionItemReportPro")) {
//                itemsViewForGallaryinnerForAll = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(null, getDateFrom(), getDateTo(), getUserData().getUserBranch().getId(), getUserData().getUser(), getGallaryListSelected(), null);
                itemsViewForGallaryinnerForAll = invPurchaseInvoiceService_1.getTheListBasedOnStagesList(null, getDateFrom(), getDateTo(), getUserData().getUserBranch().getId(), getUserData().getUser(), getGallaryListSelected(), getProGroupListSelected());
            } else {
                itemsViewForGallaryinnerForAll = invPurchaseInvoiceService_1.findSummitionQuantityNew(null, getUserData().getUserBranch().getId(), getDateFrom(), getDateTo(), getGallaryListSelected(), getProGroupListSelected(), getInvoiceType(), update);
                totalNotice = invPurchaseInvoiceService_1.findNotice(null, invoiceType, dateFrom, dateTo, getGallaryListSelected());
                totalPersentageNotice = invPurchaseInvoiceService_1.findPersentageNotice(null, getUserData().getUserBranch().getId(), dateFrom, dateTo, getGallaryListSelected());
            }
            if (itemsViewForGallaryinnerForAll != null && !itemsViewForGallaryinnerForAll.isEmpty()) {

                summitionItemsView = new SummitionItemsView();
                summitionItemsView.setName("إجماليات المعارض");
                summitionItemsView.setRowType(1);
                for (SummitionItemsView view : itemsViewForGallaryinnerForAll) {
                    if (totalPersentageNotice == null) {
                        totalPersentageNotice = BigDecimal.ZERO;
                    }
                    view.setAllCost(new BigDecimal((view.getAllCost().subtract(view.getAllCost().multiply(totalPersentageNotice))).setScale(3, RoundingMode.HALF_UP).toPlainString()));
                    if (tobAndKhiata) {
                        if (view.getCode() == 1 || view.getCode() == 2) {
                            setValueForGallary(summitionItemsView, view);
                        }
                    } else {
                        setValueForGallary(summitionItemsView, view);
                    }
                    test = map.get(view.getCode());
                    if (test == null) {
                        test = new Test();
                        test.setCode(view.getCode());
                        test.setName(view.getName());
                    }
                    test.getTotal().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
                    test.getTotal().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
                    test.getTotal().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
                    map.put(view.getCode(), test);
                }
                getItemsViewList().add(summitionItemsView);
                getItemsViewList().addAll(itemsViewForGallaryinnerForAll);

            }
            setList(new ArrayList<Test>(map.values()));
        }

    }

    private void distributeDate(InvInventoryDTO invInventoryDTO, HashMap<Integer, Test> map, SummitionItemsView view) {
        Test test;
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("001")) {

            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }

            test.getB1().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB1().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB1().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);
        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("002")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB2().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB2().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB2().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("003")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB3().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB3().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB3().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("004")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB4().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB4().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB4().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("005")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB5().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB5().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB5().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("006")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB6().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB6().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB6().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("007")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB7().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB7().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB7().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("008")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB8().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB8().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB8().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("009")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB9().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB9().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB9().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("010")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB10().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB10().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB10().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("011")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB11().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB11().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB11().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("012")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB12().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB12().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB12().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("013")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB13().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB13().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB13().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("014")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB14().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB14().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB14().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("015")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB15().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB15().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB15().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("016")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB16().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB16().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB16().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            map.put(view.getCode(), test);

        }
        if (invInventoryDTO.getCode() != null && invInventoryDTO.getCode().equals("017")) {
            test = map.get(view.getCode());
            if (test == null) {
                test = new Test();
                test.setCode(view.getCode());
                test.setName(view.getName());
            }
            test.getB17().setNumber(new BigDecimal(view.getNumber().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB17().setPrice(new BigDecimal(view.getAllCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
            test.getB17().setTotalCost(new BigDecimal(view.getAllAvarageCost().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()));
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

        hashMap.put("dateFromText", "  من تاريخ");
        hashMap.put("dateFromValue", getDateFrom());
        hashMap.put("dateToText", "  الى تاريخ");
        hashMap.put("dateToValue", getDateTo());
        hashMap.put("gallaryText", "المعرض ");
//        hashMap.put("gallaryValue", getGallaryDTOSelected() == null ? null : getGallaryDTOSelected().getName());
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
        hashMap.put("subreport", getUserData().getReportPath() + "test2sub.jasper");

        HashMap param = new HashMap();

        param.put("guideNameTXt", "البند");
        param.put("excuteQtyTxt", "الكميه المنفذه");
        param.put("priceSellTxt", "سعر البيع");
        param.put("discountValueTxt", "المقاول");
        param.put("discountPercentageTxt", "المشروع");
        param.put("netCostTxt", "صافي بند المستخلص");
        hashMap.put("param", param);

        return hashMap;
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
        if (sumNumberTotal == null) {
            sumNumberTotal = BigDecimal.ZERO;
        }
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
        if (sumAllCostTotal == null) {
            sumAllCostTotal = BigDecimal.ZERO;
        }
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
        if (sumAllAvarageCost == null) {
            sumAllAvarageCost = BigDecimal.ZERO;
        }
        return sumAllAvarageCost;
    }

    /**
     * @param sumAllAvarageCost the sumAllAvarageCost to set
     */
    public void setSumAllAvarageCost(BigDecimal sumAllAvarageCost) {
        this.sumAllAvarageCost = sumAllAvarageCost;
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
     * @return the tobAndKhiata
     */
    public boolean isTobAndKhiata() {
        return tobAndKhiata;
    }

    /**
     * @param tobAndKhiata the tobAndKhiata to set
     */
    public void setTobAndKhiata(boolean tobAndKhiata) {
        this.tobAndKhiata = tobAndKhiata;
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

}
