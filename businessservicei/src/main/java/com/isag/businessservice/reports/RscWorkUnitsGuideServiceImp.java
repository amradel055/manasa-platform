/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.entityBean.RscWorkUnitsGuideBean;
import com.isag.businessservice.reports.searchBean.RscWorkUnitsGuideSearchBean;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscWorkUnitsGuide;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mostafa
 */
@Stateless
public class RscWorkUnitsGuideServiceImp implements RscWorkUnitsGuideService {
    
    @PersistenceContext(unitName = "ISAG_PU")
    private EntityManager em;
    
    @Override
    public List<RscWorkUnitsGuideBean> getAllRscWorkUnitsGuideBeanBySearchBean(RscWorkUnitsGuideSearchBean rscWorkUnitsGuideSearchBean, Integer branchId) {
        String WorkUnitsGuideFrom = rscWorkUnitsGuideSearchBean.getRscWorkUnitsGuideDTOFrom()== null ? null : rscWorkUnitsGuideSearchBean.getRscWorkUnitsGuideDTOFrom().getCode(),
                WorkUnitsGuideTo = rscWorkUnitsGuideSearchBean.getRscWorkUnitsGuideDTOTo()== null ? null : rscWorkUnitsGuideSearchBean.getRscWorkUnitsGuideDTOTo().getCode();
        List<RscWorkUnitsGuide> rscWorkUnitsGuideList = em
                .createNamedQuery("RscWorkUnitsGuide.findByCodeReport")
                .setParameter("branchId", branchId)
                .setParameter("codeFrom", WorkUnitsGuideFrom)
                .setParameter("codeTo", WorkUnitsGuideTo)
                .getResultList();
       
       List<RscWorkUnitsGuideBean> rscWorkUnitsGuideBeanList = cutlist(rscWorkUnitsGuideList);
       return rscWorkUnitsGuideBeanList;
    }
     private List<RscWorkUnitsGuideBean> cutlist(List<RscWorkUnitsGuide> rscWorkUnitsGuideList) {
        List<RscWorkUnitsGuideBean> rscWorkUnitsGuideBeanList = new ArrayList<>();
        if (rscWorkUnitsGuideList != null && !rscWorkUnitsGuideList.isEmpty()) {
            for (RscWorkUnitsGuide rscWorkUnitsGuide : rscWorkUnitsGuideList) {
                RscWorkUnitsGuideBean rscWorkUnitsGuideBean = new RscWorkUnitsGuideBean();
                rscWorkUnitsGuideBean.setCode(rscWorkUnitsGuide.getCode());
                rscWorkUnitsGuideBean.setName(rscWorkUnitsGuide.getName());
                rscWorkUnitsGuideBean.setLevel(rscWorkUnitsGuide.getLevel()); 
                rscWorkUnitsGuideBean.setType(rscWorkUnitsGuide.getType() == 0 ? "رئيسي" : rscWorkUnitsGuide.getType() == 1 ?  "فرعي" : null);
                rscWorkUnitsGuideBean.setActive( rscWorkUnitsGuide.getActive() == true ? "نشط" : rscWorkUnitsGuide.getActive() == false ? "غير نشط" : null );
                rscWorkUnitsGuideBeanList.add(rscWorkUnitsGuideBean);
            }
        }
        return rscWorkUnitsGuideBeanList;
    } 
    
}
