/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvItemPriceDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvItemPrice;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class InvItemPriceServiceImpl implements InvItemPriceService {

    @EJB
    GenericDAO dao;
    @EJB
    InvItemService invItemService;

    @Override
    public InvItemPriceDTO findInvItemPrice(Integer invItemPriceId) {
        InvItemPrice invItemPrice = dao.findEntityById(InvItemPrice.class, invItemPriceId);
        return mapToDto(invItemPrice);
    }

    @Override
    public void deleteInvItemPrice(InvItemPriceDTO invItemPriceDTO) {
        InvItemPrice invItemPrice = new InvItemPrice();
        invItemPrice.setId(invItemPriceDTO.getId());
        dao.deleteEntity(invItemPrice);
    }

    @Override
    public List<InvItemPriceDTO> getALLInvItemPriceByBranchIdAndGallaryDTO(Integer branchId, Integer gallaryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvItemPrice i WHERE i.branchId.id = :branchId ");

        if (gallaryId != null) {
            query.append(" AND i.gallaryId.id = :gallaryId");
            params.put("gallaryId", gallaryId);
        }

        List<InvItemPrice> invItemPriceList = dao.findListByQuery(query.toString(), params);

        return returnDTOList(invItemPriceList);
    }

    public List<InvItemPriceDTO> returnDTOList(List<InvItemPrice> invItemPriceList) {
        if (invItemPriceList != null && !invItemPriceList.isEmpty()) {
            List<InvItemPriceDTO> list = new ArrayList<>();
            for (InvItemPrice invItemPrice : invItemPriceList) {
                list.add(mapToDto(invItemPrice));
            }
            return list;
        }
        return null;
    }

    public InvItemPriceDTO mapToDto(InvItemPrice entity) {
        InvItemPriceDTO dTO = new InvItemPriceDTO();
        dTO.setId(entity.getId());
        dTO.setMaxPriceMen(entity.getMaxPriceMen());
        dTO.setMaxPriceYoung(entity.getMaxPriceYoung());
        dTO.setMinPriceMen(entity.getMinPriceMen());
        dTO.setMinPriceYoung(entity.getMinPriceYoung());

        if (entity.getGallaryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(entity.getGallaryId().getId());
            invInventoryDTO.setName(entity.getGallaryId().getName());
            invInventoryDTO.setCode(entity.getGallaryId().getCode());
            dTO.setGallaryId(invInventoryDTO);
        }

        if (entity.getItemId() != null) {
            InvItemDTO itemDTO = invItemService.mapToDTO(entity.getItemId());
            dTO.setItemId(itemDTO);
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    public InvItemPrice mapToEntity(InvItemPriceDTO dto, TobyUser tobyUser) {

        InvItemPrice entity = new InvItemPrice();

        entity.setId(dto.getId());

        entity.setMaxPriceMen(dto.getMaxPriceMen());
        entity.setMaxPriceYoung(dto.getMaxPriceYoung());
        entity.setMinPriceMen(dto.getMinPriceMen());
        entity.setMinPriceYoung(dto.getMinPriceYoung());

        if (dto.getGallaryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(dto.getGallaryId().getId());
            entity.setGallaryId(invInventory);
        }

        if (dto.getItemId() != null) {
            InvItem item = new InvItem();
            item.setId(dto.getItemId().getId());
            entity.setItemId(item);
        }

        if (dto.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(dto.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dto.getCreatedDate());
            entity.setModifiedBy(tobyUser);
            entity.setModificationDate(new Date());
            if (dto.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(dto.getCompanyId());
                entity.setCompanyId(company);
            }

            if (dto.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(dto.getBranchId());
                entity.setBranchId(branch);
            }
        } else {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
            entity.setCompanyId(tobyUser.getCompanyId());
            entity.setBranchId(tobyUser.getBranchId());
        }
        return entity;
    }

    @Override
    public InvItemPriceDTO updateInvItemPrice(InvItemPriceDTO invItemPriceDTO, TobyUser tobyUser) {
        InvItemPrice invItemPrice = mapToEntity(invItemPriceDTO, tobyUser);
        invItemPrice = dao.updateEntity(invItemPrice);
        return mapToDto(invItemPrice);
    }

    @Override
    public Long getTotalInvItemPriceForGallary(Integer gallaryId, Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        filters.put("gallaryId", gallaryId);
        builder.append("SELECT count(s) FROM InvItemPrice s WHERE   s.branchId.id = :branchId AND (:gallaryId is Null OR  s.gallaryId.id = :gallaryId)");
        appendFilter(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    public void appendFilter(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (s.itemId.code like CONCAT('%',:globalFilter ,'%')  OR s.itemId.name  like CONCAT('%',:globalFilter ,'%')  ) ";
            builder.append(filter);
        }
    }
    
    @Override
    public List<InvItemPriceDTO> getInvItemPriceDTOView(Integer gallaryId,int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {

        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        filters.put("gallaryId", gallaryId);
        builder.append("SELECT s FROM InvItemPrice s where s.branchId.id = :branchId AND (:gallaryId is Null OR  s.gallaryId.id = :gallaryId)");
        appendFilter(filters, builder);
        builder.append(" ORDER BY s.id DESC");

        List<InvItemPrice> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);

        return returnDTOList(details);
    }

}
