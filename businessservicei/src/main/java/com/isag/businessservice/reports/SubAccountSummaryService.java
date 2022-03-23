package com.isag.businessservice.reports;

import com.isag.views.SubAccountSummary;
import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.entity.GlYear;

import javax.ejb.Remote;
import java.math.BigDecimal;
import java.util.List;

@Remote
public interface SubAccountSummaryService {

    public List<SubAccountSummary> getSubAccountSummary(SubAccountSummarySearchBean subAccountSummarySearchBean, GlYear glYear , boolean extended);
    
    public BigDecimal getBeforeBalance(SubAccountSummarySearchBean subAccountSummarySearchBean , GlYear glYear,boolean extended);
}
