package com.MTDap.commons.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    private static final Charset CHARSET = Charset.forName("UTF-8");

    private HttpClient() {
    }

    public static String call(String url, Map<String, String> headers, String type, String data) {
        //public static String call(String url, Map<String, String> headers, String type, String data) throws APIClientException {

        StringBuilder result = new StringBuilder();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpResponse response = null;

            if (StringUtils.endsWithIgnoreCase("POST", type)) {
                HttpPost httpPost = new HttpPost(url);
                headers.forEach(httpPost::addHeader);
                httpPost.setEntity(new StringEntity(data, "UTF-8"));
                response = httpClient.execute(httpPost);
            } else {
                HttpGet getRequest = new HttpGet(url);
                headers.forEach(getRequest::addHeader);
                response = httpClient.execute(getRequest);
            }

            if (response.getStatusLine().getStatusCode() != 200) {
                String message = "Failed : HTTP error code : " + response.getStatusLine().getStatusCode();
                LOGGER.error(message);
            }

            try (BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), CHARSET))) {

                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            }

        } catch (IOException e) {
            String message = "Error while executing request : " + url;
            LOGGER.error(message, e);
            //throw new APIClientException(message, e);

        }

        return result.toString();

    }
}
