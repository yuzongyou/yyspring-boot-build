package com.duowan.common.web.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arvin
 */
public abstract class AbstractExceptionViewResolver implements ExceptionViewResolver {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected List<ErrorMessageReader> errorMessageReaderList;

    public AbstractExceptionViewResolver(List<ErrorMessageReader> errorMessageReaderList) {
        this.errorMessageReaderList = errorMessageReaderList;
        if (this.errorMessageReaderList == null || this.errorMessageReaderList.isEmpty()) {
            this.errorMessageReaderList = new ArrayList<>(2);
            this.errorMessageReaderList.add(new CodeExceptionErrorMessageReader());
            this.errorMessageReaderList.add(new ValidationExceptionErrorMessageReader());
        }

        sortErrorMessageReaderList(this.errorMessageReaderList);
    }

    private void sortErrorMessageReaderList(List<ErrorMessageReader> errorMessageReaderList) {
        errorMessageReaderList.sort((o1, o2) -> {
            int ret = o1.getOrder() - o2.getOrder();
            return Integer.compare(ret, 0);
        });
    }

    protected boolean logException = true;

    public boolean isLogException() {
        return logException;
    }

    public void setLogException(boolean logException) {
        this.logException = logException;
    }

    protected ErrorMessage getErrorMessage(Exception ex) {
        ErrorMessage errorMessage = null;
        if (ex != null) {
            for (ErrorMessageReader reader : this.errorMessageReaderList) {
                errorMessage = reader.readErrorMessage(ex);
                if (errorMessage.isCanHandle()) {
                    break;
                }
            }
        }

        if (errorMessage == null) {
            errorMessage = new ErrorMessage(true, 500, "服务器繁忙，请稍候再试", ex);
        }

        doExceptionErrorMessageLog(errorMessage);

        return errorMessage;
    }

    private void doExceptionErrorMessageLog(ErrorMessage errorMessage) {

        if (errorMessage.isCanHandle()) {
            if (logException) {
                logger.warn(errorMessage.getMessage(), errorMessage.getException());
            } else {
                logger.warn(errorMessage.getMessage());
            }
        } else {
            // 如果是不可处理的异常则使用 error 进行打印
            logger.error(errorMessage.getMessage(), errorMessage.getException());
        }

    }
}
