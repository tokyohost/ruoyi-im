package com.xim.api.service.impl;

import java.util.List;

import com.xim.api.domain.ChatSession;
import com.xim.api.mapper.ChatSessionMapper;
import com.xim.api.service.IChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会话管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-01-09
 */
@Service
public class ChatSessionServiceImpl implements IChatSessionService
{
    @Autowired
    private ChatSessionMapper chatSessionMapper;

    /**
     * 查询会话管理
     * 
     * @param id 会话管理主键
     * @return 会话管理
     */
    @Override
    public ChatSession selectChatSessionById(Long id)
    {
        return chatSessionMapper.selectChatSessionById(id);
    }

    /**
     * 查询会话管理列表
     * 
     * @param chatSession 会话管理
     * @return 会话管理
     */
    @Override
    public List<ChatSession> selectChatSessionList(ChatSession chatSession)
    {
        return chatSessionMapper.selectChatSessionList(chatSession);
    }

    /**
     * 新增会话管理
     * 
     * @param chatSession 会话管理
     * @return 结果
     */
    @Override
    public int insertChatSession(ChatSession chatSession)
    {
        return chatSessionMapper.insertChatSession(chatSession);
    }

    /**
     * 修改会话管理
     * 
     * @param chatSession 会话管理
     * @return 结果
     */
    @Override
    public int updateChatSession(ChatSession chatSession)
    {
        return chatSessionMapper.updateChatSession(chatSession);
    }

    /**
     * 批量删除会话管理
     * 
     * @param ids 需要删除的会话管理主键
     * @return 结果
     */
    @Override
    public int deleteChatSessionByIds(Long[] ids)
    {
        return chatSessionMapper.deleteChatSessionByIds(ids);
    }

    /**
     * 删除会话管理信息
     * 
     * @param id 会话管理主键
     * @return 结果
     */
    @Override
    public int deleteChatSessionById(Long id)
    {
        return chatSessionMapper.deleteChatSessionById(id);
    }
}
