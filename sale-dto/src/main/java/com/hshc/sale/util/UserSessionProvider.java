/**
 * 
 */
package com.hshc.sale.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hshc.common.web.session.HttpSessionProvider;
import com.hshc.upms.entity.security.User;
import com.hshc.upms.vo.user.PermissionVo;

/**
 * @author huanggaoren 用户Session提供
 */
public class UserSessionProvider extends HttpSessionProvider {

	/**
	 * 用户session
	 */
	private static final String SESSION_USER = "session_user";

	/**
	 * 验证码
	 */
	private static final String SESSION_VALIDATECODE = "session_validatecode";

	/**
	 * 设置用户信息 到session
	 * 
	 * @param request
	 * @param user
	 */
	public static void setUserSerssion(HttpServletRequest request, User user) {
		setAttribute(request, SESSION_USER, user);
	}

	/**
	 * 从session中获取用户信息
	 * 
	 * @param request
	 * @return SysUser
	 */
	public static User getUserSerssion(HttpServletRequest request) {
		return (User) getAttribute(request, SESSION_USER);
	}

	/**
	 * 从session中获取用户信息
	 * 
	 * @param request
	 * @return SysUser
	 */
	public static Long getUserSerssionId(HttpServletRequest request) {
		User user = getUserSerssion(request);
		if (user != null) {
			return user.getId();
		}
		return null;
	}

	/**
	 * 从session中获取用户信息
	 * 
	 * @param request
	 * @return SysUser
	 */
	public static void removeUser(HttpServletRequest request) {
		removeAttribute(request, SESSION_USER);
	}

	/**
	 * 设置验证码 到session
	 * 
	 * @param request
	 * @param validateCode
	 */
	public static void setValidateCode(HttpServletRequest request,
			String validateCode) {
		request.getSession(true).setAttribute(SESSION_VALIDATECODE,
				validateCode);
	}

	/**
	 * 从session中获取验证码
	 * 
	 * @param request
	 * @return SysUser
	 */
	public static String getValidateCode(HttpServletRequest request) {
		return (String) request.getSession(true).getAttribute(SESSION_VALIDATECODE);
	}

	/**
	 * 从session中获删除验证码
	 * 
	 * @param request
	 * @return SysUser
	 */
	public static void removeValidateCode(HttpServletRequest request) {
		removeAttribute(request, SESSION_VALIDATECODE);
	}

	/**
	 * 获取登录用户的数据权限
	 * @param request
	 * @param viewPrefix Controller通过getViewPrefix()获取
	 * @return 返回当前登录用户的对请求菜单的数据权限集合
	 */
	/*protected static List<String> getUserDataPermission(HttpServletRequest request,String viewPrefix){
		List<String> dataPermissionList = null;
		User user = getUserSerssion(request);

		for(PermissionVo vo:user.getPermList()){
			String href = vo.getHref();
			String[] hrefPath = href.split("/");
			if(viewPrefix.equals(hrefPath[0])){
				dataPermissionList=vo.getDataPermission();
			}
		}
		return dataPermissionList;
	}*/
}
