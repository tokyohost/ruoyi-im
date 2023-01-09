package com.xim.api.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.xim.api.domain.ChatSession;
import com.xim.api.service.IChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 会话管理Controller
 * 
 * @author ruoyi
 * @date 2023-01-09
 */
@RestController
@RequestMapping("/xim/session")
public class ChatSessionController extends BaseController
{
    @Autowired
    private IChatSessionService chatSessionService;

    /**
     * 查询会话管理列表
     */
    @PreAuthorize("@ss.hasPermi('xim:session:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatSession chatSession)
    {
        startPage();
        List<ChatSession> list = chatSessionService.selectChatSessionList(chatSession);
        return getDataTable(list);
    }

    /**
     * 导出会话管理列表
     */
    @PreAuthorize("@ss.hasPermi('xim:session:export')")
    @Log(title = "会话管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatSession chatSession)
    {
        List<ChatSession> list = chatSessionService.selectChatSessionList(chatSession);
        ExcelUtil<ChatSession> util = new ExcelUtil<ChatSession>(ChatSession.class);
        util.exportExcel(response, list, "会话管理数据");
    }

    /**
     * 获取会话管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('xim:session:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(chatSessionService.selectChatSessionById(id));
    }

    /**
     * 新增会话管理
     */
    @PreAuthorize("@ss.hasPermi('xim:session:add')")
    @Log(title = "会话管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatSession chatSession)
    {
        return toAjax(chatSessionService.insertChatSession(chatSession));
    }

    /**
     * 修改会话管理
     */
    @PreAuthorize("@ss.hasPermi('xim:session:edit')")
    @Log(title = "会话管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatSession chatSession)
    {
        return toAjax(chatSessionService.updateChatSession(chatSession));
    }

    /**
     * 删除会话管理
     */
    @PreAuthorize("@ss.hasPermi('xim:session:remove')")
    @Log(title = "会话管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(chatSessionService.deleteChatSessionByIds(ids));
    }
}
