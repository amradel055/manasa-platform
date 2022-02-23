/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author hq004
 */
@Named(value = "globalRscBean")
@ApplicationScoped
public class GlobalRscBean implements Serializable {

    private static GlobalRscBean jdbc;

    private GlobalRscBean() {
    }

    public static GlobalRscBean getInstance() {
        if (jdbc == null) {
            jdbc = new GlobalRscBean();
        }
        return jdbc;
    }

}
