/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.RangDailyDocumentDetail;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class RangDailyDocumentDetailServiceImpl implements RangDailyDocumentDetailService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteRangDailyDocumentDetail(RangDailyDocumentDetail dailyDocumentDetail) {
        dao.deleteEntity(dailyDocumentDetail);
    }

    @Override
    public RangDailyDocumentDetail addRangDailyDocumentDetail(RangDailyDocumentDetail dailyDocumentDetail) {
        return dao.updateEntity(dailyDocumentDetail);
    }

    @Override
    public RangDailyDocumentDetail updateRangDailyDocumentDetail(RangDailyDocumentDetail dailyDocumentDetail) {
        return dao.updateEntity(dailyDocumentDetail);
    }

    public List<RangDailyDocumentDetail> findAllRangdailydocumentdetailChildByParentId(Integer parentId) {
        List<RangDailyDocumentDetail> rangDailyDocumentDetail = dao.executeNativeQuery("SELECT * FROM Rangdailydocumentdetail WHERE  deletedby is null and rangdailydocumentid = " + parentId);
        return rangDailyDocumentDetail;
    }
}
