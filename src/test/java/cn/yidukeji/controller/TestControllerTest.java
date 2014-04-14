package cn.yidukeji.controller;

import cn.yidukeji.utils.HMACUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-2
 * Time: 下午3:11
 * To change this template use File | Settings | File Templates.
 */
public class TestControllerTest {

    @Test
    public void test() throws IOException {
        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        DefaultHttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("accessKeyId", "aaa"));
        formparams.add(new BasicNameValuePair("expires", String.valueOf(System.currentTimeMillis()/1000)));
        String signature = HMACUtils.sha265("2014", URLEncodedUtils.format(formparams, HTTP.UTF_8));
        formparams.add(new BasicNameValuePair("signature", signature));
        HttpGet post = new HttpGet("http://localhost:8080/ydkj/test?" + URLEncodedUtils.format(formparams, HTTP.UTF_8));
        HttpResponse response = hc.execute(targetHost, post);
        HttpEntity entity1 = response.getEntity();
        String str = EntityUtils.toString(entity1);
        System.out.println(str);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(str, Map.class);

        Object code = map.get("code");
        Assert.assertEquals(200, code);
    }

    @Test
    public void aa() throws IOException {
        DefaultHttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        String token = "OPEN API - USER - ACCOUNT";
        Integer timestamp = (int)(System.currentTimeMillis()/1000);
        String nonce = String.valueOf((int)(Math.random()*100000));
        String signature = DigestUtils.md5Hex(token + timestamp + nonce);
        formparams.add(new BasicNameValuePair("signature", signature));
        formparams.add(new BasicNameValuePair("timestamp", timestamp.toString()));
        formparams.add(new BasicNameValuePair("nonce", nonce));
        formparams.add(new BasicNameValuePair("company_id", "1"));
        formparams.add(new BasicNameValuePair("department_id", "1"));
        HttpPost post = new HttpPost("http://business.iuwhy.com/open/user/account.html");
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        post.setEntity(entity);

        HttpResponse response = hc.execute(post);
        HttpEntity entity1 = response.getEntity();
        String str = EntityUtils.toString(entity1);
        System.out.println(str);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(str, Map.class);
        Map<String, Object> m = (Map<String, Object>)map.get("status");
        System.out.println(m.get("errorno"));
        System.out.println(m.get("errorcode"));
        System.out.println(map.get("account"));
    }
}
