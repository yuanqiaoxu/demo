package com.study.demo.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.demo.response.Response;
import com.study.demo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            returnError(response, 401, "未登录");
            return false;
        }
        try {
            Claims claims = JwtUtil.parseToken(token);
            String role = (String) claims.get("role");
            request.setAttribute("userId", claims.get("userId"));
            request.setAttribute("username", claims.get("username"));
            request.setAttribute("role", role);

            // 管理员管理接口只有 admin 角色能访问
            String path = request.getRequestURI();
            if (path.contains("/admin/list") || path.contains("/admin/add")
                    || path.contains("/admin/update") || path.contains("/admin/delete")) {
                if (!"admin".equals(role)) {
                    returnError(response, 403, "没有权限访问");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            returnError(response, 401, "登录已过期，请重新登录");
            return false;
        }
    }

    private void returnError(HttpServletResponse response, int code, String msg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(Response.ERROR(code, msg)));
        writer.flush();
    }
}
