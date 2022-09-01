package com.gcuedu.gcuforum.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcuedu.gcuforum.domain.LittlePlate;
import com.gcuedu.gcuforum.domain.Plate;
import com.gcuedu.gcuforum.mapper.PlateMapper;
import com.gcuedu.gcuforum.service.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlateServiceImpl extends ServiceImpl<PlateMapper, Plate> implements PlateService {

    @Autowired
    private PlateMapper mapper;

    @Override
    public List<LittlePlate> findLPById(Integer id) {
        return mapper.findLPById(id);
    }

    public Plate findPlateByLp(Integer lpId){
        return mapper.findPlateByLp(lpId);
    }
}
