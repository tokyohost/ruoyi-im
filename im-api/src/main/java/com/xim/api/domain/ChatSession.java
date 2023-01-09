package com.xim.api.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 会话管理对象 chat_session
 * 
 * @author ruoyi
 * @date 2023-01-09
 */
public class ChatSession extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** sessionId */
    @Excel(name = "sessionId")
    private String sessionid;

    /** 会话类型，1-用户对用户，2-群组 */
    @Excel(name = "会话类型，1-用户对用户，2-群组")
    private Integer type;

    /** 第一条消息发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "第一条消息发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date firstMsgTime;

    /** 最后一条消息发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后一条消息发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastMsgTime;

    /** uid 的混淆结果，简单混淆可以按一定规则（按字母顺序排序等方式）拼接两个用户的uid，不建议使用hash，会有一定概率出现哈希冲突，此字段主要用于加快会话查询速度 */
    @Excel(name = "uid 的混淆结果，简单混淆可以按一定规则", readConverterExp = "按=字母顺序排序等方式")
    private String uidHash;

    /** 发起会话用户 */
    @Excel(name = "发起会话用户")
    private String sendUid;

    /** 接收会话用户 */
    @Excel(name = "接收会话用户")
    private String receiveUid;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSessionid(String sessionid) 
    {
        this.sessionid = sessionid;
    }

    public String getSessionid() 
    {
        return sessionid;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setFirstMsgTime(Date firstMsgTime) 
    {
        this.firstMsgTime = firstMsgTime;
    }

    public Date getFirstMsgTime() 
    {
        return firstMsgTime;
    }
    public void setLastMsgTime(Date lastMsgTime) 
    {
        this.lastMsgTime = lastMsgTime;
    }

    public Date getLastMsgTime() 
    {
        return lastMsgTime;
    }
    public void setUidHash(String uidHash) 
    {
        this.uidHash = uidHash;
    }

    public String getUidHash() 
    {
        return uidHash;
    }
    public void setSendUid(String sendUid) 
    {
        this.sendUid = sendUid;
    }

    public String getSendUid() 
    {
        return sendUid;
    }
    public void setReceiveUid(String receiveUid) 
    {
        this.receiveUid = receiveUid;
    }

    public String getReceiveUid() 
    {
        return receiveUid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sessionid", getSessionid())
            .append("type", getType())
            .append("firstMsgTime", getFirstMsgTime())
            .append("lastMsgTime", getLastMsgTime())
            .append("uidHash", getUidHash())
            .append("sendUid", getSendUid())
            .append("receiveUid", getReceiveUid())
            .toString();
    }
}
