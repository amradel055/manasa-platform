/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GmFreezeDto;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.GmFreeze;
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
public class GmFreezeServiceImpl implements GmFreezeService {

    @EJB
    private GenericDAO dao;
    @EJB
    private OrganizationSiteService organizationSiteService;

    @Override
    public List<GmFreezeDto> getALLgm_freezeDtoByCompanyId(Integer companyId, IsagUser isagUser) {
        List<GmFreeze> gmFreezList = dao.findEntityListByCompanyId(GmFreeze.class, companyId);
        List<GmFreezeDto> dtoList = new ArrayList<>();
        dtoList = returnListDTOForList(gmFreezList, isagUser);
        return dtoList;
    }

    @Override
    public List<GmFreezeDto> getALLgm_freezeDtoByBranchId(Integer branchId, IsagUser isagUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<GmFreeze> gmFreezList = dao.findListByQuery("SELECT e FROM GmFreeze e WHERE e.branchId.id = :branchId ", params);
        List<GmFreezeDto> dtoList = new ArrayList<>();

        dtoList = returnListDTOForList(gmFreezList, isagUser);
        return dtoList;
    }

    @Override
    public GmFreezeDto findgm_freezeDtoById(GmFreezeDto gm_freezeDto, IsagUser isagUser) {
        GmFreeze gm_freezes = dao.findEntityById(GmFreeze.class, gm_freezeDto.getId());
        GmFreezeDto gm_freezeDtoDto = new GmFreezeDto();
        gm_freezeDtoDto = mapToDTO(gm_freezes, gm_freezeDtoDto, isagUser);
        return gm_freezeDtoDto;
    } 
     
      

    private GmFreezeDto mapToDTO(GmFreeze entity, GmFreezeDto dto, IsagUser isagUser) {
        dto = initData(dto, entity, isagUser);
        return dto;
    }

    private GmFreeze mapToEntity(GmFreezeDto dto, IsagUser isagUser) {
        GmFreeze entity = new GmFreeze();
        entity.setId(dto.getId());
        if (dto.getCustomerId() != null && dto.getCustomerId().getId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dto.getCustomerId().getId());
            entity.setCustomerId(invOrganizationSite);
        }

        entity.setDatefrom(dto.getDatefrom());
        entity.setDateto(dto.getDateto());
        entity.setNumber(dto.getNumber());

        if (dto.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dto.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dto.getCreatedDate());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);

        }
        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        if (dto.getSubscriptionId() != null) {
            GmSubscriptions gmSubscriptions = new GmSubscriptions();
            gmSubscriptions.setId(dto.getSubscriptionId().getId());
            entity.setSubscirbtionId(gmSubscriptions);
        }
        return entity;
    }

    private GmFreezeDto initData(GmFreezeDto dto, GmFreeze entity, IsagUser isagUser) {
        dto.setId(entity.getId());
//        dto.setBranchId(entity.getBranchId());
//        dto.setCompanyId(entity.getCompanyId());
        if (entity.getCustomerId() != null) {
            dto.setCustomerId(organizationSiteService.mapToDTOSmall(entity.getCustomerId()));
        }

        if (entity.getSubscirbtionId() != null) {
            GmSubscriptionsDTO gmSubscriptionsDTO = new GmSubscriptionsDTO();
            gmSubscriptionsDTO.setId(entity.getSubscirbtionId().getId());
            gmSubscriptionsDTO.setSerial(entity.getSubscirbtionId().getSerial());

            dto.setSubscriptionId(gmSubscriptionsDTO);
        }
        dto.setDatefrom(entity.getDatefrom());
        dto.setDateto(entity.getDateto());
        dto.setNumber(entity.getNumber());

        dto.setIndex(entity.getId());
        dto.setCreatedDate(entity.getCreationDate());
        dto.setCreatedBy(entity.getCreatedBy().getId());
        dto.setBranchId(entity.getBranchId().getId());
        return dto;
    }

    public GmFreezeDto validateSave(GmFreezeDto gm_freezeDtos) {
        StringBuilder error = new StringBuilder();

        if (gm_freezeDtos.getCustomerId() == null) {
            error.append("\n يجب أختيار عميل ");
        }
        if (!error.toString().isEmpty()) {
            gm_freezeDtos.setMsg(error.toString());
        }
        return gm_freezeDtos;
    }

    @Override
    public GmFreezeDto save(GmFreezeDto freezeDto, IsagUser isagUser) {
        try {
            freezeDto = validateSave(freezeDto);
            GmFreeze freeze;
            if (freezeDto != null && (freezeDto.getMsg() == null || freezeDto.getMsg().isEmpty())) {
                freeze = mapToEntity(freezeDto, isagUser);
                
                freeze = dao.updateEntity(freeze);

            }
        } catch (Exception e) {
            freezeDto.setMsg(e.toString());
            dao.rollbackQuery();
        }
        return freezeDto;
    }

    private List<GmFreezeDto> returnListDTOForList(List<GmFreeze> gmFreezList, IsagUser isagUser) {
        List<GmFreezeDto> dtosList = new ArrayList<>();
        for (GmFreeze gmFreez : gmFreezList) {
            GmFreezeDto gmFreezDto = new GmFreezeDto();
            gmFreezDto = mapToDTO(gmFreez, gmFreezDto, isagUser);
            dtosList.add(gmFreezDto);
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
       @Override
    public boolean calcFreezeStatusBySubscribtionId(Integer subscribtionId){
      
        Map<String, Object> params = new HashMap<>();
        params.put("subscribtionId", subscribtionId);
         params.put("newdate", new Date());
        List<GmFreeze> list = dao.findListByQuery("SELECT e FROM  GmFreeze e where e.subscirbtionId.id = :subscribtionId And :newdate between e.datefrom And e.dateto", params);
        if(list != null && !list.isEmpty() && list.get(0) != null ){
         return true ;
        }else{ 
         return false;
        }
        
    }

}
