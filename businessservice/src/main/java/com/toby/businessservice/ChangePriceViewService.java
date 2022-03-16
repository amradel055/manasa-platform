/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.views.ChangePriceView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author elsakr6
 */
@Remote
public interface ChangePriceViewService {

    public List<ChangePriceView> getAllChangePriceViewByBranchId(Integer branchId);
}
