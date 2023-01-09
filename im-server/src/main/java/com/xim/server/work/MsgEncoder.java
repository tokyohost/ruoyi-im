package com.xim.server.work;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/9 10:19
 * @Content
 */

public interface MsgEncoder<T,D>{

    /**
     * 编码后的消息体
     * @param msg
     * @return
     */
    D encoder(T msg);

}
