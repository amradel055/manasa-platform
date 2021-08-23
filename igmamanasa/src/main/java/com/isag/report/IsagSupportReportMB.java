/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.IsagScreenService;
import com.isag.converter.IsagScreenDTOConverter;
import com.isag.dto.IsagScreenDTO;
import com.isag.dto.IsagSupportDTO;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author khale
 */
@Named(value = "isagSupportReportMB")
@ViewScoped
public class IsagSupportReportMB implements Serializable {

    private IsagSupportDTO support;
    private IsagScreenDTOConverter isagScreenConverter;

    private List<IsagSupportDTO> supportreturn;
    
    
    private UserData userData = new UserData();

  
    @EJB
    private IsagScreenService isagScreenService;
    private List<IsagScreenDTO> isagScreenList;

   
    @PostConstruct
    public void init(){

    }
    
    public void reset() {
        setSupport(new IsagSupportDTO());
    }
   
    /**
     * @return the support
     */
    public IsagSupportDTO getSupport() {
        if (support == null) {
            support = new IsagSupportDTO();
        }
        return support;
    }

    public List<IsagScreenDTO> findScreenBymodeule() {
        if (support != null && support.getModule() != null) {
            setIsagScreenList(isagScreenService.getAllScreens(support.getModule()));
            setIsagScreenConverter(new IsagScreenDTOConverter(isagScreenList));
        }
        return getIsagScreenList();
    }

    /**
     * @param support the support to set
     */
    public void setSupport(IsagSupportDTO support) {
        this.support = support;
    }

    /**
     * @return the isagScreenConverter
     */
    public IsagScreenDTOConverter getIsagScreenConverter() {
        return isagScreenConverter;
    }

    /**
     * @param isagScreenConverter the isagScreenConverter to set
     */
    public void setIsagScreenConverter(IsagScreenDTOConverter isagScreenConverter) {
        this.isagScreenConverter = isagScreenConverter;
    }

    /**
     * @return the isagScreenList
     */
    public List<IsagScreenDTO> getIsagScreenList() {
        if (isagScreenList == null || isagScreenList.isEmpty()) {
            isagScreenList = new ArrayList<>();
        }
        return isagScreenList;
    }

    /**
     * @param isagScreenList the isagScreenList to set
     */
    public void setIsagScreenList(List<IsagScreenDTO> isagScreenList) {
        this.isagScreenList = isagScreenList;
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
     * @return the supportreturn
     */
    public List<IsagSupportDTO> getSupportreturn() {
        return supportreturn;
    }

    /**
     * @param supportreturn the supportreturn to set
     */
    public void setSupportreturn(List<IsagSupportDTO> supportreturn) {
        this.supportreturn = supportreturn;
    }

   

}
