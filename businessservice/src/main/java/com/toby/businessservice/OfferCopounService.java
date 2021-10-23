/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.OfferCopounDTO;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface OfferCopounService {

    public void deleteOfferCopoun(OfferCopounDTO offerCopounDTO);

    public List<OfferCopounDTO> getAllOfferCopoun(TobyUser tobyUser);

    public OfferCopounDTO addOfferCopoun(OfferCopounDTO offerCopounDTO, TobyUser tobyUser);

    public OfferCopounDTO findOfferCopoun(Integer offerTextId, TobyUser tobyUser);

    public List<OfferCopounDTO> getALLOfferCopounByCompanyId(Integer companyId, TobyUser tobyUser);

    public List<OfferCopounDTO> getALLOfferCopounByBranchId(Integer branchId, TobyUser tobyUser);
    
    public List<OfferCopounDTO> getALLOfferCopounByGallaryId(Integer galaryId, TobyUser tobyUser);

    public String getNoticeByCustomerId(Integer id);
    
    public void addOfferCopoun(List<OfferCopounDTO> offerCopounDTOList, TobyUser tobyUser);
    
    public BigDecimal getOfferCopounByCustomerAndCode(Integer customerId, String code);
}
