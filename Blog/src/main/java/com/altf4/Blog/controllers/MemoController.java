/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.altf4.Blog.controllers;

import com.altf4.Blog.dao.MemoDao;
import com.altf4.Blog.dao.RoleDao;
import com.altf4.Blog.dao.TagDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.altf4.Blog.dao.UserDao;
import com.altf4.Blog.dto.Memo;
import com.altf4.Blog.dto.Tag;
import com.altf4.Blog.dto.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author dnsfu
 */
@Controller
public class MemoController {

    @Autowired
    MemoDao memoDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @GetMapping("newPost")
    public String displayNewPost(Model model) {
        List<User> allUsers = userDao.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        
        String now = memoDao.convertDateToString(LocalDateTime.now());
        String nowPlusOneMonth = memoDao.convertDateToString(LocalDateTime.now().plusMonths(1));
        model.addAttribute("uploadDate", now);
        model.addAttribute("deleteDate", nowPlusOneMonth);

        return "newPost";
    }

    @PostMapping("newPost")
    public String addMemo(String title, String bodyText, String uploadDate, String deleteDate, String tags, Integer id) {

        Memo m = new Memo();
        m.setTitle(title);
        m.setBodyText(bodyText);
        m.setUploadDate(LocalDateTime.parse(uploadDate, FORMATTER));
        m.setDeleteDate(LocalDateTime.parse(deleteDate, FORMATTER));
        m.setUser(userDao.getUserById(id));
        m.setApproved(false);
        m.setTags(tagDao.generateTagsFromString(tags));
        memoDao.addMemo(m);

        return "redirect:/newPost";
    }

    @GetMapping("viewPostOn")
    public String viewPostsOn(Model model, String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime temp = LocalDateTime.parse(time, formatter);
        List<Memo> post = memoDao.getAllForDate(temp);
        model.addAttribute("post", post);

        return "viewPostOn";
    }
    
    @GetMapping("viewPostsBy")
    public String viewPostsBy(Model model, Integer id){

        List<Memo> allPosts = memoDao.getAllApproved();
        List<Memo> posts = new ArrayList<>();
        
        for(Memo post : allPosts){
            if(post.getUser().getUserId() == id){
                posts.add(post);
            }
        }

        model.addAttribute("post", posts);
        return "viewPostsBy";
    }
    
    @GetMapping("viewPost")
    public String displayPost(Model model, Integer id){

        Memo thePost = memoDao.get(id);
        
        List<Memo> post = new ArrayList<>();
        post.add(thePost);
        
        model.addAttribute("post", post);
        
        return "viewPost";
    }

    @GetMapping("editPost")
    public String editPost(Model model, Integer id) {

        Memo post = memoDao.get(id);
        model.addAttribute("post", post);

        List<User> allUsers = userDao.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        
        List<Tag> tagList = tagDao.getTagsForPost(post);
        String tags = "";

        for(Tag t : tagList){
            tags += t.getHashTag() + " ";
        }
        
        model.addAttribute("bodyText", post.getBodyText());
        model.addAttribute("tagsAsString", tags);

        return "editPost";
    }

    @PostMapping("/editPost")
    public String performEditPost(@Valid Memo post, BindingResult result, Integer userId, Integer postId, String tagsAsString) {
//        if (result.hasErrors()) {
//            return "editPost";
//        }
        post.setMemoId(postId);
        post.setUser(userDao.getUserById(userId));
        post.setTags(tagDao.generateTagsFromString(tagsAsString));
        memoDao.editMemo(post);
        
        return "redirect:/admin";
    }

}
