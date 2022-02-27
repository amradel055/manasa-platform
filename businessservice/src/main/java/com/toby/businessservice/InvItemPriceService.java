/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvItemPriceDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvItemPriceService {

    public InvItemPriceDTO findInvItemPrice(Integer invItemPriceId);

    public void deleteInvItemPrice(InvItemPriceDTO invItemPrice);

    public InvItemPriceDTO updateInvItemPrice(InvItemPriceDTO invItemPriceDTO, TobyUser tobyUser);

    public List<InvItemPriceDTO> getALLInvItemPriceByBranchIdAndGallaryDTO(Integer Id, Integer type);

    public Long getTotalInvItemPriceForGallary(Integer galaryId, Integer branchId, Map<String, Object> filters);

    public List<InvItemPriceDTO> getInvItemPriceDTOView(Integer galaryId, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);
}
