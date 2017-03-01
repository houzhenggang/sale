/**
 * 
 */
package com.hshc.sale.web.component;

import com.google.common.collect.Maps;
import com.hshc.sale.util.UserSessionProvider;
import com.hshc.common.constants.Constants;
import com.hshc.upms.entity.security.User;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author huanggaoren
 *
 */

@Aspect
@Component
public class UserOperatorLogAspect {


	@Autowired
	private HttpServletRequest request;

	@Pointcut("execution(* com.hshc.common.service.*.create*(..))")
	public void insertServiceCall() {
	}

	@AfterReturning(value = "insertServiceCall()", argNames = "rtv", returning = "rtv")
	public void createLog(JoinPoint joinPoint, Object rtv) throws Throwable {
		saveOperator(joinPoint, rtv);
	}

	@Pointcut("execution(* com.hshc.common.service.*.update*(..))")
	public void updateServiceCall() {
	}

	@AfterReturning(value = "updateServiceCall()", argNames = "rtv", returning = "rtv")
	public void updateLog(JoinPoint joinPoint, Object rtv) throws Throwable {
		saveOperator(joinPoint, rtv);
	}

	@Pointcut("execution(* com.hshc.common.service.*.delete*(..))")
	public void deleteServiceCall() {

	}

	@AfterReturning(value = "deleteServiceCall()", argNames = "rtv", returning = "rtv")
	public void deleteLog(JoinPoint joinPoint, Object rtv) throws Throwable {
		saveOperator(joinPoint, rtv);
	}

	/**
	 * 保存日志
	 * 
	 * @param joinPoint
	 * @param rtv
	 * @throws Exception
	 */
	private void saveOperator(JoinPoint joinPoint, Object rtv) throws Exception {
		// 获取方法名
		String methodName = joinPoint.getSignature().getName();
		User user = UserSessionProvider.getUserSerssion(request);
		if (user != null) {
			// 获取操作内容
			Map<String, Object> map = getContent(joinPoint.getArgs(),
					methodName);
			String opContent = (String) map.get("content");
			String className = (String) map.get("className");
			if (!className.equalsIgnoreCase("LoginSuccessLog")
					&& !className.equalsIgnoreCase("LoginFaillog")
					&& !className.equalsIgnoreCase("OperationLog")) {
				String title = geTitle(className, methodName);
			}
		}
	}

	/**
	 * 获取标题
	 * 
	 * @return
	 * @throws Exception
	 */
	private String geTitle(String className, String methodName) {
		if (StringUtils.isNotEmpty(methodName)) {
			if (methodName.equals("createEntity")) {
				methodName = Constants.Operator.CREATE; // 创建
			} else if (methodName.equals("updateEntity")) {
				methodName = Constants.Operator.UPDATE;// 更新
			} else if (methodName.equals("deleteById")
					|| methodName.equals("deleteByIds")
					|| methodName.equals("deleteByObject")) {
				methodName = Constants.Operator.DELETE;// 删除
			}
		}
		return methodName + className;
	}

	/**
	 * 使用Java反射来获取被拦截方法(insert、update、delete)的参数值， 将参数值拼接为操作内容
	 */
	private Map<String, Object> getContent(Object[] args, String mName)
			throws Exception {
		Map<String, Object> map = Maps.newHashMap();
		if (args == null) {
			return null;
		}

		StringBuffer rs = new StringBuffer();
		rs.append(mName);
		String className = null;
		int index = 1;
		// 遍历参数对象
		for (Object info : args) {
			// 获取对象类型
			className = info.getClass().getName();
			className = className.substring(className.lastIndexOf(".") + 1);
			rs.append("[参数" + index + "，类型：" + className + "，值：");
			map.put("className", className);
			// 获取对象的所有方法
			Method[] methods = info.getClass().getDeclaredMethods();

			// 遍历方法，判断get方法
			for (Method method : methods) {

				String methodName = method.getName();
				Object rsValue = null;
				try {

					// 调用get方法，获取返回值
					rsValue = method.invoke(info);

					if (rsValue == null) {// 没有返回值
						continue;
					}

				} catch (Exception e) {
					continue;
				}

				// 将值加入内容中
				rs.append("(" + methodName + " : " + rsValue + ")");
			}

			rs.append("]");

			index++;
		}
		map.put("content", rs.toString());
		return map;
	}
}
