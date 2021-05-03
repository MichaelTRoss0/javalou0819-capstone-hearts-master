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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.altf4.Blog.dao.UserDao;

/**
 *
 * @author mike
 */
@Controller
public class AboutController {

    @Autowired
    UserDao creatorDao;

    @Autowired
    MemoDao memoDao;

    @Autowired
    TagDao tagDao;

    @GetMapping("/about")
    public String displayAboutInfo(Model model) {
        return "about";
    }

}
