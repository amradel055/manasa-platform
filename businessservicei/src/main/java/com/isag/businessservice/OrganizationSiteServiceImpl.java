/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.entityBean.contractorDataReportEntity;
import com.isag.businessservice.reports.searchBean.CrmLeadsSearchBean;
import com.isag.businessservice.reports.searchBean.contractorDataReportSearchBean;
import com.isag.businessservice.reports.searchBean.invOrganizationSiteSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.ChartDTO;
import com.isag.dto.ChartDataDTO;
import com.isag.dto.CrmCustomerInterestedProductsDTO;
import com.isag.dto.CrmEventDTO;
import com.isag.dto.CurrencyDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.GlAccountDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.InventoryDelegatorDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.Currency;
import com.isag.entity.Employee;
import com.isag.entity.GlAccount;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 *
 * @author hq002
 */
@Stateless
public class OrganizationSiteServiceImpl implements OrganizationSiteService {

    @PersistenceContext(unitName = "ISAG_PU")
    private EntityManager em;

    @EJB
    private GenericDAO dao;
    @EJB
    CRMCustomerInterestedProductsServiceDTO cRMCustomerInterestedProductsServiceDTO;
    @EJB
    private CRMEventServiceDTO cRMEventServiceDTO;
    @EJB
    private EmployeeService employeeService;
    @EJB
    private CurrencyServiceDTO currencyServiceDTO;
    @EJB
    private GlAccountServiceDTO glAccountServiceDTO;

    @Override
    public List<InvOrganizationSite> getInvOrganizationSiteByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(InvOrganizationSite.class, companyId);
    }

    @Override
    public List<InvOrganizationSiteDTO> getLeads(Integer branchId, Integer type, Date creationDateFrom, Date creationDateTo, String codeFrom, String codeTo) {

        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        params.put("type", type);
        params.put("codeFrom", codeFrom);
        params.put("codeTo", codeTo);
        params.put("creationDateFrom", creationDateFrom);
        params.put("creationDateTo", creationDateTo);
        List<InvOrganizationSite> details = dao.findListByQuery("select i from InvOrganizationSite i where i.type = :type and i.creationDate >= :creationDateFrom and i.creationDate <= :creationDateTo and i.branchId.id = :branchId and i.code >= :codeFrom and i.code <= :codeTo", params);

        return returnListDTO(details);
    }

    @Override
    public ChartDTO getCountCustomerPerMonth(Integer branchId, Integer type, Integer dateFilter) {

        StringBuilder builder = new StringBuilder();
        builder.append("select DATE_FORMAT(i.creation_date, ");
        String date_Filter = "";
        if (dateFilter == 0) {
            date_Filter = "'%d'";
        } else if (dateFilter == 1) {
            date_Filter = "'%m'";
        } else if (dateFilter == 2) {
            date_Filter = "'%y'";
        } else {
            date_Filter = "'%m'";
        }
        builder.append(date_Filter + " ) , count(*) ");
        builder.append("from inv_organization_site i where i.branch_Id = " + branchId);
        builder.append(" and i.type = " + type);
        builder.append(" group by DATE_FORMAT(i.creation_date, " + date_Filter + " )");
        List<Object[]> details = dao.executeNativeQuery(builder.toString());
        return preperChart(details);
    }

    @Override
    public List<InvOrganizationSiteDTO> getLeads(Integer branchId, Integer type, CrmLeadsSearchBean crmLeadsSearchBean) {

        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        params.put("type", type);

        StringBuilder query = new StringBuilder();
        query.append("select i from InvOrganizationSite i where i.type = :type and i.branchId.id = :branchId ");
        if (crmLeadsSearchBean != null) {
            appendCodeFrom(crmLeadsSearchBean, params, query);
            appendCodeTo(crmLeadsSearchBean, params, query);
            appendDateFrom(crmLeadsSearchBean, params, query);
            appendDateTo(crmLeadsSearchBean, params, query);
            appendEmpFrom(crmLeadsSearchBean, params, query);
            appendEmpTo(crmLeadsSearchBean, params, query);
            appendSourceFrom(crmLeadsSearchBean, params, query);
            appendSourceTo(crmLeadsSearchBean, params, query);
        }

        List<InvOrganizationSite> details = dao.findListByQuery(query.toString(), params);

        return returnListDTO(details);
    }

    @Override
    public List<InvOrganizationSiteDTO> findLeads(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer type, Integer branchId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT i FROM InvOrganizationSite i LEFT JOIN  i.LearnAboutCompanyfrom l  where i.branchId.id=:branchId AND i.type = :type  ");
        appendFilter2(filters, builder, invOrganizationSiteDTOSearch, type, branchId);
        List<InvOrganizationSite> leadsList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTO(leadsList);
    }

    @Override
    public Long getTotalRegistors(Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer type, Integer branchId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(i) FROM InvOrganizationSite i LEFT JOIN  i.LearnAboutCompanyfrom l  WHERE i.branchId.id = :branchId AND i.type = :type");
        appendFilter2(filters, builder, invOrganizationSiteDTOSearch, type, branchId);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details;
    }

    private void appendFilter2(Map<String, Object> filters, StringBuilder builder, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer type, Integer branchId) {

        filters.put("branchId", branchId);
        filters.put("type", type);
        // CrmLeadsSearchBean crmLeadsSearchBean
//        if (crmLeadsSearchBean != null) {
//            appendCodeFrom(crmLeadsSearchBean, filters, builder);
//            appendCodeTo(crmLeadsSearchBean, filters, builder);
//            appendDateFrom(crmLeadsSearchBean, filters, builder);
//            appendDateTo(crmLeadsSearchBean, filters, builder);
//            appendEmpFrom(crmLeadsSearchBean, filters, builder);
//            appendEmpTo(crmLeadsSearchBean, filters, builder);
//            appendSourceFrom(crmLeadsSearchBean, filters, builder);
//            appendSourceTo(crmLeadsSearchBean, filters, builder);
//        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getAssignedTo() != null) {
            filters.put("AssignedTo", invOrganizationSiteDTOSearch.getAssignedTo().getId());
            builder.append(" AND i.assignedTo.id = :AssignedTo");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom() != null) {
            filters.put("LearnAboutCompanyfrom", invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom().getId());
            builder.append(" AND l.id = :LearnAboutCompanyfrom");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getProjectName() != null) {
            filters.put("projectName", invOrganizationSiteDTOSearch.getProjectName().getId());
            builder.append(" AND i.projectName.id = :projectName");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLeadStatus() != null) {
            filters.put("leadStatus", invOrganizationSiteDTOSearch.getLeadStatus().getId());
            builder.append(" AND i.leadStatus.id = :leadStatus");
        }

        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (i.code like CONCAT('%',:globalFilter ,'%')  OR i.mobile  like CONCAT('%',:globalFilter ,'%') OR i.name like CONCAT('%',:globalFilter ,'%') OR  i.assignedTo.name like CONCAT('%',:globalFilter ,'%') OR  i.leadStatus.name like CONCAT('%',:globalFilter ,'%') OR   FUNCTION('DATE_FORMAT',i.creationDate, '%d-%m-%Y') like CONCAT('%',:globalFilter ,'%')) ";
            String filter2 = "OR l.name like CONCAT('%',:globalFilter ,'%') ";
            builder.append(filter);
            builder.append(filter2);
        } else {
            filters.remove("globalFilter");
        }
    }

    private void appendSourceTo(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getSourceListTo() != null) {
            params.put("sourceListTo", crmLeadsSearchBean.getSourceListTo().getSerial());
            query.append(" and i.LearnAboutCompanyfrom.serial <= :sourceListTo ");

        }
    }

    private void appendSourceFrom(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getSourceListFrom() != null) {
            params.put("sourceListFrom", crmLeadsSearchBean.getSourceListFrom().getSerial());
            query.append(" and i.LearnAboutCompanyfrom.serial >= :sourceListFrom ");

        }
    }

    private void appendEmpTo(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getEmpTo() != null) {
            params.put("empTo", crmLeadsSearchBean.getEmpTo().getCode());
            query.append(" and i.assignedTo.code <= :empTo ");

        }
    }

    private void appendEmpFrom(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getEmpFrom() != null) {
            params.put("empFrom", crmLeadsSearchBean.getEmpFrom().getCode());
            query.append(" and i.assignedTo.code >= :empFrom ");

        }
    }

    private void appendDateTo(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getDateTo() != null) {
            params.put("creationDateTo", crmLeadsSearchBean.getDateTo());

            query.append(" and i.creationDate <= :creationDateTo");
        }
    }

    private void appendDateFrom(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getDateFrom() != null) {
            params.put("creationDateFrom", crmLeadsSearchBean.getDateFrom());

            query.append(" and i.creationDate >= :creationDateFrom");
        }
    }

    private void appendCodeTo(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getCodeTo() != null) {
            params.put("codeTo", crmLeadsSearchBean.getCodeTo().getCode());
            query.append(" and i.code <= :codeTo");
        }
    }

    private void appendCodeFrom(CrmLeadsSearchBean crmLeadsSearchBean, Map<String, Object> params, StringBuilder query) {
        if (crmLeadsSearchBean.getCodeFrom() != null) {
            params.put("codeFrom", crmLeadsSearchBean.getCodeFrom().getCode());
            query.append(" and i.code >= :codeFrom ");
        }
    }

    @Override
    public List<InvOrganizationSite> getInvOrganizationSiteByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ORDER BY e.code", params);
        return detailses;
    }

    @Override
    public InvOrganizationSiteDTO getInvOrganizationSiteByQuery(String query, IsagUser isagUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE  ((e.name  =:query )) OR (e.code =:query ) OR (e.phone =:query) OR (e.mobile =:query )  ", params);
        List<InvOrganizationSiteDTO> list = mapToDTOList(detailses, isagUser);
        if(list != null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<InvOrganizationSite> getInvOrganizationSiteByCustomerId(Integer customerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", customerId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.id = :customerId ORDER BY e.code", params);
        return detailses;
    }

    @Override
    public List<InvOrganizationSite> getInvOrganizationSiteByBranchIdPer(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ORDER BY e.code", params);
        List<InvOrganizationSite> detailsesReturn = retiveOrganizationSiteDate(detailses);
        return detailsesReturn;
    }

    @Override
    public void updateImage(Integer clientProfileId, String img) {
        InvOrganizationSite entity = dao.findEntityById(InvOrganizationSite.class, clientProfileId);
        entity.setClientImage(img);
        dao.updateEntity(entity);
    }

    @Override
    public synchronized InvOrganizationSite addOrganizationSite(InvOrganizationSite invOrganizationSite) {
        if (invOrganizationSite.getId() == null && invOrganizationSite.getType() == 5) {
            Integer code = getMaxGenericSerialForDetail(invOrganizationSite.getBranchId().getId(), invOrganizationSite.getType());
            invOrganizationSite.setCode(code.toString());
        }
        return dao.updateEntity(invOrganizationSite);
    }

    @Override
    public InvOrganizationSite updateOrganizationSite(InvOrganizationSite invOrganizationSite) {
        return dao.updateEntity(invOrganizationSite);
    }

    @Override
    public void deleteOrganizationSite(InvOrganizationSite invOrganizationSite) {
        dao.deleteEntity(invOrganizationSite);
    }

    @Override
    public void deleteOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, IsagUser isagUser) {
        if (invOrganizationSiteDTO != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite(invOrganizationSiteDTO.getId());
            dao.deleteEntity(invOrganizationSite);
        }
    }

    @Override
    public InvOrganizationSite findOrganizationSiteById(Integer invOrganizationSiteId) {
        return dao.findEntityById(InvOrganizationSite.class, invOrganizationSiteId);
    }

    @Override
    public InvOrganizationSiteDTO findOrganizationSiteDTOById(Integer invOrganizationSiteId) {
        InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        InvOrganizationSite invOrganizationSite = dao.findEntityById(InvOrganizationSite.class, invOrganizationSiteId);
        return mapTODTO(invOrganizationSiteDTO, invOrganizationSite);
    }

    @Override
    public List<InvOrganizationSiteDTO> findLeads(Integer branchId, Integer userId, Integer type) {

        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvOrganizationSite i where i.branchId.id=:branchId AND i.type = 5  ");
        params.put("branchId", branchId);
        if (userId != null) {
            Boolean employeesIsSuperVisor = employeeService.getAllEmployeesIsSuperVisor(userId);
            if (employeesIsSuperVisor && (type == null || type == 0)) {
                StringBuilder empIds = new StringBuilder();
                empIds = employeeService.returnEmployeeList(userId, empIds);
//                List<Integer> listEmployee = employeeService.getAllEmployeesListByParentAndchield(branchId, userId);
//                listEmployee.add(userId);
                query.append(" AND  i.assignedTo.id  IN ( " + empIds + " ) ");
            } else {
                params.put("userId", userId);
                query.append(" AND i.assignedTo.id = :userId ");
            }
            //ميجيبش اللي عندهم صرف نظر
            query.append(" AND i.leadStatus.serial != 0 ");

            //    query.append(" AND i.assignedTo.id = :userId OR i.assignedTo.id  in (select e.id from Employee e where e.parentId.id = :userId)");
        }

        List<InvOrganizationSite> invOrganizationSiteList = dao.findListByQuery(query.toString(), params);

        return returnListDTO(invOrganizationSiteList);

    }

    @Override
    public List<InvOrganizationSiteDTO> findALLCrmLeads(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT i FROM InvOrganizationSite i LEFT JOIN  i.LearnAboutCompanyfrom l where i.branchId.id=:branchId AND i.type = 5  ");
        appendFilter(filters, builder, invOrganizationSiteDTOSearch, assignedTo, type);
        builder.append("  ORDER BY CAST(i.code SIGNED)   DESC ");
        List<InvOrganizationSite> leadsList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTO(leadsList);
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(i) FROM InvOrganizationSite i LEFT JOIN  i.LearnAboutCompanyfrom l WHERE i.branchId.id = :branchId AND i.type = 5");
        appendFilter(filters, builder, invOrganizationSiteDTOSearch, assignedTo, type);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details;
    }

    private void appendFilter(Map<String, Object> filters, StringBuilder builder, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type) {
        StringBuilder empIds = new StringBuilder();
        if (type == 1) {
            if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getAssignedTo() != null) {
                empIds = employeeService.returnEmployeeList(invOrganizationSiteDTOSearch.getAssignedTo().getId(), empIds);
            } else {
                empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            }
        } else {
            empIds.append(assignedTo);
        }
        filters.put("branchId", invOrganizationSiteDTOSearch.getBranchId());
        builder.append(" AND  i.assignedTo.id  IN ( ").append(empIds.toString()).append(" )  ");

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom() != null) {
            filters.put("LearnAboutCompanyfrom", invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom().getId());
            builder.append(" AND l.id = :LearnAboutCompanyfrom");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getProjectName() != null) {
            filters.put("projectName", invOrganizationSiteDTOSearch.getProjectName().getId());
            builder.append(" AND i.projectName.id = :projectName");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLeadStatus() != null) {
            filters.put("leadStatus", invOrganizationSiteDTOSearch.getLeadStatus().getId());
            builder.append(" AND i.leadStatus.id = :leadStatus");
        } else {
            builder.append("  AND i.leadStatus.serial != 0 ");
        }

        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (i.code like CONCAT('%',:globalFilter ,'%')  OR i.mobile  like CONCAT('%',:globalFilter ,'%') OR i.name like CONCAT('%',:globalFilter ,'%') OR i.assignedTo.name like CONCAT('%',:globalFilter ,'%')  OR i.leadStatus.name like CONCAT('%',:globalFilter ,'%') OR  FUNCTION('DATE_FORMAT',i.creationDate, '%d-%m-%Y') like CONCAT('%',:globalFilter ,'%')) ";
            String filter2 = "OR l.name like CONCAT('%',:globalFilter ,'%') ";
            builder.append(filter);
            builder.append(filter2);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<InvOrganizationSiteDTO> findLeadsList(InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        StringBuilder empIds = new StringBuilder();

        query.append("SELECT i FROM InvOrganizationSite i where i.branchId.id=:branchId AND i.type = 5  ");
        params.put("branchId", invOrganizationSiteDTOSearch.getBranchId());

        if (type == 1) {
            if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getAssignedTo() != null) {
                empIds = employeeService.returnEmployeeList(invOrganizationSiteDTOSearch.getAssignedTo().getId(), empIds);
            } else {
                empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            }
        } else {
            empIds.append(assignedTo);
        }

        query.append(" AND  i.assignedTo.id  IN ( ").append(empIds.toString()).append(" )  ");

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom() != null) {
            params.put("LearnAboutCompanyfrom", invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom().getId());
            query.append(" AND i.LearnAboutCompanyfrom.id = :LearnAboutCompanyfrom");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getProjectName() != null) {
            params.put("projectName", invOrganizationSiteDTOSearch.getProjectName().getId());
            query.append(" AND i.projectName.id = :projectName");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLeadStatus() != null) {
            params.put("leadStatus", invOrganizationSiteDTOSearch.getLeadStatus().getId());
            query.append(" AND i.leadStatus.id = :leadStatus");
        } else {
            query.append("  AND i.leadStatus.serial != 0 ");
        }
        query.append("  ORDER BY CAST(i.code SIGNED)   DESC ");
        List<InvOrganizationSite> leadsList = dao.findListByQuery(query.toString(), params);

        return returnListDTO(leadsList);

    }

    @Override
    public List<InvOrganizationSiteDTO> findAllLeadsList(InvOrganizationSiteDTO invOrganizationSiteDTOSearch) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        query.append("SELECT i FROM InvOrganizationSite i where i.branchId.id=:branchId AND i.type = 5  ");
        params.put("branchId", invOrganizationSiteDTOSearch.getBranchId());

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom() != null) {
            params.put("LearnAboutCompanyfrom", invOrganizationSiteDTOSearch.getLearnAboutCompanyFrom().getId());
            query.append(" AND i.LearnAboutCompanyfrom.id = :LearnAboutCompanyfrom");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getProjectName() != null) {
            params.put("projectName", invOrganizationSiteDTOSearch.getProjectName().getId());
            query.append(" AND i.projectName.id = :projectName");
        }

        if (invOrganizationSiteDTOSearch != null && invOrganizationSiteDTOSearch.getLeadStatus() != null) {
            params.put("leadStatus", invOrganizationSiteDTOSearch.getLeadStatus().getId());
            query.append(" AND i.leadStatus.id = :leadStatus");
        }

        query.append("  ORDER BY CAST(i.code SIGNED)   DESC ");
        List<InvOrganizationSite> leadsList = dao.findListByQuery(query.toString(), params);

        return returnListDTO(leadsList);
    }

    @Override
    public String findMaxCodeForOrganizationSiteByBranchId(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<String> detailses = dao.findListByQuery("SELECT max(e.code) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND (e.type = :type  AND e.active = 1) ORDER BY e.code", params);
        return detailses.get(0);
    }

    @Override
    public List<InvOrganizationSite> getSupplierByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 1 OR e.type = 2) AND e.active = 1) ORDER BY e.code", params);
        return detailses;
    }

    public List<InvOrganizationSiteDTO> findOrganizationSite(Integer branchId, Integer type) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        query.append("select i from InvOrganizationSite i where i.branchId.id = :branchId AND ( ");
        if (type == 0) {
            query.append(" (e.type = 0 OR e.type = 2) ");
        } else if (type == 1) {
            query.append(" (e.type = 1 OR e.type = 2) ");
        } else if (type == 4) {
            query.append(" (e.type = 4 ) ");
        } else if (type == 5) {
            query.append(" (e.type = 5 ) ");
        }
        query.append("  AND e.active = 1) ORDER BY e.id ");
        List<InvOrganizationSite> invOrganizationSiteList = dao.findListByQuery(query.toString(), params);
        return returnListDTO(invOrganizationSiteList);
    }

    @Override
    public List<InvOrganizationSite> getSupplierByBranchIdForSupplierAccountReportMB(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = new ArrayList<>();
        query.append(" SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ( ");
        if (type == 0) {
            query.append(" (e.type = 0 OR e.type = 2) ");
        } else if (type == 1) {
            query.append(" (e.type = 1 OR e.type = 2) ");
        } else if (type == 4) {
            query.append(" (e.type = 4 ) ");
        } else if (type == 5) {
            query.append(" (e.type = 5 ) ");
        }
        //query.append("  AND e.active = 1) ORDER BY e.code ");
        query.append("  AND e.active = 1) ORDER BY e.id ");

        detailses = dao.findListByQuery(query.toString(), params);
        List<InvOrganizationSite> detailsesReturn = retiveOrganizationSiteDate(detailses);
        return detailsesReturn;
    }

    private List<InvOrganizationSite> retiveOrganizationSiteDate(List<InvOrganizationSite> detailses) {
        List<InvOrganizationSite> detailsesReturn = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(detail.getId());
            invOrganizationSite.setCode(detail.getCode());
            invOrganizationSite.setName(detail.getName());
            detailsesReturn.add(invOrganizationSite);
        }
        return detailsesReturn;
    }

    @Override
    public List<InvOrganizationSite> getCustomerByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code", params);
        return detailses;
    }

    @Override
    public List<InvOrganizationSite> getorganizationSiteByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type, Integer type2) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("type2", type2);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = :type OR e.type = :type2 OR e.type = 2)");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        List<InvOrganizationSite> list = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSite site = new InvOrganizationSite();
            site.setAccountId(detail.getAccountId());
            site.setCode(detail.getCode());
            site.setCurrencyId(detail.getCurrencyId());
            site.setId(detail.getId());
            site.setNationId(detail.getNationId());
            site.setName(detail.getName());
            site.setOpenBalanceCredit(detail.getOpenBalanceCredit());
            site.setOpenBalanceDebit(detail.getOpenBalanceDebit());
            site.setOpenBalanceDate(detail.getOpenBalanceDate());
            list.add(site);
        }
        return list;
    }

    @Override
    public List<InvOrganizationSite> getorganizationSiteByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = :type OR e.type = 2)");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        List<InvOrganizationSite> list = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSite site = new InvOrganizationSite();
            site.setAccountId(detail.getAccountId());
            site.setCode(detail.getCode());
            site.setCurrencyId(detail.getCurrencyId());
            site.setId(detail.getId());
            site.setNationId(detail.getNationId());
            site.setName(detail.getName());
            site.setOpenBalanceCredit(detail.getOpenBalanceCredit());
            site.setOpenBalanceDebit(detail.getOpenBalanceDebit());
            site.setOpenBalanceDate(detail.getOpenBalanceDate());
            list.add(site);
        }
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> getorganizationSiteForOpenningBalanceDTO(Integer branchId, boolean active, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId  ");

        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else {
            sql.append(" AND e.type = :type  ");
        }

        if (active) {
            sql.append(" AND e.active = 1 ");
        }

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSiteDTO site = new InvOrganizationSiteDTO();
            site.setCode(detail.getCode());
            site.setId(detail.getId());
            site.setName(detail.getName());
            site.setOpenBalanceCredit(detail.getOpenBalanceCredit());
            site.setOpenBalanceDebit(detail.getOpenBalanceDebit());
            site.setOpenBalanceDate(detail.getOpenBalanceDate());
            list.add(site);
        }
        return list;
    }

    @Override
    public List<InvOrganizationSite> getorganizationSiteByBranchIdForTasaheel(Integer branchId, boolean active, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = :type OR e.type = 2) AND e.nationId IS NOT NULL");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        List<InvOrganizationSite> list = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSite site = new InvOrganizationSite();
            site.setAccountId(detail.getAccountId());
            site.setCode(detail.getCode());
            site.setCurrencyId(detail.getCurrencyId());
            site.setId(detail.getId());
            site.setName(detail.getName());
            site.setOpenBalanceCredit(detail.getOpenBalanceCredit());
            site.setOpenBalanceDebit(detail.getOpenBalanceDebit());
            site.setOpenBalanceDate(detail.getOpenBalanceDate());
            site.setNationId(detail.getNationId());
            list.add(site);
        }
        return list;
    }

    @Override
    public List<InvOrganizationSite> getContractorByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND (e.type = :type");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        List<InvOrganizationSite> list = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {
            InvOrganizationSite site = new InvOrganizationSite();
            site.setAccountId(detail.getAccountId());
            site.setCode(detail.getCode());
            site.setCurrencyId(detail.getCurrencyId());
            site.setId(detail.getId());
            site.setName(detail.getName());
            site.setOpenBalanceCredit(detail.getOpenBalanceCredit());
            site.setOpenBalanceDebit(detail.getOpenBalanceDebit());
            site.setOpenBalanceDate(detail.getOpenBalanceDate());
            list.add(site);
        }
        return list;
    }

    @Override
    public List<InvOrganizationSite> getCustomerByBranchIdForReport(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type <> 1 AND e.active = 1 ORDER BY e.code", params);

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        List<InvOrganizationSite> detailsesReturn = new ArrayList<>();
        for (InvOrganizationSite detail : detailses) {

//            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            mapperFactory.classMap(InvOrganizationSite.class, InvOrganizationSite.class)
                    .exclude("type")
                    .exclude("phone")
                    .exclude("fax")
                    .exclude("email")
                    .exclude("mobile")
                    .exclude("address")
                    .exclude("postBox")
                    .exclude("discount")
                    .exclude("openBalanceDebit")
                    .exclude("openBalanceCredit")
                    .exclude("balanceLimit")
                    .exclude("remark")
                    .exclude("active")
                    .exclude("zip")
                    .exclude("contactPerson")
                    .exclude("phone2")
                    .exclude("openBalanceDate")
                    .exclude("accountId")
                    .exclude("branchId")
                    .exclude("countryId")
                    .exclude("currencyId")
                    .exclude("delegatorId")
                    .exclude("regionId")
                    .exclude("parent")
                    .exclude("supplierType")
                    .exclude("companyId")
                    .exclude("createdBy")
                    .exclude("modifiedBy")
                    .exclude("modificationDate")
                    .exclude("creationDate")
                    .byDefault().register();

            MapperFacade mapper = mapperFactory.getMapperFacade();

            InvOrganizationSite dest = mapper.map(detail, InvOrganizationSite.class);
            detailsesReturn.add(dest);
        }
        return detailsesReturn;
    }

    @Override
    public List<InvOrganizationSiteDTO> getCustomerByBranchIdForReportDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type <> 1 AND e.active = 1 ORDER BY e.code", params);
        List<InvOrganizationSiteDTO> dTOList = new ArrayList<>();

        returnListDTO(detailses, dTOList);
        return dTOList;
    }

    @Override
    public Boolean checkCodeExistence(InvOrganizationSite invOrganizationSite) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        params.put("code", invOrganizationSite.getCode());
        params.put("branchId", invOrganizationSite.getBranchId().getId());

        query.append("SELECT i FROM InvOrganizationSite i WHERE i.branchId.id = :branchId AND i.code = :code ");

        if (invOrganizationSite.getType() != 2) {
            params.put("type", invOrganizationSite.getType());
            query.append(" AND i.type = :type");
        }

        if (invOrganizationSite.getId() != null) {
            params.put("id", invOrganizationSite.getId());
            query.append(" AND i.id != :id");
        }

        query.append(" ORDER BY i.code");

        String q = query.toString();

        List<InvOrganizationSite> organizationSites = dao.findListByQuery(q, params);

        if (organizationSites != null && !organizationSites.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdDTO(Integer branchId, boolean active, Integer type) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        appendOrganizationTypeAndActive(params, type, sql, active);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);

        returnListDTO(detailses, list);
        return list;
    }

    private void appendOrganizationTypeAndActive(Map<String, Object> params, Integer type, StringBuilder sql, boolean active) {
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ");

        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else {
            sql.append(" AND e.type = :type  ");
        }

        if (active) {
            sql.append(" AND e.active = 1 ");
        }

        sql.append(" ORDER BY e.code");
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters, Integer type) {
        filters.put("type", type);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(i) FROM InvOrganizationSite i WHERE i.branchId.id = :branchId AND (i.type = :type OR i.type = 2) ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            //OR ( i.parent.name  like CONCAT('%',:globalFilter ,'%'))
            String filter = " and (i.code like CONCAT('%',:globalFilter ,'%')  OR i.sponsorName1  like CONCAT('%',:globalFilter ,'%') OR i.nationId  like CONCAT('%',:globalFilter ,'%') OR i.name  like CONCAT('%',:globalFilter ,'%') OR i.mobile like CONCAT('%',:globalFilter ,'%') OR i.phone like CONCAT('%',:globalFilter ,'%')  ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<InvOrganizationSiteDTO> getALLCustomerDTOByBranchIdAndType(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer type) {
        filters.put("type", type);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT i FROM InvOrganizationSite i WHERE i.branchId.id = :branchId  AND (i.type = :type OR i.type = 2) ");
        appendFilter(filters, branchId, builder);
        builder.append(" ORDER BY i.id DESC");
        List<InvOrganizationSite> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<InvOrganizationSiteDTO> dTOList = new ArrayList<>();
        returnListDTO(details, dTOList);
        return dTOList;
    }

    @Override
    public List<InvOrganizationSiteDTO> getcontractorByBranchIdDTO(Integer branchId, boolean active) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);

        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ( e.type = 4");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);

        returnListDTO(detailses, list);
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> getcontractorORSupplierByBranchIdDTO(Integer branchId, boolean active) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);

        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ( e.type = 4 or e.type = 1 or e.type = 2");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);

        returnListDTO(detailses, list);
        return list;
    }

    public InvOrganizationSiteDTO mapTODTO(InvOrganizationSiteDTO dTO, InvOrganizationSite entity) {
        if (dTO == null) {
            dTO = new InvOrganizationSiteDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(InvOrganizationSiteDTO dTO, InvOrganizationSite entity) {
        dTO.setId(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setAddress(entity.getAddress());
        dTO.setMobile(entity.getMobile());
        dTO.setFax(entity.getFax());
        dTO.setPhone(entity.getPhone());
        dTO.setType(entity.getType());
        dTO.setEmail(entity.getEmail());
        dTO.setPostBox(entity.getPostBox());
        dTO.setDiscount(entity.getDiscount());
        dTO.setOpenBalanceDebit(entity.getOpenBalanceDebit());
        dTO.setOpenBalanceCredit(entity.getOpenBalanceCredit());
        dTO.setBalanceLimit(entity.getBalanceLimit());
        dTO.setRemark(entity.getRemark());
        dTO.setActive(entity.getActive());
        dTO.setZip(entity.getZip());
        dTO.setContactPerson(entity.getContactPerson());
        dTO.setPhone2(entity.getPhone2());
        dTO.setOpenBalanceDate(entity.getOpenBalanceDate());
        dTO.setCompanyName(entity.getCompanyName());
        dTO.setNationId(entity.getNationId());
        dTO.setAccountBankNumber(entity.getAccountBankNumber());
        dTO.setSponsorName1(entity.getSponsorName1());
        dTO.setSponsorPhone1(entity.getSponsorPhone1());
        dTO.setSponsorName2(entity.getSponsorName2());
        dTO.setSponsorPhone2(entity.getSponsorPhone2());
        dTO.setBirthdate(entity.getBirthdate());
        dTO.setClientImage(entity.getClientImage());
        dTO.setUrlSite(entity.getUrlSite());
        dTO.setUrlFacebook(entity.getUrlFacebook());
        dTO.setUrlInstegram(entity.getUrlInstegram());
        dTO.setUrlSnapchat(entity.getUrlSnapchat());
        dTO.setUrlLinkedin(entity.getUrlLinkedin());
        dTO.setCategory(entity.getCategory());
        dTO.setEmployeeNumber(entity.getEmployeeNumber());
        dTO.setCallFirstDate(entity.getCallFirstDate());
        dTO.setDiscription(entity.getDiscription());
        dTO.setNationIdSerial(entity.getNationIdSerial());
        if (entity.getAssignedTo() != null) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(entity.getAssignedTo().getId());
            employeeDTO.setCode(entity.getAssignedTo().getCode());
            employeeDTO.setName(entity.getAssignedTo().getName());
            dTO.setAssignedTo(employeeDTO);
        }
        if (entity.getLeadStatus() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getLeadStatus().getId());
            symbolDTO.setSerial(entity.getLeadStatus().getSerial());
            symbolDTO.setName(entity.getLeadStatus().getName());
            dTO.setLeadStatus(symbolDTO);
        }
        if (entity.getCategoryIndustryId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getCategoryIndustryId().getId());
            symbolDTO.setSerial(entity.getCategoryIndustryId().getSerial());
            symbolDTO.setName(entity.getCategoryIndustryId().getName());
            dTO.setCategoryIndustryId(symbolDTO);
        }

        if (entity.getAccountId() != null) {
            GlAccountDTO glAccountDTO = new GlAccountDTO();
            glAccountDTO.setId(entity.getAccountId().getId());
            glAccountDTO.setName(entity.getAccountId().getName());
            glAccountDTO.setAccNumber(entity.getAccountId().getAccNumber());
            dTO.setAccountId(glAccountDTO);
        }
        if (entity.getCountryId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getCountryId().getId());
            symbolDTO.setSerial(entity.getCountryId().getSerial());
            symbolDTO.setName(entity.getCountryId().getName());
            dTO.setCountryId(symbolDTO);
        }
        if (entity.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(entity.getCurrencyId().getId());
            currencyDTO.setSerial(entity.getCurrencyId().getSerial());
            currencyDTO.setName(entity.getCurrencyId().getName());
            currencyDTO.setCode(entity.getCurrencyId().getCode());
            dTO.setCurrencyId(currencyDTO);
        }
        if (entity.getWorkTypeId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getWorkTypeId().getId());
            symbolDTO.setSerial(entity.getWorkTypeId().getSerial());
            symbolDTO.setName(entity.getWorkTypeId().getName());
            dTO.setWorkTypeId(symbolDTO);
        }
        if (entity.getParent() != null) {
            InvOrganizationSiteDTO organizationSiteDTO = new InvOrganizationSiteDTO();
            organizationSiteDTO.setId(entity.getParent().getId());
            dTO.setParent(organizationSiteDTO);
        }

        if (entity.getSupplierType() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getSupplierType().getId());
            symbolDTO.setSerial(entity.getSupplierType().getSerial());
            dTO.setSupplierType(symbolDTO);
        }
        if (entity.getContractorType() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getContractorType().getId());
            symbolDTO.setSerial(entity.getContractorType().getSerial());
            dTO.setContractorType(symbolDTO);
        }

        if (entity.getRegionId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getRegionId().getId());
            symbolDTO.setName(entity.getRegionId().getName());
            symbolDTO.setSerial(entity.getRegionId().getSerial());
            dTO.setRegionId(symbolDTO);
        }
        if (entity.getDelegatorId() != null) {
            InventoryDelegatorDTO parent = new InventoryDelegatorDTO();
            parent.setId(entity.getDelegatorId().getId());
            parent.setCode(entity.getDelegatorId().getCode());
            parent.setName(entity.getDelegatorId().getName());
            dTO.setDelegatorId(parent);
        }

        if (entity.getProjectName() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getProjectName().getId());
            symbolDTO.setName(entity.getProjectName().getName());
            symbolDTO.setSerial(entity.getProjectName().getSerial());
            dTO.setProjectName(symbolDTO);
        }
        if (entity.getLearnAboutCompanyfrom() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getLearnAboutCompanyfrom().getId());
            symbolDTO.setName(entity.getLearnAboutCompanyfrom().getName());
            symbolDTO.setSerial(entity.getLearnAboutCompanyfrom().getSerial());
            dTO.setLearnAboutCompanyFrom(symbolDTO);
        }
        List<CrmEventDTO> crmEventDTOList = cRMEventServiceDTO.findALLCrmEventByCustomerId(entity.getId());
        dTO.setCrmEventDTOList(crmEventDTOList);

        List<CrmCustomerInterestedProductsDTO> interestedProductsDTOList = cRMCustomerInterestedProductsServiceDTO.getCrmCustomerInterestedProductsByOrganizationSiteId(entity.getId());
        dTO.setCrmCustomerInterestedProductsDTOList(interestedProductsDTOList);
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

    }

    public InvOrganizationSite mapToEntity(InvOrganizationSiteDTO dTO, IsagUser isagUser) {
        InvOrganizationSite entity = new InvOrganizationSite();

        entity.setId(dTO.getId());
        entity.setName(dTO.getName());
        entity.setCode(dTO.getCode());
        entity.setNationIdSerial(dTO.getNationIdSerial());
        if (dTO.getCode() == null) {
            String maxCode = findMaxCodeForOrganizationSiteByBranchId(isagUser.getBranchId().getId(), 0);
            entity.setCode(maxCode);
        }
        entity.setAddress(dTO.getAddress());
        entity.setMobile(dTO.getMobile());
        entity.setFax(dTO.getFax());
        entity.setPhone(dTO.getPhone());
        entity.setType(dTO.getType());
        entity.setEmail(dTO.getEmail());
        entity.setPostBox(dTO.getPostBox());
        entity.setDiscount(dTO.getDiscount());
        entity.setOpenBalanceDebit(dTO.getOpenBalanceDebit());
        entity.setOpenBalanceCredit(dTO.getOpenBalanceCredit());
        entity.setBalanceLimit(dTO.getBalanceLimit());
        entity.setRemark(dTO.getRemark());
        entity.setActive(dTO.getActive());
        entity.setZip(dTO.getZip());
        entity.setContactPerson(dTO.getContactPerson());
        entity.setPhone2(dTO.getPhone2());
        entity.setOpenBalanceDate(dTO.getOpenBalanceDate());
        entity.setCompanyName(dTO.getCompanyName());
        entity.setNationId(dTO.getNationId());
        entity.setAccountBankNumber(dTO.getAccountBankNumber());
        entity.setSponsorName1(dTO.getSponsorName1());
        entity.setSponsorPhone1(dTO.getSponsorPhone1());
        entity.setSponsorName2(dTO.getSponsorName2());
        entity.setSponsorPhone2(dTO.getSponsorPhone2());
        entity.setBirthdate(dTO.getBirthdate());
        entity.setClientImage(dTO.getClientImage());
        entity.setUrlSite(dTO.getUrlSite());
        entity.setUrlFacebook(dTO.getUrlFacebook());
        entity.setUrlInstegram(dTO.getUrlInstegram());
        entity.setUrlSnapchat(dTO.getUrlSnapchat());
        entity.setUrlLinkedin(dTO.getUrlLinkedin());
        entity.setCategory(dTO.getCategory());
        entity.setEmployeeNumber(dTO.getEmployeeNumber());
        entity.setCallFirstDate(dTO.getCallFirstDate());
        entity.setDiscription(dTO.getDiscription());
        if (dTO.getProjectName() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getProjectName().getId());
            entity.setProjectName(symbol);
        }
        if (dTO.getLeadStatus() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getLeadStatus().getId());
            entity.setLeadStatus(symbol);
        }
        if (dTO.getCategoryIndustryId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getCategoryIndustryId().getId());
            entity.setCategoryIndustryId(symbol);
        }

        if (dTO.getAccountId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(dTO.getAccountId().getId());
            entity.setAccountId(glAccount);
        }
        if (dTO.getCountryId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getCountryId().getId());
            entity.setCountryId(symbol);
        }
        if (dTO.getCurrencyId() != null) {
            Currency currency = new Currency();
            currency.setId(dTO.getCurrencyId().getId());
            entity.setCurrencyId(currency);
        }
        if (dTO.getWorkTypeId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getWorkTypeId().getId());
            entity.setWorkTypeId(symbol);
        }
        if (dTO.getParent() != null) {
            InvOrganizationSite organizationSite = new InvOrganizationSite();
            organizationSite.setId(dTO.getParent().getId());
            entity.setParent(organizationSite);
        }
        if (dTO.getSupplierType() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getSupplierType().getId());
            entity.setSupplierType(symbol);
        }
        if (dTO.getContractorType() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getContractorType().getId());
            entity.setContractorType(symbol);
        }
        if (dTO.getDelegatorId() != null) {
            InventoryDelegator parent = new InventoryDelegator();
            parent.setId(dTO.getDelegatorId().getId());
            entity.setDelegatorId(parent);
        }
        if (dTO.getRegionId() != null) {
            Symbol parent = new Symbol();
            parent.setId(dTO.getRegionId().getId());
            parent.setSerial(dTO.getRegionId().getSerial());
            entity.setRegionId(parent);
        }

        if (dTO.getLearnAboutCompanyFrom() != null) {
            Symbol parent = new Symbol();
            parent.setId(dTO.getLearnAboutCompanyFrom().getId());
            parent.setSerial(dTO.getLearnAboutCompanyFrom().getSerial());
            entity.setLearnAboutCompanyfrom(parent);
        }

        if (dTO.getType() == 5) {
            if (dTO.getAssignedTo() != null) {
                Employee employee = new Employee();
                employee.setId(dTO.getAssignedTo().getId());
                entity.setAssignedTo(employee);
            } else if (isagUser.getEmployeeId() != null && isagUser.getEmployeeId().getId() != null) {
                Employee employee = new Employee();
                employee.setId(isagUser.getEmployeeId().getId());
                entity.setAssignedTo(employee);
            }
        }

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);

        }
        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<InvOrganizationSite> list, List<InvOrganizationSiteDTO> dTOList) {
        for (InvOrganizationSite guide : list) {
            dTOList.add(mapTODTO(null, guide));
        }

    }

    private List<InvOrganizationSiteDTO> returnListDTO(List<InvOrganizationSite> list) {
        List<InvOrganizationSiteDTO> DTOlist = new ArrayList();
        returnListDTO(list, DTOlist);
        return DTOlist;
    }

    private List<InvOrganizationSite> returnListEntity(List<InvOrganizationSiteDTO> list, IsagUser isagUser) {
        List<InvOrganizationSite> Entitylist = new ArrayList();
        for (InvOrganizationSiteDTO organizationSiteDTO : list) {
            Entitylist.add(mapToEntity(organizationSiteDTO, isagUser));
        }
        return Entitylist;
    }

    @Override
    public List<contractorDataReportEntity> getcontractorByCode(contractorDataReportSearchBean reportSearchBean) {
        List<contractorDataReportEntity> returnList = null;

        List<InvOrganizationSite> list = em
                .createNamedQuery("InvOrganizationSite.findByContractCode")
                .setParameter("fromCode", reportSearchBean.getContractorCodeFrom() == null ? null : reportSearchBean.getContractorCodeFrom().getCode())
                .setParameter("toCode", reportSearchBean.getContractorCodeTo() == null ? null : reportSearchBean.getContractorCodeTo().getCode())
                .setParameter("branchID", reportSearchBean.getBranchId())
                .getResultList();
        if (list != null && !list.isEmpty()) {
            returnList = new ArrayList<>();
            contractorDataReportEntity entity;
            for (InvOrganizationSite invOrganizationSite : list) {
                entity = new contractorDataReportEntity();
                entity.setCode(invOrganizationSite.getCode() != null ? invOrganizationSite.getCode() : "");
                entity.setName(invOrganizationSite.getName() != null ? invOrganizationSite.getName() : "");
                entity.setFax(invOrganizationSite.getFax() != null ? invOrganizationSite.getFax() : "");
                entity.setEmail(invOrganizationSite.getEmail() != null ? invOrganizationSite.getEmail() : "");
                entity.setMobile(invOrganizationSite.getMobile() != null ? invOrganizationSite.getMobile() : "");
                entity.setAddress(invOrganizationSite.getAddress() != null ? invOrganizationSite.getAddress() : "");
                entity.setOpenBalanceDate(invOrganizationSite.getOpenBalanceDate());
                if (invOrganizationSite.getType() == 0) {
                    entity.setOpenBalance(invOrganizationSite.getOpenBalanceDebit().subtract(invOrganizationSite.getOpenBalanceCredit()));
                } else if (invOrganizationSite.getType() == 1 || invOrganizationSite.getType() == 4) {
                    entity.setOpenBalance(invOrganizationSite.getOpenBalanceCredit().subtract(invOrganizationSite.getOpenBalanceDebit()));
                }
                returnList.add(entity);
            }
        }
        return returnList;

    }

    @Override
    public List<InvOrganizationSiteDTO> getCustomerDTOByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOrganizationSite> detailses = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code", params);
        return returnListDTO(detailses);
    }

    @Override
    public List<InvOrganizationSiteDTO> getCustomerDTOFromAndTo(Integer branchId, invOrganizationSiteSearchBean organizationSiteSearchBean) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder query = new StringBuilder();
        query.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ");
        queryFrom(query, organizationSiteSearchBean, params);
        queryTo(query, organizationSiteSearchBean, params);
        List<InvOrganizationSite> detailses = dao.findListByQuery(query.toString(), params);
        return returnListDTO(detailses);
    }

    private void queryFrom(StringBuilder query, invOrganizationSiteSearchBean From, Map<String, Object> params) {
        if (From != null) {
            if (From.getFrominvOrganizationSiteDTO() != null) {
                params.put("customerFrom", From.getFrominvOrganizationSiteDTO().getCode());
                query.append("AND (e.code >= :customerFrom)");
            }
            if (From.getFromRegion() != null) {
                params.put("RegionFrom", From.getFromRegion().getSerial());
                query.append("AND (e.regionId.serial >= :RegionFrom)");
            }
        }
    }

    private void queryTo(StringBuilder query, invOrganizationSiteSearchBean To, Map<String, Object> params) {
        if (To != null) {
            if (To.getToinvOrganizationSiteDTO() != null) {
                params.put("customerTo", To.getToinvOrganizationSiteDTO().getCode());
                query.append("AND (e.code <= :customerTo)");
            }
            if (To.getToRegion() != null) {
                params.put("RegionTo", To.getToRegion().getSerial());
                query.append("AND e.regionId.serial <= :RegionTo ");
            }
        }
    }

    @Override
    public List<InvOrganizationSiteDTO> AddDelegatorToCustomerDTO(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, IsagUser isagUser) {
        List<InvOrganizationSiteDTO> dTO = new ArrayList();
        List<InvOrganizationSite> invOrganizationSiteList = returnListEntity(invOrganizationSiteDTOList, isagUser);
        for (InvOrganizationSite organizationSite : invOrganizationSiteList) {
            dTO.add(mapTODTO(null, updateOrganizationSite(organizationSite)));
        }
        return dTO;
    }

    public InvOrganizationSiteDTO mapTODTOSmall(InvOrganizationSite entity) {
        InvOrganizationSiteDTO dTO = new InvOrganizationSiteDTO();
        initMapToDTOSmall(dTO, entity);
        return dTO;
    }

    private void initMapToDTOSmall(InvOrganizationSiteDTO dTO, InvOrganizationSite entity) {
        dTO.setId(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
    }

    private void returnListDTOSmall(List<InvOrganizationSite> list, List<InvOrganizationSiteDTO> dTOList) {
        for (InvOrganizationSite guide : list) {
            dTOList.add(mapTODTOSmall(guide));
        }

    }

    private List<InvOrganizationSiteDTO> returnListDTOSmall(List<InvOrganizationSite> list) {
        List<InvOrganizationSiteDTO> DTOlist = new ArrayList();
        returnListDTOSmall(list, DTOlist);
        return DTOlist;
    }

    @Override
    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdDTOSmall(Integer branchId, boolean active, Integer type) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        appendOrganizationTypeAndActive(params, type, sql, active);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);

        returnListDTOSmall(detailses, list);
        return list;
    }

    @Override
    public InvOrganizationSiteDTO getorganizationSiteByBranchIdAndCodeDTOSmall(Integer branchId, boolean active, Integer type, String code) {

        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        appendOrganizationTypeAndActiveAndCode(params, type, sql, active, code);
        InvOrganizationSite detailses = dao.findEntityByQuery(sql.toString(), params);
        return mapTODTOSmall(detailses);
    }

    @Override
    public InvOrganizationSite getorganizationSiteByBranchIdAndCodeSmall(Integer branchId, boolean active, Integer type, String code) {

        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        appendOrganizationTypeAndActiveAndCode(params, type, sql, active, code);
        InvOrganizationSite detailses = dao.findEntityByQuery(sql.toString(), params);
        InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
        invOrganizationSite.setId(detailses.getId());
        invOrganizationSite.setCode(detailses.getCode());
        invOrganizationSite.setName(detailses.getName());
        detailses = null;
        return invOrganizationSite;
    }

    private void appendOrganizationTypeAndActiveAndCode(Map<String, Object> params, Integer type, StringBuilder sql, boolean active, String code) {
        params.put("type", type);
        params.put("code", code);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ");

        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else {
            sql.append(" AND e.type = :type  ");
        }

        if (active) {
            sql.append(" AND e.active = 1 ");
        }

        sql.append(" AND e.code = :code");
    }

    @Override
    public List<InvOrganizationSiteDTO> getorganizationSiteDTOByBranchIdForGlBankModule(IsagUser isagUser, boolean active, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", isagUser.getBranchId().getId());
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((e.type = :type OR e.type = 2)");
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" )");

        sql.append(" ORDER BY e.code");
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params);
        return mapToDTOList(detailses, isagUser);
    }

    public List<InvOrganizationSiteDTO> mapToDTOList(List<InvOrganizationSite> invOrganizationSiteList, IsagUser isagUser) {
        List<InvOrganizationSiteDTO> invPurchaseInvoiceDTOList = new ArrayList<>();
        for (InvOrganizationSite invOrganizationSite : invOrganizationSiteList) {
            invPurchaseInvoiceDTOList.add(mapTODTO(null, invOrganizationSite));
        }
        return invPurchaseInvoiceDTOList;
    }

//    
//     public List<InvOrganizationSiteDTO> getorganizationSiteDTOByBranchIdForGlBankModule(Integer branchId, Integer type) {
//        Map<String, Object> params = new HashMap<>();
//        StringBuilder sql = new StringBuilder();
//        params.put("branchId", branchId);
//        params.put("type", type);
//        sql.append("SELECT COUNT(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = :type ");
//        Integer detailses = dao.findEntityByQuery(sql.toString(), params);
//        
//    }
//        public PieChartModel getLivePieModel() {
//        int random1 = (int) (Math.random() * 1000);
//        int random2 = (int) (Math.random() * 1000);
// 
//        livePieModel.getData().put("Candidate 1", random1);
//        livePieModel.getData().put("Candidate 2", random2);
// 
//        livePieModel.setTitle("Votes");
//        livePieModel.setLegendPosition("ne");
// 
//        return livePieModel;
//    }
    @Override
    public String validateSaveLead(InvOrganizationSiteDTO invOrganizationSiteDTO, IsagUser isagUser) {
        StringBuilder error = new StringBuilder();

        if (invOrganizationSiteDTO.getType() == 5 && invOrganizationSiteDTO.getEmployeeIsSupervisor()) {
            if (invOrganizationSiteDTO.getAssignedTo() == null && isagUser.getEmployeeId() == null) {
                error.append("\n اعطاء المستخدم لموظف معين");
            }
        }
        if (invOrganizationSiteDTO != null) {
            if (invOrganizationSiteDTO.getName() == null || invOrganizationSiteDTO.getName().isEmpty()) {
                error.append("\n يجب تسجيل اسم العميل");
            }
            if ((invOrganizationSiteDTO.getMobile() == null || invOrganizationSiteDTO.getMobile().isEmpty()) && (invOrganizationSiteDTO.getEmail() == null || invOrganizationSiteDTO.getEmail().isEmpty())) {
                error.append("\nيجب تسجيل بيان للاتصال ");
            }
            String vaildationContacts = vaildationContacts(invOrganizationSiteDTO, isagUser);
            if (vaildationContacts != null && !vaildationContacts.isEmpty()) {
                error.append(vaildationContacts);
            }

//            if (invOrganizationSiteDTO.getCompanyName() == null || invOrganizationSiteDTO.getCompanyName().isEmpty()) {
//                error.append("\n يجب تسجيل الشركة التابع لها العميل");
//            }
//            if (invOrganizationSiteDTO.getEmail() == null || invOrganizationSiteDTO.getEmail().isEmpty()) {
//                error.append("\n يجب تسجيل ايميل العميل ");
//            }
            if (invOrganizationSiteDTO.getAssignedTo() == null) {
                error.append("\nيجب اختيار الموظف المتابع للعميل ");
            }
        } else {
            error.append("لا يوجد تفاصيل");
        }

        return (error == null || error.toString().isEmpty()) ? null : error.toString();
    }

    @Override
    public String vaildationContacts(InvOrganizationSiteDTO invOrganizationSiteDTO, IsagUser isagUser) {
        StringBuilder error = new StringBuilder();
        if (invOrganizationSiteDTO != null) {
            List<InvOrganizationSiteDTO> list = findOrganizationSiteDTOByContact(isagUser.getBranchId().getId(), invOrganizationSiteDTO.getMobile(), invOrganizationSiteDTO.getEmail());
            if (!list.isEmpty() && (list.size() > 1 || !list.get(0).getCode().equalsIgnoreCase(invOrganizationSiteDTO.getCode()))) {
                error.append("\nالعميل موجود من قبل ");
            }
        }
        return (error == null || error.toString().isEmpty()) ? null : error.toString();
    }

    @Override
    public synchronized InvOrganizationSiteDTO saveLead(InvOrganizationSiteDTO invOrganizationSiteDTO, IsagUser isagUser) {
        try {
            String error = validateSaveLead(invOrganizationSiteDTO, isagUser);
            if (error == null) {
                InvOrganizationSite invOrganizationSite;
                if (invOrganizationSiteDTO != null && (invOrganizationSiteDTO.getMsg() == null || invOrganizationSiteDTO.getMsg().isEmpty())) {
                    invOrganizationSite = mapToEntity(invOrganizationSiteDTO, isagUser);
                    invOrganizationSite.setType(5);
                    if (invOrganizationSite.getId() == null) {
                        Integer code = getMaxGenericSerialForDetail(isagUser.getBranchId().getId(), 5);
                        invOrganizationSite.setCode(code.toString());
                    }

                    invOrganizationSite = dao.updateEntity(invOrganizationSite);

                    // cRMCustomerInterestedProductsServiceDTO.deleteCrmCustomerInterestedProductDTOList(invOrganizationSiteDTO.getCrmCustomerInterestedProductsDTODeleteList(), isagUser);
                    // cRMCustomerInterestedProductsServiceDTO.saveList(invOrganizationSiteDTO.getCrmCustomerInterestedProductsDTOList(), invOrganizationSite.getId(), isagUser);
                    cRMEventServiceDTO.deleteCrmEventDTOList(invOrganizationSiteDTO.getCrmEventDTODeletedList(), isagUser);
                    cRMEventServiceDTO.saveList(invOrganizationSiteDTO.getCrmEventDTOList(), invOrganizationSite.getId(), invOrganizationSite.getName(), invOrganizationSite.getAssignedTo().getId(), isagUser);

                    invOrganizationSiteDTO = mapTODTO(invOrganizationSiteDTO, invOrganizationSite);
                }
            } else {
                invOrganizationSiteDTO.setMsg(error);
            }
        } catch (Exception e) {
            invOrganizationSiteDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return invOrganizationSiteDTO;
    }

    @Override
    public synchronized String saveTransferLead(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, IsagUser isagUser) {
        StringBuilder error = new StringBuilder();
        try {
            if (invOrganizationSiteDTOList != null && !invOrganizationSiteDTOList.isEmpty()) {
                for (InvOrganizationSiteDTO invOrganizationSiteDTO : invOrganizationSiteDTOList) {
                    Boolean validate = Boolean.FALSE;
                    if (invOrganizationSiteDTO == null || (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getId() == null)
                            || (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getTransferTo() == null)
                            || (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getTransferTo() != null && invOrganizationSiteDTO.getTransferTo().getId() == null)) {
                        error.append("يجب اختيار الموظف المتابع للعميل رقم " + invOrganizationSiteDTO.getCode() + " </br>");
                        validate = Boolean.TRUE;
                    }
                    if (!validate) {
                        if (invOrganizationSiteDTO != null && (invOrganizationSiteDTO.getMsg() == null || invOrganizationSiteDTO.getMsg().isEmpty())) {
                            Employee emp = new Employee();
                            emp.setId(invOrganizationSiteDTO.getTransferTo().getId());
                            Map<String, Object> params = new HashMap<>();
                            params.put("id", invOrganizationSiteDTO.getId());
                            params.put("assignedTo", emp);
                            params.put("modifiedBy", isagUser);
                            params.put("modificationDate", new Date());
                            String query = "UPDATE InvOrganizationSite i SET i.assignedTo = :assignedTo  , i.modificationDate = :modificationDate , i.modifiedBy = :modifiedBy  WHERE i.id = :id ";
                            dao.executeUpdateQuery(query, params);
                        }
                    }
                }
            }

        } catch (Exception e) {
            error = new StringBuilder();
            error.append("حدث خطاء");
            dao.rollbackQuery();
        }

        return error.toString();
    }

    private synchronized <T> Integer getMaxGenericSerialForDetail(Integer branchId, Integer type) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT max(CONVERT(code,UNSIGNED INTEGER)) FROM isag.inv_organization_site i where type = ");
        query.append(type);
        query.append(" AND branch_id = ").append(branchId);
        List<BigInteger> list = dao.executeNativeQuery(query.toString());
        Integer code = list != null && !list.isEmpty() && list.get(0) != null ? list.get(0).intValue() : null;
        if (code == null) {
            return 1;
        } else {
            return code + 1;
        }
    }

    @Override
    public Integer getNumberOfCustomerByBranchId(Integer branchId, Integer assignedTo, Integer createdBy) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        Long invOrganizationSiteCount = 0l;
        if (assignedTo != null) {
            StringBuilder empIds = new StringBuilder();
            empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            invOrganizationSiteCount = dao.findEntityByQuery("SELECT count(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND (e.assignedTo.id in ( " + empIds + " ) ) AND e.leadStatus.serial != 0", params);
            return invOrganizationSiteCount.intValue();
        } else {
            invOrganizationSiteCount = dao.findEntityByQuery("SELECT count(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 ", params);
            return invOrganizationSiteCount.intValue();
        }
    }

    @Override
    public Integer getNumberOfCustomerByBranchIdAndAssignedTo(Integer branchId, Integer assignedTo, Integer createdBy) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        if (assignedTo != null) {
            params.put("assignedTo", assignedTo);
            Long invOrganizationSiteCount = dao.findEntityByQuery("SELECT count(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND (e.assignedTo.id = :assignedTo ) AND e.leadStatus.serial != 0", params);
            return invOrganizationSiteCount.intValue();
        }
        return 0;
    }

    @Override
    public Integer getNumberOfNewCustomerByBranchIdAndAssignedTo(Integer branchId, Integer assignedTo, Integer createdBy) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        if (assignedTo != null) {
            params.put("assignedTo", assignedTo);

            Long invOrganizationSiteCount = dao.findEntityByQuery("SELECT count(e) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND e.assignedTo.id  = :assignedTo AND  e.leadStatus.serial = 1", params);
            //AND e.id NOT IN (SELECT c.customerId.id FROM CrmEvent c WHERE c.branchId.id = :branchId)
            return invOrganizationSiteCount.intValue();
        }
        return 0;
    }

    @Override
    public Integer getNumberOfNewCustomerByBranchId(Integer branchId, Integer assignedTo, Integer createdBy) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        Long invOrganizationSiteCount = 0L;
        if (assignedTo != null) {
            StringBuilder empIds = new StringBuilder();
            empIds = employeeService.returnEmployeeList(assignedTo, empIds);
            invOrganizationSiteCount = dao.findEntityByQuery("SELECT count(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND (e.assignedTo.id in ( " + empIds + " )) AND e.id NOT IN (SELECT c.customerId.id FROM CrmEvent c WHERE c.branchId.id = :branchId) AND e.leadStatus.serial != 0", params);
            return invOrganizationSiteCount.intValue();
        } else {
            invOrganizationSiteCount = dao.findEntityByQuery("SELECT count(e.id) FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND e.id NOT IN (SELECT c.customerId.id FROM CrmEvent c WHERE c.branchId.id = :branchId) ", params);
            return invOrganizationSiteCount.intValue();
        }
    }

    @Override
    public List<InvOrganizationSiteDTO> findOrganizationSiteDTOByContact(Integer branchId, String mobile, String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 ");
        if (mobile != null && email != null && !mobile.isEmpty() && !email.isEmpty()) {
            params.put("mobile", mobile);
            params.put("email", email);
            builder.append(" AND (e.mobile = :mobile OR e.email = :email)");
        } else {
            if (mobile != null && !mobile.isEmpty()) {
                params.put("mobile", mobile);
                builder.append(" AND function('RIGHT',e.mobile,8) = function('RIGHT',:mobile,8)");
            }
            if (email != null && !email.isEmpty()) {
                params.put("email", email);
                builder.append(" AND e.email = :email");
            }
        }
        List<InvOrganizationSite> detailses = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(detailses);
    }

    @Override
    public ChartDTO getCountCustomerByLearnAboutCompanyfrom(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<Object[]> detailses = dao.findListByQuery("SELECT count(e.id) , e.LearnAboutCompanyfrom.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5  group by e.LearnAboutCompanyfrom.id  ", params);
        String title = "نسبه العملاء حسب المصدر ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountCustomerByleadStatus(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<Object[]> detailses = dao.findListByQuery("SELECT count(e.id) , e.leadStatus.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5  group by e.leadStatus.id  ", params);
        String title = "نسبه العملاء حسب الحاله ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountCustomerByAssignedTo(Integer branchId, Date fromDate, Date toDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(e.id) , e.assignedTo.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 ");
        filterChartDate(params, builder, fromDate, toDate);
        builder.append(" group by e.assignedTo.id  ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه العملاء حسب الموظف ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountCustomerByAssignedToAndStatus(Integer branchId, Integer status, Date fromDate, Date toDate) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("status", status);
        builder.append("SELECT count(e.id) , e.assignedTo.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5 AND e.leadStatus.id = :status ");
        filterChartDate(params, builder, fromDate, toDate);
        builder.append(" group by e.assignedTo.id  ");
        List<Object[]> detailses = dao.findListByQuery(builder.toString(), params);
        String title = "نسبه العملاء حسب الحاله للموظف ";
        return preperChartPie(detailses, title);
    }

    @Override
    public ChartDTO getCountCustomerByProjectName(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<Object[]> detailses = dao.findListByQuery("SELECT count(e.id) , e.projectName.name FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = 5  group by e.projectName.id  ", params);
        String title = "نسبه العملاء حسب المشروع ";
        return preperChartPie(detailses, title);
    }

    private Date getmaxCreationDate(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        return dao.findEntityByQuery("SELECT e.creationDate FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.type = :type ", params);
    }

    private ChartDTO preperChart(List<Object[]> details) {
        Map<Object, Number> map = new HashMap<>();
        List<ChartDataDTO> chartDataDTOList = new ArrayList<>();
        ChartDataDTO chartDataDTO = new ChartDataDTO();
        Integer max = 0;
        for (Object[] detail : details) {
            map.put(detail[0].toString(), (Number) detail[1]);
            max = (max < ((Number) detail[1]).intValue() ? ((Number) detail[1]).intValue() : max);
        }
        chartDataDTO.setLabel("العملاء");
        chartDataDTO.setMap(map);
        chartDataDTOList.add(chartDataDTO);
        ChartDTO chartDTO = new ChartDTO();
        chartDTO.setMax(max);
        chartDTO.setMin(0);
        chartDTO.setxLabel("الشهر");
        chartDTO.setyLabel("عدد العملاء");
        chartDTO.setTitle("عدد العملاء لكل شهر");
        chartDTO.setLegendPosition("ne");
        chartDTO.setChartDataDTOList(chartDataDTOList);
        return chartDTO;
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
    public InvOrganizationSite getInvOrganizationSiteBySerial(Integer branchId, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("code", code);
        List<InvOrganizationSite> list = dao.findListByQuery("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND e.code = :code ORDER BY e.code", params);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }

    @Override
    public Map<String, String> findMobileOrganizationSite(Integer branchId) {
        Map<String, String> mobileMap = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<String> list = dao.findListByQuery("SELECT e.mobile FROM InvOrganizationSite e WHERE e.branchId.id = :branchId ", params);
        for (String i : list) {
            mobileMap.put(i, i);
        }
        return mobileMap;
    }

    @Override
    public String saveOpenningBalance(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        String error = null;
        for (InvOrganizationSiteDTO dto : invOrganizationSiteDTOList) {
            if (checkValueDate(dto)) {
                InvOrganizationSite entity = dao.findEntityById(InvOrganizationSite.class, dto.getId());
                entity.setOpenBalanceCredit(dto.getOpenBalanceCredit());
                entity.setOpenBalanceDebit(dto.getOpenBalanceDebit());
                entity.setOpenBalanceDate(dto.getOpenBalanceDate());
                dao.saveEntity(entity);
            } else {
                error = "يجب اختيار تاريخ";
                return error;
            }
        }
        return error;
    }

    public Boolean checkValueDate(InvOrganizationSiteDTO dto) {
        try {
            if ((dto.getOpenBalanceDebit() != null && dto.getOpenBalanceDebit().compareTo(BigDecimal.ZERO) == 1)
                    || (dto.getOpenBalanceCredit() != null && dto.getOpenBalanceCredit().compareTo(BigDecimal.ZERO) == 1)) {
                if (dto.getOpenBalanceDate() == null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    private StringBuilder filterChartDate(Map<String, Object> filters, StringBuilder builder, Date fromDate, Date toDate) {
        Date maxDate = null;
        if ((fromDate != null || toDate != null) && (fromDate == null || toDate == null)) {
            maxDate = getmaxCreationDate((Integer) filters.get("branchId"), 5);
            filters.put("maxDate", maxDate);
        }
        if (fromDate != null && toDate == null && maxDate != null) {
            builder.append(" AND e.creationDate BETWEEN :fromDate AND :maxDate ");
            filters.put("fromDate", fromDate);
        }

        if (toDate != null && fromDate == null && maxDate != null) {
            builder.append(" AND e.creationDate BETWEEN :maxDate AND :toDate ");
            filters.put("toDate", toDate);
        }

        if (fromDate != null && toDate != null) {
            builder.append(" AND e.creationDate BETWEEN :fromDate AND :toDate ");
            filters.put("fromDate", fromDate);
            filters.put("toDate", toDate);
        }
        return builder;
    }

    @Override
    public InvOrganizationSiteDTO addNewCustomer(InvOrganizationSiteDTO invOrganizationSiteDTO, List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, IsagUser isagUser) {

        CurrencyDTO currencyDefault = currencyServiceDTO.getDefaultCurrenciesByCompanyId(isagUser.getCompanyId().getId());

        if (invOrganizationSiteDTO != null && invOrganizationSiteDTO.getName() != null && currencyDefault != null) {
            invOrganizationSiteDTO.setType(0);
            invOrganizationSiteDTO.setCurrencyId(currencyDefault);
            invOrganizationSiteDTO.setActive(1);

            Integer code = getMaxGenericSerialForDetail(isagUser.getBranchId().getId(), 0);
//            GlAccountDTO glAccountDTO = new GlAccountDTO();
//            glAccountDTO.setId(12892);
//            invOrganizationSiteDTO.setAccountId(glAccountDTO);
//            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
//            inventoryDelegatorDTO.setId(77);
//            invOrganizationSiteDTO.setDelegatorId(inventoryDelegatorDTO);
            invOrganizationSiteDTO.setCode(code.toString());

            GlAccountDTO glAccountDTO = glAccountServiceDTO.getGlAccountForBank(isagUser.getBranchId().getId(), 1);
            if (glAccountDTO != null) {
                invOrganizationSiteDTO.setAccountId(glAccountDTO);
            }

            invOrganizationSiteDTO = addOrganizationSiteDTO(invOrganizationSiteDTO, isagUser);
            invOrganizationSiteDTOList.add(0, invOrganizationSiteDTO);

        }
        return invOrganizationSiteDTO;
    }

    @Override
    public InvOrganizationSiteDTO addOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, IsagUser isagUser) {
        try {

            invOrganizationSiteDTO.setMsg(null);
            String error = genralValidateDelivery(isagUser, invOrganizationSiteDTO);
            if (error.isEmpty()) {
                InvOrganizationSite invOrganizationSite = mapToEntity(invOrganizationSiteDTO, isagUser);
                invOrganizationSite = dao.updateEntity(invOrganizationSite);
                invOrganizationSiteDTO = mapTODTO(null, invOrganizationSite);
            } else {
                invOrganizationSiteDTO.setMsg(error);
            }
            return invOrganizationSiteDTO;
        } catch (Exception e) {
        }
        return invOrganizationSiteDTO;

    }

    public String genralValidateDelivery(IsagUser isagUser, InvOrganizationSiteDTO invOrganizationSiteDTO) {
        StringBuilder errorMessage = new StringBuilder();
//        if (!validateName(isagUser, invOrganizationSiteDTO.getId(), invOrganizationSiteDTO.getName(), invOrganizationSiteDTO.getType())) {
//            errorMessage.append("الاسم موجود");
//
//        }
//        if (!validateCode(isagUser, invOrganizationSiteDTO.getId(), invOrganizationSiteDTO.getCode(), invOrganizationSiteDTO.getType())) {
//            errorMessage.append("الكود موجود");
//        }
        return errorMessage.toString();
    }

    @Override
    public Boolean validateName(IsagUser isagUser, Integer invOrganizationSiteDTOid, String name, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", isagUser.getBranchId().getId());
        params.put("name", name);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvOrganizationSite i where i.branchId.id = :branchId  and i.name = :name ");
        if (invOrganizationSiteDTOid != null) {
            params.put("invOrganizationSiteDTOid", invOrganizationSiteDTOid);
            query.append(" and i.id != :invOrganizationSiteDTOid ");
        }
        if (type != null) {
            if (type == 0) {
                query.append(" and (i.type = 0 or i.type = 2 ) ");
            } else if (type == 1) {
                query.append(" and (i.type = 1 or i.type = 2 ) ");
            } else if (type == 2) {
                query.append(" and (i.type = 0 or i.type = 1 or i.type = 2 ) ");
            } else {
                params.put("type", type);
                query.append(" and i.type = :type ");
            }

        }
        List<InvOrganizationSite> invOrganizationSiteList = dao.findListByQuery(query.toString(), params);
        if (invOrganizationSiteList.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean validateCode(IsagUser isagUser, Integer invOrganizationSiteDTOid, String code) {

        StringBuilder quarry = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", isagUser.getBranchId().getId());

        params.put("code", code);
        quarry.append("SELECT i FROM InvOrganizationSite i where i.branchId.id = :branchId  and i.code = :code  ORDER BY i.code");
        if (invOrganizationSiteDTOid != null) {
            params.put("invOrganizationSiteDTOid", invOrganizationSiteDTOid);
            quarry.append("and i.id != :invOrganizationSiteDTOid");

        }
        List<InvOrganizationSite> invOrganizationSiteList = dao.findListByQuery(quarry.toString(), params);
        if (invOrganizationSiteList == null || invOrganizationSiteList.isEmpty()) {
            return true;
        } else {

            return false;
        }

    }

    @Override
    public Boolean validateCode(IsagUser isagUser, Integer invOrganizationSiteDTOid, String code, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", isagUser.getBranchId().getId());
        params.put("code", code);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvOrganizationSite i where i.branchId.id = :branchId  and i.code = :code ");
        if (invOrganizationSiteDTOid != null) {
            params.put("invOrganizationSiteDTOid", invOrganizationSiteDTOid);
            query.append(" and i.id != :invOrganizationSiteDTOid ");
        }

        if (type != null) {
            if (type == 0) {
                query.append(" and (i.type = 0 or i.type = 2 ) ");
            } else if (type == 1) {
                query.append(" and (i.type = 1 or i.type = 2 ) ");
            } else if (type == 2) {
                query.append(" and (i.type = 0 or i.type = 1 or i.type = 2 ) ");
            } else {
                params.put("type", type);
                query.append(" and i.type = :type ");
            }
        }

        List<InvOrganizationSite> invOrganizationSiteList = dao.findListByQuery(query.toString(), params);
        if (invOrganizationSiteList.size() == 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public InvOrganizationSiteDTO mapToDTOSmall(InvOrganizationSite invOrganizationSite) {
        InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        invOrganizationSiteDTO.setId(invOrganizationSite.getId());
        invOrganizationSiteDTO.setName(invOrganizationSite.getName());
        invOrganizationSiteDTO.setCode(invOrganizationSite.getCode());
        invOrganizationSiteDTO.setMobile(invOrganizationSite.getMobile());
        invOrganizationSiteDTO.setDiscount(invOrganizationSite.getDiscount());
        invOrganizationSiteDTO.setAddress(invOrganizationSite.getAddress());
        if (invOrganizationSite.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(invOrganizationSite.getCurrencyId().getId());
            currencyDTO.setCode(invOrganizationSite.getCurrencyId().getCode());
            currencyDTO.setName(invOrganizationSite.getCurrencyId().getName());
            invOrganizationSiteDTO.setCurrencyId(currencyDTO);
        }
        if (invOrganizationSite.getParent() != null) {
            InvOrganizationSiteDTO parent = new InvOrganizationSiteDTO();
            parent.setId(invOrganizationSite.getParent().getId());
            parent.setName(invOrganizationSite.getParent().getName());
            parent.setCode(invOrganizationSite.getParent().getCode());
            parent.setMobile(invOrganizationSite.getParent().getMobile());
            parent.setDiscount(invOrganizationSite.getParent().getDiscount());
            invOrganizationSiteDTO.setParent(parent);
        }
//        if (invOrganizationSite.getOfferCompanyId() != null) {
//            OfferCompanyDTO offerCompanyDTO = new OfferCompanyDTO();
//            offerCompanyDTO.setId(invOrganizationSite.getOfferCompanyId().getId());
//            offerCompanyDTO.setName(invOrganizationSite.getOfferCompanyId().getName());
//            offerCompanyDTO.setCode(invOrganizationSite.getOfferCompanyId().getCode());
//            offerCompanyDTO.setDiscount(invOrganizationSite.getOfferCompanyId().getDiscount());
//            invOrganizationSiteDTO.setOfferCompanyDTO(offerCompanyDTO);
//        }

        if (invOrganizationSite.getAccountId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setAccNumber(invOrganizationSite.getAccountId().getAccNumber());
            accountDTO.setId(invOrganizationSite.getAccountId().getId());
            invOrganizationSiteDTO.setAccountId(accountDTO);
        }
        invOrganizationSiteDTO.setOpenBalanceCredit(invOrganizationSite.getOpenBalanceCredit());
        invOrganizationSiteDTO.setOpenBalanceDebit(invOrganizationSite.getOpenBalanceDebit());
        invOrganizationSiteDTO.setOpenBalanceDate(invOrganizationSite.getOpenBalanceDate());

        return invOrganizationSiteDTO;
    }

}
