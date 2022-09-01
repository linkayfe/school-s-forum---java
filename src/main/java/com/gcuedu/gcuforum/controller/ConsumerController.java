package com.gcuedu.gcuforum.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gcuedu.gcuforum.domain.Consumer;
import com.gcuedu.gcuforum.domain.SimplePost;
import com.gcuedu.gcuforum.service.ConsumerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/consumer")
@Api(tags = "包含了关于用户的所有操作接口")
public class ConsumerController {

    @Autowired
    private ConsumerService service;

    @ApiOperation("登录")
    @RequestMapping("/login")
    @Cacheable(cacheNames = "login")
    @CrossOrigin
    public Map<String,Object> login(String id, String password){
        Map<String,Object> map = new HashMap<>();
        try{
            Long username = Long.parseLong(id);
            LambdaQueryWrapper<Consumer> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Consumer::getId,username);
            wrapper.eq(Consumer::getPassword,password);
            Consumer user = service.getOne(wrapper);
            if (user==null){
                map.put("success","false");
            }else {
                map.put("success","true");
                map.put("user",user);
            }
            return map;
        }catch (Exception e){
            map.put("success","false");
            return map;
        }
    }

    @ApiIgnore
    @RequestMapping("/logout")
    @CrossOrigin
    public void logout(HttpSession session){
        session.removeAttribute("userLogin");
        session.invalidate();
    }

    @ApiOperation("用户修改个人信息")
    @PutMapping("/updateConsumerInfo")
    @Transactional
    @CrossOrigin
    public boolean updateConsumerInfo(Long id,String nickname){
        LambdaUpdateWrapper<Consumer> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Consumer::getId,id);
        wrapper.set(Consumer::getNickname,nickname);
        return service.update(wrapper);
    }

    @ApiOperation("通过一个id获取一个Consumer对象")
    @GetMapping("/getOneById")
    @CrossOrigin
    public Consumer getOneById(Long id){
        LambdaQueryWrapper<Consumer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Consumer::getId,id);
        wrapper.select(Consumer::getId,Consumer::getPhone,Consumer::getNickname,Consumer::getPower,Consumer::getState);
        return service.getOne(wrapper);
    }

    @ApiOperation("通过旧密码修改新密码")
    @PutMapping("/updatePasswordByOld")
    @Transactional
    @CrossOrigin
    public boolean updatePasswordByOld(@RequestBody Consumer consumer){
        return service.updateById(consumer);
    }

    @ApiIgnore
    @GetMapping("/getState")
    @Cacheable(cacheNames = "getState")
    @CrossOrigin
    public Map<String,Long> getState(HttpSession session){
        Map<String,Long> map = new HashMap<>();
        Consumer consumer = (Consumer) session.getAttribute("userLogin");
        if (consumer != null){
            Long id = consumer.getId();
            map.put("state",id);
        }else {
            map.put("state",-1L);
        }
        return map;
    }

    @ApiOperation("获取所有的用户信息，返回一个List<SimplePost>")
    @GetMapping("/findAll")
    @Cacheable(cacheNames = "findAllConsumer")
    @CrossOrigin
    public List<SimplePost> findAllConsumer(){
        LambdaQueryWrapper<Consumer> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Consumer::getId,Consumer::getNickname,Consumer::getPhone,Consumer::getPower,Consumer::getState);
        List<Consumer> consumers = service.list(wrapper);
        List<SimplePost> simplePosts = new ArrayList<>();
        for (Consumer consumer : consumers) {
            SimplePost simplePost = new SimplePost();
            simplePost.setId(consumer.getId());
            simplePost.setNickname(consumer.getNickname());
            simplePost.setPhone(consumer.getPhone());
            simplePost.setPower(consumer.getPower());
            Timestamp timestamp = consumer.getState();
            if (timestamp.getTime()>System.currentTimeMillis()){
                simplePost.setState("已被封号至"+timestamp);
                simplePost.setShowState(1);
            }else {
                simplePost.setState("正常");
                simplePost.setShowState(0);
            }
            simplePosts.add(simplePost);
        }
        return simplePosts;
    }

    @ApiOperation("通过id获取昵称")
    @GetMapping("/getNicknameById")
    @Cacheable(cacheNames = "getNicknameById")
    @CrossOrigin
    public String getNicknameById(Long id){
        String nickname =  service.selectNickname(id);
        if (nickname != null){
            return nickname;
        }else {
            return "nobody";
        }
    }

    @ApiOperation("封号")
    @PutMapping("/clockOne")
    @CrossOrigin
    @Transactional
    public boolean clockOne(Long id,Long state){
        boolean success;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()+state);
        LambdaUpdateWrapper<Consumer> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Consumer::getId,id);
        wrapper.set(Consumer::getState,timestamp);
        wrapper.set(Consumer::getPower,0);
        success = service.update(wrapper);
        return success;
    }

    @ApiOperation("解封")
    @PutMapping("/unlocking")
    @CrossOrigin
    @Transactional
    public boolean unlocking(Long id){
        LambdaUpdateWrapper<Consumer> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Consumer::getId,id);
        wrapper.set(Consumer::getState,new Timestamp(System.currentTimeMillis()-10000L));
        return service.update(wrapper);
    }

    @ApiOperation("修改用户的权限")
    @PutMapping("/updatePower")
    @Transactional
    @CrossOrigin
    public boolean updatePower(Long id,Integer power){
        LambdaUpdateWrapper<Consumer> consumerWrapper = new LambdaUpdateWrapper<>();
        consumerWrapper.eq(Consumer::getId,id);
        consumerWrapper.set(Consumer::getPower,power);
        return service.update(consumerWrapper);
    }

    @ApiOperation("查询不是超级管理员的用户")
    @GetMapping("/allIdAndNickname")
    @CrossOrigin
    @Cacheable
    public List<Consumer> allIdAndNickname(){
        LambdaQueryWrapper<Consumer> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(Consumer::getPower,3);
        wrapper.select(Consumer::getId,Consumer::getNickname);
        return service.list(wrapper);
    }

}
