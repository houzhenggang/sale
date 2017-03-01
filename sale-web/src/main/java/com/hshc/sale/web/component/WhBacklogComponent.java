package com.hshc.sale.web.component;

import com.google.common.collect.Maps;
import com.hshc.sale.entity.wh.WhBacklog;
import com.hshc.sale.service.api.wh.WhBacklogService;
import com.hshc.sale.util.UserSessionProvider;
import com.hshc.upms.entity.security.User;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 待办任务
 *
 * @author 弋攀 E-mail：panyi@huashenghaoche.com
 * @date 2017年2月7日 18:34:58
 */
public class WhBacklogComponent implements TemplateDirectiveModel {
	@Autowired
	private WhBacklogService<WhBacklog,Integer> whBacklogService;
	public static final String PERMISSION_VALUE = "_permission_value";
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		/** 获取当前登录用户 */
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		User user =  UserSessionProvider.getUserSerssion(request);
		/** 获取待办配置表内容  */
		WhBacklog backlogParame = new WhBacklog();
		backlogParame.setSysId(user.getSysId());
		List<WhBacklog> backlogList = whBacklogService.queryListByEntity(backlogParame);
		
	
		Map parameMap = Maps.newHashMap();
		parameMap.put("user", user);
		StringBuffer userPerm = new StringBuffer("");
		
		if(user.getDataPerm()!=null && user.getDataPerm().getOrgCodeList()!=null){
			List<String> permList = user.getDataPerm().getOrgCodeList();
			for(int i =0;i<permList.size();i++){
				String perm =user.getDataPerm().getOrgCodeList().get(i);
				userPerm.append("'"+perm+"'");
				if(i<permList.size()-1){
					userPerm.append(",");
				}
			}
			parameMap.put("userOrg", userPerm.toString());
		}
		List<WhBacklog> backlogListBody = new ArrayList<WhBacklog>();
		for(WhBacklog backlog:backlogList){
			boolean hasPermission = getBacklog(backlog,parameMap,env);
			if(hasPermission){
				backlogListBody.add(backlog);
			}
		}
		env.setVariable("list",ObjectWrapper.DEFAULT_WRAPPER.wrap(backlogListBody));
		body.render(env.getOut());
	}
	
	/**
	 * 获取待办任务数量
	 * @param backlog 一级待办菜单
	 * @param parameMap	查询条件
	 * @param env
	 * sql替换符：
	 * in(${userOrg}) 查询当前用户拥有数据权限的机构
	 * in(${userId}) 查询当前用户拥有数据权限的个人
	 * =(${userId})查询当前用户拥有数据权限的个人
	 * @return 是否拥有权限
	 */
	private boolean getBacklog(WhBacklog backlog,Map<String,Object> parameMap,Environment env){
		boolean hasPermission = true;
		/** 判断是否为一级菜单  */
		if(backlog.getLevel()!=null && backlog.getLevel()==1){
			hasPermission = false;
			/** 判断是否有下级  */
			List<WhBacklog> backlogList = new ArrayList<>();
			Integer count = 0;
			for(WhBacklog backlogChild:backlog.getBacklogList()){
				/**    获取下级菜单的待办任务*/
				if (getBacklog(backlogChild, parameMap, env)) {
					/** 待办任务条数大于0时，加入待办列表*/
					if (StringUtils.isNotBlank(backlogChild.getSqlStr()) && backlogChild.getBackLogCount() > 0) {
						backlogList.add(backlogChild);
						hasPermission = true;
						count += backlogChild.getBackLogCount();
					}
				}
			}
			backlog.setBacklogList(backlogList);
			if(count==0){
				hasPermission = false;
			}
		}else{
			/**	判断待办权限，权限为空时任何人都可查看*/
			if(backlog.getPermission()==null || hasPermission(env,backlog.getPermission())){
				hasPermission = true;
			}else{
				hasPermission = false;
			}
		}
		/**
		 * 拥有待办权限执行待办任务SQL
		 */
		if(hasPermission){
			String sqlStr = backlog.getSqlStr();
			List<Map<String,Object>>  list = new ArrayList<>();
			if(sqlStr!=null && !sqlStr.equals("")){
				String userOrg = (String)parameMap.get("userOrg");
				if(userOrg==null || userOrg.equals("")){
					sqlStr = sqlStr.replace("in(${userOrg})", " is not null ");
				}else{
					sqlStr = sqlStr.replace("${userOrg}", userOrg);
				}
				Integer userId = (Integer)parameMap.get("userId");
				if(userId==null){
					sqlStr = sqlStr.replace("in(${userId})", " is not null ");
					sqlStr = sqlStr.replace("=(${userId})", " is not null ");
				}else{
					sqlStr = sqlStr.replace("${userId}", userId+"");
				}
				
				list = whBacklogService.executeSql(sqlStr, parameMap);
			}
			for(Map<String,Object> map:list){
				Integer backlogCount = Integer.parseInt((Long)map.get("count")+"");
				backlog.setBackLogCount(backlogCount);
				if(backlogCount==0){
					hasPermission = false;
					break;
				}
			}
			
		}
		return hasPermission;
	}
	
	/**
	 * 判断用户是否拥有权限
	 * @param env
	 * @param permission
	 * @return
	 */
	private boolean hasPermission(Environment env,String permission){
		boolean hasPermission = false;
		try {
			TemplateSequenceModel perms = getPermsValue(env);
			if (perms == null) {
				// perms为null，则代表不需要判断权限。
				hasPermission = true;
			} else {
				String perm;
				for (int i = 0, len = perms.size(); i < len; i++) {
					perm = ((TemplateScalarModel) perms.get(i)).getAsString();
					if (permission.equals(perm)) {
						hasPermission = true;
						break;
					}
				}
			}
		} catch (TemplateModelException e) {
			return false;
		}
		return hasPermission;
	}
	
	/**
	 * 获取登录用户权限
	 * 
	 * @param env
	 * @return
	 * @throws TemplateModelException
	 */
	private TemplateSequenceModel getPermsValue(Environment env)
			throws TemplateModelException {
		TemplateModel model = env.getDataModel().get(PERMISSION_VALUE);
		if (model == null) {
			return null;
		}
		if (model instanceof TemplateSequenceModel) {
			return (TemplateSequenceModel) model;
		} else {
			throw new TemplateModelException(
					"'perms' in data model not a TemplateSequenceModel");
		}

	}
}
