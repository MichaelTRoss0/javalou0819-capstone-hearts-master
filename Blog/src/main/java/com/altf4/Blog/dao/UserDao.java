/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.altf4.Blog.dao;

import com.altf4.Blog.dto.User;
import com.altf4.Blog.dto.Memo;
import java.util.List;

/**
 *
 * @author dnsfu
 */
public interface UserDao {
    public User getCreatorForMemo(Memo m);
    User getUserById(int id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(int id);
    User createUser(User user);
}
