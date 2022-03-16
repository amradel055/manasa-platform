/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscContractRegister;

import com.isag.businessservice.RscContractRegisterService;

import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscContractRegisterDetailWorkUnitDTO;

import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.report.entity.RscContractRegisterBean;
import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;

/**
 *
 * @author amr
 */
public class RscContractRegisterCommon {
    
    private RscContractRegisterService rscContractRegisterService;
    
    private IReportService iReportService ;
    
     private UserData userData;
    
    
    public RscContractRegisterCommon(RscContractRegisterService rscContractRegisterService, UserData userData){
        this.rscContractRegisterService = rscContractRegisterService;
        this.userData = userData;
    }
    
    
    public RscContractRegisterDTO returnObject(Integer contractRegisterId){
        return rscContractRegisterService.findRscContractRegister(contractRegisterId);
    }
    
    public void print(RscContractRegisterDTO contractRegisterDTO){
        RscContractRegisterBean contractRegisterBean;
        List<RscContractRegisterBean> contractRegisterBeanList=new ArrayList<>();
        for(RscContractRegisterDetailWorkUnitDTO registerDetailWorkUnitDTO:contractRegisterDTO.getContractRegisterDetailList()){
            contractRegisterBean=new RscContractRegisterBean();
            contractRegisterBean.setItemName(registerDetailWorkUnitDTO.getGuideDTO().getName());
            contractRegisterBean.setUnitName(registerDetailWorkUnitDTO.getGuideDTO().getUnitName().getName());
            contractRegisterBean.setQuantity(registerDetailWorkUnitDTO.getQuantity());
            contractRegisterBean.setPrice(registerDetailWorkUnitDTO.getPrice());
            contractRegisterBean.setTotal(registerDetailWorkUnitDTO.getTotal());
            contractRegisterBean.setRemark(registerDetailWorkUnitDTO.getRemark()); 
              contractRegisterBeanList.add(contractRegisterBean);
        }
        getiReportService().printRscContractRegister(contractRegisterDTO,contractRegisterBeanList , getUserData());
    }
    
    

    

    /**
     * @return the iReportService
     */
    public IReportService getiReportService() {
        if(iReportService == null){
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
     * @return the rscContractRegisterService
     */
    public RscContractRegisterService getRscContractRegisterService() {
        return rscContractRegisterService;
    }

    /**
     * @param rscContractRegisterService the rscContractRegisterService to set
     */
    public void setRscContractRegisterService(RscContractRegisterService rscContractRegisterService) {
        this.rscContractRegisterService = rscContractRegisterService;
    }
    
    
    
}
