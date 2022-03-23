/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.root;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AhmedESSAM
 */
@XmlRootElement
public class LoginRoot {

    @XmlElement
    public String userName;
    @XmlElement
    public String password;
    @XmlElement
    public String mobile;
    @XmlElement
    public Integer branchId;
    @XmlElement
    public Integer companyId;
    @XmlElement
    public Integer type;
    @XmlElement
    public Integer branchSelected;

}
