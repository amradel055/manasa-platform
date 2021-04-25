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
import com.toby.entity.ProFactoryDelivery;
import com.toby.entity.ProFactoryDeliveryDetail;
import com.toby.entity.ProProductionItemsTransaction;
import com.toby.entity.ProProductionStages;
import com.toby.entity.ProProductionTransaction;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.ProProductionTransactionFactoryView;
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
    @EJB
    InvoiceMovementReportService movementReportService;

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

        if (proProductionItemsTransaction.getProFactoryDeliveryDetailId() != null
                && proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId() != null) {
            InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO = new InvPurchaseInvoiceDetailDTO();
            invPurchaseInvoiceDetailDTO.setId(proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getId());
            if (proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getInvPurchaseInvoiceId() != null) {
                InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO = new InvPurchaseInvoiceDTO1();
                invPurchaseInvoiceDTO.setId(proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getInvPurchaseInvoiceId().getId());
                invPurchaseInvoiceDTO.setSerial(proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getInvPurchaseInvoiceId().getSerial());
                invPurchaseInvoiceDTO.setDate(proProductionItemsTransaction.getProFactoryDeliveryDetailId().getInvPurchaseInvoiceDetailId().getInvPurchaseInvoiceId().getDate());
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

        sql.append(" group by ppit.created_by , ppit.pro_production_stages_id order by pps.serial ");

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

    @Override
    public List<ProProductionItemsTransactionDTO> getALLProProductionItemsTransactionDTO(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM ProProductionItemsTransaction s WHERE  s.branchId.id = :branchId and( s.isDeleted != 1 or s.isDeleted is NULL )  ");
        // appendFilter(filters, builder);
        List<ProProductionItemsTransaction> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return mapToDTOList(details, null);
    }

    @Override
    public List<ProProductionItemsTransactionDTO> getALLProProductionItemsTransactionDTO(ProProductionTransactionFactoryView transactionFactoryView, Integer branchId) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT s FROM ProProductionItemsTransaction s WHERE  s.branchId.id = :branchId and( s.isDeleted != 1 or s.isDeleted is NULL )  ");
        appendProProductionStatus1(transactionFactoryView, builder, params);
        appendProProductionDate1(transactionFactoryView, builder, params);
        appendProProductionUser1(transactionFactoryView, builder, params);
        appendProProductionInvoiceNumber1(transactionFactoryView, builder, params);
        appendProProductionGellery1(transactionFactoryView, builder, params);
        List<ProProductionItemsTransaction> details = dao.findListByQuery(builder.toString(), params);
        return mapToDTOList(details, null);
    }

    private void appendProProductionStatus1(ProProductionTransactionFactoryView transactionFactoryView, StringBuilder query, Map<String, Object> params) {
        if (transactionFactoryView != null && transactionFactoryView.getStatus() != null) {
            params.put("status", transactionFactoryView.getStatus().getId());
            query.append(" and s.proProductionStagesId.id = :status  ");
        }
    }

    private void appendProProductionDate1(ProProductionTransactionFactoryView transactionFactoryView, StringBuilder query, Map<String, Object> params) {
        if (transactionFactoryView != null && transactionFactoryView.getDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//            params.put("date", dateFormat.format(transactionFactoryView.getDate()));
            params.put("date", transactionFactoryView.getDate());
            query.append(" and s.proFactoryDeliveryDetailId.invPurchaseInvoiceDetailId.invPurchaseInvoiceId.date = :date ");
        }
    }

    private void appendProProductionUser1(ProProductionTransactionFactoryView transactionFactoryView, StringBuilder query, Map<String, Object> params) {
        if (transactionFactoryView != null && transactionFactoryView.getUser() != null) {
            params.put("user", transactionFactoryView.getUser().getId());
            query.append(" and s.createdBy.id = :user ");
        }
    }

    private void appendProProductionInvoiceNumber1(ProProductionTransactionFactoryView transactionFactoryView, StringBuilder query, Map<String, Object> params) {
        if (transactionFactoryView != null && transactionFactoryView.getInvoiceNumber() != null && transactionFactoryView.getInvoiceNumber() > 0) {
            params.put("invoiceNumber", transactionFactoryView.getInvoiceNumber());
            query.append(" and s.proFactoryDeliveryDetailId.invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial = :invoiceNumber ");
        }
    }

    private void appendProProductionGellery1(ProProductionTransactionFactoryView transactionFactoryView, StringBuilder query, Map<String, Object> params) {
        if (transactionFactoryView != null && transactionFactoryView.getGallerySelected() != null) {
            params.put("galleryList", transactionFactoryView.getGallerySelected().getId());
            query.append(" and s.proFactoryDeliveryDetailId.invPurchaseInvoiceDetailId.invPurchaseInvoiceId.galaryId.id = :galleryList ");
        }
    }

    private void appendProProductionDate(ProProductionTransactionFactoryView transactionFactoryView) {
        StringBuilder query = new StringBuilder();
        if (transactionFactoryView.getDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append(" and ipi.date >=  '").append(dateFormat.format(transactionFactoryView.getDate())).append("'");
        }
        if (transactionFactoryView.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append(" and ipi.date <=  '").append(dateFormat.format(transactionFactoryView.getDateTo())).append("'");
        }

    }

    private void appendProProductionGellery(ProProductionTransactionFactoryView transactionFactoryView) {
        StringBuilder query = new StringBuilder();
        if (transactionFactoryView != null && transactionFactoryView.getGallerySelected() != null) {
            query.append(" ipi.galary_Id =  '").append(transactionFactoryView.getGallerySelected().getId()).append("' ");
        }
    }

    private String appendFilter(ProProductionTransactionFactoryView transactionFactoryView) {
        StringBuilder query = new StringBuilder();
        if (transactionFactoryView != null) {
            query.append(" and s.id = (select max(p.id) From ProProductionItemsTransaction p where p.proFactoryDeliveryDetailId.invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial = s.proFactoryDeliveryDetailId.invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial ) order by s.proFactoryDeliveryDetailId.invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial  ");
        }
        if (!query.toString().isEmpty()) {
            return query.toString();
        }

        return "";
    }

    @Override
    public List<ProProductionItemsTransactionDTO> getALLProProductionItemsTransactionLastDTO(ProProductionTransactionFactoryView transactionFactoryView, Integer branchId) {
        List<ProProductionItemsTransactionDTO> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ipi.serial , tu.name , tu.user_code , pps.name , pit.date , ipi.date , pit.pro_production_stages_id , pit.pro_factory_delivery_detail_id,ipi.id\n").
                append("from toby.pro_production_items_transaction pit \n").
                append("left join pro_factory_delivery_detail pfdd on pit.pro_factory_delivery_detail_id = pfdd.id \n").
                append("left join pro_factory_delivery pfd on pfd.id = pfdd.pro_factory_delivery_id\n").
                append("left join inv_purchase_invoice ipi on ipi.id = pfd.inv_purchase_invoice_id \n").
                append("left join toby_user tu on tu.id = pit.created_by\n").
                append("left join pro_production_stages pps on pps.id = pit.pro_production_stages_id\n").
                append("WHERE pfd.branch_id = \n  ").
                append(branchId).append(" ");
        appendProProductionDate(transactionFactoryView);
        appendProProductionGellery(transactionFactoryView);
//        builder.append("and  pfd.id not in (select parent from toby.pro_factory_delivery where type = 1 and parent is not null ) AND pit.id IN (\n").
//                append("    SELECT MAX(pit.id)\n").
//                append("    from toby.pro_production_items_transaction pit \n").
//                append("left join pro_factory_delivery_detail pfdd on pit.pro_factory_delivery_detail_id = pfdd.id \n").
//                append("left join pro_factory_delivery pfd on pfd.id = pfdd.pro_factory_delivery_id\n").
//                append("    GROUP BY pfd.id\n").
//                append(") order by ipi.serial ");
        builder.append("and  pfd.id not in (select parent from toby.pro_factory_delivery where type = 1 and parent is not null ) "
                + "AND pit.id = (\n").
                append("    select ppit2.id from pro_production_items_transaction  ppit2\n"
                        + "left join pro_production_stages pps on pps.id = ppit2.pro_production_stages_id \n"
                        + "left join pro_factory_delivery_detail pfdd2 on pfdd2.id = ppit2.pro_factory_delivery_detail_id\n"
                        + "left join pro_factory_delivery pfd2 on pfd2.id = pfdd2.pro_factory_delivery_id "
                        + "where  pfd2.id = pfd.id\n"
                        + "order by pps.serial desc limit 1 ) order by ipi.serial ");
        List<Object[]> details = dao.executeNativeQuery(builder.toString());
        for (Object[] object : details) {
            ProProductionItemsTransactionDTO itemsTransactionDTO = new ProProductionItemsTransactionDTO();
            itemsTransactionDTO.setInvoiceSerial((Integer) object[0]);
            itemsTransactionDTO.setCreatedByName((String) object[1]);
            itemsTransactionDTO.setCreatedByCode((String) object[2]);
            itemsTransactionDTO.setStageName((String) object[3]);
            itemsTransactionDTO.setOrderDate((Date) object[4]);
            itemsTransactionDTO.setInvoiceDate((Date) object[5]);
            itemsTransactionDTO.setId((Integer) object[8]);
            itemsTransactionDTO.setClothNumber(movementReportService.summitionTob(itemsTransactionDTO.getId()));

            list.add(itemsTransactionDTO);
        }

        builder = new StringBuilder();
        builder.append("select ipi.id , ipi.serial , ipi.date from pro_factory_delivery pfd2\n"
                + "	left join inv_purchase_invoice ipi on ipi.id = pfd2.inv_purchase_invoice_id\n"
                + "               where pfd2.type = 0 and pfd2.id  not in (select distinct(pfd.id)\n"
                + "                from pro_production_items_transaction ppit\n"
                + "                Left join pro_factory_delivery_detail pfdd on pfdd.id = ppit.pro_factory_delivery_detail_id\n"
                + "                LEFT JOIN pro_factory_delivery pfd on pfd.id = pfdd.pro_factory_delivery_id\n"
                + "                where ppit.pro_factory_delivery_detail_id is not null \n"
                + "                and pfd.id is not null)");

        List<Object[]> details1 = dao.executeNativeQuery(builder.toString());
        for (Object[] object : details1) {
            ProProductionItemsTransactionDTO itemsTransactionDTO = new ProProductionItemsTransactionDTO();
            itemsTransactionDTO.setId((Integer) object[0]);
            itemsTransactionDTO.setInvoiceSerial((Integer) object[1]);
            itemsTransactionDTO.setInvoiceDate((Date) object[2]);
            itemsTransactionDTO.setClothNumber(movementReportService.summitionTob(itemsTransactionDTO.getId()));

            list.add(itemsTransactionDTO);
        }
        return list;
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        builder.append(" SELECT count(s) FROM ProProductionItemsTransaction s WHERE  s.branchId.id = :branchId and( s.isDeleted != 1 or s.isDeleted is NULL ) ");
//        appendFilter(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

//   private void appendFilter(Map<String, Object> filters, StringBuilder builder) {
//        if (filters.get("invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial") != null
//                && !filters.get("invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial").toString().isEmpty()) {
//
//            builder.append(" and s.invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial like CONCAT('%',:invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial ,'%')");
//        } else if (filters.containsKey("invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial")) {
//            filters.remove("invPurchaseInvoiceDetailId.invPurchaseInvoiceId.serial");
//        }
//        if (filters.get("createdByName") != null && !filters.get("createdByName").toString().isEmpty()) {
////            builder.append(" and  s.createdBy IS NOT NULL and s.createdBy.name like CONCAT('%',:createdByName ,'%') ");
//            builder.append("  and t.name like CONCAT('%',").append("'").append(filters.get("createdByName")).append("'").append(" ,'%')  ");
//        } else if (filters.containsKey("createdByName")) {
//            filters.remove("createdByName");
//        }
//        if (filters.get("createdByCode") != null && !filters.get("createdByCode").toString().isEmpty()) {
//            builder.append(" and s.createdBy.userCode like CONCAT('%',:createdByCode ,'%') ");
//        } else if (filters.containsKey("createdByCode")) {
//            filters.remove("createdByCode");
//        }
//        if (filters.get("proProductionStagesId.name") != null && !filters.get("proProductionStagesId.name").toString().isEmpty()) {
//            builder.append(" and s.proProductionStagesId.name like CONCAT('%',:proProductionStagesId.name ,'%')");
//        } else if (filters.containsKey("proProductionStagesId.name")) {
//            filters.remove("proProductionStagesId.name");
//        }
//        if (filters.get("date") != null && !filters.get("date").toString().isEmpty()) {
//            builder.append(" and ( FUNCTION('DATE_FORMAT',s.date, '%d-%m-%Y')  like CONCAT('%',:date ,'%') ");
//        } else if (filters.containsKey("date")) {
//            filters.remove("date");
//        }
//    }
}
