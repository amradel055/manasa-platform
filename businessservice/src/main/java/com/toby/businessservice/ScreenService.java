/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyScreen;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface ScreenService {

    public List<TobyScreen> getAllScreenNotAssignedToRole(Integer roleId, Integer roleParentId);
    
    public List<TobyScreen> getAllScreens();
}
