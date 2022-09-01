package com.gcuedu.gcuforum.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcuedu.gcuforum.domain.Floor;
import com.gcuedu.gcuforum.domain.ShowFloor;
import com.gcuedu.gcuforum.mapper.FloorMapper;
import com.gcuedu.gcuforum.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements FloorService {

    @Autowired
    private FloorMapper mapper;

    public List<ShowFloor> getFloorsByFid(Integer fid){
        return mapper.getFloorsByFid(fid);
    }
}
