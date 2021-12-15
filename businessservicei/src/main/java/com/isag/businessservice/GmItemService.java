/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.GmItemDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.GmItem;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface GmItemService {

    
     public GmItemDTO mapTODTO(GmItem entity, GmItemDTO dTO) ;
    
     public GmItemDTO updateGmItemDTO(GmItemDTO gmItemDTO, IsagUser isagUser);
     
     public List<GmItemDTO> findGmItemByCompanyId(Integer companyId);
     
     public void deleteGmItem(GmItem gmItem);
     
     public GmItemDTO getGmItemDTO(Integer SymbolId) ;
     
     public List<GmItemDTO> autoCompleteGmItemDTO(Integer branchId);
}
