package com.netobjex.ms.service;

import com.netobjex.ms.config.EnvConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LedgerService {
    private static final Logger LOG = LoggerFactory.getLogger(LedgerService.class);
    //    private final static String API_BASE = "https://api.stg.netobjex.com/api";
    private final static String API_BASE = EnvConfig.getEnv().getProperty("microservices.ap-api");
//    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    IotaService iotaService;


    @Autowired
    NemService nemService;

    public void save(Map<String, String> payload) {
        String privatekey = payload.get("privatekey");
        String publickey = payload.get("publickey");
        String type = payload.get("type");
        String json = payload.get("json");
        String urlEndPoint = payload.get("url");
        LOG.info("message : {}, type: {}", json, type);

        if (type.equals("IOTA")) {
            try {
                iotaService.saveMessage(urlEndPoint, json, privatekey, publickey);
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        } else if (type.equals("NEM")) {
            try {
                nemService.saveMessage(urlEndPoint, json, privatekey, publickey);
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }

        }

    }


}