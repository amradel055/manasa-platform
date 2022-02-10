/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.apiServices;

import com.toby.dto.OffersDetailsDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface OffersDatailsService {

    public OffersDetailsDTO addOffersDetails(OffersDetailsDTO offersDetails);

    public OffersDetailsDTO updateOffersDetails(OffersDetailsDTO offersDetails);

    public OffersDetailsDTO deleteOffersDetails(OffersDetailsDTO offersDetailsDTO);

    public OffersDetailsDTO findOffersDetails(OffersDetailsDTO offersDetailsDTO);

    public List<OffersDetailsDTO> getAllOffersDetailsByCompanyId(OffersDetailsDTO offersDetailsDTO);

    public List<OffersDetailsDTO> getAllOffersDetailsByCompanyIdAndBranchId(OffersDetailsDTO offersDetailsDTO);

    public Integer getMaxId();

}
