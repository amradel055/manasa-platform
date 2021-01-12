/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.comparators;

import com.toby.report.entity.SubAccountSummaryReport;
import java.util.Comparator;

/**
 *
 * @author ahmed
 */
public class SubAccountSummaryBalanceComparator implements Comparator<SubAccountSummaryReport> {

    @Override
    public int compare(SubAccountSummaryReport t, SubAccountSummaryReport t1) {
        if (t.getBalance().compareTo(t1.getBalance()) == 1) {
            return 1;
        } else {
            return -1;
        }
    }

}
