/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.IsagUserLoginBeanSearch;
import com.isag.entity.IgmaUserLogin;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface IgmaUserLoginService {

    public IgmaUserLogin addIsagUserLogin(IgmaUserLogin isagUserLogin);
    
    public void saveUserData(Integer branchId, Integer companyId, String macId, Integer studentId,Integer state);

    public void deletegetIsagUserLogin(IgmaUserLogin isagUserLogin);

    public List<IgmaUserLogin> getALLgetIsagUserLoginByCompanyId(Integer companyId);

    public List<IgmaUserLogin> getALLgetIsagUserLoginByBranchId(Integer branchId);

    public List<IgmaUserLogin> getALLgetIsagUserLoginByBranchIdAndUserCodeAndDate(IsagUserLoginBeanSearch isagUserLoginBeanSearch);

    public IgmaUserLogin getIsagUserLogin(Integer isagUserLoginId);

    public List<IgmaUserLogin> findIsagUserLoginByUserId(Integer branchId, Integer userId);
    
    public IgmaUserLogin findIsagUserLoginByStudentIdLast(Integer studentId);
    
    public void deleteByStudentId(Integer studentId);
    
    //check user already login or not
    public boolean checkUserLogged(Integer userId, Integer branchId);
}
