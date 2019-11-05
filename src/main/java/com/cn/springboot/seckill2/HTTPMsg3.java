package com.cn.springboot.seckill2;

import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @Author: lipeng
 * @CreateDate: 2019/11/1$ 9:47$
 * @Version: 1.0
 */
public class HTTPMsg3 {
    public static void main(String[] args) throws Exception {
        String url = "http://20.26.52.82:8080/email/msg";
        String params = "{\"toAddressId\": 34,\"title\": \"测试\", \"content\": \"参数值\", \"type\": \"email\" }";
//        JSONObject json = JSONObject.fromObject(params);
        JSONObject result = doPost(url, params);
        System.out.println(result);
    }

    /**
     * post请求
     *
     * @param url
     * @param
     * @return
     */
    public static JSONObject doPost(String url, String params) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(params);
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
//                response = JSONObject.fromObject(result);
                System.out.println(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}

