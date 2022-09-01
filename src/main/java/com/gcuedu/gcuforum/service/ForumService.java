package com.gcuedu.gcuforum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcuedu.gcuforum.domain.Forum;
import com.gcuedu.gcuforum.domain.LittlePlate;

import java.util.List;

public interface ForumService extends IService<Forum> {

    List<Forum> getTopForum();
    List<Integer> getForumRandom();
    List<Forum> getForumByFids(List<Integer> fids);
    List<Forum> getForumByLpId(List<LittlePlate> lps);
}
