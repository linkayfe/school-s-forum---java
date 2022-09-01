package com.gcuedu.gcuforum.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

/**
 *  贴子实例
 *  包括贴子编号，用户编号，板块编号，标题内容，发表时间
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Forum implements Serializable {
    private static final long serialVersionUID = -4625713188619685520L;

    @TableId(value="fid",type = IdType.AUTO)
    private Long fid;
    private Long id;
    private Integer lpId;
    private String title;
    private Date publishTime;
    private Integer floorNum;
    private Integer showForum;
}
