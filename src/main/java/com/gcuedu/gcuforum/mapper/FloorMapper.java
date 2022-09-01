package com.gcuedu.gcuforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcuedu.gcuforum.domain.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FloorMapper extends BaseMapper<Floor> {

    @Select("select floor_id,floor_num,f.id,c.nickname,content,create_time,picture,is_show " +
            "from floor f left join consumer c on f.id=c.id where fid=#{fid} order by floor_num asc")
    List<ShowFloor> getFloorsByFid(Integer fid);
}
