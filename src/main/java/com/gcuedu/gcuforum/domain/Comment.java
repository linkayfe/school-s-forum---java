package com.gcuedu.gcuforum.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 评论实例
 * 包括 评论编号，用户编号，贴子编号，楼层的编号
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    private static final long serialVersionUID = 5864417644091032258L;
    @TableId(value="cid",type = IdType.AUTO)
    private Long cid;
    private Long fid;
    private Long floorId;
    private Long id;
    private String content;
}
