/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.isag.businessservice.IgmaTokensService;

/**
 *
 * @author AhmedEssam@ISAG.com
 * @since 2020.06.23
 */
public class ServerConfig implements ServletContextListener {

    @EJB
    IgmaTokensService isagTokensService;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        isagTokensService.UpdateActiveAllIgmaTokenDTO(Boolean.FALSE);
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
    
}
