/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.invNotice;

import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.InstContractDetailsService;
import com.isag.businessservice.InstContractService;
import com.isag.businessservice.InvNoticeService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.SymbolService;
import com.isag.businessservice.reports.InstTransactionService;
import com.isag.converter.GlaccountConverter;
import com.isag.converter.InstContractConverter;
import com.isag.converter.InstTransactionTempConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.dto.SymbolDTO;
import com.isag.entity.GlAccount;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import com.isag.entity.InvNotice;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.Symbol;
import com.isag.entiy.InvNoticeEntity;
import com.isag.isag.AutoComplete1;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import com.isag.views.InstTransactionTemp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author hq002
 */
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
    private Boolean installment;
    private List<InstContract> instContractList;
    private InstContractConverter instContractConverter;
    private InstTransactionTempConverter instTransactionTempConverter;
    private List<InstTransactionTemp> instTransactionViewList;
    private List<SymbolDTO> symbolDtoList;
    private SymbolDTOConverter symbolDTOConverter;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private GlAccountService accountService;
    @EJB
    private InvNoticeService invNoticeService;
    @EJB
    InstContractService instContractService;
    @EJB
    InstTransactionService instTransactionService;
    @EJB
    InstContractDetailsService instContractDetailsService;
    @EJB
    SymbolService symbolService;
    
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
            setInstallment(Boolean.FALSE);
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
                    findnotesReceivablesObject();
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
                organizationSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 1, 4);
            } else {
                organizationSiteList = organizationSiteService.getSupplierByBranchIdForSupplierAccountReportMB(getUserData().getUserBranch().getId(), 0);
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
            invNotice.setInstallmentId(invNoticeEntity.getInstContractDetail());
            if (invNoticeEntity.getNoticeNaming() != null) {
                Symbol symbol = new Symbol();
                symbol.setId(invNoticeEntity.getNoticeNaming().getId());
                invNotice.setNoticeNaming(symbol);
            }
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
            installment = invNotice.getInstallmentId() != null ? true : false;
            invNoticeEntity.setInstContractDetail(invNotice.getInstallmentId());
            invNoticeEntity.setInstContract(invNotice.getInstallmentId() != null ? invNotice.getInstallmentId().getInstContractId() : null);
            if (invNotice.getNoticeNaming() != null) {
                SymbolDTO symbolDTO = new SymbolDTO();
                symbolDTO.setId(invNotice.getNoticeNaming().getId());
                symbolDTO.setName(invNotice.getNoticeNaming().getName());
                symbolDTO.setSerial(invNotice.getNoticeNaming().getSerial());
                invNoticeEntity.setNoticeNaming(symbolDTO);
            }
            
            if (installment) {
                invNoticeEntity.setOrganizationSiteId(invNotice.getInstallmentId().getInstContractId().getOrganizationSiteId());
                loadInstContract();
            }
        } catch (Exception e) {
            saveError(e, "InvNoticeFormMB", "setEditedInvNoticeValues");
        }
        
    }
    
    public void loadInstContract() {
        try {
            if (invNoticeEntity.getOrganizationSiteId() != null) {
                instContractList = new ArrayList<>();
                instContractList = instContractService.getALLInstContractByBranchIdAndCustomerId(getUserData().getUserBranch().getId(), invNoticeEntity.getOrganizationSiteId().getId());
                instContractConverter = new InstContractConverter(instContractList);
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesFormMB", "loadInstContract");
        }
    }
    
    private void findnotesReceivablesObject() {
        try {
            if (installment) {
                updateInstContractDetails();
                updateValueOfInstallment();
                InstTransactionTemp transactionView = new InstTransactionTemp();
                transactionView.setRowNum(getIndex());
                transactionView.setDueDate(invNotice.getInstallmentId().getDueDate());
                transactionView.setInstContractId(invNotice.getInstallmentId().getInstContractId().getId());
                transactionView.setInstDetailId(invNotice.getInstallmentId().getId());
                transactionView.setInstallmentValue(invNotice.getInstallmentId().getValue());
                transactionView.setPaidValue(invNotice.getValue());
                transactionView.setRemaining(invNotice.getInstallmentId().getValue().subtract(invNotice.getValue()));
                transactionView.setSerial(invNotice.getInstallmentId().getSerial());
                invNoticeEntity.setInstTransactionTemp(transactionView);
                instTransactionViewList.add(transactionView);
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesFormMB", "findnotesReceivablesObject");
        }
    }
    
    public void updateInstContractDetails() {
        try {
            if (invNoticeEntity != null && invNoticeEntity.getInstContract() != null) {
                instTransactionViewList = new ArrayList<>();
                instTransactionViewList = instTransactionService.getInstTransactionTemp(getUserData().getUserBranch().getId(), invNoticeEntity.getInstContract().getId());
                instTransactionTempConverter = new InstTransactionTempConverter(instTransactionViewList);
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesFormMB", "updateInstContractDetails");
        }
    }
    
    public void updateValueOfInstallment() {
        try {
            if (invNoticeEntity != null && invNoticeEntity.getInstTransactionTemp() != null) {
                invNoticeEntity.setAmount(invNoticeEntity.getInstTransactionTemp().getRemaining());
                InstContractDetail contractDetail = instContractDetailsService.findInstContractDetailsById(invNoticeEntity.getInstTransactionTemp().getInstDetailId());
                invNoticeEntity.setInstContractDetail(contractDetail);
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesFormMB", "updateValueOfInstallment");
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
    
    public List<InstContract> completInstContract(String query) {
        return AutoComplete1.completInstContract(query, this.instContractList, instContractConverter);
    }
    
    public List<InstTransactionTemp> completInstTransactionTemp(String query) {
        try {
            return AutoComplete1.completInstTransactionView(query, getInstTransactionViewList(), getInstTransactionTempConverter());
        } catch (Exception e) {
            saveError(e, "notesReceivablesFormMB", "completInstTransactionView");
            return null;
        }
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
     * @return the instContractList
     */
    public List<InstContract> getInstContractList() {
        return instContractList;
    }

    /**
     * @param instContractList the instContractList to set
     */
    public void setInstContractList(List<InstContract> instContractList) {
        this.instContractList = instContractList;
    }

    /**
     * @return the instContractConverter
     */
    public InstContractConverter getInstContractConverter() {
        return instContractConverter;
    }

    /**
     * @param instContractConverter the instContractConverter to set
     */
    public void setInstContractConverter(InstContractConverter instContractConverter) {
        this.instContractConverter = instContractConverter;
    }

    /**
     * @return the instTransactionTempConverter
     */
    public InstTransactionTempConverter getInstTransactionTempConverter() {
        return instTransactionTempConverter;
    }

    /**
     * @param instTransactionTempConverter the instTransactionTempConverter to
     * set
     */
    public void setInstTransactionTempConverter(InstTransactionTempConverter instTransactionTempConverter) {
        this.instTransactionTempConverter = instTransactionTempConverter;
    }

    /**
     * @return the instTransactionViewList
     */
    public List<InstTransactionTemp> getInstTransactionViewList() {
        return instTransactionViewList;
    }

    /**
     * @param instTransactionViewList the instTransactionViewList to set
     */
    public void setInstTransactionViewList(List<InstTransactionTemp> instTransactionViewList) {
        this.instTransactionViewList = instTransactionViewList;
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
    
}
