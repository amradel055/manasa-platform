/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.ReportSignatureDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.ReportSignature;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class ReportSignatureServiceImpl implements ReportSignatureService {

    @EJB
    private GenericDAO dao;

    @Override
    public ReportSignatureDTO addReportSignature(ReportSignatureDTO reportSignatureDTO, IsagUser isagUser) {
        String error = validation(reportSignatureDTO);
        if (error == null || error.isEmpty()) {
            ReportSignature reportSignature = mapFromDTO(reportSignatureDTO, isagUser);
            reportSignature = dao.updateEntity(reportSignature);
            return initMapToDTO(reportSignature);
        } else {
            reportSignatureDTO.setMsg(error);
            return reportSignatureDTO;
        }
    }

    @Override
    public void deleteReportSignatureDTO(ReportSignatureDTO reportSignatureDTO, IsagUser isagUser) {
        ReportSignature reportSignature = mapFromDTO(reportSignatureDTO, isagUser);
        dao.deleteEntity(reportSignature);
    }

    @Override
    public List<ReportSignatureDTO> getAllReportSignatureDTOByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchid", branchId);
        List<ReportSignature> reportSignatureList = dao.findListByQuery("SELECT r FROM ReportSignature r WHERE r.branchId.id = :branchid ", params);
        return returnListDTO(reportSignatureList);
    }

    @Override
    public ReportSignatureDTO getReportSignatureDTOByIdANDBranchId(Integer Id, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("Id", Id);
        params.put("branchid", branchId);
        ReportSignature reportSignature = dao.findEntityByQuery("SELECT r FROM ReportSignature r WHERE r.branchId.id = :branchid AND r.id = :Id", params);
        return initMapToDTO(reportSignature);
    }

    @Override
    public ReportSignatureDTO getReportSignatureDTOByScreenANDBranchId(Integer screen, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("screen", screen);
        params.put("branchid", branchId);
        ReportSignature reportSignature = dao.findEntityByQuery("SELECT r FROM ReportSignature r WHERE r.branchId.id = :branchid AND r.screen = :screen", params);
        return initMapToDTO(reportSignature != null ? reportSignature : new ReportSignature());
    }

    @Override
    public Boolean ReportSignatureAleardyExist(ReportSignatureDTO reportSignatureDTO) {
        Map<String, Object> params = new HashMap();
        params.put("branchid", reportSignatureDTO.getBranchId());
        params.put("screen", reportSignatureDTO.getScreen());
        ReportSignature reportSignature = dao.findEntityByQuery("SELECT r FROM ReportSignature r WHERE r.branchId.id = :branchid AND r.screen = :screen", params);
        return reportSignature != null ? Boolean.TRUE : Boolean.FALSE;

    }

    private List<ReportSignatureDTO> returnListDTO(List<ReportSignature> list) {
        List<ReportSignatureDTO> dTOList = new ArrayList<>();
        for (ReportSignature entity : list) {
            dTOList.add(initMapToDTO(entity));
        }
        return dTOList;
    }

    private ReportSignatureDTO initMapToDTO(ReportSignature entity) {
        ReportSignatureDTO dTO = new ReportSignatureDTO();
        dTO.setId(entity.getId());
        dTO.setSignature1(entity.getSignature1());
        dTO.setSignature2(entity.getSignature2());
        dTO.setSignature3(entity.getSignature3());
        dTO.setSignature4(entity.getSignature4());
        dTO.setSignature5(entity.getSignature5());
        dTO.setScreen(entity.getScreen());
        dTO.setIndex(entity.getId());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
        dTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dTO.setCreatedDate(entity.getCreationDate());

        return dTO;
    }

    private ReportSignature mapFromDTO(ReportSignatureDTO dTO, IsagUser isagUser) {
        ReportSignature entity = new ReportSignature();
        entity.setSignature1(dTO.getSignature1());
        entity.setSignature2(dTO.getSignature2());
        entity.setSignature3(dTO.getSignature3());
        entity.setSignature4(dTO.getSignature4());
        entity.setSignature5(dTO.getSignature5());
        entity.setScreen(dTO.getScreen());

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }
        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private String validation(ReportSignatureDTO dTO) {
        StringBuilder builder = new StringBuilder();
        if ((dTO.getSignature1() == null || dTO.getSignature1().isEmpty()) && (dTO.getSignature2() == null || dTO.getSignature2().isEmpty()) && (dTO.getSignature3() == null || dTO.getSignature3().isEmpty())
                && (dTO.getSignature4() == null || dTO.getSignature4().isEmpty()) && (dTO.getSignature5() == null || dTO.getSignature5().isEmpty())) {
            builder.append("من فضلك قم بادخال التوقيعات علي الاقل توقيع واحد");
        }
        if (dTO != null && dTO.getScreen() == null) {
            builder.append("لا يمكن ترك اسم التقرير فارغ");
        }
        if (dTO != null && dTO.getScreen() != null && dTO.getId() == null) {
            if (ReportSignatureAleardyExist(dTO)) {
                builder.append("هذا التقرير موجود من قبل");
            }
        }
        return builder.toString();
    }
}
