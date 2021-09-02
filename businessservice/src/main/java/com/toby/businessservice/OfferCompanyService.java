/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.OfferCompanyDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface OfferCompanyService {

    public void deleteOfferCompany(OfferCompanyDTO offerCompanyDTO);

    public List<OfferCompanyDTO> getAllOfferCompany(TobyUser tobyUser);

    public OfferCompanyDTO addOfferCompany(OfferCompanyDTO offerCompanyDTO, TobyUser tobyUser);

    public OfferCompanyDTO findOfferCompany(Integer offerCompanyId, TobyUser tobyUser);

    public List<OfferCompanyDTO> getALLGlofferCompanyByCompanyId(Integer companyId, TobyUser tobyUser);

    public List<OfferCompanyDTO> getALLOfferCompanyByBranchId(TobyUser tobyUser);

    public String getNoticeByCustomerId(Integer id);
}
