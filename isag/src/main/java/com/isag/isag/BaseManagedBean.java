/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import java.io.Serializable;

/**
 *
 * @author hq004
 */
public interface BaseManagedBean extends Serializable {

    public abstract void load();

    public abstract String getScreenName();

}
