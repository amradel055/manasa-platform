/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IgmaUserDTO;
import com.isag.dto.NewsDTO;
import com.isag.dto.SchoolStagesGuideDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.News;
import com.isag.entity.IgmaUser;
import com.isag.entity.SchoolStagesGuide;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class NewsServiceImpl implements NewsService {

    @EJB
    private GenericDAO dao;

    @Override
    public NewsDTO findByNewsId(Integer newsId) {
        return mapTODTO(dao.findEntityById(News.class, newsId));
    }

    @Override
    public List<NewsDTO> findNews(Integer newsId, Integer teacherId, Integer type, Integer schoolStageId) {
        List<News> list = new ArrayList<>();
        if (newsId != null) {
            News news = dao.findEntityById(News.class, newsId);
            list.add(news);
        } else {
            Map<String, Object> params = new HashMap<>();
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT d FROM News d WHERE d.active = 1  ");
            if (teacherId != null) {
                params.put("teacherId", teacherId);
                builder.append(" and  d.teacherId.id = :teacherId ");
            }
            if (type != null) {
                params.put("type", type);
                builder.append(" AND d.type = :type");
            }
            if (schoolStageId != null) {
                params.put("schoolStageId", schoolStageId);
                builder.append(" AND d.schoolStageId.id = :schoolStageId");
            }
            builder.append(" order by d.id desc ");
            list = dao.findListByQuery(builder.toString(), params);
        }
        return returnListDTO(list);

    }

    @Override
    public NewsDTO save(NewsDTO newsDTO, IgmaUser isagUser) {
        NewsDTO msg = new NewsDTO();
        News news = null;
        if (newsDTO.getId() != null) {
            news = dao.findEntityById(News.class, newsDTO.getId());
        }
        if (newsDTO != null && newsDTO.getNewsType() != null && newsDTO.getTeacherId() != null
                && newsDTO.getActive() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(newsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(newsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            news = mapToEntity(newsDTO, isagUser, news);
            dao.updateEntity(news);
            msg.setMsg("تم الحفظ بنجاح");
            msg.setMsgType("success");
            return msg;
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    @Override
    public void deleteNews(NewsDTO newsDTO) {
        News entity = new News();
        entity.setId(newsDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteNews(Integer newsId) {
        News entity = new News();
        entity.setId(newsId);
        dao.deleteEntity(entity);
    }

    private List<NewsDTO> returnListDTO(List<News> list) {
        List<NewsDTO> dTOList = new ArrayList<>();
        for (News entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private NewsDTO mapTODTO(News entity) {
        NewsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private NewsDTO initMapToDTO(News entity) {

        NewsDTO dTO = new NewsDTO();
        dTO.setId(entity.getId());
        dTO.setActive(entity.getActive());
        dTO.setAddress(entity.getAdrress());
        dTO.setImg(entity.getImg());
        dTO.setPost(entity.getPost());
        dTO.setNewsType(entity.getType());
        if (entity.getTeacherId() != null) {
            IgmaUserDTO igmaUserDTO = new IgmaUserDTO();
            igmaUserDTO.setId(entity.getTeacherId().getId());
            igmaUserDTO.setName(entity.getTeacherId().getName());
            dTO.setTeacher(igmaUserDTO);
            dTO.setTeacherId(entity.getTeacherId().getId());
        }
        if (entity.getSchoolStageId() != null) {
            SchoolStagesGuideDTO schoolStagesGuideDTO = new SchoolStagesGuideDTO();
            schoolStagesGuideDTO.setId(entity.getSchoolStageId().getId());
            schoolStagesGuideDTO.setName(entity.getSchoolStageId().getName());
            dTO.setSchoolStage(schoolStagesGuideDTO);
            dTO.setSchoolStageId(entity.getSchoolStageId().getId());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private News mapToEntity(NewsDTO dTO, IgmaUser isagUser, News entity) {
        if (entity == null) {
            entity = new News();
        }

        entity.setId(dTO.getId());
        entity.setActive(dTO.getActive());
        entity.setAdrress(dTO.getAddress());
        entity.setImg(dTO.getImg());
        entity.setPost(dTO.getPost());
        entity.setType(dTO.getNewsType());
        if (dTO.getTeacherId() != null) {
            IgmaUser igmaUser = new IgmaUser();
            igmaUser.setId(dTO.getTeacherId());
            entity.setTeacherId(igmaUser);
        }

        if (dTO.getSchoolStageId() != null) {
            SchoolStagesGuide schoolStagesGuide = new SchoolStagesGuide();
            schoolStagesGuide.setId(dTO.getSchoolStageId());
            entity.setSchoolStageId(schoolStagesGuide);
        }

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setBranchId(isagUser.getBranchId());
            entity.setCreationDate(new Date());
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}
