package com.gcuedu.gcuforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcuedu.gcuforum.domain.LittlePlate;
import com.gcuedu.gcuforum.domain.SimplePost;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LittlePlateMapper extends BaseMapper<LittlePlate> {

    @Select("select lp_id,plate_id from p_lp")
    List<SimplePost> selectLpAndPlate();

    @Insert("insert into p_lp values(#{plateId},#{lpId})")
    Boolean insertIntoPLp(Integer plateId,Integer lpId);

    @Select("select lp_id from p_lp where plate_id=#{plateId}")
    List<Integer> getLpIdByPlateId(Integer plateId);

    @Select("<script>" +
            "select lp_id,lp_name,id,show_lp from little_plate where lp_id in " +
            "<foreach collection='lpIds' item='lp_id' open='(' separator=',' close=')'>" +
            "#{lp_id}</foreach>" +
            "</script>")
    List<LittlePlate> getLpsByLpIds(@Param("lpIds") List<Integer> lpIds);
}
