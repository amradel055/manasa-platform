/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.GlBankDTO;
import com.toby.entity.GlBank;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserBank;
import com.toby.entity.TobyUserRole;
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
public class TobyUserBankServiceImpl implements TobyUserBankService {

    @EJB
    private GenericDAO dao;

    @EJB
    TobyUserBankService tobyUserBankService;

    @Override
    public List<GlBank> getAllGlBankForUserByTypeAndBranchId(Integer userId, Integer branchId, Integer type) {

        List<GlBank> glBankList = new ArrayList<>();

        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
//        params.put("type", type);

        List<TobyUserBank> tobyUserBankList = dao.findListByQuery("SELECT i FROM TobyUserBank i "
                + "LEFT JOIN "
                + "i.bankId b "
                + "WHERE i.userId.id =:userId AND i.branchId.id = :branchId", params);

        for (TobyUserBank tobyUserBank : tobyUserBankList) {
            glBankList.add(tobyUserBank.getBankId());
        }

        return glBankList;
    }

    @Override
    public List<GlBank> getAllGlBankForUserByTypeAndBranchIdPer(Integer userId, Integer branchId, Integer type, Integer gallaryId) {

        List<GlBank> glBankList = new ArrayList<>();

        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
//        params.put("type", type);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM TobyUserBank i LEFT JOIN i.bankId b WHERE i.userId.id =:userId AND i.branchId.id = :branchId ");

        if (gallaryId != null) {
            params.put("gallaryId", gallaryId);
            query.append(" AND i.bankId.gallaryId.id = :gallaryId");
        }

        List<TobyUserBank> tobyUserBankList = dao.findListByQuery(query.toString(), params);

        for (TobyUserBank tobyUserBank : tobyUserBankList) {
            if (tobyUserBank.getBankId() != null) {
                GlBank glBank = new GlBank();
                glBank.setId(tobyUserBank.getBankId().getId());
                glBank.setName(tobyUserBank.getBankId().getName());
                glBank.setCode(tobyUserBank.getBankId().getCode());
            }
            glBankList.add(tobyUserBank.getBankId());
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

        List<TobyUserBank> tobyUserBankList = getAllTobyUserBankListByUserAndBranch(userId, branchId);

        List<GlBank> glBanks = new ArrayList<>();

        for (TobyUserBank tobyUserBank : tobyUserBankList) {
            glBanks.add(tobyUserBank.getBankId() != null ? tobyUserBank.getBankId() : null);
        }

        return glBanks;
    }

    @Override
    public List<TobyUserBank> getAllTobyUserBankListByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);

        return dao.findListByQuery("SELECT e FROM TobyUserBank e WHERE e.branchId.id = :branchId  AND e.userId.id = :userId", params);
    }

    @Override
    public List<TobyUserBank> findAll() {
        return dao.findAll(TobyUserBank.class);
    }

    @Override
    public void deleteGlBank(TobyUserRole userRole, GlBank glBank) {
        String sql = "delete from TobyUserBank e  where e.branchId.id  = " + userRole.getBranchId().getId()
                + " AND e.userId.id = " + userRole.getUserId().getId() + " AND e.bankId.id = " + glBank.getId();
        dao.executeDeleteQuery(sql);
    }

    @Override
    public TobyUserBank updateTobyUserBank(TobyUserBank tobyUserBank) {
        return dao.updateEntity(tobyUserBank);
    }

    @Override
    public void deleteTobyUserBank(List<TobyUserBank> tobyUserBankList) {
        dao.deleteEntity(tobyUserBankList);
    }

    @Override
    public List<GlBankDTO> getAllGlBankDTOByUserAndBranch(TobyUser tobyUser) {
        List<TobyUserBank> tobyUserBankList = getAllTobyUserBankListByUserAndBranch(tobyUser.getId(), tobyUser.getBranchId().getId());

        List<GlBankDTO> glBankDTOList = new ArrayList<>();

        GlBankServiceImpl glBankServiceImpl = new GlBankServiceImpl();
        for (TobyUserBank tobyUserBank : tobyUserBankList) {
            glBankDTOList.add(glBankServiceImpl.mapToDTO(tobyUserBank.getBankId()));
        }
        return glBankDTOList;
    }

    @Override
    public List<GlBankDTO> getAllGlBankForUserByTypeAndBranchIdPerDTO(Integer userId, Integer branchId, Integer type, Integer gallaryId) {

        List<GlBank> glBankList = new ArrayList<>();

        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
//        params.put("type", type);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM TobyUserBank i LEFT JOIN i.bankId b WHERE i.userId.id =:userId AND i.branchId.id = :branchId ");

        if (gallaryId != null) {
            params.put("gallaryId", gallaryId);
            query.append(" AND i.bankId.gallaryId.id = :gallaryId");
        }

        List<TobyUserBank> tobyUserBankList = dao.findListByQuery(query.toString(), params);

        for (TobyUserBank tobyUserBank : tobyUserBankList) {
            if (tobyUserBank.getBankId() != null) {
                GlBank glBank = new GlBank();
                glBank.setId(tobyUserBank.getBankId().getId());
                glBank.setName(tobyUserBank.getBankId().getName());
                glBank.setCode(tobyUserBank.getBankId().getCode());
            }
            glBankList.add(tobyUserBank.getBankId());
        }

        return returnListDTO(glBankList);
    }

    @Override
    public List<GlBankDTO> getAllGlBankForUserByTypeAndBranchIdAndGallaryListPerDTO(Integer userId, Integer branchId, Integer type, String gallaryId) {

        List<GlBank> glBankList = new ArrayList<>();

        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
//        params.put("type", type);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM TobyUserBank i LEFT JOIN i.bankId b WHERE i.userId.id =:userId AND i.branchId.id = :branchId ");

        if (gallaryId != null) {
            query.append(" AND i.bankId.gallaryId.id in ( ").append(gallaryId).append(" ) ");
        }

        List<TobyUserBank> tobyUserBankList = dao.findListByQuery(query.toString(), params);

        for (TobyUserBank tobyUserBank : tobyUserBankList) {
            if (tobyUserBank.getBankId() != null) {
                GlBank glBank = new GlBank();
                glBank.setId(tobyUserBank.getBankId().getId());
                glBank.setName(tobyUserBank.getBankId().getName());
                glBank.setCode(tobyUserBank.getBankId().getCode());
            }
            glBankList.add(tobyUserBank.getBankId());
        }

        return returnListDTO(glBankList);
    }

    @Override
    public List<Integer> getAllGlBankIdForUserByTypeAndBranchId(Integer userId, Integer branchId, Integer type) {

        List<Integer> glBankIdList = new ArrayList<>();

        Map<String, Object> params = new HashMap();
        params.put("userId", userId);
        params.put("branchId", branchId);
//        params.put("type", type);

        List<TobyUserBank> tobyUserBankList = dao.findListByQuery("SELECT i FROM TobyUserBank i "
                + "LEFT JOIN "
                + "i.bankId b "
                + "WHERE i.userId.id =:userId AND i.branchId.id = :branchId", params);

        for (TobyUserBank tobyUserBank : tobyUserBankList) {
            glBankIdList.add(tobyUserBank.getBankId().getId());
        }

        return glBankIdList;
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
        dTO.setAccountId(entity.getAccountId().getId());
        dTO.setCurrencyId(entity.getCurrencyId().getId());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    private GlBank mapFromDTO(GlBankDTO dTO, TobyUser tobyUser) {
        GlBank entity = new GlBank();

        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());

        if (dTO.getId()
                == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }
}
