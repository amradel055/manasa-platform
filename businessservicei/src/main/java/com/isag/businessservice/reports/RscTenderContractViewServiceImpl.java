/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.GenericService;
import com.isag.businessservice.reports.searchBean.RscTenderContractReportSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.RscTenderContractViewDTO;
import com.isag.entity.IsagUser;
import com.isag.views.RscTenderContractView;
import java.util.ArrayList;
import java.util.Date;
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
public class RscTenderContractViewServiceImpl implements RscTenderContractViewService {

    @EJB
    private GenericDAO dao;

    StringBuilder appendQuery;

    @EJB
    private GenericService genericService;

    @Override
    public List<RscTenderContractViewDTO> getAllRscTenderContractViewByDateSearchBean(RscTenderContractReportSearchBean rscTenderContractReportSearchBean,Integer branchId) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RTCV FROM RscTenderContractView RTCV WHERE RTCV.branchId = :branchId ");

       
        stringBuilder.append(appendDate(rscTenderContractReportSearchBean, params));
        List<RscTenderContractViewDTO> dTOList = new ArrayList<>();
         List<RscTenderContractView> list = dao.findListByQuery(stringBuilder.toString(), params);
        returnListDTO(list, dTOList);
        return dTOList;
        
          
    }
     private String appendDate(RscTenderContractReportSearchBean rscTenderContractReportSearchBean, Map<String, Object> params) {
         appendQuery = new StringBuilder();

        if (rscTenderContractReportSearchBean.getFromDate()!= null) {
            params.put("fromDate",rscTenderContractReportSearchBean.getFromDate());
            appendQuery.append(" AND RTCV.date >= :fromDate");
        }

        if (rscTenderContractReportSearchBean.getToDate()!= null) {
            params.put("toDate", rscTenderContractReportSearchBean.getToDate());
            appendQuery.append(" AND RTCV.date <= :toDate");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public RscTenderContractViewDTO mapTODTO(RscTenderContractView tender) {
        RscTenderContractViewDTO dTO = new RscTenderContractViewDTO();
        initMapToDTO(dTO, tender);

        return dTO;
    }

    private void initMapToDTO(RscTenderContractViewDTO dTO, RscTenderContractView tender) {

   
            dTO.setCustomerId(tender.getCustomerId());
       
        dTO.setDate(tender.getDate());
        dTO.setDocNumber(tender.getDocNumber());
        dTO.setGeneralConsultant(tender.getGeneralConsultant());
        dTO.setProfissionalBussinessConsultant(tender.getProfissionalBussinessConsultant());
        dTO.setProjectManager(tender.getProjectManager());
        dTO.setRemark(tender.getRemark());
        dTO.setSerial(tender.getSerial());
        dTO.setCustomerName(tender.getCustomerName());
        dTO.setGeneralConsultantName(tender.getGeneralConsultantName());
        dTO.setProfissionalBussinessConsultantName(tender.getProfissionalBussinessConsultantName());
        dTO.setProjectManagerName(tender.getProjectManagerName());
        dTO.setCustomerCode(tender.getCustomerCode());
        dTO.setStatus(tender.getStatus() != null ? tender.getStatus() : 0);
        if (dTO.getStatus() == 0 || dTO.getStatus() == null) {
            dTO.setStatusText("لم يتم");
        } else {
            dTO.setStatusText("تم");
        }
        dTO.setId(tender.getId());
        dTO.setCreatedDate(tender.getCreationDate());
        dTO.setCreatedBy(tender.getCreatedBy().getId());
    }

    public RscTenderContractView mapFromDTO(RscTenderContractViewDTO dTO, IsagUser isagUser) {
        RscTenderContractView tenderContractView = new RscTenderContractView();
        
        tenderContractView.setCustomerId(dTO.getCustomerId());
        tenderContractView.setCustomerName(dTO.getCustomerName());
        tenderContractView.setCustomerCode(dTO.getCustomerCode());
        if (dTO.getGeneralConsultant() != null) {

            tenderContractView.setGeneralConsultant(dTO.getGeneralConsultant());
            tenderContractView.setGeneralConsultantName(dTO.getGeneralConsultantName());
        }
        if (dTO.getProfissionalBussinessConsultant() != null) {

            tenderContractView.setProfissionalBussinessConsultant(dTO.getProfissionalBussinessConsultant());
            tenderContractView.setProfissionalBussinessConsultantName(dTO.getProfissionalBussinessConsultantName());
        }
        if (dTO.getProjectManager() != null) {
            tenderContractView.setProjectManager(dTO.getProjectManager());
            tenderContractView.setProjectManagerName(dTO.getProjectManagerName());
        }

        tenderContractView.setDate(dTO.getDate());
        tenderContractView.setDocNumber(dTO.getDocNumber());
        tenderContractView.setRemark(dTO.getRemark());
        tenderContractView.setSerial(dTO.getSerial());
        tenderContractView.setStatus(dTO.getStatus());
       
        tenderContractView.setCompanyId(isagUser.getCompanyId());
        if (dTO.getId() == null) {
            tenderContractView.setCreatedBy(isagUser);
            tenderContractView.setCreationDate(new Date());
        } else {
            tenderContractView.setId(dTO.getId());
            tenderContractView.setModificationDate(new Date());
            tenderContractView.setModifiedBy(isagUser);
            IsagUser user = new IsagUser();
            user.setId(dTO.getCreatedBy());
            tenderContractView.setCreatedBy(user);
            tenderContractView.setCreationDate(dTO.getCreatedDate());
        }
        return tenderContractView;
    }

    private void returnListDTO(List<RscTenderContractView> list, List<RscTenderContractViewDTO> dTOList) {
        for (RscTenderContractView tender : list) {
            dTOList.add(mapTODTO(tender));
        }
    }

}
