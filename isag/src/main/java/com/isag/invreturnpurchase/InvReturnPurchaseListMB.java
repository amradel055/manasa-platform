/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invreturnpurchase;

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
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.InvReturnPurchaseDetailService;
import com.isag.businessservice.InvReturnPurchaseService;
import com.isag.businessservice.InvoiceNetService;
import com.isag.entity.InvReturnPurchase;
import com.isag.entity.InvReturnPurchaseDetail;
import com.isag.entiy.InvReturnDetailEntity;
import com.isag.entiy.InvReturnPurchaseEntity;
import com.isag.invPurchaseInvoice.InvPurchaseInvoiceFormMB;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
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
@ManagedBean(name = "invReturnPurchaseListMB")
@ViewScoped
public class InvReturnPurchaseListMB extends BaseListBean {

    private UserData userData;
    private String screenMode;
    private Integer branchId;
    private Integer companyId;
    private BigDecimal totalNet = BigDecimal.ZERO;
    private Integer invPurchaseInvoiceIdSeclected;
    private InvReturnPurchaseEntity invReturnPurchaseEntity;
    private InvReturnPurchase invPurchaseInvoice;
    private List<InvReturnPurchase> invReturnPurchaseList;
    private InvPurchaseInvoiceEntity invPurchaseInvoiceEntity;
    private InvReturnPurchase invReturnPurchaseSelected;
    private List<InvReturnPurchaseDetail> invReturnPurchaseDetailList;
    private List<InvReturnDetailEntity> invReturnDetailEntityList;
    private List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList;

    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvoiceNetService invoiceNetService;
    @EJB
    private InvReturnPurchaseService invReturnPurchaseService;
    @EJB
    private InvReturnPurchaseDetailService invReturnPurchaseDetailService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvReturnPurchaseListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            userData = new UserData();
            invPurchaseInvoice = new InvReturnPurchase();
            invReturnPurchaseList = new ArrayList<>();
            invReturnPurchaseDetailList = new ArrayList<>();
            invPurchaseInvoiceEntity = new InvPurchaseInvoiceEntity();
            invReturnPurchaseEntity = new InvReturnPurchaseEntity();
            invReturnDetailEntityList = new ArrayList<>();
            invReturnPurchaseSelected = new InvReturnPurchase();
            invPurchaseInvoiceEntityList = new ArrayList<>();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setBranchId(userData.getUserBranch().getId());
            setCompanyId(userData.getCompany().getId());

            invReturnPurchaseList = invReturnPurchaseService.getALLInvReturnPurchaseByBranchId(branchId, Boolean.FALSE);
//        invReturnPurchaseDetailList = invReturnPurchaseDetailService.getAllReturnPurchaseDetailsByReturnPurchaseId(invReturnPurchaseSelected.getId());
        } catch (Exception e) {
            saveError(e, "InvReturnPurchaseListMB", "load");
        }
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../invreturnpurchase/invreturnpurchaseform.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "InvReturnPurchaseListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (invPurchaseInvoiceIdSeclected != null && invPurchaseInvoiceIdSeclected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invreturnPurchaseInvoiceIdSeclected", invPurchaseInvoiceIdSeclected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../invreturnpurchase/invreturnpurchaseform.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvReturnPurchaseListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();

            invPurchaseInvoice = new InvReturnPurchase();
            invPurchaseInvoice.setId(invReturnPurchaseSelected.getId());

            try {
                invReturnPurchaseService.deleteInvReturnPurchase(invPurchaseInvoice);
                invReturnPurchaseList.remove(invReturnPurchaseSelected);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        } catch (Exception e) {
            saveError(e, "InvReturnPurchaseListMB", "delete");
        }
    }

    public String print() {
        try {
            if (invReturnPurchaseSelected != null && prepareListToPrint(invReturnPurchaseSelected.getId())) {
                // exit();
                return "invreturnpurchaselist";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvReturnPurchaseListMB", "print");
            return null;
        }
    }

    private boolean prepareListToPrint(Integer returnDetailId) {

        if (returnDetailId != null) {
            invReturnPurchaseDetailList = new ArrayList<>();
            invReturnDetailEntityList = new ArrayList<>();
            invReturnPurchaseDetailList = invReturnPurchaseDetailService.getAllReturnPurchaseDetailsByReturnPurchaseId(returnDetailId);

            InvReturnDetailEntity details;
            for (InvReturnPurchaseDetail detailsEntity : invReturnPurchaseDetailList) {
                details = new InvReturnDetailEntity();
                details.setSerial(detailsEntity.getSerial());
                details.setItemCode(detailsEntity.getItemBarcode());
                details.setItemName(detailsEntity.getItemId().getName());
                details.setUnitName(detailsEntity.getItemId().getUnitId().getName());
                details.setScrewing(detailsEntity.getScrewing());
                details.setQuantity(detailsEntity.getQuantity());
                details.setCost(detailsEntity.getUnitPrice());
                details.setNet(detailsEntity.getNet());
                totalNet = (totalNet.add(detailsEntity.getNet() != null ? detailsEntity.getNet() : BigDecimal.ZERO)).setScale(2, BigDecimal.ROUND_UP);
                invReturnDetailEntityList.add(details);
            }
            try {
                fillReport(prepareReport(), getUserData().getReportPath() + "ReturnPurchaseReport.jasper", invReturnDetailEntityList, "pdf");
            } catch (JRException | IOException ex) {
                Logger.getLogger(InvPurchaseInvoiceFormMB.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        }
        return false;

    }

    public HashMap prepareReport() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            HashMap hashMap = new HashMap();

            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
            hashMap.put("companyName", getUserData().getCompany().getName());

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

            hashMap.put("receiptNumberValue", invReturnPurchaseEntity.getSerial());
            hashMap.put("supplierCode", invReturnPurchaseEntity.getSupplierId() != null ? invReturnPurchaseEntity.getSupplierId().getCode() : null);
            hashMap.put("supplierName", invReturnPurchaseEntity.getSupplierId() != null ? invReturnPurchaseEntity.getSupplierId().getName() : null);
            hashMap.put("dateValue", invReturnPurchaseEntity.getDate());
            hashMap.put("supplierReceiptValue", invReturnPurchaseEntity.getPurchaseInvoice() != null ? invReturnPurchaseEntity.getPurchaseInvoice().getSerial() : null);
            hashMap.put("secondDateValue", invReturnPurchaseEntity.getPurchaseInvoice() != null ? invReturnPurchaseEntity.getPurchaseInvoice().getDate() : null);
            hashMap.put("remarkValue", invReturnPurchaseEntity.getRemark());

            hashMap.put("netValue", totalNet);
            hashMap.put("reportDate", new Date());

            return hashMap;
        } catch (Exception e) {
            saveError(e, "InvReturnPurchaseListMB", "prepareReport");
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
            saveError(e, "InvReturnPurchaseListMB", "fillReport");
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

    public InvReturnPurchase getInvReturnPurchaseSelected() {
        return invReturnPurchaseSelected;
    }

    public void setInvReturnPurchaseSelected(InvReturnPurchase invReturnPurchaseSelected) {
        this.invReturnPurchaseSelected = invReturnPurchaseSelected;
    }

    public InvReturnPurchase getInvPurchaseInvoice() {
        return invPurchaseInvoice;
    }

    public void setInvPurchaseInvoice(InvReturnPurchase invPurchaseInvoice) {
        this.invPurchaseInvoice = invPurchaseInvoice;
    }

    public List<InvReturnPurchase> getInvReturnPurchaseList() {
        return invReturnPurchaseList;
    }

    public void setInvReturnPurchaseList(List<InvReturnPurchase> invReturnPurchaseList) {
        this.invReturnPurchaseList = invReturnPurchaseList;
    }
}
