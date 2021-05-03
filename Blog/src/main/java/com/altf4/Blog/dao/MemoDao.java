package com.altf4.Blog.dao;

import com.altf4.Blog.dto.User;
import com.altf4.Blog.dto.Memo;
import com.altf4.Blog.dto.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author dnsfu
 */
public interface MemoDao {
    public Memo get(int id);
    public List<Memo> getAll();
    public void delete(int id);
    public List<Memo> getAllForDate(LocalDateTime time);
    public List<Memo> getAllNotApproved();
    public List<Memo> getAllApproved();
    public Memo addMemo(Memo m);
    public Memo editMemo(Memo m);
    public void associateTagsWithMemo(Memo m, List<Tag> tags);
    public void associateCreatorWithMemo(Memo m, User creator);
    public String convertDateToString(LocalDateTime time);
    public LocalDateTime convertStringToDate(String time);
}
