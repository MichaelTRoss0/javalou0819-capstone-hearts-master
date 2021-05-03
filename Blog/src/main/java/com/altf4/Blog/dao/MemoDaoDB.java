/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.altf4.Blog.dao;

import com.altf4.Blog.dto.User;
import com.altf4.Blog.dto.Memo;
import com.altf4.Blog.dto.Tag;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class MemoDaoDB implements MemoDao {

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    UserDao userDao;

    @Autowired
    TagDao tagDao;

    @Override
    public Memo get(int id) {
        String sql = "SELECT * From Memo WHERE MemoID = ?";
        return jdbc.queryForObject(sql, new MemoMapper(), id);
    }

    @Override
    public List<Memo> getAll() {
        String sql = "SELECT * FROM Memo";
        return jdbc.query(sql, new MemoMapper());
    }

    @Override
    public void delete(int id) {
        String sql
                = "DELETE FROM MemoTag WHERE MemoID = ?";
        jdbc.update(sql, id);

        sql
                = "DELETE FROM MemoCreator WHERE MemoID = ?";
        jdbc.update(sql, id);

        sql
                = "DELETE FROM Memo WHERE MemoID = ?";
        jdbc.update(sql, id);
    }

    @Override
    public List<Memo> getAllForDate(LocalDateTime time) {
        LocalDateTime nextMonth = time.plusMonths(1);
        String sql = "SELECT * FROM Memo WHERE UploadDate BETWEEN ? and ?";
        return jdbc.query(sql, new MemoMapper(), time, nextMonth);
    }

    @Override
    public List<Memo> getAllNotApproved() {
        String sql = "SELECT * FROM Memo WHERE isApproved = FALSE";
        return jdbc.query(sql, new MemoMapper());
    }
    
    @Override
    public String convertDateToString(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String timeAsString = time.format(formatter);
        return timeAsString;
    }

    @Override
    public LocalDateTime convertStringToDate(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime date = LocalDateTime.parse(time, formatter);
        return date;
    }

    @Override
    public Memo addMemo(Memo m) {

        final String sql
                = "INSERT INTO Memo(Title, Bodytext, isApproved, DeleteDate, UploadDate) "
                + "VALUES(?,?,?,?,?)";

        jdbc.update(sql,
                m.getTitle(),
                m.getBodyText(),
                m.isApproved(),
                m.getDeleteDate(),
                m.getUploadDate()
        );

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        m.setMemoId(newId);
        associateTagsWithMemo(m, m.getTags());
        associateCreatorWithMemo(m, m.getUser());
        return m;

    }

    @Override
    public void associateTagsWithMemo(Memo m, List<Tag> tags) {
        String sql = "INSERT INTO MemoTag (MemoID, TagID) VALUES (?,?)";

        for (Tag t : tags) {
            tagDao.addTagToDB(t);
            jdbc.update(sql, m.getMemoId(), t.getTagId());
        }
    }

    @Override
    public List<Memo> getAllApproved() {
        String sql = "SELECT * FROM Memo WHERE isApproved = TRUE";
        return jdbc.query(sql, new MemoMapper());
    }

    @Override
    public Memo editMemo(Memo m) {

        String sql
                = "UPDATE Memo "
                + "SET Title = ?, BodyText = ?, isApproved = ?, UploadDate = ?, DeleteDate = ? "
                + "WHERE MemoID = ?";
        jdbc.update(sql,
                m.getTitle(),
                m.getBodyText(),
                m.isApproved(),
                m.getUploadDate(),
                m.getDeleteDate(),
                m.getMemoId()
        );

        sql = "UPDATE MemoUser SET UserID = ? WHERE MemoID = ?";
        jdbc.update(sql, m.getUser().getUserId(), m.getMemoId());
        
        sql = "DELETE FROM MemoTag WHERE MemoId = ?";
        jdbc.update(sql, m.getMemoId());
        associateTagsWithMemo(m, m.getTags());

        return m;

    }

    @Override
    public void associateCreatorWithMemo(Memo m, User creator) {
        String sql = "INSERT INTO MemoUser (MemoId, UserID) VALUES (?,?)";
        jdbc.update(sql, m.getMemoId(), creator.getUserId());
    }

    public class MemoMapper implements RowMapper<Memo> {

        @Override
        public Memo mapRow(ResultSet rs, int i) throws SQLException {

            LocalDateTime upDate = rs.getTimestamp("uploadDate").toLocalDateTime();
            LocalDateTime delDate = rs.getTimestamp("deleteDate").toLocalDateTime();

            Memo m = new Memo();
            m.setMemoId(rs.getInt("memoId"));
            m.setTitle(rs.getString("Title"));
            m.setBodyText(rs.getString("bodytext"));
            m.setApproved(rs.getBoolean("isApproved"));
            m.setDeleteDate(delDate);
            m.setUploadDate(upDate);
            m.setTags(tagDao.getTagsForPost(m));
            m.setUser(userDao.getCreatorForMemo(m));
            return m;
        }

    }
}
