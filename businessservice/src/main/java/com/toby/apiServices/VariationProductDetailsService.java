/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.entity.VariationDetails;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface VariationProductDetailsService {

    public VariationDetails addVariationDetails(VariationDetails variationDetails);

    public VariationDetails updateVariationDetails(VariationDetails variationDetails);

    public VariationDetails findVariationDetails(Integer id);

    public List<VariationDetails> getAllVariationsDetailsByCompanyId(Integer companyId);
    
    public List<VariationDetails> getAllVariationDetailsByCompanyIdAndBranchId(Integer companyId, Integer branchId);

    public Integer getMaxId();

}
