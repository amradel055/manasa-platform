/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.notesreceivables;

import com.isag.businessservice.AccountsSystemSettingsService;
import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.IsagUserBankService;
import com.isag.converter.GlBankConverter;
import com.isag.dto.GlBankTransactionDTO;
import com.isag.entity.AccountsSystemSettings;
import com.isag.entity.GlBank;
import com.isag.entity.GlBankTransaction;
import com.isag.entiy.GlBankTransactionDetailEntity;
import com.isag.entiy.GlBankTransactionEntity;
import com.isag.isag.BaseGlBankListBean;
import com.isag.isag.UserData;
import java.io.IOException;
import java.util.ArrayList;
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
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author WIN7
 */
@Named(value = "notesReceivablesListMB")
@ViewScoped
public class notesReceivablesListMB extends BaseGlBankListBean {

    private UserData userData;
    private String screenMode;
    private Integer branchId;
    private Integer companyId;
    private String uri;

    private Integer glBankTransactionIdSeclected;

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

    private LazyDataModel<GlBankTransactionDTO> dataModel;

    @EJB
    AccountsSystemSettingsService accountsSystemSettingsService;
    @EJB
    private GlBankTransactionService glBankTransactionService;
    @EJB
    private IsagUserBankService isagUserBankService;

    @Override
    @PostConstruct
    public void init() {
        try {

            loadPer();
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "init");
        }
    }

    public void loadPer() {
        try {
            userData = new UserData();
            glBankTransaction = new GlBankTransaction();

            glBankTransactionList = new ArrayList<>();
            glBankTransactionEntitySelected = new GlBankTransactionDTO();
            glBankTransactionEntityList = new ArrayList<>();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setBranchId(userData.getUserBranch().getId());
            setCompanyId(userData.getCompany().getId());
            setAccountsSystemSettings(accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId()));

            setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());

            this.setDataModel(new LazyDataModel<GlBankTransactionDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<GlBankTransactionDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                    if (!getUri().contains("notesreceivablesListpre")) {
                        Integer bankId = selectGlBank != null ? selectGlBank.getId() : null;
                        setRowCount(glBankTransactionService.getTotalRegistors(1, userData.getUserBranch().getId(), userData.getUserlogin().getId(), filters, bankId).intValue());
                        glBankTransactionEntityList = glBankTransactionService.getALLGlBankTransactionRecievableByBranchId(1, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, bankId, userData.getUserlogin().getId());
                    } else {
                        setRowCount(glBankTransactionService.getTotalRegistorsPre(3, userData.getUserBranch().getId(), filters).intValue());
                        glBankTransactionEntityList = glBankTransactionService.getALLGlBankTransactionRecievableNotloadByBranchId(3, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters);
                    }

                    return glBankTransactionEntityList;
                }
            });
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
        }
    }

    @Override
    public void load() {
        try {
            userData = new UserData();
            glBankTransaction = new GlBankTransaction();

            glBankTransactionList = new ArrayList<>();
            glBankTransactionEntity = new GlBankTransactionEntity();
            glBankTransactionEntitySelected = new GlBankTransactionDTO();
            glBankTransactionEntityList = new ArrayList<>();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setBranchId(userData.getUserBranch().getId());
            setCompanyId(userData.getCompany().getId());
            setAccountsSystemSettings(accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId()));

            setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
            if (!getUri().contains("notesreceivablesListpre")) {
                glBankTransactionList = glBankTransactionService.getALLGlBankTransactionRecievableByBranchId(getBranchId(), 1);
            } else {
                glBankTransactionList = glBankTransactionService.getALLGlBankTransactionRecievableNotloadByBranchId(getBranchId(), 3);
            }

            for (GlBankTransaction gbt : glBankTransactionList) {
                GlBankTransactionDTO glBankTransactionEntity = new GlBankTransactionDTO();
                glBankTransactionEntity.setId(gbt.getId());
                glBankTransactionEntity.setDate(gbt.getDate() != null ? gbt.getDate() : null);
                glBankTransactionEntity.setGlBankId(gbt.getGlBankId() != null ? null : null);
                glBankTransactionEntity.setPostFlag(gbt.getPostFlag());

                if (gbt.getPaymentType() != null) {
                    glBankTransactionEntity.setPaymentType(gbt.getPaymentType());

                    if (gbt.getPaymentType() == 0) {
                        glBankTransactionEntity.setPaymentTypeName("نقدي");
                    } else if (gbt.getPaymentType() == 1) {
                        glBankTransactionEntity.setPaymentTypeName("الشيك");
                    } else {
                        glBankTransactionEntity.setPaymentTypeName("الشيك الخطي");

                    }
                }
                glBankTransactionEntity.setRemark(gbt.getRemark() != null ? gbt.getRemark() : null);
                glBankTransactionEntity.setRemark2(gbt.getRemark2());
                glBankTransactionEntity.setTransactionType(gbt.getTransactionType() != null ? gbt.getTransactionType() : null);
                glBankTransactionEntity.setValue(gbt.getValue() != null ? gbt.getValue() : null);
                glBankTransactionEntity.setValueLocal(gbt.getValueLocal() != null ? gbt.getValueLocal() : null);
                glBankTransactionEntity.setSerial(gbt.getSerial() != null ? gbt.getSerial() : null);
                glBankTransactionEntity.setCreatedByName(gbt.getCreatedBy().getName());
                glBankTransactionEntityList.add(glBankTransactionEntity);
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
        }
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            redirectPage();

            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    private void redirectPage() {
        String noteSreceivablesType = accountsSystemSettings.getNoteSreceivablesType();
        if (noteSreceivablesType.equals("ALLOW_DETAIL")) {
            if (getUri().contains("notesreceivablesListpre")) {
                exit("../notesreceivablespre/notesreceivablesdatailFormPre.xhtml");
            } else {
                exit("../notesreceivables/notesreceivablesdatailForm.xhtml");
            }
        } else {
            if (getUri().contains("notesreceivablesListpre")) {
                exit("../notesreceivablespre/notesreceivablesFormpre.xhtml");
            } else {
                exit("../notesreceivables/notesreceivablesForm.xhtml");
            }
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (glBankTransactionIdSeclected != null && glBankTransactionIdSeclected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("glBankTransactionIdSeclected", glBankTransactionIdSeclected);
                context.getSessionMap().put("ScreenMode", "Edit");
                redirectPage();
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "goToEdit");
            return null;
        }
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

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "هذا السند مرحل"));
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "delete");
        }
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {
            if (accountsSystemSettings.getNoteSreceivablesType().equals("ALLOW_DETAIL")) {
                exportPDFDetail(actionEvent);
            } else if (accountsSystemSettings.getNoteSreceivablesType().equals("NOT_ALLOW_DETAIL")) {
                exportPDFNoDetail(actionEvent);
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "exportPDF");
        }
    }

    private void exportPDFNoDetail(ActionEvent actionEvent) throws JRException, IOException {
        try {

            GlBankTransactionDTO transactionEntity = (GlBankTransactionDTO) actionEvent.getComponent().getAttributes().get("varValue");
            startPrintingProcess(transactionEntity);
            List<GlBankTransactionDetailEntity> gbtdes = new ArrayList<>();

            if (glBankTransactionEntity != null && glBankTransactionEntity.getId() != null) {
                if (getGlBankTransactionDetailEntityList() != null && !getGlBankTransactionDetailEntityList().isEmpty()) {
                    gbtdes.add(getGlBankTransactionDetailEntityList().get(0));
                    fillReport(prepareReport(), getUserData().getReportPath() + "notesReceivables.jasper", gbtdes, "pdf");
                }
            } else {
                if (getGlBankTransactionDetailEntityList() != null && !getGlBankTransactionDetailEntityList().isEmpty()) {
                    gbtdes.add(getGlBankTransactionDetailEntityList().get(0));
                    fillReport(prepareReport(), getUserData().getReportPath() + "notesReceivables.jasper", gbtdes, "pdf");
                }
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "exportPDFNoDetail");
        }
    }

    private void exportPDFDetail(ActionEvent actionEvent) throws JRException, IOException {
        try {

            GlBankTransactionDTO transactionEntity = (GlBankTransactionDTO) actionEvent.getComponent().getAttributes().get("varValue");
            startPrintingProcess(transactionEntity);
            List<GlBankTransactionDetailEntity> reportList = listDuplara();
            if (transactionEntity != null && transactionEntity.getId() != null) {
                if (reportList != null && !reportList.isEmpty()) {
                    fillReport(prepareReport(), getUserData().getReportPath() + "notesReceivablesDetial.jasper", reportList, "pdf");
                }
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "exportPDFDetail");
        }
    }

    private List<GlBankTransactionDetailEntity> listDuplara() throws CloneNotSupportedException {
        List<GlBankTransactionDetailEntity> list = new ArrayList<>();
        for (GlBankTransactionDetailEntity bankTransactionDetailEntity : getGlBankTransactionDetailEntityList()) {
            GlBankTransactionDetailEntity bean = (GlBankTransactionDetailEntity) bankTransactionDetailEntity.clone();
            if (accountsSystemSettings.getWorkWithAccounts().equals("ALLOW_ACCOUNT")) {
                bean.setAccountName(bankTransactionDetailEntity.getGlAccount().getName());
                bean.setAccountNumber(bankTransactionDetailEntity.getGlAccount().getAccNumber());
            } else if (accountsSystemSettings.getWorkWithAccounts().equals("NOT_ALLOW_ACCOUNT")) {
                bean.setAccountName(bankTransactionDetailEntity.getGlItems().getName());
                bean.setAccountNumber(bankTransactionDetailEntity.getGlItems().getSerial());
            }
            bean.setType(0);
            bean.setTotal(getValueSummary());
            list.add(bean);
        }

        for (GlBankTransactionDetailEntity bankTransactionDetailEntity : getGlBankTransactionDetailEntityList()) {
            GlBankTransactionDetailEntity bean = (GlBankTransactionDetailEntity) bankTransactionDetailEntity.clone();
            if (accountsSystemSettings.getWorkWithAccounts().equals("ALLOW_ACCOUNT")) {
                bean.setAccountName(bankTransactionDetailEntity.getGlAccount().getName());
                bean.setAccountNumber(bankTransactionDetailEntity.getGlAccount().getAccNumber());
            } else if (accountsSystemSettings.getWorkWithAccounts().equals("NOT_ALLOW_ACCOUNT")) {
                bean.setAccountName(bankTransactionDetailEntity.getGlItems().getName());
                bean.setAccountNumber(bankTransactionDetailEntity.getGlItems().getSerial());
            }
            bean.setType(1);
            bean.setTotal(getValueSummary());
            list.add(bean);
        }
        return list;
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

    public GlBankTransactionDTO getGlBankTransactionEntitySelected() {
        return glBankTransactionEntitySelected;
    }

    public void setGlBankTransactionEntitySelected(GlBankTransactionDTO glBankTransactionEntitySelected) {
        this.glBankTransactionEntitySelected = glBankTransactionEntitySelected;
    }

    public List<GlBankTransactionDTO> getGlBankTransactionEntityList() {
        return glBankTransactionEntityList;
    }

    public void setGlBankTransactionEntityList(List<GlBankTransactionDTO> glBankTransactionEntityList) {
        this.glBankTransactionEntityList = glBankTransactionEntityList;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
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
     * @return the glBankList
     */
    public List<GlBank> getGlBankList() {
        if (glBankList == null) {
            setGlBankList(isagUserBankService.getAllGlBankForUserByTypeAndBranchId(getUserData().getUser().getId(), getBranchId(), 1));
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
