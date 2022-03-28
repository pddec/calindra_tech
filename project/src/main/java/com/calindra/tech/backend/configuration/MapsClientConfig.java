package com.calindra.tech.backend.configuration;

import com.calindra.tech.backend.clients.MapsClient;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapsClientConfig {

    private static final String GET_ADDRESS ="https://maps.googleapis.com/maps/api/geocode/json";

    @Bean
    public MapsClient mapsClient(){
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(MapsClient.class))
                .logLevel(Logger.Level.FULL)
                .target(MapsClient.class, MapsClientConfig.GET_ADDRESS);
    }

}
