/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.OfferCopounDTO;
import com.toby.entity.OfferCopoun;
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

    public List<OfferCopounDTO> getAllOfferCopoun();

    public OfferCopounDTO addOfferCopoun(OfferCopounDTO offerCopounDTO, TobyUser tobyUser);

    public OfferCopounDTO findOfferCopoun(Integer offerTextId);

    public List<OfferCopounDTO> getALLOfferCopounByCompanyId(Integer companyId);

    public List<OfferCopounDTO> getALLOfferCopounByBranchId(Integer branchId );
    
    public List<OfferCopounDTO> getALLOfferCopounByGallaryId(Integer galaryId );
    
    public List<OfferCopounDTO> getALLOfferCopounByOfferSearch(OfferCopounDTO offerCopounDTO );

    public String getNoticeByCustomerId(Integer id);
    
    public void addOfferCopoun(List<OfferCopounDTO> offerCopounDTOList, TobyUser tobyUser);
    
    public void addOfferCopoun(Integer number, Integer discountType, BigDecimal discountValue, Integer gallaryType, TobyUser tobyUser);
    
    public BigDecimal getOfferCopounByCustomerAndCode(Integer customerId, String code);
    
    public OfferCopoun getOfferCopounByCode(String code,Integer type);
}
