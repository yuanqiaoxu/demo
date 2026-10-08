package com.study.demo.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.study.demo.entity.Admin;
import com.study.demo.mapper.AdminMapper;
import com.study.demo.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper,Admin> implements AdminService {
}
