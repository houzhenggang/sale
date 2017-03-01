/**
 * 
 */
package com.hshc.sale.entity.wh;

import com.hshc.common.entity.BaseEntity;

import java.util.List;

/**
 * 待办任务
 *
 * @author 弋攀 E-mail：panyi@huashenghaoche.com
 * @date 2017年2月7日 18:34:58
 */
public class WhBacklog extends BaseEntity {
	
	private static final long serialVersionUID = -3680460945928951509L;
	/**
	 * 主键ID
	 */
	private Integer id;
	/**
	 * 待办名称
	 */
	private String name;
	/**
	 * 上级ID
	 */
	private Integer parentId;
	/**
	 * 查询sql
	 */
	private String sqlStr;
	
	/**
	 * 跳转地址
	 */
	private String url;
	
	/**
	 * 排序
	 */
	private Integer rank;
	
	/**
	 * 级次
	 */
	private Integer level;
	
	/**
	 * 权限
	 */
	private String permission;
	
	/**
	 * 下级列表
	 */
	private List<WhBacklog> backlogList;
	
	/**
	 * 待办数量
	 */
	private Integer backLogCount;
	/**
	 * 需要排序字段 暂时只能对单字段排序，以后可以改为对多列排序，此字段为数据库中的字段，非对象.
	 */
	private String orderBy = "rank";
	
	private Long sysId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getSqlStr() {
		return sqlStr;
	}

	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<WhBacklog> getBacklogList() {
		return backlogList;
	}

	public void setBacklogList(List<WhBacklog> backlogList) {
		this.backlogList = backlogList;
	}

	public Integer getBackLogCount() {
		return backLogCount;
	}

	public void setBackLogCount(Integer backLogCount) {
		this.backLogCount = backLogCount;
	}

	public Long getSysId() {
		return sysId;
	}

	public void setSysId(Long sysId) {
		this.sysId = sysId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
}
