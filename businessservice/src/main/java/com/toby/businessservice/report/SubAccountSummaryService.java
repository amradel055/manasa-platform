package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.toby.entity.GlYear;
import com.toby.views.SubAccountSummary;

import javax.ejb.Remote;
import java.math.BigDecimal;
import java.util.List;

@Remote
public interface SubAccountSummaryService {

    public List<SubAccountSummary> getSubAccountSummary(SubAccountSummarySearchBean subAccountSummarySearchBean, GlYear glYear , boolean extended);
    
    public BigDecimal getBeforeBalance(SubAccountSummarySearchBean subAccountSummarySearchBean , GlYear glYear,boolean extended);
}
