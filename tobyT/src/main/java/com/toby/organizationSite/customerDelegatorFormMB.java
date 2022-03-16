/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.organizationSite;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.reports.searchBean.invOrganizationSiteSearchBean;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.toby.InventoryBasicDataForm;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author AhmedEssam
 */
@Named(value = "customerDelegatorFormMB")
@ViewScoped
public class customerDelegatorFormMB extends InventoryBasicDataForm {

    private invOrganizationSiteSearchBean organizationSiteSearchBean;
    private InventoryDelegatorDTO inventoryDelegatorDTO;
    private InvOrganizationSiteDTO invOrganizationSiteDTOSelected;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;

    @EJB
    private OrganizationSiteService organizationSiteService;

    @Override
    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
//            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//            url = request.getRequestURL().toString();
//
//            load();
//            checkVisability();
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));

        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "load");
        }
    }

    public void reset() {
        try {

            invOrganizationSiteDTOList = new ArrayList();
            invOrganizationSiteDTOSelected = new InvOrganizationSiteDTO();
            organizationSiteSearchBean = new invOrganizationSiteSearchBean();
            inventoryDelegatorDTO = null;
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "reset");
        }
    }
    
    @Override
    public void search() {
        try {
            invOrganizationSiteDTOList = organizationSiteService.getCustomerDTOFromAndTo(getUserlogin().getBranchId().getId(), organizationSiteSearchBean);
            updateEdit();
            
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "search");
        
        }
    }

    @Override
    public String save() {
        try {
            invOrganizationSiteDTOList = organizationSiteService.AddDelegatorToCustomerDTO(invOrganizationSiteDTOList, getUserData().getUser());
            // exit();
            savedMeesage(getUserData().getUserDDs().get("SAVED"));
            return "";
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "save");
            return null;
        }
    }
 
    public void addDelegator() {
        for (InvOrganizationSiteDTO dTO : invOrganizationSiteDTOList) {
            dTO.setDelegatorId(inventoryDelegatorDTO);
        }
    }

    public void updateEdit(){
        
     for (InvOrganizationSiteDTO dTO : invOrganizationSiteDTOList) {
            dTO.setMarkEdit(Boolean.FALSE);
        }
    }
    
    public String exit() {
        try {
            exit("../customer/customerForm_1.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "OrganizationSiteFormMB", "exit");
            return null;
        }
    }
   public void onRowSelectDetails(SelectEvent e) {
        try {
            invOrganizationSiteDTOSelected.setMarkEdit(Boolean.TRUE);
            e.getSource();
        } catch (Exception ex) {
            saveError(ex, "SettlementDeedFormMB", "onRowSelectDetails");
        }
    }

    public void onRowUnselect(UnselectEvent e) {
        e.getSource();
        System.out.println("Row unselected!!!");
    }
    public void deleteDetail() {
        try {

            if (invOrganizationSiteDTOSelected.getId() != null) {
                invOrganizationSiteDTOList.remove(invOrganizationSiteDTOSelected);
            }

        } catch (Exception e) {
            saveError(e, "InstContractFormMB", "deleteDetail");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the organizationSiteService
     */
    public OrganizationSiteService getOrganizationSiteService() {
        return organizationSiteService;
    }

    /**
     * @param organizationSiteService the organizationSiteService to set
     */
    public void setOrganizationSiteService(OrganizationSiteService organizationSiteService) {
        this.organizationSiteService = organizationSiteService;
    }

    /**
     * @return the inventoryDelegatorDTO
     */
    public InventoryDelegatorDTO getInventoryDelegatorDTO() {
        return inventoryDelegatorDTO;
    }

    /**
     * @param inventoryDelegatorDTO the inventoryDelegatorDTO to set
     */
    public void setInventoryDelegatorDTO(InventoryDelegatorDTO inventoryDelegatorDTO) {
        this.inventoryDelegatorDTO = inventoryDelegatorDTO;
    }

    /**
     * @return the invOrganizationSiteDTOSelected
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTOSelected() {
        return invOrganizationSiteDTOSelected;
    }

    /**
     * @param invOrganizationSiteDTOSelected the invOrganizationSiteDTOSelected
     * to set
     */
    public void setInvOrganizationSiteDTOSelected(InvOrganizationSiteDTO invOrganizationSiteDTOSelected) {
        this.invOrganizationSiteDTOSelected = invOrganizationSiteDTOSelected;
    }

    /**
     * @return the invOrganizationSiteDTOList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOList() {
        return invOrganizationSiteDTOList;
    }

    /**
     * @param invOrganizationSiteDTOList the invOrganizationSiteDTOList to set
     */
    public void setInvOrganizationSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        this.invOrganizationSiteDTOList = invOrganizationSiteDTOList;
    }

    /**
     * @return the organizationSiteSearchBean
     */
    public invOrganizationSiteSearchBean getOrganizationSiteSearchBean() {
        if (organizationSiteSearchBean == null) {
            organizationSiteSearchBean = new invOrganizationSiteSearchBean();
        }
        return organizationSiteSearchBean;
    }

    /**
     * @param organizationSiteSearchBean the organizationSiteSearchBean to set
     */
    public void setOrganizationSiteSearchBean(invOrganizationSiteSearchBean organizationSiteSearchBean) {
        this.organizationSiteSearchBean = organizationSiteSearchBean;
    }

}
