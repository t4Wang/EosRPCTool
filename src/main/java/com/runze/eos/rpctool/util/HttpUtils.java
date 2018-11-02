package com.runze.eos.rpctool.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.runze.eos.rpctool.exception.EosException;
import com.runze.eos.rpctool.exception.ErrorResponse;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    public static String post(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        return post(httpclient, httpPost);
    }
    /**
     * 发送json格式参数请求
     * @return
     */
    public static String post(String url, String jsonParams) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (jsonParams != null) {
            StringEntity stringEntity = new StringEntity(jsonParams, "utf-8");
            httpPost.setEntity(stringEntity);
        }

        return post(httpclient, httpPost);
    }

    public static Object post(String url, Map<String, String> paramPairs) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<>();
        // 遍历 paramPairs 获取键和值
        paramPairs.forEach((key, value) -> {
            nvps.add(new BasicNameValuePair(key, value));
        });

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps);
        httpPost.setEntity(formEntity);

        return post(httpclient, httpPost);
    }


    public static Object get() {
        return null;
    }

    private static String post(CloseableHttpClient httpclient, HttpPost httpPost) throws EosException, IOException {
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            HttpEntity entity = response.getEntity();
            String str = EntityUtils.toString(entity);
            if (statusCode / 100 != 2) {
                ErrorResponse resp = JSON.parseObject(str, new TypeReference<ErrorResponse>() {});
                throw new EosException(resp.getError().getCode() + " : " + resp.getError().getName() + "--" + resp.getError().getWhat());
            }

            EntityUtils.consume(entity);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            response.close();
        }
    }
}
