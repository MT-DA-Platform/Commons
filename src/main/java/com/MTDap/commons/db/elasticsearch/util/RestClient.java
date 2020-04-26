package com.MTDap.commons.db.elasticsearch.util;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RestClient {
    private final static Logger logger = LoggerFactory.getLogger(RestClient.class);

    public static String getCall(String url) throws ClientProtocolException, IOException {
        StringBuffer result = new StringBuffer();
        try (CloseableHttpClient client = HttpClients.createDefault();) {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new ClientProtocolException(
                        response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
            }
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));) {
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            }
        }

        return result.toString();
    }

    public static String postCall(String url, ArrayList<String> entity) throws ClientProtocolException, IOException {

        StringBuffer result = new StringBuffer();
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(entity.get(0)));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                logger.error("Error while calling" + url + " " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
                throw new ClientProtocolException(
                        response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
            }
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));) {
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            }
        }
        logger.debug(result.toString());
        return result.toString();

    }

    public static String upsertCall(String url) throws ClientProtocolException, IOException {

        StringBuffer result = new StringBuffer();
        try (CloseableHttpClient client = HttpClients.createDefault();) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new ClientProtocolException(
                        response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
            }
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));) {
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            }
        }
        logger.debug(result.toString());
        return result.toString();
    }
}
