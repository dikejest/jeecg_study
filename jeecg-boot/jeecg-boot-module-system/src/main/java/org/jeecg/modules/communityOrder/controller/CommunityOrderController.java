package org.jeecg.modules.communityOrder.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.communityOrder.entity.CommunityOrder;
import org.jeecg.modules.communityOrder.service.ICommunityOrderService;

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
 * @Description: 疫情物资入库单
 * @Author: jeecg-boot
 * @Date:   2021-04-06
 * @Version: V1.0
 */
@Api(tags="疫情物资入库单")
@RestController
@RequestMapping("/communityOrder/communityOrder")
@Slf4j
public class CommunityOrderController extends JeecgController<CommunityOrder, ICommunityOrderService> {
	@Autowired
	private ICommunityOrderService communityOrderService;

	@Autowired
	private ISysDepartService sysDepartService;
	/**
	 * 分页列表查询
	 *
	 * @param communityOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	/*@AutoLog(value = "疫情物资入库单-分页列表查询-只查该小区(管理员全查)")
	@ApiOperation(value="疫情物资入库单-分页列表查询-只查该小区(管理员全查)", notes="疫情物资入库单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CommunityOrder communityOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		//获取用户的登录信息，然后根据org_code获取用户所在职位的小区的id，
		//然后在根据小区id查询该小区的物资入库登记记录列表。
		//根据user信息获得orgcode
		LoginUser principal = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		log.info("user:{}", principal.getOrgCode());
		//根据orgcode从部门表中查询他的父id，即对应所在的小区org
		SysDepart sysDepart = sysDepartService.getSysDepart(principal.getOrgCode());
		log.info("!!:{}",sysDepart.getId());
		//然后只查看他能看到的小区的信息。
		communityOrder.setSysOrgCode(sysDepart.getParentId());
		QueryWrapper<CommunityOrder> queryWrapper = QueryGenerator.initQueryWrapper(communityOrder, req.getParameterMap());
		Page<CommunityOrder> page = new Page<CommunityOrder>(pageNo, pageSize);
		IPage<CommunityOrder> pageList = communityOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}*/

	//--------------------------------------------------


	 @AutoLog(value = "疫情物资入库单-分页列表查询")
	 @ApiOperation(value="疫情物资入库单-分页列表查询", notes="疫情物资入库单-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<?> queryPageList(CommunityOrder communityOrder,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 //获取用户的登录信息，然后根据org_code获取用户所在职位的小区的id，
		 //然后在根据小区id查询该小区的物资入库登记记录列表。
		 //根据user信息获得orgcode
		 LoginUser principal = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		 log.info("user:{}", principal.getOrgCode());
		 //根据orgcode从部门表中查询他负责的所有部门。
		 SysDepart sysDepart = sysDepartService.getSysDepart(principal.getOrgCode());
		 //根据org_code进行模糊查询，查询他的子类的所有id
		 List<String> ids = sysDepartService.getIds(principal.getOrgCode());
		 //根据ids查询所有的小区
		 QueryWrapper<CommunityOrder> wrapper = new QueryWrapper<>();
		 wrapper.in("sys_org_code",ids);
		 log.info("!!:{}",sysDepart.getId());
		 //然后只查看他权限下能看到的小区入库信息。
		 Page<CommunityOrder> page = new Page<CommunityOrder>(pageNo, pageSize);
		 IPage<CommunityOrder> pageList = communityOrderService.page(page, wrapper);
		 return Result.OK(pageList);
	 }

	//--------------------------------------------------
	/**
	 *   添加
	 *
	 * @param communityOrder
	 * @return
	 */
	@AutoLog(value = "疫情物资入库单-添加")
	@ApiOperation(value="疫情物资入库单-添加", notes="疫情物资入库单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CommunityOrder communityOrder) {
		//判断是否传入了小区编码，若没有，则自动获得该用户的小区的编码
		if (communityOrder.getSysOrgCode()==null ||communityOrder.getSysOrgCode().equals("")){
			LoginUser principle = (LoginUser)SecurityUtils.getSubject().getPrincipal();
			SysDepart depart = sysDepartService.getSysDepart(principle.getOrgCode());
			communityOrder.setSysOrgCode(depart.getParentId());
		}

		//存入数据库，同时更改库存信息
		communityOrderService.addOrder(communityOrder);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param communityOrder
	 * @return
	 */
	@AutoLog(value = "疫情物资入库单-编辑")
	@ApiOperation(value="疫情物资入库单-编辑", notes="疫情物资入库单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CommunityOrder communityOrder) {
		//编辑物资入库单，同时库存要进行改变
		communityOrderService.updateOrder(communityOrder);
		//communityOrderService.updateById(communityOrder);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "疫情物资入库单-通过id删除")
	@ApiOperation(value="疫情物资入库单-通过id删除", notes="疫情物资入库单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		communityOrderService.removeOrder(id);
		//communityOrderService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "疫情物资入库单-批量删除")
	@ApiOperation(value="疫情物资入库单-批量删除", notes="疫情物资入库单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.communityOrderService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "疫情物资入库单-通过id查询")
	@ApiOperation(value="疫情物资入库单-通过id查询", notes="疫情物资入库单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CommunityOrder communityOrder = communityOrderService.getById(id);
		if(communityOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(communityOrder);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param communityOrder
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CommunityOrder communityOrder) {
        return super.exportXls(request, communityOrder, CommunityOrder.class, "疫情物资入库单");
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
        return super.importExcel(request, response, CommunityOrder.class);
    }

}
