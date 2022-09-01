package com.gcuedu.gcuforum.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

/**
 * 此对象用于在页面展示简要帖子时使用，将展示所需数据从数据库读取注入该对象，传至页面展示
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SimplePost implements Serializable {
    private static final long serialVersionUID = 8703974984443207931L;

    private Integer plateId;
    private Integer lpId;
    private Long fid;
    private Long floorId;
    private Long id;
    private String nickname;
    private String plateName;
    private String lpName;
    private String title;
    private Date publishTime;
    private String content;
    private Integer floorNum;
    private String state;
    private Long phone;
    private Integer power;
    private Integer showState;
}
