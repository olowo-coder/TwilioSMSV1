package com.example.springtwilosmsv1.resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TwilioRouterConfig {

    private final TwilioHandler handler;

    @Autowired
    public TwilioRouterConfig(TwilioHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> handleSMS(){
        return RouterFunctions.route()
                .POST("/router/sendOTP", handler::sendOTP)
                .POST("/router/infobOTP", handler::infobApi)
                .POST("/router/validateOTP", handler::validateOTP)
                .build();
    }
}
