/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InstContractDetailDTO;
import com.isag.entity.InstContractDetail;
import java.util.ArrayList;
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
public class InstContractDetailsServiceImpl implements InstContractDetailsService {

    @EJB
    private GenericDAO dao;

    @Override
    public void addInstContractDetails(InstContractDetail instContractDetail) {
        dao.saveEntity(instContractDetail);
    }

    @Override
    public void deleteInstContractDetails(InstContractDetail instContractDetail) {
        dao.deleteEntity(instContractDetail);
    }

    @Override
    public InstContractDetail updateInstContractDetails(InstContractDetail instContractDetail) {
        return dao.updateEntity(instContractDetail);
    }

    @Override
    public List<InstContractDetail> getAllInstContractDetailsByInstContractId(Integer instContractId) {
        Map<String, Object> params = new HashMap<>();
        params.put("instContractId", instContractId);
        List<InstContractDetail> details = dao.findListByQuery("SELECT e FROM InstContractDetail e WHERE e.instContractId.id = :instContractId", params);
        return details;
    }

    @Override
    public List<InstContractDetailDTO> getAllInstContractDetailsByInstContractIdNew(Integer instContractId) {
        Map<String, Object> params = new HashMap<>();
        params.put("instContractId", instContractId);
        List<InstContractDetail> details = dao.findListByQuery("SELECT e FROM InstContractDetail e WHERE e.instContractId.id = :instContractId", params);
        return returnListDTO(details);
    }

    private InstContractDetailDTO mapTODTO(InstContractDetail entity) {
        InstContractDetailDTO dTO = new InstContractDetailDTO();
        dTO.setId(entity.getId());
        dTO.setDueDate(entity.getDueDate());
        dTO.setInstContractId(entity.getInstContractId().getId());
        dTO.setSerial(entity.getSerial());
        dTO.setStatus(entity.getStatus());
        dTO.setOrganizationSiteId(entity.getInstContractId().getOrganizationSiteId().getId());
        dTO.setAccountId(entity.getInstContractId().getOrganizationSiteId().getAccountId().getId());

        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setIndex(entity.getId());

        return dTO;
    }

    private List<InstContractDetailDTO> returnListDTO(List<InstContractDetail> list) {
        List<InstContractDetailDTO> dTOList = new ArrayList<>();
        for (InstContractDetail entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    @Override
    public InstContractDetail getInstContractDetailByInstContractId(Integer instContractId) {
        Map<String, Object> params = new HashMap<>();
        params.put("instContractId", instContractId);
        return dao.findEntityByQuery("SELECT e FROM InstContractDetail e WHERE e.instContractId.id = :instContractId", params);
    }

    @Override
    public InstContractDetail findInstContractDetailsById(Integer instContractDetailId) {
        return dao.findEntityById(InstContractDetail.class, instContractDetailId);
    }

    @Override
    public InstContractDetail addOneInstContractDetail(InstContractDetail instContractDetail) {
        return dao.updateEntity(instContractDetail);
    }

    @Override
    public Integer finMaxSerial(Integer instContractlId) {
        Map<String, Object> params = new HashMap<>();
        params.put("instContractlId", instContractlId);
        Integer max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.serial),0) +1  FROM InstContractDetail g WHERE g.instContractId.id =:instContractlId  ", params);
        return max;
    }

    @Override
    public List<InstContractDetail> getAllUnPaidInstContractDetailsByInstContractId(Integer instContractId) {
        Map<String, Object> params = new HashMap<>();
        params.put("instContractId", instContractId);
        List<InstContractDetail> details = dao.findListByQuery("SELECT e FROM InstContractDetail e WHERE e.instContractId.id = :instContractId AND (e.status != 1 OR e.status IS NULL) ", params);
        return details;
    }

}
