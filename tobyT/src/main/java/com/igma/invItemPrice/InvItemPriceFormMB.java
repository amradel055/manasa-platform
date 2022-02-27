package com.igma.invItemPrice;

import com.toby.businessservice.InvItemPriceService;
import com.toby.toby.BaseFormBean;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.toby.dto.InvItemPriceDTO;

@Named(value = "invItemPriceFormMB")
@ViewScoped
public class InvItemPriceFormMB extends BaseFormBean implements Serializable {

    private InvItemPriceDTO invItemPriceDTO;
   

    @EJB
    private InvItemPriceService invItemPriceService;
    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("add")) {
                resetItemDataForm();

            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("edit")) {
                try {
                    Integer itemPriceId = ((Integer) getContext().getSessionMap().get("invItemPriceDTOSelected"));

                    invItemPriceDTO = invItemPriceService.findInvItemPrice(itemPriceId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "load");
        }
    }

    private void resetItemDataForm() {
        try {
            setInvItemPriceDTO(new InvItemPriceDTO());
        } catch (Exception e) {
            saveError(e, "invItemDataFormMB", "resetItemDataForm");
        }
    }


    @Override
    public String save() {
        System.out.println("");
        if (invItemPriceDTO != null && invItemPriceDTO.getGallaryId() != null) {
            invItemPriceDTO = invItemPriceService.updateInvItemPrice(invItemPriceDTO, getUserData().getUser());
            savedMeesage(getUserData().getUserDDs().get("SAVED"));
        }
        return "";
    }

    @Override
    public String getScreenName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the invItemPriceDTO
     */
    public InvItemPriceDTO getInvItemPriceDTO() {
        return invItemPriceDTO;
    }

    /**
     * @param invItemPriceDTO the invItemPriceDTO to set
     */
    public void setInvItemPriceDTO(InvItemPriceDTO invItemPriceDTO) {
        this.invItemPriceDTO = invItemPriceDTO;
    }


}
