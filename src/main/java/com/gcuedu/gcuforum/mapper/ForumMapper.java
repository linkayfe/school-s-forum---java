package com.gcuedu.gcuforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcuedu.gcuforum.domain.Forum;
import com.gcuedu.gcuforum.domain.LittlePlate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumMapper extends BaseMapper<Forum> {

    @Select("select fid,lp_id,title,id,publish_time,floor_num from forum where show_forum=0 order by floor_num desc limit 0,20")
    List<Forum> getTopForum();

    @Select("select fid from forum")
    List<Integer> getForumRandom();

    @Select("<script>" +
            "select fid,title,id,floor_num from forum where fid in " +
            "<foreach collection='fids' item='fid' open='(' separator=',' close=')'>" +
            "#{fid}</foreach> and show_forum = 0" +
            "</script>")
    List<Forum> getForumByFids(@Param("fids")List<Integer> fids);

    @Select("<script>" +
            "select fid,title,id,floor_num,publish_time,show_forum,lp_id from forum where lp_id in " +
            "<foreach collection='lps' item='lp' open='(' separator=',' close=')'>" +
            "#{lp.lpId}</foreach>" +
            "</script>")
    List<Forum> getForumByLpId(@Param("lps")List<LittlePlate> lps);
}
