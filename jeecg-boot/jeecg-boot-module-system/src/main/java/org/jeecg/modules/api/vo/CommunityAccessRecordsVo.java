package org.jeecg.modules.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：hyj
 * @description：
 * @date ：Created in 2021/4/8 14:05
 */

@Data
@TableName("community_access_records")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CommunityAccessRecordsVo对象", description = "小区出入登记记录")
public class CommunityAccessRecordsVo implements Serializable {

    /**小区id*/
    @ApiModelProperty(value = "所属小区id",required = true)
    private String areaId;
    /**
     * 名字
     */
    @ApiModelProperty(value = "名字",required = true)
    private String name;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号",required = true)
    private String mobile;
    /**
     * 门牌号
     */
    @ApiModelProperty(value = "门牌号，所在小区门牌号",required = true)
    private String doorNo;
    /**
     * 事由
     */
    @ApiModelProperty(value = "事由",required = false)
    private String course;
    /**
     * 温度
     */
    @ApiModelProperty(value = "温度,1:代表37.2°以及以下，2:代表37.2°以上",required = true)
    private Integer temperature;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号",required = true)
    private String idNo;
    /**
     * 进门 or 出门
     */
    @ApiModelProperty(value = "进门:1 or 出门:2",required = true)
    private Integer isOpenDoor;
}

