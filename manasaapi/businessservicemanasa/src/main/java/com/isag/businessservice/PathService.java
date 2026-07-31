/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.Path;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface PathService {

    public Path findPath(Integer pathId);

    public String findPathbyTypeId(Integer operatingSystem, Integer typeId);
}
