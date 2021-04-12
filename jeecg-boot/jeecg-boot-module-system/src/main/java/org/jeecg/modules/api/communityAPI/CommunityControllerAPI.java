package org.jeecg.modules.api.communityAPI;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.api.vo.CommunityAccessRecordsVo;
import org.jeecg.modules.api.vo.CommunityOrderVo;
import org.jeecg.modules.api.vo.KeyWords;
import org.jeecg.modules.communityAccessRecords.entity.CommunityAccessRecords;
import org.jeecg.modules.communityAccessRecords.service.ICommunityAccessRecordsService;
import org.jeecg.modules.communityOrder.entity.CommunityOrder;
import org.jeecg.modules.communityOrder.service.ICommunityOrderService;
import org.jeecg.modules.communitySupplyQuantity.entity.CommunitySupplyQuantity;
import org.jeecg.modules.communitySupplyQuantity.service.ICommunitySupplyQuantityService;
import org.jeecg.modules.system.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author ：hyj
 * @description：
 * @date ：Created in 2021/4/8 9:41
 */
@Api(tags="小区前端响应接口api")
@RestController
@RequestMapping("/community")
@Slf4j
public class CommunityControllerAPI {

    @Autowired
    private ICommunityAccessRecordsService communityAccessRecordsService;

    @Autowired
    private ICommunityOrderService communityOrderService;

    @Autowired
    private ICommunitySupplyQuantityService communitySupplyQuantityService;


    @AutoLog(value = "小区出入登记记录添加")
    @ApiOperation(value="小区出入登记记录添加", notes="小区出入登记记录添加")
    @PostMapping(value = "/addRecords")
    public Result<?> addRecords(@RequestBody CommunityAccessRecordsVo recordsVo) {
        //判断姓名参数是否为空
        if (recordsVo.getName() == null || recordsVo.getName().equals("")) {
            return Result.error("姓名为空");
        }
        if (recordsVo.getName() == null || recordsVo.getName().equals("")) {
            return Result.error("小区id为空");
        }
        if (recordsVo.getMobile() == null || recordsVo.getMobile().equals("")) {
            return Result.error("手机号为空");
        }
        if (recordsVo.getDoorNo() == null || recordsVo.getDoorNo().equals("")) {
            return Result.error("门牌号为空");
        }
        if (recordsVo.getIdNo() == null || recordsVo.getIdNo().equals("")) {
            return Result.error("身份证号为空");
        }
        //判断该小区是否存在
        CommunitySupplyQuantity area = communitySupplyQuantityService.getById(recordsVo.getAreaId());
        if (area==null){
            return Result.error("该小区不存在");
        }
        //判断体温是否为空
        if (recordsVo.getTemperature()==null) {
            return Result.error("体温数据为空");
        }
        if (recordsVo.getIsOpenDoor()!=1 && recordsVo.getIsOpenDoor() !=2){
            return Result.error("体温格式有问题");
        }
        //判断手机号码是否正确
        String regexMobile = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16[6-7])|(17[1-8])|(18[0-9])|(19[1|3])|(19[5|6])|(19[8|9]))\\d{8}$";
        if (!Pattern.matches(regexMobile, recordsVo.getMobile())) {
            return Result.error("手机号格式错误，请重新输入");
        }
        //判断身份证号码是否正确
        String regexNO = "\\d{17}[\\d|x]|\\d{15}";
        if (!Pattern.matches(regexNO, recordsVo.getIdNo())) {
            return Result.error("身份证号码格式错误，请重新输入");
        }
        //判断最新的一条记录是否和出入小区相反，如果相同则不给通过
        CommunityAccessRecords one = communityAccessRecordsService.queryFlagByCardId(recordsVo.getIdNo());
        if (one != null && recordsVo.getIsOpenDoor() == one.getIsOpenDoor()) {
            return Result.error("您已进入小区或者出小区，请重新选择");
        }
        if (communityAccessRecordsService.addRecords(recordsVo)) {
            return Result.OK("登记成功");
        } else {
            return Result.error("登记失败");
        }
    }

    @AutoLog(value = "小区出入登记记录查询")
    @ApiOperation(value="小区出入登记记录查询", notes="小区出入登记记录查询")
    @PostMapping(value = "/QueryRecords")
    public Result<?>  queryRecords (@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                    @RequestBody KeyWords keyWords){
        if (keyWords.getAreaId()==null || keyWords.getAreaId().equals("")){
            return Result.error("小区id为空");
        }
        Map map = communityAccessRecordsService.queryRecords(keyWords,pageNo,pageSize);
        return Result.OK(map);
    }

    //疫情物资录入库登记
    @AutoLog(value = "疫情物资录库登记添加")
    @ApiOperation(value="疫情物资录库登记添加", notes="疫情物资录库登记添加")
    @PostMapping(value = "/AddOrder")
    public Result<?> addOrder (@RequestBody CommunityOrderVo communityOrderVo){
        if (communityOrderVo==null){
            return Result.error("数据为空");
        }
        CommunityOrder communityOrder = new CommunityOrder();
        BeanUtil.copyProperties(communityOrderVo,communityOrder);
        communityOrderService.addOrder(communityOrder);
        return Result.OK("入库成功");
    }

    //查询所有小区id，条件部门编码筛选,然后已text：value的形式显示出来
    @AutoLog(value = "查询能看到的小区")
    @ApiOperation(value="查询能看到的小区", notes="查询能看到的小区")
    @GetMapping(value = "QueryArea")
    public List<Map<String,String>> queryArea(){
        //根据用户的部门号获取他能得到的小区id
        LoginUser principal = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        return this.communitySupplyQuantityService.queryArea(principal.getOrgCode());
    }

}
