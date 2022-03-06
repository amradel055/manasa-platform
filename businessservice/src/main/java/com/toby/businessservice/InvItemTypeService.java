/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvItemTypeDTO;
import java.util.List;
import com.toby.entity.InvItemType;
import com.toby.entity.TobyUser;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface InvItemTypeService {

    public InvItemTypeDTO save(InvItemTypeDTO invItemTypeDTO, TobyUser tobyUser);

    public void deleteInvItemType(Integer id);

    public List<InvItemTypeDTO> getALLInvItemTypeDTOByBranchId(Integer branchId);

    public InvItemTypeDTO findInvItemTypeDTOById(Integer invItemId);
}
