package com.netobjex.ms.service;


import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class NemTest {

    String NEM_END_POINT = "http://104.128.226.60:7890";

    @Test
    public void testNemAPI() throws MalformedURLException {

        URL url = new URL(NEM_END_POINT);
        System.out.println(url.getProtocol() + "," + url.getHost() + "," + url.getPort());


    }

}
