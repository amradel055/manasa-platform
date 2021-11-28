/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.GmSubscriptionsDTO;
import com.isag.dto.GmSubscriptionsDetailDTO;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.entity.GlYear;
import com.isag.entity.GmSubscriptionsDetail;
import com.isag.entity.IsagUser;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface GmSubscriptionsDetailService {
    

    public void deleteGmSubscriptionsDetails(GmSubscriptionsDetail GmSubscriptionsDetail);

    public List<GmSubscriptionsDetail> getAllGmSubscriptionsDetailDTOsByGmSubscriptionsDTOId(Integer gmSubscriptionsId);
    
    public GmSubscriptionsDetail findGmSubscriptionsDetailById(Integer gmSubscriptionsDetailDTOsId) ;
            
    public List<GmSubscriptionsDetailDTO> getAllGmSubscriptionsDetailDTOsByGmSubscriptionsDTOIdDTO(GmSubscriptionsDTO dto);
    
    public List<GmSubscriptionsDetailDTO> getAllGmSubscriptionsDetailDTOList(GmSubscriptionsDTO dto);
            
    public void deleteGmSubscriptionsDetailDTOListDTO(List<GmSubscriptionsDetailDTO> GmSubscriptionsDetailDTOList);
    
    public void deleteGmSubscriptionsDetailDTOListDTO(Integer SubscriptionsId);
            
    public String addGmSubscriptionsDetailDTOsDTO(List<GmSubscriptionsDetailDTO> GmSubscriptionsDetailDTOList,GmSubscriptionsDTO gmSubscriptionsDTODTO , IsagUser isagUser);
           
}
