/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.crmEvent;

import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.dto.CrmEventDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.toby.CrmBasicDataForm;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author WIN7
 */
@Named(value = "crmEventFormDTOMB")
@ViewScoped
public class CrmEventFormDTOMB extends CrmBasicDataForm {

    // EJBs
    @EJB
    private CRMEventServiceDTO cRMEventServiceDTO;
    
    
    

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditcrmTaskDTO") && getUri() != null && !getUri().contains("main")) {
                Integer crmEventDTOSelected = (Integer) getContext().getSessionMap().get("crmEventDTOSelected");
                setCrmEventDTO(cRMEventServiceDTO.findById(crmEventDTOSelected));
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("addEventFromLeadsList")) {
                InvOrganizationSiteDTO crmLeadsDTOSelected = (InvOrganizationSiteDTO) getContext().getSessionMap().get("leadSelected");
                getCrmEventDTO().setCustomerId(crmLeadsDTOSelected);
            }
            initData();
            numberOfLeads();
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    public void search() {
        getCrmEventDTO();
    }

    private void initData() {
        if (getCrmEventDTO() != null) {
            if (getCrmEventDTO().getAssignedTo() != null
                    && !getEmployeeDTOList().contains(getCrmEventDTO().getAssignedTo())) {
                getEmployeeDTOList().add(0, getCrmEventDTO().getAssignedTo());
            }
            if (getCrmEventDTO().getStatus() != null
                    && !getEventStatusDTOList().contains(getCrmEventDTO().getStatus())) {
                getEventStatusDTOList().add(0, getCrmEventDTO().getStatus());
            }
            if (getCrmEventDTO().getActivityType() != null
                    && !getActivityTypeDTOList().contains(getCrmEventDTO().getActivityType())) {
                getActivityTypeDTOList().add(0, getCrmEventDTO().getActivityType());
            }
            if (getCrmEventDTO().getSymbolPriority() != null
                    && !getPriorityDTOList().contains(getCrmEventDTO().getSymbolPriority())) {
                getPriorityDTOList().add(0, getCrmEventDTO().getSymbolPriority());
            }
            if (getCrmEventDTO().getCustomerId() != null
                    && !getInvOrganizationSiteDTOListByLeaders("").contains(getCrmEventDTO().getCustomerId())) {
                getInvOrganizationSiteDTOListByLeaders("").add(0, getCrmEventDTO().getCustomerId());
            }
            if (getCrmEventDTO().getLeadStatus() != null
                    && !getLeadStatusDTOList().contains(getCrmEventDTO().getLeadStatus())) {
                getLeadStatusDTOList().add(0, getCrmEventDTO().getLeadStatus());
            }
        }
    }

    @Override
    public String save() {
        saveEvent();
        return "";
    }

    public void copy() {
        getCrmEventDTO().setId(null);
        getCrmEventDTO().setLeadStatusActive(Boolean.FALSE);
        getCrmEventDTO().setNowDate(Boolean.FALSE);
        savedMeesage("تم النسخ");
        initData();
    }

    public void resetEvent() {
        setCrmEventDTO(new CrmEventDTO());
    }

    public void resetLeads() {
        setInvOrganizationSiteDTO(null);
        getInvOrganizationSiteDTO();
    }

    public String goToEvent() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            if (getCrmEventDTO() != null && getCrmEventDTO().getId() != null) {
                context.getSessionMap().put("crmEventDTOSelected", getCrmEventDTO().getId());
                context.getSessionMap().put("ScreenMode", "EditcrmTaskDTO");
            }
            exit("../crmTodoEvent/TodoEvent.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "CrmEventFormDTOMB", "goToEvent");
            return null;
        }
    }

    public String goToEventList(Integer TaskType) {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("TaskType", TaskType);
            context.getSessionMap().put("ScreenMode", "TaskTypeEdit");
            exit("../crmTodoEvent/TodoEventList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "CrmEventFormDTOMB", "goToEvent");
            return null;
        }
    }

    public String goToLeadsList(Integer LeadsType) {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("LeadsType", LeadsType);
            context.getSessionMap().put("ScreenMode", "LeadsType");
            exit("../crmLeads/leadsList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "CrmEventFormDTOMB", "goToEvent");
            return null;
        }
    }

    public String goToLeads() {
        try {
            //   saveLead();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            if (getInvOrganizationSiteDTO() != null && getInvOrganizationSiteDTO().getId() != null) {
                context.getSessionMap().put("leadSelected", getInvOrganizationSiteDTO().getId());
                context.getSessionMap().put("ScreenMode", "EditLead");
            }
            exit("../crmLeads/leads.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "CrmEventFormDTOMB", "goToAddLeads");
            return null;
        }
    }

    public void dateNowSelected() {
        if (getCrmEventDTO().getNowDate()) {
            getCrmEventDTO().setStartDate(new Date());
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String exitCrmEvent() {
        try {
            exit("../crmTodoEvent/TodoEventList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "CrmEventFormDTOMB", "exitCrmEvent");
            return null;
        }
    }

    public String exitInvOrganizationSite() {
//        try {
//            exit("../glBank/glBankList.xhtml");
//            return "";
//        } catch (Exception e) {
//            saveError(e, "glBankFromMB", "exit");
//            return null;
//        }
        return "";
    }

}
