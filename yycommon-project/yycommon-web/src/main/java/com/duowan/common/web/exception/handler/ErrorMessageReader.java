package com.duowan.common.web.exception.handler;

import org.springframework.core.Ordered;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/19 10:36
 */
public interface ErrorMessageReader extends Ordered {

    /**
     * 如果能够处理该异常则返回非 null
     *
     * @param ex 异常信息
     * @return 返回异常信息
     */
    ErrorMessage readErrorMessage(Exception ex);
}
