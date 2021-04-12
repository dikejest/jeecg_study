package org.jeecg.modules.communityOrder.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.math.RandomUtils;
import org.jeecg.modules.communityOrder.entity.CommunityOrder;
import org.jeecg.modules.communityOrder.mapper.CommunityOrderMapper;
import org.jeecg.modules.communityOrder.service.ICommunityOrderService;
import org.jeecg.modules.communitySupplyQuantity.entity.CommunitySupplyQuantity;
import org.jeecg.modules.communitySupplyQuantity.service.ICommunitySupplyQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 疫情物资入库单
 * @Author: jeecg-boot
 * @Date:   2021-04-06
 * @Version: V1.0
 */
@Service
public class CommunityOrderServiceImpl extends ServiceImpl<CommunityOrderMapper, CommunityOrder> implements ICommunityOrderService {

    @Autowired
    ICommunityOrderService communityOrderService;

    @Autowired
    ICommunitySupplyQuantityService communitySupplyQuantityService;

    //物资入库单添加，添加完成同时要将数量加入对应的小区
    @Override
    public void addOrder(CommunityOrder communityOrder) {
        //自动生成入库单号
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        int random = RandomUtils.nextInt(90) + 10;
        Date date = new Date();
        String orderId = format.format(date)+random;
        //存入入库单号
        communityOrder.setOrderId(orderId);
        //根据部门即小区的org_code来查找对应的小区的库存，然后存入数据
        //CommunitySupplyQuantity quantity = communitySupplyQuantityService.getBySysOrgCode(communityOrder.getSysOrgCode());
        //根据小区id查找对应的小区
        CommunitySupplyQuantity quantity = communitySupplyQuantityService.getById(communityOrder.getAreaId());
        //小区属于的部门
        communityOrder.setSysOrgCode(quantity.getSysOrgCode());
        CommunitySupplyQuantity communitySupplyQuantity = account(communityOrder,quantity,true);
        communityOrderService.save(communityOrder);
        communitySupplyQuantityService.updateById(communitySupplyQuantity);
    }

    @Override
    public void updateOrder(CommunityOrder communityOrder) {
        //编辑物资入库单，同时库存要进行改变,先减掉订单数量在加
        //根据入库单号查找旧的库存的数量
        QueryWrapper<CommunityOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", communityOrder.getOrderId()).last("limit 1");
        CommunityOrder one = communityOrderService.getOne(wrapper);
        //根据部门即小区的org_code来查找对应的小区的库存，然后存入数据
        CommunitySupplyQuantity quantity = communitySupplyQuantityService.getById(communityOrder.getAreaId());
        //先减掉订单信息，在添加更改的库存信息
        CommunitySupplyQuantity account = account(one, quantity, false);
        CommunitySupplyQuantity quantity1 = account(communityOrder, account, true);
        //存入数据库
        communityOrderService.updateById(communityOrder);
        communitySupplyQuantityService.updateById(quantity1);
    }

    //删除物资入库单，同时修改库存
    @Override
    public void removeOrder(String id) {
        CommunityOrder communityOrder = communityOrderService.getById(id);
        //根据部门即小区的org_code来查找对应的小区的库存，然后存入数据
        CommunitySupplyQuantity one = communitySupplyQuantityService.getById(communityOrder.getAreaId());
        CommunitySupplyQuantity quantity = account(communityOrder, one,false);
        //修改保存进小区库存
        communitySupplyQuantityService.updateById(quantity);
        //删除小区入库单
        communityOrderService.removeById(id);
    }

    //根据订单号查找对应的小区进行数量相+-的方法
    private CommunitySupplyQuantity account(CommunityOrder communityOrder,CommunitySupplyQuantity quantity,boolean flag) {
        //flag判断是+还是-,+:true,-：false;
        if (flag){
            //将数据进行+
            quantity.setAlcohol(quantity.getAlcohol() + communityOrder.getAlcohol());
            quantity.setMask(quantity.getMask() + communityOrder.getMask());
            quantity.setDisinfectant(quantity.getDisinfectant() + communityOrder.getDisinfectant());
            quantity.setGoggle(quantity.getGoggle() + communityOrder.getGoggle());
            quantity.setTemperatureGun(quantity.getTemperatureGun() + communityOrder.getTemperatureGun());
            quantity.setDisposableGloves(quantity.getDisposableGloves() + communityOrder.getDisposableGloves());
            quantity.setRubberGloves(quantity.getRubberGloves() + communityOrder.getRubberGloves());
        } else{
            //将数据进行-
            quantity.setAlcohol(quantity.getAlcohol() - communityOrder.getAlcohol());
            quantity.setMask(quantity.getMask() - communityOrder.getMask());
            quantity.setDisinfectant(quantity.getDisinfectant() - communityOrder.getDisinfectant());
            quantity.setGoggle(quantity.getGoggle() - communityOrder.getGoggle());
            quantity.setTemperatureGun(quantity.getTemperatureGun() - communityOrder.getTemperatureGun());
            quantity.setDisposableGloves(quantity.getDisposableGloves() - communityOrder.getDisposableGloves());
            quantity.setRubberGloves(quantity.getRubberGloves() - communityOrder.getRubberGloves());
        }
        return quantity;
    }

    //删除小区的所有物资入库单
    @Override
    public void removeAllOrder(String areaId) {
        QueryWrapper<CommunityOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("area_id",areaId);
        communityOrderService.remove(wrapper);
    }

    //批量删除小区的物资入库单
    @Override
    public void removeOrders(String ids) {
        String[] list = ids.split(",");
        for (String id : list) {
            this.communityOrderService.removeOrder(id);
        }
    }

}