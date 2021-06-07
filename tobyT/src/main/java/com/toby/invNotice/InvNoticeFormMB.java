/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.invNotice;

import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlBankTransactionInvoiceViewService;
import com.toby.businessservice.InvNoticeService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.SymbolService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.converter.GlaccountConverter;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.SymbolDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlAccount;
import com.toby.entity.InvNotice;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.Symbol;
import com.toby.entiy.InvNoticeEntity;
import com.toby.toby.AutoComplete1;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import com.toby.views.GlBankTransactionInvoiceView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import tafqeet.Tafqeet;

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
    private InvOrganizationSiteDTO customer;
    private InvNoticeEntity invNoticeEntity;
    private Integer invNoticeId;
    private Integer typeFlow;
    private InvNotice invNotice;
    private String uri;
    private Boolean installment;
    private List<SymbolDTO> symbolDtoList;
    private SymbolDTOConverter symbolDTOConverter;
    private List<GlBankTransactionInvoiceView> glBankTransactionInvoiceViewList;

    private InvInventoryDTOConverter gallaryDTOConverter;
    private List<InvInventoryDTO> gallaryList;
    private InvInventoryDTO gallaryDTOSelected;

    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private GlAccountService accountService;
    @EJB
    private InvNoticeService invNoticeService;
    @EJB
    SymbolService symbolService;
    @EJB
    AutoCompleteService autoCompleteService;
    @EJB
    private GlBankTransactionInvoiceViewService bankTransactionInvoiceViewService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

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
//            if (uri.contains("supplier")) {
////                organizationSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 1, 4);
//                setOrganizationSiteList(autoCompleteService.autoCompleteOrganizationSiteDTO(getUserData().getUserBranch().getId(), true, 1, ""));
//            } else {
////                organizationSiteList = organizationSiteService.getSupplierByBranchIdForSupplierAccountReportMB(getUserData().getUserBranch().getId(), 0);
//                setOrganizationSiteList(autoCompleteService.autoCompleteOrganizationSiteDTO(getUserData().getUserBranch().getId(), true, 0, ""));
//            }
//            organizationSiteConvertor = new InvOrganizationSiteConverter(getOrganizationSiteList());

            glAccountList = accountService.getBranchGLAccountsActive(getUserData().getUserBranch().getId());
            glAccountConverter = new GlaccountConverter(glAccountList);
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "fillListts");
        }
    }

    public void chooseInvoice() {
        setGlBankTransactionInvoiceViewList(bankTransactionInvoiceViewService.getGlBankTransactionInvoiceViewList(getInvNoticeEntity().getOrganizationSiteId().getId(), getUserData().getUser()));
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
            if (invNoticeEntity.getInvoiceId() != null) {
                InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
                invPurchaseInvoice.setId(invNoticeEntity.getInvoiceId());
                invNotice.setInvInvoice(invPurchaseInvoice);
            }

            if (invNoticeEntity.getType() != null) {
                invNotice.setType(invNoticeEntity.getType());
            } else {
                errorMessage("يجب اختيار نوع الاشعار");
                return "";
            }

            if (invNoticeEntity.getOrganizationSiteId() != null) {
                InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
                invOrganizationSite.setId(invNoticeEntity.getOrganizationSiteId().getId());
                invNotice.setOrganizationSiteId(invOrganizationSite);
            } else {
                errorMessage("يجب اختيار جهة التعامل");
                return "";
            }

            invNotice.setContrastAccountId(invNoticeEntity.getAccountId());

            Map<String, String> userDDs = getUserData().getUserDDs();
            try {
                invNotice = invNoticeService.addInvNotice(invNotice);
                invNoticeEntity.setId(invNotice.getId());
                invNoticeEntity.setSerial(invNotice.getSerial());
                if (invNotice.getInvInvoice() != null) {
                    invNoticeEntity.setInvoiceSerial(invNotice.getInvInvoice().getSerial());
                }
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

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (getInvNoticeEntity() != null && getInvNoticeEntity().getId() != null) {
            List<InvNoticeEntity> invNoticeList = new ArrayList<>();
            invNoticeList.add(getInvNoticeEntity());
            fillReport(prepareReport(), getUserData().getReportPath() + "invNotice.jasper", invNoticeList, "pdf");
            invNoticeList = null;
        } else {
            errorMessage("يجب الحفظ اولاً للطباعه");
        }
    }

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();
        if (getInvNoticeEntity() != null) {
            if (getInvNoticeEntity().getType() == 0 || getInvNoticeEntity().getType() == 2) {
                hashMap.put("reportNameText", "إشعار اضافه");
            } else if (getInvNoticeEntity().getType() == 1 || getInvNoticeEntity().getType() == 3) {
                hashMap.put("reportNameText", "إشعار خصم");
            } else if (getInvNoticeEntity().getType() == 4 || getInvNoticeEntity().getType() == 5) {
                hashMap.put("reportNameText", "إشعار تسويه");
            }
            hashMap.put("amountValueText", Tafqeet.numberToText(Double.parseDouble(getInvNoticeEntity().getAmount().toString()), getInvNoticeEntity().getAccountId().getCurrencyId().getName(), getInvNoticeEntity().getAccountId().getCurrencyId().getSubName()));
        }
        if (uri.contains("supplier")) {
            hashMap.put("invOrganizationText", "مورد");
            hashMap.put("invOrganizationToText", "الي مورد");
        } else {
            hashMap.put("invOrganizationText", "العميل");
            hashMap.put("invOrganizationToText", "الي عميل");
        }
        hashMap.put("serialText", "رقم الاشعار");
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("amountText", "المبلغ");

        hashMap.put("accountNumberText", "رقم الحساب");
        hashMap.put("accountNameText", "اسم الحساب");
        hashMap.put("remarkText", "البيان");
        return hashMap;
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
            if (invNotice.getOrganizationSiteId() != null) {;
                invNoticeEntity.setOrganizationSiteId(organizationSiteService.mapToDTOSmall(invNotice.getOrganizationSiteId()));
            }
            invNoticeEntity.setAmount(invNotice.getValue());
            invNoticeEntity.setDate(invNotice.getDate());
            invNoticeEntity.setRemarks(invNotice.getRemark());
            invNoticeEntity.setType(invNotice.getType());
            invNoticeEntity.setPostFlag(invNotice.getPostFlag());
            if (invNotice.getInvInvoice() != null) {
                invNoticeEntity.setInvoiceId(invNotice.getInvInvoice().getId());
                invNoticeEntity.setInvoiceSerial(invNotice.getInvInvoice().getSerial());
            }
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "setEditedInvNoticeValues");
        }

    }

    public void fillGlaccountFrominvOrganizationSiteTo() {
        try {
            if (invNoticeEntity.getOrganizationSiteIdTo() != null && invNoticeEntity.getOrganizationSiteIdTo().getAccountId() != null) {
                if (invNoticeEntity.getOrganizationSiteIdTo().getAccountId() != null) {

                    GlAccount account = new GlAccount();
                    account.setId(invNoticeEntity.getOrganizationSiteIdTo().getAccountId().getId());
                    invNoticeEntity.setAccountId(account);
                } else {
                    errorMessage("جهه التعامل غير مربوطه علي حساب");
                }
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesFormMB", "loadInstContract");
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

    public GlaccountConverter getGlAccountConverter() {
        return glAccountConverter;
    }

    public void setGlAccountConverter(GlaccountConverter glAccountConverter) {
        this.glAccountConverter = glAccountConverter;
    }

    /**
     * @return the installment
     */
    public Boolean getInstallment() {
        return installment;
    }

    /**
     * @param installment the installment to set
     */
    public void setInstallment(Boolean installment) {
        this.installment = installment;
    }

    /**
     * @return the symbolDtoList
     */
    public List<SymbolDTO> getSymbolDtoList() {
        if (symbolDtoList == null) {
            symbolDtoList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 6);
            setSymbolDTOConverter(new SymbolDTOConverter(symbolDtoList));
        }
        return symbolDtoList;
    }

    /**
     * @param symbolDtoList the symbolDtoList to set
     */
    public void setSymbolDtoList(List<SymbolDTO> symbolDtoList) {
        this.symbolDtoList = symbolDtoList;
    }

    /**
     * @return the symbolDTOConverter
     */
    public SymbolDTOConverter getSymbolDTOConverter() {
        return symbolDTOConverter;
    }

    /**
     * @param symbolDTOConverter the symbolDTOConverter to set
     */
    public void setSymbolDTOConverter(SymbolDTOConverter symbolDTOConverter) {
        this.symbolDTOConverter = symbolDTOConverter;
    }

    /**
     * @return the glBankTransactionInvoiceViewList
     */
    public List<GlBankTransactionInvoiceView> getGlBankTransactionInvoiceViewList() {
        return glBankTransactionInvoiceViewList;
    }

    /**
     * @param glBankTransactionInvoiceViewList the
     * glBankTransactionInvoiceViewList to set
     */
    public void setGlBankTransactionInvoiceViewList(List<GlBankTransactionInvoiceView> glBankTransactionInvoiceViewList) {
        this.glBankTransactionInvoiceViewList = glBankTransactionInvoiceViewList;
    }

    /**
     * @return the gallaryDTOConverter
     */
    public InvInventoryDTOConverter getGallaryDTOConverter() {
        return gallaryDTOConverter;
    }

    /**
     * @param gallaryDTOConverter the gallaryDTOConverter to set
     */
    public void setGallaryDTOConverter(InvInventoryDTOConverter gallaryDTOConverter) {
        this.gallaryDTOConverter = gallaryDTOConverter;
    }

    /**
     * @return the gallaryList
     */
    public List<InvInventoryDTO> getGallaryList() {
        if (gallaryList == null || gallaryList.isEmpty()) {
            gallaryList = tobyUserInventoryService.getAllGallaryByUserAndBranch(getUserData().getUser());
            setGallaryDTOConverter(new InvInventoryDTOConverter(gallaryList));
        }
        return gallaryList;
    }

    /**
     * @param gallaryList the gallaryList to set
     */
    public void setGallaryList(List<InvInventoryDTO> gallaryList) {
        this.gallaryList = gallaryList;
    }

    /**
     * @return the gallaryDTOSelected
     */
    public InvInventoryDTO getGallaryDTOSelected() {
        if (gallaryDTOSelected == null) {
            gallaryDTOSelected = new InvInventoryDTO();
        }
        return gallaryDTOSelected;
    }

    /**
     * @param gallaryDTOSelected the gallaryDTOSelected to set
     */
    public void setGallaryDTOSelected(InvInventoryDTO gallaryDTOSelected) {
        this.gallaryDTOSelected = gallaryDTOSelected;
    }

}
