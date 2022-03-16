/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IsagErrorDTO;
import com.isag.entity.IsagError;
import com.isag.entity.IsagUser;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class IsagErrorServiceImpl implements IsagErrorService {

    @EJB
    private GenericDAO dao;

    @Override
    public void save(IsagErrorDTO isagErrorDTO, IsagUser isagUser) {

        IsagError isagError = mapFromDTO(isagErrorDTO, isagUser);
        dao.updateEntity(isagError);
    }

    public IsagError mapFromDTO(IsagErrorDTO dTO, IsagUser isagUser) {
        IsagError IsagError = new IsagError();

        IsagError.setClassName(dTO.getClassName());
        IsagError.setError(dTO.getError());
        IsagError.setMethod(dTO.getMethod());

        if (dTO.getId() == null) {
            IsagError.setCreatedBy(isagUser);
            IsagError.setCreationDate(new Date());

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            IsagError.setCreatedBy(isagUser1);
            IsagError.setCreationDate(dTO.getCreatedDate());
            IsagError.setId(dTO.getId());
            IsagError.setModificationDate(new Date());
            IsagError.setModifiedBy(isagUser);
        }

        IsagError.setBranchId(isagUser.getBranchId());
        IsagError.setCompanyId(isagUser.getCompanyId());
        return IsagError;
    }

}
