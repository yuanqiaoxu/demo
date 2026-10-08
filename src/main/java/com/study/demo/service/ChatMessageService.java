package com.study.demo.service;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.study.demo.entity.ChatMessage;
import com.study.demo.mapper.ChatMessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService extends ServiceImpl<ChatMessageMapper, ChatMessage> {

    /**
     * 保存一条消息
     */
    public void saveMessage(Long userId, String role, String content) {
        ChatMessage message = new ChatMessage();
        message.setUserId(userId);
        message.setRole(role);
        message.setContent(content);
        this.save(message);
    }

    /**
     * 查询某个用户的所有历史消息
     */
    public List<ChatMessage> getHistoryByUserId(Long userId) {
        return this.lambdaQuery()
                .eq(ChatMessage::getUserId, userId)
                .orderByAsc(ChatMessage::getId)
                .list();
    }
}
