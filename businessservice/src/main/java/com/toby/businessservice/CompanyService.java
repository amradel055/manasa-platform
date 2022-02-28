/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.TobyCompanyDTO;
import com.toby.entity.CompanyLanguage;
import com.toby.entity.TobyCompany;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface CompanyService {

    public void deleteCompany(TobyCompany tobyCompany);

    public List<TobyCompany> getAllCompanies();

    public TobyCompany addCompany(TobyCompany company);

    public TobyCompany findCompany(Integer companyId);

    public TobyCompany updateCompany(TobyCompany company);

    public CompanyLanguage updateCompanyLanguage(CompanyLanguage companyLanguage);

    public void removeCompanyLanguages(TobyCompany company);

    public CompanyLanguage addCompanyLanguage(CompanyLanguage companyLanguage);

    public List<CompanyLanguage> getCompanyLanguages(Integer company);

    public void removeCompanyLanguage(Integer companyId, Integer langId);

    public List<TobyCompany> getImageByCompanyId(String imageName, Integer companyId);
    
    public List<TobyCompany> findImageOfCompanyById(String imageName, Integer companyId);
    
    public List<TobyCompanyDTO> getAllCompaniesDTO();
    
}
