package com.isag.businessservice.reports.searchBean;

import com.isag.entity.InstProjectGuide;

public class ProjectGuideSearchBean {

    private InstProjectGuide projectGuideIdFrom;
    private InstProjectGuide projectGuideIdTo;
    private Integer branchId;
    private Boolean showReport;
    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the projectGuideIdFrom
     */
    public InstProjectGuide getProjectGuideIdFrom() {
        return projectGuideIdFrom;
    }

    /**
     * @param projectGuideIdFrom the projectGuideIdFrom to set
     */
    public void setProjectGuideIdFrom(InstProjectGuide projectGuideIdFrom) {
        this.projectGuideIdFrom = projectGuideIdFrom;
    }

    /**
     * @return the projectGuideIdTo
     */
    public InstProjectGuide getProjectGuideIdTo() {
        return projectGuideIdTo;
    }

    /**
     * @param projectGuideIdTo the projectGuideIdTo to set
     */
    public void setProjectGuideIdTo(InstProjectGuide projectGuideIdTo) {
        this.projectGuideIdTo = projectGuideIdTo;
    }

    /**
     * @return the showReport
     */
    public Boolean getShowReport() {
        return showReport;
    }

    /**
     * @param showReport the showReport to set
     */
    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
    }

   

}
