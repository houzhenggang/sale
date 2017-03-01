/**
 * 
 */
package com.hshc.sale.web.controller.security;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hshc.sale.web.controller.frame.WebAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hshc.sale.util.UserSessionProvider;
import com.hshc.common.service.BaseService;
import com.hshc.upms.entity.security.Resource;
import com.hshc.upms.entity.security.User;
import com.hshc.upms.service.api.security.ResourceService;
import com.hshc.upms.service.api.security.UserService;
import com.hshc.upms.vo.user.PermissionVo;

/**
 * @author zhanghua
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/")
public class LoginController extends WebAction<User, Integer> {

	@Autowired
	UserService<User, Integer> userService;

	@Autowired
	ResourceService<Resource, Integer> resourceService;

	@Override
	public BaseService<User, Integer> getBaseService() {
		return userService;
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loginInfo(HttpServletRequest request, User user) {

		Map<String, Object> map = null;


		if (user != null) {
			
			String loginAccount = user.getAccount();
			String inputPassword = user.getPassword() ;// 页面上传过来的密码是明文

			/**
			 * 调用函数验证用户信息
			 */
			map = userService.validateLoginUser(user);
			
			String errorMessage = (String) map.get("ErrorMessage");
			boolean successFlag = (boolean) map.get("success");



			if (successFlag) {// 验证成功后
				
				User queryCondition = new User();
				queryCondition.setAccount(loginAccount);
				List<User> userList = userService
						.queryListByEntity(queryCondition);
				User adminUser = userList.get(0);

				if (null != adminUser) {
					adminUser.setSysId(user.getSysId());
				}

				saveLoginUserInSession(request, adminUser);// 验证成功后，将该用户信息存入Session中
				HttpSession session = request.getSession(true);
				String browserInfo = request.getHeader("user-agent");
				if(browserInfo.contains("iPhone") ||browserInfo.contains("Android")||browserInfo.contains("iPad")){
					session.setAttribute("isApp", true);
					map.put("isApp", true);
				}
				
			} else {// 验证失败后
		
			}
		}

		return map;
	}


	/**
	 * 将用户信息存入Session中
	 * 
	 * @param session
	 * @param userName
	 */
	private void saveLoginUserInSession(HttpServletRequest request,
			User adminUser) {
		if (null != adminUser) {

			User user = userService.initLoginUserInfo(adminUser);
			
			// 将用户信息放入Session中
			UserSessionProvider.setUserSerssion(request, user);
			
		}
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String quitToLogin(HttpServletRequest request) {
		// 安全退出的时候将Session清空
		UserSessionProvider.invalidateSerssion(request);
		return redirectLoginToUrl();
	}
	
	/**
	 * 查询出登录用户的权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "loginPermission", method = RequestMethod.POST)
	@ResponseBody
	public String getLoginUserPermissions(HttpServletRequest request){
		User loginUser =  UserSessionProvider.getUserSerssion(request);
		if(null!=loginUser){
			Set<PermissionVo> permList = loginUser.getPermList();
			String permissionStr = "";
			if(null!=permList&&permList.size()>0){
				for(PermissionVo permissionVo : permList){
					String perStr = permissionVo.getPermission();
					    if((null==perStr)||("".equals(perStr))){
					    	continue;
					    }
						permissionStr = permissionStr + "," + permissionVo.getPermission(); 
				}
				return permissionStr;
			}
		}
		
		
		return null;
	}

	@Override
	protected void setDefaultValue(HttpServletRequest request, User arg0, String arg1) {
	}

}
