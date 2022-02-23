/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rscTender;

import com.isag.businessservice.RscTenderService;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderDetailWorkUnitDTO;
import com.isag.isag.UserData;
import com.isag.print.IReportService;
import com.isag.print.IReportServiceImpl;
import com.isag.report.entity.RscTenderBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amr
 */
public class RscTenderCommon {
    
    private RscTenderService rscTenderService;
    
    private IReportService iReportService ;
    
     private UserData userData;
    
    
    public RscTenderCommon(RscTenderService rscTenderService, UserData userData){
        this.rscTenderService = rscTenderService;
        this.userData = userData;
    }
    
    
    public RscTenderDTO returnObject(Integer tenderId){
        return rscTenderService.findRscTender(tenderId,getUserData().getUser());
    }
    
    public void print(RscTenderDTO rscTenderDTO){
        RscTenderBean bean ;
       List<RscTenderBean> rscTenderBeanList = new ArrayList<>();
        for(RscTenderDetailWorkUnitDTO detail : rscTenderDTO.getRscTenderDetailWorkUnitDTOList()){
            bean = new RscTenderBean();
            bean.setUnitName(detail.getGuideDTO().getUnitName()== null ? null :detail.getGuideDTO().getUnitName().getName());
            bean.setWorkUnitName(detail.getGuideDTO()==null ? null :detail.getGuideDTO().getName());
            bean.setQuantity(detail.getQuantity() ==null ? BigDecimal.ZERO : detail.getQuantity()  );
            rscTenderBeanList.add(bean);
        }
        getiReportService().printRscTender(rscTenderDTO,rscTenderBeanList, getUserData());
    }
    
    

    /**
     * @return the rscTenderService
     */
    public RscTenderService getRscTenderService() {
        return rscTenderService;
    }

    /**
     * @param rscTenderService the rscTenderService to set
     */
    public void setRscTenderService(RscTenderService rscTenderService) {
        this.rscTenderService = rscTenderService;
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
    
    
    
}
