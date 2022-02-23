/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.CrmEventsReportSearchBean;
import com.isag.dto.CrmCustomerInterestedProductsDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface CRMCustomerInterestedProductsServiceDTO {
    
    public List<CrmCustomerInterestedProductsDTO> getCrmCustomerInterestedProductsByOrganizationSiteId(Integer organizationSiteId);
    
    public List<CrmCustomerInterestedProductsDTO> saveList(List<CrmCustomerInterestedProductsDTO> dtoList,Integer invOrganizationSite , IsagUser isagUser) ;
    
    public CrmCustomerInterestedProductsDTO save(CrmCustomerInterestedProductsDTO dto, IsagUser isagUser) ;

    public String validateSave(CrmCustomerInterestedProductsDTO dto);
    
    public void deleteCrmCustomerInterestedProductDTO(Integer crmEventDTOId, IsagUser isagUser);
    
    public void deleteCrmCustomerInterestedProductDTOList(List<CrmCustomerInterestedProductsDTO> list, IsagUser isagUser);
    
    public List<CrmCustomerInterestedProductsDTO> findCrmCustomerInterestedProducts(CrmEventsReportSearchBean eventsReportSearchBean,Integer branchId);


}
