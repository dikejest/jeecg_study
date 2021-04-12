package org.jeecg.modules.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ：hyj
 * @description：
 * @date ：Created in 2021/4/9 9:57
 */
@Data
public class CommunityAccessRecordsDto {
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**名字*/
    @ApiModelProperty(value = "名字")
    private String name;
    /**手机号*/
    @ApiModelProperty(value = "手机号")
    private String mobile;
    /**门牌号*/
    @ApiModelProperty(value = "门牌号")
    private String doorNo;
    /**事由*/
    @ApiModelProperty(value = "事由")
    private String course;
    /**温度*/
    @ApiModelProperty(value = "温度")
    private Integer temperature;
    /**身份证号*/
    @Excel(name = "身份证号", width = 15)
    @ApiModelProperty(value = "身份证号")
    private String idNo;
    /**进门 or 出门*/
    @Dict(dicCode = "is_open_door")
    @ApiModelProperty(value = "进门 or 出门")
    private Integer isOpenDoor;
}
