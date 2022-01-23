/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.GallerySettingsDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface GallerySettingsService {

    public void deleteGallerySettings(GallerySettingsDTO gallerySettingsDTO);

    public List<GallerySettingsDTO> getAllGallerySettings(TobyUser tobyUser);

    public GallerySettingsDTO addGallerySettings(GallerySettingsDTO gallerySettingsDTO, TobyUser tobyUser);

    public GallerySettingsDTO findGallerySettings(Integer gallerySettingsId, TobyUser tobyUser);

    public List<GallerySettingsDTO> getALLGlgallerySettingsByCompanyId(Integer companyId, TobyUser tobyUser);

    public List<GallerySettingsDTO> getALLGlgallerySettingsByBranchId(Integer branchId, TobyUser tobyUser);

    public List<GallerySettingsDTO> getDeliveryDaysByGalleryId(Integer galleryId);

    public String getNoticeByCustomerId(Integer id);
    
    public void updateGallarySetting(Integer gallaryId);
}
