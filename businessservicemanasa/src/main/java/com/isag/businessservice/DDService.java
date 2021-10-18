/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.DataDictionary;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface DDService {

    public Map<String, String> getDDByCompanyAndLanguage(Integer lang_id, Integer companyId);

    public List<DataDictionary> getDefaultDDsByLanguage(Integer id);

    public DataDictionary addDataDictionary(DataDictionary newDD);

    public DataDictionary update(DataDictionary dataDictionary);

    public List<DataDictionary> getALLDataDictionaryByCompanyId(Integer id);

    public void saveAll(List<DataDictionary> dataDictionaryList);


}
