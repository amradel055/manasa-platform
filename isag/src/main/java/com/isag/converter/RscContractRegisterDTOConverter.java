/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.RscContractRegisterDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ahmed
 */
public class RscContractRegisterDTOConverter implements Converter {

    private List<RscContractRegisterDTO> contractRegisterDTO;

    public RscContractRegisterDTOConverter(List<RscContractRegisterDTO> rscContractRegisterDTOList) {
        this.contractRegisterDTO = rscContractRegisterDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (contractRegisterDTO != null && !contractRegisterDTO.isEmpty()) {
            for (RscContractRegisterDTO contractRegister : contractRegisterDTO) {
                try {
                    Long.parseLong(value);
                } catch (Exception e) {
                    return null;
                }
                if (!value.equals("") && !value.equals(" ") && contractRegister.getId().longValue() == Long.parseLong(value)) {
                    return contractRegister;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((RscContractRegisterDTO) value).getId() != null && ((RscContractRegisterDTO) value).getId().longValue() != 0) {
                    RscContractRegisterDTO codegenerator = (RscContractRegisterDTO) value;
                    return codegenerator.getId().longValue() + "";
                } else {
                    return "";
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
