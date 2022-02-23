/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.settlementdeed;

import com.isag.businessservice.AccountsSystemSettingsService;
import com.isag.businessservice.CreateActionService;
import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.IsagUserBankService;
import com.isag.converter.GlBankConverter;
import com.isag.dto.ActionsDTO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.GlBankTransactionDTO;
import com.isag.entity.AccountsSystemSettings;
import com.isag.entity.GlBank;
import com.isag.entity.GlBankTransaction;
import com.isag.entity.IsagUser;
import com.isag.entiy.GlBankTransactionDetailEntity;
import com.isag.entiy.GlBankTransactionEntity;
import com.isag.isag.BaseGlBankListBean;
import com.isag.isag.UserData;
import java.io.IOException;
import java.util.ArrayList;
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

/**
 *
 * @author WIN7
 */
@Named(value = "settlementDeedListMB")
@ViewScoped
public class settlementDeedListMB extends BaseGlBankListBean {

    private UserData userData;
    private String screenMode;
    private Integer branchId;
    private Integer companyId;

    private Integer glBankTransactionIdSeclected;

    private LazyDataModel<GlBankTransactionDTO> dataModel;

    // DB entites
    private GlBankTransaction glBankTransaction;
    private List<GlBankTransaction> glBankTransactionList;

    // Interface Entities
    private GlBankTransactionEntity glBankTransactionEntity;
    private GlBankTransactionDTO glBankTransactionEntitySelected;
    private List<GlBankTransactionDTO> glBankTransactionEntityList;
    private AccountsSystemSettings accountsSystemSettings;
    private List<GlBank> glBankList;
    private GlBankConverter bankConverter;
    private GlBank selectGlBank;

    @EJB
    private GlBankTransactionService glBankTransactionService;
    @EJB
    AccountsSystemSettingsService accountsSystemSettingsService;
    @EJB
    private IsagUserBankService isagUserBankService;
    @EJB
    CreateActionService createActionService;

    @Override
    @PostConstruct
    public void init() {
        try {
            loadPre();
        } catch (Exception e) {
            saveError(e, "settlementDeedListMB", "init");
        }
    }

    public void loadPre() {
        try {
            userData = new UserData();
            glBankTransaction = new GlBankTransaction();

            glBankTransactionList = new ArrayList<>();
            setGlBankTransactionEntitySelected(new GlBankTransactionDTO());
            setGlBankTransactionEntityList(new ArrayList<>());

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setBranchId(userData.getUserBranch().getId());
            setCompanyId(userData.getCompany().getId());
            setAccountsSystemSettings(accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId()));

            this.setDataModel(new LazyDataModel<GlBankTransactionDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<GlBankTransactionDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    Integer bankId = selectGlBank != null ? selectGlBank.getId() : null;
                    setRowCount(glBankTransactionService.getTotalRegistors(0, userData.getUserBranch().getId(), userData.getUserlogin().getId(), filters, bankId).intValue());
                    setGlBankTransactionEntityList(glBankTransactionService.getALLGlBankTransactionRecievableByBranchId(0, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, bankId, userData.getUserlogin().getId()));
                    return getGlBankTransactionEntityList();
                }
            });
        } catch (Exception e) {
            saveError(e, "settlementDeedListMB", "load");
        }
    }

    @Override
    public void load() {
//        try {
//            userData = new UserData();
//            glBankTransaction = new GlBankTransaction();
//
//            glBankTransactionList = new ArrayList<>();
//            glBankTransactionEntity = new GlBankTransactionEntity();
//            setGlBankTransactionEntitySelected(new GlBankTransactionEntity());
//            setGlBankTransactionEntityList(new ArrayList<>());
//
//            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//            setUserData((UserData) context.getSessionMap().get("userLogInData"));
//            setBranchId(userData.getUserBranch().getId());
//            setCompanyId(userData.getCompany().getId());
//
//            glBankTransactionList = glBankTransactionService.getALLGlBankTransactionSettlmentByBranchId(getBranchId());
//
//            for (GlBankTransaction gbt : glBankTransactionList) {
//                glBankTransactionEntity = new GlBankTransactionEntity();
//                glBankTransactionEntity.setId(gbt.getId());
//                glBankTransactionEntity.setDate(gbt.getDate() != null ? gbt.getDate() : null);
//                glBankTransactionEntity.setGlBank(gbt.getGlBankId() != null ? gbt.getGlBankId() : null);
//                glBankTransactionEntity.setPostFlag(gbt.getPostFlag());
//
//                if (gbt.getPaymentType() != null) {
//                    glBankTransactionEntity.setPaymentType(gbt.getPaymentType());
//
//                    if (gbt.getPaymentType() == 0) {
//                        glBankTransactionEntity.setPaymentTypeName("نقدي");
//                    } else if (gbt.getPaymentType() == 1) {
//                        glBankTransactionEntity.setPaymentTypeName("الشيك");
//                    } else {
//                        glBankTransactionEntity.setPaymentTypeName("الشيك الخطي");
//                    }
//                }
//
//                glBankTransactionEntity.setCreatedBy(gbt.getCreatedBy());
//                glBankTransactionEntity.setCreatedByName(gbt.getCreatedBy().getName());
//                glBankTransactionEntity.setRemark(gbt.getRemark() != null ? gbt.getRemark() : null);
//                glBankTransactionEntity.setRemark2(gbt.getRemark2());
//                glBankTransactionEntity.setTransactionType(gbt.getTransactionType() != null ? gbt.getTransactionType() : null);
//                glBankTransactionEntity.setValue(gbt.getValue() != null ? gbt.getValue() : null);
//                glBankTransactionEntity.setValueLocal(gbt.getValueLocal() != null ? gbt.getValueLocal() : null);
//                glBankTransactionEntity.setSerial(gbt.getSerial() != null ? gbt.getSerial() : null);
//
//                getGlBankTransactionEntityList().add(glBankTransactionEntity);
//            }
//        } catch (Exception e) {
//            saveError(e, "settlementDeedListMB", "load");
//        }
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
//            exit("../settlementdeed/settlementdeedForm.xhtml");
            redirectPage();
            return "";
        } catch (Exception e) {
            saveError(e, "settlementDeedListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (glBankTransactionIdSeclected != null && glBankTransactionIdSeclected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("glBankTransactionIdSeclected", glBankTransactionIdSeclected);
                context.getSessionMap().put("ScreenMode", "Edit");
//                exit("../settlementdeed/settlementdeedForm.xhtml");
                redirectPage();
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "settlementDeedListMB", "goToEdit");
            return null;
        }
    }

    private void redirectPage() {
        String noteSreceivablesType = accountsSystemSettings.getNoteSreceivablesType();
        if (noteSreceivablesType.equals("ALLOW_DETAIL")) {
            if (getUri().contains("notesreceivablesListpre")) {
//                exit("../notesreceivablespre/notesreceivablesdatailFormPre.xhtml");
            } else {
                exit("../settlementdeed/settlementdeeddatailForm.xhtml");
            }
        } else {
            if (getUri().contains("notesreceivablesListpre")) {
//                exit("../notesreceivablespre/notesreceivablesFormpre.xhtml");
            } else {
                exit("../settlementdeed/settlementdeedForm.xhtml");
            }
        }
    }

    @Override
    public void delete() {
        try {
            if (getGlBankTransactionEntitySelected().getPostFlag() == null || (getGlBankTransactionEntitySelected().getPostFlag() != null && getGlBankTransactionEntitySelected().getPostFlag() != 1)) {

                Map<String, String> userDDs = userData.getUserDDs();

                Integer glBankTransactionId = getGlBankTransactionEntitySelected().getId() != null ? getGlBankTransactionEntitySelected().getId() : null;
                glBankTransaction = new GlBankTransaction();
                glBankTransaction.setId(glBankTransactionId);

                try {
                    glBankTransactionService.deleteGlBankTransaction(glBankTransaction);
                    getGlBankTransactionEntityList().remove(getGlBankTransactionEntitySelected());
                    createActionService.SaveAction(SetActionDto(getUserData().getUser(), "settlementdeed", "delete"), getUserData().getUser());

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "هذا السند مرحل"));
            }
        } catch (Exception e) {
            saveError(e, "settlementDeedListMB", "delete");
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

    public void exportPDFWithCopy(ActionEvent actionEvent) throws JRException, IOException {

        GlBankTransactionDTO transactionEntity = (GlBankTransactionDTO) actionEvent.getComponent().getAttributes().get("varValue");
        startPrintingProcess(transactionEntity);
        String reportName = "settlementdeedWithCopy.jasper";
        HashMap hashMap = new HashMap();
        hashMap.put("Param", prepareReport());
        hashMap.put("report_name", getUserData().getReportPath() + "settlementdeed.jasper");
        hashMap.put("listSize", getGlBankTransactionDetailEntityList().size() + 1);
        hashMap.put("lienCut", getUserData().getImagePath() + "lienCut.png");
        hashMap.put("copyWordIcon", getUserData().getImagePath() + "copyWordIcon.png");
        List<GlBankTransactionDetailEntity> glBankTransactionDetailEntity = new ArrayList();
        glBankTransactionDetailEntity.add(new GlBankTransactionDetailEntity(getGlBankTransactionDetailEntityList()));
        fillReport(hashMap, getUserData().getReportPath() + reportName, glBankTransactionDetailEntity, "pdf");
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {

            GlBankTransactionDTO transactionEntity = (GlBankTransactionDTO) actionEvent.getComponent().getAttributes().get("varValue");
            startPrintingProcess(transactionEntity);
            String reportName = "settlementdeed.jasper";
            if (transactionEntity.getOrganizationType() != 2) {
                reportName = "settlementdeedforOrganizationSite.jasper";
            } else {
                if (getAccountsSystemSettings().getSettlementdeedPrinting().equals("ALLOW_COPY")) {
                    exportPDFWithCopy(actionEvent);
                    return;
                }
            }
            if (glBankTransactionEntity != null && glBankTransactionEntity.getId() != null) {
                if (getGlBankTransactionDetailEntityList() != null && !getGlBankTransactionDetailEntityList().isEmpty()) {
                    fillReport(prepareReport(), getUserData().getReportPath() + reportName, getGlBankTransactionDetailEntityList(), "pdf");
                }
            } else {
                if (getGlBankTransactionDetailEntityList() != null && !getGlBankTransactionDetailEntityList().isEmpty()) {
                    fillReport(prepareReport(), getUserData().getReportPath() + reportName, getGlBankTransactionDetailEntityList(), "pdf");
                    createActionService.SaveAction(SetActionDto(getUserData().getUser(), "settlementdeed", "print"), getUserData().getUser());

                }
            }
        } catch (Exception e) {
            saveError(e, "settlementDeedListMB", "exportPDF");
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

    public Integer getGlBankTransactionIdSeclected() {
        return glBankTransactionIdSeclected;
    }

    public void setGlBankTransactionIdSeclected(Integer glBankTransactionIdSeclected) {
        this.glBankTransactionIdSeclected = glBankTransactionIdSeclected;
    }

    public GlBankTransaction getGlBankTransaction() {
        return glBankTransaction;
    }

    public void setGlBankTransaction(GlBankTransaction glBankTransaction) {
        this.glBankTransaction = glBankTransaction;
    }

    public List<GlBankTransaction> getGlBankTransactionList() {
        return glBankTransactionList;
    }

    public void setGlBankTransactionList(List<GlBankTransaction> glBankTransactionList) {
        this.glBankTransactionList = glBankTransactionList;
    }

    public GlBankTransactionEntity getGlBankTransactionEntity() {
        return glBankTransactionEntity;
    }

    public void setGlBankTransactionEntity(GlBankTransactionEntity glBankTransactionEntity) {
        this.glBankTransactionEntity = glBankTransactionEntity;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<GlBankTransactionDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<GlBankTransactionDTO> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the glBankTransactionEntitySelected
     */
    public GlBankTransactionDTO getGlBankTransactionEntitySelected() {
        return glBankTransactionEntitySelected;
    }

    /**
     * @param glBankTransactionEntitySelected the
     * glBankTransactionEntitySelected to set
     */
    public void setGlBankTransactionEntitySelected(GlBankTransactionDTO glBankTransactionEntitySelected) {
        this.glBankTransactionEntitySelected = glBankTransactionEntitySelected;
    }

    /**
     * @return the glBankTransactionEntityList
     */
    public List<GlBankTransactionDTO> getGlBankTransactionEntityList() {
        return glBankTransactionEntityList;
    }

    /**
     * @param glBankTransactionEntityList the glBankTransactionEntityList to set
     */
    public void setGlBankTransactionEntityList(List<GlBankTransactionDTO> glBankTransactionEntityList) {
        this.glBankTransactionEntityList = glBankTransactionEntityList;
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

    /**
     * @return the glBankList
     */
    public List<GlBank> getGlBankList() {
        if (glBankList == null) {
            setGlBankList(isagUserBankService.getAllGlBankForUserByTypeAndBranchId(getUserData().getUser().getId(), getBranchId(), 0));
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
