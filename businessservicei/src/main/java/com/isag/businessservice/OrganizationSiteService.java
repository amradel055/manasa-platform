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
import com.isag.dto.ChartDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface OrganizationSiteService {
    public void updateImage(Integer clientProfileId, String img);

    public InvOrganizationSite addOrganizationSite(InvOrganizationSite invOrganizationSite);

    public InvOrganizationSite updateOrganizationSite(InvOrganizationSite invOrganizationSite);

    public void deleteOrganizationSite(InvOrganizationSite invOrganizationSite);

    public InvOrganizationSite findOrganizationSiteById(Integer invOrganizationSiteId);

    public InvOrganizationSiteDTO findOrganizationSiteDTOById(Integer invOrganizationSiteId);

    public List<InvOrganizationSiteDTO> getLeads(Integer branchId, Integer type, Date creationDateFrom, Date creationDateTo, String codeFrom, String codeTo);

    public List<InvOrganizationSiteDTO> findLeads(Integer branchId, Integer userId, Integer type);

    public List<InvOrganizationSiteDTO> findLeadsList(InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type);

    public List<InvOrganizationSiteDTO> getLeads(Integer branchId, Integer type, CrmLeadsSearchBean crmLeadsSearchBean);

    public String findMaxCodeForOrganizationSiteByBranchId(Integer branchId, Integer type);

    public List<InvOrganizationSite> getInvOrganizationSiteByCompanyId(Integer companyId);

    public List<InvOrganizationSite> getInvOrganizationSiteByBranchId(Integer BranchId);

    public List<InvOrganizationSite> getSupplierByBranchId(Integer BranchId);

    public List<InvOrganizationSite> getSupplierByBranchIdForSupplierAccountReportMB(Integer branchId, Integer type);

    public List<InvOrganizationSite> getCustomerByBranchId(Integer BranchId);

    public List<InvOrganizationSite> getorganizationSiteByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type, Integer type2);

    public List<InvOrganizationSite> getorganizationSiteByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type);

    public List<InvOrganizationSiteDTO> getorganizationSiteForOpenningBalanceDTO(Integer branchId, boolean active, Integer type);

    public List<InvOrganizationSite> getorganizationSiteByBranchIdForTasaheel(Integer branchId, boolean active, Integer type);

    public List<InvOrganizationSite> getContractorByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type);

    public List<InvOrganizationSite> getCustomerByBranchIdForReport(Integer branchId);

    public Boolean checkCodeExistence(InvOrganizationSite invOrganizationSite);

    public List<InvOrganizationSite> getInvOrganizationSiteByBranchIdPer(Integer branchId);

    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdDTO(Integer branchId, boolean active, Integer type);

    public List<InvOrganizationSiteDTO> getcontractorByBranchIdDTO(Integer branchId, boolean active);

    public List<contractorDataReportEntity> getcontractorByCode(contractorDataReportSearchBean reportSearchBean);

    public List<InvOrganizationSiteDTO> getCustomerDTOByBranchId(Integer branchId);

    public List<InvOrganizationSiteDTO> AddDelegatorToCustomerDTO(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, IsagUser isagUser);

    public List<InvOrganizationSiteDTO> getCustomerDTOFromAndTo(Integer branchId, invOrganizationSiteSearchBean organizationSiteSearchBean);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters, Integer type);

    public List<InvOrganizationSiteDTO> getALLCustomerDTOByBranchIdAndType(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer type);

    public void deleteOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, IsagUser isagUser);

    public List<InvOrganizationSiteDTO> getCustomerByBranchIdForReportDTO(Integer branchId);

    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdDTOSmall(Integer branchId, boolean active, Integer type);
    
    public List<InvOrganizationSiteDTO> getorganizationSiteDTOByBranchIdForGlBankModule(IsagUser isagUser, boolean active, Integer type);

    public InvOrganizationSiteDTO saveLead(InvOrganizationSiteDTO invOrganizationSiteDTO, IsagUser isagUser);

    public String validateSaveLead(InvOrganizationSiteDTO invOrganizationSiteDTO, IsagUser isagUser);

    public String vaildationContacts(InvOrganizationSiteDTO invOrganizationSiteDTO, IsagUser isagUser);

    public ChartDTO getCountCustomerPerMonth(Integer branchId, Integer type, Integer dateFilter);

    public Integer getNumberOfCustomerByBranchId(Integer branchId, Integer assignedTo, Integer createdBy);

    public Integer getNumberOfNewCustomerByBranchId(Integer branchId, Integer assignedTo, Integer createdBy);

    public List<InvOrganizationSiteDTO> findOrganizationSiteDTOByContact(Integer branchId, String mobile, String email);

    public ChartDTO getCountCustomerByLearnAboutCompanyfrom(Integer branchId);

    public ChartDTO getCountCustomerByleadStatus(Integer branchId);

    public ChartDTO getCountCustomerByProjectName(Integer branchId);

    public ChartDTO getCountCustomerByAssignedTo(Integer branchId, Date fromDate, Date toDate);

    public ChartDTO getCountCustomerByAssignedToAndStatus(Integer branchId, Integer status, Date fromDate, Date toDate);

    public Integer getNumberOfCustomerByBranchIdAndAssignedTo(Integer branchId, Integer assignedTo, Integer createdBy);

    public Integer getNumberOfNewCustomerByBranchIdAndAssignedTo(Integer branchId, Integer assignedTo, Integer createdBy);

    public InvOrganizationSite getInvOrganizationSiteBySerial(Integer branchId, String code);

    public Map<String, String> findMobileOrganizationSite(Integer branchId);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type);

    public List<InvOrganizationSiteDTO> findALLCrmLeads(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer assignedTo, Integer type);

    public List<InvOrganizationSiteDTO> findLeads(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer type, Integer branchId);

    public Long getTotalRegistors(Map<String, Object> filters, InvOrganizationSiteDTO invOrganizationSiteDTOSearch, Integer type, Integer branchId);

    public String saveOpenningBalance(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList);

    public List<InvOrganizationSiteDTO> findAllLeadsList(InvOrganizationSiteDTO invOrganizationSiteDTOSearch);

    public String saveTransferLead(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, IsagUser isagUser);

    public InvOrganizationSiteDTO getorganizationSiteByBranchIdAndCodeDTOSmall(Integer branchId, boolean active, Integer type, String code);

    public InvOrganizationSite getorganizationSiteByBranchIdAndCodeSmall(Integer branchId, boolean active, Integer type, String code);
    
    public InvOrganizationSiteDTO addNewCustomer(InvOrganizationSiteDTO invOrganizationSiteDTO, List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, IsagUser isagUser);
    
    public InvOrganizationSiteDTO addOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, IsagUser isagUser);
    
    public Boolean validateName(IsagUser isagUser, Integer invOrganizationSiteDTOid, String name, Integer type);
    
     public Boolean validateCode(IsagUser tobyUser, Integer invOrganizationSiteDTOid, String code) ;
     
     public Boolean validateCode(IsagUser tobyUser, Integer invOrganizationSiteDTOid, String code, Integer type);
     
     public List<InvOrganizationSiteDTO> getcontractorORSupplierByBranchIdDTO(Integer branchId, boolean active);
     
     public InvOrganizationSiteDTO mapToDTOSmall(InvOrganizationSite invOrganizationSite);
}
