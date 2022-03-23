/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.comparatorsVars;

import com.isag.entity.CostCenter;
import com.isag.entity.InstProjectGuide;
import java.util.Comparator;

/**
 *
 * @author ahmed
 * @param <CostCenter>
 */
public class ProjectGuideComparator implements Comparator<InstProjectGuide> {

    @Override
    public int compare(InstProjectGuide t, InstProjectGuide t1) {
        if (t.getCode().compareTo(t1.getCode())>0) {
            return 1;
        } else {
            return -1;
        }
    }

}
