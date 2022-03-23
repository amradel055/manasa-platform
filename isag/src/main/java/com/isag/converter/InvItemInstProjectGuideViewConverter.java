/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;


import com.isag.views.InvItemInstProjectGuideView;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author AhmedEssam
 */
public class InvItemInstProjectGuideViewConverter implements Converter {

    private List<InvItemInstProjectGuideView> InvItemInstProjectGuideView;

    public InvItemInstProjectGuideViewConverter(List<InvItemInstProjectGuideView> InvItemInstProjectGuideView) {
        this.InvItemInstProjectGuideView = InvItemInstProjectGuideView;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvItemInstProjectGuideView invItemInstProjectGuideView : InvItemInstProjectGuideView) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && invItemInstProjectGuideView.getId().longValue() == Long.parseLong(value)) {
                return invItemInstProjectGuideView;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvItemInstProjectGuideView) value).getId() != null && ((InvItemInstProjectGuideView) value).getId().longValue() != 0) {
                    InvItemInstProjectGuideView codegenerator = (InvItemInstProjectGuideView) value;
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
