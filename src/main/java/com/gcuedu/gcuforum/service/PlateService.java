package com.gcuedu.gcuforum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcuedu.gcuforum.domain.LittlePlate;
import com.gcuedu.gcuforum.domain.Plate;

import java.util.List;

public interface PlateService extends IService<Plate> {

    List<LittlePlate> findLPById(Integer id);
    Plate findPlateByLp(Integer lpId);

}
