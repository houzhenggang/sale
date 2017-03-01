package com.hshc.sale.web.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hshc.mdm.entity.mst.MstRegion;
import com.hshc.mdm.service.api.mst.MstRegionService;

public class MstInitListener extends ContextLoaderListener implements ServletContextListener {

    MstRegionService<MstRegion, Integer> mstRegionService;

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        ApplicationContext con = WebApplicationContextUtils.getWebApplicationContext(context);
        mstRegionService = (MstRegionService<MstRegion, Integer>) con.getBean("mstRegionService");
        List<MstRegion> list = this.getRegionTree(mstRegionService.queryAllList());
        context.setAttribute("region", list);
        Map<String, List<MstRegion>> cityMap = new HashMap<>();
        Map<String, List<MstRegion>> countyMap = new HashMap<>();
        for (MstRegion region : list) {
            List<MstRegion> cityList = region.getChildRegionList();
            if (cityList != null && cityList.size() > 0) {
                cityMap.put(region.getCode(), cityList);
                for (MstRegion city : cityList) {
                    List<MstRegion> countyList = city.getChildRegionList();
                    if (countyList != null && countyList.size() > 0) {
                        countyMap.put(city.getCode(), countyList);
                    }
                }
            }
        }
        context.setAttribute("cityMap", cityMap);
        context.setAttribute("countyMap", countyMap);

    }

    private List<MstRegion> getRegionTree(List<MstRegion> regions) {
        List<MstRegion> result = new ArrayList<MstRegion>();
        // 循环第一次把所有省份放进去
        for (MstRegion region : regions) {
            if (region.getParentId() == 0) {
                // 获得第一级省份，如：北京
                List<MstRegion> citys = new ArrayList<MstRegion>();
                for (MstRegion city : regions) {
                    if (region.getId().equals(city.getParentId())) {
                        // 获得第二级市
                        List<MstRegion> countys = new ArrayList<MstRegion>();
                        for (MstRegion county : regions) {
                            if (city.getId().equals(county.getParentId())) {
                                countys.add(county);
                            }
                        }
                        city.setChildRegionList(countys);
                        citys.add(city);
                    }
                }
                region.setChildRegionList(citys);
                result.add(region);
            }
        }

        return result;
    }

}
