package com.MTDap.commons.db.elasticsearch.util;

import com.MTDap.commons.client.HttpClient;
import com.MTDap.commons.db.elasticsearch.ConnectionProvider;
import com.MTDap.commons.exception.APIClientException;
import com.MTDap.commons.model.DTOParams;
import com.MTDap.commons.model.DTOResponse;
import com.MTDap.commons.util.JsonUtils;
import com.MTDap.commons.util.StringUtils;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import org.apache.http.client.ClientProtocolException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Component
public class DBUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBUtil.class);
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private String port;
    @Autowired
    private ConnectionProvider connectionProvider;

    public void upsert(String id, String index, DTOParams params) {

        IndexRequest indexRequest = new IndexRequest(index).source(params, XContentType.JSON);
        UpdateRequest updateRequest = new UpdateRequest()
                .index(index)
                .id(id)
                .doc(params, XContentType.JSON)
                .upsert(indexRequest);

        try {
            connectionProvider.getConnection().update(updateRequest, RequestOptions.DEFAULT);

        } catch (IOException e) {
            String msg = "Error while upserting record : " + params;
            LOGGER.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public List<DTOResponse> executeXPackQuery(String query) {
        LOGGER.debug("Executing query : " + query);
        String url = "http://" + host + ":" + port + "/_sql";

        Map<String, String> headers = Maps.newHashMap();
        headers.put("Accept", "application/json");
        headers.put("Content-type", "application/json");

        String response = null;

        JsonObject queryObject = new JsonObject();
        queryObject.addProperty("query", query);

        try {
            response = HttpClient.call(url, headers, "POST", queryObject.toString());

        } catch (APIClientException e) {
            String msg = "Error while executing query : " + query;
            LOGGER.error(msg, e);
            throw new RuntimeException(msg, e);
        }

        return toResultList(response);

    }

    @SuppressWarnings("unchecked")
    private List<DTOResponse> toResultList(String result) {
        List<DTOResponse> resultList = new ArrayList<>();
        Map<String, Object> resultMap = JsonUtils.readJsonAsMap(result);
        List<Map<String, Object>> columns = (List<Map<String, Object>>) resultMap.get("columns");
        List<String> columnNames = new ArrayList<>();
        for (Map<String, Object> column : columns) {
            columnNames.add((String) column.get("name"));
        }
        List<List<Object>> rows = (List<List<Object>>) resultMap.get("rows");
        for (List<Object> row : rows) {
            DTOResponse response = new DTOResponse();
            for (int i = 0; i < row.size(); i++) {
                String colName = columnNames.get(i);
                response.put(colName, row.get(i));
            }
            resultList.add(response);
        }
        return resultList;
    }

    public JSONObject executePut(String index, String dataType, HashMap<String, Object> record) throws JSONException {
        JSONObject elasticsearchResponse = null;
        String insertURL = "/" + index + "/" + dataType + "/optionalUniqueId";
        String url = "http://" + host + ":" + port + insertURL;
        ArrayList<String> postBody = new ArrayList<>();

        JSONObject postObject = new JSONObject();
        postObject.put("id", record.get("id"));
        postObject.put("location", record.get("location"));
        postObject.put("value", record.get("value"));
        postObject.put("time", record.get("time"));

        postBody.add(postObject.toString());

        try {
            elasticsearchResponse = new JSONObject(RestClient.postCall(url, postBody));
        } catch (ClientProtocolException e) {

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return elasticsearchResponse;
    }

    public JSONArray getAllQueryFromIndex(String index) throws JSONException {

        JSONObject elasticsearchResponse = null;
        String searchUrl = "/" + index + "/_search";

        JSONObject matchObject = new JSONObject();

        JSONObject queryObject = new JSONObject();
        queryObject.put("match_all", matchObject);

        JSONObject postObject = new JSONObject();
        postObject.put("query", queryObject);
        postObject.put("size", 1000);

        String url = "http://" + host + ":" + port + searchUrl;
        ArrayList<String> postBody = new ArrayList<>();
        postBody.add(postObject.toString());

        try {
            elasticsearchResponse = new JSONObject(RestClient.postCall(url, postBody));
        } catch (ClientProtocolException e) {

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return elasticsearchResponse.getJSONObject("hits").getJSONArray("hits");
    }

    public JSONArray executeQuery(String index, ArrayList<String> postBody) throws JSONException {
        JSONObject elasticsearchResponse = null;
        String searchUrl = "/" + index + "/_search";
        String url = "http://" + host + ":" + port + searchUrl;

        try {
            elasticsearchResponse = new JSONObject(RestClient.postCall(url, postBody));
        } catch (ClientProtocolException e) {

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return elasticsearchResponse.getJSONObject("hits").getJSONArray("hits");
    }

    public JSONObject executeBulkInsert(String index, String documentType, List<Map<String, Object>> bulk) {
        JSONObject elasticsearchResponse = null;
        String searchUrl = "/_bulk";
        String url = "http://" + host + ":" + port + searchUrl;

        String actionMetaData = String.format("{ \"index\" : { \"_index\" : \"%s\", \"_type\" : \"%s\" } }%n", index, documentType);

        StringBuilder bulkRequestBody = new StringBuilder();
        for (Map<String, Object> bulkItem : bulk) {
            bulkRequestBody.append(actionMetaData);
            bulkRequestBody.append(StringUtils.convertMapToString(bulkItem));
            bulkRequestBody.append("\n");
        }

        ArrayList<String> postBody = new ArrayList<>();
        postBody.add(bulkRequestBody.toString());
        try {
            elasticsearchResponse = new JSONObject(RestClient.postCall(url, postBody));
        } catch (ClientProtocolException e) {

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return elasticsearchResponse;
    }
}