package com.gcuedu.gcuforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcuedu.gcuforum.domain.LittlePlate;
import com.gcuedu.gcuforum.domain.Plate;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlateMapper extends BaseMapper<Plate> {

    @Select("select lp_id,lp_name,id from little_plate where lp_id in (select lp_id from p_lp where plate_id=#{id})")
    List<LittlePlate> findLPById(Integer id);

    @Select("select plate_id,plate_name,id from plate where plate_id = (select plate_id from p_lp where lp_id=#{lpId})")
    Plate findPlateByLp(Integer lpId);
}