/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.RangDailyDocumentDetail;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RangDailyDocumentDetailService {

    public void deleteRangDailyDocumentDetail(RangDailyDocumentDetail dailyDocumentDetail);

    public RangDailyDocumentDetail addRangDailyDocumentDetail(RangDailyDocumentDetail dailyDocumentDetail);

    public RangDailyDocumentDetail updateRangDailyDocumentDetail(RangDailyDocumentDetail dailyDocumentDetail);
}
