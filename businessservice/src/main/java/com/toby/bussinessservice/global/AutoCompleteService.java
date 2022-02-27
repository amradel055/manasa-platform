/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bussinessservice.global;

import com.toby.dto.CostCenterDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.entity.InvOrganizationSite;
import com.toby.views.ItemsBarcodesDetailsView;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface AutoCompleteService {

    public List<InvInvoiceDTO> autoCompleteProWorkOrderByBranch(Integer branchId, String qeury);

    public List<ItemsBarcodesDetailsView> autoCompleteItemsBarcodesDetailsViewBranchId(Integer branchId, Integer type, String qeury);

    public List<ItemsBarcodesDetailsView> findAllItemsBarcodesDetailsViewBranchId(Integer branchId, String qeury);

    public Map<String, ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdMap(Integer branchId, String qeury);

    public List<InvItemDTO> autoCompleteInvItem(Integer branchId, String qeury);

    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSiteDTO(Integer branchId, boolean active, Integer type, String qeury, Integer gallaryId);

    public List<InvOrganizationSite> autoCompleteOrganizationSite(Integer branchId, boolean active, Integer type, String qeury, Integer gallaryId);

    public List<InvInvoiceDTO> autoCompleteInvPurchaseForFactoryDTO(Integer branchId, String qeury);

    public List<InventoryDelegatorDTO> autoCompleteInventoryDelegatorByBranch(Integer branchId, Integer type, String qeury);

    public List<CostCenterDTO> autoCompleteActiveSubCostCentersByBranch(Integer branchId, String qeury);

    public List<GlAdminUnitDTO> autoCompleteSubAdminUnitByBranchIdActiveDTO(Integer branchId, String qeury);

    public List<InvInventoryDTO> getGallaryByBranchId(Integer branchId, String qeury);

    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSiteWitAssignedList(Integer branchId, boolean active, Integer type, String qeury, Integer assignedTo);

}
