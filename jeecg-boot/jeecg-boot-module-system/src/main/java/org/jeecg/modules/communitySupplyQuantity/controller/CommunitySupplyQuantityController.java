package org.jeecg.modules.communitySupplyQuantity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.communityOrder.entity.CommunityOrder;
import org.jeecg.modules.communityOrder.service.ICommunityOrderService;
import org.jeecg.modules.communitySupplyQuantity.entity.CommunitySupplyQuantity;
import org.jeecg.modules.communitySupplyQuantity.service.ICommunitySupplyQuantityService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 疫情物资库存表
 * @Author: jeecg-boot
 * @Date:   2021-04-06
 * @Version: V1.0
 */
@Api(tags="疫情物资库存表")
@RestController
@RequestMapping("/communitySupplyQuantity/communitySupplyQuantity")
@Slf4j
public class CommunitySupplyQuantityController extends JeecgController<CommunitySupplyQuantity, ICommunitySupplyQuantityService> {
	@Autowired
	private ICommunitySupplyQuantityService communitySupplyQuantityService;


	 @Autowired
	 private ISysDepartService sysDepartService;

	 @Autowired
	 private ICommunityOrderService communityOrderService;
	/**
	 * 分页列表查询
	 *
	 * @param communitySupplyQuantity
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	/*@AutoLog(value = "疫情物资库存表-分页列表查询")
	@ApiOperation(value="疫情物资库存表-分页列表查询", notes="疫情物资库存表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CommunitySupplyQuantity communitySupplyQuantity,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CommunitySupplyQuantity> queryWrapper = QueryGenerator.initQueryWrapper(communitySupplyQuantity, req.getParameterMap());
		Page<CommunitySupplyQuantity> page = new Page<CommunitySupplyQuantity>(pageNo, pageSize);
		IPage<CommunitySupplyQuantity> pageList = communitySupplyQuantityService.page(page, queryWrapper);
		return Result.OK(pageList);
	}*/

	 /**
	  * 分页列表查询
	  *
	  * @param communitySupplyQuantity
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "疫情物资库存表-分页列表查询")
	 @ApiOperation(value="疫情物资库存表-分页列表查询", notes="疫情物资库存表-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<?> queryPageList(CommunitySupplyQuantity communitySupplyQuantity,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {

		 //根据user信息获得orgcode
		 LoginUser principal = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 log.info("user:{}", principal.getOrgCode());
		 //根据orgcode从部门表中查询他负责的所有部门。
		 SysDepart sysDepart = sysDepartService.getSysDepart(principal.getOrgCode());
		 //根据org_code进行模糊查询，查询他的子类的所有id
		 List<String> ids = sysDepartService.getIds(principal.getOrgCode());
		 //根据ids查询所有的小区
		 QueryWrapper<CommunitySupplyQuantity> wrapper = new QueryWrapper<>();
		 wrapper.in("sys_org_code",ids);
		 Page<CommunitySupplyQuantity> page = new Page<CommunitySupplyQuantity>(pageNo, pageSize);
		 IPage<CommunitySupplyQuantity> pageList = communitySupplyQuantityService.page(page, wrapper);
		 return Result.OK(pageList);
	 }



	
	/**
	 *   添加
	 *
	 * @param communitySupplyQuantity
	 * @return
	 */
	@AutoLog(value = "疫情物资库存表-添加")
	@ApiOperation(value="疫情物资库存表-添加", notes="疫情物资库存表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CommunitySupplyQuantity communitySupplyQuantity) {
		//判断是否存在该小区
		CommunitySupplyQuantity flag = communitySupplyQuantityService.getBySysOrgCode(communitySupplyQuantity.getSysOrgCode());
		if (flag!=null){
			return Result.error("该小区已存在，请重新选择");
		}
		//添加新的小区库存记录，不允许添加已经存在的小区，第一次添加同时对应生成小区的入库登记单
		communitySupplyQuantityService.addNewCommunity(communitySupplyQuantity);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param communitySupplyQuantity
	 * @return
	 */
	@AutoLog(value = "疫情物资库存表-编辑")
	@ApiOperation(value="疫情物资库存表-编辑", notes="疫情物资库存表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CommunitySupplyQuantity communitySupplyQuantity) {
		communitySupplyQuantityService.updateById(communitySupplyQuantity);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "疫情物资库存表-通过id删除")
	@ApiOperation(value="疫情物资库存表-通过id删除", notes="疫情物资库存表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		CommunitySupplyQuantity byId = communitySupplyQuantityService.getById(id);
		communitySupplyQuantityService.removeById(id);
		//同时删除小区对应的所有入库单
		communityOrderService.removeOrders(byId.getSysOrgCode());
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "疫情物资库存表-批量删除")
	@ApiOperation(value="疫情物资库存表-批量删除", notes="疫情物资库存表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.communitySupplyQuantityService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "疫情物资库存表-通过id查询")
	@ApiOperation(value="疫情物资库存表-通过id查询", notes="疫情物资库存表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CommunitySupplyQuantity communitySupplyQuantity = communitySupplyQuantityService.getById(id);
		if(communitySupplyQuantity==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(communitySupplyQuantity);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param communitySupplyQuantity
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CommunitySupplyQuantity communitySupplyQuantity) {
        return super.exportXls(request, communitySupplyQuantity, CommunitySupplyQuantity.class, "疫情物资库存表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CommunitySupplyQuantity.class);
    }

}
