/**
 * 
 */
package com.hshc.sale.web.controller.frame;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hshc.sale.util.UserSessionProvider;
import com.hshc.common.util.ValidateCode;
import com.hshc.upms.entity.security.User;


/**
 * @author zhanghua
 * 
 */

@Controller
@RequestMapping(value = "${adminPath}")
public class FrameController {

	/**
	 * 主体框架
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public String main(ModelMap model,HttpServletRequest request) {
		
		User user = (User)UserSessionProvider.getUserSerssion(request);
		if(user != null){
			model.addAttribute("adminUserName", user.getName());
		}
//		HttpSession session = request.getSession(true);
//		Boolean isApp = (Boolean)session.getAttribute("isApp");
//		if(isApp!=null && isApp){
//			return "frame/main";
//		}

		// 属性
		Properties props = System.getProperties();
		Runtime runtime = Runtime.getRuntime();
		// 当前的剩余内存数
		long freeMemoery = runtime.freeMemory();
		// 总内存
		long totalMemory = runtime.totalMemory();
		// 已使用的内存
		long usedMemory = totalMemory - freeMemoery;
		// 最大内存
		long maxMemory = runtime.maxMemory();
		// 剩余内存
		long useableMemory = maxMemory - totalMemory + freeMemoery;

		model.addAttribute("props", props);
		model.addAttribute("freeMemoery", freeMemoery);
		model.addAttribute("totalMemory", totalMemory);
		model.addAttribute("usedMemory", usedMemory);
		model.addAttribute("maxMemory", maxMemory);
		model.addAttribute("useableMemory", useableMemory);
		return "frame/main";

	}

	/**
	 * 登录界面
	 * 
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		String browserInfo = request.getHeader("user-agent");
		if(browserInfo.contains("iPhone") ||browserInfo.contains("Android")||browserInfo.contains("iPad")){
			return "login";
		}
		
		return "login";
	}

	/**
	 * 欢迎页面
	 * 
	 * @return
	 */
	@RequestMapping("/center")
	public String center(ModelMap model) {
		/*// 属性
		Properties props = System.getProperties();
		Runtime runtime = Runtime.getRuntime();
		// 当前的剩余内存数
		long freeMemoery = runtime.freeMemory();
		// 总内存
		long totalMemory = runtime.totalMemory();
		// 已使用的内存
		long usedMemory = totalMemory - freeMemoery;
		// 最大内存
		long maxMemory = runtime.maxMemory();
		// 剩余内存
		long useableMemory = maxMemory - totalMemory + freeMemoery;
		
		model.addAttribute("props", props);
		model.addAttribute("freeMemoery", freeMemoery);
		model.addAttribute("totalMemory", totalMemory);
		model.addAttribute("usedMemory", usedMemory);
		model.addAttribute("maxMemory", maxMemory);
		model.addAttribute("useableMemory", useableMemory);*/
		return "frame/center";
	}

	/**
	 * 关于信息
	 * 
	 * @return
	 */
	@RequestMapping("/info")
	public String info() {
		return "frame/info";
	}

	/**
	 * 生成验证码
	 * 
	 * @return byte[]
	 * @throws IOException
	 */
	@RequestMapping("/validateCode")
	public void validateCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
		// 设置浏览器不缓存本页
		response.setHeader("Cache-Control", "no-cache");
		// 生成验证码，写入用户session
		String verifyCode = ValidateCode.generateTextCode(ValidateCode.TYPE_NUM_ONLY, 4, null);
		request.getSession().setAttribute("validateCode", verifyCode);
		// 输出验证码给客户端
		response.setContentType("image/jpeg");
		BufferedImage bim = ValidateCode.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);
		ImageIO.write(bim, "JPEG", response.getOutputStream());
	}

}
