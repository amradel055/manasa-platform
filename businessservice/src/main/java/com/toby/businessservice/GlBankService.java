/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.GlBankEntitySearch;
import com.toby.dto.GlBankDTO;
import com.toby.entity.GlBank;
import com.toby.entity.TobyUser;
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

    public List<GlBank> getGlBankByBranchIdAndCode(Integer branchId, String code , Integer bankId);

    public List<GlBank> findAllGlBankForReport(GlBankEntitySearch glBankEntitySearch);
    
    public List<GlBankDTO> getAllGlBankDTOByBranchId(TobyUser tobyUser);
}
