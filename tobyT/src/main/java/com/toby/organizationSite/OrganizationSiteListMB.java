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

/**
 *
 * @author hq002
 */
@Named(value = "organizationSiteListMB")
@ViewScoped
public class OrganizationSiteListMB extends BaseListBean implements Serializable {

    private List<InvOrganizationSiteDTO> invOrganizationSitesSiteDTOList;
    private InvOrganizationSiteDTO invOrganizationSiteDTO;

    private HttpServletRequest request;
    private String url;
    @EJB
    private OrganizationSiteService organizationSiteService;

    @Override
    @PostConstruct
    public void init() {
        try {
            
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "load");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (invOrganizationSitesSiteDTOList != null) {
                try {
                    organizationSiteService.deleteOrganizationSite(invOrganizationSiteDTO, getUserData().getUser());
                    invOrganizationSitesSiteDTOList.remove(invOrganizationSiteDTO);
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
            if (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getId() !=null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("invOrganizationSiteSelected", invOrganizationSiteDTO.getId());
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
     * @return the invOrganizationSitesSiteDTOList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSitesSiteDTOList() {
        if (invOrganizationSitesSiteDTOList == null || invOrganizationSitesSiteDTOList.isEmpty()) {
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            url = request.getRequestURL().toString();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));

            if (url.contains("customer")) {
                invOrganizationSitesSiteDTOList = organizationSiteService.getContractorByBranchIdForGlBankModuleDTO(getUserData().getUserBranch().getId(), false, 0, getUserData().getUser()); // its type = 0 
            } else if (url.contains("supplier")) {
                invOrganizationSitesSiteDTOList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), false, 1, getUserData().getUser());  // its type = 1 

            } else if (url.contains("contractor")) {
                invOrganizationSitesSiteDTOList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), false, 4, getUserData().getUser());  // its type = 1 

            }
            
        }
        return invOrganizationSitesSiteDTOList;
    }

    /**
     * @param invOrganizationSitesSiteDTOList the
     * invOrganizationSitesSiteDTOList to set
     */
    public void setInvOrganizationSitesSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSitesSiteDTOList) {
        this.invOrganizationSitesSiteDTOList = invOrganizationSitesSiteDTOList;
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
}
