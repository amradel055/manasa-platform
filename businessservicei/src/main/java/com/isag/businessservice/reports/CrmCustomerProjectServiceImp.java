/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.GenericService;
import com.isag.businessservice.reports.searchBean.CrmEventsReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.CrmCustomerInterestedProductsDTO;
import com.isag.dto.InstProjectGuideDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Mostafa
 */
@Stateless
 public class CrmCustomerProjectServiceImp implements CrmCutomerProjrctService{
    
   
    
    
     @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    
   // @EJB
   // private List<CrmCustomerInterestedProductsDTO> crmRebortList;

    /**
     * @return the numOfCustomer
     */
    
       @Override
    public List<CrmCustomerInterestedProductsDTO> getCrmCustomerInterestedProductsByOrganizationSiteId(Integer branchId, CrmEventsReportSearchBean crmEventsReportSearchBean) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        
        params.put("projectId", crmEventsReportSearchBean != null  && crmEventsReportSearchBean.getInstProjectGuideDTOFrom() != null? crmEventsReportSearchBean.getInstProjectGuideDTOFrom().getId():null);
        params.put("branchId", branchId);
        sql.append("SELECT c.projectId.name , count(c.organizationSiteId.id) , COALESCE(sum(c.interestedDegree),0) / count(c.organizationSiteId.id)  FROM CrmCustomerInterestedProducts c WHERE c.branchId.id = :branchId and c.projectId.id = :projectId  group by  c.projectId.id");
        List<Object[]> detailses = dao.findListByQuery(sql.toString(), params);
        List<CrmCustomerInterestedProductsDTO> list = mapToDTOList(detailses);
        return list;  
    }
    

    private List<CrmCustomerInterestedProductsDTO> mapToDTOList(List<Object[]> detailses) {
       List<CrmCustomerInterestedProductsDTO> crmCustomerInterestedProductsDTOs = new ArrayList<>();
        for (Object[] detailse : detailses) {
         crmCustomerInterestedProductsDTOs.add(initMapToDTOList( detailse));
        }
   return crmCustomerInterestedProductsDTOs;
    }
    
       private CrmCustomerInterestedProductsDTO initMapToDTOList(Object[] detailses) {
      CrmCustomerInterestedProductsDTO dto =new CrmCustomerInterestedProductsDTO();
      if(!detailses[0].toString().isEmpty()){
          InstProjectGuideDTO guideDTO = new InstProjectGuideDTO();
          guideDTO.setName(detailses[0].toString());
            dto.setProjectId(guideDTO);
      }
      if(!detailses[1].toString().isEmpty()){
      dto.setNumOfCustomer(((Long) detailses[1]).intValue());
          
      }
      if(!detailses[2].toString().isEmpty()){
      dto.setPercentageOfCustomerSatisfaction((BigDecimal) detailses[2]);
         
    return dto;
    }
         return dto;
    
    
}

}
