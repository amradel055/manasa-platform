/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.entity.GlBank;
import com.isag.entity.GmSubscriptions;
import com.isag.entity.IsagUser;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface GmSubscriptionsService {

    public List<GmSubscriptionsDTO> getALLGmSubscriptionsDTOByCompanyId(Integer companyId);

    public List<GmSubscriptionsDTO> getALLGmSubscriptionsDTOByBranchId(Integer branchId);

    public GmSubscriptionsDTO findGmSubscriptionsDTOById(GmSubscriptionsDTO gmSubscriptionsDTODto);

    public GmSubscriptionsDTO findGmSubscriptionsDTOBySerial(GmSubscriptionsDTO gmSubscriptionsDTO, Integer branchId);

    public GmSubscriptionsDTO save(GmSubscriptionsDTO gmSubscriptionsDTODTO, IsagUser isagUser);

    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters);

    public List<GmSubscriptionsDTO> getALLGmSubscriptionsDTODTOByTypeAndBranchId(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public void deleteGmSubscriptionsDTODTO(GmSubscriptionsDTO gmSubscriptionsDTO, Integer isPurchaseOrSales);

    public void deleteGmSubscriptionsDTODTO(Integer invoiceId);

    public BigDecimal netPay(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId);

    public BigDecimal returnInvInvoiceSummarySummationDuration(Integer BranchId, Integer type, Date dateFrom, Date dateTo);

    public List<GmSubscriptionsDTO> getALLGmSubscriptionsDTODTOByCustomerId(Integer customerId);

    public GmSubscriptionsDTO getValidGmSubscriptionsDTOByCustomerId(Integer customerId);

    public GmSubscriptionsDTO GmSubscribtionAutoSave(GmSubscriptionsDTO dTO, IsagUser isagUser);

    public void updateDeleteGmSubscriptionsDTODTO(GmSubscriptionsDTO gmSubscriptionsDTO, IsagUser user);

    public Integer findGmSubscriptionsDTOBySerialNew(Integer serial, Integer branchId);

    public BigDecimal FindTotalvalueByDateGM(List<GlBank> glbankIdList, Integer branchId);

    public BigDecimal FindTotalvalueByMonthGM(List<GlBank> glbankIdList, Integer branchId);

    public Integer FindInvoiceNumByDateGM(List<GlBank> glbankIdList, Integer branchId);

    public Integer FindInvoiceNumByMounthGM(List<GlBank> glbankIdList, Integer branchId);

}
