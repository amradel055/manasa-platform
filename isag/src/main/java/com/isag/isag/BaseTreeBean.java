/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

/**
 *
 * @author hq003
 */
public abstract class BaseTreeBean extends Basic implements BaseManagedBean{
    
    public abstract String save();
    
    public abstract void init();
    
    public abstract void delete();
    
    public abstract void add();
    
    private Boolean showMessage;

    /**
     * @return the showMessage
     */
    public Boolean getShowMessage() {
        return showMessage;
    }

    /**
     * @param showMessage the showMessage to set
     */
    public void setShowMessage(Boolean showMessage) {
        this.showMessage = showMessage;
    }
}
