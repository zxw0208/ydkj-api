package cn.yidukeji.controller;

import cn.yidukeji.utils.HMACUtils;
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
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-8
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 */
public class DepartmentControllerTest {

    @Test
    public void add() throws Exception {
        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        DefaultHttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("accessKeyId", "aaa"));
        formparams.add(new BasicNameValuePair("description", "abc"));
        formparams.add(new BasicNameValuePair("expires", String.valueOf(System.currentTimeMillis()/1000)));

        formparams.add(new BasicNameValuePair("name", "abc"));
        formparams.add(new BasicNameValuePair("parentId", "3"));
        String signature = HMACUtils.sha265("2014", URLEncodedUtils.format(formparams, "UTF-8"));
        formparams.add(new BasicNameValuePair("signature", signature));
        HttpPost post = new HttpPost("http://localhost:8080/ydkj/department/add");
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        post.setEntity(entity);

        HttpResponse response = hc.execute(targetHost, post);
        HttpEntity entity1 = response.getEntity();
        String str = EntityUtils.toString(entity1);
        System.out.println(str);
    }

    @Test
    public void update() throws Exception {
        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        DefaultHttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("accessKeyId", "aaa"));

        formparams.add(new BasicNameValuePair("expires", String.valueOf(System.currentTimeMillis()/1000)));
        //formparams.add(new BasicNameValuePair("id", "3"));
        formparams.add(new BasicNameValuePair("name", "rrr"));
        formparams.add(new BasicNameValuePair("parentId", "4"));
        String signature = HMACUtils.sha265("2014", URLEncodedUtils.format(formparams, "UTF-8"));
        formparams.add(new BasicNameValuePair("signature", signature));
        HttpPost post = new HttpPost("http://localhost:8080/ydkj/department/update");
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        post.setEntity(entity);

        HttpResponse response = hc.execute(targetHost, post);
        HttpEntity entity1 = response.getEntity();
        String str = EntityUtils.toString(entity1);
        System.out.println(str);
    }

    @Test
    public void get() throws Exception {
        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        DefaultHttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("accessKeyId", "aaa"));
        formparams.add(new BasicNameValuePair("expires", String.valueOf(System.currentTimeMillis()/1000)));
        formparams.add(new BasicNameValuePair("id", "3"));
        String signature = HMACUtils.sha265("2014", URLEncodedUtils.format(formparams, HTTP.UTF_8));
        formparams.add(new BasicNameValuePair("signature", signature));
        HttpGet post = new HttpGet("http://localhost:8080/ydkj/department/get?" + URLEncodedUtils.format(formparams, HTTP.UTF_8));
        HttpResponse response = hc.execute(targetHost, post);
        HttpEntity entity1 = response.getEntity();
        String str = EntityUtils.toString(entity1);
        System.out.println(str);
    }

    @Test
    public void delete() throws Exception {
        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        DefaultHttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("accessKeyId", "aaa"));

        formparams.add(new BasicNameValuePair("expires", String.valueOf(System.currentTimeMillis()/1000)));
        formparams.add(new BasicNameValuePair("id", "5"));
        String signature = HMACUtils.sha265("2014", URLEncodedUtils.format(formparams, "UTF-8"));
        formparams.add(new BasicNameValuePair("signature", signature));
        HttpPost post = new HttpPost("http://localhost:8080/ydkj/department/delete");
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        post.setEntity(entity);

        HttpResponse response = hc.execute(targetHost, post);
        HttpEntity entity1 = response.getEntity();
        String str = EntityUtils.toString(entity1);
        System.out.println(str);
    }

    @Test
    public void list() throws Exception {
        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        DefaultHttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("accessKeyId", "aaa"));
        formparams.add(new BasicNameValuePair("expires", String.valueOf(System.currentTimeMillis()/1000)));
        //formparams.add(new BasicNameValuePair("id", "3"));
        String signature = HMACUtils.sha265("2014", URLEncodedUtils.format(formparams, HTTP.UTF_8));
        formparams.add(new BasicNameValuePair("signature", signature));
        HttpGet post = new HttpGet("http://localhost:8080/ydkj/department/list?" + URLEncodedUtils.format(formparams, HTTP.UTF_8));
        HttpResponse response = hc.execute(targetHost, post);
        HttpEntity entity1 = response.getEntity();
        String str = EntityUtils.toString(entity1);
        System.out.println(str);
    }

}
