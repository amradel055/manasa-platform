/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.crmLeads;

import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.SymbolService;
import com.isag.dto.CrmCustomerInterestedProductsDTO;
import com.isag.dto.CrmEventDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.isag.CrmBasicDataForm;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author WIN7
 */
@Named(value = "crmLeadsFormDTOMB")
@ViewScoped
public class CrmLeadsFormDTOMB extends CrmBasicDataForm {

    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;

    // EJBs
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private SymbolService symbolService;

    @Override
    @PostConstruct
    public void init() {
        try {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditLead")) {
                Integer id = (Integer) getContext().getSessionMap().get("leadSelected");
                ///
                setInvOrganizationSiteDTO(organizationSiteService.findOrganizationSiteDTOById(id));
                initData();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Addlead")) {
                getInvOrganizationSiteDTO().setLeadStatus(symbolService.getSymbolByCompanyIdANDgeneralSymbolIdWithSerial(getUserData().getCompany().getId(), 57, 1));
            }

            load();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    private void initData() {
        if (getInvOrganizationSiteDTO() != null) {
            if (getInvOrganizationSiteDTO().getLearnAboutCompanyFrom() != null
                    && !getLearnAboutCompanyfromDTOList().contains(getInvOrganizationSiteDTO().getLearnAboutCompanyFrom())) {
                getLearnAboutCompanyfromDTOList().add(0, getInvOrganizationSiteDTO().getLearnAboutCompanyFrom());
            }
            if (getInvOrganizationSiteDTO().getLeadStatus() != null
                    && !getLeadStatusDTOList().contains(getInvOrganizationSiteDTO().getLeadStatus())) {
                getLeadStatusDTOList().add(0, getInvOrganizationSiteDTO().getLeadStatus());
            }
            if (getInvOrganizationSiteDTO().getAssignedTo() != null
                    && !getEmployeeDTOList().contains(getInvOrganizationSiteDTO().getAssignedTo())) {
                getEmployeeDTOList().add(0, getInvOrganizationSiteDTO().getAssignedTo());
            }
            if (getInvOrganizationSiteDTO().getCountryId() != null
                    && !getCountryIdDTOList().contains(getInvOrganizationSiteDTO().getCountryId())) {
                getCountryIdDTOList().add(0, getInvOrganizationSiteDTO().getCountryId());
            }
            if (getInvOrganizationSiteDTO().getProjectName() != null
                    && !getProjectNameDTOList().contains(getInvOrganizationSiteDTO().getProjectName())) {
                getProjectNameDTOList().add(0, getInvOrganizationSiteDTO().getProjectName());
            }
             if (getInvOrganizationSiteDTO().getWorkTypeId()!= null
                    && !getWorkTypeDTOList().contains(getInvOrganizationSiteDTO().getWorkTypeId())) {
                getWorkTypeDTOList().add(0, getInvOrganizationSiteDTO().getWorkTypeId());
            }
            eventInitData();
        }
    }

    @Override
    public void load() {
        try {

        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

     public String goToLeadsEvent() {
        try {
            if (getInvOrganizationSiteDTO() != null && getInvOrganizationSiteDTO().getId() != null) {
                goToLeadsEvent(getInvOrganizationSiteDTO().getId());
            }
            return "";
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "goToEdit");
            return null;
        }
    }

    public void addCrmCustomerInterest() {
        checkValue();
        CrmCustomerInterestedProductsDTO crmCustomerInterestedProductsDTO = new CrmCustomerInterestedProductsDTO();
        crmCustomerInterestedProductsDTO.setIndex(getIndex());
        getInvOrganizationSiteDTO().getCrmCustomerInterestedProductsDTOList().add(crmCustomerInterestedProductsDTO);
    }

    public void addActivity() {
        CrmEventDTO crmEventDTO = new CrmEventDTO();
        crmEventDTO.setIndex(getIndex());
        crmEventDTO.setCustomerId(getInvOrganizationSiteDTO());
        crmEventDTO.setStatus(getEventStatusDTOList() != null && !getEventStatusDTOList().isEmpty() ? getEventStatusDTOList().get(0) : null);
     //  crmEventDTO.setLeadStatus( getInvOrganizationSiteDTO().getCrmEventDTOList() != null && !getInvOrganizationSiteDTO().getCrmEventDTOList().isEmpty() ? getInvOrganizationSiteDTO().getCrmEventDTOList().get(getInvOrganizationSiteDTO().getCrmEventDTOList().size()-1).getLeadStatus():null);
       crmEventDTO.setLeadStatus(getInvOrganizationSiteDTO().getLeadStatus());
        getInvOrganizationSiteDTO().getCrmEventDTOList().add(crmEventDTO);

    }

    public void checkValue() {
        for (CrmCustomerInterestedProductsDTO productsDTO : getInvOrganizationSiteDTO().getCrmCustomerInterestedProductsDTOList()) {
            if (productsDTO.getItemId() != null) {
                productsDTO.setItemIdBak(productsDTO.getItemId());
            } else if (productsDTO.getItemId() == null && productsDTO.getItemIdBak() != null) {
                productsDTO.setItemId(productsDTO.getItemIdBak());
            }
            if (productsDTO.getInterestedDegree() != null) {
                productsDTO.setInterestedDegreeBak(productsDTO.getInterestedDegree());
            } else if (productsDTO.getInterestedDegree() == null && productsDTO.getInterestedDegreeBak() != null) {
                productsDTO.setInterestedDegree(productsDTO.getInterestedDegreeBak());
            }
        }
    }

    private void eventInitData() {
        for (CrmEventDTO crmEventDTO : getInvOrganizationSiteDTO().getCrmEventDTOList()) {
            if (crmEventDTO.getActivityType() != null
                    && !getActivityTypeDTOList().contains(crmEventDTO.getActivityType())) {
                getActivityTypeDTOList().add(0, crmEventDTO.getActivityType());
            }
            if (crmEventDTO.getStatus() != null
                    && !getEventStatusDTOList().contains(crmEventDTO.getStatus())) {
                getEventStatusDTOList().add(0, crmEventDTO.getStatus());
            }
        }
    }

    public void deleteCrmCustomerInterest() {
        if (getCrmCustomerInterestedProductsDTOSelected() != null) {
            if (getCrmCustomerInterestedProductsDTOSelected().getId() != null) {
                getInvOrganizationSiteDTO().getCrmCustomerInterestedProductsDTODeleteList().add(getCrmCustomerInterestedProductsDTOSelected());
            }
            getInvOrganizationSiteDTO().getCrmCustomerInterestedProductsDTOList().remove(getCrmCustomerInterestedProductsDTOSelected());
        }
    }

    public void deleteActivity() {
        if (getInvOrganizationSiteDTO() != null && getInvOrganizationSiteDTO().getCrmEventDTOSelected() != null) {
            if (getInvOrganizationSiteDTO().getCrmEventDTOSelected().getId() != null) {
                getInvOrganizationSiteDTO().getCrmEventDTODeletedList().add(getInvOrganizationSiteDTO().getCrmEventDTOSelected());
            }
            getInvOrganizationSiteDTO().getCrmEventDTOList().remove(getInvOrganizationSiteDTO().getCrmEventDTOSelected());
        }
    }

    public void dateNowSelected() {
        if (getInvOrganizationSiteDTO().getCrmEventDTOSelected().getNowDate()) {
            getInvOrganizationSiteDTO().getCrmEventDTOSelected().setStartDate(new Date());
        }
    }

    public void onEventSelect(SelectEvent selectEvent) {
        CrmEventDTO crmEventDTO = (CrmEventDTO) selectEvent.getObject();
        getInvOrganizationSiteDTO().setCrmEventDTOSelected(crmEventDTO);
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

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
