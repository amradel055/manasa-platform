/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscPriceTender;

import com.isag.businessservice.RscPriceTenderService;
import com.isag.rscTender.*;
import com.isag.businessservice.RscTenderService;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderDetailWorkUnitDTO;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.report.entity.RscPriceTenderBean;
import com.isag.report.entity.RscTenderBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amr
 */
public class RscPriceTenderCommon {
    
    private RscPriceTenderService rscPriceTenderService;
    
    private IReportService iReportService ;
    
     private UserData userData;
    
    
    public RscPriceTenderCommon(RscPriceTenderService rscPriceTenderService, UserData userData){
        this.rscPriceTenderService = rscPriceTenderService;
        this.userData = userData;
    }
    
    
    public RscPriceTenderDTO returnObject(Integer priceTenderId){
        return rscPriceTenderService.findRscPriceTender(priceTenderId);
    }
    
    public void print(RscPriceTenderDTO rscPriceTenderDTO){
        RscPriceTenderBean bean ;
       List<RscPriceTenderBean> rscPriceTenderBeanList = new ArrayList<>();
        for(RscPriceTenderDetailWorkUnitDTO detail : rscPriceTenderDTO.getRscPriceTenderDetailWorkUnitDTOList()){
           bean=new RscPriceTenderBean();
            bean.setWorkUnitName(detail.getGuideDTO()==null ? null :detail.getGuideDTO().getName());
            bean.setUnitName(detail.getGuideDTO().getUnitName()== null ? null :detail.getGuideDTO().getUnitName().getName());
            bean.setQuantityUnits(detail.getQuantityUnits());
            bean.setPriceUnit(detail.getPriceUnit());
            bean.setTotalUnits(detail.getTotalUnits());
            bean.setTotalQuantityUnit(detail.getTotalQuantityUnit());
            bean.setTotalCost(detail.getTotalCost());
            bean.setQuantityIn(detail.getQuantityIn());
            bean.setProfitValue(detail.getProfitValue());
            bean.setProfitPercentage(detail.getProfitPercentage());
            bean.setPriceUnitSell(detail.getPriceUnitSell());
            bean.setTotalProfit(detail.getTotalProfit());
            bean.setTotalSell(detail.getTotalSell());
            bean.setRemark(detail.getRemark());
          
           
            
            rscPriceTenderBeanList.add(bean);
        }
        getiReportService().printRscPriceTender(rscPriceTenderDTO, rscPriceTenderBeanList, userData);
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
     * @return the rscPriceTenderService
     */
    public RscPriceTenderService getRscPriceTenderService() {
        return rscPriceTenderService;
    }

    /**
     * @param rscPriceTenderService the rscPriceTenderService to set
     */
    public void setRscPriceTenderService(RscPriceTenderService rscPriceTenderService) {
        this.rscPriceTenderService = rscPriceTenderService;
    }
    
    
    
}
