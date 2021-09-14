/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public abstract class GeneralMapService {
    
    
    
    
//    abstract <T> List<T> findAll(Class<T> entityClass){
//        return null;
//    }

//    public  abstract T  mapTODTO(T entity, T dTO, Boolean check) {
//        if (dTO == null) {
////            dTO = new T;
//        }
////        initMapToDTO(dTO, entity);
//        if (check) {
//            
//           
//        }
//        return null;
//    }
//
//    private void initMapToDTO(DepartmentDTO dTO, Department entity) {
//
//        dTO.setIndex(entity.getId());
//        dTO.setCreatedDate(entity.getCreationDate());
//        dTO.setCreatedBy(entity.getCreatedBy().getId());
//    }
//
//    public Department mapFromDTO(DepartmentDTO dTO, IsagUser isagUser) {
//        Department entity = new Department();
//        
//        if (dTO.getId() == null) {
//            entity.setCreatedBy(isagUser);
//            entity.setCreationDate(new Date());
//
//        } else {
//            IsagUser isagUser1 = new IsagUser();
//            isagUser1.setId(dTO.getCreatedBy());
//            entity.setCreatedBy(isagUser1);
//            entity.setCreationDate(dTO.getCreatedDate());
//            entity.setId(dTO.getId());
//            entity.setModificationDate(new Date());
//            entity.setModifiedBy(isagUser);
//        }
//
//        entity.setBranchId(isagUser.getBranchId());
//        entity.setCompanyId(isagUser.getCompanyId());
//        return entity;
//    }
//
//    private void returnListDTO(List<Department> list, List<DepartmentDTO> dTOList) {
//        for (Department entity : list) {
//            dTOList.add(mapTODTO(entity, null,false));
//        }
//    }
//

}
