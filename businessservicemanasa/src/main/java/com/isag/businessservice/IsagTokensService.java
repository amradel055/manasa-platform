/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IsagTokensDTO;
import com.isag.dto.IsagUserDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam@ISAG.com
 */
@Remote
public interface IsagTokensService {

    public IsagTokensDTO addIsagToken(IsagTokensDTO isagTokensDTO, IsagUser isagUser);

    public IsagTokensDTO addIsagTokenAlreadyExist(IsagTokensDTO isagTokensDTO, IsagUser isagUser);

    public void deleteIsagTokenDTO(IsagTokensDTO isagTokensDTO, IsagUser isagUser);

    public void deleteAllIsagTokenDTO(IsagTokensDTO isagTokensDTO);

    public void UpdateActiveAllIsagTokenDTO(Boolean active);

    public void UpdateActiveIsagTokenDTObyUserID(Integer userId, Boolean active);

    public List<IsagTokensDTO> getAllIsagTokenDTO(Integer branchid);

    public List<IsagTokensDTO> getAllIsagTokenDTO();

    public List<IsagTokensDTO> getAllIsagTokenDTObyActive(IsagUser isagUser, Boolean active);

    public List<String> getAllTokan();

    public List<String> getAllTokanOpen();

    public List<String> getAllTokanByBranch(Integer branchId);

    public IsagTokensDTO getAllIsagTokenDTO(IsagUser isagUser);

    public List<IsagUserDTO> getAllIsagUserDTO();
}
