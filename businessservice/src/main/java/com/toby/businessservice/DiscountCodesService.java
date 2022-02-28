/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.DiscountCodesDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface DiscountCodesService {

    public void deleteDiscountCodes(DiscountCodesDTO discountCodesDTO);

    public List<DiscountCodesDTO> getAllDiscountCodes(TobyUser tobyUser);

    public DiscountCodesDTO addDiscountCodes(DiscountCodesDTO discountCodesDTO, TobyUser tobyUser);

    public DiscountCodesDTO findDiscountCodes(Integer discountCodesId, TobyUser tobyUser);

    public List<DiscountCodesDTO> getDiscountCodesByCompanyId(Integer companyId, TobyUser tobyUser);

    public List<DiscountCodesDTO> getDiscountCodesByBranchId(Integer branchId, TobyUser tobyUser);

}
