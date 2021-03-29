/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.glBankTransaction;

import com.toby.businessservice.GlBankTransactionDetailsService;
import com.toby.businessservice.GlBankTransactionService;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entiy.GlBankTransactionDetailEntity;
import com.toby.entiy.GlBankTransactionEntity;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
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
 * @author hq002
 */
@Named(value = "glBankTransactionListMB")
@ViewScoped
public class glBankTransactionListMB extends BaseListBean {

    private UserData userData;
    private GlBankTransactionEntity glBankTransactionEntity;
    private GlBankTransactionDetail glBankTransactionDetail;
    private GlBankTransactionDetailEntity glBankTransactionDetailEntity;
    private Collection<GlBankTransactionDetailEntity> glBankTransactionDetailEntityList;

    private GlBankTransactionEntity glBankTransactionEntitySelected;
    private List<GlBankTransactionEntity> glBankTransactionEntityList;
    private GlBankTransaction glBankTransaction;
    private List<GlBankTransaction> glBankTransactionList;
    private GlBankTransactionEntity glBankTransactionSelected;
    private Integer index = 0;

    @EJB
    private GlBankTransactionService glBankTransactionService;

    @EJB
    private GlBankTransactionDetailsService glBankTransactionDetailsService;

    @Override
    @PostConstruct
    public void init() {
        try {
        glBankTransactionEntityList = new ArrayList<>();
        glBankTransactionList = new ArrayList<>();
        glBankTransaction = new GlBankTransaction();
        load();
        } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        glBankTransactionList = glBankTransactionService.getALLGlBankTransactionRecievableByBranchId(userData.getUserBranch().getId(),2);
        for (GlBankTransaction gbt : glBankTransactionList) {
            glBankTransactionDetailEntityList = new ArrayList<>();
            glBankTransactionEntity = new GlBankTransactionEntity();
            glBankTransactionDetailEntity = new GlBankTransactionDetailEntity();

            glBankTransactionEntity.setId(gbt.getId());
            glBankTransactionEntity.setSerial(gbt.getSerial() != null ? gbt.getSerial() : null);
            glBankTransactionEntity.setDate(gbt.getDate() != null ? gbt.getDate() : null);
            glBankTransactionEntity.setPaymentType(gbt.getPaymentType() != null ? gbt.getPaymentType() : 0);
            glBankTransactionEntity.setRemark(gbt.getRemark() != null ? gbt.getRemark() : null);
            glBankTransactionEntity.setPostFlag(gbt.getPostFlag());
            glBankTransactionDetail = glBankTransactionDetailsService.getGlBankTransactionDetailByGlBankTransactionId(gbt.getId());

            glBankTransactionDetailEntity.setId(glBankTransactionDetail.getId());
            glBankTransactionDetailEntity.setBankFrom(glBankTransactionDetail.getBankIdFrom() != null ? glBankTransactionDetail.getBankIdFrom() : null);
            glBankTransactionDetailEntity.setBankTo(glBankTransactionDetail.getBankIdTo() != null ? glBankTransactionDetail.getBankIdTo() : null);
            glBankTransactionDetailEntity.setValue(glBankTransactionDetail.getValue() != null ? glBankTransactionDetail.getValue() : null);
            glBankTransactionDetailEntity.setValueLocal(glBankTransactionDetail.getValueLocal() != null ? glBankTransactionDetail.getValueLocal() : null);
            glBankTransactionDetailEntityList.add(glBankTransactionDetailEntity);

            glBankTransactionEntity.setGlBankTransactionDetailCollection(glBankTransactionDetailEntityList);

            glBankTransactionEntityList.add(glBankTransactionEntity);
        }
         } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "load");
        }
    }

    @Override
    public void delete() {
        try{
        if (glBankTransactionEntitySelected.getPostFlag() == null || (glBankTransactionEntitySelected.getPostFlag() != null && glBankTransactionEntitySelected.getPostFlag() != 1)) {
            Map<String, String> userDDs = userData.getUserDDs();

            Integer glBankTransactionId = glBankTransactionEntitySelected.getId() != null ? glBankTransactionEntitySelected.getId() : null;
            glBankTransaction = new GlBankTransaction();
            glBankTransaction.setId(glBankTransactionId);

            try {
                glBankTransactionService.deleteGlBankTransaction(glBankTransaction);
                glBankTransactionEntityList.remove(glBankTransactionEntitySelected);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "هذا السند مرحل"));
        }
         } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "delete");
        }
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("ScreenMode", "Add");
        exit("../glBankTransaction/glBankTransactionForm.xhtml");
        return "";
        } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
        if (glBankTransactionSelected != null && glBankTransactionSelected.getId() > 0) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("glBankTransactionSelected", glBankTransactionSelected.getId());
            context.getSessionMap().put("ScreenMode", "Edit");
            exit("../glBankTransaction/glBankTransactionForm.xhtml");
            return "";
        } else {
            return "";
        }
         } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "goToEdit");
            return null;
        }
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try{

        glBankTransactionEntity = (GlBankTransactionEntity) actionEvent.getComponent().getAttributes().get("varValue");

        List<GlBankTransactionEntity> gbtdes = new ArrayList<>();
        gbtdes.add(glBankTransactionEntity);
        if (glBankTransactionEntity != null && glBankTransactionEntity.getId() != null) {
            fillReport(prepareReport(), getUserData().getReportPath() + "glBankTransaction.jasper", gbtdes, "pdf");
        }
        } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "exportPDF");
        }
    }

    public HashMap prepareReport() {
        try {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("companyName", getUserData().getCompany().getName());
        hashMap.put("amountText", userDDs.get("AMOUNT"));
        hashMap.put("dateText", userDDs.get("DATE"));
        //  hashMap.put("companyImage", getUserData().getImageReportPath());

        // hashMap.put("reportNameText", "سند قبض ");
        hashMap.put("recipientText", "المستلم");
        hashMap.put("SecretaryOfTheTreasuryText", "امين الخزينة");
        hashMap.put("recievedFromText", "استلمنا من");
        hashMap.put("signatureText", "التوقيع");
        hashMap.put("valueFromText", "وذلك قيمة");
        hashMap.put("documentNumberText", "رقم السند");
        //hashMap.put("chequeDateText", "تاريخ الشيك");
        //hashMap.put("chequeDateValue", glBankTransactionEntity.getDate() != null ? glBankTransactionEntity.getDate() : null);
        //hashMap.put("deservedDateText", "تاريخ الاستحقاق");
        // hashMap.put("deservedDateValue", glBankTransactionEntityMapper.getChequeDueDate() != null ? glBankTransactionEntityMapper.getChequeDueDate() : null);

        hashMap.put("rateText", "المعدل");
        hashMap.put("rateValue", glBankTransactionEntity.getRate());
        hashMap.put("amountInRateText", userDDs.get("TOTAL"));
        hashMap.put("amountInRateValue", glBankTransactionEntity.getValueLocal());

        hashMap.put("dateValue", glBankTransactionEntity.getDate());
        hashMap.put("documentNumberValue", glBankTransactionEntity.getSerial() != null ? glBankTransactionEntity.getSerial() : null);
        //hashMap.put("recipientValue", glBankTransactionEntity.getGlBank().getName());
        hashMap.put("valueFromValue", glBankTransactionEntity.getRemark());

        /*   GlAccount recipientaccount = new GlAccount();
        recipientaccount = glAccountService.findGlAccount(GlBankTransactionDetailEntity.getAccountDebitId());
        hashMap.put("recipientValue", recipientaccount != null ? recipientaccount.getName() : null);

        GlAccount recievedaccount = new GlAccount();
        recievedaccount = glAccountService.findGlAccount(GlBankTransactionDetailEntity.getAccountCreditId());
        hashMap.put("recievedFromValue", recievedaccount != null ? recievedaccount.getName() : null);*/
        glBankTransactionDetail = glBankTransactionDetailsService.getGlBankTransactionDetailByGlBankTransactionId(glBankTransactionEntity.getId());
        hashMap.put("amountValue", glBankTransactionDetail.getValue());
        hashMap.put("amountValueText", Tafqeet.numberToText(Double.parseDouble(glBankTransactionDetail.getValue().toString()),  "ريـال", "هلله"));

        if (glBankTransactionDetail != null) {
            if (glBankTransactionDetail.getBankIdFrom() != null && glBankTransactionDetail.getBankIdFrom().getAccountId() != null) {
                hashMap.put("recievedFromValue", glBankTransactionDetail.getBankIdFrom().getAccountId().getName() != null ? glBankTransactionDetail.getBankIdFrom().getAccountId().getName() : null);
            }
            if (glBankTransactionDetail.getBankIdTo() != null && glBankTransactionDetail.getBankIdTo().getAccountId() != null) {
                hashMap.put("recipientValue", glBankTransactionDetail.getBankIdTo().getAccountId().getName() != null ? glBankTransactionDetail.getBankIdTo().getAccountId().getName() : null);
            }
        }
        /*if (glBankTransactionEntity.getOrganizationType() == 1 || glBankTransactionEntity.getOrganizationType() == 0) {
            hashMap.put("recievedFromValue", GlBankTransactionDetailEntity.getInvOrganizationSite() != null ? GlBankTransactionDetailEntity.getInvOrganizationSite().getName() : null);
        } else if (glBankTransactionEntity.getOrganizationType() == 2) {
            GlAccount recipientaccount = new GlAccount();
            recipientaccount = glAccountService.findGlAccount(GlBankTransactionDetailEntity.getAccountCreditId());
            hashMap.put("recievedFromValue", recipientaccount != null ? recipientaccount.getName() : null);
        }*/
        if (glBankTransactionEntity.getPaymentType() != null) {
            switch (glBankTransactionEntity.getPaymentType()) {
                case 0:
                    hashMap.put("reportNameText", "سند تحويل نقدي");
                    break;
                case 2:
                    hashMap.put("reportNameText", "سند تحويل شيك خطي");
                    break;
            }
        }
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
         } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "prepareReport");
            return null;
        }
    }

    public boolean isFileExist(String filePath) {
        try {
        File file = new File(getUserData().getImageReportPath());
        return file.exists();
         } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "isFileExist");    
         return false;
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
        } else if ("excel".equals(exportType)) {

        } else if ("html".equals(exportType)) {

        }
        } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "fillReport");    
                 }

    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the glBankTransactionEntity
     */
    public GlBankTransactionEntity getGlBankTransactionEntity() {
        return glBankTransactionEntity;
    }

    /**
     * @param glBankTransactionEntity the glBankTransactionEntity to set
     */
    public void setGlBankTransactionEntity(GlBankTransactionEntity glBankTransactionEntity) {
        this.glBankTransactionEntity = glBankTransactionEntity;
    }

    /**
     * @return the glBankTransactionEntityList
     */
    public List<GlBankTransactionEntity> getGlBankTransactionEntityList() {
        return glBankTransactionEntityList;
    }

    /**
     * @param glBankTransactionEntityList the glBankTransactionEntityList to set
     */
    public void setGlBankTransactionEntityList(List<GlBankTransactionEntity> glBankTransactionEntityList) {
        this.glBankTransactionEntityList = glBankTransactionEntityList;
    }

    /**
     * @return the glBankTransaction
     */
    public GlBankTransaction getGlBankTransaction() {
        return glBankTransaction;
    }

    /**
     * @param glBankTransaction the glBankTransaction to set
     */
    public void setGlBankTransaction(GlBankTransaction glBankTransaction) {
        this.glBankTransaction = glBankTransaction;
    }

    /**
     * @return the glBankTransactionList
     */
    public List<GlBankTransaction> getGlBankTransactionList() {
        return glBankTransactionList;
    }

    /**
     * @param glBankTransactionList the glBankTransactionList to set
     */
    public void setGlBankTransactionList(List<GlBankTransaction> glBankTransactionList) {
        this.glBankTransactionList = glBankTransactionList;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the glBankTransactionEntitySelected
     */
    public GlBankTransactionEntity getGlBankTransactionEntitySelected() {
        return glBankTransactionEntitySelected;
    }

    /**
     * @param glBankTransactionEntitySelected the
     * glBankTransactionEntitySelected to set
     */
    public void setGlBankTransactionEntitySelected(GlBankTransactionEntity glBankTransactionEntitySelected) {
        this.glBankTransactionEntitySelected = glBankTransactionEntitySelected;
    }

    public GlBankTransactionDetail getGlBankTransactionDetail() {
        return glBankTransactionDetail;
    }

    public void setGlBankTransactionDetail(GlBankTransactionDetail glBankTransactionDetail) {
        this.glBankTransactionDetail = glBankTransactionDetail;
    }

    public GlBankTransactionDetailEntity getGlBankTransactionDetailEntity() {
        return glBankTransactionDetailEntity;
    }

    public void setGlBankTransactionDetailEntity(GlBankTransactionDetailEntity glBankTransactionDetailEntity) {
        this.glBankTransactionDetailEntity = glBankTransactionDetailEntity;
    }

    public Collection<GlBankTransactionDetailEntity> getGlBankTransactionDetailEntityList() {
        return glBankTransactionDetailEntityList;
    }

    public void setGlBankTransactionDetailEntityList(Collection<GlBankTransactionDetailEntity> glBankTransactionDetailEntityList) {
        this.glBankTransactionDetailEntityList = glBankTransactionDetailEntityList;
    }

    public GlBankTransactionEntity getGlBankTransactionSelected() {
        return glBankTransactionSelected;
    }

    public void setGlBankTransactionSelected(GlBankTransactionEntity glBankTransactionSelected) {
        this.glBankTransactionSelected = glBankTransactionSelected;
    }

}
