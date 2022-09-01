package com.gcuedu.gcuforum.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gcuedu.gcuforum.domain.*;
import com.gcuedu.gcuforum.service.*;
import com.gcuedu.gcuforum.util.RedisUtil;
import com.gcuedu.gcuforum.util.SerializeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.*;

@Api(tags="包含了对贴子操作的接口")
@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;
    @Autowired
    private PlateService plateService;
    @Autowired
    private FloorService floorService;
    @Autowired
    private LittlePlateService lpService;
    @Autowired
    private ConsumerService consumerService;



    @ApiOperation("查找所有贴")
    @GetMapping("/findAll")
    @Cacheable(cacheNames = "findAll")
    @CrossOrigin
    public List<SimplePost> findAll(){
        List<SimplePost> simplePosts = null;
        if (RedisUtil.exists("allForum",1)&& RedisUtil.ttl("allForum",1)>1000){
            byte[] allForum = RedisUtil.get("allForum",1);
            simplePosts = (List<SimplePost>) SerializeUtil.deSerialize(allForum);
            if (simplePosts!=null){
                return simplePosts;
            }
        }
        List<Forum> forumList = forumService.list();
        Map<Integer,String> map = new HashMap<>();
        List<LittlePlate> littlePlateList = lpService.list();
        Map<Integer,String> plateNameMap = new HashMap<>();
        simplePosts = putMapKeyAndValue(littlePlateList,forumList,map,plateNameMap,"");
        RedisUtil.setex("allForum",1,60*60, SerializeUtil.serialize(simplePosts));
        return simplePosts;
    }

    @ApiOperation("通过fid获取一篇贴")
    @GetMapping("/getForumByFid")
    @Cacheable(cacheNames = "getForumByFid")
    @CrossOrigin
    public Map<String,Object> getForumByFid(Integer fid){
        Forum forum = forumService.getById(fid);
        Map<String,Object> result = new HashMap<>();
        LittlePlate lp = lpService.getById(forum.getLpId());
        Plate plate = plateService.findPlateByLp(forum.getLpId());
        //获取floor+nickname的集合
        List<ShowFloor> floorList = floorService.getFloorsByFid(fid);
        result.put("forum",forum);
        result.put("lp",lp);
        result.put("plate",plate);
        result.put("floors",floorList);
        return result;
    }

    @ApiOperation("获取回帖前20的贴")
    @GetMapping("/getTopForum")
    @Cacheable(cacheNames = "getTopForum")
    @CrossOrigin
    public List<SimplePost> getTopForum(){
        List<SimplePost> simplePosts = null;
        if (RedisUtil.exists("top",1)
                && RedisUtil.ttl("top",1)>1000){
            byte[] topBytes = RedisUtil.get("top",1);
            simplePosts = (List<SimplePost>) SerializeUtil.deSerialize(topBytes);
            if (simplePosts!=null){
                return simplePosts;
            }
        }
        simplePosts = new ArrayList<>();
        List<Forum> forums = forumService.getTopForum();
        setSimplePosts(forums,simplePosts);
        RedisUtil.setex("top",1,60*60, SerializeUtil.serialize(simplePosts));
        return simplePosts;
    }

    private List<Forum> getForumsByIdOrLpId(Long id,Integer lpId){
        LambdaQueryWrapper<Forum> wrapper = new LambdaQueryWrapper<>();
        if (id != 0)wrapper.eq(Forum::getId,id);
        else if (lpId != 0)wrapper.eq(Forum::getLpId,lpId);
        return forumService.list(wrapper);
    }

    @ApiOperation("通过id获取所有贴")
    @GetMapping("/getForumsById")
    @Cacheable(cacheNames = "getForumsById")
    @CrossOrigin
    public List<SimplePost> getForumsById(Long id){
        List<SimplePost> simplePosts = null;
        if (RedisUtil.exists("consumer"+id,1) && RedisUtil.ttl("consumer"+id,1)>1000){
            byte[] consumer =RedisUtil.get("consumer"+id,1);
            simplePosts = (List<SimplePost>) SerializeUtil.deSerialize(consumer);
            if (simplePosts != null){
                return simplePosts;
            }
        }
        simplePosts = new ArrayList<>();
        setSimplePosts(this.getForumsByIdOrLpId(id,0),simplePosts);
        RedisUtil.setex("consumer"+id,1,60*60, SerializeUtil.serialize(simplePosts));
        return simplePosts;
    }

    @ApiOperation("随机获取5片贴")
    @GetMapping("/getForumRandom")
    @Cacheable(cacheNames = "getForumRandom")
    @CrossOrigin
    public List<Forum> getForumRandom(){
        List<Forum> forums = new ArrayList<>();
        Random random = new Random();
        LambdaQueryWrapper<Forum> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Forum::getShowForum,0);
        List<Forum> forumList = forumService.list(wrapper);
        if (forumList.size()>=5){
            int i = random.nextInt(forumList.size());
            while(forums.size()!=5){
                forums.add(forumList.get(i));
                i++;
                if (i==forumList.size()){
                    i = 0;
                }
            }
        }else {
            return forumList;
        }
        return forums;
    }

    @ApiOperation("通过小版块的编号lpId获取贴")
    @GetMapping("/getForumsByLpId")
    @Cacheable(cacheNames = "getForumsByPlId")
    @CrossOrigin
    public List<SimplePost> getForumsByPlId(Integer lpId){
        List<SimplePost> simplePosts = null;
        if (RedisUtil.exists("forumByLp"+lpId,1) && RedisUtil.ttl("forumByLp"+lpId,1)>1000){
            byte[] lps = RedisUtil.get("forumByLp"+lpId,1);
            simplePosts = (List<SimplePost>) SerializeUtil.deSerialize(lps);
            if (simplePosts != null){
                return simplePosts;
            }
        }
        simplePosts = new ArrayList<>();
        setSimplePosts(this.getForumsByIdOrLpId(0L,lpId),simplePosts);
        if (simplePosts.size()==0){
            LittlePlate lp = lpService.getById(lpId);
            Plate plate = plateService.findPlateByLp(lpId);
            SimplePost simplePost = new SimplePost();
            simplePost.setPlateName(plate.getPlateName());
            simplePost.setLpName(lp.getLpName());
            simplePost.setFid(-1L);
            simplePosts.add(simplePost);
        }
        RedisUtil.setex("forumByLp"+lpId,1,60*60, SerializeUtil.serialize(simplePosts));
        return simplePosts;
    }

    @ApiOperation("添加贴")
    @RequestMapping("/postForum")
    @CrossOrigin
    @Transactional
    public Long postForum(SimplePost simplePost){
        boolean success;
        Forum forum = new Forum();
        forum.setLpId(simplePost.getLpId());
        forum.setTitle(simplePost.getTitle());
        forum.setId(simplePost.getId());
        forum.setPublishTime(new Date(System.currentTimeMillis()));
        forum.setFloorNum(1);
        success = forumService.save(forum);
        if (!success){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1L;
        }
        Floor floor = new Floor();
        floor.setFid(forum.getFid());
        floor.setContent(simplePost.getContent());
        floor.setFloorNum(1);
        floor.setId(simplePost.getId());
        floor.setCreateTime(forum.getPublishTime());
        success = floorService.save(floor);
        if (!success){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1L;
        }
        return forum.getFid();
    }

    @ApiOperation("删除贴")
    @PutMapping("/deleteForum")
    @CrossOrigin
    @Transactional
    public boolean deleteForum(Long fid){
        LambdaUpdateWrapper<Forum> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Forum::getFid,fid);
        wrapper.set(Forum::getShowForum,1);
        return forumService.update(wrapper);
    }

    @ApiOperation("通过fid解封某贴")
    @PutMapping("/openClock")
    @CrossOrigin
    @Transactional
    public boolean openClock(Long fid){
        LambdaUpdateWrapper<Forum> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Forum::getFid,fid);
        wrapper.set(Forum::getShowForum,0);
        return forumService.update(wrapper);
    }

    @ApiOperation("通过id判断权限获取帖子信息")
    @GetMapping("/getForums")
    @CrossOrigin
    @Cacheable(cacheNames = "getForums")
    public List<SimplePost> getForums(Long id,Integer power){
        List<SimplePost> simplePosts = new ArrayList<>();
        Map<Integer,String> map = new HashMap<>();
        Map<Integer,String> plateNameMap = new HashMap<>();
        List<LittlePlate> littlePlateList = new ArrayList<>();
        if (power == 1){
            LambdaQueryWrapper<LittlePlate> lpWrapper = new LambdaQueryWrapper<>();
            lpWrapper.eq(LittlePlate::getId,id);
            LittlePlate lp = lpService.getOne(lpWrapper);
            littlePlateList.add(lp);

            Plate plate = plateService.findPlateByLp(lp.getLpId());

            LambdaQueryWrapper<Forum> forumWrapper = new LambdaQueryWrapper<>();
            forumWrapper.eq(Forum::getLpId,lp.getLpId());
            List<Forum> forumList = forumService.list(forumWrapper);

            simplePosts = putMapKeyAndValue(littlePlateList,forumList,map,plateNameMap,plate.getPlateName());
        }else if (power == 2){
            LambdaQueryWrapper<Plate> plateWrapper = new LambdaQueryWrapper<>();
            plateWrapper.eq(Plate::getId,id);
            Plate plate = plateService.getOne(plateWrapper);

            littlePlateList = lpService.getLpByPlateId(plate.getPlateId());

            List<Forum> forumList = forumService.getForumByLpId(littlePlateList);
            simplePosts = putMapKeyAndValue(littlePlateList,forumList,map,plateNameMap,plate.getPlateName());
        }
        return simplePosts;
    }

    private void setSimplePosts(List<Forum> forums,List<SimplePost> simplePosts){
        LittlePlate lp = null;
        Plate plate;
        for (Forum forum : forums) {
            LambdaQueryWrapper<Floor> floorWrapper = new LambdaQueryWrapper<>();
            floorWrapper.eq(Floor::getFid, forum.getFid());
            floorWrapper.eq(Floor::getFloorNum, 1);
            Floor floor = floorService.getOne(floorWrapper);
            String nickname = consumerService.selectNickname(forum.getId());
            if (lp == null || !forum.getLpId().equals(lp.getLpId())) {
                lp = lpService.getById(forum.getLpId());
            }
            plate = plateService.findPlateByLp(forum.getLpId());
            //截取一部分content 作为帖子简要展示,当content.length>90时，才需要截取
            String content = floor.getContent();
            int lg = content.indexOf("<img");
            int mg = content.indexOf("/img>");
            boolean cut = false;
            if (90 > lg && 90 < mg) {
                content = content.substring(0, mg + 5);
                cut = true;
            }
            if (!cut) {
                if (content.length()>90) {
                    content = content.substring(0, 90);
                    if (content.lastIndexOf(">") < content.lastIndexOf("<")) {
                        content = content.substring(0, content.lastIndexOf(">") + 1);
                    }
                }
            }
            SimplePost simplePost = new SimplePost();
            simplePost.setFid(forum.getId());
            simplePost.setLpId(lp.getLpId());
            simplePost.setFid(forum.getFid());
            simplePost.setPlateId(plate.getPlateId());
            simplePost.setId(forum.getId());
            simplePost.setContent(content + "...");
            simplePost.setFloorId(floor.getFloorId());
            simplePost.setPublishTime(forum.getPublishTime());
            simplePost.setTitle(forum.getTitle());
            simplePost.setNickname(nickname);
            simplePost.setPlateName(plate.getPlateName());
            simplePost.setLpName(lp.getLpName());
            simplePosts.add(simplePost);
        }
    }

    private List<SimplePost> putMapKeyAndValue(List<LittlePlate> littlePlateList,List<Forum> forumList,
                                               Map<Integer,String> lpNameMap,Map<Integer,String> plateNameMap,
                                               String plateName){
        List<SimplePost> simplePosts = new ArrayList<>();
        if ("".equals(plateName)) {
            for (LittlePlate littlePlate : littlePlateList) {
                Integer lpId = littlePlate.getLpId();
                lpNameMap.put(lpId, littlePlate.getLpName());
                plateNameMap.put(lpId, plateService.findPlateByLp(lpId).getPlateName());
            }
        }else {
            for (LittlePlate littlePlate : littlePlateList) {
                Integer lpId = littlePlate.getLpId();
                lpNameMap.put(lpId, littlePlate.getLpName());
                plateNameMap.put(lpId,plateName);
            }
        }
        for (Forum forum : forumList) {
            SimplePost simplePost = new SimplePost();
            simplePost.setFid(forum.getFid());
            simplePost.setId(forum.getId());
            simplePost.setNickname(consumerService.selectNickname(forum.getId()));
            simplePost.setTitle(forum.getTitle());
            simplePost.setPublishTime(forum.getPublishTime());
            simplePost.setLpName(lpNameMap.get(forum.getLpId()));
            simplePost.setFloorNum(forum.getFloorNum());
            simplePost.setPlateName(plateNameMap.get(forum.getLpId()));
            simplePost.setShowState(forum.getShowForum());
            simplePosts.add(simplePost);
        }
        return simplePosts;
    }
}
