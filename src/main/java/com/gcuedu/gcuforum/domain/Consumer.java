package com.gcuedu.gcuforum.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户实例
 * 包括编号，昵称，密码，三个密保问题及其答案，权限等级，手机号属性
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Consumer implements Serializable {

    private static final long serialVersionUID = 5697118347645202893L;
    @TableId(value="id",type = IdType.AUTO)
    private Long id;
    private String nickname;
    private String password;
    private int power;
    private Long phone;
    private Timestamp state;
}
