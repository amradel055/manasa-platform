package com.toby.businessservice;

import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.entity.ProProductionStages;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserProproduction;
import com.toby.entity.TobyUserRole;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface TobyUserProproductionService {
    
    public List<TobyUserDTO> getUsersWithProductionStagesForBranch(Integer selectedBranchId);
    
    public void deleteProductionStages(TobyUserRole userRole, ProProductionStagesDTO productionStages);
    
    public void deleteProductionStages(TobyUserRole userRole, ProProductionStages productionStages);
    
    public TobyUserProproduction updateTobyUserProproduction(TobyUserProproduction tobyUserProproduction, TobyUser tobyUser);
}
