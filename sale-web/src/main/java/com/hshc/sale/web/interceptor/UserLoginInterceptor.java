/**
 * @Title:UserLoginInterceptor.java
 *
 * @Package com.aijava.wshop.mng.web.interceptor
 *
 * @Description:TODO (用一句话描述该文件做什么)
 *
 * @author huanggaoren itorac@sina.com
 *
 * @date 2014年7月25日 上午3:41:25
 *
 * @version V1.0
 */
package com.hshc.sale.web.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hshc.sale.util.UserSessionProvider;
import com.hshc.common.web.springmvc.ReadPropertyPlaceholderConfigurer;
import com.hshc.common.web.util.ResponseUtils;
import com.hshc.upms.entity.security.User;

/**
 * @ClassName:UserLoginInterceptor
 *
 * @Description:TODO(这里用一句话描述这个类的作用)
 *
 * @author huanggaoren itorac@sina.com
 * 
 * @date 2014年7月25日 上午3:41:25
 * 
 */
public class UserLoginInterceptor extends HandlerInterceptorAdapter {

	private static String loginUrl; // 登陆 url
	
	private static String h5LoginUrl; //移动端登录URL

	private static String[] excludeUrls; // 不拦截的URL

	public static final String PERMISSION_MODEL = "_permission_key";

	public static final String PERMISSION_VALUE = "_permission_value";

	public UserLoginInterceptor() {

	}

	static {
		if (StringUtils.isEmpty(loginUrl)) {
			loginUrl = (String) ReadPropertyPlaceholderConfigurer
					.getContextProperty("login.url");
		}
		if(StringUtils.isEmpty(h5LoginUrl)){
			h5LoginUrl = (String) ReadPropertyPlaceholderConfigurer
			.getContextProperty("h5Login.url");
		}
		String excludeUrl = (String) ReadPropertyPlaceholderConfigurer
				.getContextProperty("exclude.Urls");
		if (StringUtils.isNotEmpty(excludeUrl)) {
			excludeUrls = excludeUrl.split(";");
		}
	}

	/**
	 * 登录Session过期问题
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		User user = UserSessionProvider.getUserSerssion(request);
		String uri = getURI(request);
		System.out.println("浏览器信息："+request.getHeader("user-agent")); 
		Enumeration   typestr = request.getHeaderNames(); 
		System.out.println(typestr.toString());
		String browserInfo = request.getHeader("user-agent");
		if(browserInfo.contains("Android")) {
			System.out.println("Android移动客户端");
		} else if(browserInfo.contains("iPhone")) {
			System.out.println("iPhone移动客户端");
		}  else if(browserInfo.contains("iPad")) {
			System.out.println("iPad客户端");
		}  else if(browserInfo.contains("Windows")){
			System.out.println("windows客户端");
		} else{
			System.out.println("其他客户端");
		}
		boolean isApp=false;
		if(browserInfo.contains("Android") ||browserInfo.contains("Android")||browserInfo.contains("iPad")){
			isApp=true;
		}
		if (exclude(uri)) {
			return true;
		}
		if (user == null) {
			String loginUrl = getLoginUrl(request);
			if (request.getHeader("x-requested-with") != null 
					&& request.getHeader("x-requested-with").equals( 
					"XMLHttpRequest")) { // ajax请求 
				response.setHeader("sessionstatus", "timeout");
				if(isApp){
					loginUrl = getH5LoginUrl(request);
				}
				System.out.println("登录系统："+loginUrl);
				response.setHeader("loginUrl", loginUrl);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("msg", "用户过期,请重新登录!");
				map.put("state", "session_out");
				ResponseUtils.renderJson(response, map);
			}else{
				response.sendRedirect(loginUrl);
			}
			return false;
		}
		return super.preHandle(request, response, handler);
	}

	/**
	 * 把权限列表存放在权限中
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		User user = UserSessionProvider.getUserSerssion(request);
		// 不控制权限时perm为null，PermistionDirective标签将以此作为依据不处理权限问题。
		if (user != null && mav != null && mav.getModelMap() != null
				&& mav.getViewName() != null
				&& !mav.getViewName().startsWith("redirect:")) {
			mav.getModelMap().addAttribute(PERMISSION_MODEL, user.getPerms());
			mav.getModelMap().addAttribute(PERMISSION_VALUE, user.getPermsValue());
		}
	}

	/**
	 * 取得登陆URL
	 * 
	 * @param request
	 * @return
	 */
	private String getLoginUrl(HttpServletRequest request) {
		StringBuilder buff = new StringBuilder();
		if (loginUrl.startsWith("/")) {
			String uri = request.getContextPath();
			if (!StringUtils.isBlank(uri)) {
				buff.append(uri);
			}
		}
		buff.append(loginUrl);
		return buff.toString();
	}
	
	/**
	 * 取得移动端登陆URL
	 * 
	 * @param request
	 * @return
	 */
	private String getH5LoginUrl(HttpServletRequest request) {
		StringBuilder buff = new StringBuilder();
		if (h5LoginUrl.startsWith("/")) {
			String uri = request.getContextPath();
			if (!StringUtils.isBlank(uri)) {
				buff.append(uri);
			}
		}
		buff.append(h5LoginUrl);
		return buff.toString();
	}

	/**
	 * URL比较
	 * 
	 * @Title: exclude
	 * @Description: TODO
	 * @param @param uri
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	private boolean exclude(String uri) {
		if (excludeUrls != null) {
			for (String exc : excludeUrls) {
				if (exc.equals(uri)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取当前URL
	 * 
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 */
	private static String getURI(HttpServletRequest request)
			throws IllegalStateException {
		String uri = request.getServletPath()
				+ (request.getPathInfo() == null ? "" : request.getPathInfo());
		return uri;
	}
}
