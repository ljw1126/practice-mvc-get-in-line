package com.uno.getinline.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

/**
 * decoupledLogic 활성화를 위한 Configuration 등록
 * localhost:8080/events/ 호출시 index.html에 index.th.xml 내용이 include 됨
 */
@Configuration
public class ThymeleafConfig {
    /**
     *  ThymeleafAutoConfiguration 클래스에 defaultTemplateResolver 설정값만 바꾸기 위해 사용
     * @param defaultTemplateResolver
     * @return
     */
    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver(
            SpringResourceTemplateResolver defaultTemplateResolver,
            Thymeleaf3Properties thymeleaf3Properties
    ) {
        defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());

        return defaultTemplateResolver;
    }

    /**
     * ThymeleafProperties.class가 존재함
     * @SpringBootApplcation 위에 @ConfigurationPropertiesScan 추가 함
     */
    @Getter
    @RequiredArgsConstructor
    @ConstructorBinding
    @ConfigurationProperties("spring.thymeleaf3")
    public static class Thymeleaf3Properties {
        /**
         * Thymeleaf 3 Decoupled Logic 활성화
         */
        private final boolean decoupledLogic;
    }
}
