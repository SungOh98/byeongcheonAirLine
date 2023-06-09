package com.koreatech.byeongcheonairlineapi.config;

import com.koreatech.byeongcheonairlineapi.interceptor.ConfirmInterceptor;
import com.koreatech.byeongcheonairlineapi.interceptor.SynchronousInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final ConfirmInterceptor confirmInterceptor;
    private final SynchronousInterceptor synchronousInterceptor;

    @Autowired
    public InterceptorConfiguration(ConfirmInterceptor confirmInterceptor, SynchronousInterceptor synchronousInterceptor) {
        this.confirmInterceptor = confirmInterceptor;
        this.synchronousInterceptor = synchronousInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(confirmInterceptor) // 사용할 인터셉터 추가
                .addPathPatterns("/member/*") // 인터셉트 적용 URL 명시
                .excludePathPatterns("/member/login") // 적용 URL 중 제외할 URL 명시
                .excludePathPatterns("/member/logout/*")
                .addPathPatterns("/tickets/member") // 회원 티켓 조회도 추가.
                .addPathPatterns("/ticket/member")
                .addPathPatterns("/board/*");
        registry.addInterceptor(synchronousInterceptor)
                .addPathPatterns("/seats/*")
                .addPathPatterns("/tickets/*")
                .addPathPatterns("/flight/*");
    }
}
