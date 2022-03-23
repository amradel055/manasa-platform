/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IsagApiTokenDTO;
import com.isag.entity.IsagApiToken;
import com.isag.entity.IsagUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class IsagApiTokenImpl implements IsagApiTokenService {

    @EJB
    private GenericDAO dao;

    @Override
    public IsagApiTokenDTO addIsagApiToken(IsagApiTokenDTO isagApiTokenDTO, IsagUser isagUser) {
        IsagApiToken entity = mapFromDTO(isagApiTokenDTO, isagUser);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity, null);
    }

    @Override
    public IsagApiTokenDTO getIsagApiTokenDTOByTokenAndTokenID(IsagApiTokenDTO isagApiTokenDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", isagApiTokenDTO.getTokenId());
        params.put("token", isagApiTokenDTO.getToken());
        IsagApiToken isagApiToken = dao.findEntityByQuery("SELECT t FROM IsagApiToken t WHERE t.tokenId  = :tokenId  AND t.token = :token ", params);
        IsagApiTokenDTO apiTokenDTO = new IsagApiTokenDTO();
        mapTODTO(isagApiToken,apiTokenDTO);
        return apiTokenDTO;
    }

    private List<IsagApiTokenDTO> returnListDTO(List<IsagApiToken> list) {
        List<IsagApiTokenDTO> dTOList = new ArrayList<>();
        for (IsagApiToken entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
        return dTOList;
    }

    public IsagApiTokenDTO mapTODTO(IsagApiToken entity, IsagApiTokenDTO dTO) {
        if (dTO == null) {
            dTO = new IsagApiTokenDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(IsagApiTokenDTO dTO, IsagApiToken entity) {
        dTO.setToken(entity.getToken());
        dTO.setTokenId(entity.getTokenId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCompanyId(entity.getCompanyId().getId());
    }

    private IsagApiToken mapFromDTO(IsagApiTokenDTO dTO, IsagUser isagUser) {
        IsagApiToken entity = new IsagApiToken();
        entity.setToken(dTO.getToken());
        entity.setTokenId(dTO.getTokenId());
        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
        }
        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }
}
