/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InstPurchaseInvoiceDTO;
import com.isag.dto.InstPurchaseInvoiceDetailDTO;
import com.isag.entity.InstPurchaseInvoice;
import com.isag.entity.InstPurchaseInvoiceDetail;
import com.isag.entity.IsagUser;
import com.isag.views.InvItemInstProjectGuideView;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class InstPurchaseInvoiceDetailServiceImpl implements InstPurchaseInvoiceDetailService {

    @EJB
    private GenericDAO dao;

    @EJB
    private InvItemInstProjectGuideViewService invItemInstProjectGuideViewService;

    @Override
    public InstPurchaseInvoiceDetailDTO addInstPurchaseInvoiceDetail(InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTO, IsagUser isagUser) {
        InstPurchaseInvoiceDetail entity = mapFromDTO(instPurchaseInvoiceDetailDTO, isagUser);
        entity = dao.updateEntity(entity);
        instPurchaseInvoiceDetailDTO = mapTODTO(entity, instPurchaseInvoiceDetailDTO);
        return instPurchaseInvoiceDetailDTO;
    }

    @Override
    public void deleteByInstPurchaseInvoiceDetailId(Integer InstPurchaseInvoiceDetailId) {
        //The delete method includes the 4 tables to be deleted based on the employee ID
        InstPurchaseInvoiceDetail entity = new InstPurchaseInvoiceDetail();
        entity.setId(InstPurchaseInvoiceDetailId);
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteByInstPurchaseInvoiceId(Integer InstPurchaseInvoiceId) {
        Map<String, Object> params = new HashMap();
        params.put("InstPurchaseInvoiceId", InstPurchaseInvoiceId);
        dao.executeDeleteQuery("delete from InstPurchaseInvoiceDetail d WHERE d.instPurchaseInvoiceId.id=" + InstPurchaseInvoiceId);

    }

    @Override
    public InstPurchaseInvoiceDetailDTO findbyInstPurchaseInvoiceDetailId(Integer InstPurchaseInvoiceDetailId) {
        InstPurchaseInvoiceDetail instPurchaseInvoice = dao.findEntityById(InstPurchaseInvoiceDetail.class, InstPurchaseInvoiceDetailId);
        InstPurchaseInvoiceDetailDTO dTO = new InstPurchaseInvoiceDetailDTO();
        return mapTODTO(instPurchaseInvoice, dTO);
    }

    @Override
    public List<InstPurchaseInvoiceDetailDTO> findbyInstPurchaseInvoiceDetailList(Integer InstPurchaseInvoice, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("InstPurchaseInvoice", InstPurchaseInvoice);
        List<InstPurchaseInvoiceDetail> list = new ArrayList<>();
        list = dao.findListByQuery("SELECT i FROM InstPurchaseInvoiceDetail i WHERE i.branchId.id = :branchId AND i.instPurchaseInvoiceId.id = :InstPurchaseInvoice  ", params);
        return returnListDTO(list);
    }

    private List<InstPurchaseInvoiceDetailDTO> returnListDTO(List<InstPurchaseInvoiceDetail> entity) {
        List<InstPurchaseInvoiceDetailDTO> dTOList = new ArrayList<>();
        for (InstPurchaseInvoiceDetail instPurchaseInvoiceDetail : entity) {
            dTOList.add(mapTODTO(instPurchaseInvoiceDetail, null));
        }
        return dTOList;
    }

    private InstPurchaseInvoiceDetailDTO mapTODTO(InstPurchaseInvoiceDetail entity, InstPurchaseInvoiceDetailDTO dTO) {
        if (dTO == null) {
            dTO = new InstPurchaseInvoiceDetailDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(InstPurchaseInvoiceDetailDTO dTO, InstPurchaseInvoiceDetail entity) {

        if (entity.getInstPurchaseInvoiceId() != null && entity.getInstPurchaseInvoiceId().getId() != null) {
            InstPurchaseInvoiceDTO parentDTO = new InstPurchaseInvoiceDTO();
            parentDTO.setId(entity.getInstPurchaseInvoiceId().getId());
            dTO.setInstPurchaseInvoice(parentDTO);
        }

        if (entity != null && entity.getUnitPrice() != null && entity.getQuantity() != null) {
            dTO.setTotal(entity.getUnitPrice().multiply(entity.getQuantity()));
        }

        if (entity.getItemId() != null) {
            InvItemInstProjectGuideView invItemInstProjectGuideView = new InvItemInstProjectGuideView();
//         invItemInstProjectGuideView.setTableType(entity.getTableType() );
//         invItemInstProjectGuideView.setId(entity.getItemId() );
            invItemInstProjectGuideView = invItemInstProjectGuideViewService.findInvItemInstProjectGuideViewBytableId(entity.getBranchId().getId(), entity.getTableType(), entity.getItemId());
            dTO.setItemId(invItemInstProjectGuideView);
        }

        dTO.setQuantity(entity.getQuantity());
        dTO.setUnitPrice(entity.getUnitPrice());
        dTO.setTableType(entity.getTableType());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setId(entity.getId());
    }

    private InstPurchaseInvoiceDetail mapFromDTO(InstPurchaseInvoiceDetailDTO dTO, IsagUser isagUser) {
        InstPurchaseInvoiceDetail entity = new InstPurchaseInvoiceDetail();
        if (dTO.getInstPurchaseInvoice() != null && dTO.getInstPurchaseInvoice().getId() != null) {
            InstPurchaseInvoice parent = new InstPurchaseInvoice();
            parent.setId(dTO.getInstPurchaseInvoice().getId());
            entity.setInstPurchaseInvoiceId(parent);
        }
        if (dTO.getItemId() != null) {
            entity.setItemId(dTO.getItemId().getId());
            entity.setTableType(dTO.getItemId().getTableType());
        }
        entity.setQuantity(dTO.getQuantity());
        entity.setUnitPrice(dTO.getUnitPrice());

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }
        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }
}
