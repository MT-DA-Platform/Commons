package com.MTDap.commons.util;

import static com.MTDap.commons.constants.Constants.*;

public class RestUtils {
    private RestUtils() {
    }

    public static String getHostURL(String host, String port, boolean ssl) {
        String prefix;
        if (ssl) {
            prefix = HTTPS_PREFIX;
        } else {
            prefix = HTTP_PREFIX;
        }
        if(port==null){
            return prefix + host;
        }else{
            return prefix + host + COLON + port;
        }
    }

    public static String getURL(String hostURL, String[] path) {
        return hostURL + SLASH + String.join(SLASH, path);
    }

    public static String getServiceURL(String apiGatewayURL, String service, String[] path) {
        return apiGatewayURL + SLASH + service + SLASH + String.join(SLASH, path);
    }
}
