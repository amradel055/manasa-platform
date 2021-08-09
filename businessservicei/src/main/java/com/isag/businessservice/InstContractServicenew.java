/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface InstContractServicenew {

    public InstContract addInstContract(InstContract instContract, List<InstContractDetail> instContractDetailList, List<InstContractDetail> instContractDetailListDeleted,Map<String, String> userDDs);

    public Integer getMaxSerail(InstContract instContract);
    
    public boolean checkContractSerial(InstContract instContract);
}
