package com.study.demo.service;

import com.study.demo.entity.Admin;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AI 可调用的业务工具
 * 每个加了 @Tool 注解的方法，AI 都能自动调用
 */
@Component
public class AiTools {

    private final AdminService adminService;

    public AiTools(AdminService adminService) {
        this.adminService = adminService;
    }

    @Tool(description = "查询系统中管理员的总数量。当用户问有多少管理员、管理员数量时调用此方法。")
    public int getAdminCount() {
        Long count = adminService.count();
        return count == null ? 0 : count.intValue();
    }

    @Tool(description = "查询所有管理员的详细信息，包括姓名、用户名、性别、电话。当用户问管理员都有谁、列出管理员名单、管理员性别分布时调用此方法。")
    public String getAdminList() {
        List<Admin> list = adminService.list();
        if (list == null || list.isEmpty()) {
            return "没有找到管理员数据";
        }
        return list.stream()
                .map(admin -> "- 姓名：" + admin.getName() 
                        + "，用户名：" + admin.getUsername()
                        + "，性别：" + admin.getSex()
                        + "，电话：" + admin.getTel())
                .collect(Collectors.joining("\n"));
    }
}
