/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.HrEmergencyContactDTO;
import com.isag.entity.Employee;
import com.isag.entity.HrEmergencyContact;
import com.isag.entity.IsagUser;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class HrEmergencyContactServiceImpl implements HrEmergencyContactService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public HrEmergencyContactDTO addHrEmergencyContact(HrEmergencyContactDTO hrEmergencyContactDTO, IsagUser isagUser) {
        HrEmergencyContact entity = mapFromDTO(hrEmergencyContactDTO, isagUser);
        entity = dao.updateEntity(entity);
        hrEmergencyContactDTO = mapTODTO(entity, hrEmergencyContactDTO);
        return hrEmergencyContactDTO;
    }

    @Override
    public HrEmergencyContactDTO addHrEmergencyContact(HrEmergencyContactDTO hrEmergencyContactDTO) {
        return null;

    }

    @Override
    public List<HrEmergencyContactDTO> addHrEmergencyContactList(List<HrEmergencyContactDTO> hrEmergencyContactDTOList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteHrEmergencyContact(HrEmergencyContactDTO hrEmergencyContactDTO, IsagUser isagUser) {
        HrEmergencyContact entity = new HrEmergencyContact();
        entity.setId(hrEmergencyContactDTO.getId());
        dao.deleteEntity(entity);
    }

    public HrEmergencyContactDTO mapTODTO(HrEmergencyContact entity, HrEmergencyContactDTO dTO) {
        if (dTO == null) {
            dTO = new HrEmergencyContactDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(HrEmergencyContactDTO dTO, HrEmergencyContact entity) {

        dTO.setEmployeeId(entity.getId());
        dTO.setCurrentAddress(entity.getCurrentAddress());
        dTO.setMobile(entity.getMobile());
        dTO.setRelation(entity.getRelation());
        dTO.setPermenantAddress(entity.getPermenantAddress());
        dTO.setName(entity.getName());
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());

        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public HrEmergencyContact mapFromDTO(HrEmergencyContactDTO dTO, IsagUser isagUser) {
        HrEmergencyContact entity = new HrEmergencyContact();
        if (dTO.getEmployeeId() != null) {
            Employee contact = new Employee();
            contact.setId(dTO.getEmployeeId());
            entity.setEmployeeId(contact);
        }
        entity.setName(dTO.getName());
        entity.setId(dTO.getId());
        entity.setCurrentAddress(dTO.getCurrentAddress());
        entity.setMobile(dTO.getMobile());
        entity.setPermenantAddress(dTO.getPermenantAddress());
        entity.setRelation(dTO.getRelation());

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);

        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<HrEmergencyContact> list, List<HrEmergencyContactDTO> dTOList) {
        for (HrEmergencyContact entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    @Override
    public void deleteByEmployeeId(Integer employeeId) {
        dao.executeDeleteQuery("delete from HrEmergencyContact d WHERE d.employeeId.id =" + employeeId);

    }

    @Override
    public HrEmergencyContactDTO findbyEmployeeId(Integer employeeId) {

        Map<String, Object> params = new HashMap();
        params.put("employeeId", employeeId);
        HrEmergencyContactDTO hrEmergencyContactDTO = null;
        HrEmergencyContact hrEmergencyContact = dao.findEntityByQuery("SELECT d FROM HrEmergencyContact d WHERE d.employeeId.id =:employeeId", params);
        if (hrEmergencyContact != null) {
            hrEmergencyContactDTO = mapTODTO(hrEmergencyContact, null);
        }
        return hrEmergencyContactDTO;

    }

}
