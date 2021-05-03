/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.altf4.Blog.dto;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 *
 * @author dnsfu
 */

@Data
public class Memo {

    private int memoId;

    @NotBlank(message = "Title should not be blank.")
    private String title;

    @NotBlank(message = "Body should not be blank")
    private String bodyText;

    @Column(nullable = false)
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime uploadDate;

    @Column
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private LocalDateTime deleteDate;

    @Column(nullable = false)
    private boolean isApproved;

    private User user;

    private List<Tag> tags;

}
