/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.MnsLoginExamDTO;
import com.isag.entity.IgmaUser;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface MnsLoginExamService {

    public Integer findLoginExam(MnsLoginExamDTO loginExamDTO);

    public void deleteMnsLoginExam(MnsLoginExamDTO mnsLoginExamDTO);

    public void deleteMnsLoginExam(Integer mnsLoginExamId);

}
