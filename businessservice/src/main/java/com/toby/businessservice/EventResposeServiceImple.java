/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.EventResponseDTO;
import com.toby.entity.CrmEvent;
import com.toby.entity.Employee;
import com.toby.entity.EventResponse;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class EventResposeServiceImple implements EventResponseService {

    @EJB
    private GenericDAO dao;

    private EventResponseDTO mapToDTO(EventResponse entity) {
        EventResponseDTO dTO = new EventResponseDTO();
        dTO.setAssignto(entity.getAssignTo().getId());
        dTO.setAssignToName(entity.getAssignTo().getName());
        dTO.setComment(entity.getComment());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        dTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dTO.setCreatedDate(entity.getCreationDate());
        return dTO;

    }

    private EventResponse mapToEntity(EventResponseDTO dTO, TobyUser tobyUser) {
        EventResponse entity = new EventResponse();

        entity.setComment(dTO.getComment());

        Employee employee = new Employee();
        employee.setId(dTO.getAssignto());
        entity.setAssignTo(employee);

        CrmEvent crmEvent = new CrmEvent();
        crmEvent.setId(dTO.getEventId());
        entity.setEventId(crmEvent);

        if (dTO.getId() == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);

        }
        entity.setId(dTO.getId());
        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;

    }

    public void save(EventResponseDTO eventResponseDTO, TobyUser tobyUser) {

        if (eventResponseDTO != null) {
            EventResponse eventResponse = mapToEntity(eventResponseDTO, tobyUser);
            dao.updateEntity(eventResponse);
        }
    }

    @Override
    public void delete(Integer eventResponseId) {
        EventResponse eventResponse = dao.findEntityById(EventResponse.class, eventResponseId);
        dao.deleteEntity(eventResponse);

    }

    @Override
    public List<EventResponseDTO> returnEventResponseDTOByEventId(Integer eventId) {
        Map<String, Object> params = new HashMap<>();
        params.put("eventId", eventId);
        List<EventResponse> eventResponse = dao.findListByQuery("SELECT e FROM EventResponse e WHERE e.eventId.id =:eventId", params);
        return returnListDTO(eventResponse);
    }

    @Override
    public List<EventResponseDTO> returnEventResponseDTO() {
        Map<String, Object> params = new HashMap<>();
        List<EventResponse> eventResponse = dao.findListByQuery("SELECT e FROM EventResponse e ");
        return returnListDTO(eventResponse);
    }

    private List<EventResponseDTO> returnListDTO(List<EventResponse> list) {
        List<EventResponseDTO> eventResponseDTOList = new ArrayList<>();
        for (EventResponse entity : list) {
            eventResponseDTOList.add(mapToDTO(entity));
        }
        return eventResponseDTOList;
    }

}
