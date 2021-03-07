package com.toby.businessservice;

import com.toby.dto.PrintProductionDTO;
import com.toby.views.InvoiceProductionStagesReport;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface InvoiceProductionStagesReportService {
    
    public List<InvoiceProductionStagesReport> searchReport(PrintProductionDTO productionDTO, Integer branchId);
    
}
