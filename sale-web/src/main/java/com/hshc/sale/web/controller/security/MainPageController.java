/**
 * 
 */
package com.hshc.sale.web.controller.security;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hshc.sale.web.controller.frame.WebAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hshc.sale.util.UserSessionProvider;
import com.hshc.common.model.Tree;
import com.hshc.common.security.encoder.Md5PwdEncoder;
import com.hshc.common.service.BaseService;
import com.hshc.upms.entity.security.Resource;
import com.hshc.upms.entity.security.User;
import com.hshc.upms.service.api.security.ResourceService;
import com.hshc.upms.service.api.security.UserService;

/**
 * @author zhanghua
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/mainPage")
public class MainPageController extends WebAction<User, Integer> {

	@Autowired
	UserService<User, Integer> userService;
	
	@Autowired
	ResourceService<Resource, Integer> resourceService;
	
	@Override
	public BaseService<User, Integer> getBaseService() {
		return userService;
	}
	
	/**
	 * 进入“登录用户详情”页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "loginUserInfo", method = RequestMethod.GET)
	public String getLoginUser(HttpServletRequest request, Model model) {
		getCurrentAdminUser(request, model);
		return viewName("loginUserInfo");

	}

	/**
	 * 进入“修改密码”页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "modifyPassword", method = RequestMethod.GET)
	public String getModifyPassword(HttpServletRequest request, Model model) {
		getCurrentAdminUser(request, model);
		return viewName("modifyPassword");
	}

	/**
	 * 从Session中取出登录用户信息，传入页面Model中
	 * 
	 * @param request
	 * @param model
	 */
	private void getCurrentAdminUser(HttpServletRequest request, Model model) {
		User loginUser = UserSessionProvider.getUserSerssion(request);
		
		model.addAttribute("adminUser", loginUser);
	}

	/**
	 * 修改用户的密码
	 * @param request
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "modifyPassword", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modifyPassword(HttpServletRequest request,
			String password) {

		Map<String, Object> map = Maps.newHashMap();

		User loginUser = UserSessionProvider.getUserSerssion(request);

		if (null != loginUser) {
			// 修改密码的时候，存入数据库的也是加密的密码信息
			String comparePassword = Md5PwdEncoder.encodePassword(password,
					loginUser.getAccount());
			loginUser.setPassword(comparePassword);
			userService.updateEntity(loginUser);
			map.put("success", true);
		} else {
			map.put("success", false);
		}

		return map;
	}
	
	/**
	 * 登录以后，加载左边菜单栏的树形功能菜单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "user_tree_json", method = RequestMethod.POST)
	@ResponseBody
	public List<Tree> getUserTreeList(HttpServletRequest request) {
		List<Tree> list = Lists.newArrayList();
		User loginUser = UserSessionProvider.getUserSerssion(request);
		if (loginUser != null) {
			list = resourceService.getListForUser(loginUser.getAccount(),
					loginUser.getSysId());
		}
		return list;
	}

	@Override
	protected void setDefaultValue(HttpServletRequest request, User t, String operateFlag) {
	}
	
	/**
	 * 检验密码是不是输入正确
	 * @param request
	 * @param oldPassword
	 * @return
	 */
	@RequestMapping(value = "uniquePassword", method = RequestMethod.POST)
	@ResponseBody
	public boolean uniquePwd(HttpServletRequest request, String oldPassword) {

		User loginUser =  UserSessionProvider.getUserSerssion(request);

		if (null != loginUser) {
			String comparePassword = Md5PwdEncoder.encodePassword(oldPassword,
					loginUser.getAccount());
			if (loginUser.getPassword().equals(comparePassword)) {
				return true;
			}
		}
	    return false;
	}
	
}
