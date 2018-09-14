package com.duowan.common.web.converter.json;

import com.duowan.common.web.view.ViewUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义结果返回，返回为 javascript 语句
 * ControllerAdvice 只能通过 @Order 来指定顺序， 实现, PriorityOrdered, Ordered 都没有用
 * 详情请看源码：
 * {@link ControllerAdviceBean#findAnnotatedBeans(org.springframework.context.ApplicationContext)}
 * 内部new 了一个Bean：
 * new ControllerAdviceBean(name, applicationContext)
 * 最后通过 {@link ControllerAdviceBean#initOrderFromBeanType(Class)}
 * 来确定顺序， 具体代码实现：
 * <pre>
 * public static Integer getOrder(Class&lt;?&gt; type, Integer defaultOrder) {
 * Order order = AnnotationUtils.findAnnotation(type, Order.class);
 * if (order != null) {
 * return order.value();
 * }
 * Integer priorityOrder = getPriority(type);
 * if (priorityOrder != null) {
 * return priorityOrder;
 * }
 * return defaultOrder;
 * }
 * Order 注解的value越小越排在前面
 * </pre>
 *
 * @author Arvin
 */
@ControllerAdvice
@Order(value = 2)
public class JsonJavascriptAdvice implements ResponseBodyAdvice<Object> {

    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public final Object beforeBodyWrite(Object body, MethodParameter returnType,
                                        MediaType contentType, Class<? extends HttpMessageConverter<?>> converterType,
                                        ServerHttpRequest request, ServerHttpResponse response) {

        MappingJacksonJavascriptValue container = null;
        MappingJacksonValue mappingJacksonValue = body instanceof MappingJacksonValue ? (MappingJacksonValue) body : null;

        if (null == mappingJacksonValue || StringUtils.isEmpty(mappingJacksonValue.getJsonpFunction())) {
            container = getOrCreateContainer(body);
        }
        if (container != null) {
            beforeBodyWriteInternal(container, contentType, returnType, request, response);
            return container;
        }
        return body;
    }

    protected MappingJacksonJavascriptValue getOrCreateContainer(Object body) {
        return (body instanceof MappingJacksonJavascriptValue ? (MappingJacksonJavascriptValue) body : new MappingJacksonJavascriptValue(body));
    }

    protected void beforeBodyWriteInternal(MappingJacksonJavascriptValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

        String javascriptVar = ViewUtil.lookupJavascriptVar(servletRequest);

        if (StringUtils.isNotBlank(javascriptVar)) {
            MediaType contentTypeToUse = getContentType(contentType, request, response);
            response.getHeaders().setContentType(contentTypeToUse);
            bodyContainer.setJavascriptVar(javascriptVar);
        }
    }

    protected MediaType getContentType(MediaType contentType, ServerHttpRequest request, ServerHttpResponse response) {
        return new MediaType("application", "javascript");
    }
}
