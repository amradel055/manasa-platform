/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.InvPriceSellItem;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvPriceSellItemService {

    public InvPriceSellItem addInvPriceSellItem(InvPriceSellItem invPriceSellItem);
}
