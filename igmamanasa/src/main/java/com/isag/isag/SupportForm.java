/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.SymbolService;
import com.isag.converter.IsagScreenDTOConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.dto.IgmaScreenDTO;
import com.isag.dto.IgmaSupportDTO;
import com.isag.dto.SymbolDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import com.isag.businessservice.IgmaScreenService;

/**
 *
 * @author m_els
 */
public abstract class SupportForm extends BaseFormBean {

    private IgmaSupportDTO support;
    
    @EJB
    private IgmaScreenService isagScreenService;
    @EJB
    private SymbolService symbolService;
    
    private List<IgmaScreenDTO> isagScreenList;
    private IsagScreenDTOConverter isagScreenConverter;
    
    @Override
    public abstract String save();

    @Override
    public abstract void init();

    @Override
    public abstract void load();

    @Override
    public abstract String getScreenName();

    public List<IgmaScreenDTO> findScreenBymodeule() {
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
     * @return the support
     */
    public IgmaSupportDTO getSupport() {
        if (support == null) {
            support = new IgmaSupportDTO();
        }
        return support;
    }

    /**
     * @param support the support to set
     */
    public void setSupport(IgmaSupportDTO support) {
        this.support = support;
    }

}
