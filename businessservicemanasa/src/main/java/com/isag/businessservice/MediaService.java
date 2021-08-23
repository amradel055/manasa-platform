/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.MediaDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface MediaService {

    public void deleteMedia(MediaDTO mediaDTO);

    public void deleteMedia(Integer mediaId);

    public MediaDTO save(MediaDTO mediaDTO, IsagUser isagUser);

    public List<MediaDTO> findMedia(Integer branchId, Integer mediaType);
}
