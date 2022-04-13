package com.example.springtwilosmsv1.resource;

import com.example.springtwilosmsv1.dto.ResetReqDto;
import com.example.springtwilosmsv1.service.TwilioOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TwilioHandler {

    private final TwilioOTPService twilioOTPService;

    @Autowired
    public TwilioHandler(TwilioOTPService twilioOTPService) {
        this.twilioOTPService = twilioOTPService;
    }

    public Mono<ServerResponse> sendOTP(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ResetReqDto.class)
                .flatMap(twilioOTPService::sendOTP)
                .flatMap(dto -> ServerResponse.status(HttpStatus.OK)
                        .body(BodyInserters.fromValue(dto)));

    }

    public Mono<ServerResponse> validateOTP(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ResetReqDto.class)
                .flatMap(dto -> twilioOTPService.validateOTP(dto.getOtp(), dto.getUsername()))
                .flatMap(dto -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(dto));

    }

    public Mono<ServerResponse> infobApi(ServerRequest serverRequest){
        return serverRequest.bodyToMono(ResetReqDto.class)
                .flatMap(twilioOTPService::infobApi)
                .flatMap(dto -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(dto));
    }
}
