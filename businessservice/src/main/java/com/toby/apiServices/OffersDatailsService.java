/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.entity.OffersDetails;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface OffersDatailsService {

    public OffersDetails addOffersDetails(OffersDetails offersDetails);

    public OffersDetails updateOffersDetails(OffersDetails offersDetails);

    public OffersDetails findOffersDetails(Integer id);

    public List<OffersDetails> getAllOffersDetailsByCompanyId(Integer companyId);
    
    public List<OffersDetails> getAllOffersDetailsByCompanyIdAndBranchId(Integer companyId , Integer branchId);

    public Integer getMaxId();

}
