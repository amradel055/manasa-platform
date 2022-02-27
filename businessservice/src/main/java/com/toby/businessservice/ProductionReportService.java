package com.toby.businessservice;

import com.toby.dto.PrintProductionDTO;
import com.toby.entity.TobyUser;
import com.toby.views.ProductionReport;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ProductionReportService {
    
    public List<ProductionReport> searchReport(PrintProductionDTO productionDTO,TobyUser tobyUser);

}
