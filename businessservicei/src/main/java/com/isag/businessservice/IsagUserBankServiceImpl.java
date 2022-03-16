/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GlBankDTO;
import com.isag.entity.GlBank;
import com.isag.entity.IsagUser;
import com.isag.entity.IsagUserBank;
import com.isag.entity.IsagUserRole;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class IsagUserBankServiceImpl implements IsagUserBankService {

    @EJB
    private GenericDAO dao;

    @EJB
    IsagUserBankService isagUserBankService;

    @Override
    public List<GlBank> getAllGlBankForUserByTypeAndBranchId(Integer userId, Integer branchId, Integer type) {

        List<GlBank> glBankList = new ArrayList<>();

        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
//        params.put("type", type);

        List<IsagUserBank> isagUserBankList = dao.findListByQuery("SELECT i FROM IsagUserBank i "
                + "LEFT JOIN "
                + "i.bankId b "
                + "WHERE i.userId.id =:userId AND i.branchId.id = :branchId", params);

        for (IsagUserBank isagUserBank : isagUserBankList) {
            glBankList.add(isagUserBank.getBankId());
        }

        return glBankList;
    }
    @Override
    public List<Integer> getAllGlBankIdForUserByTypeAndBranchId(Integer userId, Integer branchId, Integer type) {

        List<Integer> glBankIdList = new ArrayList<>();

        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
//        params.put("type", type);

        List<IsagUserBank> isagUserBankList = dao.findListByQuery("SELECT i FROM IsagUserBank i "
                + "LEFT JOIN "
                + "i.bankId b "
                + "WHERE i.userId.id =:userId AND i.branchId.id = :branchId", params);

        for (IsagUserBank isagUserBank : isagUserBankList) {
            glBankIdList.add(isagUserBank.getBankId().getId());
        }

        return glBankIdList;
    }
    
    @Override
    public List<GlBank> getAllGlBankForUserByTypeAndBranchIdPer(Integer userId, Integer branchId, Integer type) {

        List<GlBank> glBankList = new ArrayList<>();

        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
//        params.put("type", type);

        List<IsagUserBank> isagUserBankList = dao.findListByQuery("SELECT i FROM IsagUserBank i "
                + "LEFT JOIN "
                + "i.bankId b "
                + "WHERE i.userId.id =:userId AND i.branchId.id = :branchId", params);

        for (IsagUserBank isagUserBank : isagUserBankList) {
            if(isagUserBank.getBankId() != null){
                GlBank glBank = new GlBank();
                glBank.setId(isagUserBank.getBankId().getId());
                glBank.setName(isagUserBank.getBankId().getName());
                glBank.setCode(isagUserBank.getBankId().getCode());
            }
            glBankList.add(isagUserBank.getBankId());
        }

        return glBankList;
    }

    @Override
    public List<GlBank> getAllGlBankByTypeAndBranchId(Integer branchId, Integer type) {

        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
//        params.put("type", type);

//        "SELECT e FROM GlBank e WHERE e.branchId.id = :branchId";
        return dao.findListByQuery("SELECT e FROM GlBank e WHERE e.branchId.id = :branchId", params);

    }

    @Override
    public List<GlBank> getAllBankListByUserAndBranch(Integer userId, Integer branchId) {

        List<IsagUserBank> isagUserBankList = isagUserBankService.getAllIsagUserBankListByUserAndBranch(userId, branchId);

        List<GlBank> glBanks = new ArrayList<>();

        for (IsagUserBank isagUserBank : isagUserBankList) {
            glBanks.add(isagUserBank.getBankId() != null ? isagUserBank.getBankId() : null);
        }

        return glBanks;
    }

    @Override
    public List<IsagUserBank> getAllIsagUserBankListByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);

        return dao.findListByQuery("SELECT e FROM IsagUserBank e WHERE e.branchId.id = :branchId  AND e.userId.id = :userId", params);
    }
    
    @Override
    public List<IsagUserBank> findAll() {
        return dao.findAll(IsagUserBank.class);
    }

    @Override
    public void deleteGlBank(IsagUserRole userRole, GlBank glBank) {
        String sql = "delete from IsagUserBank e  where e.branchId.id  = " + userRole.getBranchId().getId()
                + " AND e.userId.id = " + userRole.getUserId().getId() + " AND e.bankId.id = " + glBank.getId();
        dao.executeDeleteQuery(sql);
    }

    @Override
    public IsagUserBank updateIsagUserBank(IsagUserBank isagUserBank) {
        return dao.updateEntity(isagUserBank);
    }

    @Override
    public void deleteIsagUserBank(List<IsagUserBank> isagUserBankList) {
        dao.deleteEntity(isagUserBankList);
    }
    
    
    @Override
    public List<GlBankDTO> getAllGlBankForUserByTypeAndBranchIdPerDTO(Integer userId, Integer branchId, Integer type) {

        List<GlBank> glBankList = new ArrayList<>();

        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
//        params.put("type", type);

        List<IsagUserBank> isagUserBankList = dao.findListByQuery("SELECT i FROM IsagUserBank i "
                + "LEFT JOIN "
                + "i.bankId b "
                + "WHERE i.userId.id =:userId AND i.branchId.id = :branchId", params);

        for (IsagUserBank isagUserBank : isagUserBankList) {
            if(isagUserBank.getBankId() != null){
                GlBank glBank = new GlBank();
                glBank.setId(isagUserBank.getBankId().getId());
                glBank.setName(isagUserBank.getBankId().getName());
                glBank.setCode(isagUserBank.getBankId().getCode());
            }
            glBankList.add(isagUserBank.getBankId());
        }

        return returnListDTO(glBankList);
    }
    
    
    private List<GlBankDTO> returnListDTO(List<GlBank> list) {
        List<GlBankDTO> dTOList = new ArrayList<>();
        for (GlBank entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private GlBankDTO mapTODTO(GlBank entity, Boolean needDetail) {
        GlBankDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private GlBankDTO initMapToDTO(GlBank entity) {

        GlBankDTO dTO = new GlBankDTO();
        dTO.setId(entity.getId());

        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setDateOpeninngBalance(entity.getDateOpeninngBalance());
        dTO.setOpeninngBalance(entity.getOpeninngBalance());
        dTO.setAccountId(entity.getAccountId());
        dTO.setCurrencyId(entity.getCurrencyId() != null ? entity.getCurrencyId().getId() : null);


        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    private GlBank mapFromDTO(GlBankDTO dTO, IsagUser isagUser) {
        GlBank entity = new GlBank();

        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());

        if (dTO.getId()
                == null) {
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

        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}
