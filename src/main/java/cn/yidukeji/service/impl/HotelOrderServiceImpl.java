package cn.yidukeji.service.impl;

import cn.yidukeji.bean.Hotel;
import cn.yidukeji.bean.Rooms;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;
import cn.yidukeji.persistence.HotelMapper;
import cn.yidukeji.service.HotelOrderService;
import cn.yidukeji.utils.PropertiesUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-14
 * Time: 下午4:39
 * To change this template use File | Settings | File Templates.
 */
@Service
public class HotelOrderServiceImpl implements HotelOrderService {

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private HotelMapper hotelMapper;

    @Override
    public Paginator search(String cityName, String startDate, String endDate, Integer priceClass, String keyword, Paginator paginator) throws ApiException {
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
        formparams.add(new BasicNameValuePair("cityname", cityName));
        formparams.add(new BasicNameValuePair("startdate", startDate));
        formparams.add(new BasicNameValuePair("enddate", endDate));
        if(priceClass != null){
            formparams.add(new BasicNameValuePair("priceClass", priceClass.toString()));
        }
        formparams.add(new BasicNameValuePair("keyword", keyword));
        formparams.add(new BasicNameValuePair("pagenumber", String.valueOf(paginator.getPageNum())));
        formparams.add(new BasicNameValuePair("pagesize", String.valueOf(paginator.getPageSize())));
        HttpPost post = new HttpPost(url);
        try{
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.setEntity(entity);
            HttpResponse response = hc.execute(post);
            if(response.getStatusLine().getStatusCode() != 200){
                logger.error("公司用户帐号生成 调用接口错误 返回 status：" + response.getStatusLine().getStatusCode());
                throw new ApiException("生成用户账号错误，请联系管理员", 500);
            }
            HttpEntity entity1 = response.getEntity();
            String str = EntityUtils.toString(entity1);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(str, Map.class);
            Map<String, Object> m = (Map<String, Object>)map.get("status");
            if(!"0".equals(m.get("errorno").toString())){
                logger.error("公司用户帐号生成 调用接口错误 errorcode：" + m.get("errorcode"));
                throw new ApiException("生成用户账号错误，请联系管理员", 500);
            }
            List<Map<String, Object>> data = (List<Map<String, Object>>)map.get("data");
            paginator.setResults(data);
        }catch (IOException e){
            e.printStackTrace();
        }
        return paginator;
    }

    @Override
    public Rooms getRooms(Integer id) {
        return hotelMapper.getRooms(id);
    }

    @Override
    public Hotel getHotel(String id) {
        String[] arg = id.split("_");
        if(arg.length == 2){
            String mark = arg[0];
            Integer hotelId = Integer.valueOf(arg[1]);
            if("1".equals(mark)){
                return hotelMapper.getCtrip(hotelId);
            }else if("2".equals(mark)){
                return hotelMapper.getElong(hotelId);
            }else if("3".equals(mark)){
                return hotelMapper.getU17(hotelId);
            }
        }
        return null;
    }
}
