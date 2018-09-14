package com.duowan.yyspringboot.autoconfigure.admincenter;

import com.duowan.common.admincenter.exception.NoPrivilegeException;
import com.duowan.common.admincenter.service.AdmincenterService;
import com.duowan.common.exception.CodeException;
import com.duowan.common.web.view.AbstractTextView;
import com.duowan.udb.security.PrivilegeInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 16:03
 */
public class AdmincenterPrivilegeInterceptor implements PrivilegeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AdmincenterPrivilegeInterceptor.class);

    private AdmincenterService admincenterService;

    private String forbiddenUrl;

    public AdmincenterPrivilegeInterceptor(AdmincenterService admincenterService, String forbiddenUrl) {
        this.admincenterService = admincenterService;
        this.forbiddenUrl = forbiddenUrl;
    }

    @Override
    public boolean checkPrivilege(String username, HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (admincenterService.hasPrivilege(username, request, response, handler)) {
            return true;
        }

        boolean ajaxView = handler == null || !(handler instanceof HandlerMethod);
        if (!ajaxView) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            ajaxView = null != handlerMethod.getMethodAnnotation(ResponseBody.class);

            if (!ajaxView) {
                ajaxView = AbstractTextView.class.isAssignableFrom(handlerMethod.getMethod().getReturnType());
            }
        }

        if (StringUtils.isBlank(forbiddenUrl)) {
            ajaxView = true;
        }

        if (ajaxView) {
            String message = "用户[" + username + "] 没有权限操作[" + request.getRequestURI() + "]";
            logger.warn(message);
            throw new NoPrivilegeException(message);
        }

        // 返回403页面
        try {
            response.sendRedirect(this.forbiddenUrl);
        } catch (Exception e) {
            throw new CodeException(e);
        }
        return false;
    }
}
