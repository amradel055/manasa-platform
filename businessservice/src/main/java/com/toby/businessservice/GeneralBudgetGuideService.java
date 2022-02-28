/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.GeneralBudgetReportSearchBean;
import com.toby.entity.GeneralBudgetGuide;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface GeneralBudgetGuideService {

    public List<GeneralBudgetGuide> getAllGeneralBudgetGuide();

    public void deleteGeneralBudgetGuide(GeneralBudgetGuide generalBudgetGuide);

    public GeneralBudgetGuide updateGeneralBudgetGuide(GeneralBudgetGuide generalBudgetGuide);

    public GeneralBudgetGuide addGeneralBudgetGuide(GeneralBudgetGuide generalBudgetGuide);

    public GeneralBudgetGuide findGeneralBudgetGuide(Integer id);

    public List<GeneralBudgetGuide> getAllGeneralBudgetGuideByCompanyId(Integer companyId);

    public Integer getMaxId();

    public List<GeneralBudgetGuide> getGeneralBudgetGuideForUser(Integer id);

    public List<GeneralBudgetGuide> getGeneralBudgetGuide(GeneralBudgetReportSearchBean generalBudgetReportSearchBean);

    public List<GeneralBudgetGuide> getAllGeneralBudgetGuideByBranchId(Integer BranchId);

    public List<GeneralBudgetGuide> getGeneralBudgetGuideByBranchIdAndCode(Integer branchId, String number, Integer generalBudgetGuideId);

    List<GeneralBudgetGuide> getAllGeneralBudgetGuideByForFinancialItemMenuBranchId(Integer BranchId);
    List<GeneralBudgetGuide> getAllGeneralBudgetGuideByForIncomeItemMenuBranchId(Integer BranchId);
}
