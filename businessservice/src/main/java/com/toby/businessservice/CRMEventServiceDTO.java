/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.CrmEventsReportSearchBean;
import com.toby.dto.ChartDTO;
import com.toby.dto.CrmEventDTO;
import com.toby.dto.CrmEventEvaluationDTO;
import com.toby.entity.TobyUser;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface CRMEventServiceDTO {

    public String validateSave(CrmEventDTO crmEventDTO);

    public List<CrmEventDTO> getEvents(Integer branchId, CrmEventsReportSearchBean crmEventsReportSearchBean);

    public List<CrmEventDTO> getAllEvents(Integer branchId);

    public CrmEventDTO save(CrmEventDTO crmEventDTO, TobyUser tobyUser);

    public List<CrmEventDTO> findALLCrmEventByAssignedToAndBranch(Integer branchId, Integer assignedTo);

    public List<CrmEventDTO> findALLCrmEventByCustomerId(Integer custumerId);

    public List<CrmEventDTO> findALLCrmEventByAssignedToAndBranch(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer assignedTo);

    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters, Integer assignedTo);

    public void deleteCrmEventDTO(Integer crmEventDTO, TobyUser tobyUser);

    public CrmEventDTO findById(Integer crmEventId);

    public List<CrmEventDTO> findCrmEventList(Integer assignedTo);

    public List<CrmEventDTO> findCrmEventList(Integer branchId, Integer assignedTo, Integer createdBy, Boolean employeeIsSupervisor);

    //  public int findCrmEventListtCount(Integer assignedTo, Integer type, Integer taskType);
    public int findCrmEventListtCount(Integer assignedTo, Integer type, Integer taskType, Integer branchId);

    public List<CrmEventDTO> findCrmEventListt(CrmEventDTO crmEventDTOSearch, Integer assignedTo, Integer type, Integer taskType);

    public List<CrmEventDTO> findCalenderAll(Integer branchId, Integer assignedTo);

    public void deleteRow(CrmEventDTO crmEventSelected);

    public Integer numberOfEvent(Integer branchId, Integer assignedTo, Integer type);

    public void deleteCrmEventDTOList(List<CrmEventDTO> list, TobyUser tobyUser);

    public List<CrmEventDTO> saveList(List<CrmEventDTO> dtoList, Integer invOrganizationSiteId, String invOrganizationSiteName, Integer assignedTo, TobyUser tobyUser);

    public List<CrmEventDTO> findCrmEventListWithDate(Integer branchId, Integer assignedTo, Integer createdBy, Integer type, Boolean employeeIsSupervisor);

    public ChartDTO getCountEventByStatus(Integer branchId, Date dateFrom, Date dateTo);

    public ChartDTO getCountEventByActivityType(Integer branchId, Date dateFrom, Date dateTo);

    public ChartDTO getCountEventByPriority(Integer branchId, Date dateFrom, Date dateTo);

    public ChartDTO getCountEventByStatusAndAssignedTo(Integer branchId, Integer status, Date fromDate, Date toDate);

    public ChartDTO getCountEventByAssignedTo(Integer branchId, Date fromDate, Date toDate);

    public Integer numberOfEventAndAssigned(Integer branchId, Integer assignedTo, Integer type);

    public List<CrmEventDTO> findALLCrmNotification(Integer branchId, Integer EmpId);

    public List<CrmEventDTO> findALLCrmEvent(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, CrmEventDTO crmEventDTOSearch, Integer assignedTo, Integer taskType);

    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters, CrmEventDTO crmEventDTOSearch, Integer assignedTo, Integer taskType);

    public List<CrmEventEvaluationDTO> findEvaluationCrmEvent(Integer branchId, CrmEventsReportSearchBean crmEventDTOSearch);

    public void closeNotification(Integer id);

    public Long getTotalRegistorsAllEvent(Integer branchId, Map<String, Object> filters, CrmEventDTO crmEventDTOSearch, Integer type, Integer taskType);

    public List<CrmEventDTO> findALLEventBank(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, CrmEventDTO crmEventDTOSearch, Integer type, Integer taskType);

    public List<CrmEventDTO> findALLEventByCustomerId(Integer custumerId, Integer branchId);

    public List<CrmEventDTO> findALLEventByGalleryId(String GalleryIdList, Integer userId);

    public List<CrmEventDTO> findCrmEventList(Integer priorityId, Integer gallaryId, String customerCode, Integer createdBy, Integer assignedTo, Date dateTo, Date dateFrom, Integer status, Integer branchId);

    public CrmEventDTO findALLEvent(TobyUser tobyUser, Integer id);

    public Integer findCountCrmEvent(Integer priorityId, Integer gallaryId, String customerCode, Integer createdBy, Integer assignedTo, Date dateTo, Date dateFrom, Integer status, Integer branchId);

    public List<CrmEventDTO> findALLEventList(Integer branchId);

    public Integer findALLCountEventList(Integer branchId);
}
