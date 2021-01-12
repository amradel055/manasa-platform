package com.toby.organizationSideOpeningBalance;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.entity.InvOrganizationSite;
import com.toby.entiy.OrganizationSiteOpeningBalanceEntity;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import javax.servlet.http.HttpServletRequest;

@Named(value = "organizationSiteOpeningBalanceFormMB")
@ViewScoped
public class OrganizationSiteOpeningBalanceFormMB extends BaseFormBean {

    private OrganizationSiteOpeningBalanceEntity organizationSiteOpeningBalanceEntity;
    private Integer organizationSiteId;
    private InvOrganizationSite organizationSiteEntity;
    private Integer selectedCompanyId;

    private boolean showMessage = Boolean.FALSE;
    private HttpServletRequest request;
    private String url;

    @EJB
    OrganizationSiteService organizationSiteService;

    @Override
    @PostConstruct
    public void init() {
        try {
            request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            url = request.getRequestURL().toString();
            load();
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode(((String) context.getSessionMap().get("ScreenMode")));

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetOrganizationSiteOpeningBalanceForm();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    setOrganizationSiteId(((Integer) context.getSessionMap().get("organizationSiteSelected")));

                    organizationSiteEntity = organizationSiteService.findOrganizationSiteById(getOrganizationSiteId());

                    organizationSiteOpeningBalanceEntity = mapOrganizationSiteEntityToOrganizationSiteForm(organizationSiteEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
        }
    }

    public Boolean checkValueDate() {
        try {
            if (organizationSiteOpeningBalanceEntity.getOpenBalanceDebit() != null || organizationSiteOpeningBalanceEntity.getOpenBalanceCredit() != null) {
                if (organizationSiteOpeningBalanceEntity.getOrganizationBalanceDate() == null) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "checkValueDate");
            return false;
        }
    }

    private void resetOrganizationSiteOpeningBalanceForm() {
        organizationSiteOpeningBalanceEntity = new OrganizationSiteOpeningBalanceEntity();
    }

    private OrganizationSiteOpeningBalanceEntity mapOrganizationSiteEntityToOrganizationSiteForm(InvOrganizationSite organizationSiteEntity) {
        try {
            organizationSiteOpeningBalanceEntity = new OrganizationSiteOpeningBalanceEntity();

            organizationSiteOpeningBalanceEntity.setCode(organizationSiteEntity.getCode());
            organizationSiteOpeningBalanceEntity.setId(organizationSiteEntity.getId());
            organizationSiteOpeningBalanceEntity.setOpenBalanceCredit(organizationSiteEntity.getOpenBalanceCredit());
            organizationSiteOpeningBalanceEntity.setOpenBalanceDebit(organizationSiteEntity.getOpenBalanceDebit());
            organizationSiteOpeningBalanceEntity.setOrganizationSiteName(organizationSiteEntity.getName());
            organizationSiteOpeningBalanceEntity.setOrganizationBalanceDate(organizationSiteEntity.getOpenBalanceDate());
            organizationSiteOpeningBalanceEntity.setType(organizationSiteEntity.getType());

            return organizationSiteOpeningBalanceEntity;
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "resetOrganizationSiteOpeningBalanceForm");
            return null;
        }
    }

    public void reset() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            context.getSessionMap().replace("ScreenMode", "Add");
            setScreenMode("Add");

            init();
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "reset");
        }
    }

    @Override
    public String save() {
        try {
            if (organizationSiteEntity != null && organizationSiteOpeningBalanceEntity != null && organizationSiteOpeningBalanceEntity.getId() != null) {

                organizationSiteEntity = organizationSiteService.findOrganizationSiteById(organizationSiteOpeningBalanceEntity.getId());
                organizationSiteEntity.setOpenBalanceCredit(organizationSiteOpeningBalanceEntity.getOpenBalanceCredit());
                organizationSiteEntity.setOpenBalanceDebit(organizationSiteOpeningBalanceEntity.getOpenBalanceDebit());
                organizationSiteEntity.setOpenBalanceDate(organizationSiteOpeningBalanceEntity.getOrganizationBalanceDate());

                try {
                    if (checkValueDate()) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                getUserData().getUserDDs().get("ERROR"), "يجب اختيار تاريخ"));
                        return "";
                    }
                    organizationSiteService.updateOrganizationSite(organizationSiteEntity);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
                } catch (Exception e) {
                    e.printStackTrace();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("UNIQE_SERIAL")));
                    return "";
                }
          //  exit();

                return "";
            }
            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "save");
            return null;
        }

    }

    @Override
    public String getScreenName() {
        return "";
    }

    public void exit() {
        try {
            if (url.contains("supplierOpeningBalance")) {
                exit("../supplierOpeningBalance/supplierOpenningBalanceList.xhtml");
            } else {
                exit("../customerOpeningBalance/customerOpenningBalanceList.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "exit");
        }
    }

    public OrganizationSiteOpeningBalanceEntity getOrganizationSiteOpeningBalanceEntity() {
        return organizationSiteOpeningBalanceEntity;
    }

    public void setOrganizationSiteOpeningBalanceEntity(OrganizationSiteOpeningBalanceEntity organizationSiteOpeningBalanceEntity) {
        this.organizationSiteOpeningBalanceEntity = organizationSiteOpeningBalanceEntity;
    }

    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public InvOrganizationSite getOrganizationSiteEntity() {
        return organizationSiteEntity;
    }

    public void setOrganizationSiteEntity(InvOrganizationSite organizationSiteEntity) {
        this.organizationSiteEntity = organizationSiteEntity;
    }

    public Integer getSelectedCompanyId() {
        return selectedCompanyId;
    }

    public void setSelectedCompanyId(Integer selectedCompanyId) {
        this.selectedCompanyId = selectedCompanyId;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }
}
