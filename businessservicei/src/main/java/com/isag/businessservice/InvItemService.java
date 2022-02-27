/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvItemDTO;
import com.isag.dto.MissingReport;
import com.isag.entity.InvBarcode;
import com.isag.entity.InvGroup;
import com.isag.entity.InvItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvItemService {

    public InvItem addInvItem(InvItem invItem, List<InvBarcode> invBarcodeList, List<InvBarcode> invBarcodeDeletedList);

    public InvItem updateInvItem(InvItem invItem);

    public void updateInvItem(Integer invItemId);

    public void deleteInvItem(InvItem invItem);

    public InvItem findInvItem(Integer invItemById);

    public List<InvItem> getInvItemByCompanyId(Integer companyId);

    public List<InvItemDTO> getInvItemListByBranchId(Integer branchId, ScreenNameClassEnum screenNameClassEnum);

    public List<InvItem> getInvItemByBranchId(Integer branchId);

    public List<InvItemDTO> getInvItemByBranchIdDTO(Integer branchId);

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

    public Boolean checkIfGroupHasItems(InvGroup invGroup);

    public List<InvItem> getInvItemListByBranchIdForOpeningBalanceItemForm(Integer branchId);

    public List<InvItem> getInvItemReport(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean);

    public List<InvItem> getInvItemReportForInvItemMainDataByGroupReportMB(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean);

    public List<InvItem> findItemByCode(String code, Integer branchId);

    public BigDecimal findMaxItemCode(Integer settingType, Integer searchId, String searchCode);

    public <T> Integer getMaxGenericSerialForItem(Integer branchId);

    public List<InvItem> getInvItemView(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public List<InvItem> getInvItemViewData(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer inventoryId);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters);

    public void getInvItemListByBranchId(Integer branchId);

    public void updateInvItemCostAverageToZero(Integer branchId);

    public List<Integer> getInvItemListByBranchIdNotInInvPurchaseInvoice(Integer branchId);

    public InvItemDTO mapToDTO(InvItem detail);

    public List<InvItem> getInvItemByBranchIdAndGroupIdUpdate(Integer branchId, String groupId);

    public List<MissingReport> FindinvItemsList(List<InvItem> invItemsList, Integer branchId );

}
