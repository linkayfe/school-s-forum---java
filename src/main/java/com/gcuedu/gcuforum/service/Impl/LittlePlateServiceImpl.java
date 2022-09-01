package com.gcuedu.gcuforum.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcuedu.gcuforum.domain.LittlePlate;
import com.gcuedu.gcuforum.domain.SimplePost;
import com.gcuedu.gcuforum.mapper.LittlePlateMapper;
import com.gcuedu.gcuforum.service.LittlePlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LittlePlateServiceImpl extends ServiceImpl<LittlePlateMapper, LittlePlate> implements LittlePlateService {

    @Autowired
    private LittlePlateMapper mapper;

    public List<SimplePost> selectLpAndPlate(){
        return mapper.selectLpAndPlate();
    }

    @Override
    public boolean insertIntoPLp(Integer plateId, Integer lpId) {
        return mapper.insertIntoPLp(plateId, lpId);
    }

    public List<LittlePlate> getLpByPlateId(Integer plateId){
        List<Integer> lpIds = mapper.getLpIdByPlateId(plateId);
        return mapper.getLpsByLpIds(lpIds);
    }

}
