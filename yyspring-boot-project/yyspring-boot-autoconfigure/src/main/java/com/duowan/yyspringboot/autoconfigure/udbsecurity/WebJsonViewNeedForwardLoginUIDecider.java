package com.duowan.yyspringboot.autoconfigure.udbsecurity;

import com.duowan.common.web.response.JsonResponse;
import com.duowan.common.web.view.AbstractTextView;
import com.duowan.common.web.view.AjaxView;
import com.duowan.udb.security.NeedForwardLoginUIDecider;
import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/27 16:45
 */
public class WebJsonViewNeedForwardLoginUIDecider implements NeedForwardLoginUIDecider {

    private static final Class<?>[] AJAX_VIEW_TYPES = new Class[]{
            AjaxView.class,
            JsonResponse.class,
            AbstractTextView.class
    };

    @Override
    public Boolean need(HttpServletRequest request, Object handler) {

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;

            Class<?> returnType = hm.getMethod().getReturnType();
            if (null != returnType) {
                for (Class<?> viewType : AJAX_VIEW_TYPES) {
                    if (viewType.equals(returnType) || viewType.isAssignableFrom(returnType)) {
                        return false;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
