/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.core.storedProcedureParameter;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.InvItem;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.ProFactoryDelivery;
import com.toby.entity.ProFactoryDeliveryDetail;
import com.toby.entity.TobyUser;
import com.toby.procedure.FactoryNotLoadedYetProFactoeyProcedure;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class ProFactoryDeliveryDetailServiceImpl implements ProFactoryDeliveryDetailService {

    @EJB
    private GenericDAO dao;
    @EJB
    ProGlobalService proGlobalService;
    @EJB
    private GenericService genericService;
    @EJB
    private InvPurchaseInvoiceDetailDetailService invPurchaseInvoiceDetailDetailService;
    @EJB
    private ProProductionTransactionService proProductionTransactionService;
    @EJB
    InvGlobalService invGlobalService;

    @Override
    public List<InvDetailDTO> getAllProPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO dto) {
        List<InvDetailDTO> invDetailDTOList = null;
        if (dto.getNeedLoadedSerial() != null && dto.getNeedLoadedSerial() != 0) {
            invDetailDTOList = getAllProFactoryDeliveryDetailListNotLoaded(dto);
        } else {
            invDetailDTOList = getAllProFactoryDeliveryDetailList(dto);
        }

        return invDetailDTOList;
    }

    @Override
    public List<InvDetailDTO> getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO dto, Integer typeto, Integer typefrom) {
        List<InvDetailDTO> invDetailDTOList = null;
        if (dto.getNeedLoadedSerial() != null && dto.getNeedLoadedSerial() != 0) {
//            invDetailDTOList = getAllProFactoryDeliveryDetailListNotLoaded(dto);
            invoiceNotLoadedYetProFactoeyProcedure(dto.getInvPurchaseInvoiceId(), typeto, typefrom);
            List<FactoryNotLoadedYetProFactoeyProcedure> list = dao.findListByQuery("SELECT t FROM FactoryNotLoadedYetProFactoeyProcedure t");
            invDetailDTOList = returnListDTO(list, dto);
        } else {
            invDetailDTOList = getAllProFactoryDeliveryDetailList(dto);
        }

        return invDetailDTOList;
    }

    @Override
    public List<InvDetailDTO> getAllProFactoryDeliveryDetailList(InvInvoiceDTO dto) {
        List<ProFactoryDeliveryDetail> details;
        List<InvDetailDTO> invDetailDTOList;
        Map<String, Object> params = new HashMap<>();
        params.put("id", dto.getId());
        details = dao.findListByQuery("SELECT e FROM ProFactoryDeliveryDetail e WHERE e.proFactoryDeliveryId.id = :id", params);
        invDetailDTOList = returnListDTO(details, dto);
        return invDetailDTOList;
    }

    @Override
    public BigDecimal findquantityAllow(Integer serial, String item_barcode) {
        List<BigDecimal> details;
//        details = dao.executeNativeQuery("SELECT  (sum(ipid.number) - sum(pfdd.number)) as diffrent FROM toby.pro_factory_delivery_detail pfdd \n"
//                + "left join pro_factory_delivery pfd on pfd.id = pfdd.pro_factory_delivery_id\n"
//                + "left join inv_purchase_invoice ipi on ipi.id = pfd.inv_purchase_invoice_id\n"
//                + "left join inv_purchase_invoice_detail ipid on ipi.id = ipid.inv_purchase_invoice_id\n"
//                + "where ipi.id  = " + serial + " and pfdd.item_barcode = " + item_barcode + " and pfd.type = 0");
        details = dao.executeNativeQuery("SELECT (sum(s.diffrent))  from  (\n"
                + "SELECT  (sum(ipid.number)) as diffrent FROM  inv_purchase_invoice ipi \n"
                + "                left join inv_purchase_invoice_detail ipid on ipi.id = ipid.inv_purchase_invoice_id\n"
                + "                where ipi.id  = " + serial + " and ipid.item_barcode = " + item_barcode + "\n"
                + "union all \n"
                + "SELECT  (sum(pfdd.number) * -1) as diffrent FROM  toby.pro_factory_delivery_detail pfdd \n"
                + "                left join pro_factory_delivery pfd on pfd.id = pfdd.pro_factory_delivery_id\n"
                + "                where pfd.inv_purchase_invoice_id  = " + serial + " and pfdd.item_barcode = " + item_barcode + " and  pfd.type = 0\n"
                + ") s ;");
        if (details != null && details.size() > 0) {
            return details.get(0);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<InvDetailDTO> getAllProFactoryDeliveryDetailListNotLoaded(InvInvoiceDTO dto) {
        List<ProFactoryDeliveryDetail> details;
        List<InvDetailDTO> invDetailDTOList;
        Map<String, Object> params = new HashMap<>();
        params.put("id", dto.getId());
        details = dao.findListByQuery("SELECT e FROM ProFactoryDeliveryDetail e WHERE e.proFactoryDeliveryId.invPurchaseInvoiceId.id = :id AND"
                + "e.id not in (select i.invPurchaseInvoiceDetailId.id from ProFactoryDeliveryDetail i where i.proFactoryDeliveryId.invPurchaseInvoiceId.id = :id) e.proFactoryDeliveryId.id = :id", params);
        invDetailDTOList = returnListDTO(details, dto);
        return invDetailDTOList;
    }

    private void invoiceNotLoadedYetProFactoeyProcedure(Integer inv_purchase_order_id1, Integer typeto, Integer typefrom) {
        List<storedProcedureParameter> procedureParameter = new ArrayList<>();
        procedureParameter.add(new storedProcedureParameter("inv_purchase_invoice_id1", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("typeto", Integer.class, 'i'));
        procedureParameter.add(new storedProcedureParameter("typefrom", Integer.class, 'i'));
        Map<String, Object> params2 = new HashMap<>();
        params2.put("inv_purchase_invoice_id1", inv_purchase_order_id1);
        params2.put("typeto", typeto);
        params2.put("typefrom", typefrom);
        dao.StoredProcedure("factory_not_loaded_yet_Pro_Factoey_procedure", procedureParameter, params2);
    }

    private <T> List<InvDetailDTO> returnListDTO(List<T> list, InvInvoiceDTO dto) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        for (T entity : list) {
            dTOList.add(mapTODTO(entity, dto));
        }
        return dTOList;
    }

    private <T> InvDetailDTO mapTODTO(T Entity, InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvDetailDTO dTO = initMapToDTO(Entity, invPurchaseInvoiceDTO);
        return dTO;
    }

    private <T> InvDetailDTO initMapToDTO(T Entity, InvInvoiceDTO invInvoiceDTO) {

        InvDetailDTO dTO = new InvDetailDTO();
        if (Entity.getClass().equals(ProFactoryDeliveryDetail.class)) {
            ProFactoryDeliveryDetail entity = (ProFactoryDeliveryDetail) Entity;
            dTO = proGlobalService.fillItemDetail(invInvoiceDTO.getIsOld(), dTO,
                    entity.getId(),
                    entity.getInvPurchaseInvoiceDetailId() != null ? entity.getInvPurchaseInvoiceDetailId().getId() : null, entity.getSerial(), (entity.getBranchId() != null ? entity.getBranchId().getId() : null),
                    (entity.getProFactoryDeliveryId() != null ? entity.getProFactoryDeliveryId().getId() : null),
                    entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                    entity.getItemId().getId(), entity.getItemId().getCode(), entity.getItemId().getName(), entity.getClothNumber(),
                    entity.getItemId().getUnitId().getSerial(), entity.getItemId().getUnitId().getName(),
                    entity.getNumber(), entity.getItemId() == null ? null : entity.getItemId().getId(),
                    entity.getInvItemParentId() == null ? null : entity.getInvItemParentId().getCode(),
                    entity.getInvItemParentId() == null ? null : entity.getInvItemParentId().getName(),
                    invInvoiceDTO.getCompanyActivity(), invInvoiceDTO.getScreenName(), BigDecimal.ZERO);
            dTO = invPurchaseInvoiceDetailDetailService.findDetailDetail(dTO, invInvoiceDTO.getScreenName());
            if (proProductionTransactionService.checkFinish(invInvoiceDTO.getInvPurchaseInvoiceId(), invInvoiceDTO.getId(), dTO.getClothNumber())) {
                dTO.setStatusName("تم التشيك");
            }
        } else if (Entity.getClass().equals(InvPurchaseInvoiceDetail.class)) {
            InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) Entity;

            dTO = proGlobalService.fillItemDetail(false, dTO,
                    entity.getId(),
                    entity.getId() != null ? entity.getId() : null,
                    entity.getSerial(), null,
                    null,
                    entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                    entity.getItemId().getId(), entity.getItemId().getCode(), entity.getItemId().getName(), entity.getClothNumber(),
                    entity.getItemId().getUnitId().getSerial(), entity.getItemId().getUnitId().getName(),
                    entity.getNumber(), entity.getItemId() == null ? null : entity.getItemId().getId(),
                    entity.getInvItemParentId() == null ? null : entity.getInvItemParentId().getCode(),
                    entity.getInvItemParentId() == null ? null : entity.getInvItemParentId().getName(),
                    invInvoiceDTO.getCompanyActivity(), invInvoiceDTO.getScreenName(), entity.getCostAvarage());
        } else if (Entity.getClass().equals(FactoryNotLoadedYetProFactoeyProcedure.class)) {
            FactoryNotLoadedYetProFactoeyProcedure entity = (FactoryNotLoadedYetProFactoeyProcedure) Entity;

            dTO = proGlobalService.fillItemDetail(false, dTO,
                    null,
                    null,
                    null, null,
                    null,
                    entity.getItemId(), null, null,
                    entity.getItemId(), entity.getItemBarcode(), entity.getItemName(), null,
                    null, null,
                    entity.getNumber(), null,
                    null,
                    null,
                    invInvoiceDTO.getCompanyActivity(), invInvoiceDTO.getScreenName(), BigDecimal.ZERO);

        }
        return dTO;
    }

    @Override
    public String addProFactoryDeliveryDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            ProFactoryDeliveryDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), tobyUser);
                if (detail.getMsg() != null && !detail.getMsg().isEmpty()) {
                    error = detail.getMsg();
                    return error;
                } else {
                    dao.updateEntity(detail);
                }
            }
        }

        return error;
    }

    private ProFactoryDeliveryDetail mapToEntity(InvDetailDTO dTO, Integer proFactoryDeliveryId, TobyUser tobyUser) {
        ProFactoryDeliveryDetail entity = new ProFactoryDeliveryDetail();

        entity.setSerial(dTO.getSerial());
        entity.setClothNumber(dTO.getClothNumber());
        entity.setNumber(dTO.getNumber());

        if (dTO.getInvPurchaseInvoiceDetailId() != null) {
            InvPurchaseInvoiceDetail invPurchaseInvoiceDetail = new InvPurchaseInvoiceDetail();
            invPurchaseInvoiceDetail.setId(dTO.getInvPurchaseInvoiceDetailId());
            entity.setInvPurchaseInvoiceDetailId(invPurchaseInvoiceDetail);
        }

        if (proFactoryDeliveryId != null) {
            ProFactoryDelivery proFactoryDelivery = new ProFactoryDelivery();
            proFactoryDelivery.setId(proFactoryDeliveryId);
            entity.setProFactoryDeliveryId(proFactoryDelivery);
        }

        if (dTO.getItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemId().getId());
            entity.setItemId(invItem);
            entity.setItemBarcode(dTO.getItemsBarcodesDetail() == null ? dTO.getItemCode() : dTO.getItemsBarcodesDetail().getBarcode());
        }

        if (dTO.getInvItemParentId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getInvItemParentId().getId());
            entity.setInvItemParentId(invItem);
        }

        if (dTO.getId() == null) {
            if (entity.getSerial() == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("proFactoryDeliveryId", proFactoryDeliveryId);
                Integer max = genericService.getMaxGenericSerialForDetail(ProFactoryDeliveryDetail.class, "proFactoryDeliveryId", proFactoryDeliveryId);
                entity.setSerial(max);
            }
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setCompanyId(tobyUser.getCompanyId());
        entity.setBranchId(tobyUser.getBranchId());
        return entity;
    }

    @Override
    public void deleteInvDetail(InvDetailDTO detailDTO) {
        if (detailDTO.getId() != null) {
            ProFactoryDeliveryDetail proFactoryDeliveryDetail = new ProFactoryDeliveryDetail();
            proFactoryDeliveryDetail.setId(detailDTO.getId());
            dao.deleteEntity(proFactoryDeliveryDetail);
        }
    }

    @Override
    public void deleteProFactoryDeliveryDetailListDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList) {
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {
                if (detailDTO.getId() != null) {
                    ProFactoryDeliveryDetail proFactoryDeliveryDetail = new ProFactoryDeliveryDetail();
                    proFactoryDeliveryDetail.setId(detailDTO.getId());
                    dao.deleteEntity(proFactoryDeliveryDetail);
                }
            }
        }

    }

}
