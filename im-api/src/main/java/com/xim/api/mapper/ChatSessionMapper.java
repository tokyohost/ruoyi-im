package com.xim.api.mapper;

import com.xim.api.domain.ChatSession;

import java.util.List;

/**
 * 会话管理Mapper接口
 * 
 * @author ruoyi
 * @date 2023-01-09
 */
public interface ChatSessionMapper 
{
    /**
     * 查询会话管理
     * 
     * @param id 会话管理主键
     * @return 会话管理
     */
    public ChatSession selectChatSessionById(Long id);

    /**
     * 查询会话管理列表
     * 
     * @param chatSession 会话管理
     * @return 会话管理集合
     */
    public List<ChatSession> selectChatSessionList(ChatSession chatSession);

    /**
     * 新增会话管理
     * 
     * @param chatSession 会话管理
     * @return 结果
     */
    public int insertChatSession(ChatSession chatSession);

    /**
     * 修改会话管理
     * 
     * @param chatSession 会话管理
     * @return 结果
     */
    public int updateChatSession(ChatSession chatSession);

    /**
     * 删除会话管理
     * 
     * @param id 会话管理主键
     * @return 结果
     */
    public int deleteChatSessionById(Long id);

    /**
     * 批量删除会话管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatSessionByIds(Long[] ids);
}
