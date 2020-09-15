package com.wyx.i18n.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageEnum {

    HELLO("hello"),
    HELLO_I18N("hello.i18n"),
    ;
    private String key;
}
