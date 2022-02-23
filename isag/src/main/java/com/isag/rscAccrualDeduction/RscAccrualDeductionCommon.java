/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscAccrualDeduction;

import com.isag.businessservice.RscAccrualDeductionService;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.report.entity.RscAccrualDeductionBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amr
 */
public class RscAccrualDeductionCommon {

    private RscAccrualDeductionService rscAccrualDeductionService;

    private IReportService iReportService;

    private UserData userData;

    public RscAccrualDeductionCommon(RscAccrualDeductionService rscAccrualDeductionService, UserData userData) {
        this.rscAccrualDeductionService = rscAccrualDeductionService;
        this.userData = userData;
    }

    public RscAccrualDeductionDTO returnObject(Integer accrualDeductionId) {
        return rscAccrualDeductionService.findRscAccrual(accrualDeductionId);
    }

    public void print(RscAccrualDeductionDTO rscAccrualDeductionDTO) {
        RscAccrualDeductionBean bean;
        List<RscAccrualDeductionBean> rscAccrualDeductionBeanList = new ArrayList<>();

        //   getiReportService().printRscExtractRegister(rscAccrualDeductionDTO, rscAccrualDeductionBeanList, userData);
    }

    /**
     * @return the iReportService
     */
    public IReportService getiReportService() {
        if (iReportService == null) {
            iReportService = new IReportServiceImpl();
        }
        return iReportService;
    }

    /**
     * @param iReportService the iReportService to set
     */
    public void setiReportService(IReportService iReportService) {
        this.iReportService = iReportService;
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the rscAccrualDeductionService
     */
    public RscAccrualDeductionService getRscAccrualDeductionService() {
        return rscAccrualDeductionService;
    }

    /**
     * @param rscAccrualDeductionService the rscAccrualDeductionService to set
     */
    public void setRscAccrualDeductionService(RscAccrualDeductionService rscAccrualDeductionService) {
        this.rscAccrualDeductionService = rscAccrualDeductionService;
    }

}
