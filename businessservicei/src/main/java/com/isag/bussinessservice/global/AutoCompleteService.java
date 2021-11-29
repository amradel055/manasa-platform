/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.bussinessservice.global;

import com.isag.dto.CostCenterDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.GlAdminUnitDTO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.HrSalaryEffectDTO;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvItemDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.InventoryDelegatorDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InstProjectGuide;
import com.isag.views.ItemsBarcodesDetailsView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface AutoCompleteService {

    public List<InstProjectGuide> autoCompleteProjectGuidesActive(Integer branchId, String qeury);

    public List<InstProjectGuideDTO> autoCompleteInstProjectGuideDTOActive(Integer branchId, String qeury);

    public List<ItemsBarcodesDetailsView> findAllItemsBarcodesDetailsViewBranchId(Integer branchId, String qeury);

    public List<RscWorkUnitsGuideDTO> autoCompleteRscWorkUnitDTOActive(Integer branchId, String qeury);

    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSiteDTO(Integer branchId, boolean active, Integer type, String qeury);

    public List<InstProjectGuideDTO> autoCompleteAllInstProjectGuideDTOActive(Integer branchId, String qeury);

    public List<InstProjectGuideDTO> autoCompleteAllInstProjectGuideDTOActiveWithOutLevel(Integer branchId, String qeury);

    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSite(Integer branchId, boolean active, Integer type, String qeury);

    public List<InvOrganizationSiteDTO> autoCompleteConnectorAndSupplier(Integer branchId, boolean active, String qeury);

    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSite(Integer branchId, boolean active, Integer type, String qeury, Integer assignedTo);

    public List<InvOrganizationSiteDTO> autoCompleteAllLeads(Integer branchId, String qeury);

    public List<InventoryDelegatorDTO> autoCompleteInventoryDelegatorByBranch(Integer branchId, Integer type, String qeury);

    public List<CostCenterDTO> autoCompleteActiveSubCostCentersByBranch(Integer branchId, String qeury);

    public List<GlAdminUnitDTO> autoCompleteSubAdminUnitByBranchIdActiveDTO(Integer branchId, String qeury);

    public List<InvItemDTO> autoCompleteInvItem(Integer branchId, String qeury);

    public List<EmployeeDTO> autoCompleteEmployee(Integer branchId, String qeury);
    
    public List<SymbolDTO> autoCompleteSymbolByCompanyId(Integer companyId, String qeury) ;

    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSiteWitAssignedList(Integer branchId, boolean active, Integer type, String qeury, Integer assignedTo);

    public List<EmployeeDTO> autoCompleteEmployeesListByCompanyId(Integer companyId, Integer branchId, String qeury);

    public List<HrSalaryEffectDTO> autoCompleteHrSalaryEffect(Integer branchId, String qeury);

    public List<ItemsBarcodesDetailsView> autoCompleteItemsBarcodesDetailsViewBranchId(Integer branchId, String qeury);

    public List<GlBankDTO> autoCompleteGlbankByUserAndBranchId(Integer branchId, Integer userId, String qeury);

}
