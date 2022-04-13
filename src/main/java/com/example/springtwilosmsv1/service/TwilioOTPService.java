package com.example.springtwilosmsv1.service;

import com.example.springtwilosmsv1.config.TwilioConfig;
import com.example.springtwilosmsv1.dto.OtpStatus;
import com.example.springtwilosmsv1.dto.ResetReqDto;
import com.example.springtwilosmsv1.dto.ResetResponseDto;
import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.Configuration;
import com.infobip.api.SendSmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.twilio.type.PhoneNumber;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class TwilioOTPService {

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    private final TwilioConfig twilioConfig;

    private final Map<String, String> otpMap = new HashMap<>();

    @Autowired
    public TwilioOTPService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    public Mono<ResetResponseDto> sendOTP(ResetReqDto resetReqDto) {
        ResetResponseDto resetResponseDto;
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            System.out.println(resetReqDto);
            PhoneNumber to = new PhoneNumber(resetReqDto.getPhoneNumber());
            PhoneNumber from = new PhoneNumber("+17622456922");
            final String otp = generateOTP();
            otpMap.put(resetReqDto.getUsername(), otp);
            String body = "Dear customer, your OTP: " + otp + " to complete transaction";

            Message message = Message.creator(to, from, body).create();
            System.out.println(message.getSid());
            resetResponseDto = new ResetResponseDto(body, OtpStatus.DELIVERED);
        } catch (Exception ex) {
            resetResponseDto = new ResetResponseDto(ex.getMessage(), OtpStatus.DELIVERED);
        }
        return Mono.just(resetResponseDto);
    }

    public Mono<String> validateOTP(String inputOTP, String username){
        if(inputOTP.equals(otpMap.get(username))){
            return Mono.just("Valid OTP, proceed with transaction");
        }
        else {
            return Mono.error(new IllegalStateException("Invalid otp, retry"));
        }
    }

    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }

    public Mono<String> infobApi(ResetReqDto resetReqDto){
        RequestBody formBody = new FormBody.Builder()
                .add("username", "test")
                .add("password", "test")
                .build();

        ApiClient apiClient = new ApiClient();
        apiClient.setApiKeyPrefix("App");
        apiClient.setApiKey("c4c49a21c2251e02385b738978e4ab58-bcff8525-3d50-4391-a978-b38387aaea03");
        apiClient.setBasePath("http://gyqxk6.api.infobip.com");
        Configuration.setDefaultApiClient(apiClient);

        final String otp = generateOTP();
        otpMap.put(resetReqDto.getUsername(), otp);
        String body = "Dear customer, your OTP: " + otp + " to complete transaction";
        SendSmsApi sendSmsApi = new SendSmsApi();
        SmsTextualMessage smsMessage = new SmsTextualMessage()
                .from("SunDevOlowo")
                .addDestinationsItem(new SmsDestination().to(resetReqDto.getPhoneNumber()))
                .text(body);

        SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest().messages(
                Collections.singletonList(smsMessage)
        );

        try {
            SmsResponse smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest);
            System.out.println(smsResponse.getBulkId());
        } catch (ApiException apiException) {
            // HANDLE THE EXCEPTION
        }

        return Mono.just("OTP sent");
    }
}
