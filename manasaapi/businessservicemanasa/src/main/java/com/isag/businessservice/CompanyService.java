/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IgmaCompanyDTO;
import com.isag.entity.CompanyLanguage;
import com.isag.entity.IgmaCompany;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface CompanyService {

    public void deleteCompany(IgmaCompany isagCompany);

    public List<IgmaCompany> getAllCompanies();

    public IgmaCompany addCompany(IgmaCompany company);

    public IgmaCompany findCompany(Integer companyId);

    public IgmaCompany updateCompany(IgmaCompany company);

    public CompanyLanguage updateCompanyLanguage(CompanyLanguage companyLanguage);

    public void removeCompanyLanguages(IgmaCompany company);

    public CompanyLanguage addCompanyLanguage(CompanyLanguage companyLanguage);

    public List<CompanyLanguage> getCompanyLanguages(Integer company);

    public void removeCompanyLanguage(Integer companyId, Integer langId);

    public List<IgmaCompany> getImageByCompanyId(String imageName, Integer companyId);
    
    public List<IgmaCompany> findImageOfCompanyById(String imageName, Integer companyId);
    
    public List<IgmaCompanyDTO> getAllCompaniesDTO();
}
