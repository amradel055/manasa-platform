/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.organizationSite;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.InvInventory;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
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
    private InvOrganizationSiteDTO invOrganizationSiteDTO;
    private UserData userData;
    private Integer invOrganizationSiteSelected;
    private HttpServletRequest request;
    private String url;
    private LazyDataModel<InvOrganizationSiteDTO> dataModel;

    private List<InvInventory> inventoryList;
    private List<InvInventoryDTO> gallaryList;
    private InvInventory invInventorySelection;
    private InvInventoryDTO invGallarySelection;
    private InvInventoryDTOConverter gallaryDTOConverter;
    private InvInventoryConverter invInventoryConverter;

    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

    @Override
    @PostConstruct
    public void init() {
        try {
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            url = request.getRequestURL().toString();
            load();
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
                    invGallarySelectionId = getInvGallarySelection() != null ? getInvGallarySelection().getId() : null;
                    if (url.contains("customer")) {

                        setRowCount(organizationSiteService.getTotalRegistors(userData.getUserBranch().getId(), filters, 0, invGallarySelectionId).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, 0, invGallarySelectionId));
                    } else if (url.contains("supplier")) {
                        setRowCount(organizationSiteService.getTotalRegistors(userData.getUserBranch().getId(), filters, 1, invGallarySelectionId).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, 1, invGallarySelectionId));
                    } else if (url.contains("contractor")) {
                        setRowCount(organizationSiteService.getTotalRegistors(userData.getUserBranch().getId(), filters, 4, invGallarySelectionId).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, 4, invGallarySelectionId));
                    } else if (url.contains("crmLeads")) {
                        setRowCount(organizationSiteService.getTotalRegistors(userData.getUserBranch().getId(), filters, 5, invGallarySelectionId).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, 5, invGallarySelectionId));
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
            setGallaryList(tobyUserInventoryService.getAllGallaryByUserAndBranch(getUserData().getUser()));
            if (getInventoryList() != null && !inventoryList.isEmpty()) {
                setInvInventoryConverter(new InvInventoryConverter(getInventoryList()));
            }
            if (getGallaryList() != null && !getGallaryList().isEmpty()) {
                setGallaryDTOConverter(new InvInventoryDTOConverter(getGallaryList()));
            }
            if (getInvInventorySelection().getId() == null) {
                setInvInventorySelection(getInventoryList() == null || getInventoryList().isEmpty() ? null : getInventoryList().get(0));
            }
            if (getInvGallarySelection().getId() == null) {
                setInvGallarySelection(getGallaryList() == null || getGallaryList().isEmpty() ? null : getGallaryList().get(0));
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "load");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
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
     * @return the gallaryList
     */
    public List<InvInventoryDTO> getGallaryList() {
        if (gallaryList == null) {
            gallaryList = new ArrayList<>();
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
     * @return the invGallarySelection
     */
    public InvInventoryDTO getInvGallarySelection() {
        if (invGallarySelection == null) {
            invGallarySelection = new InvInventoryDTO();
        }
        return invGallarySelection;
    }

    /**
     * @param invGallarySelection the invGallarySelection to set
     */
    public void setInvGallarySelection(InvInventoryDTO invGallarySelection) {
        this.invGallarySelection = invGallarySelection;
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
}
