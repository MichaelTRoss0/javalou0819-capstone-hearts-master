/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.altf4.Blog.dao;

import com.altf4.Blog.dto.Memo;
import com.altf4.Blog.dto.Tag;
import java.util.List;

/**
 *
 * @author dnsfu
 */
public interface TagDao {
    public Tag get(int id);
    public List<Tag> getAll();
    public List<Tag> getTagsForPost(Memo m);
    public Tag addTagToDB(Tag t);
    public void deleteTagById(int id);
    public Tag editTag(Tag t);
    public List<Tag> generateTagsFromString(String tags);
}
