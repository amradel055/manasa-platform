/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.salesinvoice;

import com.isag.businessservice.InvInventoryService;
import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entiy.SalesInvoiceEntity;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.InvoiceNetService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.ItemsBarcodesDetailsViewService;
import com.isag.businessservice.ReportManagementService;
import com.isag.converter.InvInventoryConverter;
import com.isag.entity.InvInventory;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.ReportManagement;
import com.isag.entiy.GlBankTransactionDetailEntity;
import com.isag.entiy.GlBankTransactionEntity;
import com.isag.entiy.InvPurchaseInvoiceDetailsEntity;
import com.isag.entiy.InvPurchaseInvoiceEntity;
import com.isag.invPurchaseInvoice.InvPurchaseInvoiceFormMB;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.views.NetView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author WIN7
 */
@Named(value = "SalesInvoiceListMB")
@ViewScoped
public class SalesInvoiceListMB extends BaseListBean {

    private UserData userData;
    private String screenMode;
    private Integer branchId;
    private Integer companyId;
    private Integer sizeList;
    private InvPurchaseInvoice invPurchaseInvoice;
    private BigDecimal taxvalue = BigDecimal.ZERO;
    private BigDecimal totalWithTaxValue = BigDecimal.ZERO;
    private BigDecimal totalNetAfterDiscount = BigDecimal.ZERO;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsList;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityReport;

    private Integer salesInvoiceIdSeclected;
    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private BigDecimal quantity = BigDecimal.ZERO;
    private BigDecimal totalWeight = BigDecimal.ZERO;
    private BigDecimal payed = BigDecimal.ZERO;
    private BigDecimal remain = BigDecimal.ZERO;
    private BigDecimal weight = BigDecimal.ZERO;
    private BigDecimal cost = BigDecimal.ZERO;
    private BigDecimal totalSum = BigDecimal.ZERO;
    private BigDecimal totalNet = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal subtractWeight = BigDecimal.ZERO;
    private BigDecimal multiply = BigDecimal.ZERO;
    private BigDecimal finalNet = BigDecimal.ZERO;

    BigDecimal hundred = new BigDecimal(100);

    private InvPurchaseInvoice salesInvoiceInvoice;
    private List<InvInventory> inventoryList;
    private InvInventory invInventorySelection;
    private List<NetView> salesInvoiceList;
    private NetView salesInvoiceEntity;
    private NetView salesInvoiceEntitySelected;
    private List<SalesInvoiceEntity> salesInvoiceEntityList;
    private InvInventoryConverter invInventoryConverter;
    //-----------------
    private Boolean viewTaxCode = Boolean.FALSE;
    private SalesInvoiceEntity salesInvoiceEentity;
    private List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList;
    private InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntity;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityList;
    //-----------------
    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    private InvPurchaseInvoiceService salesInvoiceService;
    @EJB
    private InvoiceNetService invoiceNetService;
    @EJB
    private InvInventoryService inventoryService;
    @EJB
    private ReportManagementService reportManagementService;
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            userData = new UserData();
            salesInvoiceInvoice = new InvPurchaseInvoice();
            salesInvoiceList = new ArrayList<>();
            salesInvoiceEntity = new NetView();
            salesInvoiceEntitySelected = new NetView();
            salesInvoiceEntityList = new ArrayList<>();
            invPurchaseInvoiceDetailsEntityList = new ArrayList<>();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setBranchId(userData.getUserBranch().getId());
            setCompanyId(userData.getCompany().getId());

            //----------------------------------------------
            inventoryList = isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId);
            if (inventoryList != null && !inventoryList.isEmpty()) {
                invInventoryConverter = new InvInventoryConverter(inventoryList);
            }
            invInventorySelection = (inventoryList == null || inventoryList.isEmpty() ? null : inventoryList.get(0));
            loadInventoryList();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "load");
        }

    }

    public void loadInventoryList() {
        try {
            if (invInventorySelection != null) {
                salesInvoiceList = invoiceNetService.getInvoiceFromViewByBranchIdandInventory(branchId, 1, invInventorySelection.getId());
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("defaultInventory", invInventorySelection);
            } else {
                salesInvoiceList = invoiceNetService.getInvPurchaseInvoiceFromViewByBranchId(getBranchId(), 1);
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("defaultInventory", null);
            }

            sizeList = salesInvoiceList.size();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "loadInventoryList");
        }

    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../invsalesinvoice/invsalesinvoiceform.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (salesInvoiceIdSeclected != null && salesInvoiceIdSeclected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("salesinvoiceIdSeclected", salesInvoiceIdSeclected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../invsalesinvoice/invsalesinvoiceform.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            salesInvoiceInvoice = new InvPurchaseInvoice();
            salesInvoiceInvoice.setId(salesInvoiceEntitySelected.getHeadId());

            try {
                salesInvoiceService.deleteInvPurchaseInvoice(salesInvoiceInvoice, 0);
                salesInvoiceList.remove(salesInvoiceEntitySelected);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "delete");
        }
    }

    public void printA4() {
        try {
            print("salesReceiptCash.jasper");
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "printA4");
        }
    }

    public void print(String name) {
        try {
            invPurchaseInvoice = new InvPurchaseInvoice();
            if (salesInvoiceIdSeclected != null && salesInvoiceIdSeclected > 0) {
                invPurchaseInvoice = salesInvoiceService.findInvPurchaseInvoiceById(salesInvoiceIdSeclected);
                if (getInvPurchaseInvoice() != null && getInvPurchaseInvoice().getId() != null) {
                    invPurchaseInvoiceDetailsList = new ArrayList<>();
                    invPurchaseInvoiceDetailsEntityReport = new ArrayList<>();
                    invPurchaseInvoiceDetailsList = getInvPurchaseInvoiceDetailsService().getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(salesInvoiceIdSeclected);

                    StringBuilder sb = new StringBuilder();
                    InvPurchaseInvoiceDetailsEntity details;
                    Map<String, ItemsBarcodesDetailsView> map = new HashMap();
                    for (InvPurchaseInvoiceDetail detailsEntity : invPurchaseInvoiceDetailsList) {
                        if (sb.toString().isEmpty()) {
                            sb.append("'").append(detailsEntity.getItemBarcode()).append("'");
                        } else {
                            sb.append(" , ").append("'").append(detailsEntity.getItemBarcode()).append("'");
                        }
                    }
                    List<ItemsBarcodesDetailsView> barcodesDetailsViews = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdAndStore(branchId, sb.toString());

                    for (ItemsBarcodesDetailsView view : barcodesDetailsViews) {
                        map.put(view.getBarcode(), view);
                    }

                    for (InvPurchaseInvoiceDetail detailsEntity : invPurchaseInvoiceDetailsList) {
                        details = new InvPurchaseInvoiceDetailsEntity();
                        details.setSerial(detailsEntity.getSerial());
                        details.setItemCode(detailsEntity.getItemBarcode() != null ? detailsEntity.getItemBarcode() : "");
                        details.setItemName(detailsEntity.getItemId().getName() != null ? detailsEntity.getItemId().getName() : "");
                        details.setUnitName(detailsEntity.getItemId().getUnitId().getName() != null ? detailsEntity.getItemId().getUnitId().getName() : "");
                        details.setQuantity(detailsEntity.getQuantity() != null ? detailsEntity.getQuantity() : BigDecimal.ZERO);
                        details.setWeight(detailsEntity.getWeight() != null ? detailsEntity.getWeight() : BigDecimal.ZERO);
                        details.setCost(detailsEntity.getCost() != null ? detailsEntity.getCost() : BigDecimal.ZERO);
                        details.setNet(detailsEntity.getNet() != null ? detailsEntity.getNet() : BigDecimal.ZERO);
                        details.setColor(map.get(detailsEntity.getItemBarcode()).getPaintColorName());
                        details.setLength(map.get(detailsEntity.getItemBarcode()).getLength());
                        //----------------------
                        quantity = detailsEntity.getQuantity() != null ? detailsEntity.getQuantity() : BigDecimal.ZERO;
                        weight = detailsEntity.getWeight() != null ? detailsEntity.getWeight() : BigDecimal.ZERO;
                        if (weight != null && weight.compareTo(BigDecimal.ZERO) > 0) {
                            if (quantity != null) {
                                setTotalWeight(getTotalWeight().add(weight.multiply(quantity)));
                            } else {
                                setTotalWeight(getTotalWeight().add(weight));
                            }
                        } else {
                            setTotalWeight(getTotalWeight().add(BigDecimal.ZERO));
                        }
//            totalWeight = getTotalWeight().add(weight != null ? weight : BigDecimal.ZERO);
                        setCost(detailsEntity.getCost() != null ? detailsEntity.getCost() : BigDecimal.ZERO);

                        setDiscount(detailsEntity.getDiscount() != null ? detailsEntity.getDiscount() : BigDecimal.ZERO);
                        //---------------------------------------------------------------

                        details.setTotal((quantity.multiply(cost)));
                        details.setNet(detailsEntity.getNet());
                        totalNet = totalNet.add(details.getNet() != null ? details.getNet() : BigDecimal.ZERO);

                        invPurchaseInvoice.setTotalActualWeight(finalNet);
                        totalSum = totalSum.add(details.getTotal() != null ? details.getTotal() : BigDecimal.ZERO);
                        calculateactualweight();
                        calculateTotalNetAfterDiscount();
                        calculateRemainValue();
                        invPurchaseInvoiceDetailsEntityReport.add(details);

                    }
                    try {
                        fillReport(prepareReport(), getUserData().getReportPath() + name, invPurchaseInvoiceDetailsEntityReport, "pdf");
                    } catch (JRException ex) {
                        Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "print");
        }
    }

    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            if ("pdf".equals(exportType)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                servletOutputStream.flush();
                servletOutputStream.close();
                httpServletResponse.getOutputStream().flush();
                httpServletResponse.getOutputStream().close();
            } else if ("excel".equals(exportType)) {

            } else if ("html".equals(exportType)) {

            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "fillReport");
        }

    }

    public void updateTax() {
        try {
            removeTaxValue();
            if (invPurchaseInvoice.getTaxflag() != null && invPurchaseInvoice.getTaxflag()) {
                taxvalue = totalNetAfterDiscount.multiply(BigDecimal.valueOf(0.14));
                totalWithTaxValue = totalNetAfterDiscount.add(taxvalue);
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "updateTax");
        }
    }

    public void removeTaxValue() {
        try {
            totalWithTaxValue = totalNetAfterDiscount;
            taxvalue = BigDecimal.ZERO;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "removeTaxValue");
        }
    }

    public void calculateRemainValue() {
        try {
            remain = payed.subtract(totalWithTaxValue);
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "calculateRemainValue");
        }
    }

    public void calculateactualweight() {
        try {
            subtractWeight = (invPurchaseInvoice.getActualWeight() != null ? invPurchaseInvoice.getActualWeight() : BigDecimal.ZERO).subtract(getTotalWeight() == null ? BigDecimal.ZERO : getTotalWeight());
            multiply = subtractWeight.multiply(invPurchaseInvoice.getPriceKilo() != null ? invPurchaseInvoice.getPriceKilo() : BigDecimal.ZERO);
            finalNet = totalNet.add(multiply);
            invPurchaseInvoice.setTotalActualWeight(finalNet);
            calculateTotalNetAfterDiscount();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "calculateactualweight");
        }

    }

    public void calculateTotalNetAfterDiscount() {
        try {
            totalNetAfterDiscount = BigDecimal.ZERO;

            if (invPurchaseInvoice.getDiscountType() == 0) {
                if (invPurchaseInvoice != null && finalNet != null
                        && invPurchaseInvoice.getDiscount() != null
                        && finalNet.compareTo(BigDecimal.ZERO) == 1
                        && (finalNet.compareTo(invPurchaseInvoice.getDiscount()) == 0
                        || finalNet.compareTo(invPurchaseInvoice.getDiscount()) == 1)) {
                    totalNetAfterDiscount = (finalNet.subtract(invPurchaseInvoice.getDiscount() != null ? invPurchaseInvoice.getDiscount() : BigDecimal.ZERO)).setScale(2);
                } else {
                    invPurchaseInvoice.setDiscount(BigDecimal.ZERO);
                    totalNetAfterDiscount = finalNet.subtract(BigDecimal.ZERO);
                }
            } else {
                if (isDiscountValid(invPurchaseInvoice.getDiscount())) {
                    totalNetAfterDiscount = finalNet.subtract(finalNet.multiply(invPurchaseInvoice.getDiscount()).divide(hundred));
                } else {
                    invPurchaseInvoice.setDiscount(BigDecimal.ZERO);
                    totalNetAfterDiscount = finalNet.subtract(BigDecimal.ZERO);
                }
            }
            updateTax();
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "calculateTotalNetAfterDiscount");
        }

    }

    private Boolean isDiscountValid(BigDecimal discValue) {
        try {
            if (discValue != null) {
                if ((discValue.compareTo(BigDecimal.ZERO) == 0 || discValue.compareTo(BigDecimal.ZERO) == 1)
                        && (discValue.compareTo(hundred) == 0 || hundred.compareTo(discValue) == 1)) {
                    return true;
                } else {
                    return errorMessage(userData, "لا يجب ادخال خصم اعلى من 100% في حالة النسبة");
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "isDiscountValid");
            return false;
        }
    }

    public HashMap prepareReport() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            HashMap hashMap = new HashMap();

            ReportManagement reportManagement = reportManagementService.reportManagementByBranch(getUserData().getUserBranch().getId());
            if (reportManagement != null) {
                hashMap.put("LeftUp1", reportManagement.getLeftUp1());
                hashMap.put("LeftUp2", reportManagement.getLeftUp2());
                hashMap.put("LeftUp3", reportManagement.getLeftUp3());
                hashMap.put("LeftUp4", reportManagement.getLeftUp4());
                hashMap.put("RightUp1", reportManagement.getRightUp1());
                hashMap.put("RightUp2", reportManagement.getRightUp2());
                hashMap.put("RightUp3", reportManagement.getRightUp3());
                hashMap.put("RightUp4", reportManagement.getRightUp4());

            }
            if (isFileExist(getUserData().getImageReportPath())) {
                hashMap.put("companyImage", getUserData().getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }
            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());
            hashMap.put("phone", getUserData().getUserBranch().getPhone());
            hashMap.put("phoneText", "تليفون/فاكس");
            hashMap.put("address", getUserData().getUserBranch().getAddress1());
            hashMap.put("branchNameEn", getUserData().getUserBranch().getNameEn());
            hashMap.put("companyNameEn", getUserData().getCompany().getNameEn());
            hashMap.put("addressEn", getUserData().getUserBranch().getAddress2());

            if (getViewTaxCode()) {
                hashMap.put("taxCodeText", getUserData().getUserBranch().getTaxCode());
                if (invPurchaseInvoice.getPaymentType() == 0) {
                    hashMap.put("salesReceiptText", "فاتورة مبيعات نقدي");
                } else if (invPurchaseInvoice.getPaymentType() == 1) {
                    hashMap.put("salesReceiptText", "فاتورة مبيعات آجلة");
                } else if (invPurchaseInvoice.getPaymentType() == 2) {
                    hashMap.put("salesReceiptText", "فاتورة مبيعات اعتماد مستندي");
                }

                hashMap.put("receiptNumberText", "رقم الفاتورة");
            } else {
                hashMap.put("salesReceiptText", getUserData().getUserDDs().get("TITLE_INVOICE_SALES_NOT_TAX"));
                hashMap.put("receiptNumberText", getUserData().getUserDDs().get("NUMBER_INVOICE_SALES_NOT_TAX"));
            }

            if (invPurchaseInvoice.getDiscountType() != null && invPurchaseInvoice.getDiscountType() == 1) {
                hashMap.put("percentage", "%");
            }

            hashMap.put("weightDifferenceText", "فرق الوزن");
            hashMap.put("weightDifference", multiply != null ? multiply : BigDecimal.ZERO);
            hashMap.put("customerText", "العميل");
            hashMap.put("addressText", "المخزن");
            hashMap.put("invNameText", "المخزن");
            hashMap.put("colorText", "اللون");
            hashMap.put("lengthText", "الطول");
            hashMap.put("remarkText", userDDs.get("REMARK"));
            hashMap.put("dateText", userDDs.get("DATE"));
            hashMap.put("salesText", "المندوب");
            hashMap.put("sellText", "المبيعات");
            hashMap.put("accountText", "الحسابات");
            hashMap.put("deliveryText", "المستلم");
            hashMap.put("serialText", "م");
            hashMap.put("itemNumberText", "رقم الصنف");
            hashMap.put("itemNameText", "اسم الصنف");
            hashMap.put("unitText", "الوحدة");
            hashMap.put("amountText", "كمية");
            hashMap.put("unitCostText", "السعر");
            hashMap.put("totalText", "القيمة");
            hashMap.put("discountText", "الخصم");
            hashMap.put("netText", "الصافي");
            hashMap.put("totalWithTaxText", "الإجمالي");
            hashMap.put("payedText", "المبلغ المدفوع");
            hashMap.put("remainText", "المبلغ المتبقى");
            hashMap.put("tax", "الضريبةً");
            hashMap.put("taxValue", taxvalue != null ? taxvalue : BigDecimal.ZERO);
            hashMap.put("totalValue", totalNet != null ? totalNet : BigDecimal.ZERO);
            hashMap.put("totalWithTaxValue", totalWithTaxValue != null ? totalWithTaxValue : BigDecimal.ZERO);
            hashMap.put("payedValue", payed != null ? payed : BigDecimal.ZERO);
            hashMap.put("remainValue", remain != null ? remain : BigDecimal.ZERO);
            hashMap.put("receiptNumberValue", invPurchaseInvoice.getSerial() != null ? invPurchaseInvoice.getSerial() : 0);
            hashMap.put("invName", invPurchaseInvoice.getInvInventoryId().getName() != null ? invPurchaseInvoice.getInvInventoryId().getName() : "");
            hashMap.put("customerName", invPurchaseInvoice.getOrganizationSiteId().getName() != null ? invPurchaseInvoice.getOrganizationSiteId().getName() : "");
            hashMap.put("customerPhone", invPurchaseInvoice.getOrganizationSiteId().getMobile() != null ? invPurchaseInvoice.getOrganizationSiteId().getMobile() : "");
            hashMap.put("addressValue", invPurchaseInvoice.getOrganizationSiteId().getAddress() != null ? invPurchaseInvoice.getOrganizationSiteId().getAddress() : "");
            hashMap.put("remarkValue", invPurchaseInvoice.getRemarks() != null ? invPurchaseInvoice.getRemarks() : "");
            hashMap.put("dateValue", invPurchaseInvoice.getDate() != null ? invPurchaseInvoice.getDate() : "");
            hashMap.put("salesValue", invPurchaseInvoice.getInvDelegatorId().getName() != null ? invPurchaseInvoice.getInvDelegatorId().getName() : "");
//        hashMap.put("companyImage", getUserData().getImageReportPath());
            hashMap.put("discountValue", invPurchaseInvoice.getDiscount() != null ? invPurchaseInvoice.getDiscount() : BigDecimal.ZERO);
            hashMap.put("netValue", totalNetAfterDiscount != null ? totalNetAfterDiscount : BigDecimal.ZERO);

            return hashMap;
        } catch (Exception e) {
            saveError(e, "SalesInvoiceListMB", "prepareReport");
            return null;
        }
    }

    @Override
    public void filter() {
    }

    @Override
    public String getScreenName() {

        return "";
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getSalesInvoiceIdSeclected() {
        return salesInvoiceIdSeclected;
    }

    public void setSalesInvoiceIdSeclected(Integer salesInvoiceIdSeclected) {
        this.salesInvoiceIdSeclected = salesInvoiceIdSeclected;
    }

    public InvPurchaseInvoice getSalesInvoiceInvoice() {
        return salesInvoiceInvoice;
    }

    public void setSalesInvoiceInvoice(InvPurchaseInvoice salesInvoiceInvoice) {
        this.salesInvoiceInvoice = salesInvoiceInvoice;
    }

    public List<SalesInvoiceEntity> getSalesInvoiceEntityList() {
        return salesInvoiceEntityList;
    }

    public void setSalesInvoiceEntityList(List<SalesInvoiceEntity> salesInvoiceEntityList) {
        this.salesInvoiceEntityList = salesInvoiceEntityList;
    }

    public List<NetView> getSalesInvoiceList() {
        return salesInvoiceList;
    }

    public void setSalesInvoiceList(List<NetView> salesInvoiceList) {
        this.salesInvoiceList = salesInvoiceList;
    }

    public NetView getSalesInvoiceEntity() {
        return salesInvoiceEntity;
    }

    public void setSalesInvoiceEntity(NetView salesInvoiceEntity) {
        this.salesInvoiceEntity = salesInvoiceEntity;
    }

    public NetView getSalesInvoiceEntitySelected() {
        return salesInvoiceEntitySelected;
    }

    public void setSalesInvoiceEntitySelected(NetView salesInvoiceEntitySelected) {
        this.salesInvoiceEntitySelected = salesInvoiceEntitySelected;
    }

    /**
     * @return the invPurchaseInvoiceEntityList
     */
    public List<InvPurchaseInvoiceEntity> getInvPurchaseInvoiceEntityList() {
        return invPurchaseInvoiceEntityList;
    }

    /**
     * @param invPurchaseInvoiceEntityList the invPurchaseInvoiceEntityList to
     * set
     */
    public void setInvPurchaseInvoiceEntityList(List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList) {
        this.invPurchaseInvoiceEntityList = invPurchaseInvoiceEntityList;
    }

    /**
     * @return the invPurchaseInvoiceDetailsEntity
     */
    public InvPurchaseInvoiceDetailsEntity getInvPurchaseInvoiceDetailsEntity() {
        return invPurchaseInvoiceDetailsEntity;
    }

    /**
     * @param invPurchaseInvoiceDetailsEntity the
     * invPurchaseInvoiceDetailsEntity to set
     */
    public void setInvPurchaseInvoiceDetailsEntity(InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntity) {
        this.invPurchaseInvoiceDetailsEntity = invPurchaseInvoiceDetailsEntity;
    }

    /**
     * @return the invPurchaseInvoiceDetailsEntityList
     */
    public List<InvPurchaseInvoiceDetailsEntity> getInvPurchaseInvoiceDetailsEntityList() {
        return invPurchaseInvoiceDetailsEntityList;
    }

    /**
     * @param invPurchaseInvoiceDetailsEntityList the
     * invPurchaseInvoiceDetailsEntityList to set
     */
    public void setInvPurchaseInvoiceDetailsEntityList(List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityList) {
        this.invPurchaseInvoiceDetailsEntityList = invPurchaseInvoiceDetailsEntityList;
    }

    /**
     * @return the salesInvoiceEentity
     */
    public SalesInvoiceEntity getSalesInvoiceEentity() {
        return salesInvoiceEentity;
    }

    /**
     * @param salesInvoiceEentity the salesInvoiceEentity to set
     */
    public void setSalesInvoiceEentity(SalesInvoiceEntity salesInvoiceEentity) {
        this.salesInvoiceEentity = salesInvoiceEentity;
    }

    /**
     * @return the viewTaxCode
     */
    public Boolean getViewTaxCode() {
        return viewTaxCode;
    }

    /**
     * @param viewTaxCode the viewTaxCode to set
     */
    public void setViewTaxCode(Boolean viewTaxCode) {
        this.viewTaxCode = viewTaxCode;
    }

    /**
     * @return the invPurchaseInvoice
     */
    public InvPurchaseInvoice getInvPurchaseInvoice() {
        return invPurchaseInvoice;
    }

    /**
     * @param invPurchaseInvoice the invPurchaseInvoice to set
     */
    public void setInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice) {
        this.invPurchaseInvoice = invPurchaseInvoice;
    }

    /**
     * @return the invPurchaseInvoiceDetailsService
     */
    public InvPurchaseInvoiceDetailsService getInvPurchaseInvoiceDetailsService() {
        return invPurchaseInvoiceDetailsService;
    }

    /**
     * @param invPurchaseInvoiceDetailsService the
     * invPurchaseInvoiceDetailsService to set
     */
    public void setInvPurchaseInvoiceDetailsService(InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService) {
        this.invPurchaseInvoiceDetailsService = invPurchaseInvoiceDetailsService;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * @return the totalWeight
     */
    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    /**
     * @param totalWeight the totalWeight to set
     */
    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    /**
     * @return the inventoryList
     */
    public List<InvInventory> getInventoryList() {
        return inventoryList;
    }

    /**
     * @param inventoryList the inventoryList to set
     */
    public void setInventoryList(List<InvInventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    /**
     * @return the inventoryService
     */
    public InvInventoryService getInventoryService() {
        return inventoryService;
    }

    /**
     * @param inventoryService the inventoryService to set
     */
    public void setInventoryService(InvInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * @return the invInventoryConverter
     */
    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    /**
     * @param invInventoryConverter the invInventoryConverter to set
     */
    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    /**
     * @return the invInventorySelection
     */
    public InvInventory getInvInventorySelection() {
        return invInventorySelection;
    }

    /**
     * @param invInventorySelection the invInventorySelection to set
     */
    public void setInvInventorySelection(InvInventory invInventorySelection) {
        this.invInventorySelection = invInventorySelection;
    }

    /**
     * @return the sizeList
     */
    public Integer getSizeList() {
        return sizeList;
    }

    /**
     * @param sizeList the sizeList to set
     */
    public void setSizeList(Integer sizeList) {
        this.sizeList = sizeList;
    }
}
