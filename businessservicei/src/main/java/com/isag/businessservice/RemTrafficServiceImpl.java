/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RemCompanyDTO;
import com.isag.dto.RemTrafficDTO;
import com.isag.dto.RemTrafficDetailDTO;
import com.isag.entity.RemTraffic;
import com.isag.entity.IsagUser;
import com.isag.entity.RemCompany;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class RemTrafficServiceImpl implements RemTrafficService{

    @EJB
    private GenericDAO dao;
    @EJB
    RemTrafficDetailService remTrafficDetailService;

    @Override
    public RemTrafficDTO findTraffic(Integer trafficId) {
        if (trafficId != null) {
            RemTraffic entity = dao.findEntityById(RemTraffic.class, trafficId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public void deleteTraffic(RemTrafficDTO trafficesDTO) {
        remTrafficDetailService.deleteAllTrafficDetail(trafficesDTO.getRemTrafficDetailDTOList());
        RemTraffic traffices = new RemTraffic();
        traffices.setId(trafficesDTO.getId());
        dao.deleteEntity(traffices);
    }
    
    @Override
    public RemTrafficDTO save(RemTrafficDTO trafficesDTO, IsagUser isagUser) {
        RemTraffic traffic = mapToEntity(trafficesDTO, isagUser);
        traffic = dao.updateEntity(traffic);
        trafficesDTO.setRemTrafficDetailDTOList(remTrafficDetailService.saveAll(trafficesDTO.getRemTrafficDetailDTOList(), isagUser, traffic.getId()));
        remTrafficDetailService.deleteAllTrafficDetail(trafficesDTO.getRemTrafficDetailDTODeletedList());
        trafficesDTO = mapTODTO(traffic);
        return trafficesDTO;
    }

    @Override
    public List<RemTrafficDTO> findTraffics(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (branchId != null) {
            params.put("branchId", branchId);
            query = "SELECT b FROM RemTraffic b WHERE b.branchId.id = :branchId";
        } 
        List<RemTraffic> trafficeseList = dao.findListByQuery(query, params);
        List<RemTrafficDTO> listDTO = returnListDTO(trafficeseList);
        return listDTO;
    }

    private List<RemTrafficDTO> returnListDTO(List<RemTraffic> list) {
        List<RemTrafficDTO> dTOList = new ArrayList<>();
        for (RemTraffic entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private RemTrafficDTO mapTODTO(RemTraffic entity) {
        RemTrafficDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private RemTrafficDTO initMapToDTO(RemTraffic entity) {

        RemTrafficDTO dTO = new RemTrafficDTO();
        
        dTO.setRemTrafficDetailDTOList(remTrafficDetailService.findAllTrafficDetail(entity.getId()));
        
        dTO.setId(entity.getId());

        dTO.setName(entity.getName());
        dTO.setCode(entity.getCode());
        
        if(entity.getRemCompanyId() != null){
            RemCompanyDTO companyDTO = new RemCompanyDTO();
            companyDTO.setId(entity.getRemCompanyId().getId());
            companyDTO.setCode(entity.getRemCompanyId().getCode());
            companyDTO.setName(entity.getRemCompanyId().getName());
            dTO.setRemCompanyDTO(companyDTO);
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private RemTraffic mapToEntity(RemTrafficDTO dTO, IsagUser isagUser) {
        RemTraffic entity = new RemTraffic();

        entity.setName(dTO.getName());
        entity.setCode(dTO.getCode());
        
        if(dTO.getRemCompanyDTO() != null && dTO.getRemCompanyDTO().getId() != null){
            RemCompany remCompany = new RemCompany();
            remCompany.setId(dTO.getRemCompanyDTO().getId());
            entity.setRemCompanyId(remCompany);
        }

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setBranchId(isagUser.getBranchId());
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

}
