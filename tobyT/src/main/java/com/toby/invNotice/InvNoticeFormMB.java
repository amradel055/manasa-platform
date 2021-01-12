/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invNotice;

import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.InvNoticeService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.converter.GlaccountConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.entity.GlAccount;
import com.toby.entity.InvNotice;
import com.toby.entity.InvOrganizationSite;
import com.toby.entiy.InvNoticeEntity;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author hq002
 */
@Named(value = "invNoticeFormMB")
@ViewScoped
public class InvNoticeFormMB extends BaseFormBean {

    private List<GlAccount> glAccountList;
    private GlaccountConverter glAccountConverter;
    private GlAccount accountId;
    private List<InvOrganizationSite> organizationSiteList;
    private InvOrganizationSiteConverter organizationSiteConvertor;
    private InvOrganizationSite customer;
    private InvNoticeEntity invNoticeEntity;
    private Integer invNoticeId;
    private Integer typeFlow;
    private InvNotice invNotice;
    private String uri;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private GlAccountService accountService;
    @EJB
    private InvNoticeService invNoticeService;

    @Override
    @PostConstruct
    public void init() {
        try {

            reset();
            load();
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "init");
        }
    }

    public void reset() {
        try {

            invNotice = new InvNotice();
            invNoticeEntity = new InvNoticeEntity();
            glAccountList = new ArrayList<>();
            organizationSiteList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "reset");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

            fillListts();
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetInvNoticeData();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    invNoticeId = (Integer) context.getSessionMap().get("invNoticeEntitySelected");
                    setEditedInvNoticeValues(invNoticeId);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "load");
        }
    }

    private void fillListts() {
        try {
            if (uri.contains("supplier")) {
                organizationSiteList = organizationSiteService.getSupplierByBranchId(getUserData().getUserBranch().getId());
            } else {
                organizationSiteList = organizationSiteService.getCustomerByBranchId(getUserData().getUserBranch().getId());
            }
            organizationSiteConvertor = new InvOrganizationSiteConverter(organizationSiteList);

            glAccountList = accountService.getBranchGLAccountsActive(getUserData().getUserBranch().getId());
            glAccountConverter = new GlaccountConverter(glAccountList);
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "fillListts");
        }
    }

    @Override
    public String save() {
        try {

//        typeFlow = setType();
            if (invNotice.getId() == null) {
                invNotice.setCreatedBy(getUserData().getUser());
                invNotice.setCreationDate(new Date());
            } else {
                invNotice.setModificationDate(new Date());
                invNotice.setModifiedBy(getUserData().getUser());
            }

            invNotice.setBranchId(getUserData().getUserBranch());
            invNotice.setCompanyId(getUserData().getCompany());
            invNotice.setDate(invNoticeEntity.getDate());
            invNotice.setRemark(invNoticeEntity.getRemarks());
            invNotice.setValue(invNoticeEntity.getAmount() != null ? invNoticeEntity.getAmount() : BigDecimal.ZERO);
            invNotice.setMarkEdit(invNoticeEntity.getMarkEdit());
            if (invNoticeEntity.getType() != null) {
                invNotice.setType(invNoticeEntity.getType());
            } else {
                errorMessage("يجب اختيار نوع الاشعار");
                return "";
            }

            if (invNoticeEntity.getOrganizationSiteId() != null) {
                invNotice.setOrganizationSiteId(invNoticeEntity.getOrganizationSiteId());
            } else {
                errorMessage("يجب اختيار جهة التعامل");
                return "";
            }

            invNotice.setContrastAccountId(invNoticeEntity.getAccountId());

            Map<String, String> userDDs = getUserData().getUserDDs();
            try {
                invNotice = invNoticeService.addInvNotice(invNotice);
                invNoticeEntity.setId(invNotice.getId());
                savedMeesage(userDDs.get("SAVED"));
                // exit();

                return "";
            } catch (Exception e) {
                errorMessage("حدث خطأ اثناء التسجيل");
            }
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "save");
        }
        return "";

    }

    private Integer setType() {
        try {
            if (uri.contains("supplieraddnotice")) {
                typeFlow = 2;
            } else if (uri.contains("supplierdiscountnotice")) {
                typeFlow = 3;
            } else if (uri.contains("customeraddnotice")) {
                typeFlow = 0;
            } else if (uri.contains("customerdiscountnotice")) {
                typeFlow = 1;
            }
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "setType");
        }

        return typeFlow;
    }

    public void resetInvNoticeData() {
        try {
            invNoticeEntity = new InvNoticeEntity();
            invNoticeEntity.setDate(new Date());
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "resetInvNoticeData");
        }

    }

    public void setEditedInvNoticeValues(Integer invNoticeId) {
        try {
            invNotice = new InvNotice();
            invNotice = invNoticeService.findInvNoticeByInvNoticeId(invNoticeId);

            invNoticeEntity.setId(invNotice.getId());
            invNoticeEntity.setSerial(invNotice.getSerial());
            invNoticeEntity.setAccountId(invNotice.getContrastAccountId());
            invNoticeEntity.setOrganizationSiteId(invNotice.getOrganizationSiteId());
            invNoticeEntity.setAmount(invNotice.getValue());
            invNoticeEntity.setDate(invNotice.getDate());
            invNoticeEntity.setRemarks(invNotice.getRemark());
            invNoticeEntity.setType(invNotice.getType());
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "setEditedInvNoticeValues");
        }

    }

    @Override
    public String getScreenName() {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void exit() {
        try {
            if (uri.contains("supplieraddnotice")) {
                exit("../supplieraddnotice/suppliernoticelist.xhtml");
            } else if (uri.contains("supplierdiscountnotice")) {
                exit("../supplierdiscountnotice/supplierdiscountnoticelist.xhtml");
            } else if (uri.contains("customeraddnotice")) {
                exit("../customeraddnotice/customernoticelist.xhtml");
            } else if (uri.contains("customerdiscountnotice")) {
                exit("../customerdiscountnotice/customerdiscountnoticelist.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "exit");
        }
    }

    public List<InvOrganizationSite> completeOrganizationSite(String query) {
        List<InvOrganizationSite> filteredSuppliers = new ArrayList<>();
        try {
            List<InvOrganizationSite> supplierList = organizationSiteList;
            if (query == null || query.trim().equals("")) {

                organizationSiteConvertor = new InvOrganizationSiteConverter(supplierList);
                return supplierList;
            }

            String nameAr;
            String code;
            InvOrganizationSite supplierFilter;
            for (int i = 0; i < organizationSiteList.size(); i++) {
                supplierFilter = supplierList.get(i);
                nameAr = supplierFilter.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }

                code = supplierFilter.getCode();
                if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredSuppliers.contains(supplierFilter)) {
                        filteredSuppliers.add(supplierFilter);
                    }
                }
            }

            organizationSiteConvertor = new InvOrganizationSiteConverter(filteredSuppliers);
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "completeOrganizationSite");
        }
        return filteredSuppliers;
    }

    public List<GlAccount> completeGlAccount(String query) {
        List<GlAccount> filteredGlaccounts = new ArrayList<>();
        try {
            List<GlAccount> glaccounts = glAccountList;//45
            if (query == null || query.trim().equals("")) {
                glAccountConverter = new GlaccountConverter(glaccounts);
                return glaccounts;
            }

            String nameAr;
            Integer code;
            GlAccount glaccount;
            for (int i = 0; i < glAccountList.size(); i++) {
                glaccount = glaccounts.get(i);
                nameAr = glaccount.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlaccounts.contains(glaccount)) {
                        filteredGlaccounts.add(glaccount);
                    }
                }

                code = glaccount.getAccNumber();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!filteredGlaccounts.contains(glaccount)) {
                        filteredGlaccounts.add(glaccount);
                    }
                }
            }

            glAccountConverter = new GlaccountConverter(filteredGlaccounts);
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "completeGlAccount");
        }
        return filteredGlaccounts;
    }

    /**
     * @return the glAccountList
     */
    public List<GlAccount> getGlAccountList() {
        return glAccountList;
    }

    /**
     * @param glAccountList the glAccountList to set
     */
    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
    }

    /**
     * @return the accountId
     */
    public GlAccount getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the organizationSiteList
     */
    public List<InvOrganizationSite> getOrganizationSiteList() {
        return organizationSiteList;
    }

    /**
     * @param organizationSiteList the organizationSiteList to set
     */
    public void setOrganizationSiteList(List<InvOrganizationSite> organizationSiteList) {
        this.organizationSiteList = organizationSiteList;
    }

    /**
     * @return the customer
     */
    public InvOrganizationSite getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(InvOrganizationSite customer) {
        this.customer = customer;
    }

    /**
     * @return the invNoticeEntity
     */
    public InvNoticeEntity getInvNoticeEntity() {
        return invNoticeEntity;
    }

    /**
     * @param invNoticeEntity the invNoticeEntity to set
     */
    public void setInvNoticeEntity(InvNoticeEntity invNoticeEntity) {
        this.invNoticeEntity = invNoticeEntity;
    }

    /**
     * @return the invNoticeId
     */
    public Integer getInvNoticeId() {
        return invNoticeId;
    }

    /**
     * @param invNoticeId the invNoticeId to set
     */
    public void setInvNoticeId(Integer invNoticeId) {
        this.invNoticeId = invNoticeId;
    }

    /**
     * @return the invNotice
     */
    public InvNotice getInvNotice() {
        return invNotice;
    }

    /**
     * @param invNotice the invNotice to set
     */
    public void setInvNotice(InvNotice invNotice) {
        this.invNotice = invNotice;
    }

    /**
     * @return the typeFlow
     */
    public Integer getTypeFlow() {
        return typeFlow;
    }

    /**
     * @param typeFlow the typeFlow to set
     */
    public void setTypeFlow(Integer typeFlow) {
        this.typeFlow = typeFlow;
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

    public InvOrganizationSiteConverter getOrganizationSiteConvertor() {
        return organizationSiteConvertor;
    }

    public void setOrganizationSiteConvertor(InvOrganizationSiteConverter organizationSiteConvertor) {
        this.organizationSiteConvertor = organizationSiteConvertor;
    }

    public GlaccountConverter getGlAccountConverter() {
        return glAccountConverter;
    }

    public void setGlAccountConverter(GlaccountConverter glAccountConverter) {
        this.glAccountConverter = glAccountConverter;
    }

}
