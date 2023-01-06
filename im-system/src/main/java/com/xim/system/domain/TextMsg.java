package com.xim.system.domain;

/**
 * @author xuehui_li
 * @Version 1.0
 * @date 2023/1/6 20:56
 * @Content
 */

public class TextMsg extends BaseMsg<String>{
    String text;

    public String getText() {
        return text;
    }

    public TextMsg setText(String text) {
        this.text = text;
        return this;
    }
}
