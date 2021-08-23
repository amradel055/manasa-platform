/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IgmaErrorDTO;
import com.isag.entity.IgmaError;
import com.isag.entity.IgmaUser;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class IgmaErrorServiceImpl implements IgmaErrorService {

    @EJB
    private GenericDAO dao;

    @Override
    public void save(IgmaErrorDTO isagErrorDTO, IgmaUser isagUser) {

        IgmaError isagError = mapFromDTO(isagErrorDTO, isagUser);
        dao.updateEntity(isagError);
    }

    public IgmaError mapFromDTO(IgmaErrorDTO dTO, IgmaUser isagUser) {
        IgmaError IsagError = new IgmaError();

        IsagError.setClassName(dTO.getClassName());
        IsagError.setError(dTO.getError());
        IsagError.setMethod(dTO.getMethod());

        if (dTO.getId() == null) {
            IsagError.setCreatedBy(isagUser);
            IsagError.setCreationDate(new Date());

        } else {
            IgmaUser isagUser1 = new IgmaUser();
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
