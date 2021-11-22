/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.Exams;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class ManasaReportServiceImp implements ManasaReportService {

    @EJB
    private GenericDAO dao;
   
    @Override
    public Integer findExamStudentNumber(Integer examId) {

        Map<String, Object> params = new HashMap<>();
        params.put("parent", examId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(e.id) from Exams e where e.parent.id = :parent group by e.parent.id  ");
        List<Long> list = dao.findListByQuery(builder.toString(), params);
        Integer number = 0 ; 
        if(list != null && !list.isEmpty()){
            Long x = list.get(0);
            number = x.intValue();
        }
        return number;
    }

}
