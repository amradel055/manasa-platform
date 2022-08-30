/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.crmEvent;

import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.businessservice.EmployeeService;
import com.toby.businessservice.EventResponseService;
import com.toby.converter.EmployeeDTOConverter;
import com.toby.dto.EmployeeDTO;
import com.toby.dto.EventResponseDTO;
import com.toby.toby.CrmBasicDataForm;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author WIN7
 */
@Named(value = "crmEventFormDTOMB")
@ViewScoped
public class CrmEventFormDTOMB extends CrmBasicDataForm {

    private List<EmployeeDTO> crmEmployeeList;
    private EmployeeDTOConverter employeeDTOConverter;
    private EventResponseDTO eventResponseDTO;
    private List<EventResponseDTO> eventResponseDTOList;

    // EJBs
    @EJB
    private CRMEventServiceDTO cRMEventServiceDTO;

    @EJB
    EmployeeService employeeService;

    @EJB
    EventResponseService eventResponseService ;

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
            } 
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }

    @Override
    public String save() {
        if(getEventResponseDTO() != null){
            eventResponseService.save(getEventResponseDTO(), getUserData().getUser());
            setEventResponseDTOList(eventResponseService.returnEventResponseDTO());
        }
        return  "" ;
       
    }

    /**
     * @return the crmEmployeeList
     */
    public List<EmployeeDTO> getCrmEmployeeList() {
        if (crmEmployeeList == null || crmEmployeeList.isEmpty()) {
            crmEmployeeList = employeeService.getAllCRMEmployee(getUserData().getUserBranch().getId());
            setEmployeeDTOConverter(new EmployeeDTOConverter(crmEmployeeList));
        }
        return crmEmployeeList;
    }

    /**
     * @param crmEmployeeList the crmEmployeeList to set
     */
    public void setCrmEmployeeList(List<EmployeeDTO> crmEmployeeList) {
        this.crmEmployeeList = crmEmployeeList;
    }

    /**
     * @return the employeeDTOConverter
     */
    public EmployeeDTOConverter getEmployeeDTOConverter() {
        return employeeDTOConverter;
    }

    /**
     * @param employeeDTOConverter the employeeDTOConverter to set
     */
    public void setEmployeeDTOConverter(EmployeeDTOConverter employeeDTOConverter) {
        this.employeeDTOConverter = employeeDTOConverter;
    }

    /**
     * @return the eventResponseDTO
     */
    public EventResponseDTO getEventResponseDTO() {
        return eventResponseDTO;
    }

    /**
     * @param eventResponseDTO the eventResponseDTO to set
     */
    public void setEventResponseDTO(EventResponseDTO eventResponseDTO) {
        this.eventResponseDTO = eventResponseDTO;
    }

    /**
     * @return the eventResponseDTOList
     */
    public List<EventResponseDTO> getEventResponseDTOList() {
        if(getEventResponseDTOList() != null){
            setEventResponseDTOList(eventResponseService.returnEventResponseDTOByEventId(getCrmEventDTO().getId()));
        }
        return eventResponseDTOList;
    }

    /**
     * @param eventResponseDTOList the eventResponseDTOList to set
     */
    public void setEventResponseDTOList(List<EventResponseDTO> eventResponseDTOList) {
        this.eventResponseDTOList = eventResponseDTOList;
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
