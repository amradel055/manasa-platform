/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.dto.VariationDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface VariationProductService {

    public VariationDTO addVariation(VariationDTO variation);

    public VariationDTO updateVariation(VariationDTO variation);

    public VariationDTO findVariation(VariationDTO variationDTO);

    public VariationDTO deleteVariation(VariationDTO variationDTO);

    public List<VariationDTO> getAllVariationsByCompanyId(VariationDTO variationDTO);

    public List<VariationDTO> getAllVariationByCompanyIdAndBranchId(VariationDTO variationDTO);

    public Integer getMaxId();

}
