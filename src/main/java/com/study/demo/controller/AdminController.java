package com.study.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.demo.entity.Admin;
import com.study.demo.exception.BuessnessExcepeion;
import com.study.demo.response.Response;
import com.study.demo.response.ResponseCode;
import com.study.demo.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.study.demo.response.ResponseCode.USERNAME_EXIST;

@Tag(name = "管理员信息管理")
@RestController
public class AdminController {
    @Resource
    private AdminService adminService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     *
     * 是否成功
     * 如果失败需要知道错误提示
     * 如果成功返回数据
     */

    @Operation(summary = "新增管理员")
    @PostMapping("/admin/add")
    public Response add(@RequestBody Admin admin) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, admin.getUsername());
        Long count = adminService.count(queryWrapper);
        if (count > 0) {
            throw new BuessnessExcepeion(ResponseCode.USERNAME_EXIST);
        }
        // 密码 BCrypt 加密后再存
        admin.setUserpwd(passwordEncoder.encode(admin.getUserpwd()));
        adminService.save(admin);
        return Response.success();
    }

    @Operation(summary = "管理员新系列表")
    @PostMapping("/admin/list")
    public Response<PageInfo<Admin>> list(@RequestBody Admin admin,@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(admin.getUsername()!=null,Admin::getUsername, admin.getUsername());
        lambdaQueryWrapper.like(admin.getTel()!=null,Admin::getTel,admin.getTel());

        PageHelper.startPage(pageNum,pageSize);
        List<Admin> list = adminService.list(lambdaQueryWrapper);
        PageInfo<Admin>pageInfo = new PageInfo(list);
        return Response.data(pageInfo);
    }

    @Operation(summary = "更新管理员")
    @PostMapping("/admin/update")
    public Response update(@RequestBody Admin admin) {
        // 如果传了新密码，加密后再更新
        if (admin.getUserpwd() != null && !admin.getUserpwd().isEmpty()) {
            admin.setUserpwd(passwordEncoder.encode(admin.getUserpwd()));
        } else {
            // 留空则不修改密码
            admin.setUserpwd(null);
        }
        adminService.updateById(admin);
        return Response.data(admin);
    }

    @Operation(summary = "删除管理员")
    @PostMapping("/admin/delete")
    public Response delete(@RequestParam Long id) {
        adminService.removeById(id);
        return Response.success();
    }
}
