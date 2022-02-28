/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.GlAdminUnitDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface GlAdminUnitServiceDTO {

    public List<GlAdminUnitDTO> getAllSubAdminUnitByBranchIdActive(Integer id);

    public GlAdminUnitDTO findAdminUnit(Integer adminUnitId);
}
