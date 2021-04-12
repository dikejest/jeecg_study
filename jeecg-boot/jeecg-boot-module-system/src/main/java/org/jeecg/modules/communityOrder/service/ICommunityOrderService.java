package org.jeecg.modules.communityOrder.service;

import org.jeecg.modules.communityOrder.entity.CommunityOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.communitySupplyQuantity.entity.CommunitySupplyQuantity;

/**
 * @Description: 疫情物资入库单
 * @Author: jeecg-boot
 * @Date:   2021-04-06
 * @Version: V1.0
 */
public interface ICommunityOrderService extends IService<CommunityOrder> {

    //物资入库单添加，添加完成同时要将数量加入对应的小区
    void addOrder(CommunityOrder communityOrder);

    //编辑物资入库单，同时库存要进行改变
    void updateOrder(CommunityOrder communityOrder);

    //删除物资入库单，同时修改库存
    void removeOrder(String id);

    //删除小区的所有物资入库单
    void removeAllOrder(String areaId);

    //批量删除小区的物资入库单
    void removeOrders(String ids);
}
