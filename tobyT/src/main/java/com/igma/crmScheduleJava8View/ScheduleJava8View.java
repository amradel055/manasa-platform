/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.crmScheduleJava8View;

import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.dto.CrmEventDTO;
import com.toby.toby.CrmBasicDataForm;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author WIN7
 */
@Named(value = "scheduleJava8View")
@ViewScoped
public class ScheduleJava8View extends CrmBasicDataForm {

    private ScheduleModel eventModel;

    private ScheduleEvent event = new DefaultScheduleEvent();

    private List<CrmEventDTO> listSubject;
    @EJB
    CRMEventServiceDTO cRMEventServiceDTO;

    @PostConstruct
    public void init() {
        setEventModel(new DefaultScheduleModel());
        DefaultScheduleEvent event1;
        for (CrmEventDTO crmEventDTO : getListSubject()) {
            event1 = new DefaultScheduleEvent(crmEventDTO.getSubject(), crmEventDTO.getStartDate(), crmEventDTO.getEndDate());
            event1.setId(crmEventDTO.getId().toString());
            event1.setDescription(crmEventDTO.getId().toString());
            event1.setEndDate(crmEventDTO.getEndDate());
            event1.setStartDate(crmEventDTO.getStartDate());
            event1.setTitle(crmEventDTO.getSubject());
            getEventModel().addEvent(event1);
        }
    }

    /**
     * @return the listSubject
     */
    public List<CrmEventDTO> getListSubject() {
        if (listSubject == null) {
            listSubject = new ArrayList<>();
        }
        listSubject = cRMEventServiceDTO.findCrmEventList(getUserData().getUser().getEmployeeId().getId());

        return listSubject;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        setCrmEventDTO(cRMEventServiceDTO.findById(Integer.parseInt(event.getDescription())));
    }

    public void onDateSelect(SelectEvent selectEvent) {
        setCrmEventDTO(new CrmEventDTO());
        getCrmEventDTO().setStartDate((Date) selectEvent.getObject());
    }
    
    public String goToEdit() {
        try {
            if (getCrmEventDTO() != null && getCrmEventDTO().getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("crmEventDTOSelected", getCrmEventDTO().getId());
                context.getSessionMap().put("ScreenMode", "EditcrmTaskDTO");
                exit("../crmTodoEvent/TodoEvent.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "BranchListBean", "goToEdit");
            return null;
        }

    }

    public void saveEventToDO() {
        saveEvent();
        init();
    }
    /**
     * @param listSubject the listSubject to set
     */
    public void setListSubject(List<CrmEventDTO> listSubject) {
        this.listSubject = listSubject;
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

    /**
     * @return the eventModel
     */
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    /**
     * @param eventModel the eventModel to set
     */
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

}
