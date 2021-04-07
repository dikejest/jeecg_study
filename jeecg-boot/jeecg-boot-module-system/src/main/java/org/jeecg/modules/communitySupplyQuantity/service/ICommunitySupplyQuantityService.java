package org.jeecg.modules.communitySupplyQuantity.service;

import org.jeecg.modules.communitySupplyQuantity.entity.CommunitySupplyQuantity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 疫情物资库存表
 * @Author: jeecg-boot
 * @Date:   2021-04-06
 * @Version: V1.0
 */
public interface ICommunitySupplyQuantityService extends IService<CommunitySupplyQuantity> {

    //根据部门编码获取小区的库存信息
    CommunitySupplyQuantity getBySysOrgCode(String sysOrgCode);
    //添加新的小区库存记录，不允许添加已经存在的小区，第一次添加同时对应生成小区的入库登记单
    void addNewCommunity(CommunitySupplyQuantity communitySupplyQuantity);
}
