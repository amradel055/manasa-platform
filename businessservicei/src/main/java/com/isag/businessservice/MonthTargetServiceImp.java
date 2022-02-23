/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InvMonthTargetDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.InvMonthTarget;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class MonthTargetServiceImp implements MonthTargetService {

    @EJB
    private GenericDAO dao;

    @Override
    public synchronized InvInvoiceDTO addMonthTargetDTO(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {
        String error = Validation(invInvoiceDTO);
        if (error.isEmpty()) {
            InvMonthTarget entity = mapToEntity(invInvoiceDTO.getInvMonthTargetDTO(), isagUser);
            if (entity.getId() == null) {
                entity.setCode(finMaxSerial(isagUser.getBranchId().getId()));
            }
            entity = dao.updateEntity(entity);
            invInvoiceDTO = null;
            invInvoiceDTO = new InvInvoiceDTO();
            invInvoiceDTO.setInvMonthTargetDTO(mapTODTO(entity));
            return invInvoiceDTO;
        } else {
            invInvoiceDTO.setMsg(error);
        }
        return invInvoiceDTO;
    }

    @Override
    public void deleteMonthTargetDTO(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {
        if (invInvoiceDTO != null && invInvoiceDTO.getInvMonthTargetDTO() != null) {
            InvMonthTarget entity = mapToEntity(invInvoiceDTO.getInvMonthTargetDTO(), isagUser);
            dao.deleteEntity(entity);
        }
    }

    @Override
    public List<InvMonthTargetDTO> getMonthTargetDTOByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchid", branchId);
        List<InvMonthTarget> monthTargetList = dao.findListByQuery("SELECT m FROM InvMonthTarget m WHERE m.branchId.id = :branchid ORDER BY m.dateFrom ASC", params);
        return returnDTOList(monthTargetList);

    }

    @Override
    public InvMonthTargetDTO getMonthTargetDTOById(InvInvoiceDTO invInvoiceDTO) {
        Map<String, Object> params = new HashMap();
        params.put("id", invInvoiceDTO.getId());
        InvMonthTarget monthTarget = dao.findEntityByQuery("SELECT m FROM InvMonthTarget m WHERE m.id = :id ", params);
        return mapTODTO(monthTarget);

    }

    @Override
    public List<InvMonthTargetDTO> getMonthTargetDTOByUserId(Integer branchId, Integer userId) {
        Map<String, Object> params = new HashMap();
        params.put("branchid", branchId);
        params.put("createdBy", userId);
        List<InvMonthTarget> monthTargetList = dao.findListByQuery("SELECT m FROM InvMonthTarget m WHERE m.branchId.id = :branchid AND m.createdBy.id = :createdBy", params);
        return returnDTOList(monthTargetList);

    }

    @Override
    public Boolean monthTargetDTOaleardyExist(InvInvoiceDTO invInvoiceDTO) {
        Map<String, Object> params = new HashMap();
        params.put("branchid", invInvoiceDTO.getBranchId());
        params.put("dateFrom", FromDate(invInvoiceDTO.getInvMonthTargetDTO().getMonth()));
        params.put("dateTo", ToDate(invInvoiceDTO.getInvMonthTargetDTO().getMonth()));
        params.put("type", invInvoiceDTO.getInvMonthTargetDTO().getType());
        InvMonthTarget monthTarget = dao.findEntityByQuery("SELECT m FROM InvMonthTarget m WHERE m.branchId.id = :branchid AND  m.dateFrom = :dateFrom AND m.dateTo = :dateTo AND m.type = :type", params);
        return monthTarget != null ? Boolean.TRUE : Boolean.FALSE;

    }

    public InvMonthTargetDTO monthTargetDTOByduration(InvInvoiceDTO invInvoiceDTO) {
        Map<String, Object> params = new HashMap();
        params.put("branchid", invInvoiceDTO.getBranchId());
        params.put("date", startDate(invInvoiceDTO.getInvMonthTargetDTO().getDateFrom()));
        InvMonthTarget monthTarget = dao.findEntityByQuery("SELECT m FROM InvMonthTarget m WHERE m.branchId.id = :branchid AND :date BETWEEN m.dateFrom  AND m.dateTo ", params);
        return mapTODTO(monthTarget);

    }

    private String Validation(InvInvoiceDTO invInvoiceDTO) {
        StringBuilder builder = new StringBuilder();
        if (invInvoiceDTO == null || invInvoiceDTO.getInvMonthTargetDTO() == null || invInvoiceDTO.getInvMonthTargetDTO().getMonth() == null) {
            builder.append("يجب اختيار شهر الفتره");
        }
        
        if (invInvoiceDTO == null || invInvoiceDTO.getInvMonthTargetDTO() == null || invInvoiceDTO.getInvMonthTargetDTO().getTarget() == null) {
            builder.append("يجب ادخال المستهدف");
        }
        if (invInvoiceDTO != null && invInvoiceDTO.getInvMonthTargetDTO() != null && invInvoiceDTO.getInvMonthTargetDTO().getMonth() != null && invInvoiceDTO.getInvMonthTargetDTO().getType() != null && invInvoiceDTO.getInvMonthTargetDTO().getId() == null ) {
            if (monthTargetDTOaleardyExist(invInvoiceDTO)) {
                builder.append("هذا الشهر موجود من قبل");
            }
        }
        return builder.toString();
    }

    private List<InvMonthTargetDTO> returnDTOList(List<InvMonthTarget> entityList) {
        List<InvMonthTargetDTO> monthTargetList = new ArrayList<>();
        for (InvMonthTarget monthTarget : entityList) {
            monthTargetList.add(mapTODTO(monthTarget));
        }
        return monthTargetList;
    }

    private InvMonthTargetDTO mapTODTO(InvMonthTarget entity) {
        if (entity != null) {
            InvMonthTargetDTO dTO = initMapToDTO(entity);
            return dTO;
        }
        return new InvMonthTargetDTO();
    }

    private InvMonthTargetDTO initMapToDTO(InvMonthTarget entity) {

        InvMonthTargetDTO dTO = new InvMonthTargetDTO();
        dTO.setDateFrom(entity.getDateFrom());
        dTO.setDateTo(entity.getDateTo());
        dTO.setTarget(entity.getTarget());
        dTO.setType(entity.getType());
        dTO.setMonth(entity.getDateFrom().getMonth());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        dTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCode(entity.getCode());
        return dTO;
    }

    private InvMonthTarget mapToEntity(InvMonthTargetDTO dTO, IsagUser isagUser) {
        InvMonthTarget entity = new InvMonthTarget();

        entity.setDateFrom(FromDate(dTO.getMonth()));
        entity.setDateTo(ToDate(dTO.getMonth()));
        entity.setTarget(dTO.getTarget());
        entity.setType(dTO.getType());

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);

        }
        entity.setId(dTO.getId());
        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        entity.setCode(dTO.getCode());
        return entity;

    }

    private Integer finMaxSerial(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        Integer max = dao.findEntityByQuery("SELECT COALESCE(MAX(i.code),0) +1  FROM InvMonthTarget i WHERE i.branchId.id =:branchId  ", params);
        return max;
    }

    private Date startDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date FromDate(Integer month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date ToDate(Integer month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
