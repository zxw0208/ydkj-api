package cn.yidukeji.service.impl;

import cn.yidukeji.persistence.CityMapper;
import cn.yidukeji.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-15
 * Time: 上午11:51
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityMapper cityMapper;

    @Override
    public List<String> getCityList() {
        return cityMapper.getCityList();
    }
}
