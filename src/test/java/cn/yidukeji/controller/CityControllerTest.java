package cn.yidukeji.controller;

import cn.yidukeji.utils.HMACUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-15
 * Time: 上午11:55
 * To change this template use File | Settings | File Templates.
 */
public class CityControllerTest {

    @Test
    public void list() throws IOException {
        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        DefaultHttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("accessKeyId", "aaa"));
        formparams.add(new BasicNameValuePair("expires", String.valueOf(System.currentTimeMillis()/1000)));
        String signature = HMACUtils.sha265("2014", URLEncodedUtils.format(formparams, HTTP.UTF_8));
        formparams.add(new BasicNameValuePair("signature", signature));
        HttpGet post = new HttpGet("http://localhost:8080/ydkj/city/list?" + URLEncodedUtils.format(formparams, HTTP.UTF_8));
        HttpResponse response = hc.execute(targetHost, post);
        HttpEntity entity1 = response.getEntity();
        String str = EntityUtils.toString(entity1);
        System.out.println(str);
    }

}
