/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.IsagTokensService;
import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author AhmedEssam@ISAG.com
 * @since 2020.06.23
 */
public class ServerConfig implements ServletContextListener {

    @EJB
    IsagTokensService isagTokensService;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        isagTokensService.UpdateActiveAllIsagTokenDTO(Boolean.FALSE);
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
    
}
