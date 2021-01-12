/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.SymbolDTO;
import com.toby.entity.Symbol;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface SymbolService {

    public List<Symbol> getAllSymbol();

    public Symbol addSymbol(Symbol Symbol);

    public Symbol getSymbol(Integer SymbolId);

    public List<Symbol> getSymbol(Integer companyId, Integer SymbolId, Integer serial);

    public List<Symbol> getSymbolByGeneralSymbol(Integer companyId, Integer symbolId, Integer serial, Integer generalSymbolId);

    public List<Symbol> getSymbolBySerial(Integer companyId, Integer serial);

    public Symbol getOneSymbolBySerial(Integer companyId, Integer serial);

    public List<Symbol> getSymbolBySerialByGeneralSymbol(Integer companyId, Integer serial, Integer generalSymbolId);

    public Symbol updateSymbol(Symbol Symbol);

    public void deleteSymbol(Symbol Symbol);

    public List<Symbol> getAllLanguagesListByCompanyId(Integer companyId);

    public List<Symbol> getLanguagesWhereCompanyNull();

    public List<Symbol> getAllSupportedLanguages();

    public List<Symbol> getDocumentsTypes(Integer companyId);

    public List<Symbol> getRegion(Integer companyId);

    public List<Symbol> getOrganizationSiteType(Integer companyId);

    public List<Symbol> getSupplierType(Integer companyId);

    public List<Symbol> getContractorType(Integer companyId);

    public List<Symbol> getUnitsByCompanyId(Integer companyId);

    public List<Symbol> getStoneByCompanyId(Integer companyId);

    public List<Symbol> getAddon1ByCompanyId(Integer companyId);

    public List<Symbol> getAddon2ByCompanyId(Integer companyId);

    public List<Symbol> getPaintColorByCompanyId(Integer companyId);

    public List<Symbol> getEnamelColorByCompanyId(Integer companyId);

    public List<Symbol> getCountryByCompanyId(Integer companyId);

    public List<Symbol> getBrandByCompanyId(Integer companyId);

    public List<Symbol> getTypeCatByCompanyId(Integer companyId);

    public List<Symbol> getTheOpeningBalanceId(Integer companyId);

    public List<Symbol> getAllDocumentsByCompanyId(Integer companyId);

    public List<Symbol> getAllNationalitiesByCompanyId(Integer companyId);

    public List<Symbol> getSpecialDocumentsTypes();

    public Symbol findSymbol(Integer id);

    public List<Symbol> getprojectManagerByCompanyId(Integer companyId);

    public List<Symbol> getprofessionalBusinessConsultantByCompanyId(Integer companyId);

    public List<Symbol> getgeneralConsultantByCompanyId(Integer companyId);

    public List<Symbol> getGLItemsByCompanyId(Integer companyId);

    public List<SymbolDTO> getJobGradeByCompanyId(Integer companyId);

    public List<SymbolDTO> getReligionsCompanyId(Integer companyId);

    public List<SymbolDTO> getMaritalStatusCompanyId(Integer companyId);

    public List<SymbolDTO> getUnitCompanyId(Integer companyId);

    public List<SymbolDTO> getDesignationByCompanyId(Integer companyId);

    public List<SymbolDTO> getGenderByCompanyId(Integer companyId);

    public List<SymbolDTO> getNationalityByCompanyId(Integer companyId);

    public List<SymbolDTO> getRegionByCompanyId(Integer companyId);

    public List<SymbolDTO> getEducationLevelByCompanyId(Integer companyId);

    public List<SymbolDTO> getRelationByCompanyId(Integer companyId);

    public List<SymbolDTO> getPlaceBirthByCompanyId(Integer companyId);

    public List<SymbolDTO> getAllStoreRegionDTOByCompanyId(Integer companyId);

    public SymbolDTO getSymbolByCompanyIdANDgeneralSymbolIdWithSerial(Integer companyId, Integer generalSymbolId, Integer serial);

    //goher-----------
    public List<SymbolDTO> getDocumentsTypesDTO(Integer companyId);

    public SymbolDTO getSymbolDTO(Integer SymbolId);

    public List<SymbolDTO> getUnits();

    public List<SymbolDTO> getOrganizationSiteTypeDTO(Integer companyId, TobyUser tobyUser);

    public List<SymbolDTO> getRegionDTO(Integer companyId, TobyUser tobyUser);

    public List<SymbolDTO> getSupplierTypeDTO(Integer companyId, TobyUser tobyUser);

    public List<SymbolDTO> getContractorTypeDTO(Integer companyId, TobyUser tobyUser);

    public List<SymbolDTO> getUnitsByCompanyIdDTO(Integer companyId);

    public List<SymbolDTO> getSymbolListByCompanyId(Integer companyId, Integer generalSymbolId);

    public List<SymbolDTO> getDeterminationPriceDTO(Integer companyId);
}
