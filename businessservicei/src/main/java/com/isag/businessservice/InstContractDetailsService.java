/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.InstContractDetail;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface InstContractDetailsService {

    public void addInstContractDetails(InstContractDetail instContractDetail);

    public void deleteInstContractDetails(InstContractDetail instContractDetail);

    public InstContractDetail updateInstContractDetails(InstContractDetail instContractDetail);

    public List<InstContractDetail> getAllInstContractDetailsByInstContractId(Integer instContractId);

    public List<InstContractDetail> getAllUnPaidInstContractDetailsByInstContractId(Integer instContractId);

    public InstContractDetail getInstContractDetailByInstContractId(Integer instContractId);

    public InstContractDetail findInstContractDetailsById(Integer instContractDetailId);

    public InstContractDetail addOneInstContractDetail(InstContractDetail instContractDetail);

     public Integer finMaxSerial(Integer instContractlId);
}
