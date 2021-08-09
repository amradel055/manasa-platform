/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.IsagSupportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.APIBranchDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.IsagScreenDTO;
import com.isag.dto.IsagSupportDTO;
import com.isag.dto.IsagUserDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.Branch;
import com.isag.entity.Employee;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagScreen;
import com.isag.entity.IsagSupport;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author khale
 */
@Stateless
public class IsagSupportServiceImpl implements IsagSupportService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public IsagSupportDTO save(IsagSupportDTO isagSupportDTO, IsagUser isagUser) {
        IsagSupport support = mapToEntity(isagSupportDTO, isagUser);
        if (isagSupportDTO.getId() == null) {
            Integer serial = genericService.getMaxGenericSerialByType(IsagSupport.class, isagUser.getBranchId().getId(), null);
            support.setSerial(serial);
        }
        support = dao.updateEntity(support);
        return mapTODTO(support);
    }

    @Override
    public IsagSupportDTO findIsagSupport(Integer id) {
        IsagSupport isagSupport = dao.findEntityById(IsagSupport.class, id);
        return mapTODTO(isagSupport);
    }

    @Override
    public List<IsagSupport> getData(Integer serial) {

        Map<String, Object> params = new HashMap<>();
        params.put("serial", serial);

        List<IsagSupport> detailes = dao.findListByQuery("SELECT i FROM IsagSupport i where i.serial= 1", params);

        return detailes;

    }

    @Override
    public List<IsagSupportDTO> findBranchData(Integer branchId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT i FROM IsagSupport i where  ");
        if (branchId != null) {
            params.put("branchId", branchId);
            sql.append("  i.branchId.id= :branchId order by i.serial DESC");
        }else{
          sql.append("( i.status = 0  OR i.status is null )");
          
        }
        List<IsagSupport> detailes = dao.findListByQuery(sql.toString(), params);

        return returnListDTO(detailes);

    }

    @Override
    public List<IsagSupportDTO> findData(IsagSupportDTO isagSupportDTO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT i FROM IsagSupport i  where 1=1 ");
        appendBranch(isagSupportDTO, sql);
        appendCreatedBy(isagSupportDTO, sql);
        appendScreen(isagSupportDTO, sql);
        appendStatus(isagSupportDTO, sql);
        appendAssinTo(isagSupportDTO, sql);
        appendTitle(isagSupportDTO, sql);
        appendDescription(isagSupportDTO, sql);
        appendReply(isagSupportDTO, sql);
        appendTicketSolveDate(isagSupportDTO, sql);
        appendTransferDate(isagSupportDTO, sql);
        List<IsagSupport> detailes = dao.findListByQuery(sql.toString(), params);

        return returnListDTO(detailes);

    }
    
    @Override
 public List<IsagSupportDTO> findDataByBranch(IsagSupportDTO isagSupportDTO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", isagSupportDTO.getBranchId());
        sql.append("SELECT i FROM IsagSupport i  where i.branchId.id= :branchId");
        appendBranch(isagSupportDTO, sql);
        appendCreatedBy(isagSupportDTO, sql);
        appendScreen(isagSupportDTO, sql);
        appendStatus(isagSupportDTO, sql);
        appendAssinTo(isagSupportDTO, sql);
        appendTitle(isagSupportDTO, sql);
        appendDescription(isagSupportDTO, sql);
        appendReply(isagSupportDTO, sql);
        appendTicketSolveDate(isagSupportDTO, sql);
        appendTransferDate(isagSupportDTO, sql);
        List<IsagSupport> detailes = dao.findListByQuery(sql.toString(), params);

        return returnListDTO(detailes);

    }
 
    private void appendTitle(IsagSupportDTO isagSupportDTO, StringBuilder sql) {
        if (isagSupportDTO.getTitle() != null && !isagSupportDTO.getTitle().isEmpty()) {
            sql.append(" AND i.title like CONCAT('%', '");
            sql.append(isagSupportDTO.getTitle());
            sql.append("' ,'%')");
        }
    }

    private void appendDescription(IsagSupportDTO isagSupportDTO, StringBuilder sql) {
        if (isagSupportDTO.getDescription() != null && !isagSupportDTO.getDescription().isEmpty()) {
            sql.append(" AND i.description like CONCAT('%', '");
            sql.append(isagSupportDTO.getDescription());
            sql.append("' ,'%')");
        }
    }

    private void appendReply(IsagSupportDTO isagSupportDTO, StringBuilder sql) {
        if (isagSupportDTO.getReply() != null && !isagSupportDTO.getReply().isEmpty()) {
            sql.append(" AND i.reply like CONCAT('%', ");
            sql.append(isagSupportDTO.getReply());
            sql.append("' ,'%')");
        }
    }

    private void appendTicketSolveDate(IsagSupportDTO isagSupportDTO, StringBuilder sql) {
        if (isagSupportDTO.getTicketSolveDate() != null) {
            sql.append(" and ( FUNCTION('DATE_FORMAT',i.ticketSolveDate, '%d-%m-%Y')  like CONCAT('%', '");
            sql.append(isagSupportDTO.getTicketSolveDate());
            sql.append("' ,'%')");
        }
    }

    private void appendTransferDate(IsagSupportDTO isagSupportDTO, StringBuilder sql) {
        if (isagSupportDTO.getTransferDate() != null) {
            sql.append(" and ( FUNCTION('DATE_FORMAT',i.transferDate, '%d-%m-%Y')  like CONCAT('%', '");
            sql.append(isagSupportDTO.getTransferDate());
            sql.append("' ,'%')");
        }
    }

    private void appendBranch(IsagSupportDTO isagSupportDTO, StringBuilder sql) {
        if (isagSupportDTO.getBranchSelected() != null && !isagSupportDTO.getBranchSelected().isEmpty()) {
            sql.append(" AND i.branchId.id in ( 0 ");
            for (APIBranchDTO selected : isagSupportDTO.getBranchSelected()) {
                sql.append(" , " + selected.getId());
            }
            sql.append(")");
        }
    }

    private void appendCreatedBy(IsagSupportDTO isagSupportDTO, StringBuilder sql) {
        if (isagSupportDTO.getIsagUserDTOSelected() != null && !isagSupportDTO.getIsagUserDTOSelected().isEmpty()) {
            sql.append(" AND i.createdBy.id in ( 0 ");
            for (IsagUserDTO selected : isagSupportDTO.getIsagUserDTOSelected()) {
                sql.append(" , " + selected.getId());
            }
            sql.append(")");
        }
    }

    private void appendScreen(IsagSupportDTO isagSupportDTO, StringBuilder sql) {
        if (isagSupportDTO.getScreenSelected() != null && !isagSupportDTO.getScreenSelected().isEmpty()) {
            sql.append(" AND i.IsagScreen.id in ( 0 ");
            for (IsagScreenDTO selected : isagSupportDTO.getScreenSelected()) {
                sql.append(" , " + selected.getId());
            }
            sql.append(")");
        }
    }
    private void appendAssinTo(IsagSupportDTO isagSupportDTO, StringBuilder sql) {
        if (isagSupportDTO.getAssignToSelected() != null && !isagSupportDTO.getAssignToSelected().isEmpty()) {
            sql.append(" AND i.assignTo.id in ( 0 ");
            for ( EmployeeDTO selected : isagSupportDTO.getAssignToSelected()) {
                sql.append(" , " + selected.getId());
            }
            sql.append(")");
        }
    }

    private void appendStatus(IsagSupportDTO isagSupportDTO, StringBuilder sql) {
        if (isagSupportDTO.getStatusSelected() != null && !isagSupportDTO.getStatusSelected().isEmpty()) {
            sql.append(" AND i.status in ( ");
            StringBuilder query = new StringBuilder();
            
            for (Integer selected : isagSupportDTO.getStatusSelected()) {
                if(query.toString().isEmpty()){
                    query.append(selected);
                }else{
                    query.append(",").append(selected);
                }
            }
            sql.append(query.toString());
            sql.append(")");
        }
    }

    @Override
    public List<IsagScreen> getAllScreens(Integer moduleType) {

        Map<String, Object> params = new HashMap<>();
        params.put("moduleType", moduleType);

        List<IsagScreen> screen = dao.findListByQuery("SELECT i FROM IsagScreen i where i.moduleType=:moduleType ", params);

        return screen;
    }

    @Override
    public List<IsagSupportDTO> getIsagSupportByBranch(Integer branchId, IsagSupportSearchBean isagSupportSearchBean) {

        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM IsagSupport i where i.branchId.id = :branchId ");
        appendEmpTo(isagSupportSearchBean, params, query);
        appendEmpFrom(isagSupportSearchBean, params, query);
        appendDateTo(isagSupportSearchBean, params, query);
        appendDateFrom(isagSupportSearchBean, params, query);

        List<IsagSupport> support = dao.findListByQuery(query.toString(), params);

        return returnListDTO(support);

    }

    private void appendDateFrom(IsagSupportSearchBean isagSupportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (isagSupportSearchBean.getDateFrom() != null) {
            params.put("dateFrom", isagSupportSearchBean.getDateFrom());
            query.append(" and i.date >= :dateFrom  ");

        }
    }

    private void appendDateTo(IsagSupportSearchBean isagSupportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (isagSupportSearchBean.getDateTo() != null) {
            params.put("dateTo", isagSupportSearchBean.getDateTo());
            query.append(" and i.date <= :dateTo ");
        }
    }

    private void appendEmpFrom(IsagSupportSearchBean isagSupportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (isagSupportSearchBean.getEmpFrom() != null) {
            params.put("empFrom", isagSupportSearchBean.getEmpFrom().getCode());
            query.append(" and i.assignTo.code >= :empFrom ");

        }
    }

    private void appendEmpTo(IsagSupportSearchBean isagSupportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (isagSupportSearchBean.getEmpTo() != null) {
            params.put("empTo", isagSupportSearchBean.getEmpTo().getCode());
            query.append(" and i.assignTo.code <= :empTo ");

        }
    }

    @Override
    public void deleteIsagSupport(Integer isagSupportId) {
        IsagSupport isagSupport = new IsagSupport();
        isagSupport.setId(isagSupportId);
        dao.deleteEntity(isagSupportId);
    }

    private List<IsagSupportDTO> returnListDTO(List<IsagSupport> list) {
        List<IsagSupportDTO> dTOList = new ArrayList<>();
        for (IsagSupport entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private IsagSupportDTO mapTODTO(IsagSupport entity) {
        IsagSupportDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private IsagSupportDTO initMapToDTO(IsagSupport entity) {
        IsagSupportDTO dTO = new IsagSupportDTO();

        dTO.setDate(entity.getDate());
        dTO.setModule(entity.getModule());
        if (entity.getScreen() != null) {
            IsagScreenDTO screenDTO = new IsagScreenDTO();
            screenDTO.setId(entity.getScreen().getId());
            screenDTO.setNamear(entity.getScreen().getNamear());
            screenDTO.setSerial(entity.getScreen().getSerial());
             screenDTO.setModuleAr(entity.getScreen().getModuleAr());
            dTO.setScreen(screenDTO);
        }
        if (entity.getAssignTo() != null) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(entity.getAssignTo().getId());
            employeeDTO.setName(entity.getAssignTo().getName());
            employeeDTO.setCode(entity.getAssignTo().getCode());
            dTO.setAssignTo(employeeDTO);
        }

        dTO.setTicketSolveDate(entity.getTicketSolveDate());
        dTO.setTransferDate(entity.getTransferDate());
        dTO.setTitle(entity.getTitle());
        dTO.setDescription(entity.getDescription());
        dTO.setImg(entity.getImg());
        if (entity.getStatus() != null) {
            dTO.setStatus(entity.getStatus());
            if (entity.getStatus() == 0) {
                dTO.setStatusName("تحت التنفيذ");
            } else if (entity.getStatus() == 1) {
                dTO.setStatusName("تم التنفيذ");
            } else if (entity.getStatus() == 2) {
                dTO.setStatusName("مغلقه");
            }
        }else{
            dTO.setStatusName("تحت التنفيذ");
        }
        dTO.setPriorety(entity.getPriorety());
        dTO.setTecincal(entity.getTecincal());
        dTO.setReply(entity.getReply());
        dTO.setRemark(entity.getRemark());
        dTO.setSerial(entity.getSerial());
        dTO.setBranchName(entity.getBranchId() != null ? entity.getBranchId().getNameAr() : null);
        dTO.setCompanyName(entity.getBranchId() != null ? entity.getCompanyId().getName() : null);
        dTO.setEmployeeName(entity.getBranchId() != null ? entity.getCreatedBy().getName() : null);

        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        dTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dTO.setCreatedDate(entity.getCreationDate());

        return dTO;
    }

    private IsagSupport mapToEntity(IsagSupportDTO dTO, IsagUser isagUser) {
        IsagSupport entity = new IsagSupport();

        entity.setDate(dTO.getDate());
        entity.setModule(dTO.getModule());
        if (dTO.getScreen() != null) {
            IsagScreen screen = new IsagScreen();
            screen.setId(dTO.getScreen().getId());
            entity.setScreen(screen);
        }
        entity.setTitle(dTO.getTitle());
        entity.setDescription(dTO.getDescription());
        entity.setImg(dTO.getImg());
        entity.setStatus(dTO.getStatus());
        entity.setPriorety(dTO.getPriorety());
        entity.setTecincal(dTO.getTecincal());
        entity.setReply(dTO.getReply());
        entity.setRemark(dTO.getRemark());
        entity.setSerial(dTO.getSerial());

        if (dTO.getAssignTo() != null) {
            Employee employee = new Employee();
            employee.setId(dTO.getAssignTo().getId());
            entity.setAssignTo(employee);
        }

        if (dTO.getReply() != null && !dTO.getReply().isEmpty()) {
            entity.setTicketSolveDate(new Date());
        }

        if (dTO.getAssignTo() != null) {
            entity.setTransferDate(new Date());
        }

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
            entity.setBranchId(isagUser.getBranchId());
            entity.setCompanyId(isagUser.getCompanyId());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
           Branch branch = new Branch();
           branch.setId(dTO.getBranchId());
            entity.setBranchId( branch);
           IsagCompany company = new IsagCompany();
           company.setId(dTO.getCompanyId());
           entity.setCompanyId(company);
        }
        entity.setId(dTO.getId());
      
        return entity;

    }

}
