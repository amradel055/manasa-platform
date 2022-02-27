/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.TobyErrorDTO;
import com.toby.entity.TobyError;
import com.toby.entity.TobyUser;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class TobyErrorServiceImpl implements TobyErrorService {

    @EJB
    private GenericDAO dao;

    @Override
    public void save(TobyErrorDTO tobyErrorDTO, TobyUser tobyUser) {

        TobyError tobyError = mapFromDTO(tobyErrorDTO, tobyUser);
        dao.updateEntity(tobyError);
    }

    public TobyError mapFromDTO(TobyErrorDTO dTO, TobyUser tobyUser) {
        TobyError TobyError = new TobyError();

        TobyError.setClassName(dTO.getClassName());
        TobyError.setError(dTO.getError());
        TobyError.setMethod(dTO.getMethod());

        if (dTO.getId() == null) {
            TobyError.setCreatedBy(tobyUser);
            TobyError.setCreationDate(new Date());

        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            TobyError.setCreatedBy(tobyUser1);
            TobyError.setCreationDate(dTO.getCreatedDate());
            TobyError.setId(dTO.getId());
            TobyError.setModificationDate(new Date());
            TobyError.setModifiedBy(tobyUser);
        }

        TobyError.setBranchId(tobyUser.getBranchId());
        TobyError.setCompanyId(tobyUser.getCompanyId());
        return TobyError;
    }

}
