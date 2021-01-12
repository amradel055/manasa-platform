/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.settlementdeed;

import com.toby.businessservice.GlBankTransactionEntityService;
import com.toby.businessservice.GlBankTransactionService;
import com.toby.entity.GlBankTransaction;
import com.toby.entiy.GlBankTransactionEntity;
import com.toby.toby.BaseGlBankListBean;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.io.IOException;
import java.io.Serializable;
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
import net.sf.jasperreports.engine.JRException;

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

    // DB entites
    private GlBankTransaction glBankTransaction;
    private GlBankTransaction glBankTransactionSelected;
    private List<GlBankTransaction> glBankTransactionList;

    // Interface Entities
    private GlBankTransactionEntity glBankTransactionEntity;
   
    private GlBankTransactionEntity glBankTransactionEntitySelected;
   
    private List<GlBankTransactionEntity> glBankTransactionEntityList;
    @EJB
    private GlBankTransactionEntityService bankTransactionEntityService;

    @EJB
    private GlBankTransactionService glBankTransactionService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "settlementDeedListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            userData = new UserData();
            glBankTransaction = new GlBankTransaction();

            glBankTransactionList = new ArrayList<>();
            glBankTransactionEntity = new GlBankTransactionEntity();
            glBankTransactionEntitySelected = new GlBankTransactionEntity();
            setGlBankTransactionSelected(new GlBankTransaction());
            glBankTransactionEntityList = new ArrayList<>();

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setBranchId(userData.getUserBranch().getId());
            setCompanyId(userData.getCompany().getId());

            glBankTransactionList = glBankTransactionService.getALLGlBankTransactionSettlmentByBranchId(getBranchId());

            for (GlBankTransaction gbt : glBankTransactionList) {
                glBankTransactionEntity = new GlBankTransactionEntity();
                glBankTransactionEntity.setId(gbt.getId());
                glBankTransactionEntity.setDate(gbt.getDate() != null ? gbt.getDate() : null);
                glBankTransactionEntity.setGlBank(gbt.getGlBankId() != null ? gbt.getGlBankId() : null);
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

                glBankTransactionEntityList.add(glBankTransactionEntity);
            }
        } catch (Exception e) {
            saveError(e, "settlementDeedListMB", "load");
        }
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../settlementdeed/settlementdeedForm.xhtml");
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
                exit("../settlementdeed/settlementdeedForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "settlementDeedListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void delete() {
        try {
            if (glBankTransactionSelected.getPostFlag() == null || (glBankTransactionSelected.getPostFlag() != null && glBankTransactionSelected.getPostFlag() != 1)) {

                Map<String, String> userDDs = userData.getUserDDs();

                Integer glBankTransactionId = glBankTransactionSelected.getId() != null ? glBankTransactionSelected.getId() : null;
                glBankTransaction = new GlBankTransaction();
                glBankTransaction.setId(glBankTransactionId);

                try {
                    glBankTransactionService.deleteGlBankTransaction(glBankTransaction);
                    glBankTransactionList.remove(glBankTransactionSelected);

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

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {

            GlBankTransactionEntity transactionEntity = (GlBankTransactionEntity) actionEvent.getComponent().getAttributes().get("varValue");
            startPrintingProcess(transactionEntity);
            if (glBankTransactionEntity != null && glBankTransactionEntity.getId() != null) {
                if (getGlBankTransactionDetailEntityList() != null && !getGlBankTransactionDetailEntityList().isEmpty()) {
                    fillReport(prepareReport(), getUserData().getReportPath() + "settlementdeed.jasper", getGlBankTransactionDetailEntityList(), "pdf");
                }
            } else {
                if (getGlBankTransactionDetailEntityList() != null && !getGlBankTransactionDetailEntityList().isEmpty()) {
                    fillReport(prepareReport(), getUserData().getReportPath() + "settlementdeed.jasper", getGlBankTransactionDetailEntityList(), "pdf");
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
        if(glBankTransactionList == null || glBankTransactionList.isEmpty()){
        glBankTransactionList = bankTransactionEntityService.getGlBankTransactionEntityList(getUserData().getUser());
        }
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

    public GlBankTransactionEntity getGlBankTransactionEntitySelected() {
        return glBankTransactionEntitySelected;
    }

    public void setGlBankTransactionEntitySelected(GlBankTransactionEntity glBankTransactionEntitySelected) {
        this.glBankTransactionEntitySelected = glBankTransactionEntitySelected;
    }

    public List<GlBankTransactionEntity> getGlBankTransactionEntityList() {
       
       
        return glBankTransactionEntityList;
    }

    public void setGlBankTransactionEntityList(List<GlBankTransactionEntity> glBankTransactionEntityList) {
        this.glBankTransactionEntityList = glBankTransactionEntityList;
    }

    /**
     * @return the glBankTransactionSelected
     */
    public GlBankTransaction getGlBankTransactionSelected() {
        return glBankTransactionSelected;
    }

    /**
     * @param glBankTransactionSelected the glBankTransactionSelected to set
     */
    public void setGlBankTransactionSelected(GlBankTransaction glBankTransactionSelected) {
        this.glBankTransactionSelected = glBankTransactionSelected;
    }

    /**
     * @return the glBankTransactionSelected
     */
    
}
