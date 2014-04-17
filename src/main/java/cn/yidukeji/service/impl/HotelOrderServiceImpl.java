package cn.yidukeji.service.impl;

import cn.yidukeji.bean.*;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;
import cn.yidukeji.persistence.CategoryMapper;
import cn.yidukeji.persistence.HotelMapper;
import cn.yidukeji.persistence.OrderedMapper;
import cn.yidukeji.service.DepartmentService;
import cn.yidukeji.service.HotelOrderService;
import cn.yidukeji.utils.AccessUserHolder;
import cn.yidukeji.utils.PropertiesUtils;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private OrderedMapper orderedMapper;

    @Autowired
    private CategoryMapper categoryMapper;

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
            Object t = map.get("total");
            if(t != null && NumberUtils.isNumber(t.toString())){
                Long total = Long.valueOf(t.toString());
                paginator.setTotalCount(total);
            }
            paginator.setResults(data);
        }catch (IOException e){
            e.printStackTrace();
        }
        return paginator;
    }

    @Override
    public Rooms getRooms(Integer id) {
        Rooms rooms = hotelMapper.getRooms(id);
        rooms.setRoomList(new ArrayList<Room>());
        String data = rooms.getData();
        String regEx = "(?<=\\[).*(?=\\])";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(data);
        while(mat.find()){
            String str = mat.group();
            Room room = new Room();
            String [] args = str.split(",");
            room.setRoomType(args[0].replaceAll("\"",""));
            room.setBedType(args[1].replaceAll("\"",""));
            room.setPrice(Double.valueOf(args[3].replaceAll("\"","")));
            room.setSettle(getSettle(room.getPrice()));
            room.setBreakfast(args[4].replaceAll("\"",""));
            room.setNote(args[5].replaceAll("\"",""));
            room.setIdentity(args[6].replaceAll("\"",""));
            rooms.getRoomList().add(room);
        }
        return rooms;
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

    @Override @Transactional
    public Ordered placeOrder(Integer goodsId, User user, List<Map<String, String>> clientList, Integer rooms, String startDate, String endDate, Long day, Integer ticket, String roomIdentity) throws ApiException {
        Ordered order = new Ordered();
        order.setCompanyId(user.getCompanyId());
        Category category = categoryMapper.getCategory("hotel");
        if(category == null){
            logger.error("酒店Category不存在");
            throw new ApiException("内部错误，请联系管理员", 500);
        }
        order.setCategoryId(category.getId());
        order.setClients(clientList.size());
        order.setDepartmentId(user.getDepartmentId());
        order.setGoodsId(goodsId);
        order.setUserId(user.getId());
        order.setStatus(0);
        order.setCtime((int)(System.currentTimeMillis()/1000));
        Map<String, Object> contentMap = new LinkedHashMap<String, Object>();
        Rooms rooms1 = getRooms(goodsId);
        Hotel hotel = getHotel(rooms1.getHotelId());
        Map<String, Object> hotelMap = new LinkedHashMap<String, Object>();
        hotelMap.put("name", hotel.getCname());
        hotelMap.put("address", hotel.getAddress());
        contentMap.put("hotel", hotelMap);
        Room room = null;
        for(Room r : rooms1.getRoomList()){
            if(roomIdentity.equals(r.getIdentity())){
                room = r;
                break;
            }
        }
        if(room == null){
            throw new ApiException("房型不存在", 400);
        }
        Map<String, Object> roomMap = new LinkedHashMap<String, Object>();
        roomMap.put("name", room.getRoomType());
        roomMap.put("bed", room.getBedType());
        roomMap.put("price", room.getPrice());
        roomMap.put("settle", room.getSettle());
        roomMap.put("eat", room.getBreakfast());
        roomMap.put("net", "免费");
        contentMap.put("room", roomMap);
        contentMap.put("clients", clientList);
        contentMap.put("rooms", rooms);
        contentMap.put("from", startDate);
        contentMap.put("to", endDate);
        contentMap.put("days", day);
        if(ticket == 1){
            contentMap.put("ticket", "需要发票");
        }else if(ticket == 0){
            contentMap.put("ticket", "不需要发票");
        }
        Map<String, Object> contactMap = new LinkedHashMap<String, Object>();
        contactMap.put("name", user.getName());
        contactMap.put("mobile", user.getMobile());
        Department department = departmentService.getDepartmentById(user.getDepartmentId(), user.getCompanyId());
        if(department != null){
            contactMap.put("department", department.getName());
        }
        contactMap.put("job", user.getJob());
        order.setTitle(hotel.getCname());
        order.setSettle(room.getSettle()*day*clientList.size());
        order.setAmount(room.getPrice()*day*clientList.size());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String str = mapper.writeValueAsString(contentMap);
            order.setContent(str);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        orderedMapper.insertOrder(order);
        return order;
    }

    @Override
    public Ordered getOrderById(Integer id, Integer companyId) {
        return orderedMapper.getOrderById(id, companyId);
    }

    @Override
    public Paginator getOrderList(Paginator paginator) {
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        List<Ordered> list = orderedMapper.findOrderList(accessUser.getCompanyId(), paginator.getFirstResult(), paginator.getMaxResults());
        paginator.setResults(list);
        int count = orderedMapper.findOrderListCount(accessUser.getCompanyId());
        paginator.setTotalCount(count);
        return paginator;
    }

    private Double getSettle(Double price){
        String rule = PropertiesUtils.getProperty("settle.rule");
        String[] args = rule.split(";");
        for(String s : args){
            String [] ss = s.split(":");
            double p = Double.parseDouble(ss[0]);
            double per = Double.parseDouble(ss[1]);
            double revise = Double.parseDouble(ss[2]);
            if(price < p || p == 0){
                return Math.floor(price * per / 100 / revise) * revise;
            }
        }
        return 0d;
    }
}
