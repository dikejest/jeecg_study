package org.jeecg.modules.communitySupplyQuantity.service;

import org.jeecg.modules.communitySupplyQuantity.entity.CommunitySupplyQuantity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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

    //记录变化了多少，生成一个入库登记记录
    Boolean addOrderByEdit(CommunitySupplyQuantity communitySupplyQuantity);

    //查询所有小区id，条件部门编码筛选,然后已text：value的形式显示出来
    List<Map<String, String>> queryArea(String orgCode);

    //批量删除小区库存
    void removeCommunitys(String ids);
}
