package com.gcuedu.gcuforum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcuedu.gcuforum.domain.LittlePlate;
import com.gcuedu.gcuforum.domain.SimplePost;

import java.util.List;

public interface LittlePlateService extends IService<LittlePlate> {

    List<SimplePost> selectLpAndPlate();

    boolean insertIntoPLp(Integer plateId,Integer lpId);

    List<LittlePlate> getLpByPlateId(Integer plateId);

}
