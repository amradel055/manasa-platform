/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InvPurchaseInvoiceDetailDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductionItemsTransactionDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.dto.ProProductionTransactionDetailDetailViewDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.ProFactoryDeliveryDetail;
import com.toby.entity.ProProductionItemsTransaction;
import com.toby.entity.ProProductionStages;
import com.toby.entity.ProProductionTransaction;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.ProductionTransactionOfUser;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author H
 */
@Stateless
public class ProProductionItemsTransactionServiceImpl implements ProProductionItemsTransactionService {

    @EJB
    GenericDAO dao;

    @Override
    public ProProductionItemsTransactionDTO deleteproProductionItemsTransactionDTO(ProProductionItemsTransactionDTO proProductionItemsTransactionDTO, TobyUser tobyUser) {
//        ProProductionItemsTransaction proProductionItemsTransaction = mapToEntity(proProductionItemsTransactionDTO, tobyUser);
//        proProductionItemsTransaction.setIsDeleted(1);
//        dao.updateEntity(proProductionItemsTransaction);
        ProProductionItemsTransaction proProductionItemsTransaction = new ProProductionItemsTransaction();
        proProductionItemsTransaction.setId(proProductionItemsTransactionDTO.getId());
        dao.deleteEntity(proProductionItemsTransaction);

        return mapToDTO(proProductionItemsTransaction);

    }

    public ProProductionItemsTransaction mapToEntity(ProProductionItemsTransactionDTO proProductionItemsTransactionDTO, TobyUser tobyUser) {
        ProProductionItemsTransaction proProductionItemsTransaction = new ProProductionItemsTransaction();
        proProductionItemsTransaction.setId(proProductionItemsTransactionDTO.getId());
        proProductionItemsTransaction.setDate(proProductionItemsTransactionDTO.getDate());
        proProductionItemsTransaction.setNumber(proProductionItemsTransactionDTO.getNumber());
        proProductionItemsTransaction.setProductionStageCost(proProductionItemsTransactionDTO.getProductionStageCost());
        proProductionItemsTransaction.setSerial(proProductionItemsTransactionDTO.getSerial());
        proProductionItemsTransaction.setStatus(proProductionItemsTransactionDTO.getStatus());

        if (proProductionItemsTransactionDTO.getInvPurchaseInvoiceDetailId() != null) {
            InvPurchaseInvoiceDetail invPurchaseInvoiceDetail = new InvPurchaseInvoiceDetail();
            invPurchaseInvoiceDetail.setId(proProductionItemsTransactionDTO.getInvPurchaseInvoiceDetailId().getId());
            proProductionItemsTransaction.setInvPurchaseInvoiceDetailId(invPurchaseInvoiceDetail);
        }

        if (proProductionItemsTransactionDTO.getProProductionStagesId() != null) {
            ProProductionStages proProductionStages = new ProProductionStages();
            proProductionStages.setId(proProductionItemsTransactionDTO.getProProductionStagesId().getId());
            proProductionItemsTransaction.setProProductionStagesId(proProductionStages);
        }
        if (proProductionItemsTransactionDTO.getProProductionTransactionId() != null) {
            ProProductionTransaction proProductionTransaction = new ProProductionTransaction();
            proProductionTransaction.setId(proProductionItemsTransactionDTO.getProProductionTransactionId().getId());
            proProductionItemsTransaction.setProProductionTransactionId(proProductionTransaction);
        }

        return proProductionItemsTransaction;
    }

    @Override
    public ProProductionItemsTransaction saveProProductionItemsTransaction(ProProductionTransactionDetailDetailViewDTO proProductionTransactionDetailDetailViewDTOSelected, ProProductionTransactionDTO proProductionTransactionDTO, TobyUser tobyUser) {
        ProProductionItemsTransaction proProductionItemsTransaction = mapToEntity1(proProductionTransactionDetailDetailViewDTOSelected, proProductionTransactionDTO, tobyUser);
        dao.updateEntity(proProductionItemsTransaction);
        return proProductionItemsTransaction;
    }

    public ProProductionItemsTransaction mapToEntity1(ProProductionTransactionDetailDetailViewDTO proProductionTransactionDetailDetailViewDTOSelected, ProProductionTransactionDTO proProductionTransactionDTO, TobyUser tobyUser) {
        ProProductionItemsTransaction proProductionItemsTransaction = new ProProductionItemsTransaction();
        proProductionItemsTransaction.setSerial(1);
        proProductionItemsTransaction.setNumber(proProductionTransactionDetailDetailViewDTOSelected.getNumberExcute());
        proProductionItemsTransaction.setStatus(1);
        proProductionItemsTransaction.setDate(new Date());
        proProductionItemsTransaction.setProductionStageCost(proProductionTransactionDetailDetailViewDTOSelected.getCost());

        if (proProductionTransactionDetailDetailViewDTOSelected.getProFactoryDeliveryDetailId() != null) {
            ProFactoryDeliveryDetail deliveryDetail = new ProFactoryDeliveryDetail();
            deliveryDetail.setId(proProductionTransactionDetailDetailViewDTOSelected.getProFactoryDeliveryDetailId());
//            proProductionItemsTransaction.setInvPurchaseInvoiceDetailId(invPurchaseInvoiceDetail);
            proProductionItemsTransaction.setProFactoryDeliveryDetailId(deliveryDetail);
        }
        if (proProductionTransactionDTO != null) {
            ProProductionStages proProductionStages = new ProProductionStages();
            proProductionStages.setId(proProductionTransactionDTO.getProProductionId().getId());
            proProductionItemsTransaction.setProProductionStagesId(proProductionStages);
        }
        if (proProductionTransactionDetailDetailViewDTOSelected.getCreatedBy() != null) {

            TobyUser user = new TobyUser();
            user.setId(proProductionTransactionDetailDetailViewDTOSelected.getCreatedBy());
            proProductionItemsTransaction.setCreatedBy(user);
            proProductionItemsTransaction.setCreationDate(proProductionTransactionDetailDetailViewDTOSelected.getCreatedDate());
            proProductionItemsTransaction.setModifiedBy(tobyUser);
            proProductionItemsTransaction.setModificationDate(new Date());
            if (proProductionTransactionDetailDetailViewDTOSelected.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(proProductionTransactionDetailDetailViewDTOSelected.getCompanyId());
                proProductionItemsTransaction.setCompanyId(company);
            }

            if (proProductionTransactionDetailDetailViewDTOSelected.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(proProductionTransactionDetailDetailViewDTOSelected.getBranchId());
                proProductionItemsTransaction.setBranchId(branch);
            }
        } else {
            proProductionItemsTransaction.setCreatedBy(tobyUser);
            proProductionItemsTransaction.setCreationDate(new Date());
            proProductionItemsTransaction.setCompanyId(tobyUser.getCompanyId());
            proProductionItemsTransaction.setBranchId(tobyUser.getBranchId());
        }

        return proProductionItemsTransaction;
    }

    @Override
    public List<ProProductionItemsTransactionDTO> getProProductionItemsTransaction(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<ProProductionItemsTransaction> proProductionItemsTransaction = dao.findListByQuery("SELECT e FROM ProProductionItemsTransaction e where e.branchId.id =:branchId and( e.isDeleted != 1 or e.isDeleted is NULL ) order by e.id desc", params);
        return mapToDTOList(proProductionItemsTransaction, tobyUser);
    }

    public List<ProProductionItemsTransactionDTO> mapToDTOList(List<ProProductionItemsTransaction> ProProductionItemsTransactionlist, TobyUser tobyUser) {
        List<ProProductionItemsTransactionDTO> invInventoryTransactionDetailDTOList = new ArrayList<>();
        for (ProProductionItemsTransaction proProductionItemsTransaction : ProProductionItemsTransactionlist) {
            invInventoryTransactionDetailDTOList.add(mapToDTO(proProductionItemsTransaction));
        }
        return invInventoryTransactionDetailDTOList;
    }

    public ProProductionItemsTransactionDTO mapToDTO(ProProductionItemsTransaction proProductionItemsTransaction) {

        ProProductionItemsTransactionDTO proProductionItemsTransactionDTO = new ProProductionItemsTransactionDTO();

        proProductionItemsTransactionDTO.setId(proProductionItemsTransaction.getId());
        proProductionItemsTransactionDTO.setCreatedDate(proProductionItemsTransaction.getCreationDate());
        proProductionItemsTransactionDTO.setDate(proProductionItemsTransaction.getDate());
        proProductionItemsTransactionDTO.setIndex(proProductionItemsTransaction.getId());

        if (proProductionItemsTransaction.getProFactoryDeliveryDetailId() != null && 
                proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId() != null) {
            InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO = new InvPurchaseInvoiceDetailDTO();
            invPurchaseInvoiceDetailDTO.setId(proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getId());
            if (proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getInvPurchaseInvoiceId() != null) {
                InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO = new InvPurchaseInvoiceDTO1();
                invPurchaseInvoiceDTO.setId(proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getInvPurchaseInvoiceId().getId());
                invPurchaseInvoiceDTO.setSerial(proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getInvPurchaseInvoiceId().getSerial());
                invPurchaseInvoiceDetailDTO.setInvPurchaseInvoiceId(invPurchaseInvoiceDTO);
            }
            if (proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getItemId() != null) {
                InvItemDTO invItemDTO = new InvItemDTO();
                invItemDTO.setId((proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getItemId().getId()));
                invItemDTO.setName((proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getItemId().getName()));
                invPurchaseInvoiceDetailDTO.setItemId(invItemDTO);
            }
            invPurchaseInvoiceDetailDTO.setId(proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getId());
            proProductionItemsTransactionDTO.setInvPurchaseInvoiceDetailId(invPurchaseInvoiceDetailDTO);
        }

        proProductionItemsTransactionDTO.setNumber(proProductionItemsTransaction.getNumber());

        if (proProductionItemsTransaction.getProProductionStagesId() != null) {
            ProProductionStagesDTO proProductionStagesDTO = new ProProductionStagesDTO();
            proProductionStagesDTO.setId(proProductionItemsTransaction.getProProductionStagesId().getId());
            proProductionStagesDTO.setName(proProductionItemsTransaction.getProProductionStagesId().getName());
            proProductionItemsTransactionDTO.setProProductionStagesId(proProductionStagesDTO);

        }

        if (proProductionItemsTransaction.getProProductionTransactionId() != null) {
            ProProductionTransactionDTO proProductionTransactionDTO = new ProProductionTransactionDTO();
            proProductionTransactionDTO.setId(proProductionItemsTransaction.getProProductionTransactionId().getId());

            proProductionItemsTransactionDTO.setProProductionTransactionId(proProductionTransactionDTO);
        }
        proProductionItemsTransactionDTO.setStatus(proProductionItemsTransaction.getStatus());
        proProductionItemsTransactionDTO.setSerial(proProductionItemsTransaction.getSerial());

        proProductionItemsTransactionDTO.setCreatedBy(proProductionItemsTransaction.getCreatedBy() != null ? proProductionItemsTransaction.getCreatedBy().getId() : null);
        proProductionItemsTransactionDTO.setCreatedByName(proProductionItemsTransaction.getCreatedBy() != null ? proProductionItemsTransaction.getCreatedBy().getName() : null);
        proProductionItemsTransactionDTO.setCreatedByCode(proProductionItemsTransaction.getCreatedBy() != null ? proProductionItemsTransaction.getCreatedBy().getUserCode() : null);
        proProductionItemsTransactionDTO.setCreatedDate(proProductionItemsTransaction.getCreationDate());
        proProductionItemsTransactionDTO.setBranchId(proProductionItemsTransaction.getBranchId() != null ? proProductionItemsTransaction.getBranchId().getId() : null);

        return proProductionItemsTransactionDTO;
    }

    @Override
    public List<ProductionTransactionOfUser> getProProductionTransactionDTOForAllUsersList(PrintProductionDTO printProductionDTO, Integer branchId) {

        List<ProductionTransactionOfUser> productionTransactionOfUsers = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pps.serial as stageSerial , pps.name as stageName , \n"
                + "tu.user_code as userCode , tu.name as userName , \n"
                + "sum(ppit.number) as amount , \n"
                + "avg(ppit.production_stage_cost) as price, \n"
                + "sum(ppit.number * ppit.production_stage_cost) as total,"
                + " ((sum(ppit.number * ppit.production_stage_cost)) / 26) as ava\n"
                + "FROM toby.pro_production_items_transaction ppit\n"
                + "left join toby_user tu on tu.id = ppit.created_by\n"
                + "left join pro_production_stages pps on pps.id = ppit.pro_production_stages_id\n"
                + " ");

        sql.append(" where ppit.branch_id = ").append(branchId);

        appendProProductionStatus(printProductionDTO, sql);
        appendProProductionDate(printProductionDTO, sql);
        appendProProductionUser(printProductionDTO, sql);

        sql.append(" group by ppit.created_by , ppit.pro_production_stages_id");

        List<Object[]> res = dao.executeNativeQuery(sql.toString());

        for (Object[] object : res) {
            ProductionTransactionOfUser productionTransactionOfUser = new ProductionTransactionOfUser();
            productionTransactionOfUser.setProductionStagesId((Integer) object[0]);
            productionTransactionOfUser.setStageName((String) object[1]);
            productionTransactionOfUser.setUserCode((String) object[2]);
            productionTransactionOfUser.setUserName((String) object[3]);
            productionTransactionOfUser.setNumberExcute((BigDecimal) object[4]);
            productionTransactionOfUser.setPrice((BigDecimal) object[5]);
            productionTransactionOfUser.setTotalNumber((BigDecimal) object[6]);
            productionTransactionOfUser.setAvarage(((BigDecimal) object[7]).setScale(3, RoundingMode.HALF_UP));

            productionTransactionOfUsers.add(productionTransactionOfUser);
        }
        return productionTransactionOfUsers;
    }

    private void appendProProductionStatus(PrintProductionDTO printProductionDTO, StringBuilder query) {
        if (printProductionDTO.getFromStatus() != null) {
            query.append(" and pps.serial >=  ").append(printProductionDTO.getFromStatus());
        }
        if (printProductionDTO.getToStatus() != null) {
            query.append(" and pps.serial <=  ").append(printProductionDTO.getToStatus());
        }
    }

    private void appendProProductionDate(PrintProductionDTO printProductionDTO, StringBuilder query) {
        if (printProductionDTO.getFromDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append(" and ppit.date  >=  '").append(dateFormat.format(printProductionDTO.getFromDate())).append("' ");
        }
        if (printProductionDTO.getToDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            query.append(" and ppit.date  <=  '").append(dateFormat.format(printProductionDTO.getToDate())).append("' ");
        }
    }

    private void appendProProductionUser(PrintProductionDTO printProductionDTO, StringBuilder query) {
        if (printProductionDTO.getFromUser() != null) {
            query.append(" and tu.user_code >=  '").append(printProductionDTO.getFromUser().getUserCode()).append("' ");
        }
        if (printProductionDTO.getFromUser() != null) {
            query.append(" and tu.user_code <=  '").append(printProductionDTO.getToUser().getUserCode()).append("' ");
        }
    }

}
