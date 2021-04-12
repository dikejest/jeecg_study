package org.jeecg.modules.communityAccessRecords.service;

import org.jeecg.modules.api.vo.CommunityAccessRecordsVo;
import org.jeecg.modules.api.vo.KeyWords;
import org.jeecg.modules.communityAccessRecords.entity.CommunityAccessRecords;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 小区出入登记记录表
 * @Author: jeecg-boot
 * @Date:   2021-04-07
 * @Version: V1.0
 */
public interface ICommunityAccessRecordsService extends IService<CommunityAccessRecords> {

    //判断最新的一条记录是否和出入小区相反，如果相同则不给通过
    CommunityAccessRecords queryFlagByCardId(String idNo);

    boolean addRecords(CommunityAccessRecordsVo recordsVo);

    //根据关键词查询小区的出入记录
     Map queryRecords(KeyWords keyWords,Integer pageNo , Integer pageSize);
}
