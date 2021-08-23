/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IsagTokensDTO;
import com.isag.dto.IsagUserDTO;
import com.isag.entity.Branch;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagTokens;
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
 * @author AhmedEssam@ISAG.com
 * @since 2020.06.21
 */
@Stateless
public class IsagTokensImp implements IsagTokensService {

    @EJB
    private GenericDAO dao;

    @Override
    public IsagTokensDTO addIsagToken(IsagTokensDTO isagTokensDTO, IsagUser isagUser) {
        IsagTokens isagTokens = mapFromDTO(isagTokensDTO, isagUser);
        isagTokens = dao.updateEntity(isagTokens);
        return mapTODTO(isagTokens, Boolean.FALSE);
    }

    @Override
    public IsagTokensDTO addIsagTokenAlreadyExist(IsagTokensDTO isagTokensDTO, IsagUser isagUser) {
        IsagTokensDTO itdto = getAllIsagTokenDTO(isagUser);
        itdto.setId(itdto.getId());
        itdto.setActive(Boolean.TRUE);
        itdto.setToken(isagTokensDTO.getToken());
        return addIsagToken(itdto, isagUser);
    }

    @Override
    public void deleteIsagTokenDTO(IsagTokensDTO isagTokensDTO, IsagUser isagUser) {
        IsagTokens isagToken = mapFromDTO(isagTokensDTO, isagUser);
        dao.deleteEntity(isagToken);
    }

    @Override
    public void deleteAllIsagTokenDTO(IsagTokensDTO isagTokensDTO) {
        dao.executeDeleteQuery("DELETE token FROM IsagTokens token WHERE token.id = " + isagTokensDTO.getId());
    }

    @Override
    public void UpdateActiveAllIsagTokenDTO(Boolean active) {
       dao.executeUpdateQuery("UPDATE  IsagTokens token SET  token.active = " + active);
    }

    @Override
    public void UpdateActiveIsagTokenDTObyUserID(Integer userId, Boolean active) {
        dao.executeUpdateQuery("UPDATE IsagTokens token SET  token.active = " + active + " WHERE token.createdBy.id = " + userId);
    }

    @Override
    public List<IsagTokensDTO> getAllIsagTokenDTO(Integer branchid) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", branchid);
        List<IsagTokens> isagTokenList = dao.findListByQuery("SELECT token FROM IsagTokens token WHERE token.branchId.id = :branchid", params);
        return returnListDTO(isagTokenList);
    }

    @Override
    public IsagTokensDTO getAllIsagTokenDTO(IsagUser isagUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", isagUser.getBranchId().getId());
        params.put("createdBy", isagUser.getId());
        List<IsagTokens> isagTokenList = dao.findListByQuery("SELECT token FROM IsagTokens token WHERE token.branchId.id = :branchid AND token.createdBy.id = :createdBy", params);
        return initMapToDTO(isagTokenList != null && !isagTokenList.isEmpty() ? isagTokenList.get(0) : new IsagTokens());
    }

    @Override
    public List<IsagTokensDTO> getAllIsagTokenDTObyActive(IsagUser isagUser, Boolean active) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", isagUser.getBranchId().getId());
        params.put("active", active);
        List<IsagTokens> isagTokenList = dao.findListByQuery("SELECT token FROM IsagTokens token WHERE token.branchId.id = :branchid AND token.active = :active", params);
        return returnListDTO(isagTokenList);
    }

    @Override
    public List<IsagTokensDTO> getAllIsagTokenDTO() {
        List<IsagTokens> isagTokenList = dao.findListByQuery("SELECT token FROM IsagTokens token ");
        return returnListDTO(isagTokenList);
    }

    @Override
    public List<String> getAllTokan() {
        List<String> tokenList = dao.findListByQuery("SELECT token.token FROM IsagTokens token ");
        return tokenList;
    }

    @Override
    public List<String> getAllTokanOpen() {
        List<String> tokenList = dao.findListByQuery("SELECT token.token FROM IsagTokens token WHERE token.active = true");
        return tokenList;
    }

    @Override
    public List<String> getAllTokanByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<String> tokenList = dao.findListByQuery("SELECT token.token FROM IsagToken token WHERE token.branchId.id = :branchId");
        return tokenList;
    }

    @Override
    public List<IsagUserDTO> getAllIsagUserDTO() {
        List<IsagTokens> isagTokensList = dao.findListByQuery("SELECT token FROM IsagTokens token ");
        List<IsagUserDTO> isagUserDTOList = new ArrayList<>();
        if (isagTokensList != null && !isagTokensList.isEmpty()) { 
            for (IsagTokens isagTokens : isagTokensList) {
                IsagUserDTO isagUserDTO = new IsagUserDTO();
                isagUserDTO.setName(isagTokens.getCreatedBy().getName());
                isagUserDTO.setId(isagTokens.getCreatedBy().getId());
                isagUserDTO.setToken(isagTokens.getToken());
                isagUserDTOList.add(isagUserDTO);
            }
        }
        return isagUserDTOList;
    }

    private List<IsagTokensDTO> returnListDTO(List<IsagTokens> list) {
        List<IsagTokensDTO> dTOList = new ArrayList<>();
        for (IsagTokens entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private IsagTokensDTO mapTODTO(IsagTokens entity, Boolean needDetail) {
        IsagTokensDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private IsagTokensDTO initMapToDTO(IsagTokens entity) {
        IsagTokensDTO dTO = new IsagTokensDTO();
        dTO.setId(entity.getId());
        dTO.setToken(entity.getToken());
        dTO.setIndex(entity.getId());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        dTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setActive(entity.getActive());
        return dTO;
    }

    private IsagTokens mapFromDTO(IsagTokensDTO dTO, IsagUser isagUser) {
        IsagTokens entity = new IsagTokens();
        entity.setToken(dTO.getToken());
        entity.setActive(dTO.getActive());

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
}
