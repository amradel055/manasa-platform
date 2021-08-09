/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.DepartmentService;
import com.isag.businessservice.EmployeeService;
import com.isag.businessservice.HrSalariesEffectService;
import com.isag.businessservice.HrSponsorService;
import com.isag.businessservice.RscAccrualDeductionService;
import com.isag.businessservice.SymbolService;
import com.isag.converter.DepartmentDTOConverter;
import com.isag.converter.EmployeeDTOConverter;
import com.isag.converter.RscAccuralDeductionConverter;
import com.isag.converter.SalaryEffectTypeDTOConverter;
import com.isag.converter.SponsorDTOConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.dto.DepartmentDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.HrEmpQualificationsDTO;
import com.isag.dto.HrEmployeeFellowsDTO;
import com.isag.dto.HrSalariesStructureEffectDTO;
import com.isag.dto.HrSalaryEffectDTO;
import com.isag.dto.HrSponsorDTO;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.dto.SymbolDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import com.isag.businessservice.HrSalaryEffectService;
import com.isag.businessservice.HrSalaryEffectTypeService;
import com.isag.businessservice.HrSalaryStructureAccuralDeductionEffectTypeService;
import com.isag.businessservice.HrSalaryStructureAccuralDeductionService;
import com.isag.converter.HrSalaryStructureAccuralDeductionEffectTypeConverter;
import com.isag.converter.hrSalaryEffectTypeConvertor;
import com.isag.dto.HrSalaryEffectTypeDTO;
import com.isag.dto.HrSalaryStructureAccuralDeductionEffectTypeDTO;

/**
 *
 * @author Ahmed Shaaban
 */
public class HRBasicDataForm extends BaseFormBean {

    private List<RscAccrualDeductionDTO> accrualDTOList;
    private List<RscAccrualDeductionDTO> deductionDTOList;
    private List<HrEmployeeFellowsDTO> hrEmployeeFellowsDTOList;
    private List<HrEmpQualificationsDTO> hrEmpQualificationsDTOList;
    private List<HrSalariesStructureEffectDTO> salariesEffectDTOList;
    private List<HrSalaryEffectDTO> salaryEffectTypeDTOList;
    private List<DepartmentDTO> departmentDTOList;
    private List<SymbolDTO> empQualificationsDTOList;
    private List<EmployeeDTO> employeeDTOList;
    private List<EmployeeDTO>hremployeeDTOList;
    private List<SymbolDTO> maritalStatusList;
    private List<SymbolDTO> salaryUnitTypeList;
    private List<SymbolDTO> jobGradList;
    private List<SymbolDTO> placeBirth;
    private List<SymbolDTO> nationalityList;
    private List<SymbolDTO> designationList;
    private List<SymbolDTO> genderList;
    private List<SymbolDTO> religionList;
    private List<SymbolDTO> educationLevelList;
    private List<SymbolDTO> relationList;
    private List<SymbolDTO> salaryEffectTypeList;
    private List<HrSponsorDTO> sponsorDTOList;
    private List<HrSalaryStructureAccuralDeductionEffectTypeDTO> accuralDeductionEffectTypeDTOList;
    private List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOAccuralList;
    private List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTODeductionList;
    private SponsorDTOConverter sponsorDTOConverter;
    private SymbolDTO symbolDTO;
    private DepartmentDTOConverter departmentDTOConverter;
    private EmployeeDTOConverter employeeDTOConverter;
    private SymbolDTOConverter symbolDTOConverter;
    private SymbolDTOConverter jobGradDTOConverter;
    private SymbolDTOConverter maritalStatusDTOConverter;
    private SymbolDTOConverter nationalityDTOConverter;
    private SymbolDTOConverter designationDTOConverter;
    private SymbolDTOConverter genderDTOConverter;
    private SymbolDTOConverter religionDTOConverter;
    private SymbolDTOConverter relationDTOConverter;
    private SymbolDTOConverter educationLevelDTOConverter;
    private SymbolDTOConverter placeBirthDTOConverter;

    private SymbolDTOConverter empQualificationsConverter;
    private SymbolDTOConverter salaryEffectTypeConverter;
    private SymbolDTOConverter salaryUnitTypeConverter;
    private RscAccuralDeductionConverter acuuralConverter;
    private RscAccuralDeductionConverter deductionConverter;
    private SalaryEffectTypeDTOConverter salaryEffectTypeDTOConverter;
    private HrSalaryStructureAccuralDeductionEffectTypeConverter hrSalaryStructureAccuralDeductionEffectTypeConverter;
    private hrSalaryEffectTypeConvertor salaryEffectTypeAccuralConvertor;
    private hrSalaryEffectTypeConvertor salaryEffectTypeDeductionConvertor;
    private EmployeeDTOConverter hremployeeDTOConverter;

    @EJB
    private SymbolService symbolService;
    @EJB
    private DepartmentService departmentService;
    @EJB
    private HrSponsorService hrSponsorService;

    @EJB
    private EmployeeService employeeService;
    @EJB
    HrSalaryStructureAccuralDeductionService accuralDeductionService;
    @EJB
    RscAccrualDeductionService accrualDeductionService;
    @EJB
    HrSalariesEffectService salariesEffectService;
    @EJB
    HrSalaryEffectService salaryEffectTypeService;
    @EJB
    HrSalaryStructureAccuralDeductionEffectTypeService hrSalaryStructureAccuralDeductionEffectTypeService;
    @EJB
    HrSalaryEffectTypeService hrSalaryEffectTypeService;

    public List<DepartmentDTO> completeDepartment(String query) {
        List<DepartmentDTO> Department = getDepartmentDTOList();
        if (query == null || query.trim().equals("")) {
            departmentDTOConverter = new DepartmentDTOConverter(Department);
            return Department;
        }
        List<DepartmentDTO> filteredDepartment = new ArrayList<>();

        String nameAr;
        Integer code;
        DepartmentDTO departmentSymbol;
        for (int i = 0; i < getDepartmentDTOList().size(); i++) {
            departmentSymbol = getDepartmentDTOList().get(i);
            nameAr = departmentSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredDepartment.contains(departmentSymbol)) {
                    filteredDepartment.add(departmentSymbol);
                }
            }

            code = departmentSymbol.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredDepartment.contains(departmentSymbol)) {
                    filteredDepartment.add(departmentSymbol);
                }
            }
        }

        departmentDTOConverter = new DepartmentDTOConverter(filteredDepartment);
        return filteredDepartment;
    }

    public List<HrSponsorDTO> completeSponsor(String query) {
        List<HrSponsorDTO> Sponsor = getSponsorDTOList();
        if (query == null || query.trim().equals("")) {
            sponsorDTOConverter = new SponsorDTOConverter(Sponsor);
            return Sponsor;
        }
        List<HrSponsorDTO> filteredSponsor = new ArrayList<>();

        String nameAr;
        Integer code;
        HrSponsorDTO sponsorSymbol;
        for (int i = 0; i < getSponsorDTOList().size(); i++) {
            sponsorSymbol = getSponsorDTOList().get(i);
            nameAr = sponsorSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredSponsor.contains(sponsorSymbol)) {
                    filteredSponsor.add(sponsorSymbol);
                }
            }

            code = sponsorSymbol.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredSponsor.contains(sponsorSymbol)) {
                    filteredSponsor.add(sponsorSymbol);
                }
            }
        }

        sponsorDTOConverter = new SponsorDTOConverter(filteredSponsor);
        return filteredSponsor;
    }

    public List<EmployeeDTO> completeEmployee(String query) {
        List<EmployeeDTO> Employee = getEmployeeDTOList();
        if (query == null || query.trim().equals("")) {
            employeeDTOConverter = new EmployeeDTOConverter(Employee);
            return Employee;
        }
        List<EmployeeDTO> filteredEmployee = new ArrayList<>();

        String nameAr;
        Integer code;
        EmployeeDTO employeeSymbol;
        for (int i = 0; i < getEmployeeDTOList().size(); i++) {
            employeeSymbol = getEmployeeDTOList().get(i);
            nameAr = employeeSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredEmployee.contains(employeeSymbol)) {
                    filteredEmployee.add(employeeSymbol);
                }
            }

            code = employeeSymbol.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredEmployee.contains(employeeSymbol)) {
                    filteredEmployee.add(employeeSymbol);
                }
            }
        }

        employeeDTOConverter = new EmployeeDTOConverter(filteredEmployee);
        return filteredEmployee;
    }

     public List<EmployeeDTO> completeSalaryStructureEmployee(String query) {
        List<EmployeeDTO> Employee = getHremployeeDTOList();
        if (query == null || query.trim().equals("")) {
            hremployeeDTOConverter = new EmployeeDTOConverter(Employee);
            return Employee;
        }
        List<EmployeeDTO> filteredEmployee = new ArrayList<>();

        String nameAr;
        Integer code;
        EmployeeDTO employeeSymbol;
        for (int i = 0; i < getEmployeeDTOList().size(); i++) {
            employeeSymbol = getEmployeeDTOList().get(i);
            nameAr = employeeSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredEmployee.contains(employeeSymbol)) {
                    filteredEmployee.add(employeeSymbol);
                }
            }

            code = employeeSymbol.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredEmployee.contains(employeeSymbol)) {
                    filteredEmployee.add(employeeSymbol);
                }
            }
        }

        hremployeeDTOConverter = new EmployeeDTOConverter(filteredEmployee);
        return filteredEmployee;
    }
    
    public List<SymbolDTO> completeMaritalStatus(String query) {
        List<SymbolDTO> MaritalStatus = getMaritalStatusList();
        if (query == null || query.trim().equals("")) {
            setMaritalStatusDTOConverter(new SymbolDTOConverter(MaritalStatus));
            return MaritalStatus;
        }
        List<SymbolDTO> filteredMaritalStatus = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO maritalStatusSymbol;
        for (int i = 0; i < getMaritalStatusList().size(); i++) {
            maritalStatusSymbol = getMaritalStatusList().get(i);
            nameAr = maritalStatusSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredMaritalStatus.contains(maritalStatusSymbol)) {
                    filteredMaritalStatus.add(maritalStatusSymbol);
                }
            }

            code = maritalStatusSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredMaritalStatus.contains(maritalStatusSymbol)) {
                    filteredMaritalStatus.add(maritalStatusSymbol);
                }
            }
        }

        setMaritalStatusDTOConverter(new SymbolDTOConverter(filteredMaritalStatus));
        return filteredMaritalStatus;
    }

    public List<SymbolDTO> completeEmpQualifications(String query) {
        List<SymbolDTO> EmpQualifications = getEmpQualificationsDTOList();
        if (query == null || query.trim().equals("")) {
            setEmpQualificationsConverter(new SymbolDTOConverter(EmpQualifications));
            return EmpQualifications;
        }
        List<SymbolDTO> filteredEmpQualifications = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO empQualificationsSymbol;
        for (int i = 0; i < getEmpQualificationsDTOList().size(); i++) {
            empQualificationsSymbol = getEmpQualificationsDTOList().get(i);
            nameAr = empQualificationsSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredEmpQualifications.contains(empQualificationsSymbol)) {
                    filteredEmpQualifications.add(empQualificationsSymbol);
                }
            }

            code = empQualificationsSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredEmpQualifications.contains(empQualificationsSymbol)) {
                    filteredEmpQualifications.add(empQualificationsSymbol);
                }
            }
        }

        setEmpQualificationsConverter(new SymbolDTOConverter(filteredEmpQualifications));
        return filteredEmpQualifications;
    }

    public List<SymbolDTO> completePlaceBirth(String query) {
        List<SymbolDTO> PlaceBirth = getPlaceBirth();
        if (query == null || query.trim().equals("")) {
            setPlaceBirthDTOConverter(new SymbolDTOConverter(PlaceBirth));
            return PlaceBirth;
        }
        List<SymbolDTO> filteredplaceBirth = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO placeBirthSymbol;
        for (int i = 0; i < getPlaceBirth().size(); i++) {
            placeBirthSymbol = getPlaceBirth().get(i);
            nameAr = placeBirthSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredplaceBirth.contains(placeBirthSymbol)) {
                    filteredplaceBirth.add(placeBirthSymbol);
                }
            }

            code = placeBirthSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredplaceBirth.contains(placeBirthSymbol)) {
                    filteredplaceBirth.add(placeBirthSymbol);
                }
            }
        }

        setPlaceBirthDTOConverter(new SymbolDTOConverter(filteredplaceBirth));
        return filteredplaceBirth;
    }

    public List<SymbolDTO> completeJobGrade(String query) {
        List<SymbolDTO> JobGrade = getJobGradList();
        if (query == null || query.trim().equals("")) {
            setJobGradDTOConverter(new SymbolDTOConverter(JobGrade));
            return JobGrade;
        }
        List<SymbolDTO> filteredJobGrade = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO jobGradListSymbol;
        for (int i = 0; i < getJobGradList().size(); i++) {
            jobGradListSymbol = getJobGradList().get(i);
            nameAr = jobGradListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredJobGrade.contains(jobGradListSymbol)) {
                    filteredJobGrade.add(jobGradListSymbol);
                }
            }

            code = jobGradListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredJobGrade.contains(jobGradListSymbol)) {
                    filteredJobGrade.add(jobGradListSymbol);
                }
            }
        }

        setJobGradDTOConverter(new SymbolDTOConverter(filteredJobGrade));
        return filteredJobGrade;
    }

    public List<SymbolDTO> completeNationality(String query) {
        List<SymbolDTO> Nationality = getNationalityList();
        if (query == null || query.trim().equals("")) {
            setNationalityDTOConverter(new SymbolDTOConverter(Nationality));
            return Nationality;
        }
        List<SymbolDTO> filteredNationality = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO nationalityListSymbol;
        for (int i = 0; i < getNationalityList().size(); i++) {
            nationalityListSymbol = getNationalityList().get(i);
            nameAr = nationalityListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredNationality.contains(nationalityListSymbol)) {
                    filteredNationality.add(nationalityListSymbol);
                }
            }

            code = nationalityListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredNationality.contains(nationalityListSymbol)) {
                    filteredNationality.add(nationalityListSymbol);
                }
            }
        }

        setNationalityDTOConverter(new SymbolDTOConverter(filteredNationality));
        return filteredNationality;
    }

    public List<SymbolDTO> completeDesignation(String query) {
        List<SymbolDTO> Designation = getDeisgnationList();
        if (query == null || query.trim().equals("")) {
            setDesignationDTOConverter(new SymbolDTOConverter(Designation));
            return Designation;
        }
        List<SymbolDTO> filteredDesignation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO designationListSymbol;
        for (int i = 0; i < getDeisgnationList().size(); i++) {
            designationListSymbol = getDeisgnationList().get(i);
            nameAr = designationListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredDesignation.contains(designationListSymbol)) {
                    filteredDesignation.add(designationListSymbol);
                }
            }

            code = designationListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredDesignation.contains(designationListSymbol)) {
                    filteredDesignation.add(designationListSymbol);
                }
            }
        }

        setDesignationDTOConverter(new SymbolDTOConverter(filteredDesignation));
        return filteredDesignation;
    }

    public List<SymbolDTO> completeGender(String query) {
        List<SymbolDTO> Gender = getGenderList();
        if (query == null || query.trim().equals("")) {
            setGenderDTOConverter(new SymbolDTOConverter(Gender));
            return Gender;
        }
        List<SymbolDTO> filteredGender = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO genderListSymbol;
        for (int i = 0; i < getGenderList().size(); i++) {
            genderListSymbol = getGenderList().get(i);
            nameAr = genderListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGender.contains(genderListSymbol)) {
                    filteredGender.add(genderListSymbol);
                }
            }

            code = genderListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredGender.contains(genderListSymbol)) {
                    filteredGender.add(genderListSymbol);
                }
            }
        }

        setGenderDTOConverter(new SymbolDTOConverter(filteredGender));
        return filteredGender;
    }

    public List<SymbolDTO> completeReligion(String query) {
        List<SymbolDTO> Religion = getReligionList();
        if (query == null || query.trim().equals("")) {
            setReligionDTOConverter(new SymbolDTOConverter(Religion));
            return Religion;
        }
        List<SymbolDTO> filteredReligion = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO religionListSymbol;
        for (int i = 0; i < getReligionList().size(); i++) {
            religionListSymbol = getReligionList().get(i);
            nameAr = religionListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredReligion.contains(religionListSymbol)) {
                    filteredReligion.add(religionListSymbol);
                }
            }

            code = religionListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredReligion.contains(religionListSymbol)) {
                    filteredReligion.add(religionListSymbol);
                }
            }
        }

        setReligionDTOConverter(new SymbolDTOConverter(filteredReligion));
        return filteredReligion;
    }

    public List<SymbolDTO> completeEducationLevel(String query) {
        List<SymbolDTO> EducationLevel = getEducationLevelList();
        if (query == null || query.trim().equals("")) {
            setEducationLevelDTOConverter(new SymbolDTOConverter(EducationLevel));
            return EducationLevel;
        }
        List<SymbolDTO> filteredEducationLevel = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO educationLevelListSymbol;
        for (int i = 0; i < getEducationLevelList().size(); i++) {
            educationLevelListSymbol = getEducationLevelList().get(i);
            nameAr = educationLevelListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredEducationLevel.contains(educationLevelListSymbol)) {
                    filteredEducationLevel.add(educationLevelListSymbol);
                }
            }

            code = educationLevelListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredEducationLevel.contains(educationLevelListSymbol)) {
                    filteredEducationLevel.add(educationLevelListSymbol);
                }
            }
        }

        setEducationLevelDTOConverter(new SymbolDTOConverter(filteredEducationLevel));
        return filteredEducationLevel;
    }

    public List<SymbolDTO> completeRelation(String query) {
        List<SymbolDTO> Relation = getRelationList();
        if (query == null || query.trim().equals("")) {
            setRelationDTOConverter(new SymbolDTOConverter(Relation));
            return Relation;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < getRelationList().size(); i++) {
            relationListSymbol = getRelationList().get(i);
            nameAr = relationListSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }

            code = relationListSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredRelation.contains(relationListSymbol)) {
                    filteredRelation.add(relationListSymbol);
                }
            }
        }

        setRelationDTOConverter(new SymbolDTOConverter(filteredRelation));
        return filteredRelation;
    }

    public List<SymbolDTO> completeSalaryUnitType(String query) {
        List<SymbolDTO> SalaryUnitType = getSalaryUnitTypeList();
        if (query == null || query.trim().equals("")) {
            setSalaryUnitTypeConverter(new SymbolDTOConverter(SalaryUnitType));
            return SalaryUnitType;
        }
        List<SymbolDTO> filteredSalaryUnitType = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO salaryUnitTypeSymbol;
        for (int i = 0; i < getSalaryUnitTypeList().size(); i++) {
            salaryUnitTypeSymbol = getSalaryUnitTypeList().get(i);
            nameAr = salaryUnitTypeSymbol.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredSalaryUnitType.contains(salaryUnitTypeSymbol)) {
                    filteredSalaryUnitType.add(salaryUnitTypeSymbol);
                }
            }

            code = salaryUnitTypeSymbol.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredSalaryUnitType.contains(salaryUnitTypeSymbol)) {
                    filteredSalaryUnitType.add(salaryUnitTypeSymbol);
                }
            }
        }

        setRelationDTOConverter(new SymbolDTOConverter(filteredSalaryUnitType));
        return filteredSalaryUnitType;
    }

    
      public List<HrSalaryEffectTypeDTO> completeHrSalaryEffectTypeAccuralDTO(String query) {
        List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOList = getHrSalaryEffectTypeDTOAccuralList();
        if (query == null || query.trim().equals("")) {
            salaryEffectTypeAccuralConvertor = new hrSalaryEffectTypeConvertor(hrSalaryEffectTypeDTOList);
            return hrSalaryEffectTypeDTOList;
        }
        List<HrSalaryEffectTypeDTO> filteredhrSalaryEffectType = new ArrayList<>();

        String nameAr;
        Integer code;
        HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO;
        for (int i = 0; i < getHrSalaryEffectTypeDTOAccuralList().size(); i++) {
            hrSalaryEffectTypeDTO = getHrSalaryEffectTypeDTOAccuralList().get(i);
            nameAr = hrSalaryEffectTypeDTO.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredhrSalaryEffectType.contains(hrSalaryEffectTypeDTO)) {
                    filteredhrSalaryEffectType.add(hrSalaryEffectTypeDTO);
                }
            }

            code = hrSalaryEffectTypeDTO.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredhrSalaryEffectType.contains(hrSalaryEffectTypeDTO)) {
                    filteredhrSalaryEffectType.add(hrSalaryEffectTypeDTO);
                }
            }
        }

        salaryEffectTypeAccuralConvertor = new hrSalaryEffectTypeConvertor(filteredhrSalaryEffectType);
        return filteredhrSalaryEffectType;
    }
    
    
       public List<HrSalaryEffectTypeDTO> completeHrSalaryEffectTypeDeductionDTO(String query) {
        List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOList = getHrSalaryEffectTypeDTODeductionList();
        if (query == null || query.trim().equals("")) {
            salaryEffectTypeDeductionConvertor = new hrSalaryEffectTypeConvertor(hrSalaryEffectTypeDTOList);
            return hrSalaryEffectTypeDTOList;
        }
        List<HrSalaryEffectTypeDTO> filteredhrSalaryEffectType = new ArrayList<>();

        String nameAr;
        Integer code;
        HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO;
        for (int i = 0; i < getHrSalaryEffectTypeDTODeductionList().size(); i++) {
            hrSalaryEffectTypeDTO = getHrSalaryEffectTypeDTODeductionList().get(i);
            nameAr = hrSalaryEffectTypeDTO.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredhrSalaryEffectType.contains(hrSalaryEffectTypeDTO)) {
                    filteredhrSalaryEffectType.add(hrSalaryEffectTypeDTO);
                }
            }

            code = hrSalaryEffectTypeDTO.getSerial();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredhrSalaryEffectType.contains(hrSalaryEffectTypeDTO)) {
                    filteredhrSalaryEffectType.add(hrSalaryEffectTypeDTO);
                }
            }
        }

        salaryEffectTypeDeductionConvertor = new hrSalaryEffectTypeConvertor(filteredhrSalaryEffectType);
        return filteredhrSalaryEffectType;
    }
     
    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init() {
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
     * @return the departmentDTOList
     */
    public List<DepartmentDTO> getDepartmentDTOList() {
        if (departmentDTOList == null || departmentDTOList.isEmpty()) {
            departmentDTOList = departmentService.getDepartmentsListByCompanyId(getUserData().getCompany().getId());
            departmentDTOConverter = new DepartmentDTOConverter(departmentDTOList);
        }
        return departmentDTOList;
    }

    /**
     * @param departmentDTOList the departmentDTOList to set
     */
    public void setDepartmentDTOList(List<DepartmentDTO> departmentDTOList) {
        this.departmentDTOList = departmentDTOList;
    }

    /**
     * @return the maritalStatusList
     */
    public List<SymbolDTO> getMaritalStatusList() {
        if (maritalStatusList == null || maritalStatusList.isEmpty()) {
            maritalStatusList = symbolService.getMaritalStatusCompanyId(getUserData().getCompany().getId());
            setMaritalStatusDTOConverter(new SymbolDTOConverter(maritalStatusList));

        }
        return maritalStatusList;
    }

    /**
     * @param maritalStatusList the maritalStatusList to set
     */
    public void setMaritalStatusList(List<SymbolDTO> maritalStatusList) {
        this.maritalStatusList = maritalStatusList;
    }

    /**
     * @return the jobGradList
     */
    public List<SymbolDTO> getJobGradList() {
        if (jobGradList == null || jobGradList.isEmpty()) {
            jobGradList = symbolService.getJobGradeByCompanyId(getUserData().getCompany().getId());
            setJobGradDTOConverter(new SymbolDTOConverter(jobGradList));
        }
        return jobGradList;
    }

    /**
     * @param jobGradList the jobGradList to set
     */
    public void setJobGradList(List<SymbolDTO> jobGradList) {
        this.jobGradList = jobGradList;
    }

    /**
     * @return the nationalityList
     */
    public List<SymbolDTO> getNationalityList() {
        if (nationalityList == null || nationalityList.isEmpty()) {
            nationalityList = symbolService.getNationalityByCompanyId(getUserData().getCompany().getId());
            setNationalityDTOConverter(new SymbolDTOConverter(nationalityList));

        }
        return nationalityList;
    }

    /**
     * @param nationalityList the nationalityList to set
     */
    public void setNationalityList(List<SymbolDTO> nationalityList) {
        this.nationalityList = nationalityList;
    }

    /**
     * @return the deisgnationList
     */
    public List<SymbolDTO> getDeisgnationList() {
        if (designationList == null || designationList.isEmpty()) {
            designationList = symbolService.getDesignationByCompanyId(getUserData().getCompany().getId());
            setDesignationDTOConverter(new SymbolDTOConverter(designationList));

        }
        return designationList;
    }

    /**
     * @param deisgnationList the deisgnationList to set
     */
    public void setDesignationList(List<SymbolDTO> designationList) {
        this.designationList = designationList;
    }

    /**
     * @return the genderList
     */
    public List<SymbolDTO> getGenderList() {
        if (genderList == null || genderList.isEmpty()) {
            genderList = symbolService.getGenderByCompanyId(getUserData().getCompany().getId());
            setGenderDTOConverter(new SymbolDTOConverter(genderList));
        }
        return genderList;
    }

    /**
     * @param genderList the genderList to set
     */
    public void setGenderList(List<SymbolDTO> genderList) {
        this.genderList = genderList;
    }

    /**
     * @return the religionList
     */
    public List<SymbolDTO> getReligionList() {
        if (religionList == null || religionList.isEmpty()) {
            religionList = symbolService.getReligionsCompanyId(getUserData().getCompany().getId());
            setReligionDTOConverter(new SymbolDTOConverter(religionList));
        }
        return religionList;
    }

    /**
     * @param religionList the religionList to set
     */
    public void setReligionList(List<SymbolDTO> religionList) {
        this.religionList = religionList;
    }

    /**
     * @return the relationList
     */
    public List<SymbolDTO> getRelationList() {
        if (relationList == null || relationList.isEmpty()) {
            relationList = symbolService.getRelationByCompanyId(getUserData().getCompany().getId());
            setRelationDTOConverter(new SymbolDTOConverter(relationList));
        }
        return relationList;
    }

    /**
     * @param relationList the relationList to set
     */
    public void setRelationList(List<SymbolDTO> relationList) {
        this.relationList = relationList;
    }

    /**
     * @return the educationLevelList
     */
    public List<SymbolDTO> getEducationLevelList() {
        if (educationLevelList == null || educationLevelList.isEmpty()) {
            educationLevelList = symbolService.getEducationLevelByCompanyId(getUserData().getCompany().getId());

            setEducationLevelDTOConverter(new SymbolDTOConverter(educationLevelList));
        }

        return educationLevelList;
    }

    /**
     * @param educationLevelList the educationLevelList to set
     */
    public void setEducationLevelList(List<SymbolDTO> educationLevelList) {
        this.educationLevelList = educationLevelList;
    }

    /**
     * @return the symbolDTO
     */
    public SymbolDTO getSymbolDTO() {
        if (symbolDTO == null) {
            symbolDTO = new SymbolDTO();

        }
        return symbolDTO;
    }

    /**
     * @param symbolDTO the symbolDTO to set
     */
    public void setSymbolDTO(SymbolDTO symbolDTO) {
        this.symbolDTO = symbolDTO;
    }

    /**
     * @return the departmentDTOConverter
     */
    public DepartmentDTOConverter getDepartmentDTOConverter() {

        return departmentDTOConverter;
    }

    /**
     * @param departmentDTOConverter the departmentDTOConverter to set
     */
    public void setDepartmentDTOConverter(DepartmentDTOConverter departmentDTOConverter) {
        this.departmentDTOConverter = departmentDTOConverter;
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
     * @return the employeeDTOConverter
     */
    public EmployeeDTOConverter getHremployeeDTOConverter() {
        return hremployeeDTOConverter;
    }

    /**
     * @param hremployeeDTOConverter the employeeDTOConverter to set
     */
    public void setHremployeeDTOConverter(EmployeeDTOConverter hremployeeDTOConverter) {
        this.hremployeeDTOConverter = hremployeeDTOConverter;
    }
    

    /**
     * @return the symbolDTOConverter
     */
    public SymbolDTOConverter getSymbolDTOConverter() {
        return symbolDTOConverter;
    }

    /**
     * @param symbolDTOConverter the symbolDTOConverter to set
     */
    public void setSymbolDTOConverter(SymbolDTOConverter symbolDTOConverter) {
        this.symbolDTOConverter = symbolDTOConverter;
    }

    /**
     * @return the employeeDTOList
     */
    public List<EmployeeDTO> getEmployeeDTOList() {

        if (employeeDTOList == null || employeeDTOList.isEmpty()) {
            employeeDTOList = employeeService.getAllEmployeesListByCompanyId(getUserData().getCompany().getId());

            setEmployeeDTOConverter(new EmployeeDTOConverter(employeeDTOList));
        }

        return employeeDTOList;
    }

    /**
     * @param employeeDTOList the employeeDTOList to set
     */
    public void setEmployeeDTOList(List<EmployeeDTO> employeeDTOList) {
        this.employeeDTOList = employeeDTOList;
    }
    
      /**
     * @return the employeeDTOList
     */
    public List<EmployeeDTO> getHremployeeDTOList() {

        if (hremployeeDTOList == null || hremployeeDTOList.isEmpty()) {
            hremployeeDTOList = employeeService.getAllEmployeesListByBranchIdAndHrSalaryStructure(getUserData().getUserBranch().getId());

            setHremployeeDTOConverter(new EmployeeDTOConverter(hremployeeDTOList));
        }

        return hremployeeDTOList;
    }

    /**
     * @param hremployeeDTOList the hremployeeDTOList to set
     */
    public void setHrEmployeeDTOList(List<EmployeeDTO> hremployeeDTOList) {
        this.hremployeeDTOList = hremployeeDTOList;
    }

    /**
     * @return the jobGradDTOConverter
     */
    public SymbolDTOConverter getJobGradDTOConverter() {
        return jobGradDTOConverter;
    }

    /**
     * @param jobGradDTOConverter the jobGradDTOConverter to set
     */
    public void setJobGradDTOConverter(SymbolDTOConverter jobGradDTOConverter) {
        this.jobGradDTOConverter = jobGradDTOConverter;
    }

    /**
     * @return the maritalStatusDTOConverter
     */
    public SymbolDTOConverter getMaritalStatusDTOConverter() {
        return maritalStatusDTOConverter;
    }

    /**
     * @param maritalStatusDTOConverter the maritalStatusDTOConverter to set
     */
    public void setMaritalStatusDTOConverter(SymbolDTOConverter maritalStatusDTOConverter) {
        this.maritalStatusDTOConverter = maritalStatusDTOConverter;
    }

    /**
     * @return the nationalityDTOConverter
     */
    public SymbolDTOConverter getNationalityDTOConverter() {
        return nationalityDTOConverter;
    }

    /**
     * @param nationalityDTOConverter the nationalityDTOConverter to set
     */
    public void setNationalityDTOConverter(SymbolDTOConverter nationalityDTOConverter) {
        this.nationalityDTOConverter = nationalityDTOConverter;
    }

    /**
     * @return the deisgnationDTOConverter
     */
    public SymbolDTOConverter getDesignationDTOConverter() {
        return designationDTOConverter;
    }

    /**
     * @param deisgnationDTOConverter the deisgnationDTOConverter to set
     */
    public void setDesignationDTOConverter(SymbolDTOConverter designationDTOConverter) {
        this.designationDTOConverter = designationDTOConverter;
    }

    /**
     * @return the genderDTOConverter
     */
    public SymbolDTOConverter getGenderDTOConverter() {
        return genderDTOConverter;
    }

    /**
     * @param genderDTOConverter the genderDTOConverter to set
     */
    public void setGenderDTOConverter(SymbolDTOConverter genderDTOConverter) {
        this.genderDTOConverter = genderDTOConverter;
    }

    /**
     * @return the religionDTOConverter
     */
    public SymbolDTOConverter getReligionDTOConverter() {
        return religionDTOConverter;
    }

    /**
     * @param religionDTOConverter the religionDTOConverter to set
     */
    public void setReligionDTOConverter(SymbolDTOConverter religionDTOConverter) {
        this.religionDTOConverter = religionDTOConverter;
    }

    /**
     * @return the relationDTOConverter
     */
    public SymbolDTOConverter getRelationDTOConverter() {
        return relationDTOConverter;
    }

    /**
     * @param relationDTOConverter the relationDTOConverter to set
     */
    public void setRelationDTOConverter(SymbolDTOConverter relationDTOConverter) {
        this.relationDTOConverter = relationDTOConverter;
    }

    /**
     * @return the educationLevelDTOConverter
     */
    public SymbolDTOConverter getEducationLevelDTOConverter() {
        return educationLevelDTOConverter;
    }

    /**
     * @param educationLevelDTOConverter the educationLevelDTOConverter to set
     */
    public void setEducationLevelDTOConverter(SymbolDTOConverter educationLevelDTOConverter) {
        this.educationLevelDTOConverter = educationLevelDTOConverter;
    }

    /**
     * @return the placeBirth
     */
    public List<SymbolDTO> getPlaceBirth() {
        if (placeBirth == null || placeBirth.isEmpty()) {
            placeBirth = symbolService.getPlaceBirthByCompanyId(getUserData().getCompany().getId());

            setEducationLevelDTOConverter(new SymbolDTOConverter(placeBirth));
            placeBirth = new ArrayList<>(placeBirth);
        }

        return placeBirth;
    }

    /**
     * @param placeBirth the placeBirth to set
     */
    public void setPlaceBirth(List<SymbolDTO> placeBirth) {
        this.placeBirth = placeBirth;
    }

    /**
     * @return the placeBirthDTOConverter
     */
    public SymbolDTOConverter getPlaceBirthDTOConverter() {
        return placeBirthDTOConverter;
    }

    /**
     * @param placeBirthDTOConverter the placeBirthDTOConverter to set
     */
    public void setPlaceBirthDTOConverter(SymbolDTOConverter placeBirthDTOConverter) {
        this.placeBirthDTOConverter = placeBirthDTOConverter;
    }

    /**
     * @return the empQualificationsConverter
     */
    public SymbolDTOConverter getEmpQualificationsConverter() {
        return empQualificationsConverter;
    }

    /**
     * @param empQualificationsConverter the empQualificationsConverter to set
     */
    public void setEmpQualificationsConverter(SymbolDTOConverter empQualificationsConverter) {
        this.empQualificationsConverter = empQualificationsConverter;
    }

    /**
     * @return the empQualificationsDTOList
     */
    public List<SymbolDTO> getEmpQualificationsDTOList() {
        return empQualificationsDTOList;
    }

    /**
     * @param empQualificationsDTOList the empQualificationsDTOList to set
     */
    public void setEmpQualificationsDTOList(List<SymbolDTO> empQualificationsDTOList) {
        this.empQualificationsDTOList = empQualificationsDTOList;
    }

    /**
     * @return the sponsorDTOConverter
     */
    public SponsorDTOConverter getSponsorDTOConverter() {
        return sponsorDTOConverter;
    }

    /**
     * @param sponsorDTOConverter the sponsorDTOConverter to set
     */
    public void setSponsorDTOConverter(SponsorDTOConverter sponsorDTOConverter) {
        this.sponsorDTOConverter = sponsorDTOConverter;
    }

    /**
     * @return the sponsorDTOList
     */
    public List<HrSponsorDTO> getSponsorDTOList() {
        if (sponsorDTOList == null || sponsorDTOList.isEmpty()) {
            sponsorDTOList = hrSponsorService.getSponsorsByCompanyId(getUserData().getCompany().getId());
            setSponsorDTOConverter(new SponsorDTOConverter(sponsorDTOList));

        }
        return sponsorDTOList;
    }

    /**
     * @param sponsorDTOList the sponsorDTOList to set
     */
    public void setSponsorDTOList(List<HrSponsorDTO> sponsorDTOList) {
        this.sponsorDTOList = sponsorDTOList;
    }

    /**
     * @return the hrEmployeeFellowsDTOList
     */
    public List<HrEmployeeFellowsDTO> getHrEmployeeFellowsDTOList() {
        if (hrEmployeeFellowsDTOList == null || hrEmployeeFellowsDTOList.isEmpty()) {
            hrEmployeeFellowsDTOList = new ArrayList<>();
        }
        return hrEmployeeFellowsDTOList;
    }

    /**
     * @param hrEmployeeFellowsDTOList the hrEmployeeFellowsDTOList to set
     */
    public void setHrEmployeeFellowsDTOList(List<HrEmployeeFellowsDTO> hrEmployeeFellowsDTOList) {
        this.hrEmployeeFellowsDTOList = hrEmployeeFellowsDTOList;
    }

    /**
     * @return the hrEmpQualificationsDTOList
     */
    public List<HrEmpQualificationsDTO> getHrEmpQualificationsDTOList() {
        if (hrEmpQualificationsDTOList == null || hrEmpQualificationsDTOList.isEmpty()) {
            hrEmpQualificationsDTOList = new ArrayList<>();

        }
        return hrEmpQualificationsDTOList;
    }

    /**
     * @param hrEmpQualificationsDTOList the hrEmpQualificationsDTOList to set
     */
    public void setHrEmpQualificationsDTOList(List<HrEmpQualificationsDTO> hrEmpQualificationsDTOList) {
        this.hrEmpQualificationsDTOList = hrEmpQualificationsDTOList;
    }

    /**
     * @return the employeeService
     */
    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    /**
     * @param employeeService the employeeService to set
     */
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * @return the accrualDTOList
     */
    public List<RscAccrualDeductionDTO> getAccrualDTOList() {
        if (accrualDTOList == null || accrualDTOList.isEmpty()) {
            accrualDTOList = accrualDeductionService.getRscAccuralDeductionByBranchAndType(getUserData().getUserBranch().getId(), 0);
            setAcuuralConverter(new RscAccuralDeductionConverter(accrualDTOList));

        }
        return accrualDTOList;
    }

    /**
     * @param accrualDTOList the accrualDTOList to set
     */
    public void setAccrualDTOList(List<RscAccrualDeductionDTO> accrualDTOList) {
        this.accrualDTOList = accrualDTOList;
    }

    /**
     * @return the deductionDTOList
     */
    public List<RscAccrualDeductionDTO> getDeductionDTOList() {
        if (deductionDTOList == null || deductionDTOList.isEmpty()) {
            deductionDTOList = accrualDeductionService.getRscAccuralDeductionByBranchAndType(getUserData().getUserBranch().getId(), 1);
            setDeductionConverter(new RscAccuralDeductionConverter(deductionDTOList));

        }
        return deductionDTOList;
    }

    /**
     * @param deductionDTOList the deductionDTOList to set
     */
    public void setDeductionDTOList(List<RscAccrualDeductionDTO> deductionDTOList) {
        this.deductionDTOList = deductionDTOList;
    }

    /**
     * @return the deductionConverter
     */
    public RscAccuralDeductionConverter getDeductionConverter() {
        return deductionConverter;
    }

    /**
     * @param deductionConverter the deductionConverter to set
     */
    public void setDeductionConverter(RscAccuralDeductionConverter deductionConverter) {
        this.deductionConverter = deductionConverter;
    }

    /**
     * @return the acuuralConverter
     */
    public RscAccuralDeductionConverter getAcuuralConverter() {
        return acuuralConverter;
    }

    /**
     * @param acuuralConverter the acuuralConverter to set
     */
    public void setAcuuralConverter(RscAccuralDeductionConverter acuuralConverter) {
        this.acuuralConverter = acuuralConverter;
    }

    /**
     * @return the salaryEffectTypeList
     */
    public List<SymbolDTO> getSalaryEffectTypeList(int impactIndicator) {

        if (impactIndicator == 0) {
            setSalaryEffectTypeList(symbolService.getPositiveImpactIndicatorByCompanyId(getUserData().getCompany().getId()));
            setSalaryEffectTypeConverter(new SymbolDTOConverter(salaryEffectTypeList));

        } else if (impactIndicator == 1) {

            setSalaryEffectTypeList(symbolService.getNegativeImpactIndicatorByCompanyId(getUserData().getCompany().getId()));
            setSalaryEffectTypeConverter(new SymbolDTOConverter(salaryEffectTypeList));
        }
        return salaryEffectTypeList;
    }

    /**
     * @param salaryEffectTypeList the salaryEffectTypeList to set
     */
    public void setSalaryEffectTypeList(List<SymbolDTO> salaryEffectTypeList) {
        this.salaryEffectTypeList = salaryEffectTypeList;
    }

    /**
     * @return the salaryEffectTypeList
     */
    public List<SymbolDTO> getSalaryEffectTypeList() {
        return salaryEffectTypeList;
    }

    /**
     * @return the salaryUnitType
     */
    public List<SymbolDTO> getSalaryUnitTypeList() {
        if (salaryUnitTypeList == null || salaryUnitTypeList.isEmpty()) {
            salaryUnitTypeList = symbolService.getSalaryUnitTypeByCompanyId(getUserData().getCompany().getId());
            setSalaryUnitTypeConverter(new SymbolDTOConverter(salaryUnitTypeList));
        }
        return salaryUnitTypeList;
    }

    /**
     * @param salaryUnitType the salaryUnitType to set
     */
    public void setSalaryUnitTypeList(List<SymbolDTO> salaryUnitTypeList) {
        this.salaryUnitTypeList = salaryUnitTypeList;
    }

    /**
     * @return the salaryEffectTypeConverter
     */
    public SymbolDTOConverter getSalaryEffectTypeConverter() {
        return salaryEffectTypeConverter;
    }

    /**
     * @param salaryEffectTypeConverter the salaryEffectTypeConverter to set
     */
    public void setSalaryEffectTypeConverter(SymbolDTOConverter salaryEffectTypeConverter) {
        this.salaryEffectTypeConverter = salaryEffectTypeConverter;
    }

    /**
     * @return the salaryUnitTypeConverter
     */
    public SymbolDTOConverter getSalaryUnitTypeConverter() {
        return salaryUnitTypeConverter;
    }

    /**
     * @param salaryUnitTypeConverter the salaryUnitTypeConverter to set
     */
    public void setSalaryUnitTypeConverter(SymbolDTOConverter salaryUnitTypeConverter) {
        this.salaryUnitTypeConverter = salaryUnitTypeConverter;
    }

    /**
     * @return the salariesEffectDTOList
     */
    public List<HrSalariesStructureEffectDTO> getSalariesEffectDTOList() {
        if (salariesEffectDTOList == null || salariesEffectDTOList.isEmpty()) {
            salariesEffectDTOList = new ArrayList<>();
            salariesEffectDTOList = salariesEffectService.findSalariesEffectById(getUserData().getCompany().getId());
            departmentDTOConverter = new DepartmentDTOConverter(departmentDTOList);

        }
        return salariesEffectDTOList;
    }

    /**
     * @param salariesEffectDTOList the salariesEffectDTOList to set
     */
    public void setSalariesEffectDTOList(List<HrSalariesStructureEffectDTO> salariesEffectDTOList) {
        this.salariesEffectDTOList = salariesEffectDTOList;
    }

    /**
     * @return the salariesEffectService
     */
    public HrSalariesEffectService getSalariesEffectService() {
        return salariesEffectService;
    }

    /**
     * @param salariesEffectService the salariesEffectService to set
     */
    public void setSalariesEffectService(HrSalariesEffectService salariesEffectService) {
        this.salariesEffectService = salariesEffectService;
    }

    /**
     * @return the salaryEffectTypeDTOListt
     */
    public List<HrSalaryEffectDTO> getSalaryEffectTypeDTOList() {
        if (salaryEffectTypeDTOList == null || salaryEffectTypeDTOList.isEmpty()) {
            salaryEffectTypeDTOList = salaryEffectTypeService.getAllSalariesEffectTypeByBranchId(getUserData().getUserBranch().getId());
            salaryEffectTypeDTOConverter = new SalaryEffectTypeDTOConverter(salaryEffectTypeDTOList);

        }

        return salaryEffectTypeDTOList;
    }

    /**
     * @param salaryEffectTypeDTOListt the salaryEffectTypeDTOListt to set
     */
    public void setSalaryEffectTypeDTOList(List<HrSalaryEffectDTO> salaryEffectTypeDTOList) {
        this.salaryEffectTypeDTOList = salaryEffectTypeDTOList;
    }

    /**
     * @return the salaryEffectTypeDTOConverter
     */
    public SalaryEffectTypeDTOConverter getSalaryEffectTypeDTOConverter() {
        return salaryEffectTypeDTOConverter;
    }

    /**
     * @param salaryEffectTypeDTOConverter the salaryEffectTypeDTOConverter to
     * set
     */
    public void setSalaryEffectTypeDTOConverter(SalaryEffectTypeDTOConverter salaryEffectTypeDTOConverter) {
        this.salaryEffectTypeDTOConverter = salaryEffectTypeDTOConverter;
    }

    /**
     * @return the hrSalaryStructureAccuralDeductionEffectTypeConverter
     */
    public HrSalaryStructureAccuralDeductionEffectTypeConverter getHrSalaryStructureAccuralDeductionEffectTypeConverter() {
        return hrSalaryStructureAccuralDeductionEffectTypeConverter;
    }

    /**
     * @param hrSalaryStructureAccuralDeductionEffectTypeConverter the hrSalaryStructureAccuralDeductionEffectTypeConverter to set
     */
    public void setHrSalaryStructureAccuralDeductionEffectTypeConverter(HrSalaryStructureAccuralDeductionEffectTypeConverter hrSalaryStructureAccuralDeductionEffectTypeConverter) {
        this.hrSalaryStructureAccuralDeductionEffectTypeConverter = hrSalaryStructureAccuralDeductionEffectTypeConverter;
    }

    /**
     * @return the accuralDeductionEffectTypeDTOList
     */
    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> getAccuralDeductionEffectTypeDTOList() {
           if (accuralDeductionEffectTypeDTOList == null || accuralDeductionEffectTypeDTOList.isEmpty()) {
            accuralDeductionEffectTypeDTOList = hrSalaryStructureAccuralDeductionEffectTypeService.getAllHrSalaryStructureAccuralDeductionEffectTypeByCompanyId(getUserData().getCompany().getId());

               setHrSalaryStructureAccuralDeductionEffectTypeConverter(new HrSalaryStructureAccuralDeductionEffectTypeConverter(accuralDeductionEffectTypeDTOList));
        }
        return accuralDeductionEffectTypeDTOList;
    }

    /**
     * @param accuralDeductionEffectTypeDTOList the accuralDeductionEffectTypeDTOList to set
     */
    public void setAccuralDeductionEffectTypeDTOList(List<HrSalaryStructureAccuralDeductionEffectTypeDTO> accuralDeductionEffectTypeDTOList) {
        this.accuralDeductionEffectTypeDTOList = accuralDeductionEffectTypeDTOList;
    }

      /**
     * @return the hrSalaryEffectTypeDTOAccuralList
     */
    public List<HrSalaryEffectTypeDTO> getHrSalaryEffectTypeDTOAccuralList() {
         if (hrSalaryEffectTypeDTOAccuralList == null || hrSalaryEffectTypeDTOAccuralList.isEmpty()) {
            hrSalaryEffectTypeDTOAccuralList = hrSalaryEffectTypeService.getAllHrSalaryEffectTypeDTOByCompanyId(getUserData().getCompany().getId(),0);
               setSalaryEffectTypeAccuralConvertor(new hrSalaryEffectTypeConvertor(hrSalaryEffectTypeDTOAccuralList));
        }
        return hrSalaryEffectTypeDTOAccuralList;
    }

    /**
     * @param hrSalaryEffectTypeDTOAccuralList the hrSalaryEffectTypeDTOAccuralList to set
     */
    public void setHrSalaryEffectTypeDTOAccuralList(List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOAccuralList) {
        this.hrSalaryEffectTypeDTOAccuralList = hrSalaryEffectTypeDTOAccuralList;
    }
    
    /**
     * @return the hrSalaryEffectTypeDTODeductionList
     */
    public List<HrSalaryEffectTypeDTO> getHrSalaryEffectTypeDTODeductionList() {
        if (hrSalaryEffectTypeDTODeductionList == null || hrSalaryEffectTypeDTODeductionList.isEmpty()) {
            hrSalaryEffectTypeDTODeductionList = hrSalaryEffectTypeService.getAllHrSalaryEffectTypeDTOByCompanyId(getUserData().getCompany().getId(),1);
               setSalaryEffectTypeDeductionConvertor(new hrSalaryEffectTypeConvertor(hrSalaryEffectTypeDTODeductionList));
        }
        return hrSalaryEffectTypeDTODeductionList;
    }

    /**
     * @param hrSalaryEffectTypeDTODeductionList the hrSalaryEffectTypeDTODeductionList to set
     */
    public void setHrSalaryEffectTypeDTODeductionList(List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTODeductionList) {
        this.hrSalaryEffectTypeDTODeductionList = hrSalaryEffectTypeDTODeductionList;
    }

    /**
     * @return the salaryEffectTypeAccuralConvertor
     */
    public hrSalaryEffectTypeConvertor getSalaryEffectTypeAccuralConvertor() {
        return salaryEffectTypeAccuralConvertor;
    }

    /**
     * @param salaryEffectTypeAccuralConvertor the salaryEffectTypeAccuralConvertor to set
     */
    public void setSalaryEffectTypeAccuralConvertor(hrSalaryEffectTypeConvertor salaryEffectTypeAccuralConvertor) {
        this.salaryEffectTypeAccuralConvertor = salaryEffectTypeAccuralConvertor;
    }

    /**
     * @return the salaryEffectTypeDeductionConvertor
     */
    public hrSalaryEffectTypeConvertor getSalaryEffectTypeDeductionConvertor() {
        return salaryEffectTypeDeductionConvertor;
    }

    /**
     * @param salaryEffectTypeDeductionConvertor the salaryEffectTypeDeductionConvertor to set
     */
    public void setSalaryEffectTypeDeductionConvertor(hrSalaryEffectTypeConvertor salaryEffectTypeDeductionConvertor) {
        this.salaryEffectTypeDeductionConvertor = salaryEffectTypeDeductionConvertor;
    }
 
}
