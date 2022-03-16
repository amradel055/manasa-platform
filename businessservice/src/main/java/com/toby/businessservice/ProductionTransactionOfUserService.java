/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.PrintProductionDTO;
import com.toby.entity.TobyUser;
import com.toby.views.ProductionTransactionOfUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface ProductionTransactionOfUserService {
    public List<ProductionTransactionOfUser> getProductionTransactionOfUserList(PrintProductionDTO printProductionDTO,TobyUser tobyUser);
    
}
