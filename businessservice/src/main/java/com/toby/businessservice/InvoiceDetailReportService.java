package com.toby.businessservice;

import com.toby.dto.PrintProductionDTO;
import com.toby.entity.TobyUser;
import com.toby.views.InvoiceDetailsReport;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface InvoiceDetailReportService {
    
    public List<InvoiceDetailsReport> searchReport(PrintProductionDTO productionDTO,TobyUser tobyUser);
    
}
