package com.gcuedu.gcuforum.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gcuedu.gcuforum.domain.Floor;
import com.gcuedu.gcuforum.domain.Forum;
import com.gcuedu.gcuforum.service.FloorService;
import com.gcuedu.gcuforum.service.ForumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

@Api(tags = "包含了对贴子内部楼层操作的接口")
@RestController
@RequestMapping("/floor")
public class FloorController {

    @Autowired
    private FloorService floorService;
    @Autowired
    private ForumService forumService;

    @ApiOperation("对指定贴子添加回帖")
    @PostMapping("/postFloor")
    @Transactional
    @CrossOrigin
    public boolean postFloor(Floor floor){
        Forum forum = forumService.getById(floor.getFid());
        floor.setCreateTime(new java.sql.Date(System.currentTimeMillis()));
        floor.setFloorId(null);
        boolean success;
        LambdaUpdateWrapper<Forum> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Forum::getFid,floor.getFid());
        Integer floorNum = forum.getFloorNum();
        forum.setFloorNum(++floorNum);
        floor.setFloorNum(floorNum);
        success = floorService.save(floor);
        if (!success){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        success = forumService.update(forum,wrapper);
        if (!success){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @ApiOperation("删除楼")
    @PutMapping("/deleteFloor")
    @Transactional
    @CrossOrigin
    public boolean deleteFloor(Integer floorId){
        LambdaUpdateWrapper<Floor> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Floor::getIsShow,1);
        wrapper.eq(Floor::getFloorId,floorId);
        return floorService.update(wrapper);
    }

}
