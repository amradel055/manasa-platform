/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RemCompanyDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface RemCompanyService {

    public RemCompanyDTO findCompany(Integer companyId);

    public void deleteCompany(RemCompanyDTO companyesDTO);

    public RemCompanyDTO save(RemCompanyDTO companyesDTO, IsagUser isagUser);

    public List<RemCompanyDTO> findCompanys(Integer branchId);

}
