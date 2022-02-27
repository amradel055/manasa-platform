/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvPurchaseInvoiceDetailDetailDTO;
import com.toby.dto.ProDetailTypeDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.InvPurchaseInvoiceDetailDetail;
import com.toby.entity.ProDetailType;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class InvPurchaseInvoiceDetailDetailServiceImpl implements InvPurchaseInvoiceDetailDetailService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteInvPurchaseInvoiceDetailDetailDTO(Integer InvPurchaseInvoiceDetailId) {
        Map<String, Object> params = new HashMap<>();
        params.put("InvPurchaseInvoiceDetailId", InvPurchaseInvoiceDetailId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        List<InvPurchaseInvoiceDetailDetail> detailses = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetailDetail e WHERE e.invPurchaseInvoiceDetail.id=:InvPurchaseInvoiceDetailId ", params);
        for (InvPurchaseInvoiceDetailDetail invPurchaseInvoiceDetail : detailses) {
            dao.deleteEntity(invPurchaseInvoiceDetail);
        }
    }

    @Override
    public InvDetailDTO save(InvDetailDTO invDetailDTO, TobyUser tobyUser) {
        if (invDetailDTO != null && invDetailDTO.getId() != null) {
            String sql = "delete from InvPurchaseInvoiceDetailDetail e  where e.invPurchaseInvoiceDetail.id  = " + invDetailDTO.getId();
            dao.executeDeleteQuery(sql);
        }

        List<InvPurchaseInvoiceDetailDetailDTO> invPurchaseInvoiceDetailDetailDTOList = new ArrayList<>();

        if (invDetailDTO != null && invDetailDTO.getProDetailTypeDTOList() != null) {
            for (ProDetailTypeDTO proDetailTypeDTO : invDetailDTO.getProDetailTypeDTOList()) {
                InvPurchaseInvoiceDetailDetailDTO detailDetailDTO = new InvPurchaseInvoiceDetailDetailDTO();
                detailDetailDTO.setInvPurchaseInvoiceDetailDTO(invDetailDTO.getId());
                detailDetailDTO.setProDetailTypeDTO(proDetailTypeDTO);
                InvPurchaseInvoiceDetailDetail entity = mapToEntity(detailDetailDTO, tobyUser);
                if (entity != null) {
                    entity = dao.updateEntity(entity);
                    InvPurchaseInvoiceDetailDetailDTO dto = mapToDTO(entity);
                    invPurchaseInvoiceDetailDetailDTOList.add(dto);
                }
            }
        }

        invDetailDTO.setInvPurchaseInvoiceDetailDetailDTOList(invPurchaseInvoiceDetailDetailDTOList);
        return invDetailDTO;
    }

    @Override
    public InvDetailDTO findDetailDetail(InvDetailDTO dTO, ScreenNameClassEnum screenNameClassEnum) {
        List<InvPurchaseInvoiceDetailDetailDTO> detailDetailList;
        if (screenNameClassEnum == null || screenNameClassEnum.equals(ScreenNameClassEnum.PROPRODUCTIONTRANSACTIONDETAIL)) {
            detailDetailList = getInvPurchaseInvoiceDetailsDetailsByDetailId(dTO.getInvPurchaseInvoiceDetailId());
        } else {
            detailDetailList = getInvPurchaseInvoiceDetailsDetailsByDetailId(dTO.getId());
        }
        if (detailDetailList != null && !detailDetailList.isEmpty()) {
            dTO.setInvPurchaseInvoiceDetailDetailDTOList(detailDetailList);
            for (InvPurchaseInvoiceDetailDetailDTO invoiceDetailDetailDTO : detailDetailList) {
                dTO.getProDetailTypeDTOList().add(invoiceDetailDetailDTO.getProDetailTypeDTO());
            }
        }
        return dTO;
    }

    @Override
    public List<InvPurchaseInvoiceDetailDetailDTO> getInvPurchaseInvoiceDetailsDetailsByDetailId(Integer invDetailDTOId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invDetailDTOId", invDetailDTOId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        List<InvPurchaseInvoiceDetailDetail> detailses = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetailDetail e WHERE e.invPurchaseInvoiceDetail.id=:invDetailDTOId ", params);
        return mapToDTOList(detailses);
    }

    public List<InvPurchaseInvoiceDetailDetailDTO> mapToDTOList(List<InvPurchaseInvoiceDetailDetail> entityList) {
        List<InvPurchaseInvoiceDetailDetailDTO> list = new ArrayList<>();
        for (InvPurchaseInvoiceDetailDetail entity : entityList) {
            InvPurchaseInvoiceDetailDetailDTO dto = mapToDTO(entity);
            list.add(dto);
        }
        return list;
    }

    public InvPurchaseInvoiceDetailDetailDTO mapToDTO(InvPurchaseInvoiceDetailDetail entity) {
        InvPurchaseInvoiceDetailDetailDTO dto = new InvPurchaseInvoiceDetailDetailDTO();
        if (entity.getProDetailType() != null) {
            ProDetailTypeDTO detailTypeDTO = new ProDetailTypeDTO();
            detailTypeDTO.setId(entity.getProDetailType().getId());
            detailTypeDTO.setName(entity.getProDetailType().getName());
            detailTypeDTO.setCode(entity.getProDetailType().getCode());
            dto.setProDetailTypeDTO(detailTypeDTO);
        }
        dto.setInvPurchaseInvoiceDetailDTO(entity.getInvPurchaseInvoiceDetail() == null ? null : entity.getInvPurchaseInvoiceDetail().getId());
        dto.setIndex(entity.getId());
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreationDate());
        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setBranchId(entity.getBranchId().getId() != null ? entity.getBranchId().getId() : null);
        dto.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        return dto;
    }

    public InvPurchaseInvoiceDetailDetail mapToEntity(InvPurchaseInvoiceDetailDetailDTO dto, TobyUser tobyUser) {
        InvPurchaseInvoiceDetailDetail entity = new InvPurchaseInvoiceDetailDetail();
        entity.setId(dto.getId());

        if (dto.getInvPurchaseInvoiceDetailDTO() != null) {
            InvPurchaseInvoiceDetail invPurchaseInvoiceDetail = new InvPurchaseInvoiceDetail();
            invPurchaseInvoiceDetail.setId(dto.getInvPurchaseInvoiceDetailDTO());
            entity.setInvPurchaseInvoiceDetail(invPurchaseInvoiceDetail);
        } else {
            return null;
        }
        if (dto.getProDetailTypeDTO() != null) {
            ProDetailType proDetailType = new ProDetailType();
            proDetailType.setId(dto.getProDetailTypeDTO().getId());
            entity.setProDetailType(proDetailType);
        } else {
            return null;
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

}
