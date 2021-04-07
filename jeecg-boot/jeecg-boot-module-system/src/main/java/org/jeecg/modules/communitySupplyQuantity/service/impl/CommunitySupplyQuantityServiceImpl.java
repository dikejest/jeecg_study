package org.jeecg.modules.communitySupplyQuantity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.math.RandomUtils;
import org.jeecg.modules.communityOrder.entity.CommunityOrder;
import org.jeecg.modules.communityOrder.service.ICommunityOrderService;
import org.jeecg.modules.communitySupplyQuantity.entity.CommunitySupplyQuantity;
import org.jeecg.modules.communitySupplyQuantity.mapper.CommunitySupplyQuantityMapper;
import org.jeecg.modules.communitySupplyQuantity.service.ICommunitySupplyQuantityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 疫情物资库存表
 * @Author: jeecg-boot
 * @Date:   2021-04-06
 * @Version: V1.0
 */
@Service
public class CommunitySupplyQuantityServiceImpl extends ServiceImpl<CommunitySupplyQuantityMapper, CommunitySupplyQuantity> implements ICommunitySupplyQuantityService {


    @Autowired
    ICommunitySupplyQuantityService communitySupplyQuantityService;

    @Autowired
    ICommunityOrderService communityOrderService;

    //根据部门编码获取小区的库存信息
    @Override
    public CommunitySupplyQuantity getBySysOrgCode(String sysOrgCode) {
        QueryWrapper<CommunitySupplyQuantity> wrapper =new QueryWrapper<>();
        //只查询1条
        wrapper.eq("sys_org_code",sysOrgCode).last("limit 1");
        CommunitySupplyQuantity communitySupplyQuantity = this.baseMapper.selectOne(wrapper);
        return communitySupplyQuantity;
    }

    //添加新的小区库存记录，不允许添加已经存在的小区，第一次添加同时对应生成小区的入库登记单
    @Override
    public void addNewCommunity(CommunitySupplyQuantity communitySupplyQuantity) {
        //添加进数据库
        communitySupplyQuantityService.save(communitySupplyQuantity);
       //生成入库登记单
        CommunityOrder communityOrder = new CommunityOrder();
        //copy字段
        communityOrder.setSysOrgCode(communitySupplyQuantity.getSysOrgCode());
        //新增入库登记单
        BeanUtils.copyProperties(communitySupplyQuantity,communityOrder);
        //自动生成入库单号
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        int random = RandomUtils.nextInt(90) + 10;
        Date date = new Date();
        String orderId = format.format(date)+random;
        //存入入库单号
        communityOrder.setOrderId(orderId);
        communityOrder.setRemark("第一次添加");
        communityOrderService.save(communityOrder);
    }
}
