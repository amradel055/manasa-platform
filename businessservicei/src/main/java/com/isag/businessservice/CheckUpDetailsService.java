/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.HosCheckUpDetail;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface CheckUpDetailsService {

    public void addHosCheckUpDetails(HosCheckUpDetail hosCheckUpDetail);

    public void deleteHosCheckUpDetails(HosCheckUpDetail hosCheckUpDetail);

    public HosCheckUpDetail updateHosCheckUpDetails(HosCheckUpDetail hosCheckUpDetail);

    public List<HosCheckUpDetail> getAllHosCheckUpDetailsByHosCheckUpId(Integer hosCheckUpId);

    public HosCheckUpDetail getHosCheckUpDetailByHosCheckUpId(Integer hosCheckUpId);

    public HosCheckUpDetail findHosCheckUpDetailsById(Integer hosCheckUpDetailsId);

    public HosCheckUpDetail addOneHosCheckUpDetail(HosCheckUpDetail hosCheckUpDetail);

    public Integer finMaxSerial(Integer hosCheckUpId);

}
