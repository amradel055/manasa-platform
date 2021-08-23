/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.GeneralSymbolSearchBean;
import com.isag.entity.GeneralSymbol;
import com.isag.entity.Symbol;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface GeneralSymbolService {
    public List<GeneralSymbol> getAllGSymbol();

    public GeneralSymbol addGSymbol(GeneralSymbol gSymbol);

    public GeneralSymbol updateGSymbol(GeneralSymbol gSymbol);

    public void deleteGSymbol(GeneralSymbol gSymbol);
    
    public List<GeneralSymbol> getAllGSymbolListByCompanyId(Integer companyId);
    
    public List<GeneralSymbol> getAllGeneralSymbolListByCompanyId(Integer companyId);

    public List<Symbol> getSymbolsForGeneralSymbol(Integer selectedGSymbol ,  Integer companyId);

    public List<GeneralSymbol> getAllGSymbolWithoutLanguage();
        
    public List<Symbol> getSymbolsByCompanyId(Integer companyId);
    
    public List<GeneralSymbol> getAllGSymbolAscended();
}
