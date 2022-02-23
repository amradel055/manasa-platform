/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.GlYear;
import com.isag.entity.Path;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class PathServiceImpl implements PathService {

    @EJB
    private GenericDAO dao;

    @Override
    public Path findPath(Integer pathId) {
        return dao.findEntityById(Path.class, pathId);
    }

    @Override
    public String findPathbyTypeId(Integer operatingSystem, Integer typeId) {
        Map<String, Object> parms = new HashMap<>();
        parms.put("typeId", typeId);
        parms.put("operatingSystem", operatingSystem);
        return dao.findEntityByQuery("SELECT p.path FROM Path p WHERE p.operatingSystem = :operatingSystem  AND p.typeId = :typeId", parms);
    }
}
