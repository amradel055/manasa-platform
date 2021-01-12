/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.GeneralSymbol;
import com.toby.entity.GlAccount;
import com.toby.entity.TobyUser;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
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
public class SymbolServiceImpl implements SymbolService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<Symbol> getAllSymbol() {
        return dao.findAll(Symbol.class);
    }

    @Override
    public Symbol addSymbol(Symbol Symbol) {
        return dao.updateEntity(Symbol);
    }

    @Override
    public Symbol updateSymbol(Symbol Symbol) {
        return dao.updateEntity(Symbol);
    }

    @Override
    public void deleteSymbol(Symbol Symbol) {
        dao.deleteEntity(Symbol);
    }

    @Override
    public List<Symbol> getAllLanguagesListByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Symbol> langs = dao.findListByQuery("SELECT cl.symbol FROM CompanyLanguage cl WHERE cl.companyId.id=:companyId", params);
        return langs;
    }

    @Override
    public List<Symbol> getLanguagesWhereCompanyNull() {
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE sym.companyId IS NULL AND sym.generalSymbolId.serial=0");
    }

//    @Override
//    public List<Symbol> getSymbolForCompanyAndSymbol(Integer selectedCompany, Integer serial) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("generalSymbolSerial", serial);
//        if (selectedCompany != null) {
//            params.put("companyId", selectedCompany);
//            return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE sym.generalSymbolId.serial "
//                    + "= :generalSymbolSerial AND sym.companyId.id = :companyId", params);
//        } else {
//            return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE sym.generalSymbolId.serial "
//                    + "= :generalSymbolSerial AND sym.companyId.id IS NULL", params);
//        }
//    }
//
//    @Override
//    public List<Symbol> getLanguagesForCompany(Integer companyId) {
//        if (companyId != null) {
//            Map<String, Object> params = new HashMap<>();
//            params.put("companyId", companyId);
//            return dao.findListByQuery("SELECT companyLang.symbol FROM CompanyLanguage "
//                    + "companyLang WHERE companyLang.companyId.id = :companyId", params);
//        } else {
//            return dao.findListByQuery("SELECT symbol FROM Symbol "
//                    + "symbol WHERE symbol.generalSymbolId.serial = 1");
//        }
//    }
//
//    @Override
//    public CompanyLanguage saveCompanyLanguage(CompanyLanguage companyLanguage) {
//        return dao.updateEntity(companyLanguage);
//    }
//    @Override
    public List<Symbol> getAllSupportedLanguages() {
        return dao.findListByQuery("SELECT lang FROM Symbol lang WHERE lang.companyId IS NULL AND lang.generalSymbolId.serial = 0");
    }

    @Override
    public List<Symbol> getDocumentsTypes(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Symbol> symbolList = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=1 AND sym.companyId.id=:companyId AND (sym.serial < 1000 OR sym.serial > 2000) order by (CASE WHEN sym.arrange IS NULL THEN 999999999999 ELSE 0 END) , sym.arrange", params);
        return symbolList;

    }

    @Override
    public List<Symbol> getSpecialDocumentsTypes() {
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=1 AND sym.serial >= 1000 AND sym.serial <= 2000 ");
    }

    @Override
    public Symbol getSymbol(Integer SymbolId) {
        return dao.findEntityById(Symbol.class, SymbolId);
    }

    @Override
    public List<Symbol> getRegion(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=7 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getOrganizationSiteType(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=24 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getSupplierType(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=25 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getContractorType(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=40 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getUnitsByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 3 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getEnamelColorByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 28 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getPaintColorByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 29 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getStoneByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 27 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getAddon1ByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 34 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getAddon2ByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 35 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getCountryByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 31 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getBrandByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 30 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getTypeCatByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 12 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getSymbol(Integer companyId, Integer symbolId, Integer serial) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("symbolId", symbolId);
        params.put("serial", serial);

        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE sym.companyId.id = :companyId AND sym.id != :symbolId And sym.serial = :serial", params);
    }

    @Override
    public List<Symbol> getSymbolByGeneralSymbol(Integer companyId, Integer symbolId, Integer serial, Integer generalSymbolId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("symbolId", symbolId);
        params.put("serial", serial);
        params.put("generalSymbolId", generalSymbolId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE sym.companyId.id = :companyId AND sym.id != :symbolId And sym.serial = :serial AND sym.generalSymbolId.id = :generalSymbolId", params);
    }

    @Override
    public List<Symbol> getSymbolBySerial(Integer companyId, Integer serial) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("serial", serial);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE sym.companyId.id = :companyId And sym.serial = :serial", params);
    }

    @Override
    public List<Symbol> getSymbolBySerialByGeneralSymbol(Integer companyId, Integer serial, Integer generalSymbolId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("serial", serial);
        params.put("generalSymbolId", generalSymbolId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE sym.companyId.id = :companyId And sym.serial = :serial AND sym.generalSymbolId.id = :generalSymbolId", params);
    }

    @Override
    public List<Symbol> getTheOpeningBalanceId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE sym.companyId.id = :companyId And sym.generalSymbolId.id = 6 And sym.serial = 0", params);
    }

    @Override
    public List<Symbol> getAllDocumentsByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 6 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getAllNationalitiesByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 4 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public Symbol getOneSymbolBySerial(Integer companyId, Integer serial) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("serial", serial);
        String sql = "SELECT sym FROM Symbol sym WHERE sym.companyId.id = :companyId And sym.serial = :serial";
        List<Symbol> symbolList = dao.findListByQuery(sql, params);
        if (symbolList != null && !symbolList.isEmpty() && symbolList.size() > 0) {
            return symbolList.get(0);
        }
        return null;
    }

    @Override
    public Symbol findSymbol(Integer id) {
        return dao.findEntityById(Symbol.class, id);
    }

    @Override
    public List<Symbol> getprojectManagerByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 37 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getprofessionalBusinessConsultantByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 39 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getgeneralConsultantByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 38 AND sym.companyId.id=:companyId", params);
    }

    @Override
    public List<Symbol> getGLItemsByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 41 AND sym.companyId.id=:companyId", params);

    }

    @Override
    public List<SymbolDTO> getJobGradeByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=17 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;

    }

    @Override
    public List<SymbolDTO> getReligionsCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=45 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;

    }

    @Override
    public List<SymbolDTO> getMaritalStatusCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=42 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;

    }

    @Override
    public List<SymbolDTO> getUnitCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=3 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;

    }

    @Override
    public List<SymbolDTO> getDesignationByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=43 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;

    }

    @Override
    public List<SymbolDTO> getGenderByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=44 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;

    }

    @Override
    public List<SymbolDTO> getNationalityByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 4 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<SymbolDTO> getRegionByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=7 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<SymbolDTO> getEducationLevelByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=46 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<SymbolDTO> getRelationByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=47 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<SymbolDTO> getPlaceBirthByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=36 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public SymbolDTO mapTODTO(Symbol entity, SymbolDTO dTO) {
        if (dTO == null) {
            dTO = new SymbolDTO();
        }
        initMapToDTO(dTO, entity);

        return dTO;
    }

    private void initMapToDTO(SymbolDTO dTO, Symbol entity) {

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        if (entity.getAccountId() != null) {
            dTO.setAccountId(entity.getAccountId().getId());
        }
        dTO.setSerial(entity.getSerial());
        dTO.setArrange(entity.getArrange());
        dTO.setName(entity.getName());
        dTO.setId(entity.getId());

    }

    public Symbol mapFromDTO(SymbolDTO dTO, TobyUser tobyUser) {
        Symbol entity = new Symbol();
        entity.setName(dTO.getName());
        entity.setSerial(dTO.getSerial());
        entity.setArrange(dTO.getArrange());
        if (dTO.getAccountId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(dTO.getAccountId());
            entity.setAccountId(glAccount);
        }

        if (dTO.getId() == null) {
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

    private void returnListDTO(List<Symbol> list, List<SymbolDTO> dTOList) {
        for (Symbol entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    //goher
    public Symbol mapToEntity(SymbolDTO symbolDTO, TobyUser tobyUser) {
        Symbol symbol = new Symbol();

        symbol.setId(symbolDTO.getId());
        symbol.setName(symbolDTO.getName());
        symbol.setSerial(symbolDTO.getSerial());
        symbol.setArrange(symbolDTO.getArrange());
        if (symbolDTO.getAccountId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(symbolDTO.getAccountId());
            symbol.setAccountId(glAccount);
        }
        if (symbolDTO.getGeneralSymbolId() != null) {
            GeneralSymbol generalSymbol = new GeneralSymbol();
            generalSymbol.setId(symbolDTO.getGeneralSymbolId());
            symbol.setGeneralSymbolId(generalSymbol);
        }
        if (symbolDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(symbolDTO.getCreatedBy());
            symbol.setCreatedBy(user);
            symbol.setCreationDate(symbolDTO.getCreatedDate());
            symbol.setModifiedBy(tobyUser);
            symbol.setModificationDate(new Date());
            if (symbolDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(symbolDTO.getCompanyId());
                symbol.setCompanyId(company);
            }

        } else {
            symbol.setCreatedBy(tobyUser);
            symbol.setCreationDate(new Date());
            symbol.setCompanyId(tobyUser.getCompanyId());

        }

        return symbol;
    }

    public SymbolDTO mapToDTO(Symbol symbol) {

        SymbolDTO dTO = new SymbolDTO();

        dTO.setId(symbol.getId());
        dTO.setAccountId(symbol.getAccountId() != null ? symbol.getAccountId().getId() : null);
        dTO.setArrange(symbol.getArrange());
        dTO.setSerial(symbol.getSerial());
        dTO.setName(symbol.getName());

        return dTO;

    }

    public List<SymbolDTO> mapToList(List<Symbol> SymbolList, TobyUser tobyUser) {

        List<SymbolDTO> symbolDTOList = new ArrayList<>();
        for (Symbol symbol : SymbolList) {
            symbolDTOList.add(mapToDTO(symbol));

        }
        return symbolDTOList;

    }

    @Override
    public List<SymbolDTO> getDocumentsTypesDTO(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Symbol> symbolList = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=1 AND sym.companyId.id=:companyId AND (sym.serial < 1000 OR sym.serial > 2000) order by (CASE WHEN sym.arrange IS NULL THEN 999999999999 ELSE 0 END) , sym.arrange", params);
        List<SymbolDTO> dTOList = new ArrayList();
        returnListDTO(symbolList, dTOList);
        return dTOList;

    }

    @Override
    public SymbolDTO getSymbolDTO(Integer SymbolId) {
        return mapTODTO(dao.findEntityById(Symbol.class, SymbolId), null);
    }

    @Override
    public List<SymbolDTO> getAllStoreRegionDTOByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Symbol> detail = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=53 AND sym.companyId.id=:companyId", params);
        List<SymbolDTO> dTOList = new ArrayList();
        returnListDTO(detail, dTOList);
        return dTOList;
    }

    @Override
    public List<SymbolDTO> getUnits() {
        Map<String, Object> params = new HashMap<>();
        BigDecimal generalsymbolid = new BigDecimal(3);
        params.put("generalsymbolid", generalsymbolid);
        List<Symbol> detail = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id=generalsymbolid ", params);
        return mapToDTOList(detail);

    }

    public List<SymbolDTO> mapToDTOList(List<Symbol> symbolList) {
        List<SymbolDTO> symbolDTOList = new ArrayList<>();
        for (Symbol symbolList1 : symbolList) {
            symbolDTOList.add(mapToDTO(symbolList1));
        }
        return symbolDTOList;
    }

    @Override
    public List<SymbolDTO> getOrganizationSiteTypeDTO(Integer companyId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Symbol> details = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=24 AND sym.companyId.id=:companyId", params);
        return mapToList(details, tobyUser);
    }

    @Override
    public List<SymbolDTO> getRegionDTO(Integer companyId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Symbol> details = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=7 AND sym.companyId.id=:companyId", params);
        return mapToList(details, tobyUser);
    }

    @Override
    public List<SymbolDTO> getSupplierTypeDTO(Integer companyId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Symbol> details = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=25 AND sym.companyId.id=:companyId", params);
        return mapToList(details, tobyUser);
    }

    @Override
    public List<SymbolDTO> getContractorTypeDTO(Integer companyId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Symbol> details = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.serial=40 AND sym.companyId.id=:companyId", params);
        return mapToList(details, tobyUser);
    }

    @Override
    public List<SymbolDTO> getUnitsByCompanyIdDTO(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = 3 AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<SymbolDTO> getSymbolListByCompanyId(Integer companyId, Integer generalSymbolId) {
        Map<String, Object> params = new HashMap<>();
        List<SymbolDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        params.put("generalSymbolId", generalSymbolId);
        List<Symbol> list = dao.findListByQuery("SELECT sym FROM Symbol sym WHERE  sym.generalSymbolId.id = :generalSymbolId AND sym.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
     @Override
    public List<SymbolDTO> getDeterminationPriceDTO(Integer companyId) {
        return getSymbolListByCompanyId(companyId,56);
    }

    @Override
    public SymbolDTO getSymbolByCompanyIdANDgeneralSymbolIdWithSerial(Integer companyId, Integer generalSymbolId, Integer serial) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("serial", serial);
        params.put("generalSymbolId", generalSymbolId);
        Symbol symbol = dao.findEntityByQuery("SELECT sym FROM Symbol sym WHERE sym.companyId.id = :companyId And sym.serial = :serial AND sym.generalSymbolId.id = :generalSymbolId", params);
        return mapTODTO(symbol, null);
    }
}
