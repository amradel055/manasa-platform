/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.dto.AttributeDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface AttributeService {

    public AttributeDTO addAtrribute(AttributeDTO attribute);

    public AttributeDTO updateAttribute(AttributeDTO attribute);

    public void deleteAttribute(AttributeDTO attribute);

    public AttributeDTO findAttribute(AttributeDTO attributeDTO);

    public List<AttributeDTO> getAllAttributesByCompanyId(AttributeDTO attributeDTO);

    public List<AttributeDTO> getAllAttributesByCompanyIdAndBranchId(AttributeDTO attributeDTO);

    public Integer getMaxId();

}
