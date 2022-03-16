/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscRestrictionQuantities;


import com.isag.businessservice.RscPriceTenderService;
import com.isag.businessservice.RscRestrictionQuantitiesService;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.dto.RscRestrictionQuantitiesDetailDTO;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.report.entity.RscPriceTenderBean;
import com.isag.report.entity.RscRestrictionQuantitiesBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amr
 */
public class RscRestrictionQuantitiesCommon {
    
    private RscRestrictionQuantitiesService rscRestrictionQuantitiesService;
    
    private IReportService iReportService ;
    
     private UserData userData;
    
    
    public RscRestrictionQuantitiesCommon(RscRestrictionQuantitiesService rscRestrictionQuantitiesService, UserData userData){
        this.rscRestrictionQuantitiesService = rscRestrictionQuantitiesService;
        this.userData = userData;
    }
    
    
    public RscRestrictionQuantitiesDTO returnObject(Integer restrictionQuantitiesId){
        return rscRestrictionQuantitiesService.findRscRestrictionQuantities(restrictionQuantitiesId);
    }
    
    public void print(RscRestrictionQuantitiesDTO rscRestrictionQuantitiesDTO){
        RscRestrictionQuantitiesBean bean ;
       List<RscRestrictionQuantitiesBean> rscRestrictionQuantitiesBeanList = new ArrayList<>();
        for(RscRestrictionQuantitiesDetailDTO detail : rscRestrictionQuantitiesDTO.getRscRestrictionQuantitiesDetailDTOList()){
           bean=new RscRestrictionQuantitiesBean();
           bean.setItemName(detail.getGuideDTO().getName());
           bean.setUnitName(detail.getGuideDTO().getUnitName().getName());
           bean.setContractQuantity(detail.getContractQty());
            rscRestrictionQuantitiesBeanList.add(bean);
        }
        getiReportService().printRscRestrictionQuantities(rscRestrictionQuantitiesDTO, rscRestrictionQuantitiesBeanList, userData);
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
     * @return the rscRestrictionQuantitiesService
     */
    public RscRestrictionQuantitiesService getRscRestrictionQuantitiesService() {
        return rscRestrictionQuantitiesService;
    }

    /**
     * @param rscRestrictionQuantitiesService the rscRestrictionQuantitiesService to set
     */
    public void setRscRestrictionQuantitiesService(RscRestrictionQuantitiesService rscRestrictionQuantitiesService) {
        this.rscRestrictionQuantitiesService = rscRestrictionQuantitiesService;
    }
    
    
    
}
