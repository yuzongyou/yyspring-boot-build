package com.duowan.common.web.pageparameter;

import com.duowan.common.utils.AssertUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author Arvin
 */
public class UrlPatternPageParameterFilter implements PageParameterFilter {

    /**
     * url 匹配规则
     */
    protected final String urlPattern;

    /**
     * 是否过滤全部
     */
    protected boolean filterAll = false;

    protected Set<String> filterNames;

    public UrlPatternPageParameterFilter(String urlPattern) {
        this.urlPattern = urlPattern;
        AssertUtil.assertNotBlank(this.urlPattern, "PageParameterFilter's urlPattern should not be blank");
    }

    public boolean isFilterAll() {
        return filterAll;
    }

    public void setFilterAll(boolean filterAll) {
        this.filterAll = filterAll;
    }

    public Set<String> getFilterNames() {
        return filterNames;
    }

    public void setFilterNames(Set<String> filterNames) {
        this.filterNames = filterNames;
    }

    @Override
    public boolean support(HttpServletRequest request) {

        String uri = request.getRequestURI();

        if (uri.matches(urlPattern)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean filter(HttpServletRequest request, String name) {

        return filterAll || null != filterNames && filterNames.contains(name);

    }

}
