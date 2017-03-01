/**
 * 使用HTTP协议发送请求工具类
 * 
 * @title: HttpClientUtil.java
 * @author 邢治理
 * @date 2016年7月11日
 */
package com.hshc.sale.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 使用HTTP协议发送请求工具类
 * 
 * @className HttpClientUtil
 * @author 邢治理
 * @version V1.0 2016年7月11日
 */
public class HttpClientUtil {

    // 默认编码格式 utf-8
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    // 默认socket连接超时毫秒值 6000
    private static final int DEFAULT_SOCKET_TIMEOUT = 6000;
    // 默认连接超时毫秒值 6000
    private static final int DEFAULT_CONN_TIMEOUT = 6000;
    // 默认连接请求超时毫秒值6000
    private static final int DEFAULT_REQ_TIMEOUT = 6000;

    private HttpClientUtil() {
        // 禁止构造
    }

    /**
     * http get方式请求，采用UTF-8编码
     * 
     * @param uri 请求uri
     * @param jsonStr 请求json串
     * @return 响应字符串
     * @throws Exception
     */
    public static String httpGetRequest(String uri, String jsonStr) throws Exception {
        return httpGetRequest(uri, jsonStr, null);
    }

    /**
     * http get方式请求
     * 
     * @param uri 请求uri
     * @param jsonStr 请求json串
     * @param charset 编码格式
     * @return 响应字符串
     * @throws Exception
     */
    public static String httpGetRequest(String uri, String jsonStr, Charset charset) throws Exception {
        return innerSendRequest(uri, jsonStr, charset, "get");
    }

    /**
     * http post方式请求，采用UTF-8编码
     * 
     * @param uri 请求uri
     * @param jsonStr 请求json串
     * @param charset 编码格式
     * @return 响应字符串
     * @throws Exception
     */
    public static String httpPostRequest(String uri, String jsonStr) throws Exception {
        return httpPostRequest(uri, jsonStr, null);
    }

    /**
     * http post方式请求
     * 
     * @param uri 请求uri
     * @param jsonStr 请求json串
     * @param charset 编码格式
     * @return 响应字符串
     * @throws Exception
     */
    public static String httpPostRequest(String uri, String jsonStr, Charset charset) throws Exception {
        return innerSendRequest(uri, jsonStr, charset, "post");
    }

    private static String innerSendRequest(String uri, String jsonStr, Charset charset, String reqMethod) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse resp = null;
        URIBuilder ub = null;
        try {
            httpClient = HttpClients.createDefault();
            ub = new URIBuilder(uri);
            ub.setCharset(charset == null ? DEFAULT_CHARSET : charset);

            // 组装请求
            if ("get".equals(reqMethod)) {
                if (!StringUtils.isEmpty(jsonStr)) {
                    ub.addParameter("json", jsonStr);
                }
                HttpGet getReq = new HttpGet(ub.build());
                // 设置超时时间
                getReq.setConfig(RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONN_TIMEOUT)
                    .setConnectionRequestTimeout(DEFAULT_REQ_TIMEOUT).build());
                resp = httpClient.execute(getReq);
            } else if ("post".equals(reqMethod)) {
                HttpPost postReq = new HttpPost(ub.build());
                // 设置超时时间
                postReq.setConfig(RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONN_TIMEOUT)
                    .setConnectionRequestTimeout(DEFAULT_REQ_TIMEOUT).build());
                // 组装表单参数
                List<NameValuePair> formData = new ArrayList<NameValuePair>();
                formData.add(new BasicNameValuePair("json", jsonStr));
                postReq.setEntity(new UrlEncodedFormEntity(formData, charset == null ? DEFAULT_CHARSET : charset));
                // postReq.setEntity(new StringEntity(jsonStr, charset == null ? DEFAULT_CHARSET : charset));
                resp = httpClient.execute(postReq);
            } else {
                throw new Exception("请求类型不支持！(get|post)");
            }

            // 处理响应
            if (resp.getStatusLine().getStatusCode() == 200) {
                HttpEntity content = resp.getEntity();
                if (content != null) {
                    return EntityUtils.toString(content, charset);
                }
                return null;
            } else {
                throw new Exception("请求失败原因：" + resp.getStatusLine().getReasonPhrase());
            }
        } finally {
            ub = null;
            HttpClientUtils.closeQuietly(resp);
            HttpClientUtils.closeQuietly(httpClient);
        }
    }

    public static void main(String[] args) throws Exception {
        String msg = HttpClientUtil.httpGetRequest("https://www.baidu.com", null, Charset.forName("GBK"));
        System.out.println(msg);
    }
}
