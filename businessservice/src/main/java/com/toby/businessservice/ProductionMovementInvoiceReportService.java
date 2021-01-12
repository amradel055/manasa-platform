package com.toby.businessservice;

import com.toby.dto.PrintProProductDTO;
import com.toby.views.ProductionMovementInvoiceReport;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ProductionMovementInvoiceReportService {
    
    public List<ProductionMovementInvoiceReport> getAll(PrintProProductDTO printProProductDTO);
    
}
