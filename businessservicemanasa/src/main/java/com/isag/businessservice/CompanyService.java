/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IsagCompanyDTO;
import com.isag.entity.CompanyLanguage;
import com.isag.entity.IsagCompany;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface CompanyService {

    public void deleteCompany(IsagCompany isagCompany);

    public List<IsagCompany> getAllCompanies();

    public IsagCompany addCompany(IsagCompany company);

    public IsagCompany findCompany(Integer companyId);

    public IsagCompany updateCompany(IsagCompany company);

    public CompanyLanguage updateCompanyLanguage(CompanyLanguage companyLanguage);

    public void removeCompanyLanguages(IsagCompany company);

    public CompanyLanguage addCompanyLanguage(CompanyLanguage companyLanguage);

    public List<CompanyLanguage> getCompanyLanguages(Integer company);

    public void removeCompanyLanguage(Integer companyId, Integer langId);

    public List<IsagCompany> getImageByCompanyId(String imageName, Integer companyId);
    
    public List<IsagCompany> findImageOfCompanyById(String imageName, Integer companyId);
    
    public List<IsagCompanyDTO> getAllCompaniesDTO();
}
