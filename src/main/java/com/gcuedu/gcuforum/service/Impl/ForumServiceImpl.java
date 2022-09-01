package com.gcuedu.gcuforum.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcuedu.gcuforum.domain.Forum;
import com.gcuedu.gcuforum.domain.LittlePlate;
import com.gcuedu.gcuforum.mapper.ForumMapper;
import com.gcuedu.gcuforum.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Forum> implements ForumService {

    @Autowired
    private ForumMapper mapper;

    public List<Forum> getTopForum(){
        return mapper.getTopForum();
    }

    public List<Integer> getForumRandom(){
        return mapper.getForumRandom();
    }

    public List<Forum> getForumByFids(List<Integer> fids){
        return mapper.getForumByFids(fids);
    }

    public List<Forum> getForumByLpId(List<LittlePlate> lps){
        return mapper.getForumByLpId(lps);
    }

}
