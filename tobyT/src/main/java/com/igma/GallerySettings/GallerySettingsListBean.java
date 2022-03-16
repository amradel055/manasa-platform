package com.igma.GallerySettings;

import com.toby.businessservice.GallerySettingsService;
import com.toby.dto.GallerySettingsDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
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
@Named(value = "gallerySettingsListBean")
@ViewScoped
public class GallerySettingsListBean extends BaseFormBean {

    private GallerySettingsDTO gallerySettingsSelected;

    private List<GallerySettingsDTO> gallerySettingsList;

    @EJB
    GallerySettingsService gallerySettingsService;

    public GallerySettingsListBean() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        gallerySettingsList = new ArrayList<>();
//        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
//        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("save")) {
            try {
                gallerySettingsList = gallerySettingsService.getALLGlgallerySettingsByBranchId(getUserData().getUserBranch().getId(), getUserData().getUser());
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }

    }

    @Override
    public void load() {

    }

    public void delete() {
        gallerySettingsService.deleteGallerySettings(getGallerySettingsSelected());
        gallerySettingsList.remove(getGallerySettingsSelected());
    }

    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");
            exit("../gallerySettings/gallerySettingsForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "gallerySettingsListBean", "goToAdd");
            return null;
        }
    }

    public String goToEdit() {
        try {
            if (gallerySettingsSelected != null && gallerySettingsSelected.getId() > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("gallerySettingsSelected", gallerySettingsSelected.getId());
                context.getSessionMap().put("ScreenMode", "Edit");
                exit("../gallerySettings/gallerySettingsForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "gallerySettingsListBean", "goToEdit");
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

   
    public GallerySettingsDTO getGallerySettingsSelected() {

        if (gallerySettingsSelected == null) {
            gallerySettingsSelected = new GallerySettingsDTO();
        }
        return gallerySettingsSelected;
    }

    /**
     * @param gallerySettingsSelected the gallerySettingsSelected to set
     */
    public void setGallerySettingsSelected(GallerySettingsDTO gallerySettingsSelected) {
        this.gallerySettingsSelected = gallerySettingsSelected;
    }

    /**
     * @return the gallerySettingsList
     */
    public List<GallerySettingsDTO> getGallerySettingsList() {
        return gallerySettingsList;
    }

    /**
     * @param gallerySettingsList the gallerySettingsList to set
     */
    public void setGallerySettingsList(List<GallerySettingsDTO> gallerySettingsList) {
        this.gallerySettingsList = gallerySettingsList;
    }

}
