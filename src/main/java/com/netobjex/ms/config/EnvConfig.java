package com.netobjex.ms.config;

/**
 * Created by phuongdv on 25/06/2017.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

//@Component
@Configuration

public class EnvConfig {
    private static final Logger LOG = LoggerFactory.getLogger(EnvConfig.class);
    private static Environment env = null;

    public static Environment getEnv() {
        return env;
    }

    @Autowired
    public static void setEnvironment(Environment e) {
        env = e;
    }
}