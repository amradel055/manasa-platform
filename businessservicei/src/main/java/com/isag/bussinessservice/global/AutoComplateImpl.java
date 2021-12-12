/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.bussinessservice.global;

import com.isag.businessservice.EmployeeService;
import com.isag.core.GenericDAO;
import com.isag.dto.CostCenterDTO;
import com.isag.dto.CurrencyDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.GlAdminUnitDTO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.HrSalaryEffectDTO;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvItemDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.InventoryDelegatorDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.CostCenter;
import com.isag.entity.Employee;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.GlBank;
import com.isag.entity.HrSalaryEffect;
import com.isag.entity.InstProjectGuide;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.RscWorkUnitsGuide;
import com.isag.entity.Symbol;
import com.isag.views.ItemsBarcodesDetailsView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class AutoComplateImpl implements AutoCompleteService {

    @EJB
    private GenericDAO dao;
    @EJB
    private EmployeeService employeeService;

    @Override
    public List<InstProjectGuide> autoCompleteProjectGuidesActive(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        String sql = "SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:branchId AND cs.active=1 AND cs.level = 3 AND cs.id NOT IN (select ic.projectId.id from InstContract ic where ic.projectId IS NOT NULL) AND ((:qeury is null OR cs.name  like CONCAT('%',:qeury ,'%'))  OR (:qeury is null OR cs.code  like CONCAT('%',:qeury ,'%')))";
        return dao.findListByQuery(sql, params, 0, 10);
    }

    @Override
    public List<InstProjectGuideDTO> autoCompleteInstProjectGuideDTOActive(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        String sql = "SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:branchId AND cs.active=1 AND ((:qeury is null OR cs.name  like CONCAT('%',:qeury ,'%'))  OR (:qeury is null OR cs.code  like CONCAT('%',:qeury ,'%')))";
        List<InstProjectGuide> instProjectGuideList = dao.findListByQuery(sql, params, 0, 10);
        List<InstProjectGuideDTO> list = new ArrayList<>();
        list = returnListDTO(instProjectGuideList);
        return list;
    }

    @Override
    public List<ItemsBarcodesDetailsView> findAllItemsBarcodesDetailsViewBranchId(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendItemsBarcodesDetailsViewBranchId(sql);
        return dao.findListByQuery(sql.toString(), params);
    }

    private void appendItemsBarcodesDetailsViewBranchId(StringBuilder sql) {

        sql.append("SELECT e FROM ItemsBarcodesDetailsView e WHERE e.branchId = :branchId AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.barcode  like CONCAT('%',:qeury ,'%') ))");

        sql.append(" ORDER BY e.barcode");
    }

    @Override
    public List<RscWorkUnitsGuideDTO> autoCompleteRscWorkUnitDTOActive(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        String sql = "SELECT cs FROM RscWorkUnitsGuide cs WHERE cs.branchId.id=:branchId AND cs.active=1  AND ((:qeury is null OR cs.name  like CONCAT('%',:qeury ,'%'))  OR (:qeury is null OR cs.code  like CONCAT('%',:qeury ,'%')))";
        List<RscWorkUnitsGuide> rscWorkUnitsGuides = dao.findListByQuery(sql, params, 0, 10);
        List<RscWorkUnitsGuideDTO> list = new ArrayList<>();
        list = returnListDTO(rscWorkUnitsGuides);
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSiteDTO(Integer branchId, boolean active, Integer type, String qeury) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendOrganizationTypeAndActive(params, type, sql, active);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        list = returnListDTO(detailses);
        return list;
    }

    private void appendOrganizationTypeAndActive(Map<String, Object> params, Integer type, StringBuilder sql, boolean active) {
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((:qeury is null OR e.nationIdSerial  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.phone  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.mobile  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.email  like CONCAT('%',:qeury ,'%') ) )");
        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else {
            sql.append(" AND e.type = :type  ");
        }
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" ORDER BY e.id");
    }

    @Override
    public List<InstProjectGuideDTO> autoCompleteAllInstProjectGuideDTOActive(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        String sql = "SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:branchId AND cs.active=1 AND cs.level = 3 AND ((:qeury is null OR cs.name  like CONCAT('%',:qeury ,'%'))  OR (:qeury is null OR cs.code  like CONCAT('%',:qeury ,'%')))";
        List<InstProjectGuide> instProjectGuideList = dao.findListByQuery(sql, params, 0, 10);
        List<InstProjectGuideDTO> list = new ArrayList<>();
        list = returnListDTO(instProjectGuideList);
        return list;
    }

    @Override
    public List<InstProjectGuideDTO> autoCompleteAllInstProjectGuideDTOActiveWithOutLevel(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        String sql = "SELECT cs FROM InstProjectGuide cs WHERE cs.branchId.id=:branchId AND cs.active=1 AND ((:qeury is null OR cs.name  like CONCAT('%',:qeury ,'%'))  OR (:qeury is null OR cs.code  like CONCAT('%',:qeury ,'%')))";
        List<InstProjectGuide> instProjectGuideList = dao.findListByQuery(sql, params);
        List<InstProjectGuideDTO> list = new ArrayList<>();
        list = returnListDTO(instProjectGuideList);
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSite(Integer branchId, boolean active, Integer type, String qeury) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);

        appendOrganizationTypeAndActive(params, type, sql, active, qeury, null, false);
        List<InvOrganizationSite> detailses;
        if (type == 4) {
            detailses = dao.findListByQuery(sql.toString(), params);
        } else {
            detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        }
        list = returnListDTO(detailses);
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> autoCompleteConnectorAndSupplier(Integer branchId, boolean active, String qeury) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);

        appendOrganizationTypeAndActive(params, 41, sql, active, qeury, null, false);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        list = returnListDTO(detailses);
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSite(Integer branchId, boolean active, Integer type, String qeury, Integer assignedTo) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);

        appendOrganizationTypeAndActive(params, type, sql, active, qeury, assignedTo, false);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        list = returnListDTO(detailses);
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> autoCompleteAllLeads(Integer branchId, String qeury) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);

        appendOrganizationTypeAndActive(params, 5, sql, false, qeury, null, false);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        list = returnListDTO(detailses);
        return list;
    }

    @Override
    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSiteWitAssignedList(Integer branchId, boolean active, Integer type, String qeury, Integer assignedTo) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        Boolean employeesIsSuperVisor = employeeService.getAllEmployeesIsSuperVisor(assignedTo);
        appendOrganizationTypeAndActive(params, type, sql, active, qeury, assignedTo, employeesIsSuperVisor);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        list = returnListDTO(detailses);
        return list;
    }

    @Override
    public List<EmployeeDTO> autoCompleteEmployee(Integer branchId, String qeury) {
        List<EmployeeDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        appendEmployee(params, sql, qeury);
        List<Employee> detailses = dao.findListByQuery(sql.toString(), params);
        list = returnListDTO(detailses);
        return list;
    }

    @Override
    public List<SymbolDTO> autoCompleteSymbolByCompanyId(Integer companyId,Integer symbolType , String qeury) {
        List<SymbolDTO> list = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        query.append("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=41 AND sym.companyId.id=:companyId");
        if(symbolType != null){
            params.put("symbolType", symbolType);
            query.append(" AND sym.symbolType = :symbolType");
        }
        appendQuery(qeury, query, params);
        List<Symbol> detailses = dao.findListByQuery(query.toString(), params);
       list = returnListDTO(detailses);
        return list;
    }

    private void appendQuery(String qeury, StringBuilder query, Map<String, Object> params) {
        if (qeury != null && !qeury.isEmpty()) {
            query.append(" AND ((:qeury is null OR sym.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR sym.serial  like CONCAT('%',:qeury ,'%') ) )");
            params.put("qeury", qeury);
        }
    }

    @Override
    public List<GlBankDTO> autoCompleteGlbankByUserAndBranchId(Integer branchId, Integer userId, String qeury) {
        Map<String, Object> params = new HashMap<>();
        List<GlBankDTO> dTOList = new ArrayList<>();
        params.put("branchId", branchId);
        params.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT b FROM IsagUserBank i LEFT JOIN i.bankId b WHERE i.userId.id =:userId AND i.branchId.id = :branchId ");
        if (qeury != null && !qeury.isEmpty()) {
            sql.append(" AND ((:qeury is null OR b.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR b.code  like CONCAT('%',:qeury ,'%') ) )");
            params.put("qeury", qeury);
        }
        List<GlBank> list = dao.findListByQuery(sql.toString(), params, 0, 10);
        dTOList = returnListDTO(list);
        return dTOList;
    }

    @Override
    public List<EmployeeDTO> autoCompleteEmployeesListByCompanyId(Integer companyId, Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap<>();
        List<EmployeeDTO> dTOList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        params.put("companyId", companyId);
        params.put("branchId", branchId);
        sql.append("SELECT e.employeeId FROM IsagUser e WHERE  e.employeeId.companyId.id = :companyId AND e.branchId.id = :branchId");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.employeeId.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.employeeId.code  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.employeeId.phone  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.employeeId.mobile  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.employeeId.email  like CONCAT('%',:qeury ,'%') ) )");
            params.put("qeury", qeury);
        }
        sql.append(" ORDER BY e.employeeId.code");
        List<Employee> list = dao.findListByQuery(sql.toString(), params, 0, 10);
        dTOList = returnListDTO(list);
        return dTOList;
    }

    private void appendOrganizationTypeAndActive(Map<String, Object> params, Integer type, StringBuilder sql, boolean active, String qeury, Integer assignedTo, Boolean assignedIsLeader) {
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.phone  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.mobile  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.email  like CONCAT('%',:qeury ,'%') ) )");
            params.put("qeury", qeury);
        }
        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else if (type == 5) {
            sql.append(" AND e.type = :type ");
            if (assignedIsLeader && assignedTo != null) {
//                List<Integer> assignedList = employeeService.getAllEmployeesListByParentAndchield((Integer) params.get("branchId"), assignedTo);
//                assignedList.add(assignedTo);
                StringBuilder empIds = new StringBuilder();
                empIds = employeeService.returnEmployeeList(assignedTo, empIds);
                sql.append(" AND e.assignedTo.id IN ( ").append(empIds).append(" )");
            } else if (assignedTo != null) {
                params.put("assignedTo", assignedTo);
                sql.append(" AND e.assignedTo.id = :assignedTo");
            }

        } else if (type == 4) {
            sql.append(" AND (e.type = :type  OR e.type = 1) ");
        } else {
            sql.append(" AND e.type = :type  ");
        }
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" ORDER BY e.code");
    }

    private void appendEmployee(Map<String, Object> params, StringBuilder sql, String qeury) {
        sql.append("SELECT e FROM Employee e WHERE e.branchId.id = :branchId ");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.phone  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.mobile  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.email  like CONCAT('%',:qeury ,'%') ) )");
            params.put("qeury", qeury);
        }
        sql.append(" ORDER BY e.code");
    }

    private void appendDriver(Map<String, Object> params, StringBuilder sql, String qeury) {
        sql.append("SELECT e FROM Employee e WHERE e.branchId.id = :branchId AND e.remType = 2");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.phone  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.mobile  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.email  like CONCAT('%',:qeury ,'%') ) )");
            params.put("qeury", qeury);
        }
        sql.append(" ORDER BY e.code");
    }

    private void appendRemEmployee(Map<String, Object> params, StringBuilder sql, String qeury) {
        sql.append("SELECT e FROM Employee e WHERE e.branchId.id = :branchId AND e.remType = 1 AND e.remCompany.id = :remCompanyId");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.phone  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.mobile  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.email  like CONCAT('%',:qeury ,'%') ) )");
            params.put("qeury", qeury);
        }
        sql.append(" ORDER BY e.code");
    }

    private void appendRemCompanyDTO(Map<String, Object> params, StringBuilder sql, String qeury) {
        sql.append("SELECT e FROM RemCompany e WHERE e.branchId.id = :branchId ");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.phone  like CONCAT('%',:qeury ,'%') ))");
            params.put("qeury", qeury);
        }
        sql.append(" ORDER BY e.code");
    }

    private void appendRemPointsDTO(Map<String, Object> params, StringBuilder sql, String qeury) {
        sql.append("SELECT e FROM RemPoints e WHERE e.branchId.id = :branchId ");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')))");
            params.put("qeury", qeury);
        }
//        sql.append(" ORDER BY e.code");
    }

    private void appendRemBusesDTO(Map<String, Object> params, StringBuilder sql, String qeury) {
        sql.append("SELECT e FROM RemBuses e WHERE e.branchId.id = :branchId ");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%') OR e.code  like CONCAT('%',:qeury ,'%')))");
            params.put("qeury", qeury);
        }
        sql.append(" ORDER BY e.code");
    }

    private void appendRemTrafficDTO(Map<String, Object> params, StringBuilder sql, String qeury) {
        sql.append("SELECT e FROM RemTraffic e WHERE e.branchId.id = :branchId ");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%') OR e.code  like CONCAT('%',:qeury ,'%')))");
            params.put("qeury", qeury);
        }
        sql.append(" ORDER BY e.code");
    }

    private void appendRemShiftDTO(Map<String, Object> params, StringBuilder sql, String qeury) {
        sql.append("SELECT e FROM RemEmployeeShift e WHERE e.remCompanyId.id = :remCompanyId ");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%') OR e.code  like CONCAT('%',:qeury ,'%')))");
            params.put("qeury", qeury);
        }
        sql.append(" ORDER BY e.code");
    }

    @Override
    public List<InventoryDelegatorDTO> autoCompleteInventoryDelegatorByBranch(Integer branchId, Integer type, String qeury) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("qeury", qeury);
        String sql = "SELECT i FROM InventoryDelegator i where i.branchId.id = :branchId AND i.type = :type AND ((:qeury is null OR i.name  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR i.code  like CONCAT('%',:qeury ,'%'))) ORDER BY i.code";
        List<InventoryDelegator> inventoryDelegatorList = dao.findListByQuery(sql, params, 0, 10);
        List<InventoryDelegatorDTO> list = new ArrayList<>();
        list = returnListDTO(inventoryDelegatorList);
        return list;
    }

    @Override
    public List<CostCenterDTO> autoCompleteActiveSubCostCentersByBranch(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        List<CostCenter> costCenterList = dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.parent IS NOT NULL AND cs.branchId.id=:branchId AND cs.active = 1 AND ((:qeury is null OR cs.name  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR cs.code  like CONCAT('%',:qeury ,'%')))  ORDER BY cs.code ASC ", params, 0, 10);
        List<CostCenterDTO> list = new ArrayList<>();
        list = returnListDTO(costCenterList);
        return list;
    }

    private List<GlAdminUnit> getAllAdminUnitByBranchIdActive(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        return dao.findListByQuery("SELECT e FROM GlAdminUnit e WHERE  e.branchId.id= :branchId and e.active = 1 AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%')))  ORDER BY e.code ASC ", params, 0, 10);
    }

    @Override
    public List<InvItemDTO> autoCompleteInvItem(Integer branchId, String qeury) {
        List<InvItemDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendInvItem(sql);
        List<InvItem> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        list = returnListDTO(detailses);
        return list;
    }

    private void appendInvItem(StringBuilder sql) {

        sql.append("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId AND e.isInventoryItem = 1 AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ))");

        sql.append(" ORDER BY e.code");
    }

    @Override
    public List<GlAdminUnitDTO> autoCompleteSubAdminUnitByBranchIdActiveDTO(Integer branchId, String qeury) {
        List<GlAdminUnit> allAdminUnits = getAllAdminUnitByBranchIdActive(branchId, qeury);
        List<GlAdminUnit> childrens = new ArrayList<>();
        Map<Integer, GlAdminUnit> map = new HashMap<>();

        for (GlAdminUnit gau : allAdminUnits) {
            if (gau.getParent() != null) {
                map.put(gau.getParent().getId(), gau);
            }
        }

        for (GlAdminUnit gau : allAdminUnits) {
            if (!map.containsKey(gau.getId())) {
                if (gau.getParent() == null) {
                    childrens.add(gau);
                    continue;
                }
                childrens.add(gau);
            }
        }
        List<GlAdminUnitDTO> list = new ArrayList<>();
        list = returnListDTO(childrens);
        return list;
    }

    @Override
    public List<HrSalaryEffectDTO> autoCompleteHrSalaryEffect(Integer branchId, String qeury) {
        List<HrSalaryEffectDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        appendHrSalaryEffect(params, sql, qeury);
        List<HrSalaryEffect> detailses = dao.findListByQuery(sql.toString(), params);
        list = returnListDTO(detailses);
        return list;
    }

    private void appendHrSalaryEffect(Map<String, Object> params, StringBuilder sql, String qeury) {
        sql.append("SELECT e FROM HrSalaryEffect e WHERE e.branchId.id = :branchId ");
        if (qeury != null && !qeury.trim().isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ) )");
            params.put("qeury", qeury);
        }
        sql.append(" ORDER BY e.code");
    }

    private <T, E> List<T> returnListDTO(List<E> list) {
        List<T> DTOlist = new ArrayList();
        for (E e : list) {
            DTOlist.add((T) initMapToDTO(e));
        }
        return DTOlist;
    }

    private <T, E> T initMapToDTO(E Entity) {
        if (Entity.getClass().equals(InvOrganizationSite.class)) {
            return mapToInvOrganizationSite(Entity);
        } else if (Entity.getClass().equals(InstProjectGuide.class)) {
            return mapToInstProjectGuideDTO(Entity);
        } else if (Entity.getClass().equals(InventoryDelegator.class)) {
            return mapToInventoryDelegatorDTO(Entity);
        } else if (Entity.getClass().equals(CostCenter.class)) {
            return mapToCostCenterDTO(Entity);
        } else if (Entity.getClass().equals(GlAdminUnit.class)) {
            return mapToGlAdminUnitDTO(Entity);
        } else if (Entity.getClass().equals(InvItem.class)) {
            return mapToInvItemDTO(Entity);
        } else if (Entity.getClass().equals(Employee.class)) {
            return mapToEmployeeDTO(Entity);
        } else if (Entity.getClass().equals(RscWorkUnitsGuide.class)) {
            return mapToRscWorkUnitsGuideDTO(Entity);
        } else if (Entity.getClass().equals(HrSalaryEffect.class)) {
            return mapToHrSalaryEffectDTO(Entity);
        } else if (Entity.getClass().equals(GlBank.class)) {
            return mapToGlBankDTO(Entity);
        }else if (Entity.getClass().equals(Symbol.class)) {
            return mapToSymbolDTO(Entity);
        }
        return null;
    }

    private <E, T> T mapToInvOrganizationSite(E Entity) {
        InvOrganizationSiteDTO dTO = new InvOrganizationSiteDTO();
        InvOrganizationSite entity = (InvOrganizationSite) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setMobile(entity.getMobile());
        dTO.setAddress(entity.getAddress());
        dTO.setClientImage(entity.getClientImage());
        if (entity.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(entity.getCurrencyId().getId());
            currencyDTO.setCode(entity.getCurrencyId().getCode());
            currencyDTO.setName(entity.getCurrencyId().getName());
            dTO.setCurrencyId(currencyDTO);
        }
        return (T) dTO;
    }

    private <E, T> T mapToInstProjectGuideDTO(E Entity) {
        InstProjectGuideDTO dTO = new InstProjectGuideDTO();
        InstProjectGuide entity = (InstProjectGuide) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToInventoryDelegatorDTO(E Entity) {
        InventoryDelegatorDTO dTO = new InventoryDelegatorDTO();
        InventoryDelegator entity = (InventoryDelegator) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToCostCenterDTO(E Entity) {
        CostCenterDTO dTO = new CostCenterDTO();
        CostCenter entity = (CostCenter) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToGlAdminUnitDTO(E Entity) {
        GlAdminUnitDTO dTO = new GlAdminUnitDTO();
        GlAdminUnit entity = (GlAdminUnit) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToInvItemDTO(E Entity) {
        InvItemDTO dTO = new InvItemDTO();
        InvItem entity = (InvItem) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToEmployeeDTO(E Entity) {
        EmployeeDTO dTO = new EmployeeDTO();
        Employee entity = (Employee) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToRscWorkUnitsGuideDTO(E Entity) {
        RscWorkUnitsGuideDTO dTO = new RscWorkUnitsGuideDTO();
        RscWorkUnitsGuide entity = (RscWorkUnitsGuide) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToHrSalaryEffectDTO(E Entity) {
        HrSalaryEffectDTO dTO = new HrSalaryEffectDTO();
        HrSalaryEffect entity = (HrSalaryEffect) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToGlBankDTO(E Entity) {
        GlBankDTO dTO = new GlBankDTO();
        GlBank entity = (GlBank) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }
    
    private <E, T> T mapToSymbolDTO(E Entity) {
        SymbolDTO dTO = new SymbolDTO();
        Symbol entity = (Symbol) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setSerial(entity.getSerial());
        dTO.setCost(entity.getCost());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    @Override
    public List<ItemsBarcodesDetailsView> autoCompleteItemsBarcodesDetailsViewBranchId(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT e FROM ItemsBarcodesDetailsView e WHERE e.branchId = :branchId  ");
        if (qeury != null && !qeury.isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.barcode  like CONCAT('%',:qeury ,'%') ) )");
            params.put("qeury", qeury);
        }

        List<ItemsBarcodesDetailsView> list = dao.findListByQuery(sql.toString(), params, 0, 10);
        return list;
    }

}
