/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.crmEvent;

import com.isag.businessservice.CRMEventServiceDTO;
import com.isag.dto.CrmEventDTO;
import com.isag.isag.CrmBasicDataForm;
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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author WIN7
 */
@Named(value = "crmEventListDTOMB")
@ViewScoped
public class CrmEventListDTOMB extends CrmBasicDataForm {

    private CrmEventDTO crmEventSelected;
    private Integer dateType;
    private Integer taskType;

    private List<CrmEventDTO> listSubject;
    private List<CrmEventDTO> listEveentMainScrren;

    @EJB
    CRMEventServiceDTO cRMEventServiceDTO;

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("TaskTypeEdit")) {
            taskType = (Integer) getContext().getSessionMap().get("TaskType");
            getContext().getSessionMap().put("TaskType", null);
        }
        load();
    }

    @Override
    public void load() {
        if (getUserData() != null && getUserData().getUser().getEmployeeId() != null && getUri() != null) {
            if (getUri().contains("dashboardEventList")) {
                this.setDataModel(new LazyDataModel<CrmEventDTO>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public List<CrmEventDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                        setRowCount(cRMEventServiceDTO.getTotalRegistorsAllEvent(getUserData().getUserBranch().getId(), filters, getCrmEventDTOSearch(), null, getTaskType()).intValue());
                        listSubject = cRMEventServiceDTO.findALLEventBank(first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, getCrmEventDTOSearch(), null, getTaskType());

                        return listSubject;
                    }
                });
            } else if (getUri().contains("TodoEventList")) {
                this.setDataModel(new LazyDataModel<CrmEventDTO>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public List<CrmEventDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                        setRowCount(cRMEventServiceDTO.getTotalRegistors(getDateType(), getUserData().getUserBranch().getId(), filters, getCrmEventDTOSearch(), getUserData().getUser().getEmployeeId().getId(), getTaskType()).intValue());

                        listSubject = cRMEventServiceDTO.findALLCrmEvent(getDateType(), first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, getCrmEventDTOSearch(), getUserData().getUser().getEmployeeId().getId(), getTaskType());

                        return listSubject;
                    }
                });
            }
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String goToEdit() {
        try {
            return goToEventEdit(crmEventSelected.getId());
        } catch (Exception e) {
            saveError(e, "crmEventListDTOMB", "goToEdit");
            return null;
        }
    }

    public String goToAdd() {
        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddEvent");
            exit("../crmTodoEvent/TodoEvent.xhtml");

        } catch (Exception e) {
            saveError(e, "crmEventListDTOMB", "goToAdd");
        }
        return "";
    }

    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (crmEventSelected != null) {
                try {
                    CrmEventDTO dTO = new CrmEventDTO();
                    dTO.setId(crmEventSelected.getId());
                    cRMEventServiceDTO.deleteCrmEventDTO(crmEventSelected.getId(), getUserData().getUserlogin());
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

    /**
     * @return the listSubject
     */
    public List<CrmEventDTO> getListSubject() {
        if (listSubject == null || listSubject.isEmpty()) {
            listSubject = new ArrayList<>();
        }
        //   listSubject = cRMEventServiceDTO.findCrmEventListt(getUserlogin().getBranchId().getId() ,getUserData().getUser().getEmployeeId().getId(),getUserlogin().getId(),getEmployeeIsSupervisor());

//the old list
//listSubject = cRMEventServiceDTO.findCrmEventListt(getCrmEventDTOSearch(), getUserData().getUser().getEmployeeId().getId(), getDateType(), getTaskType());
        return listSubject;
    }

    public void search() {
        getListSubject();
    }

    /**
     * @param listSubject the listSubject to set
     */
    public void setListSubject(List<CrmEventDTO> listSubject) {
        this.listSubject = listSubject;
    }

    /**
     * @return the crmEventSelected
     */
    public CrmEventDTO getCrmEventSelected() {
        return crmEventSelected;
    }

    /**
     * @param crmEventSelected the crmEventSelected to set
     */
    public void setCrmEventSelected(CrmEventDTO crmEventSelected) {
        this.crmEventSelected = crmEventSelected;
    }

    public void deleteRow() {
        cRMEventServiceDTO.deleteRow(crmEventSelected);

    }

    /**
     * @return the listEveentMainScrren
     */
    public List<CrmEventDTO> getListEveentMainScrren() {
        if (listEveentMainScrren == null || listEveentMainScrren.isEmpty()) {
            listEveentMainScrren = new ArrayList<>();
        }
        if (getUserData() != null && getUserData().getUser().getEmployeeId() != null) {
            listEveentMainScrren = cRMEventServiceDTO.findCrmEventListWithDate(getUserData().getUserlogin().getBranchId().getId(), getUserData().getUser().getEmployeeId().getId(), getUserData().getUser().getId(), getDateType(), getEmployeeIsSupervisor());
        }
        return listEveentMainScrren;
    }

    /**
     * @param listEveentMainScrren the listEveentMainScrren to set
     */
    public void setListEveentMainScrren(List<CrmEventDTO> listEveentMainScrren) {
        this.listEveentMainScrren = listEveentMainScrren;
    }

    /**
     * @return the dateType
     */
    public Integer getDateType() {
        if (dateType == null) {
            dateType = 0;
        }
        return dateType;
    }

    /**
     * @param dateType the dateType to set
     */
    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    /**
     * @return the taskType
     */
    public Integer getTaskType() {
        return taskType;
    }

    /**
     * @param taskType the taskType to set
     */
    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

}
