/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.print;

import com.isag.businessservice.reports.entityBean.completionRateReportEntity;
import com.isag.businessservice.reports.entityBean.rscExtractJournalReportPrintEntity;
import com.isag.businessservice.reports.searchBean.RscContractExtractionWorkunitSearchBean;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscExtractRegisterDTO;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.entity.ReportManagement;
import com.isag.entiy.InvPurchaseOrderEntity;
import com.isag.isag.UserData;
import com.isag.report.entity.RscPriceTenderBean;
import com.isag.report.entity.RscRestrictionQuantitiesBean;
import com.isag.report.entity.RscTenderBean;
import com.isag.report.entity.RscContractRegisterBean;
import com.isag.report.entity.RscExtractRegisteringBean;
import com.isag.report.entity.RscQuotationPriceTenderBean;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import tafqeet.Tafqeet;

/**
 *
 * @author hq004
 */
public class IReportServiceImpl implements IReportService {

    /*  public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
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
    }*/
    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse httpServletResponse = (HttpServletResponse) context.getExternalContext().getResponse();
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            if ("pdf".equals(exportType)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                httpServletResponse.flushBuffer();
                servletOutputStream.flush();
                servletOutputStream.close();
                context.renderResponse();
                context.responseComplete();
                return;
            } else if ("excel".equals(exportType)) {
            } else if ("html".equals(exportType)) {
            } else {
                JasperPrintManager.printReport(jasperPrint, false);
                JasperPrintManager.printReport(jasperPrint, false);
            }
        } catch (Exception ex) {
            System.out.println(ex + "exception report");
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
    public void printReportInvoice(InvInvoiceDTO invInvoiceDTO, UserData userData) {

        try {
            if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(invInvoiceDTO.getScreenName())) {
                if (!invInvoiceDTO.getBarcodeSelect()) {
                    if (invInvoiceDTO.getTypeActivity() != null && invInvoiceDTO.getTypeActivity() == 1) {
                        fillReport(prepareInvPurchaseReport(invInvoiceDTO, userData), userData.getReportPath() + "purchaseReceiptCashCeramic.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                    } else {
                        fillReport(prepareInvPurchaseReport(invInvoiceDTO, userData), userData.getReportPath() + "purchaseReceiptCash.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                    }
                } else {
                    fillReport(prepareItemBarcodeReport(userData), userData.getReportPath() + "itemBarcodeList.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailBarcodeList(), "pdf");
                }
            } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(invInvoiceDTO.getScreenName())) {
                if (!invInvoiceDTO.getBarcodeSelect()) {
                    if (invInvoiceDTO.getTypeActivity() != null && invInvoiceDTO.getTypeActivity() == 1) {
                        fillReport(prepareInvSalesReport(invInvoiceDTO, userData), userData.getReportPath() + "salesReceiptCashCeramic.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                    } else {
                        fillReport(prepareInvSalesReport(invInvoiceDTO, userData), userData.getReportPath() + "salesReceiptCash.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                    }
                } else {
                    if (invInvoiceDTO.getPrintType() != null && invInvoiceDTO.getPrintType() == 3) {
                        fillReport(prepareInvSalesReport(invInvoiceDTO, userData), userData.getReportPath() + "InvReceiptCashPerfun.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                    } else {
                        fillReport(prepareInvSalesReport(invInvoiceDTO, userData), userData.getReportPath() + "InvReceiptCash.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                    }
                }
            } else if (ScreenNameClassEnum.INVPOSINVOICEFORM.equals(invInvoiceDTO.getScreenName())) {
                fillReport(prepareInvSalesReport(invInvoiceDTO, userData), userData.getReportPath() + "InvReceiptCash.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(invInvoiceDTO.getScreenName())) {
                fillReport(prepareReturnPurchaseReport(invInvoiceDTO, userData), userData.getReportPath() + "ReturnPurchaseReport.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVRETURNSALESFORM.equals(invInvoiceDTO.getScreenName())) {
            } else if (ScreenNameClassEnum.INVPOSREST.equals(invInvoiceDTO.getScreenName())) {
//                fillReport(prepareInvSalesReport(invInvoiceDTO, userData), userData.getReportPath() + "InvReceiptCashRest.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "print");
                if (invInvoiceDTO.getInventorySetupDTO().getPrintType() != null && invInvoiceDTO.getInventorySetupDTO().getPrintType() == 1) {
                    Map<Integer, List<InvDetailDTO>> groupMap = new HashMap<>();
                    List<InvDetailDTO> invDetailDTOList = new ArrayList<>();
                    if (invInvoiceDTO.getPrintType() == null || invInvoiceDTO.getPrintType() == 0) {
                        fillReport(prepareInvSalesReport(invInvoiceDTO, userData), userData.getReportPath() + "InvReceiptCashRest.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                    } else if (invInvoiceDTO.getPrintType() != null) {
                        for (InvDetailDTO invDetailDTO : invInvoiceDTO.getInvPurchaseInvoiceDetailList()) {
                            if (invDetailDTO.getItemId() != null && invDetailDTO.getItemId().getGroupId() != null
                                    && invDetailDTO.getItemId().getGroupId().getParent() != null && invDetailDTO.getItemId().getGroupId().getParent().getCode() != null
                                    && invDetailDTO.getItemId().getGroupId().getParent().getCode().equals(invInvoiceDTO.getPrintType())) {
                                if (groupMap.containsKey(invDetailDTO.getItemId().getGroupId().getParent().getId())) {
                                    invDetailDTOList = groupMap.get(invDetailDTO.getItemId().getGroupId().getParent().getId());
                                    invDetailDTOList.add(invDetailDTO);
                                    groupMap.put(invDetailDTO.getItemId().getGroupId().getParent().getId(), invDetailDTOList);
                                } else {
                                    invDetailDTOList = new ArrayList<>();
                                    invDetailDTOList.add(invDetailDTO);
                                    groupMap.put(invDetailDTO.getItemId().getGroupId().getParent().getId(), invDetailDTOList);
                                }
                            }
                            List<Integer> groupList = new ArrayList<Integer>(groupMap.keySet());
                            for (Integer group : groupList) {
                                List<InvDetailDTO> groupDetail = groupMap.get(group);
                                if (groupDetail != null && !groupDetail.isEmpty()) {
                                    fillReport(prepareInvSalesReport(invInvoiceDTO, userData), userData.getReportPath() + "InvReceiptCashRestKitchen.jasper", groupDetail, "pdf");
                                }
                            }
                        }
                    }

                } else {
                    fillReport(prepareInvSalesReport(invInvoiceDTO, userData), userData.getReportPath() + "InvReceiptCashRest.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                }
            } else if (ScreenNameClassEnum.INVPURCHASEORDERFORM.equals(invInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvPurchaseOrder(invInvoiceDTO, userData), userData.getReportPath() + "PurchaseOrderReport.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVSALESORDERFORM.equals(invInvoiceDTO.getScreenName())) {

            } else if (ScreenNameClassEnum.INVADDINGORDERFORM.equals(invInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvAddingOrder(invInvoiceDTO, userData), userData.getReportPath() + "AddingReport.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(invInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvAddingOrder(invInvoiceDTO, userData), userData.getReportPath() + "PermissionReport.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVQUOTATIONFORM.equals(invInvoiceDTO.getScreenName())) {

            } else if (ScreenNameClassEnum.INVRESERVATIONFORM.equals(invInvoiceDTO.getScreenName())) {

            } else if (ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(invInvoiceDTO.getScreenName())) {
                fillReport(prepareReportInvTransFer(invInvoiceDTO, userData), userData.getReportPath() + "invTransferReport.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
            } else if (ScreenNameClassEnum.INVUPDATE.equals(invInvoiceDTO.getScreenName())) {
                if (invInvoiceDTO.getBarcodeSelect()) {
                    fillReport(prepareInvUpdate(invInvoiceDTO, userData), userData.getReportPath() + "InvUpdate.jasper", invInvoiceDTO.getInvStripDetailList(), "pdf");
                } else {
                    fillReport(prepareInvUpdate(invInvoiceDTO, userData), userData.getReportPath() + "InvUpdate.jasper", invInvoiceDTO.getInvPurchaseInvoiceDetailList(), "pdf");
                }
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
            hashMap.put("customerBalance", invPurchaseInvoiceDTO.getCustomerBalance());
            hashMap.put("customerBalanceText", "اجمالى رصيد العميل");

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
            hashMap.put("phoneText", "تليفون");
            hashMap.put("address", userData.getUserBranch().getAddress1());
            hashMap.put("branchNameEn", userData.getUserBranch().getNameEn());
            hashMap.put("companyNameEn", userData.getCompany().getNameEn());
            hashMap.put("addressEn", userData.getUserBranch().getAddress2());
            hashMap.put("cashier", invPurchaseInvoiceDTO.getCreatedByName());

            if (invPurchaseInvoiceDTO.getTaxFlagFinal() != null && invPurchaseInvoiceDTO.getTaxFlagFinal()) {
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
            hashMap.put("totalValue", invPurchaseInvoiceDTO.getTotalNet());
            hashMap.put("totalWithTaxValue", invPurchaseInvoiceDTO.getTaxvalue().add(invPurchaseInvoiceDTO.getTotalNet()));
            hashMap.put("payedValue", invPurchaseInvoiceDTO.getPayed());
            hashMap.put("remainValue", invPurchaseInvoiceDTO.getRemain());
            hashMap.put("receiptNumberValue", invPurchaseInvoiceDTO.getSerial());
            if (invPurchaseInvoiceDTO.getInvInventoryId() != null) {
                hashMap.put("invName", invPurchaseInvoiceDTO.getInvInventoryId().getName());
            }

            if (invPurchaseInvoiceDTO.getOrganizationSiteId() != null) {
                hashMap.put("customerName", invPurchaseInvoiceDTO.getOrganizationSiteId().getName());
                hashMap.put("customerAddress", invPurchaseInvoiceDTO.getOrganizationSiteId().getAddress());
                hashMap.put("customerPhone", invPurchaseInvoiceDTO.getOrganizationSiteId().getMobile());
                hashMap.put("addressValue", invPurchaseInvoiceDTO.getOrganizationSiteId().getAddress());
            }

            hashMap.put("remarkValue", invPurchaseInvoiceDTO.getRemarks());
            hashMap.put("dateValue", invPurchaseInvoiceDTO.getDate());
            if (invPurchaseInvoiceDTO.getInvDelegatorId() != null) {
                hashMap.put("salesValue", invPurchaseInvoiceDTO.getInvDelegatorId().getName());
            }
//        hashMap.put("companyImage", getUserData().getImageReportPath());
            hashMap.put("discountValue", invPurchaseInvoiceDTO.getDiscountValue());
            hashMap.put("netValue", invPurchaseInvoiceDTO.getFinalNet());

            hashMap.put("size", "المقاس");
            hashMap.put("costText", "السعر");
            hashMap.put("size", "المقاس");
            hashMap.put("cartonsNumber", "عدد الكراتين");
            hashMap.put("add1", "إضافة1");
            hashMap.put("add2", "إضافة2");
            hashMap.put("disc1Text", "خصم1");
            hashMap.put("totalBeforefracture", "إجمالى قبل الكسر");
            hashMap.put("fractureNumber", "عدد الكسر");
            hashMap.put("valuefracture", "قيمة الكسر");
            hashMap.put("cartonsCapacity", "سعة الكرتون");
            hashMap.put("customerBalance", invPurchaseInvoiceDTO.getCustomerBalance());
            hashMap.put("customerBalanceText", "الرصيد الحالى ");
            BigDecimal oldCustomerBalance = invPurchaseInvoiceDTO.getCustomerBalance().subtract(invPurchaseInvoiceDTO.getTaxvalue().add(invPurchaseInvoiceDTO.getTotalNet()));
            hashMap.put("oldCustomerBalance",oldCustomerBalance.add(invPurchaseInvoiceDTO.getPayed().add(invPurchaseInvoiceDTO.getTaxvalue().add(invPurchaseInvoiceDTO.getTotalNet()))) );
            hashMap.put("oldCustomerBalanceText", "اجمالى الرصيد");
            hashMap.put("oldBalance", oldCustomerBalance.add(invPurchaseInvoiceDTO.getPayed()));
            hashMap.put("oldBalanceText", "رصيد قديم");

            return hashMap;
        } catch (Exception e) {
//            saveError(e, "SalesInvoiceFormMB", "prepareReport");
            return null;
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
            hashMap.put("size", "المقاس");
            hashMap.put("costText", "السعر");
            hashMap.put("size", "المقاس");
            hashMap.put("cartonsNumber", "عدد الكراتين");
            hashMap.put("add1", "إضافة1");
            hashMap.put("add2", "إضافة2");
            hashMap.put("disc1Text", "خصم1");
            hashMap.put("totalBeforefracture", "إجمالى قبل الكسر");
            hashMap.put("fractureNumber", "عدد الكسر");
            hashMap.put("valuefracture", "قيمة الكسر");
            hashMap.put("cartonsCapacity", "سعة الكرتون");
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
            hashMap.put("finalPriceText", "سعر نهائي");

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

    public HashMap prepareRscTender(RscTenderDTO rscTenderDTO, UserData userData, Boolean isPermission) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        Map<String, String> userDDs = userData.getUserDDs();

        hashMap.put("pageHeader", "العطاء");
        hashMap.put("workUnitName", "بند");
        hashMap.put("unitName", "الوحده");
        hashMap.put("quantityText", "الكميه");

        hashMap.put("serial", "رقم العطاء");
        hashMap.put("serialValue", rscTenderDTO.getSerial());
        hashMap.put("docNumber", "الرقم الدفتري");
        hashMap.put("docNumberValue", rscTenderDTO.getDocNumber());
        hashMap.put("status", "حالة العطاء");
        hashMap.put("statusValue", rscTenderDTO.getStatusText());

        hashMap.put("projectManager", "مدير المشروع");
        hashMap.put("projectManagerValue", rscTenderDTO.getProjectManager() == null ? null : rscTenderDTO.getProjectManager().getName());
        hashMap.put("proBusinessConsultant", "استشاري أعمال تخصصيه");
        hashMap.put("proBusinessConsultantValue", rscTenderDTO.getProfissionalBussinessConsultant() == null ? null : rscTenderDTO.getProfissionalBussinessConsultant().getName());
        hashMap.put("date", "تاريخ التحرير");
        hashMap.put("dateValue", rscTenderDTO.getDate());
        hashMap.put("customer", "المالك");
        hashMap.put("customerValue", rscTenderDTO.getCustomerId().getId());
        hashMap.put("generalConsultant", "الإستشاري العام");
        hashMap.put("generalConsultantValue", rscTenderDTO.getGeneralConsultant() == null ? null : rscTenderDTO.getGeneralConsultant().getName());
        hashMap.put("remark", "ملخص وصف العطاء");
        hashMap.put("remarkValue", rscTenderDTO.getRemark());

        return hashMap;
    }

    @Override
    public void printRscTender(RscTenderDTO rscTenderDTO, List<RscTenderBean> rscTenderBeanList, UserData userData) {
        try {
            fillReport(prepareRscTender(rscTenderDTO, userData, true), userData.getReportPath() + "RscTender.jasper", rscTenderBeanList, "pdf");
        } catch (JRException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //--------------------------------------------
    @Override
    public void printRscPriceTender(RscPriceTenderDTO rscPriceTenderDTO, List<RscPriceTenderBean> rscPriceTenderBeanList, UserData userData) {
        try {
            fillReport(prepareRscPriceTender(rscPriceTenderDTO, userData, true), userData.getReportPath() + "RscTenderPrice.jasper", rscPriceTenderBeanList, "pdf");
        } catch (JRException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap prepareRscPriceTender(RscPriceTenderDTO rscPriceTenderDTO, UserData userData, Boolean isPermission) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        Map<String, String> userDDs = userData.getUserDDs();

        hashMap.put("pageHeader", "تسعير العطاء");
        hashMap.put("workUnitName", "بند");
        hashMap.put("unitName", "الوحدة");
        hashMap.put("quantityUnits", "الكميه الأساسية");
        hashMap.put("priceUnit", "القيمة الأساسية");
        hashMap.put("totalUnits", "إجمالي تكلفة الوحدة");
        hashMap.put("totalQuantityUnit", "إجمالي الكمية تحليلي");
        hashMap.put("totalCost", "إجمالي التكلفة تحليلي");
        hashMap.put("quantityIn", "كمية بند العطاء");
        hashMap.put("profitValue", "قيمة الربح");
        hashMap.put("profitPercentage", "نسبة الربح");
        hashMap.put("priceUnitSell", "سعر البيع");
        hashMap.put("totalProfit", "قيمة الربح");
        hashMap.put("totalSell", "إجمالي البيع");
        hashMap.put("remark", "ملاحظات");

        hashMap.put("serial", "رقم التسعير");
        hashMap.put("serialValue", rscPriceTenderDTO.getSerial());
        hashMap.put("docNumber", "الرقم الدفتري");
        hashMap.put("docNumberValue", rscPriceTenderDTO.getDocNumber());
        hashMap.put("Date", "التاريخ");
        hashMap.put("dateValue", rscPriceTenderDTO.getDate());

        hashMap.put("status", "حالة التسعير");
        if (rscPriceTenderDTO.getStatus() == 0 || rscPriceTenderDTO.getStatus() == null) {
            hashMap.put("statusValue", "لم يتم");
        } else {
            hashMap.put("statusValue", "تم");
        }

        return hashMap;
    }

    @Override
    public void printRscQuotationPriceTender(RscQotationPriceTenderDTO rscQuotationPriceTenderDTO, List<RscQuotationPriceTenderBean> RscQuotationPriceTenderBeanList, UserData userData) {
        try {
            fillReport(prepareRscQotationPriceTender(rscQuotationPriceTenderDTO, userData, true), userData.getReportPath() + "RscQotationPriceTender.jasper", RscQuotationPriceTenderBeanList, "pdf");
        } catch (JRException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap prepareRscQotationPriceTender(RscQotationPriceTenderDTO rscQuotationPriceTenderDTO, UserData userData, Boolean isPermission) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        Map<String, String> userDDs = userData.getUserDDs();

        hashMap.put("pageHeader", "عرض سعر");
        hashMap.put("workUnitName", "بند");
        hashMap.put("unitName", "الوحدة");
        hashMap.put("quantity", "الكميه الأساسية");
        hashMap.put("price", "سعر البيع");
        hashMap.put("totalUnits", "إجمالي تكلفة الوحدة");
        hashMap.put("remark", "ملاحظات");

        hashMap.put("serial", "رقم التسعير");
        hashMap.put("serialValue", rscQuotationPriceTenderDTO.getSerial());
        hashMap.put("docNumber", "الرقم الدفتري");
        hashMap.put("docNumberValue", rscQuotationPriceTenderDTO.getDocNumber());
        hashMap.put("Date", "التاريخ");
        hashMap.put("dateValue", rscQuotationPriceTenderDTO.getDate());

        hashMap.put("status", "حالة التسعير");
        if (rscQuotationPriceTenderDTO.getStatus() == 0 || rscQuotationPriceTenderDTO.getStatus() == null) {
            hashMap.put("statusValue", "لم يتم");
        } else {
            hashMap.put("statusValue", "تم");
        }

        return hashMap;
    }

    @Override
    public void printRscExtractRegister(RscExtractRegisterDTO rscExtractRegisterDTO, List<RscExtractRegisteringBean> rscExtractionDTOBeanList, UserData userData, String url) {
        try {
            if (url.contains("rscExtractSummary")) {
                fillReport(prepareRscExtractRegister(rscExtractRegisterDTO, userData, true), userData.getReportPath() + "rscExtractsReportSummary.jasper", rscExtractionDTOBeanList, "pdf");
            } else {
                fillReport(prepareRscExtractRegister(rscExtractRegisterDTO, userData, true), userData.getReportPath() + "rscExtractsReport.jasper", rscExtractionDTOBeanList, "pdf");
            }
        } catch (JRException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap prepareRscExtractRegister(RscExtractRegisterDTO rscExtractRegisterDTO, UserData userData, Boolean isPermission) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        Map<String, String> userDDs = userData.getUserDDs();

        hashMap.put("header2", "طباعة مستخلص");
        hashMap.put("serialTitle", "المستخلص");
        hashMap.put("serialToLabel", "رقم المستخلص");
        hashMap.put("serialToValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getSerial());
        hashMap.put("documentNumberToLabel", "الرقم الدفتري");
        hashMap.put("documentNumberToValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getDocNumber());
        hashMap.put("dateToLabel", "تاريخ التحرير");
        hashMap.put("dateToValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getDate());
        hashMap.put("receivedDateToLabel", "تاريخ التسليم للعميل");
        hashMap.put("receivedDateToValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getReceivedDate());
        hashMap.put("profissionaiAcceptedDateToLabel", "تاريخ موافقة الاستشارى");
        hashMap.put("profissionaiAcceptedDateToValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getProfissionaiAcceptedDate());
        hashMap.put("clientAcceptedDateToLabel", "تاريخ موافقة العميل");
        hashMap.put("clientAcceptedDateToValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getClientAcceptedDate());
        hashMap.put("ProjectManagerToLabel", "مدير المشروع");
        hashMap.put("ProjectManagerToValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getTenderId() == null) ? null : rscExtractRegisterDTO.getTenderId().getProjectManager());
        hashMap.put("generalConsultantToLabel", "الاستشارى العام");
        hashMap.put("generalConsultantToValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getTenderId() == null) ? null : rscExtractRegisterDTO.getTenderId().getGeneralConsultant());
        hashMap.put("ProfessionalBusinessConsultantToLabel", "استشارى أعمال تخصصية");
        hashMap.put("ProfessionalBusinessConsultantToValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getTenderId() == null) ? null : rscExtractRegisterDTO.getTenderId().getProfissionalBussinessConsultant());
        hashMap.put("siteIdToLabel", "المقاول");
        hashMap.put("siteIdToValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getContractId() == null) ? null : rscExtractRegisterDTO.getContractId().getSiteId().getName());
        hashMap.put("remarksToLabel", "البيان");
        hashMap.put("remarksToValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getRemarks());
        hashMap.put("tenderTitle", "العطاء");
        hashMap.put("tenderIdToLabel", "رقم العطاء");
        hashMap.put("tenderIdToValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getTenderId() == null) ? null : rscExtractRegisterDTO.getTenderId().getSerial());
        hashMap.put("tenderDateToLabel", "تاريخ العطاء");
        hashMap.put("tenderDateToValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getTenderId() == null) ? null : rscExtractRegisterDTO.getTenderId().getDate());
        hashMap.put("tenderDescriptionToLabel", "وصف العطاء");
        hashMap.put("tenderDescriptionToValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getTenderId() == null) ? null : rscExtractRegisterDTO.getTenderId().getRemark());
        hashMap.put("contractTitle", "العقد");
        hashMap.put("contractIdToLabel", "رقم العقد");
        hashMap.put("contractIdToValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getContractId() == null) ? null : rscExtractRegisterDTO.getContractId().getId());
        hashMap.put("contractDocumentNumberToLabel", "الرقم الدفترى للعقد");
        hashMap.put("contractDocumentNumberToValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getContractId() == null) ? null : rscExtractRegisterDTO.getContractId().getDocumentNumber());
        hashMap.put("netExtractPriceToLabel", "اجمالى المستخلصات");
        hashMap.put("netExtractPriceToValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getNetExtractPrice());
        hashMap.put("contractTotalToLabel", "اجمالى فيمة العقد");
        hashMap.put("contractTotalToValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getContractId() == null) ? null : rscExtractRegisterDTO.getContractId().getContractTotal());
        hashMap.put("remainToLabel", "الباقى من المستخلص");
        hashMap.put("remainToValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getRemain());
        hashMap.put("totalDetailNetLabel", "الإجمالى");
        hashMap.put("totalDetailNetValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getTotalDetailNet().setScale(2, RoundingMode.HALF_UP));
        hashMap.put("totalAccrualNetvalueLabel", "اجمالي الاستحقاق");
        hashMap.put("totalAccrualNetvalue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getTotalAccrualNetvalue().setScale(2, RoundingMode.HALF_UP));
        hashMap.put("totalDeductionNetvalueLabel", "اجمالي الاستقطاعات");
        hashMap.put("totalDeductionNetvalue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getTotalDeductionNetvalue().setScale(2, RoundingMode.HALF_UP));
        hashMap.put("totalFinalLabel", "صافي المستخلص");
        hashMap.put("totalFinalValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getTotalFinal().setScale(2, RoundingMode.HALF_UP));
        hashMap.put("customerLabel", "العميل");
        hashMap.put("customerValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getCustomerId() == null) ? null : rscExtractRegisterDTO.getCustomerId().getName());
        hashMap.put("projectLabel", "المشروع");
        hashMap.put("projectValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getInstProjectGuideDTO() == null) ? null : rscExtractRegisterDTO.getInstProjectGuideDTO().getName());
        hashMap.put("percentageLabel", "النسبه");
        hashMap.put("percentageValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getPercentage());
        hashMap.put("SubContractorLabel", "المقاول الباطن");
        hashMap.put("SubContractorValue", rscExtractRegisterDTO == null || (rscExtractRegisterDTO.getSiteId() == null) ? null : rscExtractRegisterDTO.getSiteId().getName());
        hashMap.put("totalBeforPercentageLabel", "الاجمالي قبل النسبه");
        hashMap.put("totalBeforPercentageValue", rscExtractRegisterDTO == null ? null : rscExtractRegisterDTO.getTotalBeforePercentage());
        hashMap.put("valueLabel", "القيمة");
        hashMap.put("value", rscExtractRegisterDTO != null && rscExtractRegisterDTO.getTotalPercentage() != null ? rscExtractRegisterDTO.getTotalPercentage().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
        hashMap.put("itemName", "البند");
        hashMap.put("code", "الكود");
        hashMap.put("unitName", "الوحده");
        hashMap.put("contractQuantity", "الكمية بالعقد");
        hashMap.put("oldQuantity", "الكمية السابقة");
        hashMap.put("currentQuantity", "الكمية الحالية");
        hashMap.put("executeQuantity", "الكمية المنفذه");
        hashMap.put("priceSell", "سعر البيع");
        hashMap.put("currentValue", "القيمة الحالية");
        hashMap.put("calculateRampPercentage", "قيمة التعلية");
        hashMap.put("rampPercentage", "نسبة التعلية");
        hashMap.put("oldRamp", "تعلية سابقة");
        hashMap.put("returnRamp", "رد تعلية سابقة");
        hashMap.put("netTotalReturnRamp", "صافى التعلية بعد الرد");
        hashMap.put("valueAfterDiscount", "الاجمالى");
        hashMap.put("discountValue", "خصم قيمة");
        hashMap.put("discountPercentage", "خصم نسبة");
        hashMap.put("netCost", "صافى بند المستخلص");
        hashMap.put("remarks", "الملاحظات ");
        hashMap.put("SecretaryOfTheTreasuryText", "المحاسب");
        hashMap.put("accountManager", "مدير الحسابات ");
        hashMap.put("confirmationText", "إعتماد ");

        return hashMap;
    }

    //--------------------------------------------
    @Override
    public void printRscRestrictionQuantities(RscRestrictionQuantitiesDTO rscRestrictionQuantitiesDTO, List<RscRestrictionQuantitiesBean> rscRestrictionQuantitiesBeanList, UserData userData) {
        try {
            fillReport(prepareRscRestrictionQuantities(rscRestrictionQuantitiesDTO, userData, true), userData.getReportPath() + "rscRestrictionQuantitiesReport.jasper", rscRestrictionQuantitiesBeanList, "pdf");
        } catch (JRException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap prepareRscRestrictionQuantities(RscRestrictionQuantitiesDTO rscRestrictionQuantitiesDTO, UserData userData, Boolean isPermission) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        Map<String, String> userDDs = userData.getUserDDs();

        hashMap.put("header2", "تقرير حصر الكميات");
        hashMap.put("serialToLabel", "الرقم");
        hashMap.put("serialToValue", rscRestrictionQuantitiesDTO.getSerial());
        hashMap.put("dateToLabel", "التاريخ");
        hashMap.put("dateToValue", rscRestrictionQuantitiesDTO.getDate());
        hashMap.put("siteIdToLabel", "المقاول");
        hashMap.put("siteIdToValue", rscRestrictionQuantitiesDTO == null ? null : rscRestrictionQuantitiesDTO.getContractId().getSiteId().getName());
        hashMap.put("contractIdToLabel", "رقم العقد");
        hashMap.put("contractIdToValue", rscRestrictionQuantitiesDTO == null ? null : rscRestrictionQuantitiesDTO.getContractId().getId());
        hashMap.put("documentNumberToLabel", "الرقم الدفتري للعقد");
        hashMap.put("documentNumberToValue", rscRestrictionQuantitiesDTO == null ? null : rscRestrictionQuantitiesDTO.getContractId().getDocumentNumber());
        hashMap.put("contractTotalToLabel", "إجمالى قيمة التعاقد");
        hashMap.put("contractTotalToValue", rscRestrictionQuantitiesDTO == null ? null : rscRestrictionQuantitiesDTO.getContractId().getContractTotal());
        hashMap.put("itemName", "البند");
        hashMap.put("unitName", "الوحدة");
        hashMap.put("contractQuantity", "الكمية بالعقد");
        return hashMap;
    }

    //--------------------------------------------------------------------------------
    @Override
    public void printRscContractRegister(RscContractRegisterDTO rscContractRegisterDTO, List<RscContractRegisterBean> rscContractRegisterBeanList, UserData userData) {
        try {
            fillReport(prepareRscContractRegister(rscContractRegisterDTO, userData, true), userData.getReportPath() + "rscContractRegisterReport.jasper", rscContractRegisterBeanList, "pdf");
        } catch (JRException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap prepareRscContractRegister(RscContractRegisterDTO rscContractRegisterDTO, UserData userData, Boolean isPermission) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        Map<String, String> userDDs = userData.getUserDDs();

        Locale arabicLocale = new Locale.Builder().setLanguageTag("ar-SA-u-nu-arab").build();

        LocalDate localDate = rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getDate().toInstant().atZone(ZoneId.systemDefault().systemDefault()).toLocalDate();

        hashMap.put("header2", rscContractRegisterDTO.getRscQotationPriceTenderId().getRscPriceTenderId().getRscTenderDTO().getRemark());
        hashMap.put("dateToLabel", "الموافق");
        hashMap.put("dateToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getDate());
        hashMap.put("serialToLabel", "رقم العقد:");
        hashMap.put("serialToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getSerial());
        hashMap.put("documentNumberToLabel", "الرقم الدفتري:");
        hashMap.put("documentNumberToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getDocumentNumber());
        hashMap.put("statusToLabel", "حاله العقد:");
        if (rscContractRegisterDTO.getStatus() == 0 || rscContractRegisterDTO.getStatus() == null) {
            hashMap.put("statusToValue", "لم يتم");
        } else {
            hashMap.put("statusToValue", "تم");
        }
        hashMap.put("initialReceivedDateToLabel", "تاريخ الإستلام الإبتدائى:");
        hashMap.put("initialReceivedDateToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getInitialReceivedDate());
        hashMap.put("finallReceivedDateToLabel", "تاريخ الإستلام النهائى:");
        hashMap.put("finallReceivedDateToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getFinallReceivedDate());
        hashMap.put("contractTotalToLabel", "إجمالى قيمة التعاقد:");
        hashMap.put("contractTotalToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getContractTotal());
        hashMap.put("initialValueToLabel", "قيمة الدفعة المقدمة:");
        hashMap.put("initialValueToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getInitialValue());
        hashMap.put("initialPercentageToLabel", "نسبة الدفعة المقدمة%:");
        hashMap.put("initialPercentageToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getInitialPercentage());
        hashMap.put("siteIdToLabel", "مقاول الباطن:");
        hashMap.put("siteIdToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getSiteId().getName());
        hashMap.put("dayToLabel", "انه في اليوم");
        hashMap.put("dayToValue", rscContractRegisterDTO == null ? null : localDate.format(DateTimeFormatter.ofPattern("EEEE").withLocale(arabicLocale)));
        hashMap.put("addressToLabel", "عنوان المقاول:");
        hashMap.put("addressToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getSiteId().getAddress());
        hashMap.put("telephoneToLabel", "تليفون المقاول:");
        hashMap.put("telephoneToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getSiteId().getTelephone());
        hashMap.put("phoneToLabel", "جوال المقاول:");
        hashMap.put("phoneToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getSiteId().getMobile());
        hashMap.put("faxToLabel", "فاكس المقاول:");
        hashMap.put("faxToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getSiteId().getFax());
        hashMap.put("personResponsibleToLabel", "الشخص المسئول للمقاول:");
        hashMap.put("personResponsibleToValue", rscContractRegisterDTO == null ? null : rscContractRegisterDTO.getSiteId().getPersonResponsible());
        hashMap.put("contractItem", "بنود العقد:");
        hashMap.put("itemName", "البند");
        hashMap.put("unitName", "الوحدة");
        hashMap.put("quantity", "الكمية");
        hashMap.put("price", "الفئة تحليلى");
        hashMap.put("total", "القيمة");
        hashMap.put("remark", "الملاحظات");

        return hashMap;
    }

    //---------------------------------------------------------------------------------------------------------------
    @Override
    public void printRscExtractJournalReport(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, List<rscExtractJournalReportPrintEntity> printEntityList, UserData userData) {
        try {
            fillReport(prepareRscExtractJournalReport(contractExtractionWorkunitSearchBean, userData), userData.getReportPath() + "rscExtractjournal.jasper", printEntityList, "pdf");
        } catch (JRException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap prepareRscExtractJournalReport(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, UserData userData) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        Map<String, String> userDDs = userData.getUserDDs();

        hashMap.put("header2", "تقرير يومية المشاريع");

//            hashMap.put("ownerText", "المالك");
//            hashMap.put("ownerValue",entity.getOwnerName());
//        
//        if (contractExtractionWorkunitSearchBean.getContract() != null) {
//            hashMap.put("contractText", "العقد");
//            hashMap.put("contractValue",entity.getContractId());
//        }
//        if (contractExtractionWorkunitSearchBean.getWorkUnitList() != null) {
//            hashMap.put("WorkUnitText", "البند");
//            hashMap.put("WorkUnitValue", entity.getWorkUnitName());
//        }
//        if (contractExtractionWorkunitSearchBean.getSiteList() != null) {
//            hashMap.put("contractorText", "المقاول");
//            hashMap.put("contractorValue", entity.getSiteName());
//        }
        if (contractExtractionWorkunitSearchBean.getFromDate() != null) {
            hashMap.put("DateFromLabel", "من تاريخ");
            hashMap.put("DateFromValue", contractExtractionWorkunitSearchBean.getFromDate());
        }
        if (contractExtractionWorkunitSearchBean.getToDate() != null) {
            hashMap.put("DateToLabel", "الي تاريخ");
            hashMap.put("DateToValue", contractExtractionWorkunitSearchBean.getToDate());
        }
        hashMap.put("currentQtyText", "الكمية الحالية");
        hashMap.put("workUnitDetail", "البند");
        hashMap.put("dateText", "التاريخ");
        return hashMap;
    }

    //--------------------------------------------
    @Override
    public void printCompletionRateReportMB(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, completionRateReportEntity entity, List<completionRateReportEntity> printEntityList, UserData userData) {
        try {
            fillReport(prepareCompletionRateReport(contractExtractionWorkunitSearchBean, entity, userData), userData.getReportPath() + "RsccompletionRateReport.jasper", printEntityList, "pdf");
        } catch (JRException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RscTenderDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap prepareCompletionRateReport(RscContractExtractionWorkunitSearchBean contractExtractionWorkunitSearchBean, completionRateReportEntity entity, UserData userData) {

        HashMap hashMap = new HashMap();

        prepareReportGeneral(hashMap, userData);

        Map<String, String> userDDs = userData.getUserDDs();

        hashMap.put("pageHeader", "تقرير نسبة الإنجاز");

        hashMap.put("ownerText", "المالك");
        hashMap.put("ownerValue", (entity.getOwnerName() != null ? entity.getOwnerName() : ""));

        if (contractExtractionWorkunitSearchBean.getContract() != null) {
            hashMap.put("contractText", "العقد");
            hashMap.put("contractValue", contractExtractionWorkunitSearchBean.getContract().getId());
        }
        if (contractExtractionWorkunitSearchBean.getWorkUnitList() != null) {
            hashMap.put("WorkUnitText", "البند");
            hashMap.put("WorkUnitValue", entity.getWorkUnitName());
        }
        if (contractExtractionWorkunitSearchBean.getSiteList() != null) {
            hashMap.put("contractorText", "المقاول");
            hashMap.put("contractorValue", entity.getSiteName());
        }

        hashMap.put("currentQtyText", "إجمالي المستخلص");
        hashMap.put("contractorNameText", "اسم المقاول");
        hashMap.put("workUnitDetail", "اسم اليند");
        hashMap.put("contractQtyText", "الكمية في العقد");
        hashMap.put("completionRateText", "نسبة الإنجاز");
        return hashMap;
    }

}
