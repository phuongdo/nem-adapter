package com.netobjex.ms;


import com.netobjex.ms.config.EnvConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by phuongdv on 08/09/2017.
 */
@SpringBootApplication
@EnableScheduling
public class Bootstrap_Application {
    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap_Application.class);

    /**
     * Profile : dev ,java -jar xxx.jar
     * Profile : prod, java -jar -Dspring.profiles.active=prod
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap_Application.class, args);

        // CLOSE and RELEASE resource
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOG.info("shutting down.......");

            }
        });
    }

    @Autowired
    void setEnvironment(Environment e) {
        LOG.info("Setting environment...");
        EnvConfig.setEnvironment(e);

    }

}
// end::runner[]