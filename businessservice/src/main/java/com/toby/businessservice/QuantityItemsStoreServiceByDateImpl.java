/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.views.QuantityItemsStoreByDate;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author anady
 */
@Stateless
public class QuantityItemsStoreServiceByDateImpl implements QuantityItemsStoreByDateService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<QuantityItemsStoreByDate> findInventoryItemsList(Integer inventoryId, Integer branchId, Boolean sellBuy) {
        Map<String, Object> params = new HashMap();
        params.put("inventoryId", inventoryId);
        params.put("branchId", branchId);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT inv FROM QuantityItemsStoreByDate inv WHERE inv.inventoryId = :inventoryId AND inv.branchId = :branchId");
        if (sellBuy) {
            sql.append(" and inv.screenCode NOT IN (1 , 2)");
        } else {
            sql.append(" and inv.screenCode NOT IN (3 , 4)");
        }
        return dao.findListByQuery(sql.toString(), params);

    }

    @Override
    public synchronized BigDecimal findInventoryItem(Integer inventoryId, Integer branchId, Integer itemId, Boolean sellBuy, Date date) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap();
        params.put("inventoryId", inventoryId);
        params.put("branchId", branchId);
        params.put("itemId", itemId);
        params.put("date", date);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sum(inv.qty) FROM QuantityItemsStoreByDate inv WHERE inv.inventoryId = :inventoryId AND inv.branchId = :branchId AND inv.itemId = :itemId AND inv.date <= :date");
        if (sellBuy) {
            sql.append(" and inv.screenCode NOT IN (1 , 2)");
        } else {
            sql.append(" and inv.screenCode NOT IN (3 , 4)");
        }

        sql.append(" group by inv.itemId ");

        List<BigDecimal> itemsStores = dao.findListByQuery(sql.toString(), params);

        return (itemsStores != null && itemsStores.size() > 0) ? itemsStores.get(0) : BigDecimal.ZERO;
    }

    @Override
    public synchronized BigDecimal findInventoryItemByDate(Integer inventoryId, Integer branchId, Integer itemId, Date date) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap();
        params.put("inventoryId", inventoryId);
        params.put("branchId", branchId);
        params.put("itemId", itemId);
        params.put("date", date);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sum(inv.qty) FROM QuantityItemsStoreByDate inv WHERE inv.inventoryId = :inventoryId AND inv.branchId = :branchId AND inv.itemId = :itemId AND inv.date < :date");
        sql.append(" and inv.screenCode NOT IN (1 , 2)");

        sql.append(" group by inv.itemId ");

        List<BigDecimal> itemsStores = dao.findListByQuery(sql.toString(), params);

        return (itemsStores != null && itemsStores.size() > 0) ? itemsStores.get(0) : null;
    }

    @Override
    public List<QuantityItemsStoreByDate> findItemsForBranchList(Integer itemId, Integer branchId, Boolean sellBuy) {
        Map<String, Object> params = new HashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT inv FROM QuantityItemsStoreByDate inv WHERE inv.itemId = :itemId AND inv.branchId = :branchId AND inv.inventoryId != null");
        params.put("itemId", itemId);
        params.put("branchId", branchId);
        if (sellBuy) {
            sql.append(" and inv.screenCode NOT IN (1 , 2)");
        } else {
            sql.append(" and inv.screenCode NOT IN (3 , 4)");
        }
        return dao.findListByQuery(sql.toString(), params);

    }

    @Override
    public List<QuantityItemsStoreByDate> findAllQuantityItemsListStoreByBranchId(Integer branchId, Boolean sellBuy) {
        Map<String, Object> params = new HashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT inv FROM QuantityItemsStoreByDate inv WHERE inv.branchId = :branchId AND inv.inventoryId != null");
        params.put("branchId", branchId);
        if (sellBuy) {
            sql.append(" and inv.screenCode NOT IN (1 , 2)");
        } else {
            sql.append(" and inv.screenCode NOT IN (3 , 4)");
        }
        return dao.findListByQuery(sql.toString(), params);
    }
    
    @Override
    public BigDecimal getQuantatyItemByinventory(Integer invInventoryId, Integer invItemId, Boolean sellBuy) {

        StringBuilder query = new StringBuilder();

        query.append("SELECT (SUM((IFNULL(`i`.`qtyin`, 0) * IFNULL(`i`.`screwing`, 1))) - SUM((IFNULL(`i`.`qtyout`, 0) * IFNULL(`i`.`screwing`, 1)))) as qty\n"
                + "   FROM\n"
                + "   ( ");

        if (!sellBuy) {
            query.append(" SELECT \n"
                    + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
                    + "        0 AS `qtyout`,\n"
                    + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_purchase_invoice` `ipi`\n"
                    + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                    + "    WHERE\n"
                    + "        ((`ipi`.`type` = 0) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + "  AND `ipi`.`inv_inventory_id` = \n" + invInventoryId
                    + "    UNION ALL SELECT \n"
                    + "        0 AS `qtyin`,\n"
                    + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyout`,\n"
                    + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_purchase_invoice` `ipi`\n"
                    + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                    + "    WHERE\n"
                    + "        ((`ipi`.`type` = 1) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + "  AND `ipi`.`inv_inventory_id` =  " + invInventoryId);
        } else {
            query.append(" SELECT \n"
                    + "        0 AS `qtyin`,\n"
                    + "        IFNULL(`iaod`.`quantity`, 0) AS `qtyout`,\n"
                    + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_addingorder` `iao`\n"
                    + "        LEFT JOIN `inv_addingorder_detail` `iaod` ON `iao`.`id` = `iaod`.`addingorder_id`\n"
                    + "    WHERE\n"
                    + "         `iao`.`type` = 1  AND `iao`.`date` IS NOT NULL  AND `iaod`.`item_id` = " + invItemId + "  AND `iao`.`inv_inventory_id` = \n" + invInventoryId
                    + "    UNION ALL SELECT \n"
                    + "		IFNULL(`iaod`.`quantity`, 0) AS `qtyin`,\n"
                    + "        0 AS `qtyout`,\n"
                    + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_addingorder` `iao`\n"
                    + "        LEFT JOIN `inv_addingorder_detail` `iaod` ON `iao`.`id` = `iaod`.`addingorder_id`\n"
                    + "    WHERE\n"
                    + "    `iao`.`type` = 0  AND `iao`.`date` IS NOT NULL  AND `iaod`.`item_id` = " + invItemId + "  AND `iao`.`inv_inventory_id` =  " + invInventoryId);
        }

        query.append(" UNION ALL SELECT \n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`ird`.`amount`, 0) AS `qtyout`,\n"
                + "        IFNULL(`ird`.`screwing`, 1) AS `screwing`\n"
                + "    FROM\n"
                + "        `inv_reservation` `ir`\n"
                + "        LEFT JOIN `inv_reservation_detail` `ird` ON `ir`.`id` = `ird`.`reservation_id`\n"
                + "    WHERE\n"
                + "        `ir`.`reservation_date` IS NOT NULL\n"
                + "            AND `ir`.`end_date` >= (NOW() - INTERVAL 1 DAY) AND `ird`.`item_id` = " + invItemId + "  AND `ir`.`inv_id` = \n" + invInventoryId
                + "    UNION ALL SELECT \n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyout`,\n"
                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
                + "    FROM\n"
                + "        `inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 2) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + "  AND `ipi`.`inv_inventory_id` = \n" + invInventoryId
                + "    UNION ALL SELECT \n"
                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
                + "    FROM\n"
                + "        `inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 3) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + "  AND `ipi`.`inv_inventory_id` = \n" + invInventoryId
                + "    UNION ALL SELECT \n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`itd`.`amount`, 0) AS `qtyout`,\n"
                + "        IFNULL(`itd`.`screwing`, 1) AS `screwing`\n"
                + "    FROM\n"
                + "        `inv_transfer` `it`\n"
                + "        LEFT JOIN `inv_transfer_detail` `itd` ON `it`.`id` = `itd`.`inv_transfer_id`\n"
                + "    WHERE\n"
                + "        ((`it`.`transfer_date` IS NOT NULL)\n"
                + "            AND (`it`.`transfer_type` = 0))  AND `itd`.`inv_item_id` = " + invItemId + "  AND `it`.`inv_from` = \n" + invInventoryId
                + "    UNION ALL SELECT \n"
                + "        IFNULL(`itd`.`amount`, 0) AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`itd`.`screwing`, 1) AS `screwing`\n"
                + "    FROM\n"
                + "        `inv_transfer` `it`\n"
                + "        LEFT JOIN `inv_transfer_detail` `itd` ON `it`.`id` = `itd`.`inv_transfer_id`\n"
                + "    WHERE\n"
                + "        ((`it`.`transfer_date` IS NOT NULL)\n"
                + "            AND (`it`.`transfer_type` = 0))  AND `itd`.`inv_item_id` = " + invItemId + "  AND `it`.`inv_to` = \n" + invInventoryId
                + "    UNION ALL SELECT \n"
                + "        `isd`.`difference` AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`isd`.`screwing`, 1) AS `screwing`\n"
                + "    FROM\n"
                + "        `inv_update` `is`\n"
                + "        LEFT JOIN `inv_update_detail` `isd` ON `is`.`id` = `isd`.`inv_update_id`\n"
                + "    WHERE\n"
                + "        (`is`.`date` IS NOT NULL) AND `isd`.`inv_item_id` = " + invItemId + "  AND `is`.`inventory_id` = \n" + invInventoryId
                + "    UNION ALL SELECT \n"
                + "        `iobid`.`quantity` AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`iobid`.`screwing`, 1) AS `screwing`\n"
                + "    FROM\n"
                + "        `inv_openning_balance_item` `iobi`\n"
                + "        LEFT JOIN `inv_openning_balance_item_detail` `iobid` ON `iobi`.`id` = `iobid`.`inv_openning_balance_item_id`\n"
                + "    WHERE\n"
                + "        (`iobi`.`date` IS NOT NULL) AND `iobid`.`item_id` = " + invItemId + "  AND `iobi`.`inv_inventory_id` = " + +invInventoryId + ") as i");

        List<BigDecimal> quantity = dao.executeNativeQuery(query.toString());
        return (quantity != null && !quantity.isEmpty()) ? quantity.get(0) : BigDecimal.ZERO;

    }
    
//    @Override
//    public BigDecimal getQuantatyItemByinventory(Integer invInventoryId, Integer invItemId, Boolean sellBuy,Date date) {
//
//        StringBuilder query = new StringBuilder();
//
//        query.append("SELECT (SUM((IFNULL(`i`.`qtyin`, 0) * IFNULL(`i`.`screwing`, 1))) - SUM((IFNULL(`i`.`qtyout`, 0) * IFNULL(`i`.`screwing`, 1)))) as qty\n"
//                + "   FROM\n"
//                + "   ( ");
//            
//        if (!sellBuy) {
//            query.append(" SELECT \n"
//                    + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
//                    + "        0 AS `qtyout`,\n"
//                    + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
//                    + "    FROM\n"
//                    + "        `inv_purchase_invoice` `ipi`\n"
//                    + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
//                    + "    WHERE\n"
//                    + "        ((`ipi`.`type` = 0) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `ipi`.`date` < \'"+date+"\' ,true)"  
//                    + "  AND `ipi`.`inv_inventory_id` = \n" + invInventoryId
//                    + "    UNION ALL SELECT \n"
//                    + "        0 AS `qtyin`,\n"
//                    + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyout`,\n"
//                    + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
//                    + "    FROM\n"
//                    + "        `inv_purchase_invoice` `ipi`\n"
//                    + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
//                    + "    WHERE\n"
//                    + "        ((`ipi`.`type` = 1) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `ipi`.`date` < \'"+date+"\' ,true)"  + "  AND `ipi`.`inv_inventory_id` =  " + invInventoryId);
//        } else {
//            query.append(" SELECT \n"
//                    + "        0 AS `qtyin`,\n"
//                    + "        IFNULL(`iaod`.`quantity`, 0) AS `qtyout`,\n"
//                    + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`\n"
//                    + "    FROM\n"
//                    + "        `inv_addingorder` `iao`\n"
//                    + "        LEFT JOIN `inv_addingorder_detail` `iaod` ON `iao`.`id` = `iaod`.`addingorder_id`\n"
//                    + "    WHERE\n"
//                    + "         `iao`.`type` = 1  AND `iao`.`date` IS NOT NULL  AND `iaod`.`item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `iao`.`date` < \'"+date+"\' ,true)"  +"  AND `iao`.`inv_inventory_id` = \n" + invInventoryId
//                    + "    UNION ALL SELECT \n"
//                    + "		IFNULL(`iaod`.`quantity`, 0) AS `qtyin`,\n"
//                    + "        0 AS `qtyout`,\n"
//                    + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`\n"
//                    + "    FROM\n"
//                    + "        `inv_addingorder` `iao`\n"
//                    + "        LEFT JOIN `inv_addingorder_detail` `iaod` ON `iao`.`id` = `iaod`.`addingorder_id`\n"
//                    + "    WHERE\n"
//                    + "    `iao`.`type` = 0  AND `iao`.`date` IS NOT NULL  AND `iaod`.`item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `iao`.`date` < \'"+date+"\' ,true)"  +"  AND `iao`.`inv_inventory_id` =  " + invInventoryId);
//        }
//
//        query.append(" UNION ALL SELECT \n"
//                + "        0 AS `qtyin`,\n"
//                + "        IFNULL(`ird`.`amount`, 0) AS `qtyout`,\n"
//                + "        IFNULL(`ird`.`screwing`, 1) AS `screwing`\n"
//                + "    FROM\n"
//                + "        `inv_reservation` `ir`\n"
//                + "        LEFT JOIN `inv_reservation_detail` `ird` ON `ir`.`id` = `ird`.`reservation_id`\n"
//                + "    WHERE\n"
//                + "        `ir`.`reservation_date` IS NOT NULL\n"
//                + "            AND `ir`.`end_date` >= (NOW() - INTERVAL 1 DAY) AND `ird`.`item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `ir`.`end_date` < \'"+date+"\' ,true)"  +"  AND `ir`.`inv_id` = \n" + invInventoryId
//                + "    UNION ALL SELECT \n"
//                + "        0 AS `qtyin`,\n"
//                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyout`,\n"
//                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
//                + "    FROM\n"
//                + "        `inv_purchase_invoice` `ipi`\n"
//                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
//                + "    WHERE\n"
//                + "        ((`ipi`.`type` = 2) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `ipi`.`date` < \'"+date+"\' ,true)"  +"  AND `ipi`.`inv_inventory_id` = \n" + invInventoryId
//                + "    UNION ALL SELECT \n"
//                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
//                + "        0 AS `qtyout`,\n"
//                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
//                + "    FROM\n"
//                + "        `inv_purchase_invoice` `ipi`\n"
//                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
//                + "    WHERE\n"
//                + "        ((`ipi`.`type` = 3) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `ipi`.`date` < \'"+date+"\' ,true)"  +"  AND `ipi`.`inv_inventory_id` = \n" + invInventoryId
//                + "    UNION ALL SELECT \n"
//                + "        0 AS `qtyin`,\n"
//                + "        IFNULL(`itd`.`amount`, 0) AS `qtyout`,\n"
//                + "        IFNULL(`itd`.`screwing`, 1) AS `screwing`\n"
//                + "    FROM\n"
//                + "        `inv_transfer` `it`\n"
//                + "        LEFT JOIN `inv_transfer_detail` `itd` ON `it`.`id` = `itd`.`inv_transfer_id`\n"
//                + "    WHERE\n"
//                + "        ((`it`.`transfer_date` IS NOT NULL)\n"
//                + "            AND (`it`.`transfer_type` = 0))  AND `itd`.`inv_item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `it`.`transfer_date` < \'"+date+"\' ,true)"  +"  AND `it`.`inv_from` = \n" + invInventoryId
//                + "    UNION ALL SELECT \n"
//                + "        IFNULL(`itd`.`amount`, 0) AS `qtyin`,\n"
//                + "        0 AS `qtyout`,\n"
//                + "        IFNULL(`itd`.`screwing`, 1) AS `screwing`\n"
//                + "    FROM\n"
//                + "        `inv_transfer` `it`\n"
//                + "        LEFT JOIN `inv_transfer_detail` `itd` ON `it`.`id` = `itd`.`inv_transfer_id`\n"
//                + "    WHERE\n"
//                + "        ((`it`.`transfer_date` IS NOT NULL)\n"
//                + "            AND (`it`.`transfer_type` = 0))  AND `itd`.`inv_item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `it`.`transfer_date` < \'"+date+"\' ,true)"  +"  AND `it`.`inv_to` = \n" + invInventoryId
//                + "    UNION ALL SELECT \n"
//                + "        `isd`.`difference` AS `qtyin`,\n"
//                + "        0 AS `qtyout`,\n"
//                + "        IFNULL(`isd`.`screwing`, 1) AS `screwing`\n"
//                + "    FROM\n"
//                + "        `inv_update` `is`\n"
//                + "        LEFT JOIN `inv_update_detail` `isd` ON `is`.`id` = `isd`.`inv_update_id`\n"
//                + "    WHERE\n"
//                + "        (`is`.`date` IS NOT NULL) AND `isd`.`inv_item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `is`.`date` < \'"+date+"\' ,true)"  +"  AND `is`.`inventory_id` = \n" + invInventoryId
//                + "    UNION ALL SELECT \n"
//                + "        `iobid`.`quantity` AS `qtyin`,\n"
//                + "        0 AS `qtyout`,\n"
//                + "        IFNULL(`iobid`.`screwing`, 1) AS `screwing`\n"
//                + "    FROM\n"
//                + "        `inv_openning_balance_item` `iobi`\n"
//                + "        LEFT JOIN `inv_openning_balance_item_detail` `iobid` ON `iobi`.`id` = `iobid`.`inv_openning_balance_item_id`\n"
//                + "    WHERE\n"
//                + "        (`iobi`.`date` IS NOT NULL) AND `iobid`.`item_id` = " + invItemId + " AND IF ( \'"+date+"\' IS NOT NULL , `iobi`.`date` < \'"+date+"\' ,true)"  +"  AND `iobi`.`inv_inventory_id` = " + +invInventoryId + ") as i");
//
//        List<BigDecimal> quantity = dao.executeNativeQuery(query.toString());
//        return (quantity != null && !quantity.isEmpty()) ? (quantity.get(0) != null ? quantity.get(0)  : BigDecimal.ZERO) : BigDecimal.ZERO;
//
//    }
    
    
    
    
    @Override
        public BigDecimal getQuantatyItemByinventory(Integer invInventoryId, Integer invItemId, Boolean sellBuy, Date date) {

            StringBuilder query = new StringBuilder();
            String dateStr = null;
            if (date != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateStr = dateFormat.format(date);

            }
            query.append("SELECT (SUM((IFNULL(`i`.`qtyin`, 0) * IFNULL(`i`.`screwing`, 1))) - SUM((IFNULL(`i`.`qtyout`, 0) * IFNULL(`i`.`screwing`, 1)))) as qty\n"
                    + "   FROM\n"
                    + "   ( ");

            if (!sellBuy) {
                query.append(" SELECT \n"
                        + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
                        + "        0 AS `qtyout`,\n"
                        + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
                        + "    FROM\n"
                        + "        `inv_purchase_invoice` `ipi`\n"
                        + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                        + "    WHERE\n"
                        + "        ((`ipi`.`type` = 0) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + " ");
                if (date != null) {
                    query.append(" AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `ipi`.`date` <= '" ).append(dateStr).append(  "' ,true)");
                }
                if (invInventoryId != null) {
                    query.append("  AND `ipi`.`inv_inventory_id` = \n").append(invInventoryId);
                }
                query.append("  UNION ALL SELECT \n"
                        + "        0 AS `qtyin`,\n"
                        + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyout`,\n"
                        + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
                        + "    FROM\n"
                        + "        `inv_purchase_invoice` `ipi`\n"
                        + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                        + "    WHERE\n"
                        + "        ((`ipi`.`type` = 1) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + " ");
                if (date != null) {
                    query.append("AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `ipi`.`date` <= '" ).append(dateStr).append(  "' ,true) ");
                }
                if (invInventoryId != null) {
                    query.append("  AND `ipi`.`inv_inventory_id` = \n").append(invInventoryId);
                }
            } else {
                query.append(" SELECT \n"
                        + "        0 AS `qtyin`,\n"
                        + "        IFNULL(`iaod`.`quantity`, 0) AS `qtyout`,\n"
                        + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`\n"
                        + "    FROM\n"
                        + "        `inv_addingorder` `iao`\n"
                        + "        LEFT JOIN `inv_addingorder_detail` `iaod` ON `iao`.`id` = `iaod`.`addingorder_id`\n"
                        + "    WHERE\n"
                        + "         `iao`.`type` = 1  AND `iao`.`date` IS NOT NULL  AND `iaod`.`item_id` = " + invItemId + " ");
                if (date != null) {
                    query.append("AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `iao`.`date` <= '" ).append(dateStr).append(  "' ,true)");
                }
                if (invInventoryId != null) {
                    query.append("  AND `iao`.`inv_inventory_id` = \n").append(invInventoryId);
                }
                query.append(" UNION ALL SELECT \n"
                        + "		IFNULL(`iaod`.`quantity`, 0) AS `qtyin`,\n"
                        + "        0 AS `qtyout`,\n"
                        + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`\n"
                        + "    FROM\n"
                        + "        `inv_addingorder` `iao`\n"
                        + "        LEFT JOIN `inv_addingorder_detail` `iaod` ON `iao`.`id` = `iaod`.`addingorder_id`\n"
                        + "    WHERE\n"
                        + "    `iao`.`type` = 0  AND `iao`.`date` IS NOT NULL  AND `iaod`.`item_id` = " + invItemId + " ");
                if (date != null) {
                    query.append("AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `iao`.`date` <= '" ).append(dateStr).append(  "' ,true)");
                }
                if (invInventoryId != null) {
                    query.append("  AND `iao`.`inv_inventory_id` = \n").append(invInventoryId);
                }
            }

            query.append(" UNION ALL SELECT \n"
                    + "        0 AS `qtyin`,\n"
                    + "        IFNULL(`ird`.`amount`, 0) AS `qtyout`,\n"
                    + "        IFNULL(`ird`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_reservation` `ir`\n"
                    + "        LEFT JOIN `inv_reservation_detail` `ird` ON `ir`.`id` = `ird`.`reservation_id`\n"
                    + "    WHERE\n"
                    + "        `ir`.`reservation_date` IS NOT NULL\n"
                    + "            AND `ir`.`end_date` >= (NOW() - INTERVAL 1 DAY) AND `ird`.`item_id` = " + invItemId + "");
            if (date != null) {
                query.append(" AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `ir`.`end_date` <= '" ).append(dateStr).append(  "' ,true)");
            }
            if (invInventoryId != null) {
                query.append("  AND `ir`.`inv_id` = \n").append(invInventoryId);
            }
            query.append(" UNION ALL SELECT \n"
                    + "        0 AS `qtyin`,\n"
                    + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyout`,\n"
                    + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_purchase_invoice` `ipi`\n"
                    + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                    + "    WHERE\n"
                    + "        ((`ipi`.`type` = 2) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + "");
            if (date != null) {
                query.append(" AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `ipi`.`date` <= '" ).append(dateStr).append(  "' ,true)");
            }
            if (invInventoryId != null) {
                query.append("  AND `ipi`.`inv_inventory_id` = \n").append(invInventoryId);
            }
            query.append(" UNION ALL SELECT \n"
                    + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
                    + "        0 AS `qtyout`,\n"
                    + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_purchase_invoice` `ipi`\n"
                    + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                    + "    WHERE\n"
                    + "        ((`ipi`.`type` = 3) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + " ");
            if (date != null) {
                query.append(" AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `ipi`.`date` <= '" ).append(dateStr).append(  "' ,true)");
            }
            if (invInventoryId != null) {
                query.append("  AND `ipi`.`inv_inventory_id` = \n").append(invInventoryId);
            }
            query.append(" UNION ALL SELECT \n"
                    + "        0 AS `qtyin`,\n"
                    + "        IFNULL(`itd`.`amount`, 0) AS `qtyout`,\n"
                    + "        IFNULL(`itd`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_transfer` `it`\n"
                    + "        LEFT JOIN `inv_transfer_detail` `itd` ON `it`.`id` = `itd`.`inv_transfer_id`\n"
                    + "    WHERE\n"
                    + "        ((`it`.`transfer_date` IS NOT NULL)\n"
                    + "            AND (`it`.`transfer_type` = 0))  AND `itd`.`inv_item_id` = " + invItemId + " ");
            if (date != null) {
                query.append(" AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `it`.`transfer_date` <= '" ).append(dateStr).append(  "' ,true)");
            }
            if (invInventoryId != null) {
                query.append("  AND `it`.`inv_from` = \n").append(invInventoryId);
            }
            query.append(" UNION ALL SELECT \n"
                    + "        IFNULL(`itd`.`amount`, 0) AS `qtyin`,\n"
                    + "        0 AS `qtyout`,\n"
                    + "        IFNULL(`itd`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_transfer` `it`\n"
                    + "        LEFT JOIN `inv_transfer_detail` `itd` ON `it`.`id` = `itd`.`inv_transfer_id`\n"
                    + "    WHERE\n"
                    + "        ((`it`.`transfer_date` IS NOT NULL)\n"
                    + "            AND (`it`.`transfer_type` = 0))  AND `itd`.`inv_item_id` = " + invItemId + " ");
            if (date != null) {
                query.append(" AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `it`.`transfer_date` <= '" ).append(dateStr).append(  "' ,true)");
            }
            if (invInventoryId != null) {
                query.append("  AND `it`.`inv_to` = \n").append(invInventoryId);
            }
            query.append(" UNION ALL SELECT \n"
                    + "        `isd`.`difference` AS `qtyin`,\n"
                    + "        0 AS `qtyout`,\n"
                    + "        IFNULL(`isd`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_update` `is`\n"
                    + "        LEFT JOIN `inv_update_detail` `isd` ON `is`.`id` = `isd`.`inv_update_id`\n"
                    + "    WHERE\n"
                    + "        (`is`.`date` IS NOT NULL) AND `isd`.`inv_item_id` = " + invItemId + " ");
            if (date != null) {
                query.append(" AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `is`.`date` <= '" ).append(dateStr).append(  "' ,true)");
            }
            if (invInventoryId != null) {
                query.append("  AND `is`.`inventory_id` = \n").append(invInventoryId);
            }
            query.append(" UNION ALL SELECT \n"
                    + "        `iobid`.`quantity` AS `qtyin`,\n"
                    + "        0 AS `qtyout`,\n"
                    + "        IFNULL(`iobid`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_openning_balance_item` `iobi`\n"
                    + "        LEFT JOIN `inv_openning_balance_item_detail` `iobid` ON `iobi`.`id` = `iobid`.`inv_openning_balance_item_id`\n"
                    + "    WHERE\n"
                    + "        (`iobi`.`date` IS NOT NULL) AND `iobid`.`item_id` = " + invItemId + " ");
            if (date != null) {
                query.append(" AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `iobi`.`date` <= '" ).append(dateStr).append(  "' ,true)");
            }
            if (invInventoryId != null) {
                query.append("  AND `iobi`.`inv_inventory_id` = \n").append(invInventoryId);
            }
            query.append(") as i");

            List<BigDecimal> quantity = dao.executeNativeQuery(query.toString());
            return (quantity != null && !quantity.isEmpty()) ? (quantity.get(0) != null ? quantity.get(0) : BigDecimal.ZERO) : BigDecimal.ZERO;

        }

    @Override
    public BigDecimal getQuantatyItemByinventoryRecalcCostAvarage(Integer invInventoryId, Integer invItemId, Boolean sellBuy, Date date) {

        StringBuilder query = new StringBuilder();
        String dateStr = null;
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateStr = dateFormat.format(date);

        }
        query.append("SELECT (SUM((IFNULL(`i`.`qtyin`, 0) * IFNULL(`i`.`screwing`, 1))) - SUM((IFNULL(`i`.`qtyout`, 0) * IFNULL(`i`.`screwing`, 1)))) as qty\n"
                + "   FROM\n"
                + "   ( ");

        if (!sellBuy) {
            query.append(" SELECT \n"
                    + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
                    + "        0 AS `qtyout`,\n"
                    + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_purchase_invoice` `ipi`\n"
                    + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                    + "    WHERE\n"
                    + "        ((`ipi`.`type` = 0) AND (`ipi`.`date` IS NOT NULL)) AND `ipid`.`item_id` = " + invItemId + " ");
            if (date != null) {
                query.append(" AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `ipi`.`date` <= '" ).append(dateStr).append(  "' ,true)");
            }
            if (invInventoryId != null) {
                query.append("  AND `ipi`.`inv_inventory_id` = \n").append(invInventoryId);
            }
        } else {
            query.append(" SELECT \n"
                    + "        0 AS `qtyin`,\n"
                    + "        IFNULL(`iaod`.`quantity`, 0) AS `qtyout`,\n"
                    + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_addingorder` `iao`\n"
                    + "        LEFT JOIN `inv_addingorder_detail` `iaod` ON `iao`.`id` = `iaod`.`addingorder_id`\n"
                    + "    WHERE\n"
                    + "         `iao`.`type` = 1  AND `iao`.`date` IS NOT NULL  AND `iaod`.`item_id` = " + invItemId + " ");
            if (date != null) {
                query.append("AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `iao`.`date` <= '" ).append(dateStr).append(  "' ,true)");
            }
            if (invInventoryId != null) {
                query.append("  AND `ipi`.`inv_inventory_id` = \n").append(invInventoryId);
            }
            query.append(" UNION ALL SELECT \n"
                    + "		IFNULL(`iaod`.`quantity`, 0) AS `qtyin`,\n"
                    + "        0 AS `qtyout`,\n"
                    + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`\n"
                    + "    FROM\n"
                    + "        `inv_addingorder` `iao`\n"
                    + "        LEFT JOIN `inv_addingorder_detail` `iaod` ON `iao`.`id` = `iaod`.`addingorder_id`\n"
                    + "    WHERE\n"
                    + "    `iao`.`type` = 0  AND `iao`.`date` IS NOT NULL  AND `iaod`.`item_id` = " + invItemId + " ");
            if (date != null) {
                query.append("AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `iao`.`date` <= '" ).append(dateStr).append(  "' ,true)");
            }
            if (invInventoryId != null) {
                query.append("  AND `ipi`.`inv_inventory_id` = \n").append(invInventoryId);
            }
        }

        query.append(" UNION ALL SELECT \n"
                + "        `iobid`.`quantity` AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`iobid`.`screwing`, 1) AS `screwing`\n"
                + "    FROM\n"
                + "        `inv_openning_balance_item` `iobi`\n"
                + "        LEFT JOIN `inv_openning_balance_item_detail` `iobid` ON `iobi`.`id` = `iobid`.`inv_openning_balance_item_id`\n"
                + "    WHERE\n"
                + "        (`iobi`.`date` IS NOT NULL) AND `iobid`.`item_id` = " + invItemId + " ");
        if (date != null) {
            query.append(" AND IF ( '" ).append(dateStr).append(  "' IS NOT NULL , `iobi`.`date` <= '" ).append(dateStr).append(  "' ,true)");
        }
        if (invInventoryId != null) {
            query.append("  AND `ipi`.`inv_inventory_id` = \n").append(invInventoryId);
        }
        query.append(") as i");

        List<BigDecimal> quantity = dao.executeNativeQuery(query.toString());
        return (quantity != null && !quantity.isEmpty()) ? (quantity.get(0) != null ? quantity.get(0) : BigDecimal.ZERO) : BigDecimal.ZERO;

    }
}
