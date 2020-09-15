# i18n-demo
基于i18n利用springboot实现后台国际化demo

引入外部依赖：commons-lang3、lombok

## 背景
项目做到尾声，临时需要添加国际化内容，开始只是前台展示的页面添加了国际化支持，后来发现一些后台提示很不友好，遂也选择进行国际化处理。
## 实现
**Springboot对国际化支持的很好，我们就用自带的i18n实现国际化就行。**

1. application.properties配置文件指定messages位置	
	```
	spring.messages.basename=i18n.message
	spring.messages.encoding=UTF-8
	```
	通过查看源码我们可以知道，如果不进行配置，默认读取resource下面messages文件，源码如下：
![springboot默认国际化配置读取位置](https://img-blog.csdnimg.cn/20200511183958366.png)

 2. 在resource文件夹下面创建我们配置的basename，目录结构如下图：  
![三个配置文件](https://img-blog.csdnimg.cn/20200512093846448.png)
**其中 message.properties必须要有，其他的配置文件命名格式为：message_语言_国家.properties**,其中语言和国家格式可以查看 java.util.Locale 类中的说明。

3. 自定义重写 LocaleResolver 类的 resolveLocale 方法，代码如下：
	```
	public class MyLocaleResolverConfig implements LocaleResolver {
	
	    private static final String PATH_PARAMETER = "lang";
	
	    private static final String PATH_PARAMETER_SPLIT = "_";
	
	    @Override
	    public Locale resolveLocale(HttpServletRequest request) {
	        String lang = request.getHeader(PATH_PARAMETER);
	        Locale locale = request.getLocale();
	        if (!StringUtils.isEmpty(lang)) {
	            String[] split = lang.split(PATH_PARAMETER_SPLIT);
	            locale = new Locale(split[0], split[1]);
	        }
	        return locale;
	    }
	
	    @Override
	    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
	
	    }
	}
	```
 	 resolveLocale 方法作用就是我们的程序从哪获取指定的语言信息，我这里采用的是从header中获取指定语言，如果没有则采用浏览器 默认的。 还可以从 Session 或 Cookie 中获取。**这里因为我们采取header中获取，所以前端发的请求中 Request Headers 中要有 key 为 lang，value 为 en_US 或 zh_CN 的header。**

4.  将我们自定义的 MyLocaleResolverConfig 作为 Bean 注册进系统中：
	```
	@Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolverConfig();
    }
	```

5. 编写一个工具类用于动态获取提示信息，避免了在每个类中重复操作。
	```
	@Component
	public class MessageUtil {
	    @Autowired
	    private MessageSource messageSource;
	
	    private static MessageSource staticMessageSource;
	
	    @PostConstruct
	    public void init() {
	        MessageUtil.staticMessageSource = messageSource;
	    }
	
	    public static String getMessage(String messageKey) {
	        Locale locale = LocaleContextHolder.getLocale();
	        return staticMessageSource.getMessage(messageKey, null, locale);
	    }
	}
	```
6. 到第五步我们已经实现了动态获取消息，这一步只是更好的管理 messageKey，建立枚举类存放 messageKey （可选）
	```
	@Getter
	@AllArgsConstructor
	public enum MessageEnum {
	   // 存储 message.properties 文件中存在的key，
	   // 例如，message.properties文件中有 test.hello=你好
	   //message_en_US.properties 中有 test.hello=hello
	   //message_zh_CN.properties 中有 test.hello=你好
	   TEST_HELLO("test.hello")
	    ;
	    private String key;
	}
	```
7. 测试，在测试类中执行下面方法就可以根据 header 中的值得到对应语言的信息。
	```
	String message = MessageUtil.getMessage(MessageEnum.TEST_HELLO.getKey())
	```

----
Java官方关于国际化的说明：[https://docs.oracle.com/javase/tutorial/i18n/index.html](https://docs.oracle.com/javase/tutorial/i18n/index.html)
