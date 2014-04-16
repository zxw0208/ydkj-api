package cn.yidukeji.service.impl;

import cn.yidukeji.bean.AccessUser;
import cn.yidukeji.bean.Department;
import cn.yidukeji.bean.User;
import cn.yidukeji.core.Paginator;
import cn.yidukeji.exception.ApiException;
import cn.yidukeji.persistence.UserMapper;
import cn.yidukeji.service.DepartmentService;
import cn.yidukeji.service.UserService;
import cn.yidukeji.utils.AccessUserHolder;
import cn.yidukeji.utils.IdCardUtils;
import cn.yidukeji.utils.PropertiesUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-9
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentService departmentService;

    @Override @Transactional
    public int addUser(User user) throws ApiException {
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        user.setCompanyId(accessUser.getCompanyId());
        user.setCtime((int)(System.currentTimeMillis()/1000));
        user.setStatus(0);
        user.setSex(IdCardUtils.sex(user.getIdentification()));
        if(StringUtils.isNotBlank(user.getPasswd())){
            user.setPasswd(DigestUtils.md5Hex(user.getPasswd()));
        }else{
            user.setPasswd(DigestUtils.md5Hex("nopassword"));
        }
        if(user.getRole() == null){
            user.setRole(11);
        }else if(user.getRole() != 11 && user.getRole() != 10 ){
            user.setRole(11);
        }
        Department d = departmentService.getDepartmentById(user.getDepartmentId(), accessUser.getCompanyId());
        if(d == null){
            throw new ApiException("部门不存在", 400);
        }
        if(userMapper.isUnique(user.getMobile(), null, null) > 0){
            throw new ApiException("手机号已存在", 400);
        }
        if(userMapper.isUnique(null, user.getEmail(), null) > 0){
            throw new ApiException("电子邮件已存在", 400);
        }
        try {
            user.setAccount(generateAccount(accessUser.getCompanyId(), d.getId()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiException("用户帐号生成异常，请联系管理员", 500);
        }
        if(userMapper.isUnique(null, null, user.getAccount()) > 0){
            throw new ApiException("账户号已存在", 400);
        }
        return userMapper.insertUser(user);
    }

    @Override @Transactional
    public int updateUser(User user) throws ApiException {
        if(user.getId() == null){
            throw new ApiException("id不能为空", 400);
        }
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        User u = getUser(user.getId(), accessUser.getCompanyId());
        if(user.getMobile() != null && !u.getMobile().equals(user.getMobile()) && userMapper.isUnique(user.getMobile(), null, null) > 0){
            throw new ApiException("手机号已存在", 400);
        }else if(user.getEmail() != null && !u.getEmail().equals(user.getEmail()) && userMapper.isUnique(null, user.getEmail(), null) > 0){
            throw new ApiException("电子邮件已存在", 400);
        }
        if(user.getDepartmentId() != null){
            Department d = departmentService.getDepartmentById(user.getDepartmentId(), accessUser.getCompanyId());
            if(d == null){
                throw new ApiException("部门不存在", 400);
            }
        }
        if(StringUtils.isNotBlank(user.getPasswd())){
            user.setPasswd(DigestUtils.md5Hex(user.getPasswd()));
        }
        user.setCompanyId(accessUser.getCompanyId());
        user.setStatus(null);
        return userMapper.updateUser(user);
    }

    @Override
    public User getUser(Integer userId, Integer companyId) {
        return userMapper.getUserById(userId, companyId);
    }

    @Override
    public User getUser(String account, Integer companyId) {
        if(account.indexOf("@") > 0){
            return userMapper.getUser(companyId, account, null);
        }else{
            return userMapper.getUser(companyId, null, account);
        }
    }

    @Override @Transactional
    public int delUser(Integer id) throws ApiException {
        if(id == null){
            throw new ApiException("id不能为空", 400);
        }
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        User user = new User();
        user.setCompanyId(accessUser.getCompanyId());
        user.setId(id);
        user.setStatus(3);
        return userMapper.updateUser(user);
    }

    @Override
    public Paginator userList(Paginator paginator) {
        AccessUser accessUser = AccessUserHolder.getAccessUser();
        List<User> list = userMapper.findUserList(accessUser.getCompanyId(), paginator.getFirstResult(), paginator.getMaxResults());
        paginator.setResults(list);
        int count = userMapper.findUserListCount(accessUser.getCompanyId());
        paginator.setTotalCount(count);
        return paginator;
    }

    private  String generateAccount(Integer companyId, Integer departmentId) throws IOException, ApiException {
        String url = PropertiesUtils.getProperty("account.generate.api");
        DefaultHttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        String token = "OPEN API - USER - ACCOUNT";
        Integer timestamp = (int)(System.currentTimeMillis()/1000);
        String nonce = String.valueOf((int)(Math.random()*100000));
        String signature = DigestUtils.md5Hex(token + timestamp + nonce);
        formparams.add(new BasicNameValuePair("signature", signature));
        formparams.add(new BasicNameValuePair("timestamp", timestamp.toString()));
        formparams.add(new BasicNameValuePair("nonce", nonce));
        formparams.add(new BasicNameValuePair("company_id", companyId.toString()));
        formparams.add(new BasicNameValuePair("department_id", departmentId.toString()));
        HttpPost post = new HttpPost(url);
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

        return map.get("account").toString();
    }
}
