/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.root;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AhmedEssam <ahmedEssam.com>
 */
@XmlRootElement
public class PayDTO {

    @XmlElement
    public String token;
    @XmlElement
    public String entityId;
    @XmlElement
    public BigDecimal amount;
    @XmlElement
    public String currency;
    @XmlElement
    public String paymentType;
}
