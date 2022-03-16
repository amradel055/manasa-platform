/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AhmedESSAM
 */
public class ChartDataDTO {

    private String label;
    private Map<Object,Number> map;
    
  
    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * @return the map
     */
    public Map<Object,Number> getMap() {
        if(map == null){
        map = new HashMap<>();
        }
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map<Object,Number> map) {
        this.map = map;
    }

    
}
