/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.AccountsSystemSettingsService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlBankTransactionDetailsService;
import com.toby.businessservice.GlBankTransactionService;
import com.toby.dto.GlBankTransactionDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.AccountsSystemSettings;
import com.toby.entity.GlAccount;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.InvOrganizationSite;
import com.toby.entiy.GlBankTransactionDetailEntity;
import com.toby.entiy.GlBankTransactionEntity;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
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
 * @author ahmed
 */
public abstract class BaseGlBankListBean extends BaseListBean {

    private GlBankTransactionEntity glBankTransactionEntityMapper = new GlBankTransactionEntity();
    private GlBankTransactionDetailEntity glBankTransactionDetailEntity;
    private List<GlBankTransactionDetailEntity> glBankTransactionDetailEntityList;
    private List<GlBankTransactionDetail> glBankTransactionDetailsList;
    private UserData userData;
    private String remark1;
    private String remark2;
    private BigDecimal localValue = BigDecimal.ZERO;
    private BigDecimal valueSummary = BigDecimal.ZERO;
    private GlBankTransactionEntity glBankTransactionEntity = new GlBankTransactionEntity();
    private GlBankTransaction glBankTransaction;
    private AccountsSystemSettings accountsSystemSettings;
    @EJB
    private GlBankTransactionDetailsService glBankTransactionDetailsService;
    @EJB
    private GlBankTransactionService glBankTransactionService;
    @EJB
    private GlAccountService glAccountService;
    @EJB
    AccountsSystemSettingsService accountsSystemSettingsService;

    public void startPrintingProcess(GlBankTransactionDTO bankTransaction) {
        glBankTransactionDetailEntityList = new ArrayList<>();
        glBankTransactionDetailsList = new ArrayList<>();
        glBankTransactionEntityMapper = new GlBankTransactionEntity();
        glBankTransaction = glBankTransactionService.findGlBankTransactionById(bankTransaction.getId());
        glBankTransactionDetailsList = glBankTransactionDetailsService.getAllGlBankTransactionDetailsByGlBankTransactionId(bankTransaction.getId());

        if (bankTransaction != null) {
            remark1 = glBankTransaction.getRemark() != null ? glBankTransaction.getRemark() : null;
            remark2 = glBankTransaction.getRemark2() != null ? glBankTransaction.getRemark2() : null;
        }
        glBankTransactionEntity = mapGlBankTransactionToGlBankTransactionEntity(glBankTransaction);
    }

    private GlBankTransactionEntity mapGlBankTransactionToGlBankTransactionEntity(GlBankTransaction bankTransaction) {

        glBankTransactionDetailEntityList = new ArrayList<>();
        glBankTransactionEntityMapper = new GlBankTransactionEntity();
        glBankTransactionEntityMapper.setId(bankTransaction.getId());
        glBankTransactionEntityMapper.setSerial(bankTransaction.getSerial());
        glBankTransactionEntityMapper.setDate(bankTransaction.getDate() != null ? bankTransaction.getDate() : new Date());
        glBankTransactionEntityMapper.setPaymentType(bankTransaction.getPaymentType() != null ? bankTransaction.getPaymentType() : 0);

        if (bankTransaction.getPaymentType() == 1) {
            glBankTransactionEntityMapper.setChequeDate(bankTransaction.getChequeDate() != null ? bankTransaction.getChequeDate() : new Date());
            glBankTransactionEntityMapper.setChequeDueDate(bankTransaction.getChequeDueDate() != null ? bankTransaction.getChequeDueDate() : new Date());
            glBankTransactionEntityMapper.setChequeNumber(bankTransaction.getChequeNumber() != null ? bankTransaction.getChequeNumber() : null);
            glBankTransactionEntityMapper.setChequeStatus(bankTransaction.getChequeStatus() != null ? bankTransaction.getChequeStatus() : 0);
        }

        glBankTransactionEntityMapper.setGlBank(bankTransaction.getGlBankId() != null ? bankTransaction.getGlBankId() : null);
        glBankTransactionEntityMapper.setOrganizationType(bankTransaction.getOrganizationType() != null ? bankTransaction.getOrganizationType() : 0);
        glBankTransactionEntityMapper.setRemark(bankTransaction.getRemark() != null ? bankTransaction.getRemark() : null);
        glBankTransactionEntityMapper.setRemark2(bankTransaction.getRemark2() != null ? bankTransaction.getRemark2() : null);
        glBankTransactionEntityMapper.setTransactionType(bankTransaction.getTransactionType() != null ? bankTransaction.getTransactionType() : 0);
        glBankTransactionEntityMapper.setValue(bankTransaction.getValue() != null ? bankTransaction.getValue() : BigDecimal.ZERO);
        glBankTransactionEntityMapper.setRate(bankTransaction.getRate() != null ? bankTransaction.getRate() : BigDecimal.ONE);
        glBankTransactionEntityMapper.setValueLocal(bankTransaction.getValueLocal() == null ? BigDecimal.ZERO : bankTransaction.getValueLocal());
        glBankTransactionEntityMapper.setGlYear(bankTransaction.getGlYear() != null ? bankTransaction.getGlYear() : null);
        glBankTransactionEntityMapper.setPostFlag(bankTransaction.getPostFlag());
        glBankTransactionEntityMapper.setCreatedNameParent(bankTransaction.getParent() != null ? bankTransaction.getParent().getCreatedBy().getName() : null);
        for (GlBankTransactionDetail transactionDetail : glBankTransactionDetailsList) {
            glBankTransactionDetailEntity = new GlBankTransactionDetailEntity();

            glBankTransactionDetailEntity.setId(transactionDetail.getId() != null ? transactionDetail.getId() : null);
            glBankTransactionDetailEntity.setCostCenterId(transactionDetail.getCostCenterId() != null ? transactionDetail.getCostCenterId().getId() : null);
            glBankTransactionDetailEntity.setCostCenter(transactionDetail.getCostCenterId() != null ? transactionDetail.getCostCenterId() : null);
            glBankTransactionDetailEntity.setAccountCreditId(transactionDetail.getAccountCreditId() != null ? transactionDetail.getAccountCreditId().getId() : null);
            glBankTransactionDetailEntity.setGlAccount(transactionDetail.getAccountDebitId() != null ? transactionDetail.getAccountDebitId() : null);
            glBankTransactionDetailEntity.setAccountDebitId(transactionDetail.getAccountDebitId() != null ? transactionDetail.getAccountDebitId().getId() : null);
            glBankTransactionDetailEntity.setAdminUnitId(transactionDetail.getAdminUnitId() != null ? transactionDetail.getAdminUnitId().getId() : null);
            glBankTransactionDetailEntity.setGlAdminUnit(transactionDetail.getAdminUnitId() != null ? transactionDetail.getAdminUnitId() : null);
            glBankTransactionDetailEntity.setGlBankTransactionId(transactionDetail.getGlBankTransactionId() != null ? transactionDetail.getGlBankTransactionId().getId() : null);
            glBankTransactionDetailEntity.setRemarks(transactionDetail.getRemarks() != null ? transactionDetail.getRemarks() : null);
            InvOrganizationSite ios = transactionDetail.getOrganizationSiteId();
                InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
                if (ios != null) {
                    invOrganizationSiteDTO.setId(ios.getId());
                    invOrganizationSiteDTO.setName(ios.getName());
                    invOrganizationSiteDTO.setCode(ios.getCode());
                    invOrganizationSiteDTO.setMobile(ios.getMobile());
                }
            glBankTransactionDetailEntity.setInvOrganizationSite(transactionDetail.getOrganizationSiteId() != null ? invOrganizationSiteDTO : null);
            glBankTransactionDetailEntity.setValue(transactionDetail.getValue() != null ? transactionDetail.getValue() : null);
            glBankTransactionDetailEntity.setValueLocal(transactionDetail.getValueLocal() != null ? transactionDetail.getValueLocal() : null);
            glBankTransactionDetailEntity.setSerial(transactionDetail.getSerial() != null ? transactionDetail.getSerial() : null);
            glBankTransactionDetailEntity.setGlItems(transactionDetail.getSymbolId());
            glBankTransactionDetailEntity.setOrganizationsiteName(transactionDetail.getOrganizationSiteId() == null ? null : transactionDetail.getOrganizationSiteId().getName());
            setValueSummary(getValueSummary().add(glBankTransactionDetailEntity.getValue() != null ? glBankTransactionDetailEntity.getValue() : BigDecimal.ZERO));

            glBankTransactionDetailEntityList.add(glBankTransactionDetailEntity);
        }
        localValue = getValueSummary().multiply(glBankTransactionEntityMapper.getRate());
        localValue = localValue.setScale(2, BigDecimal.ROUND_UP);
        return glBankTransactionEntityMapper;
    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("companyName", getUserData().getCompany().getName());
        hashMap.put("amountText", userDDs.get("AMOUNT"));
        hashMap.put("dateText", userDDs.get("DATE"));

//        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
//        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_NUMBER"));
        hashMap.put("remarkText", "البيان");
        hashMap.put("remarkText2", ("مناولة"));
        hashMap.put("payAccountText", "يصرف من");
        hashMap.put("tellerText", "امين صندوق");
        hashMap.put("accountManager", "مدير الحسابات");
        hashMap.put("confirmationText", "اعتماد");
        hashMap.put("valueFromText", "وذلك قيمة");
        hashMap.put("documentNumberText", "رقم السند");
        hashMap.put("chequeDateText", "تاريخ الشيك");
        hashMap.put("chequeDateValue", glBankTransactionEntityMapper.getChequeDate() != null ? glBankTransactionEntityMapper.getChequeDate() : null);
        hashMap.put("deservedDateText", "تاريخ الاستحقاق");
        hashMap.put("deservedDateValue", glBankTransactionEntityMapper.getChequeDueDate() != null ? glBankTransactionEntityMapper.getChequeDueDate() : null);
        //  hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("dateValue", glBankTransactionEntityMapper.getDate());
        hashMap.put("documentNumberValue", glBankTransactionEntityMapper.getSerial());
        hashMap.put("payAccountValue", glBankTransactionEntityMapper.getGlBank() != null ? glBankTransactionEntityMapper.getGlBank().getName() : null);
        hashMap.put("amountValue", glBankTransactionEntityMapper.getValue());
        hashMap.put("amountValueText", Tafqeet.numberToText(Double.parseDouble(glBankTransactionEntityMapper.getValue().toString()), "جنيه", "قرش"));
        hashMap.put("valueFromValue", remark1);
        hashMap.put("explainationValue", remark2);

        hashMap.put("rateText", "المعدل");
        hashMap.put("rateValue", glBankTransactionEntityMapper.getRate());
        hashMap.put("amountInRateText", userDDs.get("TOTAL"));
        hashMap.put("amountInRateValue", glBankTransactionEntityMapper.getValueLocal());

        hashMap.put("sumValue", getValueSummary());
        hashMap.put("localValue", localValue);

        hashMap.put("serialText", "م");
        hashMap.put("accountsText", "المستلم");
        hashMap.put("recipientText", "المستلم");
        hashMap.put("recievedFromText", "استلمنا من");
        hashMap.put("SecretaryOfTheTreasuryText", "امين الخزينة");
        hashMap.put("signatureText", "التوقيع");

        hashMap.put("recipientValue", glBankTransactionEntityMapper.getGlBank().getName());
        hashMap.put("copyWordIcon", getUserData().getImagePath() + "copyWordIcon.png");

        hashMap.put("Receipt", "لا  يعتد بهذا الايصال الا اذا كان مختوم بخاتم النقابة");
        hashMap.put("stampText", "الختم");
        hashMap.put("tellerText", "امين الخزينه");
        hashMap.put("responsibleText", "مسئول الخدمة");
        hashMap.put("responsibleValue", glBankTransactionEntityMapper.getCreatedNameParent());
        hashMap.put("listSize", glBankTransactionDetailEntityList.size());
        hashMap.put("CreatedBy", glBankTransactionEntityMapper.getCreatedByName());
        hashMap.put("organizationsiteNameText", "الاسم");

        
        if (glBankTransactionEntity.getOrganizationType() == 1 || glBankTransactionEntity.getOrganizationType() == 0) {
            hashMap.put("recievedFromValue", glBankTransactionDetailEntity.getInvOrganizationSite() != null ? glBankTransactionDetailEntity.getInvOrganizationSite().getName() : null);
        } else if (glBankTransactionEntity.getOrganizationType() == 2) {
            GlAccount recipientaccount = new GlAccount();
            recipientaccount = glAccountService.findGlAccount(glBankTransactionDetailEntity.getAccountCreditId());
            hashMap.put("recievedFromValue", recipientaccount != null ? recipientaccount.getName() : null);
        }
        // GlAccount recipientaccount = new GlAccount();
        if (glBankTransactionDetailEntityList != null && !glBankTransactionDetailEntityList.isEmpty()) {
            /*for (int i = 0; i < glBankTransactionDetailEntityList.size(); i++) {
             recipientaccount = glAccountService.findGlAccount(glBankTransactionDetailEntityList.get(i).getAccountDebitId());
             if (recipientaccount != null) {
             glBankTransactionDetailEntityList.get(i).setAccountName(recipientaccount.getName());
             glBankTransactionDetailEntityList.get(i).setAccountNumber(recipientaccount.getAccNumber());
             }
             }*/
//            for (GlBankTransactionDetailEntity transactionDetailEntity : glBankTransactionDetailEntityList) {
//                if (transactionDetailEntity.getGlAccount() != null && transactionDetailEntity.getGlAccount().getId() != null) {
//                    transactionDetailEntity.setAccountName(transactionDetailEntity.getGlAccount().getName());
//                    transactionDetailEntity.setAccountNumber(transactionDetailEntity.getGlAccount().getAccNumber());
//                }
//            }
            setAccountsSystemSettings(accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId()));
            if (getAccountsSystemSettings().getWorkWithAccounts().equals("ALLOW_ACCOUNT")) {
                hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
                hashMap.put("accountNumberText", userDDs.get("ACCOUNT_NUMBER"));
                for (GlBankTransactionDetailEntity transactionDetailEntity : glBankTransactionDetailEntityList) {
                    if (transactionDetailEntity.getGlAccount() != null && transactionDetailEntity.getGlAccount().getId() != null) {
                        transactionDetailEntity.setAccountName(transactionDetailEntity.getGlAccount().getName());
                        transactionDetailEntity.setAccountNumber(transactionDetailEntity.getGlAccount().getAccNumber());
                    }
                }
            } else if (getAccountsSystemSettings().getWorkWithAccounts().equals("NOT_ALLOW_ACCOUNT")) {
                hashMap.put("accountNameText", "اسم البند");
                hashMap.put("accountNumberText", "كود البند");
                for (GlBankTransactionDetailEntity transactionDetailEntity : glBankTransactionDetailEntityList) {
                    if (transactionDetailEntity.getGlItems() != null && transactionDetailEntity.getGlItems().getId() != null) {
                        transactionDetailEntity.setAccountName(transactionDetailEntity.getGlItems().getName());
                        transactionDetailEntity.setAccountNumber(transactionDetailEntity.getGlItems().getSerial());
                    }
                }
            }
        }

        if (glBankTransactionEntityMapper.getTransactionType() == 0) {
            if (glBankTransactionEntityMapper.getPaymentType() != null) {
                switch (glBankTransactionEntityMapper.getPaymentType()) {
                    case 0:
                        hashMap.put("reportNameText", "سند صرف نقدي ");
                        break;
                    case 1:
                        hashMap.put("reportNameText", "سند صرف شيك ");
                        break;
                    case 2:
                        hashMap.put("reportNameText", "سند صرف شيك خطي ");
                        break;
                }
            }
        } else if (glBankTransactionEntityMapper.getTransactionType() == 1) {
            if (glBankTransactionEntityMapper.getPaymentType() != null) {
                switch (glBankTransactionEntityMapper.getPaymentType()) {
                    case 0:
                        hashMap.put("reportNameText", "سند قبض نقدي ");
                        break;
                    case 1:
                        hashMap.put("reportNameText", "سند قبض شيك ");
                        break;
                    case 2:
                        hashMap.put("reportNameText", "سند قبض شيك خطي ");
                        break;
                }
            }
        }
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        return hashMap;
    }

//    public boolean isFileExist(String filePath) {
//        File file = new File(getUserData().getImageReportPath());
//        return file.exists();
//    }
    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        if ("pdf".equals(exportType)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        } else if ("excel".equals(exportType)) {

        } else if ("html".equals(exportType)) {

        }

    }

    /**
     * @return the glBankTransactionEntityMapper
     */
    public GlBankTransactionEntity getGlBankTransactionEntityMapper() {
        return glBankTransactionEntityMapper;
    }

    /**
     * @param glBankTransactionEntityMapper the glBankTransactionEntityMapper to
     * set
     */
    public void setGlBankTransactionEntityMapper(GlBankTransactionEntity glBankTransactionEntityMapper) {
        this.glBankTransactionEntityMapper = glBankTransactionEntityMapper;
    }

    /**
     * @return the glBankTransactionDetailEntityList
     */
    public List<GlBankTransactionDetailEntity> getGlBankTransactionDetailEntityList() {
        return glBankTransactionDetailEntityList;
    }

    /**
     * @param glBankTransactionDetailEntityList the
     * glBankTransactionDetailEntityList to set
     */
    public void setGlBankTransactionDetailEntityList(List<GlBankTransactionDetailEntity> glBankTransactionDetailEntityList) {
        this.glBankTransactionDetailEntityList = glBankTransactionDetailEntityList;
    }

    /**
     * @return the glBankTransactionDetailEntity
     */
    public GlBankTransactionDetailEntity getGlBankTransactionDetailEntity() {
        return glBankTransactionDetailEntity;
    }

    /**
     * @param glBankTransactionDetailEntity the glBankTransactionDetailEntity to
     * set
     */
    public void setGlBankTransactionDetailEntity(GlBankTransactionDetailEntity glBankTransactionDetailEntity) {
        this.glBankTransactionDetailEntity = glBankTransactionDetailEntity;
    }

    /**
     * @return the glBankTransactionDetailsList
     */
    public List<GlBankTransactionDetail> getGlBankTransactionDetailsList() {
        return glBankTransactionDetailsList;
    }

    /**
     * @param glBankTransactionDetailsList the glBankTransactionDetailsList to
     * set
     */
    public void setGlBankTransactionDetailsList(List<GlBankTransactionDetail> glBankTransactionDetailsList) {
        this.glBankTransactionDetailsList = glBankTransactionDetailsList;
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
     * @return the valueSummary
     */
    public BigDecimal getValueSummary() {
        return valueSummary;
    }

    /**
     * @param valueSummary the valueSummary to set
     */
    public void setValueSummary(BigDecimal valueSummary) {
        this.valueSummary = valueSummary;
    }

    /**
     * @return the accountsSystemSettings
     */
    public AccountsSystemSettings getAccountsSystemSettings() {
        return accountsSystemSettings;
    }

    /**
     * @param accountsSystemSettings the accountsSystemSettings to set
     */
    public void setAccountsSystemSettings(AccountsSystemSettings accountsSystemSettings) {
        this.accountsSystemSettings = accountsSystemSettings;
    }
}
