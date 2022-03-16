/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.PrintProductionDTO;
import com.toby.entity.TobyUser;
import com.toby.views.ProductionTransactionOfUser;
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
public class ProductionTransactionOfUserServiceImpl  implements ProductionTransactionOfUserService{
@EJB
    private GenericDAO dao;

    @Override
    public List<ProductionTransactionOfUser> getProductionTransactionOfUserList(PrintProductionDTO printProductionDTO, TobyUser tobyUser) {
              Map<String, Object> params = new HashMap<>();
      params.put("branchId", tobyUser.getBranchId().getId());
        StringBuilder query = new StringBuilder();
        query.append("SELECT p FROM ProductionTransactionOfUser p where p.branchId=:branchId ");
        appendProProductionStatus(printProductionDTO, params, query);
        appendProProductionDate(printProductionDTO, params, query);
        appendProProductionUser(printProductionDTO, params, query);
        List<ProductionTransactionOfUser> productionTransactionOfUsers = dao.findListByQuery(query.toString(), params);
        return productionTransactionOfUsers;
    }
       private void appendProProductionStatus(PrintProductionDTO printProductionDTO, Map<String, Object> params, StringBuilder query) {
        if (printProductionDTO.getFromStatus() != null) {
            params.put("fromStatus", printProductionDTO.getFromStatus());
            query.append(" and p.productionStagesId >= :fromStatus ");
        }
        if (printProductionDTO.getToStatus() != null) {
            params.put("toStatus", printProductionDTO.getToStatus());
            query.append(" and p.productionStagesId <= :toStatus ");
        }
    }
        private void appendProProductionDate(PrintProductionDTO printProductionDTO, Map<String, Object> params, StringBuilder query) {
        if (printProductionDTO.getFromDate() != null) {
            params.put("fromDate", printProductionDTO.getFromDate());
            query.append(" and p.date >= :fromDate ");
        }
        if (printProductionDTO.getToDate() != null) {
            params.put("toDate", printProductionDTO.getToDate());
            query.append(" and p.date <= :toDate ");
        }
    }
        private void appendProProductionUser(PrintProductionDTO printProductionDTO, Map<String, Object> params, StringBuilder query) {
        if (printProductionDTO.getFromUser() != null) {
            params.put("fromUser", printProductionDTO.getFromUser().getUserCode());
            query.append(" and p.userCode >= :fromUser ");
        }
        if (printProductionDTO.getFromUser() != null) {
            params.put("toUser", printProductionDTO.getToUser().getUserCode());
            query.append(" and p.userCode <= :toUser ");
        }
    }
      

    }
   
