package com.igma.DiscountCodes;

import com.toby.businessservice.DiscountCodesService;
import com.toby.dto.DiscountCodesDTO;
import com.toby.toby.BaseFormBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author H
 */
@Named(value = "discountCodesListBean")
@ViewScoped
public class DiscountCodesListBean extends BaseFormBean {

    private DiscountCodesDTO discountCodesDTOSelected;

    private List<DiscountCodesDTO> discountCodesDTOList;

    @EJB
    DiscountCodesService discountCodesService;

    public DiscountCodesListBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            discountCodesDTOList = discountCodesService.getDiscountCodesByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void load() {

    }

    public void delete() {
        discountCodesService.deleteDiscountCodes(getDiscountCodesDTOSelected());
        discountCodesDTOList.remove(getDiscountCodesDTOSelected());
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../DiscountCodes/discountCodesForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "discountCodesListBean", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (discountCodesDTOSelected != null && discountCodesDTOSelected.getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("discountCodesDTOSelected", discountCodesDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../DiscountCodes/discountCodesForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "discountCodesListBean", "goToEdit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the discountCodesDTOSelected
     */
    public DiscountCodesDTO getDiscountCodesDTOSelected() {
        return discountCodesDTOSelected;
    }

    /**
     * @param discountCodesDTOSelected the discountCodesDTOSelected to set
     */
    public void setDiscountCodesDTOSelected(DiscountCodesDTO discountCodesDTOSelected) {
        this.discountCodesDTOSelected = discountCodesDTOSelected;
    }

    /**
     * @return the discountCodesDTOList
     */
    public List<DiscountCodesDTO> getDiscountCodesDTOList() {

        if (discountCodesDTOList == null | discountCodesDTOList.isEmpty()) {
            discountCodesDTOList = discountCodesService.getDiscountCodesByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());
        }

        return discountCodesDTOList;
    }

    /**
     * @param discountCodesDTOList the discountCodesDTOList to set
     */
    public void setDiscountCodesDTOList(List<DiscountCodesDTO> discountCodesDTOList) {
        this.discountCodesDTOList = discountCodesDTOList;
    }

}
