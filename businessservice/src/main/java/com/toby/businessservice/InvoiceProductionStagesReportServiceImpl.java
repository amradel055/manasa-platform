package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.views.InvoiceProductionStagesReport;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class InvoiceProductionStagesReportServiceImpl implements InvoiceProductionStagesReportService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InvoiceMovementReportService invoiceMovementReportService;

    @Override
    public List<InvoiceProductionStagesReport> searchReport(PrintProductionDTO productionDTO, Integer branchId) {
        List<InvoiceProductionStagesReport> list = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT \n"
                + "        `ipi`.`serial` AS `invoiceSerial`,\n"
                + "        `ppit`.`date` AS `date`,\n"
                + "        `ios`.`code` AS `client`,\n"
                + "        `ios`.`name` AS `client_name`,\n"
                + "        `ppit`.`number` AS `quantity`,\n"
                + "        `pps`.`name` AS `stage`,\n"
                + "        `tu`.`name` AS `emp_name`,`ipid`.`cloth_number` as `clothNumber` ,\n"
                + "        `ii`.`name` AS `productName`\n"
                + "    FROM\n"
                + "        `pro_production_items_transaction` `ppit`\n"
                + "        left join pro_factory_delivery_detail pfdd on ppit.pro_factory_delivery_detail_id = pfdd.id\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipid`.`id` = `pfdd`.`inv_purchase_invoice_detail_id`\n"
                + "        LEFT JOIN `inv_item` `ii` ON `ii`.`id` = `pfdd`.`item_id`\n"
                + "        LEFT JOIN `inv_purchase_invoice` `ipi` ON `ipid`.`inv_purchase_invoice_id` = `ipi`.`id`\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON `ipi`.`organization_site_id` = `ios`.`id`\n"
                + "        LEFT JOIN `pro_production_stages` `pps` ON `ppit`.`pro_production_stages_id` = `pps`.`id`\n"
                + "        LEFT JOIN `toby_user` `tu` ON `ppit`.`created_by` = `tu`.`id`\n"
                + "	where ipi.id is not null and ipi.branch_id =  ").append(branchId).append(appendProProductionInvoice(productionDTO)).append(appendProProductionDate(productionDTO));
        query.append(" order by pps.serial");
        List<Object[]> res = dao.executeNativeQuery(query.toString());
        BigDecimal number = invoiceMovementReportService.summitionTob(productionDTO.getFromInvoice().getId());
        for (Object[] object : res) {
            InvoiceProductionStagesReport invoiceProductionStagesReport = new InvoiceProductionStagesReport();
            invoiceProductionStagesReport.setInvInvoice((Integer) object[0]);
            invoiceProductionStagesReport.setDate((Date) object[1]);
            invoiceProductionStagesReport.setClient((String) object[2]);
            invoiceProductionStagesReport.setClientName((String) object[3]);
            invoiceProductionStagesReport.setQuantity((BigDecimal) object[4]);
            invoiceProductionStagesReport.setStage((String) object[5]);
            invoiceProductionStagesReport.setEmpName((String) object[6]);
            invoiceProductionStagesReport.setClothNumber((Integer) object[7]);
            invoiceProductionStagesReport.setProductName((String) object[8]);
            invoiceProductionStagesReport.setNumber(number);

            list.add(invoiceProductionStagesReport);
        }

        return list;
    }

    private String appendProProductionInvoice(PrintProductionDTO productionDTO) {
        StringBuilder query = new StringBuilder();
        if (productionDTO.getFromInvoice() != null) {
            query.append(" and `ipi`.`serial` =  ").append(productionDTO.getFromInvoice().getSerial());
        }

//        if (productionDTO.getToInvoice() != null) {
//            query.append(" and `ipi`.`serial` <= ").append(productionDTO.getToInvoice().getSerial());
//        }
        if (!query.toString().isEmpty()) {
            return query.toString();
        }
        return "";
    }

    private String appendProProductionDate(PrintProductionDTO productionDTO) {
        StringBuilder query = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (productionDTO.getFromDate() != null) {
            String strDate = dateFormat.format(productionDTO.getFromDate());
            query.append(" and ppit.date >=  '").append(strDate).append("'");
        }

        if (productionDTO.getToDate() != null) {
            String endDate = dateFormat.format(productionDTO.getToDate());
            query.append(" and ppit.date <=  '").append(endDate).append("'");
        }
        if (!query.toString().isEmpty()) {
            return query.toString();
        }
        return "";
    }

    @Override
    public List<InvoiceProductionStagesReport> searchReportAPI(Integer serial, Integer gallaryId, Integer branchId) {
        PrintProductionDTO printProductionDTO = new PrintProductionDTO();
        InvInvoiceDTO invoiceDTO = new InvInvoiceDTO();
        invoiceDTO.setSerial(serial);
        printProductionDTO.setFromInvoice(invoiceDTO);
        return searchReport(printProductionDTO, branchId);
    }

}
