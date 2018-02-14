package com.netobjex.ms.utils;

import java.util.Map;

/**
 * Created by phuongdv on 25/06/2017.
 */
public class QueryParamMapper {

    public static String createQuery(String strParams, Map<String, String> mapParams) {
        for (Map.Entry<String, String> entry : mapParams.entrySet()) {
            strParams = strParams.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return strParams;
    }
}
