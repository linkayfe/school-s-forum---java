package com.gcuedu.gcuforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcuedu.gcuforum.domain.Consumer;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerMapper extends BaseMapper<Consumer> {

    @Select("select nickname from consumer where id=#{id}")
    String selectNickname(Long id);
}
