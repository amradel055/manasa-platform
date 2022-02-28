package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.PrintProProductDTO;
import com.toby.views.ProductionMovementInvoiceReport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ProductionMovementInvoiceReportServiceImpl implements ProductionMovementInvoiceReportService{
    @EJB
    private GenericDAO dao;

    @Override
    public List<ProductionMovementInvoiceReport> getAll(PrintProProductDTO printProProductDTO){
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT p FROM ProductionMovementInvoiceReport p where p.rowNum >= 0 ");
        appendProProductionInvoice(printProProductDTO, params, query);
        appendProProductionDate(printProProductDTO, params, query);
        List<ProductionMovementInvoiceReport> invoiceReports = dao.findListByQuery(query.toString(), params);
        return invoiceReports;
    }

    private void appendProProductionInvoice(PrintProProductDTO printProProductDTO, Map<String, Object> params, StringBuilder query) {
        if (printProProductDTO.getFromInvoice() != null) {
            params.put("fromInvoice", printProProductDTO.getFromInvoice().getSerial());
            query.append(" and p.invoice >= :fromInvoice ");
        }
        if (printProProductDTO.getToInvoice() != null) {
            params.put("toInvoice", printProProductDTO.getToInvoice().getSerial());
            query.append(" and p.invoice <= :toInvoice ");
        }
    }

    private void appendProProductionDate(PrintProProductDTO printProProductDTO, Map<String, Object> params, StringBuilder query) {
        if (printProProductDTO.getFromDate() != null) {
            params.put("fromDate", printProProductDTO.getFromDate());
            query.append(" and p.receivedDate >= :fromDate ");
        }
        if (printProProductDTO.getToDate() != null) {
            params.put("toDate", printProProductDTO.getToDate());
            query.append(" and p.receivedDate <= :toDate ");
        }

    }

}
