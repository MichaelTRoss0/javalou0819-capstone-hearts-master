/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.altf4.Blog.dao;

import com.altf4.Blog.dao.RoleDaoDB.RoleMapper;
import com.altf4.Blog.dto.User;
import com.altf4.Blog.dto.Memo;
import com.altf4.Blog.dto.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dnsfu
 */

@Repository
public class UserDaoDB implements UserDao {
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public User getUserById(int id) {
        try {
            final String SELECT_USER_BY_ID = "SELECT * FROM user WHERE userid = ?";
            User user = jdbc.queryForObject(SELECT_USER_BY_ID, new UserMapper(), id);
            user.setRoles(getRolesForUser(user.getUserId()));
            return user;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            final String SELECT_USER_BY_USERNAME = "SELECT * FROM user WHERE username = ?";
            User user = jdbc.queryForObject(SELECT_USER_BY_USERNAME, new UserMapper(), username);
            user.setRoles(getRolesForUser(user.getUserId()));
            return user;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        final String SELECT_ALL_USERS = "SELECT * FROM user";
        List<User> users = jdbc.query(SELECT_ALL_USERS, new UserMapper());
        for (User user : users) {
            user.setRoles(getRolesForUser(user.getUserId()));
        }
        return users;
    }

    private Set<Role> getRolesForUser(int id) throws DataAccessException {
        final String SELECT_ROLES_FOR_USER = "SELECT r.* FROM userrole ur "
                + "JOIN role r ON ur.roleid = r.roleid "
                + "WHERE ur.userid = ?";
        Set<Role> roles = new HashSet(jdbc.query(SELECT_ROLES_FOR_USER, new RoleMapper(), id));
        return roles;
    }

    @Override
    public void updateUser(User user) {
        final String UPDATE_USER = "UPDATE user SET username = ?, password = ?,privilege = ?, enabled = ? WHERE userid = ?";
        jdbc.update(UPDATE_USER, user.getUsername(), user.getPassword(), user.getPrivilege(), user.isEnabled(), user.getUserId());

        final String DELETE_USER_ROLE = "DELETE FROM userrole WHERE userid = ?";
        jdbc.update(DELETE_USER_ROLE, user.getUserId());
        for (Role role : user.getRoles()) {
            final String INSERT_USER_ROLE = "INSERT INTO userrole(userid, roleid) VALUES(?,?)";
            jdbc.update(INSERT_USER_ROLE, user.getUserId(), role.getRoleId());
        }
    }

    @Override
    public void deleteUser(int id) {
        final String DELETE_USER_ROLE = "DELETE FROM userrole WHERE userid = ?";
        final String DELETE_USER = "DELETE FROM user WHERE userid = ?";
        jdbc.update(DELETE_USER_ROLE, id);
        jdbc.update(DELETE_USER, id);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        final String INSERT_USER = "INSERT INTO user(username, password, privilege, enabled) VALUES(?,?,?,?)";
        jdbc.update(INSERT_USER, user.getUsername(), user.getPassword(), user.getPrivilege(), user.isEnabled());
        int newId = jdbc.queryForObject("select LAST_INSERT_ID()", Integer.class);
        user.setUserId(newId);

        for (Role role : user.getRoles()) {
            final String INSERT_USER_ROLE = "INSERT INTO userrole(userid, roleid) VALUES(?,?)";
            jdbc.update(INSERT_USER_ROLE, user.getUserId(), role.getRoleId());
        }
        return user;
    }

    @Override
    public User getCreatorForMemo(Memo m){
        String sql
                = "SELECT u.* "
                + "FROM MemoUser mu "
                + "JOIN User u ON mu.UserID = u.UserID "
                + "WHERE mu.MemoID = ?";
        return jdbc.queryForObject(sql, new UserMapper(), m.getMemoId());
    }

    private class UserMapper implements RowMapper<User>{

        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User c = new User();
            c.setUserId(rs.getInt("userId"));
            c.setUsername(rs.getString("username"));
            c.setPassword(rs.getString("password"));
            c.setEnabled(rs.getBoolean("enabled"));
            c.setPrivilege(rs.getInt("privilege"));
            c.setRoles(getRolesForUser(c.getUserId()));
            return c;
        }
    }

}
