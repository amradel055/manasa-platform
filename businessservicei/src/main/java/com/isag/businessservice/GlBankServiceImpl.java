/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.GlBankEntitySearch;
import com.isag.core.GenericDAO;
import com.isag.dto.CurrencyDTO;
import com.isag.dto.GlAccountDTO;
import com.isag.dto.GlBankDTO;
import com.isag.entity.Currency;
import com.isag.entity.GlAccount;
import com.isag.entity.GlBank;
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

    @Override
    public BigDecimal findGlBankOpeninngBalance(Integer branchId, Integer bankId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("bankId", bankId);
        String sql = "SELECT gb.openinngBalance FROM GlBank gb WHERE gb.branchId.id = :branchId and gb.id = :bankId";

        return dao.findEntityByQuery(sql, params);
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

    @Override
    public GlBankDTO addGlBank(GlBankDTO glBankDTO, IsagUser isagUser) {
        GlBank glBank = mapToEntity(glBankDTO, isagUser);
        glBank = dao.updateEntity(glBank);
        return mapTODTO(glBank);
    }      

    @Override
    public List<GlBankDTO> getAllGlBankDTOByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<GlBank> glBankList = dao.findListByQuery("SELECT e FROM GlBank e WHERE e.branchId.id = :branchId ", params);
        return returnListDTO(glBankList);
    }

    private List<GlBankDTO> returnListDTO(List<GlBank> list) {
        List<GlBankDTO> dTOList = new ArrayList<>();
        for (GlBank entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private GlBankDTO mapTODTO(GlBank entity) {
        GlBankDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private GlBankDTO initMapToDTO(GlBank entity) {

        GlBankDTO dTO = new GlBankDTO();
        dTO.setId(entity.getId());

        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());

        CurrencyDTO currencyDTO = new CurrencyDTO();
        GlAccountDTO glAccountDTO = new GlAccountDTO();

        if (entity.getCurrencyId() != null) {
            currencyDTO.setId(entity.getCurrencyId().getId());
            currencyDTO.setCode(entity.getCurrencyId().getCode());
            currencyDTO.setName(entity.getCurrencyId().getName());
            dTO.setCurrencyId(entity.getCurrencyId().getId());
            dTO.setCurrency(currencyDTO);
        }

        if (entity.getAccountId() != null) {
            glAccountDTO.setId(entity.getAccountId().getId());
            glAccountDTO.setAccNumber(entity.getAccountId().getAccNumber());
            glAccountDTO.setName(entity.getAccountId().getName());
            dTO.setGlAccountId(glAccountDTO);
        }

        dTO.setDateOpeninngBalance(entity.getDateOpeninngBalance());
        dTO.setOpeninngBalance(entity.getOpeninngBalance());
        dTO.setType(entity.getType());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private GlBank mapToEntity(GlBankDTO dTO, IsagUser isagUser) {
        GlBank entity = new GlBank();
        Currency currency = new Currency();
        GlAccount glAccount = new GlAccount();

        if (dTO.getCurrencyId() != null) {
            currency.setId(dTO.getCurrencyId());
            entity.setCurrencyId(currency);
        }

        if (dTO.getAccountId() != null) {
            glAccount.setId(dTO.getAccountId().getId());
            entity.setAccountId(glAccount);
        }

        entity.setDateOpeninngBalance(dTO.getDateOpeninngBalance());
        entity.setOpeninngBalance(dTO.getOpeninngBalance());
        entity.setType(dTO.getType());
        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());

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
    
    @Override
    public List<GlBankDTO> findGlBankDTOList(StringBuilder stringBuilder) {
        StringBuilder sql = new StringBuilder(); 
        sql.append("SELECT e FROM GlBank e WHERE e.id  in ( ");
        sql.append(stringBuilder);
        sql.append(" )");        
        List<GlBank> glBankList = dao.findListByQuery(sql.toString());
        return returnListDTO(glBankList);
    }

}
