/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.organizationSite;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.dto.InvOrganizationSiteDTO;
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

    @EJB
    private OrganizationSiteService organizationSiteService;

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
                    if (url.contains("customer")) {
                        setRowCount(organizationSiteService.getTotalRegistors(userData.getUserBranch().getId(), filters, 0).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, 0));
                    } else if (url.contains("supplier")) {
                        setRowCount(organizationSiteService.getTotalRegistors(userData.getUserBranch().getId(), filters, 1).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, 1));
                    } else if (url.contains("contractor")) {
                        setRowCount(organizationSiteService.getTotalRegistors(userData.getUserBranch().getId(), filters, 4).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, 4));
                    } else if (url.contains("crmLeads")) {
                        setRowCount(organizationSiteService.getTotalRegistors(userData.getUserBranch().getId(), filters, 5).intValue());
                        setInvOrganizationSitesDTOList(organizationSiteService.getALLCustomerDTOByBranchIdAndType(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), userData.getUserBranch().getId(), filters, 5));
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
                    organizationSiteService.deleteOrganizationSiteDTO(invOrganizationSiteDTO,getUserData().getUserlogin());
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
}
