package com.gcuedu.gcuforum.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcuedu.gcuforum.domain.Comment;
import com.gcuedu.gcuforum.mapper.CommentMapper;
import org.springframework.stereotype.Service;
import com.gcuedu.gcuforum.service.CommentService;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}
