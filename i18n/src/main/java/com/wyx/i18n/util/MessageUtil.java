package com.wyx.i18n.util;

import com.wyx.i18n.enums.MessageEnum;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Locale;

/**
 * @ClassName MessageUtil
 * @Description 实质消息获取类
 * @Author yuxiang
 * @Date 2020/9/15
 * @Version 1.0
 **/
@Component
public class MessageUtil {


    @Resource
    private MessageSource messageSource;

    private static MessageSource staticMessageSource;

    @PostConstruct
    public void init() {
        MessageUtil.staticMessageSource = messageSource;
    }

    public static String getMessage(MessageEnum messageEnum) {
        Locale locale = LocaleContextHolder.getLocale();
        return staticMessageSource.getMessage(messageEnum.getKey(), null, locale);
    }
}
