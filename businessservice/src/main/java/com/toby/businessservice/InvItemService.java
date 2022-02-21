/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvItemDTO;
import com.toby.entity.InvBarcode;
import com.toby.entity.InvGroup;
import com.toby.entity.InvItem;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface InvItemService {

    public List<InvItemDTO> findInvItemDTOList(TobyUser tobyUser);

    public List<InvItemDTO> invItemsReport( InvItemDTO invItemDTO ,Integer branchId);

    public Map<String, String> findInvItemDTOMap(Integer branchId);

    public Map<String, String> findInvItemDTOMapForNewItem(Integer branchId);

    public InvItem addInvItem(InvItem invItem, List<InvBarcode> invBarcodeList, List<InvBarcode> invBarcodeDeletedList);

    public InvItem updateInvItem(InvItem invItem);
    
    public InvItem updateInvItemWebSite(InvItem invItem);

    public void deleteInvItem(InvItem invItem);

    public void deleteInvItemDTO(InvItemDTO invItem);

    public InvItem findInvItem(Integer invItemById);
    
    public InvItemDTO findInvItemDTO(Integer invItemById);

    public List<InvItem> getInvItemByCompanyId(Integer companyId);

    public List<InvItemDTO> getInvItemListByBranchId(Integer branchId, ScreenNameClassEnum screenNameClassEnum);

    public List<InvItem> getInvItemByBranchId(Integer branchId);

    public List<InvItem> getInvItemByBranchIdForInvItemMainDataByGroupReportMB(Integer branchId);

    public List<InvItem> getInvItemByBranchIdAndGroupId(Integer branchId, Integer groupId);
    
     public List<InvItemDTO> getInvItemByBranchIdAndGroupIdDTO(Integer branchId, Integer groupId);

    public List<InvItem> getImageByCompanyId(String imageName, Integer companyId);

    public List<InvItem> getImageByBranchId(String imageName, Integer branchId);

    public List<InvItem> findImageOfBranchId(String imageName, Integer branchId);

    public Boolean validateCode(InvItem invItem);

    public Boolean validateNickName(InvItem invItem);

    public Boolean validateCodeFromView(InvItem invItem);

    public void updateAllInvItems(List<InvItem> invItemList);

    public List<InvItem> findInventoryItem(Integer inventoryId);

    public List<InvItemDTO> findInventoryDTOItem(Integer inventoryId, TobyUser tobyUser);

    public Boolean checkIfGroupHasItems(InvGroup invGroup);

    public List<InvItem> getInvItemListByBranchIdForOpeningBalanceItemForm(Integer branchId);

    public List<InvItem> findItemByCode(String code, Integer branchId);

    public BigDecimal findMaxItemCode(Integer settingType, Integer searchId, String searchCode);

    public List<InvItem> getInvItemView(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters);

    public List<InvItem> getInvItemReportForInvItemMainDataByGroupReportMB(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean);

    public List<InvItemDTO> findInvItemDTOCompletedList(TobyUser tobyUser);

    public List<InvItemDTO> getInvItemDTOView(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, TobyUser tobyUser);

    public List<InvItemDTO> getInvItemDTOByBranchId(Integer branchId, TobyUser tobyUser);

    public List<InvItemDTO> getInvItemListByBranchIdForOpeningBalanceItemFormDTO(TobyUser tobyUser);

    public void updateInvItem2(Map<String, InvItem> invItemMap, TobyUser tobyUser);

    public void findInvItemMap(Integer branchId, List<InvItemDTO> invItemDTOList);
    
    public void updateInvItemType(Integer branchId, List<InvItemDTO> invItemDTOList,Integer type);

    public void getInvItemListByBranchId(Integer branchId);

    public void updateInvItemCostAverageToZero(Integer branchId);

    public List<Integer> getInvItemListByBranchIdNotInInvPurchaseInvoice(Integer branchId);
    
    public InvItemDTO mapToDTO(InvItem invItem, TobyUser tobyUser);
}
