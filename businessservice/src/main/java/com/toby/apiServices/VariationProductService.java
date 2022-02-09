/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.entity.Variation;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface VariationProductService {

    public Variation addVariation(Variation variation);

    public Variation updateVariation(Variation variation);

    public Variation findVariation(Integer id);

    public List<Variation> getAllVariationsByCompanyId(Integer companyId);
    
    public List<Variation> getAllVariationByCompanyIdAndBranchId(Integer companyId , Integer branchId);

    public Integer getMaxId();

}
