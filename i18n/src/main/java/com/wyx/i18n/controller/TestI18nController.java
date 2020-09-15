package com.wyx.i18n.controller;

import com.wyx.i18n.enums.MessageEnum;
import com.wyx.i18n.util.MessageUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestI18nController
 * @Description 国际化测试接口
 * @Author yuxiang
 * @Date 2020/9/15
 * @Version 1.0
 **/
@RestController
@RequestMapping("/test")
public class TestI18nController {

    @GetMapping("/i18n")
    public String testI18n() {
        String hello = MessageUtil.getMessage(MessageEnum.HELLO);
        String i18n = MessageUtil.getMessage(MessageEnum.HELLO_I18N);
        return hello + "\t" + i18n;
    }
}
