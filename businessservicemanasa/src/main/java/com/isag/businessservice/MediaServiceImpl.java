/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.MediaDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.Media;
import com.isag.entity.IgmaUser;
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
public class MediaServiceImpl implements MediaService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<MediaDTO> findMedia(Integer branchId, Integer mediaType) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Media d WHERE d.branchId.id = :branchId ");
        if (mediaType != null) {
            params.put("mediaType", mediaType);
            builder.append(" AND d.mediaType = :mediaType");
        }
        builder.append(" order by d.id desc ");
        List<Media> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);

    }

    @Override
    public MediaDTO save(MediaDTO mediaDTO, IgmaUser isagUser) {
        MediaDTO msg = new MediaDTO();
        Media media = null;
        if (mediaDTO.getId() != null) {
            media = dao.findEntityById(Media.class, mediaDTO.getId());
        }
        if (mediaDTO != null && mediaDTO.getMediaLink() != null
                && mediaDTO.getMediaType() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(mediaDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(mediaDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            media = mapToEntity(mediaDTO, isagUser, media);
            dao.updateEntity(media);
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
    public void deleteMedia(MediaDTO mediaDTO) {
        Media entity = new Media();
        entity.setId(mediaDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteMedia(Integer mediaId) {
        Media entity = new Media();
        entity.setId(mediaId);
        dao.deleteEntity(entity);
    }

    private List<MediaDTO> returnListDTO(List<Media> list) {
        List<MediaDTO> dTOList = new ArrayList<>();
        for (Media entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private MediaDTO mapTODTO(Media entity) {
        MediaDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private MediaDTO initMapToDTO(Media entity) {

        MediaDTO dTO = new MediaDTO();
        dTO.setId(entity.getId());
        dTO.setMediaLink(entity.getMediaLink());
        dTO.setMediaType(entity.getMediaType());
        dTO.setThumb(entity.getThumb());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private Media mapToEntity(MediaDTO dTO, IgmaUser isagUser, Media entity) {
        if (entity == null) {
            entity = new Media();
        }

        entity.setId(dTO.getId());
        entity.setMediaLink(dTO.getMediaLink());
        entity.setMediaType(dTO.getMediaType());
        entity.setThumb(dTO.getThumb());

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
