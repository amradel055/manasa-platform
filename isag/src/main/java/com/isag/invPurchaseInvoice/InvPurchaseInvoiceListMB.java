/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invPurchaseInvoice;

import com.isag.businessservice.InvInventoryService;
import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entiy.InvPurchaseInvoiceEntity;
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
import com.isag.converter.InvInventoryConverter;
import com.isag.entity.InvInventory;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entiy.InvPurchaseInvoiceDetailsEntity;
import com.isag.entiy.SalesInvoiceEntity;
import com.isag.views.ItemsBarcodesDetailsView;
import com.isag.views.NetView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import tafqeet.Tafqeet;

/**
 *
 * @author WIN7
 */
@Named(value = "invPurchaseInvoiceListMB")
@ViewScoped
public class InvPurchaseInvoiceListMB extends BaseListBean {

    private UserData userData;
    private String screenMode;
    private Integer branchId;
    private Integer companyId;
    private String headRemarks;
    private Integer invPurchaseInvoiceIdSeclected;
    private BigDecimal totalValueAfterDiscount = BigDecimal.ZERO;
    private InvPurchaseInvoice invPurchaseInvoice;
    private BigDecimal taxvalue = BigDecimal.ZERO;
    private BigDecimal totalWithTaxValue = BigDecimal.ZERO;
    private BigDecimal totalNetAfterDiscount = BigDecimal.ZERO;
    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsList;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityReport;
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
//-----------------
    private Boolean viewTaxCode = Boolean.FALSE;
    private InvPurchaseInvoiceEntity purchaseInvoiceEentity;
    private List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList;
    private InvPurchaseInvoiceDetailsEntity invPurchaseInvoiceDetailsEntity;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityList;
    private List<InvInventory> inventoryList;
    private InvInventory invInventorySelection;
    private Integer sizeList;
    private InvInventoryConverter invInventoryConverter;
    //-----------------
    BigDecimal hundred = new BigDecimal(100);
    private List<NetView> invPurchaseInvoiceList;
    private InvPurchaseInvoiceEntity invPurchaseInvoiceEntity;
    private NetView invPurchaseInvoiceEntitySelected;
    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;

    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvoiceNetService invoiceNetService;
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
            saveError(e, "InvPurchaseInvoiceListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            userData = new UserData();
            invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoiceList = new ArrayList<>();
            invPurchaseInvoiceEntity = new InvPurchaseInvoiceEntity();
            invPurchaseInvoiceEntitySelected = new NetView();
            invPurchaseInvoiceEntityList = new ArrayList<>();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setBranchId(userData.getUserBranch().getId());
            setCompanyId(userData.getCompany().getId());

//        invPurchaseInvoiceList = invoiceNetService.getInvPurchaseInvoiceFromViewByBranchId(getBranchId(), false);
            //----------------------------------------------
            setInventoryList(isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId));
            if (getInventoryList() != null && !inventoryList.isEmpty()) {
                setInvInventoryConverter(new InvInventoryConverter(getInventoryList()));
            }
            setInvInventorySelection(getInventoryList() == null || getInventoryList().isEmpty() ? null : getInventoryList().get(0));
            loadInventoryList();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "load");
        }
    }

    public void loadInventoryList() {
        try {
            if (invInventorySelection != null) {
                invPurchaseInvoiceList = invoiceNetService.getInvoiceFromViewByBranchIdandInventory(branchId, 0, invInventorySelection.getId());
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("defaultInventory", invInventorySelection);
            } else {
                invPurchaseInvoiceList = invoiceNetService.getInvPurchaseInvoiceFromViewByBranchId(getBranchId(), 0);
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("defaultInventory", null);
            }

            setSizeList((Integer) invPurchaseInvoiceList.size());
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "loadInventoryList");
        }

    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../invpurchaseinvoice/invpurchaseinvoiceform.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (invPurchaseInvoiceIdSeclected != null && invPurchaseInvoiceIdSeclected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invPurchaseInvoiceIdSeclected", invPurchaseInvoiceIdSeclected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../invpurchaseinvoice/invpurchaseinvoiceform.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();

            invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(invPurchaseInvoiceEntitySelected.getHeadId());

            try {
                invPurchaseInvoiceService.deleteInvPurchaseInvoice(invPurchaseInvoice, 1);
                invPurchaseInvoiceList.remove(invPurchaseInvoiceEntitySelected);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "delete");
        }
    }

    public void print() {
        try {
            setTotalValueAfterDiscount(totalWithTaxValue);
            taxvalue = totalWithTaxValue;
            invPurchaseInvoice = new InvPurchaseInvoice();
            if (invPurchaseInvoiceIdSeclected != null && invPurchaseInvoiceIdSeclected > 0) {
                invPurchaseInvoice = invPurchaseInvoiceService.findInvPurchaseInvoiceById(invPurchaseInvoiceIdSeclected);
                if (getInvPurchaseInvoice() != null && getInvPurchaseInvoice().getId() != null) {

                    invPurchaseInvoiceDetailsList = new ArrayList<>();
                    invPurchaseInvoiceDetailsEntityReport = new ArrayList<>();
                    invPurchaseInvoiceDetailsList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(invPurchaseInvoiceIdSeclected);

                    StringBuilder sb = new StringBuilder();
                    InvPurchaseInvoiceDetailsEntity details;
                    Map<String, ItemsBarcodesDetailsView> map = new HashMap();
                    for (InvPurchaseInvoiceDetail detailsEntity : invPurchaseInvoiceDetailsList) {
                        if (sb.toString().isEmpty()) {
                            sb.append(detailsEntity.getItemBarcode());
                        } else {
                            sb.append(" , ").append(detailsEntity.getItemBarcode());
                        }
                    }
                    List<ItemsBarcodesDetailsView> barcodesDetailsViews = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdAndStore(branchId, sb.toString());

                    for (ItemsBarcodesDetailsView view : barcodesDetailsViews) {
                        map.put(view.getBarcode(), view);
                    }
                    for (InvPurchaseInvoiceDetail detailsEntity : invPurchaseInvoiceDetailsList) {
                        details = new InvPurchaseInvoiceDetailsEntity();
                        details.setSerial(detailsEntity.getSerial());
                        details.setItemCode(detailsEntity.getItemBarcode());
                        details.setItemName(detailsEntity.getItemId().getName());
                        details.setUnitName(detailsEntity.getUnitId().getName());
                        details.setQuantity(detailsEntity.getQuantity());
                        details.setCost(detailsEntity.getCost());
                        details.setNet(detailsEntity.getNet());
                        details.setColor(map.get(detailsEntity.getItemBarcode()) != null ? map.get(detailsEntity.getItemBarcode()).getPaintColorName() : "");
                        details.setLength(map.get(detailsEntity.getItemBarcode()) != null ? map.get(detailsEntity.getItemBarcode()).getLength() : null);
                        totalNet = (totalNet.add(details.getNet() != null ? details.getNet() : BigDecimal.ZERO)).setScale(2, BigDecimal.ROUND_UP);
                        totalValueAfterDiscount = totalValueAfterDiscount.add(details.getValueAfterDiscount() != null
                                ? details.getValueAfterDiscount() : BigDecimal.ZERO).setScale(2);
                        taxvalue = taxvalue.add(detailsEntity.getTaxValue() == null ? BigDecimal.ZERO : detailsEntity.getTaxValue());
                        calculateactualweight();
                        calculateTotalNetAfterDiscount();
                        updateTax();
                        invPurchaseInvoiceDetailsEntityReport.add(details);
                    }
                    try {
                        fillReport(prepareReport(), getUserData().getReportPath() + "purchaseReceiptCash.jasper", invPurchaseInvoiceDetailsEntityReport, "pdf");
                    } catch (JRException ex) {
                        Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "print");
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
            saveError(e, "InvPurchaseInvoiceListMB", "calculateactualweight");
        }

    }

    public void calculateTotalNetAfterDiscount() {
        try {

            totalNetAfterDiscount = BigDecimal.ZERO;

            if (invPurchaseInvoice.getDiscountType() == 0) {
                if (finalNet.compareTo(BigDecimal.ZERO) == 1
                        && (finalNet.compareTo(invPurchaseInvoice.getDiscount()) == 0
                        || finalNet.compareTo(invPurchaseInvoice.getDiscount()) == 1)) {
                    totalNetAfterDiscount = (finalNet.subtract(invPurchaseInvoice.getDiscount() != null ? invPurchaseInvoice.getDiscount() : BigDecimal.ZERO)).setScale(2, BigDecimal.ROUND_UP);
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
            saveError(e, "InvPurchaseInvoiceListMB", "calculateTotalNetAfterDiscount");
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
            saveError(e, "InvPurchaseInvoiceListMB", "isDiscountValid");
            return null;
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
            saveError(e, "InvPurchaseInvoiceListMB", "fillReport");
        }

    }

    public HashMap prepareReport() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            HashMap hashMap = new HashMap();

            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());
            if (isFileExist(getUserData().getImageReportPath())) {
                hashMap.put("companyImage", getUserData().getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }

            hashMap.put("oresText", invPurchaseInvoice.getInvInventoryId() == null ? "" : invPurchaseInvoice.getInvInventoryId().getName());
            hashMap.put("dateText", userDDs.get("DATE"));

            if (invPurchaseInvoice.getPaymentType() == 0) {
                hashMap.put("salesReceiptText", "فاتورة مشتريات نقدية");
            } else if (invPurchaseInvoice.getPaymentType() == 1) {
                hashMap.put("salesReceiptText", "فاتورة مشتريات آجلة");
            } else {
                hashMap.put("salesReceiptText", "فاتورة مشتريات اعتماد مستندي");
            }

            hashMap.put("receiptNumberText", "رقم الفاتورة");
            hashMap.put("weightDifferenceText", "فرق الوزن");
            hashMap.put("weightDifference", getMultiply());

            hashMap.put("documentNumberText", "رقم سند استلام");
            hashMap.put("remarkText", userDDs.get("REMARK"));

            hashMap.put("supplierText", userDDs.get("SUPPLIER"));

            hashMap.put("supplierReceiptText", "فاتورة المورد");
            hashMap.put("transportationSupplierText", "مورد النقل");

            hashMap.put("secondDateText", "تاريخها");

            hashMap.put("serialText", "م");

            hashMap.put("itemNumberText", "رقم الصنف");
            hashMap.put("itemNameText", "اسم الصنف");
            hashMap.put("unitText", "الوحدة");
            hashMap.put("amountText", "كمية");
            hashMap.put("colorText", "اللون");
            hashMap.put("lengthText", "الطول");
            hashMap.put("unitCostText", "تكلفة وحدة");
            hashMap.put("totalText", "القيمة");
            hashMap.put("nolonText", "النولون");
            hashMap.put("discountText", "الخصم");
            hashMap.put("netText", "الصافي");
            hashMap.put("totalWithTaxText", " الاجمالي");
            hashMap.put("tax", "الضريبة");

            hashMap.put("taxValue", getTaxvalue());
            hashMap.put("totalWithTaxValue", getTotalWithTaxValue());
            hashMap.put("receiptNumberValue", invPurchaseInvoice.getSerial());
            hashMap.put("dateValue", invPurchaseInvoice.getDate());
            hashMap.put("remarkValue", invPurchaseInvoice.getRemarks());
            hashMap.put("supplierValue", invPurchaseInvoice.getOrganizationSiteId() != null ? invPurchaseInvoice.getOrganizationSiteId().getCode() : "");
            hashMap.put("supplierName", invPurchaseInvoice.getOrganizationSiteId() != null ? invPurchaseInvoice.getOrganizationSiteId().getName() : "");
            hashMap.put("supplierReceiptValue", invPurchaseInvoice.getSupplierInvoiceNumber());
//        hashMap.put("transportationSupplierValue", invPurchaseInvoiceEntity.get);
//        hashMap.put("transportationSupplierName", "الصافي");
            hashMap.put("nolonValue", invPurchaseInvoice.getExtraCost());

            hashMap.put("discountValue", invPurchaseInvoice.getDiscount());
            if (invPurchaseInvoice.getDiscountType() != 0) {
                hashMap.put("%", " % ");
            } else {
                hashMap.put("%", "");
            }

            hashMap.put("netValue", getTotalNet());

            hashMap.put("netValueArabic", Tafqeet.numberToText(Double.parseDouble(getTotalNetAfterDiscount().toString()), "جنيه", "قرش"));

            hashMap.put("secondDateValue", invPurchaseInvoice.getSupplierInvoiceDate());

            hashMap.put("reportDate", new Date());

            return hashMap;
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "prepareReport");
            return null;
        }
    }

    public void updateTax() {
        try {
            try {
                totalWithTaxValue = totalNetAfterDiscount;
                taxvalue = BigDecimal.ZERO;
                if (invPurchaseInvoiceEntity.getTaxFlag() != null && invPurchaseInvoiceEntity.getTaxFlag()) {
                    taxvalue = totalNetAfterDiscount.multiply(BigDecimal.valueOf(0.14));
                    totalWithTaxValue = totalNetAfterDiscount.add(taxvalue);
                }
            } catch (Exception e) {
                saveError(e, "InvPurchaseInvoiceListMB", "updateTax");

            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "updateTax");
        }
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public Integer getInvPurchaseInvoiceIdSeclected() {
        return invPurchaseInvoiceIdSeclected;
    }

    public void setInvPurchaseInvoiceIdSeclected(Integer invPurchaseInvoiceIdSeclected) {
        this.invPurchaseInvoiceIdSeclected = invPurchaseInvoiceIdSeclected;
    }

    public List<InvPurchaseInvoiceEntity> getInvPurchaseInvoiceEntityList() {
        return invPurchaseInvoiceEntityList;
    }

    public void setInvPurchaseInvoiceEntityList(List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList) {
        this.invPurchaseInvoiceEntityList = invPurchaseInvoiceEntityList;
    }

    public InvPurchaseInvoiceEntity getInvPurchaseInvoiceEntity() {
        return invPurchaseInvoiceEntity;
    }

    public void setInvPurchaseInvoiceEntity(InvPurchaseInvoiceEntity invPurchaseInvoiceEntity) {
        this.invPurchaseInvoiceEntity = invPurchaseInvoiceEntity;
    }

    public NetView getInvPurchaseInvoiceEntitySelected() {
        return invPurchaseInvoiceEntitySelected;
    }

    public void setInvPurchaseInvoiceEntitySelected(NetView invPurchaseInvoiceEntitySelected) {
        this.invPurchaseInvoiceEntitySelected = invPurchaseInvoiceEntitySelected;
    }

    public InvPurchaseInvoice getInvPurchaseInvoice() {
        return invPurchaseInvoice;
    }

    public void setInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice) {
        this.invPurchaseInvoice = invPurchaseInvoice;
    }

    public List<NetView> getInvPurchaseInvoiceList() {
        return invPurchaseInvoiceList;
    }

    public void setInvPurchaseInvoiceList(List<NetView> invPurchaseInvoiceList) {
        this.invPurchaseInvoiceList = invPurchaseInvoiceList;
    }

    /**
     * @return the taxvalue
     */
    public BigDecimal getTaxvalue() {
        return taxvalue;
    }

    /**
     * @param taxvalue the taxvalue to set
     */
    public void setTaxvalue(BigDecimal taxvalue) {
        this.taxvalue = taxvalue;
    }

    /**
     * @return the totalWithTaxValue
     */
    public BigDecimal getTotalWithTaxValue() {
        return totalWithTaxValue;
    }

    /**
     * @param totalWithTaxValue the totalWithTaxValue to set
     */
    public void setTotalWithTaxValue(BigDecimal totalWithTaxValue) {
        this.totalWithTaxValue = totalWithTaxValue;
    }

    /**
     * @return the totalNetAfterDiscount
     */
    public BigDecimal getTotalNetAfterDiscount() {
        return totalNetAfterDiscount;
    }

    /**
     * @param totalNetAfterDiscount the totalNetAfterDiscount to set
     */
    public void setTotalNetAfterDiscount(BigDecimal totalNetAfterDiscount) {
        this.totalNetAfterDiscount = totalNetAfterDiscount;
    }

    /**
     * @return the invPurchaseInvoiceDetailsList
     */
    public List<InvPurchaseInvoiceDetail> getInvPurchaseInvoiceDetailsList() {
        return invPurchaseInvoiceDetailsList;
    }

    /**
     * @param invPurchaseInvoiceDetailsList the invPurchaseInvoiceDetailsList to
     * set
     */
    public void setInvPurchaseInvoiceDetailsList(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsList) {
        this.invPurchaseInvoiceDetailsList = invPurchaseInvoiceDetailsList;
    }

    /**
     * @return the invPurchaseInvoiceDetailsEntityReport
     */
    public List<InvPurchaseInvoiceDetailsEntity> getInvPurchaseInvoiceDetailsEntityReport() {
        return invPurchaseInvoiceDetailsEntityReport;
    }

    /**
     * @param invPurchaseInvoiceDetailsEntityReport the
     * invPurchaseInvoiceDetailsEntityReport to set
     */
    public void setInvPurchaseInvoiceDetailsEntityReport(List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailsEntityReport) {
        this.invPurchaseInvoiceDetailsEntityReport = invPurchaseInvoiceDetailsEntityReport;
    }

    /**
     * @return the purchaseInvoiceEentity
     */
    public InvPurchaseInvoiceEntity getPurchaseInvoiceEentity() {
        return purchaseInvoiceEentity;
    }

    /**
     * @param purchaseInvoiceEentity the purchaseInvoiceEentity to set
     */
    public void setPurchaseInvoiceEentity(InvPurchaseInvoiceEntity purchaseInvoiceEentity) {
        this.purchaseInvoiceEentity = purchaseInvoiceEentity;
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
     * @return the totalQuatity
     */
    public BigDecimal getTotalQuatity() {
        return totalQuatity;
    }

    /**
     * @param totalQuatity the totalQuatity to set
     */
    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
    }

    /**
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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
     * @return the payed
     */
    public BigDecimal getPayed() {
        return payed;
    }

    /**
     * @param payed the payed to set
     */
    public void setPayed(BigDecimal payed) {
        this.payed = payed;
    }

    /**
     * @return the remain
     */
    public BigDecimal getRemain() {
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    /**
     * @return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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
     * @return the totalSum
     */
    public BigDecimal getTotalSum() {
        return totalSum;
    }

    /**
     * @param totalSum the totalSum to set
     */
    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

    /**
     * @return the totalNet
     */
    public BigDecimal getTotalNet() {
        return totalNet;
    }

    /**
     * @param totalNet the totalNet to set
     */
    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
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
     * @return the subtractWeight
     */
    public BigDecimal getSubtractWeight() {
        return subtractWeight;
    }

    /**
     * @param subtractWeight the subtractWeight to set
     */
    public void setSubtractWeight(BigDecimal subtractWeight) {
        this.subtractWeight = subtractWeight;
    }

    /**
     * @return the multiply
     */
    public BigDecimal getMultiply() {
        return multiply;
    }

    /**
     * @param multiply the multiply to set
     */
    public void setMultiply(BigDecimal multiply) {
        this.multiply = multiply;
    }

    /**
     * @return the finalNet
     */
    public BigDecimal getFinalNet() {
        return finalNet;
    }

    /**
     * @param finalNet the finalNet to set
     */
    public void setFinalNet(BigDecimal finalNet) {
        this.finalNet = finalNet;
    }

    /**
     * @return the totalValueAfterDiscount
     */
    public BigDecimal getTotalValueAfterDiscount() {
        return totalValueAfterDiscount;
    }

    /**
     * @param totalValueAfterDiscount the totalValueAfterDiscount to set
     */
    public void setTotalValueAfterDiscount(BigDecimal totalValueAfterDiscount) {
        this.totalValueAfterDiscount = totalValueAfterDiscount;
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
}
