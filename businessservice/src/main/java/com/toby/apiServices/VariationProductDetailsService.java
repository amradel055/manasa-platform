/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.dto.VariationDetailsDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface VariationProductDetailsService {

    public VariationDetailsDTO addVariationDetails(VariationDetailsDTO variationDetails);

    public VariationDetailsDTO updateVariationDetails(VariationDetailsDTO variationDetails);
    
    public VariationDetailsDTO deleteVariationDetails(VariationDetailsDTO variationDetailsDTO);

    public VariationDetailsDTO findVariationDetails(VariationDetailsDTO variationDetailsDTO);

    public List<VariationDetailsDTO> getAllVariationsDetailsByCompanyId(VariationDetailsDTO variationDetailsDTO);
    
    public List<VariationDetailsDTO> getAllVariationDetailsByCompanyIdAndBranchId(VariationDetailsDTO variationDetailsDTO);

    public Integer getMaxId();

}
