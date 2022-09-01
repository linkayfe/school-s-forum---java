package com.gcuedu.gcuforum.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

/**
 * 该类包含展示该楼层所需数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShowFloor implements Serializable {

    private static final long serialVersionUID = 2229827459934780592L;
    private Long floorId;
    private Long fid;
    private Long id;
    private String nickname;
    private String content;
    private Date createTime;
    private int floorNum;
    private String picture;
    private Integer isShow;
    private String yl_1;
    private String yl_2;
}
