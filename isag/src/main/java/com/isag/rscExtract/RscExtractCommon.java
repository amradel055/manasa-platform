/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscExtract;

import com.isag.businessservice.RscExtractRegisterService;
import com.isag.businessservice.RscPriceTenderService;
import com.isag.dto.RscExtractRegisterDTO;
import com.isag.dto.RscExtractRegisterDetailDTO;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscPriceTenderDetailWorkUnitDTO;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.report.entity.RscExtractRegisteringBean;
import com.isag.report.entity.RscPriceTenderBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amr
 */
public class RscExtractCommon {
    
    private RscExtractRegisterService rscExtractRegisterService;
    
    private IReportService iReportService ;
    
     private UserData userData;
    
    
    public RscExtractCommon(RscExtractRegisterService rscExtractRegisterService, UserData userData){
        this.rscExtractRegisterService = rscExtractRegisterService;
        this.userData = userData;
    }
    
    
    public RscExtractRegisterDTO returnObject(Integer extractRegisterId,String uri){
        return rscExtractRegisterService.findRscExtractRegister(extractRegisterId,uri);
    }
    
    public void print(RscExtractRegisterDTO rscExtractRegisterDTO,String uri){
        RscExtractRegisteringBean bean ;
       List<RscExtractRegisteringBean> rscExtractRegisteringBeanList = new ArrayList<>();
        for(RscExtractRegisterDetailDTO detail : rscExtractRegisterDTO.getRscExtractRegisterDetailDTOList()){
           bean=new RscExtractRegisteringBean();
           bean.setItemName(detail.getGuideDTO().getName());
           bean.setCode(detail.getGuideDTO().getCode());
           bean.setUnitName(detail.getGuideDTO().getUnitName().getName());
           bean.setContractQuantity(detail.getCurrentQty());
           bean.setOldQuantity(detail.getOldQty());
           bean.setCurrentQuantity(detail.getCurrentQty());
           bean.setPriceSell(detail.getPriceSell());
           bean.setCurrentValue(detail.getCurrentValue());
           bean.setCalculateRampPercentage(detail.getRampValue());
           bean.setRampPercentage(detail.getRampPercentage());
           bean.setOldRamp(detail.getOldRamp());
           bean.setReturnRamp(detail.getReturnRamp());
           bean.setNetTotalReturnRamp(detail.getNetTotalReturnRamp());
           bean.setValueAfterDiscount(detail.getValueAfterDiscount());
           bean.setDiscountValue(detail.getDiscountValue());
           bean.setDiscountPercentage(detail.getDiscountPercentage());
           bean.setNetCost(detail.getNetCost());
           bean.setRemarks(detail.getRemarks());
           bean.setExcuteQty(detail.getExcuteQty());
            rscExtractRegisteringBeanList.add(bean);
        }
        getiReportService().printRscExtractRegister(rscExtractRegisterDTO, rscExtractRegisteringBeanList, userData,uri);
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
     * @return the rscExtractRegisterService
     */
    public RscExtractRegisterService getRscExtractRegisterService() {
        return rscExtractRegisterService;
    }

    /**
     * @param rscExtractRegisterService the rscExtractRegisterService to set
     */
    public void setRscExtractRegisterService(RscExtractRegisterService rscExtractRegisterService) {
        this.rscExtractRegisterService = rscExtractRegisterService;
    }
    
    
    
}
