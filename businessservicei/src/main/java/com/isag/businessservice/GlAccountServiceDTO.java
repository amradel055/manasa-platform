/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.GlAccountDTO;
import com.isag.entity.GlAccount;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface GlAccountServiceDTO {

    public List<GlAccountDTO> getBranchGLAccountsActiveWithException(Integer branchId);

    public List<GlAccountDTO> getBranchGLAccountsActive(Integer branchId);

    public GlAccountDTO findGlAccountDTO(Integer glAccount);
    
    public GlAccount findGlAccount(Integer glAccount);
    
    public GlAccountDTO getGlAccountForBank(Integer branchId, Integer type);

}
