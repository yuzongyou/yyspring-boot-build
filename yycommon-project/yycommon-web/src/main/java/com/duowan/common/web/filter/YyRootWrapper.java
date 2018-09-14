package com.duowan.common.web.filter;

import com.duowan.common.web.pageparameter.PageParameter;
import com.duowan.common.web.pageparameter.PageParameterFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 */
public class YyRootWrapper extends HttpServletRequestWrapper {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected static ApplicationContext acx;

    protected static Map<String, PageParameter> pageParameterMap;

    protected static List<PageParameterFilter> pageParameterFilterList;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request  The request to wrap
     * @param response The response to wrap
     */
    public YyRootWrapper(HttpServletRequest request, HttpServletResponse response) {
        super(request);
        this.request = request;
        this.response = response;

        initContext();
    }

    private void initContext() {
        if (acx == null) {
            acx = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());

            initPageParameter();

            initPageParameterFilter();
        }
    }

    private void initPageParameter() {
        Map<String, PageParameter> map = acx.getBeansOfType(PageParameter.class);

        if (null != map) {
            pageParameterMap = new HashMap<>(map.size());

            for (PageParameter pageParameter : map.values()) {
                pageParameterMap.put(pageParameter.getName(), pageParameter);
            }

        } else {
            pageParameterMap = new HashMap<>(0);
        }
    }

    private void initPageParameterFilter() {
        Map<String, PageParameterFilter> filterMap = acx.getBeansOfType(PageParameterFilter.class);
        if (filterMap != null) {
            pageParameterFilterList = new ArrayList<>(filterMap.values());
        } else {
            pageParameterFilterList = new ArrayList<>();
        }
    }

    protected PageParameter lookupPageParameter(String name) {

        if (needPageParameterForCurrentRequest(request, name)) {
            if (null != pageParameterMap) {
                return pageParameterMap.get(name);
            }
        }

        return null;
    }

    /**
     * 检查当前请求是否应用默认的 PageParameter
     *
     * @param request 当前请求
     * @param name    参数名称
     * @return true 需要， false - 不需要
     */
    private boolean needPageParameterForCurrentRequest(HttpServletRequest request, String name) {

        if (null != pageParameterFilterList && !pageParameterFilterList.isEmpty()) {

            for (PageParameterFilter filter : pageParameterFilterList) {
                if (filter.support(request) && filter.filter(request, name)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String[] getParameterValues(String name) {

        PageParameter pageParameter = lookupPageParameter(name);

        if (null != pageParameter) {
            return new String[]{pageParameter.getValue(request)};
        }

        return super.getParameterValues(name);
    }

    @Override
    public String getParameter(String name) {
        String[] values = this.getParameterValues(name);
        if (values == null) {
            return null;
        } else {
            return values[0];
        }
    }
}
