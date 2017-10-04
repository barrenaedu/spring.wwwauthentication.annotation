package com.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityManagerImpl implements SecurityManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(SecurityManagerImpl.class);

    public boolean isAuthorized(String user, String pass, List<String> resources, List<String> actions) {
        LOGGER.info("User: '{}'", user);
        LOGGER.info("Pass: '{}'", pass);
        LOGGER.info("Resources: {}", resources);
        LOGGER.info("Actions: {}", actions);
        return "Aladdin".equals(user) && "open sesame".equals(pass);
    }

}
