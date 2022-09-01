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
 *  楼层实例
 *  包括 楼层编号，贴子编号，用户编号，楼层内容，发表时间，楼层数，图片和视频的位置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Floor implements Serializable {
    private static final long serialVersionUID = -2306436383700793691L;
    @TableId(value="floor_id",type = IdType.AUTO)
    private Long floorId;
    private Long fid;
    private Long id;
    private String content;
    private Date createTime;
    private int floorNum;
    private String picture;
    private Integer isShow;
}
