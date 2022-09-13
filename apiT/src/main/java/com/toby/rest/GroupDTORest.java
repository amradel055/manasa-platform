/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.businessservice.ProGroupService;
import com.toby.dto.ProGroupDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.entity.Branch;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Lenovo
 */
@Path("/group")
public class GroupDTORest {

    @EJB
    ProGroupService proGroupService;

    @Transactional
    @Path("/groupList")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ProGroupDTO> FindGroupList(TobyUserDTO tobyUserDTO) {
        List<ProGroupDTO> proGroupDTOList = new ArrayList<>();
        if (tobyUserDTO.getId() != null && tobyUserDTO.getBranchId() != null ) {
            TobyUser tobyUser = new TobyUser();
            tobyUser.setId(tobyUserDTO.getId());
            Branch branch = new Branch();
            tobyUserDTO.setBranchId(tobyUserDTO.getBranchId());
            tobyUser.setBranchId(branch);
            proGroupDTOList = proGroupService.getallProGroupByBranchIdDTO(tobyUser);

        }
        return proGroupDTOList;
    }

}
