package com.clientui.configurations;

import com.clientui.exceptions.CustomErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfigException {

    @Bean
    public CustomErrorDecoder mCustomDecoder(){
        return new CustomErrorDecoder();
    }
}
