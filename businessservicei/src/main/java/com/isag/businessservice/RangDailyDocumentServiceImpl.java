/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.RangDailyDocument;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class RangDailyDocumentServiceImpl implements RangDailyDocumentService {

    @EJB
    private GenericDAO dao;

    @Override
    public void deleteRangDailyDocument(RangDailyDocument dailyDocument) {
        dao.deleteEntity(dailyDocument);
    }

    @Override
    public List<RangDailyDocument> getAllRangDailyDocument() {
        return dao.findAll(RangDailyDocument.class);
    }

    @Override
    public RangDailyDocument addRangDailyDocument(RangDailyDocument dailyDocument) {
        return dao.updateEntity(dailyDocument);
    }

    @Override
    public RangDailyDocument findRangDailyDocument(Integer rangDailyDocumentId) {
        return dao.findEntityById(RangDailyDocument.class, rangDailyDocumentId);
    }

    @Override
    public RangDailyDocument updateRangDailyDocument(RangDailyDocument dailyDocument) {
        return dao.updateEntity(dailyDocument);
    }

    public List<RangDailyDocument> findRangDailyDocumentNames() {
        List<RangDailyDocument> rangDailyDocument = dao.executeNativeQuery("SELECT * FROM rang_daily_document WHERE deleted_by is null");
        return rangDailyDocument;
    }

}
