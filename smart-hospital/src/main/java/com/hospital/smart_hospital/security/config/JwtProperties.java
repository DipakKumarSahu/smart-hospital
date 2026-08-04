package com.hospital.smart_hospital.security.config;



import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * Base64 encoded secret key
     */
    private String secret;

    /**
     * Access token expiration in milliseconds
     */
    private long expiration;

}