/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.GlBankEntitySearch;
import com.isag.dto.GlBankDTO;
import com.isag.entity.GlBank;
import com.isag.entity.IsagUser;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author hq002
 */
public interface GlBankService {

    public List<GlBank> getAllGlBank();

    public void deleteGlBank(GlBank glBank);

    public GlBank updateGlBank(GlBank glBank);

    public GlBank addGlBank(GlBank glBank);

    public GlBank findGlBank(Integer id);

    public List<GlBank> getAllGlBankByCompanyId(Integer companyId);

    public List<GlBank> getAllGlBankByBranchId(Integer branchId);

    public List<GlBank> getGlBankByBranchIdAndCode(Integer branchId, String code, Integer bankId);

    public List<GlBank> findAllGlBankForReport(GlBankEntitySearch glBankEntitySearch);

    public BigDecimal findGlBankOpeninngBalance(Integer branchId, Integer bankId);

    public GlBankDTO addGlBank(GlBankDTO glBankDTO, IsagUser isagUser);

    public List<GlBankDTO> getAllGlBankDTOByBranchId(Integer branchId);
    
    public List<GlBankDTO> findGlBankDTOList(StringBuilder stringBuilder);
}
