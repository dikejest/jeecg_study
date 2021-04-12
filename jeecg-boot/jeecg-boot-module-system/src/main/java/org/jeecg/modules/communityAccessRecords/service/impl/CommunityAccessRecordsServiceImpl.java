package org.jeecg.modules.communityAccessRecords.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.api.dto.CommunityAccessRecordsDto;
import org.jeecg.modules.api.vo.CommunityAccessRecordsVo;
import org.jeecg.modules.api.vo.KeyWords;
import org.jeecg.modules.communityAccessRecords.entity.CommunityAccessRecords;
import org.jeecg.modules.communityAccessRecords.mapper.CommunityAccessRecordsMapper;
import org.jeecg.modules.communityAccessRecords.service.ICommunityAccessRecordsService;
import org.jeecg.modules.communitySupplyQuantity.entity.CommunitySupplyQuantity;
import org.jeecg.modules.communitySupplyQuantity.service.ICommunitySupplyQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 小区出入登记记录表
 * @Author: jeecg-boot
 * @Date:   2021-04-07
 * @Version: V1.0
 */
@Service
public class CommunityAccessRecordsServiceImpl extends ServiceImpl<CommunityAccessRecordsMapper, CommunityAccessRecords> implements ICommunityAccessRecordsService {

    @Autowired
    ICommunityAccessRecordsService recordsService;

    @Autowired
    ICommunitySupplyQuantityService communitySupplyQuantityService;
    //判断最新的一条记录是否和出入小区相反，如果相同则不给通过
    @Override
    public CommunityAccessRecords queryFlagByCardId(String idNo) {
        QueryWrapper<CommunityAccessRecords> wrapper = new QueryWrapper<>();
        wrapper.eq("id_no",idNo).orderByDesc("create_time").last("limit 1");
        CommunityAccessRecords one = recordsService.getOne(wrapper);
        return one;
    }

    public boolean addRecords(CommunityAccessRecordsVo recordsVo) {
        CommunityAccessRecords communityAccessRecords =new CommunityAccessRecords();
        BeanUtil.copyProperties(recordsVo, communityAccessRecords);
        //存入当前小区的org_code
        CommunitySupplyQuantity area = communitySupplyQuantityService.getById(recordsVo.getAreaId());
        communityAccessRecords.setSysOrgCode(area.getSysOrgCode());
        //获取当前时间
        Long now = System.currentTimeMillis();
        Date date = new Date();
        date.setTime(now);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = format.format(date);
        try {
            Date date1 = format.parse(format1);
            communityAccessRecords.setCreateTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //调用方法添加记录
        boolean save = recordsService.save(communityAccessRecords);
        return save;
    }

    //根据关键词查询小区的出入记录
    @Override
    public Map queryRecords(KeyWords keyWords,Integer pageNo , Integer pageSize) {
        QueryWrapper<CommunityAccessRecords> wrapper = new QueryWrapper();
        wrapper.eq("area_id",keyWords.getAreaId());
        if (keyWords.getIdNo()!=null && !keyWords.getIdNo().equals("")){
            wrapper.eq("id_no",keyWords.getIdNo());
        }
        if (keyWords.getName()!=null && !keyWords.getName().equals("")){
            wrapper.like("name",keyWords.getName());
        }
        if (keyWords.getTemperature()!= 0 ){
            wrapper.eq("temperature",keyWords.getTemperature());
        }
        if (keyWords.getTemperature()!= 0 ){
            wrapper.eq("is_open_door",keyWords.getIsOpenDoor());
        }
        if (keyWords.getMobile()!=null && !keyWords.getMobile().equals("")){
            wrapper.eq("mobile",keyWords.getMobile());
        }
        if (keyWords.getBeginTime()!=null || keyWords.getEndTime()!=null){
            if (!keyWords.getBeginTime().equals("") && !keyWords.getEndTime().equals("")){
                wrapper.between("create_time",keyWords.getBeginTime(),keyWords.getEndTime());
            }
            if (!keyWords.getBeginTime().equals("")){
                wrapper.ge("create_time",keyWords.getBeginTime());
            }
            if (!keyWords.getEndTime().equals("")){
                wrapper.le("create_time",keyWords.getEndTime());
            }
        }
        wrapper.orderByDesc("create_time");
        Page<CommunityAccessRecords> page = new Page<CommunityAccessRecords>(pageNo, pageSize);
        IPage<CommunityAccessRecords> pageList = recordsService.page(page, wrapper);
        List<CommunityAccessRecords> records = pageList.getRecords();
        List<CommunityAccessRecordsDto> dtoList = new ArrayList<>();
        //遍历添加进dto，返回
        records.forEach(e->{
            CommunityAccessRecordsDto recordsDto = new CommunityAccessRecordsDto();
            BeanUtil.copyProperties(e,recordsDto);
            dtoList.add(recordsDto);
        });
        Map data = new HashMap();
        data.put("pageNo",pageNo);
        data.put("pageSize",pageSize);
        data.put("total",page.getTotal());
        data.put("data",dtoList);
        return data;
    }
}
