package com.gcuedu.gcuforum.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcuedu.gcuforum.domain.Consumer;
import com.gcuedu.gcuforum.mapper.ConsumerMapper;
import com.gcuedu.gcuforum.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl extends ServiceImpl<ConsumerMapper, Consumer> implements ConsumerService {
    @Autowired
    private ConsumerMapper mapper;

    public String selectNickname(Long id){
        return mapper.selectNickname(id);
    }
}
