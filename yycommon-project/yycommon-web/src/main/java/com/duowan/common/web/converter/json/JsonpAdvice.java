package com.duowan.common.web.converter.json;

import com.duowan.common.web.WebContext;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * 支持JSONP, ControllerAdvice 只能通过 @Order 来指定顺序  实现, PriorityOrdered, Ordered 都没有用
 *
 * @author Arvin
 */
@ControllerAdvice
@Order(value = 0)
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {

    /**
     * 指定如法存在JSONP参数，则返回JSONP结果
     */
    public JsonpAdvice() {
        super(WebContext.getJsonpCallbackVars());
    }
}
