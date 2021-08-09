/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InstProfitRateDTO;
import com.isag.entity.InstProfitRate;
import com.isag.entity.IsagUser;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class InstProfitRateServiceImpl implements InstProfitRateService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<InstProfitRateDTO> getAllInstProfitRatesListByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<InstProfitRateDTO> dTOList = new ArrayList<>();
        List<InstProfitRate> list = dao.findListByQuery("SELECT d FROM InstProfitRate d WHERE d.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public InstProfitRateDTO addInstProfitRate(InstProfitRateDTO profitRateDTO) {
        return dao.updateEntity(profitRateDTO);
    }

    @Override
    public InstProfitRateDTO findInstProfitRate(Integer profitRateId) {
        InstProfitRate intsInstProfitRate = dao.findEntityById(InstProfitRate.class, profitRateId);
        InstProfitRateDTO instProfitRateDTO = mapTODTO(intsInstProfitRate, null);
        return instProfitRateDTO;
    }

    
    @Override
    public InstProfitRateDTO updateInstProfitRate(InstProfitRateDTO profitRateDTO) {
        return dao.updateEntity(profitRateDTO);
    }

    @Override
    public void deleteInstProfitRate(InstProfitRateDTO profitRateDTO, IsagUser isagUser) {
        InstProfitRate entity = new InstProfitRate();
        entity.setId(profitRateDTO.getId());
        dao.deleteEntity(entity);
    }

    public InstProfitRateDTO mapTODTO(InstProfitRate entity, InstProfitRateDTO dTO) {
        if (dTO == null) {
            dTO = new InstProfitRateDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(InstProfitRateDTO dTO, InstProfitRate entity) {

        dTO.setMonthNumber(entity.getMonthNumber());
        dTO.setRate(entity.getRate());
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());

        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public InstProfitRate mapFromDTO(InstProfitRateDTO dTO, IsagUser isagUser) {
        InstProfitRate entity = new InstProfitRate();

        entity.setMonthNumber(dTO.getMonthNumber());
        entity.setRate(dTO.getRate());
        entity.setId(dTO.getId());

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);

        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<InstProfitRate> list, List<InstProfitRateDTO> dTOList) {
        for (InstProfitRate entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    @Override
    public List<InstProfitRateDTO> addInstProfitRate(List<InstProfitRateDTO> profitRateDTOList, IsagUser isagUser) {
        List<InstProfitRateDTO> profitRateDTOLists = new ArrayList<>();
        for (InstProfitRateDTO profitRateDTO : profitRateDTOList) {
            InstProfitRate profitRate = mapFromDTO(profitRateDTO, isagUser);
            profitRate = dao.updateEntity(profitRate);
            profitRateDTO = mapTODTO(profitRate, profitRateDTO);
            profitRateDTOLists.add(profitRateDTO);
        }

        return profitRateDTOLists;
    }
    
      @Override
    public BigDecimal getInstProfitRatesByMonthAndBranchId(Integer branchId,Integer monthNumber) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("monthNumber", monthNumber); 
        List<InstProfitRate> list = dao.findListByQuery("SELECT d FROM InstProfitRate d WHERE d.branchId.id=:branchId AND d.monthNumber >= :monthNumber order by d.monthNumber asc", params);
          BigDecimal rate = BigDecimal.ZERO;
        if(!list.isEmpty()){
           rate = list.get(0).getRate();
       }
        return rate;
    }
}
