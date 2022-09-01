package com.gcuedu.gcuforum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcuedu.gcuforum.domain.Floor;
import com.gcuedu.gcuforum.domain.ShowFloor;

import java.util.List;

public interface FloorService extends IService<Floor> {

    List<ShowFloor> getFloorsByFid(Integer fid);
}
