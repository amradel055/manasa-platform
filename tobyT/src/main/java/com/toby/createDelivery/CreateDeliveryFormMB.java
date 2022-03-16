package com.toby.createDelivery;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.toby.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "createDeliveryFormMB")
@ViewScoped
public class CreateDeliveryFormMB extends BaseFormBean {

    private InvOrganizationSiteDTO invOrganizationSiteDTO;
    private Boolean showGeneralMessage;

    @EJB
    private OrganizationSiteService organizationSiteService;

    public CreateDeliveryFormMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
            try {
                Integer organizationSiteId = (Integer) getContext().getSessionMap().get("invOrganizationSiteDTOSelected");
                if (invOrganizationSiteDTO == null) {
                    invOrganizationSiteDTO = organizationSiteService.findOrganizationSiteById(organizationSiteId, getUserData().getUser());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public String save() {
        if (getInvOrganizationSiteDTO() != null) {
            invOrganizationSiteDTO.setType(5);
            invOrganizationSiteDTO = organizationSiteService.addOrganizationSiteDTO(invOrganizationSiteDTO, getUserData().getUser());
            if (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getId() != null && invOrganizationSiteDTO.getMsg()==null) {
                try {
                    getContext().getSessionMap().put("ScreenMode", "save");
                    getContext().getSessionMap().put("invOrganizationSiteDTOSelected", invOrganizationSiteDTO);
                    exit("../procreateDelivery/createDeliveryList.xhtml");
                    return "";
                } catch (Exception e) {
                    saveError(e, "createDeliveryFormMB", "save");
                    return null;
                }
            } else {
                errorMessage(invOrganizationSiteDTO !=null ?invOrganizationSiteDTO.getMsg():null);
                showGeneralMessage = true;
            }

        }
        return "";
    }

    public boolean validateCode() {
        if (invOrganizationSiteDTO != null) {
            Boolean x = organizationSiteService.validateCode(getUserData().getUser(), invOrganizationSiteDTO.getId(), invOrganizationSiteDTO.getCode(), 5);
            if (!x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "الكود موجود", getUserData().getUserDDs().get("SAVED")));
                showGeneralMessage = true;
                return false;
            }
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطا فى الحفظ", getUserData().getUserDDs().get("SAVED")));
            showGeneralMessage = true;
            return false;
        }
    }

    public boolean validateName() {
        if (invOrganizationSiteDTO != null) {
            Boolean x = organizationSiteService.validateName(getUserData().getUser(), invOrganizationSiteDTO.getId(), invOrganizationSiteDTO.getName(), 5);
            if (!x) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "الاسم موجود", getUserData().getUserDDs().get("SAVED")));
                showGeneralMessage = true;
                return false;
            }
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطا فى الحفظ", getUserData().getUserDDs().get("SAVED")));
            showGeneralMessage = true;
            return false;
        }
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public InvOrganizationSiteDTO getInvOrganizationSiteDTO() {
        if (invOrganizationSiteDTO == null) {
            invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        }
        return invOrganizationSiteDTO;
    }

    public void setInvOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        this.invOrganizationSiteDTO = invOrganizationSiteDTO;
    }

    public Boolean getShowGeneralMessage() {
        return showGeneralMessage;
    }

    public void setShowGeneralMessage(Boolean showGeneralMessage) {
        this.showGeneralMessage = showGeneralMessage;
    }

}
