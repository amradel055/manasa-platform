/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.TobyUser;
import com.toby.views.ProProductionTransactionOfInvoice;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author H
 */
@Stateless
public class ProProductionTransactionOfInvoiceServiceImpl implements ProProductionTransactionOfInvoiceService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ProProductionTransactionOfInvoice> getProProductionTransactionOfInvoiceListOfPourchais(Integer invPurchaseInvoiceDTOSerial, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("invPurchaseInvoiceserial", invPurchaseInvoiceDTOSerial);
        List<ProProductionTransactionOfInvoice> details = dao.findListByQuery("SELECT e FROM ProProductionTransactionOfInvoice e where e.branchId=:branchId and e.invPurchaseInvoiceserial=:invPurchaseInvoiceserial", params);
        return details;
    }

    @Override
    public ProProductionTransactionOfInvoice getRenaimNumberListOfproduction(Integer invPurchaseInvoiceId, Integer clothNumber, Integer itemId, Integer proProductionStage, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
//        params.put("productionLineId", productionLineId);
        params.put("itemId", itemId);
        params.put("proProductionStage", proProductionStage);
//     params.put("productionStagesId", productionStagesId);
        params.put("clothNumber", clothNumber);
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);
        ProProductionTransactionOfInvoice proProductionTransactionOfInvoice = dao.findEntityByQuery("SELECT e FROM ProProductionTransactionOfInvoice e where  e.invPurchaseInvoiceId=:invPurchaseInvoiceId and e.productionStagesId =:proProductionStage and e.itemId=:itemId  and e.clothNumber=:clothNumber and e.branchId=:branchId  ", params);

        return proProductionTransactionOfInvoice;
    }

    @Override
    public ProProductionTransactionOfInvoice getRenaimNumberListOfproduction(Integer invInvoiceSerial, Integer itemId, Integer proProductionStage, TobyUser tobyUser) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("itemId", itemId);
//        params.put("proProductionStage", proProductionStage);
//        params.put("branchId", tobyUser.getBranchId().getId());
//        params.put("invInvoiceSerial", invInvoiceSerial);
//        ProProductionTransactionOfInvoice proProductionTransactionOfInvoice = dao.findEntityByQuery("SELECT e FROM ProProductionTransactionOfInvoice e where  e.invPurchaseInvoiceserial =:invInvoiceSerial and e.productionStagesId =:proProductionStage and e.itemId=:itemId  and e.branchId=:branchId  ", params);

        StringBuilder query = new StringBuilder();
        query.append(" SELECT \n"
                + "        (SELECT \n"
                + "                SUM(`pd`.`number`)\n"
                + "            FROM\n"
                + "                ((`pro_factory_delivery_detail` `pd`\n"
                + "                LEFT JOIN `pro_factory_delivery` `p` ON ((`p`.`id` = `pd`.`pro_factory_delivery_id`)))\n"
                + "                LEFT JOIN `inv_purchase_invoice` `ipi2` ON ((`ipi2`.`id` = `p`.`inv_purchase_invoice_id`)))\n"
                + "            WHERE\n"
                + "                ((`pd`.`item_id` = `pfdd`.`item_id`)\n"
                + "                    AND (`ipi2`.`serial` = `ipi`.`serial`)\n"
                + "                    AND (`p`.`type` = 0))) AS `totalNumber`,\n"
                + "        SUM(`ppit`.`number`) AS `numberExcute`,\n"
                + "        (`pfdd`.`number` - SUM(`ppit`.`number`)) AS `numberRemain`,\n"
                + "        `ppit`.`pro_production_stages_id` AS `productionStagesId`,\n"
                + "        `pps`.`name` AS `productionStagesName`,\n"
                + "        `ppit`.`production_stage_cost` AS `productionStageCost`,\n"
                + "        `ppit`.`date` AS `date`,\n"
                + "        `pfdd`.`item_id` AS `itemId`,\n"
                + "        `ii`.`name` AS `itemName`,\n"
                + "        `ii`.`code` AS `itemCode`,\n"
                + "        `ipi`.`id` AS `invPurchaseInvoiceId`,\n"
                + "        `ipi`.`serial` AS `invPurchaseInvoiceserial`,\n"
                + "        `ii2`.`name` AS `ProductionLine`,\n"
                + "        `pfdd`.`id` AS `invoiceDetailId`,\n"
                + "        `pfdd`.`cloth_number` AS `clothNumber`,\n"
                + "        `pfdd`.`inv_item_parent_id` AS `ProductionLineId`,\n"
                + "        `ppit`.`branch_id` AS `branchId`,\n"
                + "        `ppit`.`created_by` AS `userId`,\n"
                + "        `tu`.`name` AS `userName`\n"
                + "    FROM\n"
                + "        (((((((`pro_production_items_transaction` `ppit`\n"
                + "        LEFT JOIN `pro_factory_delivery_detail` `pfdd` ON ((`ppit`.`pro_factory_delivery_detail_id` = `pfdd`.`id`)))\n"
                + "        LEFT JOIN `pro_factory_delivery` `pfd` ON ((`pfd`.`id` = `pfdd`.`pro_factory_delivery_id`)))\n"
                + "        LEFT JOIN `inv_purchase_invoice` `ipi` ON ((`ipi`.`id` = `pfd`.`inv_purchase_invoice_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `pfdd`.`item_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii2` ON ((`ii2`.`id` = `pfdd`.`inv_item_parent_id`)))\n"
                + "        LEFT JOIN `pro_production_stages` `pps` ON ((`pps`.`id` = `ppit`.`pro_production_stages_id`)))\n"
                + "        LEFT JOIN `toby_user` `tu` ON ((`tu`.`id` = `ppit`.`created_by`)))\n"
                + "    WHERE \n"
                + "        `pfdd`.`item_id` IS NOT NULL AND `pfd`.`type` = 0 AND `pfdd`.`item_id` = ").append(itemId).append(" AND `ppit`.`pro_production_stages_id` = ").append(proProductionStage).append("  and `ppit`.`branch_id`= ").append(tobyUser.getBranchId().getId()).append("  AND `ipi`.`serial`= ").append(invInvoiceSerial).append(" GROUP BY `pfdd`.`item_id`  , `ppit`.`pro_production_stages_id` , `pfdd`.`cloth_number` ");

        List<Object[]> res = dao.executeNativeQuery(query.toString());

        ProProductionTransactionOfInvoice list = null;

        if (res != null && !res.isEmpty()) {
            list = getProProductionTransactionOfInvoice(res);
        }
        return list;
    }

    public ProProductionTransactionOfInvoice getProProductionTransactionOfInvoice(List<Object[]> res) {

        ProProductionTransactionOfInvoice proProductionTransactionOfInvoice = new ProProductionTransactionOfInvoice();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            proProductionTransactionOfInvoice.setTotalNumber((BigDecimal) line[0]);
            proProductionTransactionOfInvoice.setNumberExcute((BigDecimal) line[1]);
            proProductionTransactionOfInvoice.setNumberRemain(proProductionTransactionOfInvoice.getTotalNumber().subtract(proProductionTransactionOfInvoice.getNumberExcute()));
            proProductionTransactionOfInvoice.setProductionStagesId((Integer) line[3]);
            proProductionTransactionOfInvoice.setProductionStagesName((String) line[4]);
            proProductionTransactionOfInvoice.setProductionStageCost((BigDecimal) line[5]);
            proProductionTransactionOfInvoice.setDate((Date) line[6]);
            proProductionTransactionOfInvoice.setItemId((Integer) line[7]);
            proProductionTransactionOfInvoice.setItemName((String) line[8]);
            proProductionTransactionOfInvoice.setItemCode((String) line[9]);
            proProductionTransactionOfInvoice.setInvPurchaseInvoiceId((Integer) line[10]);
            proProductionTransactionOfInvoice.setInvPurchaseInvoiceserial((Integer) line[11]);
//            proProductionTransactionOfInvoice.setProductionLine((String) line[12]);
            proProductionTransactionOfInvoice.setInvoiceDetailId((Integer) line[13]);
            proProductionTransactionOfInvoice.setClothNumber((Integer) line[14]);
            proProductionTransactionOfInvoice.setProductionLineId((Integer) line[15]);
//            proProductionTransactionOfInvoice.setBranchId((Integer) line[16]);
            proProductionTransactionOfInvoice.setUserId((Integer) line[17]);
            proProductionTransactionOfInvoice.setUserName((String) line[18]);

        }
        return proProductionTransactionOfInvoice;
    }

}
