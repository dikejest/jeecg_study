package org.jeecg.modules.communitySupplyQuantity.entity;

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
 * @Description: 疫情物资库存表
 * @Author: jeecg-boot
 * @Date:   2021-04-06
 * @Version: V1.0
 */
@Data
@TableName("community_supply_quantity")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="community_supply_quantity对象", description="疫情物资库存表")
public class CommunitySupplyQuantity implements Serializable {
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
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "org_code")
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**小区名*/
    @ApiModelProperty(value = "小区名")
    private String name;
	/**口罩*/
	@Excel(name = "口罩", width = 15)
    @ApiModelProperty(value = "口罩")
    private Integer mask;
	/**橡胶手套*/
	@Excel(name = "橡胶手套", width = 15)
    @ApiModelProperty(value = "橡胶手套")
    private Integer rubberGloves;
	/**一次性手套*/
	@Excel(name = "一次性手套", width = 15)
    @ApiModelProperty(value = "一次性手套")
    private Integer disposableGloves;
	/**护目镜*/
	@Excel(name = "护目镜", width = 15)
    @ApiModelProperty(value = "护目镜")
    private Integer goggle;
	/**消毒液*/
	@Excel(name = "消毒液", width = 15)
    @ApiModelProperty(value = "消毒液")
    private Integer disinfectant;
	/**酒精*/
	@Excel(name = "酒精", width = 15)
    @ApiModelProperty(value = "酒精")
    private Integer alcohol;
	/**体温枪*/
	@Excel(name = "体温枪", width = 15)
    @ApiModelProperty(value = "体温枪")
    private Integer temperatureGun;
}
