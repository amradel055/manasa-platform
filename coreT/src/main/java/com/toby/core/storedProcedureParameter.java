/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.core;

/**
 *
 * @author AhmedEssam
 */
public class storedProcedureParameter {

    public storedProcedureParameter(String parameterName, Class parameterClass, char parameterType) {
        this.parameterName = parameterName;
        this.parameterClass = parameterClass;
        this.parameterType = parameterType;
    }

    private String parameterName;
    private Class parameterClass;
    private Character parameterType;
    
    /**
     * @return the parameterName
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * @param parameterName the parameterName to set
     */
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    /**
     * @return the parameterClass
     */
    public Class getParameterClass() {
        return parameterClass;
    }

    /**
     * @param parameterClass the parameterClass to set
     */
    public void setParameterClass(Class parameterClass) {
        this.parameterClass = parameterClass;
    }

    /**
     * @return the parameterType
     */
    public Character getParameterType() {
        return parameterType;
    }

    /**
     * @param parameterType the parameterType to set
     */
    public void setParameterType(Character parameterType) {
        this.parameterType = parameterType;
    }
     
}
