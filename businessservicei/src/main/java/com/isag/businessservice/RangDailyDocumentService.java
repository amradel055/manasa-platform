/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.RangDailyDocument;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RangDailyDocumentService {

    public void deleteRangDailyDocument(RangDailyDocument dailyDocument);

    public List<RangDailyDocument> getAllRangDailyDocument();

    public RangDailyDocument addRangDailyDocument(RangDailyDocument dailyDocument);

    public RangDailyDocument findRangDailyDocument(Integer rangDailyDocumentId);

    public RangDailyDocument updateRangDailyDocument(RangDailyDocument dailyDocument);
}
