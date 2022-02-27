/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.ProDetailTypeDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ProDetailTypeService {

   public List<ProDetailTypeDTO> getallProDetailTypeByBranchId(Integer branchId, String query);

   public void deleteProDetailTypeDTO(ProDetailTypeDTO dto);
   
   public ProDetailTypeDTO findProDetailTypeDTO(Integer id, TobyUser tobyUser);
   
   public ProDetailTypeDTO saveProDetailTypeDTO(ProDetailTypeDTO dto, TobyUser tobyUser);
   
   public Boolean validateName(TobyUser tobyUser, Integer ProDetailTypeId, String name);
   
   public Boolean validateCode(TobyUser tobyUser, Integer ProDetailTypeId, Integer code);
}
