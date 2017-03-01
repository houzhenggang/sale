package com.hshc.sale.web.controller.frame;

import com.hshc.sale.util.BeanUtil;
import com.hshc.sale.util.PropertiesUtil;
import com.hshc.sale.util.UserSessionProvider;
import com.hshc.common.entity.BaseEntity;
import com.hshc.common.web.controller.BaseCRUDAction;
import com.hshc.upms.entity.security.User;
import com.hshc.upms.vo.user.DataPermissionVo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ServletRequestUtils
// WebRequest
/**
 * Controller控制类父类
 * 提供页面属性获取等方法
 * @author 弋攀 yipan@huashenghaoche.com
 * 2016年12月15日 14:07:31
 */
public abstract class WebAction<T extends BaseEntity, ID extends Serializable> extends BaseCRUDAction<T, ID> {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected Model model;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response, Model model) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        this.model = model;
        request.setAttribute("contextPath", request.getContextPath());
        // 图片下载预览路径
        request.setAttribute("access_root_path", PropertiesUtil.getUPLOAD("access_root_path"));
    }

    /**
     * 取得页面属性，不带前后缀
     * @param aClass 类
     * @return Object
     * 弋攀 E-mail：panyi@huashenghaoche.com
     * 2016年12月15日 14:07:31
     */
    public Object getBean(Class<? extends BaseEntity> aClass) {
        return BeanUtil.getBean(aClass, request);
    }

    /**
     * 取得页面属性List，不带前后缀
     * @param aClass 类
     * @return Object
     * 弋攀 E-mail：panyi@huashenghaoche.com
     * 2016年12月15日 14:07:31
     */
    public List<?> getBeanList(Class<? extends BaseEntity> aClass) {
        return BeanUtil.getBeanList(aClass, request, true);
    }

    /**
     * 取得页面属性，带前后缀
     * @param aClass 类
     * @param prefix 前缀
     * @param postfix 后缀
     * @return Object
     * 弋攀 E-mail：panyi@huashenghaoche.com
     * 2016年12月15日 14:07:31
     */
    public Object getBeanByFix(Class<? extends BaseEntity> aClass, String prefix, String postfix) {
        return BeanUtil.getBean(aClass, prefix, postfix, request);
    }

    /**
     * 取得页面属性List，带前后缀
     * @param aClass 类
     * @param prefix 前缀
     * @param postfix 后缀
     * @return List
     * 弋攀 E-mail：panyi@huashenghaoche.com
     * 2016年12月15日 14:07:31
     */
    public List<?> getBeanListByFix(Class<? extends BaseEntity> aClass, String prefix, String postfix) {
        return BeanUtil.getBeanList(aClass, prefix, postfix, request);
    }

    /**
     * 得到用户对象
     * @return User
     * @throws Exception
     * 弋攀 E-mail：panyi@huashenghaoche.com
     * 2016年12月15日 14:07:31
     */
    public User getUser() throws Exception {
        User user = UserSessionProvider.getUserSerssion(request);
        // 说明用户失效 则抛出异常
        if (user == null) {
            throw new Exception("无法获取当前登录用户信息");
        }
        return user;
    }

    /**
     * 为数据添加权限，insertBy为创建人，parameList为门店集合
     * @param baseEntity 传入参数
     * @return 返回参数
     * 弋攀 E-mail：panyi@huashenghaoche.com
     * 2016年12月27日 18:15:48
     */
    public BaseEntity setPermission(BaseEntity baseEntity) {
        try {
            DataPermissionVo vo = getUser().getDataPerm();
            //如果数据权限为空，不需要添加参数，查询所有机构数据
            if (vo != null && !getUser().getName().equals("admin")) {
                //如果IsSelf为true，只查询自己创建的数据，将insertBy设置为userId
                if (vo.getIsSelf() != null && vo.getIsSelf()) {
                    baseEntity.setInsertBy(getUser().getId().toString());
                } else {
                    //如果getOrgCodeList.size==0，表示没有任何数据权限，直接返回null;
                    if (vo.getOrgCodeList() != null && vo.getOrgCodeList().size() > 0) {
                        //如果为false，将数据权限机构列表付给ParameList
                        baseEntity.setParameList(vo.getOrgCodeList());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseEntity;
    }

    /**
     * 得到list列表当前页数
     * @return 当前页
     * 弋攀 E-mail：panyi@huashenghaoche.com
     * 2016年12月28日 16:53:34
     */
    public int getCurrenPage() {
        int currenPage = 1;
        if (request.getParameter("currenPage") != null && !"".equals(request.getParameter("currenPage"))) {
            currenPage = Integer.parseInt(request.getParameter("currenPage"));
        }
        return currenPage;
    }

    /**
     * 响应Ajax请求， 返回的json串的格式组装
     *
     * @param status 状态，成功或者失败
     * @param info   额外信息（可用于弹框提示的信息等）
     * @param object 数据对象
     * @return 上述参数的组合成的一个Map
     */
    private Map<String, Object> response(String status, String info, Object object) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("result", status);
        resp.put("info", info);
        resp.put("object", object);
        return resp;
    }

    /**
     * 得到所有的paramter属性
     * @return map
     */
    @SuppressWarnings("rawtypes")
    public Map getParameters() {
        Map<String, Object> m = new HashMap<>();
        if (request == null) {
            return m;
        }
        Enumeration<?> en = request.getParameterNames();
        while (en.hasMoreElements()) {
            Object enN = en.nextElement();
            String para = request.getParameter(enN.toString()).trim();
            m.put(enN.toString(), ("undefined".equals(para)) ? "" : para.trim());
        }
        return m;
    }

    /**
     * 返回操作成功的参数组合
     *
     * @param info 额外信息（可用于弹框提示的信息等）
     * @param object 数据对象
     * @return 成功的参数组合
     */
    protected Map<String, Object> success(String info, Object object) {
        return response("success", info, object);
    }

    /**
     * 返回操作失败的参数组合
     *
     * @param info 额外信息（可用于弹框提示的信息等）
     * @param object 数据对象
     * @return 失败的操作组合
     */
    protected Map<String, Object> fail(String info, Object object) {
        return response("fail", info, object);
    }
}
