/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.GlAnnualClosing;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class GlAnnualClosingServiceImpl implements GlAnnualClosingService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<GlAnnualClosing> findGlAnnualClosingListByBranch(Integer branchId, Integer glYearId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("glYearId", glYearId);
        List<GlAnnualClosing> glAnnualClosingList = dao.findListByQuery("SELECT gac FROM GlAnnualClosing gac WHERE gac.branchId.id = :branchId AND gac.glYearId.id = :glYearId", params);
        return glAnnualClosingList;
    }

    @Override
    public void updateGlAnuualList(List<GlAnnualClosing> glAnnualClosingsUpdated, List<GlAnnualClosing> glAnnualClosingsDeleted) {
        if (glAnnualClosingsUpdated != null && !glAnnualClosingsUpdated.isEmpty()) {
            for (GlAnnualClosing closing : glAnnualClosingsUpdated) {
                if (closing.getId() != null) {
                    dao.updateEntity(closing);
                } else {
                    dao.saveEntity(closing);
                }
            }
        }
        if (glAnnualClosingsDeleted != null && !glAnnualClosingsDeleted.isEmpty()) {
            for (GlAnnualClosing deleteClosing : glAnnualClosingsDeleted) {
                dao.deleteEntity(deleteClosing);
            }
        }
    }

}
