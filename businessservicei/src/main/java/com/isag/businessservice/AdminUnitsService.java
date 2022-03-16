/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.AdminUnitSearchBean;
import com.isag.entity.GlAdminUnit;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface AdminUnitsService {

    public List<GlAdminUnit> getAllAdminUnits();

    public List<GlAdminUnit> getCompanyAdminUnits(Integer companyId);

    public void deleteAdminUnit(GlAdminUnit selectedAU);

    public GlAdminUnit addAdminUnit(GlAdminUnit newAdminUnit);

    public List<GlAdminUnit> getAllAdminUnitsNodesByCompany(Integer companyId);

    public List<GlAdminUnit> getAllAdminUnitsNodesByBranch(Integer id);

    public List<GlAdminUnit> getBranchAdminUnitsRoots(Integer branchId);

    public List<GlAdminUnit> getAllAdminUnitsByCompany(Integer id);

    public List<GlAdminUnit> getAdminUnits(AdminUnitSearchBean adminUnitSearchBean);

    public List<GlAdminUnit> getAdminUnitsBBranchId(Integer branchId, AdminUnitSearchBean adminUnitSearchBean);

    public List<GlAdminUnit> getAllAdminUnitsByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer adminUnitCode);

    public List<GlAdminUnit> getAllAdminUnitsByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer adminUnitCode, Integer adminUnitId);

}
