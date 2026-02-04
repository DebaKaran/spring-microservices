package com.eazybytes.gatewayserver.configs;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;

@Configuration
public class ReactorMdcConfig {

   @PostConstruct
   public void enableMdc() {
       Hooks.enableAutomaticContextPropagation();
   }
}
