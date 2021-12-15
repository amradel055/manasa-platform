/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.resualtDTO;
import com.isag.dto.searchDTO;
import com.isag.entity.GmSubscriptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Lenovo
 */

@Stateless
public class reportserviceimp implements  ReportService{
    @EJB
    private GenericDAO dao ;
    
    
    @Override
    public  List<resualtDTO> getReporySearch(searchDTO search) {
         Map<String, Object> params = new HashMap<>();
         params.put("dateFrom", search.getDateFrom());
         params.put("dateTo", search.getDateTo());
         params.put("status", search.getStatus());

        List<GmSubscriptions> gmSubscriptionses = dao.findListByQuery("SELECT e FROM GmSubscriptions e WHERE e.startDate = :dateFrom AND e.endDate= :dateTo AND e.status= :status" , params);
        List<resualtDTO> dtos = mapToDtoList (gmSubscriptionses);
         return dtos ;
    }
    
     private resualtDTO initMapToDTO( GmSubscriptions entity) {
        resualtDTO dTO = new resualtDTO();
        
        dTO.setClientName(entity.getOrganizationSiteId().getName());
        dTO.setClientName(entity.getOrganizationSiteId().getName());
        dTO.setSerial(entity.getSerial());
        dTO.setPhone(entity.getOrganizationSiteId().getPhone());
        dTO.setCreationDate(entity.getCreationDate());
        dTO.setValue(entity.getValue());
    
    return  dTO;
    
     } 
     
     private List<resualtDTO> mapToDtoList (List<GmSubscriptions> GmSubscriptionsDTOList) {
         List<resualtDTO> dtos = new ArrayList<>(); 
         for(int i =0 ; i < GmSubscriptionsDTOList.size() ; i++){
             dtos.add(initMapToDTO(GmSubscriptionsDTOList.get(i))); 
         }
     return dtos ;
     }

        
     }

