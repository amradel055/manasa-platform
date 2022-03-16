/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.GlYear;
import com.toby.entity.Path;
import java.util.HashMap;
import java.util.List;
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

}
