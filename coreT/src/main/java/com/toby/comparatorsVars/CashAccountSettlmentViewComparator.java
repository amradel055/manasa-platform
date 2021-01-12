/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.comparatorsVars;

import com.toby.views.CashAccountSettlmentView;
import java.util.Comparator;

/**
 *
 * @author ahmed
 */
public class CashAccountSettlmentViewComparator implements Comparator<CashAccountSettlmentView> {

    @Override
    public int compare(CashAccountSettlmentView t, CashAccountSettlmentView t1) {
        if (t.getValue().compareTo(t1.getValue()) == 1) {
            return -1;
        } else {
            return 1;
        }
    }

}
