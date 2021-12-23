/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GmFreezeDto;
import com.isag.dto.GmMovementDTO;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.GmFreeze;
import com.isag.entity.GmMovement;
import com.isag.entity.GmSubscriptions;
import com.isag.entity.InvOrganizationSite;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class GmMovementServiceImpl implements GmMovementService {

    @EJB
    private GenericDAO dao;
    @EJB
    private OrganizationSiteService organizationSiteService;
     @EJB
    private GmSubscriptionsService gmSubscriptionsService;

    @Override
    public List<GmMovementDTO> getALLGmMovementDtoByCompanyId(Integer companyId, IsagUser isagUser) {
        List<GmMovement> gmFreezList = dao.findEntityListByCompanyId(GmMovement.class, companyId);
        List<GmMovementDTO> dtoList = new ArrayList<>();
        dtoList = returnListDTOForList(gmFreezList, isagUser);
        return dtoList;
    }

    @Override
    public List<GmMovementDTO> getALLGmMovementDtoByBranchId(Integer branchId, IsagUser isagUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<GmMovement> gmMovementsList = dao.findListByQuery("SELECT e FROM GmMovement e WHERE e.branchId.id = :branchId ", params);
        List<GmMovementDTO> dtoList = new ArrayList<>();

        dtoList = returnListDTOForList(gmMovementsList, isagUser);
        return dtoList;
    }

    @Override
    public GmMovementDTO findGmMovementDtoById(GmMovementDTO gmMovementDTO, IsagUser isagUser) {
        GmMovement gmMovement = dao.findEntityById(GmMovement.class, gmMovementDTO.getId());
        GmMovementDTO gmMovementDTO1 = new GmMovementDTO();
        gmMovementDTO1 = mapToDTO(gmMovement, gmMovementDTO1, isagUser);
        return gmMovementDTO1;
    } 
     
      

    private GmMovementDTO mapToDTO(GmMovement entity, GmMovementDTO dto, IsagUser isagUser) {
        dto = initData(dto, entity, isagUser);
        return dto;
    }

    private GmMovement mapToEntity(GmMovementDTO dto, IsagUser isagUser) {
        GmMovement entity = new GmMovement();
        entity.setId(dto.getId());
        if (dto.getCustomerIdFrom()!= null && dto.getCustomerIdFrom().getId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dto.getCustomerIdFrom().getId());
            entity.setCustomerIdFrom(invOrganizationSite);
        }
         if (dto.getCustomerIdTo()!= null && dto.getCustomerIdTo().getId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dto.getCustomerIdTo().getId());
            entity.setCustomerIdTo(invOrganizationSite);
        }
         if (dto.getSubscriptionIdFrom()!= null && dto.getSubscriptionIdFrom().getId() != null) {
            GmSubscriptions gmSubscriptions = new GmSubscriptions();
            gmSubscriptions.setId(dto.getSubscriptionIdFrom().getId());
            entity.setSubscribtionIdFrom(gmSubscriptions);
        }
         if (dto.getSubscriptionIdTo()!= null && dto.getSubscriptionIdTo().getId() != null) {
            GmSubscriptions gmSubscriptions = new GmSubscriptions();
            gmSubscriptions.setId(dto.getSubscriptionIdTo().getId());
            entity.setSubscribtionIdTo(gmSubscriptions);
        }
        entity.setDate(new Date());
        entity.setStartDate(dto.getStratDate() != null ? dto.getStratDate() : new Date());
        entity.setDays(dto.getDays());

        
        return entity;
    }

    private GmMovementDTO initData(GmMovementDTO dto, GmMovement entity, IsagUser isagUser) {
         
         dto.setId(entity.getId());
//        dto.setBranchId(entity.getBranchId());
//        dto.setCompanyId(entity.getCompanyId());
       

        if (entity.getSubscribtionIdFrom() != null) {
            GmSubscriptionsDTO gmSubscriptionsDTO = new GmSubscriptionsDTO();
            gmSubscriptionsDTO.setId(entity.getSubscribtionIdFrom().getId());
      
            dto.setSubscriptionIdFrom(gmSubscriptionsDTO);
        }
        if (entity.getSubscribtionIdTo()!= null) {
            GmSubscriptionsDTO gmSubscriptionsDTO = new GmSubscriptionsDTO();
            gmSubscriptionsDTO.setId(entity.getSubscribtionIdTo().getId());
      
            dto.setSubscriptionIdTo(gmSubscriptionsDTO);
        }
        if (entity.getCustomerIdFrom()!= null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(entity.getCustomerIdFrom().getId());
      
            dto.setCustomerIdFrom(invOrganizationSiteDTO);
        }
        if (entity.getCustomerIdTo()!= null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(entity.getCustomerIdTo().getId());
      
            dto.setCustomerIdTo(invOrganizationSiteDTO);
        }
        dto.setDate(entity.getDate());
        dto.setStratDate(entity.getStartDate());
        dto.setDays(entity.getDays());
        
        return dto;
    }

    public GmMovementDTO validateSave(GmMovementDTO gmMovementDTO) {
        StringBuilder error = new StringBuilder();

        if (gmMovementDTO.getCustomerIdFrom()== null) {
            error.append("\n يجب أختيار عميل ");
        }
        if (gmMovementDTO.getCustomerIdTo()== null) {
            error.append("\n يجب أختيار عميل ");
        }
        if (!error.toString().isEmpty()) {
            gmMovementDTO.setMsg(error.toString());
        }
        return gmMovementDTO;
    }

    @Override
    public GmMovementDTO save(GmMovementDTO gmMovementDTO, GmSubscriptionsDTO gmSubscriptionsDTO  , IsagUser isagUser) {
        try {
            gmMovementDTO = validateSave(gmMovementDTO);
            GmMovement gmMovement;  
            if (gmMovementDTO != null && (gmMovementDTO.getMsg() == null || gmMovementDTO.getMsg().isEmpty())) {
                GmSubscriptionsDTO gmSubscriptionsDTOTo =  new GmSubscriptionsDTO() ;
               gmSubscriptionsDTOTo =  gmSubscriptionsService.getValidGmSubscriptionsDTOByCustomerId(gmMovementDTO.getCustomerIdTo().getId());
                if(gmSubscriptionsDTOTo != null){
                gmMovementDTO.setSubscriptionIdTo(gmSubscriptionsDTOTo);
                }
                else {
               gmMovementDTO.getSubscriptionIdTo().setGlBankId(gmSubscriptionsDTO.getGlBankId());
               gmSubscriptionsDTOTo = gmSubscriptionsService.GmSubscribtionAutoSave(gmMovementDTO.getSubscriptionIdTo() , isagUser);

                
                }
                gmMovement = mapToEntity(gmMovementDTO, isagUser);
                gmMovement = dao.updateEntity(gmMovement);

            }
        } catch (Exception e) {
            gmMovementDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }
        return gmMovementDTO;
    }

    private List<GmMovementDTO> returnListDTOForList(List<GmMovement> gmMovementList, IsagUser isagUser) {
        List<GmMovementDTO> dtosList = new ArrayList<>();
        for (GmMovement gmMovement : gmMovementList) {
            GmMovementDTO gmMovementDTO = new GmMovementDTO();
            gmMovementDTO = mapToDTO(gmMovement, gmMovementDTO, isagUser);
            dtosList.add(gmMovementDTO);
        }
        return dtosList;
    }
    
     @Override
    public Integer calcTakenDaysByCustomerIdAndSubscribtionId(Integer subscribtionId){
      
        Map<String, Object> params = new HashMap<>();
        params.put("subscribtionId", subscribtionId);
        List<Long> list = dao.findListByQuery("SELECT sum(e.number) FROM  GmFreeze e where e.subscirbtionId.id = :subscribtionId", params);
        if(list != null && !list.isEmpty() && list.get(0) != null ){
         Integer i = list.get(0).intValue() ;
         return i ;
        }
        
      return 0 ;
    }

}
