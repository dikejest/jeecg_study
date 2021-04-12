package org.jeecg.modules.communityOrder.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：hyj
 * @description：
 * @date ：Created in 2021/4/10 11:58
 */
@Data
public class MsmVo {

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "验证码")
    private String code;
}
