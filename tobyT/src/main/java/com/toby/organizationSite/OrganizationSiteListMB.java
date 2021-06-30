/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.organizationSite;

import com.toby.businessservice.GlBankTransactionInvoiceViewService;
import com.toby.businessservice.OfferCompanyService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.report.SupplierAccountService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.converter.OfferCompanyDTOConverter;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.OfferCompanyDTO;
import com.toby.entity.InvInventory;
import com.toby.entiy.GlBankTransactionDetailEntity;
import com.toby.entiy.GlBankTransactionEntity;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import com.toby.views.GlBankTransactionInvoiceView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.list.AbstractLinkedList;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author hq002
 */
@Named(value = "organizationSiteListMB")
@ViewScoped
public class OrganizationSiteListMB extends BaseListBean implements Serializable {

    private List<InvOrganizationSiteDTO> invOrganizationSitesDTOList;
    private List<InvOrganizationSiteDTO> OrganizationSitesDTOList;
    private InvOrganizationSiteDTO invOrganizationSiteDTO;
    private InvOrganizationSiteConverter invOrganizationSiteConverter;
    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter;
    private Integer invOrganizationSiteSelected;
    private HttpServletRequest request;
    private String url;
    private LazyDataModel<InvOrganizationSiteDTO> dataModel;

    private List<InvInventory> inventoryList;
    private InvInventory invInventorySelection;
    private InvInventoryConverter invInventoryConverter;

    private List<OfferCompanyDTO> offerCompanyDTOList;
    private OfferCompanyDTOConverter offerCompanyDTOConverter;
    private String code;

    private GlBankTransactionDetailEntity glBankTransactionDetailEntity;
    private GlBankTransactionDetailEntity glBankTransactionDetailEntitySelected;

    private List<GlBankTransactionDetailEntity> glBankTransactionDetailEntityList;

    private List<GlBankTransactionInvoiceView> glBankTransactionInvoiceViewList;
    private GlBankTransactionEntity glBankTransactionEntity;

    private Boolean isEdit;

    @EJB
    private GlBankTransactionInvoiceViewService bankTransactionInvoiceViewService;
    @EJB
    SupplierAccountService supplierAccountService;

    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;
    @EJB
    private OfferCompanyService offerCompanyService;
    @EJB
    AutoCompleteService autoCompleteService;

    @Override
    @PostConstruct
    public void init() {
        try {
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            url = request.getRequestURL().toString();
//            load();
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setDataModel(new LazyDataModel<InvOrganizationSiteDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<InvOrganizationSiteDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    Integer invGallarySelectionId = null, invInventorySelectionId = null;
                    invInventorySelectionId = getInvInventorySelection() != null ? getInvInventorySelection().getId() : null;
                    invGallarySelectionId = getGallaryDTOSelect() != null ? getGallaryDTOSelect().getId() : null;
                    if (url.contains("customer")) {
                        setRowCount(organizationSiteService.getTotalRegistors(getUserData().getUserBranch().getId(), filters, 0, invGallarySelectionId).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, 0, invGallarySelectionId));
                    } else if (url.contains("supplier")) {
                        setRowCount(organizationSiteService.getTotalRegistors(getUserData().getUserBranch().getId(), filters, 1, invGallarySelectionId).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, 1, invGallarySelectionId));
                    } else if (url.contains("contractor")) {
                        setRowCount(organizationSiteService.getTotalRegistors(getUserData().getUserBranch().getId(), filters, 4, invGallarySelectionId).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, 4, invGallarySelectionId));
                    } else if (url.contains("crmLeads")) {
                        setRowCount(organizationSiteService.getTotalRegistors(getUserData().getUserBranch().getId(), filters, 5, invGallarySelectionId).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, 5, invGallarySelectionId));
                    }

                    return invOrganizationSitesDTOList;
                }
            });

//            if (url.contains("customer")) {
//                invOrganizationSites = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), false, 0); // its type = 0 
//            } else if (url.contains("supplier")) {
//                invOrganizationSites = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), false, 1);  // its type = 1 
//
//            } else if (url.contains("contractor")) {
//                invOrganizationSites = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), false, 4);  // its type = 1 
//
//            }
            setInventoryList(tobyUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), getUserlogin().getBranchId().getId()));
            if (getInventoryList() != null && !inventoryList.isEmpty()) {
                setInvInventoryConverter(new InvInventoryConverter(getInventoryList()));
            }
            if (getInvInventorySelection().getId() == null) {
                setInvInventorySelection(getInventoryList() == null || getInventoryList().isEmpty() ? null : getInventoryList().get(0));
            }
            if (getGallaryDTOSelect().getId() == null) {
                setGallaryDTOSelect(getGallaryLists() == null || getGallaryLists().isEmpty() ? null : getGallaryLists().get(0));
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "load");
        }
    }

    public void search() {
        try {
            if (code != null && !code.isEmpty()) {
                try {
                    setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndTypeAndCode(getCode(), getUserData().getUserBranch().getId(), 0, getGallaryDTOSelect() != null ? getGallaryDTOSelect().getId() : null));
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "delete");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (invOrganizationSiteDTO != null) {
                try {
                    organizationSiteService.deleteOrganizationSiteDTO(invOrganizationSiteDTO, getUserData().getUserlogin());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "delete");
        }
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadGallaryList() {
        try {
            if (getInvInventorySelection() != null && getInvInventorySelection().getId() != null && getInvInventorySelection().getId() > 0) {
                load();
            } else {
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "loadInventoryList");
        }

    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");

            if (url.contains("customer")) {
                exit("../customer/customerForm.xhtml");
            } else if (url.contains("supplier")) {
                exit("../supplier/supplierForm.xhtml");
            } else if (url.contains("contractor")) {
                exit("../contractor/contractorForm.xhtml");
            } else if (url.contains("crmLeads")) {
                exit("../crmLeads/leads.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (invOrganizationSiteSelected != null && invOrganizationSiteSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invOrganizationSiteSelected", invOrganizationSiteSelected);
                context.getSessionMap().put("ScreenMode", "Edit");
                if (url.contains("customer")) {
                    exit("../customer/customerForm.xhtml");
                } else if (url.contains("supplier")) {
                    exit("../supplier/supplierForm.xhtml");
                } else if (url.contains("contractor")) {
                    exit("../contractor/contractorForm.xhtml");
                }
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "goToEdit");
            return null;
        }
    }

    public void updateCompany(InvOrganizationSiteDTO invOrganizationSiteDTO2) {
        organizationSiteService.updateOrganizationSiteCompany(invOrganizationSiteDTO2);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "تم الحفظ بنجاح", "تم الحفظ بنجاح"));
    }

    public void check() {
        if (getGlBankTransactionDetailEntity().getInvOrganizationSite() != null
                && getGlBankTransactionDetailEntity().getInvOrganizationSite().getId() != null) {
            setGlBankTransactionInvoiceViewList(bankTransactionInvoiceViewService.getGlBankTransactionInvoiceViewList(getGlBankTransactionDetailEntity().getInvOrganizationSite().getId(), getUserData().getUser()));
            getGlBankTransactionEntity().setAccountStatement(supplierAccountService.getAccountForOrganizationSite(getGlBankTransactionDetailEntity().getInvOrganizationSite().getId()));
        }
    }

    public Boolean getIsEdit() {
        if (isEdit == null) {
            isEdit = Boolean.FALSE;
        }
        return isEdit;
    }

    public List<InvOrganizationSiteDTO> completeCustomerFrom(String query) {
        OrganizationSitesDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getUserData().getUserBranch().getId(), Boolean.TRUE, 0, query, (getGallaryDTOSelect() != null ? getGallaryDTOSelect().getId() : null));
        invOrganizationSiteDTOConverter = new InvOrganizationSiteDTOConverter(OrganizationSitesDTOList);
        return OrganizationSitesDTOList;

    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invOrganizationSiteSelected
     */
    public Integer getInvOrganizationSiteSelected() {
        return invOrganizationSiteSelected;
    }

    /**
     * @param invOrganizationSiteSelected the invOrganizationSiteSelected to set
     */
    public void setInvOrganizationSiteSelected(Integer invOrganizationSiteSelected) {
        this.invOrganizationSiteSelected = invOrganizationSiteSelected;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<InvOrganizationSiteDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<InvOrganizationSiteDTO> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the invOrganizationSiteDTO
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTO() {
        return invOrganizationSiteDTO;
    }

    /**
     * @param invOrganizationSiteDTO the invOrganizationSiteDTO to set
     */
    public void setInvOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        this.invOrganizationSiteDTO = invOrganizationSiteDTO;
    }

    /**
     * @return the invOrganizationSitesDTOList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSitesDTOList() {
        if (invOrganizationSitesDTOList == null || invOrganizationSitesDTOList.isEmpty()) {
            invOrganizationSitesDTOList = new ArrayList<>();
        }
        return invOrganizationSitesDTOList;
    }

    /**
     * @param invOrganizationSitesDTOList the invOrganizationSitesDTOList to set
     */
    public void setInvOrganizationSitesDTOList(List<InvOrganizationSiteDTO> invOrganizationSitesDTOList) {
        this.invOrganizationSitesDTOList = invOrganizationSitesDTOList;
    }

    /**
     * @return the inventoryList
     */
    public List<InvInventory> getInventoryList() {
        if (inventoryList == null) {
            inventoryList = new ArrayList<>();
        }
        return inventoryList;
    }

    /**
     * @param inventoryList the inventoryList to set
     */
    public void setInventoryList(List<InvInventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    /**
     * @return the invInventorySelection
     */
    public InvInventory getInvInventorySelection() {
        if (invInventorySelection == null) {
            invInventorySelection = new InvInventory();
        }
        return invInventorySelection;
    }

    /**
     * @param invInventorySelection the invInventorySelection to set
     */
    public void setInvInventorySelection(InvInventory invInventorySelection) {
        this.invInventorySelection = invInventorySelection;
    }

    /**
     * @return the invInventoryConverter
     */
    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    /**
     * @param invInventoryConverter the invInventoryConverter to set
     */
    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    /**
     * @return the offerCompanyDTOList
     */
    public List<OfferCompanyDTO> getOfferCompanyDTOList() {
        if (offerCompanyDTOList == null || offerCompanyDTOList.isEmpty()) {
            offerCompanyDTOList = offerCompanyService.getALLGlofferCompanyByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());
            setOfferCompanyDTOConverter(new OfferCompanyDTOConverter(offerCompanyDTOList));
        }
        return offerCompanyDTOList;
    }

    /**
     * @param offerCompanyDTOList the offerCompanyDTOList to set
     */
    public void setOfferCompanyDTOList(List<OfferCompanyDTO> offerCompanyDTOList) {
        this.offerCompanyDTOList = offerCompanyDTOList;
    }

    /**
     * @return the offerCompanyDTOConverter
     */
    public OfferCompanyDTOConverter getOfferCompanyDTOConverter() {
        return offerCompanyDTOConverter;
    }

    /**
     * @param offerCompanyDTOConverter the offerCompanyDTOConverter to set
     */
    public void setOfferCompanyDTOConverter(OfferCompanyDTOConverter offerCompanyDTOConverter) {
        this.offerCompanyDTOConverter = offerCompanyDTOConverter;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
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
     * @return the glBankTransactionDetailEntitySelected
     */
    public GlBankTransactionDetailEntity getGlBankTransactionDetailEntitySelected() {
        return glBankTransactionDetailEntitySelected;
    }

    /**
     * @param glBankTransactionDetailEntitySelected the
     * glBankTransactionDetailEntitySelected to set
     */
    public void setGlBankTransactionDetailEntitySelected(GlBankTransactionDetailEntity glBankTransactionDetailEntitySelected) {
        this.glBankTransactionDetailEntitySelected = glBankTransactionDetailEntitySelected;
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
     * @return the invOrganizationSiteConverter
     */
    public InvOrganizationSiteConverter getInvOrganizationSiteConverter() {
        return invOrganizationSiteConverter;
    }

    /**
     * @param invOrganizationSiteConverter the invOrganizationSiteConverter to
     * set
     */
    public void setInvOrganizationSiteConverter(InvOrganizationSiteConverter invOrganizationSiteConverter) {
        this.invOrganizationSiteConverter = invOrganizationSiteConverter;
    }

    /**
     * @return the invOrganizationSiteDTOConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrganizationSiteDTOConverter() {
        return invOrganizationSiteDTOConverter;
    }

    /**
     * @param invOrganizationSiteDTOConverter the
     * invOrganizationSiteDTOConverter to set
     */
    public void setInvOrganizationSiteDTOConverter(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter) {
        this.invOrganizationSiteDTOConverter = invOrganizationSiteDTOConverter;
    }

    /**
     * @return the OrganizationSitesDTOList
     */
    public List<InvOrganizationSiteDTO> getOrganizationSitesDTOList() {
        if (OrganizationSitesDTOList == null) {
            OrganizationSitesDTOList = new ArrayList<>();
        }
        return OrganizationSitesDTOList;
    }

    /**
     * @param OrganizationSitesDTOList the OrganizationSitesDTOList to set
     */
    public void setOrganizationSitesDTOList(List<InvOrganizationSiteDTO> OrganizationSitesDTOList) {
        this.OrganizationSitesDTOList = OrganizationSitesDTOList;
    }
}
