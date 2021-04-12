package org.jeecg.modules.communitySupplyQuantity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.math.RandomUtils;
import org.jeecg.modules.communityOrder.entity.CommunityOrder;
import org.jeecg.modules.communityOrder.service.ICommunityOrderService;
import org.jeecg.modules.communitySupplyQuantity.entity.CommunitySupplyQuantity;
import org.jeecg.modules.communitySupplyQuantity.mapper.CommunitySupplyQuantityMapper;
import org.jeecg.modules.communitySupplyQuantity.service.ICommunitySupplyQuantityService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartPermissionService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    ISysDepartService departService;
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
        //待完善 TODO 改成直接获得sys_org_code,而不是在去查询
        SysDepart depart = departService.getById(communitySupplyQuantity.getSysOrgCode());
        communitySupplyQuantity.setSysOrgCode(depart.getOrgCode());
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
        //存入小区id
        communityOrder.setAreaId(communitySupplyQuantity.getId());
        communityOrder.setOrderId(orderId);
        communityOrder.setRemark("第一次添加");
        communityOrderService.save(communityOrder);
    }

    //记录变化了多少，生成一个入库登记记录
    @Override
    public Boolean addOrderByEdit(CommunitySupplyQuantity community) {
        //查询旧的库存
        CommunitySupplyQuantity quantity = communitySupplyQuantityService.getById(community.getId());
        //自动生成入库单号
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        int random = RandomUtils.nextInt(90) + 10;
        Date date = new Date();
        String orderId = format.format(date)+random;
        //创建订单
        CommunityOrder order = new CommunityOrder();
        //存入入库单号
        order.setSysOrgCode(community.getSysOrgCode());
        order.setOrderId(orderId);
        order.setAreaId(community.getId());
        order.setRemark("小区进行库调产生的记录");
        //进行相减
        order.setAlcohol(community.getAlcohol()-quantity.getAlcohol());
        order.setDisinfectant(community.getDisinfectant()-quantity.getDisinfectant());
        order.setDisposableGloves(community.getDisposableGloves()-quantity.getDisposableGloves());
        order.setGoggle(community.getGoggle()-quantity.getGoggle());
        order.setRubberGloves(community.getRubberGloves()-quantity.getRubberGloves());
        order.setMask(community.getMask()-quantity.getMask());
        order.setTemperatureGun(community.getTemperatureGun()-quantity.getTemperatureGun());
        //存入数据库
        return  communityOrderService.save(order);
    }

    //查询所有小区id，条件部门编码筛选,然后已text：value的形式显示出来
    @Override
    public List<Map<String, String>> queryArea(String orgCode) {
        QueryWrapper<CommunitySupplyQuantity> wrapper = new QueryWrapper<>();
        wrapper.likeRight("sys_org_code",orgCode).select("id","name");
        List<CommunitySupplyQuantity> list = communitySupplyQuantityService.list(wrapper);
        List<Map<String,String>> data = new ArrayList<>();
        list.forEach(e->{
            Map map = new HashMap();
            map.put("text",e.getName());
            map.put("value",e.getId());
            data.add(map);
        });
        return data;
    }

    //批量删除小区库存
    @Override
    public void removeCommunitys(String ids) {
        String[] areaIds = ids.split(",");
        for (String id : areaIds) {
            CommunitySupplyQuantity byId = communitySupplyQuantityService.getById(id);
            communitySupplyQuantityService.removeById(id);
            //同时删除小区对应的所有入库单
            communityOrderService.removeAllOrder(byId.getId());
        }
    }
}
