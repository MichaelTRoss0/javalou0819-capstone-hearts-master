/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.altf4.Blog.dao;

import com.altf4.Blog.dto.Memo;
import com.altf4.Blog.dto.Tag;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dnsfu
 */
@Repository
public class TagDaoDB implements TagDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Tag get(int id) {
        String sql = "SELECT * FROM Tag WHERE TagID = ?";
        return jdbc.queryForObject(sql, new TagMapper(), id);
    }

    @Override
    public List<Tag> getAll() {
        String sql = "SELECT * FROM Tag";
        return jdbc.query(sql, new TagMapper());
    }

    @Override
    public List<Tag> getTagsForPost(Memo m) {
        String sql
                = "SELECT t.* "
                + "FROM MemoTag mt "
                + "JOIN Tag t ON mt.TagID = t.TagID "
                + "WHERE mt.MemoID = ?";
        return jdbc.query(sql, new TagMapper(), m.getMemoId());
    }

    @Override
    public void deleteTagById(int id) {
        String sql
                = "DELETE FROM MemoTag WHERE TagID = ?";
        jdbc.update(sql, id);

        sql
                = "DELETE FROM Tag WHERE TagID = ?";
        jdbc.update(sql, id);
    }

    @Override
    public Tag addTagToDB(Tag t) {
        String sql
                = "INSERT INTO Tag(Hashtag) "
                + "VALUES (?)";
        jdbc.update(sql, t.getHashTag());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        t.setTagId(newId);
        return t;
    }

    @Override
    public Tag editTag(Tag t) {
        String sql = "UPDATE Tag SET Hashtag = ?";
        jdbc.update(sql,
                t.getHashTag());
        return t;
    }

    @Override
    public List<Tag> generateTagsFromString(String tags) {
        String[] split = tags.split("\\s+");
        List<Tag> generatedListOfTags = new ArrayList<>();
        for(String s : split){
            Tag t = new Tag();
            t.setHashTag(s);
            addTagToDB(t);
            generatedListOfTags.add(t);
        }
        
        return generatedListOfTags;
    }

    public class TagMapper implements RowMapper<Tag> {

        @Override
        public Tag mapRow(ResultSet rs, int i) throws SQLException {
            Tag t = new Tag();
            t.setTagId(rs.getInt("tagId"));
            t.setHashTag(rs.getString("hashTag"));
            return t;
        }

    }
}
