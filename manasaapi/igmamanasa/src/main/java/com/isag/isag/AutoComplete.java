/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.converter.DepartmentDTOConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.SymbolConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.dto.DepartmentDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.Symbol;
import java.util.ArrayList;
import java.util.List;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

/**
 *
 * @author amr
 */
public class AutoComplete {

    public static List<InvOrganizationSite> completOrgSite(String query, List<InvOrganizationSite> invOrgSites, InvOrganizationSiteConverter invOrgSiteConverter) {

        List<InvOrganizationSite> invOrgSiteList = invOrgSites;
        if (query == null || query.trim().equals("")) {

            invOrgSiteConverter = new InvOrganizationSiteConverter(invOrgSites);
            return invOrgSiteList;
        }
        List<InvOrganizationSite> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSite invOrgSiteFilter;

        for (int i = 0; i < invOrgSites.size(); i++) {
            invOrgSiteFilter = invOrgSiteList.get(i);
            nameAr = invOrgSiteFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }

            code = invOrgSiteFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }
        }

        invOrgSiteConverter = new InvOrganizationSiteConverter(filteredInvs);
        return filteredInvs;
    }
    public static List<Symbol> completeSymbol(String query, List<Symbol> symbolList, SymbolConverter symbolConverter) {
        List<Symbol> list = new ArrayList<>();
        try {
            List<Symbol> symbols = symbolList;
            if (query == null || query.trim().equals("")) {
                symbolConverter = new SymbolConverter(symbols);
                return symbols;
            }

            String nameAr;
            Integer code;
            Symbol symbol;
            for (int i = 0; i < symbolList.size(); i++) {
                symbol = symbols.get(i);
                nameAr = symbol.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !list.contains(symbol)) {
                    list.add(symbol);
                }

                code = symbol.getId();
                if (code != null && String.valueOf(code).contains(query) && !list.contains(symbol)) {
                    list.add(symbol);
                }
            }

            symbolConverter = new SymbolConverter(list);
        } catch (Exception e) {
            printStackTrace(e);
        }
        return list;
    }

    public static List<SymbolDTO> completeSymbolDTO(String query, List<SymbolDTO> symbolList, SymbolDTOConverter symbolConverter) {
        List<SymbolDTO> list = new ArrayList<>();
        try {
            List<SymbolDTO> symbols = symbolList;
            if (query == null || query.trim().equals("")) {
                symbolConverter = new SymbolDTOConverter(symbols);
                return symbols;
            }

            String nameAr;
            Integer code;
            SymbolDTO symbol;
            for (int i = 0; i < symbolList.size(); i++) {
                symbol = symbols.get(i);
                nameAr = symbol.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !list.contains(symbol)) {
                    list.add(symbol);
                }

                code = symbol.getId();
                if (code != null && String.valueOf(code).contains(query) && !list.contains(symbol)) {
                    list.add(symbol);
                }
            }

            symbolConverter = new SymbolDTOConverter(list);
        } catch (Exception e) {
            printStackTrace(e);
        }
        return list;
    }

        ////// HR ////////
    public static List<DepartmentDTO> completeDepartment(String query, List<DepartmentDTO> departmentDTOList, DepartmentDTOConverter departmentDTOConverter) {
        List<DepartmentDTO> Department = departmentDTOList;
        if (query == null || query.trim().equals("")) {
            departmentDTOConverter = new DepartmentDTOConverter(Department);
            return Department;
        }
        List<DepartmentDTO> filteredDepartment = new ArrayList<>();

        String nameAr;
        Integer code;
        DepartmentDTO departmentSymbol;
        for (int i = 0; i < departmentDTOList.size(); i++) {
            departmentSymbol = departmentDTOList.get(i);
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
    
    public static List<SymbolDTO> completeMaritalStatus(String query, List<SymbolDTO> maritalStatusList, SymbolDTOConverter maritalStatusDTOConverter) {
        List<SymbolDTO> MaritalStatus = maritalStatusList;
        if (query == null || query.trim().equals("")) {
            maritalStatusDTOConverter = new SymbolDTOConverter(MaritalStatus);
            return MaritalStatus;
        }
        List<SymbolDTO> filteredMaritalStatus = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO maritalStatusSymbol;
        for (int i = 0; i < maritalStatusList.size(); i++) {
            maritalStatusSymbol = maritalStatusList.get(i);
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

        maritalStatusDTOConverter = new SymbolDTOConverter(filteredMaritalStatus);
        return filteredMaritalStatus;
    }

    public static List<SymbolDTO> completeEmpQualifications(String query, List<SymbolDTO> empQualificationsDTOList, SymbolDTOConverter empQualificationsConverter) {
        List<SymbolDTO> EmpQualifications = empQualificationsDTOList;
        if (query == null || query.trim().equals("")) {
            empQualificationsConverter = new SymbolDTOConverter(EmpQualifications);
            return EmpQualifications;
        }
        List<SymbolDTO> filteredEmpQualifications = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO empQualificationsSymbol;
        for (int i = 0; i < empQualificationsDTOList.size(); i++) {
            empQualificationsSymbol = empQualificationsDTOList.get(i);
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

        empQualificationsConverter = new SymbolDTOConverter(filteredEmpQualifications);
        return filteredEmpQualifications;
    }

    public static List<SymbolDTO> completePlaceBirth(String query, List<SymbolDTO> placeBirthList, SymbolDTOConverter placeBirthDTOConverter) {
        List<SymbolDTO> PlaceBirth = placeBirthList;
        if (query == null || query.trim().equals("")) {
            placeBirthDTOConverter = new SymbolDTOConverter(PlaceBirth);
            return PlaceBirth;
        }
        List<SymbolDTO> filteredplaceBirth = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO placeBirthSymbol;
        for (int i = 0; i < placeBirthList.size(); i++) {
            placeBirthSymbol = placeBirthList.get(i);
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

        placeBirthDTOConverter = new SymbolDTOConverter(filteredplaceBirth);
        return filteredplaceBirth;
    }

    public static List<SymbolDTO> completeJobGrade(String query, List<SymbolDTO> obGradList, SymbolDTOConverter jobGradDTOConverter) {
        List<SymbolDTO> JobGrade = obGradList;
        if (query == null || query.trim().equals("")) {
            jobGradDTOConverter = new SymbolDTOConverter(JobGrade);
            return JobGrade;
        }
        List<SymbolDTO> filteredJobGrade = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO jobGradListSymbol;
        for (int i = 0; i < obGradList.size(); i++) {
            jobGradListSymbol = obGradList.get(i);
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

        jobGradDTOConverter = new SymbolDTOConverter(filteredJobGrade);
        return filteredJobGrade;
    }

    public static List<SymbolDTO> completeNationality(String query, List<SymbolDTO> nationalityList, SymbolDTOConverter nationalityDTOConverter) {
        List<SymbolDTO> Nationality = nationalityList;
        if (query == null || query.trim().equals("")) {
            nationalityDTOConverter = new SymbolDTOConverter(Nationality);
            return Nationality;
        }
        List<SymbolDTO> filteredNationality = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO nationalityListSymbol;
        for (int i = 0; i < nationalityList.size(); i++) {
            nationalityListSymbol = nationalityList.get(i);
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

        nationalityDTOConverter = new SymbolDTOConverter(filteredNationality);
        return filteredNationality;
    }

    public static List<SymbolDTO> completeDesignation(String query, List<SymbolDTO> deisgnationList, SymbolDTOConverter designationDTOConverter) {
        List<SymbolDTO> Designation = deisgnationList;
        if (query == null || query.trim().equals("")) {
            designationDTOConverter = new SymbolDTOConverter(Designation);
            return Designation;
        }
        List<SymbolDTO> filteredDesignation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO designationListSymbol;
        for (int i = 0; i < deisgnationList.size(); i++) {
            designationListSymbol = deisgnationList.get(i);
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

        designationDTOConverter = new SymbolDTOConverter(filteredDesignation);
        return filteredDesignation;
    }

    public static List<SymbolDTO> completeGender(String query, List<SymbolDTO> genderList, SymbolDTOConverter genderDTOConverter) {
        List<SymbolDTO> Gender = genderList;
        if (query == null || query.trim().equals("")) {
            genderDTOConverter = new SymbolDTOConverter(Gender);
            return Gender;
        }
        List<SymbolDTO> filteredGender = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO genderListSymbol;
        for (int i = 0; i < genderList.size(); i++) {
            genderListSymbol = genderList.get(i);
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

        genderDTOConverter = new SymbolDTOConverter(filteredGender);
        return filteredGender;
    }

    public static List<SymbolDTO> completeReligion(String query, List<SymbolDTO> religionList, SymbolDTOConverter religionDTOConverter) {
        List<SymbolDTO> Religion = religionList;
        if (query == null || query.trim().equals("")) {
            religionDTOConverter = new SymbolDTOConverter(Religion);
            return Religion;
        }
        List<SymbolDTO> filteredReligion = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO religionListSymbol;
        for (int i = 0; i < religionList.size(); i++) {
            religionListSymbol = religionList.get(i);
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

        religionDTOConverter = new SymbolDTOConverter(filteredReligion);
        return filteredReligion;
    }

    public static List<SymbolDTO> completeEducationLevel(String query, List<SymbolDTO> educationLevelList, SymbolDTOConverter educationLevelDTOConverter) {
        List<SymbolDTO> EducationLevel = educationLevelList;
        if (query == null || query.trim().equals("")) {
            educationLevelDTOConverter = new SymbolDTOConverter(EducationLevel);
            return EducationLevel;
        }
        List<SymbolDTO> filteredEducationLevel = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO educationLevelListSymbol;
        for (int i = 0; i < educationLevelList.size(); i++) {
            educationLevelListSymbol = educationLevelList.get(i);
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

        educationLevelDTOConverter = new SymbolDTOConverter(filteredEducationLevel);
        return filteredEducationLevel;
    }

    public static List<SymbolDTO> completeRelation(String query, List<SymbolDTO> relationList, SymbolDTOConverter relationDTOConverter) {
        List<SymbolDTO> Relation = relationList;
        if (query == null || query.trim().equals("")) {
            relationDTOConverter = new SymbolDTOConverter(Relation);
            return Relation;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < relationList.size(); i++) {
            relationListSymbol = relationList.get(i);
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

        relationDTOConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }

    public static List<SymbolDTO> completeSalaryUnitType(String query, List<SymbolDTO> salaryUnitTypeList, SymbolDTOConverter salaryUnitTypeConverter) {
        List<SymbolDTO> SalaryUnitType = salaryUnitTypeList;
        if (query == null || query.trim().equals("")) {
            salaryUnitTypeConverter = new SymbolDTOConverter(SalaryUnitType);
            return SalaryUnitType;
        }
        List<SymbolDTO> filteredSalaryUnitType = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO salaryUnitTypeSymbol;
        for (int i = 0; i < salaryUnitTypeList.size(); i++) {
            salaryUnitTypeSymbol = salaryUnitTypeList.get(i);
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

        salaryUnitTypeConverter = new SymbolDTOConverter(filteredSalaryUnitType);
        return filteredSalaryUnitType;
    }
    
    ///// CRM ////
   
    public static List<SymbolDTO> completeLeadStatus(String query, List<SymbolDTO> leadStatusList, SymbolDTOConverter leadStatusDTOConverter) {
        List<SymbolDTO> leadStatus = leadStatusList;
        if (query == null || query.trim().equals("")) {
            leadStatusDTOConverter = new SymbolDTOConverter(leadStatus);
            return leadStatus;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < leadStatusList.size(); i++) {
            relationListSymbol = leadStatusList.get(i);
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

        leadStatusDTOConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }
    
    public static List<SymbolDTO> completeEventStatus(String query, List<SymbolDTO> eventStatusList, SymbolDTOConverter eventStatusDTOConverter) {
        List<SymbolDTO> leadStatus = eventStatusList;
        if (query == null || query.trim().equals("")) {
            eventStatusDTOConverter = new SymbolDTOConverter(leadStatus);
            return leadStatus;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < eventStatusList.size(); i++) {
            relationListSymbol = eventStatusList.get(i);
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

        eventStatusDTOConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }

    public static List<SymbolDTO> completeSymbol(String query, List<SymbolDTO> symbolList, SymbolDTOConverter symbolConverter) {
        List<SymbolDTO> activityType = symbolList;
        if (query == null || query.trim().equals("")) {
            symbolConverter = new SymbolDTOConverter(activityType);
            return activityType;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < symbolList.size(); i++) {
            relationListSymbol = symbolList.get(i);
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

        symbolConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }
    
    public static List<SymbolDTO> completeActivityType(String query, List<SymbolDTO> activityTypeList, SymbolDTOConverter activityTypeDTOConverter) {
        List<SymbolDTO> activityType = activityTypeList;
        if (query == null || query.trim().equals("")) {
            activityTypeDTOConverter = new SymbolDTOConverter(activityType);
            return activityType;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < activityTypeList.size(); i++) {
            relationListSymbol = activityTypeList.get(i);
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

        activityTypeDTOConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }
    
    public static List<SymbolDTO> completepriority(String query, List<SymbolDTO> priorityList, SymbolDTOConverter priorityDTOConverter) {
        List<SymbolDTO> activityType = priorityList;
        if (query == null || query.trim().equals("")) {
            priorityDTOConverter = new SymbolDTOConverter(activityType);
            return activityType;
        }
        List<SymbolDTO> filteredRelation = new ArrayList<>();

        String nameAr;
        Integer code;
        SymbolDTO relationListSymbol;
        for (int i = 0; i < priorityList.size(); i++) {
            relationListSymbol = priorityList.get(i);
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

        priorityDTOConverter = new SymbolDTOConverter(filteredRelation);
        return filteredRelation;
    }


}
