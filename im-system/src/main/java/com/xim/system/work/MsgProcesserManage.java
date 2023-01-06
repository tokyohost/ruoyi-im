package com.xim.system.work;

import com.xim.system.enums.MsgType;
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
    private List<MsgProcesser<?>> allProcesser;

    private HashMap<MsgType,MsgProcesser<?>> processerMap = new HashMap<>();

    public MsgProcesserManage(List<MsgProcesser<?>> allProcesser) {
        this.allProcesser = allProcesser;
        Map<MsgType, ? extends MsgProcesser<?>> map = allProcesser.stream().collect(Collectors.toMap(MsgProcesser::getMsgType, msgProcesser -> msgProcesser, (a, b) -> a));
        processerMap.putAll(map);
    }

    public MsgProcesser getProcesser(MsgType msgType){
        return processerMap.get(msgType);
    }

}
