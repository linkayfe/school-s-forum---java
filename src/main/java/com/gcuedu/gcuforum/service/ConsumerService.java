package com.gcuedu.gcuforum.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcuedu.gcuforum.domain.Consumer;

public interface ConsumerService extends IService<Consumer>  {

    String selectNickname(Long id);
}
