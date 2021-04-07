package org.jeecg.modules.communityAccessRecords.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 小区出入登记记录表
 * @Author: jeecg-boot
 * @Date:   2021-04-07
 * @Version: V1.0
 */
@Data
@TableName("community_access_records")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="community_access_records对象", description="小区出入登记记录表")
public class CommunityAccessRecords implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**名字*/
	@Excel(name = "名字", width = 15)
    @ApiModelProperty(value = "名字")
    private String name;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private Integer mobile;
	/**门牌号*/
	@Excel(name = "门牌号", width = 15)
    @ApiModelProperty(value = "门牌号")
    private String doorNo;
	/**事由*/
	@Excel(name = "事由", width = 15)
    @ApiModelProperty(value = "事由")
    private String course;
	/**温度*/
	@Excel(name = "温度", width = 15, dicCode = "temperature")
	@Dict(dicCode = "temperature")
    @ApiModelProperty(value = "温度")
    private Integer temperature;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
    @ApiModelProperty(value = "身份证号")
    private String idNo;
	/**进门 or 出门*/
	@Excel(name = "进门 or 出门", width = 15, dicCode = "is_open_door")
	@Dict(dicCode = "is_open_door")
    @ApiModelProperty(value = "进门 or 出门")
    private String isOpenDoor;
}
