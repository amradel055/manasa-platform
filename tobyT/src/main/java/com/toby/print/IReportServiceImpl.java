/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.print;

import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.ReportManagement;
import com.toby.entiy.InvPurchaseOrderEntity;
import com.toby.toby.UserData;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
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
 * @author hq004
 */
public class IReportServiceImpl implements IReportService {

    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
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
    }

    public boolean isFileExist(String filePath, UserData userData) {
        try {
            if (userData.getImageReportPath() != null) {
                File file = new File(userData.getImageReportPath());
                return file.exists();
            }
        } catch (Exception e) {

        }
        return false;
    }

    public HashMap prepareReportGeneral(HashMap hashMap, UserData userData) {

        hashMap.put("PrintedBy", userData.getUser().getName());
        hashMap.put("Branch", userData.getUserBranch().getNameAr());
        hashMap.put("companyName", userData.getCompany().getName());
        if (isFileExist(userData.getImageReportPath(), userData)) {
            hashMap.put("companyImage", userData.getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        return hashMap;

    }

    @Override
    public void printReportInvoice(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData) {

        try {
            if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareInvPurchaseReport(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "purchaseReceiptCash.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareInvSalesReport(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "salesReceiptCash.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareReturnPurchaseReport(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "ReturnPurchaseReport.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVRETURNSALESFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {

            } else if (ScreenNameClassEnum.INVPURCHASEORDERFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvPurchaseOrder(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "PurchaseOrderReport.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVSALESORDERFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {

            } else if (ScreenNameClassEnum.INVADDINGORDERFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvAddingOrder(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "AddingReport.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvAddingOrder(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "PermissionReport.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVQUOTATIONFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {

            } else if (ScreenNameClassEnum.INVRESERVATIONFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {

            }
        } catch (JRException ex) {
            Logger.getLogger(InvPurchaseOrderEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InvPurchaseOrderEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap prepareReturnPurchaseReport(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData) {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            HashMap hashMap = new HashMap();

            hashMap.put("PrintedBy", userData.getUser().getName());
            hashMap.put("branchName", userData.getUserBranch().getNameAr());
            hashMap.put("companyName", userData.getCompany().getName());

            if (isFileExist(userData.getImageReportPath(), userData)) {
                hashMap.put("companyImage", userData.getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }

            hashMap.put("oresText", "مردودات المشتريات");
            hashMap.put("returnReportText", "مردودات مشتريات آجلة");

            hashMap.put("receiptNumberText", "رقم المردود");
            hashMap.put("supplierText", userDDs.get("SUPPLIER"));
            hashMap.put("dateText", userDDs.get("DATE"));
            hashMap.put("supplierReceiptText", "رقم الفاتورة");
            hashMap.put("secondDateText", "تاريخ الفاتورة");
            hashMap.put("remarkText", userDDs.get("REMARK"));

            hashMap.put("serialText", "م");
            hashMap.put("itemNumberText", "رقم الصنف");
            hashMap.put("itemNameText", "اسم الصنف");
            hashMap.put("unitText", "الوحدة");
            hashMap.put("screwingText", "الشد");
            hashMap.put("amountText", "الكمية");
            hashMap.put("unitCostText", "التكلفة");

            hashMap.put("totalText", "الاجمالى");

            hashMap.put("discountText", "الخصم");
            hashMap.put("netText", "الصافي");

            hashMap.put("receiptNumberValue", invPurchaseInvoiceDTO.getSerial());
            hashMap.put("supplierCode", invPurchaseInvoiceDTO.getOrganizationSiteId() != null ? invPurchaseInvoiceDTO.getOrganizationSiteId().getCode() : null);
            hashMap.put("supplierName", invPurchaseInvoiceDTO.getOrganizationSiteId() != null ? invPurchaseInvoiceDTO.getOrganizationSiteId().getName() : null);
            hashMap.put("dateValue", invPurchaseInvoiceDTO.getDate());
            hashMap.put("supplierReceiptValue", invPurchaseInvoiceDTO.getSupplierInvoiceNumber());
            hashMap.put("secondDateValue", invPurchaseInvoiceDTO.getSupplierInvoiceDate());
            hashMap.put("remarkValue", invPurchaseInvoiceDTO.getRemarks());

            hashMap.put("netValue", invPurchaseInvoiceDTO.getFinalNet());
            hashMap.put("reportDate", new Date());

            return hashMap;
        } catch (Exception e) {
//            saveError(e, "InvReturnPurchaseFormMB", "prepareReport");
            return null;
        }
    }

    public HashMap prepareInvSalesReport(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData) {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            HashMap hashMap = new HashMap();

            ReportManagement reportManagement = invPurchaseInvoiceDTO.getReportManagement();
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
            if (isFileExist(userData.getImageReportPath(), userData)) {
                hashMap.put("companyImage", userData.getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }
            hashMap.put("PrintedBy", userData.getUser().getName());
            hashMap.put("branchName", userData.getUserBranch().getNameAr());
            hashMap.put("companyName", userData.getCompany().getName());
            hashMap.put("phone", userData.getUserBranch().getPhone());
            hashMap.put("phoneText", "تليفون/فاكس");
            hashMap.put("address", userData.getUserBranch().getAddress1());
            hashMap.put("branchNameEn", userData.getUserBranch().getNameEn());
            hashMap.put("companyNameEn", userData.getCompany().getNameEn());
            hashMap.put("addressEn", userData.getUserBranch().getAddress2());
            hashMap.put("cashier", invPurchaseInvoiceDTO.getCreatedByName());

            if (invPurchaseInvoiceDTO.getTaxFlagFinal()) {
                hashMap.put("taxCodeText", userData.getUserBranch().getTaxCode());
                if (invPurchaseInvoiceDTO.getPaymentType() == 0) {
                    hashMap.put("salesReceiptText", "فاتورة مبيعات نقدي");
                } else if (invPurchaseInvoiceDTO.getPaymentType() == 1) {
                    hashMap.put("salesReceiptText", "فاتورة مبيعات آجلة");
                } else if (invPurchaseInvoiceDTO.getPaymentType() == 2) {
                    hashMap.put("salesReceiptText", "فاتورة مبيعات اعتماد مستندي");
                }

                hashMap.put("receiptNumberText", "رقم الفاتورة");
            } else {
                hashMap.put("salesReceiptText", userData.getUserDDs().get("TITLE_INVOICE_SALES_NOT_TAX"));
                hashMap.put("receiptNumberText", userData.getUserDDs().get("NUMBER_INVOICE_SALES_NOT_TAX"));
            }

            if (invPurchaseInvoiceDTO.getDiscountType() != null && invPurchaseInvoiceDTO.getDiscountType() == 1) {
                hashMap.put("percentage", "%");
            }
            hashMap.put("adressText", userData.getUserBranch().getAddress1());
//            hashMap.put("weightDifferenceText", "فرق الوزن");
//            hashMap.put("weightDifference", multiply);
            hashMap.put("customerText", "العميل");
            hashMap.put("addressText", "المخزن");
            hashMap.put("invNameText", "المخزن");
            hashMap.put("timeText", "الوقت");
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
            hashMap.put("taxValue", invPurchaseInvoiceDTO.getTaxvalue());
//            hashMap.put("totalValue", invPurchaseInvoiceDTO.getTotalNet());
            hashMap.put("totalValue", invPurchaseInvoiceDTO.getTotalNetAfterDiscount());
            hashMap.put("totalWithTaxValue", invPurchaseInvoiceDTO.getTaxvalue().add(invPurchaseInvoiceDTO.getTotalNet()));
            hashMap.put("payedValue", invPurchaseInvoiceDTO.getPayed());
            hashMap.put("remainValue", invPurchaseInvoiceDTO.getRemain());
            hashMap.put("receiptNumberValue", invPurchaseInvoiceDTO.getSerial());
            hashMap.put("invName", invPurchaseInvoiceDTO.getInvInventoryId().getName());
            hashMap.put("customerName", invPurchaseInvoiceDTO.getOrganizationSiteId().getName());
            hashMap.put("customerPhone", invPurchaseInvoiceDTO.getOrganizationSiteId().getMobile());
            hashMap.put("addressValue", invPurchaseInvoiceDTO.getOrganizationSiteId().getAddress());
            hashMap.put("remarkValue", invPurchaseInvoiceDTO.getRemarks());
            hashMap.put("dateValue", invPurchaseInvoiceDTO.getDate());
            hashMap.put("salesValue", invPurchaseInvoiceDTO.getInvDelegatorId().getName());
//        hashMap.put("companyImage", getUserData().getImageReportPath());
            hashMap.put("discountValue", invPurchaseInvoiceDTO.getDiscountValue());
            hashMap.put("netValue", invPurchaseInvoiceDTO.getFinalNet());

            return hashMap;
        } catch (Exception e) {
//            saveError(e, "SalesInvoiceFormMB", "prepareReport");
            return null;
        }
    }

    public HashMap prepareInvPurchaseReport(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData) {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            HashMap hashMap = new HashMap();

            hashMap.put("PrintedBy", userData.getUser().getName());
            hashMap.put("branchName", userData.getUserBranch().getNameAr());
            hashMap.put("companyName", userData.getCompany().getName());
            if (isFileExist(userData.getImageReportPath(), userData)) {
                hashMap.put("companyImage", userData.getImageReportPath());
            } else {
                hashMap.put("companyImage", null);
            }

            hashMap.put("oresText", invPurchaseInvoiceDTO.getInvInventoryId() == null ? "" : invPurchaseInvoiceDTO.getInvInventoryId().getName());
            hashMap.put("dateText", userDDs.get("DATE"));

            if (invPurchaseInvoiceDTO.getPaymentType() == 0) {
                hashMap.put("salesReceiptText", "فاتورة مشتريات نقدية");
            } else if (invPurchaseInvoiceDTO.getPaymentType() == 1) {
                hashMap.put("salesReceiptText", "فاتورة مشتريات آجلة");
            } else {
                hashMap.put("salesReceiptText", "فاتورة مشتريات اعتماد مستندي");
            }

            hashMap.put("receiptNumberText", "رقم الفاتورة");

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

            hashMap.put("taxValue", invPurchaseInvoiceDTO.getTaxvalue());
            hashMap.put("totalWithTaxValue", invPurchaseInvoiceDTO.getFinalNet());
            hashMap.put("receiptNumberValue", invPurchaseInvoiceDTO.getSerial());
            hashMap.put("dateValue", invPurchaseInvoiceDTO.getDate());
            hashMap.put("remarkValue", invPurchaseInvoiceDTO.getRemarks());
            hashMap.put("supplierValue", invPurchaseInvoiceDTO.getOrganizationSiteId() != null ? invPurchaseInvoiceDTO.getOrganizationSiteId().getCode() : "");
            hashMap.put("supplierName", invPurchaseInvoiceDTO.getOrganizationSiteId() != null ? invPurchaseInvoiceDTO.getOrganizationSiteId().getName() : "");
            hashMap.put("supplierReceiptValue", invPurchaseInvoiceDTO.getSupplierInvoiceNumber());
//        hashMap.put("transportationSupplierValue", invPurchaseInvoiceEntity.get);
//        hashMap.put("transportationSupplierName", "الصافي");
            hashMap.put("nolonValue", invPurchaseInvoiceDTO.getExtraCost());

            hashMap.put("discountValue", invPurchaseInvoiceDTO.getDiscount());
            if (invPurchaseInvoiceDTO.getDiscountType() != 0) {
                hashMap.put("%", " % ");
            } else {
                hashMap.put("%", "");
            }

            hashMap.put("netValue", invPurchaseInvoiceDTO.getTotalNet());

            hashMap.put("netValueArabic", Tafqeet.numberToText(Double.parseDouble(invPurchaseInvoiceDTO.getTotalNetAfterDiscount().toString()), "جنيه", "قرش"));

            hashMap.put("secondDateValue", invPurchaseInvoiceDTO.getSupplierInvoiceDate());

            hashMap.put("reportDate", new Date());

            return hashMap;
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceListMB", "prepareReport");
            return null;
        }
    }

    public HashMap prepareReportInvPurchaseOrder(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        Map<String, String> userDDs = userData.getUserDDs();

        hashMap.put("header2", " طلب شراء");
        hashMap.put("serialText", "رقم أمر الشراء");
        hashMap.put("serialvalue", invPurchaseInvoiceDTO.getSerial());
        hashMap.put("supplierText", "اسم المورد");
        hashMap.put("supplierValue", invPurchaseInvoiceDTO.getOrganizationSiteId() == null ? null : invPurchaseInvoiceDTO.getOrganizationSiteId().getName());
        hashMap.put("BillSDateText", "التاريخ");
        hashMap.put("BillSDatevalue", invPurchaseInvoiceDTO.getDate());
        hashMap.put("supplierbillText", "المعدل");
        hashMap.put("supplierBillValue", invPurchaseInvoiceDTO.getRate());
        hashMap.put("pOrderNumText", "العملة");
        hashMap.put("pOrderNumValue", invPurchaseInvoiceDTO.getCurrencyId() == null ? null : invPurchaseInvoiceDTO.getCurrencyId().getName());

        hashMap.put("delegatorText", "المندوب");
        hashMap.put("delegatorValue", invPurchaseInvoiceDTO.getInvDelegatorId() == null ? null : invPurchaseInvoiceDTO.getInvDelegatorId().getName());
        hashMap.put("remarkText", "الملاحظات");
        hashMap.put("remarkValue", invPurchaseInvoiceDTO.getRemarks());

        hashMap.put("itemText", "الصنف");
        hashMap.put("unitText", "رقم الوحدة");
        hashMap.put("quantityText", "الكمية");
        hashMap.put("priceText", "السعر");
        hashMap.put("discountText", "الخصم");
        hashMap.put("totalText", "الاجمالى");
        String x = invPurchaseInvoiceDTO.getInvDelegatorId() == null ? null : invPurchaseInvoiceDTO.getInvDelegatorId().getName();
        hashMap.put("test", x + " مندوب مشتريات الدقي tttttttest");

        hashMap.put("reportDate", new Date());

        return hashMap;
    }

    public HashMap prepareReportInvAddingOrder(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        Map<String, String> userDDs = userData.getUserDDs();

        if (invPurchaseInvoiceDTO.getType() == 1) {
            hashMap.put("header2", "اذن صرف بضاعة");
            hashMap.put("supplierText", "اسم العميل");
            hashMap.put("supplierValue", invPurchaseInvoiceDTO.getOrganizationSiteId().getName());
            hashMap.put("supplierbillText", "فاتورة العميل");
            hashMap.put("supplierBillValue", invPurchaseInvoiceDTO.getOrganizationSiteId());
            hashMap.put("BillSDateText", "تاريخ فاتورة العميل");
            hashMap.put("BillSDatevalue", invPurchaseInvoiceDTO.getSupplierInvoiceDate());

        } else {
            hashMap.put("header2", "اذن استلام بضاعة");
            hashMap.put("supplierText", "اسم المورد");
            hashMap.put("supplierValue", invPurchaseInvoiceDTO.getOrganizationSiteId().getName());
            hashMap.put("supplierbillText", "فاتورة المورد");
            hashMap.put("supplierBillValue", invPurchaseInvoiceDTO.getSupplierInvoiceNumber());
            hashMap.put("pOrderNumText", "رقم أمر شراء");
            hashMap.put("pOrderNumValue", invPurchaseInvoiceDTO.getLoadedSerial());
            hashMap.put("BillSDateText", "تاريخ فاتورة المورد");
            hashMap.put("BillSDatevalue", invPurchaseInvoiceDTO.getSupplierInvoiceDate());
            hashMap.put("delegatorText", "المندوب");
            hashMap.put("delegatorValue", invPurchaseInvoiceDTO.getInvDelegatorId() == null ? null : invPurchaseInvoiceDTO.getInvDelegatorId().getName());
        }

        hashMap.put("serialText", "مسلسل");
        hashMap.put("serialvalue", invPurchaseInvoiceDTO.getSerial());

        hashMap.put("dateText", "تاريخ");
        hashMap.put("datevalue", invPurchaseInvoiceDTO.getDate());

        hashMap.put("depositoryText", "المخزن");
        hashMap.put("depositoryValue", invPurchaseInvoiceDTO.getInvInventoryId() == null ? null : invPurchaseInvoiceDTO.getInvInventoryId().getName());

        hashMap.put("remarkText", "الملاحظات");
        hashMap.put("remarkValue", invPurchaseInvoiceDTO.getRemarks());

        hashMap.put("itemText", "الصنف");
        hashMap.put("unitText", "رقم الوحدة");
        hashMap.put("quantityText", "الكمية");

        hashMap.put("reportDate", new Date());

        return hashMap;
    }

}
