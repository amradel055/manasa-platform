package com.isag.businessservice;

import com.isag.dto.IgmaMessageDTO;
import javax.ejb.Remote;

@Remote
public interface IgmaMessageService {

    public IgmaMessageDTO getIgmaMessageDTO(Integer branchId);
    
    public void updateIgmaMessage(Integer IgmaMessageId, Integer remainNumber);
    
}
