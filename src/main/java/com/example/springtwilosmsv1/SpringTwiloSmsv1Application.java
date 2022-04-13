package com.example.springtwilosmsv1;

import com.example.springtwilosmsv1.config.TwilioConfig;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringTwiloSmsv1Application {

    @Autowired
    private TwilioConfig twilioConfig;

//    @PostConstruct
//    public void initTwilio(){
//        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
//    }

    public static void main(String[] args) {
        SpringApplication.run(SpringTwiloSmsv1Application.class, args);
    }

}
