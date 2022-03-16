/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.TobyCompanyDTO;
import com.toby.entity.CompanyLanguage;
import com.toby.entity.DataDictionary;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
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
    public List<TobyCompany> getAllCompanies() {
        List<TobyCompany> companys = dao.findAll(TobyCompany.class);
        return companys;
    }

    @Override
    public TobyCompany addCompany(TobyCompany company) {
        return dao.updateEntity(company);
    }

    @Override
    public TobyCompany findCompany(Integer companyId) {
        return dao.findEntityById(TobyCompany.class, companyId);
    }

    @Override
    public TobyCompany updateCompany(TobyCompany company) {
        return dao.updateEntity(company);
    }

    @Override
    public void deleteCompany(TobyCompany company) {
        dao.deleteEntity(company);
    }

    @Override
    public CompanyLanguage updateCompanyLanguage(CompanyLanguage companyLanguage) {
        return addCompanyLanguage(companyLanguage);
    }

    @Override
    public void removeCompanyLanguages(TobyCompany company) {

    }

    @Override
    public CompanyLanguage addCompanyLanguage(CompanyLanguage companyLanguage) {
        CompanyLanguage compLang = dao.updateEntity(companyLanguage);
        List<DataDictionary> dds = ddService.getDefaultDDsByLanguage(compLang.getSymbol().getId());
        for (DataDictionary dd : dds) {
            DataDictionary newDD = new DataDictionary();
            newDD.setCompanyId(compLang.getCompanyId());
            newDD.setLang(compLang.getSymbol());
            newDD.setPropertyKey(dd.getPropertyKey());
            newDD.setPropertyValue(dd.getPropertyValue());
            newDD.setCreatedBy(compLang.getCreatedBy());
            newDD.setCreationDate(new Date());
            newDD = ddService.addDataDictionary(newDD);
        }
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
    public List<TobyCompany> getImageByCompanyId(String imageName, Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("imageName", imageName);
        params.put("companyId", companyId);
        List<TobyCompany> tobyCompanyList = dao.findListByQuery("SELECT c FROM TobyCompany c WHERE c.id != :companyId AND c.image = :imageName", params);

        return tobyCompanyList;
    }
    
    @Override
    public List<TobyCompany> findImageOfCompanyById(String imageName, Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("imageName", imageName);
        params.put("companyId", companyId);
        List<TobyCompany> tobyCompanyList = dao.findListByQuery("SELECT c FROM TobyCompany c WHERE c.id = :companyId AND c.image = :imageName", params);

        return tobyCompanyList;

    }
    
    @Override
    public List<TobyCompanyDTO> getAllCompaniesDTO() {
        List<TobyCompany> list = dao.findAll(TobyCompany.class);
        List<TobyCompanyDTO> dTOList = new ArrayList<>();
        returnListDTO(list,dTOList);
        return dTOList;
    }

    private TobyCompanyDTO mapTODTO(TobyCompany entity, TobyCompanyDTO dTO) {
        if (dTO == null) {
            dTO = new TobyCompanyDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(TobyCompanyDTO dTO, TobyCompany entity) {

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

    private TobyCompany mapFromDTO(TobyCompanyDTO dTO, TobyUser tobyUser) {
        TobyCompany entity = new TobyCompany();

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

    private void returnListDTO(List<TobyCompany> list, List<TobyCompanyDTO> dTOList) {
        for (TobyCompany entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

}
