/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.resualtDTO;
import com.isag.dto.searchDTO;
import com.isag.entity.GmFreeze;
import com.isag.entity.GmSubscriptions;
import com.isag.entity.IsagUser;
import java.math.BigDecimal;
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
public class reportserviceimp implements ReportService {
    
    @EJB
    private GenericDAO dao;
    
    @EJB
    private IsagUserService isagUseService;
    
    @Override
    public List<resualtDTO> getReporySearch(searchDTO search) {
        Map<String, Object> params = new HashMap<>();
        params.put("dateFrom", search.getDateFrom());
        params.put("dateTo", search.getDateTo());
        params.put("status", search.getStatus());
        
        List<GmSubscriptions> gmSubscriptionses = dao.findListByQuery("SELECT e FROM GmSubscriptions e WHERE e.startDate = :dateFrom AND e.endDate= :dateTo AND e.status= :status", params);
        List<resualtDTO> dtos = mapToDtoList(gmSubscriptionses);
        return dtos;
    }
    
    @Override
    public List<resualtDTO> getAllValidSubsByDate(searchDTO search) {
        
        Map<String, Object> params = new HashMap<>();
        
        params.put("branchId", search.getBranchId());
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT e FROM GmSubscriptions e WHERE e.branchId.id = :branchId ");
        if (search.getDateFrom() != null) {
            params.put("dateFrom", search.getDateFrom());
            query.append(" AND e.date >= :dateFrom");
        }
        
        if (search.getDateTo() != null) {
            params.put("dateTo", search.getDateTo());
            query.append(" AND e.date <= :dateTo");
        }
        
        List<GmSubscriptions> gmSubscriptionses = dao.findListByQuery(query.toString(), params);
        List<resualtDTO> dtos = mapToDtoList(gmSubscriptionses);
        return dtos;
    }
    
    @Override
    public List<resualtDTO> getAllDeletedSubsByDate(searchDTO search) {
        
        Map<String, Object> params = new HashMap<>();
        
        params.put("branchId", search.getBranchId());
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT e FROM GmSubscriptions e WHERE e.branchId.id = :branchId AND e.deletedBy IS NOT NULL");
        if (search.getDateFrom() != null) {
            params.put("dateFrom", search.getDateFrom());
            query.append(" AND e.deletionDate >= :dateFrom");
        }
        
        if (search.getDateTo() != null) {
            params.put("dateTo", search.getDateTo());
            query.append(" AND e.deletionDate <= :dateTo");
        }
        
        List<GmSubscriptions> gmSubscriptionses = dao.findListByQuery(query.toString(), params);
        List<resualtDTO> dtos = mapToDtoList(gmSubscriptionses);
        return dtos;
    }
    
    @Override
    public BigDecimal getAllValidSubsFinalNet(searchDTO search) {
        
        Map<String, Object> params = new HashMap<>();
        
        params.put("branchId", search.getBranchId());
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT sum(e.finalNet) FROM GmSubscriptions e WHERE e.branchId.id = :branchId ");
        if (search.getDateFrom() != null) {
            params.put("dateFrom", search.getDateFrom());
            query.append(" AND e.date >= :dateFrom");
        }
        
        if (search.getDateTo() != null) {
            params.put("dateTo", search.getDateTo());
            query.append(" AND e.date <= :dateTo");
        }
        
        List<BigDecimal> finalNet = dao.findListByQuery(query.toString(), params);
        if (finalNet != null && !finalNet.isEmpty() && finalNet.get(0) != null) {
            return finalNet.get(0);
        } else {
            
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public List<resualtDTO> getAllFreezeByDate(searchDTO search) {
        
        Map<String, Object> params = new HashMap<>();
        
        params.put("branchId", search.getBranchId());
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT e FROM GmFreeze e WHERE e.branchId.id = :branchId ");
        if (search.getDateFrom() != null) {
            params.put("dateFrom", search.getDateFrom());
            query.append(" AND e.datefrom >= :dateFrom");
        }
        
        if (search.getDateTo() != null) {
            params.put("dateTo", search.getDateTo());
            query.append(" AND e.dateto <= :dateTo");
        }
        
        List<GmFreeze> gmFreezes = dao.findListByQuery(query.toString(), params);
        List<GmSubscriptions> gmSubscriptionseList = new ArrayList<>();
        for (GmFreeze freeze : gmFreezes) {
            if (freeze.getSubscirbtionId() != null && freeze.getSubscirbtionId().getId() != null) {
                GmSubscriptions gmSubscriptions = dao.findEntityById(GmSubscriptions.class, freeze.getSubscirbtionId().getId());
                gmSubscriptionseList.add(gmSubscriptions);                
            }
        }
        
        List<resualtDTO> dtos = mapToDtoList(gmSubscriptionseList);
        
        return dtos;
    }
    
    private resualtDTO initMapToDTO(GmSubscriptions entity) {
        resualtDTO dTO = new resualtDTO();
        dTO.setId(entity.getId());
        dTO.setClientName(entity.getOrganizationSiteId().getName());
        dTO.setClientName(entity.getOrganizationSiteId().getName());
        dTO.setSerial(entity.getSerial());
        dTO.setPhone(entity.getOrganizationSiteId().getMobile());
        dTO.setCreationDate(entity.getCreationDate());
        dTO.setValue(entity.getFinalNet());
        dTO.setCreatedBy(entity.getCreatedBy().getName());
        if (entity.getDeletedBy() != null) {
            
            dTO.setDeletedBy(entity.getDeletedBy());
        }
        
        if (entity.getDeletionDate() != null) {
            dTO.setDeletionDate(entity.getDeletionDate());
        }
        
        return dTO;
        
    }
    
    private List<resualtDTO> mapToDtoList(List<GmSubscriptions> GmSubscriptionsDTOList) {
        List<resualtDTO> dtos = new ArrayList<>();
        for (int i = 0; i < GmSubscriptionsDTOList.size(); i++) {
            dtos.add(initMapToDTO(GmSubscriptionsDTOList.get(i)));
        }
        return dtos;
    }
    
    @Override
    public resualtDTO findElresultById(resualtDTO reseualtdto) {
        GmSubscriptions gmSubscriptions = dao.findEntityById(GmSubscriptions.class, reseualtdto.getId());
        resualtDTO resualt = initMapToDTO(gmSubscriptions);
        return resualt;
    }
    
}
