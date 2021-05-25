/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.PrintProductionDTO;
import com.toby.views.InvoiceMovementReport;
import java.math.BigDecimal;
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
 * @author Toby
 */
@Stateless
public class InvoiceMovementReportServiceImpl implements InvoiceMovementReportService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<InvoiceMovementReport> searchReport(PrintProductionDTO printProductionDTO, Integer branchId) {
        StringBuilder query = new StringBuilder();
        query.append("select ipi.serial as serial , ios.name as customerName , inv.name as inventoryName , pfd.date , pfd.type , ipi.id as id , pfd.id as idFactory , pfd.parent as parent  from pro_factory_delivery pfd \n").
                append(" left join inv_purchase_invoice ipi on ipi.id = pfd.inv_purchase_invoice_id\n").
                append(" left join inv_organization_site ios on ios.id = ipi.organization_site_id\n").
                append(" left join inv_inventory inv on inv.id = ipi.inv_inventory_id ").
                append(" left join inv_inventory inv2 on inv2.id = ipi.galary_Id ").
                append(" where ipi.branch_id = \n ").
                append(branchId).append(" ").
                append(appendProProductionInvoice(printProductionDTO)).
                append(appendGallary(printProductionDTO)).
                append(appendDateProFactoryDelivery(printProductionDTO)).
                append(" union all select ipi.serial as serial , ios.name as customerName , inv.name as inventoryName , ipi.date , 3 , ipi.id as id , null  as idFactory, null as parent  from inv_purchase_invoice ipi \n").
                append(" left join inv_organization_site ios on ios.id = ipi.organization_site_id\n").
                append(" left join inv_inventory inv on inv.id = ipi.inv_inventory_id ").
                append(" left join inv_inventory inv2 on inv2.id = ipi.galary_Id  ").
                append(" where ipi.type = 1 AND ipi.branch_id = \n ").
                append(branchId).append(" ").
                append(appendProProductionInvoice(printProductionDTO)).
                append(appendGallary(printProductionDTO)).
                append(appendDateInvoice(printProductionDTO));

        List<Object[]> res = dao.executeNativeQuery(query.toString());
        List<InvoiceMovementReport> list = new ArrayList<>();
        Map<String, InvoiceMovementReport> mapList = new HashMap<>();
        InvoiceMovementReport invoiceMovementReport;
        for (Object[] object : res) {
            Integer invoiceSerial = (Integer) object[0];
            Integer idFactory = (Integer) object[6];
            Integer parent = (Integer) object[7];
//            Long type = (Long) object[4];
//            int type2 = (int) type;
            long type = (Long) object[4];
            Date date = (Date) object[3];

            if (null == parent) {
                invoiceMovementReport = new InvoiceMovementReport();
                invoiceMovementReport.setInvoiceSerial((Integer) object[0]);
                invoiceMovementReport.setClientName((String) object[1]);
                invoiceMovementReport.setInventoryName((String) object[2]);
                invoiceMovementReport.setId((Integer) object[5]);
                invoiceMovementReport.setIdFactory((Integer) object[6]);
                invoiceMovementReport.setParent((Integer) object[7]);
                invoiceMovementReport.setParentCheck((invoiceMovementReport.getInvoiceSerial() + "" + invoiceMovementReport.getIdFactory()));
            } else {
                invoiceMovementReport = mapList.get((invoiceSerial + "" + parent));
                if (invoiceMovementReport != null) {
                    invoiceMovementReport.setParent(parent);
                    invoiceMovementReport.setParentCheck((invoiceSerial + "" + idFactory));
                }
            }

//            if (mapList.containsKey(idFactory)) {
//                invoiceMovementReport = mapList.get(idFactory);
//            } else {
//                invoiceMovementReport = new InvoiceMovementReport();
//                invoiceMovementReport.setInvoiceSerial((Integer) object[0]);
//                invoiceMovementReport.setClientName((String) object[1]);
//                invoiceMovementReport.setInventoryName((String) object[2]);
//                invoiceMovementReport.setId((Integer) object[5]);
//                invoiceMovementReport.setIdFactory((Integer) object[6]);
//            }
            if (invoiceMovementReport != null) {
                invoiceMovementReport.setNumberTobInvoice(summitionTob(invoiceMovementReport.getId()));

                if (type == 0) {
                    invoiceMovementReport.setFactoryDate(date);
                    invoiceMovementReport.setNumberToFactory(summitionTob(invoiceMovementReport.getIdFactory(), (int) type));

                } else if (type == 1) {
                    invoiceMovementReport.setDeliveryDate(date);
                    invoiceMovementReport.setNumberTobExitFactory(summitionTob(invoiceMovementReport.getIdFactory(), (int) type));
                }
                if (type == 2) {
                    invoiceMovementReport.setGalaryDate(date);
                    invoiceMovementReport.setNumberTobgallary(summitionTob(invoiceMovementReport.getIdFactory(), (int) type));
                }
                if (type == 3) {
                    invoiceMovementReport.setClientDate(date);
//                invoiceMovementReport.setNumberTobcustomer(summitionTob(invoiceMovementReport.getIdFactory(), (int) type));
                }

                if (null != invoiceMovementReport.getParent()) {
                    mapList.remove((invoiceSerial + "" + parent), invoiceMovementReport);
                }
                mapList.put(invoiceMovementReport.getParentCheck(), invoiceMovementReport);
            }

        }
        list = new ArrayList(mapList.values());
        return list;
    }

    @Override
    public BigDecimal summitionTob(Integer id) {
        if (id != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT sum(i.number) FROM InvPurchaseInvoiceDetail i WHERE  i.invPurchaseInvoiceId.id = :id  AND( i.itemId.proGroupId.id = 1 or i.itemId.proGroupId.id = 5)  ");
            builder.append(" group by i.itemId.proGroupId.id ");
            List<BigDecimal> res = dao.findListByQuery(builder.toString(), params);
            return (res != null && !res.isEmpty()) ? res.get(0) : BigDecimal.ZERO;
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal summitionTob(Integer id, Integer type) {
        if (id != null && type != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("type", type);
            StringBuilder builder = new StringBuilder();
            if (type == 0) {
                builder.append("SELECT sum(i.number) FROM ProFactoryDeliveryDetail i WHERE  i.proFactoryDeliveryId.id = :id AND i.proFactoryDeliveryId.type = :type  AND (i.itemId.proGroupId.id = 1 or i.itemId.proGroupId.id = 5)  ");
            } else {
                builder.append("SELECT sum(i.number) FROM ProFactoryDeliveryDetail i WHERE  i.proFactoryDeliveryId.parent.id = :id AND i.proFactoryDeliveryId.type = :type  AND (i.itemId.proGroupId.id = 1 or i.itemId.proGroupId.id = 5)  ");
            }
            builder.append(" group by i.itemId.proGroupId.id ");
            List<BigDecimal> res = dao.findListByQuery(builder.toString(), params);
            return (res != null && !res.isEmpty()) ? res.get(0) : BigDecimal.ZERO;
        } else {
            return BigDecimal.ZERO;
        }
    }

    private String appendProProductionInvoice(PrintProductionDTO productionDTO) {
        StringBuilder query = new StringBuilder();
        if (productionDTO.getFromInvoice() != null) {
            query.append(" and ipi.serial >=  ").append(productionDTO.getFromInvoice().getSerial());
        }
        if (productionDTO.getFromInvInvoice() != null) {
            query.append(" and ipi.serial >=  ").append(productionDTO.getFromInvInvoice());
        }

//        if (productionDTO.getToInvoice() != null) {
//            query.append(" and ipi.serial <=  ").append(productionDTO.getToInvoice().getSerial());
//        }
        
        if (productionDTO.getToInvInvoice() != null) {
            query.append(" and ipi.serial <=  ").append(productionDTO.getToInvInvoice());
        }
        if (!query.toString().isEmpty()) {
            return query.toString();
        }
        return "";
    }

    private String appendGallary(PrintProductionDTO productionDTO) {
        StringBuilder query = new StringBuilder();
        if (productionDTO.getGallaryFrom() != null) {
            query.append(" and inv2.code >=  '").append(productionDTO.getGallaryFrom().getCode()).append("'");
        }

        if (productionDTO.getGallaryTo() != null) {
            query.append(" and inv2.code <=  '").append(productionDTO.getGallaryTo().getCode()).append("'");
        }

        if (!query.toString().isEmpty()) {
            return query.toString();
        }

        return "";
    }

    private String appendDateInvoice(PrintProductionDTO productionDTO) {
        StringBuilder query = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (productionDTO.getFromDate() != null) {
            query.append(" and ipi.date >=  '").append(dateFormat.format(productionDTO.getFromDate())).append("'");
        }

        if (productionDTO.getToDate() != null) {
            query.append(" and ipi.date <=  '").append(dateFormat.format(productionDTO.getToDate())).append("'");
        }
        if (!query.toString().isEmpty()) {
            return query.toString();
        }
        return "";
    }

    private String appendDateProFactoryDelivery(PrintProductionDTO productionDTO) {
        StringBuilder query = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (productionDTO.getFromDate() != null) {
            query.append(" and pfd.date >=  '").append(dateFormat.format(productionDTO.getFromDate())).append("'");
        }

        if (productionDTO.getToDate() != null) {
            query.append(" and pfd.date <=  '").append(dateFormat.format(productionDTO.getToDate())).append("'");
        }
        if (!query.toString().isEmpty()) {
            return query.toString();
        }
        return "";
    }

}
