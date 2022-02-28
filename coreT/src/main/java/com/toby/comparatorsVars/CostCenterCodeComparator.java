/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.comparatorsVars;

import com.toby.entity.CostCenter;
import java.util.Comparator;

/**
 *
 * @author ahmed
 * @param <CostCenter>
 */
public class CostCenterCodeComparator implements Comparator<CostCenter> {

    @Override
    public int compare(CostCenter t, CostCenter t1) {
        if (t.getCode() > t1.getCode()) {
            return 1;
        } else {
            return -1;
        }
    }

}
