/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IsagApiTokenDTO;
import com.isag.entity.IsagUser;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface IsagApiTokenService {

    public IsagApiTokenDTO addIsagApiToken(IsagApiTokenDTO isagApiTokenDTO, IsagUser isagUser);
    
    public IsagApiTokenDTO getIsagApiTokenDTOByTokenAndTokenID(IsagApiTokenDTO isagApiTokenDTO);

}
