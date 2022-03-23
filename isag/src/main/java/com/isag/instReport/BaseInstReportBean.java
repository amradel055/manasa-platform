/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.instReport;

import com.isag.businessservice.InstProjectGuideService;
import com.isag.businessservice.ReportManagementService;
import com.isag.businessservice.reports.searchBean.InstallmentSearchBean;
import com.isag.converter.InstProjectGuideDTOConverter;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.entity.ReportManagement;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseReportBean;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBs;

/**
 *
 * @author ahmedEssam <ahmedEssam@isag.com>
 */
public abstract class BaseInstReportBean extends BaseReportBean {

    //Other property
    private AutoComplete autoComplete;
    private InstallmentSearchBean installmentSearchBean;

    //Converter
    private InstProjectGuideDTOConverter instProjectGuideConverter;

    //Lists
    private List<InstProjectGuideDTO> instProjectGuideList;

    //EJBs
    @EJB
    private InstProjectGuideService instProjectGuideService;
   

    public List<InstProjectGuideDTO> completProjectGuide(String query) {
        return getAutoComplete().completeInstProjectDTO(query, getInstProjectGuideList(), getInstProjectGuideConverter());

    }

    

    /**
     * @return the autoComplete
     */
    public AutoComplete getAutoComplete() {
        if (autoComplete == null) {
            autoComplete = new AutoComplete();
        }
        return autoComplete;
    }

    /**
     * @param autoComplete the autoComplete to set
     */
    public void setAutoComplete(AutoComplete autoComplete) {
        this.autoComplete = autoComplete;
    }

    /**
     * @return the instProjectGuideConverter
     */
    public InstProjectGuideDTOConverter getInstProjectGuideConverter() {
        return instProjectGuideConverter;
    }

    /**
     * @param instProjectGuideConverter the instProjectGuideConverter to set
     */
    public void setInstProjectGuideConverter(InstProjectGuideDTOConverter instProjectGuideConverter) {
        this.instProjectGuideConverter = instProjectGuideConverter;
    }

    /**
     * @return the instProjectGuideList
     */
    public List<InstProjectGuideDTO> getInstProjectGuideList() {
        instProjectGuideList = instProjectGuideService.getAllInstProjectGuidesDTO(getUserData().getSelectedBranch());
        setInstProjectGuideConverter(new InstProjectGuideDTOConverter(instProjectGuideList));
        return instProjectGuideList;
    }

    /**
     * @param instProjectGuideList the instProjectGuideList to set
     */
    public void setInstProjectGuideList(List<InstProjectGuideDTO> instProjectGuideList) {
        this.instProjectGuideList = instProjectGuideList;
    }

    /**
     * @return the installmentSearchBean
     */
    public InstallmentSearchBean getInstallmentSearchBean() {
        return installmentSearchBean;
    }

    /**
     * @param installmentSearchBean the installmentSearchBean to set
     */
    public void setInstallmentSearchBean(InstallmentSearchBean installmentSearchBean) {
        this.installmentSearchBean = installmentSearchBean;
    }
}
