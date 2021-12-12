/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.GmSubscriptionsDTO;
import javax.ejb.Remote;

@Remote
public interface GmGlobalService {

    public GmSubscriptionsDTO updateSummition(GmSubscriptionsDTO gmSubscriptionsDTO);
    
    public GmSubscriptionsDTO recalcHeadValues(GmSubscriptionsDTO dto);
    
    public GmSubscriptionsDTO addGmSubscriptionsDTODetail(GmSubscriptionsDTO gmSubscriptionsDTO, Integer index);
    
    public GmSubscriptionsDTO updateSummitionSmall(GmSubscriptionsDTO gmSubscriptionsDTO);
}
