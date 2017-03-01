package com.hshc.sale.service.impl.wh;

import com.hshc.common.dao.BaseMapper;
import com.hshc.common.entity.BaseEntity;
import com.hshc.common.service.impl.BaseServiceImpl;
import com.hshc.sale.dao.wh.WhBacklogMapper;
import com.hshc.sale.entity.wh.WhBacklog;
import com.hshc.sale.service.api.wh.WhBacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 待办任务
 *
 * @author 弋攀 E-mail：panyi@huashenghaoche.com
 * @date 2017年2月7日 18:34:58
 */
@Service
public class WhBacklogServiceImpl<T extends BaseEntity, ID extends Serializable> extends BaseServiceImpl<WhBacklog, Integer>
		implements WhBacklogService<WhBacklog, Integer> {
	@Autowired
	WhBacklogMapper<WhBacklog, Integer> whBacklogMapper;
	
	@Override
	public BaseMapper<WhBacklog, Integer> getMapper() {
		return whBacklogMapper;
	}

}
