/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.dto.CloseAndSaveMonthDTO;
import com.toby.dto.GlYearDTO;
import com.toby.entity.CloseAndSaveMonth;
import com.toby.entity.GlYear;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class CloseAndSaveMonthServiceImplDTO implements CloseAndSaveMonthServiceDTO {

    @EJB
    private GenericDAO dao;

    @Override
    public List<CloseAndSaveMonthDTO> getCloseAndSaveMonthsDTOByYearIdAndMounthNumberAndBranchId(Integer branchId, Integer yearId, Integer monthNumber) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        params.put("yearId", yearId);
        params.put("monthNumber", monthNumber);
        List<CloseAndSaveMonth> details = dao.findListByQuery("SELECT c FROM CloseAndSaveMonth c "
                + "WHERE c.branchId.id =:branchId AND c.year.id = :yearId AND c.monthNumber = :monthNumber AND c.status= 1 ", params);
        return returnListDTO(details);
    }

    private List<CloseAndSaveMonthDTO> returnListDTO(List<CloseAndSaveMonth> list) {
        List<CloseAndSaveMonthDTO> dTOList = new ArrayList<>();
        for (CloseAndSaveMonth entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private CloseAndSaveMonthDTO mapTODTO(CloseAndSaveMonth entity, Boolean needDetail) {
        CloseAndSaveMonthDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private CloseAndSaveMonthDTO initMapToDTO(CloseAndSaveMonth entity) {

        CloseAndSaveMonthDTO dTO = new CloseAndSaveMonthDTO();
        dTO.setId(entity.getId());
        if (entity.getYear() != null) {
            GlYearDTO glYearDTO = new GlYearDTO();
            glYearDTO.setId(entity.getYear().getId());
            glYearDTO.setName(entity.getYear().getName());
            glYearDTO.setDateFrom(entity.getYear().getDateFrom());
            glYearDTO.setDateTo(entity.getYear().getDateTo());
            glYearDTO.setYear(entity.getYear().getYear());
            dTO.setYear(glYearDTO);
        }

        dTO.setMonthName(entity.getMonthName());
        dTO.setMonthNumber(entity.getMonthNumber());
        dTO.setStatus(entity.getStatus());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    private CloseAndSaveMonth mapFromDTO(CloseAndSaveMonthDTO dTO, TobyUser tobyUser) {
        CloseAndSaveMonth entity = new CloseAndSaveMonth();

        if (dTO.getYear() != null && dTO.getYear().getId() != null) {
            GlYear parent = new GlYear();
            parent.setId(dTO.getYear().getId());
            entity.setYear(parent);
        }

        entity.setMonthNumber(dTO.getMonthNumber());
        entity.setMonthName(dTO.getMonthName());
        entity.setStatus(dTO.getStatus());

        if (dTO.getId() == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }
}
