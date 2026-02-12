package com.huangjun.taskmanager.config;

import com.huangjun.taskmanager.interceptor.GlobalInterceptor;
import com.huangjun.taskmanager.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Autowired
    private GlobalInterceptor globalInterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/registry")
                .order(2);
        registry.addInterceptor(globalInterceptor)
                .addPathPatterns("/**")
                .order(0);
    }

//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//
//        // 2. 定义ObjectMapper (核心配置)
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // --- 配置 A: 处理 Long 类型精度丢失 (转为 String) ---
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
//        objectMapper.registerModule(simpleModule);
//
//        // --- 配置 B: 处理日期格式 (如 yyyy-MM-dd HH:mm:ss) ---
//        // 处理 JDK8 之前的 Date
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//
//        // 处理 JDK8 后的 LocalDateTime
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
//        objectMapper.registerModule(javaTimeModule);
//
//        // 3. 将配置好的 Mapper 设置给转换器
//        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
//
//        // 4. 将转换器添加到列表 (索引设为0，确保它排在第一位优先生效)
//        converters.add(0, jackson2HttpMessageConverter);
//    }
}
