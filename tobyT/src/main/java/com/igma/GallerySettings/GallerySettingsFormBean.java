package com.igma.GallerySettings;

import com.toby.businessservice.GallerySettingsService;
import com.toby.dto.GallerySettingsDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
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
@Named(value = "gallerySettingsFormBean")
@ViewScoped
public class GallerySettingsFormBean extends BaseFormBean {

    private GallerySettingsDTO gallerySettingsDTO;

    @EJB
    GallerySettingsService gallerySettingsService;

    public GallerySettingsFormBean() {
    }

    @Override
    @PostConstruct
    public void init() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
            try {
                Integer gallerySettingsId = (Integer) context.getSessionMap().get("gallerySettingsSelected");
                if (gallerySettingsDTO == null) {
                    gallerySettingsDTO = gallerySettingsService.findGallerySettings(gallerySettingsId, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void reset() {
        setGallerySettingsDTO(null);
    }

    public String exit() {
        try {
            exit("../gallerySettings/gallerySettingsList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "gallerySettings", "exit");
            return null;
        }
    }

    @Override
    public void load() {

    }

    @Override
    public String save() {
        if (getGallerySettingsDTO() != null) {
            setGallerySettingsDTO(gallerySettingsService.addGallerySettings(getGallerySettingsDTO(), getUserData().getUser()));
            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("ScreenMode", "save");
                context.getSessionMap().put("gallerySettingsDTOSelected", gallerySettingsDTO);
                exit("../gallerySettings/gallerySettingsList.xhtml");
                return "";
            } catch (Exception e) {
                saveError(e, "GallerySettingsFormBean", "save");
                return null;
            }
        }

        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the gallerySettingsDTO
     */
    public GallerySettingsDTO getGallerySettingsDTO() {
        if (gallerySettingsDTO == null) {
            gallerySettingsDTO = new GallerySettingsDTO();
        }
        return gallerySettingsDTO;
    }

    /**
     * @param gallerySettingsDTO the gallerySettingsDTO to set
     */
    public void setGallerySettingsDTO(GallerySettingsDTO gallerySettingsDTO) {
        this.gallerySettingsDTO = gallerySettingsDTO;
    }

}
