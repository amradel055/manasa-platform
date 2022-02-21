/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.dto.OffersDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface OffersService {

    public OffersDTO addOffers(OffersDTO offers);

    public OffersDTO updateOffers(OffersDTO offers);

    public OffersDTO findOffers(OffersDTO offersDTO);

    public OffersDTO deleteOffers(OffersDTO offersDTO);

    public List<OffersDTO> getAllOffersByCompanyId(OffersDTO offersDTO);

    public List<OffersDTO> getAllOffersByCompanyIdAndBranchId(OffersDTO offersDTO);

    public Integer getMaxId();

}
