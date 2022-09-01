package com.gcuedu.gcuforum.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LittlePlate implements Serializable {
    private static final long serialVersionUID = -1157361341744684920L;

    @TableId(value="lp_id",type = IdType.AUTO)
    private Integer lpId;
    private String lpName;
    private Long id;
    private Integer showLp;
}
