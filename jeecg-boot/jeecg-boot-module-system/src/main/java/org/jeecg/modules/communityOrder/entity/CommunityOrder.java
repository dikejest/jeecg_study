package org.jeecg.modules.communityOrder.entity;

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
 * @Description: 疫情物资入库单
 * @Author: jeecg-boot
 * @Date:   2021-04-06
 * @Version: V1.0
 */
@Data
@TableName("community_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="community_order对象", description="疫情物资入库单")
public class CommunityOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**入库单号*/
	@Excel(name = "入库单号", width = 25)
    @ApiModelProperty(value = "入库单号")
    private String orderId;
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
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
}
