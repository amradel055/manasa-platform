package com.isag.businessservice.reports.searchBean;

import com.isag.dto.InstProjectGuideDTO;
import com.isag.entity.InstProjectGuide;
import java.util.Date;
import java.util.List;

public class ProjectGuideSearchBean {

    private InstProjectGuide projectGuideIdFrom;
    private InstProjectGuide projectGuideIdTo;
    private Integer branchId;
    private Boolean showReport;
    private List<InstProjectGuideDTO> projectGuideListSelected;
    private Date startDate;
    private Date endDate;
    
    
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

    /**
     * @return the projectGuideListSelected
     */
    public List<InstProjectGuideDTO> getProjectGuideListSelected() {
        return projectGuideListSelected;
    }

    /**
     * @param projectGuideListSelected the projectGuideListSelected to set
     */
    public void setProjectGuideListSelected(List<InstProjectGuideDTO> projectGuideListSelected) {
        this.projectGuideListSelected = projectGuideListSelected;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

   

}
