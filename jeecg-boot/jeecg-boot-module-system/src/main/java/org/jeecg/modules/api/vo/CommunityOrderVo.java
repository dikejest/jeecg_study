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
 * @Description: 疫情物资入库单
 * @Author: jeecg-boot
 * @Date:   2021-04-06
 * @Version: V1.0
 */
@Data
@ApiModel(value="community_orderVo对象", description="疫情物资入库")
public class CommunityOrderVo implements Serializable {


    /**小区id*/
    @Dict(dictTable = "community_supply_quantity", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属小区id,必填",required = true)
    private String areaId;
	/**口罩*/
    @ApiModelProperty(value = "口罩")
    private Integer mask;
	/**橡胶手套*/
    @ApiModelProperty(value = "橡胶手套")
    private Integer rubberGloves;
	/**一次性手套*/
    @ApiModelProperty(value = "一次性手套")
    private Integer disposableGloves;
	/**护目镜*/
    @ApiModelProperty(value = "护目镜")
    private Integer goggle;
	/**消毒液*/
    @ApiModelProperty(value = "消毒液")
    private Integer disinfectant;
	/**酒精*/
    @ApiModelProperty(value = "酒精")
    private Integer alcohol;
	/**体温枪*/
    @ApiModelProperty(value = "体温枪")
    private Integer temperatureGun;
	/**备注*/
    @ApiModelProperty(value = "备注")
    private String remark;
}
