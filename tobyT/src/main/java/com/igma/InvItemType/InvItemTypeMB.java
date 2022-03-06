package com.igma.InvItemType;

import com.toby.businessservice.InvItemTypeService;
import com.toby.dto.InvItemTypeDTO;
import com.toby.entity.InvItemType;
import com.toby.toby.BaseFormBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author H
 */
@Named(value = "invItemTypeMB")
@ViewScoped
public class InvItemTypeMB extends BaseFormBean {

    private InvItemType invItemType;
    private InvItemTypeDTO invItemTypedDTO;

    @EJB
    InvItemTypeService invItemTypeService;

    @Override
    @PostConstruct
    public void init() {
        load();

    }

    public void reset() {
        setInvItemType(null);
        setInvItemTypedDTO(null);

    }

    @Override
    public void load() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("editInvItemTyped")) {
       Integer invInteger =((Integer) getContext().getSessionMap().get("InvItemTypedDTOSelected"));
            setInvItemTypedDTO(invItemTypeService.findInvItemTypeDTOById(invInteger));
        }

    }

    @Override
    public String save() {

        setInvItemTypedDTO(invItemTypeService.save(getInvItemTypedDTO(), getUserData().getUser()));

        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the invItemType
     */
    public InvItemType getInvItemType() {
        if (invItemType == null) {
            setInvItemType(new InvItemType());
        }
        return invItemType;
    }

    /**
     * @param invItemType the invItemType to set
     */
    public void setInvItemType(InvItemType invItemType) {
        this.invItemType = invItemType;
    }

    /**
     * @return the invItemTypedDTO
     */
    public InvItemTypeDTO getInvItemTypedDTO() {
        if (invItemTypedDTO == null) {
            setInvItemTypedDTO(new InvItemTypeDTO());
        }
        return invItemTypedDTO;
    }

    /**
     * @param invItemTypedDTO the invItemTypedDTO to set
     */
    public void setInvItemTypedDTO(InvItemTypeDTO invItemTypedDTO) {
        this.invItemTypedDTO = invItemTypedDTO;
    }

}
