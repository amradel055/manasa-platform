/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.dto.AttributeDTO;
import com.toby.dto.AttributeDetailsDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface AttributeDetailsService {

    public AttributeDetailsDTO addAtrributeDetails(AttributeDetailsDTO attributeDetailsDTO);

    public AttributeDetailsDTO updateAttributeDetails(AttributeDetailsDTO attributeDetailsDTO);

    public AttributeDetailsDTO findAttributeDetails(AttributeDetailsDTO attributeDetailsDTO);

    public List<AttributeDetailsDTO> findAttributeDetailsForHeadByBranchId(AttributeDTO attributeDTO);

    public void DeleteAttributeDetailsForHeadByBranchId(AttributeDTO attributeDTO);

    public AttributeDetailsDTO DeleteAttributeDetails(AttributeDetailsDTO attributeDetailsDTO);

    public List<AttributeDetailsDTO> getAllAttributesDetailsByCompanyId(AttributeDetailsDTO attributeDetailsDTO);

    public List<AttributeDetailsDTO> getAllAttributesDetailsByCompanyIdAndBranchId(AttributeDetailsDTO attributeDetailsDTO);

    public Integer getMaxId();

}
