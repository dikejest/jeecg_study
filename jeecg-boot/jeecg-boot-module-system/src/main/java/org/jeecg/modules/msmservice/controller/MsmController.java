package org.jeecg.modules.msmservice.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.communityOrder.vo.MsmVo;
import org.jeecg.modules.msmservice.service.MsmService;
import org.jeecg.modules.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ：hyj
 * @description：
 * @date ：Created in 2021/1/13 8:52
 */

@Api(tags="发送短信验证码")
@RestController
@Slf4j
@RequestMapping("/edumsm/msm")
public class MsmController {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MsmService msmService;


    //发送短信的方法
    @AutoLog(value = "根据手机号发送短信验证码")
    @ApiOperation(value = "根据手机号发送短信验证码", notes = "发送短信验证码")
    @GetMapping("send/{phone}")
    public Result<?> sendMsm(@PathVariable String phone) {
        //1.从redis中获取验证码，如果获取得到直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return Result.OK();
        }
        //2.如果获取不到就发验证码
        //生成随机值，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        //调用service发送短信的方法
        boolean isSend = msmService.send(param, phone);
        if (isSend) {
            //存入redis，设置有效时间为5min
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return Result.OK();
        } else {
            return Result.error("短信发送失败");
        }

    }

    //发送短信的方法
    @AutoLog(value = "根据手机号验证短信验证码是否正确")
    @ApiOperation(value = "根据手机号验证短信验证码是否正确", notes = "根据手机号验证短信验证码是否正确")
    @PostMapping("/verify")
    //获取redis验证码
    public Result<?> verifyMsm(@RequestBody MsmVo msmVo) {

        String redisCode = redisTemplate.opsForValue().get(msmVo.getMobile());
        if (!msmVo.getCode().equals(redisCode)) {
            return Result.error("验证码错误");
        }
        return Result.OK("登陆成功");

    }
}