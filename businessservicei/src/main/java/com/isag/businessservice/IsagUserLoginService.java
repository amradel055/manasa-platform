/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.IsagUserLoginBeanSearch;
import com.isag.entity.IsagUserLogin;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface IsagUserLoginService {

    public IsagUserLogin addIsagUserLogin(IsagUserLogin isagUserLogin);

    public void deletegetIsagUserLogin(IsagUserLogin isagUserLogin);

    public List<IsagUserLogin> getALLgetIsagUserLoginByCompanyId(Integer companyId);

    public List<IsagUserLogin> getALLgetIsagUserLoginByBranchId(Integer branchId);

    public List<IsagUserLogin> getALLgetIsagUserLoginByBranchIdAndUserCodeAndDate(IsagUserLoginBeanSearch isagUserLoginBeanSearch);

    public IsagUserLogin getIsagUserLogin(Integer isagUserLoginId);

    public List<IsagUserLogin> findIsagUserLoginByUserId(Integer branchId, Integer userId);
    
    //check user already login or not
    public boolean checkUserLogged(Integer userId, Integer branchId);
}
