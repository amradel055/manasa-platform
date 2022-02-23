/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author hq004
 */
@Named(value = "globalInventoryBean")
@ApplicationScoped
public class GlobalInventoryBean implements Serializable {


    @PostConstruct
    public void init() {
        

    }


}
