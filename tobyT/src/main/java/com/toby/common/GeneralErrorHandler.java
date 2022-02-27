/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.common;

import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hq003
 */
public class GeneralErrorHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;

    public GeneralErrorHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;

    }

    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();

        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context
                    = (ExceptionQueuedEventContext) event.getSource();
            // get the exception from context             
            Throwable t = context.getException();
            final FacesContext fc = FacesContext.getCurrentInstance();
            final ExternalContext externalContext = fc.getExternalContext();
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            final ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            //here you do what ever you want with exception             
            try {
                //log.log(Level.SEVERE, "Critical Exception!", t);                   
                //redirect error page                 
                requestMap.put("exceptionMessage", t.getMessage());
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

                NavigationCase naveCase = nav.getNavigationCase(fc, "*", "login");
                nav.handleNavigation(fc, naveCase.getFromAction(), naveCase.getFromOutcome());

                fc.renderResponse();
                // remove the comment below if you want to report the error in a jsf error message                 
                //JsfUtil.addErrorMessage(t.getMessage());               
            } finally {
                //remove it from queue                 
                i.remove();
            }
        }
        //parent hanle         
        getWrapped().handle();
    }

}
