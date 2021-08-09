/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.common;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author hq003
 */
public class GeneralErrorHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory parent;

    public GeneralErrorHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new GeneralErrorHandler(parent.getExceptionHandler());

    }

}
