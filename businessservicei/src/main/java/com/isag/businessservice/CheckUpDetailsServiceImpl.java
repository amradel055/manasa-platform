/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.HosCheckUpDetail;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class CheckUpDetailsServiceImpl implements CheckUpDetailsService {

    @EJB
    private GenericDAO dao;

    @Override
    public void addHosCheckUpDetails(HosCheckUpDetail hosCheckUpDetail) {
        dao.saveEntity(hosCheckUpDetail);
    }

    @Override
    public void deleteHosCheckUpDetails(HosCheckUpDetail hosCheckUpDetail) {
        dao.deleteEntity(hosCheckUpDetail);
    }

    @Override
    public HosCheckUpDetail updateHosCheckUpDetails(HosCheckUpDetail hosCheckUpDetail) {
        return dao.updateEntity(hosCheckUpDetail);
    }

    @Override
    public List<HosCheckUpDetail> getAllHosCheckUpDetailsByHosCheckUpId(Integer hosCheckUpId) {
        Map<String, Object> params = new HashMap<>();
        params.put("hosCheckUpId", hosCheckUpId);
        List<HosCheckUpDetail> details = dao.findListByQuery("SELECT e FROM HosCheckUpDetail e WHERE e.hosCheckUpId.id = :hosCheckUpId", params);
        return details;
    }

    @Override
    public HosCheckUpDetail findHosCheckUpDetailsById(Integer hosCheckUpDetailsId) {
        return dao.findEntityById(HosCheckUpDetail.class, hosCheckUpDetailsId);
    }

    @Override
    public HosCheckUpDetail addOneHosCheckUpDetail(HosCheckUpDetail hosCheckUpDetail) {
        return dao.updateEntity(hosCheckUpDetail);
    }

    @Override
    public HosCheckUpDetail getHosCheckUpDetailByHosCheckUpId(Integer hosCheckUpId) {
        Map<String, Object> params = new HashMap<>();
        params.put("hosCheckUpId", hosCheckUpId);
        return dao.findEntityByQuery("SELECT e FROM HosCheckUpDetail e WHERE e.hosCheckUpId.id = :hosCheckUpId", params);

    }

    @Override
    public Integer finMaxSerial(Integer hosCheckUpId) {
        Map<String, Object> params = new HashMap<>();
        params.put("hosCheckUpId", hosCheckUpId);
        Integer max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.serial),0) +1  FROM HosCheckUpDetail g WHERE g.hosCheckUpId.id =:hosCheckUpId  ", params);
        return max;
    }

}
