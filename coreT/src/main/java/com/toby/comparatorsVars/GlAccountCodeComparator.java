/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.comparatorsVars;

import com.toby.entity.GlAccount;
import java.util.Comparator;

/**
 *
 * @author ahmed
 */
public class GlAccountCodeComparator implements Comparator<GlAccount> {

    @Override
    public int compare(GlAccount t, GlAccount t1) {
        if (t.getAccNumber()> t1.getAccNumber()) {
            return 1;
        } else {
            return -1;
        }
    }

}
