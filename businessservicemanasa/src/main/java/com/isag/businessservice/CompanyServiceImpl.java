/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IgmaCompanyDTO;
import com.isag.entity.CompanyLanguage;
import com.isag.entity.DataDictionary;
import com.isag.entity.IgmaCompany;
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
 * @author hq004
 */
@Stateless
public class CompanyServiceImpl implements CompanyService {

    @EJB
    private GenericDAO dao;

    @EJB
    private DDService ddService;

    @Override
    public List<IgmaCompany> getAllCompanies() {
//        return dao.findAll(IgmaCompany.class);
        return dao.findListByQuery("SELECT b FROM IgmaCompany b order by b.id DESC ");
    }

    @Override
    public IgmaCompany addCompany(IgmaCompany company) {
        return dao.updateEntity(company);
    }

    @Override
    public IgmaCompany findCompany(Integer companyId) {
        return dao.findEntityById(IgmaCompany.class, companyId);
    }

    @Override
    public IgmaCompany updateCompany(IgmaCompany company) {
        return dao.updateEntity(company);
    }

    @Override
    public void deleteCompany(IgmaCompany company) {
        dao.deleteEntity(company);
    }

    @Override
    public CompanyLanguage updateCompanyLanguage(CompanyLanguage companyLanguage) {
        return addCompanyLanguage(companyLanguage);
    }

    @Override
    public void removeCompanyLanguages(IgmaCompany company) {

    }

    @Override
    public CompanyLanguage addCompanyLanguage(CompanyLanguage companyLanguage) {
        CompanyLanguage compLang = dao.updateEntity(companyLanguage);
//        List<DataDictionary> dds = ddService.getDefaultDDsByLanguage(compLang.getSymbol().getId());
//        for (DataDictionary dd : dds) {
//            DataDictionary newDD = new DataDictionary();
//            newDD.setCompanyId(compLang.getCompanyId());
//            newDD.setLang(compLang.getSymbol());
//            newDD.setPropertyKey(dd.getPropertyKey());
//            newDD.setPropertyValue(dd.getPropertyValue());
//            newDD.setCreatedBy(compLang.getCreatedBy());
//            newDD.setCreationDate(new Date());
//            newDD = ddService.addDataDictionary(newDD);
//        }
        return compLang;
    }

    @Override
    public List<CompanyLanguage> getCompanyLanguages(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", companyId);
        return dao.findListByQuery("SELECT cl FROM CompanyLanguage cl WHERE cl.companyId.id=:id", params);
    }

    @Override
    public void removeCompanyLanguage(Integer companyId, Integer langId) {
        dao.executeDeleteQuery("DELETE FROM CompanyLanguage l WHERE l.companyId.id = " + companyId + " AND l.symbol.id = " + langId);
        dao.executeDeleteQuery("DELETE FROM DataDictionary dd WHERE dd.companyId.id = " + companyId + " AND dd.lang.id = " + langId);
    }

    @Override
    public List<IgmaCompany> getImageByCompanyId(String imageName, Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("imageName", imageName);
        params.put("companyId", companyId);
        List<IgmaCompany> isagCompanyList = dao.findListByQuery("SELECT c FROM IgmaCompany c WHERE c.id != :companyId AND c.image = :imageName", params);

        return isagCompanyList;
    }

    @Override
    public List<IgmaCompany> findImageOfCompanyById(String imageName, Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("imageName", imageName);
        params.put("companyId", companyId);
        List<IgmaCompany> isagCompanyList = dao.findListByQuery("SELECT c FROM IgmaCompany c WHERE c.id = :companyId AND c.image = :imageName", params);

        return isagCompanyList;

    }

    @Override
    public List<IgmaCompanyDTO> getAllCompaniesDTO() {
        List<IgmaCompany> list = dao.findAll(IgmaCompany.class);
        List<IgmaCompanyDTO> dTOList = new ArrayList<>();
        returnListDTO(list,dTOList);
        return dTOList;
    }

    private IgmaCompanyDTO mapTODTO(IgmaCompany entity, IgmaCompanyDTO dTO) {
        if (dTO == null) {
            dTO = new IgmaCompanyDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(IgmaCompanyDTO dTO, IgmaCompany entity) {

        dTO.setName(entity.getName());
        dTO.setCode(entity.getCode());
        dTO.setNameEn(entity.getNameEn());
        dTO.setAddress(entity.getAddress());
        dTO.setAddressEn(entity.getAddressEn());
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());
        dTO.setCore_business(entity.getCore_business());
        dTO.setResponsible(entity.getResponsible());
        dTO.setPhone(entity.getPhone());
        dTO.setLogo(entity.getLogo());
        dTO.setImage(entity.getImage());
        dTO.setFax(entity.getFax());
        dTO.setRowCountList(entity.getRowCountList());
        dTO.setRowCountMasterDetails(entity.getRowCountMasterDetails());

        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

    }

    private IgmaCompany mapFromDTO(IgmaCompanyDTO dTO, IgmaUser isagUser) {
        IgmaCompany entity = new IgmaCompany();

        entity.setName(dTO.getName());
        entity.setCode(dTO.getCode());
        entity.setNameEn(dTO.getNameEn());
        entity.setAddress(dTO.getAddress());
        entity.setAddressEn(dTO.getAddressEn());
        entity.setId(dTO.getId());
        entity.setCore_business(dTO.getCore_business());
        entity.setResponsible(dTO.getResponsible());
        entity.setPhone(dTO.getPhone());
        entity.setLogo(dTO.getLogo());
        entity.setImage(dTO.getImage());
        entity.setFax(dTO.getFax());
        entity.setRowCountList(dTO.getRowCountList());
        entity.setRowCountMasterDetails(dTO.getRowCountMasterDetails());
        entity.setId(dTO.getId());
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
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<IgmaCompany> list, List<IgmaCompanyDTO> dTOList) {
        for (IgmaCompany entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

}
