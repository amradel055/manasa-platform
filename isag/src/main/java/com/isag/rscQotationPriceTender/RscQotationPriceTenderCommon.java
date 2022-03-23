/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscQotationPriceTender;

import com.isag.businessservice.RscPriceTenderService;
import com.isag.businessservice.RscQotationPriceTenderService;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDetailWorkUnitDTO;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.report.entity.RscPriceTenderBean;
import com.isag.report.entity.RscQuotationPriceTenderBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amr
 */
public class RscQotationPriceTenderCommon {

  
    
    private RscQotationPriceTenderService rscQotationPriceTenderService;
    
    private IReportService iReportService ;
    
     private UserData userData;
    
    
    public RscQotationPriceTenderCommon(RscQotationPriceTenderService rscQotationPriceTenderService, UserData userData){
        this.rscQotationPriceTenderService = rscQotationPriceTenderService;
        this.userData = userData;
    }
    
    
    public RscQotationPriceTenderDTO returnObject(Integer qotationPriceTenderId){
        return rscQotationPriceTenderService.findRscQotationPriceTender(qotationPriceTenderId);
    }
    
    public void print(RscQotationPriceTenderDTO rscQotationPriceTenderDTO){
        RscQuotationPriceTenderBean bean ;
       List<RscQuotationPriceTenderBean> rscQuotationPriceTenderBeanList = new ArrayList<>();
        for(RscQotationPriceTenderDetailWorkUnitDTO detail : rscQotationPriceTenderDTO.getRscQotationPriceTenderDetailWorkUnitDTOList()){
           bean=new RscQuotationPriceTenderBean();
            rscQuotationPriceTenderBeanList.add(bean);
            bean.setWorkUnitName(detail.getGuideDTO()==null ? null :detail.getGuideDTO().getName());
            bean.setUnitName(detail.getGuideDTO().getUnitName()== null ? null :detail.getGuideDTO().getUnitName().getName());
            bean.setQuantity(detail.getQuantity());
            bean.setPrice(detail.getPrice());
            bean.setTotal(detail.getTotal());
            bean.setRemark(detail.getRemark());
        }
        getiReportService().printRscQuotationPriceTender(rscQotationPriceTenderDTO, rscQuotationPriceTenderBeanList, userData);
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
     * @return the rscQotationPriceTenderService
     */
    public RscQotationPriceTenderService getRscQotationPriceTenderService() {
        return rscQotationPriceTenderService;
    }

    /**
     * @param rscQotationPriceTenderService the rscQotationPriceTenderService to set
     */
    public void setRscQotationPriceTenderService(RscQotationPriceTenderService rscQotationPriceTenderService) {
        this.rscQotationPriceTenderService = rscQotationPriceTenderService;
    }
    
    
    
}
