/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.toby.businessservice.reports.searchBean.invOrganizationSiteSearchBean;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.TobyUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface OrganizationSiteService {

    public List<InvOrganizationSite> getContractorByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type);

    public List<InvOrganizationSite> getorganizationSiteByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type);

    public List<InvOrganizationSiteDTO> getorganizationSiteDTOByBranchIdForGlBankModule(TobyUser tobyUser, boolean active, Integer type);

    public List<InvOrganizationSite> getInvOrganizationSiteByBranchId(Integer BranchId);

    public void deleteOrganizationSite(InvOrganizationSite invOrganizationSite);

    public InvOrganizationSite addOrganizationSite(InvOrganizationSite invOrganizationSite);

    public InvOrganizationSite updateOrganizationSite(InvOrganizationSite invOrganizationSite);

    public void deleteOrganizationSite(InvOrganizationSiteDTO invOrganizationSite, TobyUser tobyUser);

    public InvOrganizationSite findOrganizationSiteById(Integer invOrganizationSiteId);

    public InvOrganizationSiteDTO findOrganizationSiteById(Integer invOrganizationSiteId, TobyUser tobyUser);

    public List<InvOrganizationSite> getInvOrganizationSiteByCompanyId(Integer companyId);

    public List<InvOrganizationSiteDTO> getInvOrganizationSiteByBranchId(TobyUser tobyUser);

    public List<InvOrganizationSite> getSupplierByBranchId(Integer BranchId);

    public List<InvOrganizationSite> getSupplierByBranchIdForSupplierAccountReportMB(Integer branchId, Integer type);

    public List<InvOrganizationSite> getSupplierByBranchIdForSupplierAccountReportMB(Integer branchId,Integer gallaryId, Integer type, SupplierAccountSearchBean supplierAccountSearchBean);

    public List<InvOrganizationSite> getCustomerByBranchId(Integer branchId);
    
        public List<InvOrganizationSite> getorganizationSiteByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type, Integer type2);

    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdForGlBankModule(Integer branchId, boolean active, Integer type, TobyUser tobyUser);

    public List<InvOrganizationSiteDTO> getContractorByBranchIdForGlBankModuleDTO(Integer branchId, boolean active, Integer type, TobyUser tobyUser);

    public List<InvOrganizationSite> getCustomerByBranchIdForReport(Integer branchId);

    public Boolean checkCodeExistence(InvOrganizationSite invOrganizationSite);

    public List<InvOrganizationSite> getInvOrganizationSiteByBranchIdPer(Integer branchId);
    
     public InvOrganizationSite addOrganizationSiteNew(InvOrganizationSite invOrganizationSite);

    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdDTO(TobyUser tobyUser, boolean active, Integer type);

    public List<InvOrganizationSiteDTO> getcontractorByBranchIdDTO(Integer branchId, boolean active);

    public List<InvOrganizationSiteDTO> getCustomerByBranchIdDTO(TobyUser tobyUser);

    public List<InvOrganizationSiteDTO> getSupplierDTOByBranchId(TobyUser tobyUser);

    public List<InvOrganizationSiteDTO> findInvOrganizeSiteID(TobyUser tobyUser, Integer type, Integer active);

    public List<InvOrganizationSiteDTO> findInvOrganizeSiteClient(TobyUser tobyUser);

    public InvOrganizationSiteDTO addOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser);

    public List<InvOrganizationSiteDTO> getCustomerDTOByBranchId(Integer branchId, TobyUser tobyUser);

    public String findMaxCodeForOrganizationSiteByBranchId(Integer branchId, Integer type);

    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdDTO(Integer branchId, boolean active, Integer type);

    public List<InvOrganizationSiteDTO> getorganizationSiteDTOByType(Integer branchId, Integer type, TobyUser tobyUser);

    public List<InvOrganizationSiteDTO> getCustomerDTOByBranchId(Integer branchId);

    public List<InvOrganizationSiteDTO> AddDelegatorToCustomerDTO(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, TobyUser isagUser);

    public List<InvOrganizationSiteDTO> getCustomerDTOFromAndTo(Integer branchId, invOrganizationSiteSearchBean organizationSiteSearchBean);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters, Integer type);

    public List<InvOrganizationSiteDTO> getALLCustomerDTOByBranchIdAndType(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer type);

    public void deleteOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser isagUser);

    public List<InvOrganizationSiteDTO> getCustomerByBranchIdForReportDTO(Integer branchId);

    public List<InvOrganizationSiteDTO> getInvOrganizationSiteByBranchIdPerDTO(Integer branchId, TobyUser tobyUser);

    public InvOrganizationSiteDTO findOrganizationSiteByIdDTO(Integer invOrganizationSiteId);

    public InvOrganizationSiteDTO saveInvOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO, TobyUser tobyUser);

    public Boolean validateName(TobyUser tobyUser, Integer invOrganizationSiteDTOid, String name);

    public Boolean validateCode(TobyUser tobyUser, Integer invOrganizationSiteDTOid, String code);

    public Boolean validateName(TobyUser tobyUser, Integer invOrganizationSiteDTOid, String name, Integer type);

    public Boolean validateCode(TobyUser tobyUser, Integer invOrganizationSiteDTOid, String code, Integer type);

    public List<InvOrganizationSiteDTO> getSupplierByBranchIdDTO(Integer BranchId, TobyUser tobyUser);

    public List<InvOrganizationSiteDTO> getCustomerByBranchIdDTO(Integer branchId, TobyUser tobyUser);

    public List<InvOrganizationSiteDTO> getOrganizationDTOByBranchIdDTO(TobyUser tobyUser, Integer type);

    public List<InvOrganizationSiteDTO> getorganizationSiteByBranchIdDTOSmall(Integer branchId, boolean active, Integer type);
    
    public InvOrganizationSiteDTO addNewCustomer(InvOrganizationSiteDTO invOrganizationSiteDTO, List<InvOrganizationSiteDTO> invOrganizationSiteDTOList, TobyUser tobyUser);

    public List<InvOrganizationSiteDTO> getorganizationSiteForOpenningBalanceDTO(Integer branchId, boolean active, Integer type) ;

    public InvOrganizationSite getInvOrganizationSiteBySerial(Integer branchId, String code);
    
    public Map<String,String> findMobileOrganizationSite(Integer branchId);
        
    public String saveOpenningBalance(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList);
    
    public InvOrganizationSiteDTO mapToDTOSmall(InvOrganizationSite invOrganizationSite);
    
}
