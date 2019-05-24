package com.mpaiement.configurations;

import brave.sampler.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfig {

    @Bean
    public Sampler defaultSampler(){
        // Demande à Sleuth d'exporter toutes les requêtes vers Zipkin
        // pour les annalyser
        return Sampler.ALWAYS_SAMPLE;
    }
}
