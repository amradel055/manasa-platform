/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IgmaTokensDTO;
import com.isag.dto.IgmaUserDTO;
import com.isag.entity.IgmaTokens;
import com.isag.entity.IgmaUser;
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
public class IgmaTokensImp implements IgmaTokensService {

    @EJB
    private GenericDAO dao;

    @Override
    public IgmaTokensDTO addIgmaToken(IgmaTokensDTO isagTokensDTO, IgmaUser isagUser) {
        IgmaTokens isagTokens = mapFromDTO(isagTokensDTO, isagUser);
        isagTokens = dao.updateEntity(isagTokens);
        return mapTODTO(isagTokens, Boolean.FALSE);
    }

    @Override
    public IgmaTokensDTO addIgmaTokenAlreadyExist(IgmaTokensDTO isagTokensDTO, IgmaUser isagUser) {
        IgmaTokensDTO itdto = getAllIgmaTokenDTO(isagUser);
        itdto.setId(itdto.getId());
        itdto.setActive(Boolean.TRUE);
        itdto.setToken(isagTokensDTO.getToken());
        return addIgmaToken(itdto, isagUser);
    }

    @Override
    public void deleteIgmaTokenDTO(IgmaTokensDTO isagTokensDTO, IgmaUser isagUser) {
        IgmaTokens isagToken = mapFromDTO(isagTokensDTO, isagUser);
        dao.deleteEntity(isagToken);
    }

    @Override
    public void deleteAllIgmaTokenDTO(IgmaTokensDTO isagTokensDTO) {
        dao.executeDeleteQuery("DELETE token FROM IgmaTokens token WHERE token.id = " + isagTokensDTO.getId());
    }

    @Override
    public void UpdateActiveAllIgmaTokenDTO(Boolean active) {
       dao.executeUpdateQuery("UPDATE  IgmaTokens token SET  token.active = " + active);
    }

    @Override
    public void UpdateActiveIgmaTokenDTObyUserID(Integer userId, Boolean active) {
        dao.executeUpdateQuery("UPDATE IgmaTokens token SET  token.active = " + active + " WHERE token.createdBy.id = " + userId);
    }

    @Override
    public List<IgmaTokensDTO> getAllIgmaTokenDTO(Integer branchid) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", branchid);
        List<IgmaTokens> isagTokenList = dao.findListByQuery("SELECT token FROM IgmaTokens token WHERE token.branchId.id = :branchid", params);
        return returnListDTO(isagTokenList);
    }

    @Override
    public IgmaTokensDTO getAllIgmaTokenDTO(IgmaUser isagUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", isagUser.getBranchId().getId());
        params.put("createdBy", isagUser.getId());
        List<IgmaTokens> isagTokenList = dao.findListByQuery("SELECT token FROM IgmaTokens token WHERE token.branchId.id = :branchid AND token.createdBy.id = :createdBy", params);
        return initMapToDTO(isagTokenList != null && !isagTokenList.isEmpty() ? isagTokenList.get(0) : new IgmaTokens());
    }

    @Override
    public List<IgmaTokensDTO> getAllIgmaTokenDTObyActive(IgmaUser isagUser, Boolean active) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", isagUser.getBranchId().getId());
        params.put("active", active);
        List<IgmaTokens> isagTokenList = dao.findListByQuery("SELECT token FROM IgmaTokens token WHERE token.branchId.id = :branchid AND token.active = :active", params);
        return returnListDTO(isagTokenList);
    }

    @Override
    public List<IgmaTokensDTO> getAllIgmaTokenDTO() {
        List<IgmaTokens> isagTokenList = dao.findListByQuery("SELECT token FROM IgmaTokens token ");
        return returnListDTO(isagTokenList);
    }

    @Override
    public List<String> getAllTokan() {
        List<String> tokenList = dao.findListByQuery("SELECT token.token FROM IgmaTokens token ");
        return tokenList;
    }

    @Override
    public List<String> getAllTokanOpen() {
        List<String> tokenList = dao.findListByQuery("SELECT token.token FROM IgmaTokens token WHERE token.active = true");
        return tokenList;
    }

    @Override
    public List<String> getAllTokanByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<String> tokenList = dao.findListByQuery("SELECT token.token FROM IgmaToken token WHERE token.branchId.id = :branchId");
        return tokenList;
    }

    @Override
    public List<IgmaUserDTO> getAllIgmaUserDTO() {
        List<IgmaTokens> isagTokensList = dao.findListByQuery("SELECT token FROM IgmaTokens token ");
        List<IgmaUserDTO> isagUserDTOList = new ArrayList<>();
        if (isagTokensList != null && !isagTokensList.isEmpty()) { 
            for (IgmaTokens isagTokens : isagTokensList) {
                IgmaUserDTO isagUserDTO = new IgmaUserDTO();
                isagUserDTO.setName(isagTokens.getCreatedBy().getName());
                isagUserDTO.setId(isagTokens.getCreatedBy().getId());
                isagUserDTO.setToken(isagTokens.getToken());
                isagUserDTOList.add(isagUserDTO);
            }
        }
        return isagUserDTOList;
    }

    private List<IgmaTokensDTO> returnListDTO(List<IgmaTokens> list) {
        List<IgmaTokensDTO> dTOList = new ArrayList<>();
        for (IgmaTokens entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private IgmaTokensDTO mapTODTO(IgmaTokens entity, Boolean needDetail) {
        IgmaTokensDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private IgmaTokensDTO initMapToDTO(IgmaTokens entity) {
        IgmaTokensDTO dTO = new IgmaTokensDTO();
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

    private IgmaTokens mapFromDTO(IgmaTokensDTO dTO, IgmaUser isagUser) {
        IgmaTokens entity = new IgmaTokens();
        entity.setToken(dTO.getToken());
        entity.setActive(dTO.getActive());

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IgmaUser isagUser1 = new IgmaUser();
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
