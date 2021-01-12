package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.PrintProductionDTO;
import com.toby.entity.TobyUser;
import com.toby.views.ProductionReport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ProductionReportServiceImpl implements ProductionReportService{
    
    @EJB
    private GenericDAO dao;

    @Override
    public List<ProductionReport> searchReport(PrintProductionDTO productionDTO, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
//       params.put("branchId", tobyUser.getBranchId().getId());
        StringBuilder query = new StringBuilder();
        query.append("SELECT p FROM ProductionReport p where p.price >= 0 ");
        appendProProductionStages(productionDTO, params, query);
        appendProProductionInvoice(productionDTO, params, query);
        appendProProductionDate(productionDTO, params, query);
        return  dao.findListByQuery(query.toString(), params);
        

    }
    
    private void appendProProductionStages(PrintProductionDTO productionDTO, Map<String, Object> params, StringBuilder query) {
        if (productionDTO.getFromStatus() != null) {
            params.put("fromStatus", productionDTO.getFromStatus());
            query.append(" and p.productionStage >= :fromStatus ");
        }
        
        if (productionDTO.getToStatus() != null) {
            params.put("toStatus", productionDTO.getToStatus());
            query.append("and p.productionStage <= :toStatus ");
        }
    }
    
     private void appendProProductionInvoice(PrintProductionDTO productionDTO, Map<String, Object> params, StringBuilder query) {
        if (productionDTO.getFromInvoice()!= null) {
            params.put("fromInvoice", productionDTO.getFromInvoice().getSerial());
            query.append(" and p.invoiceSerial >= :fromInvoice ");
        }
        
        if (productionDTO.getToInvoice()!= null) {
            params.put("toInvoice", productionDTO.getToInvoice().getSerial());
            query.append("and p.invoiceSerial <= :toInvoice ");
        }
    }
     
     private void appendProProductionDate(PrintProductionDTO productionDTO, Map<String, Object> params, StringBuilder query) {
        if (productionDTO.getFromDate() != null) {
            params.put("fromDate", productionDTO.getFromDate());
            query.append(" and p.date >= :fromDate ");
        }

        if (productionDTO.getToDate() != null) {
            params.put("toDate", productionDTO.getToDate());
            query.append("and p.date <= :toDate ");
        }
    }
     



}
