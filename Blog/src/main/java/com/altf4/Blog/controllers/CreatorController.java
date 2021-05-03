/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.altf4.Blog.controllers;

import com.altf4.Blog.dao.MemoDao;
import com.altf4.Blog.dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.altf4.Blog.dao.UserDao;

/**
 *
 * @author dnsfu
 */
@Controller
public class CreatorController {
    
    @Autowired
    MemoDao memoDao;
    
    @Autowired
    TagDao tagDao;
    
    @Autowired
    UserDao creatorDao;

}