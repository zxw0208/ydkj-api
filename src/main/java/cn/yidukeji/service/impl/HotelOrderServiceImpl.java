package cn.yidukeji.service.impl;

import cn.yidukeji.core.Paginator;
import cn.yidukeji.service.HotelOrderService;
import cn.yidukeji.utils.PropertiesUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-14
 * Time: 下午4:39
 * To change this template use File | Settings | File Templates.
 */
@Service
public class HotelOrderServiceImpl implements HotelOrderService {

    @Override
    public Paginator search(String cityName, String startDate, String endDate, Integer priceClass, String keyword, Paginator paginator) {
        String url = PropertiesUtils.getProperty("hotel.search.api");
        DefaultHttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        String token = "OPEN API - HOTEL";
        Integer timestamp = (int)(System.currentTimeMillis()/1000);
        String nonce = String.valueOf((int)(Math.random()*100000));
        String signature = DigestUtils.md5Hex(token + timestamp + nonce);
        formparams.add(new BasicNameValuePair("signature", signature));
        formparams.add(new BasicNameValuePair("timestamp", timestamp.toString()));
        formparams.add(new BasicNameValuePair("nonce", nonce));
        return null;
    }
}
