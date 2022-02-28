/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.common;

import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class Validation {
    
    public static Boolean isDiscountValid(BigDecimal discValue) {
        if (discValue != null) {
            return (discValue.compareTo(BigDecimal.ZERO) == 0 || discValue.compareTo(BigDecimal.ZERO) == 1)
                    && (discValue.compareTo(BigDecimal.valueOf(100)) == 0 || BigDecimal.valueOf(100).compareTo(discValue) == 1);
        } else {
            return false;
        }
    }
    
}
