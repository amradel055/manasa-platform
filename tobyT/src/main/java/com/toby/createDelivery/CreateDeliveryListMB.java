package com.toby.createDelivery;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.toby.BaseFormBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "createDeliveryListMB")
@ViewScoped
public class CreateDeliveryListMB extends BaseFormBean {

    private InvOrganizationSiteDTO invOrganizationSiteDTOSelected;

    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOs;

    @EJB
    private OrganizationSiteService organizationSiteService;

    public CreateDeliveryListMB() {
    }

    @Override
    @PostConstruct
    public void init() {

    }

    public String goToAdd() {
        try {
            getContext().getSessionMap().put("ScreenMode", "Add");
            exit("../procreateDelivery/createDeliveryForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "createDeliveryListMB", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (invOrganizationSiteDTOSelected != null && invOrganizationSiteDTOSelected.getId() > 0) {
                getContext().getSessionMap().put("invOrganizationSiteDTOSelected", invOrganizationSiteDTOSelected.getId());
                getContext().getSessionMap().put("ScreenMode", "Edit");
                exit("../procreateDelivery/createDeliveryForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "createDeliveryListMB", "goToEdit");
            return null;
        }
    }

    public void delete() {
        try {
            organizationSiteService.deleteOrganizationSiteDTO(invOrganizationSiteDTOSelected, getUserData().getUser());

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "error ", "هناك عمليات مرتبطة بهذا الطيار عند المسح قد يحدث مشكلة"));
        }
        invOrganizationSiteDTOs = organizationSiteService.findInvOrganizeSiteID(getUserData().getUser(), 5, null);
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public InvOrganizationSiteDTO getInvOrganizationSiteDTOSelected() {
        return invOrganizationSiteDTOSelected;
    }

    public void setInvOrganizationSiteDTOSelected(InvOrganizationSiteDTO invOrganizationSiteDTOSelected) {
        this.invOrganizationSiteDTOSelected = invOrganizationSiteDTOSelected;
    }

    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOs() {
        if (invOrganizationSiteDTOs == null || invOrganizationSiteDTOs.isEmpty()) {
            invOrganizationSiteDTOs = organizationSiteService.findInvOrganizeSiteID(getUserData().getUser(), 5, null);
        }
        return invOrganizationSiteDTOs;
    }

    public void setInvOrganizationSiteDTOs(List<InvOrganizationSiteDTO> invOrganizationSiteDTOs) {
        this.invOrganizationSiteDTOs = invOrganizationSiteDTOs;
    }

}
