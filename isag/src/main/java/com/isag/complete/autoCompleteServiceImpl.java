/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.complete;

import com.isag.businessservice.RscWorkUnitsGuideService;
import com.isag.converter.RscWorkUnitDTOConverter;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.isag.UserData;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Mostafa
 */
public class autoCompleteServiceImpl implements autoCompleteService {

    private List<RscWorkUnitsGuideDTO> workUnitsGuideDTO;
    private RscWorkUnitDTOConverter rscWorkUnitDTOConverter;

    @EJB
    private RscWorkUnitsGuideService rscWorkUnitsGuideService;

    @Override
    public List<RscWorkUnitsGuideDTO> completeWorkUnitsGuideAsUnit(String query) {
        List<RscWorkUnitsGuideDTO> RscWorkUnitsGuides = getWorkUnitsGuideDTO();
        if (query == null || query.trim().equals("")) {
            setRscWorkUnitDTOConverter(new RscWorkUnitDTOConverter(RscWorkUnitsGuides));
            return RscWorkUnitsGuides;
        }
        List<RscWorkUnitsGuideDTO> filteredRscWorkUnitsGuides = new ArrayList<>();

        String nameAr;
        String code;
        RscWorkUnitsGuideDTO RscWorkUnitsGuide;
        for (int i = 0; i < getWorkUnitsGuideDTO().size(); i++) {
            RscWorkUnitsGuide = RscWorkUnitsGuides.get(i);
            nameAr = RscWorkUnitsGuide.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredRscWorkUnitsGuides.contains(RscWorkUnitsGuide)) {
                    filteredRscWorkUnitsGuides.add(RscWorkUnitsGuide);
                }
            }

            code = RscWorkUnitsGuide.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredRscWorkUnitsGuides.contains(RscWorkUnitsGuide)) {
                    filteredRscWorkUnitsGuides.add(RscWorkUnitsGuide);
                }
            }
        }

        setRscWorkUnitDTOConverter(new RscWorkUnitDTOConverter(filteredRscWorkUnitsGuides));
        return filteredRscWorkUnitsGuides;
    }

    /**
     * @return the workUnitsGuideDTO
     */
    public List<RscWorkUnitsGuideDTO> getWorkUnitsGuideDTO() {
        if (workUnitsGuideDTO == null || workUnitsGuideDTO.isEmpty()) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            UserData userData = (UserData) context.getSessionMap().get("userLogInData");
            workUnitsGuideDTO = rscWorkUnitsGuideService.getBranchRscWorkUnitsGuidesActiveByBranch(userData.getUserBranch().getId());
            rscWorkUnitDTOConverter = new RscWorkUnitDTOConverter(workUnitsGuideDTO);
        }
        return workUnitsGuideDTO;
    }

    /**
     * @param workUnitsGuideDTO the workUnitsGuideDTO to set
     */
    public void setWorkUnitsGuideDTO(List<RscWorkUnitsGuideDTO> workUnitsGuideDTO) {
        this.workUnitsGuideDTO = workUnitsGuideDTO;
    }

    /**
     * @return the rscWorkUnitDTOConverter
     */
    public RscWorkUnitDTOConverter getRscWorkUnitDTOConverter() {
        return rscWorkUnitDTOConverter;
    }

    /**
     * @param rscWorkUnitDTOConverter the rscWorkUnitDTOConverter to set
     */
    public void setRscWorkUnitDTOConverter(RscWorkUnitDTOConverter rscWorkUnitDTOConverter) {
        this.rscWorkUnitDTOConverter = rscWorkUnitDTOConverter;
    }

}
