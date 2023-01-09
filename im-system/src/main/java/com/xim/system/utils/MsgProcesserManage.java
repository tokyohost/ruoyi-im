package com.xim.system.utils;

import com.xim.system.enums.MsgType;
import com.xim.system.work.MsgProcesser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 21:12
 * @Content
 */

@Component
public class MsgProcesserManage {
    @Autowired
    private List<MsgProcesser<?>> allProcesser;

    private boolean inited = false;

    private HashMap<MsgType,MsgProcesser<?>> processerMap = new HashMap<>();


    public MsgProcesser getProcesser(MsgType msgType){
        //延迟初始化map
        if (!inited) {
            Map<MsgType, ? extends MsgProcesser<?>> map = allProcesser.stream().collect(Collectors.toMap(MsgProcesser::getMsgType, msgProcesser -> msgProcesser, (a, b) -> a));
            processerMap.putAll(map);
            inited = !inited;
        }

        return processerMap.get(msgType);
    }

}
