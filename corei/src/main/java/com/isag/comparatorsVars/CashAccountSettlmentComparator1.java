/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.comparatorsVars;

import com.isag.views.CashAccountSettlment;
import java.util.Comparator;

/**
 *
 * @author ahmed
 */
public class CashAccountSettlmentComparator1 implements Comparator<CashAccountSettlment> {

    @Override
    public int compare(CashAccountSettlment t, CashAccountSettlment t1) {
        if (t.getValue().compareTo(t1.getValue()) == 1) {
            return -1;
        } else {
            return 1;
        }
    }

}
