/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.converter.IsagScreenDTOConverter;
import com.isag.dto.IgmaScreenDTO;
import com.isag.dto.IgmaSupportDTO;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.IgmaScreenService;

/**
 *
 * @author khale
 */
@Named(value = "isagSupportReportMB")
@ViewScoped
public class IsagSupportReportMB implements Serializable {

    private IgmaSupportDTO support;
    private IsagScreenDTOConverter isagScreenConverter;

    private List<IgmaSupportDTO> supportreturn;
    
    
    private UserData userData = new UserData();

  
    @EJB
    private IgmaScreenService isagScreenService;
    private List<IgmaScreenDTO> isagScreenList;

   
    @PostConstruct
    public void init(){

    }
    
    public void reset() {
        setSupport(new IgmaSupportDTO());
    }
   
    /**
     * @return the support
     */
    public IgmaSupportDTO getSupport() {
        if (support == null) {
            support = new IgmaSupportDTO();
        }
        return support;
    }

    public List<IgmaScreenDTO> findScreenBymodeule() {
        if (support != null && support.getModule() != null) {
            setIsagScreenList(isagScreenService.getAllScreens(support.getModule()));
            setIsagScreenConverter(new IsagScreenDTOConverter(isagScreenList));
        }
        return getIsagScreenList();
    }

    /**
     * @param support the support to set
     */
    public void setSupport(IgmaSupportDTO support) {
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
    public List<IgmaScreenDTO> getIsagScreenList() {
        if (isagScreenList == null || isagScreenList.isEmpty()) {
            isagScreenList = new ArrayList<>();
        }
        return isagScreenList;
    }

    /**
     * @param isagScreenList the isagScreenList to set
     */
    public void setIsagScreenList(List<IgmaScreenDTO> isagScreenList) {
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
    public List<IgmaSupportDTO> getSupportreturn() {
        return supportreturn;
    }

    /**
     * @param supportreturn the supportreturn to set
     */
    public void setSupportreturn(List<IgmaSupportDTO> supportreturn) {
        this.supportreturn = supportreturn;
    }

   

}
