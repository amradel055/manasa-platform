/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.CrmEventsReportSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.ChartDTO;
import com.toby.dto.ChartDataDTO;
import com.toby.dto.CrmEventDTO;
import com.toby.dto.CrmEventEvaluationDTO;
import com.toby.dto.EmployeeDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.CrmEvent;
import com.toby.entity.Employee;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.TobyUser;
import com.toby.entity.Symbol;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class CRMEventServiceImplDTO implements CRMEventServiceDTO {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    @EJB
    private EmployeeService employeeService;
    @EJB
    private EventResponseService eventResponseService;

    @Override
    public synchronized CrmEventDTO save(CrmEventDTO crmEventDTO, TobyUser tobyUser) {
        try {
            String error = validateSave(crmEventDTO);
            if (error == null) {
                CrmEvent crmEvent;
                if (crmEventDTO != null && (crmEventDTO.getMsg() == null || crmEventDTO.getMsg().isEmpty())) {
                    crmEvent = mapToEntity(crmEventDTO, tobyUser);
                    if (crmEvent.getId() == null) {
                        Integer serial = genericService.getMaxSerialForInvoice(CrmEvent.class, crmEvent.getBranchId().getId(), null, crmEvent.getType());
                        crmEvent.setSerial(serial);
                    }
                    crmEvent = dao.updateEntity(crmEvent);

                    if (crmEvent != null && crmEvent.getLeadStatus() != null) {
                        InvOrganizationSite invOrganizationSite = crmEvent.getCustomerId();
                        invOrganizationSite.setLeadStatus(new Symbol(crmEvent.getLeadStatus().getId()));
                        dao.updateEntity(invOrganizationSite);
                    }

                    crmEventDTO = mapToDTO(crmEventDTO, crmEvent);

                }
            } else {
                crmEventDTO.setMsg(error);
            }
        } catch (Exception e) {
            crmEventDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return crmEventDTO;
    }

    @Override
    public synchronized void closeNotification(Integer id) {
        try {
            if (id != null) {
                dao.executeUpdateQuery("UPDATE CrmEvent e SET e.sendNotification = 0 WHERE e.id = " + id);
            }
        } catch (Exception e) {
            dao.rollbackQuery();
        }
    }

    @Override
    public String validateSave(CrmEventDTO crmEventDTO) {
        StringBuilder error = new StringBuilder();

        if (crmEventDTO != null) {

//            if (crmEventDTO.getActivityType() == null) {
//                error.append("\n يجب اختيار نوع النشاط");
//            }
//            if (crmEventDTO.getEndDate() == null) {
//                error.append("\n يجب اختيار تاريخ النهايه");
//            }
//            if (crmEventDTO.getStartDate() != null && crmEventDTO.getStartDate().before(new Date()) && !crmEventDTO.getNowDate() && crmEventDTO.getId() == null) {
//                Calendar calStartDate = Calendar.getInstance();
//                calStartDate.setTime(crmEventDTO.getStartDate());
//                calStartDate.set(Calendar.HOUR_OF_DAY, 0);
//                calStartDate.set(Calendar.MINUTE, 0);
//                calStartDate.set(Calendar.SECOND, 0);
//                calStartDate.set(Calendar.MILLISECOND, 0);
//
//                Calendar calNowDate = Calendar.getInstance();
//                calNowDate.setTime(new Date());
//                calNowDate.set(Calendar.HOUR_OF_DAY, 0);
//                calNowDate.set(Calendar.MINUTE, 0);
//                calNowDate.set(Calendar.SECOND, 0);
//                calNowDate.set(Calendar.MILLISECOND, 0);
//                if (calStartDate.getTime().before(calNowDate.getTime())) {
//                    error.append("\n لا يمكن اختيار تاريخ او وقت سابق");
//                }
//            }
//            if (crmEventDTO.getStartDate() == null && !crmEventDTO.getNowDate()) {
//                error.append("\n يجب اختيار تاريخ وتوقيت البدايه");
//            }
//            if (crmEventDTO.getEndTime() == null) {
//                error.append("\n يجب اختيار توقيت للنهايه");
//            }
//            if (crmEventDTO.getStartDate() == null) {
//                error.append("\n يجب اختيار تاريخ البدايه");
//            }
//            if (crmEventDTO.getStartTime() == null) {
//                error.append("\n يجب اختيار توقيت البدايه");
//            }
//            if (crmEventDTO.getSubject() == null || crmEventDTO.getSubject().isEmpty()) {
//                error.append("\n يجب ادخل اسم الموضوع");
//            }
//            if (crmEventDTO.getActivityType() == null) {
//                error.append("\n يجب اختيار نوع النشاط");
//            }
//            if (crmEventDTO.getType() == null) {
//                error.append("\n يجب اختيار نوع العمليه");
//            }
            if (crmEventDTO.getPriority() == null && crmEventDTO.getSymbolPriority() == null) {
                error.append("\n يجب تحديد أولوية الشكوي ");
            }
            if (crmEventDTO.getAssignedTo() == null) {
                error.append("\n يجب تحديد متابع ");
            }
            if (crmEventDTO.getStatus() == null) {
                error.append("\n يجب تحديد حالة الشكوي ");
            }
            if (crmEventDTO.getCustomerId() == null) {
                error.append("\n يجب اختيار  عميل");
            }

            if (crmEventDTO.getDiscription() == null || crmEventDTO.getDiscription().isEmpty()) {
                error.append("\n يجب كتابة نص للشكوي");
            }
        } else {
            error.append("لا يوجد تفاصيل");
        }

        return (error == null || error.toString().isEmpty()) ? null : error.toString();
    }

    @Override
    public List<CrmEventDTO> findALLCrmEventByAssignedToAndBranch(Integer branchId, Integer assignedTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("assignedTo", assignedTo);
        List<CrmEvent> crmEventList = dao.findListByQuery("SELECT e FROM CrmEvent e WHERE e.branchId.id = :branchId and e.assignedTo.id = :assignedTo and e.deletedBy is null order by e.startDate", params);
        return mapToDTOList(crmEventList);
    }

    @Override
    public List<CrmEventDTO> findALLCrmEventByCustomerId(Integer custumerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("custumerId", custumerId);
        List<CrmEvent> crmEventList = dao.findListByQuery("SELECT e FROM CrmEvent e WHERE e.customerId.id = :custumerId and e.deletedBy is null", params);
        return mapToDTOList(crmEventList);
    }

    @Override
    public List<CrmEventDTO> getEvents(Integer branchId, CrmEventsReportSearchBean crmEventsReportSearchBean) {

        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM CrmEvent i where i.branchId.id = :branchId ");

        appendDateFrom(crmEventsReportSearchBean, params, query);
        appendDateTo(crmEventsReportSearchBean, params, query);
        appendSerialfrom(crmEventsReportSearchBean, params, query);

        appendSerialTo(crmEventsReportSearchBean, params, query);

        appendType(crmEventsReportSearchBean, params, query);
        appendCustomerFrom(crmEventsReportSearchBean, params, query);
        appendCustomerTo(crmEventsReportSearchBean, params, query);
        appendEmpFrom(crmEventsReportSearchBean, params, query);
        appendEmpTo(crmEventsReportSearchBean, params, query);

        List<CrmEvent> details = dao.findListByQuery(query.toString(), params);

        return mapToDTOList(details);

    }

    private void appendEmpTo(CrmEventsReportSearchBean crmEventsReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmEventsReportSearchBean.getEmpTo() != null) {
            params.put("empTo", crmEventsReportSearchBean.getEmpTo().getCode());
            query.append(" and i.assignedTo.code <= :empTo ");

        }
    }

    private void appendEmpFrom(CrmEventsReportSearchBean crmEventsReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmEventsReportSearchBean.getEmpFrom() != null) {
            params.put("empFrom", crmEventsReportSearchBean.getEmpFrom().getCode());
            query.append(" and i.assignedTo.code >= :empFrom ");

        }
    }

    private void appendCustomerTo(CrmEventsReportSearchBean crmEventsReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmEventsReportSearchBean.getLeadsTo() != null) {
            params.put("customerTo", crmEventsReportSearchBean.getLeadsTo().getCode());
            query.append(" and i.customerId.code <= :customerTo ");
        }
    }

    private void appendCustomerFrom(CrmEventsReportSearchBean crmEventsReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmEventsReportSearchBean.getLeadsFrom() != null) {
            params.put("customerFrom", crmEventsReportSearchBean.getLeadsFrom().getCode());
            query.append(" and i.customerId.code >= :customerFrom ");
        }
    }

    private void appendType(CrmEventsReportSearchBean crmEventsReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmEventsReportSearchBean.getType() != null) {
            params.put("type", crmEventsReportSearchBean.getType());
            query.append(" and i.type = :type ");

        }
    }

    @Override
    public List<CrmEventDTO> getAllEvents(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<CrmEvent> details = dao.findListByQuery("SELECT i FROM CrmEvent i WHERE i.branchId.id = :branchId", params);
        return mapToDTOList(details);
    }

    private void appendSerialTo(CrmEventsReportSearchBean crmEventsReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmEventsReportSearchBean.getCrmEventDTOTo() != null) {
            params.put("serialTo", crmEventsReportSearchBean.getCrmEventDTOTo().getSerial());
            query.append(" and i.serial <= :serialTo ");

        }
    }

    private void appendSerialfrom(CrmEventsReportSearchBean crmEventsReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmEventsReportSearchBean.getCrmEventDTOFrom() != null) {
            params.put("serialFrom", crmEventsReportSearchBean.getCrmEventDTOFrom().getSerial());
            query.append(" and i.serial >=:serialFrom");
        }
    }

    private void appendDateTo(CrmEventsReportSearchBean crmEventsReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmEventsReportSearchBean.getDateTo() != null) {
            params.put("startDateTo", crmEventsReportSearchBean.getDateTo());
            query.append(" and i.startDate <= :startDateTo ");
        }
    }

    private void appendDateFrom(CrmEventsReportSearchBean crmEventsReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmEventsReportSearchBean.getDateFrom() != null) {
            params.put("startDateFrom", crmEventsReportSearchBean.getDateFrom());
            query.append(" and i.startDate >= :startDateFrom  ");

        }
    }

    @Override
    public CrmEventDTO findById(Integer crmEventId) {
        CrmEvent crmEventList = dao.findEntityById(CrmEvent.class, crmEventId);
        return mapToDTO(null, crmEventList);
    }

    @Override
    public List<CrmEventDTO> findALLCrmEventByAssignedToAndBranch(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer assignedTo) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", branchId);

//        if (type != null) {
//            filters.put("type", type);
//        }
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM CrmEvent s WHERE s.branchId.id = :branchId and s.deletedBy is null   ");

        appendFilter(filters, branchId, builder, assignedTo, type);
        builder.append(" order by s.startDate DESC");

        List<CrmEvent> crmEventList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return mapToDTOList(crmEventList);
    }

    @Override
    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters, Integer assignedTo) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(s) FROM CrmEvent s WHERE s.branchId.id = :branchId ");

        appendFilter(filters, branchId, builder, assignedTo, type);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder, Integer assignedTo, Integer type) {
        filters.put("branchId", branchId);

        if (type != null) {
            filters.put("type", type);
            builder.append(" AND s.type =:type ");
        }

        if (assignedTo != null) {
            filters.put("assignedTo", assignedTo);
            builder.append("  and s.assignedTo.id = :assignedTo ");
        }
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (s.serial like CONCAT('%',:globalFilter ,'%')  OR s.subject  like CONCAT('%',:globalFilter ,'%') OR s.discription like CONCAT('%',:globalFilter ,'%') OR s.createdBy.name like CONCAT('%',:globalFilter ,'%')) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    public List<CrmEventDTO> findALLCrmEventByAssignedToAndBranchAndType(Integer branchId, Integer type, Integer assignedTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("assignedTo", assignedTo);
        List<CrmEvent> crmEventList = dao.findListByQuery("SELECT e FROM CrmEvent e WHERE e.branchId.id = :branchId and e.assignedTo.id = :assignedTo and  e.type = :type and e.deletedBy is null order by e.startDate", params);
        return mapToDTOList(crmEventList);
    }

    public List<CrmEventDTO> findALLCrmEventByCreatedByAndBranch(Integer branchId, Integer type, Integer createdBy) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("createdBy", createdBy);
        List<CrmEvent> crmEventList = dao.findListByQuery("SELECT e FROM CrmEvent e WHERE e.branchId.id = :branchId and e.createdBy.id = :createdBy and  e.type = :type  and e.deletedBy is null order by e.startDate", params);
        return mapToDTOList(crmEventList);
    }

    @Override
    public List<CrmEventDTO> findALLCrmNotification(Integer branchId, Integer EmpId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startdate = cal.getTime();
        params.put("notificationDate", startdate);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.MILLISECOND, 0);
        Date endDate = calEnd.getTime();
        params.put("endDate", endDate);
        params.put("EmpId", EmpId);
        List<CrmEvent> crmEventList = dao.findListByQuery("SELECT e FROM CrmEvent e WHERE e.branchId.id = :branchId and e.sendNotification = true AND e.notificationDate >= :notificationDate AND e.notificationDate < :endDate AND e.deletedBy is null AND e.assignedTo.id = :EmpId order by e.notificationDate asc", params);
        return returnCrmNotificationListDTO(crmEventList);
    }

    public List<CrmEventDTO> findALLCrmEventByCreatedByANDAssignedToAndBranch(Integer branchId, Integer type, Integer createdBy, Integer assignedTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("createdBy", createdBy);
        params.put("assignedTo", assignedTo);
        List<CrmEvent> crmEventList = dao.findListByQuery("SELECT e FROM CrmEvent e WHERE e.branchId.id = :branchId and e.createdBy.id = :createdBy and e.assignedTo.id = :assignedTo and  e.type = :type  and e.deletedBy is null order by e.startDate", params);
        return mapToDTOList(crmEventList);
    }

    @Override
    public synchronized void deleteCrmEventDTO(Integer crmEventDTOId, TobyUser tobyUser) {
        if (crmEventDTOId != null && crmEventDTOId > 0) {
            dao.executeDeleteQuery("DELETE FROM EventResponse e WHERE a.eventId.id = " + crmEventDTOId);
            CrmEvent crmEvent = new CrmEvent();
            crmEvent.setId(crmEventDTOId);
            dao.deleteEntity(crmEvent);
        }
    }

    private List<CrmEventDTO> returnCrmNotificationListDTO(List<CrmEvent> list) {
        List<CrmEventDTO> dTOList = new ArrayList<>();
        for (CrmEvent entity : list) {
            CrmEventDTO dTO = new CrmEventDTO();
            dTO.setId(entity.getId());
            dTO.setNotificationTitle(entity.getNotificationTitle());
            dTO.setNotificationMessage(entity.getNotificationMessage());
            dTO.setNotificationDate(entity.getNotificationDate());
            dTO.setCloseNotification(entity.getCloseNotification());
            dTO.setNotificationOldTime(entity.getNotificationDate().after(new Date()));
            dTOList.add(dTO);
        }
        return dTOList;
    }

    private List<CrmEventDTO> returnListDTO(List<CrmEvent> list) {
        List<CrmEventDTO> dTOList = new ArrayList<>();
        for (CrmEvent entity : list) {
            dTOList.add(mapToDTO(null, entity));
        }
        return dTOList;
    }

    private List<CrmEventDTO> mapToDTOList(List<CrmEvent> crmEventList) {
        List<CrmEventDTO> crmEventDTOList = new ArrayList<>();
        if (crmEventList != null && !crmEventList.isEmpty()) {
            int index = 0;
            for (CrmEvent entity : crmEventList) {
                index++;
                CrmEventDTO crmEventDTO = initMapToDTO(entity);
                if (crmEventList.size() == index && crmEventDTO.getCloseEvent() != null) {
                    if (!crmEventDTO.getCloseEvent()) {
                        crmEventDTO.setCloseEvent(Boolean.FALSE);
                    }
                }
                crmEventDTOList.add(crmEventDTO);
            }
        }
        return crmEventDTOList;
    }

    private CrmEventDTO mapToDTO(CrmEventDTO crmEventDTO, CrmEvent entity) {
        crmEventDTO = initMapToDTO(entity);
        return crmEventDTO;
    }

    private CrmEventDTO initMapToDTO(CrmEvent entity) {

        CrmEventDTO dTO = new CrmEventDTO();
        dTO.setId(entity.getId());

        if (entity.getActivityType() != null) {
            SymbolDTO symbol = new SymbolDTO();
            symbol.setId(entity.getActivityType().getId());
            symbol.setName(entity.getActivityType().getName());
            symbol.setSerial(entity.getActivityType().getSerial());
            symbol.setIndex(entity.getActivityType().getId());
            dTO.setActivityType(symbol);
        }

        if (entity.getStatus() != null) {
            SymbolDTO symbol = new SymbolDTO();
            symbol.setId(entity.getStatus().getId());
            symbol.setName(entity.getStatus().getName());
            symbol.setSerial(entity.getStatus().getSerial());
            symbol.setIndex(entity.getStatus().getId());
            dTO.setStatus(symbol);
        }
        if (entity.getPriority() != null) {
            SymbolDTO symbol = new SymbolDTO();
            symbol.setId(entity.getPriority().getId());
            symbol.setName(entity.getPriority().getName());
            symbol.setSerial(entity.getPriority().getSerial());
            symbol.setIndex(entity.getPriority().getId());
            dTO.setPeriority(symbol);
        }
        if (entity.getCrmType() != null) {
            SymbolDTO symbol = new SymbolDTO();
            symbol.setId(entity.getCrmType().getId());
            symbol.setName(entity.getCrmType().getName());
            symbol.setSerial(entity.getCrmType().getSerial());
            symbol.setIndex(entity.getCrmType().getId());
            dTO.setCrmType(symbol);
        }
        if (entity.getAssignedTo() != null) {
            EmployeeDTO employee = new EmployeeDTO();
            employee.setId(entity.getAssignedTo().getId());
            employee.setName(entity.getAssignedTo().getName());
            employee.setCode(entity.getAssignedTo().getCode());
            dTO.setAssignedTo(employee);
            dTO.setActiveAssignedTo(Boolean.TRUE);
        }

        dTO.setEndDate(entity.getEndDate());
        dTO.setEndTime(entity.getEndTime());
        dTO.setStartDate(entity.getStartDate());
        dTO.setStartTime(entity.getStartTime());
        dTO.setSubject(entity.getSubject());

        if (entity.getActivityType() != null) {
            dTO.setSubject(entity.getActivityType().getName());
        }
        
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setType(entity.getType());
        dTO.setSerial(entity.getSerial());
        if (entity.getSendNotification() != null) {
            dTO.setSendNotification(entity.getSendNotification() == 1);
        }
        dTO.setNotificationNumber(entity.getNotificationNumber());
        dTO.setNotificationTimeType(entity.getNotificationTimeType());
        dTO.setLocation(entity.getLocation());
        dTO.setNotificationTitle(entity.getNotificationTitle());
        dTO.setNotificationMessage(entity.getNotificationMessage());
        dTO.setNotificationDate(entity.getNotificationDate());
        dTO.setCloseNotification(entity.getCloseNotification());
        dTO.setCloseEvent(entity.getCloseEvent());
        if (entity.getPriority() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setSerial(entity.getPriority().getSerial());
            symbolDTO.setName(entity.getPriority().getName());
            symbolDTO.setId(entity.getPriority().getId());
            symbolDTO.setIndex(entity.getPriority().getId());
            dTO.setSymbolPriority(symbolDTO);

        }
        if (entity.getLeadStatus() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setSerial(entity.getLeadStatus().getSerial());
            symbolDTO.setName(entity.getLeadStatus().getName());
            symbolDTO.setId(entity.getLeadStatus().getId());
            symbolDTO.setIndex(entity.getLeadStatus().getId());
            dTO.setLeadStatus(symbolDTO);

        }

        if (entity.getVisibility() != null) {
            dTO.setVisibility(entity.getVisibility() == 1);
        }
        dTO.setSendEmail(entity.getSendEmail());
        if (entity.getRepeat1() != null) {
            dTO.setRepeat1(entity.getRepeat1() == 1);
        }

        if (entity.getCustomerId() != null && entity.getCustomerId().getId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(entity.getCustomerId().getId());
            invOrganizationSiteDTO.setName(entity.getCustomerId().getName());
            invOrganizationSiteDTO.setCode(entity.getCustomerId().getCode());
            invOrganizationSiteDTO.setPhone(entity.getCustomerId().getPhone());
            invOrganizationSiteDTO.setMobile(entity.getCustomerId().getMobile());
            invOrganizationSiteDTO.setEmail(entity.getCustomerId().getEmail());
            dTO.setCustomerId(invOrganizationSiteDTO);
        }

        dTO.setRepeatNumber(entity.getRepeatNumber());
        dTO.setRepeatPeriod(entity.getRepeatPeriod());
        dTO.setRepeattimeType(entity.getTimeType());
        dTO.setDiscription(entity.getDiscription());
        dTO.setEventType(entity.getEventType());
        dTO.setLeadStatusActive(Boolean.TRUE);

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setCloseLeadStatus(Boolean.TRUE);
        dTO.setGallaryId(entity.getGalleryId());

        return dTO;
    }

    private CrmEvent mapToEntity(CrmEventDTO dTO, TobyUser tobyUser) {
        CrmEvent entity = new CrmEvent();

        if (dTO.getActivityType() != null && dTO.getActivityType().getId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getActivityType().getId());
            entity.setActivityType(symbol);
        }

        if (dTO.getStatus() != null && dTO.getStatus().getId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getStatus().getId());
            entity.setStatus(symbol);
        }
        if (dTO.getAssignedTo() != null && dTO.getAssignedTo().getId() != null) {
            Employee employee = new Employee();
            employee.setId(dTO.getAssignedTo().getId());
            entity.setAssignedTo(employee);
        }

        if (dTO.getLeadStatus() != null && dTO.getLeadStatus().getId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getLeadStatus().getId());
            entity.setLeadStatus(symbol);
        }

        if (dTO.getCrmType() != null && dTO.getCrmType().getId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getLeadStatus().getId());
            entity.setCrmType(symbol);
        }

        entity.setEndDate(dTO.getEndDate());
        entity.setEndTime(dTO.getEndTime());
        entity.setStartDate(dTO.getStartDate() == null ? new Date() : dTO.getStartDate());
        entity.setStartTime(dTO.getStartTime());

        //    entity.setSubject(dTO.getSubject());
        entity.setSubject(dTO.getActivityType() != null ? dTO.getActivityType().getName() : dTO.getSubject());
        entity.setType(dTO.getType());
        entity.setSerial(dTO.getSerial());
        entity.setRemark(dTO.getRemark());
        entity.setInvoiceId(dTO.getInvoiceId());

        if (dTO.getCustomerId() != null && dTO.getCustomerId().getId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getCustomerId().getId());
            entity.setCustomerId(invOrganizationSite);
        }

        if (dTO.getSendNotification() != null) {
            entity.setSendNotification(dTO.getSendNotification() ? 1 : 0);
        }

        entity.setNotificationNumber(dTO.getNotificationNumber());
        entity.setNotificationTimeType(dTO.getNotificationTimeType());
        entity.setLocation(dTO.getLocation());
        entity.setNotificationTitle(dTO.getActivityType() != null ? dTO.getActivityType().getName() : null);
        entity.setNotificationMessage(dTO.getCustomerId() != null ? dTO.getCustomerId().getName() : null);
        entity.setNotificationDate(dTO.getStartDate());
        entity.setCloseNotification(dTO.getSendNotification());
        entity.setCloseEvent(dTO.getCloseEvent());

        if (dTO.getSymbolPriority() != null) {
            entity.setPriority(new Symbol(dTO.getSymbolPriority().getId()));
        } else if (dTO.getPriority() != null) {
            entity.setPriority(new Symbol(dTO.getPriority()));
        }

        if (dTO.getVisibility() != null) {
            entity.setVisibility(dTO.getVisibility() ? 1 : 0);
        }
        entity.setSendEmail(dTO.getSendEmail());
        if (dTO.getRepeat1() != null) {
            entity.setRepeat1(dTO.getRepeat1() ? 1 : 0);
        }
        entity.setRepeatNumber(dTO.getRepeatNumber());
        entity.setRepeatPeriod(dTO.getRepeatPeriod());
        entity.setTimeType(dTO.getRepeattimeType());
        entity.setDiscription(dTO.getDiscription());

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

        entity.setEventType(dTO.getEventType());
        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        entity.setGalleryId(dTO.getGallaryId());
        return entity;
    }

    @Override
    public List<CrmEventDTO> findCrmEventList(Integer assignedTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("assignedTo", assignedTo);
        List<CrmEvent> crmEventList = dao.findListByQuery("SELECT e FROM CrmEvent e where e.assignedTo.id = :assignedTo AND e.deletedBy IS NULL", params);
        return mapToDTOList(crmEventList);
    }

    @Override
    public List<CrmEventDTO> findCrmEventList(Integer branchId, Integer assignedTo, Integer createdBy, Boolean employeeIsSupervisor) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder empIds = new StringBuilder();
        empIds = employeeService.returnEmployeeList(assignedTo, empIds);
        List<CrmEvent> crmEventList = dao.findListByQuery("SELECT e FROM CrmEvent e where (e.assignedTo.id in ( " + empIds + " ) ) AND e.status.serial != 0 AND e.deletedBy IS NULL AND e.eventType = false", params);
        return mapToDTOList(crmEventList);

    }

    @Override
    public int findCrmEventListtCount(Integer assignedTo, Integer type, Integer taskType, Integer branchId) {
        // List<CrmEventDTO> list = findCrmEventListt(null, assignedTo, type, taskType);

        Integer counter = (getTotalRegistors(type, branchId, new HashMap<>(), null, assignedTo, taskType).intValue());
        return counter;

    }

    @Override
    public List<CrmEventDTO> findCrmEventListt(CrmEventDTO crmEventDTOSearch, Integer assignedTo, Integer type, Integer taskType) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        StringBuilder empIds = new StringBuilder();

        query.append("SELECT e FROM CrmEvent e ");

        if (type == 1) {
            if (crmEventDTOSearch != null && crmEventDTOSearch.getAssignedTo() != null) {
                empIds = employeeService.returnEmployeeList(crmEventDTOSearch.getAssignedTo().getId(), empIds);
            } else {
                empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            }
        } else {
            empIds.append(assignedTo);
        }

        query.append(" where e.deletedBy IS NULL AND (e.assignedTo.id in ( ").append(empIds.toString()).append(" ) ) ");

        if (crmEventDTOSearch != null && crmEventDTOSearch.getActivityType() != null) {
            params.put("activityType", crmEventDTOSearch.getActivityType().getId());
            query.append(" AND e.activityType.id = :activityType");
        }

        if (crmEventDTOSearch != null && crmEventDTOSearch.getStatus() != null) {
            params.put("status", crmEventDTOSearch.getStatus().getId());
            query.append(" AND e.status.id = :status");
        } else if (taskType != null && taskType == 0) {
            query.append("  AND e.status.serial  = 1 ");
        } else if (taskType != null && taskType == 1) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startdate = cal.getTime();
            params.put("startDate", startdate);
            Date endDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);
            endCal.set(Calendar.HOUR_OF_DAY, 0);
            endCal.set(Calendar.MINUTE, 0);
            endCal.set(Calendar.SECOND, 0);
            endCal.set(Calendar.MILLISECOND, 0);
            params.put("endDate", endCal.getTime());
            query.append(" AND e.status.serial != 0 AND e.startDate >= :startDate AND e.startDate < :endDate");
        } else if (taskType != null && taskType == 2) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startdate = cal.getTime();
            params.put("startDate", startdate);
            query.append(" AND e.status.serial != 0  AND e.startDate < :startDate ");
        } else {
            query.append("  AND e.status.serial != 0 ");
        }

        query.append(" order by e.startDate asc ");

        List<CrmEvent> crmEventList = dao.findListByQuery(query.toString(), params);

        return mapToDTOList(crmEventList);

    }
//    @Override
//    public List<CrmEventDTO> findCrmEventListt(Integer branchId, Integer assignedTo, Integer createdBy, Boolean employeeIsSupervisor) {
//        Map<String, Object> params = new HashMap<>();
//
//        params.put("createdBy", createdBy);
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT e FROM CrmEvent e where ( ");
//        if (employeeIsSupervisor) {
//            List<Integer> assignedList = employeeService.getAllEmployeesListByParentAndchield(branchId, assignedTo);
//            params.put("assignedTo", assignedList);
//            builder.append(" e.assignedTo.id IN :assignedTo");
//        } else {
//            params.put("assignedTo", assignedTo);
//            builder.append(" e.assignedTo.id = :assignedTo");
//        }
//        builder.append(" OR e.createdBy.id =:createdBy) AND e.deletedBy IS NULL");
//        List<CrmEvent> crmEventList = dao.findListByQuery(builder.toString(), params);
//        return mapToDTOList(crmEventList);
//
//    }

    @Override
    public List<CrmEventDTO> findCalenderAll(Integer branchId, Integer assignedTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e FROM CrmEvent e WHERE e.branchId.id = :branchId ");
        Boolean employeesIsSuperVisor = employeeService.getAllEmployeesIsSuperVisor(assignedTo);
        if (employeesIsSuperVisor) {
//            List<Integer> employeeDTOList = employeeService.getAllEmployeesListByParentAndchield(branchId, assignedTo);
//            employeeDTOList.add(assignedTo);
            StringBuilder empIds = new StringBuilder();
            empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            builder.append(" AND e.assignedTo.id IN ( " + empIds + " ) ");
        } else {
            params.put("assignedTo", assignedTo);
            builder.append(" AND e.assignedTo.id = :assignedTo ");
        }
        builder.append(" AND e.deletedBy IS NULL");
        List<CrmEvent> crmAll = dao.findListByQuery(builder.toString(), params);
        return mapToDTOList(crmAll);
    }

    public void deleteRow(CrmEventDTO eventRow) {
        CrmEvent event = new CrmEvent();
        event.setId(eventRow.getId());
        dao.deleteEntity(event);

    }

    public Integer numberOfEvent(Integer branchId, Integer assignedTo, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        StringBuilder empIds = new StringBuilder();
        empIds = employeeService.returnEmployeeList(assignedTo, empIds);
        Long numberOfEvent = dao.findEntityByQuery("SELECT count(e.id) FROM CrmEvent e where e.branchId.id = :branchId AND e.type = :type AND e.assignedTo.id  in ( " + empIds + " ) AND e.deletedBy IS NULL", params);
        return numberOfEvent.intValue();
    }

    @Override
    public Integer numberOfEventAndAssigned(Integer branchId, Integer assignedTo, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("assignedTo", assignedTo);
        Long numberOfEvent = dao.findEntityByQuery("SELECT count(e.id) FROM CrmEvent e where e.branchId.id = :branchId AND e.type = :type AND e.assignedTo.id = :assignedTo AND e.deletedBy IS NULL", params);
        return numberOfEvent.intValue();
    }

    @Override
    public void deleteCrmEventDTOList(List<CrmEventDTO> list, TobyUser tobyUser) {
        if (list != null && !list.isEmpty()) {
            for (CrmEventDTO crmEventDTO : list) {
                if (crmEventDTO.getId() != null) {
                    dao.executeDeleteQuery("DELETE FROM EventResponse e WHERE a.eventId.id = " + crmEventDTO.getId());
                    deleteCrmEventDTO(crmEventDTO.getId(), tobyUser);
                }
            }
        }
    }

    @Override
    public List<CrmEventDTO> saveList(List<CrmEventDTO> dtoList, Integer invOrganizationSiteId, String invOrganizationSiteName, Integer assignedTo, TobyUser tobyUser) {
        List<CrmEventDTO> list = new ArrayList<>();
        if (dtoList != null && !dtoList.isEmpty()) {
            for (CrmEventDTO crmEventDTO : dtoList) {
                InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
                invOrganizationSiteDTO.setId(invOrganizationSiteId);
                invOrganizationSiteDTO.setName(invOrganizationSiteName);
                crmEventDTO.setCustomerId(invOrganizationSiteDTO);
                crmEventDTO.setAssignedTo(new EmployeeDTO(assignedTo));
                if (crmEventDTO.getStartDate() == null) {
                    crmEventDTO.setStartDate(new Date());
                }
                CrmEventDTO dto = save(crmEventDTO, tobyUser);
                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public List<CrmEventDTO> findCrmEventListWithDate(Integer branchId, Integer assignedTo, Integer createdBy, Integer type, Boolean employeeIsSupervisor) {
        Map<String, Object> params = new HashMap<>();

        params.put("createdBy", createdBy);
        if (type == 0) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startdate = cal.getTime();
            params.put("startDate", startdate);
            Date endDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);
            endCal.set(Calendar.HOUR_OF_DAY, 0);
            endCal.set(Calendar.MINUTE, 0);
            endCal.set(Calendar.SECOND, 0);
            endCal.set(Calendar.MILLISECOND, 0);
            params.put("endDate", endCal.getTime());
        } else {
            Date startdate = firstDayOfWeek(Calendar.getInstance(Locale.US)).getTime();
            Date endDate = lastDayOfWeek(Calendar.getInstance()).getTime();
            params.put("startDate", startdate);
            params.put("endDate", endDate);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e FROM CrmEvent e where (e.createdBy.id = :createdBy  ");
        if (assignedTo != null) {
            if (employeeIsSupervisor) {

//            List<Integer> assignedList = employeeService.getAllEmployeesListByParentAndchield(branchId, assignedTo);
//            params.put("assignedTo", assignedList);
                StringBuilder empIds = new StringBuilder();
                empIds = employeeService.returnEmployeeList(assignedTo, empIds);
                builder.append(" OR e.assignedTo.id IN ( " + empIds + " )");
            } else {
                params.put("assignedTo", assignedTo);
                builder.append(" OR e.assignedTo.id = :assignedTo ");
            }
        }
        builder.append(" ) AND e.deletedBy IS NULL AND e.startDate >= :startDate AND e.startDate <= :endDate AND e.status.serial != 0 ");
        List<CrmEvent> crmEventList = dao.findListByQuery(builder.toString(), params);
        return mapToDTOList(crmEventList);
    }

    private Calendar firstDayOfWeek(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        int day = cal.get(Calendar.DAY_OF_YEAR);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.set(Calendar.DAY_OF_YEAR, --day);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }

    private Calendar lastDayOfWeek(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        int day = cal.get(Calendar.DAY_OF_YEAR);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            cal.set(Calendar.DAY_OF_YEAR, ++day);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }

    @Override
    public ChartDTO getCountEventByStatus(Integer branchId, Date dateFrom, Date dateTo) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT count(c.id) , c.status.name FROM CrmEvent c WHERE c.branchId.id = :branchId ");
        if (dateFrom != null) {
            Calendar calFrom = Calendar.getInstance();
            calFrom.setTime(dateFrom);
            calFrom.set(Calendar.HOUR_OF_DAY, 0);
            calFrom.set(Calendar.MINUTE, 0);
            calFrom.set(Calendar.SECOND, 0);
            calFrom.set(Calendar.MILLISECOND, 0);
            params.put("dateFrom", calFrom.getTime());
            builder.append(" AND  c.startDate >= :dateFrom ");
        }
        if (dateTo != null) {
            Calendar calTo = Calendar.getInstance();
            calTo.setTime(dateTo);
            calTo.set(Calendar.HOUR_OF_DAY, 0);
            calTo.set(Calendar.MINUTE, 0);
            calTo.set(Calendar.SECOND, 0);
            calTo.set(Calendar.MILLISECOND, 0);
            params.put("dateTo", calTo.getTime());
            builder.append(" AND c.startDate <= :dateTo ");
        }
        builder.append(" group by c.status.id ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه المهمات حسب الحاله ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountEventByAssignedTo(Integer branchId, Date fromDate, Date toDate) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        builder.append("SELECT count(c.id) , c.assignedTo.name FROM CrmEvent c WHERE c.branchId.id = :branchId ");
        filterChartDate(params, builder, fromDate, toDate);
        builder.append("  group by c.status.id ,c.assignedTo.id ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه المهمات حسب الموظف ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountEventByStatusAndAssignedTo(Integer branchId, Integer status, Date fromDate, Date toDate) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("status", status);
        builder.append("SELECT count(c.id) , c.assignedTo.name FROM CrmEvent c WHERE c.branchId.id = :branchId AND  c.status.id = :status ");
        filterChartDate(params, builder, fromDate, toDate);
        builder.append(" group by c.assignedTo.id  ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه المهمات حسب الحاله للموظف";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountEventByActivityType(Integer branchId, Date dateFrom, Date dateTo) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(c.id) , c.activityType.name FROM CrmEvent c WHERE c.branchId.id = :branchId ");
        if (dateFrom != null) {
            Calendar calFrom = Calendar.getInstance();
            calFrom.setTime(dateFrom);
            calFrom.set(Calendar.HOUR_OF_DAY, 0);
            calFrom.set(Calendar.MINUTE, 0);
            calFrom.set(Calendar.SECOND, 0);
            calFrom.set(Calendar.MILLISECOND, 0);
            params.put("dateFrom", calFrom.getTime());
            builder.append(" AND c.startDate >= :dateFrom ");
        }
        if (dateTo != null) {
            Calendar calTo = Calendar.getInstance();
            calTo.setTime(dateTo);
            calTo.set(Calendar.HOUR_OF_DAY, 0);
            calTo.set(Calendar.MINUTE, 0);
            calTo.set(Calendar.SECOND, 0);
            calTo.set(Calendar.MILLISECOND, 0);
            params.put("dateTo", calTo.getTime());
            builder.append(" AND c.startDate <= :dateTo ");
        }
        builder.append(" group by c.activityType.id ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه المهمات حسب نوع النشاط ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountEventByPriority(Integer branchId, Date dateFrom, Date dateTo) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(c.id) , c.priority.name FROM CrmEvent c WHERE c.branchId.id = :branchId ");
        if (dateFrom != null) {
            Calendar calFrom = Calendar.getInstance();
            calFrom.setTime(dateFrom);
            calFrom.set(Calendar.HOUR_OF_DAY, 0);
            calFrom.set(Calendar.MINUTE, 0);
            calFrom.set(Calendar.SECOND, 0);
            calFrom.set(Calendar.MILLISECOND, 0);
            params.put("dateFrom", calFrom.getTime());
            builder.append(" AND c.startDate >= :dateFrom ");
        }
        if (dateTo != null) {
            Calendar calTo = Calendar.getInstance();
            calTo.setTime(dateTo);
            calTo.set(Calendar.HOUR_OF_DAY, 0);
            calTo.set(Calendar.MINUTE, 0);
            calTo.set(Calendar.SECOND, 0);
            calTo.set(Calendar.MILLISECOND, 0);
            params.put("dateTo", calTo.getTime());
            builder.append(" AND c.startDate <= :dateTo ");
        }
        builder.append(" group by c.priority.id  ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه المهمات حسب الاولويه ";
        return preperChartPie(detailses, title);
    }

    private ChartDTO preperChartPie(List<Object[]> details, String title) {
        ChartDTO chartDTO = new ChartDTO();
        int i = 0;
        for (Object[] detail : details) {
            chartDTO.getPieValues().add(((Number) detail[0]).intValue());
            chartDTO.getPieLabels().add(detail[1].toString());
            chartDTO.getPieColers().add(getColors().get(i));
            if (i < getColors().size() - 1) {
                i++;
            } else {
                i = 0;
            }
        }
        chartDTO.setTitle(title);
        return chartDTO;
    }

    private List<String> getColors() {
        List<String> colors = new ArrayList<>();
        colors.add("rgb(255, 99, 132)");//Red");
        colors.add("rgb(75, 192, 192)");//Green
        colors.add("rgb(255, 205, 86)");//Yellow
        colors.add("rgb(201, 203, 207)");//Grey
        colors.add("rgb(54, 162, 235)");//Blue
        return colors;
    }

    @Override
    public List<CrmEventDTO> findALLCrmEvent(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, CrmEventDTO crmEventDTOSearch, Integer assignedTo, Integer taskType) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e FROM CrmEvent e  where e.deletedBy IS NULL ");
        appendFilter(filters, branchId, builder, assignedTo, type, crmEventDTOSearch, taskType);
        builder.append(" order by e.startDate asc ");
        List<CrmEvent> crmEventList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return mapToDTOList(crmEventList);
    }

    @Override
    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters, CrmEventDTO crmEventDTOSearch, Integer assignedTo, Integer taskType) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(e) FROM CrmEvent e WHERE e.deletedBy IS NULL ");

        appendFilter(filters, branchId, builder, assignedTo, type, crmEventDTOSearch, taskType);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder, Integer assignedTo, Integer type, CrmEventDTO crmEventDTOSearch, Integer taskType) {
        StringBuilder empIds = new StringBuilder();
        if (type == 1) {
            if (crmEventDTOSearch != null && crmEventDTOSearch.getAssignedTo() != null) {
                empIds = employeeService.returnEmployeeList(crmEventDTOSearch.getAssignedTo().getId(), empIds);
            } else {
                empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            }
        } else {
            empIds.append(assignedTo);
        }

        builder.append("  AND (e.assignedTo.id in ( ").append(empIds.toString()).append(" ) ) ");

        filters.put("branchId", branchId);
        builder.append(" AND e.branchId.id= :branchId ");
        if (crmEventDTOSearch != null && crmEventDTOSearch.getActivityType() != null) {
            filters.put("activityType", crmEventDTOSearch.getActivityType().getId());
            builder.append(" AND e.activityType.id = :activityType ");
        }

        if (crmEventDTOSearch != null && crmEventDTOSearch.getPeriority() != null) {
            filters.put("activityType", crmEventDTOSearch.getPeriority().getId());
            builder.append(" AND e.priority.id = :priority ");
        }

        if (crmEventDTOSearch != null && crmEventDTOSearch.getStatus() != null) {
            filters.put("status", crmEventDTOSearch.getStatus().getId());
            builder.append(" AND e.status.id = :status ");
        } else if (taskType != null && taskType == 0) {
            builder.append("  AND e.status.serial  = 1 AND e.closeEvent = false");
        } else if (taskType != null && taskType == 1) {
            filters.put("startDate", startDate());
            filters.put("endDate", endDate());
            builder.append(" AND e.status.serial != 0 AND e.closeEvent = false AND e.startDate >= :startDate AND e.startDate < :endDate");
        } else if (taskType != null && taskType == 2) {
            filters.put("startDate", startDate());
            builder.append(" AND e.status.serial != 0 AND e.closeEvent = false AND e.startDate < :startDate ");
        } else {
            builder.append("  AND e.status.serial != 0 AND e.closeEvent = false ");
        }

        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (e.customerId.name like CONCAT('%',:globalFilter ,'%')  OR e.customerId.phone  like CONCAT('%',:globalFilter ,'%') OR e.activityType.name like CONCAT('%',:globalFilter ,'%') OR  FUNCTION('DATE_FORMAT',e.startDate, '%d-%m-%Y') like CONCAT('%',:globalFilter ,'%') OR e.discription like CONCAT('%',:globalFilter ,'%') OR  e.status.name like CONCAT('%',:globalFilter ,'%') OR  e.assignedTo.name like CONCAT('%',:globalFilter ,'%')) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public Long getTotalRegistorsAllEvent(Integer branchId, Map<String, Object> filters, CrmEventDTO crmEventDTOSearch, Integer type, Integer taskType) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(e) FROM CrmEvent e WHERE e.branchId.id = :branchId  AND e.deletedBy IS NULL ");
        filters.put("branchId", branchId);
        appendFilterallEvent(filters, builder, crmEventDTOSearch, type, taskType);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details;
    }

    @Override
    public List<CrmEventDTO> findALLEventBank(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, CrmEventDTO crmEventDTOSearch, Integer type, Integer taskType) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e FROM CrmEvent e  where e.branchId.id = :branchId  AND e.deletedBy IS NULL ");
        filters.put("branchId", branchId);
        appendFilterallEvent(filters, builder, crmEventDTOSearch, type, taskType);
        builder.append(" order by e.startDate asc ");
        List<CrmEvent> crmEventList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return mapToDTOList(crmEventList);
    }

    private void appendFilterallEvent(Map<String, Object> filters, StringBuilder builder, CrmEventDTO crmEventDTOSearch, Integer type, Integer taskType) {
        StringBuilder empIds = new StringBuilder();
        if (type != null && type == 1) {
            if (crmEventDTOSearch != null && crmEventDTOSearch.getAssignedTo() != null) {
                empIds = employeeService.returnEmployeeList(crmEventDTOSearch.getAssignedTo().getId(), empIds);
                builder.append("  AND (e.assignedTo.id in ( ").append(empIds.toString()).append(" ) ) ");
            }
        } else {
            if (crmEventDTOSearch != null && crmEventDTOSearch.getAssignedTo() != null) {
                builder.append("  AND (e.assignedTo.id in ( ").append(crmEventDTOSearch.getAssignedTo().getId()).append(" ) ) ");
            }
        }

        if (crmEventDTOSearch != null && crmEventDTOSearch.getActivityType() != null) {
            filters.put("activityType", crmEventDTOSearch.getActivityType().getId());
            builder.append(" AND e.activityType.id = :activityType ");
        }

        if (crmEventDTOSearch != null && crmEventDTOSearch.getStatus() != null) {
            filters.put("status", crmEventDTOSearch.getStatus().getId());
            builder.append(" AND e.status.id = :status ");
        } else if (taskType != null && taskType == 0) {
            builder.append("  AND e.status.serial  = 1 AND e.closeEvent = false");
        } else if (taskType != null && taskType == 1) {
            filters.put("startDate", startDate());
            filters.put("endDate", endDate());
            builder.append(" AND e.status.serial != 0 AND e.closeEvent = false AND e.startDate >= :startDate AND e.startDate < :endDate");
        } else if (taskType != null && taskType == 2) {
            filters.put("startDate", startDate());
            builder.append(" AND e.status.serial != 0 AND e.closeEvent = false AND e.startDate < :startDate ");
        }

        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (e.customerId.name like CONCAT('%',:globalFilter ,'%')  OR e.customerId.phone  like CONCAT('%',:globalFilter ,'%') OR e.activityType.name like CONCAT('%',:globalFilter ,'%') OR  FUNCTION('DATE_FORMAT',e.startDate, '%d-%m-%Y') like CONCAT('%',:globalFilter ,'%') OR e.discription like CONCAT('%',:globalFilter ,'%') OR  e.status.name like CONCAT('%',:globalFilter ,'%') OR  e.assignedTo.name like CONCAT('%',:globalFilter ,'%')) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<CrmEventEvaluationDTO> findEvaluationCrmEvent(Integer branchId, CrmEventsReportSearchBean crmEventDTOSearch) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(e.id) ,  e.startDate , e.status.name , e.assignedTo.name FROM CrmEvent e  where e.deletedBy IS NULL AND e.branchId.id= :branchId group by function('DATE_FORMAT', e.startDate, '%Y-%m-%d')  , e.status.id , e.assignedTo.id");
        List<Object[]> crmEventList = dao.findListByQuery(builder.toString(), params);
        return mapToCrmEventEvaluationDTOList(crmEventList);

    }

    private List<CrmEventEvaluationDTO> mapToCrmEventEvaluationDTOList(List<Object[]> crmEventList) {
        List<CrmEventEvaluationDTO> crmEventEvaluationDTOList = new ArrayList<>();
        if (crmEventList != null && !crmEventList.isEmpty()) {
            for (Object[] entity : crmEventList) {
                CrmEventEvaluationDTO crmEventEvaluationDTO = initMapToCrmEventEvaluationDTO(entity);
                crmEventEvaluationDTOList.add(crmEventEvaluationDTO);
            }
        }
        return crmEventEvaluationDTOList;
    }

    private CrmEventEvaluationDTO initMapToCrmEventEvaluationDTO(Object[] entity) {
        CrmEventEvaluationDTO dTO = new CrmEventEvaluationDTO();
        dTO.setCounter(((Long) entity[0]).intValue());
        dTO.setStartDate(entity[1] != null ? (Date) entity[1] : null);
        dTO.setStatusName(entity[2].toString());
        dTO.setAssignedToName(entity[3].toString());
        return dTO;
    }

    private Date startDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date endDate() {
        Date endDate = new Date(startDate().getTime() + (1000 * 60 * 60 * 24));
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        endCal.set(Calendar.HOUR_OF_DAY, 0);
        endCal.set(Calendar.MINUTE, 0);
        endCal.set(Calendar.SECOND, 0);
        endCal.set(Calendar.MILLISECOND, 0);
        return endCal.getTime();
    }

    private Date getmaxStartDate(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findEntityByQuery("SELECT c.startDate FROM CrmEvent c  WHERE c.branchId.id = :branchId ", params);
    }

    private StringBuilder filterChartDate(Map<String, Object> filters, StringBuilder builder, Date fromDate, Date toDate) {
        Date maxDate = null;
        if ((fromDate != null || toDate != null) && (fromDate == null || toDate == null)) {
            maxDate = getmaxStartDate((Integer) filters.get("branchId"));
            filters.put("maxDate", maxDate);
        }

        if (fromDate != null && toDate == null) {
            builder.append(" AND c.startDate BETWEEN :fromDate AND :maxDate ");
            filters.put("fromDate", fromDate);
        }

        if (toDate != null && fromDate == null) {
            builder.append(" AND c.startDate BETWEEN :maxDate AND :toDate ");
            filters.put("toDate", toDate);
        }

        if (fromDate != null && toDate != null) {
            builder.append(" AND c.startDate BETWEEN :fromDate AND :toDate ");
            filters.put("fromDate", fromDate);
            filters.put("toDate", toDate);
        }
        return builder;
    }

    private ChartDTO preperChart(List<Object[]> details, String xLabel, String yLabel, String title) {
        Map<Object, Number> map = new HashMap<>();
        List<ChartDataDTO> chartDataDTOList = new ArrayList<>();
        ChartDataDTO chartDataDTO = new ChartDataDTO();
        int max = 0;
        for (Object[] detail : details) {
            map.put(detail[0].toString(), (Number) detail[1]);
            if (max < (((Number) detail[1]).intValue())) {
                max = ((Number) detail[1]).intValue();
            }
            chartDataDTO.setLabel(detail[2].toString());
        }
        chartDataDTO.setMap(map);
        chartDataDTOList.add(chartDataDTO);
        ChartDTO chartDTO = new ChartDTO();
        chartDTO.setMax(max);
        chartDTO.setMin(0);
        chartDTO.setxLabel(xLabel);
        chartDTO.setyLabel(yLabel);
        chartDTO.setTitle(title);
        chartDTO.setLegendPosition("ne");
        chartDTO.setChartDataDTOList(chartDataDTOList);
        return chartDTO;
    }

    @Override
    public List<CrmEventDTO> findALLEventByCustomerId(Integer custumerId, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("custumerId", custumerId);
        params.put("branchId", branchId);
        List<CrmEvent> crmEventList = dao.findListByQuery("SELECT e FROM CrmEvent e WHERE e.customerId.id = :custumerId AND e.branchId.id = :branchId AND e.deletedBy is null", params);
        return mapToDTOList(crmEventList);
    }

    @Override
    public List<CrmEventDTO> findALLEventByGalleryId(String GalleryIdList, Integer userId) {
        Map<String, Object> params = new HashMap<>();
//        params.put("galleryid", GalleryIdList);
        params.put("userId", userId);
        List<CrmEvent> crmEventList = dao.findListByQuery("SELECT e FROM CrmEvent e WHERE e.galleryId in( " + GalleryIdList + " ) And e.id not in ( Select n.eventId from UserNotifications n where n.userId = :userId ) AND e.deletedBy is null", params);
        return mapToDTOList(crmEventList);
    }

    @Override
    public List<CrmEventDTO> findCrmEventList(Integer priorityId, Integer gallaryId, String customerCode, Integer createdBy, Integer assignedTo, Date dateTo, Date dateFrom, Integer status, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT e FROM CrmEvent e WHERE ");
        if (priorityId != null) {
            params.put("priorityId", priorityId);
            stringBuilder.append("e.priority.id = :priorityId ");
        }
        if (gallaryId != null) {
            params.put("gallaryId", gallaryId);
            stringBuilder.append("AND e.galleryId = :gallaryId ");
        }
        if (customerCode != null) {
            params.put("customerCode", customerCode);
            stringBuilder.append("AND e.customerId.code = :customerCode ");
        }
        if (createdBy != null) {
            params.put("createdBy", createdBy);
            stringBuilder.append("AND e.createdBy.id = :createdBy ");
        }
        if (assignedTo != null) {
            params.put("assignedTo", assignedTo);
            stringBuilder.append("AND e.assignedTo.id = :assignedTo ");
        }
        if (dateTo != null) {
            params.put("dateTo", dateTo);
            stringBuilder.append("AND e.endDate <= :dateTo ");
        }
        if (dateFrom != null) {
            params.put("dateFrom", dateFrom);
            stringBuilder.append("AND e.startDate >= :dateFrom ");
        }
        if (status != null) {
            params.put("status", status);
            stringBuilder.append("AND e.status.id =:status ");
        }
        if (branchId != null) {
            params.put("branchId", branchId);
            stringBuilder.append("AND e.branchId.id = :branchId ");
        }

        List<CrmEvent> crmEventList = dao.findListByQuery(stringBuilder.toString(), params);
        return mapToDTOList(crmEventList);
    }

    @Override
    public Integer findCountCrmEvent(Integer priorityId, Integer gallaryId, String customerCode, Integer createdBy, Integer assignedTo, Date dateTo, Date dateFrom, Integer status, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT count(e.id) FROM CrmEvent e WHERE ");
        if (priorityId != null) {
            params.put("priorityId", priorityId);
            stringBuilder.append("e.priority.id = :priorityId ");
        }
        if (gallaryId != null) {
            params.put("gallaryId", gallaryId);
            stringBuilder.append("AND e.galleryId = :gallaryId ");
        }
        if (customerCode != null) {
            params.put("customerCode", customerCode);
            stringBuilder.append("AND e.customerId.code = :customerCode ");
        }
        if (createdBy != null) {
            params.put("createdBy", createdBy);
            stringBuilder.append("AND e.createdBy.id = :createdBy ");
        }
        if (assignedTo != null) {
            params.put("assignedTo", assignedTo);
            stringBuilder.append("AND e.assignedTo.id = :assignedTo ");
        }
        if (dateTo != null) {
            params.put("dateTo", dateTo);
            stringBuilder.append("AND e.endDate <= :dateTo ");
        }
        if (dateFrom != null) {
            params.put("dateFrom", dateFrom);
            stringBuilder.append("AND e.startDate >= :dateFrom ");
        }
        if (status != null) {
            params.put("status", status);
            stringBuilder.append("AND e.status.id =:status ");
        }
        if (branchId != null) {
            params.put("branchId", branchId);
            stringBuilder.append("AND e.branchId.id = :branchId ");
        }

        Long x = dao.findEntityByQuery(stringBuilder.toString(), params);
        return x.intValue();
    }

    @Override
    public CrmEventDTO findALLEvent(TobyUser tobyUser, Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        CrmEvent crmEvent = dao.findEntityByQuery("SELECT e FROM CrmEvent e WHERE e.id = :id ", params);
        return initMapToDTO(crmEvent);
    }

    @Override
    public List<CrmEventDTO> findALLEventList(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<CrmEvent> crmEventList = dao.findEntityByQuery("SELECT e FROM CrmEvent e WHERE e.branchId.id = :branchId ", params);
        return returnListDTO(crmEventList);
    }

    @Override
    public Integer findALLCountEventList(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        Long x = dao.findEntityByQuery("SELECT count(e.id) FROM CrmEvent e WHERE e.branchId.id = :branchId ", params);
        return x.intValue();
    }

}
