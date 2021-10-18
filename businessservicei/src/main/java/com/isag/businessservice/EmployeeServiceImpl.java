/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.EmployeeReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.DepartmentDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.HrSponsorDTO;
import com.isag.dto.RemCompanyDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.Department;
import com.isag.entity.Employee;
import com.isag.entity.HrSponsor;
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
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class EmployeeServiceImpl implements EmployeeService {

    @EJB
    private GenericDAO dao;
    @EJB
    HrSponsorService hrSponsorService;
    @EJB
    HrEmergencyContactService emergencyContactService;
    @EJB
    HrEmployeeFellowsService employeeFellowsService;
    @EJB
    HrEmpQualificationsService empQualificationsService;

    @Override
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO, IsagUser isagUser) {

        // Add method which includes the 4 tables data in .
        Employee entity = mapFromDTO(employeeDTO, isagUser);
        entity = dao.updateEntity(entity);
        employeeDTO.getHrEmergencyContactDTO().setEmployeeId(entity.getId());
        emergencyContactService.addHrEmergencyContact(employeeDTO.getHrEmergencyContactDTO(), isagUser);
        employeeFellowsService.addHrEmployeeFellowsList(employeeDTO.getHrEmployeeFellowsDTOList(), isagUser, entity.getId());
        empQualificationsService.addHrEmpQualificationsList(employeeDTO.getHrEmpQualificationsDTOList(), isagUser, entity.getId());

        employeeDTO = mapTODTO(entity, employeeDTO);

        return employeeDTO;

    }

    @Override
    public List<Employee> getAllEmployeesListByBranchId(Integer branchId) {

        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<EmployeeDTO> dTOList = new ArrayList<>();
        List<Employee> list = dao.findListByQuery("SELECT d FROM Employee d WHERE d.branchId.id=:branchId", params);
        return list;
    }
    
    @Override
    public List<EmployeeDTO> getAllDriversListByBranchId(Integer branchId,Integer type) {

        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("type", type);
        List<EmployeeDTO> dTOList = new ArrayList<>();
        List<Employee> list = dao.findListByQuery("SELECT d FROM Employee d WHERE d.branchId.id=:branchId AND d.remType = :type", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<EmployeeDTO> getEmployeebyBranchId(Integer branchId, EmployeeReportSearchBean employeeReportSearchBean) {

        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        StringBuilder query = new StringBuilder();
        query.append(" SELECT i FROM Employee i WHERE i.branchId.id =:branchId ");
        appendEmpFrom(employeeReportSearchBean, params, query);
        appendEmpTo(employeeReportSearchBean, params, query);
        List<EmployeeDTO> dTOList = new ArrayList<>();
        List<Employee> employees = dao.findListByQuery(query.toString(), params);
        returnListDTO(employees, dTOList);
        return dTOList;

    }

    private void appendEmpTo(EmployeeReportSearchBean employeeReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (employeeReportSearchBean.getEmpTo() != null) {
            params.put("empTo", employeeReportSearchBean.getEmpTo().getCode());
            query.append(" and i.code <= :empTo ");

        }
    }

    private void appendEmpFrom(EmployeeReportSearchBean employeeReportSearchBean, Map<String, Object> params, StringBuilder query) {
        if (employeeReportSearchBean.getEmpFrom() != null) {
            params.put("empFrom", employeeReportSearchBean.getEmpFrom().getCode());
            query.append(" and i.code >= :empFrom ");

        }
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesListByParentId(Integer parentId) {

        Map<String, Object> params = new HashMap();
        params.put("parentId", parentId);
        List<EmployeeDTO> dTOList = new ArrayList<>();
        List<Employee> list = dao.findListByQuery("SELECT d FROM Employee d WHERE d.parentId.id=:parentId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesListByParentAndchields(Integer branchId, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("parentId", parentId);
        List<EmployeeDTO> dTOList = new ArrayList<>();
        List<Employee> list = dao.findListByQuery("SELECT e FROM Employee e JOIN e.parentId e2 WHERE e.branchId.id = :branchId AND e2.parentId.id=:parentId or e.parentId.id=:parentId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<Integer> getAllEmployeesListByParentAndchield(Integer branchId, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("parentId", parentId);
        // query.append("SELECT e.id FROM InvOrganizationSite e LEFT JOIN e.parent e2 WHERE e.branchId.id = :branchId AND e.type = 5 AND (e.parent.id =: parentId OR e.parent.id = e2.id)");

        List<Integer> list = dao.findListByQuery("SELECT e.id FROM Employee e LEFT JOIN e.parentId c WHERE e.branchId.id = :branchId AND ( e.parentId.id = :parentId OR e.parentId.id= c.id )", params);
        return list;
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesListByParentId(Integer companyId, Integer branchId, Integer parentId) {

        Map<String, Object> params = new HashMap();

        params.put("branchId", branchId);
        params.put("companyId", companyId);
        List<EmployeeDTO> dTOList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Employee d WHERE d.companyId.id = :companyId AND  d.branchId.id = :branchId AND ");
        Boolean employeesIsSuperVisor = getAllEmployeesIsSuperVisor(parentId);
        if (employeesIsSuperVisor) {
//            List<Integer> parentList = getAllEmployeesListByParentAndchield(branchId, parentId);
//            parentList.add(parentId);
            StringBuilder empIds = new StringBuilder();
            empIds = returnEmployeeList(parentId, empIds);
            builder.append(" d.parentId.id IN ( " + empIds + " )");
        } else {
            params.put("parentId", parentId);
            builder.append(" d.parentId.id = :parentId ");
        }
        List<Employee> list = dao.findListByQuery(builder.toString(), params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesListByCompanyIdAndBranchId(Integer companyId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("companyId", companyId);
        List<EmployeeDTO> dTOList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Employee d WHERE d.companyId.id = :companyId AND  d.branchId.id = :branchId ");
        List<Employee> list = dao.findListByQuery(builder.toString(), params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public Boolean getAllEmployeesIsSuperVisor(Integer employeeId) {
        Map<String, Object> params = new HashMap();
        params.put("employeeId", employeeId);
        Integer count = ((Long) dao.findEntityByQuery("SELECT count(e) FROM Employee e WHERE e.parentId.id = :employeeId AND e.id != :employeeId", params)).intValue();
        if (count > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }

    }

    @Override
    public List<EmployeeDTO> getAllEmployeesListByBranchIdAndHrSalaryStructure(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<EmployeeDTO> dTOList = new ArrayList<>();
        List<Employee> list = dao.findListByQuery("SELECT e FROM Employee e WHERE  e.branchId.id=:branchId and e.id NOT IN (SELECT h.employeeId.id FROM HrSalaryStructure h )", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public EmployeeDTO findEmployeeId(Integer id) {
        Employee employee = dao.findEntityById(Employee.class, id);
        EmployeeDTO dTO = new EmployeeDTO();
        return mapTODTO(employee, dTO);
    }

    @Override
    public EmployeeDTO addEmployee(EmployeeDTO employee) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Employee> findEmployeeByCode(Integer companyId, Integer code) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("code", code);
        return dao.findListByQuery("SELECT e FROM Employee e WHERE e.companyId.id = :companyId And e.code = :code", params);
    }

    @Override
    public EmployeeDTO findEmployee(Integer departmentId) {
        Employee employee = dao.findEntityById(Employee.class, departmentId);
        EmployeeDTO employeeDTO = mapTODTO(employee, null);
        return employeeDTO;
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO employee) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteEmployee(EmployeeDTO employee, IsagUser isagUser) {
        //The delete method includes the 4 tables to be deleted based on the employee ID
        Employee entity = new Employee();
        entity.setId(employee.getId());
        empQualificationsService.deleteByEmployeeId(employee.getId());
        employeeFellowsService.deleteHrEmployeeFellowsByEmployeeId(employee.getId());
        emergencyContactService.deleteByEmployeeId(employee.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesListByCompanyId(Integer companyId) {

        Map<String, Object> params = new HashMap<>();
        List<EmployeeDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Employee> list = dao.findListByQuery("SELECT e FROM Employee e WHERE  e.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesListByCompanyId(Integer companyId, Integer branchId) {

        Map<String, Object> params = new HashMap<>();
        List<EmployeeDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        params.put("branchId", branchId);
        List<Employee> list = dao.findListByQuery("SELECT i.employeeId FROM IsagUser i WHERE  i.employeeId.companyId.id = :companyId AND i.branchId.id = :branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesListByParentANDUserLogin(Integer employeeId) {

        Map<String, Object> params = new HashMap<>();
        List<EmployeeDTO> dTOList = new ArrayList<>();
        params.put("employeeId", employeeId);
        List<Employee> list = dao.findListByQuery("SELECT e FROM Employee e WHERE e.parentId.id=:employeeId or e.id = :employeeId ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<Employee> getAllEmployeeListByCompanyId(Integer companyId) {

        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Employee> list = dao.findListByQuery("SELECT e FROM Employee e WHERE  e.companyId.id=:companyId", params);
        return list;
    }

    @Override
    public List<Integer> findEmployeeForMe(Integer employeeId) {
        Employee entity = dao.findEntityById(Employee.class, employeeId);
        List<Integer> empList = new ArrayList<>();
        return findEmployeeForMe(empList, entity);
    }

    public List<Integer> findEmployeeForMe(List<Integer> empList, Employee entity) {
        empList.add(entity.getId());
        if (entity.getChildEmployee() != null && !entity.getChildEmployee().isEmpty()) {
            for (Employee employee : entity.getChildEmployee()) {
                empList.add(employee.getId());
                if (employee.getChildEmployee() != null && !employee.getChildEmployee().isEmpty()) {
                    findEmployeeForMe(empList, employee);
                }
            }
        }
        return empList;
    }

    @Override
    public StringBuilder returnEmployeeList(Integer assignedTo, StringBuilder empIds) {
        List<Integer> empList = findEmployeeForMe(assignedTo);
        if (empList != null && !empList.isEmpty()) {
            for (Integer id : empList) {
                if (empIds.toString().isEmpty()) {
                    empIds.append(id);
                } else {
                    empIds.append(" , ");
                    empIds.append(id);
                }
            }
        }
        return empIds;
    }

    public EmployeeDTO mapTODTO(Employee entity, EmployeeDTO dTO) {
        if (dTO == null) {
            dTO = new EmployeeDTO();
        }
        initMapToDTO(dTO, entity);

        return dTO;
    }

    private void initMapToDTO(EmployeeDTO dTO, Employee entity) {

        if (entity.getParentId() != null && entity.getParentId().getId() != null) {
            EmployeeDTO parentDTO = new EmployeeDTO();
            parentDTO.setName(entity.getParentId().getName());
            parentDTO.setCode(entity.getParentId().getCode());
            parentDTO.setId(entity.getParentId().getId());
            dTO.setParentId(parentDTO);
        }
        if (entity.getMaritalStatus() != null && entity.getMaritalStatus().getId() != null) {
            SymbolDTO maritalStatusDTO = new SymbolDTO();
            maritalStatusDTO.setName(entity.getMaritalStatus().getName());
            maritalStatusDTO.setId(entity.getMaritalStatus().getId());
            dTO.setMaritalStatus(maritalStatusDTO);
        }
        if (entity.getSponsorId() != null && entity.getSponsorId().getId() != null) {
            HrSponsorDTO sponsorDTO = new HrSponsorDTO();
            sponsorDTO.setName(entity.getSponsorId().getName());
            sponsorDTO.setId(entity.getSponsorId().getId());
            dTO.setSponsorDTO(sponsorDTO);
        }
        if (entity.getDesignation() != null && entity.getDesignation().getId() != null) {
            SymbolDTO designationDTO = new SymbolDTO();
            designationDTO.setName(entity.getDesignation().getName());
            designationDTO.setId(entity.getDesignation().getId());
            dTO.setDesignation(designationDTO);
        }
        if (entity.getPlaceBirth() != null && entity.getPlaceBirth().getId() != null) {
            SymbolDTO placeBirthDTO = new SymbolDTO();
            placeBirthDTO.setName(entity.getPlaceBirth().getName());
            placeBirthDTO.setId(entity.getPlaceBirth().getId());
            dTO.setPlaceBirth(placeBirthDTO);
        }
        if (entity.getNationality() != null && entity.getNationality().getId() != null) {
            SymbolDTO nationalityDTO = new SymbolDTO();
            nationalityDTO.setName(entity.getNationality().getName());
            nationalityDTO.setId(entity.getNationality().getId());
            dTO.setNationality(nationalityDTO);
        }
        if (entity.getReligion() != null && entity.getReligion().getId() != null) {
            SymbolDTO religionDTO = new SymbolDTO();
            religionDTO.setName(entity.getReligion().getName());
            religionDTO.setId(entity.getReligion().getId());
            dTO.setReligion(religionDTO);
        }
        if (entity.getJobGrade() != null && entity.getJobGrade().getId() != null) {
            SymbolDTO jobGradeDTO = new SymbolDTO();
            jobGradeDTO.setName(entity.getJobGrade().getName());
            jobGradeDTO.setId(entity.getJobGrade().getId());
            dTO.setJobGrade(jobGradeDTO);
        }

        if (entity.getGender() != null && entity.getGender().getId() != null) {
            SymbolDTO genderDTO = new SymbolDTO();
            genderDTO.setName(entity.getGender().getName());
            genderDTO.setId(entity.getGender().getId());
            dTO.setGender(genderDTO);
        }
        if (entity.getDepartmentId() != null && entity.getDepartmentId().getId() != null) {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setName(entity.getDepartmentId().getName());
            departmentDTO.setId(entity.getDepartmentId().getId());
            dTO.setDepartmentId(departmentDTO);
        }

//        if (entity.getRemCompany() != null) {
//            RemCompanyDTO remCompanyDTO = new RemCompanyDTO();
//            remCompanyDTO.setId(entity.getRemCompany().getId());
//            remCompanyDTO.setName(entity.getRemCompany().getName());
//            remCompanyDTO.setCode(entity.getRemCompany().getCode());
//            dTO.setRemCompany(remCompanyDTO);
//        }
        dTO.setRemType(entity.getRemType());
        dTO.setUserName(entity.getUserName());
        dTO.setPassword(entity.getPassword());
        dTO.setHrEmergencyContactDTO(emergencyContactService.findbyEmployeeId(entity.getId()));
        dTO.setHrEmpQualificationsDTOList(empQualificationsService.findHrEmpQualificationsByEmployeeId(entity.getId()));
        dTO.setHrEmployeeFellowsDTOList(employeeFellowsService.findHrEmployeeFellowsByEmployeeId(entity.getId()));
        dTO.setId(entity.getId());
        dTO.setName(entity.getName());
        dTO.setNameEn(entity.getNameEn());
        dTO.setCode(entity.getCode());
        dTO.setEmail(entity.getEmail());
        dTO.setIdNumber(entity.getIdNumber());
        dTO.setMobile(entity.getMobile());
        dTO.setPhone(entity.getPhone());
        dTO.setPassportNumber(entity.getPassportNumber());
        dTO.setIdNumber(entity.getIdNumber());
        dTO.setSocialInsuranceId(entity.getSocialInsuranceId());
        dTO.setIdNumberReleaseDate(entity.getIdNumberReleaseDate());
        dTO.setIdNumberEndDate(entity.getIdNumberEndDate());
        dTO.setJoinDate(entity.getJoinDate());
        dTO.setPassportReleaseDate(entity.getPassportReleaseDate());
        dTO.setPassportEndDate(entity.getPassportEndDate());
        dTO.setContractStartDate(entity.getContractStartDate());
        dTO.setContractEndDate(entity.getContractEndDate());
        dTO.setDateBirth(entity.getDateBirth());
        dTO.setRetirementDate(entity.getRetirementDate());
        dTO.setStatus(entity.getStatus() != null ? entity.getStatus() : 0);
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setImgName(entity.getImgName());
        dTO.setCvName(entity.getCvName());
    }

    public Employee mapFromDTO(EmployeeDTO dTO, IsagUser isagUser) {
        Employee entity = new Employee();
        if (dTO.getParentId() != null && dTO.getParentId().getId() != null) {
            Employee parent = new Employee();
            parent.setId(dTO.getParentId().getId());
            entity.setParentId(parent);
        }
        if (dTO.getSponsorDTO() != null && dTO.getSponsorDTO().getId() != null) {
            HrSponsor sponsor = new HrSponsor();
            sponsor.setId(dTO.getSponsorDTO().getId());
            entity.setSponsorId(sponsor);
        }
        if (dTO.getMaritalStatus() != null && dTO.getMaritalStatus().getId() != null) {
            Symbol maritalStatus = new Symbol();
            maritalStatus.setId(dTO.getMaritalStatus().getId());
            entity.setMaritalStatus(maritalStatus);
        }

        if (dTO.getReligion() != null && dTO.getReligion().getId() != null) {
            Symbol religion = new Symbol();
            religion.setId(dTO.getReligion().getId());
            entity.setReligion(religion);
        }
        if (dTO.getPlaceBirth() != null && dTO.getPlaceBirth().getId() != null) {
            Symbol placeBirth = new Symbol();
            placeBirth.setId(dTO.getPlaceBirth().getId());
            entity.setPlaceBirth(placeBirth);
        }
        if (dTO.getJobGrade() != null && dTO.getJobGrade().getId() != null) {
            Symbol jobGrade = new Symbol();
            jobGrade.setId(dTO.getJobGrade().getId());
            entity.setJobGrade(jobGrade);
        }
        if (dTO.getNationality() != null && dTO.getNationality().getId() != null) {
            Symbol nationality = new Symbol();
            nationality.setId(dTO.getNationality().getId());
            entity.setNationality(nationality);
        }
        if (dTO.getGender() != null && dTO.getGender().getId() != null) {
            Symbol gender = new Symbol();
            gender.setId(dTO.getGender().getId());
            entity.setGender(gender);
        }
        if (dTO.getDesignation() != null && dTO.getDesignation().getId() != null) {
            Symbol designation = new Symbol();
            designation.setId(dTO.getDesignation().getId());
            entity.setDesignation(designation);
        }
        if (dTO.getDepartmentId() != null) {
            Department department = new Department();
            department.setId(dTO.getDepartmentId().getId());
            entity.setDepartmentId(department);
        }

        entity.setRemType(dTO.getRemType());
        entity.setUserName(dTO.getUserName());
        entity.setPassword(dTO.getPassword());
        entity.setId(dTO.getId());
        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());
        entity.setIdNumber(dTO.getIdNumber());
        entity.setPassportNumber(dTO.getPassportNumber());
        entity.setPassportReleaseDate(dTO.getPassportReleaseDate());
        entity.setPassportEndDate(dTO.getPassportEndDate());
        entity.setContractEndDate(dTO.getContractEndDate());
        entity.setEmail(dTO.getEmail());
        entity.setIdNumberReleaseDate(dTO.getIdNumberReleaseDate());
        entity.setIdNumberEndDate(dTO.getIdNumberEndDate());
        entity.setStatus(dTO.getStatus());
        entity.setNameEn(dTO.getNameEn());
        entity.setSocialInsuranceId(dTO.getSocialInsuranceId());
        entity.setSocialInsuranceId(dTO.getSocialInsuranceId());
        entity.setMobile(dTO.getMobile());
        entity.setPhone(dTO.getPhone());
        entity.setImgName(dTO.getImgName());
        entity.setCvName(dTO.getCvName());
        entity.setRetirementDate(dTO.getRetirementDate());
        entity.setContractStartDate(dTO.getContractStartDate());
        entity.setContractEndDate(dTO.getContractEndDate());
        entity.setJoinDate(dTO.getJoinDate());
        entity.setDateBirth(dTO.getDateBirth());

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

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<Employee> list, List<EmployeeDTO> dTOList) {
        for (Employee entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    @Override
    public void deleteIdfromFellowsQualifications(Integer employeeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
