/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.ZoomLinkDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface ZoomLinkService {

    public ZoomLinkDTO save(ZoomLinkDTO dto , IgmaUser igmaUser);

    public ZoomLinkDTO findById(Integer id);
    
    public void delete(Integer id);

    public List<ZoomLinkDTO> find(Integer teacherId, Integer schoolId, 
            Integer materialId);

}
