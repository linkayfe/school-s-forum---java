package com.gcuedu.gcuforum.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 *  版块实例
 *  包括版块编号，版块名，板块里的分区
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Plate implements Serializable {
    private static final long serialVersionUID = -538616780562338669L;

    @TableId(value="plate_id",type = IdType.AUTO)
    private Integer plateId;
    private String plateName;
    private Long id;
    private Integer showPlate;
}
