package com.gcuedu.gcuforum.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gcuedu.gcuforum.domain.*;
import com.gcuedu.gcuforum.service.ConsumerService;
import com.gcuedu.gcuforum.service.LittlePlateService;
import com.gcuedu.gcuforum.util.RedisUtil;
import com.gcuedu.gcuforum.util.SerializeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.gcuedu.gcuforum.service.PlateService;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "包含了对大小版块操作的接口")
@RestController
@RequestMapping("/plates")
public class PlateController {

    @Autowired
    private PlateService service;
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private LittlePlateService lpService;


    @ApiOperation("获取所有大版块")
    @GetMapping("/findAll")
    @CrossOrigin
    public List<SimplePost> findAll(boolean flag){
        if (RedisUtil.exists("allPlate",1) && RedisUtil.ttl("allPlate",1)>1000){
            byte[] allPlate = RedisUtil.get("allPlate",1);
            List<SimplePost> simplePosts = (List<SimplePost>) SerializeUtil.deSerialize(allPlate);
            if (simplePosts!=null){
                return simplePosts;
            }
        }
        LambdaQueryWrapper<Plate> plateWrapper = new LambdaQueryWrapper<>();
        if (flag) {
            plateWrapper.eq(Plate::getShowPlate, 0);
        }
        List<SimplePost> simplePosts = new ArrayList<>();
        List<Plate> plateList = service.list(plateWrapper);
        Map<Long,String> map = getNicknameMapById();
        for (Plate plate : plateList) {
            SimplePost simplePost = new SimplePost();
            simplePost.setId(plate.getId());
            simplePost.setPlateId(plate.getPlateId());
            simplePost.setPlateName(plate.getPlateName());
            simplePost.setNickname(map.get(plate.getId()));
            simplePost.setShowState(plate.getShowPlate());
            simplePosts.add(simplePost);
        }
        RedisUtil.setex("allPlate",1,24*60*60,SerializeUtil.serialize(simplePosts));
        return simplePosts;
    }

    private Map<Long,String> getNicknameMapById(){
        LambdaQueryWrapper<Consumer> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Consumer::getId,Consumer::getNickname);
        List<Consumer> consumerList = consumerService.list(wrapper);
        Map<Long,String> map = new HashMap<>();
        for (Consumer consumer : consumerList) {
            map.put(consumer.getId(),consumer.getNickname());
        }
        return map;
    }

    @ApiOperation("获取所有小版块")
    @GetMapping("/getAllLp")
    @Cacheable(cacheNames = "getAllLp")
    @CrossOrigin
    public List<SimplePost> getAllLp() {
        if (RedisUtil.exists("allLp",1) && RedisUtil.ttl("allLp",1)>1000){
            byte[] allPlate = RedisUtil.get("allLp",1);
            List<SimplePost> simplePosts = (List<SimplePost>) SerializeUtil.deSerialize(allPlate);
            if (simplePosts!=null){
                return simplePosts;
            }
        }
        List<Plate> plateList = service.list();
        Map<Integer, String> plateIdAndName = new HashMap<>();
        for (Plate plate : plateList) {
            plateIdAndName.put(plate.getPlateId(), plate.getPlateName());
        }

        List<SimplePost> pAndLp = lpService.selectLpAndPlate();
        Map<Integer, Integer> lpAndPlate = new HashMap<>();
        for (SimplePost simplePost : pAndLp) {
            lpAndPlate.put(simplePost.getLpId(), simplePost.getPlateId());
        }
        List<LittlePlate> lpList = lpService.list();
        Map<Long, String> nicknameMap = getNicknameMapById();
        Integer plateId;
        List<SimplePost> allLp = new ArrayList<>();
        for (LittlePlate littlePlate : lpList) {
            SimplePost simplePost = new SimplePost();
            simplePost.setId(littlePlate.getId());
            simplePost.setNickname(nicknameMap.get(littlePlate.getId()));
            simplePost.setLpId(littlePlate.getLpId());
            simplePost.setLpName(littlePlate.getLpName());
            plateId = lpAndPlate.get(littlePlate.getLpId());
            simplePost.setPlateId(plateId);
            simplePost.setPlateName(plateIdAndName.get(plateId));
            simplePost.setShowState(littlePlate.getShowLp());
            allLp.add(simplePost);
        }
        RedisUtil.setex("allLp",1,24*60*60,SerializeUtil.serialize(allLp));
        return allLp;
    }

    @ApiOperation("通过大版块编号plateId获取小版块")
    @GetMapping("/getLittle")
    @Cacheable(cacheNames = "getLittle")
    @CrossOrigin
    public Map<String,List<SimplePost>> getLittle(Integer plateId){
        if (RedisUtil.exists("plate"+plateId,1) && RedisUtil.ttl("plate"+plateId,1)>1000){
            byte[] littlePlate = RedisUtil.get("plate"+plateId,1);
            Map<String,List<SimplePost>> little = (Map<String, List<SimplePost>>) SerializeUtil.deSerialize(littlePlate);
            if (little!=null){
                return little;
            }
        }
        Map<String,List<SimplePost>> result = new HashMap<>();
        List<LittlePlate> lps = service.findLPById(plateId);

        LambdaQueryWrapper<Plate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Plate::getPlateId,plateId);
        wrapper.eq(Plate::getShowPlate,0);
        Plate plate = service.getById(plateId);

        List<SimplePost> simplePosts = new ArrayList<>();
        Map<Long,String> nicknameMap = getNicknameMapById();
        for (LittlePlate littlePlate : lps) {
            SimplePost simplePost = new SimplePost();
            simplePost.setId(littlePlate.getId());
            simplePost.setNickname(nicknameMap.get(littlePlate.getId()));
            simplePost.setLpId(littlePlate.getLpId());
            simplePost.setLpName(littlePlate.getLpName());
            simplePost.setPlateId(plateId);
            simplePost.setPlateName(plate.getPlateName());
            simplePost.setShowState(littlePlate.getShowLp());
            simplePosts.add(simplePost);
        }
        result.put("little",simplePosts);
        RedisUtil.setex("plate"+plateId,1,24*60*60,SerializeUtil.serialize(result));
        return result;
    }

    @ApiOperation("删除小版块")
    @PutMapping("/deleteLp")
    @CrossOrigin
    @Transactional
    public boolean deleteLp(Integer lpId){
        LambdaUpdateWrapper<LittlePlate> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LittlePlate::getLpId,lpId);
        wrapper.set(LittlePlate::getShowLp,1);
        delKey("lp"+lpId);
        return lpService.update(wrapper);
    }

    @ApiOperation("删除大版块")
    @PutMapping("/deletePlate")
    @CrossOrigin
    @Transactional
    public boolean deletePlate(Integer plateId){
        LambdaUpdateWrapper<Plate> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Plate::getPlateId,plateId);
        wrapper.set(Plate::getShowPlate,1);
        delKey("plate"+plateId);
        return service.update(wrapper);
    }

    @ApiOperation("通过参数判断修改大版块信息或小版块信息")
    @PutMapping("/updatePlateOrLp")
    @CrossOrigin
    @Transactional
    public boolean updatePlateOrLp(Long theOldId,SimplePost simplePost,Boolean isLp){
        //通过lpId判断修改的是Plate还是LittlePlate
        boolean success;
        if (!isLp){
            LambdaUpdateWrapper<Plate> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Plate::getPlateId,simplePost.getPlateId());
            wrapper.set(Plate::getPlateName,simplePost.getPlateName());
            wrapper.set(Plate::getId,simplePost.getId());
            success = service.update(wrapper);
        }else {
            LambdaUpdateWrapper<LittlePlate> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(LittlePlate::getLpId,simplePost.getLpId());
            wrapper.set(LittlePlate::getLpName,simplePost.getLpName());
            wrapper.set(LittlePlate::getId,simplePost.getId());
            success = lpService.update(wrapper);
        }
        LambdaQueryWrapper<Plate> plateWrapper = new LambdaQueryWrapper<>();
        plateWrapper.eq(Plate::getId,theOldId);
//        Plate plate = service.getOne(plateWrapper);
        List<Plate> plates = service.list(plateWrapper);
//            查看旧版主的权限
//            分为是小版主、是另外一个大版块的版主两种情况
        LambdaUpdateWrapper<Consumer> consumerWrapper = new LambdaUpdateWrapper<>();
        consumerWrapper.eq(Consumer::getId,theOldId);
        if (plates.size() == 0) {
            LambdaQueryWrapper<LittlePlate> lpWrapper = new LambdaQueryWrapper<>();
            lpWrapper.eq(LittlePlate::getId, theOldId);
//            LittlePlate lp = lpService.getOne(lpWrapper);
            List<LittlePlate> lps = lpService.list(lpWrapper);
            if (lps.size() == 0) {
                consumerWrapper.set(Consumer::getPower,0);
            }else {
                consumerWrapper.set(Consumer::getPower,1);
            }
            consumerService.update(consumerWrapper);
        }
        delKey("");
        return success;
    }

    @ApiOperation("通过参数判断新增大版块或小版块")
    @GetMapping("/postPlateOrLp")
    @CrossOrigin
    @Transactional
    public boolean postPlateOrLp(SimplePost simplePost,boolean isPostLp){
        boolean success;
        Consumer consumer = consumerService.getById(simplePost.getId());
        int power = consumer.getPower();
        if (isPostLp){
            LittlePlate lp = new LittlePlate();
            lp.setId(simplePost.getId());
            lp.setLpName(simplePost.getPlateName());
            lp.setShowLp(0);
            success = lpService.save(lp);
            if (success) {
                success = lpService.insertIntoPLp(simplePost.getPlateId(), lp.getLpId());
                if (power==0) {
                    LambdaUpdateWrapper<Consumer> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.eq(Consumer::getId, simplePost.getId());
                    wrapper.set(Consumer::getPower, 1);
                    consumerService.update(wrapper);
                }
            }
        }else {
            Plate plate = new Plate();
            plate.setPlateName(simplePost.getPlateName());
            plate.setId(simplePost.getId());
            plate.setShowPlate(0);
            success = service.save(plate);
            if (power<2) {
                LambdaUpdateWrapper<Consumer> wrapper = new LambdaUpdateWrapper<>();
                wrapper.eq(Consumer::getId, simplePost.getId());
                wrapper.set(Consumer::getPower, 2);
                consumerService.update(wrapper);
            }
        }
        delKey("");
        return success;
    }

    @ApiOperation("通过参数判断检查大版块名或小版块名是否存在")
    @GetMapping("/checkPlateOrLpName")
    @CrossOrigin
    @Transactional
    public boolean checkPlateOrLpName(String name,boolean isLp){
        if (isLp){
            LambdaQueryWrapper<LittlePlate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LittlePlate::getLpName,name);
            wrapper.eq(LittlePlate::getShowLp,0);
            LittlePlate littlePlate= lpService.getOne(wrapper);
            return littlePlate == null;
        }else {
            LambdaQueryWrapper<Plate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Plate::getPlateName,name);
            wrapper.eq(Plate::getShowPlate,0);
            Plate plate= service.getOne(wrapper);
            return plate == null;
        }
    }

    @ApiOperation("通过用户id获取大版块")
    @GetMapping("/getPlateById")
    @CrossOrigin
    @Cacheable(cacheNames = "getPlateById")
    public String getPlateById(Long id){
        LambdaQueryWrapper<Plate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Plate::getId,id);
//        wrapper.select(Plate::getPlateName);
        Plate plate = service.getOne(wrapper);
        if(plate!=null){
            return plate.getPlateName();
        }
        return "null";
    }

    @ApiOperation("通过用户id获取小版块")
    @GetMapping("/getLpById")
    @CrossOrigin
    @Cacheable(cacheNames = "getLpById")
    public String getLpById(Long id){
        LambdaQueryWrapper<LittlePlate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LittlePlate::getId,id);
//        wrapper.select(LittlePlate::getLpName);
        LittlePlate littlePlate = lpService.getOne(wrapper);
        if (littlePlate!=null){
            return littlePlate.getLpName();
        }
        return "null";
    }

    @ApiOperation("通过参数判断恢复不展示的大版块或小版块")
    @PutMapping("/recoverPOrLp")
    @CrossOrigin
    @Transactional
    public boolean recoverPOrLp(Integer theId,boolean flag){
        if (flag){
            LambdaUpdateWrapper<LittlePlate> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(LittlePlate::getLpId,theId);
            wrapper.set(LittlePlate::getShowLp,0);
            return lpService.update(wrapper);
        }else {
            LambdaUpdateWrapper<Plate> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Plate::getPlateId,theId);
            wrapper.set(Plate::getShowPlate,0);
            return service.update(wrapper);
        }
    }

    @ApiOperation("修改大版块的版主")
    @PutMapping("/updatePlateControl")
    @CrossOrigin
    @Transactional
    public boolean updatePlateControl(Long id,Integer power,Integer plateId){
        LambdaUpdateWrapper<Plate> plateWrapper = new LambdaUpdateWrapper<>();
        plateWrapper.eq(Plate::getPlateId,plateId);
        plateWrapper.set(Plate::getId,id);
        boolean success = service.update(plateWrapper);
        if (!success){
            return false;
        }
        success = this.setConsumerPower(id,power);
        delKey("plate"+plateId);
        return success;
    }

    @ApiOperation("修改小版块的版主")
    @PutMapping("/updateLpControl")
    @Transactional
    @CrossOrigin
    public boolean updateLpControl(Long id,Integer power,Integer lpId){
        LambdaUpdateWrapper<LittlePlate> littleWrapper = new LambdaUpdateWrapper<>();
        littleWrapper.eq(LittlePlate::getLpId,lpId);
        littleWrapper.set(LittlePlate::getId,id);
        boolean success = lpService.update(littleWrapper);
        if (!success){
            return false;
        }
        success = this.setConsumerPower(id,power);
        delKey("lp"+lpId);
        return success;
    }

    @ApiIgnore
    @RequestMapping("/updatePower")
    @Transactional
    @CrossOrigin
    public boolean updatePower(Long id,Integer power){
        return this.setConsumerPower(id,power);
    }

    @ApiOperation("通过用户id获取小版块")
    @GetMapping("/getLpsById")
    @Cacheable(cacheNames = "getLpsById")
    @CrossOrigin
    public List<SimplePost> getLpsById(Long id){
        LambdaQueryWrapper<Plate> plateWrapper = new LambdaQueryWrapper<>();
        plateWrapper.eq(Plate::getId,id);
        Plate plate = service.getOne(plateWrapper);
        List<LittlePlate> lpList = lpService.getLpByPlateId(plate.getPlateId());
        Map<Long, String> nicknameMap = getNicknameMapById();
        List<SimplePost> allLp = new ArrayList<>();
        for (LittlePlate littlePlate : lpList) {
            SimplePost simplePost = new SimplePost();
            simplePost.setId(littlePlate.getId());
            simplePost.setNickname(nicknameMap.get(littlePlate.getId()));
            simplePost.setLpId(littlePlate.getLpId());
            simplePost.setLpName(littlePlate.getLpName());
            simplePost.setPlateId(plate.getPlateId());
            simplePost.setPlateName(plate.getPlateName());
            simplePost.setShowState(littlePlate.getShowLp());
            allLp.add(simplePost);
        }
        return allLp;
    }

    private boolean setConsumerPower(Long id,Integer power){
        LambdaUpdateWrapper<Consumer> consumerWrapper = new LambdaUpdateWrapper<>();
        consumerWrapper.eq(Consumer::getId,id);
        consumerWrapper.set(Consumer::getPower,power);
        return consumerService.update(consumerWrapper);
    }

    private void delKey(String key){
        RedisUtil.delKey("allPlate",1);
        RedisUtil.delKey("allLp",1);
        if (!"".equals(key)){
            RedisUtil.delKey(key,1);
        }
    }

}

