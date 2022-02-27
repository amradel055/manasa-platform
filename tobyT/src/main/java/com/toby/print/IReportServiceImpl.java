/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.print;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.ReportManagement;
import com.toby.entiy.InvPurchaseOrderEntity;
import com.toby.toby.UserData;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
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
                if (invPurchaseInvoiceDTO.getBarcodeSelect() != null && !invPurchaseInvoiceDTO.getBarcodeSelect()) {
                    fillReport(prepareInvPurchaseReport(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "purchaseReceiptCash.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                } else {
                    fillReport(prepareItemBarcodeReport(userData), userData.getReportPath() + "itemBarcodeList.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailBarcodeList(), "pdf");
                }

            } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                if (!invPurchaseInvoiceDTO.getBarcodeSelect()) {
                    fillReport(prepareInvSalesReport(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "salesReceiptCash.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                } else {
                    fillReport(prepareInvSalesReport(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "InvReceiptCash.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                }
            } else if (ScreenNameClassEnum.PROWORKORDER.equals(invPurchaseInvoiceDTO.getScreenName())) {

                fillReport(prepareProWorkOrderReport(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "salesReceiptCash1.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareReturnPurchaseReport(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "ReturnPurchaseReport.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVRETURNSALESFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {

            } else if (ScreenNameClassEnum.INVPURCHASEORDERFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvPurchaseOrder(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "PurchaseOrderReport.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVPURCHASEADDING.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvPurchaseOrder(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "PurchaseOrderReport.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVSALESORDERFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {

            } else if (ScreenNameClassEnum.INVADDINGORDERFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvAddingOrder(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "AddingReport.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvAddingOrder(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "PermissionReport.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVQUOTATIONFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {

            } else if (ScreenNameClassEnum.INVRESERVATIONFORM.equals(invPurchaseInvoiceDTO.getScreenName())) {

            } else if (ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(invPurchaseInvoiceDTO.getScreenName())) {
//                fillReport(prepareReportInvTransFer(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "purchaseReceiptCash1.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                fillReport(prepareInvPurchaseReport1(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "purchaseReceiptCash1.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVUPDATE.equals(invPurchaseInvoiceDTO.getScreenName())) {
                if (invPurchaseInvoiceDTO.getBarcodeSelect()) {
                    fillReport(prepareInvUpdate(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "InvUpdate.jasper", invPurchaseInvoiceDTO.getInvStripDetailList(), "pdf");
                } else {
                    fillReport(prepareInvUpdate(invPurchaseInvoiceDTO, userData), userData.getReportPath() + "InvUpdate.jasper", invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                }
            }
        } catch (JRException ex) {
            Logger.getLogger(InvPurchaseOrderEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InvPurchaseOrderEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap prepareItemBarcodeReport(UserData userData) {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            HashMap hashMap = new HashMap();
            hashMap.put("companyName", userData.getCompany().getName());
            return hashMap;
        } catch (Exception e) {

            return null;
        }
    }

    public HashMap prepareInvUpdate(InvInvoiceDTO invInvoiceDTO, UserData userData) {
        Map<String, String> userDDs = userData.getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("UserName", userData.getUser().getName());
        hashMap.put("Branch", userData.getUserBranch().getNameAr());

        if (isFileExist(userData.getImageReportPath(), userData)) {
            hashMap.put("companyImage", userData.getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("date", invInvoiceDTO.getDate());

        hashMap.put("header2", "ضبط المخزن");
        hashMap.put("codeText", "كود الصنف");

        hashMap.put("itemText", "اسم الصنف");

        hashMap.put("ActualAmountText", "الفعلى");
        hashMap.put("AmountText", "الدفترى");

        if (invInvoiceDTO.getInvInventoryId() != null) {
            hashMap.put("invInventoryText", "المخزن ");
            hashMap.put("invInventoryValue", invInvoiceDTO.getInvInventoryId().getName());
        }

        return hashMap;
    }

    public HashMap prepareReportInvTransFer(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        hashMap.put("serialText", "مسلسل");
        hashMap.put("serialvalue", invPurchaseInvoiceDTO.getSerial());

        hashMap.put("header2", "التحويل بين المخازن");

        hashMap.put("dateText", "التاريخ");
        hashMap.put("BillSDateText", "المخزن الرئيسي");
        hashMap.put("dateFrom", invPurchaseInvoiceDTO.getDate());

        hashMap.put("inventoryFromText", "من مخزن");
        hashMap.put("inventoryToText", "إلى مخزن");
        hashMap.put("inventoryFrom", invPurchaseInvoiceDTO.getInvInventoryId() == null ? null : invPurchaseInvoiceDTO.getInvInventoryId().getName());
        hashMap.put("inventoryTo", invPurchaseInvoiceDTO.getInvInventoryIdTo() == null ? null : invPurchaseInvoiceDTO.getInvInventoryIdTo().getName());

        hashMap.put("remarkText", "الملاحظات");
        hashMap.put("remarkValue", invPurchaseInvoiceDTO.getRemarks());

        hashMap.put("itemText", "الصنف");
        hashMap.put("unitText", "رقم الوحدة");
        hashMap.put("quantityText", "الكمية");
        hashMap.put("availableText", "المتاح");

        hashMap.put("reportDate", new Date());

        return hashMap;
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

    public HashMap prepareProWorkOrderReport(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData) {
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

//            if (invPurchaseInvoiceDTO.getTaxFlagFinal()) {
            hashMap.put("taxCodeText", userData.getUserBranch().getTaxCode());
            hashMap.put("salesReceiptText", "فاتورة ضريبية مبسطة ");

            hashMap.put("receiptNumberText", "رقم الفاتورة");
//            } else {
//                hashMap.put("salesReceiptText", userData.getUserDDs().get("TITLE_INVOICE_SALES_NOT_TAX"));
//                hashMap.put("receiptNumberText", userData.getUserDDs().get("NUMBER_INVOICE_SALES_NOT_TAX"));
//            }

            if (invPurchaseInvoiceDTO.getDiscountType() != null && invPurchaseInvoiceDTO.getDiscountType() == 1) {
                hashMap.put("percentage", "%");
            }
            hashMap.put("adressText", userData.getUserBranch().getAddress1());
//            hashMap.put("weightDifferenceText", "فرق الوزن");
//            hashMap.put("weightDifference", multiply);
            hashMap.put("customerText", "العميل");
            hashMap.put("addressText", "المعرض");
            hashMap.put("invNameText", "المعرض");
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
            hashMap.put("daribaNumber", "الرقم الضريبي");
            hashMap.put("segilText", "رقم السجل التجاري");
            hashMap.put("deliveryDateText", "تاريخ التسليم");

            hashMap.put("deliveryDateValue", invPurchaseInvoiceDTO.getDueDate());
            hashMap.put("taxValue", invPurchaseInvoiceDTO.getTaxvalue());
//            hashMap.put("totalValue", invPurchaseInvoiceDTO.getTotalNet());
            hashMap.put("totalValue", invPurchaseInvoiceDTO.getTotalNetAfterDiscount());
            hashMap.put("totalWithTaxValue", invPurchaseInvoiceDTO.getTaxvalue().add(invPurchaseInvoiceDTO.getTotalNet()));
            hashMap.put("payedValue", invPurchaseInvoiceDTO.getPayed());
            hashMap.put("remainValue", invPurchaseInvoiceDTO.getRemain());
            hashMap.put("receiptNumberValue", invPurchaseInvoiceDTO.getSerialTax());
            hashMap.put("workOrderNumberValue", invPurchaseInvoiceDTO.getSerial());
            hashMap.put("workOrderNumberValueText", "امر الشغل  ");
            if (invPurchaseInvoiceDTO.getGallaryId() != null) {
                hashMap.put("invName", invPurchaseInvoiceDTO.getGallaryId().getName());
                hashMap.put("daribaValue", invPurchaseInvoiceDTO.getGallaryId().getDaribaValue());
                hashMap.put("segilValue", invPurchaseInvoiceDTO.getGallaryId().getSegilValue());
            }
            hashMap.put("customerName", invPurchaseInvoiceDTO.getOrganizationSiteId() == null ? "" : invPurchaseInvoiceDTO.getOrganizationSiteId().getName());
            hashMap.put("customerPhone", invPurchaseInvoiceDTO.getOrganizationSiteId() == null ? "" : invPurchaseInvoiceDTO.getOrganizationSiteId().getMobile());
            hashMap.put("addressValue", invPurchaseInvoiceDTO.getOrganizationSiteId() == null ? "" : invPurchaseInvoiceDTO.getOrganizationSiteId().getAddress());
            hashMap.put("customerCodeValue", invPurchaseInvoiceDTO.getOrganizationSiteId() == null ? "" : invPurchaseInvoiceDTO.getOrganizationSiteId().getCode());
            hashMap.put("remarkValue", invPurchaseInvoiceDTO.getRemarks());
            hashMap.put("dateValue", invPurchaseInvoiceDTO.getDate());
            hashMap.put("salesValue", invPurchaseInvoiceDTO.getInvDelegatorId() == null ? "" : invPurchaseInvoiceDTO.getInvDelegatorId().getName());
//        hashMap.put("companyImage", getUserData().getImageReportPath());
            hashMap.put("discountValue", invPurchaseInvoiceDTO.getDiscountValue());
            hashMap.put("netValue", invPurchaseInvoiceDTO.getFinalNet());

            printQRCode(invPurchaseInvoiceDTO, hashMap);

            return hashMap;
        } catch (Exception e) {
//            saveError(e, "SalesInvoiceFormMB", "prepareReport");
            return null;
        }
    }
    
    private static final TimeZone utc = TimeZone.getTimeZone("UTC");
    private static final SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private void printQRCode(InvInvoiceDTO invPurchaseInvoiceDTO, HashMap hashMap) throws UnsupportedEncodingException, IOException {
        isoFormatter.setTimeZone(utc);
        String date = isoFormatter.format(invPurchaseInvoiceDTO.getDate());
        
//        String sellerName = "Bobs Records";
//        String sallerTRN = "310122393500003";
//        String invoiceDateTime = "2022-04-25T15:30:00Z";
//        String totalWithVAT = "1000.00";
//        String VATTotal = "150.00";
        
        
        String sellerName = invPurchaseInvoiceDTO.getGallaryId().getSellerName();
        String sallerTRN = invPurchaseInvoiceDTO.getGallaryId().getDaribaValue();
        String invoiceDateTime = date;
        String totalWithVAT = invPurchaseInvoiceDTO.getFinalNet().toString();
        String VATTotal = invPurchaseInvoiceDTO.getTaxvalue().toString();

        byte[] tag1Hex = sellerName.getBytes("UTF-8");
        sellerName =  new String(tag1Hex, StandardCharsets.UTF_8);
        String L1 = Integer.toHexString(sellerName.length());
        String L2 = Integer.toHexString(sallerTRN.length());
        String L3 = Integer.toHexString(invoiceDateTime.length());
        String L4 = Integer.toHexString(totalWithVAT.length());
        String L5 = Integer.toHexString(VATTotal.length());

        String hex = "01" + ((L1.length() == 1) ? ("0" + L1) : L1) + hex(sellerName)
                + "02" + ((L2.length() == 1) ? ("0" + L2) : L2) + hex(sallerTRN)
                + "03" + ((L3.length() == 1) ? ("0" + L3) : L3) + hex(invoiceDateTime)
                + "04" + ((L4.length() == 1) ? ("0" + L4) : L4) + hex(totalWithVAT)
                + "05" + ((L5.length() == 1) ? ("0" + L5) : L5) + hex(VATTotal);
        reportQRCode(hashMap, hex, invPurchaseInvoiceDTO.getGallaryId() == null ? 0 : invPurchaseInvoiceDTO.getGallaryId().getId());
    }

    public String hex(String string) {
        char[] ch = string.toCharArray();
        StringBuilder result = new StringBuilder();
        for (char c : ch) {
            result.append(Integer.toHexString(c));
        }
        return result.toString();
    }

     public void reportQRCode(HashMap hashMap, String qrCodeText, Integer GallaryId) {
        String filePath = "JD" + GallaryId + ".png";
        int size = 200;
        String fileType = "png";
        File qrFile = new File(filePath);
        createQRImage(qrFile, qrCodeText, size, fileType);
        hashMap.put("QRCode", "JD" + GallaryId + ".png");

    }

    public static String convertHexTo64(String hex) {
        //convert each letter in the hex string to a 4-digit binary string to create a binary representation of the hex string
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < hex.length(); i++) {
            int dec = Integer.parseInt(hex.charAt(i) + "", 16);
            StringBuilder bin = new StringBuilder(Integer.toBinaryString(dec));
            while (bin.length() < 4) {
                bin.insert(0, '0');
            }
            binary.append(bin);
        }
        //now take 6 bits at a time and convert to a single b64 digit to create the final b64 representation
        StringBuilder b64 = new StringBuilder();
        for (int i = 0; i < binary.length(); i++) {
            String temp = binary.substring(i, i + 5);
            int dec = Integer.parseInt(temp, 10);
            //convert dec to b64 with the lookup table here then append to b64
//            byte[] decodedHex = Hex.decodeHex(hex);
//            byte[] encodedHexB64 = Base64.codeBase64(decodedHex);
        }

        return b64.toString();
    }

    private void createQRImage(File qrFile, String qrCodeText, int size, String fileType) {
        try {
            // Create the ByteMatrix for the QR-Code that encodes the given String
            Hashtable hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hintMap.put(EncodeHintType.MARGIN, 1);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            byte[] decodedHex = Hex.decodeHex(qrCodeText.toCharArray());
            String imaged = Base64.encodeBase64String(decodedHex);
//            String imaged = Base64.getEncode.encodeToString(qrCodeText.getBytes());
            BitMatrix byteMatrix = qrCodeWriter.encode(imaged, BarcodeFormat.QR_CODE, size, size, hintMap);
            // Make the BufferedImage that are to hold the QRCode
            int matrixWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, matrixWidth, matrixWidth);
            // Paint and save the image using the ByteMatrix
            graphics.setColor(Color.BLACK);
            for (int i = 0; i < matrixWidth; i++) {
                for (int j = 0; j < matrixWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, fileType, qrFile);

//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(byteMatrix, "png", bos);
//        String images = Base64.getEncoder().encodeToString(bos.toByteArray()); // base64 encode
//
//        // return QrInfo
//        QrInfo qrInfo = new QrInfo();
//        qrInfo.setUrl(url);
//        qrInfo.setImage(image);
//        return qrInfo;
        } catch (Exception e) {
            e.printStackTrace();
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

            hashMap.put("netValueArabic", Tafqeet.numberToText(Double.parseDouble(invPurchaseInvoiceDTO.getTotalNetAfterDiscount().toString()), "ريـال", "هلله"));

            hashMap.put("secondDateValue", invPurchaseInvoiceDTO.getSupplierInvoiceDate());

            hashMap.put("reportDate", new Date());

            return hashMap;
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceListMB", "prepareReport");
            return null;
        }
    }

    public HashMap prepareInvPurchaseReport1(InvInvoiceDTO invPurchaseInvoiceDTO, UserData userData) {
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

            hashMap.put("salesReceiptText", "التحويل بين المخازن");

            hashMap.put("receiptNumberText", "رقم الفاتورة");

            hashMap.put("documentNumberText", "رقم سند استلام");
            hashMap.put("remarkText", userDDs.get("REMARK"));

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

            hashMap.put("netValueArabic", Tafqeet.numberToText(Double.parseDouble(invPurchaseInvoiceDTO.getTotalNetAfterDiscount().toString()), "ريـال", "هلله"));

            hashMap.put("secondDateValue", invPurchaseInvoiceDTO.getSupplierInvoiceDate());

            hashMap.put("reportDate", new Date());

            hashMap.put("inventoryFromText", "من مخزن");
            hashMap.put("inventoryToText", "إلى مخزن");
            hashMap.put("inventoryFrom", invPurchaseInvoiceDTO.getInvInventoryId() == null ? null : invPurchaseInvoiceDTO.getInvInventoryId().getName());
            hashMap.put("inventoryTo", invPurchaseInvoiceDTO.getInvInventoryIdTo() == null ? null : invPurchaseInvoiceDTO.getInvInventoryIdTo().getName());

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
        if (invPurchaseInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPURCHASEADDING)) {
            hashMap.put("header2", "امر استلام");
        }
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
            hashMap.put("supplierValue", invPurchaseInvoiceDTO.getOrganizationSiteId() == null ? "" : invPurchaseInvoiceDTO.getOrganizationSiteId().getName());
            hashMap.put("supplierbillText", "فاتورة العميل");
            hashMap.put("supplierBillValue", invPurchaseInvoiceDTO.getLoadedSerial());
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

        hashMap.put("gallaryIdText", "المعرض");
        hashMap.put("gallaryIdValue", invPurchaseInvoiceDTO.getGallaryId() == null ? null : invPurchaseInvoiceDTO.getGallaryId().getName());

        hashMap.put("organizationSiteIdText", "العميل");
        hashMap.put("organizationSiteIdValue", invPurchaseInvoiceDTO.getOrganizationSiteId() == null ? null : invPurchaseInvoiceDTO.getOrganizationSiteId().getName());

        hashMap.put("remarkText", "الملاحظات");
        hashMap.put("remarkValue", invPurchaseInvoiceDTO.getRemarks());

        hashMap.put("itemText", "الصنف");
        hashMap.put("unitText", "رقم الوحدة");
        hashMap.put("quantityText", "الكمية");

        hashMap.put("reportDate", new Date());

        return hashMap;
    }
//    public void reportQRCode(HashMap hashMap, String qrCodeText, Integer GallaryId, byte[] b) {
//        String filePath = "JD" + GallaryId + ".png";
//        int size = 200;
//        String fileType = "png";
//        File qrFile = new File(filePath);
//        createQRImage(qrFile, qrCodeText, size, fileType, b);
//        hashMap.put("QRCode", "JD" + GallaryId + ".png");
//
//    }

   
//    private void createQRImage(File qrFile, String qrCodeText, int size, String fileType, byte[] b) {
//        try {
//            // Create the ByteMatrix for the QR-Code that encodes the given String
//            Hashtable hintMap = new Hashtable<>();
//            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//            hintMap.put(EncodeHintType.MARGIN, 1);
//            QRCodeWriter qrCodeWriter = new QRCodeWriter();
////            String imaged = Base64.getEncoder().encodeToString(b);
//            imaged = "AQxCb2JzIFJlY29yZHMCDzMxMDEyMjM5MzUwMDAwMwMUMjAyMi0wNC0yNVQxNTozMDowMFoEBzEwMDAuMDAFBjE1MC4wMA==";
//            BitMatrix byteMatrix = qrCodeWriter.encode(imaged, BarcodeFormat.QR_CODE, size, size, hintMap);
//            // Make the BufferedImage that are to hold the QRCode
//            int matrixWidth = byteMatrix.getWidth();
//            BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
//            image.createGraphics();
//            Graphics2D graphics = (Graphics2D) image.getGraphics();
//            graphics.setColor(Color.WHITE);
//            graphics.fillRect(0, 0, matrixWidth, matrixWidth);
//            // Paint and save the image using the ByteMatrix
//            graphics.setColor(Color.BLACK);
//            for (int i = 0; i < matrixWidth; i++) {
//                for (int j = 0; j < matrixWidth; j++) {
//                    if (byteMatrix.get(i, j)) {
//                        graphics.fillRect(i, j, 1, 1);
//                    }
//                }
//            }
//            ImageIO.write(image, fileType, qrFile);
//
////            ByteArrayOutputStream bos = new ByteArrayOutputStream();
////        MatrixToImageWriter.writeToStream(byteMatrix, "png", bos);
////        String images = Base64.getEncoder().encodeToString(bos.toByteArray()); // base64 encode
////
////        // return QrInfo
////        QrInfo qrInfo = new QrInfo();
////        qrInfo.setUrl(url);
////        qrInfo.setImage(image);
////        return qrInfo;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
