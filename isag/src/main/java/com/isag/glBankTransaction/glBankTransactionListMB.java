/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.glBankTransaction;

import com.isag.businessservice.CreateActionService;
import com.isag.businessservice.GlBankTransactionDetailsService;
import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.IsagUserBankService;
import com.isag.businessservice.ReportManagementService;
import com.isag.businessservice.ReportSignatureService;
import com.isag.businessservice.reports.GlTransactionBankListViewService;
import com.isag.converter.GlBankConverter;
import com.isag.define.ReportSignatureEnum;
import com.isag.dto.ActionsDTO;
import com.isag.dto.ReportSignatureDTO;
import com.isag.entity.GlBank;
import com.isag.entity.GlBankTransaction;
import com.isag.entity.GlBankTransactionDetail;
import com.isag.entity.IsagUser;
import com.isag.entity.ReportManagement;
import com.isag.entiy.GlBankTransactionDetailEntity;
import com.isag.entiy.GlBankTransactionEntity;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import com.isag.views.GlTransactionBankListView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import tafqeet.Tafqeet;

/**
 *
 * @author hq002
 */
@Named(value = "glBankTransactionListMB")
@ViewScoped
public class glBankTransactionListMB extends BaseListBean {

    private UserData userData;
    private GlTransactionBankListView glBankTransactionEntity;
    private GlBankTransactionDetail glBankTransactionDetail;
    private GlBankTransactionDetailEntity glBankTransactionDetailEntity;
    private Collection<GlBankTransactionDetailEntity> glBankTransactionDetailEntityList;

    private GlTransactionBankListView glBankTransactionEntitySelected;
    private List<GlBankTransactionEntity> glBankTransactionEntityList;
    private GlBankTransaction glBankTransaction;
    private List<GlTransactionBankListView> glBankTransactionList;
    private GlTransactionBankListView glBankTransactionSelected;
    private Integer index = 0;
    private List<GlBank> glBankList;
    private GlBankConverter bankConverter;
    private GlBank selectGlBank;

    private LazyDataModel<GlTransactionBankListView> dataModel;
    @EJB
    CreateActionService createActionService;

    @EJB
    private GlBankTransactionService glBankTransactionService;

    @EJB
    private GlBankTransactionDetailsService glBankTransactionDetailsService;

    @EJB
    private GlTransactionBankListViewService glTransactionBankListViewService;
    @EJB
    private ReportManagementService reportManagementService;
    @EJB
    private ReportSignatureService reportSignatureService;
    @EJB
    private IsagUserBankService isagUserBankService;

    @Override
    @PostConstruct
    public void init() {
        try {
            glBankTransactionEntityList = new ArrayList<>();
            glBankTransactionList = new ArrayList<>();
            glBankTransaction = new GlBankTransaction();
//            load();
            loadPer();
        } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "init");
        }
    }

    public void loadPer() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");

            this.setDataModel(new LazyDataModel<GlTransactionBankListView>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<GlTransactionBankListView> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    Integer bankId = selectGlBank != null ? selectGlBank.getId() : null;
                    setRowCount(glTransactionBankListViewService.getTotalRegistorslazy(2, userData.getUserBranch().getId(), getUserData().getUserlogin().getId(), filters, bankId).intValue());
                    //glTransactionBankListViewService.getALLGlBankTransactionRecievableByBranchId(2, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters);
                    glBankTransactionList = glTransactionBankListViewService.getALLGlTransactionBankListViewLazy(2, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, bankId, getUserData().getUserlogin().getId());
//                    for (GlTransactionBankListView gbt : glBankTransactionList) {
//                        glBankTransactionDetailEntityList = new ArrayList<>();
//                        glBankTransactionEntity = new GlBankTransactionEntity();
//                        glBankTransactionDetailEntity = new GlBankTransactionDetailEntity();
//
//                        glBankTransactionEntity.setId(gbt.getId());
//                        glBankTransactionEntity.setSerial(gbt.getSerial() != null ? gbt.getSerial() : null);
//                        glBankTransactionEntity.setDate(gbt.getDate() != null ? gbt.getDate() : null);
//                        glBankTransactionEntity.setPaymentType(gbt.getPaymentType() != null ? gbt.getPaymentType() : 0);
//                        glBankTransactionEntity.setRemark(gbt.getRemark() != null ? gbt.getRemark() : null);
//                        glBankTransactionEntity.setPostFlag(gbt.getPostFlag());
//                        glBankTransactionDetail = glBankTransactionDetailsService.getGlBankTransactionDetailByGlBankTransactionId(gbt.getId());
//
//                        glBankTransactionDetailEntity.setId(glBankTransactionDetail.getId());
//                        glBankTransactionDetailEntity.setBankFrom(glBankTransactionDetail.getBankIdFrom() != null ? glBankTransactionDetail.getBankIdFrom() : null);
//                        glBankTransactionDetailEntity.setBankTo(glBankTransactionDetail.getBankIdTo() != null ? glBankTransactionDetail.getBankIdTo() : null);
//                        glBankTransactionDetailEntity.setValue(glBankTransactionDetail.getValue() != null ? glBankTransactionDetail.getValue() : null);
//                        glBankTransactionDetailEntity.setValueLocal(glBankTransactionDetail.getValueLocal() != null ? glBankTransactionDetail.getValueLocal() : null);
//                        glBankTransactionDetailEntityList.add(glBankTransactionDetailEntity);
//
//                        glBankTransactionEntity.setGlBankTransactionDetailCollection(glBankTransactionDetailEntityList);
//
//                        glBankTransactionEntityList.add(glBankTransactionEntity);
//                    }
                    return glBankTransactionList;
                }
            });

        } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "loadPre");
        }
    }

    @Override
    public void load() {
//        try {
//            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//            userData = (UserData) context.getSessionMap().get("userLogInData");
//            glBankTransactionList = glBankTransactionService.getALLGlBankTransactionRecievableByBranchId(userData.getUserBranch().getId(), 2);
//            for (GlBankTransaction gbt : glBankTransactionList) {
//                glBankTransactionDetailEntityList = new ArrayList<>();
//                glBankTransactionEntity = new GlBankTransactionEntity();
//                glBankTransactionDetailEntity = new GlBankTransactionDetailEntity();
//
//                glBankTransactionEntity.setId(gbt.getId());
//                glBankTransactionEntity.setSerial(gbt.getSerial() != null ? gbt.getSerial() : null);
//                glBankTransactionEntity.setDate(gbt.getDate() != null ? gbt.getDate() : null);
//                glBankTransactionEntity.setPaymentType(gbt.getPaymentType() != null ? gbt.getPaymentType() : 0);
//                glBankTransactionEntity.setRemark(gbt.getRemark() != null ? gbt.getRemark() : null);
//                glBankTransactionEntity.setPostFlag(gbt.getPostFlag());
//                glBankTransactionDetail = glBankTransactionDetailsService.getGlBankTransactionDetailByGlBankTransactionId(gbt.getId());
//
//                glBankTransactionDetailEntity.setId(glBankTransactionDetail.getId());
//                glBankTransactionDetailEntity.setBankFrom(glBankTransactionDetail.getBankIdFrom() != null ? glBankTransactionDetail.getBankIdFrom() : null);
//                glBankTransactionDetailEntity.setBankTo(glBankTransactionDetail.getBankIdTo() != null ? glBankTransactionDetail.getBankIdTo() : null);
//                glBankTransactionDetailEntity.setValue(glBankTransactionDetail.getValue() != null ? glBankTransactionDetail.getValue() : null);
//                glBankTransactionDetailEntity.setValueLocal(glBankTransactionDetail.getValueLocal() != null ? glBankTransactionDetail.getValueLocal() : null);
//                glBankTransactionDetailEntityList.add(glBankTransactionDetailEntity);
//
//                glBankTransactionEntity.setGlBankTransactionDetailCollection(glBankTransactionDetailEntityList);
//
//                glBankTransactionEntityList.add(glBankTransactionEntity);
//            }
//        } catch (Exception e) {
//            saveError(e, "glBankTransactionListMB", "load");
//        }
    }

    @Override
    public void delete() {
        try {
            if (glBankTransactionEntitySelected.getPostFlag() == null || (glBankTransactionEntitySelected.getPostFlag() != null && glBankTransactionEntitySelected.getPostFlag() != 1)) {
                Map<String, String> userDDs = userData.getUserDDs();

                Integer glBankTransactionId = glBankTransactionEntitySelected.getId() != null ? glBankTransactionEntitySelected.getId() : null;
                glBankTransaction = new GlBankTransaction();
                glBankTransaction.setId(glBankTransactionId);

                try {
                    glBankTransactionService.deleteGlBankTransaction(glBankTransaction);
                    glBankTransactionEntityList.remove(glBankTransactionEntitySelected);
                    createActionService.SaveAction(SetActionDto(getUserData().getUser(), "glBankTransaction", "delete"), getUserData().getUser());

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

    public ActionsDTO SetActionDto(IsagUser isagUser, String screenname, String type) {
        ActionsDTO actionsDTO = new ActionsDTO();
        actionsDTO.setDate(new Date());
        actionsDTO.setUserId(isagUser.getName());
        actionsDTO.setScreenName(screenname);
        actionsDTO.setType(type);
        return actionsDTO;
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
        try {

            glBankTransactionEntity = (GlTransactionBankListView) actionEvent.getComponent().getAttributes().get("varValue");

            List<GlTransactionBankListView> gbtdes = new ArrayList<>();
            gbtdes.add(glBankTransactionEntity);
            if (glBankTransactionEntity != null && glBankTransactionEntity.getId() != null) {
                fillReport(prepareReport(), getUserData().getReportPath() + "glBankTransaction.jasper", gbtdes, "pdf");
                createActionService.SaveAction(SetActionDto(getUserData().getUser(), "glBankTransaction", "print"), getUserData().getUser());

            }
        } catch (Exception e) {
            saveError(e, "glBankTransactionListMB", "exportPDF");
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
            ReportSignatureDTO reportSignatureDTO = reportSignatureService.getReportSignatureDTOByScreenANDBranchId(ReportSignatureEnum.TransferBank.getValue(), getUserData().getUserBranch().getId());
            if (reportSignatureDTO != null) {

                if (reportSignatureDTO.getSignature3() == null || reportSignatureDTO.getSignature3().isEmpty()) {
                    hashMap.put("report_signature", getUserData().getReportPath() + "signatureTwoElement.jasper");
                } else if (reportSignatureDTO.getSignature3() != null && (reportSignatureDTO.getSignature4() == null || reportSignatureDTO.getSignature4().isEmpty())) {
                    hashMap.put("report_signature", getUserData().getReportPath() + "signatureThreeElement.jasper");
                } else if (reportSignatureDTO.getSignature4() != null && (reportSignatureDTO.getSignature5() == null || reportSignatureDTO.getSignature5().isEmpty())) {
                    hashMap.put("report_signature", getUserData().getReportPath() + "signatureFourElement.jasper");
                } else {
                    hashMap.put("report_signature", getUserData().getReportPath() + "signatureFiveElement.jasper");
                }
                HashMap signatureHashMap = new HashMap();
                signatureHashMap.put("Signature1", reportSignatureDTO.getSignature1());
                signatureHashMap.put("Signature2", reportSignatureDTO.getSignature2());
                signatureHashMap.put("Signature3", reportSignatureDTO.getSignature3());
                signatureHashMap.put("Signature4", reportSignatureDTO.getSignature4());
                signatureHashMap.put("Signature5", reportSignatureDTO.getSignature5());
                hashMap.put("signatureHashMap", signatureHashMap);
            }
            hashMap.put("PrintedBy", getUserData().getUser().getName());
            hashMap.put("ModifiedBy", null);
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
            hashMap.put("amountValueText", Tafqeet.numberToText(Double.parseDouble(glBankTransactionDetail.getValue().toString()), "جنيه", "قرش"));

            if (glBankTransactionDetail != null) {
                if (glBankTransactionDetail.getBankIdFrom() != null && glBankTransactionDetail.getBankIdFrom().getAccountId() != null) {
                    hashMap.put("recievedFromValue", glBankTransactionDetail.getBankIdFrom() != null ? glBankTransactionDetail.getBankIdFrom().getName() : null);
                }
                if (glBankTransactionDetail.getBankIdTo() != null && glBankTransactionDetail.getBankIdTo().getAccountId() != null) {
                    hashMap.put("recipientValue", glBankTransactionDetail.getBankIdTo() != null ? glBankTransactionDetail.getBankIdTo().getName() : null);
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
            hashMap.put("lienCut", getUserData().getImagePath() + "lienCut.png");
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
    public GlTransactionBankListView getGlBankTransactionEntity() {
        return glBankTransactionEntity;
    }

    /**
     * @param glBankTransactionEntity the glBankTransactionEntity to set
     */
    public void setGlBankTransactionEntity(GlTransactionBankListView glBankTransactionEntity) {
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
    public List<GlTransactionBankListView> getGlBankTransactionList() {
        return glBankTransactionList;
    }

    /**
     * @param glBankTransactionList the glBankTransactionList to set
     */
    public void setGlBankTransactionList(List<GlTransactionBankListView> glBankTransactionList) {
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
    public GlTransactionBankListView getGlBankTransactionEntitySelected() {
        return glBankTransactionEntitySelected;
    }

    /**
     * @param glBankTransactionEntitySelected the
     * glBankTransactionEntitySelected to set
     */
    public void setGlBankTransactionEntitySelected(GlTransactionBankListView glBankTransactionEntitySelected) {
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

    public GlTransactionBankListView getGlBankTransactionSelected() {
        return glBankTransactionSelected;
    }

    public void setGlBankTransactionSelected(GlTransactionBankListView glBankTransactionSelected) {
        this.glBankTransactionSelected = glBankTransactionSelected;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<GlTransactionBankListView> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<GlTransactionBankListView> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the glBankList
     */
    public List<GlBank> getGlBankList() {
        if (glBankList == null) {
            setGlBankList(isagUserBankService.getAllGlBankForUserByTypeAndBranchId(getUserData().getUser().getId(), getUserData().getUserBranch().getId(), 2));
            setBankConverter(new GlBankConverter(glBankList));
        }
        return glBankList;
    }

    /**
     * @param glBankList the glBankList to set
     */
    public void setGlBankList(List<GlBank> glBankList) {
        this.glBankList = glBankList;
    }

    /**
     * @return the bankConverter
     */
    public GlBankConverter getBankConverter() {
        return bankConverter;
    }

    /**
     * @param bankConverter the bankConverter to set
     */
    public void setBankConverter(GlBankConverter bankConverter) {
        this.bankConverter = bankConverter;
    }

    /**
     * @return the selectGlBank
     */
    public GlBank getSelectGlBank() {
        return selectGlBank;
    }

    /**
     * @param selectGlBank the selectGlBank to set
     */
    public void setSelectGlBank(GlBank selectGlBank) {
        this.selectGlBank = selectGlBank;
    }
}
