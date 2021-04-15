/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductionItemsTransactionDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.dto.ProProductionTransactionDetailDetailViewDTO;
import com.toby.entity.ProProductionItemsTransaction;
import com.toby.entity.TobyUser;
import com.toby.views.ProProductionTransactionFactoryView;
import com.toby.views.ProductionTransactionOfUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface ProProductionItemsTransactionService {

    public ProProductionItemsTransaction saveProProductionItemsTransaction(ProProductionTransactionDetailDetailViewDTO proProductionTransactionDetailDetailViewDTOSelected, ProProductionTransactionDTO proProductionTransactionDTO, TobyUser tobyUser);

    public List<ProProductionItemsTransactionDTO> getProProductionItemsTransaction(TobyUser tobyUser);

    public ProProductionItemsTransactionDTO deleteproProductionItemsTransactionDTO(ProProductionItemsTransactionDTO proProductionItemsTransactionDTO, TobyUser tobyUser);

    public List<ProductionTransactionOfUser> getProProductionTransactionDTOForAllUsersList(PrintProductionDTO printProductionDTO, Integer branchId);

    public List<ProProductionItemsTransactionDTO> getALLProProductionItemsTransactionDTO(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public List<ProProductionItemsTransactionDTO> getALLProProductionItemsTransactionDTO(ProProductionTransactionFactoryView transactionFactoryView, Integer branchId);

    public List<ProProductionItemsTransactionDTO> getALLProProductionItemsTransactionLastDTO(ProProductionTransactionFactoryView transactionFactoryView, Integer branchId);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters);
}
