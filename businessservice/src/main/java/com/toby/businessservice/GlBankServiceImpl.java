/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.GlBankEntitySearch;
import com.toby.core.GenericDAO;
import com.toby.dto.GlBankDTO;
import com.toby.entity.Branch;
import com.toby.entity.Currency;
import com.toby.entity.GlAccount;
import com.toby.entity.GlBank;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
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
 * @author hq002
 */
@Stateless
public class GlBankServiceImpl implements GlBankService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<GlBank> getAllGlBank() {
        return dao.findAll(GlBank.class);
    }

    @Override
    public void deleteGlBank(GlBank glBank) {
        dao.deleteEntity(glBank);
    }

    @Override
    public GlBank updateGlBank(GlBank glBank) {
        return dao.updateEntity(glBank);
    }

    @Override
    public GlBank addGlBank(GlBank glBank) {
        if (glBank.getId() == null) {
            dao.saveEntity(glBank);
        } else {
            return dao.updateEntity(glBank);
        }
        return glBank;
    }

    @Override
    public GlBank findGlBank(Integer id) {
        return dao.findEntityById(GlBank.class, id);
    }

    @Override
    public List<GlBank> getAllGlBankByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<GlBank> glBankList = dao.findListByQuery("SELECT e FROM GlBank e WHERE e.companyId.id = :companyId ", params);
        return glBankList;
    }

    @Override
    public List<GlBank> getAllGlBankByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<GlBank> glBankList = dao.findListByQuery("SELECT e FROM GlBank e WHERE e.branchId.id = :branchId ", params);
        return glBankList;
    }

    @Override
    public List<GlBank> getGlBankByBranchIdAndCode(Integer branchId, String code, Integer bankId) {
        Map<String, Object> params = new HashMap<>();
        List<GlBank> glBanks = null;
        String query = null;
        params.put("branchId", branchId);
        params.put("code", code);

        if (bankId == null) {
            query = "SELECT e FROM GlBank e WHERE e.branchId.id = :branchId AND e.code = :code";
        } else {
            params.put("bankId", bankId);
            query = "SELECT e FROM GlBank e WHERE e.id != :bankId AND e.branchId.id = :branchId AND e.code = :code";
        }

        glBanks = dao.findListByQuery(query, params);

        return glBanks;
    }

    @Override
    public List<GlBank> findAllGlBankForReport(GlBankEntitySearch glBankEntitySearch) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", glBankEntitySearch.getBranchId());
        String sql = "SELECT gb FROM GlBank gb WHERE gb.branchId.id = :branchId " + createSql(glBankEntitySearch, params);

        return dao.findListByQuery(sql, params);
    }

    private String createSql(GlBankEntitySearch glBankEntitySearch, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        addGlBankFromAndTo(sql, params, glBankEntitySearch);
        addCurrencyFromAndTo(sql, params, glBankEntitySearch);
        addAccountFromAndTo(sql, params, glBankEntitySearch);
        addType(sql, params, glBankEntitySearch);
        return sql.toString();
    }

    private void addGlBankFromAndTo(StringBuilder sql, Map<String, Object> params, GlBankEntitySearch glBankEntitySearch) {
        if (glBankEntitySearch.getBankFrom() != null && glBankEntitySearch.getBankFrom().getId() > 0) {
            sql.append(" and gb.id >= :glBankFrom");
            params.put("glBankFrom", glBankEntitySearch.getBankFrom().getId());
        }

        if (glBankEntitySearch.getBankTo() != null && glBankEntitySearch.getBankTo().getId() > 0) {
            sql.append(" and gb.id <= :glBankTo");
            params.put("glBankTo", glBankEntitySearch.getBankTo().getId());
        }
    }

    private void addCurrencyFromAndTo(StringBuilder sql, Map<String, Object> params, GlBankEntitySearch glBankEntitySearch) {
        if (glBankEntitySearch.getCurrencyTypeFrom() != null && glBankEntitySearch.getCurrencyTypeFrom().getId() > 0) {
            sql.append(" and gb.currencyId.id >= :currencyFrom");
            params.put("currencyFrom", glBankEntitySearch.getCurrencyTypeFrom().getId());
        }

        if (glBankEntitySearch.getCurrencyTypeTo() != null && glBankEntitySearch.getCurrencyTypeTo().getId() > 0) {
            sql.append(" and gb.currencyId.id <= :currencyTo");
            params.put("currencyTo", glBankEntitySearch.getCurrencyTypeTo().getId());
        }
    }

    private void addAccountFromAndTo(StringBuilder sql, Map<String, Object> params, GlBankEntitySearch glBankEntitySearch) {
        if (glBankEntitySearch.getAccountFrom() != null && glBankEntitySearch.getAccountFrom().getId() > 0) {
            sql.append(" and gb.accountId.id >= :accountFrom");
            params.put("accountFrom", glBankEntitySearch.getAccountFrom().getId());
        }

        if (glBankEntitySearch.getAccountTo() != null && glBankEntitySearch.getAccountTo().getId() > 0) {
            sql.append(" and gb.accountId.id <= :accountTo");
            params.put("accountTo", glBankEntitySearch.getAccountTo().getId());
        }
    }

    private void addType(StringBuilder sql, Map<String, Object> params, GlBankEntitySearch glBankEntitySearch) {
        if (glBankEntitySearch.getType() != null && glBankEntitySearch.getType() != 3) {
            sql.append(" and gb.type = :type");
            params.put("type", glBankEntitySearch.getType());
        }
    }
    
    //mapToEntity
    public GlBank mapToEntity(GlBankDTO glBankDTO, TobyUser tobyUser) {
        
        GlBank glBank = new GlBank();
        glBank.setCode(glBankDTO.getCode());
        glBank.setCode(glBankDTO.getCode());
        glBank.setId(glBankDTO.getId());
        glBank.setMarkDisable(glBankDTO.getMarkDisable());
        glBank.setMarkEdit(glBankDTO.getMarkEdit());
        glBank.setName(glBankDTO.getName());
        glBank.setName(glBankDTO.getName());
        glBank.setCode(glBankDTO.getCode());
        glBank.setDateOpeninngBalance(glBankDTO.getDateOpeninngBalance());
        glBank.setName(glBankDTO.getName());
        glBank.setOpeninngBalance(glBankDTO.getOpeninngBalance());
        glBank.setType(glBankDTO.getType());
        if (glBankDTO.getAccountId()!=null){
            GlAccount glAccount =new GlAccount();
            glAccount.setId(glBankDTO.getAccountId());
            glBank.setAccountId(glAccount);
        }
        if (glBankDTO.getCurrencyId()!=null){
            Currency currency =new Currency();
            currency.setId(glBankDTO.getCurrencyId());
            glBank.setCurrencyId(currency);
        }
        if (glBank.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(glBankDTO.getCreatedBy());
            glBank.setCreatedBy(user);
            glBank.setCreationDate(glBankDTO.getCreatedDate());
            glBank.setModifiedBy(tobyUser);
            glBank.setModificationDate(new Date());
            if (glBank.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(glBankDTO.getCompanyId());
                glBank.setCompanyId(company);
            }

            if (glBankDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(glBankDTO.getBranchId());
                glBank.setBranchId(branch);
            }
        } else {
            glBank.setCreatedBy(tobyUser);
            glBank.setCreationDate(new Date());
            glBank.setCompanyId(tobyUser.getCompanyId());
            glBank.setBranchId(tobyUser.getBranchId());
        }

        return glBank;
    }
    
    
    

    public GlBankDTO mapToDTO(GlBank glBank) {
        
        GlBankDTO glBankDTO = new GlBankDTO();
        glBankDTO.setCreatedBy(glBank.getCreatedBy() !=null ? glBank.getCreatedBy().getId() : null);
        glBankDTO.setCreatedDate(glBank.getCreationDate());
        glBankDTO.setBranchId(glBank.getBranchId() !=null ? glBank.getBranchId().getId() : null );
        glBankDTO.setId(glBank.getId());
        glBankDTO.setCompanyId(glBank.getCompanyId() !=null ? glBank.getCompanyId().getId() : null );
        glBankDTO.setMarkDisable(glBank.getMarkDisable());
        glBankDTO.setMarkEdit(glBank.getMarkEdit());
        glBankDTO.setName(glBank.getName());
        glBankDTO.setCode(glBank.getCode());
        glBankDTO.setMsg(glBank.getMsg());
        glBankDTO.setType(glBank.getType());
        
        
        if (glBank.getAccountId()!=null){
            GlAccount glAccount =new GlAccount();
            glAccount.setId(glBank.getAccountId().getId());
            glBankDTO.setAccountId(glAccount.getId());
        }
        if (glBank.getCurrencyId()!=null){
            Currency currency =new Currency();
            currency.setId(glBank.getCurrencyId().getId());
            glBankDTO.setCurrencyId(currency.getId());
        }
        
        
        return glBankDTO;
    }

    public List<GlBankDTO> mapToDTOList(List<GlBank> glBankList, TobyUser tobyUser) {
        List<GlBankDTO> glBankDTOList = new ArrayList<>();
        for (GlBank glAdminUnit : glBankList) {
            glBankDTOList.add(mapToDTO(glAdminUnit));
        }
        return glBankDTOList;
    }

    @Override
    public List<GlBankDTO> getAllGlBankDTOByBranchId(TobyUser tobyUser) {
       Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<GlBank> glBankList = dao.findListByQuery("SELECT e FROM GlBank e WHERE e.branchId.id = :branchId ", params);
        return mapToDTOList(glBankList,tobyUser);      
        
    }

    @Override
    public BigDecimal findGlBankOpeninngBalance(Integer branchId , Integer bankId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("bankId", bankId);
        String sql = "SELECT gb.openinngBalance FROM GlBank gb WHERE gb.branchId.id = :branchId and gb.id = :bankId"  ;

        return dao.findEntityByQuery(sql, params);
    }
}
