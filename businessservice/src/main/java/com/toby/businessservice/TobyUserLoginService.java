/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.TobyUserLoginBeanSearch;
import com.toby.entity.TobyUserLogin;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface TobyUserLoginService {

    public TobyUserLogin addTobyUserLogin(TobyUserLogin tobyUserLogin);

    public void deletegetTobyUserLogin(TobyUserLogin tobyUserLogin);

    public List<TobyUserLogin> getALLgetTobyUserLoginByCompanyId(Integer companyId);

    public List<TobyUserLogin> getALLgetTobyUserLoginByBranchId(Integer branchId);

    public List<TobyUserLogin> getALLgetTobyUserLoginByBranchIdAndUserCodeAndDate(TobyUserLoginBeanSearch tobyUserLoginBeanSearch);

    public TobyUserLogin getTobyUserLogin(Integer tobyUserLoginId);

    public List<TobyUserLogin> findTobyUserLoginByUserId(Integer branchId, Integer userId);
    
    //check user already login or not
    public boolean checkUserLogged(Integer userId, Integer branchId);
}
