/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.altf4.Blog.dto;

import javax.persistence.Column;
import lombok.Data;

/**
 *
 * @author dnsfu
 */
@Data
public class Role {

    private int roleId;

    @Column(nullable = false)
    private String role;

}
