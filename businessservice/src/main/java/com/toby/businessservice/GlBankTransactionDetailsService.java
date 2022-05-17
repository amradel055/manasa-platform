/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.GeneralJournalDTO;
import com.toby.dto.GlBankDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.balanceGalary;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface GlBankTransactionDetailsService {

    public void addGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail);

    public void deleteGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail);

    public Integer updateGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail, TobyUser tobyUser);

    public List<GlBankTransactionDetail> getAllGlBankTransactionDetailsByGlBankTransactionId(Integer glBankTransactionId);

    public GlBankTransactionDetail getGlBankTransactionDetailByGlBankTransactionId(Integer glBankTransactionId);

    public GlBankTransactionDetail findGlBankTransactionDetailsById(Integer glBankTransactionDetailsId);

    public GlBankTransactionDetail addOneGlBankTransactionDetail(GlBankTransactionDetail glBankTransactionDetail);

    public Integer finMaxSerial(Integer glBankTransactionId);

    public Integer addCashDetail(InvInvoiceDTO dTO, Integer glBankTransactionId, Integer glBankTransactionSerial, TobyUser tobyUser);

    public List<GeneralJournalDTO> tarhilOne(TobyUser tobyUser, Integer type);

    public Integer updateGlBankTransactionDetails(List<GlBankTransactionDetail> glBankTransactionDetailList, GlBankTransaction glBankTransaction, TobyUser tobyUser);

    public Integer tarhilSettlement(List<GlBankTransactionDetail> list, GlBankTransaction glBankTransaction, TobyUser tobyUser, Integer specialDocumentsTypes, boolean postFlag);

    public Integer tarhilSettlementVoucher(List<GlBankTransactionDetail> list1, GlBankTransaction glBankTransaction, TobyUser tobyUser, Integer specialDocumentsTypes, boolean postFlag);

    public Integer tarhilSettlementVoucherNOSumDetail(List<GlBankTransactionDetail> list1, GlBankTransaction glBankTransaction, TobyUser tobyUser,
            Integer specialDocumentsTypes, boolean postFlag);

    public List<GeneralJournal> tarhilCashOne(TobyUser tobyUser, Integer type);
    
    public List<balanceGalary> GlBankTransactionDetailAfterSearchNative(Integer branchId, Date dateFrom1, Date dateTo1, String stringBuilder, TobyUser tobyUser) ;
    
    public void addCashDetail(GlBankDTO glBankDTO, Integer invOrganizationId, Integer glBankTransactionId, GlBankDTO bankDTO, String remarks, BigDecimal value, TobyUser tobyUser);

    }
