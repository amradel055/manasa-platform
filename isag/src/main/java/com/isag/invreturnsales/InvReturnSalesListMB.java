/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invreturnsales;

import com.isag.businessservice.InvReturnPurchaseDetailService;
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
import com.isag.businessservice.InvReturnPurchaseService;
import com.isag.entity.InvReturnPurchase;
import com.isag.entity.InvReturnPurchaseDetail;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author WIN7
 */
@ManagedBean(name = "InvReturnSalesListMB")
@ViewScoped
public class InvReturnSalesListMB extends BaseListBean {

    /**
     * @return the invReturnPurchaseDetailList
     */
    public List<InvReturnPurchaseDetail> getInvReturnPurchaseDetailList() {
        return invReturnPurchaseDetailList;
    }

    /**
     * @param invReturnPurchaseDetailList the invReturnPurchaseDetailList to set
     */
    public void setInvReturnPurchaseDetailList(List<InvReturnPurchaseDetail> invReturnPurchaseDetailList) {
        this.invReturnPurchaseDetailList = invReturnPurchaseDetailList;
    }

    private UserData userData;
    private String screenMode;
    private Integer branchId;
    private Integer companyId;

    private Integer invReturnSalesIdSeclected;
    private List<InvReturnPurchaseDetail> invReturnPurchaseDetailList;
    private InvReturnPurchase invReturnSales;
    private List<InvReturnPurchase> invReturnSalesList;
    private InvReturnPurchase invReturnPurchaseSelected;
    private List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList;

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
            saveError(e, "InvReturnSalesListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            userData = new UserData();
            invReturnSales = new InvReturnPurchase();
            invReturnSalesList = new ArrayList<>();
            invReturnPurchaseSelected = new InvReturnPurchase();
            invPurchaseInvoiceEntityList = new ArrayList<>();
            setInvReturnPurchaseDetailList(new ArrayList<>());
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setBranchId(userData.getUserBranch().getId());
            setCompanyId(userData.getCompany().getId());

            invReturnSalesList = invReturnPurchaseService.getALLInvReturnPurchaseByBranchId(branchId, Boolean.TRUE);
            setInvReturnPurchaseDetailList(invReturnPurchaseDetailService.getAllReturnPurchaseDetailsByReturnPurchaseId(invReturnPurchaseSelected.getId()));
        } catch (Exception e) {
            saveError(e, "InvReturnSalesListMB", "load");
        }
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../invreturnsales/invreturnsalesform.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "InvReturnSalesListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (invReturnSalesIdSeclected != null && invReturnSalesIdSeclected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invPurchaseInvoiceIdSeclected", invReturnSalesIdSeclected);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../invreturnsales/invreturnsalesform.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();

            invReturnSales = new InvReturnPurchase();
            invReturnSales.setId(invReturnPurchaseSelected.getId());

            try {
                invReturnPurchaseService.deleteInvReturnPurchase(invReturnSales);
                invReturnSalesList.remove(invReturnPurchaseSelected);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
            }
        } catch (Exception e) {
            saveError(e, "InvReturnSalesListMB", "delete");
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

    public Integer getInvReturnSalesIdSeclected() {
        return invReturnSalesIdSeclected;
    }

    public void setInvReturnSalesIdSeclected(Integer invReturnSalesIdSeclected) {
        this.invReturnSalesIdSeclected = invReturnSalesIdSeclected;
    }

    public List<InvPurchaseInvoiceEntity> getInvPurchaseInvoiceEntityList() {
        return invPurchaseInvoiceEntityList;
    }

    public void setInvPurchaseInvoiceEntityList(List<InvPurchaseInvoiceEntity> invPurchaseInvoiceEntityList) {
        this.invPurchaseInvoiceEntityList = invPurchaseInvoiceEntityList;
    }

    public InvReturnPurchase getInvReturnPurchaseSelected() {
        return invReturnPurchaseSelected;
    }

    public void setInvReturnPurchaseSelected(InvReturnPurchase invReturnPurchaseSelected) {
        this.invReturnPurchaseSelected = invReturnPurchaseSelected;
    }

    public InvReturnPurchase getInvReturnSales() {
        return invReturnSales;
    }

    public void setInvReturnSales(InvReturnPurchase invReturnSales) {
        this.invReturnSales = invReturnSales;
    }

    public List<InvReturnPurchase> getInvReturnSalesList() {
        return invReturnSalesList;
    }

    public void setInvReturnSalesList(List<InvReturnPurchase> invReturnSalesList) {
        this.invReturnSalesList = invReturnSalesList;
    }
}
