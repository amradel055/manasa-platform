/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.CrmEventsReportSearchBean;
import com.toby.dto.CrmCustomerInterestedProductsDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface CRMCustomerInterestedProductsServiceDTO {
    
    public List<CrmCustomerInterestedProductsDTO> getCrmCustomerInterestedProductsByOrganizationSiteId(Integer organizationSiteId);
    
    public List<CrmCustomerInterestedProductsDTO> saveList(List<CrmCustomerInterestedProductsDTO> dtoList,Integer invOrganizationSite , TobyUser tobyUser) ;
    
    public CrmCustomerInterestedProductsDTO save(CrmCustomerInterestedProductsDTO dto, TobyUser tobyUser) ;

    public String validateSave(CrmCustomerInterestedProductsDTO dto);
    
    public void deleteCrmCustomerInterestedProductDTO(Integer crmEventDTOId, TobyUser tobyUser);
    
    public void deleteCrmCustomerInterestedProductDTOList(List<CrmCustomerInterestedProductsDTO> list, TobyUser tobyUser);
    
    public List<CrmCustomerInterestedProductsDTO> findCrmCustomerInterestedProducts(CrmEventsReportSearchBean eventsReportSearchBean,Integer branchId);


}
