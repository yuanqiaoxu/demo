package com.study.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.study.demo.entity.Admin;
import com.study.demo.response.Response;
import com.study.demo.service.AdminService;
import com.study.demo.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Tag(name = "登录")
@RestController
public class LoginController {

    private final AdminService adminService;
    private final StringRedisTemplate redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final int MAX_RETRY = 5;
    private static final long LOCK_MINUTES = 10;

    public LoginController(AdminService adminService, StringRedisTemplate redisTemplate) {
        this.adminService = adminService;
        this.redisTemplate = redisTemplate;
    }

    @Operation(summary = "登录")
    @PostMapping("/admin/login")
    public Response login(@RequestBody Admin admin) {
        String username = admin.getUsername();
        String errorKey = "login:error:" + username;

        String countStr = redisTemplate.opsForValue().get(errorKey);
        int count = countStr == null ? 0 : Integer.parseInt(countStr);
        if (count >= MAX_RETRY) {
            Long ttl = redisTemplate.getExpire(errorKey, TimeUnit.MINUTES);
            return Response.ERROR("密码错误次数过多，请" + ttl + "分钟后再试");
        }

        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        Admin dbAdmin = adminService.getOne(wrapper);

        if (dbAdmin == null) {
            return Response.ERROR("用户名或密码错误");
        }

        if (!passwordEncoder.matches(admin.getUserpwd(), dbAdmin.getUserpwd())) {
            redisTemplate.opsForValue().increment(errorKey);
            redisTemplate.expire(errorKey, LOCK_MINUTES, TimeUnit.MINUTES);
            return Response.ERROR("用户名或密码错误，还能试" + (MAX_RETRY - count - 1) + "次");
        }

        redisTemplate.delete(errorKey);

        String token = JwtUtil.createToken(dbAdmin.getId(), dbAdmin.getUsername(), dbAdmin.getRole());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", dbAdmin.getUsername());
        data.put("role", dbAdmin.getRole());
        return Response.data(data);
    }

    // 临时接口：生成 BCrypt 密码，用完删掉
    @GetMapping("/admin/genPwd")
    public String genPwd(String raw) {
        return passwordEncoder.encode(raw);
    }
}
