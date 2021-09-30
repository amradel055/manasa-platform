/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InstProjectGuideDTO;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractProject;
import com.isag.entity.InstProjectGuide;
import com.isag.entity.IsagUser;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class InstContractProjectServiceImpl implements InstContractProjectService {

    @EJB
    private GenericDAO dao;

    public void save(List<InstProjectGuideDTO> list, Integer instContractId,IsagUser isagUser) {
        if (instContractId != null) {
            if (instContractId != null && instContractId > 0) {
                dao.executeDeleteQuery("delete from InstContractProject e WHERE e.id > 0 AND e.instContract.id = " + instContractId);
            }
            for (InstProjectGuideDTO dTO : list) {
                InstContractProject instContractProject = new InstContractProject();

                InstContract instContract = new InstContract();
                instContract.setId(instContractId);
                instContractProject.setInstContract(instContract);

                InstProjectGuide instProjectGuide = new InstProjectGuide();
                instProjectGuide.setId(dTO.getId());
                instContractProject.setInstProjectGuide(instProjectGuide);
                instContractProject.setCreationDate(new Date());
                instContractProject.setCreatedBy(isagUser);
                instContractProject.setCompanyId(isagUser.getCompanyId());

                dao.saveEntity(instContractProject);
            }
        }
    }

}
