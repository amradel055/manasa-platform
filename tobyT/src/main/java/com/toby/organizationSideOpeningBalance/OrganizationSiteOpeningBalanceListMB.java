package com.toby.organizationSideOpeningBalance;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entiy.OrganizationSiteOpeningBalanceEntity;
import com.toby.toby.BaseListBean;

@Named(value = "organizationSiteOpeningBalanceListMB")
@ViewScoped
public class OrganizationSiteOpeningBalanceListMB extends BaseListBean {

    private List<OrganizationSiteOpeningBalanceEntity> organizationSideOpeningBalanceEntityList;

    private Integer organizationSiteOpeningBalanceId;

    private List<InvOrganizationSiteDTO> organizationSiteList;

    @EJB
    private OrganizationSiteService organizationSiteService;

    @Override
    @PostConstruct
    public void init() {
        try {

        } catch (Exception e) {
            saveError(e, "OrganizationSiteOpeningBalanceListMB", "init");
        }
    }

    @Override
    public void load() {
        try {

        } catch (Exception e) {
            saveError(e, "OrganizationSiteOpeningBalanceListMB", "load");
        }
    }

    public void saveAll() {
        try {
            String error = organizationSiteService.saveOpenningBalance(organizationSiteList);
            if (error == null || error.isEmpty()) {
                savedMeesage(getUserData(), getUserData().getUserDDs().get("SAVED"));
            } else {
                errorMessage(getUserData(), error);
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteOpeningBalanceListMB", "load");
        }
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit();
            return "";
        } catch (Exception e) {
            saveError(e, "OrganizationSiteOpeningBalanceListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (organizationSiteOpeningBalanceId != null && organizationSiteOpeningBalanceId > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("organizationSiteSelected", organizationSiteOpeningBalanceId);
                context.getSessionMap().put("ScreenMode", "Edit");
                exit();
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteOpeningBalanceListMB", "goToEdit");
            return null;
        }

    }

    @Override
    public void delete() {

    }

    @Override
    public void filter() {
    }

    @Override
    public String getScreenName() {
        return null;
    }

    public void exit() {
        try {
            if (getUri().contains("supplierOpeningBalance")) {
                exit("../supplierOpeningBalance/supplierOpenningBalanceForm.xhtml");
            } else {
                exit("../customerOpeningBalance/customerOpenningBalanceForm.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteOpeningBalanceListMB", "exit");
        }
    }

    public List<OrganizationSiteOpeningBalanceEntity> getOrganizationSideOpeningBalanceEntityList() {
        return organizationSideOpeningBalanceEntityList;
    }

    public void setOrganizationSideOpeningBalanceEntityList(List<OrganizationSiteOpeningBalanceEntity> organizationSideOpeningBalanceEntityList) {
        this.organizationSideOpeningBalanceEntityList = organizationSideOpeningBalanceEntityList;
    }

    public List<InvOrganizationSiteDTO> getOrganizationSiteList() {
        if (organizationSiteList == null) {
            if (getUri().contains("supplierOpeningBalance")) {
                organizationSiteList = organizationSiteService.getorganizationSiteForOpenningBalanceDTO(getUserData().getUserBranch().getId(), true, 1);
            } else {
                organizationSiteList = organizationSiteService.getorganizationSiteForOpenningBalanceDTO(getUserData().getUserBranch().getId(), true, 0);
            }
        }
        return organizationSiteList;
    }

    public void setOrganizationSiteList(List<InvOrganizationSiteDTO> organizationSiteList) {
        this.organizationSiteList = organizationSiteList;
    }

    public Integer getOrganizationSiteOpeningBalanceId() {
        return organizationSiteOpeningBalanceId;
    }

    public void setOrganizationSiteOpeningBalanceId(Integer organizationSiteOpeningBalanceId) {
        this.organizationSiteOpeningBalanceId = organizationSiteOpeningBalanceId;
    }

}
