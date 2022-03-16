/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.IsagScreenService;
import com.isag.businessservice.SymbolService;
import com.isag.converter.IsagScreenDTOConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.dto.IsagScreenDTO;
import com.isag.dto.IsagSupportDTO;
import com.isag.dto.SymbolDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author m_els
 */
public abstract class SupportForm extends BaseFormBean {

    private IsagSupportDTO support;
    
    @EJB
    private IsagScreenService isagScreenService;
    @EJB
    private SymbolService symbolService;
    
    private List<IsagScreenDTO> isagScreenList;
    private IsagScreenDTOConverter isagScreenConverter;
    
    @Override
    public abstract String save();

    @Override
    public abstract void init();

    @Override
    public abstract void load();

    @Override
    public abstract String getScreenName();

    public List<IsagScreenDTO> findScreenBymodeule() {
        if (support != null && support.getModule() != null) {
            setIsagScreenList(isagScreenService.getAllScreens(support.getModule()));
            setIsagScreenConverter(new IsagScreenDTOConverter(isagScreenList));
        }
        return getIsagScreenList();
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
     * @return the support
     */
    public IsagSupportDTO getSupport() {
        if (support == null) {
            support = new IsagSupportDTO();
        }
        return support;
    }

    /**
     * @param support the support to set
     */
    public void setSupport(IsagSupportDTO support) {
        this.support = support;
    }

}
