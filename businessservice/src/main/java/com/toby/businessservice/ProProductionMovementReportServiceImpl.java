/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.PrintProProductDTO;
import com.toby.entity.TobyUser;
import com.toby.views.ProProductionMovementReport;
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
public class ProProductionMovementReportServiceImpl implements ProProductionMovementReportService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ProProductionMovementReport> getProProductionMovementReportList(PrintProProductDTO printProProductDTO,TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
      params.put("branchId", tobyUser.getBranchId().getId());
        StringBuilder query = new StringBuilder();
        query.append("SELECT p FROM ProProductionMovementReport p where p.branchId=:branchId ");
        appendProProductionInvoice(printProProductDTO, params, query);
        appendProProductionDate(printProProductDTO, params, query);
        List<ProProductionMovementReport> productionTransactions = dao.findListByQuery(query.toString(), params);
        return productionTransactions;
    }

    private void appendProProductionInvoice(PrintProProductDTO printProProductDTO, Map<String, Object> params, StringBuilder query) {
        if (printProProductDTO.getFromInvoice() != null) {
            params.put("fromInvoice", printProProductDTO.getFromInvoice().getSerial());
            query.append(" and p.invoiceSerial >= :fromInvoice ");
        }
        if (printProProductDTO.getToInvoice() != null) {
            params.put("toInvoice", printProProductDTO.getToInvoice().getSerial());
            query.append(" and p.invoiceSerial <= :toInvoice ");
        }
    }

    private void appendProProductionDate(PrintProProductDTO printProProductDTO, Map<String, Object> params, StringBuilder query) {
        if (printProProductDTO.getFromDate() != null) {
            params.put("fromDate", printProProductDTO.getFromDate());
            query.append(" and p.creationDate >= :fromDate ");
        }
        if (printProProductDTO.getToDate() != null) {
            params.put("toDate", printProProductDTO.getToDate());
            query.append(" and p.creationDate <= :toDate ");
        }
    }
}
