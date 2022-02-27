/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.GlAdminUnitSearchBean;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface GlAdminUnitService {

    public void deleteAdminUnit(GlAdminUnit tobyAdminUnit);

    public List<GlAdminUnit> getAllAdminUnit();

    public GlAdminUnit addAdminUnit(GlAdminUnit adminUnit);

    public GlAdminUnit findAdminUnit(Integer adminUnitId);

    public GlAdminUnit updateAdminUnit(GlAdminUnit adminUnit);

    public List<GlAdminUnit> getAllAdminUnitByCompanyId(Integer companyId);

    public List<GlAdminUnit> getAllAdminUnitByBranchIdActive(Integer id);
    
    public List<GlAdminUnitDTO> getAllAdminUnitDTOByBranchIdActive(TobyUser tobyUser);

    public List<GlAdminUnit> getAllSubAdminUnitByBranchIdActive(Integer id);

    public List<GlAdminUnit> getAllAdminUnitIndex(GlAdminUnitSearchBean glAdminUnitSearchBean);

    public GlAdminUnit findAdminUnitByCode(Integer parentCode, Integer branchId);

    public List<GlAdminUnit> getAllSubAdminUnitByBranchIdActiveAndLevel(Integer branchId, Integer level);

    public List<GlAdminUnit> getAllSubAdminUnitByBranchIdActiveAndCostCenterId(Integer id);
    
    public List<GlAdminUnitDTO> getAllSubAdminUnitByBranchIdActiveDTO(Integer id);

}
