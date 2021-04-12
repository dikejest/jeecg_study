package org.jeecg.modules.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ：hyj
 * @description：
 * @date ：Created in 2021/4/9 9:14
 */
@Data
public class KeyWords {
    /**小区id*/
    @ApiModelProperty(value = "所属小区id,必填",required = true)
    private String areaId;
    /**
     * 名字
     */
    @ApiModelProperty(value = "名字,模糊查询",required = false)
    private String name;
    /**
     * 温度
     */
    @ApiModelProperty(value = "温度,1:代表37.2°以及以下，2:代表37.2°以上,0查全部",required = false)
    private Integer temperature;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号，精准匹配",required = false)
    private String idNo;
    /**手机号*/
    @ApiModelProperty(value = "手机号，精准匹配",required = false)
    private String mobile;
    /**
     * 进门 or 出门
     */
    @ApiModelProperty(value = "进门:1 or 出门:2,查询全部: 0",required = false)
    private Integer isOpenDoor;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "查询开始时间，请求格式yyyy-MM-dd HH:mm:ss",required = false)
    private Date beginTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "查询结束时间,请求格式yyyy-MM-dd HH:mm:ss",required = false)
    private Date endTime;


}
