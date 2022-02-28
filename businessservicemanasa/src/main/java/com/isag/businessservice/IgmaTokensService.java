/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IgmaTokensDTO;
import com.isag.dto.IgmaUserDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam@ISAG.com
 */
@Remote
public interface IgmaTokensService {

    public IgmaTokensDTO addIgmaToken(IgmaTokensDTO isagTokensDTO, IgmaUser isagUser);

    public IgmaTokensDTO addIgmaTokenAlreadyExist(IgmaTokensDTO isagTokensDTO, IgmaUser isagUser);

    public void deleteIgmaTokenDTO(IgmaTokensDTO isagTokensDTO, IgmaUser isagUser);

    public void deleteAllIgmaTokenDTO(IgmaTokensDTO isagTokensDTO);

    public void UpdateActiveAllIgmaTokenDTO(Boolean active);

    public void UpdateActiveIgmaTokenDTObyUserID(Integer userId, Boolean active);

    public List<IgmaTokensDTO> getAllIgmaTokenDTO(Integer branchid);

    public List<IgmaTokensDTO> getAllIgmaTokenDTO();

    public List<IgmaTokensDTO> getAllIgmaTokenDTObyActive(IgmaUser isagUser, Boolean active);

    public List<String> getAllTokan();

    public List<String> getAllTokanOpen();

    public List<String> getAllTokanByBranch(Integer branchId);

    public IgmaTokensDTO getAllIgmaTokenDTO(IgmaUser isagUser);

    public List<IgmaUserDTO> getAllIgmaUserDTO();
}
