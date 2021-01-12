/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;


import com.toby.businessservice.reports.searchBean.GeneralSymbolSearchBean;
import com.toby.entity.GeneralSymbol;
import com.toby.entity.Symbol;
import com.toby.views.GeneralSymbolView;
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
    
    public List<GeneralSymbolView> getAllGSymbol(GeneralSymbolSearchBean generalSymbolSearchBean);
    
    public List<Symbol> getSymbolsByCompanyId(Integer companyId);

    public List<GeneralSymbol> getAllGSymbolAscended();
}
