package com.study.demo.controller;

import com.study.demo.entity.ChatMessage;
import com.study.demo.response.Response;
import com.study.demo.service.AiTools;
import com.study.demo.service.ChatMessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Spring AI 测试接口
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    private final ChatClient chatClient;
    private final ChatMessageService chatMessageService;

    /**
     * Spring AI 自动配置好了 ChatClient.Builder，直接注入即可
     * 同时注入 AiTools，把业务方法注册成 AI 可调用的工具
     */
    public AiController(ChatClient.Builder chatClientBuilder, AiTools aiTools, ChatMessageService chatMessageService) {
        this.chatClient = chatClientBuilder
                .defaultTools(aiTools)
                .build();
        this.chatMessageService = chatMessageService;
    }

    /**
     * 简单对话接口
     */
    @GetMapping("/chat")
    public Response<String> chat(@RequestParam(defaultValue = "你好") String message, HttpServletRequest request) {
        Object userIdObj = request.getAttribute("userId");
        Long userId = ((Number) userIdObj).longValue();

        // 1. 调用 AI
        String result = chatClient.prompt()
                .user(message)
                .call()
                .content();

        // 2. 保存用户消息和 AI 回复到数据库
        chatMessageService.saveMessage(userId, "user", message);
        chatMessageService.saveMessage(userId, "assistant", result);

        return Response.data(result);
    }

    /**
     * 获取当前用户的对话历史
     */
    @GetMapping("/history")
    public Response<List<ChatMessage>> getHistory(HttpServletRequest request) {
        Object userIdObj = request.getAttribute("userId");
        Long userId = ((Number) userIdObj).longValue();
        List<ChatMessage> history = chatMessageService.getHistoryByUserId(userId);
        return Response.data(history);
    }
}
