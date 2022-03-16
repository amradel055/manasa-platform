/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.entityBean.RscExtractUnitItemAmountViewBean;
import com.isag.businessservice.reports.searchBean.RscExtractUnitItemAmountSearchBean;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface RscExtractUnitItemAmountViewService {

    public List<RscExtractUnitItemAmountViewBean> findRscExtractUnitItemViewService(RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean, Integer branchId);

    public List<RscExtractUnitItemAmountViewBean> findRscExtractUnitItemDetailViewService(RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean, Integer branchId, Integer itemId);
}
