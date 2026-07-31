/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.NewsDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface NewsService {

    public void deleteNews(NewsDTO newsDTO);

    public void deleteNews(Integer mediaId);

    public NewsDTO save(NewsDTO newsDTO, IgmaUser isagUser);

    public List<NewsDTO> findNews(Integer newsId ,Integer teacherId, Integer type, Integer schoolStageId, Integer activ);
    
    public NewsDTO findByNewsId(Integer newsId);
}
