/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.ReportManagement;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface ReportManagementService {

    public ReportManagement reportManagementByBranch(Integer branchId);
    
    public void savereportManagement(ReportManagement reportManagement);
    
}
